package com.x8.mt.entity;

public class MetadataViewNode {
	int id;
	int viewid;
	String name;
	String type;
	int metamodelid;
	String childtype;
	int childmetamodelid;
	int level;
	int parentid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getViewid() {
		return viewid;
	}
	public void setViewid(int viewid) {
		this.viewid = viewid;
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
	public int getMetamodelid() {
		return metamodelid;
	}
	public void setMetamodelid(int metamodelid) {
		this.metamodelid = metamodelid;
	}
	public String getChildtype() {
		return childtype;
	}
	public void setChildtype(String childtype) {
		this.childtype = childtype;
	}
	public int getChildmetamodelid() {
		return childmetamodelid;
	}
	public void setChildmetamodelid(int childmetamodelid) {
		this.childmetamodelid = childmetamodelid;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getParentid() {
		return parentid;
	}
	public void setParentid(int parentid) {
		this.parentid = parentid;
	}
	
	
}
