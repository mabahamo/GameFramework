package cl.automind.gameframework;
import java.awt.Dimension;

import javax.swing.JFrame;


public class GameEngine {
	
	JFrame frame;
	BackgroundPanel im;
	
	public GameEngine(int width, int height, String title){
		String os = System.getProperty("os.name");
		System.out.println("OS: " + os);
		
		Dimension size = new Dimension(width,height);
		frame = new JFrame(title);
		frame.setMinimumSize(size);	    
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.setVisible(true);
	    frame.setResizable(false);

	    GameManager.getInstance().setMainFrame(frame);
	    
	    Thread t = new Thread(){
	    	public void run(){
	    		SoundManager.getInstance();
	    	}
	    };
	    t.setPriority(Thread.MIN_PRIORITY);
	    t.setDaemon(true);
	    t.setName("inicializador");
	    t.start();
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void startLevel(Class level) {
		GameManager.getInstance().startLevel(level);
	}



	
	
}
