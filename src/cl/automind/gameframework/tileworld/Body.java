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
	protected boolean moving = false;
	private boolean needCheckForCollisions = true;
	
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
		return 2;
	}
	
	protected void checkHeading(float dx, float dy) {
		if (dx == 0 && dy == 0){
			return;
		}
		if (dx != 0){
			heading = (int)(180*Math.atan(Math.abs(dy/dx))/Math.PI);
			if (dx < 0 && dy <= 0){
				heading = 180 + heading;
			}
			if (dx < 0 && dy > 0){
				heading = 90 + heading;
			}
			if (dx > 0 && dy < 0){
				heading = 270 + heading;
			}
			if (dx > 0 && dy == 0){
				heading = 0;
			}
		}
		else {
			if (dy > 0){
				heading = 90;
			}
			else {
				heading = 270;
			}
		}
	}

	public void update(int remainingFrames) {
		if (disabled()){
			return;
		}
		float dx = this.tx - this.sx;
		float dy = this.ty - this.sy;

		if (dx != 0) {
			if (Math.abs(dx) > 1.0*getSpeed()*SIZE/FPS){
				this.sx = this.sx + Math.signum(dx)*getSpeed()*SIZE/FPS;
			}
			else {
				this.sx = this.sx + dx;
			}
		}

		if (dy != 0) {
			if (Math.abs(dy) > 1.0*getSpeed()*SIZE/FPS){
				this.sy = this.sy + Math.signum(sy)*getSpeed()*SIZE/FPS;
			}
			else {
				this.sy = this.sy + dy;
			}			
		}
		//al terminar el movimiento seteamos el tile en el que estamos
		if (remainingFrames == 0 && dx == 0 && dy == 0){
			moving = false;
			this.x = (int)((sx-10)/SIZE);
			this.y = (int)((sy-10)/SIZE);
			setChanged();
			notifyObservers(new BodyPositionEvent(x,y));
		}
		
		checkHeading(dx,dy);
		
	}
	
	public boolean isMoving(){
		return moving;
	}

	public void moveTo(int x2, int y2) {
		if (Math.abs(x2) > getSpeed()){
			x2 = (int)(Math.signum(x2)*getSpeed());
		}
		if (Math.abs(y2) > getSpeed()){
			y2 = (int)(Math.signum(y2)*getSpeed());
		}
		
		setTileTarget(getTileX()+x2,getTileY()+y2);
	}

	public boolean needCheckForCollisions() {
		return needCheckForCollisions;
	}

	public void setNeedCheckCollisions(boolean b) {
		needCheckForCollisions = b;
	}

	

	

	
	
}
