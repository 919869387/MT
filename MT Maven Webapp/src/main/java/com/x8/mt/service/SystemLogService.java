package com.x8.mt.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.stereotype.Service;

import com.x8.mt.common.PageParam;
import com.x8.mt.dao.ISystemLogDao;
import com.x8.mt.entity.SystemLog;

@Service
public class SystemLogService {
	@Resource
    private ISystemLogDao iSystemLogDao;
    
    
    /**
     * 
     * 作者:allen
     * 时间:2017年11月25日
     * 作用:插入一条日志记录
     */
    public int insert(SystemLog record) {
        return iSystemLogDao.insert(record);
    }

    /**
	 * 
	 * 作者:itcoder
	 * 时间:2017年11月28日
	 * 作用:得到systemlog表中总记录数
	 */
	public int getRowCount() {
		return iSystemLogDao.getRowCount();
	}

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年11月28日
	 * 作用:获取分页数据
	 */
	public PageParam getSystemlogListByPage(PageParam pageParam) {
		int currPage = pageParam.getCurrPage();
		int offset = (currPage-1)*pageParam.getPageSize();//计算出偏移量，起始位置
		int size = pageParam.getPageSize();//一页的数量
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("offset", offset);
		params.put("size", size);

		List<SystemLog> systemlogList = iSystemLogDao.selectByParams(params);
		

		List list = new ArrayList();
		for(SystemLog systemLog : systemlogList){
			JSONObject log = new JSONObject();
			log.put("ID", systemLog.getId());
			log.put("operationtype", systemLog.getOperationtype());
			log.put("operationdesc", systemLog.getOperationdesc());
			log.put("result", systemLog.getResult());
			log.put("startdatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(systemLog.getStartdatetime()));
			log.put("systemusername", systemLog.getSystemusername());
			
			list.add(log);
		}
			
		pageParam.setDate(list);

		return pageParam;

	}
	
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年3月22日
	 * 备注：根据条件筛选日志
	 */
	public PageParam selectLogListByCondition(SystemLog systemLog,
			PageParam pageParam) {
		//当前页码
		int currPage = pageParam.getCurrPage();
		//每一页的记录条数
		int pageSize = pageParam.getPageSize();
		//计算出偏移量，当前页码的起始位置
		int offset = (currPage - 1)*pageSize;
		
		//将分页参数封装到map中
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("offset", offset);
		map.put("size", pageSize);
		map.put("systemusername", systemLog.getSystemusername());
		map.put("operationtype", systemLog.getOperationtype());
		map.put("startdatetime", systemLog.getStartdatetime());
		map.put("enddatetime", systemLog.getEnddatetime());
		
		List<SystemLog> systemlogList = iSystemLogDao.selectLogByCondition(map);
		
		

		List list = new ArrayList();
		for(SystemLog systemLog2 : systemlogList){
			JSONObject log = new JSONObject();
			log.put("ID", systemLog2.getId());
			log.put("operationtype", systemLog2.getOperationtype());
			log.put("operationdesc", systemLog2.getOperationdesc());
			log.put("result", systemLog2.getResult());
			log.put("startdatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(systemLog2.getStartdatetime()));
			log.put("systemusername", systemLog2.getSystemusername());
			
			list.add(log);
		}
			
		pageParam.setDate(list);

		return pageParam;

	}

	
	/**
	 * 作者：yangyuan
	 * 时间：2018年3月22日
	 * 备注：根据条件筛选日志的总条数
	 */
	public int getRowCountByCondition(SystemLog systemLog) {
		return iSystemLogDao.getRowCountByCon(systemLog);
	}

	

}
