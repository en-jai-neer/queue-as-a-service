package com.queue.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Catalog {
	public final static String serviceCatalog = "{\"services\": [{"
											+ " \"name\": " + "\"queue-service\", "
											+ "\"id\": \"907a2df2-e81c-4e65-8d51-fcdc3771a8d2\","
											+ "\"description\": \"You can send, store, and receive messages between software components without losing messages or requiring other services to be available\","
											+ "\"bindable\": true,"
											+ "\"plan_updateable\": true,"
											+ "\"plans\": [{"
											+ " 		\"name\": \"basic\","
											+ " 		\"id\": \"deebe627-8471-4849-8966-345ccb00cdc0\","
											+ " 		\"description\": \"Queue will be able to hold 512 MB of memory\""
											+ " 		}]"
											+ " 	}]}";
	
	public static JSONObject getCatalogJSON() {
		JSONParser parser = new JSONParser();
		Object obj1 = "";
		try{
			obj1 = parser.parse(serviceCatalog);
		}
		catch(Exception e){
			System.out.println("Exception...");
			e.printStackTrace();
		}
		return (JSONObject)obj1;
	}
}
