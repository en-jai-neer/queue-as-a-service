package com.queue.service;

import javax.json.bind.annotation.JsonbProperty;

public class ServiceInstance {
	
	@JsonbProperty("service_instance_id")
	private String service_instance_id;
	
	@JsonbProperty("service_id")
	private String service_id;
	
	@JsonbProperty("plan_id")
	private String plan_id;
	
	@JsonbProperty("organization_guid")
	private String organization_guid;
	
	@JsonbProperty("space_guid")
	private String space_guid;
	
	@JsonbProperty("queue_id")
	private String queue_id;
	
	public ServiceInstance() {
		super();
	}

	public ServiceInstance(String service_instance_id, String service_id, String plan_id, String organization_guid, String space_guid, String queue_id) {
		super();
		this.service_instance_id = service_instance_id;
		this.service_id = service_id;
		this.plan_id = plan_id;
		this.organization_guid = organization_guid;
		this.space_guid = space_guid;
		this.queue_id = queue_id;
	}

	public String getService_instance_id() {
		return service_instance_id;
	}

	public void setService_instance_id(String service_instance_id) {
		this.service_instance_id = service_instance_id;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

	public String getOrganization_guid() {
		return organization_guid;
	}

	public void setOrganization_guid(String organization_guid) {
		this.organization_guid = organization_guid;
	}

	public String getSpace_guid() {
		return space_guid;
	}

	public void setSpace_guid(String space_guid) {
		this.space_guid = space_guid;
	}
	
	public String getQueue_id() {
		return queue_id;
	}

	public void setQueue_id(String queue_id) {
		this.queue_id = queue_id;
	}

	@Override
	public String toString() {
		return "ServiceInstance [service_instance_id=" + service_instance_id + ", service_id=" + service_id
				+ ", plan_id=" + plan_id + ", organization_guid=" + organization_guid + ", space_guid=" + space_guid
				+ "]";
	}
	
}