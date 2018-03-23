package com.x8.mt.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import java.util.List;
import com.x8.mt.dao.IMetaDataDao;
import com.x8.mt.entity.Metadata;

@Service
public class MetaDataService {
	@Resource
	IMetaDataDao iMetadataDao;
	
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2018年3月12日
	 * 作用:根据id删除元数据
	 */
	public boolean deleteMetadataById(int id) {
		try{			
			return iMetadataDao.deleteMetadataById(id) == 1 ? true : false;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2018年3月14日
	 * 作用:根据metaModelId查找元数据
	 */
	public List<Metadata> getMetadataByMetaModelId(int id) {
		try{			
			return iMetadataDao.getMetadataByMetaModelId(id);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2018年3月14日
	 * 作用:根据id更新元数据描述字段
	 */
	public boolean updateMetadataDescribeById(Metadata Metadata) {
		try{
			return iMetadataDao.updateMetadataDescribeById(Metadata) == 1 ? true : false;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2018年3月14日
	 * 作用:根据id查找元数据
	 */
	public Metadata getMetadataById(int id){
		try{
			return iMetadataDao.getMetadataById(id);
		}catch(Exception e){
			return null;
		}
		
	}
}
