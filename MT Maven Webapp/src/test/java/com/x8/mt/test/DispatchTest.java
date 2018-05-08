package com.x8.mt.test;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.special.JobEntrySpecial;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.entity.Dispatch;
import com.x8.mt.entity.Metadata;
import com.x8.mt.service.DispatchService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
/**
 * 作者： GodDispose
 * 时间：2018年4月11日
 * 作用：
 */
public class DispatchTest {
	@Autowired
	DispatchService dispatchService;


//	@Test	
//	public void queryAll(){
//		List<Dispatch> lists = dispatchService.queryAll();
//		for(Dispatch dis : lists){
//			System.out.println(dis.getUser());
//		}
//	}
//	
//	@Test
//	public void addDispatch(){
//		Dispatch dispatch = new Dispatch();
//		dispatch.setIntervalTime(1);
//		dispatch.setLastTime("2017-03-24 22:25:24");
//		dispatch.setTime("2017-03-24 22:25:24");
//		dispatch.setUser("testes");
//		dispatchService.addDispatch(dispatch);
//		
//	}
//	
//	@Test
//	public void deleteDispatch(){
//		dispatchService.deleteDispatch(1);
//	}
//	
//	@Test
//	public void updateDispatch(){
//		Dispatch dispatch = new Dispatch();
//		dispatch.setIntervalTime(1);
//		dispatch.setLastTime("2017-03-24 22:25:24");
//		dispatch.setTime("2017-03-24 22:25:24");
//		dispatch.setUser("testes111");
//		dispatchService.addDispatch(dispatch);
//		dispatchService.updateDispatch(dispatch);
//	}
//	
//	@Test
//	public void queryByDispatchId(){
//		Dispatch dis = dispatchService.queryByDispatchId(2);
//		System.out.println(dis.getUser());
//	}
	
	@Test
	public void testExternalJob(){
//		KettleEnvironment.init();
//		Metadata metadata = iMetaDataDao.getMetadataById(etlJob.getMetadata_id());
//		Datasource_connectinfo res = datasource_connectinfoService.getDatasource_connectinfoListByparentid(
//				collectJobService.getCollectJobById(metadata.getCOLLECTJOBID()).getConnectinfoId());			
//		
//		KettleDatabaseRepository kettleDatabaseRepository = kettleMetadataCollectService.connectKettleDatabaseRepository(res);
//		ObjectId objectId = new LongObjectId(new Long(0));
//	       RepositoryDirectoryInterface directory = kettleDatabaseRepository.findDirectory(objectId);
//
//		JobMeta jobMeta = kettleDatabaseRepository.loadJob(metadata.getNAME(),directory,null,null);
//		
//		Job job = new Job(kettleDatabaseRepository,jobMeta);
//		
//		JobEntryCopy start = jobMeta.findStart();
//		JobEntrySpecial entry = (JobEntrySpecial) start.getEntry();
//		entry.get
	}
	
	@Test
	public void testDate() throws Exception{
		String date = "2016-08-15T16:00:00.000Z";
		date = date.replace("Z", " UTC");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
		Date d = format.parse(date);
		System.out.println(date);
		System.out.println(d.toString());
		System.out.println(d);
	}
}
