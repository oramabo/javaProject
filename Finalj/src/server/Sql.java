package server;

import java.lang.reflect.Array;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.mysql.jdbc.Connection;




public class Sql {

	private static Connection connect; 
	
	
	public static void update_statement(String userID,String colName, String table, String val){
		
		String sqlupdate = "UPDATE "+table+" SET "+colName+"=?  WHERE id =? ";
		
		try {
			PreparedStatement pst = connect.prepareStatement(sqlupdate);
			
			pst.setString(1, val);
			pst.setString(2, userID);

			pst.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void insert_statement(String tableName, String[] cols,String[] values ){

		String col = Arrays.toString(cols).replaceAll("["," ").replaceAll("]"," ");
		String[] vals = new String[values.length];
		Arrays.fill(vals,"?");
		String val =  Arrays.toString(vals).replaceAll("["," ").replaceAll("]"," ");

		String sqlInsert = "insert into" + tableName +" ( "+ col +" ) values( "+val+")";
		System.out.println("sql command:" + sqlInsert);
		try {
			PreparedStatement pst = connect.prepareStatement(sqlInsert);
			int cnt = 0;
			for( String value : values){
				pst.setString(cnt++, value);
			}
			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	
	}
	
	public ResultSet selectQuery(String col ,String table, String val){
		try {
			// PreparedStatement - takes the java code select and replace it with sql code
			PreparedStatement statement = connect.prepareStatement("select * from javaProj."+table + " where "+col+"='"+val+"'");
			ResultSet result = statement.executeQuery();// execute the statement
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet selectAll(String table){
		try {
			// PreparedStatement - takes the java code select and replace it with sql code
			PreparedStatement statement = connect.prepareStatement("select * from javaProj."+table);
			ResultSet result = statement.executeQuery();// execute the statement
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void connection()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");// connect to the driver jar file mysql connector 
			System.out.println("Works");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void ConectingToSQL ()
	{
		
		connection();
		String host = "jdbc:mysql://localhost:3306/javaProj";
		String username = "root";//user name
		String password = "";// password of the sqlworkbanch
		
		try {
			connect = (Connection) DriverManager.getConnection(host, username, password);
			System.out.println("work");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
}