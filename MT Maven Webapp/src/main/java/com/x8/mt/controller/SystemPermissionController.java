package com.x8.mt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.dao.PermissionInfoDao;
import com.x8.mt.entity.PermissionInfo;
import com.x8.mt.entity.SystemRole;
import com.x8.mt.entity.SystemUser;
import com.x8.mt.service.PermissionInfoService;
import com.x8.mt.service.SystemroleService;
import com.x8.mt.service.SystemuserService;


@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping("/systemPermissionController")
public class SystemPermissionController {
	
	@Autowired
	PermissionInfoService permissionInfoService;
	
	@Autowired
	SystemroleService systemroleService;
	
	@Autowired
	SystemuserService systemuserService;
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年4月10日
	 * 备注：查询权限列表
	 * 
	 */
	@RequestMapping(value = "/permissionList",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemPermissionController",operationDesc="查询权限列表")
	public JSONObject findPermissionList(HttpServletRequest request,HttpServletResponse response){
		
		GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		JSONObject responsejson = new JSONObject();
		
		List<PermissionInfo> list = permissionInfoService.findAllPermission();
		
		if (list != null && list.size() > 0) {
			responsejson.put("status", true);
			responsejson.put("msg", "查询成功");
			responsejson.put("data", list);
			
		}else{
			responsejson.put("status", false);
			responsejson.put("msg", "查询失败");
			
		}
		
		return responsejson;
	}
	
	
	
	
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年4月10日
	 * 备注：按权限名称查询具有该权限的所有用户
	 */
	
	@RequestMapping(value = "/permissionByPname",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemPermissionController",operationDesc="按权限名称查询具有该权限的所有用户")
	public JSONObject findpermissionByPname(HttpServletRequest request,HttpServletResponse response,Map<String,Object> map){
		
        GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		JSONObject responsejson = new JSONObject();
		
		//接收参数信息
		String permissionName = map.get("permissionName").toString();
		
        List<PermissionInfo> list = permissionInfoService.findPermissionListByPname(permissionName);
        
        List<Map<String, Object>> permissionList = new ArrayList<>();
        if (list != null && list.size() > 0) {
        	for(PermissionInfo permissionInfo : list){
        		Map<String, Object> permissionMap = new HashMap<>();
        		permissionMap.put("permissionId", permissionInfo.getId());
        		permissionMap.put("permissionName", permissionInfo.getPermissionName());
        		
        		//根据权限id查询具有该权限的角色名称
        		List<SystemRole> rolesByPerId = systemroleService.RolesByPerId(permissionInfo.getId());
        		List<String> rolesNameList = new ArrayList<>();
        		if(rolesByPerId != null && rolesByPerId.size() > 0){
        			for(SystemRole roles : rolesByPerId){
            			rolesNameList.add(roles.getRolename());
        			}
        			responsejson.put("msg", "查询成功");
        			permissionMap.put("roleName", rolesNameList);
        		}else {
        			responsejson.put("msg", "该权限还没有被分配");
				}

        		permissionList.add(permissionMap);
        		
        	}
        	
        	responsejson.put("status", true);
        	responsejson.put("data", permissionList);
        	
		}else {
			responsejson.put("status", false);
			responsejson.put("msg", "查询失败,没有该权限项");
			responsejson.put("data", permissionList);
		}
        
        return responsejson;
	}
	
	
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年4月11日
	 * 备注：根据角色名称查询角色具有的权限
	 */
	@RequestMapping(value="/rolePermissions", method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemPermissionController",operationDesc="根据角色名称查询角色具有的权限")
	public JSONObject findPermissionByRoleName(HttpServletRequest request, HttpServletResponse response, Map<String,Object> map){
		
        GlobalMethodAndParams.setHttpServletResponse(request, response);
		
		JSONObject responsejson = new JSONObject();
		
		//接收参数
		String roleName = map.get("roleName").toString();
		
		//根据角色名称查询角色具有的权限
		List<PermissionInfo> permissionByRoleName = permissionInfoService.permissionByRoleName(roleName);
		if (permissionByRoleName != null && permissionByRoleName.size() > 0){
			responsejson.put("status", true);
			responsejson.put("msg", "查询成功");
			responsejson.put("data", permissionByRoleName);
		}else {
			responsejson.put("status", false);
			responsejson.put("msg", "该角色没有被分配权限");
			responsejson.put("data", permissionByRoleName);
		}
		return responsejson;
	}
	
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年4月11日
	 * 备注：添加权限
	 */
	
	
	
	
}
