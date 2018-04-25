package com.x8.mt.test;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.PageParam;
import com.x8.mt.dao.IDatasource_connectinfoDao;
import com.x8.mt.dao.IETLJobDao;
import com.x8.mt.entity.CollectJob;
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.entity.ETLJob;
import com.x8.mt.entity.ETLJobParam;
import com.x8.mt.service.CollectJobService;
import com.x8.mt.service.ETLJobService;
import com.x8.mt.service.KettleMetadataCollectService;
import com.x8.mt.service.TransDataService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
/**
 * 作者： GodDispose
 * 时间：2018年4月19日
 * 作用：
 */
public class ETLJobTest {
	@Autowired
	ETLJobService etlJobService;
	
	@Autowired
	TransDataService transDataService;
	
	@Autowired
	KettleMetadataCollectService kettleMetadataCollectService;
	
	@Autowired
	IETLJobDao ietlJobDao;

	@Test
	public void getJobs() throws Exception{
//		Datasource_connectinfo datasource_connectinfo = new Datasource_connectinfo();
//		datasource_connectinfo.setUrl("192.168.1.132");
//		datasource_connectinfo.setPort("3306");
//		datasource_connectinfo.setUsername("root");
//		datasource_connectinfo.setPassword("root");
//		datasource_connectinfo.setDatabasename("sys");
//		datasource_connectinfo.setDatabasetype(GlobalMethodAndParams.databasetype_MYSQL);
//		
//		Datasource_connectinfo datasource_connectinfo1 = new Datasource_connectinfo();
//		datasource_connectinfo1.setUrl("192.168.1.132");
//		datasource_connectinfo1.setPort("3306");
//		datasource_connectinfo1.setUsername("root");
//		datasource_connectinfo1.setPassword("root");
//		datasource_connectinfo1.setDatabasename("test");
//		datasource_connectinfo1.setDatabasetype(GlobalMethodAndParams.databasetype_MYSQL);
//		
//		ETLJobParam job = new ETLJobParam();
//		job.setSource(datasource_connectinfo);
//		job.setTarget(datasource_connectinfo1);
//		job.setSource_table("city");
//		job.setTarget_table("city");
//		job.setFieldSteram("id,name,countrycode,district,population,datefd");
//		job.setFieldDatabase("id,name,countrycode,district,population,dated");
//		
		
		ETLJob etljob = new ETLJob();
		etljob.setMappingid(82853);
	//	transDataService.saveJob(job);
		etlJobService.executeLocalJob(etljob);
	}
	
	@Test
	public void excuteExternalJob(){
		ETLJob job = new ETLJob();
		job.setMetadata_id(82836);
		etlJobService.excuteExternalJob(job);
	}
	
	@Test
	public void stopJob() throws KettleException{
		
//		Datasource_connectinfo datasource_connectinfo1 = new Datasource_connectinfo();
//		datasource_connectinfo1.setUrl("192.168.1.111");
//		datasource_connectinfo1.setPort("3306");
//		datasource_connectinfo1.setUsername("root");
//		datasource_connectinfo1.setPassword("root");
//		datasource_connectinfo1.setDatabasename("kettle_first");
//		datasource_connectinfo1.setDatabasetype(GlobalMethodAndParams.databasetype_MYSQL);
//		
//		KettleDatabaseRepository kettleDatabaseRepository = kettleMetadataCollectService.connectKettleDatabaseRepository(datasource_connectinfo1);
//		ObjectId objectId = new LongObjectId(new Long(0));
//	       RepositoryDirectoryInterface directory = kettleDatabaseRepository.findDirectory(objectId);
//
//	    
//	       
//		JobMeta jobMeta = kettleDatabaseRepository.loadJob("作业 1",directory,null,null);
//		Job job = new Job(kettleDatabaseRepository,jobMeta);
//		job.stopAll();
		Job job = (Job)etlJobService.theJob.get(82835);
		System.out.println(job.getName());
		
	}
	
	@Test
	public void getETLJob(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("jobtype", 0);
		params.put("offset", 0);
		params.put("size", 4);
		if(ietlJobDao.selectByParams(params) != null){
			
			System.out.println(ietlJobDao.selectByParams(params).size());
		}else{
			System.out.println(false);
		}
	}
	
	@Test
	public void testExcuteSchedule() throws Exception{
		ETLJob etljob = new ETLJob();
		etljob.setMappingid(82853);
		etljob.setJobtype(1);
		//etlJobService.saveSchedule("city");
		etlJobService.executeLocalJob(etljob);
	}
	

}
