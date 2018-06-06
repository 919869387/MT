package com.x8.mt.common;

import java.util.List;

/**
 * 
 * 作者:  allen
 * 时间:2017年11月13日
 * 作用:分页公共类
 */
public class PageParam {
	private int currPage;//当前页
	private int totalPage;//总页
	private int rowCount;//总记录数
	private int pageSize;//页大小
	private List data;//数据
	private int totalMetadataCount;//总元数据记录数
	private String queryTime;//查询时间-秒为单位
	
	
	public int getTotalMetadataCount() {
		return totalMetadataCount;
	}
	public void setTotalMetadataCount(int totalMetadataCount) {
		this.totalMetadataCount = totalMetadataCount;
	}
	public String getQueryTime() {
		return queryTime;
	}
	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}
	public void setData(List data) {
		this.data = data;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	private void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		int totalPage = rowCount/pageSize;
		if(rowCount%pageSize>0){
			totalPage +=1;
		}
		setTotalPage(totalPage);
		this.rowCount = rowCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List getData() {
		return data;
	}
	public void setDate(List data) {
		this.data = data;
	}
}
