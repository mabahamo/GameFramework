package cl.automind.gameframework;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class AnimatedSprite extends JComponent {

	private static final long serialVersionUID = 1695387636634869514L;
	private BufferedImage[] bufferImage;
	private int current = 0;
	private volatile boolean running = false;

	public AnimatedSprite(BufferedImage[] image) {
		this.bufferImage = image;
		this.current = 0;
		running = false;
	}
	
	public int getCurrentFrame(){
		return current;
	}
	
	public int getLastFrame() {
		return bufferImage.length-1;
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bufferImage[current], 0, 0,null);
	}
	
	public void start(){
		Thread t = new Thread() {
			public void run() {
				running = true;
				@SuppressWarnings("unused")
				int zeros = 0;
				while (running) {
					current = (current++)%bufferImage.length;
					if (current == 0){
						zeros++;
					}
					repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.setDaemon(true);
		t.setName("AnimatedSprite thread");
		t.start();
	}
}
