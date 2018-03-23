package com.x8.mt.entity;

public class Metamodel_datatype {
	private int id;
	private String name;
	private String type;//enum
	private String desribe;//可以缺省
	private int metamodelid;//元模型id
	private String category;
	
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
	public int getMetamodelid() {
		return metamodelid;
	}
	public void setMetamodelid(int metamodelid) {
		this.metamodelid = metamodelid;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	
	
}	
