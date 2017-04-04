/*
 JTable�� ������ �г�
*/
package book;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
public class TablePanel extends JPanel{
	Connection con;
	JTable table;
	JScrollPane scroll;
	TableModel model;
	
	//Vector ArrayList�� �Ѵ� ���� 
	//������ ? ����ȭ ���� ���� 
	Vector list=new Vector();
	Vector<String> columnName=new Vector<String>();
	
	int cols;
	
	public TablePanel(){
		this.con=con;
		//db ���� 
		table = new JTable();
		scroll = new JScrollPane(table);
		this.setLayout(new BorderLayout());
		this.add(scroll);
		
		this.setBackground(Color.PINK);
		setPreferredSize( new Dimension(650, 550));
	}
	
	public void setConnection(Connection con){
		this.con=con;
		
		init();
		
		//���̺� ���� jtable�� ����
		model = new AbstractTableModel() {
			public int getRowCount() {
				return list.size();
			}
			public int getColumnCount() {
				return cols;
			}
			@Override
			public String getColumnName(int col) {
				return columnName.elementAt(col);
			}
			public Object getValueAt(int row, int col) {
				Vector vec=(Vector)list.get(row);	
				return vec.elementAt(col);
			}
		};
		
		//���̺� �� ���� 
		table.setModel(model);
		
	}
	
	//book ���̺��� ���ڵ� ��������
	public void init(){
		String sql="select * from book order by book_id asc";
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			cols=meta.getColumnCount();
			
			//���� ������ ��� ����� 
			list.removeAll(list);
			columnName.removeAll(columnName);
			
			for(int i=0;i<cols;i++){
				columnName.add(meta.getColumnName(i+1));
			}
			
			//rs�� ������ �÷����� DTO�� �Űܴ���!!
			while(rs.next()){
				Vector<String> data = new Vector<String>();
				
				data.add(Integer.toString(rs.getInt("book_id")));
				data.add(rs.getString("book_name"));
				data.add(Integer.toString(rs.getInt("price")));
				data.add(rs.getString("img"));
				data.add(Integer.toString(rs.getInt("subcategory_id")));
				
				list.add(data);//�⺻ ���Ϳ� ���� �߰�
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
}











