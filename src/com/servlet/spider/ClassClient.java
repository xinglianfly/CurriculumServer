package com.servlet.spider;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.servlet.bean.MyClass;
import com.servlet.bean.MyInformation;
import com.servlet.bean.MyPoint;

public class ClassClient {
	String loginUrl = "http://jwxt.sdu.edu.cn:7890/pls/wwwbks/bks_login2.login";
	String infoUrl = "http://jwxt.sdu.edu.cn:7890/pls/wwwbks/bks_login2.loginmessage";
	String pastGradeUrl = "http://jwxt.sdu.edu.cn:7890/pls/wwwbks/bkscjcx.yxkc";
	String currentClassUrl = "http://jwxt.sdu.edu.cn:7890/pls/wwwbks/xk.CourseView";
	String currentGradeUrl = "http://jwxt.sdu.edu.cn:7890/pls/wwwbks/bkscjcx.curscopre";
	private String useCookie = ""; // 登录后的cookie
	private String myStudentID;// 学生学号
	private MyInformation studentInfo;
	private float[][] semesterPoint;
	private ArrayList<ArrayList<MyPoint>> semesterList;
	private ArrayList<MyClass> currentClassList;

	// private int all = 0;

	public static void main(String[] args) {
		ClassClient cf = new ClassClient();
		// cf.login("201200301102", "as1993915");
		cf.login("201200301250", "090110");
		// cf.login("201200301312", "xiaodi0x");
		// cf.login("201205301230", "wx19940401");
		// cf.login("201200301117", "580724");
		// cf.login("201205301135", "19940418");
		// System.out.println(cf.studentInfo);
		System.out.println(cf.getSemesterList(1));
		System.out.println("------------------------------");
		System.out.println(cf.getSemesterList(2));
		System.out.println("------------------------------");
		System.out.println(cf.getSemesterList(3));
		// System.out.println(cf.getSemesterList(4));
		System.out.println("------------------------------");
		System.out.println(cf.getUserInfo());
		System.out.println(cf.getUserInfo().getFirstAveGrade());
		System.out.println(cf.getUserInfo().getSecondAveGrade());
		System.out.println(cf.getUserInfo().getThridAveGrade());
		System.out.println(cf.getUserInfo().getForthAveGrade());
		System.out.println("------------------------------");
		System.out.println(cf.currentClassList);
		// System.out.println(cf.semesterList);
	}

	public void login(String studentId, String password) {
		this.realLogin(studentId, password);// 必须先登录
		this.formatteUserInfo();// 确定登录信息是否属实
		this.formatteCurrentClass();
		this.formatteSemester();
		this.formatteUserAvgCredit();
	}

	
	public boolean ifLogin(String studentId,String password){
		boolean Log = false;
		this.realLogin(studentId, password);
		this.formatteUserInfo();
		if(useCookie != null){
			Log = true;
			this.formatteCurrentClass();
			this.formatteSemester();
			this.formatteUserAvgCredit();
		}else{
			Log =false;
		}
		return Log;
	}
	public MyInformation getUserInfo() {
		return studentInfo;
	}

	public ArrayList<MyClass> getClassList() {
		return currentClassList;
	}

	public ArrayList<MyPoint> getSemesterList(int i) {
		return semesterList.get(i - 1);
	}

	private void realLogin(String myStudentID, String password) {
		Connection studyInfoSystem = Jsoup.connect(loginUrl);
		System.out.println(studyInfoSystem+"studyInfo");
		try {
			Response res = studyInfoSystem
					.data("stuid", myStudentID, "pwd", password)
					.method(Method.POST).execute();
			useCookie = res.cookie("ACCOUNT");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void formatteUserInfo() {
		Document htmlDoc = this.getStudyInfoHtml(infoUrl);
		String myInformation = htmlDoc.select("span").first().text();
		String[] informations = myInformation.split(" ");// 以空格为间隔
		studentInfo = new MyInformation();
		studentInfo.setMyAcademy(informations[0]);
		studentInfo.setMySpecialty(informations[1]);// 专业
		int tempInfoIndex = informations[2].indexOf("(");// 名字(学号)
		studentInfo.setMyName(informations[2].substring(0, tempInfoIndex));
		myStudentID = informations[2].substring(tempInfoIndex + 1,
				informations[2].length() - 1);
		studentInfo.setMyStudentID(myStudentID);
	}

	private void formatteUserAvgCredit() {
		float firstAveGrade = ((semesterPoint[0][1] + semesterPoint[1][1]) / (semesterPoint[0][0] + semesterPoint[1][0]));
		float secondAveGrade = ((semesterPoint[2][1] + semesterPoint[3][1]) / (semesterPoint[2][0] + semesterPoint[3][0]));
		float thridAveGrade = ((semesterPoint[4][1] + semesterPoint[5][1]) / (semesterPoint[4][0] + semesterPoint[5][0]));
		float forthAveGrade = ((semesterPoint[6][1] + semesterPoint[7][1]) / (semesterPoint[6][0] + semesterPoint[7][0]));
		if (Float.isNaN(firstAveGrade)) {
			firstAveGrade = 0;
		}
		if (Float.isNaN(secondAveGrade)) {
			secondAveGrade = 0;
		}
		if (Float.isNaN(thridAveGrade)) {
			thridAveGrade = 0;
		}
		if (Float.isNaN(forthAveGrade)) {
			forthAveGrade = 0;
		}
		studentInfo.setFirstAveGrade(firstAveGrade);
		studentInfo.setSecondAveGrade(secondAveGrade);
		studentInfo.setThridAveGrade(thridAveGrade);
		studentInfo.setForthAveGrade(forthAveGrade);
		// System.out.println(firstAveGrade + " " + secondAveGrade + " "
		// + thridAveGrade + " " + forthAveGrade);
	}

	private void formatteCurrentClass() {
		Document htmlDoc = this.getStudyInfoHtml(currentClassUrl);
		Elements classes = htmlDoc.select("table").last().select("p");
		currentClassList = new ArrayList<MyClass>();
		int column = 9;// 该表有九列信息
		int eleSize = classes.size();// 该表的行数
		for (int i = column; i < eleSize; i += column) {
			try {
				MyClass tempClass = new MyClass();
				String className = classes.get(i).text();
				tempClass.setClassName(className.substring(0,
						className.length() - 1));
				String tempClassPlace = classes.get(i + 6).text();
				tempClass.setClassPlace(tempClassPlace.substring(0,
						tempClassPlace.length() - 1));
				String classTime = classes.get(i + 7).text().substring(0, 3);
				tempClass.setClassDayOfWeek(Integer.parseInt(classTime
						.substring(0, 1)));
				tempClass.setClassDayOfTime(Integer.parseInt(classTime
						.substring(2, 3)));
				currentClassList.add(tempClass);
				// System.out.println(tempClass);
			} catch (NumberFormatException e) {
				// System.out.println(myStudentID + " : " + "格式转换错误, 已忽略该科目");
				continue;// 避免因某种格式错误而导致不能全部读取
			} catch (IndexOutOfBoundsException e1) {
				// 数组越界，原因是因为有重修的课，但该课没有上课时间
				// System.out.println(myStudentID + " : " + "数组越界错误, 已忽略该科目");
				continue;// 同上
			}
		}
	}

	private void formatteSemester() {
		int semesterNum = 8;
		semesterList = new ArrayList<ArrayList<MyPoint>>();
		for (int i = 0; i < semesterNum; i++) {
			semesterList.add(new ArrayList<MyPoint>());
		}
		semesterPoint = new float[8][2];
		Document pastDoc = this.getStudyInfoHtml(pastGradeUrl);
		System.out.println(pastDoc+"pastDoc");
		System.out.println(pastDoc.select("table")+"table");
		System.out.println(pastDoc.select("table").get(3)+"3");
		System.out.println(pastDoc.select("table").get(3).select("tr")+"tr");
		Elements tempClasses = pastDoc.select("table").get(3).select("tr");// 3是必修的表
		tempClasses.remove(0);
		Elements compulsoryClasses = tempClasses.select("p");
		this.formatteSemester(compulsoryClasses, "必修", false);
		Elements limitedClasses = pastDoc.select("table").get(4).select("td");// 4是限选
		this.formatteSemester(limitedClasses, "限选", false);
		Elements electiveClasses = pastDoc.select("table").get(5).select("td");// 5是选修
		this.formatteSemester(electiveClasses, "选修", false);
		Document currentDoc = this.getStudyInfoHtml(currentGradeUrl);
		Elements tempGrades = currentDoc.getElementsByTag("table").get(3)
				.getElementsByTag("tr");
		tempGrades.remove(0);
		Elements currentGrades = tempGrades.select("td");
		this.formatteSemester(currentGrades, "", true);
	}

	private void formatteSemester(Elements elements, String classAttitude,
			boolean isCurrent) {
		int column, currentChange;// column,currentChange,因为以前成绩表和现在成绩表的不同,需要这个参数来过滤变化
		if (isCurrent) {
			column = 8;
			currentChange = 1;
		} else {
			column = 6;
			currentChange = 0;
		}
		int comSize = elements.size();
		int schoolYear = Integer.parseInt(myStudentID.substring(0, 4));
		// System.out.println(schoolYear);
		String className, classCredit, classGrade;
		int classYear, classMouth, semester;
		for (int i = 0; i < comSize; i += column) {
			MyPoint tempPoint = new MyPoint();
			if (isCurrent)
				tempPoint.setClassAttitude(elements.get(i + 7).text());
			else
				tempPoint.setClassAttitude(classAttitude);
			className = elements.get(i + currentChange + 1).text();// 课程名
			classCredit = elements.get(i + currentChange + 3).text();// 学分
			classGrade = elements.get(i + currentChange + 5).text();// 成绩
			tempPoint.setClassName(className);
			tempPoint.setClassCredit(classCredit);
			tempPoint.setClassGrade(classGrade);
			String tempDate = elements.get(i + currentChange + 4).text();
			// System.out.println(tempDate);
			classYear = Integer.parseInt(tempDate.substring(0, 4));
			classMouth = Integer.parseInt(tempDate.substring(4, 6));

			// 国家六级考试成绩的解释
			if (classMouth == 12) {
				classMouth += 1;
				classYear += 1;
				semester = 1 - (classMouth % 6);
				// tempPoint.setClassCredit("0");
				// System.out.println(tempPoint.getClassCredit());
				// System.out.println(classYear + " " + classMouth);
			} else {
				semester = 1 - (classMouth % 6);
			}
			tempPoint.setClassYear(classYear);
			tempPoint.setSemester(semester);
			// System.out.println(classYear + " " + schoolYear + " " + semester
			// + " " + classMouth);
			int classSeason = 2 * (classYear - schoolYear - 1) + semester;
			// System.out.println(classSeason);
			semesterList.get(classSeason).add(tempPoint);
			try {
				// if (tempPoint.getClassYear() == 2014) {
				// all++;
				// }
				// 任选课学分不计入绩点
				if (tempPoint.getClassAttitude().equals("任选")) {
					continue;
				} else {
					float f_classGrade = Float.parseFloat(classGrade);
					float f_credit = Float.parseFloat(classCredit);
					semesterPoint[classSeason][0] += f_credit;
					semesterPoint[classSeason][1] += f_classGrade * f_credit;
				}
			} catch (NumberFormatException e) {
			}
		}
	}

	private Document getStudyInfoHtml(String url) {
		Document htmlDoc = null;
		try {
			if (useCookie != null)
				htmlDoc = Jsoup.connect(url).timeout(30000).cookie("ACCOUNT", useCookie).get();
			else
				System.out.println("请先登录:");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return htmlDoc;
	}
}