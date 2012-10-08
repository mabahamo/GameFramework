package cl.automind.gameframework;

import java.util.Observable;

import javax.swing.JLabel;

public class Clock extends Observable {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	private volatile long targetTime = System.currentTimeMillis() + (99 * 60 + 59)*1000;
	private volatile boolean running = false;
	private Object lock = new Object();
	private static Clock instance = new Clock();
	private JLabel label;
	private boolean ready = false;

	
	private Clock(){
		label = new JLabel("Tiempo:  99:59");
		label.setVisible(false);
		running = false;
		Thread t = new Thread() {
			public void run() {
				running = true;
				update();
			}
		};
		t.setDaemon(true);
		t.setName("Clock Thread");
		t.start();
	}
	
	private String updateText(){
		long delta = getTime();
		int minutes = (int)(delta/60);
		int seconds = (int)(delta%60);
		String aux = "";
		if (minutes < 10){
			aux += "0";
		}
		aux += minutes + ":";
		if (seconds < 10){
			aux += "0";
		}
		aux += seconds;
		return  "Tiempo : " + aux;
	}
	
	private void update(){
		while (running) {
			label.setText(updateText());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setTime(int time){
		synchronized (lock) {
			this.targetTime = System.currentTimeMillis() + time * 1000;
		}
		if (!ready){
			label.setText(updateText());
			label.setVisible(true);
			ready = true;
		}
	}

	public static Clock getInstance(){
		return instance;
	}

	public JLabel getLabel() {
		return label;
	}

	public int getTime() {
		long delta;
		synchronized (lock) {
			delta = (targetTime - System.currentTimeMillis())/1000;
		}
		if (delta <= 0){
			delta = 0;
			setChanged();
			notifyObservers();
		}
		return (int)delta;
	}

	public boolean isSync() {
		return ready;
	}

	public void setVisible(boolean b) {
		System.out.println("Se cambia la visibilidad del reloj por " + Thread.currentThread().getName());
		label.setVisible(b);
	}
	
}
