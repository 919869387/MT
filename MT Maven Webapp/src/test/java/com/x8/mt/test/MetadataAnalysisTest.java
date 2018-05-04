package com.x8.mt.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		map.put("quanlianid", 83259);
		
		System.out.println(metadataAnalysisController.fieldChain(request, response, map));
	}
	
	@Test
	public void fieldBlood(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xuetongid", 83259);
		
		System.out.println(metadataAnalysisController.fieldBlood(request, response, map));
	}
	
	@Test
	public void fieldImpact(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("yingxiangid", 83239);
		
		System.out.println(metadataAnalysisController.fieldImpact(request, response, map));
	}
	
	@Test
	public void isListContainsMap(){
		List<Map<String, Object>> list = new ArrayList();
		Map<String, Object> map1=new HashMap();
		map1.put("id", 123);
		list.add(map1);
		Map<String, Object> map2=new HashMap();
		map2.put("id", 123);
		list.add(map2);
		
		Map<String, Object> newMap=new HashMap();
		newMap.put("id", 123);
		
		System.out.println(list.contains(newMap));
		
		for(Map map : list){
			if(map.equals(newMap)){
				System.out.println("相等");
			}else{
				System.out.println("bu相等");
			}
		}
	}

}
