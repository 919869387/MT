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
import com.x8.mt.entity.Metamodel_relation;
import com.x8.mt.service.Metamodel_hierarchyService;
import com.x8.mt.service.Metamodel_relationService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
public class Metamodel_relationTest {
	@Autowired
	Metamodel_relationService metamodel_relationService ;
	
//	@Test
//	public void deleteMetamodel_relation(){
//		boolean result = metamodel_relationService.deleteMetamodel_relation(159);
//		System.out.println(result);
//	}

	

}
