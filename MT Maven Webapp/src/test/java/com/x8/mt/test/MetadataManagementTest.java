package com.x8.mt.test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.x8.mt.controller.MetadataManagementController;
import com.x8.mt.service.MetadataManagementService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
/**
 * 作者： Administrator
 * 时间：2018年3月15日
 * 作用：
 */
public class MetadataManagementTest {
	@Autowired
	MetadataManagementService metadataManagementService;
	@Autowired
	MetadataManagementController metadataManagementController;
	
	@Test
	public void addMetadataStepTwo(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metamodelId", 31);
		map.put("parentMetadataId", 1239);
		map.put("NAME", "table111");
		map.put("DESRIBE", "测试元数据添加");
		map.put("tablename", "table111");
		metadataManagementController.addMetadataStepTwo(request, response, map);
	}
	
	@Test
	public void addMetadataStepOne(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metamodelId", 10);
		System.out.println(metadataManagementController.addMetadataStepOne(request, response, map));
	}
	
	
	@Test
	public void getMetadata(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadataId", 1248);
		map.put("metamodelId", 31);
		System.out.println(metadataManagementController.getMetadataInfo(request,response,map));
	}
	
	@Test
	public void getFieldMetadata(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableMetadataId", 1240);
		System.out.println(metadataManagementController.getFieldMetadata(request,response,map));
	}
	
	@Test
	public void getMetadataViewFirstLevel(){
		System.out.println(metadataManagementService.getMetadataViewFirstLevel(1));
	}

	@Test
	public void getMetadataViewTreeChild(){
		//System.out.println(metadataManagementService.getMetadataViewTreeChildExceptFieldMetadata("1239","mt_db"));
	}

	@Test
	public void getMetadataViewTree(){

		Date a = new Date();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("viewid", 1);
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		//System.out.println(metadataManagementController.getMetadataViewTree(request,response,map));
		
		Date b = new Date();

		long interval = b.getTime() - a.getTime();

		System.out.println("两个时间相差"+interval+"秒");//会打印出相差3秒
	}
}
