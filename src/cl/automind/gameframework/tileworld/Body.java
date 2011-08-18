package cl.automind.gameframework.tileworld;

import java.util.Observable;

import cl.automind.math.Coordinate;


public class Body extends Observable{
	
	public int x,y,tx,ty;
	public float sx,sy;
	public static final int SIZE = 20;
	public static final int FPS = 10;
	
	public int heading = 0;
	private boolean enabled = true;
	private boolean moving = false;
	private boolean needCheckForCollisions = true;
	
	//cuantos cuadros puede avanzar en cada direccion
	private int speed = 1;

	public Body(int x, int y) {
		setPosition(x,y);
	}

	public void setEnabled(boolean b) {
		this.enabled = b;
	}
	
	public double distance(Body target) {
		return target.getPosition().distance(this.getPosition());
	}
	
	public Coordinate getPosition() {
		return new Coordinate(x,y);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.sx = this.tx = x*SIZE+10;
		this.sy = this.ty = y*SIZE+10;
	}

	public boolean disabled() {
		return !enabled;
	}

	public int getTileX(){
		return x;
	}
	
	public int getTileY(){
		return y;
	}
	
	public float getScreenX(){
		return sx;
	}
	
	public float getScreenY(){
		return sy;
	}
	
	public void setTileTarget(int x, int y){
		moving = true;
		needCheckForCollisions = true;
		this.tx = x*SIZE+10;
		this.ty = y*SIZE+10;
	}
	
	
	public int getSpeed(){
		return speed;
	}

	public void update() {
		if (disabled()){
			return;
		}
		float dx = this.tx - this.sx;
		float dy = this.ty - this.sy;

		if (dx != 0) {
			this.sx = this.sx + (dx / Math.abs(dx))*getSpeed()*SIZE/FPS;
		}

		if (dy != 0) {
			this.sy = this.sy + (dy / Math.abs(dy))*getSpeed()*SIZE/FPS;
		}
		
//		System.out.println("dx: " + dx + "\tdy: " + dy);

		//al terminar el movimiento seteamos el tile en el que estamos
		if (moving && dx == 0 && dy == 0){
			moving = false;
			this.x = (int)((sx-10)/SIZE);
			this.y = (int)((sy-10)/SIZE);
			setChanged();
			notifyObservers(new BodyPositionEvent(x,y));
		}
	}

	public void moveTo(int x2, int y2) {
		if (Math.abs(x2) > speed){
			x2 = (int)(Math.signum(x2)*speed);
		}
		if (Math.abs(y2) > speed){
			y2 = (int)(Math.signum(x2)*speed);
		}
		System.out.println("moveTo " + x2 + "," + y2);
		setTileTarget(getTileX()+x2,getTileY()+y2);
	}

	public boolean needCheckForCollisions() {
		return needCheckForCollisions;
	}

	public void setNeedCheckCollisions(boolean b) {
		needCheckForCollisions = b;
	}

	

	

	
	
}
