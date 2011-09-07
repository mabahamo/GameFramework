package cl.automind.gameframework.tileworld;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Hashtable;
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
	private Hashtable<Integer,Vector<Body>> counter = new Hashtable<Integer,Vector<Body>>();
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
		if (!counter.containsKey((Integer)p.getType())){
			counter.put((Integer)p.getType(),new Vector<Body>());
		}
		Vector<Body> v = counter.get((Integer)p.getType());
		v.add(p);
		
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
		if (arg instanceof BodyDisabledEvent){
			disable((Body)o);
		}
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
	
	private void disable(Body b){
		System.out.println("Destroy " + b);
		Vector<Body> v = counter.get((Integer)b.getType());
		v.remove(b);
		
	}

	public int getPopulation(int bodyType) {
		Vector<Body> v = counter.get((Integer)bodyType);
		return v.size();
	}

	public Vector<Body> getBodyByType(int bodyType) {
		return counter.get((Integer)bodyType);
	}

	/**
	 * Retorna la m’nima distancia en un mundo toroidal
	 * @param p
	 * @param test
	 * @return
	 */
	public double distance(Body p, Body test) {
		Coordinate o = p.getPosition(); 
		Coordinate a = test.getPosition();
		Coordinate b = translate(a,getWidth(),0);
		Coordinate c = translate(a,-1*getWidth(),0);
		Coordinate d = translate(a,0,getHeight());
		Coordinate e = translate(a,0,-1*getHeight());
		
		double da = o.distance(a);
		double db = o.distance(b);
		double dc = o.distance(c);
		double dd = o.distance(d);
		double de = o.distance(e);

		return Math.min(Math.min(da, db), Math.min(dc, Math.min(dd, de)));
	}

	private Coordinate translate(Coordinate a, int dx, int dy){
		Coordinate aux = new Coordinate(a);
		aux.x += dx;
		aux.y += dy;
		return aux;
	}

	/**
	 * Retorna la posici—n mas cercana entre los cuerpos p y t considerando un mundo toroidal
	 * @param p
	 * @param t
	 * @return
	 */
	public Coordinate getPosition(Body p, Body target) {
		Coordinate o = p.getPosition(); 
		Coordinate a = target.getPosition();
		Coordinate b = translate(a,getWidth(),0);
		Coordinate c = translate(a,-1*getWidth(),0);
		Coordinate d = translate(a,0,getHeight());
		Coordinate e = translate(a,0,-1*getHeight());
		double da = o.distance(a);
		double db = o.distance(b);
		double dc = o.distance(c);
		double dd = o.distance(d);
		double de = o.distance(e);
		double minDistance = Math.min(Math.min(da, db), Math.min(dc, Math.min(dd, de)));
		
		if (minDistance == da){
			return a;
		}
		if (minDistance == db){
			return b;
		}
		if (minDistance == dc){
			return c;
		}
		if (minDistance == dd){
			return d;
		}
		if (minDistance == de){
			return e;
		}
		
		return a;
	}
	
	/**
	 * Retorna un vector con todos los agentes que son del tipo indicado y que estan en la vecindad de p 
	 * @param bodyType
	 * @param p 
	 * @param distance radio de la vecindad
	 * @return
	 */
	public Vector<Body> getNear(int bodyType, Body p, int distance) {
		Vector<Body> type = counter.get((Integer)bodyType);
		Vector<Body> aux = new Vector<Body>();
		for(Body test: type){
			if (!test.disabled()){
				double td = distance(p,test);
				if ((int)td <= distance){
					aux.add(test);
				}
			}
		}
		return aux;
	}
	

}
