package com.atrin.lib.map.database;


import java.sql.*;
import com.atrin.lib.map.coord.*;
import com.atrin.lib.map.queries.*;
import com.atrin.map.configuration.Configuration;
import org.postgresql.util.PSQLException;

public class DataBase {
	private String db_host;
	private int    db_port;
	private String db_username;
	private String db_password;
	private String db_name;

	private Connection connection;

	public static final String 	DEFAULT_DATABASE_HOST 		=	Configuration.config.get			("database.default.host");
	public static final int	 	DEFAULT_DATABASE_PORT 		=	Configuration.config.getAsInteger	("database.default.port");
	public static final String 	DEFAULT_DATABASE_USERNAME 	=	Configuration.config.get			("database.default.username");
	public static final String 	DEFAULT_DATABASE_PASSWORD 	=	Configuration.config.get			("database.default.password");
	public static final String 	DEFAULT_DATABASE_NAME 		=	Configuration.config.get			("database.default.name");


	public DataBase (String db_host,int db_port,String db_username,String db_password,String db_name){
		this.db_host = db_host;
		this.db_port = db_port;
		this.db_username = db_username;
		this.db_password = db_password;
		this.db_name = db_name;

		this.createConnection();
	}

	public DataBase (){
		this.db_host = DEFAULT_DATABASE_HOST;
		this.db_port = DEFAULT_DATABASE_PORT;
		this.db_username = DEFAULT_DATABASE_USERNAME;
		this.db_password = DEFAULT_DATABASE_PASSWORD;
		this.db_name = DEFAULT_DATABASE_NAME;

		this.createConnection();
	}

	private void createConnection(){
		try{
			Class.forName("org.postgresql.Driver");
			this.connection = DriverManager.getConnection(String.format("jdbc:postgresql://%s:%d/%s",db_host,db_port,db_name),db_username,db_password);
			System.out.println("Connection Created.");
			System.out.println("Waiting to Queries...");
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void exit(){
		try{
			this.connection.close();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}

	private Statement createStatement() throws Exception{
		return connection.createStatement();
	}

	public void getVectorTileData(VectorTileQuery bbox){
		Statement stmt = null;
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//{"type":"Feature","properties":{"name":%s,"tunnel":%d,"bridge":%d,"oneway":%d,"class":%s},"geometry":%s}

	public String getTehranRoads(BBox bbox){
		Statement statement = null;
		StringBuilder features = new StringBuilder();
		StringBuilder result = new StringBuilder();
		try{
			statement = createStatement();
			String command = String.format(Configuration.config.get("tehranroads.database.query"),bbox.getSimplifyingLevelForTehranRoads(), bbox.getBBoxDBFormat());

			ResultSet results = statement.executeQuery(command);

			int counter = 0;

			results.next();
			while(true){
				try {
					String geojson = results.getString(Configuration.config.get("tehranroads.database.fields.geojosn"));
					String name = (results.getString(Configuration.config.get("tehranroads.database.fields.name"))==null ? "null" :"\"" + results.getString(Configuration.config.get("tehranroads.database.fields.name")) + "\"");
					Integer oneway = results.getInt(Configuration.config.get("tehranroads.database.fields.oneway"));
					Integer tunnel = results.getInt(Configuration.config.get("tehranroads.database.fields.tunnel"));
					Integer bridge = results.getInt(Configuration.config.get("tehranroads.database.fields.bridge"));
					String _class = (results.getString(Configuration.config.get("tehranroads.database.fields._class"))==null ? "null" : "\"" + results.getString(Configuration.config.get("tehranroads.database.fields._class")) + "\"");

//					System.out.println(++counter);

					features.append(String.format(Configuration.config.get("tehranroads.geojson.format.feature"), name, tunnel, bridge, oneway, _class, geojson));
				}catch(PSQLException psqle){

				}

				if (results.next()) {
					features.append( ',');
				} else {
					break;
				}

			}

			result.append(String.format(Configuration.config.get("tehranroads.geojson.format.whole"),features.toString()));

		}catch(Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public String getSimplifiedLandPolygons(BBox bbox){
		Statement statement = null;
		StringBuilder features = new StringBuilder();
		StringBuilder result = new StringBuilder();
		try{
			statement = createStatement();
			String command = String.format(Configuration.config.get("simplified_land_polygons.database.query"), bbox.getBBoxDBFormat());

			ResultSet results = statement.executeQuery(command);

			int counter = 0;

			results.next();
			while(true){
				try {
					String geojson = results.getString(Configuration.config.get("simplified_land_polygons.database.fields.geojosn"));

//					System.out.println(++counter);

					features.append(String.format(Configuration.config.get("simplified_land_polygons.geojson.format.feature"), geojson));
				}catch(PSQLException psqle){

				}

				if (results.next()) {
					features.append( ',');
				} else {
					break;
				}
			}

			result.append(String.format(Configuration.config.get("simplified_land_polygons.geojson.format.whole"),features.toString()));

		}catch(Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public void searchDB(SearchQuery query){
		Statement stmt = null;
		try{

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}