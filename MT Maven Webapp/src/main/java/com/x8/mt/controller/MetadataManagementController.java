package com.x8.mt.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.common.PageParam;
import com.x8.mt.entity.MetadataViewNode;
import com.x8.mt.entity.Metamodel_hierarchy;
import com.x8.mt.service.MetadataManagementService;
import com.x8.mt.service.MetadataViewNodeService;
import com.x8.mt.service.Metamodel_hierarchyService;
import com.x8.mt.service.SystemLogService;
/**
 * 作者： allen
 * 时间：2018年3月15日
 * 作用：
 */
@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/metadataManagement")
public class MetadataManagementController {
	@Resource
	MetadataManagementService metadataManagementService;
	@Resource
	Metamodel_hierarchyService metamodel_hierarchyService;
	@Resource
	MetadataViewNodeService metadataViewNodeService;
	@Resource
	SystemLogService systemLogService;

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月16日
	 * 作用:导出元数据到excel
	 * 参数：metadataid、filename
	 */
	@RequestMapping(value = "/exportMetadataToExcel",method = RequestMethod.GET)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="导出元数据到excel")
	public JSONObject exportMetadataToExcel(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);

		String metadataid = request.getParameter("metadataid");
		String filename =request.getParameter("filename");

		HSSFWorkbook wb = metadataManagementService.exportMetadataToExcel(metadataid);

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try  
		{
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="+ new String((filename + ".xls").getBytes("utf-8"), "utf-8"));
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			wb.write(os);

			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			ServletOutputStream out = response.getOutputStream();
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			
			byte[] buff = new byte[2048];
			int bytesRead;

			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			
			responsejson.put("result", true);
			responsejson.put("count",1);
		}catch (IOException e) {
			responsejson.put("result", false);
			responsejson.put("count",0);
		}finally{
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}

		return responsejson;
	}

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2018年5月5日
	 * 作用:得到指定用户等出后元数据变更数目
	 *  
	 * 参数：用户名systemusername
	 */
	@RequestMapping(value = "/getModifedMetadataNumbers", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="得到历史版本元数据的公共属性")
	public JSONObject getModifedMetadataNumbers(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,String> map) {
		JSONObject responsejson = new JSONObject();

		//检测参数是否正确
		if(!map.containsKey("systemusername")||map.get("systemusername").trim().equals("")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String systemusername = map.get("systemusername");

		int count = systemLogService.getModifiedNumbers(systemusername);

		responsejson.put("result", true);
		responsejson.put("count",count);
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月26日
	 * 作用:添加表映射元数据--就用addMetadataStepThree
	 *  
	 * 参数：metamodelId、parentMetadataId、NAME、DESCRIPTION、
	 * mappingtype、sourcetableid、targettableid
	 */

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月26日
	 * 作用:添加表字段映射元数据
	 * 
	 * 参数：metamodelId、parentMetadataId、NAME、DESCRIPTION、
	 * mappingtype、sourcetableid、sourcefieldid、targettableid、targetfieldid
	 */
	@RequestMapping(value = "/addTableFieldMappingMetadata", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="添加表字段映射元数据")
	public JSONObject addTableFieldMappingMetadata(HttpServletRequest request,
			HttpServletResponse response,@RequestBody List<Map<String, Object>> list){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(list.size()==0){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		boolean result = metadataManagementService.addTableFieldMappingMetadata(list);

		if(result){
			responsejson.put("result", true);
			responsejson.put("count", list.size());
		}else{
			responsejson.put("result", false);
			responsejson.put("count", 0);
		}
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月25日
	 * 作用:得到历史版本元数据的私有属性
	 *  
	 * 参数：tankid、metamodelid
	 */
	@RequestMapping(value = "/getHistoryMetadataPrivateInfo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="得到历史版本元数据的私有属性")
	public JSONObject getHistoryMetadataPrivateInfo(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(!map.containsKey("tankid") || !map.containsKey("metamodelid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String tankidStr = map.get("tankid").toString();
		String metamodelidStr = map.get("metamodelid").toString();

		List<Map<String,Object>> historyMetadataPrivateInfoList = new ArrayList();

		JSONObject historyMetadataPrivateInfo = metadataManagementService.getHistoryMetadataPrivateInfo(tankidStr);
		Map<String,Object> metamodelInfo = metadataManagementService.getMetamodelPrivateInfo(metamodelidStr);

		Iterator it = historyMetadataPrivateInfo.entrySet().iterator(); 
		Map<String,Object> privateInfo = null;
		while (it.hasNext()) {
			Entry entry = (Map.Entry) it.next();  
			String key = (String) entry.getKey();  
			String cnKey = metamodelInfo.get(key).toString();
			Object value = entry.getValue(); 

			privateInfo = new HashMap();
			privateInfo.put("key", cnKey);
			privateInfo.put("value", value);

			historyMetadataPrivateInfoList.add(privateInfo);
		} 

		responsejson.put("result", true);
		responsejson.put("PrivateInfo",historyMetadataPrivateInfoList);
		responsejson.put("count",1);
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月25日
	 * 作用:得到历史版本元数据的公共属性
	 *  
	 * 参数：metadataid
	 */
	@RequestMapping(value = "/getHistoryMetadataCommonInfo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="得到历史版本元数据的公共属性")
	public JSONObject getHistoryMetadataCommonInfo(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(!map.containsKey("metadataid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metadataidStr = map.get("metadataid").toString();

		List<Map<String,Object>> historyMetadataCommonInfoList = metadataManagementService.getHistoryMetadataCommonInfo(metadataidStr);

		responsejson.put("result", true);
		responsejson.put("CommonInfo",historyMetadataCommonInfoList);
		responsejson.put("count",historyMetadataCommonInfoList.size());
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:删除元数据依赖关系
	 *  
	 * 参数：relationid
	 */
	@RequestMapping(value = "/deleteMetadataDepend", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="删除元数据依赖关系")
	public JSONObject deleteMetadataDepend(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(!map.containsKey("relationid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String relationidStr = map.get("relationid").toString();

		boolean delateResult = metadataManagementService.deleteMetadataDepend(relationidStr);

		if(delateResult){
			responsejson.put("result", true);
			responsejson.put("count",1);
		}else{
			responsejson.put("result", false);
			responsejson.put("count",0);
		}
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:展示元数据依赖关系
	 *  
	 * 参数：metadataid
	 */
	@RequestMapping(value = "/showMetadataDepend", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="展示元数据依赖关系")
	public JSONObject showMetadataDepend(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(!map.containsKey("metadataid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metadataidStr = map.get("metadataid").toString();

		List<Map<String,Object>> dependMetadataList = metadataManagementService.showMetadataDepend(metadataidStr);

		responsejson.put("result", true);
		responsejson.put("data", dependMetadataList);
		responsejson.put("count",dependMetadataList.size());
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:添加元数据依赖
	 *  
	 * 参数：sourcemetadataid、targetmetadataid
	 */
	@RequestMapping(value = "/addMetadataDepend", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="添加元数据依赖")
	public JSONObject addMetadataDepend(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(!map.containsKey("sourcemetadataid") || !map.containsKey("targetmetadataid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String sourcemetadataidStr = map.get("sourcemetadataid").toString();
		String targetmetadataidStr = map.get("targetmetadataid").toString();

		boolean addResult = metadataManagementService.addMetadataDepend(sourcemetadataidStr,targetmetadataidStr);
		if(addResult){
			responsejson.put("result", true);
			responsejson.put("count",1);
		}else{
			responsejson.put("result", false);
			responsejson.put("count",0);
		}

		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年4月24日
	 * 作用:寻找可依赖的元数据
	 *  
	 * 参数：metadataid
	 */
	@RequestMapping(value = "/getDependMetadata", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="寻找可依赖的元数据")
	public JSONObject getDependMetadata(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(!map.containsKey("metadataid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metadataidStr = map.get("metadataid").toString();

		List<Map<String,Object>> dependMetadataList = metadataManagementService.getDependMetadata(metadataidStr);

		responsejson.put("result", true);
		responsejson.put("data", dependMetadataList);
		responsejson.put("count",dependMetadataList.size());
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年4月19日
	 * 作用:查找元数据(元数据name和description中含有该关键字的，展示公共属性)
	 *  
	 * 参数： key
	 * currPage 从1开始
	 * pageSize
	 */
	@RequestMapping(value = "/searchMetadata", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="查找元数据")
	public JSONObject searchMetadata(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(!map.containsKey("key") || !map.containsKey("currPage") || !map.containsKey("pageSize") ){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String key = map.get("key").toString();
		int currPage = Integer.parseInt(map.get("currPage").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());

		PageParam pageParam = metadataManagementService.searchMetadata(key,currPage,pageSize);
		
		if(pageParam==null){
			responsejson.put("result", false);
			responsejson.put("count",0);
		}else{
			responsejson.put("result", true);
			responsejson.put("data", pageParam);
			responsejson.put("count",1);
		}
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年4月18日
	 * 作用:根据表元数据id，获取字段元数据
	 *  
	 * 参数： metadataid
	 */
	@RequestMapping(value = "/getFieldMetadataList", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="根据表元数据id，获取字段元数据")
	public JSONObject getFieldMetadataList(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(!map.containsKey("metadataid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metadataId = map.get("metadataid").toString();

		List<Map<String,Object>> getFieldMetadataList = metadataManagementService.getFieldMetadataList(metadataId);

		responsejson.put("result", true);
		responsejson.put("field", getFieldMetadataList);
		responsejson.put("count",getFieldMetadataList.size());
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年4月18日
	 * 作用:根据数据库元数据id，获取表元数据
	 *  
	 * 参数： metadataid
	 */
	@RequestMapping(value = "/getTableMetadataList", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="根据数据库元数据id,获取表元数据")
	public JSONObject getTableMetadataList(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(!map.containsKey("metadataid")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metadataId = map.get("metadataid").toString();

		List<Map<String,Object>> getTableMetadataList = metadataManagementService.getTableMetadataList(metadataId);

		responsejson.put("result", true);
		responsejson.put("table", getTableMetadataList);
		responsejson.put("count",getTableMetadataList.size());
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年4月18日
	 * 作用:获取所有数据库
	 */
	@RequestMapping(value = "/getDatabaseMetadataList",method=RequestMethod.GET)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="获取所有数据库元数据")
	public JSONObject getDatabaseMetadataList(HttpServletRequest request,HttpServletResponse response){
		JSONObject responsejson = new JSONObject();
		List<Map<String,Object>> getDatabaseMetadataList = metadataManagementService.getDatabaseMetadataList();

		responsejson.put("result", true);
		responsejson.put("database", getDatabaseMetadataList);
		responsejson.put("count",getDatabaseMetadataList.size());
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月25日
	 * 作用:删除元数据信息
	 *  
	 * 参数： ID[元数据id]
	 */
	@RequestMapping(value = "/daleteMetadataInfo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="删除元数据信息")
	public JSONObject daleteMetadataInfo(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!map.containsKey("ID")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metadataId = map.get("ID").toString();

		if(!metadataManagementService.existMetadata(metadataId)){
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}

		List<Object> count = new ArrayList<Object>();
		if(metadataManagementService.daleteMetadataInfo(metadataId,count)){
			responsejson.put("result", true);
			responsejson.put("count", count.size());
		}else{
			responsejson.put("result", false);
			responsejson.put("count", count.size());
		}
		count.clear();
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年3月24日
	 * 作用:修改元数据信息
	 *  	1.将元数据在metadata表修改
	 *  	2.修改后的记录加入metadata_tank表
	 *  
	 * 参数： ID [元数据id]
	 * 		。。。。。
	 * 
	 * {
		"ID": 1248,
		"tablename": "metadata",
		"METAMODELID": 31,
		"NAME": "metadata",
		 "type":"COMMON",
		 "metadataTankid":1212
		}
	 */
	@RequestMapping(value = "/updateMetadataInfoStepTwo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="修改元数据信息步骤二")
	public JSONObject updateMetadataInfoStepTwo(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("ID")&&map.containsKey("METAMODELID"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		if(map.containsKey("metadataTankid")){
			//说明是更新私有属性
			if(metadataManagementService.updateMetadataInfoForPRIVATE(map)){
				responsejson.put("result", true);
				responsejson.put("count", 1);
			}else{
				responsejson.put("result", false);
				responsejson.put("count", 0);
			}
		}else{
			int metadataTankid = metadataManagementService.updateMetadataInfoForCommon(map);
			if(metadataTankid>0){
				responsejson.put("result", true);
				responsejson.put("metadataTankid", metadataTankid);
				responsejson.put("count", 1);
			}else{
				responsejson.put("result", false);
				responsejson.put("count", 0);
			}
		}
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月24日
	 * 作用:修改元数据信息步骤一,先显示可以修改的信息
	 *  
	 * 参数： metadataId[元数据id]
	 * 		metamodelId[元模型id]
	 * 		type[请求类型信息]
	 */
	@RequestMapping(value = "/updateMetadataInfoStepOne", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="修改元数据信息步骤一")
	public JSONObject updateMetadataInfoStepOne(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//检查传参是否正确
		if(!(map.containsKey("metadataId")&&map.containsKey("metamodelId")&&map.containsKey("type"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String type = map.get("type").toString();
		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//不可更新的字段
		List<String> noupdatefield = new ArrayList<String>();
		noupdatefield.add("COLLECTJOBID");
		noupdatefield.add("METAMODELID");
		noupdatefield.add("CHECKSTATUS");
		noupdatefield.add("VERSION");
		noupdatefield.add("UPDATETIME");
		noupdatefield.add("CREATETIME");
		noupdatefield.add("ID");

		//元模型的公共属性
		List<String> systemModelField= new ArrayList<String>();
		systemModelField.add("ID");
		systemModelField.add("NAME");
		systemModelField.add("DESCRIPTION");
		systemModelField.add("CREATETIME");
		systemModelField.add("UPDATETIME");
		systemModelField.add("VERSION");
		systemModelField.add("COLLECTJOBID");
		systemModelField.add("CHECKSTATUS");
		systemModelField.add("METAMODELID");

		String metadataIdStr = map.get("metadataId").toString();
		String metamodelIdStr = map.get("metamodelId").toString();

		Map<String, Object> metadataMap= metadataManagementService.getMetadata(metadataIdStr);
		JSONObject metamodelInfo = metadataManagementService.getMetamodelInfo(metamodelIdStr);

		JSONArray canUpdateInfo = new JSONArray();

		Iterator it = metadataMap.entrySet().iterator();  
		while (it.hasNext()) {
			JSONObject metadataInfo = new JSONObject();
			Entry entry = (Map.Entry) it.next();  
			String key = (String) entry.getKey(); 

			if(type.equals("COMMON")){
				if((systemModelField.contains(key)&&(!noupdatefield.contains(key)))){
					String cnKey = metamodelInfo.get(key).toString();
					Object value = entry.getValue();

					metadataInfo.put("key", key);
					metadataInfo.put("description", cnKey);
					metadataInfo.put("value", value);
					canUpdateInfo.add(metadataInfo);
				}
			}else{
				if((!systemModelField.contains(key)&&(!noupdatefield.contains(key)))){
					String cnKey = metamodelInfo.get(key).toString();
					Object value = entry.getValue();

					metadataInfo.put("key", key);
					metadataInfo.put("description", cnKey);
					metadataInfo.put("value", value);
					canUpdateInfo.add(metadataInfo);
				}
			}
		}

		responsejson.put("result", true);
		responsejson.put("canUpdateInfo", canUpdateInfo);
		responsejson.put("count", 1);
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:insert元数据,插入实际元数据
	 *  	1.将元数据加入到metadata表
	 *  	2.加入metadata_relation表
	 *  	3.加入metadata_tank表
	 *  
	 * 参数： metamodelId [元模型id]
	 * 		parentMetadataId [父元数据id]--如果添加第一层节点元数据,这个参数值为0
	 * 		。。。。。
	 */
	@RequestMapping(value = "/addMetadataStepThree", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="新增元数据步骤三")
	public JSONObject addMetadataStepThree(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("metamodelId")&&map.containsKey("parentMetadataId"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		int metadataId = metadataManagementService.addMetadata(map);
		if(metadataId>0){
			responsejson.put("result", true);
			responsejson.put("metadataId", metadataId);
			responsejson.put("count", 1);
		}else{
			responsejson.put("result", false);
			responsejson.put("count", 0);
		}

		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:新增元数据第二步,获取具体要增加元数据的元模型的信息
	 *  
	 * 参数： metamodelId [要添加元数据的元模型id]
	 */
	@RequestMapping(value = "/addMetadataStepTwo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="新增元数据步骤二")
	public JSONObject addMetadataStepTwo(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!map.containsKey("metamodelId")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metamodelIdStr = map.get("metamodelId").toString();

		JSONArray metamodelInfos = metadataManagementService.getMetamodelInfoForAddMetadata(metamodelIdStr);

		responsejson.put("result", true);
		responsejson.put("metamodelInfos", metamodelInfos);
		responsejson.put("metamodelInfoCount", metamodelInfos.size());
		responsejson.put("count", 1);
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:新增元数据第一步,获取子元模型
	 *  
	 * 参数： metamodelId [这里的元模型id是父亲元模型的id]
	 */
	@RequestMapping(value = "/addMetadataStepOne", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="新增元数据步骤一")
	public JSONObject addMetadataStepOne(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		if(map.containsKey("id")){//增加第一层元数据的逻辑
			String idStr = map.get("id").toString();

			List<MetadataViewNode> metadataViewNodes= metadataViewNodeService.getMetadataViewNode(idStr);

			List<JSONObject> includeMetaModel = new ArrayList<JSONObject>();

			for(MetadataViewNode metadataViewNode:metadataViewNodes){
				int modelid = metadataViewNode.getChildmetamodelid();
				Metamodel_hierarchy metamodel_hierarchy = metamodel_hierarchyService.getMetamodel_hierarchy(modelid);

				JSONObject metamodel = new JSONObject();
				metamodel.put("modelid", modelid);
				metamodel.put("name", metamodel_hierarchy.getName());

				includeMetaModel.add(metamodel);
			}

			responsejson.put("result", true);
			responsejson.put("metamodelname", metadataViewNodes.get(0).getName());
			responsejson.put("includeMetaModel", includeMetaModel);
			responsejson.put("includeCount", includeMetaModel.size());
			responsejson.put("count", includeMetaModel.size());
		}else{//增加第一层之外的元数据逻辑
			//检查传参是否正确
			if(!map.containsKey("metamodelId")){
				responsejson.put("result", false);
				responsejson.put("count",0);
				return responsejson;
			}

			String metamodelIdStr = map.get("metamodelId").toString();
			String metamodelname = metamodel_hierarchyService.getMetamodel_hierarchy(Integer.parseInt(metamodelIdStr)).getName();

			List<JSONObject> includeMetaModel = metadataManagementService.getCOMPOSITIONMetamodel(metamodelIdStr);

			if(includeMetaModel==null){
				responsejson.put("result", false);
				responsejson.put("count",0);
			}else{
				responsejson.put("result", true);
				responsejson.put("metamodelname", metamodelname);
				responsejson.put("includeMetaModel", includeMetaModel);
				responsejson.put("includeCount", includeMetaModel.size());
				responsejson.put("count", includeMetaModel.size());
			}
		}
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:获取某一个元数据详细信息
	 * 
	 * 参数：metadataId [元数据id]
	 * 	   metamodelId [元模型id]
	 */
	@RequestMapping(value = "/getMetadataInfo", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="查找元数据详细信息")
	public JSONObject getMetadataInfo(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("metadataId")&&map.containsKey("metamodelId"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String metadataIdStr = map.get("metadataId").toString();
		String metamodelIdStr = map.get("metamodelId").toString();


		//元模型的公共属性
		List<String> systemModelField= new ArrayList<String>();
		systemModelField.add("ID");
		systemModelField.add("NAME");
		systemModelField.add("DESCRIPTION");
		systemModelField.add("CREATETIME");
		systemModelField.add("UPDATETIME");
		systemModelField.add("VERSION");
		systemModelField.add("COLLECTJOBID");
		systemModelField.add("CHECKSTATUS");
		systemModelField.add("METAMODELID");

		JSONArray CommonModelInfo= new JSONArray();
		JSONArray PrivateModelInfo= new JSONArray();

		Map<String, Object> metadataMap= metadataManagementService.getMetadata(metadataIdStr);
		JSONObject metamodelInfo = metadataManagementService.getMetamodelInfo(metamodelIdStr);

		Iterator it = metadataMap.entrySet().iterator();  
		while (it.hasNext()) {
			JSONObject metadataInfo = new JSONObject();
			Entry entry = (Map.Entry) it.next();  
			String key = (String) entry.getKey();  
			String cnKey = metamodelInfo.get(key).toString();
			Object value = entry.getValue(); 

			metadataInfo.put("key", cnKey);
			metadataInfo.put("value", value);
			if(systemModelField.contains(key)){
				CommonModelInfo.add(metadataInfo);
			}else{
				PrivateModelInfo.add(metadataInfo);
			}
		} 

		responsejson.put("result", true);
		responsejson.put("CommonModelInfo", CommonModelInfo);
		responsejson.put("PrivateModelInfo", PrivateModelInfo);
		responsejson.put("count", 1);
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月19日
	 * 作用:获取字段元数据
	 * 
	 * 参数：tableMetadataId [表元数据id]
	 */
	@RequestMapping(value = "/getFieldMetadata", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="查找字段元数据")
	public JSONObject getFieldMetadata(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!map.containsKey("tableMetadataId")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String tableMetadataIdStr = map.get("tableMetadataId").toString();
		int tableMetadataId = 0;
		try {
			tableMetadataId = Integer.parseInt(tableMetadataIdStr);
		} catch (Exception e) {
		}

		List<Object> fieldMetadatas= metadataManagementService.getFieldMetadata(tableMetadataId);
		JSONObject metamodelInfo = metadataManagementService.getMetamodelInfo(GlobalMethodAndParams.FieldMetamodelId);

		responsejson.put("result", true);
		responsejson.put("data", fieldMetadatas);
		responsejson.put("metamodelInfo", metamodelInfo);
		responsejson.put("count", fieldMetadatas.size());
		return responsejson;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月15日
	 * 作用:查询得到元数据视图树第一层节点
	 * 
	 * 参数：viewid [系统默认视图的viewid=1]
	 */
	@RequestMapping(value = "/getMetadataViewTreeNode", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="查找元数据视图树第一层节点")
	public JSONObject getMetadataViewTreeNode(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		List<Map<String, Object>> treeNode = null;
		if(map.containsKey("viewid")){//第一层试图节点
			String viewidStr = map.get("viewid").toString();

			treeNode = metadataManagementService.getViewNode(viewidStr);
		}else if(map.containsKey("id")){//元数据的第一层节点
			String id = map.get("id").toString();

			treeNode = metadataManagementService.getMetadataFirstNode(id);
		}else if(map.containsKey("metadataid")){//元数据的其他层次节点
			String metadataid = map.get("metadataid").toString();

			treeNode = metadataManagementService.getMetadataOtherNode(metadataid);
		}else{
			responsejson.put("result", false);
			responsejson.put("count", 0);
			return responsejson;
		}

		responsejson.put("result", true);
		responsejson.put("data", treeNode);
		responsejson.put("count", treeNode.size());
		return responsejson;
	}
}
