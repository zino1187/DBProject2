package calendar;

import java.awt.BorderLayout;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CalendarMain extends JFrame{
	JPanel p_north, p_center;
	JLabel title;
	DateCell[] cell = new DateCell[42];
	Calendar cal=Calendar.getInstance();
	
	public CalendarMain(){
		init();
		p_north =new JPanel();
		p_center = new JPanel();
		title = new JLabel("4¿ù");
		
		for(int i=0;i<cell.length;i++){
			cell[i]=new DateCell();
			p_center.add(cell[i]);
		}
		
		p_north.add(title);
		add(p_north, BorderLayout.NORTH);
		add(p_center);
		
		setSize(750, 700);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void init(){
		int total=cal.get(Calendar.DAY_OF_MONTH);
		System.out.println(total);
	}
	public static void main(String[] args) {
		new CalendarMain();
	}

}
