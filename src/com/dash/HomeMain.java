package com.dash;


public class HomeMain {
	public static void main(String []args)
	{
		PropertyValueReader props = new PropertyValueReader();
		String url = props.getPropertyValue();
		
		System.out.println("Webservice URL = " + url);
		
		String jsonResponse = HttpClient.GetRequest(url);
		System.out.println("JSON Response = " + jsonResponse);
		
		Geoname[] geos = JSONparser.parseIt(jsonResponse);

		//insert into database
	}
}
