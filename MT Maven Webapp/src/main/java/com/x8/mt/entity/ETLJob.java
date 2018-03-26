/**
 * 
 */
package com.x8.mt.entity;

import java.util.Date;


/**
 * @author Tomcroods
 *
 */
public class ETLJob {
	private int id;
	private String description;
	private String status;
	private Date create_date;
	private int createuserid;
	private Date recently_run_date;
	private int recentlyrunuserid;
	private String log;
	private int metadata_id;
	private String type;
	private int mappingid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public int getCreateuserid() {
		return createuserid;
	}
	public void setCreateuserid(int createuserid) {
		this.createuserid = createuserid;
	}
	public Date getRecently_run_date() {
		return recently_run_date;
	}
	public void setRecently_run_date(Date recently_run_date) {
		this.recently_run_date = recently_run_date;
	}
	public int getRecentlyrunuserid() {
		return recentlyrunuserid;
	}
	public void setRecentlyrunuserid(int recentlyrunuserid) {
		this.recentlyrunuserid = recentlyrunuserid;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public int getMetadata_id() {
		return metadata_id;
	}
	public void setMetadata_id(int metadata_id) {
		this.metadata_id = metadata_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getMappingid() {
		return mappingid;
	}
	public void setMappingid(int mappingid) {
		this.mappingid = mappingid;
	}

}
