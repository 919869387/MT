package com.x8.mt.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.StringUtils;

@Service
public class SystemInfoService {
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月3日
	 * 作用:获取系统信息
	 * @throws IOException 
	 */
	public JSONArray getSystemInfo() throws IOException{
		InputStream in = SystemInfoService.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties p = new Properties();
		p.load(in);
		
		JSONArray systemInfo = new JSONArray();
		
		String url = p.getProperty("jdbc.url");
		String[] splitFirst = url.split("\\?");
		String[] splitSecond = splitFirst[0].split("//");
		String[] temp = splitSecond[0].split(":");
		JSONObject node1 = new JSONObject();
		node1.put("key", "数据库类型");
		node1.put("value", temp[1]);
		systemInfo.add(node1);
		
		String[] splitThird = splitSecond[1].split(":");
		JSONObject node2 = new JSONObject();
		node2.put("key", "地址");
		node2.put("value", splitThird[0]);
		systemInfo.add(node2);
		
		String[] temp1 = splitThird[1].split("/");	
		JSONObject node3 = new JSONObject();
		node3.put("key", "端口号");
		node3.put("value", temp1[0]);
		systemInfo.add(node3);
		JSONObject node4 = new JSONObject();
		node4.put("key", "数据库名");
		node4.put("value", temp1[1]);
		systemInfo.add(node4);	
		
		return systemInfo;		
	}
}
