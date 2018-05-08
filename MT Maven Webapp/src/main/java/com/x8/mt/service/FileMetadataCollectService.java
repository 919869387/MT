package com.x8.mt.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

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
			
	        HSSFSheet sheet=null;
//	        System.out.println("工作列表名称：" );
//	        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//	        	System.out.print(workbook.getSheetName(i)+"(共有"+ workbook.getSheetAt(i).getPhysicalNumberOfRows()+ "条记录)"+"\t");
//	        }
//	        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
	        for (int i = 0; i < 1; i++) {//只获取第一个Sheet表
	             sheet=workbook.getSheetAt(i);
	             if(sheet.getPhysicalNumberOfRows() == 0){	//只存在0行
	            	 System.out.println("此文件不存在元数据！");
	             }else if(sheet.getPhysicalNumberOfRows() > 0){  //存在数据
	            	 HSSFRow row1=sheet.getRow(0);
	            	 HSSFRow row2=sheet.getRow(1);
	                 for (int k = 0; k < row1.getPhysicalNumberOfCells(); k++) {
	                	 String fieldType = "String";
	                     switch (row2.getCell(k).getCellType()) {
		 	                case HSSFCell.CELL_TYPE_STRING:
		 	                	fieldType = "String";
		 	                	break;
		 	                case HSSFCell.CELL_TYPE_NUMERIC:
		 	                	fieldType = "Number";
		 	                	break;
		 	                case HSSFCell.CELL_TYPE_BOOLEAN:
		 	                	fieldType = "Boolean";
		 	                	break;
		 	                default:
		 	                	break;
	                     }
	                     
	                    //添加字段元数据 
	                    Metadata metadataField = new Metadata();
	     				metadataField.setNAME(row1.getCell(k).toString());
	     				metadataField.setCOLLECTJOBID(collectjobid);
	     				metadataField.setMETAMODELID(32);
	     				metadataField.setCREATETIME(createDate);
	     				metadataField.setUPDATETIME(createDate);
	     				metadataField.setCHECKSTATUS("1");
	     				metadataField.setVERSION(1);
	     				String fieldAttributes = "{\"fieldname\":\""+ row1.getCell(k).toString()
	     					+ "\",\"fieldtype\":\"" + fieldType
	     					+"\",\"length\":\"" + 0
	     					+"\",\"isprimarykey\":\"" + false
	     					+"\",\"isforeignkey\":\"" + false
	     					+"\",\"allownull\":\"" + false
	     					+"\",\"defaultvalue\":\"" + 0
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
		    }
	        
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
            Element fooElement;
            //遍历所有名叫“Row”的节点
            
            List<Element> elements1 = rootElement.elements();
            List<Element> elements = elements1.get(0).elements();
            for(Element child:elements){
            	//添加字段元数据 
                Metadata metadataField = new Metadata();
 				metadataField.setNAME(child.getName());
 				metadataField.setCOLLECTJOBID(collectjobid);
 				metadataField.setMETAMODELID(32);
 				metadataField.setCREATETIME(createDate);
 				metadataField.setUPDATETIME(createDate);
 				metadataField.setCHECKSTATUS("1");
 				metadataField.setVERSION(1);
 				String fieldAttributes = "{\"fieldname\":\""+ child.getName()
 					+ "\",\"fieldtype\":\"" + judgeType(child.getText())
 					+"\",\"length\":\"" + 0
 					+"\",\"isprimarykey\":\"" + false
 					+"\",\"isforeignkey\":\"" + false
 					+"\",\"allownull\":\"" + false
 					+"\",\"defaultvalue\":\"" + 0
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
        } catch (Exception e) {
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
			
			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> maps = mapper.readValue(file, Map.class);
			for(String key : maps.keySet()){
                //添加字段元数据 
                Metadata metadataField = new Metadata();
 				metadataField.setNAME(key);
 				metadataField.setCOLLECTJOBID(collectjobid);
 				metadataField.setMETAMODELID(32);
 				metadataField.setCREATETIME(createDate);
 				metadataField.setUPDATETIME(createDate);
 				metadataField.setCHECKSTATUS("1");
 				metadataField.setVERSION(1);
 				String fieldAttributes = "{\"fieldname\":\""+ key
 					+ "\",\"fieldtype\":\"" + judgeType(maps.get(key).toString())
 					+"\",\"length\":\"" + 0
 					+"\",\"isprimarykey\":\"" + false
 					+"\",\"isforeignkey\":\"" + false
 					+"\",\"allownull\":\"" + false
 					+"\",\"defaultvalue\":\"" + 0
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
			
			InputStreamReader insr = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(insr);
			String row1 = br.readLine();
			String row2 = br.readLine();
			String[] fieldNames = row1.split(flag);
			String[] fieldData = row2.split(flag);
			for(int i = 0 ; i < fieldNames.length ; i++){
				//添加字段元数据 
                Metadata metadataField = new Metadata();
 				metadataField.setNAME(fieldNames[i]);
 				metadataField.setCOLLECTJOBID(collectjobid);
 				metadataField.setMETAMODELID(32);
 				metadataField.setCREATETIME(createDate);
 				metadataField.setUPDATETIME(createDate);
 				metadataField.setCHECKSTATUS("1");
 				metadataField.setVERSION(1);
 				String fieldAttributes = "{\"fieldname\":\""+ fieldNames[i]
 					+ "\",\"fieldtype\":\"" + judgeType(fieldData[i])
 					+"\",\"length\":\"" + 0
 					+"\",\"isprimarykey\":\"" + false
 					+"\",\"isforeignkey\":\"" + false
 					+"\",\"allownull\":\"" + false
 					+"\",\"defaultvalue\":\"" + 0
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
