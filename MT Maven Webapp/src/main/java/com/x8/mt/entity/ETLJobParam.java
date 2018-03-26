/**
 * 
 */
package com.x8.mt.entity;

/**
 * @author Tomcroods
 *
 */
public class ETLJobParam {
	private Datasource_connectinfo source;
	private Datasource_connectinfo target;
	String target_table;
	String source_table;
	String fieldSteram;
	String fieldDatabase;
	
	
	public Datasource_connectinfo getSource() {
		return source;
	}
	public void setSource(Datasource_connectinfo source) {
		this.source = source;
	}
	public Datasource_connectinfo getTarget() {
		return target;
	}
	public void setTarget(Datasource_connectinfo target) {
		this.target = target;
	}
	public String getTarget_table() {
		return target_table;
	}
	public void setTarget_table(String target_table) {
		this.target_table = target_table;
	}
	public String getSource_table() {
		return source_table;
	}
	public void setSource_table(String source_table) {
		this.source_table = source_table;
	}
	public String getFieldSteram() {
		return fieldSteram;
	}
	public void setFieldSteram(String fieldSteram) {
		this.fieldSteram = fieldSteram;
	}
	public String getFieldDatabase() {
		return fieldDatabase;
	}
	public void setFieldDatabase(String fieldDatabase) {
		this.fieldDatabase = fieldDatabase;
	}
	
}
