package com.x8.mt.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;

import net.sf.cglib.beans.BeanMap;
import net.sf.json.JSONObject;

import com.x8.mt.entity.Metadata;

/**
 * 作者：allen
 * 时间：2018年3月22日
 * 作用：Metadata与map相互转换
 */
public class TransformMetadata {
	
	/**
	 * 作者:allen
	 * 时间:2018年3月26日
	 * 作用:生产Attributes
	 */
	public static JSONObject createAttributes(Map<String,Object> map , List<String> attributesField){
		JSONObject attributes = new JSONObject();
		Iterator it = map.entrySet().iterator();  
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();  
			String key = (String) entry.getKey();
			Object value = entry.getValue();
			if(attributesField.contains(key)){
				attributes.put(key, value);
			}
		}
		return attributes;
	}
	
	/**
	 * 作者:allen
	 * 时间:2018年3月22日
	 * 作用:Metadata转化为map
	 */
	public static Map<String, Object> transformMetadataToMap(Metadata metadata) {
		Map<String, Object> metadataMap = new HashMap<String, Object>();  
		if (metadata != null) {
			BeanMap beanMap = BeanMap.create(metadata);  
			for (Object key : beanMap.keySet()) {  
				if(key.toString().equals("ATTRIBUTES")){
					JSONObject attributes = JSONObject.fromObject(beanMap.get(key));
					Iterator iterator = attributes.keys();
					//attributes是json类型，将他们展开
					while(iterator.hasNext()){
						String attributes_key = (String) iterator.next();
						Object attributes_value = attributes.get(attributes_key);
						metadataMap.put(attributes_key+"", attributes_value.toString());  
					}
				}else{
					Object value = beanMap.get(key);
					if(value==null){
						value="";
					}
					metadataMap.put(key+"", value);  
				}
			}             
		}  
		return metadataMap;
	}


	/**
	 * 作者:allen
	 * 时间:2018年3月22日
	 * 作用:map转化为Metadata
	 */
	public static Metadata transformMapToMetadata(Map<String,Object> map , List<String> attributesField){
		Map<Object,Object> metadataMap = new HashMap<Object, Object>();
		JSONObject attributes = new JSONObject();
		Iterator it = map.entrySet().iterator();  
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();  
			String key = (String) entry.getKey();
			Object value = entry.getValue();
			if(attributesField.contains(key)){
				attributes.put(key, value);
			}else{
				metadataMap.put(key, value);
			}
		}
		if(attributes.size()>0){
			metadataMap.put(GlobalMethodAndParams.Metadata_Attributes, attributes.toString());
		}
		Metadata metadata = new Metadata();
		transMapToBean(metadataMap,metadata);
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(metadataMap.containsKey("CREATETIME")){
				Date CREATETIME = sdf.parse(metadataMap.get("CREATETIME").toString());
				metadata.setCREATETIME(CREATETIME);
			}
		} catch (ParseException e) {
			throw new RuntimeException("DATE Transform Error");
		}
		
		return metadata;
	}

	/**
	 * 作者:allen
	 * 时间:2018年3月22日
	 * 作用:map转化为Obj
	 */
	public static void transMapToBean(Map<Object, Object> map, Object obj) {  
		if (map == null || obj == null) {  
			return;  
		}  
		try {  
			BeanUtils.populate(obj, map);  
		} catch (Exception e) {  
			System.out.println("transMapToBean Error" + e);  
		}  
	} 

}
