package com.x8.mt.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * 作者:GodDispose
 * 时间:下午8:40:22
 * 作用:
 * 参数:
 */
public class Metadata {
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化时间
	
	private int id;
	private String name;
	private int collectJobId;
	private String description;//可以缺省
	private int metaModelId;
	private Date createTime;
	private Date updateTime;
	private String checkStatus;
	private String attributes;
	private int version;

	public Metadata(){}

	public Metadata( String name, int collectJobId,
			String description, int metaModelId, Date createTime,
			Date updateTime, String checkStatus, String attributes,int version) {
		super();
		this.name = name;
		this.collectJobId = collectJobId;
		this.description = description;
		this.metaModelId = metaModelId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.checkStatus = checkStatus;
		this.attributes = attributes;
		this.version = version;
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

	public int getCollectJobId() {
		return collectJobId;
	}

	public void setCollectJobId(int collectJobId) {
		this.collectJobId = collectJobId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMetaModelId() {
		return metaModelId;
	}

	public void setMetaModelId(int metaModelId) {
		this.metaModelId = metaModelId;
	}

	public String getCreateTime() {//allen修改
		String str=sdf.format(createTime); 
		return str;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {//allen修改
		String str=sdf.format(updateTime); 
		return str;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
