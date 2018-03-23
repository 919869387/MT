package com.x8.mt.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.x8.mt.dao.ICollectJobDao;
import com.x8.mt.entity.CollectJob;

@Service
public class CollectJobService {
	@Resource
	ICollectJobDao iCollectJobDao;
	
	 /** 
	 * 作者:GodDipose
	 * 时间:2018年3月12日
	 * 作用:插入一条采集任务记录
	 */
	public boolean insertCollectJob(CollectJob collectJob){
		try{
			return iCollectJobDao.insert(collectJob);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2018年3月12日
	 * 作用:根据id删除采集任务
	 */
	public boolean deleteById(int id){
		try{
			return  iCollectJobDao.delete(id) > 0? false:true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2018年3月12日
	 * 作用:查找采集任务记录
	 */
	public List<CollectJob> getCollectJob(Date createDate,Date endDate){
		try{
			return  iCollectJobDao.getCollectJobByDate(createDate, endDate);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2018年3月14日
	 * 作用:根据connectinfoid查找采集任务
	 */
	public CollectJob getCollectJobByConnectinfoId(int id){
		try{
			return  iCollectJobDao.getCollectJobByConnectinfoId(id);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}
