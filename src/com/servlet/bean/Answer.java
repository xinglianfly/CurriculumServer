package com.servlet.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Answer implements Serializable {

	String userid;
	String answer;
	int ansid;
	Timestamp time;
	int quesid;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "Answer [userid=" + userid + ", answer=" + answer + ", ansid="
				+ ansid + ", time=" + time + ", quesid=" + quesid + "]";
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getAnsid() {
		return ansid;
	}

	public void setAnsid(int ansid) {
		this.ansid = ansid;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getQuesid() {
		return quesid;
	}

	public void setQuesid(int quesid) {
		this.quesid = quesid;
	}
}
