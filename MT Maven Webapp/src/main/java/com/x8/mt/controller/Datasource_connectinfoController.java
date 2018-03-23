package com.x8.mt.controller;

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
import com.x8.mt.entity.Connectinfo;
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.entity.Metadata;
import com.x8.mt.service.CollectJobService;
import com.x8.mt.service.ConnectinfoService;
import com.x8.mt.service.Datasource_connectinfoService;
import com.x8.mt.service.MetaDataService;

@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/datasource_connectinfo")
public class Datasource_connectinfoController {
	@Resource
	Datasource_connectinfoService datasource_connectinfoService;
	@Resource
	ConnectinfoService connectinfoService;	
	@Resource
	CollectJobService collectJobService;
	@Resource
	MetaDataService metaDataService;	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据ID获取所用数据源信息
	 * 参数：id
	 */
	@RequestMapping(value = "/getConnectinfo",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="connectinfo",operationDesc="根据ID获取所用数据源信息")
	public JSONObject getConnectinfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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

		String idStr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {
		}
		
		JSONArray data = new JSONArray();
		Connectinfo connectinfo = connectinfoService.getConnectinfoByid(id);
		
		JSONObject connectInfoId = new JSONObject();
		connectInfoId.put("key","ID");
		connectInfoId.put("value", connectinfo.getId());
		data.add(connectInfoId);

		JSONObject name = new JSONObject();
		name.put("key","数据源名称");
		name.put("value", connectinfo.getName());
		data.add(name);
		
		JSONObject type = new JSONObject();
		type.put("key","数据源类型");
		type.put("value", connectinfo.getType());
		data.add(type);
		
		JSONObject describe = new JSONObject();
		describe.put("key","数据源描述");
		describe.put("value", connectinfo.getDescription());
		data.add(describe);
		
//		JSONObject systemName = new JSONObject();
//		systemName.put("key","所属系统");
//		systemName.put("value", datasource_connectinfoService.getPath(connectinfo.getId()));
//		data.add(systemName);
//		
		responsejson.put("result", true);
		responsejson.put("data", data);
		//获取记录数，为日志所用
		responsejson.put("count", 1);
		return responsejson;
	}
	
//	/**
//	 * 
//	 * 作者:GodDispose
//	 * 时间:2018年3月14日
//	 * 作用:根据系统ID获取所属数据源
//	 * 参数：id
//	 */
//	@RequestMapping(value = "/getConnectinfoBySystem",method=RequestMethod.POST)
//	@ResponseBody
//	@Log(operationType="connectinfo",operationDesc="根据系统ID获取所属数据源")
//	public JSONObject getConnectinfoBySystem(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
//		if(!map.containsKey("id")){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
//
//		String idStr = map.get("id").toString();
//		int id = 0;
//		try {
//			id = Integer.parseInt(idStr);		
//			JSONArray data = new JSONArray();
//			List<Connectinfo> connectinfoList = connectinfoService.getConnectinfoListByparentid(id);
//			
//			for(Connectinfo connectinfo : connectinfoList){
//				JSONObject node = new JSONObject();
//				node.put("id",connectinfo.getId());
//				node.put("name", connectinfo.getName());
//				data.add(node);
//			}	
//			
//			responsejson.put("result", true);
//			responsejson.put("data", data);
//			//获取记录数，为日志所用
//			responsejson.put("count", data.size());
//		} catch (Exception e) {
//			responsejson.put("result", false);
//			responsejson.put("count", 0);
//		}		
//		
//		return responsejson;
//	}
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:根据数据源ID获取所用数据源连接信息
	 * 参数：id
	 */
	@RequestMapping(value = "/getDataSource_Connectinfo",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="datasource_connectinfo",operationDesc="根据数据源ID获取所用数据源连接信息")
	public JSONObject getDataSource_Connectinfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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

		String idStr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {
		}
		
		JSONArray data = new JSONArray();
		Datasource_connectinfo datasource_connectinfo = datasource_connectinfoService.getDatasource_connectinfoListByparentid(id);
		
		JSONObject datasource_connectinfoId = new JSONObject();
		datasource_connectinfoId.put("key","ID");
		datasource_connectinfoId.put("value", datasource_connectinfo.getParentid());
		data.add(datasource_connectinfoId);
		
		JSONObject url = new JSONObject();
		url.put("key","主机名");
		url.put("value", datasource_connectinfo.getUrl());
		data.add(url);
		
		JSONObject port = new JSONObject();
		port.put("key","端口号");
		port.put("value", datasource_connectinfo.getPort());
		data.add(port);
		
		JSONObject name = new JSONObject();
		name.put("key","数据库名称");
		name.put("value", datasource_connectinfo.getDatabasename());
		data.add(name);
		
		JSONObject type = new JSONObject();
		type.put("key","数据库类型");
		type.put("value", datasource_connectinfo.getDatabasetype());
		data.add(type);
		
		JSONObject userName = new JSONObject();
		userName.put("key","数据库用户名");
		userName.put("value", datasource_connectinfo.getUsername());
		data.add(userName);
		
		JSONObject userPassword = new JSONObject();
		userPassword.put("key","数据库密码");
		userPassword.put("value", datasource_connectinfo.getPassword());
		data.add(userPassword);
		
		responsejson.put("result", true);
		responsejson.put("data", data);
		//获取记录数，为日志所用
		responsejson.put("count", 1);
		return responsejson;
	}
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:更新一条Datasource_connectinfo记录
	 * 参数：id,url、port、username、password、databasename、databasetype、parentid
	 * 		databasetype--enum('postgresql','oracle','mysql')
	 * 		如果要更新desribe为空，就传desribe为""
	 */
	@RequestMapping(value = "/updateDatasource_connectinfo",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="datasource_connectinfo",operationDesc="更新一条Datasource_connectinfo记录")
	public JSONObject updateDatasource_connectinfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
		
		if(map.containsKey("databasetype")){
			//确保databasetype传参的枚举类型值
			if((!map.get("databasetype").toString().equals(GlobalMethodAndParams.databasetype_MYSQL))&&
					(!map.get("databasetype").toString().equals(GlobalMethodAndParams.databasetype_ORACLE))&&
						(!map.get("databasetype").toString().equals(GlobalMethodAndParams.databasetype_POSTGRESQL))){
				responsejson.put("result", false);
				responsejson.put("count",0);
				return responsejson;
			}
		}

		Datasource_connectinfo datasource_connectinfo = new Datasource_connectinfo();
		datasource_connectinfo.setParentid(Integer.parseInt(map.get("id").toString()));;
		if(map.containsKey("url")){
			datasource_connectinfo.setUrl(map.get("url").toString());
		}
		if(map.containsKey("port")){
			datasource_connectinfo.setPort(map.get("port").toString());
		}
		if(map.containsKey("username")){
			datasource_connectinfo.setUsername(map.get("username").toString());
		}
		if(map.containsKey("password")){
			datasource_connectinfo.setPassword(map.get("password").toString());
		}
		if(map.containsKey("databasename")){
			datasource_connectinfo.setDatabasename(map.get("databasename").toString());
		}	
		if(map.containsKey("databasetype")){
			datasource_connectinfo.setDatabasetype(map.get("databasetype").toString());
		}
		
		boolean result = datasource_connectinfoService.updateDatasource_connectinfo(datasource_connectinfo);
		responsejson.put("result", result);
		if(result){
			responsejson.put("count",1);
		}else{
			responsejson.put("count",0);
		}
		return responsejson;
	}

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:更新一条connectinfo记录
	 * 参数：id,name(可选),describe(可选)
	 */
	@RequestMapping(value = "/updateConnectinfo",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="connectinfo",operationDesc="更新一条connectinfo记录")
	public JSONObject updateConnectinfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
		
		Connectinfo connectinfo = new Connectinfo();
		connectinfo.setId(Integer.parseInt(map.get("id").toString()));
		if(map.containsKey("name")){
			connectinfo.setName(map.get("name").toString());
		}
		if(map.containsKey("describe")){
			connectinfo.setDescription(map.get("describe").toString());
		}
		
		boolean result = connectinfoService.updateConnectinfoNameOrDescriptionById(connectinfo);

		responsejson.put("result", result);
		if(result){
			responsejson.put("count",1);
		}else{
			responsejson.put("count",0);
		}
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2018年3月14日
	 * 作用:删除一条数据源记录
	 * 参数：id
	 */
	@RequestMapping(value = "/deleteConnectinfo",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="connectinfo",operationDesc="删除一条数据源记录")
	public JSONObject deleteConnectinfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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

		String idStr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {
		}
		
		boolean result = connectinfoService.deleteConnectInfoById(id);

		responsejson.put("result", result);
		if(result){
			responsejson.put("count",1);
		}else{
			responsejson.put("count",0);
		}
		return responsejson;
	}


	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:插入一条数据源记录
	 * 参数：name、type、url、port、username、password、databasename、databasetype、desribe（可选）、parentid
	 * 		databasetype--enum('postgresql','oracle','mysql')
	 */
	@RequestMapping(value = "/insertConnectinfo",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="connectinfo",operationDesc="插入数据源")
	public JSONObject insertConnectinfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();
		
//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("name")&&
			map.containsKey("type")&&
			map.containsKey("mountmetadataid")&&
			map.containsKey("needcheck")&&
			map.containsKey("url")&&
			map.containsKey("port")&&
			map.containsKey("username")&&
			map.containsKey("password")&&
			map.containsKey("databasename")&&
			map.containsKey("databasetype"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		//确保type传参的枚举类型值
		if(!(map.get("type").toString().equals("database") || map.get("type").toString().equals("file"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}		
		
		//确保databasetype传参的枚举类型值
		if((!map.get("databasetype").toString().equals(GlobalMethodAndParams.databasetype_MYSQL))&&
				(!map.get("databasetype").toString().equals(GlobalMethodAndParams.databasetype_ORACLE))&&
					(!map.get("databasetype").toString().equals(GlobalMethodAndParams.databasetype_POSTGRESQL))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		//插入数据源信息记录		
		Connectinfo connectInfo = new Connectinfo();
		connectInfo.setName(map.get("name").toString());
		connectInfo.setType(map.get("type").toString());
		if(map.containsKey("describe")){
			connectInfo.setDescription(map.get("describe").toString());
		}
		connectInfo.setMountMetaDataId(Integer.parseInt(map.get("mountmetadataid").toString()));
		connectInfo.setNeedCheck(Integer.parseInt(map.get("needcheck").toString()));
		System.out.println(connectInfo.getMountMetaDataId());
		System.out.println(connectInfo.getNeedCheck());
		if(!connectinfoService.insertConnectinfo(connectInfo)){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		//插入数据源连接信息记录
		Datasource_connectinfo datasource_connectinfo = new Datasource_connectinfo();

		datasource_connectinfo.setUrl(map.get("url").toString());
		datasource_connectinfo.setPort(map.get("port").toString());
		datasource_connectinfo.setUsername(map.get("username").toString());
		datasource_connectinfo.setPassword(map.get("password").toString());
		datasource_connectinfo.setDatabasename(map.get("databasename").toString());
		datasource_connectinfo.setDatabasetype(map.get("databasetype").toString());
		datasource_connectinfo.setParentid(connectInfo.getId());
		boolean result = datasource_connectinfoService.insertDatasource_connectinfo(datasource_connectinfo);
		
		responsejson.put("result", result);
		if(result){
			responsejson.put("count",1);
		}else{
			responsejson.put("count",0);
		}
		return responsejson;
	}

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:根据数据源id获取采集的表
	 * 参数：id
	 */
	@RequestMapping(value = "/getTables",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata_relationtable",operationDesc="根据数据源id获取采集的表")
	public JSONObject getTables(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();
		
//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("id") && map.containsKey("flag"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		String idstr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idstr);
			CollectJob collectJob = collectJobService.getCollectJobByConnectinfoId(id);
			List<Metadata> metaDatas = metaDataService.getMetadataByMetaModelId(collectJob.getId());
						
			JSONArray data = new JSONArray();
			for(Metadata metaData : metaDatas){
				JSONObject node = new JSONObject();
				node.put("id", metaData.getId());
				node.put("name", metaData.getName());
				data.add(node);
			}
			
			responsejson.put("result", true);
			responsejson.put("data", data);
			responsejson.put("count", data.size());

		} catch (Exception e) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
		}		

		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:根据数据源id获取采集的数据库
	 * 参数：id
	 */
	@RequestMapping(value = "/getDatabaseName",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="datasource_connectinfo",operationDesc="根据数据源id获取采集的数据库")
	public JSONObject getDatabaseName(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
			
			Datasource_connectinfo datasource_connectinfo = datasource_connectinfoService.getDatasource_connectinfoListByparentid(id);
			
			JSONArray data = new JSONArray();
			JSONObject node = new JSONObject();
			node.put("id", datasource_connectinfo.getId());
			node.put("name", datasource_connectinfo.getDatabasename());
			data.add(node);
			
			responsejson.put("result", true);
			responsejson.put("data", data);
			responsejson.put("count", 1);

		} catch (Exception e) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
		}		

		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:根据字段id获取字段描述
	 * 参数：id
	 */
	@RequestMapping(value = "/getDescribe",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="根据字段id获取字段描述")
	public JSONObject getDescribe(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
			
			Metadata metaData = metaDataService.getMetadataById(id);
			
			JSONObject data = new JSONObject();
			data.put("describe", metaData.getDescription());
			responsejson.put("result", true);
			responsejson.put("data", data);
			responsejson.put("count", 1);

		} catch (Exception e) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
		}		

		return responsejson;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:根据数据源id，表，字段名获取数据
	 * 参数：datasourceid,tableid,


	 */
//	@RequestMapping(value = "/getData",method=RequestMethod.POST)
//	@ResponseBody
//	@Log(operationType="datasource_connectinfo",operationDesc="根据数据源id，表，字段名获取数据")
//	public JSONObject getData(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
//		if(!(map.containsKey("datasourceid") && map.containsKey("tableid") && map.containsKey("fieldid"))){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
//		
//		String sourceIdstr = map.get("datasourceid").toString();
//		String tableIdstr = map.get("tableid").toString();
//		String fieldIdStr = map.get("fieldid").toString();
//		int sourceId = 0;
//		int tableId = 0;
//		int fieldId =0;
//		try {
//			sourceId = Integer.parseInt(sourceIdstr);
//			tableId = Integer.parseInt(tableIdstr);
//			fieldId =Integer.parseInt(fieldIdStr);
//			Datasource_connectinfo datasource_connectinfo = datasource_connectinfoService.getDatasource_connectinfoListByparentid(sourceId);
//			
//			Metadata metaDataTable = metaDataService.getMetadataById(tableId);
//			Metadata metaDataField = metaDataService.getMetadataById(fieldId);		
//			List<String> temp = connectinfoService.getData(datasource_connectinfo, metaDataTable.getName(), metaDataField.getName());
//			JSONArray data = new JSONArray();					
//			if(temp.size()>6){
//				for(int i = 0 ;i < 6 ;i++){
//					JSONObject node = new JSONObject();
//					node.put("example",temp.get(i));
//					data.add(node);
//				}
//			}else{
//				for(int i = 0 ;i < temp.size() ;i++){
//					JSONObject node = new JSONObject();
//					node.put("example",temp.get(i));
//					data.add(node);
//				}
////				for(int i = 6 ;i > temp.size() ;i--){
////					JSONObject node = new JSONObject();
////					node.put("example",null);
////					data.add(node);
////				}
//			}
//
//			responsejson.put("result", true);
//			responsejson.put("data", data);
//			responsejson.put("count", data.size());
//		}catch (Exception e) {
//			e.printStackTrace();
//			responsejson.put("result", false);
//			responsejson.put("count", 0);
//		}		
//
//		return responsejson;
//	}
//	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:获取数据源
	 * 
	 */
	@RequestMapping(value = "/getAllConnectinfo",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="connectinfo",operationDesc="获取数据源")
	public JSONObject getAllConnectinfo(HttpServletRequest request,HttpServletResponse response){
		JSONObject responsejson = new JSONObject();
		
//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		try {
			List<Connectinfo> connectinfoList = connectinfoService.getConnectinfoList();
			JSONArray data = new JSONArray();
			for (Connectinfo connectinfo : connectinfoList) {
				JSONObject node = new JSONObject();
				node.put("id", connectinfo.getId());
				node.put("name", connectinfo.getName());
				data.add(node);
			}
			
			responsejson.put("result", true);
			responsejson.put("data", data);
			responsejson.put("count", data.size());
		} catch (Exception e) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
		}		
		
		return responsejson;
	}
	
	
}
