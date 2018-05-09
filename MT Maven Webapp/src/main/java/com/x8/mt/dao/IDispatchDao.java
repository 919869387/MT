package com.x8.mt.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sun.org.glassfish.gmbal.ParameterNames;
import com.x8.mt.entity.CollectJob;
import com.x8.mt.entity.Dispatch;
import com.x8.mt.entity.ETLJob;

@Repository
public interface IDispatchDao {

	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:增加一条调度记录
	 */
	boolean addDispatch(Dispatch dispatch);
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:删除一条调度记录
	 */
	boolean deleteDispatch(int dispatchId);
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:删除一组调度记录
	 */
	int deleteDispatchs(int[] id);
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:更新一条调度记录
	 */
	boolean updateDispatch(Dispatch dispatch);
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:查询所有调度记录
	 */
	List<Dispatch> queryAll();
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:根据id查找一条调度记录
	 */
	Dispatch queryByDispatchId(int dispatchId);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月7日 
	 * 作用:获取调度数量
	 */
	int getRowCount();
	
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月9日 
	 * 作用:根据描述筛选，并选取指定偏移量的一组etl调度
	 */
	List<Dispatch> selectByDescription(@Param("description")String description,@Param("offset") int offset,@Param("size") int size);
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月7日 
	 * 作用:选取指定偏移量的一组etl调度
	 */
	List<Dispatch> selectByParams(Map<String, Object> params);
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月7日 
	 * 作用:判断作业名称和调度名称是否已经存在
	 */
	List<Dispatch> isRepeatByJobName(String jobname);
}
