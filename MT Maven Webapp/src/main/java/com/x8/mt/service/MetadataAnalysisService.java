package com.x8.mt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.dao.IMetadataAnalysisDao;
import com.x8.mt.entity.Metadata;

@Service
public class MetadataAnalysisService {

	@Resource
	IMetadataAnalysisDao iMetadataAnalysisDao;

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月3日
	 * 作用:字段影响分析
	 * 
	 * type为false时为影响分析
	 * type为true时为血统分析 
	 */
	public void fieldAnalysis(String fieldid,List<Map<String, String>> nodesList,
			List<Map<String, String>> linksList,boolean type) {
		
		Metadata sourceFieldMetadata = iMetadataAnalysisDao.getMetadataById(fieldid);
		
		addNode(sourceFieldMetadata, nodesList);//添加源表字段节点
		
		Map<String,String> paramMap = new HashMap<String, String>();
		if(!type){
			paramMap.put("key", GlobalMethodAndParams.JSONKey_sourcefieldid);
		}else{
			paramMap.put("key", GlobalMethodAndParams.JSONKey_targetfieldid);
		}
		paramMap.put("value", fieldid);
		
		List<Metadata> metadataLists = iMetadataAnalysisDao.getMetadataByJson(paramMap);//查找到字段元数据
		if(metadataLists.size()==0){
			//说明分析在没有目标字段，终止递归
			return;
		}else{
			for(Metadata metadata : metadataLists){
				JSONObject fieldAttributes = JSONObject.fromObject(metadata.getATTRIBUTES());

				String targetFieldid = null;
				String targetTableid = null;
				if(!type){
					targetFieldid = fieldAttributes.get("targetfieldid").toString();
					targetTableid = fieldAttributes.get("targettableid").toString();
				}else{
					targetFieldid = fieldAttributes.get("sourcefieldid").toString();
					targetTableid = fieldAttributes.get("sourcetableid").toString();
				}

				Map<String, String> mapLinktoTargetField=new HashMap<String, String>();
				mapLinktoTargetField.put("sourceid", fieldid);
				mapLinktoTargetField.put("targetid", targetFieldid);
				if(linksList.contains(mapLinktoTargetField)){
					//说明分析出现了环路，终止递归
					return;
				}else{
					linksList.add(mapLinktoTargetField);//添加源表字段到目标表字段的连线

					Metadata targetFieldMetadata = iMetadataAnalysisDao.getMetadataById(targetFieldid);
					
					addNode(targetFieldMetadata, nodesList);//添加目标表字段节点
					
					Map<String, String> mapLinktoTargetTable=new HashMap<String, String>();
					mapLinktoTargetTable.put("sourceid", targetFieldid);
					mapLinktoTargetTable.put("targetid", targetTableid);
					if(!linksList.contains(mapLinktoTargetTable)){
						linksList.add(mapLinktoTargetTable);//添加字段到表的连线
						
						Metadata targetTableMetadata = iMetadataAnalysisDao.getMetadataById(targetTableid);
						
						addNode(targetTableMetadata, nodesList);//添加表节点
						
						Metadata targetDatabaseMetadata = iMetadataAnalysisDao.getCompositionMetadata(targetTableid);
						
						Map<String, String> mapLinktoTargetDatabase=new HashMap<String, String>();
						mapLinktoTargetDatabase.put("sourceid", targetTableid);
						mapLinktoTargetDatabase.put("targetid", targetDatabaseMetadata.getID()+"");
						
						if(!linksList.contains(mapLinktoTargetDatabase)){
							linksList.add(mapLinktoTargetDatabase);//添加表到数据库连线
							
							addNode(targetDatabaseMetadata, nodesList);//添加数据库节点
						}
					}
				}
				fieldAnalysis(targetFieldid,nodesList,linksList,type);//继续递归进行分析
			}
		}
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月4日
	 * 作用:内部方法，添加节点
	 */
	private void addNode(Metadata metadata,List<Map<String, String>> nodesList){
		if(metadata==null){
			return;
		}
		String status = null;
		if(metadata.getMETAMODELID()==32){
			status="字段类型";
		}else if(metadata.getMETAMODELID()==31){
			status="表类型";
		}else if(metadata.getMETAMODELID()==10){
			status="数据库类型";
		}

		Map<String, String> mapNode=new HashMap<String, String>();
		mapNode.put("id", metadata.getID()+"");
		mapNode.put("status", status);
		mapNode.put("name", metadata.getNAME());
		if(!nodesList.contains(mapNode)){
			nodesList.add(mapNode);//添加节点
		}
	}

}
