package com.x8.mt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.Connectinfo;

@Repository
public interface IConnectinfoDao {

	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:208年3月14日 
	 * 作用:更新一条Connectinfo记录
	 */
	int updateConnectinfoNameOrDescriptionById(Connectinfo connectinfo);

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日 
	 * 作用:插入一条Connectinfo记录
	 */
	int insert(Connectinfo connectinfo);

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日 
	 * 作用:删除一条Connectinfo记录
	 */
	int deleteConnectInfoById(int id);

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月3日
	 * 作用:根据parentid得到所有相应Connectinfo
	 */
	List<Connectinfo> getConnectinfoListByparentid(int parentid);
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年3月14日
	 * 作用:根据id得到一条Connectinfo记录
	 */
	Connectinfo getConnectinfoByid(int id);
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年3月14日
	 * 作用:根据id得到一条Connectinfo记录
	 */
	Connectinfo getConnectinfoByName(String name);
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:获取全部记录
	 */
	List<Connectinfo> getConnectinfo();

}
