package com.x8.mt.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.stereotype.Service;

import com.x8.mt.dao.IConnectinfoDao;
import com.x8.mt.entity.Connectinfo;
import com.x8.mt.entity.Datasource_connectinfo;

@Service
public class ConnectinfoService {
//	@Resource
//	KettleMetadataCollectService kettleMetadataCollectService;
//	
	@Resource
	IConnectinfoDao iConnectinfoDao;
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:根据id得到一条Connectinfo记录
	 */
	public Connectinfo getConnectinfoByid(int id){
		return iConnectinfoDao.getConnectinfoByid(id);
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:修改一条Connectinfo记录
	 */
	public boolean updateConnectinfoNameOrDescriptionById(Connectinfo connectinfo){
		try {
			return iConnectinfoDao.updateConnectinfoNameOrDescriptionById(connectinfo)>0 ? true:false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:删除一条Connectinfo记录
	 */
	public boolean deleteConnectInfoById(int id){
		try {
			return iConnectinfoDao.deleteConnectInfoById(id)>0 ? true:false;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月14日
	 * 作用:插入一条Connectinfo记录
	 */
	public boolean insertConnectinfo(Connectinfo connectinfo){
		try{
			return iConnectinfoDao.insert(connectinfo)>0 ? true:false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年12月3日
	 * 作用:根据parentid得到一条Connectinfo记录
	 */
	public List<Connectinfo> getConnectinfoListByparentid(int parentid){
		return iConnectinfoDao.getConnectinfoListByparentid(parentid);
	}
	

	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月13日
	 * 作用:查找数据
	 * @throws SQLException 
	 */
//	public List<String> getData(Datasource_connectinfo datasource_connectinfo,String tableName,String fieldName)throws KettleException, SQLException {
//		 Database database = kettleMetadataCollectService.initKettleEnvironment(datasource_connectinfo);
//		 String sql = "select " + fieldName + " from " + tableName;
//		 PreparedStatement ps = database.prepareSQL(sql);
//		 ResultSet rs = ps.executeQuery(sql);
//		 List<String> data = new ArrayList<String>();
//		 while(rs.next()){
//			 data.add(rs.getString(fieldName));
//		 }	
//		
//		return data;
//	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:获取全部记录
	 */
	public List<Connectinfo> getConnectinfoList(){
		return iConnectinfoDao.getConnectinfo();
	}


}
