package cl.automind.gameframework;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class BackgroundPanel extends JPanel {

	private static final long serialVersionUID = -3428064067152448503L;
	private Image img;

	public BackgroundPanel(String img) {
		this(new ImageIcon(img).getImage());
	}

	public BackgroundPanel(Image img) {
		this.img = img;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}

}