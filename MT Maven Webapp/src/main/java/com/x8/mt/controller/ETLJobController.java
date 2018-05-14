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

import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
import com.x8.mt.entity.Dispatch;
import com.x8.mt.entity.ETLJob;
import com.x8.mt.entity.Metadata;
import com.x8.mt.service.DispatchService;
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
	@Resource
	DispatchService dispatchService;
	

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
		
		Subject subject = SecurityUtils.getSubject();  
		Session session = subject.getSession();
		String creater = session.getAttribute("username").toString();
		
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
			etlJob.setCreateuserid(systemuserService.selectUser(creater).getId());
			etlJob.setType("LOCAL");
			etlJob.setStatus("NewCreate");	
			etlJob.setJobtype(0);
			responsejson.put("result", etlJobService.saveJob(etlJob) && etlJobService.insertETLJob(etlJob));
		}else{
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
		
		//获取当前的用户名
		Subject subject = SecurityUtils.getSubject();  
		Session session = subject.getSession();
		String creater = session.getAttribute("username").toString();		
		
		int id = 1;
		
		try{
			id = Integer.parseInt((String) map.get("id"));
			ETLJob etlJob = new ETLJob();
			etlJob.setMetadata_id(id);
			if (map.containsKey("description")) {
				etlJob.setDescription((String) map.get("description"));
			}
			etlJob.setCreate_date(new Date());
			etlJob.setCreateuserid(systemuserService.selectUser(creater).getId());
			etlJob.setType("EXTERNAL");
			etlJob.setStatus("NewCreate");
			
			if((boolean)etlJobService.judgeJobOrSchedule(id).get("flag")){
				etlJob.setJobtype(1);	
			}else{	
				etlJob.setJobtype(0);	
			}	
			responsejson.put("result", etlJobService.insertETLJob(etlJob));	
	
		}catch(Exception e){
			e.printStackTrace();
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
	@Log(operationType="ETLJob",operationDesc="插入新的ETLJob调度任务")
	public JSONObject insertETLJobSchedule(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) throws Exception{
		JSONObject responsejson = new JSONObject();
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		//检查传参是否正确
		if(!(map.containsKey("id")
				&& map.containsKey("schedulerType"))){
			responsejson.put("description", "参数不正确");
			responsejson.put("result", false);
			return responsejson;
		}
		
		if(etlJobService.isRepeatByIdORName(Integer.parseInt(map.get("id").toString()))){
			responsejson.put("description", "该作业已经存在调度");
			responsejson.put("result", false);
			return responsejson;
		}
		
		int schedulerType = 0;
		//int intervalSeconds = 0;
		//int intervalMinutes = 0;
		int hour = 0;
		int minutes = 0;
		//int day = 0;
		int week = 0;
		
		try{
			ETLJob etlJob = etlJobService.getETLJobById(Integer.parseInt(map.get("id").toString()));
			schedulerType = Integer.parseInt(map.get("schedulerType").toString());
			
			Map dataMap = new HashedMap();
			dataMap.put("schedulerType", schedulerType);
			String date = map.get("daytime").toString();
			date = date.replace("Z", " UTC");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
			Date d = format.parse(date);
			String[] daytime = d.toString().split(" ");
			String[] time = daytime[3].split(":");
			hour = Integer.parseInt(time[0]);
			minutes = Integer.parseInt(time[1]);
			dataMap.put("hour", hour);
			dataMap.put("minutes", minutes);
			if(schedulerType == 3){
				week = Integer.parseInt(map.get("week").toString());
				dataMap.put("week", week);
			}
			dataMap.put("description", map.get("description").toString());
			//day = Integer.parseInt(map.get("day").toString());
			//intervalSeconds = Integer.parseInt(map.get("intervalSeconds").toString());
			//intervalMinutes = Integer.parseInt(map.get("intervalMinutes").toString());
			//map.put("intervalSeconds", intervalSeconds);
			//map.put("intervalMinutes", intervalMinutes);
			//map.put("day", day);

			responsejson.put("result", etlJobService.saveSchedule(metaDataService.getMetadataById(etlJob.getMappingid()).getNAME(), dataMap));
			
		}catch(Exception e){
			e.printStackTrace();
			responsejson.put("description", "运行出错");
			responsejson.put("result", false);
		}
		
		return responsejson;
	}
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年4月25日
	 * 作用:获取能够添加调度的作业
	 */
	@RequestMapping(value = "/getETLJobToSchedule",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="获取能够添加调度的作业")
	public JSONObject getETLJobToSchedule(HttpServletRequest request,HttpServletResponse response) {
		JSONObject responsejson = new JSONObject();
	
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		List<ETLJob> etlJobs = etlJobService.getETLJobtoSchedule("LOCAL");
		JSONArray data = new JSONArray();
		for(ETLJob etlJob:etlJobs){
			JSONObject node = new JSONObject();
			node.put("value", etlJob.getId());	
			node.put("label", metaDataService.getMetadataById(etlJob.getMappingid()).getNAME());	
			data.add(node);
		}
		responsejson.put("result", true);
		responsejson.put("data",data);
		
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
	 * 作者:GodDispose
	 * 时间:2018年5月8日
	 * 作用:执行ETL调度
	 * 参数:id
	 * @throws Exception
	 */
	@RequestMapping(value = "/executeETLSchedule",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="执行ETL调度")
	public JSONObject executeETLSchedule(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) throws UnsupportedEncodingException, KettleException, IOException{
		JSONObject responsejson = new JSONObject();
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		//检查传参是否正确
		if(!(map.containsKey("id"))){
			responsejson.put("result", false);
			return responsejson;
		}		
		int id=Integer.parseInt((String)map.get("id"));
		
		Dispatch dispatch = dispatchService.queryByDispatchId(id);
		boolean result = false;
		try {
			responsejson.put("result", etlJobService.excuteSchedule(dispatch));
		} catch (Exception e) {
			e.printStackTrace();
			responsejson.put("result", false);
		}
		
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
		if(!(map.containsKey("ids"))){
			responsejson.put("result", false);
			responsejson.put("message", "参数不正确");
			return responsejson;
		}
		String[]ids= map.get("ids").toString().split(",");
		int[] id = new int[ids.length];	
		for(int i=0;i<ids.length;i++){
			id[i]=Integer.parseInt(ids[i]);
		}
		if(etlJobService.deleteETLJobs(id)){
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
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("target_table_id"))){
			responsejson.put("result", false);
			responsejson.put("message", "参数不正确");
			return responsejson;
		}
		
		try{			
			int id = Integer.parseInt(map.get("target_table_id").toString());
			ETLJob job = etlJobService.getETLJobById(id);
			if(job.getType().equals("LOCAL")){				
				responsejson.put("result",etlJobService.stopETLJob(job.getMappingid()));				
			}else{
				responsejson.put("result",etlJobService.stopETLJob(job.getMetadata_id()));
			}
			
		}catch(Exception e){
			e.printStackTrace();
			responsejson.put("result",false);
		}
		return responsejson;
	}
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年5月4日
	 * 作用:停止调度执行
	 * 参数:id
	 */
	@RequestMapping(value = "/stopETLSchedule",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="Dispatch",operationDesc="停止调度执行")
	public JSONObject stopETLSchedule(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("id"))){
			responsejson.put("result", false);
			responsejson.put("message", "参数不正确");
			return responsejson;
		}
		
		try{			
			int id = Integer.parseInt(map.get("id").toString());			
			responsejson.put("result",etlJobService.stopETLSchedule(id));				
			
		}catch(Exception e){
			e.printStackTrace();
			responsejson.put("result",false);
		}
		return responsejson;
	}
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年5月4日
	 * 作用:根据id删除一个ETL调度
	 * 参数:id
	 */
	@RequestMapping(value = "/deleteETLSchedule",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="Dispatch",operationDesc="根据id删除一个ETL调度")
	public JSONObject deleteETLSchedule(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!map.containsKey("id")){
			responsejson.put("result", false);
			responsejson.put("message", "参数不正确");
			return responsejson;
		}
		
		int id = 1;
		try{
			id = Integer.parseInt(map.get("id").toString());
		}catch(Exception e){
			responsejson.put("result", false);
			responsejson.put("message","转化不正确");
			return responsejson;
		}
		
		if(dispatchService.deleteETLSchedule(id)){
			responsejson.put("result", true);
			
		}else{
			responsejson.put("result", false);
			responsejson.put("message", "删除失败");
		}		
	
		return responsejson;
	}
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年5月4日
	 * 作用:根据id字符串（逗号隔开）删除多条数据
	 * 参数:ids(1,2,3这种)
	 */
	@RequestMapping(value = "/deleteETLSchedules",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="Dispatch",operationDesc="根据id字符串（逗号隔开）删除多条数据")
	public JSONObject deleteETLSchedules(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("ids"))){
			responsejson.put("result", false);
			responsejson.put("message", "参数不正确");
			return responsejson;
		}
		String[]ids= map.get("ids").toString().split(",");
		int[] id = new int[ids.length];	
		for(int i=0;i<ids.length;i++){
			id[i]=Integer.parseInt(ids[i]);
		}
		if(dispatchService.deleteETLSchedules(id)){
			responsejson.put("result", true);
			responsejson.put("message", "删除成功");
		}else{
			responsejson.put("result", false);
			responsejson.put("message", "删除失败");
		}		
	
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
					map.containsKey("pageSize"))){
				responsejson.put("result", false);
				responsejson.put("count",0);
				return responsejson;
			}
	
			String currPageStr = map.get("page").toString();
			String pageSizeStr = map.get("pageSize").toString();
			int currPage = 1;
			int pageSize = 1;
			int type = 0;
			try {
				currPage = Integer.parseInt(currPageStr);
				pageSize = Integer.parseInt(pageSizeStr);
			} catch (Exception e) {
			}
			//获取总记录数
			int rowCount = etlJobService.getRowCount("");
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
						etljobJson.put("target_table", metaDataService.getMetadataById(etlJob.getMappingid()).getNAME());
						etljobJson.put("description", etlJob.getDescription());
						etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
						etljobJson.put("recently_run_date","");
						etljobJson.put("status",etlJob.getStatus());
						etljobJson.put("type", "内部");
						etljobJson.put("jobtype", "否");
						etljobJson.put("log","该任务刚刚新建");					
						data.add(etljobJson);
					}else{			
						etljobJson.put("target_table",  metaDataService.getMetadataById(etlJob.getMappingid()).getNAME());
						etljobJson.put("description", etlJob.getDescription());
						etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
						etljobJson.put("recently_run_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getRecently_run_date()));
						etljobJson.put("status", etlJob.getStatus());
						etljobJson.put("type", "内部");
						etljobJson.put("jobtype", "否");
						etljobJson.put("log", etlJob.getLog());
						data.add(etljobJson);
					}
				}else{
					if(etlJob.getStatus().equals("NewCreate")){
						etljobJson.put("target_table",  metaDataService.getMetadataById(etlJob.getMetadata_id()).getNAME());
						etljobJson.put("description", etlJob.getDescription());
						etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
						etljobJson.put("recently_run_date","");
						etljobJson.put("status",etlJob.getStatus());
						etljobJson.put("type","外部");
						etljobJson.put("jobtype", etlJob.getJobtype() == 0 ? "否":"是");
						etljobJson.put("log","该任务刚刚新建");					
						data.add(etljobJson);
					}else{
						etljobJson.put("target_table",  metaDataService.getMetadataById(etlJob.getMetadata_id()).getNAME());
						etljobJson.put("description", etlJob.getDescription());
						etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
						etljobJson.put("recently_run_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getRecently_run_date()));
						etljobJson.put("type","外部");
						etljobJson.put("jobtype", etlJob.getJobtype() == 0 ? "否":"是");
						etljobJson.put("status", etlJob.getStatus());
						etljobJson.put("log", etlJob.getLog());
						data.add(etljobJson);
					}
				}
			}
			responsejson.put("result", true);
			responsejson.put("data", data);
			responsejson.put("total", etlJobService.getRowCount(""));
			responsejson.put("count",ETLJobList.size());
			return responsejson;
			}
		}

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月7日
	 * 作用:获取分页数据(数据按 createDate 倒叙排序，新添加的在最前面)
	 * 参数:page(页码)、pageSize(每页多少行)
	 */
	@RequestMapping(value = "/getETLScheduleListByPage",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="Dispatch",operationDesc="获取分页ETLJSchedule数据")
	public JSONObject getETLScheduleListByPage(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("page")&&
				map.containsKey("pageSize"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String currPageStr = map.get("page").toString();
		String pageSizeStr = map.get("pageSize").toString();
		int currPage = 1;
		int pageSize = 1;
		try {
			currPage = Integer.parseInt(currPageStr);
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (Exception e) {
		}
		//获取总记录数
		int rowCount = dispatchService.getRowCount("");
		//构造分页数据
		PageParam pageParam = new PageParam();
		pageParam.setPageSize(pageSize);
		pageParam.setRowCount(rowCount);
		if(pageParam.getTotalPage()<currPage){
			currPage = pageParam.getTotalPage();
		}
		pageParam.setCurrPage(currPage);
		pageParam = dispatchService.getETLScheduleListByPage(pageParam);
		if(pageParam.getData()==null|| pageParam.getData().isEmpty()){
			responsejson.put("result", false);
			responsejson.put("message", "没有查询到任务");
			return responsejson;
		}else{
		JSONArray data = new JSONArray();
		List<Dispatch> dispatchList=pageParam.getData();
		for(Dispatch dispatch : dispatchList){
			JSONObject dispatchJson = new JSONObject();
			dispatchJson.put("dispatchid", dispatch.getDispatchid());
				dispatchJson.put("name", dispatch.getName());
				dispatchJson.put("description", dispatch.getDescription());
				dispatchJson.put("jobname", dispatch.getJobname());
				if(dispatch.getStatus() == 1){
					dispatchJson.put("status",  "新建");
				}else if(dispatch.getStatus() == 2){
					dispatchJson.put("status",  "运行中");
				}else if(dispatch.getStatus() == 3){
					dispatchJson.put("status",  "运行结束");
				}
				dispatchJson.put("runinterval",dispatch.getRuninterval());
				dispatchJson.put("createtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dispatch.getCreatetime()));	
				dispatchJson.put("recenttime",dispatch.getRecenttime() == null ? "" :new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dispatch.getRecenttime()));	
				dispatchJson.put("endtime",dispatch.getEndtime() == null ? "" :new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dispatch.getEndtime()));	
				data.add(dispatchJson);
		}
		responsejson.put("result", true);
		responsejson.put("data", data);
		responsejson.put("total", dispatchService.getRowCount(""));
		responsejson.put("count",dispatchList.size());
		return responsejson;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月7日
	 * 作用:根据描述筛选，并获取分页ETLJSchedule数据(数据按 createDate 倒叙排序，新添加的在最前面)
	 * 参数:page(页码)、pageSize(每页多少行)、description(描述)
	 */
	@RequestMapping(value = "/getETLScheduleListByDescription",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="Dispatch",operationDesc="根据描述筛选，并获取分页ETLJSchedule数据")
	public JSONObject getETLScheduleListByDescription(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("page")&&
				map.containsKey("pageSize"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String currPageStr = map.get("page").toString();
		String pageSizeStr = map.get("pageSize").toString();
		String description = null;
		if(map.containsKey("description")){
			description = map.get("description").toString();
			if(description.isEmpty()){
				description = null;
			}
		}
		int currPage = 1;
		int pageSize = 1;
		try {
			currPage = Integer.parseInt(currPageStr);
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (Exception e) {
		}
		//获取总记录数
		int rowCount = dispatchService.getRowCount(description);
		//构造分页数据
		PageParam pageParam = new PageParam();
		pageParam.setPageSize(pageSize);
		pageParam.setRowCount(rowCount);
		if(pageParam.getTotalPage()<currPage){
			currPage = pageParam.getTotalPage();
		}
		pageParam.setCurrPage(currPage);
		pageParam = dispatchService.getETLScheduleListByDescription(pageParam,description);
		if(pageParam.getData()==null|| pageParam.getData().isEmpty()){
			responsejson.put("result", false);
			responsejson.put("message", "没有查询到任务");
			return responsejson;
		}else{
		JSONArray data = new JSONArray();
		List<Dispatch> dispatchList=pageParam.getData();
		for(Dispatch dispatch : dispatchList){
			JSONObject dispatchJson = new JSONObject();
			dispatchJson.put("dispatchid", dispatch.getDispatchid());
				dispatchJson.put("name", dispatch.getName());
				dispatchJson.put("description", dispatch.getDescription());
				dispatchJson.put("jobname", dispatch.getJobname());
				if(dispatch.getStatus() == 1){
					dispatchJson.put("status",  "新建");
				}else if(dispatch.getStatus() == 2){
					dispatchJson.put("status",  "运行中");
				}else if(dispatch.getStatus() == 3){
					dispatchJson.put("status",  "运行结束");
				}
				dispatchJson.put("runinterval",dispatch.getRuninterval());
				dispatchJson.put("createtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dispatch.getCreatetime()));	
				dispatchJson.put("recenttime",dispatch.getRecenttime() == null ? "" :new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dispatch.getRecenttime()));	
				dispatchJson.put("endtime",dispatch.getEndtime() == null ? "" :new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dispatch.getEndtime()));	
				data.add(dispatchJson);
		}
		responsejson.put("result", true);
		responsejson.put("data", data);
		responsejson.put("total", dispatchService.getRowCount(description));
		responsejson.put("count",dispatchList.size());
		return responsejson;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月7日
	 * 作用:根据描述筛选，并获取分页ETLJob数据(数据按 createDate 倒叙排序，新添加的在最前面)
	 * 参数:page(页码)、pageSize(每页多少行)、description(描述)
	 */
	@RequestMapping(value = "/getETLJobListByDescription",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="ETLJob",operationDesc="根据描述筛选，并获取分页ETLJob数据(数据按 createDate 倒叙排序，新添加的在最前面)")
	public JSONObject getETLJobListByDescription(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("page")&&
				map.containsKey("pageSize"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String currPageStr = map.get("page").toString();
		String pageSizeStr = map.get("pageSize").toString();
		String description = null;
		if(map.containsKey("description")){
			description = map.get("description").toString();
			if(description.isEmpty()){
				description = null;
			}
		}
		int currPage = 1;
		int pageSize = 1;
		try {
			currPage = Integer.parseInt(currPageStr);
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (Exception e) {
		}
		//获取总记录数
		int rowCount = etlJobService.getRowCount(description);
		//构造分页数据
		PageParam pageParam = new PageParam();
		pageParam.setPageSize(pageSize);
		pageParam.setRowCount(rowCount);
		if(pageParam.getTotalPage()<currPage){
			currPage = pageParam.getTotalPage();
		}
		pageParam.setCurrPage(currPage);
		pageParam = etlJobService.getETLJobListByDescrption(pageParam,description);
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
					etljobJson.put("target_table", metaDataService.getMetadataById(etlJob.getMappingid()).getNAME());
					etljobJson.put("description", etlJob.getDescription());
					etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
					etljobJson.put("recently_run_date","");
					etljobJson.put("status",etlJob.getStatus());
					etljobJson.put("type", "内部");
					etljobJson.put("jobtype", "否");
					etljobJson.put("log","该任务刚刚新建");					
					data.add(etljobJson);
				}else{			
					etljobJson.put("target_table",  metaDataService.getMetadataById(etlJob.getMappingid()).getNAME());
					etljobJson.put("description", etlJob.getDescription());
					etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
					etljobJson.put("recently_run_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getRecently_run_date()));
					etljobJson.put("status", etlJob.getStatus());
					etljobJson.put("type", "内部");
					etljobJson.put("jobtype", "否");
					etljobJson.put("log", etlJob.getLog());
					data.add(etljobJson);
				}
			}else{
				if(etlJob.getStatus().equals("NewCreate")){
					etljobJson.put("target_table",  metaDataService.getMetadataById(etlJob.getMetadata_id()).getNAME());
					etljobJson.put("description", etlJob.getDescription());
					etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
					etljobJson.put("recently_run_date","");
					etljobJson.put("status",etlJob.getStatus());
					etljobJson.put("type","外部");
					etljobJson.put("jobtype", etlJob.getJobtype() == 0 ? "否":"是");
					etljobJson.put("log","该任务刚刚新建");					
					data.add(etljobJson);
				}else{
					etljobJson.put("target_table",  metaDataService.getMetadataById(etlJob.getMetadata_id()).getNAME());
					etljobJson.put("description", etlJob.getDescription());
					etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
					etljobJson.put("recently_run_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getRecently_run_date()));
					etljobJson.put("type","外部");
					etljobJson.put("jobtype", etlJob.getJobtype() == 0 ? "否":"是");
					etljobJson.put("status", etlJob.getStatus());
					etljobJson.put("log", etlJob.getLog());
					data.add(etljobJson);
				}
			}
		}
		responsejson.put("result", true);
		responsejson.put("data", data);
		responsejson.put("total", etlJobService.getRowCount(description));
		responsejson.put("count",ETLJobList.size());
		return responsejson;
		}
	}
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年5月10日
	 * 作用:获取已经采集的ETL作业元数据
	 * 参数:无
	 */
	@RequestMapping(value = "/queryJobIdAndName",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="Metadata",operationDesc="获取已经采集的ETL作业元数据")
	public JSONObject queryJobIdAndName(HttpServletRequest request,HttpServletResponse response) {
		JSONObject responsejson = new JSONObject();
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		List<Metadata> target_table_ids= metaDataService.getMetadataByMetaModelIdAndNoNull(203);
		if(target_table_ids==null || target_table_ids.isEmpty() || target_table_ids.get(0) == null){
			responsejson.put("result",false);
			responsejson.put("message","没有查询到任何字段映射元数据");
			
			return responsejson;
		}else{
		
		JSONArray data = new JSONArray();
		for(Metadata metadata:target_table_ids){
			JSONObject node = new JSONObject();
			node.put("id", metadata.getID());	
			node.put("name", metadata.getNAME());	
			data.add(node);
		}
		responsejson.put("result", true);
		responsejson.put("data",data);
		return responsejson;
		}
		
	}
	
	
}	

