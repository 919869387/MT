package com.x8.mt.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.entity.File_connectinfo;

@Repository
public interface IFile_connectinfoDao {

	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月8日 
	 * 作用:更新一条File_connectinfo记录
	 */
	int update(File_connectinfo file_connectinfo);

	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月8日 
	 * 作用:插入一条File_connectinfo记录
	 */
	int insert(File_connectinfo file_connectinfo);

	/**
	 * 
	 * 作者:GodDispose 
	 * 时间:2018年5月8日 
	 * 作用:删除一条File_connectinfo记录
	 */
	int delete(int id);

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月8日
	 * 作用:根据parentid得到所有相应File_connectinfo
	 */
	File_connectinfo getFile_connectinfoListByparentid(int parentid);
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月8日
	 * 作用:根据id得到一条File_connectinfo记录
	 */
	File_connectinfo getFile_connectinfoByid(int id);
	
	
	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2018年5月8日
	 * 作用:得到所有File_connectinfo
	 */
	List<File_connectinfo> getFile_connectinfo();

	

}
