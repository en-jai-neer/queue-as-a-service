package com.queue.service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.security.PermitAll;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Path("/v1/queueservice")
public class ServiceController implements javax.ws.rs.container.ContainerRequestFilter{
	private final int maxSize = 536870912; 
	
	@Context
    private ResourceInfo resourceInfo;
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private String username;
    private String password;
	
	@PUT
	@Path("/add/{queue_id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response addToQueue(@PathParam("queue_id") String queue_id, String message) {
		
		String res = DBConnection.authenticate(queue_id, username, password);
		System.out.println(res);
		if(res.equals("AUTHENTICATED")) {
			System.out.println(queue_id);
			QueueInstance queue = HashMapQueue.getHashMapQueue().get(queue_id);
			System.out.println(queue.toString());
			long queueSize = queue.getSize() + message.length();
			if(queueSize > maxSize) {
				return Response.status(400).entity("Cannot add. Queue is full").build();
			}
			else {
				queue.setSize(queueSize);
				queue.getQueue().add(message);
				return Response.status(200).entity(null).build();
			}
		}
		else {
			return Response.status(403).entity(null).build();
		}
	}
	
	@GET
	@Path("/peek/{queue_id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response peekQueue(@PathParam("queue_id") String queue_id){
		String res = DBConnection.authenticate(queue_id, username, password);
		System.out.println(res);
		if(res.equals("AUTHENTICATED")) {
			System.out.println(queue_id);
			QueueInstance queue = HashMapQueue.getHashMapQueue().get(queue_id);
			System.out.println(queue.toString());
			if(queue.getSize() > 0) {
				String message = queue.getQueue().peek();
				return Response.status(200).entity(message).build();
			}
			else {
				return Response.status(400).entity("Queue is Empty").build();
			}
		}
		else {
			return Response.status(403).entity(null).build();
		}
	}
	
	@DELETE
	@Path("/remove/{queue_id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response removeFromQueue(@PathParam("queue_id") String queue_id){
		String res = DBConnection.authenticate(queue_id, username, password);
		System.out.println(res);
		if(res.equals("AUTHENTICATED")) {
			System.out.println(queue_id);
			QueueInstance queue = HashMapQueue.getHashMapQueue().get(queue_id);
			System.out.println(queue.toString());
			if(queue.getSize() > 0) {
				String message = queue.getQueue().poll();
				queue.setSize(queue.getSize() - message.length());
				return Response.status(200).entity(message).build();
			}
			else {
				return Response.status(400).entity("Queue is Empty").build();
			}
		}
		else {
			return Response.status(403).entity(null).build();
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		Method method = resourceInfo.getResourceMethod();
		if(!method.isAnnotationPresent(PermitAll.class))
        {
			final MultivaluedMap<String, String> headers = requestContext.getHeaders();
	        
	        //Fetch authorization header
	        final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
	          
	        //If no authorization information present; block access
	        if(authorization == null || authorization.isEmpty())
	        {
	        	System.out.println("Cannot authenticate");
	            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("You cannot access this resource").build());
	            return;
	        }
	        System.out.println("Authenticated and Authorized");
	        //Get encoded username and password
	        final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
	          
	        //Decode username and password
	        String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));;
	
	        //Split username and password tokens
	        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
	        this.username = tokenizer.nextToken();
	        this.password = tokenizer.nextToken();
	          
	        //Verify user access
	        int responseCode = DBConnection.authenticateUser(username, password);
	        if(responseCode == 401) {
	        	requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("You cannot access this resource").build());
	        	return;
	        }
        }
	}
}
