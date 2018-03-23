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
import com.x8.mt.entity.Metamodel_hierarchy;
import com.x8.mt.service.Metamodel_hierarchyService;

@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/metamodel_hierarchy")
public class Metamodel_hierarchyController {

	@Resource
	Metamodel_hierarchyService metamodel_hierarchyService;
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年3月12日
	 * 作用:获得所有的元模型包
	 */
	@RequestMapping(value = "/getAllMetamodel_package",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="metamodel_hierarchy",operationDesc="获得所有的元模型包")
	public JSONObject getAllMetamodel_package(HttpServletRequest request,HttpServletResponse response){
		JSONObject responsejson = new JSONObject();
		
//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);

		List<Metamodel_hierarchy> metamodelPackageList = metamodel_hierarchyService.getAllMetamodel_package();
		
		JSONArray data = new JSONArray();
		for (Metamodel_hierarchy metamodel_hierarchy : metamodelPackageList) {
			JSONObject json = new JSONObject();
			json.put("id", metamodel_hierarchy.getId());
			json.put("name", metamodel_hierarchy.getName());
			data.add(json);
		}
		responsejson.put("result", true);
		responsejson.put("data", data);
		responsejson.put("count", 1);
		return responsejson;
	}
	
//	/**
//	 * 
//	 * 作者:itcoder
//	 * 时间:2018年3月12日
//	 * 作用:获得元模型结构树
//	 */
//	@RequestMapping(value = "/getMetamodel_packageTree",method=RequestMethod.GET)
//	@ResponseBody
//	@Log(operationType="metamodel_hierarchy",operationDesc="获得元模型结构树")
//	public JSONObject getMetamodel_packageTree(HttpServletRequest request,HttpServletResponse response){
//		JSONObject responsejson = new JSONObject();
//		
////		if(!GlobalMethodAndParams.checkLogin()){
////			responsejson.put("result", false);
////			responsejson.put("count",0);
////			return responsejson;
////		}
//		GlobalMethodAndParams.setHttpServletResponse(request, response);
//
//		
//		
//		responsejson.put("result", true);
////		responsejson.put("data", data);
//		responsejson.put("count", 1);
//		return responsejson;
//	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年3月14日
	 * 作用:根据入参id,更新元模型的name,describe
	 * 参数：元模型的id,name,description
	 */
	@RequestMapping(value = "/updateMetamodel_hierarchy",method=RequestMethod.POST)
	@Log(operationType="metamodel_hierarchy",operationDesc="根据入参id,更新元模型的name,description")
	@ResponseBody
	public JSONObject updateMetamodel_hierarchy(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();
		
//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("id")&&map.containsKey("name"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
	        return responsejson;
		}
		
		// 检查传参是否为null
		if (map.get("id") == null||map.get("name")==null) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}
		Metamodel_hierarchy metamodel_hierarchy = new Metamodel_hierarchy();
		String idStr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {
		}
		metamodel_hierarchy.setId(id);
		metamodel_hierarchy.setName(map.get("name").toString());
		if(map.get("description")!=null){
			metamodel_hierarchy.setDesribe(map.get("description").toString());			
		}
		
		boolean result = metamodel_hierarchyService.updateMetamodel_hierarchy(metamodel_hierarchy);
		responsejson.put("result",result);
		if(result){
			responsejson.put("count",1);
		}else{
			responsejson.put("count",0);
		}
        return responsejson;
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年3月12日
	 * 作用:根据某个包或者元模型的id，获取该对象的名称和描述
	 * 参数：某个包或者元模型的id
	 */
	@RequestMapping(value = "/getMetamodel_hierarchyAttribute",method=RequestMethod.POST)
	@Log(operationType="metamodel_hierarchy",operationDesc="根据某个包或者元模型的id，获取该对象的名称和描述")
	@ResponseBody
	public JSONObject getMetamodel_hierarchyAttribute(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
		
		// 检查传参是否为null
		if (map.get("id") == null) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}
		
		String idStr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {
		}
		JSONObject attributeObject = new JSONObject();
		Metamodel_hierarchy metamodel_hierarchy = metamodel_hierarchyService.getMetamodel_hierarchy(id);
		attributeObject.put("name", metamodel_hierarchy.getName());
		if(metamodel_hierarchy.getDesribe()==null){
			attributeObject.put("describe", "");
		}else{
			attributeObject.put("describe", metamodel_hierarchy.getDesribe());
		}
		
		responsejson.put("result",true);
		responsejson.put("data", attributeObject);
		responsejson.put("count", 1);
        return responsejson;
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年3月13日
	 * 作用:删除元模型或元模型包，元模型包有元模型的时候，级联删除元模型
	 * 参数：删除元模型或元模型包的id
	 */
	@RequestMapping(value = "/deleteMetamodel_hierarchy",method=RequestMethod.POST)
	@Log(operationType="metamodel_hierarchy",operationDesc="根据入参id,删除元模型或元模型包[级联删除包下的所有元模型]")
	@ResponseBody
	public JSONObject deleteMetamodel_hierarchy(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
		
		// 检查传参是否为null
		if (map.get("id") == null) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}
		
		String idStr = map.get("id").toString();
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {
		}
		
		boolean result = metamodel_hierarchyService.deleteMetamodel_hierarchy(id);
		
		responsejson.put("result",result);
		if(result){
			responsejson.put("count",1);
		}else{
			responsejson.put("count",0);
		}
        return responsejson;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月17日
	 * 作用:删除包[只有包下面没有元模型时才可以删除]
	 * 参数：需要删除包的id(已经废弃)
	 */
	@RequestMapping(value = "/deleteMetamodel_hierarchy_PACKAGE",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metamodel_hierarchy",operationDesc="根据包id,删除包")
	public JSONObject deleteMetamodel_hierarchy_PACKAGE(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
		boolean result = metamodel_hierarchyService.deleteMetamodel_hierarchy_PACKAGE(id);
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
	 * 作者:allen
	 * 时间:2017年11月16日
	 * 作用:得到元模型包结构树
	 */
	@RequestMapping(value = "/getMetamodelPackageTree",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="metamodel_hierarchy",operationDesc="得到元模型包树结构")
	public JSONObject getMetamodelPackageTree(HttpServletRequest request,HttpServletResponse response){
		JSONObject responsejson = new JSONObject();
		
//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
        
		JSONArray data = new JSONArray();
		//获取系统元模型包
		JSONArray getSystemMetamodelPackageTree = metamodel_hierarchyService.getMetamodelPackageTree(GlobalMethodAndParams.systemMetamodelPackageId);
		for(int i = 0 ; i < getSystemMetamodelPackageTree.size();i++){
			data.add(getSystemMetamodelPackageTree.get(i));
		}
		
		//获取自定义元模型包
		JSONArray getSelfMetamodelPackageTree = metamodel_hierarchyService.getMetamodelPackageTree(GlobalMethodAndParams.selfMetamodelPackageId);
		for(int i = 0 ; i < getSelfMetamodelPackageTree.size();i++){
			data.add(getSelfMetamodelPackageTree.get(i));
		}
		
		responsejson.put("result", true);
		responsejson.put("data",data);
		responsejson.put("count",2);
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月16日
	 * 作用:新增一条Metamodel_hierarchy记录
	 * 参数：name、type、parentid[desribeurl可选] 
	 * 		如果是包类型  parentid为""
	 */
	@RequestMapping(value = "/insertMetamodel_hierarchy",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metamodel_hierarchy",operationDesc="插入一条Metamodel_hierarchy记录")
	public JSONObject insertMetamodel_hierarchy(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
				map.containsKey("parentid"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		Metamodel_hierarchy metamodel_hierarchy = new Metamodel_hierarchy();
		metamodel_hierarchy.setName(map.get("name").toString());
		if(map.get("type").toString().equals(GlobalMethodAndParams.metamodel_hierarchy_PACKAGE)){
			metamodel_hierarchy.setType(map.get("type").toString());
			metamodel_hierarchy.setParentid(Integer.parseInt(map.get("parentid").toString()));
		}else if(map.get("type").toString().equals(GlobalMethodAndParams.metamodel_hierarchy_METAMODEL)){
			metamodel_hierarchy.setType(map.get("type").toString());
			metamodel_hierarchy.setParentid(Integer.parseInt(map.get("parentid").toString()));
		}else{//type枚举类型不合法
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		if(map.containsKey("desribe")){
			metamodel_hierarchy.setDesribe(map.get("desribe").toString());
		}
		
		boolean result = metamodel_hierarchyService.insertMetamodel_hierarchy(metamodel_hierarchy);
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
	 * 作者:itcoder
	 * 时间:2018年3月13日
	 * 作用:根据元模型包id得到元模型包结构
	 */
	@RequestMapping(value = "/getSonMetamodel_hierarchy",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metamodel_hierarchy",operationDesc="根据元模型包id得到元模型包结构")
	public JSONObject getSonMetamodel_hierarchy(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
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
		//判断id是否是一级包id
		if(metamodel_hierarchyService.getSelfMetamodel_hierarchyParentid(id)){
			responsejson.put("result", false);
			responsejson.put("count",0);
	        return responsejson;
		}
		List<Metamodel_hierarchy> MetamodelPackageTreeList = metamodel_hierarchyService.getSonMetamodel_hierarchy(id);
		JSONArray data = new JSONArray();
		for (Metamodel_hierarchy metamodel_hierarchy : MetamodelPackageTreeList) {
			JSONObject json = new JSONObject();
			json.put("id", metamodel_hierarchy.getId());
			json.put("name", metamodel_hierarchy.getName());
			data.add(json);
		}
		responsejson.put("result", true);
		responsejson.put("data", data);
		responsejson.put("count", 2);
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月4日
	 * 作用:插入一条Metamodel记录[只有元模型名字不能为null，入参id不能为null]
	 * 参数：name、id、describe[describe可选]
	 * 		
	 */
	@RequestMapping(value = "/insertMetamodel",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metamodel_hierarchy",operationDesc="插入一条Metamodel记录[只有元模型名字不能为null，入参id不能为null]")
	public JSONObject insertMetamodel(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();
		
//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//检查传参是否正确
		if(!(map.containsKey("id")&&
				map.containsKey("name"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		//检查传参是否为null
		if (!(map.get("id")!=null&&map.get("name")!=null)) {
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}
		
		//判断插入的元模型名称是否重复，重复则添加失败
		Metamodel_hierarchy metamodel_hierarchy = new Metamodel_hierarchy();
		String name = map.get("name").toString();//获取传入的元模型名称
		int id = Integer.parseInt(map.get("id").toString());
		List<Metamodel_hierarchy> metamodelList = metamodel_hierarchyService.getSonMetamodel_hierarchy(id);
		for (Metamodel_hierarchy metamodel : metamodelList) {
			if(name.equals(metamodel.getName())){
				responsejson.put("result", false);
				responsejson.put("count", 0);
				return responsejson;
			}
		}
		
		metamodel_hierarchy.setName(name);
		metamodel_hierarchy.setParentid(id);
		metamodel_hierarchy.setType(GlobalMethodAndParams.metamodel_hierarchy_METAMODEL);
		if(map.containsKey("desribe")){
			metamodel_hierarchy.setDesribe(map.get("desribe").toString());
		}
		
		boolean result = metamodel_hierarchyService.insertMetamodel_hierarchy(metamodel_hierarchy);
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
	 * 作用:查询所有能被悬挂的元模型
	 * 		
	 */
	@RequestMapping(value = "/getMetaModelByMountMode",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="metamodel_hierarchy",operationDesc="插入一条Metamodel记录[只有元模型名字不能为null，入参id不能为null]")
	public JSONObject getMetaModelByMountMode(HttpServletRequest request,HttpServletResponse response){
		JSONObject responsejson = new JSONObject();
		
//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		List<Metamodel_hierarchy> metaModels = metamodel_hierarchyService.getMetaModelByMountNode();
		JSONArray data = new JSONArray();
		for(Metamodel_hierarchy metaModel : metaModels){
			JSONObject node = new JSONObject();
			node.put("value", metaModel.getId());
			node.put("label", metaModel.getName());
			data.add(node);
		}
		
		responsejson.put("result", true);
		responsejson.put("data", data);
		responsejson.put("count", data.size());
		
		return responsejson;
	}
}
