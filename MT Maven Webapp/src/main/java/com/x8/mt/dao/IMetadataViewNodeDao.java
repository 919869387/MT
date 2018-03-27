package com.x8.mt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.MetadataViewNode;

@Repository
public interface IMetadataViewNodeDao {
	
	/**
	 * allen
	 * @param id
	 * @return 
	 */
	List<MetadataViewNode> getMetadataViewNode (String id);

}
