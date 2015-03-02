package com.servlet.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.catalina.startup.HomesUserDatabase;

import com.servlet.bean.MyHomework;
import com.servlet.bean.MyHomeworkComment;
import com.servlet.bean.SignComment;
import com.servlet.bean.SignRelease;

public class DatabaseReleaseHomework {
	// Connection conn;
	Statement state = null;
	PreparedStatement pre;
	DataSource ds;

	public DatabaseReleaseHomework(DataSource ds) {
		// ConnectDatabase con = new ConnectDatabase();
		// conn = con.conn;
		this.ds = ds;
	}

	/*
	 * 插入作业的内容
	 */
	public void insertTask(String username, String subject, String deadline,
			String homework) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			pre = conn
					.prepareStatement("insert into homework (username,subject,deadline,homework)values(?,?,?,?)");
			pre.setString(1, username);
			pre.setString(2, subject);
			pre.setString(3, deadline);
			pre.setString(4, homework);
			pre.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * 得到个人的作业信息
	 */
	public ArrayList<MyHomework> getPersonHomework(String userid) {
		ResultSet rs;
		ArrayList<MyHomework> homelist = null;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			homelist = new ArrayList<MyHomework>();
			state = conn.createStatement();
			rs = state.executeQuery("select * from homework where username = '"
					+ userid + "'");
			while (rs.next()) {
				MyHomework home = new MyHomework();
				home.setDeadline(rs.getString("deadline"));
				home.setSubject(rs.getString("subject"));
				home.setHomework(rs.getString("homework"));
				home.setHomeworkid(rs.getInt("number"));
				homelist.add(home);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return homelist;
	}

	/**
	 * 这个方法也暂时没用找
	 * 
	 * @param homeworkid
	 * @return
	 */
	public ArrayList<MyHomework> getHomework(int homeworkid) {
		int i = 0;
		ResultSet numrs, rs, rscom;
		ArrayList<MyHomework> homelist20 = new ArrayList<MyHomework>();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			numrs = state
					.executeQuery("select count(*) from homework where num > '"
							+ homeworkid + "'");
			numrs.next();
			i = numrs.getInt(1);
			state = conn.createStatement();
			ResultSet all = state
					.executeQuery("select count(*) from homework ");
			all.next();
			int allnum = all.getInt(1);
			if (allnum <= 4) {
				System.out.println("zhi xingl eif");
				rs = state.executeQuery("select * from homework");// order by
																	// num desc
				while (rs.next()) {
					MyHomework home = new MyHomework();
					home.setDeadline(rs.getString("deadline"));
					home.setHomework(rs.getString("homework"));
					home.setName(rs.getString("username"));
					home.setReleasePerson(rs.getString("releaseperson"));
					home.setSubject(rs.getString("subject"));
					home.setCommentnum(rs.getInt("commentnum"));
					state = conn.createStatement();
					rscom = state
							.executeQuery("select * from homeworkcomment where homeworkid = '"
									+ rs.getInt("number") + "' ");
					ArrayList<MyHomeworkComment> homecomlist = new ArrayList<MyHomeworkComment>();
					while (rscom.next()) {
						MyHomeworkComment homecom = new MyHomeworkComment();
						homecom.setComment(rscom.getString("comment"));
						homecom.setTime(rscom.getTimestamp("time"));
						homecom.setName(rscom.getString("username"));
						homecom.setHomeworkid(rscom.getInt("homeworkid"));
						homecom.setCommentid(rscom.getInt("num"));
						homecomlist.add(homecom);
					}
					home.setHomeworklist(homecomlist);
					homelist20.add(home);
				}
			} else {
				System.out.println("zhi xing le else");
				rs = state
						.executeQuery("select * from(select * from curriculum.homework order by number desc limit 4) as tt order by number");
				while (rs.next()) {
					MyHomework home = new MyHomework();
					home.setDeadline(rs.getString("deadline"));
					home.setHomework(rs.getString("homework"));
					home.setName(rs.getString("username"));
					home.setReleasePerson(rs.getString("releaseperson"));
					home.setSubject(rs.getString("subject"));
					home.setCommentnum(rs.getInt("commentnum"));
					state = conn.createStatement();
					rscom = state
							.executeQuery("select * from homeworkcomment where homeworkid = '"
									+ rs.getInt("number") + "' ");
					ArrayList<MyHomeworkComment> homecomlist = new ArrayList<MyHomeworkComment>();
					while (rscom.next()) {
						MyHomeworkComment homecom = new MyHomeworkComment();
						homecom.setComment(rscom.getString("comment"));
						homecom.setTime(rscom.getTimestamp("time"));
						homecom.setName(rscom.getString("username"));
						homecom.setHomeworkid(rscom.getInt("homeworkid"));
						homecom.setCommentid(rscom.getInt("num"));
						homecomlist.add(homecom);
					}
					home.setHomeworklist(homecomlist);
					homelist20.add(home);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return homelist20;
	}

	/**
	 * 这个方法也暂时没用着
	 * 
	 * @param homeworkid
	 */
	public void insertcomment(int homeworkid) {
		ResultSet rs;
		int numcomment = 0;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			rs = state
					.executeQuery("select * from homework where commentnum = '"
							+ homeworkid + "'");
			while (rs.next()) {
				numcomment = rs.getInt("commentnum");
				++numcomment;
			}
			state = conn.createStatement();
			state.executeUpdate("update homework set commentnum = '"
					+ numcomment + "'where number = '" + homeworkid + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 这个方法暂时没用着
	 * 
	 * @param homeworkId
	 * @return
	 */
	public ArrayList<MyHomework> getdownhomework(int homeworkId) {

		int i = 0;
		ResultSet numrs, rs, rscom;
		ArrayList<MyHomework> homelist20 = new ArrayList<MyHomework>();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			numrs = state
					.executeQuery("select count(*) from homework where number < '"
							+ homeworkId + "'");
			numrs.next();
			i = numrs.getInt(1);
			state = conn.createStatement();
			if (i <= 4) {
				System.out.println("zhi xingl eif");
				rs = state
						.executeQuery("select * from homework where number < '"
								+ homeworkId + "'order by number desc");// order
																		// by
																		// num
																		// desc
				while (rs.next()) {
					MyHomework home = new MyHomework();
					home.setDeadline(rs.getString("deadline"));
					home.setHomework(rs.getString("homework"));
					home.setName(rs.getString("username"));
					home.setReleasePerson(rs.getString("releaseperson"));
					home.setSubject(rs.getString("subject"));
					home.setCommentnum(rs.getInt("commentnum"));
					state = conn.createStatement();
					rscom = state
							.executeQuery("select * from homeworkcomment where homeworkid = '"
									+ rs.getInt("commentnum") + "' ");// order
																		// by
																		// num
					// desc
					ArrayList<MyHomeworkComment> homecomlist = new ArrayList<MyHomeworkComment>();
					while (rscom.next()) {
						MyHomeworkComment homecom = new MyHomeworkComment();
						homecom.setComment(rscom.getString("comment"));
						homecom.setTime(rscom.getTimestamp("time"));
						homecom.setName(rscom.getString("username"));
						homecom.setHomeworkid(rscom.getInt("homeworkid"));
						homecom.setCommentid(rscom.getInt("num"));
						homecomlist.add(homecom);
					}
					home.setHomeworklist(homecomlist);
					homelist20.add(home);
				}
			} else {
				System.out.println("zhi xing le else");
				rs = state
						.executeQuery("select * from curriculum.homework where number < '"
								+ homeworkId + "' order by num desc limit 4");
				while (rs.next()) {
					MyHomework home = new MyHomework();
					home.setDeadline(rs.getString("deadline"));
					home.setHomework(rs.getString("homework"));
					home.setName(rs.getString("username"));
					home.setReleasePerson(rs.getString("releaseperson"));
					home.setSubject(rs.getString("subject"));
					home.setCommentnum(rs.getInt("commentnum"));
					state = conn.createStatement();
					rscom = state
							.executeQuery("select * from homeworkcomment where homeworkid = '"
									+ rs.getInt("number")
									+ "' order by number desc");
					ArrayList<MyHomeworkComment> homecomcom = new ArrayList<MyHomeworkComment>();
					while (rscom.next()) {
						MyHomeworkComment homecom = new MyHomeworkComment();
						homecom.setComment(rscom.getString("comment"));
						homecom.setTime(rscom.getTimestamp("time"));
						homecom.setName(rscom.getString("username"));
						homecom.setHomeworkid(rscom.getInt("homeworkid"));
						homecom.setCommentid(rscom.getInt("num"));
						homecomcom.add(homecom);
					}
					home.setHomeworklist(homecomcom);
					homelist20.add(home);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return homelist20;
	}

	/*
	 * 删除作业
	 */
	public void deleteHomework(String id, int homeworkid) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			System.out.println(id + "++" + homeworkid);
			state = conn.createStatement();
			state.executeUpdate("delete from homework where username = '" + id
					+ "' and number ='" + homeworkid + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
