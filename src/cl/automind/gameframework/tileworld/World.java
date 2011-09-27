package cl.automind.gameframework.tileworld;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import cl.automind.math.VectorXY;


public class World implements Observer{
	private final static boolean DRAW_GRID = false;
	private int w,h;
	private ConcurrentLinkedQueue<Body> list = new ConcurrentLinkedQueue<Body>();
	private BodyDecorator decorator;
	private Random r = new Random(42);
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
		p.setWorldSize(this.w,this.h);
		list.add(p);
		p.addObserver(this);
		System.out.println("Players on world " + list.size());
	}
	
	
	public void update(int remainingFrames) {
		Iterator<Body> it = list.iterator();
		while(it.hasNext()){
			Body b = it.next();
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
		Iterator<Body> it = list.iterator();
		while(it.hasNext()){
			Body b = it.next();
			if (!b.disabled() && decorator != null){
				decorator.paint(g2,b,this);
			}			
		}
	}

	public VectorXY randomEmptyPosition() {
		if (list.size() >= w*h){
			return null;
		}
		while(true){
			VectorXY candidate = new VectorXY(r.nextInt(w),r.nextInt(h));
			if (!existPlayerOnTile(candidate)){
				return candidate;
			}
		}
	}

	private boolean existPlayerOnTile(VectorXY candidate) {
		for(Body b: list){
			if (!b.disabled() && b.getPosition().equal(candidate)){
				return true;
			}
		}
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
		Iterator<Body> it = list.iterator();
		while(it.hasNext()){
			Body b = it.next();
				if (!b.disabled() && b != target){
					if (b.x == target.x && b.y == target.y){
						collisionListener.collisionOccured(new CollisionEvent(target,b));
					}
				}
			}			
	}

	public void checkCollisions() {
		Iterator<Body> it = list.iterator();
		while(it.hasNext()){
			Body b = it.next();
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
		
//		Vector<Body> v = counter.get((Integer)b.getType());
//		v.remove(b);
//		list.remove(b);
		
	}

	public int getPopulation(int bodyType) {
		int i = 0;
		Iterator<Body> it = list.iterator();
		while(it.hasNext()){
			Body b = it.next();
			if (b.getType() == bodyType && !b.disabled()){
				i++;
			}
		}
		return i;
	}


	/**
	 * Retorna la m’nima distancia en un mundo toroidal
	 * @param p
	 * @param test
	 * @return
	 */
	public double distance(Body p, Body test) {
		VectorXY o = p.getPosition(); 
		VectorXY a = test.getPosition();
		VectorXY b = translate(a,getWidth(),0);
		VectorXY c = translate(a,-1*getWidth(),0);
		VectorXY d = translate(a,0,getHeight());
		VectorXY e = translate(a,0,-1*getHeight());
		
		double da = o.distance(a);
		double db = o.distance(b);
		double dc = o.distance(c);
		double dd = o.distance(d);
		double de = o.distance(e);

		return Math.min(Math.min(da, db), Math.min(dc, Math.min(dd, de)));
	}

	private VectorXY translate(VectorXY a, int dx, int dy){
		VectorXY aux = new VectorXY(a);
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
	public VectorXY getPosition(Body p, Body target) {
		VectorXY o = p.getPosition(); 
		VectorXY a = target.getPosition();
		VectorXY b = translate(a,getWidth(),0);
		VectorXY c = translate(a,-1*getWidth(),0);
		VectorXY d = translate(a,0,getHeight());
		VectorXY e = translate(a,0,-1*getHeight());
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
	@SuppressWarnings("rawtypes")
	public ArrayList getNear(int bodyType, Body p, int distance) {
		ArrayList aux = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			Body test = (Body)it.next();
			if (test.getType() == bodyType && !test.disabled() && !p.equals(test)){
				double td = distance(p,test);
				if ((int)td <= distance){
					aux.add(test);
				}
			}
		}
		return aux;
	}



	public VectorXY getRandomNearPosition(VectorXY b) {
		VectorXY c = new VectorXY(b);
		if (!existPlayerOnTile(c)){
			return c;
		}
		for(int i=1;i<40;i++){
			c.x = b.x + i;	
			if (!existPlayerOnTile(c)){
				return c;
			}
			c.x = b.x - i;
			if (!existPlayerOnTile(c)){
				return c;
			}
			c.y = b.y + i;
			if (!existPlayerOnTile(c)){
				return c;
			}
			c.y = b.y - i;
			if (!existPlayerOnTile(c)){
				return c;
			}
		}
		System.out.println("Error no pude entregar coordenada cerca de " + b);
		return b;
	}

	public Iterator<Body> getIterator() {
		return list.iterator();
	}
	

}
