package com.atrin.lib.map.queries;

import com.atrin.lib.map.coord.*;

import java.util.HashMap;

public class SearchQuery implements Query{
	private BBox bbox;
	private String keyword;
	private String json_query;

	public SearchQuery(String json_query){
		this.json_query = json_query;
	}


	public static HashMap parseFromJson(String input){
		return null;
	}

	@Override
	public String createDataBaseQuery(){
		return null;
	}

	// public JsonObject getAll(){
	// 	return null;
	// }
	@Override
	public void getAll(){

	}
}