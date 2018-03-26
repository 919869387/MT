/**
 * 
 */
package com.x8.mt.dao;

import java.util.List;
import java.util.Map;

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
	
	int getRowCount();
	
	List<ETLJob> selectByParams(Map<String, Object> params);
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:删除一条etl任务记录
	 */
	int deleteETLJob(int id);
	
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
