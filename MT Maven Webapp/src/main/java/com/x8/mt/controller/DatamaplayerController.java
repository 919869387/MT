package com.x8.mt.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.entity.Datamapitems;
import com.x8.mt.entity.Metamodel_relation;
import com.x8.mt.service.DatamapitemsService;
import com.x8.mt.service.DatamaplayerService;

@CrossOrigin(origins = "*", maxAge = 3600)
// 解决跨域请求的注解
@Controller
@RequestMapping(value = "/datamaplayer")
public class DatamaplayerController {

	@Resource
	DatamaplayerService datamaplayerService;

	/**
	 * 
	 * 作者:itcoder 
	 * 时间:2018年4月10日 
	 * 作用:获得所有的节点属性
	 */
	@RequestMapping(value = "/getDatamap", method = RequestMethod.GET)
	@ResponseBody
	@Log(operationType = "getDatamap", operationDesc = "获得所有的节点属性")
	public JSONObject getDatamap(
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject responsejson = new JSONObject();

		// if(!GlobalMethodAndParams.checkLogin()){
		// responsejson.put("result", false);
		// responsejson.put("count",0);
		// return responsejson;
		// }
		GlobalMethodAndParams.setHttpServletResponse(request, response);

//		List<Datamapitems> dependencyRelationList = datamapitemsService.getDatamap();
//
//		JSONArray data = new JSONArray();
//		for (Datamapitems datamapitems : dependencyRelationList) {
//			JSONObject json = new JSONObject();
//			json.put("id", datamapitems.getId());
//			json.put("name", datamapitems.getPosx());
//			data.add(json);
//		}
		responsejson.put("result", true);
//		responsejson.put("data", data);
		responsejson.put("count", 1);
		return responsejson;
	}


}
