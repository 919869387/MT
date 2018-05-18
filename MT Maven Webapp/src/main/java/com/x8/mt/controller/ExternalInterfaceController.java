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
import com.x8.mt.common.Log;
import com.x8.mt.service.ExternalInterfaceService;

/**
 * 作者： allen
 * 时间：2018年5月5日
 * 作用：系统给外部提供的接口
 */
@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/externalInterface")
public class ExternalInterfaceController {
	@Resource
	ExternalInterfaceService externalInterfaceService;
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月15日
	 * 作用:外部接口-请求全部协议类型清单
	 */
	@RequestMapping(value = "/getProtocolType", method = RequestMethod.GET)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="外部接口-请求全部协议类型清单")
	public JSONObject getProtocolType(HttpServletRequest request,
			HttpServletResponse response){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		List<String> protocolType = externalInterfaceService.getProtocolType();

		responsejson.put("result", true);
		responsejson.put("data", protocolType);
		responsejson.put("count", protocolType.size());

		return responsejson;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月16日
	 * 作用:外部接口-请求全部协议清单，包含协议的id,name,type
	 */
	@RequestMapping(value = "/getProtocolInfo", method = RequestMethod.GET)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="外部接口-请求全部协议清单")
	public JSONObject getProtocolInfo(HttpServletRequest request,
			HttpServletResponse response){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		List<Map<String,String>> protocolInfo = externalInterfaceService.getProtocolInfo();

		responsejson.put("result", true);
		responsejson.put("data", protocolInfo);
		responsejson.put("count", protocolInfo.size());

		return responsejson;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月9日
	 * 作用:外部接口-根据一个协议type获取该类型的全部协议元数据
	 * 
	 * 参数：protocolType--opc、dds、modbus、udp
	 */
	@RequestMapping(value = "/getProtocolMetadataByprotocolType", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="外部接口-根据一个协议type获取该类型的全部协议元数据")
	public JSONObject getProtocolMetadataByprotocolType(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String,String> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检测参数是否正确
		if(!map.containsKey("protocolType")){
			responsejson.put("result", false);
			responsejson.put("message", "请求参数有误");
			responsejson.put("count",0);
			return responsejson;
		}

		String protocolType = map.get("protocolType");

		JSONArray protocolMetadata = externalInterfaceService.getProtocolMetadataByprotocolType(protocolType);
		
		if(protocolMetadata.size()==0){
			responsejson.put("result", false);
			responsejson.put("message", "请求参数值不存在");
			responsejson.put("count", 0);
		}else{
			responsejson.put("result", true);
			responsejson.put("data", protocolMetadata);
			responsejson.put("count", protocolMetadata.size());
		}
		
		return responsejson;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月9日
	 * 作用:外部接口-根据一个协议id获取该协议的元数据
	 * 
	 * 参数：protocolId
	 */
	@RequestMapping(value = "/getProtocolMetadataByprotocolId", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="外部接口-根据一个协议id获取该协议的元数据")
	public JSONObject getProtocolMetadataByprotocolId(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String,String> map){
		JSONObject responsejson = new JSONObject();

		//GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检测参数是否正确
		if(!map.containsKey("protocolId")){
			responsejson.put("result", false);
			responsejson.put("message", "请求参数有误");
			responsejson.put("count",0);
			return responsejson;
		}

		String protocolId = map.get("protocolId");

		JSONObject protocolMetadata = externalInterfaceService.getProtocolMetadataByprotocolId(protocolId);
		if(protocolMetadata==null){
			responsejson.put("result", false);
			responsejson.put("message", "请求参数值不存在");
			responsejson.put("count", 0);
		}else{
			responsejson.put("result", true);
			responsejson.put("data", protocolMetadata);
			responsejson.put("count", 1);
		}
		
		return responsejson;
	}

}
