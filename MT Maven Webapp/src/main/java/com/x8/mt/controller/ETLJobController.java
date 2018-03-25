//package com.x8.mt.controller;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.text.SimpleDateFormat;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.pentaho.di.core.exception.KettleException;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.x8.mt.common.GlobalMethodAndParams;
//import com.x8.mt.common.Log;
//import com.x8.mt.common.PageParam;
//import com.x8.mt.entity.ETLJob;
//import com.x8.mt.service.ETLJobService;
//import com.x8.mt.service.Metadata_relationtableService;
//import com.x8.mt.service.SystemLogService;
//
//@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
//@Controller
//@RequestMapping(value = "/ETLJob")
//public class ETLJobController {
//
//	@Resource
//	SystemLogService systemlogService;
//	@Resource
//	ETLJobService etlJobService;
//	@Resource
//	Metadata_relationtableService metadata_relationtableService;
//
//	/**
//	 * 
//	 * 作者:Tomcroods
//	 * 时间:2017年12月15日
//	 * 作用:插入新的ETLJob
//	 * 参数:target_table_id,description.
//	 */
//	@RequestMapping(value = "/insertETLJob",method=RequestMethod.POST)
//	@ResponseBody
//	@Log(operationType="ETLJob",operationDesc="插入一条ETLJob")
//	public JSONObject insertETLJob(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
//		JSONObject responsejson = new JSONObject();
//		GlobalMethodAndParams.setHttpServletResponse(request, response);
//		//检查传参是否正确
//		if(!(map.containsKey("target_table_id")&&
//				map.containsKey("description"))){
//			responsejson.put("result", false);
//			return responsejson;
//		}
//		
//		int target_table_id=Integer.parseInt((String)map.get("target_table_id"));
//		String target_table=metadata_relationtableService.getMetadata_relationtableByid(target_table_id).getName();
//		String description=(String)map.get("description");
//		
//		responsejson.put("result", etlJobService.insertETLJob(target_table_id, target_table, description));
//		return responsejson;
//	}
//	
//	/**
//	 * 
//	 * 作者:Tomcroods
//	 * 时间:2017年12月15日
//	 * 作用:执行新的ETLJob
//	 * 参数:target_table_id
//	 * @throws IOException 
//	 * @throws KettleException 
//	 * @throws UnsupportedEncodingException 
//	 */
//	@RequestMapping(value = "/executeETLJob",method=RequestMethod.POST)
//	@ResponseBody
//	@Log(operationType="ETLJob",operationDesc="执行ETLJob")
//	public JSONObject executeETLJob(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) throws UnsupportedEncodingException, KettleException, IOException{
//		JSONObject responsejson = new JSONObject();
//		GlobalMethodAndParams.setHttpServletResponse(request, response);
//		//检查传参是否正确
//		if(!(map.containsKey("target_table_id"))){
//			responsejson.put("result", false);
//			return responsejson;
//		}
//		int target_table_id=Integer.parseInt((String)map.get("target_table_id"));
//		String target_table=metadata_relationtableService.getMetadata_relationtableByid(target_table_id).getName();
//		ETLJob etlJob = new ETLJob();
//		etlJob.setTarget_table_id(target_table_id);
//		etlJob.setTarget_table(target_table);
//		boolean result=etlJobService.executeJob(etlJob);
//		responsejson.put("result", result);
//		return responsejson;
//	}
//	
//	/**
//	 * 
//	 * 作者:Tomcroods
//	 * 时间:2017年12月15日
//	 * 作用:获取分页数据(数据按 createDate 倒叙排序，新添加的在最前面)
//	 * 参数:page(页码)、pageSize(每页多少行)
//	 */
//	@RequestMapping(value = "/getETLJobListByPage",method=RequestMethod.POST)
//	@ResponseBody
//	@Log(operationType="ETLJob",operationDesc="获取分页ETLJob数据")
//	public JSONObject getETLJobListByPage(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
//		JSONObject responsejson = new JSONObject();
//
////		if(!GlobalMethodAndParams.checkLogin()){
////			responsejson.put("result", false);
////			responsejson.put("count",0);
////			return responsejson;
////		}
//		GlobalMethodAndParams.setHttpServletResponse(request, response);
//
//		//检查传参是否正确
//		if(!(map.containsKey("page")&&
//				map.containsKey("pageSize"))){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
//
//		String currPageStr = map.get("page").toString();
//		String pageSizeStr = map.get("pageSize").toString();
//		int currPage = 1;
//		int pageSize = 1;
//		try {
//			currPage = Integer.parseInt(currPageStr);
//			pageSize = Integer.parseInt(pageSizeStr);
//		} catch (Exception e) {
//		}
//		//获取总记录数
//		int rowCount = etlJobService.getRowCount();
//		//构造分页数据
//		PageParam pageParam = new PageParam();
//		pageParam.setPageSize(pageSize);
//		pageParam.setRowCount(rowCount);
//		if(pageParam.getTotalPage()<currPage){
//			currPage = pageParam.getTotalPage();
//		}
//		pageParam.setCurrPage(currPage);
//		pageParam = etlJobService.getETLJobListByPage(pageParam);
//		if(pageParam.getDate()==null|| pageParam.getDate().isEmpty()){
//			responsejson.put("result", false);
//			responsejson.put("message", "没有查询到任务");
//			return responsejson;
//		}else{
//		JSONArray data = new JSONArray();
//		@SuppressWarnings("unchecked")
//		List<ETLJob> ETLJobList=pageParam.getDate();
//		for(ETLJob etlJob :ETLJobList){
//			JSONObject etljobJson = new JSONObject();
//			if(etlJob.getStatus().equals("newCreated")){
//				etljobJson.put("target_table_id", etlJob.getTarget_table_id());
//				etljobJson.put("target_table", etlJob.getTarget_table());
//				etljobJson.put("description", etlJob.getDescription());
//				etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
//				etljobJson.put("recently_run_date","");
//				etljobJson.put("status",etlJob.getStatus());
//				etljobJson.put("log","该任务刚刚新建");
//				
//				data.add(etljobJson);
//			}else{			
//			etljobJson.put("target_table_id", etlJob.getTarget_table_id());
//			etljobJson.put("target_table", etlJob.getTarget_table());
//			etljobJson.put("description", etlJob.getDescription());
//			etljobJson.put("create_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getCreate_date()));
//			etljobJson.put("recently_run_date",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(etlJob.getRecently_run_date()));
//			etljobJson.put("status", etlJob.getStatus());
//			etljobJson.put("log", etlJob.getLog());
//			data.add(etljobJson);
//			}
//		}
//		responsejson.put("result", true);
//		responsejson.put("data", data);
//		responsejson.put("count",ETLJobList.size());
//		return responsejson;
//		}
//	}
//	
//	/**
//	 * 作者:Tomcroods
//	 * 时间:2017年12月15日
//	 * 作用:删除一条数据
//	 * 参数:target_table_id
//	 */
//	@RequestMapping(value = "/deleteETLJob",method=RequestMethod.POST)
//	@ResponseBody
//	@Log(operationType="ETLJob",operationDesc="根据id删除一条数据")
//	public JSONObject deleteMetadata(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
//		JSONObject responsejson = new JSONObject();
//
//		GlobalMethodAndParams.setHttpServletResponse(request, response);
//		
//		//检查传参是否正确
//		if(!map.containsKey("target_table_id")){
//			responsejson.put("result", false);
//			responsejson.put("message", "参数不正确");
//			return responsejson;
//		}
//		
//		int target_table_id = 1;
//		try{
//			target_table_id = Integer.parseInt(map.get("target_table_id").toString());
//		}catch(Exception e){
//			responsejson.put("result", false);
//			responsejson.put("message","转化不正确");
//			return responsejson;
//		}
//
//		
//		if(etlJobService.deleteETLJob(target_table_id)){
//			responsejson.put("result", true);
//			
//		}else{
//			responsejson.put("result", false);
//			responsejson.put("message", "删除失败");
//		}		
//	
//		return responsejson;
//	}
//	
//	/**
//	 * 作者:Tomcroods
//	 * 时间:2017年12月15日
//	 * 作用:删除多条数据
//	 * 参数:ids(1,2,3这种)
//	 */
//	@RequestMapping(value = "/deleteETLJobs",method=RequestMethod.POST)
//	@ResponseBody
//	@Log(operationType="ETLJob",operationDesc="根据id字符串（逗号隔开）删除多条数据")
//	public JSONObject deleteETLJobs(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
//		JSONObject responsejson = new JSONObject();
//
//		GlobalMethodAndParams.setHttpServletResponse(request, response);
//		
//		//检查传参是否正确
//		if(!map.containsKey("ids")){
//			responsejson.put("result", false);
//			responsejson.put("message", "参数不正确");
//			return responsejson;
//		}
//		String[]ids= map.get("ids").toString().split(",");
//		int[] id = new int[ids.length];
//				
//			
//		
//			for(int i=0;i<ids.length;i++){
//				id[i]=Integer.parseInt(ids[i]);
//			}
//		
//
//		
//		if(etlJobService.deleteETLJobs(id)){
//			responsejson.put("result", true);
//			responsejson.put("message", "删除成功");
//		}else{
//			responsejson.put("result", false);
//			responsejson.put("message", "删除失败");
//		}		
//	
//		return responsejson;
//	}
//	
//	/**
//	 * 作者:Tomcroods
//	 * 时间:2017年12月15日
//	 * 作用:用于展示规则下拉框
//	 * 参数:无
//	 */
//	@RequestMapping(value = "/queryTargetTableIdAndName",method=RequestMethod.GET)
//	@ResponseBody
//	@Log(operationType="ETLJob",operationDesc="返回字段映射元数据（目标表）")
//	public JSONObject queryTargetTableIdAndName(HttpServletRequest request,HttpServletResponse response) {
//		JSONObject responsejson = new JSONObject();
//		GlobalMethodAndParams.setHttpServletResponse(request, response);
//		List<Integer> target_table_ids= etlJobService.queryTargetTableIdAndName();
//		if(target_table_ids==null||target_table_ids.isEmpty()){
//			responsejson.put("result",false);
//			responsejson.put("message","没有查询到任何字段映射元数据");
//			
//			return responsejson;
//		}else{
//		
//		JSONArray data = new JSONArray();
//		for(Integer target_table_id:target_table_ids){
//			if(true){
//			JSONObject node = new JSONObject();
//			node.put("target_table_id", target_table_id);	
//			node.put("target_table", metadata_relationtableService.getMetadata_relationtableByid(target_table_id).getName());	
//			data.add(node);
//			}
//		}
//		responsejson.put("result", true);
//		responsejson.put("data",data);
////		for(Connectinfo connectinfo : connectinfoList){
////			JSONObject node = new JSONObject();
////			node.put("id",connectinfo.getId());
////			node.put("name", connectinfo.getName());
////			data.add(node);
////		}
//		return responsejson;
//		}
//		
//	}
//	/**
//	 * 作者:Tomcroods
//	 * 时间:2017年12月15日
//	 * 作用:用于展示规则下拉框
//	 * 参数:target_table_id
//	 */
//	@RequestMapping(value = "/stopETLJob",method=RequestMethod.POST)
//	@ResponseBody
//	@Log(operationType="ETLJob",operationDesc="停止作业执行")
//	public JSONObject stopETLJob(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
//		JSONObject responsejson = new JSONObject();
//		
//		ETLJobService etlJobService = new ETLJobService();
//		responsejson.put("result",etlJobService.stopETLJob(Integer.parseInt(map.get("target_table_id").toString())));
//		return responsejson;
//}
//	
//}	
//
