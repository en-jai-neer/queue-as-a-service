package com.queue.service;

public class Credentials {
	final String uri = "https://q-service.cfapps.stagingaws.hanavlab.ondemand.com/v1/queueservice/";
	
	@Override
	public String toString() {
		return "Credentials [uri=" + uri + ", queue_id=" + queue_id + ", username=" + username + ", password="
				+ password + "]";
	}

	private String queue_id;
	
	private String username;
	
	private String password;

	public Credentials() {
		super();
	}

	public Credentials(String queue_id, String username, String password) {
		super();
		this.queue_id = queue_id;
		this.username = username;
		this.password = password;
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
	
	public String getQueue_id() {
		return queue_id;
	}

	public void setQueue_id(String queue_id) {
		this.queue_id = queue_id;
	}
	
	public String getUri() {
		return uri;
	}
}