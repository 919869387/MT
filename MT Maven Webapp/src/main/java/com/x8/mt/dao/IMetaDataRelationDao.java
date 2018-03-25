package com.x8.mt.dao;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.MetaDataRelation;

@Repository
public interface IMetaDataRelationDao {

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:插入一条MetaDataRelation记录
	 */
	int insertMetaDataRelation(MetaDataRelation metaDataRelation);
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年3月25日
	 * 作用:删除一条MetaDataRelation记录
	 */
	int daleteMetaDataRelation(String metadataId);
}
