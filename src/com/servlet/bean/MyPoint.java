package com.servlet.bean;

import java.io.Serializable;

public class MyPoint implements Serializable{
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getClassYear() {
		return classYear;
	}

	public 	void setClassYear(int classYear) {
		this.classYear = classYear;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public String getClassCredit() {
		return classCredit;
	}

	public void setClassCredit(String classCredit) {
		this.classCredit = classCredit;
	}

	public String getClassAttitude() {
		return classAttitude;
	}

	public void setClassAttitude(String classAttitude) {
		this.classAttitude = classAttitude;
	}

	public String getClassGrade() {
		return classGrade;
	}

	public void setClassGrade(String classGrade) {
		this.classGrade = classGrade;
	}

	public String toString() {
		return "[ " + className + " : " + classYear + " , " + semester + " , "
				+ classCredit + " , " + classGrade + " , " + classAttitude
				+ " ]";
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else {
			if (this.getClass() == obj.getClass()) {
				MyPoint cour = (MyPoint) obj;
				if ((this.getClassAttitude().equals(cour.getClassAttitude()))
						&& (this.getClassGrade().equals(cour.getClassGrade()))
						&& (this.getClassName().equals(cour.getClassName()))
						&& (this.getClassGrade().equals(cour.getClassGrade()))
						&& (this.getClassYear() == cour.getClassYear())) {
					return true;

				} else {
					return false;
				}

			} else {
				return false;
			}
		}
	}
	private String className;// ��Ŀ����
	private int classYear;// ȷ�ϸÿ�Ŀ����һ��ѧ���
	private int semester;// ��ѧѧ��,�������ѧ��,��ֵΪ0,�������ѧ��,��ֵΪ1
	private String classCredit;// ÿ�Ƶ�ѧ��,������Ϊѧ�ֲ�һ����int
	private String classAttitude;// ��Ŀ������:����,ѡ��,��ѡ
	private String classGrade;// ÿ�Ƶĳɼ�
}
