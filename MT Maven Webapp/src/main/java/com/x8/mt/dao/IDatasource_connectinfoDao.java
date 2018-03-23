package com.x8.mt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.Datasource_connectinfo;

@Repository
public interface IDatasource_connectinfoDao {

	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年11月15日 
	 * 作用:更新一条Datasource_connectinfo记录
	 */
	int update(Datasource_connectinfo datasource_connectinfo);

	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年11月13日 
	 * 作用:插入一条Datasource_connectinfo记录
	 */
	int insert(Datasource_connectinfo datasource_connectinfo);

	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年11月14日 
	 * 作用:删除一条Datasource_connectinfo记录
	 */
	int delete(int id);

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月21日
	 * 作用:根据parentid得到所有相应Datasource_connectinfo
	 */
	Datasource_connectinfo getDatasource_connectinfoListByparentid(int parentid);
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月22日
	 * 作用:根据id得到一条Datasource_connectinfo记录
	 */
	Datasource_connectinfo getDatasource_connectinfoByid(int id);
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月17日
	 * 作用:得到所有Datasource_connectinfo
	 */
	List<Datasource_connectinfo> getDatasource_connectinfo();

	

}
