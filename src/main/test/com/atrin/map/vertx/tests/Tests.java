package com.atrin.map.vertx.tests;

import com.atrin.lib.map.coord.BBox;
import com.atrin.map.configuration.Configuration;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.*;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

@RunWith(VertxUnitRunner.class)
public class Tests {

//    public double north = 35.7009577239299, west = 51.059923, south = 35.6789243445849, east = 51.0954462034338;
    public double north=35.767057861965,west=51.3796318309041,south=35.74502448262, east=51.4151550343379;
    public int zoomLevel = 16, x = 42063, y = 25802;
    private Vertx vertx;

    private Configuration config = Configuration.config;

    @Before
    public void setUp(TestContext tcontext) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(com.atrin.map.vertx.Main.class.getName(),
                tcontext.asyncAssertSuccess());
    }


    @After
    public void TearDown(TestContext tcontext) {
        vertx.close(tcontext.asyncAssertSuccess());
    }


    // TODO Test BBox Conversion Library
    @Test
    public void TestBBoxLatLonToXYConversion(TestContext tcontext) {
        System.out.println("\n-------------------------------------\n");
        BBox bbox_test01 = new BBox(north, east, west, south);
        bbox_test01.createXYZ(zoomLevel);
        System.out.println(bbox_test01);
    }

    // TODO Test Vector Tile By XYZ BBox Format
    @Test
    public void TestVectorTileXYZBBox(TestContext tcontext) {
        System.out.println("\n-------------------------------------\n");
        final Async async = tcontext.async();

        System.out.println("XYZBBOX started...");

        vertx.createHttpClient().getNow(config.getAsInteger("server.port"), config.get("server.host"), String.format(config.get("vtile.formatedstring"), config.get("layers.defaults.buildings") + "," + config.get("layers.defaults.water"), zoomLevel, x, y, "mvt"),
                response -> {
                    response.handler(body -> {
                        System.out.println("XYZBBOX : \n" + body.toString());
                        tcontext.assertTrue(true);
                        async.complete();
                    });
                }
        );
    }

    // TODO Test Vector Tile By Lat0,Lon0,Lat1,Lon1 Format
    @Test
    public void TestVectorTileBBox(TestContext tcontext) {
        System.out.println("\n-------------------------------------\n");
        final Async async = tcontext.async();

        vertx.createHttpClient().getNow(config.getAsInteger("server.port"),config.get("server.host"), String.format(config.get("bboxvtile.formatedstring"), config.get("layers.defaults.all"), north, east, west, south, "mvt"),
                response -> {
                    response.handler(body -> {
                        System.out.println("BBoxTEST : \n" + body.toString());
                        tcontext.assertTrue(true);
                        async.complete();
                    });
                }
        );
    }

    // TODO Test JavaScript Assets
    @Test
    public void TestJavaScriptAssets(TestContext tcontext) {
        System.out.println("\n-------------------------------------\n");
        final Async async = tcontext.async();

        System.out.println("JavaScript Assets");

        vertx.createHttpClient().getNow(config.getAsInteger("server.port"),config.get("server.host"), config.get("web.dir")+"js/sample.js",
                response -> {
                    response.handler(body -> {
                        System.out.println(body.toString());
//                        tcontext.assertTrue(body.toString().contains("alert"));
                        async.complete();
                    });
                }
        );
    }

    // TODO Test Css Assets
    @Test
    public void TestCSSAssets(TestContext tcontext) {
        System.out.println("\n-------------------------------------\n");
        final Async async = tcontext.async();

        System.out.println("Css Assets");

        vertx.createHttpClient().getNow(config.getAsInteger("server.port"),config.get("server.host"), config.get("web.dir")+"css/sample.css",
                response -> {
                    response.handler(body -> {
                        System.out.println("Css Assets Test : \n" + body.toString());
                        tcontext.assertTrue(true);
                        async.complete();
                    });
                }
        );
    }

    // TODO Test Html Assets
    @Test
    public void TestHTMLAssets(TestContext tcontext) {
        System.out.println("\n-------------------------------------\n");
        final Async async = tcontext.async();

        System.out.println("HTML Assets");

        vertx.createHttpClient().getNow(config.getAsInteger("server.port"),config.get("server.host"), config.get("web.dir")+"/html/index.html",
                response -> {
                    response.handler(body -> {
                        System.out.println(body.toString());
                        tcontext.assertTrue(true);
                        async.complete();
                    });
                }
        );
    }

    // TODO Test Tangram style.json Asset
    @Test
    public void TestTangramStyleAsset(TestContext tcontext) {
        System.out.println("\n-------------------------------------\n");
        final Async async = tcontext.async();

        System.out.println("Tangram style.json Asset");

        vertx.createHttpClient().getNow(config.getAsInteger("server.port"),config.get("server.host"), config.get("tangram.styles.metadata.json.url"),
                response -> {
                    response.handler(body -> {
                        System.out.println(body.toString());
//                        tcontext.assertTrue(body.toString().contains("alert"));
                        async.complete();
                    });
                }
        );
    }

    @Test
    public void CalculateTehranBBoxTiles(TestContext tcontext){
        final Async async = tcontext.async();
        try {
            Double north = 35.833158;
            Double west = 51.059923;
            Double south = 35.544912;
            Double east = 51.628068;
            final BBox main_bbox = new BBox(north, east, west, south);

            System.out.println(main_bbox);

            int zoom = 11;

            Double count = 0.0;

            for (; zoom <= 18; zoom++) {
                BBox bbox0 = BBox.createXYZTile(zoom,main_bbox.getNorth(),main_bbox.getWest());
                BBox bbox1 = BBox.createXYZTile(zoom,main_bbox.getSouth(),main_bbox.getEast());
                int x = bbox1.getX() -bbox0.getX();
                int y = bbox1.getY() -bbox0.getY();
                int res = x*y;
                System.out.println("Tiles With Zoom Level "+ zoom +" : "+res);
                count+= res;
            }
            System.out.println("Whole Tiles : " + count);
        }catch(Exception e){

        }
        async.complete();
    }

//    @Test
    public void VTileRenderingSizeTest(TestContext tcontext){
        final Async async = tcontext.async();
//        Scanner input = new Scanner(System.in);
//        System.out.print("Do VTile Rendering Speed Test ? ");
//        char answer = input.next();
//
//        if(answer == "yes"){
//            System.out.println("Starting VTile Rendering Speed Test");
//        }else {
//            async.complete();
//            System.out.println("Exiting..");
//            return;
//        }
        try {
            System.out.println("VTile Rendering Speed Test Started...");
            Double north = 35.833158;
            Double west = 51.059923;
            Double south = 35.544912;
            Double east = 51.628068;
            final BBox main_bbox = new BBox(north, east, west, south);

            System.out.println(main_bbox);

            File file = new File("tehranbbox.csv");

            file.createNewFile();

            PrintStream out = new PrintStream(file);

            System.out.println(file.getAbsolutePath());
            out.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s","Zoom", "X", "Y", "North", "East", "West", "South","Size"));

            int zoom = 11;

            for (; zoom <= 18; zoom++) {
                JOptionPane.showConfirmDialog(null,"Zoom Level "+zoom);
                BBox bbox = main_bbox;
                bbox.createXYZ(zoom);
                while(bbox.getNorth()<=main_bbox.getNorth() && bbox.getNorth() >= main_bbox.getSouth()){
                    while(bbox.getEast()<=main_bbox.getEast()&&bbox.getEast()>=main_bbox.getWest()){

                        final BBox _bbox = bbox;

                        vertx.createHttpClient().getNow(config.getAsInteger("server.port"), config.get("server.host"), String.format(config.get("tehranroads.url.formated"), bbox.getZoom(), bbox.getX(), bbox.getY()),
                                response -> {
                                    response.handler(body -> {
                                        double size;
                                        size = (body.getBytes().length / 1024) / 1024;
                                        out.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s", _bbox.getZoom(), _bbox.getX(), _bbox.getY(), _bbox.getNorth(), _bbox.getEast(), _bbox.getWest(), _bbox.getSouth(), size));
                                    });
                                }
                        );
                        try{Thread.sleep(10);}catch(Exception e){};
                        bbox = new BBox(_bbox.getZoom(),_bbox.getX()+1,_bbox.getY());
                    }
                    bbox = new BBox(bbox.getZoom(),main_bbox.getX(),bbox.getY()+1);
                }
            }
        }catch(Exception e){

        }
        async.complete();
    }


}