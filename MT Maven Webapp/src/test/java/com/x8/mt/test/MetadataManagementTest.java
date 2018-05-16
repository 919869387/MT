package com.x8.mt.test;

import java.util.ArrayList;
import java.util.Date;
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
	public void addTableFieldMappingMetadata(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		List<Map<String, Object>> list = new ArrayList();
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("metamodelId", 100);
		map1.put("parentMetadataId", 83328);
		map1.put("NAME", "表字段映射元数据1");
		map1.put("DESCRIPTION", "表字段映射元数据测试");
		map1.put("mappingtype", "DIRECT");
		map1.put("sourcetableid", 83237);
		map1.put("sourcefieldid", 83239);
		map1.put("targettableid", 83250);
		map1.put("targetfieldid", 83259);
		
//		Map<String, Object> map2 = new HashMap<String, Object>();
//		map2.put("metamodelId", 100);
//		map2.put("parentMetadataId", 83328);
//		map2.put("NAME", "表字段映射元数据2");
//		map2.put("DESCRIPTION", "表字段映射元数据测试");
//		map2.put("mappingtype", "DIRECT");
//		map2.put("sourcetableid", 83237);
//		map2.put("sourcefieldid", 83240);
//		map2.put("targettableid", 83250);
//		map2.put("targetfieldid", 83260);
		
		list.add(map1);
		//list.add(map2);
		
		System.out.println(metadataManagementController.addTableFieldMappingMetadata(request, response, list));
	}
	
	@Test
	public void addTableMappingMetadata(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metamodelId", 99);
		map.put("parentMetadataId", 0);
		map.put("NAME", "表映射元数据1");
		map.put("DESCRIPTION", "表映射元数据测试");
		map.put("mappingtype", "DIRECT");
		map.put("sourcetableid", 83238);
		map.put("targettableid", 83251);
		
		System.out.println(metadataManagementController.addMetadataStepThree(request, response, map));
	}
	
	@Test
	public void getHistoryMetadataPrivateInfo(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tankid", 80932);
		map.put("metamodelid", 10);
		
		System.out.println(metadataManagementController.getHistoryMetadataPrivateInfo(request, response, map));
	}
	
	@Test
	public void getHistoryMetadataCommonInfo(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadataid", 82836);
		
		System.out.println(metadataManagementController.getHistoryMetadataCommonInfo(request, response, map));
	}
	
	@Test
	public void deleteMetadataDepend(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("relationid", 82417);
		
		System.out.println(metadataManagementController.deleteMetadataDepend(request, response, map));
	}
	
	@Test
	public void showMetadataDepend(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadataid", 2070);
		
		System.out.println(metadataManagementController.showMetadataDepend(request, response, map));
	}
	
	@Test
	public void addMetadataDepend(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourcemetadataid", 2070);
		map.put("targetmetadataid", 22742);
		
		System.out.println(metadataManagementController.addMetadataDepend(request, response, map));
	}
	
	@Test
	public void getDependMetadata(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadataid", 2070);
		
		System.out.println(metadataManagementController.getDependMetadata(request, response, map));
	}
	
	@Test
	public void searchMetadata(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", "id1");
		
		System.out.println(metadataManagementController.searchMetadata(request, response, map));
	}
	
	@Test
	public void getFieldMetadataList(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadataid", 39125);
		
		System.out.println(metadataManagementController.getFieldMetadataList(request, response, map));
	}
	
	@Test
	public void getTableMetadataList(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadataid", 39124);
		
		System.out.println(metadataManagementController.getTableMetadataList(request, response, map));
	}
	
	@Test
	public void getDatabaseMetadataList(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		System.out.println(metadataManagementController.getDatabaseMetadataList(request, response));
	}
	
	@Test
	public void daleteMetadataInfo(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID", 2004);
		
		System.out.println(metadataManagementController.daleteMetadataInfo(request, response, map));
	}
	
	@Test
	public void updateMetadataInfoStepTwo(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("type","COMMON");
//		map.put("ID", 1282);
//		map.put("METAMODELID", 31);
//		map.put("NAME", "mountnode");
//		map.put("DESCRIPTION", "AAAAA");
//		map.put("tablename", "mountnode");
		
		map.put("type","PRIVATE");
		map.put("ID", 1282);
		map.put("METAMODELID", 31);
		map.put("metadataTankid", "170");
		map.put("tablename", "mountnode");
	
		System.out.println(metadataManagementController.updateMetadataInfoStepTwo(request, response, map));
	}
	
	@Test
	public void updateMetadataInfoStepOne(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadataId", 1282);
		map.put("metamodelId", 31);
		//map.put("type", "COMMON");
		map.put("type", "PRIVATE");
		
		System.out.println(metadataManagementController.updateMetadataInfoStepOne(request, response, map));
	}
	
	@Test
	public void addMetadataStepThree(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metamodelId", 31);
		map.put("parentMetadataId", 1542);
		map.put("NAME", "table111");
		map.put("DESCRIPTION", "测试元数据添加");
		map.put("tablename", "table111");
		System.out.println(metadataManagementController.addMetadataStepThree(request, response, map));
	}
	
	@Test
	public void addMetadataStepTwo(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metamodelId", 31);
		System.out.println(metadataManagementController.addMetadataStepTwo(request, response, map));
	}
	
	@Test
	public void addMetadataStepOne(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("metamodelId", 10);
		map.put("id", 2);
		System.out.println(metadataManagementController.addMetadataStepOne(request, response, map));
	}
	
	
	@Test
	public void getMetadataInfo(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadataId", 1291);
		map.put("metamodelId", 32);
		System.out.println(metadataManagementController.getMetadataInfo(request,response,map));
	}
	
	@Test
	public void getFieldMetadata(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableMetadataId", 1322);
		System.out.println(metadataManagementController.getFieldMetadata(request,response,map));
	}

	@Test
	public void getMetadataViewTreeChild(){
		//System.out.println(metadataManagementService.getMetadataViewTreeChildExceptFieldMetadata("1239","mt_db"));
	}

	@Test
	public void getMetadataViewTreeNode(){
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("viewid", 1);
		//map.put("id", 2);
		map.put("metadataid", 83236);
		
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		System.out.println(metadataManagementController.getMetadataViewTreeNode(request,response,map));
		
	}
}
