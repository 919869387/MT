package com.x8.mt.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
/**
 * 
 * 作者:allen
 * 时间:2018年5月9日
 */
@Service
public class ExternalInterfaceService {

	/**
	 * 
	 * 作者:allen
	 * 时间:2018年5月9日
	 * 作用:获取通信报文元数据
	 */
	public JSONArray getCommunicationMessageMetadata() {
		
		JSONObject protocolSegment1 = new JSONObject();
		protocolSegment1.put("startIndex", 0);
		protocolSegment1.put("endIndex", 7);
		protocolSegment1.put("name", "Start_BC");
		protocolSegment1.put("meaning", "发电机备车");
		
		JSONObject protocolSegment2 = new JSONObject();
		protocolSegment2.put("startIndex", 8);
		protocolSegment2.put("endIndex", 15);
		protocolSegment2.put("name", "GeneratorState");
		protocolSegment2.put("meaning", "发电机起停指令");
		
		JSONArray protocolContent1 = new JSONArray();
		protocolContent1.add(protocolSegment1);
		protocolContent1.add(protocolSegment2);
		
		JSONObject protocolSegment3 = new JSONObject();
		protocolSegment3.put("startIndex", 1330);
		protocolSegment3.put("endIndex", 1336);
		protocolSegment3.put("name", "");
		protocolSegment3.put("meaning", "主锅炉");
		
		JSONObject protocolSegment4 = new JSONObject();
		protocolSegment4.put("startIndex", 1337);
		protocolSegment4.put("endIndex", 1349);
		protocolSegment4.put("name", "");
		protocolSegment4.put("meaning", "主锅炉操作器");
		
		JSONArray protocolContent2 = new JSONArray();
		protocolContent2.add(protocolSegment3);
		protocolContent2.add(protocolSegment4);
		
		JSONObject messageMetadata1 = new JSONObject();
		messageMetadata1.put("protocolName", "Modbus数据协议（电力系统区配、专配电压电流）");
		messageMetadata1.put("protocolContent", protocolContent1);
		
		JSONObject messageMetadata2 = new JSONObject();
		messageMetadata2.put("protocolName", "Modbus数据协议（动力系统输入量以及状态反馈）");
		messageMetadata2.put("protocolContent", protocolContent2);
		
		JSONArray communicationMessageMetadatas = new JSONArray();
		communicationMessageMetadatas.add(messageMetadata1);
		communicationMessageMetadatas.add(messageMetadata2);
		
		return communicationMessageMetadatas;
	}

}
