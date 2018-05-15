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
	 * 作用:外部接口-获取通信报文元数据种类
	 */
	@RequestMapping(value = "/getProtocolType", method = RequestMethod.GET)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="外部接口-获取通信报文元数据种类")
	public JSONObject getProtocolType(HttpServletRequest request,
			HttpServletResponse response){
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);

		List<String> protocolType = externalInterfaceService.getProtocolType();

		responsejson.put("result", true);
		responsejson.put("data", protocolType);
		responsejson.put("count", protocolType.size());

		return responsejson;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月9日
	 * 作用:外部接口-获取通信报文元数据
	 * 
	 * 参数：protocolType--opc、dds、modbus、udp
	 */
	@RequestMapping(value = "/getProtocolMetadata", method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="metadata",operationDesc="外部接口-获取通信报文元数据")
	public JSONObject getProtocolMetadata(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Map<String,String> map){
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检测参数是否正确
		if(!map.containsKey("protocolType")){
			responsejson.put("result", false);
			responsejson.put("count",0);
			return responsejson;
		}

		String protocolType = map.get("protocolType");

		JSONArray protocolMetadata = externalInterfaceService.getProtocolMetadata(protocolType);

		responsejson.put("result", true);
		responsejson.put("data", protocolMetadata);
		responsejson.put("count", protocolMetadata.size());

		return responsejson;
	}

}
