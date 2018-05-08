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

import com.x8.mt.common.CalDataSize;
import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.entity.CollectJob;
import com.x8.mt.entity.Connectinfo;
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.entity.File_connectinfo;
import com.x8.mt.entity.Metadata;
import com.x8.mt.service.CollectJobService;
import com.x8.mt.service.ConnectinfoService;
import com.x8.mt.service.Datasource_connectinfoService;
import com.x8.mt.service.File_connectinfoService;
import com.x8.mt.service.MetaDataService;
import com.x8.mt.service.Metamodel_datatypeService;

@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/datasource_connectinfo")
public class Datasource_connectinfoController {
	@Resource
	Datasource_connectinfoService datasource_connectinfoService;
	@Resource
	File_connectinfoService file_connectinfoService;
	@Resource
	ConnectinfoService connectinfoService;	
	@Resource
	CollectJobService collectJobService;
	@Resource
	MetaDataService metaDataService;
	@Resource
	Metamodel_datatypeService metamodel_datatypeService;
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
		System.out.println(connectinfo.getDescription());
		describe.put("value", connectinfo.getDescription() == null ? "":connectinfo.getDescription());
		data.add(describe);
		
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
	 * 作用:根据数据库元数据ID获取所用数据源连接信息
	 * 参数：id
	 */
	@RequestMapping(value = "/getDataSource_Connectinfo",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="根据数据库元数据ID获取所用数据源连接信息")
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
//		Metadata metaData = metaDataService.getMetadataById(id);
//		JSONObject json = JSONObject.fromObject(metaData.getAttributes());
//		List<Metamodel_datatype> privateMetaModels = metamodel_datatypeService.getMetamodel_datatypeByMetaModelId(metaData.getMetaModelId());
//		for(Metamodel_datatype pri : privateMetaModels){
//			JSONObject node = new JSONObject();
//			node.put("key", pri.getDesribe());
//			node.put("value", json.get(pri.getName()));
//			data.add(node);
//		}	
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
	 * 时间:2018年5月8日
	 * 作用:根据数据库元数据ID获取所用文件连接信息
	 * 参数：id
	 */
	@RequestMapping(value = "/getFile_Connectinfo",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="file_connectinfo",operationDesc="根据数据库元数据ID获取所用文件连接信息")
	public JSONObject getFile_Connectinfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
		File_connectinfo file_connectinfo = file_connectinfoService.getFile_connectinfoListByparentid(id);
		
		JSONObject file_connectinfoId = new JSONObject();
		file_connectinfoId.put("key","ID");
		file_connectinfoId.put("value", file_connectinfo.getParentid());
		data.add(file_connectinfoId);
		
		JSONObject path = new JSONObject();
		path.put("key","文件路径");
		path.put("value", file_connectinfo.getPath());
		data.add(path);
		
		JSONObject filename = new JSONObject();
		filename.put("key","文件名");
		filename.put("value", file_connectinfo.getFilename());
		data.add(filename);
		
		JSONObject size = new JSONObject();
		size.put("key","文件大小");
		size.put("value", file_connectinfo.getSize());
		data.add(size);
		
		JSONObject type = new JSONObject();
		type.put("key","文件类型");
		if(file_connectinfo.getFiletype() == 1){
			type.put("value","EXCEL");
		}else if (file_connectinfo.getFiletype() == 2){
			type.put("value","JSON");
		}else {
			type.put("value", file_connectinfo.getFiletype() == 3 ? "XML" :"TXT");
		}
		data.add(type);
		
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
		 * 时间:2018年5月8日
		 * 作用:更新文件数据源
		 * 参数：name、filename、filetype、desribe（可选）
		 * 		filetype--enum(1,2,3,4)
		 */
		@RequestMapping(value = "/updateFile_connectinfo",method=RequestMethod.POST)
		@ResponseBody
		@Log(operationType="connectinfo",operationDesc="更新文件数据源")
		public JSONObject updateFile_connectinfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
			
			//更新文件数据源连接信息记录
			File_connectinfo file_connectinfo = new File_connectinfo();
			file_connectinfo.setParentid(Integer.parseInt(map.get("id").toString()));;
			if(map.containsKey("filename")){
				file_connectinfo.setFilename(map.get("filename").toString());
			}
			if(map.containsKey("filetype")){
				file_connectinfo.setFiletype(Integer.parseInt(map.get("filetype").toString()));
			}
			if(map.containsKey("size")){
				file_connectinfo.setSize(map.get("size").toString());
			}
			
			boolean result = file_connectinfoService.insertFile_connectinfo(file_connectinfo);
			
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
	@RequestMapping(value = "/insertConnectinfoBySelf",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="connectinfo",operationDesc="插入数据源")
	public JSONObject insertConnectinfoBySelf(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();
		
//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("name")&&
			map.containsKey("mountmetadataid")&&
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
		
		//确保databasetype传参的枚举类型值
		if((!map.get("databasetype").toString().equals(GlobalMethodAndParams.databasetype_MYSQL))&&
				(!map.get("databasetype").toString().equals(GlobalMethodAndParams.databasetype_ORACLE))&&
					(!map.get("databasetype").toString().equals(GlobalMethodAndParams.databasetype_SQLSERVER))&&
						(!map.get("databasetype").toString().equals(GlobalMethodAndParams.databasetype_POSTGRESQL))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		if(connectinfoService.getConnectinfoByName(map.get("name").toString()) != null){
			responsejson.put("result", false);
			responsejson.put("description", "数据源名称已经存在");
			responsejson.put("count",0);
			return responsejson;
		}
		
		//插入数据源信息记录		
		Connectinfo connectInfo = new Connectinfo();
		connectInfo.setName(map.get("name").toString());
		connectInfo.setType("database");
		if(map.containsKey("describe")){
			connectInfo.setDescription(map.get("describe").toString());
		}
		
		int metaModelId = Integer.parseInt(map.get("mountmetadataid").toString());
		connectInfo.setMountMetaDataId(metaModelId);
		//connectInfo.setNeedCheck(Integer.parseInt(map.get("checkstatus").toString()));
		connectInfo.setNeedCheck(1);
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
		if(metaModelId == 202){
			datasource_connectinfo.setRepositoryname(map.get("resourceUserName").toString());
			datasource_connectinfo.setRepositorypwd(map.get("resourcePassword").toString());
		}
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
	 * 时间:2018年5月8日
	 * 作用:插入一条数据源记录
	 * 参数：name、filename、filetype、desribe（可选）
	 * 		filetype--enum(1,2,3,4)
	 */
	@RequestMapping(value = "/insertFileConnectinfoBySelf",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="connectinfo",operationDesc="插入数据源")
	public JSONObject insertFileConnectinfoBySelf(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();
		
//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("name")
				&& map.containsKey("filename")
				&& map.containsKey("filetype"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}	
		
		if(connectinfoService.getConnectinfoByName(map.get("name").toString()) != null){
			responsejson.put("result", false);
			responsejson.put("description", "数据源名称已经存在");
			responsejson.put("count",0);
			return responsejson;
		}
		
		//插入数据源信息记录		
		Connectinfo connectInfo = new Connectinfo();
		connectInfo.setName(map.get("name").toString());
		connectInfo.setType("file");
		if(map.containsKey("describe")){
			connectInfo.setDescription(map.get("describe").toString());
		}
		
		//int metaModelId = Integer.parseInt(map.get("mountmetadataid").toString());
		connectInfo.setMountMetaDataId(110);
		//connectInfo.setNeedCheck(Integer.parseInt(map.get("checkstatus").toString()));
		connectInfo.setNeedCheck(1);
		if(!connectinfoService.insertConnectinfo(connectInfo)){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		//插入文件数据源连接信息记录
		File_connectinfo file_connectinfo = new File_connectinfo();
		file_connectinfo.setPath(GlobalMethodAndParams.PATH_NAME);
		file_connectinfo.setFilename(map.get("filename").toString());
		file_connectinfo.setFiletype(Integer.parseInt(map.get("filetype").toString()));
		file_connectinfo.setSize(map.get("size").toString());
		file_connectinfo.setParentid(connectInfo.getId());
		
		boolean result = file_connectinfoService.insertFile_connectinfo(file_connectinfo);
		
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
	 * 时间:2018年3月24日
	 * 作用:插入一条数据源记录
	 * 参数：id
	 */
	@RequestMapping(value = "/insertConnectinfoByMetadata",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="connectinfo",operationDesc="插入数据源")
	public JSONObject insertConnectinfoByMetadata(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
		
		String idstr = map.get("id").toString();
		int id = 0;
		id = Integer.parseInt(idstr);
		
		Metadata metadata = metaDataService.getMetadataById(id);

		
		if(connectinfoService.getConnectinfoByName(metadata.getNAME()) != null){
			responsejson.put("result", false);
			responsejson.put("description", "数据源名称已经存在");
			responsejson.put("count",0);
			return responsejson;
		}
		try {

			JSONObject json = JSONObject.fromObject(metadata.getATTRIBUTES());
			//插入数据源信息记录		
			Connectinfo connectInfo = new Connectinfo();
			connectInfo.setName(metadata.getNAME());
			connectInfo.setType("database");
			connectInfo.setMountMetaDataId(metadata.getMETAMODELID());
			connectInfo.setNeedCheck(1);
			if(!connectinfoService.insertConnectinfo(connectInfo)){
				responsejson.put("result", false);
				responsejson.put("count",0);
				return responsejson;
			}
			
			//插入数据源连接信息记录
			Datasource_connectinfo datasource_connectinfo = new Datasource_connectinfo();
	
			datasource_connectinfo.setUrl(json.get("dbip").toString());
			datasource_connectinfo.setPort(json.get("dbport").toString());
			datasource_connectinfo.setUsername(json.get("dbuser").toString());
			datasource_connectinfo.setPassword(json.get("dbpassword").toString());
			datasource_connectinfo.setDatabasename(json.get("dbname").toString());
			datasource_connectinfo.setDatabasetype(json.get("dbtype").toString());
			datasource_connectinfo.setParentid(connectInfo.getId());
			if(metadata.getMETAMODELID() == 202){
				datasource_connectinfo.setRepositoryname(map.get("reposuser").toString());
				datasource_connectinfo.setRepositorypwd(map.get("repospassword").toString());
			}
			
			boolean result = datasource_connectinfoService.insertDatasource_connectinfo(datasource_connectinfo);
			responsejson.put("result", result);
			if(result){
				responsejson.put("count",1);
			}else{
				responsejson.put("count",0);
			}
		}catch(Exception e){
			responsejson.put("result", false);
			responsejson.put("count",0);
		}
		return responsejson;
	}
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月8日
	 * 作用:插入一条数据源记录
	 * 参数：name、filename、filetype、desribe（可选）
	 * 		filetype--enum(1,2,3,4)
	 */
	@RequestMapping(value = "/insertFileConnectinfoByMetadata",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="connectinfo",operationDesc="插入数据源")
	public JSONObject insertFileConnectinfoByMetadata(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
		
		String idstr = map.get("id").toString();
		int id = 0;
		id = Integer.parseInt(idstr);
		
		Metadata metadata = metaDataService.getMetadataById(id);

		
		if(connectinfoService.getConnectinfoByName(metadata.getNAME()) != null){
			responsejson.put("result", false);
			responsejson.put("description", "数据源名称已经存在");
			responsejson.put("count",0);
			return responsejson;
		}
		
		try {
			JSONObject json = JSONObject.fromObject(metadata.getATTRIBUTES());
			//插入数据源信息记录		
			Connectinfo connectInfo = new Connectinfo();
			connectInfo.setName(metadata.getNAME());
			connectInfo.setType("file");
			if(json.containsKey("describe")){
				connectInfo.setDescription(metadata.getDESCRIPTION());
			}
			
			//int metaModelId = Integer.parseInt(map.get("mountmetadataid").toString());
			connectInfo.setMountMetaDataId(110);
			//connectInfo.setNeedCheck(Integer.parseInt(map.get("checkstatus").toString()));
			connectInfo.setNeedCheck(1);
			if(!connectinfoService.insertConnectinfo(connectInfo)){
				responsejson.put("result", false);
				responsejson.put("count",0);
				return responsejson;
			}
			
			//插入文件数据源连接信息记录
			File_connectinfo file_connectinfo = new File_connectinfo();
			file_connectinfo.setPath(GlobalMethodAndParams.PATH_NAME);
			file_connectinfo.setFilename(json.get("filename").toString());
			file_connectinfo.setFiletype(Integer.parseInt(json.get("filetype").toString()));
			if(json.get("filelength") != null){
				file_connectinfo.setSize(CalDataSize.getPrintSize(Long.parseLong(json.get("filelength").toString())));
			}
			file_connectinfo.setParentid(connectInfo.getId());
			
			boolean result = file_connectinfoService.insertFile_connectinfo(file_connectinfo);
			
			responsejson.put("result", result);
			if(result){
				responsejson.put("count",1);
			}else{
				responsejson.put("count",0);
			}
		}catch(Exception e){
			responsejson.put("result", false);
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
			CollectJob collectJob = collectJobService.getRecentCollectJobByConnectinfoId(id);
			List<Metadata> metaDatas = metaDataService.getMetadataByCollectJobById(collectJob.getId());
						
			JSONArray data = new JSONArray();
			for(Metadata metaData : metaDatas){
				JSONObject node = new JSONObject();
				node.put("id", metaData.getID());
				node.put("name", metaData.getNAME());
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
			data.put("describe", metaData.getDESCRIPTION());
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
				node.put("label", connectinfo.getName());
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
