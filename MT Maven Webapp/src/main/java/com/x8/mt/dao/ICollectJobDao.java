package com.x8.mt.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.x8.mt.entity.CollectJob;

@Repository
public interface ICollectJobDao {

	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月12日 
	 * 作用:增加一条采集任务记录
	 */
	boolean insert(CollectJob collectJob);


	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月14日 
	 * 作用:根据时间查找采集任务 记录
	 */
	List<CollectJob> getCollectJobByDate(@Param("startdate")Date startdate,@Param("enddate")Date enddate);

	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月14日 
	 * 作用:根据id获取采集任务
	 */
	CollectJob getCollectJobById(int id);
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月14日 
	 * 作用:根据connectinfoid获取采集任务
	 */
	CollectJob getCollectJobByConnectinfoId(int id);
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年1月22日 
	 * 作用:删除一条CollectJob记录
	 */
	int delete(int id);
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月28日 
	 * 作用:查询是否存在该名称
	 */
	CollectJob isExistName(String name);
}
