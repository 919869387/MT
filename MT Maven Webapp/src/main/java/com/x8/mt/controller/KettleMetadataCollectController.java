package com.x8.mt.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.x8.mt.common.CalDataSize;
import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.entity.CollectJob;
import com.x8.mt.entity.Connectinfo;
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.entity.File_connectinfo;
import com.x8.mt.entity.Metadata;
import com.x8.mt.entity.Metamodel_datatype;
import com.x8.mt.entity.Table;
import com.x8.mt.service.CollectJobService;
import com.x8.mt.service.ConnectinfoService;
import com.x8.mt.service.Datasource_connectinfoService;
import com.x8.mt.service.FileMetadataCollectService;
import com.x8.mt.service.File_connectinfoService;
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
	File_connectinfoService file_connectinfoService;
	@Resource
	ConnectinfoService connectinfoService;
	@Resource
	KettleMetadataCollectService kettleMetadataCollectService;
	@Resource
	FileMetadataCollectService fileMetadataCollectService;
	@Resource
	MetaDataService metaDataService;
	@Resource
	CollectJobService collectJobService;
	//@Resource
	//Metamodel_hierarchyService metamodel_hierarchyService;	
	@Resource
	Metamodel_datatypeService metamodel_datatypeService;
	
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年4月11日
	 * 作用:根据入参id修改状态字段
	 * 参数:id
	 * 
	 */
	@RequestMapping(value = "/updateMetaDataCheckstatus",method=RequestMethod.POST)
	@ResponseBody
	@Transactional
	@Log(operationType="metadata",operationDesc="根据入参id，describe修改描述字段")
	public JSONObject updateMetaDataCheckstatus(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("id")&&map.containsKey("checkstatus"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		//判断id是否为空
		if(map.get("id").toString().trim().equals("")||map.get("checkstatus").toString().trim().equals("")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		String idStr = map.get("id").toString();
		String checkstatusStr = map.get("checkstatus").toString();
		int id = 0;
		try{
			id = Integer.parseInt(idStr);			
			boolean result = false;
			CollectJob collectJob = new CollectJob();
			collectJob.setId(id);
			collectJob.setCheckResult(checkstatusStr);
			
			Metadata metadata = new Metadata();
			metadata.setCHECKSTATUS(checkstatusStr);
			metadata.setID(id);
			result = (metaDataService.updateMetadataCheckstatus(metadata) && collectJobService.updateCollectJobCheckResult(collectJob));
			System.out.println(result);
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
	 * 作用:根据入参id删除元数据
	 * 参数:id,flag
	 * 
	 */
	@RequestMapping(value = "/deleteMetadataById",method=RequestMethod.POST)
	@ResponseBody
	@Transactional
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
	@Transactional
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
			metaData.setID(id);
			metaData.setDESCRIPTION(describe);
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
			JSONObject json = JSONObject.fromObject(metaData.getATTRIBUTES());
			List<Metamodel_datatype> privateMetaModels = metamodel_datatypeService.getMetamodel_datatypeByMetaModelId(metaData.getMETAMODELID());
			data.put("元数据id", metaData.getID());
			data.put("元数据名称", metaData.getNAME());
			data.put("元数据业务说明", metaData.getDESCRIPTION());
			data.put("元数据入库时间", metaData.getCREATETIME());
			data.put("元数据修改时间", metaData.getUPDATETIME());
			data.put("元数据的版本号", metaData.getVERSION());
			data.put("采集元数据的编号", metaData.getCOLLECTJOBID());
			data.put("审核状态", metaData.getCHECKSTATUS());
			data.put("所属元模型id", metaData.getMETAMODELID());
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
	 * 时间:2018年4月8日
	 * 作用:根据connectinfo信息，kettle自动采集元数据
	 * 参数：id(connectinfo的id)
	 * @throws KettleException 
	 */
	@RequestMapping(value = "/metadataAutoCollect",method=RequestMethod.POST)
	@ResponseBody
	@Transactional
	@Log(operationType="metadata,metadata_relation",operationDesc="kettle自动采集关系型数据库元数据")
	public JSONObject metadataAutoCollect(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) throws KettleException{
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!map.containsKey("id") && !map.containsKey("name") && !map.containsKey("checkResult")
				&& !map.containsKey("multipleSelection")){
			responsejson.put("result", false);
			responsejson.put("description", "传输参数 错误");
			responsejson.put("count",0);
			return responsejson;
		}
		
		//定义返回的数据
		JSONArray data = new JSONArray();
		
		//获取数据源id
		String idstr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idstr);
		} catch (Exception e) {}	
		//获取采集任务名称
		String name = map.get("name").toString();
		//获取审核状态
		String checkResult = map.get("checkResult").toString();		
		//获取数据源编号
		int connectinfoid = id;
		//获取采集方式
		String mode ="All";
		//获取当前日期
		Date createDate = null;
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			createDate = sdf.parse(sdf.format(new Date()));
		}catch(Exception e){}
		//获取当前的用户名
		Subject subject = SecurityUtils.getSubject();  
		Session session = subject.getSession();
		String creater = session.getAttribute("username").toString();
		//获取数据源的详细连接信息
	
		Connectinfo connectinfo = connectinfoService.getConnectinfoByid(id);
		
		//删除以前采集的元数据
		List<Connectinfo> connectinfoList = connectinfoService.getConnectinfoListBymountmetadataid(connectinfo.getMountMetaDataId());
		for(Connectinfo connect : connectinfoList){
			CollectJob oldCollectJob = collectJobService.getRecentCollectJobByConnectinfoId(id);
			if(oldCollectJob != null){
				metaDataService.deleteMetadataByCollectJobId(oldCollectJob.getId());		
			}
		}	

		
		CollectJob newCollectJob = new CollectJob(name,connectinfoid,mode,checkResult,createDate,creater);			
		collectJobService.insertCollectJob(newCollectJob);				
	
		try {
			
			String json = JSON.toJSONString(map, true);
			HashMap parseMap = JSON.parseObject(json, HashMap.class);
			List<com.alibaba.fastjson.JSONObject> tableList = (List<com.alibaba.fastjson.JSONObject>) parseMap.get("multipleSelection");
			
			//解析需要采集的表名并封装成Table类
			List<Table> tables = new ArrayList<>();
			
			for(com.alibaba.fastjson.JSONObject tableName :tableList){
				Table table = new Table();
				table.setName(tableName.getString("tablename"));
				table.setOperationDescription(null);
				table.setOperationName(null);
				tables.add(table);
			}
			
			//表示接下来采集元数据
			Metadata metadata = metaDataService.getMetadataById(connectinfo.getMountMetaDataId());
			int mountmodelid = metadata.getMETAMODELID();
			
			if(connectinfo.getType().equals("file")){
				File_connectinfo file_connectinfo = file_connectinfoService.getFile_connectinfoListByparentid(id);
				String filename = file_connectinfo.getFilename();
				if(file_connectinfo.getFiletype() == 1){					
					fileMetadataCollectService.collectExcelMetaData(filename,newCollectJob.getId(),createDate,connectinfo.getMountMetaDataId());
				}else if(file_connectinfo.getFiletype() == 2){
					fileMetadataCollectService.collectJSONMetadata(filename,newCollectJob.getId(),createDate,connectinfo.getMountMetaDataId());
				}else if(file_connectinfo.getFiletype() == 3){
					fileMetadataCollectService.collectXmlMetadata(filename,newCollectJob.getId(),createDate,connectinfo.getMountMetaDataId());
				}else if(file_connectinfo.getFiletype() == 4){
					fileMetadataCollectService.collectTXTMetadata(filename, ";",newCollectJob.getId(),createDate,connectinfo.getMountMetaDataId());;
				}
				JSONObject node = new JSONObject();
				node.put("tablename", file_connectinfo.getFilename());
				node.put("operationname", null);
				node.put("operationdescribe", null);
				data.add(node);
			}else if(connectinfo.getType().equals("database")){
				if(mountmodelid == 10){	
					Datasource_connectinfo datasource_connectinfo = datasource_connectinfoService.getDatasource_connectinfoListByparentid(id);	

					//采集的数据库和表的数量以及大小
					String tableSize ;
					//采集的字段的数量以及大小
					String fieldSize ;
					
					tableSize = kettleMetadataCollectService.collectDataBaseAndTableMetaData(datasource_connectinfo,newCollectJob.getId(),createDate,tables,connectinfo.getMountMetaDataId());
					fieldSize = kettleMetadataCollectService.collectFieldMetaData(datasource_connectinfo,newCollectJob.getId(),createDate,tables);				
					
					for(Table table : tables){
						JSONObject node = new JSONObject();
						node.put("tablename", table.getName());
						node.put("operationname", table.getOperationName());
						node.put("operationdescribe", table.getOperationDescription());
						data.add(node);
					}			
					String[] temp = tableSize.split("_");
					responsejson.put("tableLength",temp[0]);
					responsejson.put("tableSize",CalDataSize.getPrintSize(Integer.parseInt(temp[1])));
					
					temp = fieldSize.split("_");				
					responsejson.put("fieldLength",temp[0]);
					responsejson.put("fieldSize",CalDataSize.getPrintSize(Integer.parseInt(temp[1])));
		

				}else if(mountmodelid == 202){
					Datasource_connectinfo datasource_connectinfo = datasource_connectinfoService.getDatasource_connectinfoListByparentid(id);	
					String repositoryName = connectinfo.getName();				
					kettleMetadataCollectService.collectKettleJob(datasource_connectinfo,newCollectJob.getId(),createDate,repositoryName,tables,connectinfo.getMountMetaDataId());
				
					for(Table table : tables){
						JSONObject node = new JSONObject();
						node.put("tablename", table.getName());
						node.put("operationname", table.getOperationName());
						node.put("operationdescribe", table.getOperationDescription());
						data.add(node);
					}	
					responsejson.put("collectionId",newCollectJob.getId());	
				}
			}
			
			responsejson.put("collectionId",newCollectJob.getId());	
			responsejson.put("result", true);
			responsejson.put("data",data);
			responsejson.put("count",data.size());

		} catch (Exception e) {
			e.printStackTrace();
			collectJobService.deleteById(newCollectJob.getId());
			responsejson.put("result", false);
			responsejson.put("count",0);
		}

		return responsejson;
	}
	
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年4月11日
	 * 作用:判断数据源是否已经采集
	 * 参数：id(数据源的id)
	 */
	@RequestMapping(value = "/judgeConnectExist",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="null",operationDesc="判断数据源是否已经采集")
	public JSONObject judgeConnectExist(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
			responsejson.put("description", "传输参数 错误");
			responsejson.put("count",0);
			return responsejson;
		}
		
		//获取数据源id
		String idstr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idstr);
		} catch (Exception e) {
		}
		
		Connectinfo connectinfo = connectinfoService.getConnectinfoByid(id);
		List<Connectinfo> connectinfoList = connectinfoService.getConnectinfoListBymountmetadataid(connectinfo.getMountMetaDataId());
		
		try {
			for(Connectinfo connect : connectinfoList){
				List<CollectJob> collectJob = collectJobService.getCollectJobByConnectinfoId(connect.getId());
				if(collectJob.size() > 1){
					responsejson.put("result", false);
					responsejson.put("flag",0);
					responsejson.put("desciption","数据源已经采集");
					responsejson.put("count",1);
					return responsejson;
				}
			}				
			responsejson.put("result", true);
			responsejson.put("flag",1);
			responsejson.put("desciption","数据源未采集");
			responsejson.put("count",1);
		} catch (Exception e) {
			e.printStackTrace();
			responsejson.put("result", false);
			responsejson.put("flag",2);
			responsejson.put("count",0);
		}

		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年4月8日
	 * 作用:根据数据源信息获取表结构，并判断采集名称是否正确
	 * 参数：id(数据源的id)
	 */
	@RequestMapping(value = "/getTables",method=RequestMethod.POST)
	@ResponseBody
	@Transactional
	@Log(operationType="null",operationDesc="根据数据源信息获取表结构，并判断采集名称是否正确")
	public JSONObject getTables(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!map.containsKey("id") && !map.containsKey("name") && !map.containsKey("checkResult")){
			responsejson.put("result", false);
			responsejson.put("description", "传输参数 错误");
			responsejson.put("count",0);
			return responsejson;
		}
		
		//获取数据源id
		String idstr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idstr);
		} catch (Exception e) {
		}
		
		//获取采集任务名称并判断是否存在
		String name = map.get("name").toString();
		if(name == null || name.isEmpty()){
			responsejson.put("result", false);
			responsejson.put("description", "采集名称不能为空");
			responsejson.put("count",0);
			return responsejson;
		}else if(!collectJobService.isExistName(name)){
			responsejson.put("result", false);
			responsejson.put("description", "采集名称已经存在");
			responsejson.put("count",0);
			return responsejson;
		}
		
		try {
			Connectinfo connectinfo = connectinfoService.getConnectinfoByid(id);
			if(connectinfo.getType().equals("file")){
				JSONArray data = new JSONArray();
				JSONObject node = new JSONObject();
				node.put("tablename", connectinfo.getName());
				node.put("operationname", connectinfo.getName());
				node.put("operationdescribe", connectinfo.getDescription());
				data.add(node);
				responsejson.put("result", true);
				responsejson.put("data",data);
				responsejson.put("count",data.size());
			}else if (connectinfo.getType().equals("database")){
				Metadata metadata = metaDataService.getMetadataById(connectinfo.getMountMetaDataId());
				Datasource_connectinfo datasource_connectinfo = datasource_connectinfoService.getDatasource_connectinfoListByparentid(id);	
				//CollectJob collectJob = collectJobService.getRecentCollectJobByConnectinfoId(id);
				List<Table> tables = kettleMetadataCollectService.getTables(datasource_connectinfo,metadata.getMETAMODELID());
				JSONArray data = new JSONArray();
				for(Table table : tables){
					JSONObject node = new JSONObject();
					node.put("tablename", table.getName());
					node.put("operationname", table.getOperationName());
					node.put("operationdescribe", table.getOperationDescription());
					data.add(node);
				}
				responsejson.put("result", true);
				responsejson.put("data",data);
				responsejson.put("count",data.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
			responsejson.put("result", false);
			responsejson.put("count",0);
		}

		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年4月10日
	 * 作用:根据采集任务id获取采集任务详细信息
	 * 参数：id(采集任务的id)
	 */
	@RequestMapping(value = "/getCollectJobById",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="null",operationDesc="根据采集任务id获取采集任务详细信息")
	public JSONObject getCollectJobById(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
			responsejson.put("description", "传输参数 错误");
			responsejson.put("count",0);
			return responsejson;
		}
		
		String idstr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idstr);
		} catch (Exception e) {
		}
		
		try {
			CollectJob collectJob = kettleMetadataCollectService.getCollectJobById(id);
			JSONArray data = new JSONArray();
			JSONObject node = new JSONObject();				
			node.put("id", collectJob.getId());
			node.put("name", collectJob.getName());
			node.put("databasename",connectinfoService.getConnectinfoByid(collectJob.getConnectinfoId()).getName());
			node.put("createdate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(collectJob.getCreateDate()));
			node.put("creater", collectJob.getCreater());
			node.put("checkresult", collectJob.getCheckResult().equals("1")?"已审核":"未审核");
			data.add(node);

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
			System.out.println("出现问题");
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
				node.put("checkresult", kettleMetadataCollectService.getCheckResult(collectJob.getCheckResult()));
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
				node.put("checkresult",kettleMetadataCollectService.getCheckResult(collectJob.getCheckResult()));
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
	 * 时间:2018年3月28日
	 * 作用:根据id获取采集任务
	 * 
	 */
	@RequestMapping(value = "/getMetadataCollectById",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="collectjob",operationDesc="根据id获取采集任务")
	public JSONObject getMetadataCollectById(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {
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
			CollectJob collectJob = collectJobService.getCollectJobById(id);
			if(collectJob == null){
				responsejson.put("result",true);
				responsejson.put("description", "今日没采集日志");
				responsejson.put("count",0);
			}else{
				data.put("id", collectJob.getId());
				data.put("name", collectJob.getName());
				data.put("databasename",connectinfoService.getConnectinfoByid(collectJob.getConnectinfoId()).getName());
				data.put("createdate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(collectJob.getCreateDate()));
				data.put("creater", collectJob.getCreater());
				data.put("checkresult", kettleMetadataCollectService.getCheckResult(collectJob.getCheckResult()));		
				responsejson.put("result",true);
				responsejson.put("data",data);
				responsejson.put("count",data.size());
			}
		}catch(Exception e){
			e.printStackTrace();			
			responsejson.put("result",true);
			responsejson.put("description", "今日没采集日志");
			responsejson.put("count",0);
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
				node.put("id", metadata.getID());
				node.put("label", metadata.getNAME());
				data.add(node);
			}
//			metadatas = metaDataService.getMetadataByMetaModelId(202);
//			for(Metadata metadata : metadatas){
//				JSONObject node = new JSONObject();
//				node.put("id", metadata.getID());
//				node.put("label", metadata.getNAME());
//				data.add(node);
//			}
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
