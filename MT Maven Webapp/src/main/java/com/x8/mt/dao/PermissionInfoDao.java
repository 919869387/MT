package com.x8.mt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.PermissionInfo;

@Repository
public interface PermissionInfoDao {

	/**
	 * 根据用户id用户的权限菜单列表
	 * @param id
	 * @return
	 */
	List<PermissionInfo> findMenuListByUserid(int id);

	
	/**
	 * 根据用户id查询权限信息
	 * @param userid
	 * @return
	 */
	List<PermissionInfo> findPermissionByUserId(String userid);
}
