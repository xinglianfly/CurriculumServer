package com.servlet.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.servlet.bean.MyInfo;

public class DatabaseInfomation {
	// Connection conn;
	DataSource ds;
	Statement state = null;

	public DatabaseInfomation(DataSource ds) {
		// ConnectDatabase con = new ConnectDatabase();
		// conn = con.conn;
		this.ds = ds;
	}

	public void insertIntoInfo(String name, String sex, String aim,
			String talk, String id) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			if (ifMark(id)) {
				state.executeUpdate("update information set name = '" + name
						+ "',sex ='" + sex + "',aim='" + aim + "',talktome='"
						+ talk + "' where id = '" + id + "'");
			} else {
				state.executeUpdate("insert into information (name,sex,aim,talktome,id) values ('"
						+ name
						+ "','"
						+ sex
						+ "','"
						+ aim
						+ "','"
						+ talk
						+ "','" + id + "')");
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

	}

	/**
	 * 将图片的路径插入
	 * 
	 * @param id
	 * @param path
	 */
	public void insertpath(String id, String path) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			if (ifMark(id)) {
				state.executeUpdate("update information set imagepath = '"
						+ path + "' where id = '" + id + "'");
			} else {
				state.executeUpdate("insert into information (path,id) values ('"
						+ path + "','" + id + "')");
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

	}

	/**
	 * 将手势密码插入
	 * 
	 * @param id
	 * @param gesture
	 */
	public void insertGuesture(String id, String gesture) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			if (ifMark(id)) {
				state.executeUpdate("update information set setpassword = '"
						+ gesture + "' where id = '" + id + "'");
			} else {
				state.executeUpdate("insert into information (setpassword,id) values ('"
						+ gesture + "','" + id + "')");
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
	}

	public void insertRankInfo(String id, String ifSign, int signrank,
			int signcontinue, int signall, int day) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			if (ifMark(id)) {
				state.executeUpdate("update information set ifsign = '"
						+ ifSign + "',signrank = '" + signrank
						+ "',signcontinue = '" + signcontinue + "',signall = '"
						+ signall + "',day ='" + day + "' where id = '" + id
						+ "'");
			} else {
				state.executeUpdate("insert into information (ifsign,signrank,signcontinue,signall,day,id) values ('"
						+ ifSign
						+ "','"
						+ signrank
						+ "','"
						+ signcontinue
						+ "','" + signall + "','" + day + "','" + id + "')");
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
	}

	/**
	 * 得到个人信息页面的信息
	 * 
	 * @param id
	 * @return
	 */
	public MyInfo getInfo(String id) {
		MyInfo info = null;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			ResultSet rs = state
					.executeQuery("select * from information where id = '" + id
							+ "'");
			while (rs.next()) {
				info = new MyInfo();
				info.setName(rs.getString("name"));
				info.setPassword(rs.getString("setpassword"));
				info.setSex(rs.getString("sex"));
				info.setTalktome(rs.getString("talktome"));
				info.setAim(rs.getString("aim"));
				info.setImagepath(rs.getString("imagepath"));
				info.setIfsign(rs.getString("ifsign"));
				info.setSignrank(rs.getInt("signrank"));
				info.setSigncontinue(rs.getInt("signcontinue"));
				info.setSignall(rs.getInt("signall"));
				info.setDay(rs.getInt("day"));
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
		return info;

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
			result = state
					.executeQuery("select * from information where id = '" + id
							+ "'");
			mark = result.next();
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
		return mark;
	}

	public void cancelPassword(String id) {
		ResultSet rs;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			rs = state.executeQuery("select * from information where id = '"
					+ id + "'");
			while (rs.next()) {
				state = conn.createStatement();
				state.executeUpdate("update information set setpassword = null where id = '"
						+ id + "'");
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

	}
}
