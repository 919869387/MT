package com.x8.mt.entity;

import java.util.Date;

public class CollectJob {
	private int id;
	private String name;
	private int connectinfoId;
	private String mode;
	private String checkResult;
	private Date createDate;
	private String creater;
	
	public CollectJob() {
	}

	public CollectJob(String name, int connectinfoId, String mode,
			String checkResult, Date createDate, String creater) {
		super();
		this.name = name;
		this.connectinfoId = connectinfoId;
		this.mode = mode;
		this.checkResult = checkResult;
		this.createDate = createDate;
		this.creater = creater;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getConnectinfoId() {
		return connectinfoId;
	}

	public void setConnectinfoId(int connectinfoId) {
		this.connectinfoId = connectinfoId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}
	
	
	

}