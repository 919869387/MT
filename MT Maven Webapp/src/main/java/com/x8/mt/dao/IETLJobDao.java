/**
 * 
 */
package com.x8.mt.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.x8.mt.entity.ETLJob;

/**
 * @author Tomcroods
 *
 */
@Repository
public interface IETLJobDao {
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:增加一条etl任务记录
	 */
	boolean insert(ETLJob job);
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:更新一条etl任务记录
	 */
	boolean update(ETLJob job);
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年4月25日 
	 * 作用:获取作业数量
	 */
	int getRowCount(@Param("description")String description);
	
	
	/**
	 * 
	 * 作者:谭凯旋 
	 * 时间:2018年3月26日 
	 * 作用:选取指定偏移量的一组etljob作业
	 */
	List<ETLJob> selectByParams(Map<String, Object> params);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月9日 
	 * 作用:根据描述筛选，并选取指定偏移量的一组etljob作业
	 */
	List<ETLJob> selectByDescription(@Param("description")String description,@Param("status")String status,@Param("offset") int offset,@Param("size") int size);
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年4月25日 
	 * 作用:获取能够添加调度的作业
	 */
	List<ETLJob> getETLJobtoSchedule(String type);
	
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:删除一条etl作业或者调度记录
	 */
	int deleteETLJob(@Param ("id")int id,@Param("type")int type);
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:删除一组etl任务记录
	 */
	int deleteETLJobs(int[] id);
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:获取已经定义好的字段映射元数据(未使用)
	 */
	List<String> queryTargetTableIdAndName();
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:获取已经定义好的字段映射元数据(所有的)
	 */
	List<String> queryTargetTableId();
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:根据id获取一条etl任务记录
	 */
	ETLJob getETLJobById(int id);
}
