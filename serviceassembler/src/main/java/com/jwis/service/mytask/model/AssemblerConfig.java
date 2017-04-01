package com.jwis.service.mytask.model;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "assembler",locations = "classpath:config/config.properties")  
public class AssemblerConfig  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5392484296056410735L;
	private String app_gateway_address;
	private String usercenter_service_path;
	private String systems_service_path;
	public String getApp_gateway_address() {
		return app_gateway_address;
	}
	public void setApp_gateway_address(String app_gateway_address) {
		this.app_gateway_address = app_gateway_address;
	}
	public String getUsercenter_service_path() {
		return usercenter_service_path;
	}
	public void setUsercenter_service_path(String usercenter_service_path) {
		this.usercenter_service_path = usercenter_service_path;
	}
	public String getSystems_service_path() {
		return systems_service_path;
	}
	public void setSystems_service_path(String systems_service_path) {
		this.systems_service_path = systems_service_path;
	}
}
