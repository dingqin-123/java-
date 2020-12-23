package com.jiayu.utils;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.jiayu.constants.Contants;

import java.sql.Connection;

public class JDBCUtils {  
	
	
	public static void main(String[] args) throws Exception {
		
	}
	
	
	//连接数据库
	public static Connection getConnection(){
		//定义数据库连接对象
		Connection conn=null;
		try {
			//你导入的数据库驱动包，mysql
			//jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8
			//jdbc:数据库名称://数据库IP  :端口  
			conn=DriverManager.getConnection(
					Contants.JDBC_URL,
					Contants.JDBC_USER,
					Contants.JDBC_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	//关闭连接
	public static void close(Connection conn){
		try {
			if(conn!=null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
