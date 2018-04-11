package com.x8.mt.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import java.util.List;

import com.x8.mt.dao.IMetaDataDao;
import com.x8.mt.dao.IMetadataManagementDao;
import com.x8.mt.entity.Metadata;

@Service
public class MetaDataService {
	@Resource
	IMetaDataDao iMetadataDao;
	@Resource
	IMetadataManagementDao imetadataManagementDao;	
	
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
			return null;
		}		
	}
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2018年3月14日
	 * 作用:根据id查找元数据
	 */
	public boolean insertMetadata(Metadata metadata){
		try{
			return iMetadataDao.insertMetadata(metadata) > 0 ? true : false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2018年3月22日
	 * 作用:根据id查找元数据
	 */
	public boolean insertMetadataWithNoCollectJob(Metadata metadata){
		try{
			return imetadataManagementDao.insertMetadata(metadata) > 0 ? true : false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
	}

	public boolean updateMetadataCheckstatus(Metadata metadata) {
		return imetadataManagementDao.updataMetadataCheckstatus(metadata);
	}
	
	
}
