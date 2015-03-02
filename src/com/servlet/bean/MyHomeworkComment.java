package com.servlet.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class MyHomeworkComment implements Serializable {

	@Override
	public String toString() {
		return "MyHomeworkComment [comment=" + comment + ", name=" + name
				+ ", time=" + time + ", homeworkid=" + homeworkid
				+ ", commentid=" + commentid + "]";
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getHomeworkid() {
		return homeworkid;
	}

	public void setHomeworkid(int homeworkid) {
		this.homeworkid = homeworkid;
	}

	public int getCommentid() {
		return commentid;
	}

	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}

	private String comment;
	private String name;
	private Timestamp time;
	private int homeworkid;
	private int commentid;
}
