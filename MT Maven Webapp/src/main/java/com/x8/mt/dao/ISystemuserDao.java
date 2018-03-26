package com.x8.mt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.PermissionInfo;
import com.x8.mt.entity.SystemUser;

@Repository
public interface ISystemuserDao {
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月28日
	 * 作用:查询用户
	 */
	List<SystemUser> selectSystemUser(SystemUser systemuser);
	
	/**
	 * 作者：yangyaun
	 * 时间：2018年1月23日
	 * 备注：修改用户密码
	 */
	int updatePassword(SystemUser systemUser);
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年3月2日
	 * 备注：查询所有用户信息
	 */
	List<SystemUser> selectAllSystemUsers(Map<String, Object> params);
	
	/**
	 * 添加用户信息
	 */
	int addUserInfo(SystemUser systemUser);
	
	/**
	 * 查询用户表总记录条数
	 */
	int countUser();
	
	/**
	 * 修改用户信息
	 */
	int updateUserInfo(SystemUser systemUser);
	
	/**
	 * 根据用户名查询用户看用户是否已经存在
	 */
	SystemUser selectUserByUserName(String username);
	
	
	//根据用户id删除用户
	int deleteUser(int id);
	
}
