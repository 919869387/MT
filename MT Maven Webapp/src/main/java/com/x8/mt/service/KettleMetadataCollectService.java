package com.x8.mt.service;

import java.util.Date;
import java.util.List;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.dao.ICollectJobDao;
import com.x8.mt.dao.IMetaDataDao;
import com.x8.mt.dao.IMetamodel_datatypeDao;
import com.x8.mt.entity.CollectJob;
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.entity.MetaDataRelation;
import com.x8.mt.entity.Metadata;

@Service
public class KettleMetadataCollectService {

	@Resource
	ICollectJobDao iCollectJobDao;	
	@Resource
	IMetaDataDao iMetaDataDao;
	@Resource
	MetaDataRelationService metaDataRelationService;
	@Resource
	IMetamodel_datatypeDao iMetamodel_datatypeDao;


	Database database = null;
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据Datasource_connectinfo信息，kettle自动采集元数据
	 * 多表插入，有事务
	 * @throws SQLException 
	 */
	@Transactional
	public int metadataAutoCollect(Datasource_connectinfo datasource_connectinfo,int collectjobid,Date createDate,int datasourceId) throws KettleException, SQLException {
		int collectCount = 0;//表元数据、字段元数据记录数共同记录
		
		database = initKettleEnvironment(datasource_connectinfo);

		String[] tablenames = database.getTablenames();//获取数据库中所有表名		
		Metadata metadataDatabase = new Metadata();
		metadataDatabase.setNAME(datasource_connectinfo.getDatabasename());
		metadataDatabase.setCOLLECTJOBID(collectjobid);
		metadataDatabase.setMETAMODELID(10);
		metadataDatabase.setCREATETIME(createDate);
		metadataDatabase.setCREATETIME(createDate);
		metadataDatabase.setCHECKSTATUS("1");
		metadataDatabase.setVERSION(1);
		String databaseAttributes = "{\"dbtype\":\""+datasource_connectinfo.getDatabasetype()
				+ "\",\"dbversion\":\"1" 
				+"\",\"dbip\":\"" + datasource_connectinfo.getUrl()
				+"\",\"dbport\":\"" + datasource_connectinfo.getPort()
				+"\",\"dbuser\":\"" + datasource_connectinfo.getUsername()
				+"\",\"dbpassword\":\"" + datasource_connectinfo.getPassword()
				+"\",\"dbname\":\"" + datasource_connectinfo.getDatabasename()
				+"\"}";	
		
		metadataDatabase.setATTRIBUTES(databaseAttributes);
		if(!(iMetaDataDao.insertMetadata(metadataDatabase)>0 ? true:false)){//插入不成功
			throw new RuntimeException("数据库元数据插入失败");
		}
		
		for(String tablename : tablenames){
			//表信息插入Metadata
			Metadata metadataTable = new Metadata();
			metadataTable.setNAME(tablename);
			metadataTable.setCOLLECTJOBID(collectjobid);
			metadataTable.setMETAMODELID(31);
			metadataTable.setCREATETIME(createDate);
			metadataTable.setCREATETIME(createDate);
			metadataTable.setCHECKSTATUS("1");
			metadataTable.setVERSION(1);
			String tableAttributes = "{\"tablename\":\""+ tablename +"\"}";
			metadataTable.setATTRIBUTES(tableAttributes);	
			if(!(iMetaDataDao.insertMetadata(metadataTable)>0 ? true:false)){//插入不成功
				throw new RuntimeException("表元数据插入失败");
			}
			collectCount++;//记录表元数据
			
			MetaDataRelation metadataRelation = new MetaDataRelation();
			metadataRelation.setMetaDataId(metadataDatabase.getID());
			metadataRelation.setRelateMetaDataId(metadataTable.getID());
			metadataRelation.setType("COMPOSITION");
			
			if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
				throw new RuntimeException("元数据关系插入失败");
			}
			
			RowMetaInterface tableFields = database.getTableFields(tablename);

			String[] primaryKeyColumnNames = database.getPrimaryKeyColumnNames(tablename);//得到表中所有主键名
			
			List<ValueMetaInterface> fieldNameTypeList = tableFields.getValueMetaList();
			for(ValueMetaInterface fieldNameType : fieldNameTypeList){
				Metadata MetadataField = new Metadata();
				MetadataField.setNAME(fieldNameType.getName());
				MetadataField.setCOLLECTJOBID(collectjobid);
				MetadataField.setMETAMODELID(32);
				MetadataField.setCREATETIME(createDate);
				MetadataField.setCREATETIME(createDate);
				MetadataField.setCHECKSTATUS("1");
				MetadataField.setVERSION(1);
				String fieldAttributes = "{\"fieldname\":\""+ fieldNameType.getName()
					+ "\",\"fieldtype\":\"" + fieldNameType.getOriginalColumnTypeName()
					+"\",\"length\":\"" + fieldNameType.getLength()
					+"\",\"isprimarykey\":\"" + isHave(primaryKeyColumnNames,fieldNameType.getName())
					+"\",\"isforeignkey\":\"" + false
					+"\",\"allownull\":\"" + false
					+"\",\"defaultvalue\":\"" + 0
					+"\"}";	
				
				MetadataField.setATTRIBUTES(fieldAttributes);
					
				if(!(iMetaDataDao.insertMetadata(MetadataField)>0 ? true:false)){//插入不成功
					throw new RuntimeException("字段元数据插入失败");
				}
				collectCount++;//记录字段元数据

				metadataRelation.setMetaDataId(metadataTable.getID());
				metadataRelation.setRelateMetaDataId(MetadataField.getID());
				metadataRelation.setType("COMPOSITION");
				
				if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
					throw new RuntimeException("元数据关系插入失败");
				}
			}

		}
		shutdownKettleEnvironment(database);
		
		return collectCount;
	}
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月12日
	 * 作用:根据Datasource_connectinfo信息，获取表结构
	 * 
	 */
	@Transactional
	public String[] getTables(Datasource_connectinfo datasource_connectinfo) throws KettleException {
		database = initKettleEnvironment(datasource_connectinfo);

		String[] tablenames = database.getTablenames();//获取数据库中所有表名
		
		shutdownKettleEnvironment(database);
		
		return tablenames;
	}
	

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月21日
	 * 作用:kettle创建数据库连接
	 */
	public Database initKettleEnvironment(Datasource_connectinfo datasource_connectinfo) throws KettleException {

		//初始化kettle环境
		KettleEnvironment.init();

		//创建资源库对象，此时的对象还是一个空对象
		DatabaseMeta dataMeta = new DatabaseMeta(GlobalMethodAndParams.DatabaseMetaName,
				datasource_connectinfo.getDatabasetype(), 
				GlobalMethodAndParams.kettleDatabaseMetaAccess_JDBC, 
				datasource_connectinfo.getUrl(),
				datasource_connectinfo.getDatabasename(),
				datasource_connectinfo.getPort(), 
				datasource_connectinfo.getUsername(), 
				datasource_connectinfo.getPassword());
		

		database = new Database(null, dataMeta);
		database.connect();

		return database;
	}

	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月21日
	 * 作用:关闭kettle连接资源
	 */
	private void shutdownKettleEnvironment(Database database){
		database.disconnect();
		KettleEnvironment.shutdown();
	}
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月22日
	 * 作用:循环查找字符串数组中的每个字符串中是否包含所有查找的内容
	 */
	private boolean isHave(String[] strs,String s){ 
		for(int i=0;i<strs.length;i++){ 
			if(strs[i].indexOf(s)!=-1){//循环查找字符串数组中的每个字符串中是否包含所有查找的内容 
				return true;//查找到了就返回真，不在继续查询 
			} 
		} 
		return false;//没找到返回false 
	} 
	


	
	/**
	 * 
	 * 作者:GodDipose
	 * 时间:2017年12月16日
	 * 作用:根据id查找采集任务
	 */
	public CollectJob getCollectJobById(int id){
		try{
			return  iCollectJobDao.getCollectJobById(id);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	

}
