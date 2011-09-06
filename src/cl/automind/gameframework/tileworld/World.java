package cl.automind.gameframework.tileworld;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Vector;

import cl.automind.math.Coordinate;


public class World implements Observer{
	private final static boolean DRAW_GRID = false;
	private int w,h;
	private Vector<Body> list = new Vector<Body>();
	private BodyDecorator decorator;
	private Random r = new Random(42);
//	private long time = System.currentTimeMillis();
	private CollisionListener collisionListener;
	
	public World(int i, int j) {
		this.w = i;
		this.h = j;
	}
	
	public void addDecorator(BodyDecorator decorator){
		this.decorator = decorator;
	}

	public void addListener(CollisionListener collisionListener) {
		this.collisionListener = collisionListener;
	}

	public void add(Body p) {
		list.add(p);
		p.addObserver(this);
		System.out.println("Players on world " + list.size());
	}
	
	public Iterator<Body> getIterator(){
		return list.iterator();
	}

	public void update(int remainingFrames) {
		for(int i=0;i<list.size();i++){
			Body b = list.get(i);
			if (!b.disabled()){
				b.update(remainingFrames);
			}
		}
	}

	
	public void draw(Graphics g) {
		if (DRAW_GRID){
			g.setColor(Color.BLACK);
			for(int i=0;i<h;i++){
				g.drawLine(0, i*20, w*20, i*20);
				for(int j=0;j<w;j++){
					g.drawLine(j*20, 0, j*20,h*20);
				}
			}
		}	
		
		Graphics2D g2 = (Graphics2D)g;
		for(int i=0;i<list.size();i++){
			Body b = list.get(i);
			if (!b.disabled() && decorator != null){
				decorator.paint(g2,b,this);
			}
		}
	}

	public Coordinate randomEmptyPosition() {
		if (list.size() >= w*h){
			return null;
		}
		while(true){
			Coordinate candidate = new Coordinate(r.nextInt(w),r.nextInt(h));
			if (!isPlayerOnTile(candidate)){
				return candidate;
			}
		}
	}

	private boolean isPlayerOnTile(Coordinate candidate) {
		return false;
	}

	@Override
	public void update(Observable o, Object arg) {
//		if (arg instanceof BodyPositionEvent){
//			checkCollissions((Body)o);
//			
//		}
	}

	private void checkCollissions(Body target) {
		if (collisionListener == null){
			return;
		}
		for(int i=0;i<list.size();i++){
			Body b = list.get(i);
			if (!b.disabled() && b != target){
				if (b.x == target.x && b.y == target.y){
					collisionListener.collisionOccured(new CollisionEvent(target,b));
				}
			}
		}
	}

	public void checkCollisions() {
		for(int i=0;i<list.size();i++){
			Body b = list.get(i);
			if (!b.disabled() && b.needCheckForCollisions()){
				//solamente se chequea el agente si es que se ha movido
				checkCollissions(b);
				b.setNeedCheckCollisions(false);
			}
		}
	}

	public int getWidth(){
		return w;
	}
	
	public int getHeight(){
		return h;
	}


}
