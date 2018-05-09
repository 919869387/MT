package com.x8.mt.test;

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
	public void getCommunicationMessageMetadata(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		System.out.println(externalInterfaceController.getCommunicationMessageMetadata(request, response));
	}
	
}
