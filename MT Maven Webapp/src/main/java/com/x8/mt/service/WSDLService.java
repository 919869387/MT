package com.x8.mt.service;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

import net.sf.json.JSONObject;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.entity.Metadata;

@Service
public class WSDLService {

	@Autowired  
	private ThreadPoolTaskExecutor threadPoolTaskExecutor; 
	
	@Resource
	MetadataAnalysisService metadataAnalysisService;
	
	public void protocolOperationWebService(int metamodelid,String metadataid,String operationType,Metadata inputProtocolMetadata){
		if(metamodelid!=GlobalMethodAndParams.protocolMetamodelID&&
				metamodelid!=GlobalMethodAndParams.protocolParamArrayMetamodelID&&
						metamodelid!=GlobalMethodAndParams.protocolParamMetamodelID){
			return;
		}
		
		if(operationType==GlobalMethodAndParams.protocolOperationType_DELATE){//对删除操作传入已经删除的元数据
			if(metamodelid==GlobalMethodAndParams.protocolMetamodelID){
				JSONObject attributes = JSONObject.fromObject(inputProtocolMetadata.getATTRIBUTES());
				doThreadPoolTaskExecutor(GlobalMethodAndParams.protocolOperationType_DELATE,attributes.get(GlobalMethodAndParams.protocolType).toString(),
						attributes.get(GlobalMethodAndParams.protocolName).toString(),
						attributes.get(GlobalMethodAndParams.protocolId).toString());
			}else{
				JSONObject attributes = JSONObject.fromObject(inputProtocolMetadata.getATTRIBUTES());
				doThreadPoolTaskExecutor(GlobalMethodAndParams.protocolOperationType_UPDATE,attributes.get(GlobalMethodAndParams.protocolType).toString(),
						attributes.get(GlobalMethodAndParams.protocolName).toString(),
						attributes.get(GlobalMethodAndParams.protocolId).toString());
			}
			return;
		}
		
		Metadata metadata = metadataAnalysisService.getMetadataById(metadataid);
		if(metadata.getMETAMODELID()==GlobalMethodAndParams.protocolMetamodelID){//对某一协议操作
			JSONObject attributes = JSONObject.fromObject(metadata.getATTRIBUTES());

			doThreadPoolTaskExecutor(operationType,attributes.get(GlobalMethodAndParams.protocolType).toString(),
					attributes.get(GlobalMethodAndParams.protocolName).toString(),
					attributes.get(GlobalMethodAndParams.protocolId).toString());

		}else if(metadata.getMETAMODELID()==GlobalMethodAndParams.protocolParamArrayMetamodelID){//对协议参数组操作
			Metadata protocolMetadata = metadataAnalysisService.getCompositionMetadata(metadataid);
			JSONObject attributes = JSONObject.fromObject(protocolMetadata.getATTRIBUTES());

			doThreadPoolTaskExecutor(GlobalMethodAndParams.protocolOperationType_UPDATE,attributes.get(GlobalMethodAndParams.protocolType).toString(),
					attributes.get(GlobalMethodAndParams.protocolName).toString(),
					attributes.get(GlobalMethodAndParams.protocolId).toString());

		}else if(metadata.getMETAMODELID()==GlobalMethodAndParams.protocolParamMetamodelID){//对协议参数操作
			Metadata fatherMetadata = metadataAnalysisService.getCompositionMetadata(metadataid);
			if(fatherMetadata.getMETAMODELID()==GlobalMethodAndParams.protocolMetamodelID){//父元数据为协议
				JSONObject attributes = JSONObject.fromObject(fatherMetadata.getATTRIBUTES());

				doThreadPoolTaskExecutor(GlobalMethodAndParams.protocolOperationType_UPDATE,attributes.get(GlobalMethodAndParams.protocolType).toString(),
						attributes.get(GlobalMethodAndParams.protocolName).toString(),
						attributes.get(GlobalMethodAndParams.protocolId).toString());
			}else{//父元数据为协议参数组
				Metadata protocolMetadata = metadataAnalysisService.getCompositionMetadata(fatherMetadata.getID()+"");
				JSONObject attributes = JSONObject.fromObject(protocolMetadata.getATTRIBUTES());

				doThreadPoolTaskExecutor(GlobalMethodAndParams.protocolOperationType_UPDATE,attributes.get(GlobalMethodAndParams.protocolType).toString(),
						attributes.get(GlobalMethodAndParams.protocolName).toString(),
						attributes.get(GlobalMethodAndParams.protocolId).toString());
			}
		}
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年6月1日
	 * 作用:发送webservice
	 */
	void sendWebService(String operationType,String protocolType,String protocolName,
			String protocolId) {
		String url="http://192.168.1.103/webservice/protocolservice/protocolManagerService?wsdl"; //wsdl地址 
		String tns = "/method";  						  //命名空间
		String method="notify";	

		try {
			RPCServiceClient serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();
			// 指定调用WebService的URL
			EndpointReference targetEPR = new EndpointReference(url);
			options.setTo(targetEPR);

			// 指定要调用的WSDL文件的命名空间及getValue方法
			QName qn = new QName(tns, method);
			// 指定getValue方法的参数值
			Object[] ob = new Object[] { operationType,protocolType,protocolName,protocolId};
			// 指定getValue方法返回值的数据类型的Class对象
			Class[] classes = new Class[] { Object.class };
			// 调用getValue方法并输出该方法的返回值
			serviceClient.invokeBlocking(qn,ob);
			
		} catch (AxisFault e) {
			System.out.println("webservice出错");
		}
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2018年6月2日
	 * 作用:利用线程池执行
	 */
	void doThreadPoolTaskExecutor(String operationType,String protocolType,String protocolName,
			String protocolId){
		final String finalOperationType = operationType;
		final String finalProtocolType = protocolType;
		final String finalProtocolName = protocolName;
		final String finalProtocolId = protocolId;
		
		threadPoolTaskExecutor.execute(new Runnable() {  
			@Override  
			public void run() {
				sendWebService(finalOperationType,finalProtocolType,finalProtocolName,finalProtocolId);
			}  
		}); 
	}
}
