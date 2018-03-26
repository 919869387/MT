package com.x8.mt.common;


import org.pentaho.di.core.Const;
import org.pentaho.di.core.logging.BaseLogTable;
import org.pentaho.di.core.logging.LogStatus;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.job.Job;
import org.pentaho.di.trans.HasDatabasesInterface;

/**
 * Created by Joker on 2017/11/28.
 */
public class LogUtil extends BaseLogTable{

    public LogUtil(VariableSpace space, HasDatabasesInterface databasesInterface){
        super(space,databasesInterface,null,null,null);
    }

    @Override
    public String getLogTableCode() {
        return null;
    }

    @Override
    public String getConnectionNameVariable() {
        return null;
    }

    @Override
    public String getSchemaNameVariable() {
        return null;
    }

    @Override
    public String getTableNameVariable() {
        return null;
    }

    public String getJobLog(Job job) {
        return this.getLogBuffer(job,job.getLogChannelId(), LogStatus.END,job.getVariable(Const.KETTLE_LOG_SIZE_LIMIT, null));
    }

}
