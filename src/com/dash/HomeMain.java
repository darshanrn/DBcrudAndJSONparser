package com.dash;


public class HomeMain {
	public static void main(String []args)
	{
		double d = -0.07857;
		System.out.println(d);
		PropertyValueReader props = new PropertyValueReader();
		String url = props.getPropertyValue();
		
		System.out.println("Webservice URL = " + url);
		
		String jsonResponse = HttpClient.GetRequest(url);
		System.out.println("JSON Response = " + jsonResponse);
		
		final Geoname[] geos = JSONparser.parseIt(jsonResponse);

		//insert into database
		final DBconn dbConnection = new DBconn();
		
		class FirstThread extends Thread{
			public void run()
			{
				dbConnection.CreateAndInsertValuesToDB(geos);
			}
		}
		
		class SecondThread extends Thread{
			public void run()
			{
				dbConnection.GetAllFeatures("landmark");
			}
		}
		
		FirstThread t1 = new FirstThread();
		t1.start();
		
		/*Wait for 2 seconds while the 1st thread inserts few values to the database */
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SecondThread t2 = new SecondThread();
		t2.start();
	}
}
