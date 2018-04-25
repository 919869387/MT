package com.x8.mt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.x8.mt.dao.IMetamodel_relationDao;
import com.x8.mt.entity.Metamodel_relation;

@Service
public class Metamodel_relationService {

	@Autowired
	IMetamodel_relationDao iMetamodel_relationDao;

	/**
	 * 
	 * 作者:itcoder 时间:2018年4月10日 作用:获得所有的依赖关系
	 */
	public List<Metamodel_relation> getDependencyRelationByMetamodelid(int metamodelid) {
		return iMetamodel_relationDao.getDependencyRelationByMetamodelid(metamodelid);
	}

	public boolean insertMetamodel_relation(Metamodel_relation metamodel_relation) {
		boolean flag = false;

		int count = iMetamodel_relationDao.insertMetamodel_relation(metamodel_relation);
		if (count == 1) {
			flag = true;
		}

		return flag;
	}

	public boolean deleteMetamodel_relation(int id) {
		boolean flag = false;

		int count = iMetamodel_relationDao.deleteMetamodel_relation(id);
		if (count == 1) {
			flag = true;
		}

		return flag;
	}

}
