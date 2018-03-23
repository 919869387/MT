package com.x8.mt.entity;

public class MetaDataRelation {

	private int id;
	private int metaDataId;
	private int relateMetaDataId;
	private String type;
	
	public MetaDataRelation(){}
	
	public MetaDataRelation( int metaDataId, int relateMetaDataId,
			String type) {
		super();
		this.metaDataId = metaDataId;
		this.relateMetaDataId = relateMetaDataId;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMetaDataId() {
		return metaDataId;
	}

	public void setMetaDataId(int metaDataId) {
		this.metaDataId = metaDataId;
	}

	public int getRelateMetaDataId() {
		return relateMetaDataId;
	}

	public void setRelateMetaDataId(int relateMetaDataId) {
		this.relateMetaDataId = relateMetaDataId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
