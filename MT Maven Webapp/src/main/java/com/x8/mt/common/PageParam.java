package com.x8.mt.common;

import java.util.List;

/**
 * 
 * 作者:allen
 * 时间:2017年11月13日
 * 作用:分页公共类
 */
public class PageParam {
	private int currPage;//当前页
	private int totalPage;//总页
	private int rowCount;//总记录数
	private int pageSize;//页大小
	private List date;//数据
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
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
	public List getDate() {
		return date;
	}
	public void setDate(List date) {
		this.date = date;
	}
}
