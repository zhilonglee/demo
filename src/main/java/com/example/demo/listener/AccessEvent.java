package com.example.demo.listener;

import org.springframework.context.ApplicationEvent;

public class AccessEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private String ipAddress;

	public AccessEvent(Object source,String url,String ipAddress) {
		super(source);
		this.url = url;
		this.ipAddress = ipAddress;

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	

}
