package com.x8.mt.entity;

public class Sourcesystem {
	private int id;
	private String name;
	private String type;//enum('targetsystem','srcsystem')
	private String desribe;//可以缺省
	private Integer parentid;
	
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
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
	public String getDesribe() {
		return desribe;
	}
	public void setDesribe(String desribe) {
		this.desribe = desribe;
	}
}
