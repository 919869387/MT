package com.x8.mt.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
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
	
	/**
	 * 查询权限列表
	 */
	List<PermissionInfo> findPermissionList();
	
	/**
	 * 根据权限名称模糊查询权限项
	 */
	List<PermissionInfo> findPermissionByPname(String permissionName);
	
	
	/**
	 * 根据角色名称查询该角色具有的权限
	 */
	List<PermissionInfo> findPermissionByRoleName(String roleName);
	
	/**
	 * 根据角色id查询用户没有的权限项
	 */
	List<PermissionInfo> roleNoPermission(String roleName);
	
	/**
	 * 给角色添加权限
	 */
	int addPermission(List<Map<String, Object>> permissionList);
	
	/**
	 * 删除指定的角色权限项
	 */
	int deletePermission(Map<String, Object> perMap);
}
