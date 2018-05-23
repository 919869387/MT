package com.x8.mt.dao;

import java.util.List;

import com.x8.mt.entity.Datamapitems;

public interface IDatamapitemsDao {

	List<Datamapitems> queryDatamap();

	List<Datamapitems> getDatamapitemsListByMaplayerId(Integer id);

	void insertDatamapitems(Datamapitems datamapitems);

	List<Datamapitems> getDatamapitemsIDByMetadataId(int id);

	int getDatamapitemsMetadataidById(int sourceid);

	int isExist(int id);

	int updateDatamapitems(Datamapitems datamapitems);

	boolean deleteAllDatamapitems();
	
	
}
