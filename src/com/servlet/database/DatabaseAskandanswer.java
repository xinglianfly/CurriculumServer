package com.servlet.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.servlet.bean.Answer;
import com.servlet.bean.Question;

public class DatabaseAskandanswer {
//	Connection conn;
	Statement state = null;
	DataSource ds;
	public DatabaseAskandanswer(DataSource ds) {
//		ConnectDatabase con = new ConnectDatabase();
//		conn = con.conn;
		this.ds=ds;

	}
/*
 * 插入问题
 */
	public void insertAsk(String userid, String question, String brief) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			System.out.println("zhixing le charu");
			PreparedStatement insertaskstate = conn
					.prepareStatement("INSERT INTO ask(stuid,question,briefques,answernum)"
							+ "VALUES(?,?,?,?)");
			insertaskstate.setString(1, userid);
			insertaskstate.setString(2, question);
			insertaskstate.setString(3, brief);
			insertaskstate.setInt(4, 0);
			insertaskstate.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
/*
 * 插入回答
 */
	public void insertAnswer(String userid, String answer, int questionid) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			PreparedStatement insertanswerstate = conn
					.prepareStatement("INSERT INTO answer(userid,quesid,answer)VALUES(?,?,?)");
			insertanswerstate.setString(1, userid);
			insertanswerstate.setInt(2, questionid);
			insertanswerstate.setString(3, answer);
			insertanswerstate.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
/*
 * 插入回答的个数
 */
	public void insertAnswernum(int quesid) {
		ResultSet rs;
		int numans = 0;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			rs = state.executeQuery("select * from ask where queid = '"
					+ quesid + "'");
			while (rs.next()) {
				numans = rs.getInt("answernum");
				++numans;
			}
			state = conn.createStatement();
			state.executeUpdate("update ask set answernum = '" + numans
					+ "'where queid = '" + quesid + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//	public int getAnswerSize(int quesid) {
//		ResultSet rs;
//		int size = 0;
//		try {
//			state = conn.createStatement();
//			rs = state.executeQuery("select * from answer where quesid = '"
//					+ quesid + "'");
//			while (rs.next()) {
//				size++;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return size;
//	}

//	public ArrayList<Question> getQues() {
//		ResultSet rsques, rsans;
//		ArrayList<Question> queslist = new ArrayList<Question>();
//		try {
//			state = conn.createStatement();
//			rsques = state.executeQuery("select* from ask");
//			while (rsques.next()) {
//				Question ques = new Question();
//				ques.setUserid(rsques.getString("stuid"));
//				ques.setTime(rsques.getTimestamp("questime"));
//				ques.setQuestion(rsques.getString("question"));
//				ques.setQuesid(rsques.getInt("queid"));
//				ques.setBrief(rsques.getString("briefques"));
//				ArrayList<Answer> anslist = new ArrayList<Answer>();
//				state = conn.createStatement();
//				rsans = state
//						.executeQuery("select * from answer where quesid = '"
//								+ ques.getQuesid() + "'");
//				while (rsans.next()) {
//					Answer ans = new Answer();
//					ans.setAnsid(rsans.getInt("ansid"));
//					ans.setAnswer(rsans.getString("answer"));
//					ans.setQuesid(rsans.getInt("quesid"));
//					ans.setTime(rsans.getTimestamp("answertime"));
//					ans.setUserid(rsans.getString("userid"));
//					anslist.add(ans);
//				}
//				ques.setAns(anslist);
//				queslist.add(ques);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return queslist;
//
//	}
/*
 * 下拉刷新
 */
	public ArrayList<Question> getDownrefreshQuestion(int questionid) {
		ResultSet rsques, rsans, numrs;
		int i;
		ArrayList<Question> queslist = new ArrayList<Question>();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			numrs = state
					.executeQuery("select count(*) from ask where queid > '"
							+ questionid + "'");
			numrs.next();
			i = numrs.getInt(1);
			state = conn.createStatement();
			ResultSet all = state.executeQuery("select count(*) from ask ");
			all.next();
			int allnum = all.getInt(1);
			state = conn.createStatement();
			if (allnum <= 4) {
				rsques = state.executeQuery("select* from ask");
				while (rsques.next()) {
					Question ques = new Question();
					ques.setUserid(rsques.getString("stuid"));
					ques.setTime(rsques.getTimestamp("questime"));
					ques.setQuestion(rsques.getString("question"));
					ques.setQuesid(rsques.getInt("queid"));
					ques.setBrief(rsques.getString("briefques"));
					ques.setAnswernum(rsques.getInt("answernum"));
					ArrayList<Answer> anslist = new ArrayList<Answer>();
					state = conn.createStatement();
					rsans = state
							.executeQuery("select * from answer where quesid = '"
									+ ques.getQuesid() + "'");
					while (rsans.next()) {
						Answer ans = new Answer();
						ans.setAnsid(rsans.getInt("ansid"));
						ans.setAnswer(rsans.getString("answer"));
						ans.setQuesid(rsans.getInt("quesid"));
						ans.setTime(rsans.getTimestamp("answertime"));
						ans.setUserid(rsans.getString("userid"));
						anslist.add(ans);
					}
					ques.setAns(anslist);
					queslist.add(ques);
				}
			} else {
				System.out.println("zhi xing le else");
				rsques = state
						.executeQuery("select * from(select * from ask order by queid desc limit 4) as tt order by queid");
				while (rsques.next()) {
					Question ques = new Question();
					ques.setUserid(rsques.getString("stuid"));
					ques.setTime(rsques.getTimestamp("questime"));
					ques.setQuestion(rsques.getString("question"));
					ques.setQuesid(rsques.getInt("queid"));
					ques.setBrief(rsques.getString("briefques"));
					ques.setAnswernum(rsques.getInt("answernum"));
					ArrayList<Answer> anslist = new ArrayList<Answer>();
					state = conn.createStatement();
					rsans = state
							.executeQuery("select * from answer where quesid = '"
									+ ques.getQuesid()
									+ "'order by ansid desc");//
					while (rsans.next()) {
						Answer ans = new Answer();
						ans.setAnsid(rsans.getInt("ansid"));
						ans.setAnswer(rsans.getString("answer"));
						ans.setQuesid(rsans.getInt("quesid"));
						ans.setTime(rsans.getTimestamp("answertime"));
						ans.setUserid(rsans.getString("userid"));
						anslist.add(ans);
					}
					ques.setAns(anslist);
					queslist.add(ques);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return queslist;

	}

	/*
	 * 上拉加载更多
	 */
	public ArrayList<Question> uploadmore(int questionid) {
		ResultSet rsques, rsans, numrs;
		int i;
		ArrayList<Question> queslist = new ArrayList<Question>();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			numrs = state
					.executeQuery("select count(*) from ask where queid < '"
							+ questionid + "'");
			numrs.next();
			i = numrs.getInt(1);
			state = conn.createStatement();
			ResultSet all = state.executeQuery("select count(*) from ask where queid < '"
							+ questionid + "'");
			all.next();
			int allnum = all.getInt(1);
			state = conn.createStatement();
			if (allnum <= 4) {
				rsques = state
						.executeQuery("select* from ask where queid < '"
							+ questionid + "'order by queid desc");
				while (rsques.next()) {
					Question ques = new Question();
					ques.setUserid(rsques.getString("stuid"));
					ques.setTime(rsques.getTimestamp("questime"));
					ques.setQuestion(rsques.getString("question"));
					ques.setQuesid(rsques.getInt("queid"));
					ques.setBrief(rsques.getString("briefques"));
					ques.setAnswernum(rsques.getInt("answernum"));
					ArrayList<Answer> anslist = new ArrayList<Answer>();
					state = conn.createStatement();
					rsans = state
							.executeQuery("select * from answer where quesid = '"
									+ ques.getQuesid() + "' order by ansid desc");
					while (rsans.next()) {
						Answer ans = new Answer();
						ans.setAnsid(rsans.getInt("ansid"));
						ans.setAnswer(rsans.getString("answer"));
						ans.setQuesid(rsans.getInt("quesid"));
						ans.setTime(rsans.getTimestamp("answertime"));
						ans.setUserid(rsans.getString("userid"));
						anslist.add(ans);
					}
					ques.setAns(anslist);
					queslist.add(ques);
				}
			} else {
				System.out.println("zhi xing le else");
				rsques = state
						.executeQuery("select * from ask order by queid desc limit 4");
				while (rsques.next()) {
					Question ques = new Question();
					ques.setUserid(rsques.getString("stuid"));
					ques.setTime(rsques.getTimestamp("questime"));
					ques.setQuestion(rsques.getString("question"));
					ques.setQuesid(rsques.getInt("queid"));
					ques.setBrief(rsques.getString("briefques"));
					ques.setAnswernum(rsques.getInt("answernum"));
					ArrayList<Answer> anslist = new ArrayList<Answer>();
					state = conn.createStatement();
					rsans = state
							.executeQuery("select * from answer where quesid = '"
									+ ques.getQuesid()
									+ "'order by quesid desc");
					while (rsans.next()) {
						Answer ans = new Answer();
						ans.setAnsid(rsans.getInt("ansid"));
						ans.setAnswer(rsans.getString("answer"));
						ans.setQuesid(rsans.getInt("quesid"));
						ans.setTime(rsans.getTimestamp("answertime"));
						ans.setUserid(rsans.getString("userid"));
						anslist.add(ans);
					}
					ques.setAns(anslist);
					queslist.add(ques);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return queslist;
	}
}
