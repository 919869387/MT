package com.x8.mt.service;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;

import com.x8.mt.dao.IConnectinfoDao;
import com.x8.mt.dao.IFile_connectinfoDao;
import com.x8.mt.entity.Connectinfo;
import com.x8.mt.entity.File_connectinfo;

@Service
public class File_connectinfoService {

	@Resource
	IFile_connectinfoDao iFile_connectinfoDao;
	
	@Resource
	IConnectinfoDao iConnectinfoDao;

	@Resource
	ConnectinfoService connectinfoService;
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月8日
	 * 作用:根据id得到一条File_connectinfo记录
	 */
	public File_connectinfo getFile_connectinfoByid(int id){
		return iFile_connectinfoDao.getFile_connectinfoByid(id);
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月8日 
	 * 作用:更新一条File_connectinfo记录
	 */
	public boolean updateFile_connectinfo(File_connectinfo File_connectinfo){
		try {
			return iFile_connectinfoDao.update(File_connectinfo)>0 ? true:false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月8日 
	 * 作用:删除一条File_connectinfo记录
	 */
	public boolean deleteFile_connectinfo(int id){
		try {
			int sid = iFile_connectinfoDao.delete(id);
			System.out.println(sid);
			return sid>0 ? true:false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月8日 
	 * 作用:插入一条File_connectinfo记录
	 */
	public boolean insertFile_connectinfo(File_connectinfo File_connectinfo){
		try {
			return iFile_connectinfoDao.insert(File_connectinfo)>0 ? true:false;
		} catch (Exception e) {
			System.out.println("datasource");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月8日
	 * 作用:根据parentid得到所有相应File_connectinfo
	 */
	public File_connectinfo getFile_connectinfoListByparentid(int parentid) {
		return iFile_connectinfoDao.getFile_connectinfoListByparentid(parentid);
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月8日
	 * 作用:得到所有File_connectinfo
	 */
	public List<File_connectinfo> getFile_connectinfo() {
		return iFile_connectinfoDao.getFile_connectinfo();
	}
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月6日
	 * 作用:查询Connectinfo记录
	 */
	public Connectinfo getConnectinfo(int id){
		try {
			return iConnectinfoDao.getConnectinfoByid(iFile_connectinfoDao.getFile_connectinfoByid(id).getParentid());
		} catch (Exception e) {
			return null;
		}
	}
	
//	/**
//	 * 
//	 * 作者:GodDispose
//	 * 时间:2017年12月26日
//	 * 作用:获取目标系统的所有数据库
//	 */
//	public JSONArray getTargetDatabase(){
//		try {
//			JSONArray data = new JSONArray();			
//			List<Connectinfo> connectinfoList = connectinfoService.getConnectinfoList();
//			for(Connectinfo connectinfo : connectinfoList){
//				if(iSourcesystemDao.getSourcesystemByid(connectinfo.getParentid()).getType().equals("targetsystem")){
//					JSONObject node = new JSONObject();
////					node.put("id", File_connectinfoService.getFile_connectinfoListByparentid(connectinfo.getId()).get(0).getId());
////					node.put("name",  File_connectinfoService.getFile_connectinfoListByparentid(connectinfo.getId()).get(0).getDatabasename());
//					node.put("id", connectinfo.getId());
//					node.put("name",  connectinfo.getName());
//					data.add(node);
//				}
//			}
//			
//			return data;
//		} catch (Exception e) {
//			return null;
//		}
//	}
	
//	/**
//	 * 
//	 * 作者:GodDispose
//	 * 时间:2018年1月21日
//	 * 作用:根据数据源id查找数据源的路径
//	 */
//	public String getPath(int id){
//		try{
//			StringBuilder path = new StringBuilder();
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
//			
//			return path.toString();
//		}catch(Exception e){
//			return null;
//		}
//	}

}
