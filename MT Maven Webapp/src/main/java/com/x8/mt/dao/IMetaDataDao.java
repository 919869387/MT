package com.x8.mt.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
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
	 * 作用:根据id获取一条Metadata记录
	 */
	Metadata getMetadataById(int id);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据id获取一条Metadata记录
	 */
	List<Metadata> getMetadataByMetaModelId(int id);
}
