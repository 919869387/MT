package com.x8.mt.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class GlobalMethodAndParams {
	
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
	 * 表元模型id
	 */
	public static final String TableMedamodelId_InDatabase= "31";
	
	/**
	 * 字段元模型id
	 */
	public static final String FieldMetamodelId= "32";
	
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
	 * 
	 * 作者:allen
	 * 时间:2017年11月29日
	 * 作用:设置跨域返回头
	 */
	public static final void setHttpServletResponse(HttpServletRequest request,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed","1");
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
