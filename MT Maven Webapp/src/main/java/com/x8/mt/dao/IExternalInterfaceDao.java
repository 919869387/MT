package com.x8.mt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.Metadata;

@Repository
public interface IExternalInterfaceDao {

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月10日
	 * 获取通信报文元数据的参数元数据
	 */
	List<Metadata> getCompositionRelatedmetadata(String metadataid);
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月15日
	 * 作用:获取通信报文元数据种类
	 */
	List<String> getProtocolType();

}
