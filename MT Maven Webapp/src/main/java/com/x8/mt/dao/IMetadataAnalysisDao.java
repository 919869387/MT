package com.x8.mt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.Metadata;

@Repository
public interface IMetadataAnalysisDao {

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月3日
	 * 作用:通过JSON查元数据
	 */
	List<Metadata> getMetadataByJson(Map<String, String> paramMap);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月4日
	 * 作用:通过relatedmetadataid,找到关系为COMPOSITION的元数据
	 */
	Metadata getCompositionMetadata(String relatedmetadataid);
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月4日
	 * 作用:根据id获取一条Metadata记录
	 */
	Metadata getMetadataById(String id);

}
