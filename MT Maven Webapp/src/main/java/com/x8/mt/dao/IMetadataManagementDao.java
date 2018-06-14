package com.x8.mt.dao;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.Metadata;
import com.x8.mt.entity.MetadataTank;
import com.x8.mt.entity.Metamodel_datatype;
import com.x8.mt.entity.Metamodel_hierarchy;
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
	 * 时间:2018年5月17日 
	 * 作用:查找分页元数据
	 */
	List<Metadata> searchMetadataPage(Map<String,Object> param);
	
	/**
	 * 
	 * 作者:allen 
	 * 时间:2018年4月19日 
	 * 作用:查找元数据总记录数
	 */
	int searchMetadataCount(String key);
    
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

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:寻找可依赖的元数据
	 */
	List<Map<String, Object>> getDependMetadata(String metadataidStr);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:添加元数据依赖
	 */
	int addMetadataDepend(Map<String, Object> map);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:展示元数据依赖关系
	 */
	List<Map<String, Object>> showMetadataDepend(String metadataidStr);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:删除元数据依赖关系
	 */
	int deleteMetadataDepend(String relationidStr);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月25日
	 * 作用:得到历史版本元数据的公共属性
	 */
	List<MetadataTank> getHistoryMetadataCommonInfo(String metadataidStr);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月25日
	 * 作用:得到历史版本元数据的私有属性
	 */
	String getHistoryMetadataPrivateInfo(String tankidStr);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月25日
	 * 作用:得到元模型私有属性
	 */
	List<Metamodel_datatype> getMetamodelPrivateInfo(String metamodelidStr);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月15日
	 * 作用:请求第一层试图节点
	 */
	List<Map<String, Object>> getViewNode(String viewid);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月15日
	 * 作用:请求元数据的第一层节点
	 */
	List<Map<String, Object>> getMetadataFirstNode(String id);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月15日
	 * 作用:请求元数据的其他层次节点
	 */
	List<Map<String, Object>> getMetadataOtherNode(String metadataid);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月16日
	 * 作用:根据元数据id得到相应元模型
	 */
	Metamodel_hierarchy getMetamodelByMetadataid(
			String metadataid);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月17日
	 * 作用:根据父元数据id得到儿子元数据
	 */
	List<Metadata> getSonMetadata(String metadataid);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年6月6日
	 * 作用:查询所有元数据记录个数
	 */
	int searchTotalMetadataCount();

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年6月13日
	 * 作用:协议元数据中参数总个数
	 */
	int protocolParamMetadataCount(String fatherProtocolMetadataId);

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年6月13日
	 * 作用:分页查询协议元数据中参数
	 */
	List<Metadata> protocolParamMetadataPage(Map<String, Object> param);
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年6月14日
	 * 作用:得到协议参数组下面的参数元数据
	 */
	List<Metadata> getProtocolParamByParamArray(String metadataId);
}
