package com.servlet.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.naming.spi.DirStateFactory.Result;
import javax.sql.DataSource;

import com.servlet.bean.MyClass;
import com.servlet.bean.MyInfo;
import com.servlet.bean.MyInformation;
import com.servlet.bean.MyPoint;
import com.servlet.spider.ClassClient;

public class Databasecurriculum {
	DataSource ds;
	// Connection conn;
	Statement state = null;
	private PreparedStatement insertLesson;
	private PreparedStatement insertCourse;
	ArrayList<MyClass> arrlesson;
	ArrayList<MyPoint> arrcou;
	PreparedStatement insertStuInfoStatement;

	// PreparedStatement
	public Databasecurriculum(DataSource ds) {
		this.ds = ds;
		try {
			Connection conn = ds.getConnection();
			insertStuInfoStatement = conn
					.prepareStatement("INSERT INTO students(name,id,academy,specialty,firstgrade,"
							+ "secondgrade,thirdgrade,fourthgrade,password)"
							+ "VALUES(?,?,?,?,?,?,?,?,?)");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Databasecurriculum() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 插入个人绩点等相关信息
	 * 
	 * @param id
	 * @param name
	 * @param Academe
	 * @param Specialty
	 * @param firstgrade
	 * @param secondgrade
	 * @param thirdgrade
	 * @param fourthgrade
	 * @param password
	 */
	public void insertStudentsInfo(String id, String name, String Academe,
			String Specialty, float firstgrade, float secondgrade,
			float thirdgrade, float fourthgrade, String password) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			ResultSet rs = state
					.executeQuery("select * from students where id='" + id
							+ "'");
			if (!rs.next()) {
				insertStuInfoStatement.setString(1, name);
				insertStuInfoStatement.setString(2, id);
				insertStuInfoStatement.setString(3, Academe);
				insertStuInfoStatement.setString(4, Specialty);
				insertStuInfoStatement.setFloat(5, firstgrade);
				insertStuInfoStatement.setFloat(6, secondgrade);
				insertStuInfoStatement.setFloat(7, thirdgrade);
				insertStuInfoStatement.setFloat(8, fourthgrade);
				insertStuInfoStatement.setString(9, password);
				insertStuInfoStatement.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (state != null) {
					state.close();
				}
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 插入学生课表的信息
	 * 
	 * @param id
	 * @param name
	 * @param place
	 * @param timeofday
	 * @param dayofweek
	 */
	public void insertStudentsLesson(String id, String name, String place,
			int timeofday, int dayofweek) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			insertLesson = conn
					.prepareStatement("INSERT INTO lesson(id,coursename,"
							+ "courseplase,timeofday,dayofweek)"
							+ "VALUES(?,?,?,?,?)");
			insertLesson.setString(1, id);
			insertLesson.setString(2, name);
			insertLesson.setString(3, place);
			insertLesson.setInt(4, timeofday);
			insertLesson.setInt(5, dayofweek);
			insertLesson.executeUpdate();
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
	 * 将学生的课程信息插入
	 * 
	 * @param id
	 * @param coursename
	 * @param courseyear
	 * @param coursecredit
	 * @param coursescore
	 * @param courseAttitude
	 * @param semester
	 */
	public void insertStudentsCourse(String id, String coursename,
			int courseyear, String coursecredit, String coursescore,
			String courseAttitude, int semester) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			insertCourse = conn
					.prepareStatement("INSERT INTO course(id,coursename,"
							+ "courseyear,coursecredit,coursescore,courseattitude,semester)"
							+ "VALUES(?,?,?,?,?,?,?)");
			insertCourse.setString(1, id);
			insertCourse.setString(2, coursename);
			insertCourse.setInt(3, courseyear);
			insertCourse.setString(4, coursecredit);
			insertCourse.setString(5, coursescore);
			insertCourse.setString(6, courseAttitude);
			insertCourse.setInt(7, semester);
			insertCourse.executeUpdate();
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
	 * 判断是否是第一次登陆
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public boolean IsLogin(String id, String password) {
		ResultSet rs;
		String pass = null;
		boolean isLogin = false;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			rs = state.executeQuery("select * from students where id='" + id
					+ "'");
			if (rs.next()) {
				pass = rs.getString("password");
			}
			if (password.equals(pass)) {
				isLogin = true;
			} else {
				isLogin = false;
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

		return isLogin;
	}

	/**
	 * 得到个人信息
	 * 
	 * @param id
	 * @return
	 */
	public MyInformation info(String id) {
		MyInformation per = null;
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			ResultSet rs = state
					.executeQuery("select * from students where id='" + id
							+ "'");
			per = new MyInformation();
			if (rs.next()) {
				per.setMyName(rs.getString("name"));
				per.setMyAcademy(rs.getString("academy"));
				per.setMySpecialty(rs.getString("specialty"));
				per.setMyStudentID(id);
				per.setFirstAveGrade(rs.getFloat("firstgrade"));
				per.setSecondAveGrade(rs.getFloat("secondgrade"));
				per.setThridAveGrade(rs.getFloat("thirdgrade"));
				per.setForthAveGrade(rs.getFloat("fourthgrade"));
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
		return per;
	}

	/**
	 * 得到Myclass的信息
	 * 
	 * @param id
	 * @return
	 */
	public ArrayList<MyClass> les(String id) {
		arrlesson = new ArrayList<MyClass>();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			ResultSet rs = state.executeQuery("select * from lesson where id='"
					+ id + "'");
			while (rs.next()) {
				MyClass lesson = new MyClass();
				lesson.setClassDayOfTime(rs.getInt("timeofday"));
				lesson.setClassDayOfWeek(rs.getInt("dayofweek"));
				lesson.setClassName(rs.getString("coursename"));
				lesson.setClassPlace(rs.getString("courseplase"));
				arrlesson.add(lesson);
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
		return arrlesson;
	}

	/**
	 * 得到Mypoint的信息
	 * 
	 * @param id
	 * @param sem
	 * @return
	 */
	public ArrayList<MyPoint> cour(String id, int sem) {
		arrcou = new ArrayList<MyPoint>();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			ResultSet rs = state
					.executeQuery("select * from course where id ='" + id
							+ "' and semester= '" + sem + "'");
			while (rs.next()) {
				MyPoint course = new MyPoint();
				course.setClassAttitude(rs.getString("courseattitude"));
				course.setClassGrade(rs.getString("coursescore"));
				course.setClassName(rs.getString("coursename"));
				course.setClassCredit(rs.getString("coursecredit"));
				course.setClassYear(rs.getInt("courseyear"));
				course.setSemester(rs.getInt("semester"));
				arrcou.add(course);
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
		return arrcou;
	}

	/**
	 * 晚上的时候对数据库课表成绩等信息进行更新
	 */
	public void updateDatabase() {
		String id;
		String password;
		ResultSet rsse;
		ClassClient cf = new ClassClient();
		Connection conn = null;
		try {
			conn = ds.getConnection();
			state = conn.createStatement();
			rsse = state.executeQuery("select * from students");
			while (rsse.next()) {
				id = rsse.getString("id");
				password = rsse.getString("password");
				if (cf.ifLogin(id, password)) {
					MyInformation userInformation = cf.getUserInfo();
					MyInformation per = info(id);
					if (!userInformation.equals(per)) {
						System.out.println("user bianle");
						state = conn.createStatement();
						state.executeUpdate("UPDATE students SET name ='"
								+ userInformation.getMyName()
								+ "', academy = '"
								+ userInformation.getMyAcademy()
								+ "',specialty='"
								+ userInformation.getMySpecialty()
								+ "',firstgrade= '"
								+ userInformation.getFirstAveGrade()
								+ "',secondgrade = '"
								+ userInformation.getSecondAveGrade()
								+ "',thirdgrade = '"
								+ userInformation.getThridAveGrade()
								+ "',fourthgrade = '"
								+ userInformation.getForthAveGrade()
								+ "'WHERE id = '" + id + "'");
					}

					les(id);
					ArrayList<MyClass> classList = cf.getClassList();// 传用户课表信息
					if (!ifEquall(classList, arrlesson)) {
						state = conn.createStatement();
						state.executeUpdate("DELETE  FROM lesson WHERE id = '"
								+ id + "'");
						for (int i = 0; i < classList.size(); i++) {
							insertStudentsLesson(id, classList.get(i)
									.getClassName(), classList.get(i)
									.getClassPlace(), classList.get(i)
									.getClassDayOfTime(), classList.get(i)
									.getClassDayOfWeek());
						}
					}
					int se1 = 1;
					while (cf.getSemesterList(se1).size() != 0) {
						ArrayList<MyPoint> semesterList = cf
								.getSemesterList(se1);
						ArrayList<MyPoint> course = cour(id, se1);
						if (!ifEqualc(semesterList, course)) {
							state = conn.createStatement();
							state.executeUpdate("DELETE  FROM course WHERE id = '"
									+ id + "' AND semester = '" + se1 + "'");
							for (int s = 0; s < cf.getSemesterList(se1).size(); s++) {
								insertStudentsCourse(id, cf
										.getSemesterList(se1).get(s)
										.getClassName(), cf
										.getSemesterList(se1).get(s)
										.getClassYear(), cf
										.getSemesterList(se1).get(s)
										.getClassCredit(),
										cf.getSemesterList(se1).get(s)
												.getClassGrade(), cf
												.getSemesterList(se1).get(s)
												.getClassAttitude(), se1);
							}

						}
						se1++;
					}
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
	}

	/**
	 * MyClass 是不是改变了
	 * 
	 * @param l1
	 * @param l2
	 * @return
	 */
	public boolean ifEquall(ArrayList<MyClass> l1, ArrayList<MyClass> l2) {
		if (l1.size() != l2.size()) {
			return false;
		}
		for (int m = 0; m < l1.size(); m++) {
			if (!l1.get(m).equals(l2.get(m))) {
				return false;
			}

		}
		return true;
	}

	/**
	 * Mypoint 内容是不是改变了
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public boolean ifEqualc(ArrayList<MyPoint> c1, ArrayList<MyPoint> c2) {
		if (c1.size() != c2.size()) {
			return false;
		}
		for (int i = 0; i < c1.size(); i++) {
			if (!c1.get(i).equals(c2.get(i))) {
				return false;
			}
		}
		return true;

	}
}