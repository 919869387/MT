package com.x8.mt.service;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.ObjectLocationSpecificationMethod;
import org.pentaho.di.core.SQLStatement;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleFileException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobHopMeta;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.special.JobEntrySpecial;
import org.pentaho.di.job.entries.sql.JobEntrySQL;
import org.pentaho.di.job.entries.trans.JobEntryTrans;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.RowListener;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;
import org.pentaho.di.trans.steps.tableoutput.TableOutputMeta;
import org.springframework.stereotype.Service;

import com.x8.mt.common.GlobalMethodAndParams;
import com.x8.mt.entity.Datasource_connectinfo;
import com.x8.mt.entity.ETLJob;
import com.x8.mt.entity.ETLJobParam;


@Service
public class TransDataService {

	/**
	 * 
	 * 作者:GodDispose
	 * 时间:2017年12月12日
	 * 作用:动态生成转换
	 * @throws KettleException 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public void generateTransMeta(ETLJobParam job) throws KettleException, UnsupportedEncodingException, IOException{
		KettleEnvironment.init();
		TransMeta transMeta = new TransMeta();
		transMeta.setName("transMeta");
		
//		DatabaseMeta source = new DatabaseMeta("test","MySQL","JDBC","localhost",
//				"test","3306","root","root");
//		DatabaseMeta target = new DatabaseMeta("test1","MySQL","JDBC","116.62.196.234",
//				"test1","3306","root","root");
		DatabaseMeta source = new DatabaseMeta(job.getSource().getDatabasename(),job.getSource().getDatabasetype(),"JDBC",job.getSource().getUrl(),
				job.getSource().getDatabasename(),job.getSource().getPort(),job.getSource().getUsername(),job.getSource().getPassword());
		DatabaseMeta target = new DatabaseMeta(job.getTarget().getDatabasename(),job.getTarget().getDatabasetype(),"JDBC",job.getTarget().getUrl(),
				job.getTarget().getDatabasename(),job.getTarget().getPort(),job.getTarget().getUsername(),job.getTarget().getPassword());
		transMeta.addDatabase(source);
		transMeta.addDatabase(target);
		
		//String sql = "SELECT ID,NAME FROM database_type";
		
		
//		String sourcefields=job.getFieldStream()[0];
//		for(int i=1;i<job.getFieldStream().length;i++){
//			sourcefields=sourcefields+","+job.getFieldStream()[i];
//		}
		String sql = "SELECT "+job.getFieldSteram()+" FROM "+job.getSource_table() ;
		TableInputMeta tableInputMeta = new TableInputMeta();
		tableInputMeta.setDefault();
		tableInputMeta.setDatabaseMeta(source);
		tableInputMeta.setSQL(sql);
		
		StepMeta tableInputStep = new StepMeta(GlobalMethodAndParams.TABLEINPUT,tableInputMeta);
		tableInputStep.setLocation(50, 50);
		tableInputStep.setDraw(true);
		transMeta.addStep(tableInputStep);
		
		TableOutputMeta tableOutputMeta = new TableOutputMeta();
		tableOutputMeta.setDefault();
		tableOutputMeta.setDatabaseMeta(target);
		tableOutputMeta.setSpecifyFields(true);
		tableOutputMeta.setTruncateTable(true);
		//String tableName = "test";
		String tableName = job.getTarget_table();
		tableOutputMeta.setTableName(tableName);
		//String[] fieldStream = {"id","name"};
		
		String[] fieldStream =job.getFieldSteram().split(",");
		tableOutputMeta.setFieldStream(fieldStream);
		
		//String[] fieldDatabase = {"iid","iname"};
		String[] fieldDatabase = job.getFieldDatabase().split(",");
		tableOutputMeta.setFieldDatabase(fieldDatabase);


		StepMeta tableOutputStep = new StepMeta(GlobalMethodAndParams.TABLEOUTPUT,tableOutputMeta);
		tableOutputStep.setLocation(150, 50);
		tableOutputStep.setDraw(true);
		transMeta.addStep(tableOutputStep);
		
		
		
		TransHopMeta hop = new TransHopMeta(tableInputStep,tableOutputStep);
		hop.setEnabled();
		transMeta.addTransHop(hop);
		
//		//添加表输出步骤监听器来监听抽取行数。
//		RowListener rowListener = new RowListener(){
//			public void rowWrittenEvent(RowMetaInterface rowMeta, Object[] row)throws KettleStepException{
//				
//			}
//
//			@Override
//			public void errorRowWrittenEvent(RowMetaInterface arg0,
//					Object[] arg1) throws KettleStepException {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void rowReadEvent(RowMetaInterface arg0, Object[] arg1)
//					throws KettleStepException {
//				// TODO Auto-generated method stub
//				
//			}
//		};
		
		File file = new File(tableName + ".ktr");
		 if (file.exists() && file.isFile()) {
	         if (file.delete()) {}
		 }
		
		String xml = XMLHandler.getXMLHeader() + transMeta.getXML();
		DataOutputStream dos = new DataOutputStream(KettleVFS.getOutputStream(tableName+ ".ktr",true));
		dos.write(xml.getBytes(Const.XML_ENCODING));
		dos.close();
		
		KettleEnvironment.shutdown();
	}
	
	/*
	 * 作者:GodDispose
	 * 时间:2018年4月19日
	 * 作用:动态生成作业，并返回
	 */
	public boolean saveJob(ETLJobParam job) throws Exception{
		try{			
			KettleEnvironment.init();
			generateTransMeta(job);
			JobMeta jobMeta = new JobMeta();
			jobMeta.setName("jobMeta");
			int x=50,y =50;
			
			JobEntrySpecial jobEntrySpecial = new JobEntrySpecial( "START", true, false );			
			
		    JobEntryCopy jobEntry = new JobEntryCopy();
		    jobEntry.setObjectId( null );
		    jobEntry.setEntry( jobEntrySpecial );
		    jobEntry.setLocation( 50, 50 );
		    jobEntry.setDrawn( false );
		    jobEntry.setDescription( BaseMessages.getString( JobMeta.class, "JobMeta.StartJobEntry.Description" ) );
		    
			JobEntryCopy startCopy = jobEntry;
			startCopy.setLocation(x, y);
			startCopy.setDrawn();
			jobMeta.addJobEntry(startCopy);
			JobEntryCopy lastCopy = startCopy;
			
			JobEntryTrans jobEntryTrans = new JobEntryTrans();
			jobEntryTrans.setSpecificationMethod(ObjectLocationSpecificationMethod.FILENAME);
			jobEntryTrans.setFileName(job.getTarget_table()+ ".ktr");
			
			JobEntryCopy transCopy = new JobEntryCopy(jobEntryTrans);
			transCopy.setName("Execute" + job.getTarget_table()+ ".ktr");
			x+=100;
			transCopy.setLocation(x, y);
			transCopy.setDrawn();
			jobMeta.addJobEntry(transCopy);
			JobHopMeta transHop = new JobHopMeta(lastCopy,transCopy);
			jobMeta.addJobHop(transHop);
			lastCopy = transCopy;
			jobMeta.setObjectId(new LongObjectId(new Long(1)));
			
			String jobName = job.getTarget_table()+ ".kjb";
			File file = new File(jobName);
			if (file.exists() && file.isFile()) {
				if (file.delete()) {}
			}
			
			String xml = XMLHandler.getXMLHeader() + jobMeta.getXML();
			DataOutputStream dos = new DataOutputStream(KettleVFS.getOutputStream(jobName,true));
			dos.write(xml.getBytes(Const.XML_ENCODING));
			dos.close();
			
			KettleEnvironment.shutdown();
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return  false;
		}
		
	}
}
