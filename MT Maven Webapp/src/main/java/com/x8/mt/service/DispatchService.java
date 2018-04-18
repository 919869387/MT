package com.x8.mt.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.x8.mt.dao.IDispatchDao;
import com.x8.mt.entity.Dispatch;

@Service
public class DispatchService {
	@Resource
	IDispatchDao dispatchDAO;
	
	
	public boolean addDispatch(Dispatch dispatch) {
		if (dispatchDAO.addDispatch(dispatch)) {
			return true;
		}
		return false;
	}
	
	public boolean updateDispatch(Dispatch dispatch) {
		return dispatchDAO.updateDispatch(dispatch);
	}
	
	public List<Dispatch> queryAll() {
		return dispatchDAO.queryAll();
	}
	
	public Dispatch queryByDispatchId(int i) {
		return dispatchDAO.queryByDispatchId(i);
		}
	public boolean deleteDispatch(int i) {
		return dispatchDAO.deleteDispatch(i);
	}

}
