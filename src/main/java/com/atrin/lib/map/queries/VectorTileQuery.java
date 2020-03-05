package com.atrin.lib.map.queries;

import com.atrin.lib.map.coord.*;

// import com.google.gson.JsonArray;
// import com.google.gson.JsonObject;
// import com.google.gson.JsonParser;
// import com.google.gson.JsonPrimitive;

import java.util.HashMap;

public class VectorTileQuery implements Query{
	private BBox bbox;
	private String keyword;
	private HashMap input_data;

	public VectorTileQuery(String json_query){
		input_data = this.parseFromJson(json_query);
	}

	public VectorTileQuery(int x,int y,int z,String query,String format){
		input_data = new HashMap();

		input_data.put("__X__",x);
		input_data.put("__Y__",y);
		input_data.put("__Z__",z);
		input_data.put("__BBOX__",new BBox(x,y,z));
		input_data.put("__QUERY__",query);
		input_data.put("__FORMAT__",format);
	}

	public static HashMap parseFromJson(String input){
		// JsonObject json = new JsonParser().parse(input).getAsJsonObject();
		return null;
	}

	public HashMap getRawInput(){
		return input_data;
	}

	@Override
	public String createDataBaseQuery(){
		return String.format("");
	}

	@Override
	public void getAll(){

	}
}