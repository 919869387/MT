package com.x8.mt.dao;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.MetadataTank;

@Repository
public interface IMetadataTankDao {
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年3月24日
	 * 作用:插入一条MetaDataTank记录
	 */
	int insertMetaDataTank(MetadataTank metadataTank);
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年3月25日
	 * 作用:删除与metadataId相关的所有MetaDataTank记录
	 */
	int daletetMetaDataTank(String metadataId);

}
