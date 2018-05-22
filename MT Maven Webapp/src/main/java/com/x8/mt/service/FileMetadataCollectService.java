package com.x8.mt.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import com.x8.mt.common.CalDataSize;
import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.dao.ICollectJobDao;
import com.x8.mt.dao.IMetaDataDao;
import com.x8.mt.dao.IMetadataTankDao;
import com.x8.mt.dao.IMetamodel_datatypeDao;
import com.x8.mt.entity.MetaDataRelation;
import com.x8.mt.entity.Metadata;
import com.x8.mt.entity.MetadataTank;

@Service
public class FileMetadataCollectService {

	@Resource
	ICollectJobDao iCollectJobDao;	
	@Resource
	IMetaDataDao iMetaDataDao;
	@Resource
	MetaDataRelationService metaDataRelationService;
	@Resource
	IMetamodel_datatypeDao iMetamodel_datatypeDao;
	@Resource
	IMetadataTankDao iMetadataTankDao;

	/**
	 * 作者:GodDispose
	 * 时间:2018年5月7日
	 * 作用:根据file_connectinfo信息，自动采集Excel文件元数据
	 */
	public void collectExcelMetaData(String filename,int collectjobid ,Date createDate){
		try{
			File file = new File(GlobalMethodAndParams.PATH_NAME + filename); 			
			HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream(file));

			//存储表格列名，name,type,default,isNull,description
			Map<String,Integer> map = new HashMap<String, Integer>();

			MetaDataRelation metadataRelation = new MetaDataRelation();

			//1.将文件元数据存入元数据表	
			Metadata metadataFile = new Metadata();
			metadataFile.setNAME(filename);
			metadataFile.setCOLLECTJOBID(collectjobid);
			metadataFile.setMETAMODELID(110);
			metadataFile.setCREATETIME(createDate);
			metadataFile.setUPDATETIME(createDate);
			metadataFile.setCHECKSTATUS("1");
			metadataFile.setVERSION(1);
			String fileAttributes = "{\"filename\":\""+ filename
					+"\",\"filetype\":\"" + "1"
					+"\",\"filelength\":\"" + CalDataSize.getPrintSize(file.length())
					+"\",\"filepath\":\"" + GlobalMethodAndParams.PATH_NAME
					+"\"}";	

			metadataFile.setATTRIBUTES(fileAttributes);

			if(!(iMetaDataDao.insertMetadata(metadataFile)>0 ? true:false)){//插入不成功
				throw new RuntimeException("文件元数据插入失败");
			}

			//2.将文件元数据加入metadata_tank表
			MetadataTank metadataTank = new MetadataTank();
			metadataTank.setCHECKSTATUS(metadataFile.getCHECKSTATUS());
			metadataTank.setATTRIBUTES(metadataFile.getATTRIBUTES());
			metadataTank.setCREATETIME(new Date());
			metadataTank.setDESCRIPTION(metadataFile.getDESCRIPTION());
			metadataTank.setKeyid(metadataFile.getID());
			metadataTank.setMETAMODELID(metadataFile.getMETAMODELID());
			metadataTank.setNAME(metadataFile.getNAME());
			metadataTank.setUPDATETIME(new Date());
			metadataTank.setVERSION(metadataFile.getVERSION());
			metadataTank.setCOLLECTJOBID(collectjobid);

			if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
				throw new RuntimeException("insertMetaDataTank Error");
			}

			HSSFSheet sheet=null;

			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
				sheet=workbook.getSheetAt(i);
				if(sheet.getPhysicalNumberOfRows() == 0){	//只存在0行
					System.out.println("此文件不存在元数据！");
				}else if(sheet.getPhysicalNumberOfRows() > 2){  //只存在2行
					String database = sheet.getRow(0).getCell(0).toString();
					//	          	   String databaseName = database.split("(")[0];
					//	          	   String databaseDescription = database.substring(database.indexOf("(")+1,database.indexOf(")"));
					String tableName = database;
					String tableDescription = null;
					if(database.contains("（")){
						tableName = database.split("（")[0];
						tableDescription = database.substring(database.indexOf("（")+1,database.indexOf("）"));
					}

					//3.将表数据存入元数据表	
					Metadata metadataTable = new Metadata();
					metadataTable.setNAME(tableName);
					metadataTable.setCOLLECTJOBID(collectjobid);
					metadataTable.setMETAMODELID(110);
					metadataTable.setCREATETIME(createDate);
					metadataTable.setUPDATETIME(createDate);
					metadataTable.setCHECKSTATUS("1");
					metadataTable.setVERSION(1);
					metadataTable.setDESCRIPTION(tableDescription);
					String tableAttributes = "{\"tablename\":\""+ tableName +"\"}";

					metadataTable.setATTRIBUTES(tableAttributes);

					if(!(iMetaDataDao.insertMetadata(metadataTable)>0 ? true:false)){//插入不成功
						throw new RuntimeException("表元数据插入失败");
					}

					//4.将表元数据加入metadata_tank表
					metadataTank.setCHECKSTATUS(metadataTable.getCHECKSTATUS());
					metadataTank.setATTRIBUTES(metadataTable.getATTRIBUTES());
					metadataTank.setCREATETIME(new Date());
					metadataTank.setDESCRIPTION(metadataTable.getDESCRIPTION());
					metadataTank.setKeyid(metadataTable.getID());
					metadataTank.setMETAMODELID(metadataTable.getMETAMODELID());
					metadataTank.setNAME(metadataTable.getNAME());
					metadataTank.setUPDATETIME(new Date());
					metadataTank.setVERSION(metadataTable.getVERSION());
					metadataTank.setCOLLECTJOBID(collectjobid);

					if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
						throw new RuntimeException("insertMetaDataTank Error");
					}

					metadataRelation.setMetaDataId(metadataFile.getID());
					metadataRelation.setRelateMetaDataId(metadataTable.getID());
					metadataRelation.setType("DEPENDENCY");

					if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
						throw new RuntimeException("元数据关系插入失败");
					} 

					HSSFRow tempRow = sheet.getRow(1);
					for(int j = 0 ; j < tempRow.getPhysicalNumberOfCells(); j++){
						map.put(tempRow.getCell(i).toString(), j);
					}

					for(int k = 2 ; k < sheet.getPhysicalNumberOfRows(); k++){
						HSSFRow row = sheet.getRow(k);
						if(row == null ||row.getCell(0) == null || row.getRowNum() == 0 ){
							break;
						}
						String fieldTypeAndLength = row.getCell(map.get("type")).toString();
						String fieldType = null;
						String fieldLength = null;
						if(fieldTypeAndLength.contains("（")){
							fieldType = fieldTypeAndLength.split("（")[0];
							fieldLength = fieldTypeAndLength.substring(fieldTypeAndLength.indexOf("（")+1,fieldTypeAndLength.indexOf("）"));
						}

						boolean allownull = true;
						if(row.getCell(map.get("isNull")).toString().equals("N")){
							allownull = false;
						}

						//5.添加字段元数据 
						Metadata metadataField = new Metadata();
						metadataField.setNAME(row.getCell(map.get("name")).toString());
						metadataField.setCOLLECTJOBID(collectjobid);
						metadataField.setMETAMODELID(32);
						metadataField.setCREATETIME(createDate);
						metadataField.setUPDATETIME(createDate);
						metadataField.setCHECKSTATUS("1");
						metadataField.setVERSION(1);
						metadataField.setDESCRIPTION(map.get("description").toString());
						String fieldAttributes = "{\"fieldname\":\""+ row.getCell(map.get("name")).toString()
								+ "\",\"fieldtype\":\"" + fieldType
								+"\",\"length\":\"" + fieldLength
								+"\",\"isprimarykey\":\"" + false
								+"\",\"isforeignkey\":\"" + false
								+"\",\"allownull\":\"" + allownull
								+"\",\"defaultvalue\":\"" + row.getCell(map.get("default")).toString()
								+"\"}";	

						metadataField.setATTRIBUTES(fieldAttributes);
						if(!(iMetaDataDao.insertMetadata(metadataField)>0 ? true:false)){//插入不成功
							throw new RuntimeException("字段元数据插入失败");
						}

						metadataRelation.setMetaDataId(metadataTable.getID());
						metadataRelation.setRelateMetaDataId(metadataField.getID());
						metadataRelation.setType("COMPOSITION");

						//6.添加字段元数据 到MetadataTank表
						metadataTank.setCHECKSTATUS(metadataField.getCHECKSTATUS());
						metadataTank.setATTRIBUTES(metadataField.getATTRIBUTES());
						metadataTank.setCREATETIME(new Date());
						metadataTank.setDESCRIPTION(metadataField.getDESCRIPTION());
						metadataTank.setKeyid(metadataField.getID());
						metadataTank.setMETAMODELID(metadataField.getMETAMODELID());
						metadataTank.setNAME(metadataField.getNAME());
						metadataTank.setUPDATETIME(new Date());
						metadataTank.setVERSION(metadataField.getVERSION());
						metadataTank.setCOLLECTJOBID(collectjobid);

						if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
							throw new RuntimeException("insertMetaDataTank Error");
						}

						if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
							throw new RuntimeException("元数据关系插入失败");
						} 
					}
				}
			}
			workbook.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 作者:GodDispose
	 * 时间:2018年5月7日
	 * 作用:根据file_connectinfo信息，自动采集XML文件元数据
	 */
	public  void collectXmlMetadata(String filename,int collectjobid ,Date createDate){
		try {
			File file = new File(GlobalMethodAndParams.PATH_NAME + filename);


			MetaDataRelation metadataRelation = new MetaDataRelation();

			//1.将文件元数据存入元数据表	
			Metadata metadataFile = new Metadata();
			metadataFile.setNAME(filename);
			metadataFile.setCOLLECTJOBID(collectjobid);
			metadataFile.setMETAMODELID(110);
			metadataFile.setCREATETIME(createDate);
			metadataFile.setUPDATETIME(createDate);
			metadataFile.setCHECKSTATUS("1");
			metadataFile.setVERSION(1);
			String databaseAttributes = "{\"filename\":\""+ filename
					+"\",\"filetype\":\"" + "1"
					+"\",\"filelength\":\"" + CalDataSize.getPrintSize(file.length())
					+"\",\"filepath\":\"" + GlobalMethodAndParams.PATH_NAME
					+"\"}";	

			metadataFile.setATTRIBUTES(databaseAttributes);

			if(!(iMetaDataDao.insertMetadata(metadataFile)>0 ? true:false)){//插入不成功
				throw new RuntimeException("数据库元数据插入失败");
			}

			//2.将文件元数据加入metadata_tank表
			MetadataTank metadataTank = new MetadataTank();
			metadataTank.setCHECKSTATUS(metadataFile.getCHECKSTATUS());
			metadataTank.setATTRIBUTES(metadataFile.getATTRIBUTES());
			metadataTank.setCREATETIME(new Date());
			metadataTank.setDESCRIPTION(metadataFile.getDESCRIPTION());
			metadataTank.setKeyid(metadataFile.getID());
			metadataTank.setMETAMODELID(metadataFile.getMETAMODELID());
			metadataTank.setNAME(metadataFile.getNAME());
			metadataTank.setUPDATETIME(new Date());
			metadataTank.setVERSION(metadataFile.getVERSION());
			metadataTank.setCOLLECTJOBID(collectjobid);

			if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
				throw new RuntimeException("insertMetaDataTank Error");
			}

			SAXReader reader=new SAXReader();
			//读取xml文件到Document中
			Document doc=reader.read(file);
			//获取xml文件的根节点
			Element rootElement=doc.getRootElement();
			//定义一个Element用于遍历

			//存储表格列名，name,type,default,isNull
			Map<String,String> map = new HashMap<String, String>();

			List<Element> tables = rootElement.elements();      

			for(Element table : tables){
				//3.将表数据存入元数据表	
				Metadata metadataTable = new Metadata();
				metadataTable.setNAME(table.attributeValue("name"));
				metadataTable.setCOLLECTJOBID(collectjobid);
				metadataTable.setMETAMODELID(110);
				metadataTable.setCREATETIME(createDate);
				metadataTable.setUPDATETIME(createDate);
				metadataTable.setCHECKSTATUS("1");
				metadataTable.setVERSION(1);
				metadataTable.setDESCRIPTION(table.attributeValue("description"));
				String tableAttributes = "{\"tablename\":\""+ table.attributeValue("name") +"\"}";

				metadataTable.setATTRIBUTES(tableAttributes);

				if(!(iMetaDataDao.insertMetadata(metadataTable)>0 ? true:false)){//插入不成功
					throw new RuntimeException("表元数据插入失败");
				}

				//4.将表元数据加入metadata_tank表
				metadataTank.setCHECKSTATUS(metadataTable.getCHECKSTATUS());
				metadataTank.setATTRIBUTES(metadataTable.getATTRIBUTES());
				metadataTank.setCREATETIME(new Date());
				metadataTank.setDESCRIPTION(metadataTable.getDESCRIPTION());
				metadataTank.setKeyid(metadataTable.getID());
				metadataTank.setMETAMODELID(metadataTable.getMETAMODELID());
				metadataTank.setNAME(metadataTable.getNAME());
				metadataTank.setUPDATETIME(new Date());
				metadataTank.setVERSION(metadataTable.getVERSION());
				metadataTank.setCOLLECTJOBID(collectjobid);

				if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
					throw new RuntimeException("insertMetaDataTank Error");
				}

				metadataRelation.setMetaDataId(metadataFile.getID());
				metadataRelation.setRelateMetaDataId(metadataTable.getID());
				metadataRelation.setType("DEPENDENCY");

				if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
					throw new RuntimeException("元数据关系插入失败");
				} 

				List<Element> fields = table.elements();
				for(Element field:fields){
					List<Element> attributes = field.elements();
					for(Element attribute : attributes){
						map.put(attribute.getName(), attribute.getText());
					}

					String fieldTypeAndLength = map.get("type").toString();
					String fieldType = fieldTypeAndLength;
					String fieldLength = null;
					if(fieldTypeAndLength.contains("（")){
						fieldType = fieldTypeAndLength.split("（")[0];
						fieldLength = fieldTypeAndLength.substring(fieldTypeAndLength.indexOf("（")+1,fieldTypeAndLength.indexOf("）"));
					}  
					//添加字段元数据 
					Metadata metadataField = new Metadata();
					metadataField.setNAME(map.get("name").toString());
					metadataField.setCOLLECTJOBID(collectjobid);
					metadataField.setMETAMODELID(32);
					metadataField.setCREATETIME(createDate);
					metadataField.setUPDATETIME(createDate);
					metadataField.setCHECKSTATUS("1");
					metadataField.setVERSION(1);
					metadataField.setDESCRIPTION(map.get("description").toString());
					String fieldAttributes = "{\"fieldname\":\""+ map.get("name").toString()
							+ "\",\"fieldtype\":\"" + fieldType
							+"\",\"length\":\"" + fieldLength
							+"\",\"isprimarykey\":\"" + false
							+"\",\"isforeignkey\":\"" + false
							+"\",\"allownull\":\"" + map.get("isNull").toString()
							+"\",\"defaultvalue\":\"" + map.get("default").toString()
							+"\"}";	

					metadataField.setATTRIBUTES(fieldAttributes);
					if(!(iMetaDataDao.insertMetadata(metadataField)>0 ? true:false)){//插入不成功
						throw new RuntimeException("字段元数据插入失败");
					}

					metadataRelation.setMetaDataId(metadataFile.getID());
					metadataRelation.setRelateMetaDataId(metadataField.getID());
					metadataRelation.setType("COMPOSITION");

					metadataTank.setCHECKSTATUS(metadataField.getCHECKSTATUS());
					metadataTank.setATTRIBUTES(metadataField.getATTRIBUTES());
					metadataTank.setCREATETIME(new Date());
					metadataTank.setDESCRIPTION(metadataField.getDESCRIPTION());
					metadataTank.setKeyid(metadataField.getID());
					metadataTank.setMETAMODELID(metadataField.getMETAMODELID());
					metadataTank.setNAME(metadataField.getNAME());
					metadataTank.setUPDATETIME(new Date());
					metadataTank.setVERSION(metadataField.getVERSION());
					metadataTank.setCOLLECTJOBID(collectjobid);

					if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
						throw new RuntimeException("insertMetaDataTank Error");
					}

					if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
						throw new RuntimeException("元数据关系插入失败");
					} 
				}
			}
			doc.clearContent();
			reader.resetHandlers();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 作者:GodDispose
	 * 时间:2018年5月7日
	 * 作用:根据file_connectinfo信息，自动采集JSON文件元数据
	 * @throws Exception 
	 */
	public  void collectJSONMetadata(String filename,int collectjobid ,Date createDate){
		try{
			File file =  new File(GlobalMethodAndParams.PATH_NAME + filename);			

			MetaDataRelation metadataRelation = new MetaDataRelation();

			//1.将文件元数据存入元数据表	
			Metadata metadataFile = new Metadata();
			metadataFile.setNAME(filename);
			metadataFile.setCOLLECTJOBID(collectjobid);
			metadataFile.setMETAMODELID(110);
			metadataFile.setCREATETIME(createDate);
			metadataFile.setUPDATETIME(createDate);
			metadataFile.setCHECKSTATUS("1");
			metadataFile.setVERSION(1);
			String databaseAttributes = "{\"filename\":\""+ filename
					+"\",\"filetype\":\"" + "1"
					+"\",\"filelength\":\"" + CalDataSize.getPrintSize(file.length())
					+"\",\"filepath\":\"" + GlobalMethodAndParams.PATH_NAME
					+"\"}";	

			metadataFile.setATTRIBUTES(databaseAttributes);

			if(!(iMetaDataDao.insertMetadata(metadataFile)>0 ? true:false)){//插入不成功
				throw new RuntimeException("文件元数据插入失败");
			}

			//2.将文件元数据加入metadata_tank表
			MetadataTank metadataTank = new MetadataTank();
			metadataTank.setCHECKSTATUS(metadataFile.getCHECKSTATUS());
			metadataTank.setATTRIBUTES(metadataFile.getATTRIBUTES());
			metadataTank.setCREATETIME(new Date());
			metadataTank.setDESCRIPTION(metadataFile.getDESCRIPTION());
			metadataTank.setKeyid(metadataFile.getID());
			metadataTank.setMETAMODELID(metadataFile.getMETAMODELID());
			metadataTank.setNAME(metadataFile.getNAME());
			metadataTank.setUPDATETIME(new Date());
			metadataTank.setVERSION(metadataFile.getVERSION());
			metadataTank.setCOLLECTJOBID(collectjobid);

			if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
				throw new RuntimeException("insertMetaDataTank Error");
			}

			ObjectMapper mapper = new ObjectMapper();
			JSONObject maps = mapper.readValue(file, JSONObject.class);
			JSONArray tables = maps.getJSONArray("TABLES");
			for(int i = 0 ; i < tables.size();	i++){
				JSONObject tableJson = (JSONObject)tables.get(i);
				String tableName = tableJson.get("tableName").toString();
				String tableDescription = tableJson.get("tableDescription").toString();

				//3.将表数据存入元数据表	
				Metadata metadataTable = new Metadata();
				metadataTable.setNAME(tableName);
				metadataTable.setCOLLECTJOBID(collectjobid);
				metadataTable.setMETAMODELID(110);
				metadataTable.setCREATETIME(createDate);
				metadataTable.setUPDATETIME(createDate);
				metadataTable.setCHECKSTATUS("1");
				metadataTable.setVERSION(1);
				metadataTable.setDESCRIPTION(tableDescription);
				String tableAttributes = "{\"tablename\":\""+ tableName +"\"}";

				metadataTable.setATTRIBUTES(tableAttributes);

				if(!(iMetaDataDao.insertMetadata(metadataTable)>0 ? true:false)){//插入不成功
					throw new RuntimeException("表元数据插入失败");
				}

				//4.将表元数据加入metadata_tank表
				metadataTank.setCHECKSTATUS(metadataTable.getCHECKSTATUS());
				metadataTank.setATTRIBUTES(metadataTable.getATTRIBUTES());
				metadataTank.setCREATETIME(new Date());
				metadataTank.setDESCRIPTION(metadataTable.getDESCRIPTION());
				metadataTank.setKeyid(metadataTable.getID());
				metadataTank.setMETAMODELID(metadataTable.getMETAMODELID());
				metadataTank.setNAME(metadataTable.getNAME());
				metadataTank.setUPDATETIME(new Date());
				metadataTank.setVERSION(metadataTable.getVERSION());
				metadataTank.setCOLLECTJOBID(collectjobid);

				if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
					throw new RuntimeException("insertMetaDataTank Error");
				}

				metadataRelation.setMetaDataId(metadataFile.getID());
				metadataRelation.setRelateMetaDataId(metadataTable.getID());
				metadataRelation.setType("DEPENDENCY");

				if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
					throw new RuntimeException("元数据关系插入失败");
				} 

				JSONArray table = tableJson.getJSONArray("TABLE");
				for(int j = 0 ; j < table.size(); j++){
					JSONObject fieldJson = (JSONObject)table.get(j);
					String fieldTypeAndLength = fieldJson.get("type").toString();
					String fieldType = fieldTypeAndLength;
					String fieldLength = null;
					if(fieldTypeAndLength.contains("（")){
						fieldType = fieldTypeAndLength.split("（")[0];
						fieldLength = fieldTypeAndLength.substring(fieldTypeAndLength.indexOf("（")+1,fieldTypeAndLength.indexOf("）"));
					}  
					//添加字段元数据 
					Metadata metadataField = new Metadata();
					metadataField.setNAME(fieldJson.get("name").toString());
					metadataField.setCOLLECTJOBID(collectjobid);
					metadataField.setMETAMODELID(32);
					metadataField.setCREATETIME(createDate);
					metadataField.setUPDATETIME(createDate);
					metadataField.setCHECKSTATUS("1");
					metadataField.setVERSION(1);
					metadataField.setDESCRIPTION(fieldJson.get("description").toString());
					String fieldAttributes = "{\"fieldname\":\""+ fieldJson.get("name").toString()
							+ "\",\"fieldtype\":\"" + fieldType
							+"\",\"length\":\"" + fieldLength
							+"\",\"isprimarykey\":\"" + false
							+"\",\"isforeignkey\":\"" + false
							+"\",\"allownull\":\"" + fieldJson.get("isNull").toString()
							+"\",\"defaultvalue\":\"" + fieldJson.get("default").toString()
							+"\"}";	

					metadataField.setATTRIBUTES(fieldAttributes);
					if(!(iMetaDataDao.insertMetadata(metadataField)>0 ? true:false)){//插入不成功
						throw new RuntimeException("字段元数据插入失败");
					}

					metadataRelation.setMetaDataId(metadataFile.getID());
					metadataRelation.setRelateMetaDataId(metadataField.getID());
					metadataRelation.setType("COMPOSITION");

					metadataTank.setCHECKSTATUS(metadataField.getCHECKSTATUS());
					metadataTank.setATTRIBUTES(metadataField.getATTRIBUTES());
					metadataTank.setCREATETIME(new Date());
					metadataTank.setDESCRIPTION(metadataField.getDESCRIPTION());
					metadataTank.setKeyid(metadataField.getID());
					metadataTank.setMETAMODELID(metadataField.getMETAMODELID());
					metadataTank.setNAME(metadataField.getNAME());
					metadataTank.setUPDATETIME(new Date());
					metadataTank.setVERSION(metadataField.getVERSION());
					metadataTank.setCOLLECTJOBID(collectjobid);

					if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
						throw new RuntimeException("insertMetaDataTank Error");
					}

					if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
						throw new RuntimeException("元数据关系插入失败");
					} 

				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 作者:GodDispose
	 * 时间:2018年5月7日
	 * 作用:根据file_connectinfo信息，自动采集TXT文件元数据
	 */
	public void collectTXTMetadata(String filename,String flag,int collectjobid ,Date createDate){
		File file = new File(GlobalMethodAndParams.PATH_NAME + filename);
		try {
			MetaDataRelation metadataRelation = new MetaDataRelation();
			//存储表格列名，name,type,default,isNull,description
			Map<String,Integer> map = new HashMap<String, Integer>();

			//1.将文件元数据存入元数据表	
			Metadata metadataFile = new Metadata();
			metadataFile.setNAME(filename);
			metadataFile.setCOLLECTJOBID(collectjobid);
			metadataFile.setMETAMODELID(110);
			metadataFile.setCREATETIME(createDate);
			metadataFile.setUPDATETIME(createDate);
			metadataFile.setCHECKSTATUS("1");
			metadataFile.setVERSION(1);
			String databaseAttributes = "{\"filename\":\""+ filename
					+"\",\"filetype\":\"" + "1"
					+"\",\"filelength\":\"" + CalDataSize.getPrintSize(file.length())
					+"\",\"filepath\":\"" + GlobalMethodAndParams.PATH_NAME
					+"\"}";	

			metadataFile.setATTRIBUTES(databaseAttributes);

			if(!(iMetaDataDao.insertMetadata(metadataFile)>0 ? true:false)){//插入不成功
				throw new RuntimeException("文件元数据插入失败");
			}

			//2.将文件元数据加入metadata_tank表
			MetadataTank metadataTank = new MetadataTank();
			metadataTank.setCHECKSTATUS(metadataFile.getCHECKSTATUS());
			metadataTank.setATTRIBUTES(metadataFile.getATTRIBUTES());
			metadataTank.setCREATETIME(new Date());
			metadataTank.setDESCRIPTION(metadataFile.getDESCRIPTION());
			metadataTank.setKeyid(metadataFile.getID());
			metadataTank.setMETAMODELID(metadataFile.getMETAMODELID());
			metadataTank.setNAME(metadataFile.getNAME());
			metadataTank.setUPDATETIME(new Date());
			metadataTank.setVERSION(metadataFile.getVERSION());
			metadataTank.setCOLLECTJOBID(collectjobid);

			if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
				throw new RuntimeException("insertMetaDataTank Error");
			}

			InputStreamReader insr = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(insr);
			String start = br.readLine();
			if(start.equals("Start")){
				String tableStart = br.readLine();
				while(tableStart.equals("Table Start")){
					String table = br.readLine();
					String tableName = table;
					String tableDescription = null;
					if(table.contains("（")){					
						tableName = table.split("（")[0];
						tableDescription = table.substring(table.indexOf("（")+1,table.indexOf("）"));
					}
					System.out.println(tableName + "..." + tableDescription);

					//3.将表数据存入元数据表	
					Metadata metadataTable = new Metadata();
					metadataTable.setNAME(tableName);
					metadataTable.setCOLLECTJOBID(collectjobid);
					metadataTable.setMETAMODELID(110);
					metadataTable.setCREATETIME(createDate);
					metadataTable.setUPDATETIME(createDate);
					metadataTable.setCHECKSTATUS("1");
					metadataTable.setVERSION(1);
					metadataTable.setDESCRIPTION(tableDescription);
					String tableAttributes = "{\"tablename\":\""+ tableName +"\"}";

					metadataTable.setATTRIBUTES(tableAttributes);

					if(!(iMetaDataDao.insertMetadata(metadataTable)>0 ? true:false)){//插入不成功
						throw new RuntimeException("表元数据插入失败");
					}

					//4.将表元数据加入metadata_tank表
					metadataTank.setCHECKSTATUS(metadataTable.getCHECKSTATUS());
					metadataTank.setATTRIBUTES(metadataTable.getATTRIBUTES());
					metadataTank.setCREATETIME(new Date());
					metadataTank.setDESCRIPTION(metadataTable.getDESCRIPTION());
					metadataTank.setKeyid(metadataTable.getID());
					metadataTank.setMETAMODELID(metadataTable.getMETAMODELID());
					metadataTank.setNAME(metadataTable.getNAME());
					metadataTank.setUPDATETIME(new Date());
					metadataTank.setVERSION(metadataTable.getVERSION());
					metadataTank.setCOLLECTJOBID(collectjobid);

					if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
						throw new RuntimeException("insertMetaDataTank Error");
					}

					metadataRelation.setMetaDataId(metadataFile.getID());
					metadataRelation.setRelateMetaDataId(metadataTable.getID());
					metadataRelation.setType("DEPENDENCY");

					if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
						throw new RuntimeException("元数据关系插入失败");
					} 

					String[] columns = br.readLine().split("\t");
					for(int i = 0 ; i < columns.length; i++){
						map.put(columns[i], i);
					}
					String temp = br.readLine().toString();
					while(!temp.equals("Table End")){
						String[] data = temp.split("\t");
						String fieldTypeAndLength = data[Integer.parseInt((map.get("type")).toString())];
						String fieldType = fieldTypeAndLength;
						String fieldLength = null;
						if(fieldTypeAndLength.contains("（")){
							fieldType = fieldTypeAndLength.split("（")[0];
							fieldLength = fieldTypeAndLength.substring(fieldTypeAndLength.indexOf("（")+1,fieldTypeAndLength.indexOf("）"));
						}   
						
						//添加字段元数据 
						Metadata metadataField = new Metadata();
						metadataField.setNAME(data[Integer.parseInt((map.get("name")).toString())]);
						metadataField.setCOLLECTJOBID(collectjobid);
						metadataField.setMETAMODELID(32);
						metadataField.setCREATETIME(createDate);
						metadataField.setUPDATETIME(createDate);
						metadataField.setCHECKSTATUS("1");
						metadataField.setVERSION(1);
						metadataField.setDESCRIPTION(data[Integer.parseInt((map.get("description")).toString())]);
						String fieldAttributes = "{\"fieldname\":\""+ data[Integer.parseInt((map.get("name")).toString())]
								+ "\",\"fieldtype\":\"" + fieldType
								+"\",\"length\":\"" + fieldLength
								+"\",\"isprimarykey\":\"" + false
								+"\",\"isforeignkey\":\"" + false
								+"\",\"allownull\":\"" + data[Integer.parseInt((map.get("isNull")).toString())]
										+"\",\"defaultvalue\":\"" + data[Integer.parseInt((map.get("default")).toString())]
												+"\"}";	

						metadataField.setATTRIBUTES(fieldAttributes);
						if(!(iMetaDataDao.insertMetadata(metadataField)>0 ? true:false)){//插入不成功
							throw new RuntimeException("字段元数据插入失败");
						}

						metadataRelation.setMetaDataId(metadataTable.getID());
						metadataRelation.setRelateMetaDataId(metadataField.getID());
						metadataRelation.setType("COMPOSITION");

						metadataTank.setCHECKSTATUS(metadataField.getCHECKSTATUS());
						metadataTank.setATTRIBUTES(metadataField.getATTRIBUTES());
						metadataTank.setCREATETIME(new Date());
						metadataTank.setDESCRIPTION(metadataField.getDESCRIPTION());
						metadataTank.setKeyid(metadataField.getID());
						metadataTank.setMETAMODELID(metadataField.getMETAMODELID());
						metadataTank.setNAME(metadataField.getNAME());
						metadataTank.setUPDATETIME(new Date());
						metadataTank.setVERSION(metadataField.getVERSION());
						metadataTank.setCOLLECTJOBID(collectjobid);

						if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
							throw new RuntimeException("insertMetaDataTank Error");
						}

						if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
							throw new RuntimeException("元数据关系插入失败");
						} 
						temp = br.readLine().toString();
					}
					tableStart = br.readLine();
				}
			}
			br.close();
			insr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 作者:GodDispose
	 * 时间:2018年5月7日
	 * 作用:判断数据的类型(Number、Boolean、String)
	 */
	public String judgeType(String data){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(data);
		if( isNum.matches() ){
			return "Number";
		}else if("true".equalsIgnoreCase(data) || "false".equalsIgnoreCase(data)){
			return "Boolean";
		}		
		return "String";
	}

}
