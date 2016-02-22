
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class dbconnection {
	private Connection myConn;
	
	public dbconnection() throws Exception
	{
		Properties props = new Properties();
		props.load(new FileInputStream("gamescores.properties"));
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
		myConn = DriverManager.getConnection(dburl, user, password);
		
	}
	public void insertquery(String name,int score,String size,int level)
	{
		PreparedStatement myStmt=null;
		try {
			myStmt=myConn.prepareStatement("CALL addnewrecord(?,?,?,?);");
			myStmt.setString(1,name);
			myStmt.setInt(2,score);
			myStmt.setString(3,size);
			myStmt.setInt(4,level);
			myStmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public JTable showquery()
	{
		ResultSet rs;
		try {
			Statement myStmt=myConn.createStatement();
			rs=myStmt.executeQuery("select * from listscores");
			JTable table=new JTable(buildTableModel(rs));
			return table;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
		
	}
	public static DefaultTableModel buildTableModel(ResultSet rs)
	        throws SQLException {
	    ResultSetMetaData metaData = rs.getMetaData();
	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	   columnNames.add("Name");
	   columnNames.add("Turns");
	   columnNames.add("Game Size");
	   columnNames.add("Allowed Moves");
	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }
	    return new DefaultTableModel(data, columnNames);
	}
}