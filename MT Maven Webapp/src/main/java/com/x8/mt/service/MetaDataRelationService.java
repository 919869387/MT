package com.x8.mt.service;

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
}
