package com.x8.mt.test;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.x8.mt.dao.IDatasource_connectinfoDao;
import com.x8.mt.entity.CollectJob;
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.service.CollectJobService;
import com.x8.mt.service.FileMetadataCollectService;
import com.x8.mt.service.KettleMetadataCollectService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration({"classpath*:applicationContext.xml"}) 
/**
 * 作者： GodDispose
 * 时间：2018年4月11日
 * 作用：
 */
public class CollectKettleMetadataTest {
	@Autowired
	KettleMetadataCollectService kettleMetadataCollectService;
	
	@Autowired
	FileMetadataCollectService fileMetadataCollectService;
	
	@Autowired
	CollectJobService collectJobService;
	
	@Autowired
	IDatasource_connectinfoDao datasource_connectinfoDao;

	@Test
	public void getJobs() throws KettleException{
		Datasource_connectinfo datasource_connectinfo = datasource_connectinfoDao.getDatasource_connectinfoByid(1);
		//int collectKettleJob = kettleMetadataCollectService.collectKettleJob(datasource_connectinfo);
	}
	
	@Test
	public void getRecentCollectJobByConnectinfoId(){
		int id = collectJobService.getRecentCollectJobByConnectinfoId(102).getId();
		System.out.println(id);
	}
	
	@Test
	public void judgeIsExist(){
		List<CollectJob> collectJob = collectJobService.getCollectJobByConnectinfoId(88);
		if(!collectJob.isEmpty()  || collectJob.size() > 0){
			System.out.println(1);
		}else{
			System.out.println(2);
		}
	}
	
//	@Test
//	public void calString(){
//		int size = 0;
//		for(String str : tablenames){
//			size = str.get
//		    char[] chs = str.toCharArray();
//		    for(int i = 0; i < chs.length; i++) {
//		    	size += (chs[i] > 0xff) ? 2 : 1;
//		    }
//		}
//		String a = "ddddddd";
//
//		byte[] buf = a.getBytes();
//
//		System.out.println(buf.length+"Byte="+buf.length/1024+"KB" );
//	}	
	
	@Test
	public void testExcel() throws FileNotFoundException, IOException{
		//fileMetadataCollectService.collectExcelMetaData("new1.xls");
		File file = new File("C:/Users/jason zhou/Desktop/学习项目/文件类型说明/file.xls"); 
      HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream(file));
      
      //System.out.println("文件大小："+FormetFileSize(file.length()));
      HSSFSheet sheet=null;
      System.out.println("工作列表名称：" );
      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
      	System.out.print(workbook.getSheetName(i)+"(共有"+ workbook.getSheetAt(i).getPhysicalNumberOfRows()+ "条记录)"+"\t");
      }
      System.out.println("\n");
      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
           sheet=workbook.getSheetAt(i);
           System.out.println(workbook.getSheetName(i));
           if(sheet.getPhysicalNumberOfRows() == 0){	//只存在0行
          	 System.out.println("此文件不存在元数据！");
           }else if(sheet.getPhysicalNumberOfRows() > 2){  //只存在2行
        	   String database = sheet.getRow(0).getCell(0).toString();
//        	   String databaseName = database.split("(")[0];
//        	   String databaseDescription = database.substring(database.indexOf("(")+1,database.indexOf(")"));
        	   String databaseName = database.split("（")[0];
        	   String databaseDescription = database.substring(database.indexOf("（")+1,database.indexOf("）"));
        	   for(int k = 2 ; k < sheet.getPhysicalNumberOfRows(); k++){
        		   HSSFRow row = sheet.getRow(k);
            	   String fieldTypeAndLength = row.getCell(2).toString();
//            	   String fieldType = database.split("(")[0];
//            	   String fieldLength = database.substring(database.indexOf("(")+1,database.indexOf(")")); 
            	   String fieldType = database.split("（")[0];
            	   String fieldLength = database.substring(database.indexOf("（")+1,database.indexOf("）"));
            	   System.out.println("FieldName:" + row.getCell(0).toString() 
        				   + "\t" + "FieldType:" + fieldType
        				   + "\t" + "FieldTLength:" + fieldLength
        				   + "\t" + "isNull:" + row.getCell(3).toString()
        				   + "\t" + "Default:" + row.getCell(4).toString()
        				   + "\t" + "Description:" + row.getCell(6).toString());
        		   System.out.println();
        	   }        	  
           }
      	}
	}
	
	@Test
	public void testXml(){
		//fileMetadataCollectService.collectXmlMetadata("input_test.xml");
		System.out.println("xml......................");
	}
	
	@Test
	public void testJSON(){
		//fileMetadataCollectService.collectJSONMetadata("json_test.js");
		System.out.println("JSON......................");
	}
	
	@Test
	public void testText(){
		//fileMetadataCollectService.collectTXTMetadata("new.txt",";");
		System.out.println("TEXT......................");
	}

}
