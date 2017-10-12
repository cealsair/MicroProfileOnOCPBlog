package com.mpexample.mpHelloWorld.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

@ApplicationScoped
@Path("/hello")
public class HelloWorldEndpoint {

	@GET
	@Produces("text/plain")
	public Response doGet() {
		String greeting = "Greeting should not be this one";
		
		try {
			String dbURL = "jdbc:postgresql://";
			dbURL += System.getenv("POSTGRESQL_SERVICE_HOST");
			dbURL += ":" + System.getenv("POSTGRESQL_SERVICE_PORT");
			dbURL += "/" + System.getenv("POSTGRESQL_DATABASE");
			
			String user = System.getenv("POSTGRESQL_USER");
			String pass = System.getenv("PGPASSWORD");
			
			Connection conn = DriverManager.getConnection(dbURL, user, pass);
			
			if (conn != null) {
				String sqlStmt = "select id AS theKey, string AS theName from names ORDER BY random() limit 1";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlStmt);
				while (rs.next()) {
					greeting = "Hello " + rs.getString("theName") + " from MicroProfile microservice";
				}
				rs.close();
				conn.close();
			}
		} catch (Exception e) {
			return Response.ok("Database connection problem!" + e.toString()).build();
		}
		return Response.ok(greeting.toString()).build();
	}
}
