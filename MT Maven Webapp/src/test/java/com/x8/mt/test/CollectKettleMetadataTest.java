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
		File file = new File("C:/Users/jason zhou/Desktop/file1.xls"); 
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
           }else if(sheet.getPhysicalNumberOfRows() == 1){  //只存在1行
          	 HSSFRow row=sheet.getRow(0);
               for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
                   System.out.print(row.getCell(k)+"\t");
               }
           }else if(sheet.getPhysicalNumberOfRows() == 2){  //只存在2行
          	 HSSFRow row1=sheet.getRow(0);
          	 HSSFRow row2=sheet.getRow(1);
               for (int k = 0; k < row1.getPhysicalNumberOfCells(); k++) {
                   System.out.print(row1.getCell(i).toString());
                   switch (row2.getCell(0).getCellType()) {
	 	                case HSSFCell.CELL_TYPE_STRING:
	 	                	System.out.print("("+"String" + ")"+ "\t");
	 	                	break;
	 	                case HSSFCell.CELL_TYPE_NUMERIC:
	 	                	System.out.print("(" + "Number" +")"+ "\t");
	 	                	break;
	 	                case HSSFCell.CELL_TYPE_BOOLEAN:
	 	                	System.out.print("(" + "Boolean" +")"+ "\t");
	 	                	break;
	 	                default:
	 	                	break;
                   }
               }
           }else if(sheet.getPhysicalNumberOfRows() > 2){  //只存在2行
          	 HSSFRow row1=sheet.getRow(0);
          	 HSSFRow row2=sheet.getRow(1);
          	 HSSFRow row3=sheet.getRow(2);
          	 List<String> list = new ArrayList<String>();
               for (int k = 0; k < row1.getPhysicalNumberOfCells(); k++) {
                   System.out.print(row1.getCell(k).toString());
                   switch (row2.getCell(0).getCellType()) {
	 	                case HSSFCell.CELL_TYPE_STRING:
	 	                 	System.out.print("("+"String" + ")" + "\t");
	 	                	break;
	 	                case HSSFCell.CELL_TYPE_NUMERIC:
	 	                	if(row3.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING){
	 	                     	System.out.print("("+"String" + ")"+ "\t");
	 	                	}else{	 	                		
	 	                		System.out.print("(" + "Number" +")"+ "\t");
	 	                	}
	 	                	break;
	 	                case HSSFCell.CELL_TYPE_BOOLEAN:
	 	                	System.out.print("(" + "Boolean" +")"+ "\t");
	 	                	break;
	 	                default:
	 	                	break;
                   }
               }
           }
           System.out.println();
	    }
		System.out.println("Excel......................");
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
