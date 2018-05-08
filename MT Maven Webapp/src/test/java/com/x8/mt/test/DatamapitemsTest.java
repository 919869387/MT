package com.x8.mt.test;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.entity.Metadata;
import com.x8.mt.service.MetaDataService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
public class DatamapitemsTest {
	@Resource
	MetaDataService metaDataService;
	
	@Test
	public void testString2Json() {

		// 找到所有的99对应的表映射元数据列表
		List<Metadata> mappingList = metaDataService
				.getMetadataByMetaModelId(Integer
						.parseInt(GlobalMethodAndParams.MetaDataTableMappingModelId));
		System.out.println(mappingList.size() + ":Size");
		for (Metadata metadata : mappingList) {
			String attributes = metadata.getATTRIBUTES();
			JSONObject json1 = JSONObject.fromObject(attributes);
			System.out.println(attributes);
			
			System.out.println(json1);
			System.out.println(json1.get("sourcetableid"));
			System.out.println(json1.get("targettableid"));
			
			if (json1.containsKey("sourcetableid")) {
				Object sourcetablidObj = json1.get("sourcetableid");
				int sourcetablid = Integer.parseInt(sourcetablidObj.toString());

				Object targettableidObj = json1.get("targettableid");
				int targettableid = Integer.parseInt(targettableidObj
						.toString());

				System.out.println(targettableid + "---" + sourcetablid);

			}
		}
	}
}
