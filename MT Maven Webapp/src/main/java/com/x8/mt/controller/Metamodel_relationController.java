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
import com.x8.mt.entity.Metamodel_relation;
import com.x8.mt.service.Metamodel_relationService;

@CrossOrigin(origins = "*", maxAge = 3600)
// 解决跨域请求的注解
@Controller
@RequestMapping(value = "/metamodel_relation")
public class Metamodel_relationController {

	@Resource
	Metamodel_relationService metamodel_relationService;

	/**
	 * 
	 * 作者:itcoder 
	 * 时间:2018年4月10日 
	 * 作用:获得所有的依赖关系
	 */
	@RequestMapping(value = "/getMetamodel_relationDEPENDENCY", method = RequestMethod.GET)
	@ResponseBody
	@Log(operationType = "getMetamodel_relationDEPENDENCY", operationDesc = "获得所有的依赖关系")
	public JSONObject getMetamodel_relationDEPENDENCY(
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject responsejson = new JSONObject();

		// if(!GlobalMethodAndParams.checkLogin()){
		// responsejson.put("result", false);
		// responsejson.put("count",0);
		// return responsejson;
		// }
		GlobalMethodAndParams.setHttpServletResponse(request, response);

		List<Metamodel_relation> dependencyRelationList = metamodel_relationService
				.getDependencyRelation();

		JSONArray data = new JSONArray();
		for (Metamodel_relation metamodel_relation : dependencyRelationList) {
			JSONObject json = new JSONObject();
			json.put("id", metamodel_relation.getId());
			json.put("name", metamodel_relation.getRelatedmetamodelid());
			data.add(json);
		}
		responsejson.put("result", true);
		responsejson.put("data", data);
		responsejson.put("count", 1);
		return responsejson;
	}

	/**
	 * 
	 * 作者:itcoder 
	 * 时间:2018年4月10日 
	 * 作用:增加元模型关系
	 * 参数：metamodelid，relatedmetamodelid，type都不能为空
	 */
	@RequestMapping(value = "/insertMetamodel_relation", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType = "insertMetamodel_relation", operationDesc = "增加元模型关系")
	public JSONObject insertMetamodel_relation(HttpServletRequest request,
			HttpServletResponse response, @RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

		// if(!GlobalMethodAndParams.checkLogin()){
		// responsejson.put("result", false);
		// responsejson.put("count",0);
		// return responsejson;
		// }
		GlobalMethodAndParams.setHttpServletResponse(request, response);

		if (!(map.containsKey("metamodelid")
				&& map.containsKey("relatedmetamodelid") && map
					.containsKey("type"))) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}

		if (map.get("metamodelid").toString().trim().equals("")
				|| map.get("relatedmetamodelid").toString().trim().equals("")
				|| map.get("type").toString().trim().equals("")) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}
		
		Metamodel_relation metamodel_relation = new Metamodel_relation();
		metamodel_relation.setMetamodelid(Integer.parseInt(map.get("metamodelid").toString()));
		metamodel_relation.setRelatedmetamodelid(Integer.parseInt(map.get("relatedmetamodelid").toString()));
		metamodel_relation.setType(GlobalMethodAndParams.metamodel_relation_dependency);
		
		boolean flag = metamodel_relationService.insertMetamodel_relation(metamodel_relation);
		
		if(flag){
			responsejson.put("result", true);
			responsejson.put("count", 1);
			return responsejson;
		}else{
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}
		
	}
	
	/**
	 * 
	 * 作者:itcoder 
	 * 时间:2018年4月10日
	 * 作用:删除元模型关系
	 * 参数：id
	 */
	@RequestMapping(value = "/deleteMetamodel_relation", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType = "deleteMetamodel_relation", operationDesc = "删除元模型关系")
	public JSONObject deleteMetamodel_relation(HttpServletRequest request,
			HttpServletResponse response, @RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

		// if(!GlobalMethodAndParams.checkLogin()){
		// responsejson.put("result", false);
		// responsejson.put("count",0);
		// return responsejson;
		// }
		GlobalMethodAndParams.setHttpServletResponse(request, response);

		if (!(map.containsKey("id"))) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}

		if (map.get("id").toString().trim().equals("")) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}
		
		int id = Integer.parseInt(map.get("id").toString().trim());
		
		boolean flag = metamodel_relationService.deleteMetamodel_relation(id);
		
		if(flag){
			responsejson.put("result", true);
			responsejson.put("count", 1);
			return responsejson;
		}else{
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}
		
	}

}
