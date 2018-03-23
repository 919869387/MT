package com.x8.mt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.Metamodel_datatype;

@Repository
public interface IMetamodel_datatypeDao {
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月16日 
	 * 作用:根据metamodelid查询元模型数据项
	 */
	List<Metamodel_datatype> getMetamodel_datatypeByMetaModelId(int metamodelid);

	/**
	 * 
	 * 作者:allen 
	 * 时间:2018年3月23日 
	 * 作用:通过元模型id,得到元模型的所有数据项,包括公共与特殊的
	 */
	List<Metamodel_datatype> getMetamodelDatatype_PublicAndAttributes(Map<Object,Object> map);
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月6日 
	 * 作用:添加元模型数据项
	 */
	boolean addMetamodel_datatype(Metamodel_datatype metamodel_datatype);

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月6日 
	 * 作用:修改元模型数据项
	 */
	boolean modifyMetamodel_datatype(Metamodel_datatype metamodel_datatype);
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月6日 
	 * 作用:根据id删除元模型数据项
	 */
	boolean deleteMetamodel_datatype(int id);
	
}
