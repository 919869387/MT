package com.x8.mt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.Metamodel_hierarchy;
import com.x8.mt.entity.Metamodel_relation;

@Repository
public interface IMetamodel_relationDao {
	/**
	 * 
	 * 作者:itcoder	 
	 * 时间:2018年4月10日 
	 * 作用:得到依赖关系列表
	 */
	List<Metamodel_relation> getDependencyRelationByMetamodelid(int metamodelid);
	
	/**
	 * 
	 * 作者:itcoder	 
	 * 时间:2018年4月10日 
	 * 作用:插入元模型关系
	 */
	int insertMetamodel_relation(Metamodel_relation metamodel_relation);

	/**
	 * 
	 * 作者:itcoder	 
	 * 时间:2018年4月10日 
	 * 作用:删除元模型关系
	 */
	int deleteMetamodel_relation(int id);
	 
	
}
