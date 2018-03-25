package com.x8.mt.test;


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

import com.x8.mt.dao.IMetaDataDao;
import com.x8.mt.entity.Metadata;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
/**
 * 作者： Administrator
 * 时间：2018年3月15日
 * 作用：
 */
public class TableMappingTest {
	@Autowired
	IMetaDataDao iMetaDataDao;

	@Test
	public void getMetadataViewTree(){
		Map<String,String> data = new HashMap<String,String>();
		data.put("targettableid", "2048");
		List<Metadata> metadatas = iMetaDataDao.getMetadataByMap("$.targettableid", "2048");
		for(Metadata metadata:metadatas){
			System.out.println(metadata.getNAME());
		}
	}
}
