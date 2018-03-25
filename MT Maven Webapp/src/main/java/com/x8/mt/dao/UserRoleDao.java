package com.x8.mt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.SystemUser;
import com.x8.mt.entity.UserRole;

@Repository
public interface UserRoleDao {
	/**
	 * 添加用户-角色对应关系
	 */
	int addUserRole(UserRole userRole);
	
	//根据用户id，修改用户角色
	int updateUserRolr(UserRole userRole);
}
