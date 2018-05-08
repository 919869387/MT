/**
 * 
 */
package com.x8.mt.service;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.ObjectLocationSpecificationMethod;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobHopMeta;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.special.JobEntrySpecial;
import org.pentaho.di.job.entries.trans.JobEntryTrans;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.springframework.stereotype.Service;
















import com.x8.mt.common.LogUtil;
import com.x8.mt.common.PageParam;
import com.x8.mt.common.TransformMetadata;
import com.x8.mt.dao.ICollectJobDao;
import com.x8.mt.dao.IDispatchDao;
import com.x8.mt.dao.IETLJobDao;
import com.x8.mt.dao.IConnectinfoDao;
import com.x8.mt.dao.IDatasource_connectinfoDao;
import com.x8.mt.dao.IMetaDataDao;
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.entity.Dispatch;
import com.x8.mt.entity.ETLJob;
import com.x8.mt.entity.ETLJobParam;
import com.x8.mt.entity.Metadata;
/**
 * @author Tomcroods
 *
 */
@Service
public class ETLJobService {
	@Resource
	IETLJobDao eTLJobDao;
	@Resource
	IMetaDataDao iMetaDataDao;
	@Resource
	ICollectJobDao iCollectJobDao;
	@Resource
	IConnectinfoDao iConnectinfoDao;
	@Resource
	IDatasource_connectinfoDao iDatasource_connectinfoDao;
	@Resource
	IDispatchDao iDispatchDao;
	
	@Resource
	KettleMetadataCollectService kettleMetadataCollectService;
	@Resource
	Datasource_connectinfoService datasource_connectinfoService;
	@Resource
	CollectJobService collectJobService;
	
	//正在执行的作业队列
	public static Map<Integer,Job> theJob = new HashMap<Integer,Job>();
	
	//正在执行的调度队列
	public static Map<String,Job> theSchedule = new HashMap<String,Job>();

	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年4月25日 
	 * 作用:停止执行作业
	 */
	public boolean stopETLJob(int index){
		if(theJob.containsKey(index)){
		((Job)theJob.get(index)).stopAll();
		return true;
		}		
		else return false;		
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年4月25日 
	 * 作用:停止执行调度
	 */
	public boolean stopETLSchedule(int id){
		if(theSchedule.containsKey(id + " Schedule")){
			((Job)theSchedule.get(id + " Schedule")).stopAll();
			System.out.println("停止");
			Dispatch dispatch = new Dispatch();
			dispatch.setDispatchid(id);
			dispatch.setStatus(3);
			iDispatchDao.updateDispatch(dispatch);
		
			return true;
		}
		return false;		
	}
	
	public int getRowCount(int type){
		return eTLJobDao.getRowCount(type);
	}
	
	public PageParam getETLJobListByPage(PageParam pageParam) {
		int currPage = pageParam.getCurrPage();
		if(currPage>0){
		int offset = (currPage-1)*pageParam.getPageSize();//计算出偏移量，起始位置
		int size = pageParam.getPageSize();//一页的数量
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("jobtype", 0);
		params.put("offset", offset);
		params.put("size", size);

		List<ETLJob> ETLJobList = eTLJobDao.selectByParams(params);
		System.out.println(ETLJobList.size());
		pageParam.setDate(ETLJobList);

		return pageParam;
		}else{
			return pageParam;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:增加一条etl任务记录
	 */
	public boolean insertETLJob(ETLJob etlJob){
		try{
			return eTLJobDao.insert(etlJob);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年4月19日 
	 * 作用:执行本地job作业或者调度
	 */
	public boolean executeLocalJob(ETLJob etlJob) throws UnsupportedEncodingException, KettleException, IOException{
		KettleEnvironment.init();
		String targetTableName = iMetaDataDao.getMetadataById(etlJob.getMappingid()).getNAME() + (etlJob.getJobtype() == 1 ? " Schedule" : "");
		JobMeta jobMeta = new JobMeta(targetTableName+ ".kjb", null);
		Job job = new Job(null,jobMeta);		

		theJob.put(etlJob.getMappingid(),job);
		job.start();		
		job.waitUntilFinished();
			
		theJob.remove(etlJob.getMappingid());
		etlJob.setStatus(job.getStatus());
		etlJob.setRecently_run_date(new Date());
		System.out.println(job.getStatus());
	    LogUtil logUtil = new LogUtil(job,job.getJobMeta());
	    String log=logUtil.getJobLog(job);
	    if(etlJob.getStatus().equals("Finished")){
	    	if(log.indexOf("完成处理")>-1){
	    		log = log.substring(log.indexOf("完成处理"));
	    		log = log.substring(log.indexOf("W="));
	    		log = log.substring(2,log.indexOf(", U"));            
	    		log = "此次抽取了 "+log+" 条数据";
	    		etlJob.setLog(log);
	    	}else{
	    		etlJob.setLog("此次没有抽取到数据！");
	    		eTLJobDao.update(etlJob);
	    		return false;
	    	}
	    }else if(etlJob.getStatus().equals("Stopped")){
	    	etlJob.setLog("作业已停止");
	    	eTLJobDao.update(etlJob);
	    	return false;
	    }

	    eTLJobDao.update(etlJob);

       
        return true;
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年4月19日 
	 * 作用:执行外部job作业
	 */
	public boolean excuteExternalJob(ETLJob etlJob){
		try{
			KettleEnvironment.init();
			Metadata metadata = iMetaDataDao.getMetadataById(etlJob.getMetadata_id());
			Datasource_connectinfo res = datasource_connectinfoService.getDatasource_connectinfoListByparentid(
					collectJobService.getCollectJobById(metadata.getCOLLECTJOBID()).getConnectinfoId());			
			
			KettleDatabaseRepository kettleDatabaseRepository = kettleMetadataCollectService.connectKettleDatabaseRepository(res);
			ObjectId objectId = new LongObjectId(new Long(0));
		       RepositoryDirectoryInterface directory = kettleDatabaseRepository.findDirectory(objectId);

			JobMeta jobMeta = kettleDatabaseRepository.loadJob(metadata.getNAME(),directory,null,null);
			
			Job job = new Job(kettleDatabaseRepository,jobMeta);
			
			theJob.put(etlJob.getMetadata_id(),job);
			etlJob.setStatus("Excuting");
			etlJob.setRecently_run_date(new Date());
			eTLJobDao.update(etlJob);				
			
			job.start();
			job.waitUntilFinished();
			kettleDatabaseRepository.disconnect();
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月4日 
	 * 作用:执行本地调度
	 */
	public boolean excuteSchedule(Dispatch dispatch) throws Exception{
		KettleEnvironment.init();
		if(dispatch.getType().equals("LOCAL")){
			JobMeta jobMeta = new JobMeta(dispatch.getName()+ ".kjb", null);
			Job job = new Job(null,jobMeta);		
			
			theSchedule.put(dispatch.getDispatchid() + " Schedule",job);
			dispatch.setStatus(2);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dispatch.setRecenttime(sdf.parse(sdf.format(new Date())));
			iDispatchDao.updateDispatch(dispatch);
			
			job.start();				
			job.waitUntilFinished();
			

		}else if(dispatch.getType().equals("EXTERNAL")){
			Metadata metadata = iMetaDataDao.getMetadataById(Integer.parseInt(dispatch.getJobname()));
			Datasource_connectinfo res = datasource_connectinfoService.getDatasource_connectinfoListByparentid(
					collectJobService.getCollectJobById(metadata.getCOLLECTJOBID()).getConnectinfoId());			
			
			KettleDatabaseRepository kettleDatabaseRepository = kettleMetadataCollectService.connectKettleDatabaseRepository(res);
			ObjectId objectId = new LongObjectId(new Long(0));
		       RepositoryDirectoryInterface directory = kettleDatabaseRepository.findDirectory(objectId);

			JobMeta jobMeta = kettleDatabaseRepository.loadJob(metadata.getNAME(),directory,null,null);
			
			Job job = new Job(kettleDatabaseRepository,jobMeta);
			
			theSchedule.put(dispatch.getDispatchid() + " Schedule",job);
			job.start();
			job.waitUntilFinished();
			
			dispatch.setStatus(2);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dispatch.setRecenttime(sdf.parse(sdf.format(new Date())));
			iDispatchDao.updateDispatch(dispatch);	
		}

       
        return true;
	}
	
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:删除一条etl任务记录
	 */
	public boolean deleteETLJob(int id,int type){
		try{
			return eTLJobDao.deleteETLJob(id,type)>0?true:false;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:删除一组etl任务记录
	 */
	public boolean deleteETLJobs(int[] id,int type){
		try{
			return eTLJobDao.deleteETLJobs(id,type)>0?true:false;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:获取已经定义好的字段映射元数据(未使用)
	 */
	public List<String> queryTargetTableIdAndName(){		
		return eTLJobDao.queryTargetTableIdAndName();
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:获取已经定义好的字段映射元数据(所有的)
	 */
	public List<String> queryTargetTableId(){
		
		return eTLJobDao.queryTargetTableId();
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年3月26日 
	 * 作用:根据id获取一条etl任务记录
	 */
	public ETLJob getETLJobById(int id){
		try{
			return eTLJobDao.getETLJobById(id);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年4月19日 
	 * 作用:根据id存储一组ktr和kjb文件
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public boolean saveJob(ETLJob etlJob) throws Exception{
		ETLJobParam job= new ETLJobParam();
		List<Metadata> metadatas = iMetaDataDao.getMetadataByMap("$.targettableid", String.valueOf(etlJob.getMappingid()));
		Map<String, Object> metadataMap = TransformMetadata.transformMetadataToMap(metadatas.get(0));
		int srctableid = Integer.parseInt((String)metadataMap.get("sourcetableid"));
		String source_table = iMetaDataDao.getMetadataById(srctableid).getNAME();
		String fieldStream= iMetaDataDao.getMetadataById(Integer.parseInt((String)metadataMap.get("sourcefieldid"))).getNAME();
		String fieldDatabase= iMetaDataDao.getMetadataById(Integer.parseInt((String) metadataMap.get("targetfieldid"))).getNAME();
		int length=metadatas.size();
		for(int i=1; i<length;i++){
			fieldStream+=","+iMetaDataDao.getMetadataById(Integer.parseInt((String) TransformMetadata.transformMetadataToMap(metadatas.get(i)).get("sourcefieldid"))).getNAME();
			fieldDatabase+=","+iMetaDataDao.getMetadataById(Integer.parseInt((String)TransformMetadata.transformMetadataToMap(metadatas.get(i)).get("sourcefieldid"))).getNAME();	
		}
		Datasource_connectinfo datasource_connectinfo =iDatasource_connectinfoDao.getDatasource_connectinfoListByparentid(iCollectJobDao.getCollectJobById(iMetaDataDao.getMetadataById(srctableid).getCOLLECTJOBID()).getConnectinfoId());
		Datasource_connectinfo datatarget_connectinfo =iDatasource_connectinfoDao.getDatasource_connectinfoListByparentid(iCollectJobDao.getCollectJobById(iMetaDataDao.getMetadataById(etlJob.getMappingid()).getCOLLECTJOBID()).getConnectinfoId());
		job.setSource(datasource_connectinfo);
		job.setTarget(datatarget_connectinfo);
		job.setSource_table(source_table);
		job.setTarget_table(iMetaDataDao.getMetadataById(etlJob.getMappingid()).getNAME());

		job.setFieldSteram(fieldStream);
		job.setFieldDatabase(fieldDatabase);
		TransDataService transDataService= new TransDataService();
		
		transDataService.saveJob(job);
       
        return true;
	}
	
	/*
	 * 作者:GodDispose
	 * 时间:2018年4月25日
	 * 作用:生成调度
	 */
	public boolean saveSchedule(String name,Map map) throws Exception{
		try{			
			KettleEnvironment.init();
			JobMeta jobMeta = new JobMeta();
			jobMeta.setName("jobMeta");
			int x=50,y =50;
			
			JobEntrySpecial jobEntrySpecial = new JobEntrySpecial( "START", true, false );
			jobEntrySpecial.setRepeat(true);
			jobEntrySpecial.setSchedulerType((int)map.get("schedulerType"));	//0 no,1 interval,2 day,3 week,4 month
			//以下两个方法只会在设置为INTERVAL的时候用
			//jobEntrySpecial.setIntervalSeconds((int)map.get("intervalSeconds"));
			//jobEntrySpecial.setIntervalMinutes((int)map.get("intervalMinutes"));
			
			//选用2或者以上会用到一下方法
			jobEntrySpecial.setHour((int)map.get("hour"));
			jobEntrySpecial.setMinutes((int)map.get("minutes"));
			
			if(Integer.parseInt(map.get("schedulerType").toString()) == 3){				
				jobEntrySpecial.setWeekDay((int)map.get("week"));
			}
			//jobEntrySpecial.setDayOfMonth((int)map.get("week"));
			
		    JobEntryCopy jobEntry = new JobEntryCopy();
		    jobEntry.setObjectId( null );
		    jobEntry.setEntry( jobEntrySpecial );
		    jobEntry.setLocation( 50, 50 );
		    jobEntry.setDrawn( false );
		    jobEntry.setDescription( BaseMessages.getString( JobMeta.class, "JobMeta.StartJobEntry.Description" ) );
		    
			JobEntryCopy startCopy = jobEntry;
			startCopy.setLocation(x, y);
			startCopy.setDrawn();
			jobMeta.addJobEntry(startCopy);
			JobEntryCopy lastCopy = startCopy;
			
			JobEntryTrans jobEntryTrans = new JobEntryTrans();
			jobEntryTrans.setSpecificationMethod(ObjectLocationSpecificationMethod.FILENAME);
			jobEntryTrans.setFileName(name+ ".ktr");
			
			JobEntryCopy transCopy = new JobEntryCopy(jobEntryTrans);
			transCopy.setName("Execute" + name+ ".ktr");
			x+=100;
			transCopy.setLocation(x, y);
			transCopy.setDrawn();
			jobMeta.addJobEntry(transCopy);
			JobHopMeta transHop = new JobHopMeta(lastCopy,transCopy);
			jobMeta.addJobHop(transHop);
			lastCopy = transCopy;
			jobMeta.setObjectId(new LongObjectId(new Long(1)));
			
			String jobName = name + " Schedule"+".kjb";
			File file = new File(jobName);
			System.out.println(jobName);
			if (file.exists() && file.isFile()) {
				if (file.delete()) {}
			}
			
			String xml = XMLHandler.getXMLHeader() + jobMeta.getXML();
			DataOutputStream dos = new DataOutputStream(KettleVFS.getOutputStream(jobName,true));
			dos.write(xml.getBytes(Const.XML_ENCODING));
			dos.close();
			
			KettleEnvironment.shutdown();
			
			Dispatch dispatch = new Dispatch();
			dispatch.setName(name + " Schedule");
			dispatch.setDescription(map.get("description").toString());
			dispatch.setJobname(name);
			dispatch.setStatus(1);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dispatch.setCreatetime(sdf.parse(sdf.format(new Date())));			
			dispatch.setRuninterval((int)map.get("schedulerType") == 2 ? "每周执行" : "每天执行");
			dispatch.setType("LOCAL");
			
			iDispatchDao.addDispatch(dispatch);
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return  false;
		}
		
	}

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年4月25日 
	 * 作用:获取能够添加调度的作业
	 */
	public List<ETLJob> getETLJobtoSchedule(String type){
		try{
			return eTLJobDao.getETLJobtoSchedule(type);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月7日 
	 * 作用:判断外部创建是单次作业还是调度作业
	 * 返回值类型Map
	 * 如果是单次作业，只返回flag:false
	 * 如果是调度作业，返回flag:true以及一些调度信息
	 */
	public Map<String, Object> judgeJobOrSchedule(int id){
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			
			KettleEnvironment.init();
			Metadata metadata = iMetaDataDao.getMetadataById(id);
			Datasource_connectinfo res = datasource_connectinfoService.getDatasource_connectinfoListByparentid(
					collectJobService.getCollectJobById(metadata.getCOLLECTJOBID()).getConnectinfoId());			
			
			KettleDatabaseRepository kettleDatabaseRepository = kettleMetadataCollectService.connectKettleDatabaseRepository(res);
			ObjectId objectId = new LongObjectId(new Long(0));
		       RepositoryDirectoryInterface directory = kettleDatabaseRepository.findDirectory(objectId);

			JobMeta jobMeta = kettleDatabaseRepository.loadJob(metadata.getNAME(),directory,null,null);			
				
			JobEntryCopy startEntry = jobMeta.getStart();
			JobEntrySpecial start = (JobEntrySpecial) startEntry.getEntry();
			boolean flag = start.isRepeat();
			map.put("flag", flag);
			if(flag){
				map.put("SchedulerType", start.getSchedulerType());
				map.put("hour", start.getHour());
				map.put("minutes", start.getMinutes());
				map.put("week", start.getWeekDay());
			}
			
			kettleDatabaseRepository.disconnect();
			
			return map;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年4月25日 
	 * 作用:获取能够添加调度的作业
	 */
	public boolean isRepeatByIdORName(int id){
		try{
			String jobname = iMetaDataDao.getMetadataById(eTLJobDao.getETLJobById(id).getMappingid()).getNAME();
			List<Dispatch> dispatchs = iDispatchDao.isRepeatByJobName(jobname);
			if(dispatchs.size() > 0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
	}

	
}
