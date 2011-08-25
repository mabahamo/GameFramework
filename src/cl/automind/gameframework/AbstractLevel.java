package cl.automind.gameframework;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

public abstract class AbstractLevel extends JPanel implements GameLevelInterface{

	private static final long serialVersionUID = 6133661711376549624L;
	private volatile boolean running = false;
	private volatile BufferedImage backgroundImage;
	private volatile boolean startToEnd = false;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null){
			g.drawImage(backgroundImage, (this.getWidth() - backgroundImage.getWidth())/2, (this.getHeight() - backgroundImage.getHeight())/2, null);
		}
		render(g);
	}
	
	private void runLevel(){
		while(running){
			update();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startLevel(){
		if (getBackgroundPath() != null) {
			try {
				backgroundImage = ImageLoader.getInstance().getImage(getBackgroundPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			repaint();
		}
		running = true;
		Thread t = new Thread(){
			public void run(){
				runLevel();
				
			}
		};
		t.start();
	}
	
	public void endLevel(){
		running = false;
		if (!startToEnd){
			//se recibe solamente una peticion por nivel
			startToEnd = true;
			GameManager.getInstance().goToNextLevel();
		}

		
	}

	public void destroy() {
		running = false;
		backgroundImage = null;
	}
	
//	private void gameRender(){
//		if (this.getWidth() == 0){
//			//cuando la ventana aun no se dibuja
//			return;
//		}
//		if (bufferImage == null){
//			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//			GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
//			bufferImage = gc.createCompatibleImage(this.getWidth(), this.getHeight(), Transparency.OPAQUE);
//			if (bufferImage == null){
//				System.out.println("Error al crear imagen");
//				return;
//			}
//			dbg = bufferImage.getGraphics();
//		}
//		render(dbg);
//	}
//	
//	private void paintScreen(){
//		Graphics g;
//		try {
//			g = this.getGraphics();
//			if (g!= null && bufferImage != null){
//				g.drawImage(bufferImage, 0,0,null);
//			}
//			Toolkit.getDefaultToolkit().sync();
//			g.dispose();
//		}
//		catch(Exception ex){
//			ex.printStackTrace();
//		}
//	}
//	
}
