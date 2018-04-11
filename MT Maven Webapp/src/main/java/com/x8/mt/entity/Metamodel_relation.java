package com.x8.mt.entity;

public class Metamodel_relation {
	
	private Integer id;
	private Integer metamodelid;
	private Integer relatedmetamodelid;
	private String type;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMetamodelid() {
		return metamodelid;
	}
	public void setMetamodelid(Integer metamodelid) {
		this.metamodelid = metamodelid;
	}
	public Integer getRelatedmetamodelid() {
		return relatedmetamodelid;
	}
	public void setRelatedmetamodelid(Integer relatedmetamodelid) {
		this.relatedmetamodelid = relatedmetamodelid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
