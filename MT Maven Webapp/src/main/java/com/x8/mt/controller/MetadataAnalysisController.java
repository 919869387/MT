package com.x8.mt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
		
		metadataAnalysisService.fieldImpact(yingxiangidStr,nodesList,linksList);
		
		JSONObject data = new JSONObject();
		data.put("nodes", nodesList);
		data.put("links", linksList);
		
		responsejson.put("result", true);
		responsejson.put("data",data);
		responsejson.put("count",1);
		return responsejson;
	}
}
