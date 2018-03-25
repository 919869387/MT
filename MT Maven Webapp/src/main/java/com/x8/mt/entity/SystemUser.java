package com.x8.mt.entity;

public class SystemUser {
	int id;
	String username;
	String password;
	String salt;
	String status;
	String usercode;
	
	
	
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	@Override
	public String toString() {
		return "SystemUser [id=" + id + ", username=" + username
				+ ", password=" + password + ", salt=" + salt + ", status="
				+ status + ", usercode=" + usercode + "]";
	}

	
	
	
}
