package com.x8.mt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.SystemRole;


/**
 * 
 * @author yangyuan
 * 时间：2018年3月2日
 * 
 */

@Repository
public interface ISystemRoleDao {

	/**
	 * 作者：yangyuan
	 * 时间：2018年3月2日
	 * 备注：查询所有角色信息
	 */
	List<SystemRole> selectAllSystemRole();

	List<SystemRole> getRoleById(int id);
	
	/**
	 * 修改角色信息
	 */
	int updateRoleInfoByRoleId(SystemRole systemRole);
	
	/**
	 * 添加角色信息
	 */
	int addRoleInfo(SystemRole systemRole);
	
	/**
	 * 根据角色名称查找角色id
	 */
	int selectIdByRoleName(String roleName);
	
	//根据角色id删除角色
	int deleteRole(int id);

	
	//根据角色名称查询角色是否存在
	SystemRole selectRoleByRoleName(String rolename);
}
