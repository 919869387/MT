package com.x8.mt.entity;

public class RolePermission {
	private int id;
	private String roleId;
	private String permisssionId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getPermisssionId() {
		return permisssionId;
	}
	public void setPermisssionId(String permisssionId) {
		this.permisssionId = permisssionId;
	}
	@Override
	public String toString() {
		return "RolePermission [id=" + id + ", roleId=" + roleId
				+ ", permisssionId=" + permisssionId + "]";
	}
	
	
}
