package com.x8.mt.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.x8.mt.dao.IMetadataViewNodeDao;
import com.x8.mt.entity.MetadataViewNode;

@Service
public class MetadataViewNodeService {
	@Resource
	IMetadataViewNodeDao iMetadataViewNodeDao;
	
	/**
	 * allen
	 * @param id
	 * @return
	 */
	public List<MetadataViewNode> getMetadataViewNode(String id){
		return iMetadataViewNodeDao.getMetadataViewNode(id);
	}
}
