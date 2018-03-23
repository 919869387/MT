package com.x8.mt.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.x8.mt.dao.IMetamodel_datatypeDao;
import com.x8.mt.entity.Metamodel_datatype;

@Service
public class Metamodel_datatypeService {
	@Resource
	IMetamodel_datatypeDao iMetamodel_datatypeDao;
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月16日
	 * 作用:根据metamodelid查询元模型数据项
	 */
	public List<Metamodel_datatype> getMetamodel_datatypeByMetaModelId(int id) {
		try{			
			return iMetamodel_datatypeDao.getMetamodel_datatypeByMetaModelId(id);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月6日
	 * 作用:添加元模型数据项
	 */
	public boolean addMetamodel_datatype(Metamodel_datatype metamodel_datatype) {
		return iMetamodel_datatypeDao.addMetamodel_datatype(metamodel_datatype);
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月6日
	 * 作用:修改元模型数据项
	 */
	public boolean modifyMetamodel_datatype(Metamodel_datatype metamodel_datatype) {
		return iMetamodel_datatypeDao.modifyMetamodel_datatype(metamodel_datatype);
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月6日
	 * 作用:根据id删除元模型数据项
	 */
	public boolean deleteMetamodel_datatype(int id) {
		return iMetamodel_datatypeDao.deleteMetamodel_datatype(id);
	}
	

	
}
