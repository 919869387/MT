package com.x8.mt.service;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;

import com.x8.mt.dao.IConnectinfoDao;
import com.x8.mt.dao.IDatasource_connectinfoDao;
import com.x8.mt.entity.Connectinfo;
import com.x8.mt.entity.Datasource_connectinfo;

@Service
public class Datasource_connectinfoService {

	@Resource
	IDatasource_connectinfoDao iDatasource_connectinfoDao;
	
	@Resource
	IConnectinfoDao iConnectinfoDao;
		@Resource
	Datasource_connectinfoService datasource_connectinfoService;
	@Resource
	ConnectinfoService connectinfoService;
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月22日
	 * 作用:根据id得到一条Datasource_connectinfo记录
	 */
	public Datasource_connectinfo getDatasource_connectinfoByid(int id){
		return iDatasource_connectinfoDao.getDatasource_connectinfoByid(id);
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月15日
	 * 作用:更新一条Datasource_connectinfo记录
	 */
	public boolean updateDatasource_connectinfo(Datasource_connectinfo datasource_connectinfo){
		try {
			return iDatasource_connectinfoDao.update(datasource_connectinfo)>0 ? true:false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月14日
	 * 作用:删除一条Datasource_connectinfo记录
	 */
	public boolean deleteDatasource_connectinfo(int id){
		try {
			int sid = iDatasource_connectinfoDao.delete(id);
			System.out.println(sid);
			return sid>0 ? true:false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月13日
	 * 作用:插入一条Datasource_connectinfo记录
	 */
	public boolean insertDatasource_connectinfo(Datasource_connectinfo datasource_connectinfo){
		try {
			return iDatasource_connectinfoDao.insert(datasource_connectinfo)>0 ? true:false;
		} catch (Exception e) {
			System.out.println("datasource");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月21日
	 * 作用:根据parentid得到所有Datasource_connectinfo
	 */
	public Datasource_connectinfo getDatasource_connectinfoListByparentid(int parentid) {
		return iDatasource_connectinfoDao.getDatasource_connectinfoListByparentid(parentid);
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月17日
	 * 作用:得到所有Datasource_connectinfo
	 */
	public List<Datasource_connectinfo> getDatasource_connectinfo() {
		return iDatasource_connectinfoDao.getDatasource_connectinfo();
	}
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月6日
	 * 作用:查询Connectinfo记录
	 */
	public Connectinfo getConnectinfo(int id){
		try {
			return iConnectinfoDao.getConnectinfoByid(iDatasource_connectinfoDao.getDatasource_connectinfoByid(id).getParentid());
		} catch (Exception e) {
			return null;
		}
	}
	

	

	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月26日
	 * 作用:获取目标系统的所有数据库
	 */
	public JSONArray getTargetDatabase(){
		try {
			JSONArray data = new JSONArray();			
//			List<Connectinfo> connectinfoList = connectinfoService.getConnectinfoList();
//			for(Connectinfo connectinfo : connectinfoList){
//				if(iSourcesystemDao.getSourcesystemByid(connectinfo.getParentid()).getType().equals("targetsystem")){
//					JSONObject node = new JSONObject();
////					node.put("id", datasource_connectinfoService.getDatasource_connectinfoListByparentid(connectinfo.getId()).get(0).getId());
////					node.put("name",  datasource_connectinfoService.getDatasource_connectinfoListByparentid(connectinfo.getId()).get(0).getDatabasename());
//					node.put("id", connectinfo.getId());
//					node.put("name",  connectinfo.getName());
//					data.add(node);
//				}
//			}
//			
			return data;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年1月21日
	 * 作用:根据数据源id查找数据源的路径
	 */
	public String getPath(int id){
		try{
			StringBuilder path = new StringBuilder();
//				
//			int sourceId = connectinfoService.getConnectinfoByid(id).getParentid();
//
//			while(sourcesystemService.getSourcesystemsById(sourceId) != null){
//					if(sourcesystemService.getSourcesystemsById(sourceId).getType().equals("parent")){
//						path.insert(0, sourcesystemService.getSourcesystemsById(sourceId).getName());
//						break;
//					}else{
//						path.insert(0, "/" + sourcesystemService.getSourcesystemsById(sourceId).getName());
//						sourceId = sourcesystemService.getSourcesystemsById(sourceId).getParentid();
//					}
//			}
			
			return path.toString();
		}catch(Exception e){
			return null;
		}
	}

}
