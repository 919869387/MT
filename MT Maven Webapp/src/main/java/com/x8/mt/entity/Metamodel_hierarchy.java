package com.x8.mt.entity;

public class Metamodel_hierarchy {
	private int id;
	private String name;
	private String type;//enum('METAMODEL','PACKAGE')
	private String desribe;//可以缺省
	private int parentid=0;//类型是包的时候为null
	private String category;
	private int mountnode;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getMountnode() {
		return mountnode;
	}
	public void setMountnode(int mountnode) {
		this.mountnode = mountnode;
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
	public int getParentid() {
		return parentid;
	}
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	
	
}	
