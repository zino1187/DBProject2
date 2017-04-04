package calendar;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class DateCell extends Canvas{
	public DateCell() {
		this.setBackground(Color.YELLOW);
		this.setPreferredSize(new Dimension(100, 100));
	}
	@Override
	public void paint(Graphics g) {
		g.drawString("1¿œ", 0, 20);
	}
}
