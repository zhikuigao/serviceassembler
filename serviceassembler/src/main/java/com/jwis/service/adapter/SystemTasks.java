package com.jwis.service.adapter;

import java.util.List;

public class SystemTasks {
	private int result;
	private String userId;
	private List<TaskData> data;
	private String errorMsg;
	private String currentTime;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<TaskData> getData() {
		return data;
	}
	public void setData(List<TaskData> data) {
		this.data = data;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
}
