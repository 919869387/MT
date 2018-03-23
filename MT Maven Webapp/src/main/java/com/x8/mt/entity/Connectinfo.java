package com.x8.mt.entity;

public class Connectinfo {
	private int id;
	private String name;
	private String type;
	private int mountMetaDataId;
	private int needCheck;
	private String description;
	
	public Connectinfo(){}
	
	public Connectinfo(String name, String type, int mountMetaDataId,
			int needCheck, String description) {
		this.name = name;
		this.type = type;
		this.mountMetaDataId = mountMetaDataId;
		this.needCheck = needCheck;
		this.description = description;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getMountMetaDataId() {
		return mountMetaDataId;
	}
	public void setMountMetaDataId(int mountMetaDataId) {
		this.mountMetaDataId = mountMetaDataId;
	}
	public int getNeedCheck() {
		return needCheck;
	}
	public void setNeedCheck(int needCheck) {
		this.needCheck = needCheck;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
