package cl.automind.gameframework;

import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameManager {

	private static GameManager instance = new GameManager();
	
	private JFrame mainFrame;
	private AbstractLevel currentLevel;
	private HashMap<String, Object> hash = new HashMap<String, Object>();
	
	private GameManager(){
		
	}
	
	public static GameManager getInstance(){
		return instance;
	}
	
	public void goToNextLevel() {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				try {
					@SuppressWarnings("unchecked")
					Class<AbstractLevel> next = currentLevel.nextLevel();
					mainFrame.remove(currentLevel);
					currentLevel.destroy();
					currentLevel = next.newInstance();
					currentLevel.init();
					currentLevel.startLevel();
					mainFrame.add(currentLevel);
					mainFrame.validate();
				} catch (InstantiationException ex) {
					ex.printStackTrace();
				} catch (IllegalAccessException ex) {
					ex.printStackTrace();
				}
			}
		});

	}

	public void setMainFrame(JFrame frame) {
		this.mainFrame = frame;
	}
	
	public JFrame getMainFrame(){
		return this.mainFrame;
	}

	public void startLevel(Class<AbstractLevel> level) {
		try {
			currentLevel = level.newInstance();
			currentLevel.init();
			currentLevel.startLevel();
			mainFrame.add(currentLevel);
			mainFrame.validate();
		}
		catch(InstantiationException ex){
			ex.printStackTrace();
		}
		catch(IllegalAccessException ex){
			ex.printStackTrace();
		}		
		
	}

	public void store(String key, Object value) {
		hash.put(key, value);
	}

	public Object get(String key) {
		return hash.get(key);
	}
	
}
