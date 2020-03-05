package com.atrin.lib.map.coord;

public class BBox {

	public static final int MAX_ZOOM = 20;

	// TODO Create and Modify BBox

	private double north 	;
	private double east  	;
	private double west  	;
	private double south 	;

	private int    zoom		;
	private int    x 		;
	private int    y 		;


	/*
     * North-East-West-South
     * Lat0 -Lon1-Lon0-Lat1
     *
     * North-West-South-East
     * Lat0 -Lon0-Lat1 -Lon1
	 */

	public BBox(double north,double east,double west, double south){
		this.north = north 				;
		this.east  = east  				;
		this.west  = west  				;
		this.south = south 				;

		this.zoom  = -1 				; // Undifined
	}

	public BBox(double north,double east,double west, double south,int zoom){
		this.north = north 				;
		this.east  = east  				;
		this.west  = west  				;
		this.south = south 				;

		this.zoom  = zoom				; 
	}

	public BBox(int zoom,int x,int y){
		this.zoom  = zoom 				;
		this.x     = x     				;
		this.y     = y     				;

		this.north = getLat(y+1 , zoom)	; // Y0 - Lat0
		this.east  = getLon(x+1 , zoom)	; // X1 - Lon1
		this.west  = getLon(x, zoom)	; // X0 - Lon0
		this.south = getLat(y, zoom)	; // Y1 - Lat1
	}

	public void createXYZ(int zoom){
		this.zoom = zoom;
		this.x = this.getX(this.west, this.zoom);
		this.y = this.getY(this.north, this.zoom);
	}

	public static BBox createXYZTile(int zoom,Double north,Double west){
		int x = getX(west, zoom);
		int y = getY(north, zoom);

		return new BBox(zoom,x,y);
	}


	public static double getLon(int x,int z){
		if(z < 0)
			return -1;
		return x/Math.pow(2.00,z) * 360.0 - 180.0;
	}
	public static double getLat(int y,int z){
		if(z < 0)
			return -1;
		double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
		return Math.toDegrees(Math.atan(Math.sinh(n)));
	}


	public static int getX (double lon, int zoom){
		if(zoom < 0)
			return -1;
		int  x;
		return x = (x = (int) Math.floor( (lon+180)/360 * (1<<zoom)) ) < 0 ?  x=0 : (x >= (1<<zoom)) ? x=(1<<zoom)-1 : x;
	}

	public static int getY (double lat, int zoom){
		if(zoom < 0)
			return -1;
		int y;
		return y = (y = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ) < 0 ?
				  y=0 : (y >= (1<<zoom)) ? y=(1<<zoom)-1 : y;

	}

	public double getNorth () {return north;}
	public double getEast () {return east;}
	public double getWest () {return west;}
	public double getSouth () {return south;}

	public int	  getZoom () {return zoom;}
	public int    getX () {return x;}
	public int    getY () {return y;}

	public Double getSimplifyingLevelForTehranRoads(){
		if (this.zoom == -1)
			return 0.0;
		else
			switch(this.zoom){
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
					return 0.75;
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
					return 0.5;
				case 11:
				case 12:
				case 13:
//					return 0.2;
				case 14:
				case 15:
//					return 0.1;
				case 16:
				case 17:
				case 18:
//					return 0.25;
				case 19:
				case 20:
					return 0.0;
				default:
					return 1.0;
			}
//		return 0.0;
	}

	public String getBBoxDBFormat(){
		return ""+this.west+","+this.south+","+this.east+","+this.north+",4326";
	}

	@Override
	public String toString(){
		String ret ;
		ret = String.format("BBox:\n\tNorth=%s\n\tEast=%s\n\tWest=%s\n\tSouth=%s",this.north,this.east,this.west,this.south) + (this.zoom > 0 ? String.format("\nXYZ:\n\tZ=%s\n\tX=%s\n\tY=%s",this.zoom,this.x,this.y) : "");
		return ret;
	}
}