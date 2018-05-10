package com.x8.mt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.dao.IExternalInterfaceDao;
import com.x8.mt.dao.IMetadataAnalysisDao;
import com.x8.mt.entity.Metadata;
/**
 * 
 * 作者:allen
 * 时间:2018年5月9日
 */
@Service
public class ExternalInterfaceService {
	@Resource
	IExternalInterfaceDao iExternalInterfaceDao;
	@Resource
	IMetadataAnalysisDao iMetadataAnalysisDao;

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月9日
	 * 获取通信报文元数据
	 */
	public JSONArray getProtocolMetadata(String protocolType) {
		JSONArray protocols = new JSONArray();
		
		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("key", GlobalMethodAndParams.JSONKey_protocolid);
		paramMap.put("value", protocolType);
		
		List<Metadata> protocolMetadatas= iMetadataAnalysisDao.getMetadataByJson(paramMap);
		
		JSONObject protocol = null;
		for(Metadata metadata : protocolMetadatas){
			protocol = new JSONObject();
			protocol.put("protocolName", metadata.getNAME());
			protocol.put("protocolDescription", metadata.getDESCRIPTION());
			protocol.put("protocolId", JSONObject.fromObject(metadata.getATTRIBUTES()).get("protocolid"));
			protocol.put("protocolType", JSONObject.fromObject(metadata.getATTRIBUTES()).get("protocoltype"));
			protocol.put("protocolParams", getProtocolParamMetadata(metadata.getID()+""));
			
			protocols.add(protocol);
		}
		return protocols;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月9日
	 * 获取通信报文元数据的参数元数据
	 */
	public JSONArray getProtocolParamMetadata(String metadataid){
		JSONArray paramArray = new JSONArray();
		
		List<Metadata> protocolParams = iExternalInterfaceDao.getCompositionRelatedmetadata(metadataid);
		
		JSONObject param = null;
		for(Metadata metadata : protocolParams){
			param = new JSONObject();
			param.put("paramName", metadata.getNAME());
			param.put("paramDescription", metadata.getDESCRIPTION());
			JSONObject attributes = JSONObject.fromObject(metadata.getATTRIBUTES());
			param.put("paramTag", attributes.get("tag"));
			param.put("paramPosition", attributes.get("position"));
			param.put("paramLength", attributes.get("length"));
			param.put("paramLengthmetric", attributes.get("lengthmetric"));
			param.put("paramType",attributes.get("type"));
			param.put("paramMeaning", attributes.get("meaning"));
			param.put("paramRemark", attributes.get("remark"));
			param.put("paramSignificance", attributes.get("significance"));
			param.put("paramIotype", attributes.get("iotype"));
			
			paramArray.add(param);
		}
		
		return paramArray;
	}

}
