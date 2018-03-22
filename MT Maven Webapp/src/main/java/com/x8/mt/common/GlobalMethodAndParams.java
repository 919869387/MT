package com.x8.mt.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalMethodAndParams {
	
	/**
	 * 数据库中表元模型id
	 */
	public static String TableMedamodelId_InDatabase= "31";
	
	/**
	 * 获取元数据视图树所需要的数据库表字段名
	 */
	public static String MetadataViewTree_id= "id";
	public static String MetadataViewTree_label= "label";
	public static String MetadataViewTree_children= "children";
	public static String MetadataViewTree_metamodelid= "metamodelid";
	
	public static String Metadata_package_id= "package_id";
	public static String Metadata_package_name= "package_name";
	public static String Metadata_metadata_id= "metadata_id";
	public static String Metadata_metadata_name= "metadata_name";
	public static String Metadata_metadata_metamodelid= "metadata_metamodelid";
	
	/**
	 * DatabaseMeta名字
	 */
	public static String DatabaseMetaName= "DatabaseMetaName";
	
	/**
	 * 层次类型为元模型
	 */
	public static String metamodel_hierarchy_METAMODEL= "METAMODEL";
	
	/**
	 * 层次类型为包
	 */
	public static String metamodel_hierarchy_PACKAGE= "PACKAGE";
	
	/**
	 * 目标系统（即数据仓库里的系统）
	 */
	public static String sourcesystemtype_TARGETSYSTEM= "targetsystem";
	
	/**
	 * 源系统（即各种装备系统）
	 */
	public static String sourcesystemtype_SRCSYSTEM= "srcsystem";
	
	/**
	 * etl（即kettle数据集成）
	 */
	public static String sourcesystemtype_ETL= "etl";
	
	/**
	 * 数据库类型mysql
	 */
	public static String databasetype_MYSQL= "mysql";
	
	/**
	 * 数据库类型postgresql
	 */
	public static String databasetype_POSTGRESQL= "postgresql";
	
	/**
	 * 数据库类型oracle
	 */
	public static String databasetype_ORACLE= "oracle";
	
	/**
	 * kettle连接方式
	 */
	public static String kettleDatabaseMetaAccess_JDBC = "jdbc";
	
	/**
	 * 日志结果:exception
	 */
	public static String systemlogResult_exception = "exception";
	
	/**
	 * 自定义元模型包数据库id
	 */
	public static int selfMetamodelPackageId = 20;
	
	/**
	 * 系统元模型包数据库id
	 */
	public static int systemMetamodelPackageId = 9;
	
	/**
	 * 表输入名称
	 */
	public static String TABLEINPUT = "tableInput";
	
	/**
	 * 表输出名称
	 */
	public static String TABLEOUTPUT = "tableOutput";
	
	/**
	 * 转换名称
	 */
	public static String TRANS_NAME = "trans.ktr";
	
	/**
	 * 
	 * 作者:allen
	 * 时间:2017年11月29日
	 * 作用:设置跨域返回头
	 */
	public static void setHttpServletResponse(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed","1");
	}
	
}
