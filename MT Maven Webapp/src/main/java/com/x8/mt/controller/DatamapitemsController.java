package com.x8.mt.controller;

import java.util.HashMap;
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
import com.x8.mt.entity.Datamapitems;
import com.x8.mt.entity.Datamaplayer;
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
		HashMap<String,Integer> map = new HashMap<>();
		// if(!GlobalMethodAndParams.checkLogin()){
		// responsejson.put("result", false);
		// responsejson.put("count",0);
		// return responsejson;
		// }
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		HashMap<Integer,Integer> countMap = new HashMap<>();
		JSONArray data = new JSONArray();
		JSONArray links = new JSONArray();

		//获取第一层数据地图图层id=1
		List<Datamaplayer> maplayerlist = datamaplayerService
				.getDatamaplayerList();
		if (maplayerlist.size() == 0) {
			responsejson.put("nodes", data);
			responsejson.put("links", links);
			return responsejson;
		}
		System.out.println(maplayerlist.size());
		for (Datamaplayer datamaplayer : maplayerlist) {
			List<Datamapitems> mapitemsList = datamapitemsService
					.getDatamapitemsListByMaplayerId(datamaplayer.getId());
			System.out.println(mapitemsList.size()+"------------"+datamaplayer.getName());
			//如果datamapitems表数据为空，关联查询多张表，初始化数据地图，初始数据地图，连线无法显示
			if (mapitemsList.size() == 0) {
				int count = 0;

				Metamodel_hierarchy sysMetamodel = metamodel_hierarchyService
						.getMetamodel_hierarchy(GlobalMethodAndParams.SystemMedamodelId_InDatabase);
				//获得系统元模型下的元模型关系
				List<Metamodel_relation> dbRelatedMetamodelList = metamodel_relationService
						.getDependencyRelationByMetamodelid(sysMetamodel
								.getId());
				for (Metamodel_relation metamodel_relation : dbRelatedMetamodelList) {
					System.out.println(metamodel_relation.getRelatedmetamodelid()+"-----------------------");
					List<Metadata> dbMetadataList = metaDataService
							.getMetadataByMetaModelIdNoNull(metamodel_relation
									.getRelatedmetamodelid());
					//遍历数据库元数据列表
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
//				System.out.println(datamapitems.getId());
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
					if(targetDatamapitems.size()!=0 && targetDatamapitems.get(0).getMaplayerid()==1){
						JSONObject link = new JSONObject();
						link.put("sourceid", datamapitems.getId());
						link.put("targetid", targetDatamapitems.get(0).getId());
						links.add(link);
					}
				}
				
			}
			//db之间的关系，数据库没有存储，手动编写彼此的关系。数据库之间的对应关系
			List<Metadata> tableMappingList = metaDataService
					.getMetadataByMetaModelIdNoNull(Integer
							.parseInt(GlobalMethodAndParams.MetaDataTableMappingModelId));
			System.out.println(tableMappingList.size()+":元数据表映射列表的总个数");
			for (Metadata mappingMetadata : tableMappingList) {
				String json = mappingMetadata.getATTRIBUTES();
				JSONObject json1 = JSONObject.fromObject(json);
				
//				System.out.println("1111111111111");
//				int sourcetableid = (int) json1.get("sourcetableid");
//				int targettableid = (int) json1.get("targettableid");
				Object sourcetableid = json1.get("sourcetableid");
				Object targettableid = json1.get("targettableid");
				
				Metadata sourcMetadata = metaDataService.getMetadataById(Integer.parseInt(sourcetableid.toString()));
				Metadata targetMetadata = metaDataService.getMetadataById(Integer.parseInt(targettableid.toString()));
				
				int sourceMetadataid = metaDataRelationService.getMetadataidByRelatedmetadataid(sourcMetadata.getID());
				List<Datamapitems> sourceDatamapitems = datamapitemsService.getDatamapitemsIDByMetadataId(sourceMetadataid);
				
				int targetMetadataid = metaDataRelationService.getMetadataidByRelatedmetadataid(targetMetadata.getID());
				List<Datamapitems> targetDatamapitems = datamapitemsService.getDatamapitemsIDByMetadataId(targetMetadataid);
				
				System.out.println(sourcMetadata.getNAME()+"+++"+targetMetadata.getNAME());
//				System.out.println("metadata的attributes属性为："+json1.toString());
//				System.out.println("源表对应的数据库id："
//						+ sourceMetadataid + "     目标表对应的数据库id："
//						+ targetMetadataid
//				);
				
				JSONObject link = new JSONObject();
				int sourceid = sourceDatamapitems.get(0).getId();
				int targetid = targetDatamapitems.get(0).getId();
				if(map.get(sourceid+""+targetid)==null){
					map.put(sourceid+""+targetid, 1);
					link.put("sourceid", sourceid);
					link.put("targetid", targetid);
					links.add(link);
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
			HttpServletResponse response,@RequestBody Map<String,Object> map) {
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
		if(!(map.containsKey("sourceid")&&map.containsKey("targetid"))){
			responsejson.put("nodes", data);
			responsejson.put("links", links);
			return responsejson;
		}
		
		String sourceidStr = map.get("sourceid").toString();
		int sourceid = Integer.parseInt(sourceidStr);
		String targetidStr = map.get("targetid").toString();
		int targetid = Integer.parseInt(targetidStr);
		//根据图元表的id，获得元数据id
		int sourceMetadataid = datamapitemsService.getDatamapitemsMetadataidById(sourceid);
		int targetMetadataid = datamapitemsService.getDatamapitemsMetadataidById(targetid);
		//获取数据库下的表id列表
		List<String> sonMetadataList = metaDataRelationService.getMetadata_relationByMetadataid(sourceMetadataid);
		List<String> sonTargetMetadataList = metaDataRelationService.getMetadata_relationByMetadataid(targetMetadataid);
		int num = 0;
		for (String string : sonTargetMetadataList) {
			Metadata tableMetadata = metaDataService.getMetadataById(Integer.parseInt(string));
			boolean isExist = datamapitemsService.isExist(tableMetadata.getID());
			if(!isExist){
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
			}
			
			JSONObject node = new JSONObject();
			Datamapitems itemNode = datamapitemsService.getDatamapitemsIDByMetadataId(tableMetadata.getID()).get(0);
			node.put("id", itemNode.getId());
			node.put("x", itemNode.getPosx());
			node.put("y", itemNode.getPosy());
			node.put("status", tableMetadata.getCHECKSTATUS());
			node.put("name", tableMetadata.getNAME());
			node.put("bgcolor", itemNode.getBackgroundcolor());
			node.put("fontcolor", itemNode.getFontcolor());
			node.put("width", itemNode.getWidth());
			node.put("height", itemNode.getHeight());
			node.put("flag", "db");
			data.add(node);
		}
		
		//遍历元数据表查询该表是否映射了其他数据库中的表，如果有则获取其表id，数据库id。
		
		for (String sourceTableid : sonMetadataList) {
			Metadata tableMetadata = metaDataService.getMetadataById(Integer.parseInt(sourceTableid));
			boolean isExist = datamapitemsService.isExist(tableMetadata.getID());
			if(!isExist){
				Datamapitems datamapitems = new Datamapitems();
				datamapitems.setMaplayerid(2);
				datamapitems.setMetadataid(tableMetadata.getID());
				datamapitems.setPosx(800);
				datamapitems.setPosy(110 * num++);
				datamapitems.setWidth(100);
				datamapitems.setHeight(100);
				datamapitems.setBackgroundcolor("rgba(48, 208, 198, 0.5)");
				datamapitems.setFontcolor("#fff");
				datamapitemsService.insertDatamapitems(datamapitems);
			}
			
			JSONObject node = new JSONObject();
			Datamapitems itemNode = datamapitemsService.getDatamapitemsIDByMetadataId(tableMetadata.getID()).get(0);
			node.put("id", itemNode.getId());
			node.put("x", itemNode.getPosx());
			node.put("y", itemNode.getPosy());
			node.put("status", tableMetadata.getCHECKSTATUS());
			node.put("name", tableMetadata.getNAME());
			node.put("bgcolor", itemNode.getBackgroundcolor());
			node.put("fontcolor", itemNode.getFontcolor());
			node.put("width", itemNode.getWidth());
			node.put("height", itemNode.getHeight());
			node.put("flag", "db");
			data.add(node);
		}
		//找到所有的99对应的表映射元数据列表
		List<Metadata> mappingList = metaDataService.getMetadataByMetaModelId(Integer.parseInt(GlobalMethodAndParams.MetaDataTableMappingModelId));
		System.out.println(mappingList.size()+":Size");
		for (Metadata metadata : mappingList) {
			String attributes = metadata.getATTRIBUTES();
			JSONObject json1 = JSONObject.fromObject(attributes);
			if(json1.containsKey("sourcetableid")){
				Object sourcetablidObj = json1.get("sourcetableid");
				int sourcetablid = Integer.parseInt(sourcetablidObj.toString());
				
				Object targettableidObj = json1.get("targettableid");
				int targettableid = Integer.parseInt(targettableidObj.toString());
				
				System.out.println(targettableid+"---"+sourcetablid);
				
				JSONObject link = new JSONObject();
				link.put("sourceid", datamapitemsService.getDatamapitemsIDByMetadataId(sourcetablid));
				link.put("targetid", datamapitemsService.getDatamapitemsIDByMetadataId(targettableid));
				links.add(link);
				
			}
		}
		responsejson.put("nodes", data);
		responsejson.put("links", links);
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:itcoder 
	 * 时间:2018年5月8日 
	 * 作用:获取第三层数据地图
	 * 参数：sourceid，targetid
	 */
	@RequestMapping(value = "/getThreeDatamap", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType = "getThreeDatamap", operationDesc = "获取第三层数据地图")
	public JSONObject getThreeDatamap(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String,Object> map) {
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
		if(!(map.containsKey("sourceid")&&map.containsKey("targetid"))){
			responsejson.put("nodes", data);
			responsejson.put("links", links);
			return responsejson;
		}
		
		String sourceidStr = map.get("sourceid").toString();
		int sourceid = Integer.parseInt(sourceidStr);
		String targetidStr = map.get("targetid").toString();
		int targetid = Integer.parseInt(targetidStr);
		//根据图元表的id，获得元数据id
		int sourceMetadataid = datamapitemsService.getDatamapitemsMetadataidById(sourceid);
		int targetMetadataid = datamapitemsService.getDatamapitemsMetadataidById(targetid);
		//获取表下的字段id列表
		List<String> sonMetadataList = metaDataRelationService.getMetadata_relationByMetadataid(sourceMetadataid);
		List<String> sonTargetMetadataList = metaDataRelationService.getMetadata_relationByMetadataid(targetMetadataid);
		int num = 0;
		for (String string : sonTargetMetadataList) {
			Metadata tableMetadata = metaDataService.getMetadataById(Integer.parseInt(string));
			boolean isExist = datamapitemsService.isExist(tableMetadata.getID());
			if(!isExist){
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
			}
			
			JSONObject node = new JSONObject();
			Datamapitems itemNode = datamapitemsService.getDatamapitemsIDByMetadataId(tableMetadata.getID()).get(0);
			node.put("id", itemNode.getId());
			node.put("x", itemNode.getPosx());
			node.put("y", itemNode.getPosy());
			node.put("status", tableMetadata.getCHECKSTATUS());
			node.put("name", tableMetadata.getNAME());
			node.put("bgcolor", itemNode.getBackgroundcolor());
			node.put("fontcolor", itemNode.getFontcolor());
			node.put("width", itemNode.getWidth());
			node.put("height", itemNode.getHeight());
			node.put("flag", "db");
			data.add(node);
		}
		
		//遍历元数据字段查询该字段是否映射了其他数据库中的表的字段，如果有则获取其字段id，数据库id。
		
		for (String sourceTableid : sonMetadataList) {
			Metadata tableMetadata = metaDataService.getMetadataById(Integer.parseInt(sourceTableid));
			boolean isExist = datamapitemsService.isExist(tableMetadata.getID());
			if(!isExist){
				Datamapitems datamapitems = new Datamapitems();
				datamapitems.setMaplayerid(3);
				datamapitems.setMetadataid(tableMetadata.getID());
				datamapitems.setPosx(800);
				datamapitems.setPosy(110 * num++);
				datamapitems.setWidth(100);
				datamapitems.setHeight(100);
				datamapitems.setBackgroundcolor("rgba(48, 208, 198, 0.5)");
				datamapitems.setFontcolor("#fff");
				datamapitemsService.insertDatamapitems(datamapitems);
			}
			
			JSONObject node = new JSONObject();
			Datamapitems itemNode = datamapitemsService.getDatamapitemsIDByMetadataId(tableMetadata.getID()).get(0);
			node.put("id", itemNode.getId());
			node.put("x", itemNode.getPosx());
			node.put("y", itemNode.getPosy());
			node.put("status", tableMetadata.getCHECKSTATUS());
			node.put("name", tableMetadata.getNAME());
			node.put("bgcolor", itemNode.getBackgroundcolor());
			node.put("fontcolor", itemNode.getFontcolor());
			node.put("width", itemNode.getWidth());
			node.put("height", itemNode.getHeight());
			node.put("flag", "db");
			data.add(node);
		}
		//找到所有的100对应的字段映射元数据列表
		List<Metadata> mappingList = metaDataService.getMetadataByMetaModelId(Integer.parseInt(GlobalMethodAndParams.MetaDataMappingModelId));
		System.out.println(mappingList.size()+":Size");
		for (Metadata metadata : mappingList) {
			String attributes = metadata.getATTRIBUTES();
			JSONObject json1 = JSONObject.fromObject(attributes);
			if(json1.containsKey("sourcefieldid")){
				Object sourcetablidObj = json1.get("sourcefieldid");
				int sourcetablid = Integer.parseInt(sourcetablidObj.toString());
				
				Object targettableidObj = json1.get("targetfieldid");
				int targettableid = Integer.parseInt(targettableidObj.toString());
				
				System.out.println(targettableid+"---"+sourcetablid);
				
				JSONObject link = new JSONObject();
				link.put("sourceid", datamapitemsService.getDatamapitemsIDByMetadataId(sourcetablid));
				link.put("targetid", datamapitemsService.getDatamapitemsIDByMetadataId(targettableid));
				links.add(link);
				
			}
		}
		responsejson.put("nodes", data);
		responsejson.put("links", links);
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:itcoder 
	 * 时间:2018年5月8日 
	 * 作用:修改数据地图图元位置
	 * 参数：JSONArray
	 */
	@RequestMapping(value = "/modifyDatamapPosition", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType = "modifyDatamapPosition", operationDesc = "修改数据地图图元位置")
	public JSONObject modifyDatamapPosition(HttpServletRequest request,
			HttpServletResponse response,@RequestBody String jsonStr) {
		JSONObject responsejson = new JSONObject();

		// if(!GlobalMethodAndParams.checkLogin()){
		// responsejson.put("result", false);
		// responsejson.put("count",0);
		// return responsejson;
		// }
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		boolean result = false;
		int count = 0;
		
		JSONArray paramArray = JSONArray.fromObject(jsonStr);
		for (int i = 0; i < paramArray.size(); i++) {
			JSONObject node = paramArray.getJSONObject(i);
			int id = node.getInt("id");
			int posx = node.getInt("posx");
			int posy = node.getInt("posy");
			Datamapitems datamapitems = new Datamapitems();
			datamapitems.setPosx(posx);
			datamapitems.setPosy(posy);
			datamapitems.setId(id);
			count += datamapitemsService.updateDatamapitems(datamapitems);
		}
		
		if(count==paramArray.size()){
			result = true;
		}
		if(result){
			responsejson.put("result", result);
			responsejson.put("count", 1);
		}else{
			responsejson.put("result", result);
			responsejson.put("count", 0);
		}
		return responsejson;
	}
	
}
