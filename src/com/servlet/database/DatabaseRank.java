package com.servlet.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import javax.sql.DataSource;

import com.servlet.bean.SignComment;
import com.servlet.bean.SignRelease;

public class DatabaseRank {

//	Connection conn;
	Statement state = null;
DataSource ds;
	public DatabaseRank(DataSource ds) {
//		Databasecurriculum data = new Databasecurriculum();
//		conn = data.conn;
		this.ds=ds;
		
	}

	public DatabaseRank() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 插入排名到个人信息里面
	 * 
	 * @param id
	 * @param rank
	 */

	public void insertRank(String id, String name, int rank, String ifconti) {
		PreparedStatement pre;
		ResultSet rs;
		String ifSign = null;
		int signContinue = 0;
		int signAll = 0;
		int today = 0;
		int tomonth = 0;
		Calendar ca = Calendar.getInstance();
		int day = ca.get(Calendar.DATE);// 获取当前日期
		int month = ca.get(Calendar.MONTH);
		Connection conn = null;
		try {
			conn = ds.getConnection();
			if (!ifMark(id)) {
				pre = conn
						.prepareStatement("insert into information (id,name,ifsign,signrank,signcontinue,signall,day,month) values (?,?,?,?,?,?,?,?)");
				pre.setString(1, id);
				pre.setString(2, name);
				pre.setString(3, "1");
				pre.setInt(4, rank);
				pre.setInt(5, 1);
				pre.setInt(6, 1);
				pre.setInt(7, day);
				pre.setInt(8, month);
				pre.executeUpdate();
			} else {
				 state = conn.createStatement();

				rs = state
						.executeQuery("select * from information where id = '"
								+ id + "'");
				while (rs.next()) {
					signContinue = rs.getInt("signcontinue");
					signAll = rs.getInt("signall");
					today = rs.getInt("day");
					tomonth = rs.getInt("month");
					if (ifconti.equals("是")) {
						signContinue = ++signContinue;
					} else {
						signContinue = 1;
					}
					signAll = ++signAll;
					state = conn.createStatement();
					state.executeUpdate("update information set ifsign = 1,signrank = '"
							+ rank
							+ "',signcontinue = '"
							+ signContinue
							+ "',signall = '"
							+ signAll
							+ "',day = '"
							+ day
							+ "',month ='"
							+ month
							+ "',name = '"
							+ name
							+ "' where id ='" + id + "'");
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

	}
	/*
	 * 更新是否签到
	 */

	public void updateifSign() {
		ResultSet rss;
		String id;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			System.out.println("zhi xingle updaterank+ zai lin gyi leizhong");
			state = conn.createStatement();
			rss = state.executeQuery("select * from information");
			while (rss.next()) {
				id = rss.getString("id");
				state = conn.createStatement();
				state.executeUpdate("update information set ifsign = 0  where id = '"
						+ id + "'");
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

	}
/*
 * 更新连续签到
 */
	public void updatecontinusign() {
		ResultSet rs;
		int today, tomonth;
		String id;
		Calendar ca = Calendar.getInstance();
		int day = ca.get(Calendar.DATE);// 获取当前日期
		int month = ca.get(Calendar.MONTH);
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			rs = state.executeQuery("select * from information");
			while (rs.next()) {
				today = rs.getInt("day");
				tomonth = rs.getInt("month");
				id = rs.getString("id");
				if ((day - today != 1)&&(month == tomonth)) {
						state.executeQuery("update information set signcontinue = 0 where id = '"
								+ id + "'");
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
		

	}

	/**
	 * 判断是否已经插入过
	 * 
	 * @param id
	 * @return
	 */
	public boolean ifMark(String id) {
		boolean mark = false;
		ResultSet result;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			result = state.executeQuery("select * from information where id = '"
					+ id + "'");
			mark = result.next();
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
		return mark;
	}
/*
 * 插入签到的内容
 */
	public void insertRealeaseRank(String content, String id, String name) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			PreparedStatement pre = conn
					.prepareStatement("insert into rank (stuid,name,releaserank,zan,comment)values (?,?,?,?,?)");
			pre.setString(1, id);
			pre.setString(2, name);
			pre.setString(3, content);
			pre.setInt(4, 0);
			pre.setInt(5, 0);
			pre.executeUpdate();
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
 * 下拉刷新
 */

	public ArrayList<SignRelease> getDownrefreshsignInfo(int rankid) {
		int i = 0;
		ResultSet numrs, rs, rscom;
		ArrayList<SignRelease> signlist20 = new ArrayList<SignRelease>();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			numrs = state
					.executeQuery("select count(*) from rank where num > '"
							+ rankid + "'");
			numrs.next();
			i = numrs.getInt(1);
			state = conn.createStatement();
			ResultSet all = state.executeQuery("select count(*) from rank ");
			all.next();
			int allnum = all.getInt(1);
			if (allnum <= 4) {
				System.out.println("zhi xingl eif");
				rs = state.executeQuery("select * from rank");// order by
																// num desc
				while (rs.next()) {
					SignRelease sign = new SignRelease();
					sign.setName(rs.getString("name"));
					sign.setNumcomment(rs.getInt("comment"));
					sign.setRankid(rs.getInt("num"));
					sign.setReleaserank(rs.getString("releaserank"));
					sign.setTime(rs.getTimestamp("releasetime"));
					sign.setZan(rs.getInt("zan"));
					state = conn.createStatement();
					rscom = state
							.executeQuery("select * from rankcomment where rankid = '"
									+ rs.getInt("num") + "' ");// order by
																// num
																// desc
					ArrayList<SignComment> signcom = new ArrayList<SignComment>();
					while (rscom.next()) {
						SignComment sico = new SignComment();
						sico.setComment(rscom.getString("comment"));
						sico.setTime(rscom.getTimestamp("commenttime"));
						sico.setUsername(rscom.getString("username"));
						sico.setRankid(rscom.getInt("rankid"));
						sico.setCommentid(rscom.getInt("num"));
						signcom.add(sico);
					}
					sign.setCommentlist(signcom);
					signlist20.add(sign);
				}
			} else {
				System.out.println("zhi xing le else");
				rs = state
						.executeQuery("select * from(select * from rank order by num desc limit 4) as tt order by num");
				while (rs.next()) {
					SignRelease sign = new SignRelease();
					sign.setName(rs.getString("name"));
					sign.setNumcomment(rs.getInt("comment"));
					sign.setRankid(rs.getInt("num"));
					sign.setReleaserank(rs.getString("releaserank"));
					sign.setTime(rs.getTimestamp("releasetime"));
					sign.setZan(rs.getInt("zan"));
					state = conn.createStatement();
					rscom = state
							.executeQuery("select * from rankcomment where rankid = '"
									+ rs.getInt("num") + "' order by num desc");
					ArrayList<SignComment> signcom = new ArrayList<SignComment>();
					while (rscom.next()) {
						SignComment sico = new SignComment();
						sico.setComment(rscom.getString("comment"));
						sico.setTime(rscom.getTimestamp("commenttime"));
						sico.setUsername(rscom.getString("username"));
						sico.setRankid(rscom.getInt("rankid"));
						sico.setCommentid(rscom.getInt("num"));
						signcom.add(sico);
					}
					sign.setCommentlist(signcom);
					signlist20.add(sign);
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
		return signlist20;
	}
/*
 * 插入评论
 */
	public void insertRankComment(int id, String content, String username) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			PreparedStatement pre = conn
					.prepareStatement("insert into rankcomment (rankid,comment,username) values (?,?,?)");
			pre.setInt(1, id);
			pre.setString(2, content);
			pre.setString(3, username);
			pre.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * c插入赞的个数
	 */

	public void insertZanNum(int rankid) {
		ResultSet rs;
		int numzan = 0;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			rs = state.executeQuery("select * from rank where num = '" + rankid
					+ "'");
			while (rs.next()) {
				numzan = rs.getInt("zan");
				++numzan;
			}
			state = conn.createStatement();
			state.executeUpdate("update rank set zan = '" + numzan
					+ "'where num = '" + rankid + "'");
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
	 * 插入评论的个数
	 */
	public void insertcommentNum(int rankid) {
		ResultSet rs;
		int numcomment = 0;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			rs = state.executeQuery("select * from rank where num = '" + rankid
					+ "'");
			while (rs.next()) {
				numcomment = rs.getInt("comment");
				++numcomment;
			}
			state = conn.createStatement();
			state.executeUpdate("update rank set comment = '" + numcomment
					+ "'where num = '" + rankid + "'");
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
	 * 上拉加载
	 */
	public ArrayList<SignRelease> getuprefreshrRankcomment(int rankId) {

		int i = 0;
		ResultSet numrs, rs, rscom;
		ArrayList<SignRelease> signlist20 = new ArrayList<SignRelease>();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			numrs = state
					.executeQuery("select count(*) from rank where num < '"
							+ rankId + "'");
			numrs.next();
			i = numrs.getInt(1);
			state = conn.createStatement();
			if (i <= 4) {
				System.out.println("zhi xingl eif");
				rs = state.executeQuery("select * from rank where num < '"
						+ rankId + "'order by num desc");// order by num desc
				while (rs.next()) {
					SignRelease sign = new SignRelease();
					sign.setName(rs.getString("name"));
					sign.setNumcomment(rs.getInt("comment"));
					sign.setRankid(rs.getInt("num"));
					sign.setReleaserank(rs.getString("releaserank"));
					sign.setTime(rs.getTimestamp("releasetime"));
					sign.setZan(rs.getInt("zan"));
					state = conn.createStatement();
					rscom = state
							.executeQuery("select * from rankcomment where rankid = '"
									+ rs.getInt("num") + "' ");// order by num
																// desc
					ArrayList<SignComment> signcom = new ArrayList<SignComment>();
					while (rscom.next()) {
						SignComment sico = new SignComment();
						sico.setComment(rscom.getString("comment"));
						sico.setTime(rscom.getTimestamp("commenttime"));
						sico.setUsername(rscom.getString("username"));
						sico.setRankid(rscom.getInt("rankid"));
						sico.setCommentid(rscom.getInt("num"));
						signcom.add(sico);
					}
					sign.setCommentlist(signcom);
					signlist20.add(sign);
				}
			} else {
				System.out.println("zhi xing le else");
				rs = state.executeQuery("select * from rank where num < '"
						+ rankId + "' order by num desc limit 4");
				while (rs.next()) {
					SignRelease sign = new SignRelease();
					sign.setName(rs.getString("name"));
					sign.setNumcomment(rs.getInt("comment"));
					sign.setRankid(rs.getInt("num"));
					sign.setReleaserank(rs.getString("releaserank"));
					sign.setTime(rs.getTimestamp("releasetime"));
					sign.setZan(rs.getInt("zan"));
					state = conn.createStatement();
					rscom = state
							.executeQuery("select * from rankcomment where rankid = '"
									+ rs.getInt("num") + "' order by num desc");
					ArrayList<SignComment> signcom = new ArrayList<SignComment>();
					while (rscom.next()) {
						SignComment sico = new SignComment();
						sico.setComment(rscom.getString("comment"));
						sico.setTime(rscom.getTimestamp("commenttime"));
						sico.setUsername(rscom.getString("username"));
						sico.setRankid(rscom.getInt("rankid"));
						sico.setCommentid(rscom.getInt("num"));
						signcom.add(sico);
					}
					sign.setCommentlist(signcom);
					signlist20.add(sign);
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
		return signlist20;
	}
}
