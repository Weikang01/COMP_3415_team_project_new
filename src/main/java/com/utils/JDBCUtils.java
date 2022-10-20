package com.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @Description: TODO
 * @author: Weikang
 * @version: 1.0
 * @date: 29/09/2022 10:59
 */

public class JDBCUtils {
    public final static String USER = "root";
    public final static String PASSWORD = "1234";
    public final static String URL = "jdbc:mysql://localhost:3306/team_project";
    public final static String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        // TODO: [Path error] ClassLoader.class.getResourceAsStream cannot find jdbc.properties
//        InputStream is = ClassLoader.class.getResourceAsStream("jdbc.properties");
//        Properties prop = new Properties();
//        prop.load(is);
//        String user = prop.getProperty("user");
//        String password = prop.getProperty("password");
//        String url = prop.getProperty("url");
//        String driverClass = prop.getProperty("driverClass");
//        Class.forName(driverClass);
//        return DriverManager.getConnection(url, user, password);

        Class.forName(DRIVER_CLASS);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeStatement(Statement st) {
        try {
            if (st != null)
                st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
