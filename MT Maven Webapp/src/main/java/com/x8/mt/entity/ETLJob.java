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
	private int target_table_id;
	private String target_table;
	private String description;
	private String status;
	private Date create_date;
	private Date recently_run_date;
	private String log;
	
	
	public int getTarget_table_id() {
		return target_table_id;
	}
	public void setTarget_table_id(int target_table_id) {
		this.target_table_id = target_table_id;
	}
	public String getTarget_table() {
		return target_table;
	}
	public void setTarget_table(String target_table) {
		this.target_table = target_table;
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
	public Date getRecently_run_date() {
		return recently_run_date;
	}
	public void setRecently_run_date(Date recently_run_date) {
		this.recently_run_date = recently_run_date;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	
	
}
