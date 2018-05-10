package com.x8.mt.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public void getProtocolMetadata(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("protocolType", "modbus");
		//map.put("protocolType", "opc");
		
		System.out.println(externalInterfaceController.getProtocolMetadata(request, response,map));
	}
	
}
