package com.x8.mt.controller;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.common.Log;
import com.x8.mt.common.PageParam;
import com.x8.mt.service.SystemLogService;

@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/systemlog")
public class SystemlogController {

	@Resource
	SystemLogService systemlogService;

	/**
	 * 
	 * 作者:itcoder
	 * 时间:2017年11月28日
	 * 作用:获取分页数据(数据按 startdatetime 倒叙排序，新添加的在最前面)
	 * 参数:page(页码)、pageSize(每页多少行)
	 */
	@RequestMapping(value = "/getSystemlogListByPage",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemlog",operationDesc="获取分页systemlog数据")
	public JSONObject getSystemlogListByPage(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

//		if(!GlobalMethodAndParams.checkLogin()){
//			responsejson.put("result", false);
//			responsejson.put("count",0);
//			return responsejson;
//		}
		GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("page")&&
				map.containsKey("pageSize"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
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
		int rowCount = systemlogService.getRowCount();
		//构造分页数据
		PageParam pageParam = new PageParam();
		pageParam.setPageSize(pageSize);
		pageParam.setRowCount(rowCount);
		if(pageParam.getTotalPage()<currPage){
			currPage = pageParam.getTotalPage();
		}
		pageParam.setCurrPage(currPage);
		pageParam = systemlogService.getSystemlogListByPage(pageParam);

		responsejson.put("result", true);
		responsejson.put("data", pageParam);
		responsejson.put("count",pageParam.getDate().size());
		return responsejson;
	}
	
	
	/**
	 * 作者：yangyuan
	 * 时间：2018年3月7日
	 * 备注：查询系统日志信息
	 */
	
	@RequestMapping(value = "/selectAllLog",method=RequestMethod.POST)
	@ResponseBody
	@Log(operationType="systemlog",operationDesc="查询系统日志信息")
	public JSONObject selectAllLog(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		GlobalMethodAndParams.setHttpServletResponse(request, response);

		//检查传参是否正确
		if(!(map.containsKey("page")&&
				map.containsKey("pageSize"))){
			responsejson.put("result", false);
			responsejson.put("count",0);
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
		int rowCount = systemlogService.getRowCount();
		//构造分页数据
		PageParam pageParam = new PageParam();
		pageParam.setPageSize(pageSize);
		pageParam.setRowCount(rowCount);
		if(pageParam.getTotalPage()<currPage){
			currPage = pageParam.getTotalPage();
		}
		pageParam.setCurrPage(currPage);
		pageParam = systemlogService.getSystemlogListByPage(pageParam);
		

		responsejson.put("result", true);
		responsejson.put("page", pageParam);
		responsejson.put("count",pageParam.getDate().size());
		return responsejson;
	}
	
	
}
