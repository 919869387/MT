package com.x8.mt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.x8.mt.common.PageParam;
import com.x8.mt.dao.IDispatchDao;
import com.x8.mt.entity.Dispatch;
import com.x8.mt.entity.ETLJob;

@Service
public class DispatchService {
	@Resource
	IDispatchDao dispatchDAO;
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:添加一个etl调度
	 */
	public boolean addDispatch(Dispatch dispatch) {
		return dispatchDAO.addDispatch(dispatch);
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:更新一个etl调度
	 */
	public boolean updateDispatch(Dispatch dispatch) {
		return dispatchDAO.updateDispatch(dispatch);
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:查询所有的etl调度
	 */
	public List<Dispatch> queryAll() {
		return dispatchDAO.queryAll();
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:根据id查询etl调度
	 */
	public Dispatch queryByDispatchId(int i) {
		return dispatchDAO.queryByDispatchId(i);
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:删除一个etl调度
	 */
	public boolean deleteDispatch(int i) {
		return dispatchDAO.deleteDispatch(i);
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:删除一组etl调度
	 */
	public boolean deleteDispatchs(int[] id){
		try{
			return dispatchDAO.deleteDispatchs(id) > 0?true:false;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月7日 
	 * 作用:获取etl调度数量
	 */
	public int getRowCount(){
		return dispatchDAO.getRowCount();
	}
	
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月7日 
	 * 作用:获取etl调度数量
	 */
	public PageParam getETLScheduleListByPage(PageParam pageParam) {
		int currPage = pageParam.getCurrPage();
		if(currPage>0){
		int offset = (currPage-1)*pageParam.getPageSize();//计算出偏移量，起始位置
		int size = pageParam.getPageSize();//一页的数量
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("jobtype", 0);
		params.put("offset", offset);
		params.put("size", size);

		List<Dispatch> scheduleList = dispatchDAO.selectByParams(params);
		pageParam.setDate(scheduleList);

		return pageParam;
		}else{
			return pageParam;
		}
	}

}
