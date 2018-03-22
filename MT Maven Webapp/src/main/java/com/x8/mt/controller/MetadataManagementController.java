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
import com.x8.mt.service.MetadataManagementService;
/**
 * 作者： allen
 * 时间：2018年3月15日
 * 作用：
 */
@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/metadata_management")
public class MetadataManagementController {
	@Resource
	MetadataManagementService metadataManagementService;
	
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
		
		responsejson.put("result", true);
		responsejson.put("data", fieldMetadatas);
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
