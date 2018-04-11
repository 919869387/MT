package com.x8.mt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.x8.mt.dao.ISystemRoleDao;
import com.x8.mt.entity.SystemRole;

/**
 * 
 * @author yangyuan
 *
 */
@Service
public class SystemroleService {
	
	@Autowired
	ISystemRoleDao iSystemRoleDao;
	
	public List<SystemRole> selectALLSystemRoles(){
		List<SystemRole> systemRoles = iSystemRoleDao.selectAllSystemRole();
		if(systemRoles == null){
			return null;
		}else{
			return systemRoles;
		}
	}

	public List<SystemRole> getRoleByUserId(int id) {
		List<SystemRole> systemRoles = iSystemRoleDao.getRoleById(id);
		if(systemRoles == null || systemRoles.size() == 0){
			return null;
		}else{
			return systemRoles;
		}
	}
	
	
	/**
	 * 修改角色信息
	 */
	public int updateRoleById(SystemRole systemRole){
		int result = iSystemRoleDao.updateRoleInfoByRoleId(systemRole);
		if(result == 0){
			return 0;
		}else{
			return 1;
		}
	}
	
	/**
	 * 添加角色信息
	 */
	public int addRole(SystemRole systemRole){
		int resule = iSystemRoleDao.addRoleInfo(systemRole);
		if(resule == 0){
			return 0;
		}else {
			return 1;
		}
		
	}
	
	/**
	 * 根据角色名称查询角色id
	 */
	public int selectId(String roleName){
		return iSystemRoleDao.selectIdByRoleName(roleName);
	}
	
	
	//根据角色id删除角色
	public int deleteRoleById(int id){
		return iSystemRoleDao.deleteRole(id);
	}

	
	//根据角色名称查询角色是否存在
	public SystemRole selectRole(String rolename) {
		SystemRole systemRole = iSystemRoleDao.selectRoleByRoleName(rolename);
		return systemRole;
	}
	
	//根据权限id查询具有该权限的角色名称
	public List<SystemRole> RolesByPerId(int permissionId){
		List<SystemRole> rolesByPerId = iSystemRoleDao.findRolesByPerId(permissionId);
		return rolesByPerId;
	}
}
