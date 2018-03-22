package com.x8.mt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.x8.mt.entity.SystemLog;

@Repository
public interface ISystemLogDao {
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月27日
	 * 作用:插入一条日志记录
	 */
	int insert(SystemLog record);
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年11月28日 
	 * 作用:得到SystemLog表中总记录数
	 */
	int getRowCount();

	/**
	 * 
	 * 作者:itcoder 
	 * 时间:2017年11月28日 
	 * 作用:获取SystemLog分页数据
	 */
	List<SystemLog> selectByParams(Map<String, Object> params);
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年3月2日
	 * 备注：查询系统日志
	 */

	List<SystemLog> selectAllLog();
}
