package com.jwis.service.adapter;

public enum PLMSystemEnum {
	WINDCHILL,
	ARAS,
	OA,
	IMIS,
	ERP,
	ALL;
	
	public String getName(){
		return this.name();
	}
}
