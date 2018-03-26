package com.x8.mt.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.TransformMetadata;
import com.x8.mt.dao.IMetaDataDao;
import com.x8.mt.dao.IMetaDataRelationDao;
import com.x8.mt.dao.IMetadataManagementDao;
import com.x8.mt.dao.IMetadataTankDao;
import com.x8.mt.dao.IMetamodel_datatypeDao;
import com.x8.mt.dao.IMetamodel_hierarchyDao;
import com.x8.mt.entity.MetaDataRelation;
import com.x8.mt.entity.Metadata;
import com.x8.mt.entity.MetadataTank;
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
	@Resource
	IMetaDataRelationDao iMetaDataRelationDao;
	@Resource
	IMetadataTankDao iMetadataTankDao;
	@Resource
	IMetaDataDao iMetaDataDao;

	public boolean existMetadata(String metadataId) {
		if(iMetaDataDao.getMetadataById(Integer.parseInt(metadataId))!=null){
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月25日
	 * 作用:删除元数据信息[因为数据库中是级联删除,删除主键，外键跟着删除。只需要删除metadata表即可]
	 *  	1.删除metadata_tank表中记录
	 *  	2.删除metadata_relation表中记录
	 *  	3.删除metadata表中记录
	 */
	@Transactional
	public boolean daleteMetadataInfo(String metadataId,List<Object> count) {

		List<String> sonMetadataIds = iMetaDataRelationDao.getSonMetadataID(metadataId);

		for(int i=0;i<sonMetadataIds.size();i++){
			String sonMetadataId = sonMetadataIds.get(i);

			//递归先删除儿子元数据
			daleteMetadataInfo(sonMetadataId,count);
		}

		if(imetadataManagementDao.daleteMetadata(metadataId)==1){
			count.add(1);
		}else{
			throw new RuntimeException("daleteMetadata Error");
		}

		return true;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月26日
	 * 作用:修改元数据私有信息
	 *  	1.将元数据在metadata表修改Attributes
	 *  	2.再修改metadata_tank表Attributes
	 */
	@Transactional
	public boolean updateMetadataInfoForPRIVATE(Map<String, Object> map) {

		//1.将元数据在metadata表修改
		String metaModelId = (map.get(GlobalMethodAndParams.Public_Metamodel_METAMODELID)).toString();

		List<String> attributesField = imetadataManagementDao.getAttributesField(metaModelId);
		JSONObject attributes = TransformMetadata.createAttributes(map, attributesField);
		
		Metadata metadata = new Metadata();
		metadata.setID(Integer.parseInt(map.get("ID").toString()));
		metadata.setATTRIBUTES(attributes.toString());
		
		if(!(imetadataManagementDao.updateMetadatAttributes(metadata)>0)){
			throw new RuntimeException("updateMetadatAttributes Error");
		}

		//2.加入metadata_tank表
		MetadataTank metadataTank = new MetadataTank();
		metadataTank.setATTRIBUTES(metadata.getATTRIBUTES());
		metadataTank.setID(Integer.parseInt(map.get("metadataTankid").toString()));

		if(!(iMetadataTankDao.updateMetaDataTankAttributes(metadataTank)>0)){
			throw new RuntimeException("updateMetaDataTankAttributes Error");
		}

		return true;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月24日
	 * 作用:修改元数据信息
	 *  	1.将元数据在metadata表修改
	 *  	2.修改后的记录加入metadata_tank表
	 */
	@Transactional
	public int updateMetadataInfoForCommon(Map<String, Object> map) {

		//1.将元数据在metadata表修改
		Metadata metadata = iMetaDataDao.getMetadataById(Integer.parseInt(map.get("ID").toString()));
		
		metadata.setNAME(map.get("NAME").toString());
		metadata.setDESCRIPTION(map.get("DESCRIPTION").toString());

		Date updateDataTime = new Date();
		metadata.setUPDATETIME(updateDataTime);

		metadata.setVERSION(metadata.getVERSION()+1);

		if(!(imetadataManagementDao.updateMetadata(metadata)>0)){
			throw new RuntimeException("updateMetadata Error");
		}
		
		//2.加入metadata_tank表
		MetadataTank metadataTank = new MetadataTank();
		metadataTank.setCHECKSTATUS(metadata.getCHECKSTATUS());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			metadataTank.setCREATETIME(sdf.parse(metadata.getCREATETIME()));
		} catch (ParseException e) {
			throw new RuntimeException("DATE Transform Error");
		}

		metadataTank.setDESCRIPTION(metadata.getDESCRIPTION());
		metadataTank.setKeyid(metadata.getID());
		metadataTank.setMETAMODELID(metadata.getMETAMODELID());
		metadataTank.setNAME(metadata.getNAME());
		metadataTank.setUPDATETIME(updateDataTime);
		metadataTank.setVERSION(metadata.getVERSION());
		metadataTank.setCOLLECTJOBID(metadata.getCOLLECTJOBID());

		if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
			throw new RuntimeException("insertMetaDataTank Error");
		}

		return metadataTank.getID();
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月23日
	 * 作用:insert元数据,插入实际元数据
	 *  	1.将元数据加入到metadata表
	 *  	2.加入metadata_relation表
	 *  	3.加入metadata_tank表
	 */
	@Transactional
	public boolean addMetadata(Map<String,Object> map){

		String parentMetadataIdStr = map.get("parentMetadataId").toString();
		String metamodelIdStr = map.get("metamodelId").toString();
		Date dataTime = new Date();

		//1.将元数据加入到metadata表
		Metadata metadata = new Metadata();
		metadata.setMETAMODELID(Integer.parseInt(metamodelIdStr));
		metadata.setNAME(map.get("NAME").toString());
		metadata.setDESCRIPTION(map.get("DESCRIPTION").toString());
		metadata.setCHECKSTATUS("1");
		metadata.setCREATETIME(dataTime);
		metadata.setUPDATETIME(dataTime);
		metadata.setVERSION(1);

		map.remove("metamodelId");
		map.remove("NAME");
		map.remove("DESCRIPTION");
		map.remove("parentMetadataId");

		metadata.setATTRIBUTES(JSONObject.fromObject(map).toString());

		//1.将元数据加入到metadata表
		if(!(imetadataManagementDao.insertMetadata(metadata)>0)){
			throw new RuntimeException("insertMetadata Error");
		}

		//2.加入metadata_relation表
		int metadataId = metadata.getID();

		MetaDataRelation metaDataRelation = new MetaDataRelation();
		metaDataRelation.setMetaDataId(Integer.parseInt(parentMetadataIdStr));
		metaDataRelation.setRelateMetaDataId(metadataId);
		metaDataRelation.setType(GlobalMethodAndParams.COMPOSITION);

		if(!(iMetaDataRelationDao.insertMetaDataRelation(metaDataRelation)>0)){
			throw new RuntimeException("insertMetaDataRelation Error");
		}

		//3.加入metadata_tank表
		MetadataTank metadataTank = new MetadataTank();
		metadataTank.setCHECKSTATUS(metadata.getCHECKSTATUS());
		metadataTank.setATTRIBUTES(metadata.getATTRIBUTES());
		metadataTank.setCREATETIME(dataTime);
		metadataTank.setDESCRIPTION(metadata.getDESCRIPTION());
		metadataTank.setKeyid(metadataId);
		metadataTank.setMETAMODELID(metadata.getMETAMODELID());
		metadataTank.setNAME(metadata.getNAME());
		metadataTank.setUPDATETIME(dataTime);
		metadataTank.setVERSION(metadata.getVERSION());
		metadataTank.setCOLLECTJOBID(0);

		if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
			throw new RuntimeException("insertMetaDataTank Error");
		}

		return true;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:查询得到元数据对应的元模型信息,这个方法显示全部元模型的信息
	 * 
	 */
	public JSONArray getMetamodelInfoForAddMetadata(String metamodelid){

		JSONObject metamodelInfoMap = getMetamodelInfo(metamodelid);

		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_ATTRIBUTES);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_COLLECTJOBID);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_METAMODELID);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_CHECKSTATUS);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_VERSION);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_UPDATETIME);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_CREATETIME);
		metamodelInfoMap.remove(GlobalMethodAndParams.Public_Metamodel_ID);

		JSONArray metamodelInfos = new JSONArray();  
		Iterator iterator = metamodelInfoMap.keys();  
		while(iterator.hasNext()){
			JSONObject metamodelInfo = new JSONObject();
			String key = (String) iterator.next();  
			String describe = metamodelInfoMap.get(key).toString();
			metamodelInfo.put("key", key);
			metamodelInfo.put("describe", describe);
			metamodelInfos.add(metamodelInfo);
		}  
		return metamodelInfos;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:查询得到COMPOSITION类型的儿子元模型
	 * 
	 */
	public List<JSONObject> getCOMPOSITIONMetamodel(String metamodelId){
		List<Metamodel_hierarchy> metamodel_hierarchys = iMetamodel_hierarchyDao.getCOMPOSITIONMetamodel(metamodelId);
		if(metamodel_hierarchys.size()==0){
			return null;
		}else{
			List<JSONObject> includeMetaModel = new ArrayList<JSONObject>();

			for(int i=0;i<metamodel_hierarchys.size();i++){
				Metamodel_hierarchy metamodel_hierarchy = metamodel_hierarchys.get(i);
				JSONObject metamodel = new JSONObject();
				metamodel.put("modelid",metamodel_hierarchy.getId());
				metamodel.put("name",metamodel_hierarchy.getName());
				includeMetaModel.add(metamodel);
			}

			return includeMetaModel;
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
			Map<String, Object> metadataMap = TransformMetadata.transformMetadataToMap(metadata);
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
	public Map<String, Object> getMetadata(String metadataId) {
		Metadata metadata= imetadataManagementDao.getMetadata(metadataId);
		Map<String, Object> metadataMap = TransformMetadata.transformMetadataToMap(metadata);
		return metadataMap;
	}

}
