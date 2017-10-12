package com.mpexample.mpHelloWorld.rest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;
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

		// fetch env var for number of data items in IMDG and determine key
		int keyValue;
		String numOfDataItemsStr = System.getenv("NUM_OF_DATA_ITEMS");
		int numOfDataItems = Integer.parseInt(numOfDataItemsStr);
		keyValue = ThreadLocalRandom.current().nextInt(0, numOfDataItems);
		
		// fetch env var for REST calling url and append key to it
		String urlCacheName = System.getenv("URL_CACHE_NAME");
		urlCacheName += keyValue;
		
		try {
			String name = getMethod(urlCacheName);		
			greeting = "Hello " + name + " from MicroProfile microservice";

		} catch (IOException e) {
			return Response.ok("REST call to microservice problem!" + e.toString()).build();
		}
		
		return Response.ok(greeting.toString()).build();
	}
	
	/**
	    * Method that gets an value by a key in url as param value.
	    * @param urlServerAddress
	    * @return String value
	    * @throws IOException
	    */
	   public String getMethod(String urlServerAddress) throws IOException {
	      String line = new String();
	      StringBuilder stringBuilder = new StringBuilder();

	      URL address = new URL(urlServerAddress);

	      HttpURLConnection connection = (HttpURLConnection) address.openConnection();
	      connection.setRequestMethod("GET");
	      connection.setRequestProperty("Content-Type", "text/plain");
	      connection.setDoOutput(true);

	      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

	      connection.connect();
	      while ((line = bufferedReader.readLine()) != null) {
	         //stringBuilder.append(line + '\n');
	         stringBuilder.append(line);
	      }
	      connection.disconnect();

	      return stringBuilder.toString();
	   }	
}
