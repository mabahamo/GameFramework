package cl.automind.gameframework;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{

	private static final long serialVersionUID = 5995076224975831178L;
	BufferedImage backgroundImage;
	
	public ImagePanel(String path){
		super();
		try {
			backgroundImage = ImageLoader.getInstance().getImage(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ImagePanel(BufferedImage bi) {
		backgroundImage = bi;
		Dimension d = new Dimension(bi.getWidth(),bi.getHeight());
		setSize(d);
		setPreferredSize(d);
		setMinimumSize(d);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null){
			g.drawImage(backgroundImage, 0, 0, null);
		}
	}
}
