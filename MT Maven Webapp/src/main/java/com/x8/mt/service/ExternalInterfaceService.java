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
	public JSONArray getProtocolMetadataByprotocolType(String protocolType) {
		JSONArray protocols = new JSONArray();

		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("key", GlobalMethodAndParams.JSONKey_protocolType);
		paramMap.put("value", protocolType);

		List<Metadata> protocolMetadatas= iMetadataAnalysisDao.getMetadataByJson(paramMap);

		JSONObject protocol = null;
		for(Metadata metadata : protocolMetadatas){
			if(metadata.getMETAMODELID()!=GlobalMethodAndParams.protocolMetamodelID){
				continue;
			}
			protocol = new JSONObject();
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
			param.putAll((Map)JSONObject.fromObject(metadata.getATTRIBUTES()));//添加私有属性

			if(metadata.getMETAMODELID()==GlobalMethodAndParams.protocolParamArrayMetamodelID){//通信协议参数组元数据
				param.put("paramArrayParams", getProtocolParamArrayMetadataOrParamMetadata(metadata.getID()+""));//递归添加参数元数据
			}

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
		return iExternalInterfaceDao.getProtocolType();
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月16日
	 * 作用:外部接口-请求全部协议清单，包含协议的id,name,type
	 */
	public List<Map<String,String>> getProtocolInfo() {
		return iExternalInterfaceDao.getProtocolInfo();
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月9日
	 * 作用:外部接口-根据一个协议id获取该协议的元数据
	 */
	public JSONObject getProtocolMetadataByprotocolId(String protocolId) {
		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("key", GlobalMethodAndParams.JSONKey_protocolId);
		paramMap.put("value", protocolId);

		List<Metadata> protocolMetadatas = iMetadataAnalysisDao.getMetadataByJson(paramMap);
		if(protocolMetadatas.size()==0){
			return null;
		}else{
			Metadata metadata = protocolMetadatas.get(0);//协议id唯一
			JSONObject protocol = new JSONObject();
			protocol.putAll((Map)JSONObject.fromObject(metadata.getATTRIBUTES()));
			protocol.put("protocolParams", getProtocolParamArrayMetadataOrParamMetadata(metadata.getID()+""));

			return protocol;
		}
	}

}
