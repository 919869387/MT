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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.entity.Datamapitems;
import com.x8.mt.entity.Datamaplayer;
import com.x8.mt.entity.MetaDataRelation;
import com.x8.mt.entity.Metadata;
import com.x8.mt.entity.Metamodel_hierarchy;
import com.x8.mt.entity.Metamodel_relation;
import com.x8.mt.service.DatamapitemsService;
import com.x8.mt.service.DatamaplayerService;
import com.x8.mt.service.MetaDataRelationService;
import com.x8.mt.service.MetaDataService;
import com.x8.mt.service.Metamodel_hierarchyService;
import com.x8.mt.service.Metamodel_relationService;

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
	@Resource
	Metamodel_hierarchyService metamodel_hierarchyService;
	@Resource
	Metamodel_relationService metamodel_relationService;
	@Resource
	MetaDataRelationService metaDataRelationService;

	/**
	 * 
	 * 作者:itcoder 
	 * 时间:2018年4月10日 
	 * 作用:获得所有的节点属性
	 */
	@RequestMapping(value = "/getDatamap", method = RequestMethod.GET)
	@ResponseBody
	@Log(operationType = "getDatamap", operationDesc = "获得所有的节点属性")
	public JSONObject getDatamap(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject responsejson = new JSONObject();

		// if(!GlobalMethodAndParams.checkLogin()){
		// responsejson.put("result", false);
		// responsejson.put("count",0);
		// return responsejson;
		// }
		GlobalMethodAndParams.setHttpServletResponse(request, response);

		JSONArray data = new JSONArray();
		JSONArray links = new JSONArray();

		List<Datamaplayer> maplayerlist = datamaplayerService
				.getDatamaplayerList();
		if (maplayerlist == null) {
			responsejson.put("nodes", data);
			responsejson.put("links", links);
			return responsejson;
		}
		for (Datamaplayer datamaplayer : maplayerlist) {
			List<Datamapitems> mapitemsList = datamapitemsService
					.getDatamapitemsListByMaplayerId(datamaplayer.getId());
			if (mapitemsList.size() == 0) {
				int count = 0;

				Metamodel_hierarchy sysMetamodel = metamodel_hierarchyService
						.getMetamodel_hierarchy(GlobalMethodAndParams.SystemMedamodelId_InDatabase);
				List<Metamodel_relation> dbRelatedMetamodelList = metamodel_relationService
						.getDependencyRelationByMetamodelid(sysMetamodel
								.getId());
				for (Metamodel_relation metamodel_relation : dbRelatedMetamodelList) {
					List<Metadata> dbMetadataList = metaDataService
							.getMetadataByMetaModelId(metamodel_relation
									.getRelatedmetamodelid());
					for (Metadata metadata : dbMetadataList) {
						System.out.println(metadata.getID()+"---"+"db");
						JSONObject node = new JSONObject();
						node.put("id", ++count);
						node.put("x", 800);
						node.put("y", 110 * count);
						node.put("status", metadata.getCHECKSTATUS());
						node.put("name", metadata.getNAME());
						node.put("bgcolor", "rgba(48, 208, 198, 0.5)");
						node.put("fontcolor", "#fff");
						node.put("width", 100);
						node.put("height", 100);
						node.put("flag", "db");
						data.add(node);
						Datamapitems datamapitems = new Datamapitems();
//						datamapitems.setId(count);
						datamapitems.setMaplayerid(1);
						datamapitems.setMetadataid(metadata.getID());
						datamapitems.setPosx(800);
						datamapitems.setPosy(110 * count);
						datamapitems.setWidth(100);;
						datamapitems.setHeight(100);;
						datamapitems.setBackgroundcolor("rgba(48, 208, 198, 0.5)");;
						datamapitems.setFontcolor("#fff");;
						datamapitemsService.insertDatamapitems(datamapitems);
					}
				}

				int num = 0;
				List<Metadata> sysMetadataList = metaDataService.getMetadataByMetaModelId(sysMetamodel.getId());
				for (Metadata metadata : sysMetadataList) {
					num++;
					System.out.println(metadata.getID()+"---"+"sys");
					JSONObject node = new JSONObject();
					node.put("id", ++count);
					node.put("x", 400);
					node.put("y", 110 * num);
					node.put("status", metadata.getCHECKSTATUS());
					node.put("name", metadata.getNAME());
					node.put("bgcolor", "rgba(48, 208, 198, 0.5)");
					node.put("fontcolor", "#fff");
					node.put("width", 100);
					node.put("height", 100);
					node.put("flag", "sys");
					data.add(node);
					Datamapitems datamapitems = new Datamapitems();
//					datamapitems.setId(count);
					datamapitems.setMaplayerid(1);
					datamapitems.setMetadataid(metadata.getID());
					datamapitems.setPosx(400);
					datamapitems.setPosy(110 * num);
					datamapitems.setWidth(100);
					datamapitems.setHeight(100);
					datamapitems.setBackgroundcolor("rgba(48, 208, 198, 0.5)");
					datamapitems.setFontcolor("#fff");
					datamapitemsService.insertDatamapitems(datamapitems);
				}
			}
			
			// 当datamapitems有数据的时候
			for (Datamapitems datamapitems : mapitemsList) {

				Metadata metadata = metaDataService
						.getMetadataById(datamapitems.getMetadataid());

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
				
				//获取节点之后,获取连接
				//获取元数据关系表关联的元数据id
				List<String> metadata_relationidList = metaDataRelationService
						.getMetadata_relationByMetadataid(datamapitems
								.getMetadataid());
				//查询关联元数据在图元表中对应的id
				for (String string : metadata_relationidList) {
					List<Datamapitems> targetDatamapitems = datamapitemsService.getDatamapitemsIDByMetadataId(Integer.parseInt(string));
					if(targetDatamapitems.size()!=0){
						JSONObject link = new JSONObject();
						link.put("sourceid", datamapitems.getId());
						link.put("targetid", targetDatamapitems.get(0).getId());
						links.add(link);
					}
				}
			}
			

		}
		responsejson.put("nodes", data);
		responsejson.put("links", links);
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:itcoder 
	 * 时间:2018年4月18日 
	 * 作用:获取第二层数据地图
	 * 参数：sourceid，targetid
	 */
	@RequestMapping(value = "/getSecondDatamap", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType = "getSecondDatamap", operationDesc = "获取第二层数据地图")
	public JSONObject getSecondDatamap(HttpServletRequest request,
			HttpServletResponse response,Map<String,Object> map) {
		JSONObject responsejson = new JSONObject();

		// if(!GlobalMethodAndParams.checkLogin()){
		// responsejson.put("result", false);
		// responsejson.put("count",0);
		// return responsejson;
		// }
		GlobalMethodAndParams.setHttpServletResponse(request, response);

		JSONArray data = new JSONArray();
		JSONArray links = new JSONArray();
		int count = 0;
		
		String sourceidStr = map.get("sourceid").toString();
		int sourceid = Integer.parseInt(sourceidStr);
		String targetidStr = map.get("targetid").toString();
		int targetid = Integer.parseInt(targetidStr);
		
		int sourceMetadataid = datamapitemsService.getDatamapitemsMetadataidById(sourceid);
		int targetMetadataid = datamapitemsService.getDatamapitemsMetadataidById(targetid);
		//获取数据库下的表id列表
		List<String> sonMetadataList = metaDataRelationService.getMetadata_relationByMetadataid(sourceMetadataid);
		List<String> sonTargetMetadataList = metaDataRelationService.getMetadata_relationByMetadataid(targetMetadataid);
		int num = 0;
		for (String string : sonTargetMetadataList) {
			Metadata tableMetadata = metaDataService.getMetadataById(Integer.parseInt(string));
			Datamapitems datamapitems = new Datamapitems();
			datamapitems.setMaplayerid(2);
			datamapitems.setMetadataid(tableMetadata.getID());
			datamapitems.setPosx(400);
			datamapitems.setPosy(110 * num++);
			datamapitems.setWidth(100);
			datamapitems.setHeight(100);
			datamapitems.setBackgroundcolor("rgba(48, 208, 198, 0.5)");
			datamapitems.setFontcolor("#fff");
			datamapitemsService.insertDatamapitems(datamapitems);
			
			JSONObject node = new JSONObject();
			node.put("id", datamapitemsService.getDatamapitemsIDByMetadataId(tableMetadata.getID()).get(0).getId());
			node.put("x", 400);
			node.put("y", 110 * num);
			node.put("status", tableMetadata.getCHECKSTATUS());
			node.put("name", tableMetadata.getNAME());
			node.put("bgcolor", "rgba(48, 208, 198, 0.5)");
			node.put("fontcolor", "#fff");
			node.put("width", 100);
			node.put("height", 100);
			node.put("flag", "db");
			data.add(node);
		}
		
		//遍历元数据表查询该表是否映射了其他数据库中的表，如果有则获取其表id，数据库id。
		
		for (String sourceTableid : sonMetadataList) {
			Metadata tableMetadata = metaDataService.getMetadataById(Integer.parseInt(sourceTableid));
			Datamapitems datamapitems = new Datamapitems();
			datamapitems.setMaplayerid(2);
			datamapitems.setMetadataid(tableMetadata.getID());
			datamapitems.setPosx(400);
			datamapitems.setPosy(110 * num);
			datamapitems.setWidth(100);
			datamapitems.setHeight(100);
			datamapitems.setBackgroundcolor("rgba(48, 208, 198, 0.5)");
			datamapitems.setFontcolor("#fff");
			datamapitemsService.insertDatamapitems(datamapitems);
			
			JSONObject node = new JSONObject();
			node.put("id", datamapitemsService.getDatamapitemsIDByMetadataId(tableMetadata.getID()).get(0).getId());
			node.put("x", 400);
			node.put("y", 110 * num);
			node.put("status", tableMetadata.getCHECKSTATUS());
			node.put("name", tableMetadata.getNAME());
			node.put("bgcolor", "rgba(48, 208, 198, 0.5)");
			node.put("fontcolor", "#fff");
			node.put("width", 100);
			node.put("height", 100);
			node.put("flag", "db");
			data.add(node);
			
			List<Metadata> mappingList = metaDataService.getMetadataByMetaModelId(Integer.parseInt(GlobalMethodAndParams.MetaDataMappingModelId));
			for (Metadata metadata : mappingList) {
				String attributes = metadata.getATTRIBUTES();
				String[] key_values = attributes.split(",");
				String sourcetableid = null;
				String targettableid = null;
				for (String string : key_values) {
					//剥离出来sourcetableid对应的值，与sourceTableid比较看是否相等
					if(string.contains("sourcetablid")){
						sourcetableid = string.substring(17, string.length()-1);
					}
					if(string.contains("targettableid")){
						targettableid = string.substring(17,string.length()-2);
					}
				}
				
				if(sourceTableid.equals(sourcetableid)){
					for(String targetTableid : sonTargetMetadataList){
						if(targettableid.equals(targetTableid)){
							//是指定两个数据库的表映射对应关系，放入JSONObject中返回
							JSONObject link = new JSONObject();
							link.put("sourceid", datamapitemsService.getDatamapitemsIDByMetadataId(tableMetadata.getID()).get(0).getId());
							link.put("targetid", datamapitemsService.getDatamapitemsIDByMetadataId(Integer.parseInt(targetTableid)).get(0).getId());
							links.add(link);
						}
					}
				}
			}
		}
		responsejson.put("nodes", data);
		responsejson.put("links", links);
		return responsejson;
	}
}
