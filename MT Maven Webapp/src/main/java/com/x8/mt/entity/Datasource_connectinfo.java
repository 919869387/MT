package com.x8.mt.entity;

public class Datasource_connectinfo {
	private int id;
	private String url;
	private String port;
	private String username;
	private String password;
	private String databasename;
	private String databasetype;
	private int parentid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
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

	public String getDatabasename() {
		return databasename;
	}
	public void setDatabasename(String databasename) {
		this.databasename = databasename;
	}
	public String getDatabasetype() {
		return databasetype;
	}
	public void setDatabasetype(String databasetype) {
		this.databasetype = databasetype;
	}
	public int getParentid() {
		return parentid;
	}
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	
}
