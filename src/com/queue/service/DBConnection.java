package com.queue.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class DBConnection {
	private static Connection conn = null;
	private static PreparedStatement ps = null;
	
	static {
		init();
	}
	
	public static void init() {
		try {
			System.out.println("Creating service_instance table in given database...");
			DatabaseMetaData dbm = makeJDBCConnection().getMetaData();
			// check if "employee" table is there
			ResultSet tables = dbm.getTables(null, null, "service_instance", null);
			if (tables.next()) {
			    log("Service_instance already created");
			}
			else {
				ps = makeJDBCConnection().prepareStatement("create table service_instance "
						+ " ( "
						+ " service_instance_id varchar(100) NOT NULL,"
						+ " service_id varchar(100),"
						+ " plan_id varchar(100),"
						+ " organization_guid varchar(100),"
						+ " space_guid varchar(100), "
						+ " queue_id varchar(100), "
						+ " PRIMARY KEY (service_instance_id)"
						+ " )");
				
				ps.executeUpdate();
				System.out.println("Created service_instance table in given database...");
			}
			
			tables = dbm.getTables(null, null, "service_binding", null);
			System.out.println("Creating service_binding table in given database...");
			if (tables.next()) {
			    log("service_binding already created");
			}
			else {
				ps = makeJDBCConnection().prepareStatement("create table service_binding "
						+ " ( "
						+ " binding_id varchar(100) NOT NULL, "
						+ " service_instance_id varchar(100),"
						+ " service_id varchar(100), "
						+ " plan_id varchar(100), "
						+ " username varchar(100), "
						+ " password varchar(100), "
						+ " PRIMARY KEY (binding_id) "
						+ " ) ");
				ps.executeUpdate();
				System.out.println("Created service_binding table in given database...");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	public static Connection makeJDBCConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			log("MySQL JDBC Driver Registered!");
		} catch (Exception e) {
			log("Sorry, couldn't found JDBC driver.");
			e.printStackTrace();
		}
 
		try {
			// DriverManager: The basic service for managing a set of JDBC drivers.
			//Map<String, String> env = System.getenv();
//			String s = "{\"postgresql\": [{\"binding_name\": null,\"credentials\": {\"dbname\": \"a3yNGLe1AaadljmT\",\"end_points\": ["
//		        		+"{\"host\": \"10.11.241.0\",\"network_id\": \"SF\",\"port\": \"56209\"}],\"hostname\": \"10.11.241.0\", \"password\": \"hnOYuZs4jfnHkkO6\","
//		        		+"\"port\": \"56209\", \"ports\": { \"5432/tcp\": \"56209\" },\"uri\": \"postgres://U2tuH1Cp3ZOjkynH:hnOYuZs4jfnHkkO6@10.11.241.0:56209/a3yNGLe1AaadljmT\","
//		        		+" \"username\": \"U2tuH1Cp3ZOjkynH\"},\"instance_name\": \"queue-service-postgres\",\"label\": \"postgresql\","
//		        		+"\"name\": \"queue-service-postgres\",\"plan\": \"v9.6-dev\", \"provider\": null, \"syslog_drain_url\": null, \"tags\": ["
//		        		+" \"postgresql\", \"relational\"], \"volume_mounts\": [] }]}";
     		JSONObject vcap = new JSONObject(System.getenv("VCAP_SERVICES")).getJSONArray("postgresql").getJSONObject(0).getJSONObject("credentials");
			String host = vcap.getString("hostname");
			String port = vcap.getString("port");
			String dbname = vcap.getString("dbname");
			String username = vcap.getString("username");
			String password = vcap.getString("password");
			String uri = "jdbc:postgresql://" + host + ":" + port + "/" + dbname + "?user=" + username + "&password=" + password;
			log(uri);
			conn = DriverManager.getConnection(uri);
			//conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb?user=postgres&password=1234");
			//conn = DriverManager.getConnection("jdbc:postgresql://10.11.241.0:56209/a3yNGLe1AaadljmT?user=U2tuH1Cp3ZOjkynH&password=hnOYuZs4jfnHkkO6");
			if (conn != null) {
				log("Connection Successful!");
			} else {
				log("Failed to make connection!");
			}
		} catch (SQLException e) {
			log("MySQL Connection Failed!");
			e.printStackTrace();
		}
		return conn;
	}
 
	public static void addServiceInstanceToDB(ServiceInstance si) {
 
		try {
			String insertQueryStatement = "Insert into service_instance "
					+ " (service_instance_id, service_id, plan_id, organization_guid, space_guid, queue_id) values "
					+ " (?,?,?,?,?,?)";
 
			ps = makeJDBCConnection().prepareStatement(insertQueryStatement);
			ps.setString(1, si.getService_instance_id());
			ps.setString(2, si.getService_id());
			ps.setString(3, si.getPlan_id());
			ps.setString(4, si.getOrganization_guid());
			ps.setString(5, si.getSpace_guid());
			ps.setString(6, si.getQueue_id());
			// execute insert SQL statement
			ps.executeUpdate();
			log(si.getService_id() + " added successfully");
		} 
		catch(SQLException e) {
			log("Could not add instance to DB");
			e.printStackTrace();
		}
		finally {
			closeConnection();
		}
	}
	
	public static String isServiceInstancePresent(ServiceInstance si) {
		 
		try {
			String getQueryStatement = "select * from service_instance where " + "service_instance_id = ?";
 
			ps = makeJDBCConnection().prepareStatement(getQueryStatement);
			ps.setString(1, si.getService_instance_id());
			// Execute the Query, and get a java ResultSet
			ResultSet rs = ps.executeQuery();
 
			if(rs.next()){
				if(rs.getString("service_id").equals(si.getService_id())
						&& rs.getString("plan_id").equals(si.getPlan_id()) 
						&& rs.getString("organization_guid").equals(si.getOrganization_guid())
						&& rs.getString("space_guid").equals(si.getSpace_guid())
						&& rs.getString("queue_id").equals(si.getQueue_id())) {
					log("SERVICE_WITH_SAME_ID_PROVISIONED");
					return "SERVICE_WITH_SAME_ID_PROVISIONED";
				}
				else {
					log("SERVICE_WITH_SAME_ID_PROVISIONED_BUT_ATTRIBUTES_ARE_DIFFERENT");
					return "SERVICE_WITH_SAME_ID_PROVISIONED_BUT_ATTRIBUTES_ARE_DIFFERENT";
				}
			}
			else{
				log("SERVICE_INSTANCE_ID_NOT_PRESENT");
				return "SERVICE_INSTANCE_ID_NOT_PRESENT";
			}
 
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			closeConnection();
		}
		return "SERVICE_INSTANCE_ID_NOT_PRESENT";
	}
	
	public static boolean isServiceInstancePresent(String serviceInstanceId) {
		 
		try{
			ps = makeJDBCConnection().prepareStatement("select * from service_instance where " + "service_instance_id = ?");
			ps.setString(1, serviceInstanceId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				log("true");
				return true;
			}
			else { 
				log("false");
				return false;
			}
		}
		catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			closeConnection();
		}
		return false;
	}
	
	public static int deleteServiceInstance(String serviceInstanceId) {
		int returnCode = 400;
		try{
			ps = makeJDBCConnection().prepareStatement("Delete from service_instance where service_instance_id = ?");
			ps.setString(1, serviceInstanceId);
			int res = ps.executeUpdate();
			if(res > 0){
				log("200");
				returnCode = 200;
			}
			else if(res == 0){
				log("410");
				returnCode = 410;
			}
		}
		catch(SQLException e){
			log("400");
			e.printStackTrace();
			returnCode = 400;
		}
		finally {
			closeConnection();
		}
		return returnCode;
	}
	
	public static void createServiceBinding(ServiceBinding sb) {
		try{
			ps = makeJDBCConnection().prepareStatement("Insert into service_binding "
					+ " (binding_id, service_instance_id, service_id, plan_id,"
					+ "	username, password) values"
					+ " (?,?,?,?,?,?)");
			ps.setString(1, sb.getBinding_id());
			ps.setString(2, sb.getService_instance_id());
			ps.setString(3, sb.getService_id());
			ps.setString(4, sb.getPlan_id());
			ps.setString(5, sb.getUsername());
			ps.setString(6, sb.getPassword());
			ps.executeUpdate();
			log(sb.toString() + " added successfully");
		}
		catch(SQLException e){
			log("Could not add the service binding");
			e.printStackTrace();
		}
		finally{
			closeConnection();
		}
		
	}

	public static String isBindingPresent(ServiceBinding sb) {
		try {
			ps = makeJDBCConnection().prepareStatement("select * from service_binding where " + "binding_id = ?");
			ps.setString(1, sb.getBinding_id());
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				if(rs.getString("service_instance_id").equals(sb.getService_instance_id())
						&& rs.getString("service_id").equals(sb.getService_id())
						&& rs.getString("plan_id").equals(sb.getPlan_id())) {
					log("BINDING_ID_PROVISIONED");
					return "BINDING_ID_PROVISIONED";
				}
				else {
					log("BINDING_ID_PROVISIONED_BUT_ATTRIBUTES_ARE_DIFFERENT");
					return "BINDING_ID_PROVISIONED_BUT_ATTRIBUTES_ARE_DIFFERENT";
				}
			}
			else{
				log("BINDING_ID_DOESNT_EXISTS");
				return "BINDING_ID_DOESNT_EXISTS";
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			closeConnection();
		}
		
		return "BINDING_ID_DOESNT_EXISTS";
	}
	
	public static Credentials getBindingCredentials(String serviceBindId) {
		// TODO Auto-generated method stub
		Credentials creds = null;		
		try {
			ps = makeJDBCConnection().prepareStatement("select * from service_binding where " + "binding_id = ?");
			ps.setString(1, serviceBindId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				creds = new Credentials("q" + rs.getString("service_instance_id"), rs.getString("username"), rs.getString("password"));
			}
			else {
				return null;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			closeConnection();
		}
		log(creds.toString());
		return creds;
	}
	
	public static String authenticate(String queue_id, String username, String password) {
		try {
			ps = makeJDBCConnection().prepareStatement("select * from service_binding where " + "username = ?");
			log(username);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				String instance_id = rs.getString("service_instance_id");
				log(instance_id);
				if(queue_id.equals("q" + instance_id)&& rs.getString("password").equals(password)) {
					log("AUTHENTICATED");
					return "AUTHENTICATED";
				}
				else {
					log("INCORRECT_USERNAME_OR_PASSWORD");
					return "INCORRECT_USERNAME_OR_PASSWORD";
				}
			}
			else{
				log("INCORRECT_USERNAME_OR_PASSWORD");
				return "INCORRECT_USERNAME_OR_PASSWORD";
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			closeConnection();
		}
		
		return "INCORRECT_USERNAME_OR_PASSWORD";
	}
	
	public static int authenticateUser(String username, String password) {
		try {
			ps = makeJDBCConnection().prepareStatement("select * from service_binding where " + "username = ?");
			log(username);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				if(rs.getString("password").equals(password)) {
					log("200");
					return 200;
				}
				else {
					log("401");
					return 401;
				}
			}
			else{
				log("401");
				return 401;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			closeConnection();
		}
		
		return 401;
	}
	
	public static int deleteBinding(String serviceBindId) {
		int returnCode = 400;
		Credentials cred = getBindingCredentials(serviceBindId);
		if(cred == null){
			returnCode =  410;
		}
		else{
			try{
				ps = makeJDBCConnection().prepareStatement( "Delete from service_binding "
						+ "where binding_id = ?");
				ps.setString(1, serviceBindId);
				if(ps.executeUpdate() >=0 ){
					log("200");
					returnCode = 200;
				}
			}
			catch(SQLException e){
				log("400");
				e.printStackTrace();
				returnCode = 400;
			}
			finally{
				closeConnection();
			}
		}
		return returnCode;
	}
	
	public static void closeConnection(){
		try {
			conn.close();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("problems closing connection");
			e.printStackTrace();
		}
	}
 
	// Simple log utility
	private static void log(String string) {
		System.out.println(string);
	}

}
