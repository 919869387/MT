package com.x8.mt.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.x8.mt.dao.IDatamapitemsDao;
import com.x8.mt.entity.Datamapitems;
import com.x8.mt.entity.Metamodel_relation;

@Service
public class DatamapitemsService {
	@Resource
	IDatamapitemsDao iDatamapitemsDao;

	public List<Datamapitems> getDatamap() {
		return iDatamapitemsDao.queryDatamap();
	}

	public List<Datamapitems> getDatamapitemsListByMaplayerId(Integer id) {
		return iDatamapitemsDao.getDatamapitemsListByMaplayerId(id);
	}

	public void insertDatamapitems(Datamapitems datamapitems) {
		iDatamapitemsDao.insertDatamapitems(datamapitems);
		
	}

	public Datamapitems getDatamapitemsIDByMetadataId(int id) {
		return iDatamapitemsDao.getDatamapitemsIDByMetadataId(id);
	}

	public int getDatamapitemsMetadataidById(int sourceid) {
		return iDatamapitemsDao.getDatamapitemsMetadataidById(sourceid);
	}

	
	
}
