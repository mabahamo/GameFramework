package cl.automind.gameframework.modal;

import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Botonera extends JPanel implements MouseListener {

	private static final long serialVersionUID = 8158391150645965936L;
	private BotoneraListenerInterface bl;
	private StateButton botones[];

	/**
	 * Crea una botonera con varios botones. Cada bot—n puede tener varios
	 * estados y se puede detectar el click derecho y el izquierdo.
	 * 
	 * @param buttons
	 *            cantidad de botones
	 * @param states
	 *            cantidad de estados que posee cada bot—n
	 * @param layout
	 * @param bl
	 * @param decorator
	 */
	public Botonera(int buttons, int states, LayoutManager layout,
			BotoneraListenerInterface bl, BotoneraDecoratorInterface decorator) {
		this.bl = bl;
		this.botones = new StateButton[buttons];
		for (int i = 1; i <= buttons; i++) {
			this.botones[i-1] = new StateButton(i, states, decorator);
			this.botones[i-1].setSize(50, 50);
			add(this.botones[i-1]);
			this.botones[i-1].addMouseListener(this);
		}
		setLayout(layout);
	}
	
	public int getState(int button){
		return this.botones[button-1].getCurrentState();
	}
	

	public void setState(int casilla, int newState) {
		this.botones[casilla-1].setNewState(newState);
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		/**
		 * este mŽtodo detecta mal los clicks, as’ que se mueve la
		 * identificaci—n a mousepressed
		 */
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(final MouseEvent arg0) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				StateButton button = (StateButton) arg0.getSource();
				int number = Integer.parseInt(button.getText());
				button.requestFocus();
				if (arg0.getButton() == MouseEvent.BUTTON1) {
					int newState = bl.leftClick(number, button.getCurrentState());
					button.setNewState(newState);
				}
				if (arg0.getButton() == MouseEvent.BUTTON3) {
					bl.rightClick(number, button.getCurrentState());
				}
			}
		});
		
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

}
