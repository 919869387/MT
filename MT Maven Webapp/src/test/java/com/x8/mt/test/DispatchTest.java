package com.x8.mt.test;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.x8.mt.service.DispatchService;
import com.x8.mt.service.ETLJobService;
import com.x8.mt.service.MetaDataService;

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
	@Autowired
	ETLJobService eTLJobService;
	@Autowired
	MetaDataService metaDataService;


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
		Map dataMap = new HashedMap();
		dataMap.put("schedulerType", 2);
		String date = "2018-05-08T12:32:25.000Z";
		date = date.replace("Z", " UTC");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
		Date d = format.parse(date);
		String[] daytime = d.toString().split(" ");
		String[] time = daytime[3].split(":");
		int hour = Integer.parseInt(time[0]);
		int minutes = Integer.parseInt(time[1]);
		dataMap.put("hour", hour);
		dataMap.put("minutes", minutes);
		dataMap.put("description", "haha");
		
		eTLJobService.saveSchedule(metaDataService.getMetadataById(83250).getNAME(),dataMap);
	}
}
