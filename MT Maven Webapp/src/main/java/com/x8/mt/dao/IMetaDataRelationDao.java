package com.x8.mt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.MetaDataRelation;
import com.x8.mt.entity.Metadata;

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
	 * 作用:根据metadataId,找到COMPOSITION关系的儿子元数据id
	 */
	List<String> getSonMetadataID(String metadataId);
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年5月22日
	 * 作用:根据metadataId,找到具有关系的儿子元数据id
	 */
	List<String> getChildrenMetadataID(String metadataId);

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年4月23日
	 * 作用:根据metadataId,找到DEPENCY关系的元数据id
	 */
	List<Integer> getDependencyRelatedMetadataidList(int sourceMetadataid);

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年5月2日
	 * 作用:根据relatedmetadataid,找到metadataid
	 */
	int getMetadataidByRelatedmetadataid(int i);
}
