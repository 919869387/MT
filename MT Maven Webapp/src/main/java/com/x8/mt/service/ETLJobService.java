/**
 * 
 */
package com.x8.mt.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.springframework.stereotype.Service;


import com.x8.mt.common.LogUtil;
import com.x8.mt.common.PageParam;
import com.x8.mt.common.TransformMetadata;
import com.x8.mt.dao.ICollectJobDao;
import com.x8.mt.dao.IETLJobDao;
import com.x8.mt.dao.IConnectinfoDao;
import com.x8.mt.dao.IDatasource_connectinfoDao;
import com.x8.mt.dao.IMetaDataDao;
import com.x8.mt.entity.Datasource_connectinfo;
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
	
	@SuppressWarnings("rawtypes")
	static HashMap theJob = new HashMap();
	
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
	
	@SuppressWarnings("unchecked")
	public boolean executeJob(ETLJob etlJob) throws UnsupportedEncodingException, KettleException, IOException{
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
		System.out.println(fieldStream);
		System.out.println(fieldDatabase);
		job.setFieldSteram(fieldStream);
		job.setFieldDatabase(fieldDatabase);
		TransDataService tramnsDataService= new TransDataService();
		Job job2 = tramnsDataService.generateJobMeta(job);
		theJob.put(etlJob.getMappingid(),job2);
		
		job2.start();		
		job2.waitUntilFinished();
		System.out.println("进程死亡");
		System.out.println(job2.getStatus());
		theJob.remove(etlJob.getMappingid());
		etlJob.setStatus(job2.getStatus());
		etlJob.setRecently_run_date(new Date());
        LogUtil logUtil = new LogUtil(job2,job2.getJobMeta());
        String log=logUtil.getJobLog(job2);
//        System.out.println(log);
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
//	public void showETLJob(){
//		
//		List<ETLJob> jobLists = new List<ETLJob>();
//		
//	}

	public boolean stopETLJob(int index){
		if(theJob.containsKey(index)){
		((Job)theJob.get(index)).stopAll();
		return true;
		}		
		else return false;
		
	}
	
	public int getRowCount(){
		return eTLJobDao.getRowCount();
	}
	
	public PageParam getETLJobListByPage(PageParam pageParam) {
		int currPage = pageParam.getCurrPage();
		if(currPage>0){
		int offset = (currPage-1)*pageParam.getPageSize();//计算出偏移量，起始位置
		int size = pageParam.getPageSize();//一页的数量
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("offset", offset);
		params.put("size", size);

		List<ETLJob> ETLJobList = eTLJobDao.selectByParams(params);
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
	 * 作用:删除一条etl任务记录
	 */
	public boolean deleteETLJob(int id){
		try{
			return eTLJobDao.deleteETLJob(id)>0?true:false;
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
	public boolean deleteETLJobs(int[] id){
		try{
			return eTLJobDao.deleteETLJobs(id)>0?true:false;
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
	
	public ETLJobParam getEtlJobParam(){
		return null;
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
	
}
