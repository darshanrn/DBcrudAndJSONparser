package com.dash;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

public class DBconn {
	private static Connection conn;
	private static AtomicInteger mOpenCounter = new AtomicInteger();

	public static synchronized Connection getDBinstance()
	{
		if(conn == null && mOpenCounter.incrementAndGet() == 1)
		{
			try {
				conn = DriverManager.getConnection("jdbc:sqlite:geonames.db");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return conn;		
	}

	public static synchronized void closeDB()
	{
		if(mOpenCounter.decrementAndGet() == 0) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean CreateAndInsertValuesToDB(Geoname[] geonames)
	{
		Connection conn = null;
		Statement stmt = null;

		File dbFile = new File("geonames.db");
		if(dbFile.exists()){
			System.out.println("Database already exists. Hence deleting..");
			dbFile.delete();
		}

		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:geonames.db");
			System.out.println("Created & Opened a new database successfully");

			stmt = conn.createStatement();
			String sql = "CREATE TABLE GEONAME " +
					"(ID				INTEGER 		AUTO INCREMENT, " + 
					"SUMMARY     		VARCHAR(500)			, " + 
					" RANK          	INTEGER    		NOT NULL, " + 
					" TITLE        		VARCHAR(50)				, " + 
					" WIKIPEDIAURL      VARCHAR(150)			," +
					" ELEVATION         INTEGER    		NOT NULL, " + 
					" COUNTRYCODE       VARCHAR(2) 		NOT NULL, " + 
					" LONGITUDE         DOUBLE					, " +
					" FEATURE 			VARCHAR(20)				, " +	
					" THUMBNAILIMG 		VARCHAR(150)			, " +
					" GEONAMEID 		BIGINT					, " +
					" LANGUAGE 			VARCHAR(2)				, " +
					" LATITUDE 			DOUBLE					)"; 
			stmt.executeUpdate(sql);

			for(int index = 0; index < geonames.length; index++)
			{
				sql =  "INSERT INTO GEONAME " +
						"VALUES (" + (index + 1) + "," + 
						"\'" + geonames[index].summary.replace("'", "") + "\'," + 
						geonames[index].rank + "," + 
						"\"" + 	geonames[index].title + "\"," + 
						"\"" + 	geonames[index].wikipediaUrl + "\"," + 
						geonames[index].elevation + "," + 
						"\"" + 	geonames[index].countryCode + "\"," + 
						geonames[index].lng + "," + 
						"\"" + 	geonames[index].feature + "\"," + 
						"\"" + 	geonames[index].thumbnailImg + "\"," + 
						geonames[index].geoNameId + "," + 
						"\"" + 	geonames[index].lang + "\"," + 
						geonames[index].lat + ")";
				System.out.println("SQL query = " + sql);
				stmt.executeUpdate(sql);
			}

			stmt.close();
			conn.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
		System.out.println("Table created successfully");
		return true;
	}

	public void GetAllFeatures(String featureName)
	{
		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:geonames.db");
			System.out.println("Opened the database successfully");

			stmt = conn.createStatement();
			
			/*use this sql query to avoid the following problem
			 * [SQLITE_BUSY] The database file is locked (database is locked)
			 * pragma busy_timeout keeps the busy handler of the DB silent for
			 * multiple times within the specified timeout
			 */
			String waitTimeoutSQL = "pragma busy_timeout=30000;";
			stmt.executeQuery(waitTimeoutSQL);
			
			String sql = "SELECT * FROM GEONAME WHERE FEATURE LIKE '%" + featureName + "%'"; 
			ResultSet rs = stmt.executeQuery(sql);

			while ( rs.next() ) {				
				System.out.println(rs.getString("ID") + "|" +
						rs.getString("TITLE") + "|" + 
						rs.getString("COUNTRYCODE") + "|" +
						rs.getString("FEATURE") + "|" +
						rs.getString("LANGUAGE") + "|" );
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
	}
}

