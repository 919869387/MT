package com.x8.mt.test;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.x8.mt.controller.ExternalInterfaceController;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
/**
 * 作者： Administrator
 * 时间：2018年5月9日
 * 作用：
 */
public class ExternallnterfaceTest {
	@Autowired
	ExternalInterfaceController externalInterfaceController;
	
	@Test
	public void getProtocolType(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		System.out.println(externalInterfaceController.getProtocolType(request, response));
	}
	
	@Test
	public void getProtocolInfo(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		System.out.println(externalInterfaceController.getProtocolInfo(request, response));
	}
	
	@Test
	public void getProtocolMetadataByprotocolType(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("protocolType", "modbus");
		//map.put("protocolType11", "aaa");
		
		//System.out.println(externalInterfaceController.getProtocolMetadataByprotocolType(request, response,map));
	}
	
	@Test
	public void getProtocolMetadataByprotocolId(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("protocolId", "GFFH");
		//map.put("protocolType", "opc");
		
		//System.out.println(externalInterfaceController.getProtocolMetadataByprotocolId(request, response,map));
	}
	
	@Test
	public void testHEWEIWSDL(){
		String endpoint = "http://192.168.0.108:8082/webservice/ProtocolManagerService";
		String result = "no result!";
		Service service = new Service();
		Call call;
		Object object[] = {"AA","AA","AA","AA"};
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(endpoint);// 远程调用路径
			call.setOperationName("notifying");// 调用的方法名

			// 设置参数名:
			call.addParameter("operationType", // 参数名
					XMLType.XSD_STRING,// 参数类型:String
					ParameterMode.IN);// 参数模式：'IN' or 'OUT'
			call.addParameter("protocolType", // 参数名
					XMLType.XSD_STRING,// 参数类型:String
					ParameterMode.IN);// 参数模式：'IN' or 'OUT'
			call.addParameter("protocolName", // 参数名
					XMLType.XSD_STRING,// 参数类型:String
					ParameterMode.IN);// 参数模式：'IN' or 'OUT'
			call.addParameter("protocolId", // 参数名
					XMLType.XSD_STRING,// 参数类型:String
					ParameterMode.IN);// 参数模式：'IN' or 'OUT'			
			call.invoke(object);// 远程调用
		} catch (ServiceException e) {
			System.out.println("wsdl服务Error-ServiceException");
		} catch (RemoteException e) {
			System.out.println("wsdl服务Error-RemoteException");
			e.printStackTrace();
		}
	}
	
}
