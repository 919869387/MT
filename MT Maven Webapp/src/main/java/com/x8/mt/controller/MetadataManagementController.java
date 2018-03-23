package com.x8.mt.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
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
import com.x8.mt.entity.Metadata;
import com.x8.mt.service.MetadataManagementService;
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
	 * 		parentMetadataId [父元数据id]
	 * 		。。。。。
	 */
	@RequestMapping(value = "/addMetadataStepTwo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="新增元数据步骤二")
	public JSONObject addMetadataStepTwo(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("metamodelId")&&map.containsKey("parentMetadataId"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		

		
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:新增元数据,判断是否可以添加。如果可以添加返回相应元模型的信息
	 * 	步骤一:判断传入的metamodelid下面是否有组合关系的id，有的话前端页面进行步骤二
	 *  这里新增的不包含：
	 *  
	 * 参数： metamodelId [这里的元模型id是父亲元模型的id]
	 */
	@RequestMapping(value = "/addMetadataStepOne", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="新增元数据步骤一")
	public JSONObject addMetadataStepOne(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!map.containsKey("metamodelId")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metamodelIdStr = map.get("metamodelId").toString();

		//根据metamodelId找到所有组合关系的元模型
		JSONObject metamodelNames = metadataManagementService.getCOMPOSITIONMetamodel(metamodelIdStr);
		if(metamodelNames==null){
			//说明该类元数据下面不能再加元数据
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		JSONObject metamodelInfos =  new JSONObject();
		
		Iterator iterator = metamodelNames.keys();  
		while(iterator.hasNext()){  
			String metamodelid = (String) iterator.next();
			/**
			 * 这里处理特殊元数据的添加
			 */
			JSONObject metamodelInfo = metadataManagementService.getMetamodelInfoForAddMetadata(metamodelid);
			metamodelInfos.put(metamodelid, metamodelInfo);
		}  


		responsejson.put("result", true);
		responsejson.put("metamodelNames", metamodelNames);
		responsejson.put("metamodelInfos", metamodelInfos);
		responsejson.put("count", metamodelNames.size());
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

		GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("metadataId")&&map.containsKey("metamodelId"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metadataIdStr = map.get("metadataId").toString();
		String metamodelIdStr = map.get("metamodelId").toString();

		Object metadataMap= metadataManagementService.getMetadata(metadataIdStr);
		JSONObject metamodelInfo = metadataManagementService.getMetamodelInfo(metamodelIdStr);

		responsejson.put("result", true);
		responsejson.put("data", metadataMap);
		responsejson.put("metamodelInfo", metamodelInfo);
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

		GlobalMethodAndParams.setHttpServletResponse(request, response);

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
	//	@RequestMapping(value = "/getMetadataViewTree", method = RequestMethod.POST)
	//	@ResponseBody
	//	@Log(operationType="metadata",operationDesc="查找元数据视图树")
	//	public JSONObject getMetadataViewTree(HttpServletRequest request,
	//			HttpServletResponse response,@RequestBody Map<String, Object> map){
	//		JSONObject responsejson = new JSONObject();
	//
	//		GlobalMethodAndParams.setHttpServletResponse(request, response);
	//
	//		//检查传参是否正确
	//		if(!map.containsKey("viewid")){
	//			responsejson.put("result", false);
	//			responsejson.put("count",0);
	//			return responsejson;
	//		}
	//
	//		String viewidStr = map.get("viewid").toString();
	//		int viewid = 0;
	//		try {
	//			viewid = Integer.parseInt(viewidStr);
	//		} catch (Exception e) {
	//		}
	//		
	//		JSONArray metadataViewTree= metadataManagementService.getMetadataViewTree(viewid);
	//		
	//		responsejson.put("result", true);
	//		responsejson.put("data", metadataViewTree);
	//		responsejson.put("count", 1);
	//		return responsejson;
	//	}

	@RequestMapping(value = "/getMetadataViewTree", method = RequestMethod.GET)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="查找元数据视图树")
	public JSONObject getMetadataViewTree(HttpServletRequest request,
			HttpServletResponse response){
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);

		String viewidStr = "1";
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
