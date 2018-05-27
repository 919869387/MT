package com.x8.mt.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.x8.mt.entity.Metadata;

@Repository
public interface IMetaDataDao {
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据id删除一条Metadata记录
	 */
	int deleteMetadataById(int id);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年4月11日
	 * 作用:根据采集任务id删除Metadata记录
	 */
	int deleteMetadataByCollectJobId(int id);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:更新一条Metadata记录
	 */
	int  updateMetadataDescribeById(Metadata Metadata);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:插入一条Metadata记录
	 */
	int insertMetadata(Metadata Metadata);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:插入一条Metadata记录
	 */
	int insertMetadataWithoutCollecjob(Metadata Metadata);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据id获取一条Metadata记录
	 */
	Metadata getMetadataById(int id);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年4月18日
	 * 作用:根据元模型id获取一组Metadata记录
	 */
	List<Metadata> getMetadataByMetaModelId(int id);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据采集任务id获取一组Metadata记录
	 */
	List<Metadata> getMetadataByCollectJobById(int id);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据Map获取一组Metadata记录
	 */
	List<Metadata> 	getMetadataByMap(@Param("key")String key,@Param("value")String value);

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年5月2日
	 * 作用:根据元模型id获取一组Metadata记录
	 */
	List<Metadata> getMetadataByMetaModelIdNoNull(int id);
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月10日
	 * 作用:根据元模型id获取一组Metadata记录
	 */
	List<Metadata> getMetadataByMetaModelIdAndNoNull(int id);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月10日
	 * 作用:根据数据库名称查找元数据
	 */
	List<Metadata> getMetadataByDatabaseName(String name);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月19日
	 * 作用:根据数据库名称查找元数据
	 */
	List<Metadata> getAvailableMountMetadata();
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月19日
	 * 作用:查找文件元数据所挂载的数据库元数据
	 */
	Metadata getFileSourceMetadata(int id);
}
