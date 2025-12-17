package utils;

import java.sql.*;

public class Connect {
	
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE = "databarang";
	private final String HOST = "localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

	public ResultSet rs;
	public ResultSetMetaData rsm;
	private Connection con;
	private Statement st;
	private PreparedStatement pst;
	
	private static Connect connect = new Connect();
	
	public static Connect getInstance() {
		
		return connect;
		
	}
	
	private Connect() {
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
		st = con.createStatement();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	}
	
	public Connection getConnection() {
		return con;
		
	}
	
	public ResultSet execQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			System.out.println("");
			e.printStackTrace();
		}
		return rs;
	}
	
	public PreparedStatement execUpdate(String query) {
		try {
			
			pst = con.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pst;
	}
}
