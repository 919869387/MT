package com.x8.mt.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.dao.ICollectJobDao;
import com.x8.mt.dao.IMetaDataDao;
import com.x8.mt.dao.IMetadataTankDao;
import com.x8.mt.dao.IMetamodel_datatypeDao;
import com.x8.mt.entity.CollectJob;
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.entity.MetaDataRelation;
import com.x8.mt.entity.Metadata;
import com.x8.mt.entity.MetadataTank;
import com.x8.mt.entity.Table;

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
	@Resource
	IMetadataTankDao iMetadataTankDao;


	Database database = null;
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年3月12日
	 * 作用:根据Datasource_connectinfo信息，kettle自动采集数据库与表元数据
	 * 多表插入，有事务
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	@Transactional
	public String collectDataBaseAndTableMetaData(Datasource_connectinfo datasource_connectinfo,int collectjobid,Date createDate,int datasourceId
			,List<Table> tables) throws KettleException, SQLException, ParseException {
		int collectCount = 0;//表元数据、字段元数据记录数共同记录
		database = initKettleEnvironment(datasource_connectinfo);
		
		//1.将数据库元数据存入元数据表
		String[] tablenames = database.getTablenames();//获取数据库中所有表名		
		Metadata metadataDatabase = new Metadata();
		metadataDatabase.setNAME(datasource_connectinfo.getDatabasename());
		metadataDatabase.setCOLLECTJOBID(collectjobid);
		metadataDatabase.setMETAMODELID(10);
		metadataDatabase.setCREATETIME(createDate);
		metadataDatabase.setUPDATETIME(createDate);
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
		collectCount++;//记录数据库元数据
		
		//2.将数据库元数据加入metadata_tank表
		MetadataTank metadataTank = new MetadataTank();
		metadataTank.setCHECKSTATUS(metadataDatabase.getCHECKSTATUS());
		metadataTank.setATTRIBUTES(metadataDatabase.getATTRIBUTES());
		metadataTank.setCREATETIME(new Date());
		metadataTank.setDESCRIPTION(metadataDatabase.getDESCRIPTION());
		metadataTank.setKeyid(metadataDatabase.getID());
		metadataTank.setMETAMODELID(metadataDatabase.getMETAMODELID());
		metadataTank.setNAME(metadataDatabase.getNAME());
		metadataTank.setUPDATETIME(new Date());
		metadataTank.setVERSION(metadataDatabase.getVERSION());
		metadataTank.setCOLLECTJOBID(collectjobid);

		if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
			throw new RuntimeException("insertMetaDataTank Error");
		}
		
		
		for(Table table : tables){
			//表信息插入Metadata
			Metadata metadataTable = new Metadata();
			metadataTable.setNAME(table.getName());
			metadataTable.setCOLLECTJOBID(collectjobid);
			metadataTable.setMETAMODELID(31);
			metadataTable.setCREATETIME(createDate);
			metadataTable.setUPDATETIME(createDate);
			metadataTable.setCHECKSTATUS("1");
			metadataTable.setVERSION(1);
			String tableAttributes = "{\"tablename\":\""+ table.getName() +"\"}";
			metadataTable.setATTRIBUTES(tableAttributes);	

			if(!(iMetaDataDao.insertMetadata(metadataTable)>0 ? true:false)){//插入不成功
				throw new RuntimeException("表元数据插入失败");
			}
			collectCount++;//记录表元数据
			
			table.setId(metadataTable.getID());
			table.setOperationDescription(null);
			table.setOperationName(null);
			
			MetaDataRelation metadataRelation = new MetaDataRelation();
			metadataRelation.setMetaDataId(metadataDatabase.getID());
			metadataRelation.setRelateMetaDataId(metadataTable.getID());
			metadataRelation.setType("COMPOSITION");
			
			if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
				throw new RuntimeException("元数据关系插入失败");
			}

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

		}
		shutdownKettleEnvironment(database);
		
		long size = 0 ;
		
		size += datasource_connectinfo.getDatabasename().getBytes().length;
		for(String str : tablenames){
			size += str.getBytes().length;
		}
		
		return collectCount + "_" + size;
	}
	
	/**
	 * 作者:GodDispose
	 * 时间:2018年4月8日
	 * 作用:根据Datasource_connectinfo信息，kettle自动采集字段元数据
	 * 多表插入，有事务
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	@Transactional
	public String collectFieldMetaData(Datasource_connectinfo datasource_connectinfo,int collectjobid,Date createDate,int datasourceId
			,List<Table> tables) throws KettleException, SQLException, ParseException {
		int collectCount = 0;//表元数据、字段元数据记录数共同记录
		
		database = initKettleEnvironment(datasource_connectinfo);

		String[] tablenames = database.getTablenames();//获取数据库中所有表名	
		MetaDataRelation metadataRelation = new MetaDataRelation();
		MetadataTank metadataTank = new MetadataTank();
		
		int size = 0;
		
		for(Table table : tables){
			RowMetaInterface tableFields = database.getTableFields(table.getName());

			String[] primaryKeyColumnNames = database.getPrimaryKeyColumnNames(table.getName());//得到表中所有主键名
			
			List<ValueMetaInterface> fieldNameTypeList = tableFields.getValueMetaList();
			for(ValueMetaInterface fieldNameType : fieldNameTypeList){
				Metadata metadataField = new Metadata();
				metadataField.setNAME(fieldNameType.getName());
				metadataField.setCOLLECTJOBID(collectjobid);
				metadataField.setMETAMODELID(32);
				metadataField.setCREATETIME(createDate);
				metadataField.setUPDATETIME(createDate);
				metadataField.setCHECKSTATUS("1");
				metadataField.setVERSION(1);
				String fieldAttributes = "{\"fieldname\":\""+ fieldNameType.getName()
					+ "\",\"fieldtype\":\"" + fieldNameType.getOriginalColumnTypeName()
					+"\",\"length\":\"" + fieldNameType.getLength()
					+"\",\"isprimarykey\":\"" + isHave(primaryKeyColumnNames,fieldNameType.getName())
					+"\",\"isforeignkey\":\"" + false
					+"\",\"allownull\":\"" + false
					+"\",\"defaultvalue\":\"" + 0
					+"\"}";	
				
				metadataField.setATTRIBUTES(fieldAttributes);
				if(!(iMetaDataDao.insertMetadata(metadataField)>0 ? true:false)){//插入不成功
					throw new RuntimeException("字段元数据插入失败");
				}
				collectCount++;//记录字段元数据

				metadataRelation.setMetaDataId(table.getId());
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
				
				size = size + fieldNameType.getName().getBytes().length * 2
						+ fieldNameType.getOriginalColumnTypeName().getBytes().length ;
			}
		}
		shutdownKettleEnvironment(database);
		
		return collectCount + "_"  + size;
	}
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月12日
	 * 作用:根据Datasource_connectinfo信息，获取表结构
	 * 
	 */
	@Transactional
	public List<Table> collectKettleJob(Datasource_connectinfo datasource_connectinfo
			,int collectjobid,Date createDate,int datasourceId,String repositoryName) throws KettleException {
		KettleDatabaseRepository kettleDatabaseRepository = connectKettleDatabaseRepository(datasource_connectinfo);
       //绑定根目录
       //RepositoryDirectoryInterface directory =kettleDatabaseRepository.loadRepositoryDirectoryTree();

       //LongObjectId是ObjectId的实现类，这里只能用LongObjectId ,用String失败Long.parseLong
       //默认绑定根目录，所有作业都在根目录下创建
       ObjectId objectId = new LongObjectId(new Long(0));
       RepositoryDirectoryInterface directory = kettleDatabaseRepository.findDirectory(objectId);
       
		MetadataTank metadataTank = new MetadataTank();
  		Metadata metadataKettleRepository = new Metadata();
  		metadataKettleRepository.setNAME(datasource_connectinfo.getDatabasename());
  		metadataKettleRepository.setCOLLECTJOBID(collectjobid);
  		metadataKettleRepository.setMETAMODELID(202);
  		metadataKettleRepository.setCREATETIME(createDate);
  		metadataKettleRepository.setUPDATETIME(createDate);
  		metadataKettleRepository.setCHECKSTATUS("1");
  		metadataKettleRepository.setVERSION(1);
		String repositoryAttributes = "{\"reposname\":\""+repositoryName
				+"\",\"reposuser\":\""+datasource_connectinfo.getRepositoryname()
				+"\",\"repospassword\":\"" + datasource_connectinfo.getRepositorypwd()
				+"\",\"dbtype\":\"" + datasource_connectinfo.getDatabasetype()
				+ "\",\"dbversion\":\"1" 
				+"\",\"dbip\":\"" + datasource_connectinfo.getUrl()
				+"\",\"dbport\":\"" + datasource_connectinfo.getPort()
				+"\",\"dbuser\":\"" + datasource_connectinfo.getUsername()
				+"\",\"dbpassword\":\"" + datasource_connectinfo.getPassword()
				+"\",\"dbname\":\"" + datasource_connectinfo.getDatabasename()
				+"\"}";	
		
		metadataKettleRepository.setATTRIBUTES(repositoryAttributes);
		
		if(!(iMetaDataDao.insertMetadata(metadataKettleRepository)>0 ? true:false)){//插入不成功
			throw new RuntimeException("作业资源库元数据插入失败");
		}
		
		metadataTank.setCHECKSTATUS(metadataKettleRepository.getCHECKSTATUS());
		metadataTank.setATTRIBUTES(metadataKettleRepository.getATTRIBUTES());
		metadataTank.setCREATETIME(new Date());
		metadataTank.setDESCRIPTION(metadataKettleRepository.getDESCRIPTION());
		metadataTank.setKeyid(metadataKettleRepository.getID());
		metadataTank.setMETAMODELID(metadataKettleRepository.getMETAMODELID());
		metadataTank.setNAME(metadataKettleRepository.getNAME());
		metadataTank.setUPDATETIME(new Date());
		metadataTank.setVERSION(metadataKettleRepository.getVERSION());
		metadataTank.setCOLLECTJOBID(collectjobid);
		
		if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
			throw new RuntimeException("insertMetaDataTank Error");
		}

       List<Table> tables = new ArrayList<>();      
       String[] jobNames = kettleDatabaseRepository.getJobNames(objectId, false);
      
       if(jobNames.length==0 || jobNames == null){
           return tables;
       }else{
           for(int i = 0;i<jobNames.length;i++){
               JobMeta jobMeta = kettleDatabaseRepository.loadJob(jobNames[i],directory,null,null);

	       		Metadata metadataKettleJob = new Metadata();
	       		metadataKettleJob.setNAME(datasource_connectinfo.getDatabasename());
	       		metadataKettleJob.setCOLLECTJOBID(collectjobid);
	       		metadataKettleJob.setMETAMODELID(203);
	       		metadataKettleJob.setCREATETIME(createDate);
	       		metadataKettleJob.setUPDATETIME(createDate);
	       		metadataKettleJob.setCHECKSTATUS("1");
	       		metadataKettleJob.setVERSION(1);
	    		String jobAttributes = "{\"jobname\":\""+jobNames[i]
	    				+"\",\"jobdescribe\":\"" + jobMeta.getDescription()
	    				+"\",\"reposid\":\"" + metadataKettleRepository.getID()
	    				+"\"}";	
	    		
	    		metadataKettleJob.setATTRIBUTES(jobAttributes);
	    		
				if(!(iMetaDataDao.insertMetadata(metadataKettleJob)>0 ? true:false)){//插入不成功
					throw new RuntimeException("kettle作业元数据插入失败");
				}
				
				metadataTank.setCHECKSTATUS(metadataKettleJob.getCHECKSTATUS());
				metadataTank.setATTRIBUTES(metadataKettleJob.getATTRIBUTES());
				metadataTank.setCREATETIME(new Date());
				metadataTank.setDESCRIPTION(metadataKettleJob.getDESCRIPTION());
				metadataTank.setKeyid(metadataKettleJob.getID());
				metadataTank.setMETAMODELID(metadataKettleJob.getMETAMODELID());
				metadataTank.setNAME(metadataKettleJob.getNAME());
				metadataTank.setUPDATETIME(new Date());
				metadataTank.setVERSION(metadataKettleJob.getVERSION());
				metadataTank.setCOLLECTJOBID(collectjobid);
				
				if(!(iMetadataTankDao.insertMetaDataTank(metadataTank)>0)){
					throw new RuntimeException("insertMetaDataTank Error");
				}
				
				MetaDataRelation metadataRelation = new MetaDataRelation();
				
				metadataRelation.setMetaDataId(metadataKettleRepository.getID());
				metadataRelation.setRelateMetaDataId(metadataKettleJob.getID());
				metadataRelation.setType("COMPOSITION");
				
				if(!(metaDataRelationService.insertMetaDataRelation(metadataRelation))){//插入不成功
					throw new RuntimeException("元数据关系插入失败");
				}
               
				Table table = new Table();
				table.setName(jobNames[i]);
				table.setOperationDescription(jobMeta.getDescription());
				table.setOperationName(null);
				tables.add(table);
				
//               System.out.println(jobNames[i]+" ,"+jobMeta.getDescription()+" ,"+
//                       jobMeta.getJobstatus()+" ,"+jobMeta.getCreatedDate()+" ,"+
//                       jobMeta.getModifiedDate());
               	
           }
       }

       kettleDatabaseRepository.disconnect();


       return tables;
	}
	
	 /**
   * 创建探索资源库目录连接
   *
   * @param getRepositoryParam
   * @return
   * @throws KettleException
   */
  public KettleDatabaseRepository connectKettleDatabaseRepository(Datasource_connectinfo datasource_connectinfo) throws KettleException {
      String dataBaseConType = "jdbc";
      //连接类型
      String dataBaseType = datasource_connectinfo.getDatabasetype();
      //开始连接资源库，探索资源库
      Map<String, Object> map = new HashMap();
      //初始化kettle环境
      try {
          KettleEnvironment.init();
      } catch (KettleException e) {
          e.printStackTrace();
      }
      //创建资源库对象，此时的对象还是一个空对象

      KettleDatabaseRepository kettleDatabaseRepository = new KettleDatabaseRepository();
      //创建资源库数据库对象，类似我们在spoon里面创建资源库
      DatabaseMeta dataMeta = new DatabaseMeta(GlobalMethodAndParams.DatabaseMetaName,
				datasource_connectinfo.getDatabasetype(), 
				GlobalMethodAndParams.kettleDatabaseMetaAccess_JDBC, 
				datasource_connectinfo.getUrl(),
				datasource_connectinfo.getDatabasename(),
				datasource_connectinfo.getPort(), 
				datasource_connectinfo.getUsername(), 
				datasource_connectinfo.getPassword());

      dataMeta.testConnection();
      Database database = new Database(null, dataMeta);
      try {
          database.connect();
      } catch (KettleDatabaseException e) {
          e.printStackTrace();
      }
      //id参数,名称参数，描述,资源库元对象等可以随便定义
      KettleDatabaseRepositoryMeta kettleDatabaseMeta = new KettleDatabaseRepositoryMeta("null", null, null, dataMeta);

      //给资源库赋值
      kettleDatabaseRepository.init(kettleDatabaseMeta);
      //连接资源库
      try {
          kettleDatabaseRepository.connect(datasource_connectinfo.getRepositoryname(), datasource_connectinfo.getRepositorypwd());
      } catch (KettleException e) {
          e.printStackTrace();
      }
      return kettleDatabaseRepository;
  }
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月12日
	 * 作用:根据Datasource_connectinfo信息，获取表结构
	 * 
	 */
	@Transactional
	public List<Table> getTables(Datasource_connectinfo datasource_connectinfo) throws KettleException {
		List<Table> tables = new ArrayList<>();
		database = initKettleEnvironment(datasource_connectinfo);

		String[] tablenames = database.getTablenames();//获取数据库中所有表名
		
		shutdownKettleEnvironment(database);
		for(String tablename : tablenames){
			Table table = new Table();
			table.setName(tablename);
			table.setOperationDescription(null);
			table.setOperationName(null);
			tables.add(table);
		}
		
		return tables;
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
