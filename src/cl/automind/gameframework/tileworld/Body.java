package cl.automind.gameframework.tileworld;

import java.util.Observable;

import cl.automind.math.VectorXY;


public abstract class Body extends Observable{
	
	public int x,y,tx,ty;
	public float sx,sy;
	public static final int SIZE = 20;
	public static final int FPS = 10;
	
	public int heading = 0;
	private boolean enabled = true;
	protected boolean moving = false;
	private boolean needCheckForCollisions = true;
	private int type;
	private int worldWidth = 0;
	private int worldHeight = 0;
	double energy;
	private int startEnergy;
	private static int counter = 1;
	protected int id = 0;
	
	/**
	 * Inicializa un nuevo agente.
	 * @param x
	 * @param y
	 * @param type tipo de agente
	 */
	public Body(int x, int y, int type, int startEnergy) {
		setPosition(x,y);
		this.type = type;
		this.energy = this.startEnergy = startEnergy;
		this.id = Body.counter++;
	}
	
	public int getStartEnergy() {
		return this.startEnergy;
	}

	public void setEnabled(boolean b) {
		this.enabled = b;
		if (b == false){
			setChanged();
			notifyObservers(new BodyDisabledEvent());
		}
	}
	
	public VectorXY getPosition() {
		return new VectorXY(x,y);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.sx = this.tx = x*SIZE+10;
		this.sy = this.ty = y*SIZE+10;
	}

	public void setPosition(VectorXY v) {
		setPosition((int)v.x, (int)v.y);
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
	
	
	public int getMaxSpeed(){
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
		if (disabled() || !moving){
			return;
		}
		float dx = this.tx - this.sx;
		float dy = this.ty - this.sy;
		

		if (dx != 0) {
			if (Math.abs(dx) > 1.0*getMaxSpeed()*SIZE/FPS){
				this.sx = this.sx + Math.signum(dx)*getMaxSpeed()*SIZE/FPS;
			}
			else {
				this.sx = this.sx + dx;
			}
		}

		if (dy != 0) {
			if (Math.abs(dy) > 1.0*getMaxSpeed()*SIZE/FPS){
				this.sy = this.sy + Math.signum(dy)*getMaxSpeed()*SIZE/FPS;
			}
			else {
				this.sy = this.sy + dy;
			}			
		}
		
		
		
		//toro magic!
		if (worldWidth > 0 && worldHeight > 0){
			if (this.sx > worldWidth*SIZE){
				this.sx = this.sx - worldWidth*SIZE;
				this.tx = this.tx - worldWidth*SIZE;
			}
			if (this.sx < 0){
				this.sx = worldWidth*SIZE + this.sx;
				this.tx = worldWidth*SIZE + this.tx;
			}
			if (this.sy > worldHeight*SIZE){
				this.sy = this.sy - worldHeight*SIZE;
				this.ty = this.ty - worldHeight*SIZE;
			}
			if (this.sy < 0){
				this.sy = worldHeight*SIZE + this.sy;
				this.ty = worldHeight*SIZE + this.ty;
			}
		}
		
		//al terminar el movimiento seteamos el tile en el que estamos		
		if (remainingFrames == 0){
			moving = false;
			this.x = (int)((sx-10)/SIZE);
			this.y = (int)((sy-10)/SIZE);
			setPosition(x,y);
			setChanged();
			notifyObservers(new BodyPositionEvent(x,y));
		}
		
		checkHeading(dx,dy);
	}
	
	public boolean isMoving(){
		return moving;
	}

	public void moveTo(int x2, int y2) {
		if (x2 == 0 && y2 == 0){
			return;
		}
		if (Math.abs(x2) > getMaxSpeed()){
			x2 = (int)(Math.signum(x2)*getMaxSpeed());
		}
		if (Math.abs(y2) > getMaxSpeed()){
			y2 = (int)(Math.signum(y2)*getMaxSpeed());
		}
		setTileTarget(getTileX()+x2,getTileY()+y2);
	}

	public boolean needCheckForCollisions() {
		return needCheckForCollisions;
	}

	public void setNeedCheckCollisions(boolean b) {
		needCheckForCollisions = b;
	}

	public int getType() {
		return type;
	}

	public void setWorldSize(int w, int h) {
		this.worldWidth = w;
		this.worldHeight = h;
	}

	public void addEnergy(double d) {
		setEnergy(getEnergy() + d);
	}

	public double getEnergy() {
		return this.energy;
	}

	public void setEnergy(double d) {
		if (d > getMaxEnergy()){
			d = getMaxEnergy();
		}
		this.energy = d;
		if (this.energy <= 0){
			setEnabled(false);
			
		}
	}
	
	abstract public int getMaxEnergy();

	public int getId() {
		return id;
	}
	
	
}
