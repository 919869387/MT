package com.x8.mt.entity;

public class SystemRole {
	private int id;
	private String rolename;
	private String description;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "SystemRole [id=" + id + ", rolename=" + rolename
				+ ", descriptionString=" + description + "]";
	}
	
	
	
}
