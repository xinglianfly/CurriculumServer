package com.servlet.AlarmRefresh;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.servlet.database.DatabaseRank;

public class UpdateRank implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		DatabaseRank rank = new DatabaseRank();
		rank.updatecontinusign();
		rank.updateifSign();
	}

}
