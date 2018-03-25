package com.x8.mt.controller;

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
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.service.Datasource_connectinfoService;
import com.x8.mt.service.KettleMetadataCollectService;
import com.x8.mt.service.SystemInfoService;

@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/systemInfo")
public class SystemInfoController {
	@Resource
	SystemInfoService systemInfoService;

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月3日
	 * 作用:获取系统配置信息
	 */
	@RequestMapping(value = "/getSystemInfo",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="jdbc.properties",operationDesc="获取系统配置信息")
	public JSONObject getSystemInfo(HttpServletRequest request,HttpServletResponse response){
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		try{
			JSONArray data = systemInfoService.getSystemInfo();
			
			responsejson.put("result", true);
			responsejson.put("data",data);
			responsejson.put("count", data.size());
		}catch(Exception e){
			e.printStackTrace();
			responsejson.put("result", false);
			responsejson.put("count", 0);
		}		

		return responsejson;
	}
	
	
}

