package com.x8.mt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.TransformMetadata;
import com.x8.mt.dao.IMetadataManagementDao;
import com.x8.mt.dao.IMetamodel_datatypeDao;
import com.x8.mt.dao.IMetamodel_hierarchyDao;
import com.x8.mt.entity.Metadata;
import com.x8.mt.entity.Metamodel_datatype;
import com.x8.mt.entity.Metamodel_hierarchy;
/**
 * 作者： allen
 * 时间：2018年3月15日
 */
@Service
public class MetadataManagementService {
	@Resource
	IMetadataManagementDao imetadataManagementDao;	
	@Resource
	IMetamodel_datatypeDao iMetamodel_datatypeDao;
	@Resource
	IMetamodel_hierarchyDao iMetamodel_hierarchyDao;

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:insert元数据,插入实际元数据
	 *  	1.将元数据加入到metadata表
	 *  	2.加入metadata_relation表
	 *  	3.加入metadata_tank表
	 */
	public boolean addMetadata(Map<String,Object> map){
		
		String parentMetadataIdStr = map.get("parentMetadataId").toString();
		String metamodelIdStr = map.get("metamodelId").toString();
		Date dataTime = new Date();
		
		//1.将元数据加入到metadata表
		Metadata metadata = new Metadata();
		metadata.setMetaModelId(Integer.parseInt(metamodelIdStr));
		metadata.setName(map.get("NAME").toString());
		metadata.setDescription(map.get("DESRIBE").toString());
		metadata.setCheckStatus("1");
		metadata.setCreateTime(dataTime);
		metadata.setUpdateTime(dataTime);
		metadata.setVersion(1);
		
		map.remove("metamodelId");
		map.remove("NAME");
		map.remove("DESRIBE");
		map.remove("parentMetadataIdStr");
		
		metadata.setAttributes(JSONObject.fromObject(map).toString());
		
		int metadataId = imetadataManagementDao.insertMetadata(metadata);
		
		System.out.println(metadataId);
		return false;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:查询得到元数据对应的元模型信息,这个方法显示全部元模型的信息
	 * 
	 */
	public JSONObject getMetamodelInfoForAddMetadata(String metamodelid){

		JSONObject metamodelInfo = getMetamodelInfo(metamodelid);

		metamodelInfo.remove(GlobalMethodAndParams.Public_Metamodel_ATTRIBUTES);
		metamodelInfo.remove(GlobalMethodAndParams.Public_Metamodel_COLLECTJOBID);
		metamodelInfo.remove(GlobalMethodAndParams.Public_Metamodel_METAMODELID);
		metamodelInfo.remove(GlobalMethodAndParams.Public_Metamodel_CHECKSTATUS);
		metamodelInfo.remove(GlobalMethodAndParams.Public_Metamodel_VERSION);
		metamodelInfo.remove(GlobalMethodAndParams.Public_Metamodel_UPDATETIME);
		metamodelInfo.remove(GlobalMethodAndParams.Public_Metamodel_CREATETIME);
		metamodelInfo.remove(GlobalMethodAndParams.Public_Metamodel_ID);

		return metamodelInfo;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:查询得到COMPOSITION类型的儿子元模型
	 * 
	 */
	public JSONObject getCOMPOSITIONMetamodel(String metamodelId){
		List<Metamodel_hierarchy> metamodel_hierarchys = iMetamodel_hierarchyDao.getCOMPOSITIONMetamodel(metamodelId);
		if(metamodel_hierarchys.size()==0){
			return null;
		}else{
			JSONObject metamodels = new JSONObject();
			for(int i=0;i<metamodel_hierarchys.size();i++){
				Metamodel_hierarchy metamodel_hierarchy = metamodel_hierarchys.get(i);
				metamodels.put(metamodel_hierarchy.getId(), metamodel_hierarchy.getName());
			}
			return metamodels;
		}
	}


	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:查询得到元数据对应的元模型信息,这个方法显示全部元模型的信息
	 * 
	 */
	public JSONObject getMetamodelInfo(Object attribute_metamodelid){
		Map<Object,Object> metamodelidSelectMap = new HashMap<Object,Object>();
		metamodelidSelectMap.put(GlobalMethodAndParams.Public_metamodelid_Name, GlobalMethodAndParams.PublicMetamodelId);
		metamodelidSelectMap.put(GlobalMethodAndParams.Attribute_metamodelid_Name, attribute_metamodelid);
		List<Metamodel_datatype> metamodel_datatypes = iMetamodel_datatypeDao.getMetamodelDatatype_PublicAndAttributes(metamodelidSelectMap);

		JSONObject metamodelInfo = new JSONObject();
		for(int i=0;i<metamodel_datatypes.size();i++){
			Metamodel_datatype metamodel_datatype= metamodel_datatypes.get(i);
			metamodelInfo.put(metamodel_datatype.getName(), metamodel_datatype.getDesribe());
		}
		metamodelInfo.remove(GlobalMethodAndParams.Public_Metamodel_ATTRIBUTES);
		return metamodelInfo;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:查询得到元数据视图树
	 * 
	 */
	public JSONArray getMetadataViewTree(int viewID){
		JSONArray metadataViewTree = new JSONArray();
		List<Map<Object, Object>> firstLevelMap = getMetadataViewFirstLevel(viewID);
		List<String> package_ids = new ArrayList<String>();
		for(Map<Object, Object> map: firstLevelMap){  
			String package_id = map.get(GlobalMethodAndParams.Metadata_package_id).toString();
			String package_name = map.get(GlobalMethodAndParams.Metadata_package_name).toString();
			if(package_ids.contains(package_id)){//如果这个包已经出现过，那么在metadataViewTree的最后一个JSONObject就是相同的package_id,并且package_id下面都有元数据
				JSONObject samePackage_idJSON = (JSONObject) metadataViewTree.get(metadataViewTree.size()-1);
				JSONArray children = (JSONArray) samePackage_idJSON.get(GlobalMethodAndParams.MetadataViewTree_children);

				String metadata_id = map.get(GlobalMethodAndParams.Metadata_metadata_id).toString();
				String metadata_name = map.get(GlobalMethodAndParams.Metadata_metadata_name).toString();
				String metadata_metamodelid = map.get(GlobalMethodAndParams.Metadata_metadata_metamodelid).toString();
				children.add(getMetadataViewTreeChildExceptFieldMetadata(metadata_id,metadata_name,metadata_metamodelid));

				samePackage_idJSON.put(GlobalMethodAndParams.MetadataViewTree_children, children);

				metadataViewTree.remove(metadataViewTree.size()-1);
				metadataViewTree.add(samePackage_idJSON);
			}else{//如果这个包没有出现过
				JSONObject metadataViewTreeNode = new JSONObject();
				metadataViewTreeNode.put(GlobalMethodAndParams.MetadataViewTree_id, package_id);
				metadataViewTreeNode.put(GlobalMethodAndParams.MetadataViewTree_label, package_name);

				if(map.get(GlobalMethodAndParams.Metadata_metadata_id)!=null){//说明这个包下面有元数据
					String metadata_id = map.get(GlobalMethodAndParams.Metadata_metadata_id).toString();
					String metadata_name = map.get(GlobalMethodAndParams.Metadata_metadata_name).toString();
					String metadata_metamodelid = map.get(GlobalMethodAndParams.Metadata_metadata_metamodelid).toString();

					JSONArray metadataViewTreeChild = new JSONArray();
					metadataViewTreeChild.add(getMetadataViewTreeChildExceptFieldMetadata(metadata_id,metadata_name,metadata_metamodelid));

					metadataViewTreeNode.put(GlobalMethodAndParams.MetadataViewTree_children, metadataViewTreeChild);
				}

				metadataViewTree.add(metadataViewTreeNode);

				package_ids.add(package_id);
			}

		}

		return metadataViewTree;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:查询元数据视图第一层
	 * 
	 */
	public List<Map<Object, Object>> getMetadataViewFirstLevel(int viewID){
		return imetadataManagementDao.getMetadataViewFirstLevel(viewID);
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:递归查询元数据视图第一层之后的元数据
	 * 
	 */
	public JSONObject getMetadataViewTreeChildExceptFieldMetadata(String metadata_id,String metadata_name,String metadata_metamodelid){
		List<Map<Object, Object>> sonMetadata= imetadataManagementDao.getSonMetadata(metadata_id);
		if(sonMetadata.size()==0){
			JSONObject tagTree = new JSONObject();
			tagTree.put(GlobalMethodAndParams.MetadataViewTree_id, metadata_id);
			tagTree.put(GlobalMethodAndParams.MetadataViewTree_label, metadata_name);
			tagTree.put(GlobalMethodAndParams.MetadataViewTree_metamodelid, metadata_metamodelid);
			return tagTree;
		}else{
			JSONArray children = new JSONArray();
			for(int i=0;i<sonMetadata.size();i++){
				String id = sonMetadata.get(i).get(GlobalMethodAndParams.Metadata_metadata_id).toString();
				String name = sonMetadata.get(i).get(GlobalMethodAndParams.Metadata_metadata_name).toString();
				String childMetadata_metamodelid = sonMetadata.get(i).get(GlobalMethodAndParams.Metadata_metadata_metamodelid).toString();
				if(!childMetadata_metamodelid.equals(GlobalMethodAndParams.TableMedamodelId_InDatabase)){
					//如果不是表元数据，继续向下递归
					JSONObject childTree = getMetadataViewTreeChildExceptFieldMetadata(id,name,childMetadata_metamodelid);
					children.add(childTree);
				}else{
					//如果是表元数据，就不递归，不需要加字段元数据
					JSONObject tagTree = new JSONObject();
					tagTree.put(GlobalMethodAndParams.MetadataViewTree_id, id);
					tagTree.put(GlobalMethodAndParams.MetadataViewTree_label, name);
					tagTree.put(GlobalMethodAndParams.MetadataViewTree_metamodelid, childMetadata_metamodelid);
					children.add(tagTree);
				}
			}
			JSONObject tagTree = new JSONObject();
			tagTree.put(GlobalMethodAndParams.MetadataViewTree_id, metadata_id);
			tagTree.put(GlobalMethodAndParams.MetadataViewTree_label, metadata_name);
			tagTree.put(GlobalMethodAndParams.MetadataViewTree_metamodelid, metadata_metamodelid);
			tagTree.put(GlobalMethodAndParams.MetadataViewTree_children, children);
			return tagTree;
		}

	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月19日
	 * 作用:获取字段元数据,要将元数据的attributes属性展开
	 * 
	 */
	public List<Object> getFieldMetadata(int tableMetadataId) {
		List<Object> metadataList = new ArrayList<Object>();
		List<Metadata> fieldMetadatas= imetadataManagementDao.getFieldMetadata(tableMetadataId);
		for(int i = 0 ; i < fieldMetadatas.size() ; i++) {
			Metadata metadata= (Metadata)fieldMetadatas.get(i);
			Object metadataMap = TransformMetadata.transformMetadataToMap(metadata);
			metadataList.add(metadataMap);
		}
		return metadataList;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月19日
	 * 作用:根据元数据id,获取某一个元数据
	 */
	public Object getMetadata(String metadataId) {
		Metadata metadata= imetadataManagementDao.getMetadata(metadataId);
		Object metadataMap = TransformMetadata.transformMetadataToMap(metadata);
		return metadataMap;
	}
}
