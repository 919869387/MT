package com.x8.mt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.x8.mt.dao.PermissionInfoDao;
import com.x8.mt.entity.PermissionInfo;

@Service
public class PermissionInfoService {
	
	@Autowired
	PermissionInfoDao permissionInfoDao;
	
	//根据用户id查询权限菜单
	public List<PermissionInfo> findMenuListByUserId(int id) {
		List<PermissionInfo> permissionInfos = permissionInfoDao.findMenuListByUserid(id);
		return permissionInfos;
	}

	//根据用户id查询权限信息
	public List<PermissionInfo> findPermissionListByUserId(String userid) {

		List<PermissionInfo> permissionInfos = permissionInfoDao.findPermissionByUserId(userid);
		return null;
	}
	
	//查询权限列表
	public List<PermissionInfo> findAllPermission(){
		List<PermissionInfo> permissionList = permissionInfoDao.findPermissionList();
		return permissionList;
	}

	
	//根据权限名称模糊查询权限
	public List<PermissionInfo> findPermissionListByPname(String permissionName){
		List<PermissionInfo> permissionList = permissionInfoDao.findPermissionByPname(permissionName);
		return permissionList;
	}
		
	
	//根据用户名称查询用户具有的权限
	public List<PermissionInfo> permissionByRoleName(String roleName){
		List<PermissionInfo> permissionByRoleName = permissionInfoDao.findPermissionByRoleName(roleName);
		return permissionByRoleName;
	}
}
