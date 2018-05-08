package com.x8.mt.entity;

import java.util.Date;

public class Dispatch {
	private int dispatchid;
	private String name;
	private String description;
	private String jobname;
	private int status;
	private String runinterval;
	private Date createtime;
	private Date recenttime;
	private Date endtime;
	private String type;
	public int getDispatchid() {
		return dispatchid;
	}
	public void setDispatchid(int dispatchid) {
		this.dispatchid = dispatchid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRuninterval() {
		return runinterval;
	}
	public void setRuninterval(String runinterval) {
		this.runinterval = runinterval;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getRecenttime() {
		return recenttime;
	}
	public void setRecenttime(Date recenttime) {
		this.recenttime = recenttime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return type;
	}
	
	
}
