package com.dash;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class JSONparser {

	class JObject{
		String key;
		String value;
		
		public String getValue()
		{
			return value;
		}
	}
	
	public static Geoname[] parseIt(String jsonString)
	{
		JSONObject obj;
		String geonames = null;
		try {
			obj = new JSONObject(jsonString);
			geonames = obj.getString("geonames");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		Gson g = new Gson();
		Geoname[] geoArrayItems = g.fromJson(geonames, Geoname[].class);

		return geoArrayItems;
	}
}
