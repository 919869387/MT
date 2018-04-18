package com.x8.mt.dao;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.Metadata;
/**
 * 作者： Administrator
 * 时间：2018年3月15日
 * 作用：
 */
@Repository
public interface IMetadataManagementDao {
    /**
	 * 
	 * 作者:allen 
	 * 时间:2017年3月16日 
	 * 作用:查询元数据视图第一层
	 */
    List<Map<Object, Object>> getMetadataViewFirstLevel(int viewID);
    
    /**
	 * 
	 * 作者:allen 
	 * 时间:2017年3月16日 
	 * 作用:查询儿子元数据
	 */
    List<Map<Object, Object>> getSonMetadata(String metadata_id);
    
    /**
	 * 
	 * 作者:allen 
	 * 时间:2017年3月19日 
	 * 作用:根据表元数据id，得到字段元数据
	 */
	List<Metadata> getFieldMetadata(int tableMetadataId);
	
    /**
	 * 
	 * 作者:allen 
	 * 时间:2017年3月23日 
	 * 作用:获取某一个元数据
	 */
	Metadata getMetadata(String metadataId);

	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年3月23日 
	 * 作用:向metadata表插入一条数据
	 */
	int insertMetadata(Metadata metadata);
	
	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年3月23日 
	 * 作用:得到元模型的私有数据类型
	 */
	List<String> getAttributesField(String metaModelId);

	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年3月23日 
	 * 作用:更新元数据
	 */
	int updateMetadata(Metadata metadata);
	
	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年3月23日 
	 * 作用:删除一条元数据
	 */
	int daleteMetadata(String metadataId);
	
	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年3月23日 
	 * 作用:更新元数据的Attributes属性
	 */
	int updateMetadatAttributes(Metadata metadata);

	boolean updataMetadataCheckstatus(Metadata metadata);
	
	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年4月18日 
	 * 作用:获取所有数据库元数据
	 */
	List<Map<String, Object>> getDatabaseMetadataList(String metaModelId);

	/**
	 * 
	 * 作者:allen 
	 * 时间:2017年4月18日 
	 * 作用:获取某一类元数据
	 */
	List<Map<String, Object>> getMetadataList(Map<String, Object> map);
}
