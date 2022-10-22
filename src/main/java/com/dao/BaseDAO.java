package com.dao;

import com.utils.JDBCUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: baseDAO
 * @author: Weikang
 * @version: 1.0
 * @date: 29/09/2022 11:11
 */

public abstract class BaseDAO {

    protected Connection connection;
    protected PreparedStatement ps;
    protected ResultSet rs;

    public int update(String sql, Object... args) {
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtils.closeConnection(connection);
            JDBCUtils.closeStatement(ps);
        }
        return 0;
    }

    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        T t = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            if (rs.next()) {
                t = clazz.newInstance();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    Object columnValue = rs.getObject(i + 1);

                    String columnName = rsmd.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeStatement(ps);
            JDBCUtils.closeResultSet(rs);
            JDBCUtils.closeConnection(connection);
        }
        return t;
    }

    public <T> List<T> getInstanceList(Class<T> clazz, String sql, Object ...args) {
        ArrayList<T> objects = new ArrayList<T>();
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    Object columnValue = rs.getObject(i + 1);

                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = null;
                    field = clazz.getDeclaredField(columnLabel);

                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                objects.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeStatement(ps);
            JDBCUtils.closeResultSet(rs);
        }
        return objects;
    }

    public <E> E getValue( String sql, Object ...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();

            if (rs.next()) {
                return (E) rs.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeStatement(ps);
            JDBCUtils.closeResultSet(rs);
        }
        return null;
    }
}
