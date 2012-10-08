package cl.automind.gameframework.modal;

import java.awt.Graphics;

import javax.swing.JButton;

import cl.automind.gameframework.SoundManager;


public class StateButton extends JButton {
	
	private static final long serialVersionUID = -7859062780536119896L;
//	private int states, casilla;
	private int currentState = 0;
	private BotoneraDecoratorInterface decorator;
	
	public StateButton(int casilla, int states, BotoneraDecoratorInterface decorator){
		super("" + casilla);
//		this.states = states;
//		this.casilla = casilla;
		this.decorator = decorator;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (decorator != null){
			decorator.draw(this,g);
		}
	}
	
	public int getCurrentState(){
		return currentState;
	}

	public final static int PLOMO = 0;
	public final static int NEGRO = 1;
	public final static int BLANCO = 2;
	public final static int REGLA = 3;
	public final static int REGLA_NEGRO = 4;
	public final static int REGLA_BLANCO = 5;
	public void setNewState(int state) {
		SoundManager.getInstance().playFromJar("/cl/metaforas/sorpresasmagicas/sounds/" + (state > 2? "switch": "menu_select_1") + ".mp3", false);
		this.currentState = state;
		this.repaint(); 
//		System.out.println("New buttonState " + casilla + " : " + currentState);
	}

}
