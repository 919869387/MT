/**
 * 
 */
package com.x8.mt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.x8.mt.entity.ETLJob;

/**
 * @author Tomcroods
 *
 */
@Repository
public interface IETLJobDao {

	boolean insert(ETLJob job);
	
	boolean update(ETLJob job);
	
	int getRowCount();
	
	List<ETLJob> selectByParams(Map<String, Object> params);
	
	int deleteETLJob(int id);
	
	int deleteETLJobs(int[] id);
	
	List<Integer> queryTargetTableIdAndName();
	
	List<Integer> queryTargetTableId();
}
