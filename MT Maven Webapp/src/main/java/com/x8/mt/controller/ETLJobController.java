package com.x8.mt.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.common.PageParam;
import com.x8.mt.entity.ETLJob;
import com.x8.mt.entity.Metadata;
import com.x8.mt.service.ETLJobService;
import com.x8.mt.service.MetaDataService;
import com.x8.mt.service.MetadataManagementService;
import com.x8.mt.service.SystemLogService;
import com.x8.mt.service.SystemuserService;

@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/ETLJob")
public class ETLJobController {

	@Resource
	SystemLogService systemlogService;
	@Resource
	ETLJobService etlJobService;
	@Resource
	MetaDataService metaDataService;
	@Resource
	SystemuserService systemuserService;

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月26日
	 * 作用:插入新的ETLJob任务
	 * 参数:target_table_id
	 * @throws Exception 
	 */
	@RequestMapping(value = "/insertETLJobBySelf",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="插入新的ETLJob任务")
	public JSONObject insertETLJobBySelf(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) throws Exception{
		JSONObject responsejson = new JSONObject();
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		//检查传参是否正确
		if(!(map.containsKey("target_table_id"))){
			responsejson.put("result", false);
			return responsejson;
		}
		
		Metadata metadata = new Metadata();
		metadata.setNAME(metaDataService.getMetadataById(Integer.parseInt((String)map.get("target_table_id"))).getNAME());
		metadata.setMETAMODELID(203);
		metadata.setCREATETIME(new Date());
		metadata.setUPDATETIME(new Date());
		metadata.setCHECKSTATUS("1");
		metadata.setVERSION(1);
		String attributes = "{\"jobname\":\""+metaDataService.getMetadataById(Integer.parseInt((String)map.get("target_table_id"))).getNAME()
				+ "\",\"jobdescribe\":\"1" 
				+"\",\"reposid\":\"" + "\"}";	
		metadata.setATTRIBUTES(attributes);
		if(metaDataService.insertMetadataWithNoCollectJob(metadata)){
			ETLJob etlJob = new ETLJob();
			etlJob.setMappingid(Integer.parseInt((String)map.get("target_table_id")));
			etlJob.setMetadata_id(metadata.getID());
			if(map.containsKey("description")){			
				etlJob.setDescription((String)map.get("description"));
			}
			etlJob.setCreate_date(new Date());
			etlJob.setCreateuserid(systemuserService.selectUser("admin").getId());
			etlJob.setType("LOCAL");
			etlJob.setStatus("NewCreate");	
			etlJob.setJobtype(Integer.parseInt((String)map.get("type")));
			responsejson.put("result", etlJobService.saveJob(etlJob) && etlJobService.insertETLJob(etlJob));
		}else{
			responsejson.put("result", false);
		}
		
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年4月25日
	 * 作用:插入新的ETLJob调度任务
	 * 参数:id
	 * @throws Exception 
	 */
	@RequestMapping(value = "/insertETLJobSchedule",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="插入新的ETLJob任务")
	public JSONObject insertETLJobSchedule(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) throws Exception{
		JSONObject responsejson = new JSONObject();
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		//检查传参是否正确
		if(!(map.containsKey("id") && map.containsKey("isRepeat") && map.containsKey("SchedulerType"))){
			responsejson.put("description", "参数不正确");
			responsejson.put("result", false);
			return responsejson;
		}
		
		int type = 0;
		boolean isRepeat = false;
		//int 
		try{
			type = Integer.parseInt(map.get("SchedulerType").toString());
			
		}catch(Exception e){
			responsejson.put("description", "运行出错");
			responsejson.put("result", false);
		}
		
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:Tomcroods
	 * 时间:2017年12月15日
	 * 作用:通过ETL元数据建立ETL任务
	 * 参数:id
	 */
	@RequestMapping(value = "/insertETLJobByMetaData",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="通过ETL元数据建立ETL任务")
	public JSONObject insertETLJobByMetaData(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		//检查传参是否正确
		if(!(map.containsKey("id"))){
			responsejson.put("result", false);
			return responsejson;
		}
		
		try{			
			Metadata metadata = metaDataService.getMetadataById(Integer.parseInt((String)map.get("id")));
			
			ETLJob etlJob = new ETLJob();
			etlJob.setMetadata_id(Integer.parseInt((String) map.get("id")));
			if (map.containsKey("description")) {
				etlJob.setDescription((String) map.get("description"));
			}
			etlJob.setCreate_date(new Date());
			etlJob.setCreateuserid(systemuserService.selectUser("admin").getId());
			etlJob.setType("EXTERNAL");
			etlJob.setStatus("NewCreate");
			etlJob.setJobtype(0);
			responsejson.put("result", etlJobService.insertETLJob(etlJob));	
		}catch(Exception e){
			e.printStackTrace();
			responsejson.put("result", false);
		}
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:Tomcroods
	 * 时间:2017年12月15日
	 * 作用:执行新的ETLJob
	 * 参数:target_table_id
	 * @throws IOException 
	 * @throws KettleException 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/executeETLJob",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="执行ETLJob")
	public JSONObject executeETLJob(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) throws UnsupportedEncodingException, KettleException, IOException{
		JSONObject responsejson = new JSONObject();
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		//检查传参是否正确
		if(!(map.containsKey("id"))){
			responsejson.put("result", false);
			return responsejson;
		}		
		int id=Integer.parseInt((String)map.get("id"));
		
		ETLJob etlJob = etlJobService.getETLJobById(id);
		boolean result = true;
		if(etlJob.getType().equals("LOCAL")){
			int target_table_id=etlJob.getMappingid();
			ETLJob job = new ETLJob();
			job.setMappingid(target_table_id);
			job.setJobtype(etlJob.getJobtype());
			result = etlJobService.executeLocalJob(job);
		}else{
			ETLJob job = new ETLJob();
			job.setMetadata_id(etlJob.getMetadata_id());
			result = etlJobService.excuteExternalJob(job);
		}

		responsejson.put("result", result);
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:Tomcroods
	 * 时间:2017年12月15日
	 * 作用:获取分页数据(数据按 createDate 倒叙排序，新添加的在最前面)
	 * 参数:page(页码)、pageSize(每页多少行)
	 */
	@RequestMapping(value = "/getETLJobListByPage",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="获取分页ETLJob数据")
	public JSONObject getETLJobListByPage(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("page")&&
				map.containsKey("pageSize")&&
					map.containsKey("type"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String currPageStr = map.get("page").toString();
		String pageSizeStr = map.get("pageSize").toString();
		String typeStr = map.get("type").toString();
		int currPage = 1;
		int pageSize = 1;
		int type = 0;
		try {
			currPage = Integer.parseInt(currPageStr);
			pageSize = Integer.parseInt(pageSizeStr);
			type = Integer.parseInt(typeStr);
		} catch (Exception e) {
		}
		//获取总记录数
		int rowCount = etlJobService.getRowCount(type);
		//构造分页数据
		PageParam pageParam = new PageParam();
		pageParam.setPageSize(pageSize);
		pageParam.setRowCount(rowCount);
		if(pageParam.getTotalPage()<currPage){
			currPage = pageParam.getTotalPage();
		}
		pageParam.setCurrPage(currPage);
		pageParam = etlJobService.getETLJobListByPage(pageParam);
		if(pageParam.getData()==null|| pageParam.getData().isEmpty()){
			responsejson.put("result", false);
			responsejson.put("message", "没有查询到任务");
			return responsejson;
		}else{
		JSONArray data = new JSONArray();
		@SuppressWarnings("unchecked")
		List<ETLJob> ETLJobList=pageParam.getData();
		for(ETLJob etlJob :ETLJobList){
			JSONObject etljobJson = new JSONObject();
			etljobJson.put("id", etlJob.getId());
			if(etlJob.getType().equals("LOCAL")){				
				if(etlJob.getStatus().equals("NewCreate")){
					etljobJson.put("target_table_id", etlJob.getId());
					etljobJson.put("target_table", metaDataService.getMetadataById(etlJob.getMappingid()).getNAME());
					etljobJson.put("description", etlJob.getDescription());
					etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
					etljobJson.put("recently_run_date","");
					etljobJson.put("status",etlJob.getStatus());
					etljobJson.put("log","该任务刚刚新建");					
					data.add(etljobJson);
				}else{			
					etljobJson.put("target_table_id", etlJob.getId());
					etljobJson.put("target_table",  metaDataService.getMetadataById(etlJob.getMappingid()).getNAME());
					etljobJson.put("description", etlJob.getDescription());
					etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
					etljobJson.put("recently_run_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getRecently_run_date()));
					etljobJson.put("status", etlJob.getStatus());
					etljobJson.put("log", etlJob.getLog());
					data.add(etljobJson);
				}
			}else{
				if(etlJob.getStatus().equals("NewCreate")){
					etljobJson.put("target_table_id", etlJob.getId());
					etljobJson.put("target_table",  metaDataService.getMetadataById(etlJob.getMetadata_id()).getNAME());
					etljobJson.put("description", etlJob.getDescription());
					etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
					etljobJson.put("recently_run_date","");
					etljobJson.put("status",etlJob.getStatus());
					etljobJson.put("log","该任务刚刚新建");					
					data.add(etljobJson);
				}else{
					etljobJson.put("target_table_id", etlJob.getId());
					etljobJson.put("target_table",  metaDataService.getMetadataById(etlJob.getMetadata_id()).getNAME());
					etljobJson.put("description", etlJob.getDescription());
					etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
					etljobJson.put("recently_run_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getRecently_run_date()));
					etljobJson.put("status", etlJob.getStatus());
					etljobJson.put("log", etlJob.getLog());
					data.add(etljobJson);
				}
			}
		}
		responsejson.put("result", true);
		responsejson.put("data", data);
		responsejson.put("count",ETLJobList.size());
		return responsejson;
		}
	}
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年4月25日
	 * 作用:获取能够添加调度的作业
	 */
	@RequestMapping(value = "/getETLJobToSchedule",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="根据id删除一个ETL任务")
	public JSONObject getETLJobToSchedule(HttpServletRequest request,HttpServletResponse response) {
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		List<ETLJob> etlJobs = etlJobService.getETLJobtoSchedule(0);
		JSONArray data = new JSONArray();
		for(ETLJob etlJob:etlJobs){
			JSONObject node = new JSONObject();
			node.put("target_table_id", etlJob.getId());	
			node.put("target_table", metaDataService.getMetadataById(etlJob.getMappingid()).getNAME());	
			data.add(node);
		}
		responsejson.put("result", true);
		responsejson.put("data",data);
		
		return responsejson;
	}
	
	
	/**
	 * 作者:Tomcroods
	 * 时间:2017年12月15日
	 * 作用:根据id删除一个ETL任务
	 * 参数:id
	 */
	@RequestMapping(value = "/deleteETLJob",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="根据id删除一个ETL任务")
	public JSONObject deleteETLJob(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!map.containsKey("id")){
			responsejson.put("result", false);
			responsejson.put("message", "参数不正确");
			return responsejson;
		}
		
		int jobId = 1;
		int type = 0;
		try{
			jobId = Integer.parseInt(map.get("id").toString());
			type = Integer.parseInt(map.get("type").toString());
		}catch(Exception e){
			responsejson.put("result", false);
			responsejson.put("message","转化不正确");
			return responsejson;
		}

		
		if(etlJobService.deleteETLJob(jobId,type)){
			responsejson.put("result", true);
			
		}else{
			responsejson.put("result", false);
			responsejson.put("message", "删除失败");
		}		
	
		return responsejson;
	}
	
	/**
	 * 作者:Tomcroods
	 * 时间:2017年12月15日
	 * 作用:根据id字符串（逗号隔开）删除多条数据
	 * 参数:ids(1,2,3这种)
	 */
	@RequestMapping(value = "/deleteETLJobs",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="根据id字符串（逗号隔开）删除多条数据")
	public JSONObject deleteETLJobs(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("ids")
				&& map.containsKey("type"))){
			responsejson.put("result", false);
			responsejson.put("message", "参数不正确");
			return responsejson;
		}
		String[]ids= map.get("ids").toString().split(",");
		int[] id = new int[ids.length];	
		for(int i=0;i<ids.length;i++){
			id[i]=Integer.parseInt(ids[i]);
		}
		String typeStr = map.get("type").toString();
		int type = Integer.parseInt(typeStr);
		if(etlJobService.deleteETLJobs(id,type)){
			responsejson.put("result", true);
			responsejson.put("message", "删除成功");
		}else{
			responsejson.put("result", false);
			responsejson.put("message", "删除失败");
		}		
	
		return responsejson;
	}
	
	/**
	 * 作者:Tomcroods
	 * 时间:2017年12月15日
	 * 作用:用于展示规则下拉框
	 * 参数:无
	 */
	@RequestMapping(value = "/queryTargetTableIdAndName",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="返回字段映射元数据（目标表）")
	public JSONObject queryTargetTableIdAndName(HttpServletRequest request,HttpServletResponse response) {
		JSONObject responsejson = new JSONObject();
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		List<String> target_table_ids= etlJobService.queryTargetTableIdAndName();
		if(target_table_ids==null || target_table_ids.isEmpty() || target_table_ids.get(0) == null){
			responsejson.put("result",false);
			responsejson.put("message","没有查询到任何字段映射元数据");
			
			return responsejson;
		}else{
		
		JSONArray data = new JSONArray();
		for(String target_table_id:target_table_ids){
			target_table_id = target_table_id.replace("\"","").replace("\"","");
			JSONObject node = new JSONObject();
			node.put("target_table_id", target_table_id);	
			node.put("target_table", metaDataService.getMetadataById(Integer.parseInt(target_table_id)).getNAME());	
			data.add(node);
		}
		responsejson.put("result", true);
		responsejson.put("data",data);
		return responsejson;
		}
		
	}
	/**
	 * 作者:Tomcroods
	 * 时间:2017年12月15日
	 * 作用:停止作业执行
	 * 参数:target_table_id
	 */
	@RequestMapping(value = "/stopETLJob",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="停止作业执行")
	public JSONObject stopETLJob(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();
		try{			
			int id = Integer.parseInt(map.get("target_table_id").toString());
			ETLJob job = etlJobService.getETLJobById(id);
			if(job.getType().equals("LOCAL")){
				if(job.getJobtype() == 0){					
					responsejson.put("result",etlJobService.stopETLJob(job.getMappingid()));
				}else{
					responsejson.put("result",etlJobService.stopETLSchedule(job.getMappingid() + "Schedule"));
				}				
			}else{
				responsejson.put("result",etlJobService.stopETLJob(job.getMetadata_id()));
			}
			
		}catch(Exception e){
			e.printStackTrace();
			responsejson.put("result",false);
		}
		return responsejson;
}
	
}	

