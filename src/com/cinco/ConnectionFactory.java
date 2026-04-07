package com.cinco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;

/**
 * Factory class that produces and closes connections one-by-one.
 */
public class ConnectionFactory {

	private static Logger logger;

	public ConnectionFactory(Logger logger) {
		this.logger = logger;
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			logger.info("Initializing connection");
			conn = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
		} catch (SQLException e) {
			logger.error("Unable to initialize connection", e);
			throw new RuntimeException(e);
		}
		return conn;
	}

	public void putConnection(Connection conn) {
		logger.info("Closing connection");
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
