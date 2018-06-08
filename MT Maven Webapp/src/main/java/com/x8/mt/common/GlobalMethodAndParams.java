package com.x8.mt.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class GlobalMethodAndParams {
	
	/*
	 * 打开wsdlService通知
	 */
	public static final String wsdlServiceSwitch_CLOSE= "CLOSE";
	
	/**
	 * 通信协议操作类型
	 */
	public static final String protocolOperationType_DELATE= "DELATE";
	public static final String protocolOperationType_UPDATE= "UPDATE";
	public static final String protocolOperationType_INSERT= "INSERT";
	
	/**
	 * 通信协议私有属性名称
	 */
	public static final String protocolId= "protocolId";
	public static final String protocolName= "protocolName";
	public static final String protocolType= "protocolType";
	
	public static final int protocolMetamodelID= 1001;
	public static final int protocolParamArrayMetamodelID= 1002;
	public static final int protocolParamMetamodelID= 1003;

	public static final String JSONKey_protocolType= "$.protocolType";
	public static final String JSONKey_protocolId= "$.protocolId";
	
	/**
	 * 字段编号,字段名
	 */
	public static final String JSONKey_sourcefieldid= "$.sourcefieldid";
	public static final String JSONKey_targetfieldid= "$.targetfieldid";
	
	/**
	 * 组合关系名
	 */
	public static final String COMPOSITION= "COMPOSITION";
	
	/**
	 * 公共元模型的属性名
	 */
	public static final String Public_Metamodel_ATTRIBUTES= "ATTRIBUTES";
	public static final String Public_Metamodel_COLLECTJOBID= "COLLECTJOBID";
	public static final String Public_Metamodel_METAMODELID= "METAMODELID";
	public static final String Public_Metamodel_CHECKSTATUS= "CHECKSTATUS";
	public static final String Public_Metamodel_VERSION= "VERSION";
	public static final String Public_Metamodel_UPDATETIME= "UPDATETIME";
	public static final String Public_Metamodel_CREATETIME= "CREATETIME";
	public static final String Public_Metamodel_DESRIBE= "DESRIBE";
	public static final String Public_Metamodel_NAME= "NAME";
	public static final String Public_Metamodel_ID= "ID";
	
	/**
	 * 元数据视图节点类型type
	 */
	public static final String MetaDataViewNodeTypeCATEGORY= "CATEGORY";
	
	/**
	 * 元数据视图节点类型type
	 */
	public static final String MetaDataViewNodeTypeMETAMODEL= "METAMODEL";
	
	/**
	 * 私有元模型SQL字段名
	 */
	public static final String Attribute_metamodelid_Name= "attribute_metamodelid";
	
	/**
	 * 公共元模型SQL字段名
	 */
	public static final String Public_metamodelid_Name= "public_metamodelid";
	
	/**
	 * 公共元模型,metamodelid
	 */
	public static final String PublicMetamodelId= "2";
	
	/**
	 * 元数据字段映射对应元模型，metamodelid
	 */
	public static final String MetaDataMappingModelId= "100";
	
	/**
	 * 元数据表映射对应元模型，metamodelid
	 */
	public static final String MetaDataTableMappingModelId= "99";
	
	/**
	 * 系统元模型id
	 */
	public static final Integer SystemMedamodelId_InDatabase= 3;
	
	/**
	 * 数据库元模型id
	 */
	public static final Integer DatabaseMedamodelId_InDatabase= 10;
	
	/**
	 * 表元模型id
	 */
	public static final String TableMedamodelId_InDatabase= "31";
	
	/**
	 * 字段元模型id
	 */
	public static final String FieldMetamodelId= "32";
	
	/**
	 * 数据库元模型id
	 */
	public static final String DatabaseMetamodelId= "10";
	
	/**
	 * 元数据特有数据项字段名
	 */
	public static final String Metadata_Attributes= "ATTRIBUTES";
	
	/**
	 * 获取元数据视图树所需要的数据库表字段名
	 */
	public static final String MetadataViewTree_id= "id";
	public static final String MetadataViewTree_label= "label";
	public static final String MetadataViewTree_children= "children";
	public static final String MetadataViewTree_metamodelid= "metamodelid";
	
	public static final String Metadata_package_id= "package_id";
	public static final String Metadata_package_name= "package_name";
	public static final String Metadata_metadata_id= "metadata_id";
	public static final String Metadata_metadata_name= "metadata_name";
	public static final String Metadata_metadata_metamodelid= "metadata_metamodelid";
	
	/**
	 * DatabaseMeta名字
	 */
	public static final String DatabaseMetaName= "DatabaseMetaName";
	
	/**
	 * 层次类型为元模型
	 */
	public static final String metamodel_hierarchy_METAMODEL= "METAMODEL";
	
	/**
	 * 元模型依赖关系
	 */
	public static final String metamodel_relation_dependency= "DEPENDENCY";
	
	/**
	 * 元模型组合关系
	 */
	public static final String metamodel_relation_composition= "COMPOSITION";
	
	/**
	 * 层次类型为包
	 */
	public static final String metamodel_hierarchy_PACKAGE= "PACKAGE";
	
	/**
	 * 目标系统（即数据仓库里的系统）
	 */
	public static final String sourcesystemtype_TARGETSYSTEM= "targetsystem";
	
	/**
	 * 源系统（即各种装备系统）
	 */
	public static final String sourcesystemtype_SRCSYSTEM= "srcsystem";
	
	/**
	 * etl（即kettle数据集成）
	 */
	public static final String sourcesystemtype_ETL= "etl";
	
	/**
	 * 数据库类型mysql
	 */
	public static final String databasetype_MYSQL= "mysql";
	
	/**
	 * 数据库类型postgresql
	 */
	public static final String databasetype_POSTGRESQL= "postgresql";
	
	/**
	 * 数据库类型oracle
	 */
	public static final String databasetype_ORACLE= "oracle";
	
	/**
	 * 数据库类型oracle
	 */
	public static final String databasetype_SQLSERVER= "mssql";
	
	/**
	 * kettle连接方式
	 */
	public static final String kettleDatabaseMetaAccess_JDBC = "jdbc";
	
	/**
	 * 日志结果:exception
	 */
	public static final String systemlogResult_exception = "exception";
	
	/**
	 * 自定义元模型包数据库id
	 */
	public static final int selfMetamodelPackageId = 1000;
	
	/**
	 * 系统元模型包数据库id
	 */
	public static final int systemMetamodelPackageId = 1;
	
	/**
	 * 表输入名称
	 */
	public static final String TABLEINPUT = "tableInput";
	
	/**
	 * 表输出名称
	 */
	public static final String TABLEOUTPUT = "tableOutput";
	
	/**
	 * 转换名称
	 */
	public static final String TRANS_NAME = "trans.ktr";
	
	/**
	 * 所需采集元数据的文件存放路径
	 */
	public static final String PATH_NAME = "C:/data/";
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月29日
	 * 作用:设置跨域返回头
	 */
	public static final void setHttpServletResponse(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE"); 
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Credentials","true");
	}
	
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月29日
	 * 作用:检查用户是否登录
	 */
	public static boolean checkLogin(){ 
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {  
			return false;  
		}
		return true;  
	}
}
