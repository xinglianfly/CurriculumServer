package com.servlet.bean;

import java.io.Serializable;

public class MyClass implements Serializable {
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassPlace() {
		return classPlace;
	}

	public void setClassPlace(String classPlace) {
		this.classPlace = classPlace;
	}

	public int getClassDayOfWeek() {
		return classDayOfWeek;
	}

	public void setClassDayOfWeek(int classDayOfWeek) {
		this.classDayOfWeek = classDayOfWeek;
	}

	public int getClassDayOfTime() {
		return classDayOfTime;
	}

	public void setClassDayOfTime(int classDayOfTime) {
		this.classDayOfTime = classDayOfTime;
	}

	public String toString() {
		return "[ " + className + " : " + classPlace + " , " + classDayOfWeek
				+ " , " + classDayOfTime + " ]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else {
			if (this.getClass() == obj.getClass()) {
				MyClass les = (MyClass) obj;
				if ((this.getClassName().equals(les.getClassName()))
						&& (this.getClassPlace().equals(les.getClassPlace()))
						&& (this.getClassDayOfTime() == les.getClassDayOfTime())
						&& (this.getClassDayOfWeek() == les.getClassDayOfWeek())) {
					return true;

				} else {
					return false;
				}

			} else {
				return false;
			}
		}
	}

	private String className;
	private String classPlace;
	private int classDayOfWeek;
	private int classDayOfTime;
}
