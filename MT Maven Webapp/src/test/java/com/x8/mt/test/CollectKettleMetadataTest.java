package com.x8.mt.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.x8.mt.dao.IDatasource_connectinfoDao;
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.service.KettleMetadataCollectService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
/**
 * 作者： GodDispose
 * 时间：2018年4月11日
 * 作用：
 */
public class CollectKettleMetadataTest {
	@Autowired
	KettleMetadataCollectService kettleMetadataCollectService;
	
	@Autowired
	IDatasource_connectinfoDao datasource_connectinfoDao;

	@Test
	public void getJobs() throws KettleException{
		Datasource_connectinfo datasource_connectinfo = datasource_connectinfoDao.getDatasource_connectinfoByid(1);
		//int collectKettleJob = kettleMetadataCollectService.collectKettleJob(datasource_connectinfo);
	}
}
