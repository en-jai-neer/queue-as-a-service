package com.queue.service;

import javax.json.bind.annotation.JsonbProperty;

public class ServiceBinding {
	
	@JsonbProperty("binding_id")
	private String binding_id;
	
	@JsonbProperty("service_instance_id")
	private String service_instance_id;
	
	@JsonbProperty("service_id")
	private String service_id;
	
	@JsonbProperty("plan_id")
	private String plan_id;
	
	@JsonbProperty("username")
	private String username;
	
	@JsonbProperty("password")
	private String password;

	public ServiceBinding() {
		super();
	}

	public ServiceBinding(String binding_id, String service_instance_id, String service_id, String plan_id, String username, String password) {
		super();
		this.binding_id = binding_id;
		this.service_instance_id = service_instance_id;
		this.service_id = service_id;
		this.plan_id = plan_id;
		this.username = username;
		this.password = password;
	}

	public String getBinding_id() {
		return binding_id;
	}

	public void setBinding_id(String binding_id) {
		this.binding_id = binding_id;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ServiceBinding [service_bind_id=" + binding_id + ", service_instance_id=" + service_instance_id
				+ ", service_id=" + service_id + ", plan_id=" + plan_id + ", " + "username="
				+ username + ", password=" + password + "]";
	}

	
}