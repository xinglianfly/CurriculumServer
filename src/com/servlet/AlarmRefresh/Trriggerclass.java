package com.servlet.AlarmRefresh;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

public class Trriggerclass {
	Connection conn;

	public Trriggerclass(Connection conn) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
	}

	public void startTriggle() {
		try {
			System.out.println("tr" + caltime());
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			Scheduler sched = schedFact.getScheduler();
			sched.start();
			JobDetail job = newJob(Jobnew.class)
					.withIdentity("myJob", "group1").build();
			Trigger trigger = newTrigger().withIdentity("myTrigger", "group1")
					// .startNow()
					.startAt(caltime())
					.withSchedule(
							simpleSchedule()
									.withIntervalInSeconds(24 * 60 * 60)
									.repeatForever()).build();
			sched.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Date caltime() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		return ca.getTime();
	}

	public void updateRank() {
		try {
			System.out.println("updat zhi xingle");
			System.out.println("tr" + caltime());
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			Scheduler sched = schedFact.getScheduler();
			sched.start();
			JobDetail job = newJob(UpdateRank.class).withIdentity("updateRank",
					"group2").build();
			Trigger trigger = newTrigger()
					.withIdentity("updateTrriger", "group2")
					// .startNow()
					.startAt(caltime())
					.withSchedule(
							simpleSchedule()
									// .withIntervalInSeconds(24 * 60 * 60)
									.withIntervalInSeconds(24 * 60 * 60)
									.repeatForever()).build();
			sched.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
