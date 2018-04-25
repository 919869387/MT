package com.x8.mt.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.x8.mt.dao.IMetaDataRelationDao;
import com.x8.mt.entity.MetaDataRelation;

@Service
public class MetaDataRelationService {

	@Resource
	IMetaDataRelationDao iMetaDataRelationDao;
	
	public boolean 	insertMetaDataRelation(MetaDataRelation metaDataRelation){
		try{
			return iMetaDataRelationDao.insertMetaDataRelation(metaDataRelation) > 0 ? true : false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年4月23日
	 * 作用:根据metadataId,找到COMPOSITION关系的儿子元数据id
	 */
	public List<String> getMetadata_relationByMetadataid(int id) {
		return iMetaDataRelationDao.getSonMetadataID(String.valueOf(id));
	}

	public List<Integer> getDependencyRelatedMetadataidList(int sourceMetadataid) {
		return iMetaDataRelationDao.getDependencyRelatedMetadataidList(sourceMetadataid);
	}
}
