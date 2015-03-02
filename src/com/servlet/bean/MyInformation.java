package com.servlet.bean;

import java.io.Serializable;

public class MyInformation implements Serializable{
	public String getMyName() {
		return myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
	}

	public String getMyStudentID() {
		return myStudentID;
	}

	public void setMyStudentID(String myStudentID) {
		this.myStudentID = myStudentID;
	}

	public String getMyAcademy() {
		return myAcademy;
	}

	public void setMyAcademy(String myAcademy) {
		this.myAcademy = myAcademy;
	}

	public String getMySpecialty() {
		return mySpecialty;
	}

	public void setMySpecialty(String specialty) {
		this.mySpecialty = specialty;
	}

	public String toString() {
		String result = myAcademy + " , " + mySpecialty + " , " + myName
				+ " , " + myStudentID + " , ";
		result += firstAveGrade + " , " + secondAveGrade + " , "
				+ thridAveGrade + " , " + forthAveGrade;
		return result;
	}

	public float getFirstAveGrade() {
		return firstAveGrade;
	}

	public void setFirstAveGrade(float firstAveGrade) {
		this.firstAveGrade = firstAveGrade;
	}

	public float getSecondAveGrade() {
		return secondAveGrade;
	}

	public void setSecondAveGrade(float secondAveGrade) {
		this.secondAveGrade = secondAveGrade;
	}

	public float getThridAveGrade() {
		return thridAveGrade;
	}

	public void setThridAveGrade(float thridAveGrade) {
		this.thridAveGrade = thridAveGrade;
	}

	public float getForthAveGrade() {
		return forthAveGrade;
	}

	public void setForthAveGrade(float forthAveGrade) {
		this.forthAveGrade = forthAveGrade;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else {
			if (this.getClass() == obj.getClass()) {
				MyInformation u = (MyInformation) obj;
				if ((Math.abs(this.getFirstAveGrade() - u.getFirstAveGrade())<0.1)
						&& (Math.abs(this.getSecondAveGrade() - u.getSecondAveGrade())<0.1)
						&& (Math.abs(this.getThridAveGrade() - u.getThridAveGrade())<0.1)
						&& (Math.abs(this.getForthAveGrade() - u.getForthAveGrade())<0.1)
						&& (this.getMyAcademy().equals(u.getMyAcademy()))
						&& (this.getMySpecialty().equals(u.getMySpecialty()))
						&& (this.getMyName().equals(u.getMyName()))) {
					return true;
				} else {
					return false;
				}

			} else {
				return false;
			}
		}
	}
	private String myName;
	private String myStudentID;
	private String myAcademy;// ѧԺ
	private String mySpecialty;// רҵ
	private float firstAveGrade;
	private float secondAveGrade;
	private float thridAveGrade;
	private float forthAveGrade;
}
