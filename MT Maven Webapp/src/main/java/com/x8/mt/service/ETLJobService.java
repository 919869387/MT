///**
// * 
// */
//package com.x8.mt.service;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.pentaho.di.core.exception.KettleException;
//import org.pentaho.di.job.Job;
//import org.springframework.stereotype.Service;
//
//
//
//
//
////import com.x8.mt.common.LogUtil;
//import com.x8.mt.common.PageParam;
//import com.x8.mt.common.TransformMetadata;
//import com.x8.mt.dao.IETLJobDao;
//import com.x8.mt.dao.IConnectinfoDao;
//import com.x8.mt.dao.IDatasource_connectinfoDao;
//import com.x8.mt.dao.IMetaDataDao;
//import com.x8.mt.entity.Datasource_connectinfo;
//import com.x8.mt.entity.ETLJob;
//import com.x8.mt.entity.ETLJobParam;
//import com.x8.mt.entity.Metadata;
///**
// * @author Tomcroods
// *
// */
//@Service
//public class ETLJobService {
//
//
//	@Resource
//	IETLJobDao eTLJobDao;
//	@Resource
//	IMetaDataDao iMetaDataDao;
//
//	@Resource
//	IDatasource_connectinfoDao iDatasource_connectinfoDao;
//	@SuppressWarnings("rawtypes")
//	static HashMap theJob = new HashMap();
//	public boolean insertETLJob(int target_table_id,String target_table, String description){
//		ETLJob etlJob= new ETLJob();
//		etlJob.setTarget_table_id(target_table_id);
//		etlJob.setTarget_table(target_table);
//		etlJob.setDescription(description);
//		etlJob.setCreate_date(new Date());
//		boolean result =eTLJobDao.insert(etlJob);
//		return result;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public boolean executeJob(ETLJob etlJob) throws UnsupportedEncodingException, KettleException, IOException{
//		ETLJobParam job= new ETLJobParam();
//		List<Metadata> metadatas = iMetaDataDao.getMetadataByMap("$.targettableid", String.valueOf(etlJob.getTarget_table_id()));
//		Map<String, Object> metadataMap = TransformMetadata.transformMetadataToMap(metadatas.get(0));
//		int srctableid = (Integer) metadataMap.get("sourcetableid");
//		String source_table = iMetaDataDao.getMetadataById(srctableid).getNAME();
//		String fieldStream=iMetadata_tablefieldDao.getMetadata_tablefieldByid(metadata_tablefieldmapping.get(0).getSrcfieldid()).getName();
//		String fieldDatabase=iMetadata_tablefieldDao.getMetadata_tablefieldByid(metadata_tablefieldmapping.get(0).getTargetfieldid()).getName();
//		int length=metadata_tablefieldmapping.size();
//		for(int i=1; i<length;i++){
//			fieldStream+=","+iMetadata_tablefieldDao.getMetadata_tablefieldByid(metadata_tablefieldmapping.get(i).getSrcfieldid()).getName();
//			fieldDatabase+=","+iMetadata_tablefieldDao.getMetadata_tablefieldByid(metadata_tablefieldmapping.get(i).getTargetfieldid()).getName();
//		}
//		Datasource_connectinfo datasource_connectinfo =iDatasource_connectinfoDao.getDatasource_connectinfoListByparentid(iMetadata_relationtableDao.getMetadata_relationtableByid(srctableid).getDatasource_connectinfoid());
//		Datasource_connectinfo datatarget_connectinfo =iDatasource_connectinfoDao.getDatasource_connectinfoListByparentid(iMetadata_relationtableDao.getMetadata_relationtableByid(etlJob.getTarget_table_id()).getDatasource_connectinfoid());
//		job.setSource(datasource_connectinfo);
//		job.setTarget(datatarget_connectinfo);
//		job.setSource_table(source_table);
//		job.setTarget_table(etlJob.getTarget_table());
//		System.out.println(fieldStream);
//		System.out.println(fieldDatabase);
//		job.setFieldSteram(fieldStream);
//		job.setFieldDatabase(fieldDatabase);
//		TransDataService tramnsDataService= new TransDataService();
//		Job job2 = tramnsDataService.generateJobMeta(job);
//		theJob.put(etlJob.getTarget_table_id(),job2);
//		
//		job2.start();		
//		job2.waitUntilFinished();
//		System.out.println("进程死亡");
//		System.out.println(job2.getStatus());
//		theJob.remove(etlJob.getTarget_table_id());
//		etlJob.setStatus(job2.getStatus());
//		etlJob.setRecently_run_date(new Date());
//        LogUtil logUtil = new LogUtil(job2,job2.getJobMeta());
//        String log=logUtil.getJobLog(job2);
////        System.out.println(log);
//        if(etlJob.getStatus().equals("Finished")){
//        	if(log.indexOf("完成处理")>-1){
//        	log = log.substring(log.indexOf("完成处理"));
//            log = log.substring(log.indexOf("W="));
//            log = log.substring(2,log.indexOf(", U"));            
//            log = "此次抽取了 "+log+" 条数据";
//            etlJob.setLog(log);
//        	}else{
//        		etlJob.setLog("此次没有抽取到数据！");
//        		 eTLJobDao.update(etlJob);
//        	        return false;
//        	}
//        }else if(etlJob.getStatus().equals("Stopped")){
//        	etlJob.setLog("作业已停止");
//        	 eTLJobDao.update(etlJob);
//             return false;
//        }
//        
//        eTLJobDao.update(etlJob);
//       
//        return true;
//	}
////	public void showETLJob(){
////		
////		List<ETLJob> jobLists = new List<ETLJob>();
////		
////	}
//	public boolean stopETLJob(int index){
//		if(theJob.containsKey(index)){
//		((Job)theJob.get(index)).stopAll();
//		return true;
//		}		
//		else return false;
//		
//	}
//	
//	public int getRowCount(){
//		return eTLJobDao.getRowCount();
//	}
//	
//	public PageParam getETLJobListByPage(PageParam pageParam) {
//		int currPage = pageParam.getCurrPage();
//		if(currPage>0){
//		int offset = (currPage-1)*pageParam.getPageSize();//计算出偏移量，起始位置
//		int size = pageParam.getPageSize();//一页的数量
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("offset", offset);
//		params.put("size", size);
//
//		List<ETLJob> ETLJobList = eTLJobDao.selectByParams(params);
//		pageParam.setDate(ETLJobList);
//
//		return pageParam;
//		}else{
//			return pageParam;
//		}
//	}
//	
//	public boolean deleteETLJob(int id){
//		try{
//			return eTLJobDao.deleteETLJob(id)>0?true:false;
//		}catch(Exception e){
//			return false;
//		}
//	}
//	public boolean deleteETLJobs(int[] id){
//		try{
//			return eTLJobDao.deleteETLJobs(id)>0?true:false;
//		}catch(Exception e){
//			return false;
//		}
//	}
//	public List<Integer> queryTargetTableIdAndName(){
//		
//		return eTLJobDao.queryTargetTableIdAndName();
//	}
//	
//	public List<Integer> queryTargetTableId(){
//		
//		return eTLJobDao.queryTargetTableId();
//	}
//	
//	public ETLJobParam getEtlJobParam(){
//		return null;
//	}
//	
//	
//}
