package com.sam.mysql;

/********
 * 
 * this cannot be run !
 * 
 * ****/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCMysql {

	/**
	 * @param args
	 */
	public static final String DBDriver ="org.git.mm.mysql.Driver";
	public static final String DBURL = "jdbc:mysql://localhost:3306/orcl";
	public static final String username = "root";
	public static final String password = "123";
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement ps = null;
		String name = "sam";
		int age = 30;
		String sql = "insert into person(name,age) values (?,?)";
		System.out.println(sql);
		
		Class.forName(DBDriver);
		connection = DriverManager.getConnection(DBURL, username, password);
		ps = connection.prepareStatement(sql);
		ps.setString(1, name);
		ps.setInt(2, age);
		ps.executeUpdate();
		ps.close();
		connection.close();
		
		
	}

}
