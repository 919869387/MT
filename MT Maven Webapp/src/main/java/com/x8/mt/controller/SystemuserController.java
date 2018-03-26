package com.x8.mt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.x8.mt.exception.CustomException;

import com.sun.tools.classfile.Annotation.element_value;
import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.common.PageParam;
import com.x8.mt.entity.SystemRole;
import com.x8.mt.entity.SystemUser;
import com.x8.mt.entity.UserRole;
import com.x8.mt.service.SystemroleService;
import com.x8.mt.service.SystemuserService;
import com.x8.mt.service.UserRoleService;

@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping("/systemusercontroller")
public class SystemuserController {

	@Autowired
	SystemuserService systemuserService;
	
	@Autowired
	SystemroleService systemroleService;
	
	@Autowired
	UserRoleService userRoleService;
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月29日
	 * 作用:用户登录
	 * 参数：username、password
	 */
	@RequestMapping(value = "/login",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemusercontroller",operationDesc="用户登录")
	public JSONObject login(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) {

		GlobalMethodAndParams.setHttpServletResponse(request, response);

		
		
		JSONObject responsejson = new JSONObject();
		String username = map.get("username").toString();
		String password = map.get("password").toString();

		Subject currentUser = SecurityUtils.getSubject();
		boolean result = false;
		if (!currentUser.isAuthenticated()) {//不用重复登陆
			result = systemuserService.login(currentUser,username,password); 
			Session session = currentUser.getSession();
			session.setAttribute("username", username);
		}

		responsejson.put("result", result);
		if(result){
			responsejson.put("count",1);
		}else{
			responsejson.put("count",0);
		}
		return responsejson;
	}
	
	
	
	/**
	 * 作者：yangyaun 
	 * 时间：2018年3月14日
	 * 备注：登录验证
	 *//*
	@RequestMapping(value = "/login",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemusercontroller",operationDesc="用户登录")
	public String login(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map) throws Exception{
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//如果登陆失败从request中获取认证异常信息，shiroLoginFailure就是shiro异常类的全限定名
				String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
				//根据shiro返回的异常类路径判断，抛出指定异常信息
				if(exceptionClassName!=null){
					if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
						//最终会抛给异常处理器
						throw new CustomException("账号不存在");
					} else if (IncorrectCredentialsException.class.getName().equals(
							exceptionClassName)) {
						throw new CustomException("用户名/密码错误");
					} else if("randomCodeError".equals(exceptionClassName)){
						throw new CustomException("验证码错误 ");
					}else {
						throw new Exception();//最终在异常处理器生成未知错误
					}
				}else {
					//此方法不处理登陆成功（认证成功），shiro认证成功会自动跳转到上一个请求路径
					JSONObject responsejson = new JSONObject();
					String username = map.get("username").toString();
					String password = map.get("password").toString();
					
					Subject currentUser = SecurityUtils.getSubject();
					Session session = currentUser.getSession();
					session.setAttribute("username", username);
					session.setAttribute("password", password);
				}
				
				
				//登陆失败还到login页面
				return "/login";
		
	}*/
	
	
	
	
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月29日
	 * 作用:用户登出
	 * 备注：在浏览器没有请求过服务器(没有session)，就直接请求登出方法的时候，
	 * 	该方法返回error路径。前段在这个方法的ajax里的error中需要写逻辑
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)  
	@ResponseBody
	@Log(operationType="systemusercontroller",operationDesc="用户登出")
	public JSONObject logout(HttpServletRequest request,HttpServletResponse response) {		
		JSONObject responsejson = new JSONObject();
		
		if(GlobalMethodAndParams.checkLogin()){//确保登陆，才能登出
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.logout(); 
			responsejson.put("result",true);
			responsejson.put("count",1);
		}else{
			responsejson.put("result",false);
			responsejson.put("count",0);
		}
		return responsejson;  
	}
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年1月23日
	 * 备注：修改用户密码
	 */
	@RequestMapping(value = "/modifyPassword",method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemusercontroller" , operationDesc="修改密码")
	public JSONObject updatePassword(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		//解决跨域问题
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//创建一个JSON对象
		JSONObject responsejson = new JSONObject();
		//获取参数信息
		String username = map.get("name").toString();
		String password = map.get("password").toString();
		String rePassword = map.get("rePassword").toString();
		
	
		try {
			//调用sevice层查询方法
			SystemUser systemUser = systemuserService.findSystemUserByUsername(username);
			System.out.println("用户对象****"+systemUser);
			//判断查询结果并做处理
			if(systemUser != null){
				//获取原始密码密文
				String MDPassword = systemUser.getPassword();
				System.out.println("用户对象****的加密密码"+MDPassword);
				//获取盐
				String salt = systemUser.getSalt();
				//将新密码加密
				Md5Hash MD5RePassword = new Md5Hash(rePassword,salt,1);
				String MD5RePassword2 = MD5RePassword.toString();
			    System.out.println("新密码加密后**"+MD5RePassword2); 
				
				//将用户输入的原始密码加密后与数据库密码做比对
				Md5Hash md5Hash = new Md5Hash(password, salt, 1);
				String md5Password = md5Hash.toString();
				System.out.println("手动加密密码***"+md5Password);
				//判断密码是否正确
				if(md5Password.equals(MDPassword)){
					//密码正确，则修改原始密码
					System.out.println("密码比较正确，，，，");
					SystemUser systemuser2 = new SystemUser();
					systemuser2.setUsername(username);
					systemuser2.setPassword(MD5RePassword2);
					System.out.println(systemuser2);
					int result = systemuserService.updatePassword(systemuser2);
					System.out.println("更新密码状态###"+result);
					if(result > 0){
						responsejson.put("state", true);
						responsejson.put("msg", "修改成功");
					}else{
						responsejson.put("state", false);
						responsejson.put("msg", "修改失败");
					}
					
				}else{
					responsejson.put("state", false);
					responsejson.put("msg", "原始密码输入错误");
			  }
			}else{
				responsejson.put("state", false);
				responsejson.put("msg", "用户不存在");
			}
		} catch (Exception e) {
			responsejson.put("state", false);
			responsejson.put("msg", "修改失败,系统异常");	
		}
		return responsejson;
	}
	
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年3月2日
	 * 备注：分页查询所有用户信息
	 */
	@RequestMapping(value = "/selectAllUser",method = RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemusercontroller" , operationDesc="查询所有用户信息")
	public JSONObject selectAllSystemUsers(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){
		//解决跨域问题
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		//创建一个JSON对象
		JSONObject responsejson = new JSONObject();
		
		//检查传参是否正确
		if(!(map.containsKey("page")&&
				map.containsKey("pageSize"))){
			responsejson.put("status", false);
			responsejson.put("msg","参数错误");
			return responsejson;
		}
		
		String currPageStr = map.get("page").toString();
		String pageSizeStr = map.get("pageSize").toString();
		
		int currPage = 1;
		int pageSize = 1;
		
			try {
				currPage = Integer.parseInt(currPageStr);
				pageSize = Integer.parseInt(pageSizeStr);
			} catch (Exception e) {
			}
			
		//获取总记录数
		int rowCount = systemuserService.rowCount();
		//构造分页数据
		PageParam pageParam = new PageParam();
		pageParam.setPageSize(pageSize);
		pageParam.setRowCount(rowCount);
		if(pageParam.getTotalPage()<currPage){
			currPage = pageParam.getTotalPage();
		}
		pageParam.setCurrPage(currPage);
		
		pageParam = systemuserService.selectAllSystemUsers(pageParam);
		
		if(pageParam.getDate() == null){
			responsejson.put("state", false);
			responsejson.put("msg", "没有用户");
		}else {
			responsejson.put("state", true);
			responsejson.put("msg", "成功");
			
			List<SystemUser> systemUsers = pageParam.getDate();
			
			
			List list = new ArrayList();
			for(SystemUser systemUser : systemUsers){
				JSONObject user = new JSONObject();
				user.put("id", systemUser.getId());
				user.put("username", systemUser.getUsername());
				user.put("password", systemUser.getPassword());
				user.put("nickname", systemUser.getUsercode());
				user.put("status", systemUser.getStatus());
				List<SystemRole> systemRoles = systemroleService.getRoleByUserId(systemUser.getId());
				if(systemRoles == null){
					user.put("userRole", "用户没有角色");
				}else{
					StringBuilder sb = new StringBuilder();
					for(SystemRole systemRole : systemRoles){
						if(sb.length() > 0){
							//该步即不会第一位有逗号，也防止最后一位拼接逗号
							sb.append(",");
						}
						sb.append(systemRole.getRolename());
					}
					user.put("userRole", sb.toString());
				}
				
				list.add(user);
			
			}
			pageParam.setDate(list);
			responsejson.put("page", pageParam);
			responsejson.put("count", pageParam.getDate().size());
		}
		return responsejson;
	}
	
	
	/**
	 * 添加用户信息
	 */
	@RequestMapping(value="/addUserMessage",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemusercontroller" , operationDesc="添加用户信息")
	public JSONObject addUserInfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){
		//解决跨域问题
		GlobalMethodAndParams.setHttpServletResponse(request, response);
						
		//创建一个JSON对象
		JSONObject responsejson = new JSONObject();
		
//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		//检查传参是否正确
		if(!(map.containsKey("userName")&&map.containsKey("password")&&map.containsKey("userCode")&&map.containsKey("userRole")&&map.containsKey("state"))){
			responsejson.put("status", false);
			responsejson.put("msg", "参数错误");
			return responsejson;
		}
		
		//接收参数
		String username = map.get("userName").toString();
		String password = map.get("password").toString();
		String usercode = map.get("userCode").toString();
		String userrole = map.get("userRole").toString();
		String status = map.get("state").toString();
		
		//根据用户名查询用户看用户是否已经存在
		SystemUser systemUser2 = systemuserService.selectUser(username);
		if(systemUser2 != null){
			//说明用户已被占用或用户已存在
			responsejson.put("status", false);
			responsejson.put("msg", "用户名被占用或用户已存在");
		}else{
		
		//密码加密：随机生成字符串当盐
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i<3; i++){
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}
		//盐
		String salt = sb.toString();
		//加密
		Md5Hash md5Password = new Md5Hash(password, salt, 1);
		String mPassword = md5Password.toString();
		
		//将新用户信息封装在sysuser对象中
		SystemUser systemUser = new SystemUser();
		systemUser.setUsername(username);
		systemUser.setUsercode(usercode);
		systemUser.setPassword(mPassword);
		systemUser.setSalt(salt);
		systemUser.setStatus(status);
		
		//调用service方法添加用户信息,返回自增id
		int result = systemuserService.addUser(systemUser);
		System.out.println("*************"+result);
		if(result == 0){
			responsejson.put("status", false);
			responsejson.put("msg", "添加失败");
		}else {
			//将新添加的用户与角色的关系添加到关系表中
			//根据角色名称，查询对应的角色id
			String id = String.valueOf(systemroleService.selectId(userrole));
			//将用户与角色的对应关系存入用户角色关系表中
			UserRole userRole2 = new UserRole();
			userRole2.setRoleId(id);
			userRole2.setUserId(String.valueOf(result));
			
			int result2 = userRoleService.addUserrole(userRole2);
			if(result2 == 0){
				responsejson.put("status", false);
				responsejson.put("msg", "添加失败");
			}else {
				responsejson.put("status", true);
				responsejson.put("msg", "添加成功");
			}
		}
		}
		return responsejson;
		
	}
	
	
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年3月17日
	 * 备注：修改用户信息
	 */
	@RequestMapping(value="/modifyUserMessage", method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemusercontroller" , operationDesc="修改用户信息")
	public JSONObject updateUserInfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object>map){
		//解决跨域问题
		GlobalMethodAndParams.setHttpServletResponse(request, response);
								
		//创建一个JSON对象
		JSONObject responsejson = new JSONObject();
		
		//检查传参是否正确
		if(!(map.containsKey("id")&&map.containsKey("userName")&&map.containsKey("password")
				&&map.containsKey("userCode")&&map.containsKey("userRole")&&map.containsKey("state"))){
			responsejson.put("status", false);
			responsejson.put("msg", "参数错误");
		}
		
		//接收参数
		int id = Integer.parseInt(map.get("id").toString());
		String username = map.get("userName").toString();
		String password = map.get("password").toString();
		String usercode = map.get("userCode").toString();
		String userRole = map.get("userRole").toString();
		String state = map.get("state").toString();
		
		//密码加密：随机生成字符串当盐
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i<3; i++){
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}
		//盐
		String salt = sb.toString();
		
		//密码加密
		Md5Hash md5Password = new Md5Hash(password, salt, 1);
		String passWord = md5Password.toString();
		
		//将要修改的信息封装在systemuser对象中
		SystemUser systemUser = new SystemUser();
		systemUser.setId(id);
		systemUser.setPassword(passWord);
		systemUser.setSalt(salt);
		systemUser.setStatus(state);
		systemUser.setUsercode(usercode);
		
		//调用修改用户基本信息的方法
		int result1 = systemuserService.updateUser(systemUser);
		
		//修改用户角色
		//首先根据角色名称查询到角色id，再在用户和角色表中做修改
		UserRole userRole2 = new UserRole();
		userRole2.setRoleId(Integer.toString(systemroleService.selectId(userRole)));
		userRole2.setUserId(Integer.toString(id));
		int result2 = userRoleService.updateRoleByUserId(userRole2);
		
		if(result1 != 0 && result2 != 0){
			responsejson.put("status", true);
			responsejson.put("msg", "修改成功");
		}else{
			responsejson.put("status", false);
			responsejson.put("msg", "修改失败");
		}
		
		return responsejson;
	}
	
	
	
	

	/**
	 * 作者：yangyuan
	 * 时间：2018年3月22日
	 * 备注：删除用户
	 */
	@RequestMapping(value="/deleteUserMessage", method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemusercontroller" , operationDesc="删除用户")
	public JSONObject deleteUser(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){
		//解决跨域问题
		GlobalMethodAndParams.setHttpServletResponse(request, response);
										
		//创建一个JSON对象
		JSONObject responsejson = new JSONObject();
		
		//检查传参是否正确
		if(!(map.containsKey("id"))){
			responsejson.put("status", false);
			responsejson.put("msg", "参数错误");
		}
		
		//接收参数
		int id = Integer.parseInt(map.get("id").toString());
		
		//根据用户id删除该用户
		int resule = systemuserService.deleteUserById(id);
		
		if (resule != 0) {
			responsejson.put("status", true);
			responsejson.put("msg", "删除成功");
		}else{
			responsejson.put("status", false);
			responsejson.put("msg", "修改失败");
		}
		return responsejson;
	}
	
	
	
	
	
}
