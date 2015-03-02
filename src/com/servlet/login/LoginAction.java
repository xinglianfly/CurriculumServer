package com.servlet.login;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.servlet.bean.MyClass;
import com.servlet.bean.MyInformation;
import com.servlet.bean.MyPoint;
import com.servlet.database.DatabaseAskandanswer;
import com.servlet.database.DatabaseInfomation;
import com.servlet.database.DatabaseRank;
import com.servlet.database.DatabaseReleaseHomework;
import com.servlet.database.Databasecurriculum;
import com.servlet.spider.ClassClient;

@WebServlet("/login")
public class LoginAction extends HttpServlet {
	/**
     *  
     */
	public static int Ranking;// 签到排名
	public static int Oldday;// 前一天的号数
	private static final long serialVersionUID = 1L;
	ClassClient cf;
	Databasecurriculum data;
//	ConnectDatabase conndatabse;
	DatabaseRank datarank;
	DatabaseInfomation datainfo;
	DatabaseAskandanswer askandans;
	DatabaseReleaseHomework datawork;
	Gson gson;
	String username;
	String password;
	MyInformation userInformation;
	ArrayList<MyClass> classList;
	ArrayList<MyPoint> semesterList;

	public LoginAction() {
		
	}
	
	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource)getServletContext().getAttribute("datasource");
//		Connection conn = (Connection)getServletContext().getAttribute("conn");
		cf = new ClassClient();
		gson = new Gson();
		data = new Databasecurriculum(ds);
		datarank = new DatabaseRank(ds);
		datainfo = new DatabaseInfomation(ds);
		askandans = new DatabaseAskandanswer(ds);
		datawork = new DatabaseReleaseHomework(ds);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		System.out.println(name + password);
		PrintWriter out = res.getWriter();
		if (cf.ifLogin(name, password)) {
			out.println("登陆成功");
			out.close();
		} else {
			out.println("用户名或密码错误");

		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		if (null == req) {
			return;
		}
		res.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		PrintWriter out = res.getWriter();

		String RequestType = req.getParameter("Type");// 客户端发出请求类型
		System.out.println("RequestType" + RequestType);
		if (RequestType.equals("getInformation")) {
			username = req.getParameter("username");
			password = req.getParameter("password");
			System.out.println("RequestType" + RequestType + username
					+ password);

			if (!data.IsLogin(username, password)) {
				if (cf.ifLogin(username, password)) {
					System.out.println("zhe que de ");
					out.println("0"); // /< 正确
					out.println("学生在线课程格子");
					userInformation = cf.getUserInfo();// 传用户信息
					String userinformation = gson.toJson(userInformation);
					out.println(userinformation);
					out.println("学生在线课程格子");
					classList = cf.getClassList();// 传用户课表信息
					String classlist = gson.toJson(classList);
					System.out.println(classlist);
					out.println(classlist);
					int i = 1;
					while (cf.getSemesterList(i).size() != 0) {
						out.println("学生在线课程格子");
						semesterList = cf.getSemesterList(i);
						String semesterlist = gson.toJson(semesterList);
						System.out.println(semesterlist);
						out.println(semesterlist);
						i++;
					}
				}

			} else if (data.IsLogin(username, password)) {

				out.println("0");
				out.println("学生在线课程格子");
				String userinformation = gson.toJson(data.info(username));
				out.println(userinformation);
				out.println("学生在线课程格子");
				String classlist = gson.toJson(data.les(username));
				System.out.println(classlist);
				out.println(classlist);
				for (int seme = 1; seme <= 8; seme++) {
					out.println("学生在线课程格子");
					String semesterlist = gson
							.toJson(data.cour(username, seme));
					if (semesterlist != null) {
						out.println(semesterlist);
					}
				}
				out.println("学生在线课程格子");
				String info = gson.toJson(datainfo.getInfo(username));
				if (info != null) {
					out.println(info);
				}
				out.println("学生在线课程格子");
				String homework = gson.toJson(datawork
						.getPersonHomework(username));
				if (homework != null) {
					out.println(homework);
				}
				// out.println("学生在线课程格子");
				// String rankrelease = gson.toJson(datarank.getRealeaseRank());
				// if(rankrelease!=null){
				// out.println(rankrelease);
				// System.out.println(rankrelease+"{{{{{{{");
				// }

			} else {
				out.println("1");
			}
			insertsomething();
		} // /< 用户名或密码错误
		else if (RequestType.equals("uploadFile")) {

			String imageid = null;
			// 创建文件项目工厂对象
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// 设置文件上传路径
			String upload = this.getServletContext().getRealPath("/upload/");
			System.out.println(upload);
			// 获取系统默认的临时文件保存路径，该路径为Tomcat根目录下的temp文件夹
			String temp = System.getProperty("java.io.tmpdir");
			// 设置缓冲区大小为 5M
			factory.setSizeThreshold(1024 * 1024 * 5);
			// 设置临时文件夹为temp
			factory.setRepository(new File(temp));
			// 用工厂实例化上传组件,ServletFileUpload 用来解析文件上传请求
			ServletFileUpload servletFileUpload = new ServletFileUpload(factory);

			// 解析结果放在List中
			try {
				List<FileItem> list = servletFileUpload.parseRequest(req);
				for (FileItem item : list) {
					String name = item.getFieldName();
					InputStream is = item.getInputStream();
					if (name.contains("id")) {
						imageid = inputStream2String(is);
						System.out.println(imageid);
					}
					System.out.println(is + "is wei");

					if (name.contains("file")) {
						try {
							inputStream2File(is, upload + "\\" + imageid
									+ ".png");
							datainfo.insertpath(imageid, imageid + ".png");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}

				out.write("success");
			} catch (FileUploadException e) {
				e.printStackTrace();
				out.write("failure");
			}
		} else if (RequestType.equals("getRank")) {
			String id = req.getParameter("id");
			String name = req.getParameter("name");
			String content = req.getParameter("content");
			String ifconti = req.getParameter("ifcontinusign");
			System.out.println(content+")))"+name);
			Calendar ca = Calendar.getInstance();
			int day = ca.get(Calendar.DATE);// 获取当前日期
			if (day > Oldday || day == 1)// 判断是否为新的一天 对排名清零
			{
				Oldday = day;
				Ranking = 0;
			}
			int getrank = ++Ranking;
			out.println(getrank);
			datarank.insertRank(id, name, getrank,ifconti);
			if (content != null) {
				datarank.insertRealeaseRank(content, id, name);
			}
		} else if (RequestType.equals("uploadInformation")) {
			System.out.println("upload information");
			String name = req.getParameter("name");
			String sex = req.getParameter("sex");
			String talktome = req.getParameter("talktome");
			String aim = req.getParameter("aim");
			String id = req.getParameter("id");
			datainfo.insertIntoInfo(name, sex, aim, talktome, id);
		} else if (RequestType.equals("uploadGesture")) {
			String id = req.getParameter("id");
			String Gesture = req.getParameter("gesture");
			datainfo.insertGuesture(id, Gesture);
		} else if (RequestType.equals("cancelpassword")) {
			String id = req.getParameter("id");
			datainfo.cancelPassword(id);
		} else if (RequestType.equals("askquestion")) {
			String user = req.getParameter("username");
			String ques = req.getParameter("question_details");
			String bri = req.getParameter("question_brief");
			askandans.insertAsk(user, ques, bri);
		} else if (RequestType.equals("getRankInfo")) {
			int id = Integer.parseInt(req.getParameter("flagrankid"));
			String rankrelease = gson.toJson(datarank.getDownrefreshsignInfo(id));
			if (rankrelease != null) {
				out.println(rankrelease);
				System.out.println(rankrelease + "{{{{{{{");
			}

		} else if (RequestType.equals("getRankdownInfo")) {
			int id = Integer.parseInt(req.getParameter("flagdownrankid"));
			System.out.println(id + "id shi shen me ne");
			String downrankcomment = gson.toJson(datarank
					.getuprefreshrRankcomment(id));
			if (downrankcomment != null) {
				out.println(downrankcomment);
			}
		}

		else if (RequestType.equals("rankcomment")) {
			int id = Integer.parseInt(req.getParameter("stateid"));
			String username = req.getParameter("username");
			String comment = req.getParameter("content");
			datarank.insertRankComment(id, comment, username);
			datarank.insertcommentNum(id);
		} else if (RequestType.equals("rankzan")) {
			int id = Integer.parseInt(req.getParameter("stateid"));
			datarank.insertZanNum(id);

		} else if (RequestType.equals("getaskdownrefresh")) {
			String asdandanswer = gson.toJson(askandans.getDownrefreshQuestion(0));
			if (asdandanswer != null) {
				out.println(asdandanswer);
				System.out.println(asdandanswer + "______");
			}
		} else if (RequestType.equals("uploadanswer")) {
			String username = req.getParameter("username");
			String answer = req.getParameter("content");
			int questionid = Integer.parseInt(req.getParameter("id"));
			System.out.println(answer + "___");
			askandans.insertAnswer(username, answer, questionid);
			askandans.insertAnswernum(questionid);
		} else if (RequestType.equals("getanswerupload")) {
			int id = Integer.parseInt(req.getParameter("flagupquesid"));
			System.out.println(id);
			String more = gson.toJson(askandans.uploadmore(id));
			if (more != null) {
				out.println(more);
				System.out.println(more + "{{{{{{{{{{[");
			}
		} else if (RequestType.equals("releasehomework")) {
			String id = req.getParameter("userid");
			String subject = req.getParameter("subject");
			String content = req.getParameter("content");
			String deadline = req.getParameter("deadline");
			datawork.insertTask(id, subject, deadline, content);
		} else if (RequestType.equals("loadhomework")) {
			String id = req.getParameter("id");
			String homework = gson.toJson(datawork.getPersonHomework(id));
			if (homework != null) {
				out.println(homework);
				System.out.println(homework);
			}
		} else if (RequestType.equals("deletehomework")) {

			String user = req.getParameter("stuid");
			int homeid = Integer.parseInt((req.getParameter("homeworkid")));
			System.out.println("8**" + user + homeid);
			datawork.deleteHomework(user, homeid);
		}

		out.flush();
		out.close();
	}

	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	// 流转化成文件
	public static void inputStream2File(InputStream is, String savePath)
			throws Exception {
		System.out.println("文件保存路径为:" + savePath);
		File file = new File(savePath);
		InputStream inputSteam = is;
		BufferedInputStream fis = new BufferedInputStream(inputSteam);
		FileOutputStream fos = new FileOutputStream(file);
		int f;
		while ((f = fis.read()) != -1) {
			fos.write(f);
		}
		fos.flush();
		fos.close();
		fis.close();
		inputSteam.close();

	}

	public void insertsomething() {
		if (!data.IsLogin(username, password)) {
			data.insertStudentsInfo(userInformation.getMyStudentID(),
					userInformation.getMyName(),
					userInformation.getMyAcademy(),
					userInformation.getMySpecialty(),
					userInformation.getFirstAveGrade(),
					userInformation.getSecondAveGrade(),
					userInformation.getThridAveGrade(),
					userInformation.getForthAveGrade(), password);

			for (int i = 0; i < classList.size(); i++) {
				data.insertStudentsLesson(username, classList.get(i)
						.getClassName(), classList.get(i).getClassPlace(),
						classList.get(i).getClassDayOfTime(), classList.get(i)
								.getClassDayOfWeek());
			}
			int se = 1;
			while (cf.getSemesterList(se).size() != 0) {
				for (int s = 0; s < cf.getSemesterList(se).size(); s++) {
					data.insertStudentsCourse(username, cf.getSemesterList(se)
							.get(s).getClassName(),
							cf.getSemesterList(se).get(s).getClassYear(), cf
									.getSemesterList(se).get(s)
									.getClassCredit(), cf.getSemesterList(se)
									.get(s).getClassGrade(), cf
									.getSemesterList(se).get(s)
									.getClassAttitude(), se);
				}
				se++;
			}
		}
	}

}
