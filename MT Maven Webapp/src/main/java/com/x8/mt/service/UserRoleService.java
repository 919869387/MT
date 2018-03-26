package com.x8.mt.service;

import java.util.List;

//import org.apache.regexp.recompile;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.x8.mt.dao.ISystemuserDao;
import com.x8.mt.dao.UserRoleDao;
import com.x8.mt.entity.SystemUser;
import com.x8.mt.entity.UserRole;

@Service
public class UserRoleService {
	@Autowired
	UserRoleDao userRoleDao;

	/**
	 * 作者：yangyuan
	 * 时间：2018年3月8日
	 * 备注：添加用户-角色关系
	 */
	public int addUserrole(UserRole userRole){
		int result = userRoleDao.addUserRole(userRole);
		return result;
	}
	
	//根据用户id修改角色id
	public int updateRoleByUserId(UserRole userRole){
		return userRoleDao.updateUserRolr(userRole);
	}
}
