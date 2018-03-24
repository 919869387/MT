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
import com.x8.mt.entity.Metamodel_datatype;
import com.x8.mt.entity.Metamodel_hierarchy;
import com.x8.mt.service.Metamodel_datatypeService;
import com.x8.mt.service.Metamodel_hierarchyService;

@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/metamodel_datatype")
public class Metamodel_datatypeController {
	@Resource
	Metamodel_datatypeService metamodel_datatypeService;
	
	@Resource
	Metamodel_hierarchyService metamodel_hierarchyService;
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年3月16日
	 * 作用:根据id删除元模型数据项
	 */
	@RequestMapping(value = "/deleteMetamodel_datatype",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metamodel_datatype",operationDesc="根据id删除元模型数据项")
	public JSONObject deleteMetamodel_datatype(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
		
		//判断是否为null
		if(map.get("id")==null){
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
		
		boolean result = metamodel_datatypeService.deleteMetamodel_datatype(id);	
		
		responsejson.put("result", result);
		if(result){
			responsejson.put("count", 1);
		}else{
			responsejson.put("count", 0);
		}
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年3月16日
	 * 作用:修改元模型数据项
	 */
	@RequestMapping(value = "/modifyMetamodel_datatype",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metamodel_datatype",operationDesc="修改元模型数据项")
	public JSONObject modifyMetamodel_datatype(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("id")&&map.containsKey("name")&&map.containsKey("type")&&map.containsKey("desribe"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		//判断是否为null
		if(map.get("id").toString().equals("")||map.get("name").toString().equals("")||map.get("type").toString().equals("")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		Metamodel_datatype metamodel_datatype = new Metamodel_datatype();
		String idStr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
			metamodel_datatype.setId(id);
		} catch (Exception e) {
		}
		
		metamodel_datatype.setName(map.get("name").toString());
		metamodel_datatype.setType(map.get("type").toString());
		if(map.get("desribe")!=null){
			metamodel_datatype.setDesribe(map.get("desribe").toString());
		}
		
		boolean result = metamodel_datatypeService.modifyMetamodel_datatype(metamodel_datatype);	
		
		responsejson.put("result", result);
		if(result){
			responsejson.put("count", 1);
		}else{
			responsejson.put("count", 0);
		}
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年3月16日
	 * 作用:添加元模型数据项
	 * 参数：name,type,description,metamodelid,category
	 */
	@RequestMapping(value = "/addMetamodel_datatype",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metamodel_datatype",operationDesc="添加元模型数据项")
	public JSONObject addMetamodel_datatypee(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("name")&&map.containsKey("type")&&map.containsKey("metamodelid")&&map.containsKey("category"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		//判断是否为null
		if(map.get("name").toString().equals("")||map.get("type").toString().equals("")||map.get("metamodelid").toString().equals("")||map.get("category").toString().equals("")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		Metamodel_datatype metamodel_datatype = new Metamodel_datatype();
		String metamodelidStr = map.get("metamodelid").toString();
		int metamodelid = 0;
		try {
			metamodelid = Integer.parseInt(metamodelidStr);
			metamodel_datatype.setMetamodelid(metamodelid);
		} catch (Exception e) {
		}
		
		metamodel_datatype.setName(map.get("name").toString());
		metamodel_datatype.setType(map.get("type").toString());
		if(map.get("description")!=null){
			metamodel_datatype.setDesribe(map.get("description").toString());
		}
		
		if(map.get("category")!=null){
			metamodel_datatype.setCategory(map.get("category").toString());
		}
		
		boolean result = metamodel_datatypeService.addMetamodel_datatype(metamodel_datatype);	
		
		responsejson.put("result", result);
		if(result){
			responsejson.put("count", 1);
		}else{
			responsejson.put("count", 0);
		}
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月17日
	 * 作用:通过元模型id,得到元模型的所有数据项
	 */
	@RequestMapping(value = "/getMetamodel_datatype",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metamodel_datatype",operationDesc="根据元模型id,得到元模型的所有数据项")
	public JSONObject getMetamodel_datatype(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
		
		List<Metamodel_datatype> metamodel_datatypes= metamodel_datatypeService.getMetamodel_datatypeByMetaModelId(id);
		
		if(metamodel_datatypes == null  || metamodel_datatypes.isEmpty()){
			responsejson.put("result", false);
			responsejson.put("count",0);
		}
		
		JSONArray data = new JSONArray();
		for(Metamodel_datatype metamodel_datatype : metamodel_datatypes){
			JSONObject json = new JSONObject();
			json.put("id", metamodel_datatype.getId());
			json.put("attribute",metamodel_datatype.getName());
			json.put("type",metamodel_datatype.getType());
			json.put("description",metamodel_datatype.getDesribe());
			json.put("metamodelid",metamodel_datatype.getMetamodelid() );
			json.put("category", metamodel_datatype.getCategory());
			data.add(json);
		}	
		
		responsejson.put("result", true);
		responsejson.put("data", data);
		if(metamodel_hierarchyService.getMetamodel_hierarchyById(id).getDesribe()==null){
			responsejson.put("description","");
		}else{
			responsejson.put("description",metamodel_hierarchyService.getMetamodel_hierarchyById(id).getDesribe());
		}
			responsejson.put("count",data.size());
		return responsejson;
	}
	
}
