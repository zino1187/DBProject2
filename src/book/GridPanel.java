/*
 JTable�� ������ �г�
*/
package book;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
public class GridPanel extends JPanel{
	public GridPanel() {
		this.setVisible(false);
		this.setBackground(Color.CYAN);
		setPreferredSize( new Dimension(650, 550));
	}
}