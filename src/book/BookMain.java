package book;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookMain extends JFrame implements ItemListener, ActionListener{
	DBManager manager=DBManager.getInstance();
	Connection con;
	
	JPanel p_west; //좌측 등록폼
	JPanel p_content; //우측 영역 전체 
	JPanel p_north; //우측 선택 모드 영역
	JPanel p_center; //Flow가 적용되어 p_table, p_grid
							//를 모두 존재시켜놓을 컨테이너 역할!!
	JPanel p_table; //JTalbe이  붙여질 패널 
	JPanel p_grid; //그리드 방식으로 보여질 패널 
	Choice ch_top;
	Choice ch_sub;
	JTextField t_name;
	JTextField t_price;
	Canvas can;
	JButton bt_regist;
	CheckboxGroup group;
	Checkbox ch_table, ch_grid;
	Toolkit kit=Toolkit.getDefaultToolkit();
	Image img;
	JFileChooser chooser;
	File file;
	
	//html option과는 다르므로, Choice 컴포넌트
	//의 값을 미리 받아놓자!!
	//이 컬렉션은 rs 객체를 대체할 것이다. 
	//그럼으로써 얻는 장점??
	//더이상 rs.last, rs.getRow 고생하지 말자!! 
	ArrayList<SubCategory> subcategory=new ArrayList<SubCategory>();
	
	public BookMain() {
		p_west = new JPanel();
		p_content = new JPanel();
		p_north = new JPanel();
		p_center = new JPanel();
		
		p_table = new TablePanel();
		p_grid = new GridPanel();
		
		ch_top = new Choice();
		ch_sub = new Choice();
		t_name = new JTextField(12);
		t_price = new JTextField(12);
		
		URL url = this.getClass().getResource("/pica2.jpg");
		try {
			img=ImageIO.read(url);
			System.out.println(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		can = new Canvas(){
			public void paint(Graphics g){
				g.drawImage(img, 0,0, 140, 140, this);
			}
		};
		can.setPreferredSize(new Dimension(150, 150));
		
		bt_regist = new JButton("등록");
		group = new CheckboxGroup();
		ch_table = new Checkbox("테이블형식", true, group);
		ch_grid = new Checkbox("겔러리 형식", false, group);
		
		//파일 추저 올리기 
		chooser = new JFileChooser("C:/html_workspace/images");
		
		
		//초이스 컴포넌트의 크기 폭 조정
		ch_top.setPreferredSize(new Dimension(130, 45));
		ch_sub.setPreferredSize(new Dimension(130, 45));
		
		p_west.setPreferredSize(new Dimension(150, 600));
		p_west.setBackground(Color.WHITE);
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can);
		p_west.add(bt_regist);
		
		//컨텐츠
		p_content.setLayout(new BorderLayout());
		
		//컨텐츠 북쪽
		p_north.add(ch_table);
		p_north.add(ch_grid);
		
		p_center.setBackground(Color.YELLOW);
		p_center.add(p_table);
		p_center.add(p_grid);
		
		p_content.add(p_north, BorderLayout.NORTH);
		p_content.add(p_center);
		
		add(p_west, BorderLayout.WEST);
		add(p_content);
		
		init();
		
		ch_top.addItemListener(this);
		
		can.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				openFile();
			}
		});
		
		bt_regist.addActionListener(this);
		
		//초이스 컴포넌트와 리스너 연결 
		ch_table.addItemListener(this);
		ch_grid.addItemListener(this);

		
		setSize(800,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void init(){
		//초이스 컴포넌트에 최상위 목록 보이기!!
		con=manager.getConnection();
		String sql="select * from topcategory order by topcategory_id asc ";
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				ch_top.add(rs.getString("category_name"));
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
		
		//테이블 패널과 그리드 패널에게 Connection
		//전달 
		((TablePanel)p_table).setConnection(con);
		
	}
	
	//하위 카테고리 가져오기 
	public void getSub(String v){
		//기존에 이미 채워진 아이템이 잇다면, 먼저 싹~~지운다
		ch_sub.removeAll();
		
		StringBuffer sb = new StringBuffer();
		sb.append("select * from subcategory ");
		sb.append(" where topcategory_id=(");
		sb.append(" select topcategory_id from");
		sb.append(" topcategory where category_name='"+v+"') order by subcategory_id asc ");
		
		System.out.println(sb.toString());
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			pstmt=con.prepareStatement(sb.toString());
			rs=pstmt.executeQuery();
			
			//rs에 담겨진 레코드 1개는 SubCategory
			//클래스의 인스턴스 1개로 받자!!
			while(rs.next()){
				SubCategory dto = new SubCategory();
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				dto.setCategory_name(rs.getString("category_name"));
				dto.setTopcategory_id(rs.getInt("topcategory_id"));
				
				subcategory.add(dto);//컬렉션에 담기!!
				ch_sub.add(dto.getCategory_name());
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
	
	//상품 등록 메서드 
	public void regist(){
		//내가 지금 선택한 서브 카테고리 초이스의
		//index를 구해서, 그 index 로 ArrayList를
		//접근하여 객체를 반환 받으면 정보를
		//유용하게 쓸 수 있다.
		int index=ch_sub.getSelectedIndex();
		SubCategory dto=subcategory.get(index);
		
		String book_name=t_name.getText();//책이름
		int price=Integer.parseInt(t_price.getText());
		String img=file.getName();//파일명
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into book(book_id, subcategory_id, book_name, price, img)");
		sb.append(" values(seq_book.nextval,"+dto.getSubcategory_id()+",'"+book_name+"', "+price+" ,'"+img+"')");  
		
		System.out.println(sb.toString());
		
		PreparedStatement pstmt=null;
		try {
			pstmt=con.prepareStatement(sb.toString());
			
			//SQL문이 DML(insert, delete, update) 일 경우
			int result=pstmt.executeUpdate();
			
			//위의 메서드는 숫자값을 반환하며, 이 숫자값은 
			//이 쿼리에 의해 영향을 받는 레코드수를 반환
			//insert의 경우 언제나 ?? 반환될까? 1
			if(result !=0){
				//System.out.println(book_name+"등록 성공");
				copy();
				
				((TablePanel)p_table).init();
				((TablePanel)p_table).table.updateUI();
				
				
			}else{
				System.out.println(book_name+"등록 실패");
			}
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
			
		}
		
	}
	
	public void itemStateChanged(ItemEvent e) {
		Object obj=e.getSource();
		if(obj == ch_top){
			Choice ch=(Choice)e.getSource();
			getSub(ch.getSelectedItem());
		}else if(obj==ch_table){
			p_table.setVisible(true);
			p_grid.setVisible(false);
		}else if(obj==ch_grid){
			p_table.setVisible(false);
			p_grid.setVisible(true);
		}
	}
	
	//그림 파일 불러오기!!
	public void openFile(){
		int result=chooser.showOpenDialog(this);
		
		if(result == JFileChooser.APPROVE_OPTION){
			//선택한 이미지를 canvas에 그릴것이다~!
			file=chooser.getSelectedFile();
			img=kit.getImage(file.getAbsolutePath());
			can.repaint();
		}
	}
	
	/*이미지 복사하기
	 유저가 선택한 이미지를, 개발자 지정한 위치
	 로 복사를 해놓자!!
	*/ 
	public void copy(){
		FileInputStream fis=null;
		FileOutputStream fos=null;
		
		try {
			fis= new FileInputStream(file);
			
			String filename=file.getName();
			String dest="C:/workspace/jsp_workspace/DBProject2/data/"+filename;
			fos= new FileOutputStream(dest);
			
			int data; //읽어들인 데이터가 X, 갯수 
			byte[] b = new byte[1024];
			while(true){
				data=fis.read(b);
				
				if(data==-1)break;
				
				fos.write(b);
			}
			JOptionPane.showMessageDialog(this, "등록완료");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		regist();
		
	}
	public static void main(String[] args) {
		new BookMain();
	}

}





