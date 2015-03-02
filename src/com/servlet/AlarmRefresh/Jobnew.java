package com.servlet.AlarmRefresh;

import java.sql.Connection;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.servlet.database.ConnectDatabase;
import com.servlet.database.Databasecurriculum;


public class Jobnew implements Job{
//Connection conn;
	Databasecurriculum data = new Databasecurriculum();
	ConnectDatabase con = new ConnectDatabase();
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("调度执行了");
		con.connect();
		data.updateDatabase();
	}

}
