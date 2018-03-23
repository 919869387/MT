package com.x8.mt.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.entity.CollectJob;
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.entity.Metadata;
import com.x8.mt.entity.Metamodel_datatype;
import com.x8.mt.service.CollectJobService;
import com.x8.mt.service.ConnectinfoService;
import com.x8.mt.service.Datasource_connectinfoService;
import com.x8.mt.service.KettleMetadataCollectService;
import com.x8.mt.service.MetaDataService;
import com.x8.mt.service.Metamodel_datatypeService;
import com.x8.mt.service.Metamodel_hierarchyService;

@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/kettleMetadataCollect")
public class KettleMetadataCollectController {
	@Resource
	Datasource_connectinfoService datasource_connectinfoService;
	@Resource
	ConnectinfoService connectinfoService;
	@Resource
	KettleMetadataCollectService kettleMetadataCollectService;
	@Resource
	MetaDataService metaDataService;
	@Resource
	CollectJobService collectJobService;
	@Resource
	Metamodel_hierarchyService metamodel_hierarchyService;	
	@Resource
	Metamodel_datatypeService metamodel_datatypeService;
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据入参id删除元数据
	 * 参数:id,flag
	 * 
	 */
	@RequestMapping(value = "/deleteMetadataById",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="根据入参id删除元数据")
	public JSONObject deleteMetadataById(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
//		//检查传参是否正确
		if(!(map.containsKey("id"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		String idStr = map.get("id").toString();
		int id = 0;
		try{
			id = Integer.parseInt(idStr);
			boolean result = false;
			
			result = metaDataService.deleteMetadataById(id);
			if(result==true){
				responsejson.put("result", result);
				responsejson.put("count", 1);
			}else{
				responsejson.put("result", result);
				responsejson.put("count", 0);
			}			
			
		}catch(Exception e){
			e.printStackTrace();
			responsejson.put("result", false);
			responsejson.put("count", 0);
		}		
		
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据入参id，describe修改描述字段
	 * 参数:id,describe
	 * 
	 */
	@RequestMapping(value = "/updateMetaDataDescribeById",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="根据入参id，describe修改描述字段")
	public JSONObject updateMetaDataDescribeById(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("id"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		//判断id，flag是否为空
		
		String idStr = map.get("id").toString();
		String describe = map.get("describe").toString();
		int id = 0;
		try{
			id = Integer.parseInt(idStr);			
			boolean result = false;
			Metadata metaData = new Metadata();
			metaData.setId(id);
			metaData.setDescription(describe);
			result = metaDataService.updateMetadataDescribeById(metaData);

			if(result==true){
				responsejson.put("result", result);
				responsejson.put("count", 1);
			}else{
				responsejson.put("result", result);
				responsejson.put("count", 0);
			}			
		}catch(Exception e){
			e.printStackTrace();
			responsejson.put("result", false);
			responsejson.put("count", 0);
		}		
		
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据入参id，返回元数据信息
	 * 参数:id
	 * 
	 */
	@RequestMapping(value = "/getMetadataById",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="根据入参id，返回元数据信息")
	public JSONObject getMetadataById(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("id"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		//判断id是否为空
		
		String idStr = map.get("id").toString();
		int id = 0;
		try{
			id = Integer.parseInt(idStr);
			
			JSONObject data = new JSONObject();
			
			Metadata metaData = metaDataService.getMetadataById(id);
			JSONObject json = JSONObject.fromObject(metaData.getAttributes());
			List<Metamodel_datatype> privateMetaModels = metamodel_datatypeService.getMetamodel_datatypeByMetaModelId(metaData.getMetaModelId());
			data.put("元数据id", metaData.getId());
			data.put("元数据名称", metaData.getName());
			data.put("元数据业务说明", metaData.getDescription());
			data.put("元数据入库时间", metaData.getCreateTime());
			data.put("元数据修改时间", metaData.getUpdateTime());
			data.put("元数据的版本号", metaData.getVersion());
			data.put("采集元数据的编号", metaData.getCollectJobId());
			data.put("审核状态", metaData.getCheckStatus());
			data.put("所属元模型id", metaData.getMetaModelId());
			for(Metamodel_datatype pri : privateMetaModels){
				data.put(pri.getDesribe(), json.get(pri.getName()));
			}			
			
			responsejson.put("result", true);
			responsejson.put("data", data);
			responsejson.put("count", data.size());
		}catch(Exception e){
			e.printStackTrace();
			responsejson.put("result", false);
			responsejson.put("count", 10000);
		}		
		
		return responsejson;
	}

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据connectinfo信息，kettle自动采集元数据
	 * 参数：id(connectinfo的id)
	 */
	@RequestMapping(value = "/metadataAutoCollect",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata,metadata_relation",operationDesc="kettle自动采集关系型数据库元数据")
	public JSONObject metadataAutoCollect(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!map.containsKey("id") && !map.containsKey("name") ){
			responsejson.put("result", false);
			responsejson.put("description", "传输参数 错误");
			responsejson.put("count",0);
			return responsejson;
		}
		
		//获取采集任务名称
		String name = map.get("name").toString();
		if(name == null || name.isEmpty()){
			responsejson.put("result", false);
			responsejson.put("description", "采集名称不能为空");
			responsejson.put("count",0);
			return responsejson;
		}
		
		String idstr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idstr);
		} catch (Exception e) {
		}

		Datasource_connectinfo datasource_connectinfo = datasource_connectinfoService.getDatasource_connectinfoListByparentid(id);		
		
		//将采集任务结果插入到数据库中		
		//获取数据源编号
		int connectinfoid = id;
		//获取采集方式
		String mode ="All";
		//获取采集方式
		String checkResult ="1";
		//获取当前日期
		Date createDate = null;
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			createDate = sdf.parse(sdf.format(new Date()));
		}catch(Exception e){}
		//获取当前的用户名
		String creater = "admin";
		
		
		CollectJob collectjob = new CollectJob(name,connectinfoid,mode,checkResult,createDate,creater);			
		collectJobService.insertCollectJob(collectjob);				
	
		try {
			int collectCount = kettleMetadataCollectService.metadataAutoCollect(datasource_connectinfo,collectjob.getId(),createDate,id);
			
			String[] tables = kettleMetadataCollectService.getTables(datasource_connectinfo);
			JSONArray data = new JSONArray();
			for(String table : tables){
				JSONObject node = new JSONObject();
				node.put("tablename", table);
				node.put("operationname", null);
				node.put("operationdescribe", null);
				data.add(node);
			}
			responsejson.put("result", true);
			responsejson.put("data",data);
			responsejson.put("count",data.size());
		} catch (Exception e) {
			e.printStackTrace();
			collectJobService.deleteById(collectjob.getId());
			responsejson.put("result", false);
			responsejson.put("count",0);
		}

		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据Datasource_connectinfo信息获取表结构
	 * 参数：id(Datasource_connectinfo的id)
	 */
	@RequestMapping(value = "/getTables",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="null",operationDesc="根据Datasource_connectinfo信息获取表结构")
	public JSONObject getTables(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!map.containsKey("id")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		String idstr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idstr);
		} catch (Exception e) {
		}
		
		Datasource_connectinfo datasource_connectinfo = datasource_connectinfoService.getDatasource_connectinfoListByparentid(id);		
	
		try {
			String[] tables = kettleMetadataCollectService.getTables(datasource_connectinfo);
			JSONArray data = new JSONArray();
			for(String table : tables){
				JSONObject node = new JSONObject();
				node.put("tablename", table);
				node.put("operationname", null);
				node.put("operationdescribe", null);
				data.add(node);
			}
			responsejson.put("result", true);
			responsejson.put("data",data);
			responsejson.put("count",data.size());
		} catch (Exception e) {
			responsejson.put("result", false);
			responsejson.put("count",0);
		}

		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据指定时间获取采集任务调度
	 * 参数:startdate,enddate(两个至少有一个存在)
	 * 
	 */
	@RequestMapping(value = "/getMetadataCollectSelf",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="collectjob",operationDesc="根据指定时间获取采集任务调度")
	public JSONObject getMetadataCollectSelf(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("startdate") || map.containsKey("enddate"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		Date createDate = null;
		if(map.containsKey("startdate")){			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try{
				createDate = sdf.parse(map.get("startdate").toString());
			}catch(Exception e){
			}				
		}
		
		Date endDate = null;
		if(map.containsKey("enddate")){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try{
				endDate = sdf.parse(map.get("enddate").toString());
			}catch(Exception e){
			}	
		}

		List<CollectJob> collectJobs = collectJobService.getCollectJob(createDate, endDate);
		if(collectJobs == null || collectJobs.isEmpty()){
			responsejson.put("result",false);
			responsejson.put("count",0);
		}else{
			JSONArray data = new JSONArray();
			for(CollectJob collectJob:collectJobs){
				JSONObject node = new JSONObject();
				node.put("id", collectJob.getId());
				node.put("name", collectJob.getName());
				node.put("databasename",connectinfoService.getConnectinfoByid(collectJob.getConnectinfoId()).getName());
				node.put("createdate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(collectJob.getCreateDate()));
				node.put("creater", collectJob.getCreater());
				node.put("checkresult", collectJob.getCheckResult());
				data.add(node);
			}
			
			responsejson.put("result",true);
			responsejson.put("data",data);
			responsejson.put("count",data.size());
		}
		
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:获取当天的采集任务调度
	 * 
	 */
	@RequestMapping(value = "/getMetadataCollect",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="collectjob",operationDesc="获取当天的采集任务调度")
	public JSONObject getMetadataCollect(HttpServletRequest request,HttpServletResponse response) {
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
        Date createDate = null;
        Date endDate = null;
		
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        
        String createStr = "%s-%s-%s 00:00:00";
        String endStr = "%s-%s-%s 23:59:59";
        
        
        
//        long current=System.currentTimeMillis();//当前时间毫秒数
//        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
//        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        try{
            createDate = format.parse(String.format(createStr, year,month,day));
            endDate = format.parse(String.format(endStr, year,month,day));
        }catch(Exception e){}	

		List<CollectJob> collectJobs = collectJobService.getCollectJob(createDate, endDate);
		if(collectJobs == null || collectJobs.isEmpty()){
			responsejson.put("result",true);
			responsejson.put("description", "今日没采集日志");
			responsejson.put("count",0);
		}else{
			JSONArray data = new JSONArray();
			for(CollectJob collectJob:collectJobs){
				JSONObject node = new JSONObject();
				node.put("id", collectJob.getId());
				node.put("name", collectJob.getName());
				node.put("databasename",connectinfoService.getConnectinfoByid(collectJob.getConnectinfoId()).getName());
				node.put("createdate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(collectJob.getCreateDate()));
				node.put("creater", collectJob.getCreater());
				node.put("checkresult", collectJob.getCheckResult());
				data.add(node);
			}
			
			responsejson.put("result",true);
			responsejson.put("data",data);
			responsejson.put("count",data.size());
		}
		
		return responsejson;
	}

	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月23日
	 * 作用:获取数据库元数据
	 * 
	 */
	@RequestMapping(value = "/getDatabaseMetadata",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="获取数据库元数据")
	public JSONObject getDatabaseMetadata(HttpServletRequest request,HttpServletResponse response) {
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
	
		try{
			List<Metadata> metadatas = metaDataService.getMetadataByMetaModelId(10);
			JSONArray data = new JSONArray();
			for(Metadata metadata : metadatas){
				JSONObject node = new JSONObject();
				node.put("id", metadata.getId());
				node.put("label", metadata.getName());
				data.add(node);
			}
			responsejson.put("result", true);
			responsejson.put("data",data);
			responsejson.put("count",data.size());
		} catch (Exception e) {
			responsejson.put("result", false);
			responsejson.put("count",0);
		}
		return responsejson;
	}
		
}
