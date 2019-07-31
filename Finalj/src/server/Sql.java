package server;

import java.lang.reflect.Array;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.sql.Connection;






public class Sql {

	private static Connection connect; 
	
	
	public static void update_statement(String[] cond,String colName, String table, String val){
		// sql update statement
		String sqlupdate = "UPDATE "+table+" SET "+colName+"=?  WHERE "+cond[0]+"=? ";
		
		try {
			// execute sql query
			PreparedStatement pst = connect.prepareStatement(sqlupdate);
			
			pst.setString(1, val);
			pst.setString(2, cond[1]);

			pst.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void insert_statement(String tableName, String[] cols,String[] values ){
		// sql instet stament
		String col = Arrays.toString(cols).replace("["," ").replace("]"," ");
		// get cols arr and break into string
		String[] vals = new String[values.length];
		// fill array with ? for the sql satement
		Arrays.fill(vals,"?");
		String val =  Arrays.toString(vals).replace("["," ").replace("]"," ");

		// create a string var to save to query
		String sqlInsert = "insert into " + tableName +" ( "+ col +" ) values( "+val+")";
		System.out.println("sql command:" + sqlInsert);
		try {
			// execute sql query
			PreparedStatement pst = connect.prepareStatement(sqlInsert);
			int cnt = 1;
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

	public ResultSet sumQuery(String col ,String table, String[] vals){
		try {
			// PreparedStatement - takes the java code select and replace it with sql code
			PreparedStatement sumStatement = connect.prepareStatement("SELECT SUM("+col+") FROM "+table+" WHERE "+vals[0]+" = ?");
			sumStatement.setString(1, vals[1]);
			ResultSet result = sumStatement.executeQuery();// execute the statement
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
		String password = "";// password of the sqlworkbench
		
		try {
			connect = (Connection) DriverManager.getConnection(host, username, password);
			System.out.println("work");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
}