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
			protocol.putAll((Map)JSONObject.fromObject(metadata.getATTRIBUTES()));//添加私有属性
			protocol.put("protocolParams", getProtocolParamArrayMetadataOrParamMetadata(metadata.getID()+""));
			
			protocols.add(protocol);
		}
		return protocols;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月9日
	 * 获取通信报文元数据的参数组元数据或参数元数据
	 */
	public JSONArray getProtocolParamArrayMetadataOrParamMetadata(String metadataid){
		JSONArray paramArray = new JSONArray();
		
		List<Metadata> protocolParams = iExternalInterfaceDao.getCompositionRelatedmetadata(metadataid);
		
		JSONObject param = null;//可能是参数组元数据或参数元数据
		for(Metadata metadata : protocolParams){
			param = new JSONObject();
			if(metadata.getMETAMODELID()==GlobalMethodAndParams.protocolParamArrayMetamodelID){//通信协议参数组元数据
				param.put("paramArrayName", metadata.getNAME());
				param.put("paramArrayDescription", metadata.getDESCRIPTION());
				param.put("paramArrayParams", getProtocolParamArrayMetadataOrParamMetadata(metadata.getID()+""));//递归添加参数元数据
			}else{//通信协议参数元数据
				param.put("paramName", metadata.getNAME());
				param.put("paramDescription", metadata.getDESCRIPTION());
			}
			param.putAll((Map)JSONObject.fromObject(metadata.getATTRIBUTES()));//添加私有属性
			paramArray.add(param);
		}
		return paramArray;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月15日
	 * 作用:获取通信报文元数据种类
	 */
	public List<String> getProtocolType() {
		List<String> protocolType = iExternalInterfaceDao.getProtocolType();
		return protocolType;
	}

}
