package com.x8.mt.entity;

import java.util.Date;

public class SystemLog {
    private String id;//日志太多用UUID
    private String operationtype;//操作类型,也就是指操作哪个表
    private String operationdesc;//操作说明,包含操作影响的总记录数
    private String result;//成功、失败、异常
    private Date startdatetime;//开始时间
    private Date enddatetime;//结束时间
    private String systemusername;//用户名
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getStartdatetime() {
		return startdatetime;
	}
	public void setStartdatetime(Date startdatetime) {
		this.startdatetime = startdatetime;
	}
	public Date getEnddatetime() {
		return enddatetime;
	}
	public void setEnddatetime(Date enddatetime) {
		this.enddatetime = enddatetime;
	}
	public String getOperationtype() {
		return operationtype;
	}
	public void setOperationtype(String operationtype) {
		this.operationtype = operationtype;
	}
	public String getOperationdesc() {
		return operationdesc;
	}
	public void setOperationdesc(String operationdesc) {
		this.operationdesc = operationdesc;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getSystemusername() {
		return systemusername;
	}
	public void setSystemusername(String systemusername) {
		this.systemusername = systemusername;
	}
}
