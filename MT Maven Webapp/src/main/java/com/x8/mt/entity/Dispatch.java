package com.x8.mt.entity;

public class Dispatch {
	private String dispatchid;
	private String taskname;
	private String time;
	private String user;
	private String status;
	private int intervaltime;
	private String lasttime;
	
	public String getDispatchId() {
		return dispatchid;
	}
	public String getTaskName() {
		return taskname;
	}
	public String getTime() {
		return time;
	}
	public String getUser() {
		return user;
	}
	public String getStatus() {
		return status;
	}
	public int getIntervalTime() {
		return intervaltime;
	}
	public String getLastTime() {
		return lasttime;
	}
	
	public void setDispatchId(String dispatchId) {
		this.dispatchid = dispatchId;
	}
	public void setTaskName(String taskName) {
		this.taskname = taskName;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setIntervalTime(int intervalTime) {
		this.intervaltime = intervalTime;
	}
	public void setLastTime(String lastTime) {
		this.lasttime = lastTime;
	}
	
	public Dispatch() {
		
	}
	
}
