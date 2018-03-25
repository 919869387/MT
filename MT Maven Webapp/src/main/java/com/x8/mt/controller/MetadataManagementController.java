package com.x8.mt.controller;

import java.util.ArrayList;
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
		"UPDATETIME": "2018-03-16 16:31:02",
		"DESCRIPTION": null,
		"VERSION": 1,
		"ID": 1248,
		"tablename": "metadata",
		"CREATETIME": "2018-03-16 16:31:02",
		"METAMODELID": 31,
		"CHECKSTATUS": "1",
		"COLLECTJOBID": 172,
		"NAME": "metadata"
	},
	"metamodelInfo": {
		"ID": "元数据id",
		"NAME": "元数据名称",
		"DESCRIPTION": "元数据业务说明",
		"CREATETIME": "元数据入库时间",
		"UPDATETIME": "元数据修改时间",
		"VERSION": "元数据的版本号",
		"COLLECTJOBID": "采集元数据的任务编号",
		"CHECKSTATUS": "审核状态",
		"METAMODELID": "所属的元模型id",
		"tablename": "表名"
	}
	不管属性有没有改，都要有
	 */
	@RequestMapping(value = "/updateMetadataInfoStepTwo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="修改元数据信息")
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

		if(metadataManagementService.updateMetadataInfo(map)){
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
	 * 时间:2017年3月24日
	 * 作用:修改元数据信息步骤一,先显示可以修改的信息
	 *  
	 * 参数： metadataId[元数据id]
	 * 		metamodelId[元模型id]
	 */
	@RequestMapping(value = "/updateMetadataInfoStepOne", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="修改元数据信息步骤一")
	public JSONObject updateMetadataInfoStepOne(HttpServletRequest request,
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

		Map<String, Object> metadataMap= metadataManagementService.getMetadata(metadataIdStr);
		JSONObject metamodelInfo = metadataManagementService.getMetamodelInfo(metamodelIdStr);
		
		List<String> noupdatefield = new ArrayList<String>();
		noupdatefield.add(GlobalMethodAndParams.Public_Metamodel_COLLECTJOBID);
		noupdatefield.add(GlobalMethodAndParams.Public_Metamodel_METAMODELID);
		noupdatefield.add(GlobalMethodAndParams.Public_Metamodel_CHECKSTATUS);
		noupdatefield.add(GlobalMethodAndParams.Public_Metamodel_VERSION);
		noupdatefield.add(GlobalMethodAndParams.Public_Metamodel_UPDATETIME);
		noupdatefield.add(GlobalMethodAndParams.Public_Metamodel_CREATETIME);
		noupdatefield.add(GlobalMethodAndParams.Public_Metamodel_ID);
		
		responsejson.put("result", true);
		responsejson.put("data", metadataMap);
		responsejson.put("metamodelInfo", metamodelInfo);
		responsejson.put("noupdatefield", noupdatefield);
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
	 * 		parentMetadataId [父元数据id]
	 * 		。。。。。
	 */
	@RequestMapping(value = "/addMetadataStepTwo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="新增元数据步骤二")
	public JSONObject addMetadataStepTwo(HttpServletRequest request,
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

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

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

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("metadataId")&&map.containsKey("metamodelId"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metadataIdStr = map.get("metadataId").toString();
		String metamodelIdStr = map.get("metamodelId").toString();

		Map<String, Object> metadataMap= metadataManagementService.getMetadata(metadataIdStr);
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
