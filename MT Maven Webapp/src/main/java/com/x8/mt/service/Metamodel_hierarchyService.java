package com.x8.mt.service;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.dao.IMetamodel_datatypeDao;
import com.x8.mt.dao.IMetamodel_hierarchyDao;
import com.x8.mt.entity.Metamodel_hierarchy;

@Service
public class Metamodel_hierarchyService {

	@Resource
	IMetamodel_hierarchyDao iMetamodel_hierarchyDao;
	@Resource
	IMetamodel_datatypeDao iMetamodel_datatypeDao;

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月5日
	 * 作用:更新元模型的id,name,describe
	 */
	public boolean updateMetamodel_hierarchy(Metamodel_hierarchy metamodel_hierarchy){
		return iMetamodel_hierarchyDao.updateMetamodel_hierarchy(metamodel_hierarchy);
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月30日
	 * 作用:得到系统与模型树、或者自定义元模型树
	 */
	public JSONArray getMetamodelPackageTree(int id){
		JSONArray tagTrees = new JSONArray();
		Metamodel_hierarchy metamodel_hierarchy = iMetamodel_hierarchyDao.getMetamodel_hierarchy(id);
		JSONObject tagTree = getTreeByParentTag(metamodel_hierarchy);
		tagTrees.add(tagTree);
		return tagTrees;
	}
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月30日
	 * 作用:getMetamodelPackageTree子方法,递归
	 */
	public JSONObject getTreeByParentTag(Metamodel_hierarchy metamodel_hierarchy){
		List<Metamodel_hierarchy> sonMetamodel_hierarchyList= iMetamodel_hierarchyDao.getSonMetamodel_hierarchy(metamodel_hierarchy.getId());
		if(sonMetamodel_hierarchyList.size()==0){
			JSONObject tagTree = new JSONObject();
			tagTree.put("id", metamodel_hierarchy.getId());
			tagTree.put("label", metamodel_hierarchy.getName());
			tagTree.put("type", metamodel_hierarchy.getType());
			return tagTree;
		}
		JSONArray children = new JSONArray();
		for(int i=0;i<sonMetamodel_hierarchyList.size();i++){
			int id = sonMetamodel_hierarchyList.get(i).getId();
			Metamodel_hierarchy sonMetamodel_hierarchy = sonMetamodel_hierarchyList.get(i);
			if(!sonMetamodel_hierarchy.getType().equals(GlobalMethodAndParams.metamodel_hierarchy_PACKAGE)){
				JSONObject node = new JSONObject();
				node.put("id", sonMetamodel_hierarchy.getId());
				node.put("label", sonMetamodel_hierarchy.getName());
				node.put("type", sonMetamodel_hierarchy.getType());
				children.add(node);
			}else{
				JSONObject childTree = getTreeByParentTag(sonMetamodel_hierarchy);
				children.add(childTree);
			}
		}
		JSONObject tagTree = new JSONObject();
		tagTree.put("id", metamodel_hierarchy.getId());
		tagTree.put("label", metamodel_hierarchy.getName());
		tagTree.put("type", metamodel_hierarchy.getType());
		tagTree.put("children", children);
		return tagTree;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月16日
	 * 作用:插入一条Metamodel_hierarchy记录
	 */
	public boolean insertMetamodel_hierarchy(Metamodel_hierarchy metamodel_hierarchy){
		try {
			return iMetamodel_hierarchyDao.insert(metamodel_hierarchy)>0 ? true:false;
		} catch (Exception e) {
			return false;
		}
	}

	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月2日
	 * 作用:根据ID获取元模型
	 */
	public Metamodel_hierarchy getMetamodel_hierarchyById(int id) {
		try {
			return iMetamodel_hierarchyDao.getMetamodel_hierarchy(id);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月2日
	 * 作用:根据元模型包id得到元模型包结构
	 */
	public List<Metamodel_hierarchy> getSonMetamodel_hierarchy(int id) {
		return iMetamodel_hierarchyDao.getSonMetamodel_hierarchy(id);
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月5日
	 * 作用:根据入参id删除元模型或元模型包结构
	 */
	public boolean deleteMetamodel_hierarchy(int id) {
		return iMetamodel_hierarchyDao.deleteMetamodel_hierarchy(id);
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月5日
	 * 作用:根据入参id得到一条Metamodel_hierarchy记录
	 */
	public Metamodel_hierarchy getMetamodel_hierarchy(int id) {
		return iMetamodel_hierarchyDao.getMetamodel_hierarchy(id);
	}

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月12日
	 * 作用:获得所有的元模型包
	 */
	public List<Metamodel_hierarchy> getAllMetamodel_package() {
		
		return iMetamodel_hierarchyDao.getAllMetamodel_package();
	}
	
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2018年3月24日
	 * 作用:查询所有元模型
	 */
	public List<Metamodel_hierarchy> getMetamodel_packageByType() {
		
		return iMetamodel_hierarchyDao.getMetamodel_packageByType();
	}


	/**
	 * 
	 * @param id
	 * @return 判断parentid是否为空，null返回true
	 */
	public boolean getSelfMetamodel_hierarchyParentid(int id) {
		Boolean result = iMetamodel_hierarchyDao.getSelfMetamodel_hierarchyParentid(id);
		if(result==null){
			return true;
		}
		return false;
	}

	
	/**
	 * 
	 * 作者:GodDIspose
	 * 时间:2018年3月14日
	 * 作用:查询所有能被悬挂的元模型
	 */
	public List<Metamodel_hierarchy> getMetaModelByMountNode(){
		try{
			return iMetamodel_hierarchyDao.getMetaModelByMountNode();
		}catch(Exception e){
			return null;
		}
	}

	public List<Metamodel_hierarchy> getAvailableMetamodel(int metamodelid) {
		return iMetamodel_hierarchyDao.getAvailableMetamodel(metamodelid);
	}
}
