package com.x8.mt.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.entity.SystemRole;
import com.x8.mt.entity.SystemUser;
import com.x8.mt.service.SystemroleService;
import com.x8.mt.service.SystemuserService;

/**
 * 
 * @author yangyuan
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping("/systemrolecontroller")
public class SystemroleController {

	@Autowired
	SystemroleService systemroleService;
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年3月2日
	 * 备注：查询所有角色信息
	 */
	@RequestMapping(value = "/selectAllRole",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemrolecontroller",operationDesc="查询所有角色信息")
	public JSONObject selectAllSystemRoles(HttpServletRequest request,HttpServletResponse response){
		//解决跨域问题
		GlobalMethodAndParams.setHttpServletResponse(request, response);
				
		//创建一个JSON对象
		JSONObject responsejson = new JSONObject();
		
		List<SystemRole> systemRoles = systemroleService.selectALLSystemRoles();
		
		if(systemRoles == null){
			responsejson.put("status", false);
			responsejson.put("msg", "没有角色信息");
		}else{
			responsejson.put("systemrole", systemRoles);
		}
		return responsejson;
	}
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年3月8日
	 * 备注：修改角色信息
	 */
	@RequestMapping(value="modifyRoleMessage",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemrolecontroller",operationDesc="修改角色信息")
	public JSONObject updateRoleInfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		//解决跨域问题
		GlobalMethodAndParams.setHttpServletResponse(request, response);
						
		//创建一个JSON对象
		JSONObject responsejson = new JSONObject();
		
		//检查传参是否正确
		if(!(map.containsKey("id")&&map.containsKey("rolename")&&map.containsKey("description"))){
			responsejson.put("status", false);
			responsejson.put("msg","参数错误");
			return responsejson;
		}
		
		String id = map.get("id").toString();
		String rolename = map.get("rolename").toString();
		String description = map.get("description").toString();
		
		SystemRole systemRole = new SystemRole();
		systemRole.setId(Integer.parseInt(id));
		systemRole.setRolename(rolename);
		systemRole.setDescription(description);
		
		int resule = systemroleService.updateRoleById(systemRole);
		
		if(resule == 0){
			responsejson.put("status", false);
			responsejson.put("msg", "修改失败");
		}else {
			responsejson.put("status", true);
			responsejson.put("msg", "修改成功");
		}
		return responsejson;
	}
	
	
	/**
	 * 添加角色信息
	 */
	@RequestMapping(value="addRoleMessage",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemrolecontroller",operationDesc="添加角色信息")
	public JSONObject addRoleInfo(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		//解决跨域问题
		GlobalMethodAndParams.setHttpServletResponse(request, response);
						
		//创建一个JSON对象
		JSONObject responsejson = new JSONObject();
		
		//检查传参是否正确
		if(!(map.containsKey("rolename")&&map.containsKey("description"))){
			responsejson.put("status", false);
			responsejson.put("msg", "参数错误");
			return responsejson;
		}
		
		String rolename = map.get("rolename").toString();
		String description = map.get("description").toString();
		
		SystemRole systemRole = new SystemRole();
		systemRole.setRolename(rolename);
		systemRole.setDescription(description);
		
		//根据用户名查询用户看用户是否已经存在
		SystemRole systemRole2 = systemroleService.selectRole(rolename);
		System.out.println(systemRole2+"****************************************************");
		if(systemRole2 != null){
			//说明用户已被占用或用户已存在
			responsejson.put("status", false);
			responsejson.put("msg", "角色已存在");
		}else{
		
        int result = systemroleService.addRole(systemRole);
        if (result == 0){
        	responsejson.put("status", false);
        	responsejson.put("msg", "添加失败");
			
		}else {
			responsejson.put("status", true);
			responsejson.put("msg", "添加成功");
		}
		}
        return responsejson;
	}
	
	
	/**
	 * 作者：yangyaun
	 * 时间：2018年3月22日
	 * 备注：根据角色id删除角色
	 */
	@RequestMapping(value="deleteRoleMessage",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemrolecontroller",operationDesc="删除角色")
	public JSONObject deleteRole(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){
		//解决跨域问题
		GlobalMethodAndParams.setHttpServletResponse(request, response);
						
		//创建一个JSON对象
		JSONObject responsejson = new JSONObject();
		
		//检查传参是否正确
		if(!(map.containsKey("id"))){
			responsejson.put("status", false);
			responsejson.put("msg", "参数错误");
			return responsejson;
		}
		
		//接收参数
		int id = Integer.parseInt(map.get("id").toString());
		
		int result = systemroleService.deleteRoleById(id);
		
		if(result != 0){
			responsejson.put("status",true);
			responsejson.put("msg", "删除成功");
		}else {
			responsejson.put("status", false);
			responsejson.put("msg", "删除失败");
		}
		
		return responsejson;
	}
}
