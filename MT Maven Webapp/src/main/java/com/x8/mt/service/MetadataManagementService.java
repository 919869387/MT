/**
 * 
 */
package com.x8.mt.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.Metadata;
import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.dao.IMetadataManagementDao;
/**
 * 作者： Administrator
 * 时间：2018年3月15日
 * 作用：
 */
@Service
public class MetadataManagementService {
	@Resource
    IMetadataManagementDao metadataManagementDao;	
	
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
		return metadataManagementDao.getMetadataViewFirstLevel(viewID);
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年3月16日
	 * 作用:递归查询元数据视图第一层之后的元数据
	 * 
	 */
	public JSONObject getMetadataViewTreeChildExceptFieldMetadata(String metadata_id,String metadata_name,String metadata_metamodelid){
		List<Map<Object, Object>> sonMetadata= metadataManagementDao.getSonMetadata(metadata_id);
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
	 * 作用:获取字段元数据
	 * 
	 */
	public List<Metadata> getFieldMetadata(int tableMetadataId) {
		List<Metadata> fieldMetadatas= metadataManagementDao.getFieldMetadata(tableMetadataId);
		return fieldMetadatas;
	}
}
