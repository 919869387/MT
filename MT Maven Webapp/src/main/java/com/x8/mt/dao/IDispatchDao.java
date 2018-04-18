package com.x8.mt.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.x8.mt.entity.CollectJob;
import com.x8.mt.entity.Dispatch;

@Repository
public interface IDispatchDao {

	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月12日 
	 * 作用:增加一条采集任务记录
	 */
	boolean addDispatch(Dispatch dispatch);
	
	boolean deleteDispatch(int dispatchId);
	
	boolean updateDispatch(Dispatch dispatch);
	
	List<Dispatch> queryAll();
	
	Dispatch queryByDispatchId(int dispatchId);
}
