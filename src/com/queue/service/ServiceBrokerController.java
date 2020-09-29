package com.queue.service;

import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/v2")
public class ServiceBrokerController {
	private final String serviceId = "907a2df2-e81c-4e65-8d51-fcdc3771a8d2";
	private final String planId = "deebe627-8471-4849-8966-345ccb00cdc0";
	
	@PermitAll
	@GET
	@Path("/catalog")
	@Produces("application/json")
	public Response getCatalogServices(){
		
		org.json.simple.JSONObject catalogJSON = Catalog.getCatalogJSON();
		if(catalogJSON.isEmpty()){
			return Response.status(501).entity("{}").build();
		}
		return Response.status(200).entity(catalogJSON.toString()).build();
	}
	
	@PermitAll
	@PUT
	@Path("/service_instances/{instance_id}")
	@Produces("application/json")
	public Response createServiceInstance(@PathParam("instance_id") String instanceId, String requestBody){
		log(requestBody);
		Jsonb jsonb = JsonbBuilder.create();
		ServiceInstance serviceInstance = jsonb.fromJson(requestBody, ServiceInstance.class);

		serviceInstance.setService_instance_id(instanceId);
		serviceInstance.setQueue_id("q" + instanceId);
		
		if(serviceInstance.getService_id() == null || serviceInstance.getPlan_id() == null || serviceInstance.getOrganization_guid() == null || serviceInstance.getSpace_guid() == null) {
			return Response.status(400).entity("{}").build();
		}
		
		if(serviceInstance.getService_id().equals(serviceId) == false || serviceInstance.getPlan_id().equals(planId) == false) {
			return Response.status(400).entity("{}").build();
		}
			
		
		String res = DBConnection.isServiceInstancePresent(serviceInstance);
		
		if(res == "SERVICE_WITH_SAME_ID_PROVISIONED") {
			return Response.status(200).entity("{}").build();
		} 
		else if (res == "SERVICE_WITH_SAME_ID_PROVISIONED_BUT_ATTRIBUTES_ARE_DIFFERENT") {
			return Response.status(409).entity("{}").build();
		} 
		else if (res == "SERVICE_INSTANCE_ID_NOT_PRESENT") {
			HashMapQueue.getHashMapQueue().put("q" + instanceId, new QueueInstance());
			
			DBConnection.addServiceInstanceToDB(serviceInstance);
			return Response.status(201).entity("{}").build();
		}
		
		return Response.status(400).entity("{}").build();
	}
	
	@PermitAll
	@PUT
	@Path("/service_instances/{instance_id}/service_bindings/{service_bind_id}")
	@Produces("application/json")
	public Response createServiceBinding(@PathParam("instance_id") String instanceId,
										@PathParam("service_bind_id") String bindingId,
										String requestBody){
		boolean isInstancePresent = DBConnection.isServiceInstancePresent(instanceId);
		if(isInstancePresent == false){
			return Response.status(400).entity("Service Instance ID doesnt exist").build();
		}
		
		log(requestBody);
		Jsonb jsonb = JsonbBuilder.create();
		ServiceBinding serviceBinding = jsonb.fromJson(requestBody, ServiceBinding.class);
		
		serviceBinding.setBinding_id(bindingId);
		serviceBinding.setService_instance_id(instanceId);
		log(serviceBinding.toString());
		if(serviceBinding.getService_id() == null || serviceBinding.getPlan_id() == null) {
			return Response.status(400).entity("{}").build();
		}
		if(serviceBinding.getService_id().equals(serviceId) == false || serviceBinding.getPlan_id().equals(planId) == false) {
			return Response.status(400).entity("{}").build();
		}
		
		String res = DBConnection.isBindingPresent(serviceBinding);
		log(res);
		if(res == "BINDING_ID_PROVISIONED") {
			return Response.status(200).entity(jsonb.toJson(DBConnection.getBindingCredentials(bindingId))).build();
		} 
		else if (res == "BINDING_ID_PROVISIONED_BUT_ATTRIBUTES_ARE_DIFFERENT") {
			return Response.status(409).entity("{}").build();
		} 
		else if (res == "BINDING_ID_DOESNT_EXISTS") {
			serviceBinding.setUsername("user" + bindingId);
			UUID uuid = UUID.randomUUID();
			serviceBinding.setPassword(uuid.toString());
			
			DBConnection.createServiceBinding(serviceBinding);
			String resp = jsonb.toJson(DBConnection.getBindingCredentials(bindingId));
			log(resp);
			log("{" + "\"credentials\"" + ":" + resp + "}");
			return Response.status(201).entity("{" + "\"credentials\"" + ":" + resp + "}").build();
		}
		return Response.status(400).entity("{}").build();
	}
	
	@PermitAll
	@DELETE
	@Path("/service_instances/{instance_id}/service_bindings/{service_bind_id}")
	@Produces("application/json")
	public Response deleteServiceBinding(@PathParam("instance_id") String instanceId, @PathParam("service_bind_id") String bindingId, @QueryParam("service_id") String service_id, @QueryParam("plan_id") String plan_id){
		if(serviceId.equals(service_id) == false || planId.equals(plan_id)) {
			return Response.status(400).entity("{}").build();
		}
		
		int response_code = DBConnection.deleteBinding(bindingId);
		
		return Response.status(response_code).entity("{}").build();
	}
	
	@PermitAll
	@DELETE
	@Path("/service_instances/{instance_id}")
	@Produces("application/json")
	public Response deleteService(@PathParam("instance_id") String instanceId, @QueryParam("service_id") String service_id, @QueryParam("plan_id") String plan_id) {
		if(serviceId.equals(service_id) == false || planId.equals(plan_id)) {
			return Response.status(400).entity("{}").build();
		}
		
		int response_code = DBConnection.deleteServiceInstance(instanceId);
		
		if(response_code == 200)
			HashMapQueue.getHashMapQueue().remove("q" + instanceId);
		return Response.status(response_code).entity("{}").build();
	}
	
	private void log(String string) {
		System.out.println(string);
	}
}
