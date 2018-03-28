package com.x8.mt.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.x8.mt.entity.MetadataViewNode;
import com.x8.mt.entity.Metamodel_hierarchy;
import com.x8.mt.service.MetadataManagementService;
import com.x8.mt.service.MetadataViewNodeService;
import com.x8.mt.service.Metamodel_hierarchyService;
/**
 * 作者： allen
 * 时间：2018年3月15日
 * 作用：
 */
@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/metadataManagement")
public class MetadataManagementController {
	@Resource
	MetadataManagementService metadataManagementService;
	@Resource
	Metamodel_hierarchyService metamodel_hierarchyService;
	@Resource
	MetadataViewNodeService metadataViewNodeService;

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月25日
	 * 作用:删除元数据信息
	 *  
	 * 参数： ID[元数据id]
	 */
	@RequestMapping(value = "/daleteMetadataInfo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="删除元数据信息")
	public JSONObject daleteMetadataInfo(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!map.containsKey("ID")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metadataId = map.get("ID").toString();

		if(!metadataManagementService.existMetadata(metadataId)){
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}

		List<Object> count = new ArrayList<Object>();
		if(metadataManagementService.daleteMetadataInfo(metadataId,count)){
			responsejson.put("result", true);
			responsejson.put("count", count.size());
		}else{
			responsejson.put("result", false);
			responsejson.put("count", count.size());
		}
		count.clear();
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月24日
	 * 作用:修改元数据信息
	 *  	1.将元数据在metadata表修改
	 *  	2.修改后的记录加入metadata_tank表
	 *  
	 * 参数： ID [元数据id]
	 * 		。。。。。
	 * 
	 * {
		"ID": 1248,
		"tablename": "metadata",
		"METAMODELID": 31,
		"NAME": "metadata",
		 "type":"COMMON",
		 "metadataTankid":1212
		}
	 */
	@RequestMapping(value = "/updateMetadataInfoStepTwo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="修改元数据信息步骤二")
	public JSONObject updateMetadataInfoStepTwo(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("ID")&&map.containsKey("METAMODELID"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		if(map.containsKey("metadataTankid")){
			//说明是更新私有属性
			if(metadataManagementService.updateMetadataInfoForPRIVATE(map)){
				responsejson.put("result", true);
				responsejson.put("count", 1);
			}else{
				responsejson.put("result", false);
				responsejson.put("count", 0);
			}
		}else{
			int metadataTankid = metadataManagementService.updateMetadataInfoForCommon(map);
			if(metadataTankid>0){
				responsejson.put("result", true);
				responsejson.put("metadataTankid", metadataTankid);
				responsejson.put("count", 1);
			}else{
				responsejson.put("result", false);
				responsejson.put("count", 0);
			}
		}
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月24日
	 * 作用:修改元数据信息步骤一,先显示可以修改的信息
	 *  
	 * 参数： metadataId[元数据id]
	 * 		metamodelId[元模型id]
	 * 		type[请求类型信息]
	 */
	@RequestMapping(value = "/updateMetadataInfoStepOne", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="修改元数据信息步骤一")
	public JSONObject updateMetadataInfoStepOne(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(!(map.containsKey("metadataId")&&map.containsKey("metamodelId")&&map.containsKey("type"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String type = map.get("type").toString();
		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//不可更新的字段
		List<String> noupdatefield = new ArrayList<String>();
		noupdatefield.add("COLLECTJOBID");
		noupdatefield.add("METAMODELID");
		noupdatefield.add("CHECKSTATUS");
		noupdatefield.add("VERSION");
		noupdatefield.add("UPDATETIME");
		noupdatefield.add("CREATETIME");
		noupdatefield.add("ID");

		//元模型的公共属性
		List<String> systemModelField= new ArrayList<String>();
		systemModelField.add("ID");
		systemModelField.add("NAME");
		systemModelField.add("DESCRIPTION");
		systemModelField.add("CREATETIME");
		systemModelField.add("UPDATETIME");
		systemModelField.add("VERSION");
		systemModelField.add("COLLECTJOBID");
		systemModelField.add("CHECKSTATUS");
		systemModelField.add("METAMODELID");

		String metadataIdStr = map.get("metadataId").toString();
		String metamodelIdStr = map.get("metamodelId").toString();

		Map<String, Object> metadataMap= metadataManagementService.getMetadata(metadataIdStr);
		JSONObject metamodelInfo = metadataManagementService.getMetamodelInfo(metamodelIdStr);

		JSONArray canUpdateInfo = new JSONArray();

		Iterator it = metadataMap.entrySet().iterator();  
		while (it.hasNext()) {
			JSONObject metadataInfo = new JSONObject();
			Entry entry = (Map.Entry) it.next();  
			String key = (String) entry.getKey(); 

			if(type.equals("COMMON")){
				if((systemModelField.contains(key)&&(!noupdatefield.contains(key)))){
					String cnKey = metamodelInfo.get(key).toString();
					Object value = entry.getValue();

					metadataInfo.put("key", key);
					metadataInfo.put("description", cnKey);
					metadataInfo.put("value", value);
					canUpdateInfo.add(metadataInfo);
				}
			}else{
				if((!systemModelField.contains(key)&&(!noupdatefield.contains(key)))){
					String cnKey = metamodelInfo.get(key).toString();
					Object value = entry.getValue();

					metadataInfo.put("key", key);
					metadataInfo.put("description", cnKey);
					metadataInfo.put("value", value);
					canUpdateInfo.add(metadataInfo);
				}
			}
		}

		responsejson.put("result", true);
		responsejson.put("canUpdateInfo", canUpdateInfo);
		responsejson.put("count", 1);
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:insert元数据,插入实际元数据
	 *  	1.将元数据加入到metadata表
	 *  	2.加入metadata_relation表
	 *  	3.加入metadata_tank表
	 *  
	 * 参数： metamodelId [元模型id]
	 * 		parentMetadataId [父元数据id]--如果添加第一层节点元数据,这个参数值为0
	 * 		。。。。。
	 */
	@RequestMapping(value = "/addMetadataStepThree", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="新增元数据步骤三")
	public JSONObject addMetadataStepThree(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("metamodelId")&&map.containsKey("parentMetadataId"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		if(metadataManagementService.addMetadata(map)){
			responsejson.put("result", true);
			responsejson.put("count", 1);
		}else{
			responsejson.put("result", false);
			responsejson.put("count", 0);
		}

		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:新增元数据第二步,获取具体要增加元数据的元模型的信息
	 *  
	 * 参数： metamodelId [要添加元数据的元模型id]
	 */
	@RequestMapping(value = "/addMetadataStepTwo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="新增元数据步骤二")
	public JSONObject addMetadataStepTwo(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!map.containsKey("metamodelId")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metamodelIdStr = map.get("metamodelId").toString();

		JSONArray metamodelInfos = metadataManagementService.getMetamodelInfoForAddMetadata(metamodelIdStr);

		responsejson.put("result", true);
		responsejson.put("metamodelInfos", metamodelInfos);
		responsejson.put("metamodelInfoCount", metamodelInfos.size());
		responsejson.put("count", 1);
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:新增元数据第一步,获取子元模型
	 *  
	 * 参数： metamodelId [这里的元模型id是父亲元模型的id]
	 */
	@RequestMapping(value = "/addMetadataStepOne", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="新增元数据步骤一")
	public JSONObject addMetadataStepOne(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//增加第一层元数据的逻辑
		if(map.containsKey("id")){
			String idStr = map.get("id").toString();
			
			List<MetadataViewNode> metadataViewNodes= metadataViewNodeService.getMetadataViewNode(idStr);
			
			List<JSONObject> includeMetaModel = new ArrayList<JSONObject>();
			
			for(MetadataViewNode metadataViewNode:metadataViewNodes){
				int modelid = metadataViewNode.getChildmetamodelid();
				Metamodel_hierarchy metamodel_hierarchy = metamodel_hierarchyService.getMetamodel_hierarchy(modelid);
				
				JSONObject metamodel = new JSONObject();
				metamodel.put("modelid", modelid);
				metamodel.put("name", metamodel_hierarchy.getName());
				
				includeMetaModel.add(metamodel);
			}
			
			responsejson.put("result", true);
			responsejson.put("metamodelname", metadataViewNodes.get(0).getName());
			responsejson.put("includeMetaModel", includeMetaModel);
			responsejson.put("includeCount", includeMetaModel.size());
			responsejson.put("count", includeMetaModel.size());
			return responsejson;
		}
			
			
		//检查传参是否正确
		if(!map.containsKey("metamodelId")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metamodelIdStr = map.get("metamodelId").toString();
		String metamodelname = metamodel_hierarchyService.getMetamodel_hierarchy(Integer.parseInt(metamodelIdStr)).getName();

		List<JSONObject> includeMetaModel = metadataManagementService.getCOMPOSITIONMetamodel(metamodelIdStr);

		responsejson.put("result", true);
		responsejson.put("metamodelname", metamodelname);
		responsejson.put("includeMetaModel", includeMetaModel);
		responsejson.put("includeCount", includeMetaModel.size());
		responsejson.put("count", includeMetaModel.size());
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:获取某一个元数据详细信息
	 * 
	 * 参数：metadataId [元数据id]
	 * 	   metamodelId [元模型id]
	 */
	@RequestMapping(value = "/getMetadataInfo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="查找元数据详细信息")
	public JSONObject getMetadataInfo(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("metadataId")&&map.containsKey("metamodelId"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metadataIdStr = map.get("metadataId").toString();
		String metamodelIdStr = map.get("metamodelId").toString();


		//元模型的公共属性
		List<String> systemModelField= new ArrayList<String>();
		systemModelField.add("ID");
		systemModelField.add("NAME");
		systemModelField.add("DESCRIPTION");
		systemModelField.add("CREATETIME");
		systemModelField.add("UPDATETIME");
		systemModelField.add("VERSION");
		systemModelField.add("COLLECTJOBID");
		systemModelField.add("CHECKSTATUS");
		systemModelField.add("METAMODELID");

		JSONArray CommonModelInfo= new JSONArray();
		JSONArray PrivateModelInfo= new JSONArray();

		Map<String, Object> metadataMap= metadataManagementService.getMetadata(metadataIdStr);
		JSONObject metamodelInfo = metadataManagementService.getMetamodelInfo(metamodelIdStr);

		Iterator it = metadataMap.entrySet().iterator();  
		while (it.hasNext()) {
			JSONObject metadataInfo = new JSONObject();
			Entry entry = (Map.Entry) it.next();  
			String key = (String) entry.getKey();  
			String cnKey = metamodelInfo.get(key).toString();
			Object value = entry.getValue(); 

			metadataInfo.put("key", cnKey);
			metadataInfo.put("value", value);
			if(systemModelField.contains(key)){
				CommonModelInfo.add(metadataInfo);
			}else{
				PrivateModelInfo.add(metadataInfo);
			}
		} 

		responsejson.put("result", true);
		responsejson.put("CommonModelInfo", CommonModelInfo);
		responsejson.put("PrivateModelInfo", PrivateModelInfo);
		responsejson.put("count", 1);
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月19日
	 * 作用:获取字段元数据
	 * 
	 * 参数：tableMetadataId [表元数据id]
	 */
	@RequestMapping(value = "/getFieldMetadata", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="查找字段元数据")
	public JSONObject getFieldMetadata(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!map.containsKey("tableMetadataId")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String tableMetadataIdStr = map.get("tableMetadataId").toString();
		int tableMetadataId = 0;
		try {
			tableMetadataId = Integer.parseInt(tableMetadataIdStr);
		} catch (Exception e) {
		}

		List<Object> fieldMetadatas= metadataManagementService.getFieldMetadata(tableMetadataId);
		JSONObject metamodelInfo = metadataManagementService.getMetamodelInfo(GlobalMethodAndParams.FieldMetamodelId);

		responsejson.put("result", true);
		responsejson.put("data", fieldMetadatas);
		responsejson.put("metamodelInfo", metamodelInfo);
		responsejson.put("count", fieldMetadatas.size());
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月17日
	 * 作用:查询得到元数据视图树(不包括字段元数据)
	 * 
	 * 参数：viewid [系统默认视图的viewid=1]
	 */
	@RequestMapping(value = "/getMetadataViewTree", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="查找元数据视图树")
	public JSONObject getMetadataViewTree(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!map.containsKey("viewid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String viewidStr = map.get("viewid").toString();
		int viewid = 0;
		try {
			viewid = Integer.parseInt(viewidStr);
		} catch (Exception e) {
		}

		JSONArray metadataViewTree= metadataManagementService.getMetadataViewTree(viewid);

		responsejson.put("result", true);
		responsejson.put("data", metadataViewTree);
		responsejson.put("count", 1);
		return responsejson;
	}
}
