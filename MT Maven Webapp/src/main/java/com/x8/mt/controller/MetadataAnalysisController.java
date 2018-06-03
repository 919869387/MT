package com.x8.mt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.x8.mt.common.Log;
import com.x8.mt.service.MetadataAnalysisService;

/**
 * 作者： allen
 * 时间：2018年5月3日
 * 作用：
 */
@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/metadataAnalysis")
public class MetadataAnalysisController {
	
	@Resource
	MetadataAnalysisService metadataAnalysisService;

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月3日
	 * 作用:字段影响分析
	 *  
	 * 参数：yingxiangid
	 */
	@RequestMapping(value = "/fieldImpact", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadataAnalysis",operationDesc="字段影响分析")
	public JSONObject fieldImpact(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();
		
		//检查传参是否正确
		if(!map.containsKey("yingxiangid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		String yingxiangidStr = map.get("yingxiangid").toString();
		
		List<Map<String,String>> nodesList = new ArrayList<Map<String, String>>();
		List<Map<String,String>> linksList = new ArrayList<Map<String, String>>();
		
		metadataAnalysisService.fieldAnalysis(yingxiangidStr,nodesList,linksList,false);//false时为影响分析
		
		if(nodesList.size()==0){//说明传入的元数据id不存在
			responsejson.put("result", false);
			responsejson.put("count",0);
		}else{
			JSONObject data = new JSONObject();
			data.put("nodes", nodesList);
			data.put("links", linksList);
			
			responsejson.put("result", true);
			responsejson.put("data",data);
			responsejson.put("count",1);
		}
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月4日
	 * 作用:字段血统分析
	 *  
	 * 参数：xuetongid
	 */
	@RequestMapping(value = "/fieldBlood", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadataAnalysis",operationDesc="字段血统分析")
	public JSONObject fieldBlood(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();
		
		//检查传参是否正确
		if(!map.containsKey("xuetongid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		String xuetongidStr = map.get("xuetongid").toString();
		
		List<Map<String,String>> nodesList = new ArrayList<Map<String, String>>();
		List<Map<String,String>> linksList = new ArrayList<Map<String, String>>();
		
		metadataAnalysisService.fieldAnalysis(xuetongidStr,nodesList,linksList,true);//为true时为血统分析
		
		if(nodesList.size()==0){//说明传入的元数据id不存在
			responsejson.put("result", false);
			responsejson.put("count",0);
		}else{
			JSONObject data = new JSONObject();
			data.put("nodes", nodesList);
			data.put("links", linksList);
			
			responsejson.put("result", true);
			responsejson.put("data",data);
			responsejson.put("count",1);
		}
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月4日
	 * 作用:字段全链分析
	 *  
	 * 参数：quanlianid
	 */
	@RequestMapping(value = "/fieldChain", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadataAnalysis",operationDesc="字段全链分析")
	public JSONObject fieldChain(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();
		
		//检查传参是否正确
		if(!map.containsKey("quanlianid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}
		
		String quanlianidStr = map.get("quanlianid").toString();
		
		List<Map<String,String>> nodesList = new ArrayList<Map<String, String>>();
		List<Map<String,String>> linksList = new ArrayList<Map<String, String>>();
		
		metadataAnalysisService.fieldAnalysis(quanlianidStr,nodesList,linksList,false);//false时为影响分析
		metadataAnalysisService.fieldAnalysis(quanlianidStr,nodesList,linksList,true);//为true时为血统分析
		metadataAnalysisService.addTableAndDatabaseInfoForfieldChain(quanlianidStr, nodesList, linksList);//为全连分析添加源节点的表和数据库
		
		if(nodesList.size()==0){//说明传入的元数据id不存在
			responsejson.put("result", false);
			responsejson.put("count",0);
		}else{
			JSONObject data = new JSONObject();
			data.put("nodes", nodesList);
			data.put("links", linksList);
			
			responsejson.put("result", true);
			responsejson.put("data",data);
			responsejson.put("count",1);
		}
		return responsejson;
	}
}
