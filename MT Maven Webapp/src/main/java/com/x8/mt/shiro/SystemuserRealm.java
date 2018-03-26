package com.x8.mt.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.x8.mt.entity.SystemUser;
import com.x8.mt.service.SystemuserService;



/**
 * 
 * 作者:allen
 * 时间:2017年11月28日
 * 作用:自定义realm认证和授权
 */
public class SystemuserRealm extends AuthorizingRealm {
	//admin admin aaa
	@Autowired
	SystemuserService systemuserService;

	// 设置realm的名称
	@Override
	public void setName(String name) {
		super.setName("systemuserRealm");
	}
	
	//realm的认证方法，从数据库查询用户信息
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		//System.out.println("验证是否登陆");
		// token是用户输入的用户名和密码 
		// 第一步从token中取出用户名
		String username = (String) token.getPrincipal();

		// 第二步：根据用户输入的userCode从数据库查询
		SystemUser systemuser = null;
		try {
			systemuser = systemuserService.findSystemUserByUsername(username);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// 如果查询不到返回null
		if(systemuser==null){//
			return null;
		}
		// 从数据库查询到密码
		String password = systemuser.getPassword();
		
		//盐
		String salt = systemuser.getSalt();

		// 如果查询到返回认证信息AuthenticationInfo
		

		//将activeUser设置simpleAuthenticationInfo
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
				username, password,ByteSource.Util.bytes(salt), this.getName());

		return simpleAuthenticationInfo;
	}
	
	// 用于授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}
}
