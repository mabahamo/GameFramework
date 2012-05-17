package cl.automind.gameframework.modal;

import java.awt.Graphics;

import javax.swing.JButton;


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
	
	public void setNewState(int state) {
		this.currentState = state;
		this.repaint(); 
//		System.out.println("New buttonState " + casilla + " : " + currentState);
	}

}
