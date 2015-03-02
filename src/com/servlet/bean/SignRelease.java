package com.servlet.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class SignRelease implements Serializable {
	String name;
	String releaserank;
	Timestamp time;
	int zan;
	int rankid;
	int numcomment;
	ArrayList<SignComment> commentlist;

	public ArrayList<SignComment> getCommentlist() {
		return commentlist;
	}

	public void setCommentlist(ArrayList<SignComment> commentlist) {
		this.commentlist = commentlist;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReleaserank() {
		return releaserank;
	}

	public void setReleaserank(String releaserank) {
		this.releaserank = releaserank;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getZan() {
		return zan;
	}

	public void setZan(int zan) {
		this.zan = zan;
	}

	public int getRankid() {
		return rankid;
	}

	public void setRankid(int rankid) {
		this.rankid = rankid;
	}

	public int getNumcomment() {
		return numcomment;
	}

	public void setNumcomment(int numcomment) {
		this.numcomment = numcomment;
	}

}
