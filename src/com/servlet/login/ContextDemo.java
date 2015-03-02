package com.servlet.login;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.servlet.AlarmRefresh.Trriggerclass;

/**
 * Application Lifecycle Listener implementation class Context
 *
 */
@WebListener
public class ContextDemo implements ServletContextListener {

	private Connection conn;

	/**
	 * Default constructor.
	 */
	public ContextDemo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		Statement state;
		Context ctx;
		try {
			ctx = new InitialContext();

			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/xiner");
			 conn = ds.getConnection();
			arg0.getServletContext().setAttribute("datasource", ds);
			Trriggerclass trri = new Trriggerclass(conn);
			trri.startTriggle();
			trri.updateRank();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
