package com.x8.mt.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import org.pentaho.di.trans.steps.edi2xml.grammar.FastSimpleGenericEdifactDirectXMLParser.txt_return;
import org.pentaho.di.trans.steps.excelinput.ExcelInputMeta;
import org.springframework.stereotype.Service;

import com.x8.mt.common.CalDataSize;
import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.dao.ICollectJobDao;
import com.x8.mt.dao.IMetaDataDao;
import com.x8.mt.dao.IMetadataTankDao;
import com.x8.mt.dao.IMetamodel_datatypeDao;

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
	
//	/**
//	 * 作者:GodDispose
//	 * 时间:2018年3月12日
//	 * 作用:根据Datasource_connectinfo信息，kettle自动采集数据库与表元数据
//	 * 多表插入，有事务
//	 * @throws SQLException 
//	 * @throws ParseException 
//	 */
//	@Transactional
//	public String collectExcelMetaData(Datasource_connectinfo datasource_connectinfo,int collectjobid,Date createDate,int datasourceId
//			,List<Table> tables) throws KettleException, SQLException, ParseException {
//		int collectCount = 0;//表元数据、字段元数据记录数共同记录
//		
//		//1.将数据库元数据存入元数据表
//		String[] tablenames = database.getTablenames();//获取数据库中所有表名		
//		Metadata metadataDatabase = new Metadata();
//		metadataDatabase.setNAME(datasource_connectinfo.getDatabasename());
//		metadataDatabase.setCOLLECTJOBID(collectjobid);
//		metadataDatabase.setMETAMODELID(10);
//		metadataDatabase.setCREATETIME(createDate);
//		metadataDatabase.setUPDATETIME(createDate);
//		metadataDatabase.setCHECKSTATUS("1");
//		metadataDatabase.setVERSION(1);
//		String databaseAttributes = "{\"dbtype\":\""+datasource_connectinfo.getDatabasetype()
//				+ "\",\"dbversion\":\"1" 
//				+"\",\"dbip\":\"" + datasource_connectinfo.getUrl()
//				+"\",\"dbport\":\"" + datasource_connectinfo.getPort()
//				+"\",\"dbuser\":\"" + datasource_connectinfo.getUsername()
//				+"\",\"dbpassword\":\"" + datasource_connectinfo.getPassword()
//				+"\",\"dbname\":\"" + datasource_connectinfo.getDatabasename()
//				+"\"}";	
//		
//		metadataDatabase.setATTRIBUTES(databaseAttributes);
//
//		if(!(iMetaDataDao.insertMetadata(metadataDatabase)>0 ? true:false)){//插入不成功
//			throw new RuntimeException("数据库元数据插入失败");
//		}
//		collectCount++;//记录数据库元数据
//		
//		//2.将数据库元数据加入metadata_tank表
//		MetadataTank metadataTank = new MetadataTank();
//		metadataTank.setCHECKSTATUS(metadataDatabase.getCHECKSTATUS());
//		metadataTank.setATTRIBUTES(metadataDatabase.getATTRIBUTES());
//		metadataTank.setCREATETIME(new Date());
//		metadataTank.setDESCRIPTION(metadataDatabase.getDESCRIPTION());
//		metadataTank.setKeyid(metadataDatabase.getID());
//		metadataTank.setMETAMODELID(metadataDatabase.getMETAMODELID());
//		metadataTank.setNAME(metadataDatabase.getNAME());
//		metadataTank.setUPDATETIME(new Date());
//		metadataTank.setVERSION(metadataDatabase.getVERSION());
//		metadataTank.setCOLLECTJOBID(collectjobid);
//
//		if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
//			throw new RuntimeException("insertMetaDataTank Error");
//		}
//		
//		
//		for(Table table : tables){
//			//表信息插入Metadata
//			Metadata metadataTable = new Metadata();
//			metadataTable.setNAME(table.getName());
//			metadataTable.setCOLLECTJOBID(collectjobid);
//			metadataTable.setMETAMODELID(31);
//			metadataTable.setCREATETIME(createDate);
//			metadataTable.setUPDATETIME(createDate);
//			metadataTable.setCHECKSTATUS("1");
//			metadataTable.setVERSION(1);
//			String tableAttributes = "{\"tablename\":\""+ table.getName() +"\"}";
//			metadataTable.setATTRIBUTES(tableAttributes);	
//
//			if(!(iMetaDataDao.insertMetadata(metadataTable)>0 ? true:false)){//插入不成功
//				throw new RuntimeException("表元数据插入失败");
//			}
//			collectCount++;//记录表元数据
//			
//			table.setId(metadataTable.getID());
//			table.setOperationDescription(null);
//			table.setOperationName(null);
//			
//			MetaDataRelation metadataRelation = new MetaDataRelation();
//			metadataRelation.setMetaDataId(metadataDatabase.getID());
//			metadataRelation.setRelateMetaDataId(metadataTable.getID());
//			metadataRelation.setType("COMPOSITION");
//			
//			if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
//				throw new RuntimeException("元数据关系插入失败");
//			}
//
//			metadataTank.setCHECKSTATUS(metadataTable.getCHECKSTATUS());
//			metadataTank.setATTRIBUTES(metadataTable.getATTRIBUTES());
//			metadataTank.setCREATETIME(new Date());
//			metadataTank.setDESCRIPTION(metadataTable.getDESCRIPTION());
//			metadataTank.setKeyid(metadataTable.getID());
//			metadataTank.setMETAMODELID(metadataTable.getMETAMODELID());
//			metadataTank.setNAME(metadataTable.getNAME());
//			metadataTank.setUPDATETIME(new Date());
//			metadataTank.setVERSION(metadataTable.getVERSION());
//			metadataTank.setCOLLECTJOBID(collectjobid);
//
//			if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
//				throw new RuntimeException("insertMetaDataTank Error");
//			}
//
//		}
//		shutdownKettleEnvironment(database);
//		
//		long size = 0 ;
//		
//		size += datasource_connectinfo.getDatabasename().getBytes().length;
//		for(String str : tablenames){
//			size += str.getBytes().length;
//		}
//		
//		return collectCount + "_" + size;
//	}
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年5月7日
	 * 作用:根据file_connectinfo信息，自动采集Excel文件元数据
	 */
	public void collectExcelMetaData(String filename){
		try{
			File file = new File(GlobalMethodAndParams.PATH_NAME + filename); 
			
			HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream(file));
	        
	        System.out.println("文件大小："+CalDataSize.getPrintSize(file.length()));
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
	                     System.out.print(row1.getCell(k).toString());
	                     switch (row2.getCell(k).getCellType()) {
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
	                     switch (row2.getCell(k).getCellType()) {
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
		}catch(Exception e){
			e.printStackTrace();
		}
		
	 }
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年5月7日
	 * 作用:根据file_connectinfo信息，自动采集XML文件元数据
	 */
	public  void collectXmlMetadata(String filename){
		try {
            File file = new File(GlobalMethodAndParams.PATH_NAME + filename);
            System.out.println("文件大小："+CalDataSize.getPrintSize(file.length()));
            SAXReader reader=new SAXReader();
            //读取xml文件到Document中
            Document doc=reader.read(file);
            //获取xml文件的根节点
            Element rootElement=doc.getRootElement();
            //定义一个Element用于遍历
            Element fooElement;
            //遍历所有名叫“Row”的节点
            
            List<Element> elements1 = rootElement.elements();
            for(Element e:elements1){
            	System.out.println("节点名:" + e.getName() + "\t" + "节点路径:" + e.getPath());
            	List<Element> elements = e.elements();
                for(Element child:elements){
                	System.out.print(child.getName() +"("+judgeType(child.getText())+ ")" + "\t"+ child.getText()+"\t");
                	
                }
                System.out.println();
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
	public  void collectJSONMetadata(String filename){
		try{
			File file =  new File(GlobalMethodAndParams.PATH_NAME + filename);
			ObjectMapper mapper = new ObjectMapper();
			Map<String,Object> maps = mapper.readValue(file, Map.class);
			for(String key : maps.keySet()){
				System.out.println(key +"("+judgeType(maps.get(key).toString()) +")"+"...." + maps.get(key));
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
	public void collectTXTMetadata(String filename,String flag){
		File file = new File(GlobalMethodAndParams.PATH_NAME + filename);
		try {
			InputStreamReader insr = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(insr);
			String row1 = br.readLine();
			String row2 = br.readLine();
			String[] fieldNames = row1.split(flag);
			String[] fieldData = row2.split(flag);
			for(int i = 0 ; i < fieldNames.length ; i++){
				System.out.println(fieldNames[i] + "(" + judgeType(fieldData[i]) + ")" );
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
