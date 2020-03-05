package com.atrin.map.vertx;

import com.atrin.lib.map.database.DataBase;
import com.atrin.map.configuration.Configuration;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.http.*;
import io.vertx.core.AbstractVerticle;

import java.util.Scanner;

import com.atrin.lib.map.coord.BBox;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class Main extends AbstractVerticle {

	private Scanner index_html;
	private String html_context;

	private Configuration config = Configuration.config;
	private DataBase db = new DataBase();

	@Override
	public void start() throws Exception {

		System.out.println(config.get("server.host")+":"+config.get("server.port"));
		
		HttpServer server =	vertx.createHttpServer();

		Router router = Router.router(vertx);

		new DeploymentOptions().setWorker(true);

//		router.route().handler(BodyHandler.create());
//		router.route().handler(routingContext -> routingContext.next() );
//		Route main_page_router = router.get("/");


		// TODO Route Asset Files
		Route assets_router = router.route().path(config.get("web.url")).method(HttpMethod.GET).method(HttpMethod.POST).handler(StaticHandler.create(config.get("web.dir")));

		// TODO Route Tangram Style Files
		Route tangram_router = router.route().path(config.get("tangram.url")).method(HttpMethod.GET).method(HttpMethod.POST).handler(StaticHandler.create(config.get("tangram.styles.dir")));

		// TODO Route Prototype GeoJson Files
		Route prototype_geojson_data_route = router.route().path(config.get("data.prototype.geojson.url")).method(HttpMethod.GET).method(HttpMethod.POST).handler(StaticHandler.create(config.get("data.prototype.geojson.dir")));

		// TODO Route Prototype TopoJson Files
		Route prototype_topojson_data_route = router.route().path(config.get("data.prototype.topojson.url")).method(HttpMethod.GET).method(HttpMethod.POST).handler(StaticHandler.create(config.get("data.prototype.topojson.dir")));


		// TODO Route Vector Tiles
		Route vector_tile_route =  router.route().path(config.get("vtile.url")).method(HttpMethod.GET).method(HttpMethod.POST).handler(this::xyzVTile);

		// TODO Route Lat,Lon BBox Vector Tiles
		Route bbox_vector_tile_route =  router.route().path(config.get("bboxvtile.url")).method(HttpMethod.GET).method(HttpMethod.POST).handler(this::bboxVTile);

		// TODO Route Tehran Raod Vector Tiles
		Route tehran_roads_test_route =  router.route().path(config.get("tehranroads.url")).method(HttpMethod.GET).method(HttpMethod.POST).handler(this::tehranroads);

		// TODO Route Simplified Land Polyogons Vector Tiles
		Route geojson_test_route =  router.route().path(config.get("simplified_land_polygons.url")).method(HttpMethod.GET).method(HttpMethod.POST).handler(this::simplifiedLandPolygons);

		server.requestHandler(router::accept).listen(config.getAsInteger("server.port"), config.get("server.host"));
	}

	// TODO Response LatLon BBox Vector Tiles
	private void bboxVTile(RoutingContext routingContext){
		HttpServerResponse response = routingContext.response();
		HttpServerRequest request = routingContext.request();

		String layers = request.getParam("LAYERS");
		double north = Double.parseDouble(request.getParam("NORTH"));
		double east = Double.parseDouble(request.getParam("EAST"));
		double west = Double.parseDouble(request.getParam("WEST"));
		double south = Double.parseDouble(request.getParam("SOUTH"));

		String format = request.getParam("FORMAT");

		BBox bbox = new BBox(north, east, west, south);

		System.out.println(bbox);

		response.setChunked(true);

		response.end(bbox.toString() + "\nFormat:" + format + "\nLayers: " + layers);
	}

	// TODO Response XYZ Vector Tiles
	private void xyzVTile (RoutingContext routingContext){
		HttpServerResponse response = routingContext.response();
		HttpServerRequest  request  = routingContext.request();

		String layers = request.getParam("LAYERS");
		int zoom = Integer.parseInt(request.getParam("Z"));
		int y = Integer.parseInt(request.getParam("Y"));
		int x = Integer.parseInt(request.getParam("X"));


		String format = request.getParam("FORMAT");

		BBox bbox = new BBox(zoom,x,y);

		response.setChunked(true);

		response.end(bbox.toString()+"\nFormat:"+format+"\nLayers: "+layers);
	}

	// TODO Response Tehran Raods By XYZ Vector Tiles
	private void tehranroads (RoutingContext routingContext){
		HttpServerResponse response = routingContext.response();
		HttpServerRequest  request  = routingContext.request();

		int zoom = Integer.parseInt(request.getParam("Z"));
		int y = Integer.parseInt(request.getParam("Y"));
		int x = Integer.parseInt(request.getParam("X"));

		BBox bbox = new BBox(zoom,x,y);

		String res = db.getTehranRoads(bbox);

		response.setChunked(true);

		response.putHeader("Content-Type", "application/json");

		response.end(res);
	}

	// TODO Response Simplified Land Polygons By XYZ Vector Tiles
	private void simplifiedLandPolygons (RoutingContext routingContext){
		HttpServerResponse response = routingContext.response();
		HttpServerRequest  request  = routingContext.request();

		int zoom = Integer.parseInt(request.getParam("Z"));
		int y = Integer.parseInt(request.getParam("Y"));
		int x = Integer.parseInt(request.getParam("X"));

		BBox bbox = new BBox(zoom,x,y);

		String res = db.getSimplifiedLandPolygons(bbox);

		response.setChunked(true);

		response.putHeader("Content-Type","application/json");

		response.end(res);
	}

}