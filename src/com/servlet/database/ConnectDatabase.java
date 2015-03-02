package com.servlet.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {
	static Connection conn;
	public void connect() {
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
//			conn = DriverManager
//					.getConnection("jdbc:mysql://localhost/curriculum?"
//							+ "user=root&characterEncoding=UTF8");
			conn = DriverManager
					.getConnection("jdbc:mysql://211.87.234.50:3306/xiner?"
							+ "user=xiner&password=xiner&characterEncoding=UTF8");
			System.out.println("liangjie shujuku+~~~~~~~");
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		} catch (Exception ex) {
			// handle the error
			ex.printStackTrace();
		}
		

	}
}
