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

import com.x8.mt.controller.MetadataAnalysisController;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
public class MetadataAnalysisTest {
	@Autowired
	MetadataAnalysisController metadataAnalysisController;
	
	@Test
	public void fieldChain(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("quanlianid", 83515);
		
		System.out.println(metadataAnalysisController.fieldChain(request, response, map));
	}
	
	@Test
	public void fieldBlood(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xuetongid", 2070);
		
		System.out.println(metadataAnalysisController.fieldBlood(request, response, map));
	}
	
	@Test
	public void fieldImpact(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yingxiangid", 83259);
		
		System.out.println(metadataAnalysisController.fieldImpact(request, response, map));
	}

}
