package com.x8.mt.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.common.TransformMetadata;
import com.x8.mt.entity.Datamapitems;
import com.x8.mt.entity.Datamaplayer;
import com.x8.mt.entity.Metadata;
import com.x8.mt.entity.Metamodel_relation;
import com.x8.mt.service.DatamapitemsService;
import com.x8.mt.service.DatamaplayerService;
import com.x8.mt.service.MetaDataService;

@CrossOrigin(origins = "*", maxAge = 3600)
// 解决跨域请求的注解
@Controller
@RequestMapping(value = "/datamapitems")
public class DatamapitemsController {

	@Resource
	DatamapitemsService datamapitemsService;
	@Resource
	DatamaplayerService datamaplayerService;
	@Resource
	MetaDataService metaDataService;

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

		JSONArray data = new JSONArray();
		JSONArray links = new JSONArray();

		List<Datamaplayer> maplayerlist = datamaplayerService.getDatamaplayerList();
		for (Datamaplayer datamaplayer : maplayerlist) {
			List<Datamapitems> mapitemsList = datamapitemsService.getDatamapitemsListByMaplayerId(datamaplayer.getId());
			for (Datamapitems datamapitems : mapitemsList) {
				
				Metadata metadata = metaDataService.getMetadataById(datamapitems.getMetadataid());
				
				JSONObject node = new JSONObject();
				node.put("id", datamapitems.getId());
				node.put("x", datamapitems.getPosx());
				node.put("y", datamapitems.getPosy());
				node.put("status", metadata.getCHECKSTATUS());
				node.put("name", metadata.getNAME());
				node.put("bgcolor", datamapitems.getBackgroundcolor());
				node.put("fontcolor", datamapitems.getFontcolor());
				node.put("width", datamapitems.getWidth());
				node.put("height", datamapitems.getHeight());
				data.add(node);
				
				JSONObject link = new JSONObject();
				String attributes = metadata.getATTRIBUTES();
				String[] split = attributes.split(",");
				for (int i = 0; i < split.length; i++) {
					if(split[i].contains("sourcetableid")){
						String[] split2 = split[i].split(":");
						System.out.println(split2[1].trim());
						int parseInt = Integer.parseInt(split2[1].trim().substring(1, split2[1].length()-2));
						link.put("sourceid", parseInt);
					}
					if(split[i].contains("targettableid")){
						String[] split2 = split[i].split(":");
						System.out.println(split2[1].trim());
						int parseInt = Integer.parseInt(split2[1].trim().substring(1, split2[1].length()-3));
						link.put("targetid",parseInt);
					}
				}
				links.add(link);
			}
			
		}
		
		responsejson.put("nodes", data);
		responsejson.put("links", links);
		return responsejson;
	}


}
