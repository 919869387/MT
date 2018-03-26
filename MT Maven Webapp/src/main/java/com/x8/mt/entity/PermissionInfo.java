package com.x8.mt.entity;

public class PermissionInfo {
	private int id;
	private String permissionName;
	private String percode;
	private String resUrl;
	private String resType;
	private String parentId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public String getPercode() {
		return percode;
	}
	public void setPercode(String percode) {
		this.percode = percode;
	}
	public String getResUrl() {
		return resUrl;
	}
	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Override
	public String toString() {
		return "PermissionInfo [id=" + id + ", permissionName="
				+ permissionName + ", percode=" + percode + ", resUrl="
				+ resUrl + ", resType=" + resType + ", parentId=" + parentId
				+ "]";
	}
	
	
}
