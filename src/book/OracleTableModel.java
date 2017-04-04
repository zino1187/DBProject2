package book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

public class OracleTableModel extends AbstractTableModel{
	String[] column;
	String[][] data;
	Connection con;
	
	public OracleTableModel(Connection con) {
		this.con=con;
		
		String sql="select * from book";
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}		
		}
	}
	
	public int getColumnCount() {
		return 5;
	}
	public int getRowCount() {
		return 7;
	}
	public Object getValueAt(int arg0, int arg1) {
		return "apple";
	}
	
}
