package com.x8.mt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.Metamodel_hierarchy;

@Repository
public interface IMetamodel_hierarchyDao {
	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年11月16日 
	 * 作用:得到一条Metamodel_hierarchy记录
	 */
	Metamodel_hierarchy getMetamodel_hierarchy(int id);
	 
	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年11月16日 
	 * 作用:插入一条Metamodel_hierarchy记录
	 */
	int insert(Metamodel_hierarchy metamodel_hierarchy);

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月2日 
	 * 作用:根据元模型包id得到元模型包结构
	 */
	List<Metamodel_hierarchy> getSonMetamodel_hierarchy(int id);

	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年11月17日 
	 * 作用:删除元模型(已经废弃)
	 */
	int delete_METAMODEL(int id);

	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年11月17日 
	 * 作用:删除包(已经废弃)
	 */
	int delete_PACKAGE(int id);

	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年11月17日 
	 * 作用:删除包时，先判断是否含有元模型
	 */
	int packageIshavaSon(int id);

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月5日 
	 * 作用:根据入参id删除元模型或元模型包结构
	 */
	boolean deleteMetamodel_hierarchy(int id);

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月6日 
	 * 作用:根据入参id更新元模型
	 */
	boolean updateMetamodel_hierarchy(Metamodel_hierarchy metamodel_hierarchy);

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月12日
	 * 作用:获得所有的元模型包
	 */
	List<Metamodel_hierarchy> getAllMetamodel_package();

	/**
	 * 
	 * @param id
	 * @return 获取指定id的parentid
	 */
	boolean getSelfMetamodel_hierarchyParentid(int id);
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * @return 查询所有能被悬挂的元模型
	 */
	List<Metamodel_hierarchy> getMetaModelByMountNode();
	
	/**
	 * 作者:allen
	 * 时间:2018年3月14日
	 * 作用:查询所有组合关系的下层元模型
	 */
	List<Metamodel_hierarchy> getCOMPOSITIONMetamodel(String metamodelId);
}
