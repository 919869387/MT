package com.x8.mt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.regexp.recompile;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.x8.mt.common.PageParam;
import com.x8.mt.dao.ISystemuserDao;
import com.x8.mt.entity.PermissionInfo;
import com.x8.mt.entity.SystemUser;

@Service
public class SystemuserService {
	@Autowired
	ISystemuserDao iSystemUserDao;

	//根据用户账号查询用户信息
	public SystemUser findSystemUserByUsername(String username)throws Exception{
		SystemUser systemUser = new SystemUser();
		systemUser.setUsername(username);

		List<SystemUser> list = iSystemUserDao.selectSystemUser(systemUser);

		if(list!=null && list.size()==1){
			return list.get(0);
		}		
		return null;
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月29日
	 * 作用:验证登陆
	 */
	public Boolean login(Subject currentUser,String username,String password){  
		boolean loginResult = false;  
		UsernamePasswordToken token = new UsernamePasswordToken(username,password);  
		token.setRememberMe(true);  
		try {  
			currentUser.login(token);  
			loginResult = true;  
		} catch (UnknownAccountException uae) {  
			loginResult = false;  
		} catch (IncorrectCredentialsException ice) {  
			loginResult = false;  
		} catch (LockedAccountException lae) {  
			loginResult = false;  
		} catch (AuthenticationException ae) {  
			loginResult = false;  
		}  
		return loginResult;  
	}  
	
	/**
	 * 作者：yangyaun
	 * 时间：2018年1月23日
	 * 备注：修改用户密码
	 */
	public int updatePassword(SystemUser systemUser){
		//调用修改密码的方法
		return iSystemUserDao.updatePassword(systemUser);
	}
	
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年3月2日
	 * 备注：查询所有用户信息
	 */
	public PageParam selectAllSystemUsers(PageParam pageParam){
		int currPage = pageParam.getCurrPage();
		int offset = (currPage-1)*pageParam.getPageSize();//计算出偏移量，当前页面记录的其实位置
		int size = pageParam.getPageSize();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("size", size);
		
		List<SystemUser> systemUsers = iSystemUserDao.selectAllSystemUsers(params);
		
		pageParam.setDate(systemUsers);
		return pageParam;
	}
	
	/**
	 * 添加用户信息
	 */
	public int addUser(SystemUser systemUser){
		int result = iSystemUserDao.addUserInfo(systemUser);
		if(result == 0){
			return 0;
		}else {
			return systemUser.getId();
		}
	}
	
	
	
	/**
	 * 查询用户总记录数
	 * 
	 */
	public int rowCount(){
		return iSystemUserDao.countUser();
	}

	
	/**
	 * 修改用户信息
	 */
	public int updateUser(SystemUser systemUser){
		int result = iSystemUserDao.updateUserInfo(systemUser);
		return result;
	}
	
	
	/**
	 * 根据用户名查询用户看用户是否存在
	 */
	public SystemUser selectUser(String username){
		SystemUser systemUser = iSystemUserDao.selectUserByUserName(username);
		return systemUser;
	}
	
	
	//根据用户id删除用户
	public int deleteUserById(int id){
		return iSystemUserDao.deleteUser(id);
	}
	
	
	
	
}
