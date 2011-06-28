package cl.automind.gameframework;

import java.awt.Graphics;

public interface GameLevelInterface {

	public void init();
	
	public void update();
	
	public void render(Graphics g);
	
	@SuppressWarnings("rawtypes")
	public Class nextLevel();
	
	/**
	 * Retorna el path para encontrar el background de este level
	 * @return
	 */
	public String getBackgroundPath();
}
