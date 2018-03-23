package com.x8.mt.test;

import java.util.List;

import net.sf.json.JSONArray;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.entity.Metamodel_hierarchy;
import com.x8.mt.service.Metamodel_hierarchyService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
public class Metamodel_hierarchyTest {
	@Autowired
	Metamodel_hierarchyService metamodel_hierarchyService;
	
	@Test
	public void getAllMetamodel_package(){
//		List<Metamodel_hierarchy> list = metamodel_hierarchyService.getAllMetamodel_package();
//		for (Metamodel_hierarchy metamodel_hierarchy : list) {
//			System.out.println(metamodel_hierarchy.getId()+"---"+metamodel_hierarchy.getName());
//		}
	}
	
	@Test
	public void updateMetamodel_hierarchy(){
//		Metamodel_hierarchy metamodel_hierarchy = new Metamodel_hierarchy();
//		metamodel_hierarchy.setId(1006);
//		metamodel_hierarchy.setName("123");
//		metamodel_hierarchy.setDesribe("123");
//		boolean res = metamodel_hierarchyService.updateMetamodel_hierarchy(metamodel_hierarchy);
//		System.out.println(res);
	}
	
	@Test
	public void getMetamodel_hierarchyAttribute(){
//		Metamodel_hierarchy metamodel_hierarchy = metamodel_hierarchyService.getMetamodel_hierarchy(1006);
//		System.out.println(metamodel_hierarchy.getId()+"--"+metamodel_hierarchy.getName()+"---"+metamodel_hierarchy.getDesribe());
	}
	
	@Test  //true
    public void deleteMetamodel_hierarchy(){
//		System.out.println(metamodel_hierarchyService.deleteMetamodel_hierarchy(1008));
	}
	
	@Test
	public void getMetamodelPackageTree(){
		
	}
	
	@Test  //true
    public void insertTest(){
//		Metamodel_hierarchy metamodel_hierarchy = new Metamodel_hierarchy();
//		metamodel_hierarchy.setName("bccere");
//		metamodel_hierarchy.setType(GlobalMethodAndParams.metamodel_hierarchy_PACKAGE);
//		metamodel_hierarchy.setDesribe("dsdsd");
//		metamodel_hierarchy.setParentid(1001);
//		metamodel_hierarchy.setCategory("CORE");
//		metamodel_hierarchy.setMountnode(0);
//		System.out.println(metamodel_hierarchyService.insertMetamodel_hierarchy(metamodel_hierarchy));
	}
	
	@Test
	public void getSonMetamodel_hierarchy(){
//		List<Metamodel_hierarchy> sonMetamodel_hierarchy = metamodel_hierarchyService.getSonMetamodel_hierarchy(201);
//		for (Metamodel_hierarchy metamodel_hierarchy : sonMetamodel_hierarchy) {
//			System.out.println(metamodel_hierarchy.getId()+"---"+metamodel_hierarchy.getName()+"---"+metamodel_hierarchy.getDesribe());
//		}
	}
	

}
