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
	private static final int BODY_SIZE = 20;
	private int w,h;
	private ConcurrentLinkedQueue<Body> list = new ConcurrentLinkedQueue<Body>();
	private BodyDecorator decorator;
	private Random r = new Random(42);
	private CollisionListener collisionListener;
	private PlayerListenerInterface playerListener;
	
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
		if (playerListener != null){
			playerListener.newPlayer(p);
		}
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
	
	private VectorXY toLocal(VectorXY vector){
		VectorXY s = new VectorXY(vector);
			if (s.x > w*getBodySize()){
				s.x = s.x - w*getBodySize();
			}
			if (s.x < 0){
				s.x = w*getBodySize() + s.x;
			}
			if (s.y > h*getBodySize()){
				s.y = s.y - h*getBodySize();
			}
			if (s.y < 0){
				s.y = h*getBodySize() + s.y;
			}
			return s;
	}

	private boolean existPlayerOnTile(VectorXY candidate) {
		for(Body b: list){
			if (!b.disabled() && b.getPosition().equal(toLocal(candidate))){
				return true;
			}
		}
		return false;
	}
	
	private int getBodySize(){
		return BODY_SIZE;
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
		if (playerListener != null){
			playerListener.destroy(b);
		}
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
		return getVector(p,test).length();
	}

	/**
	 * Retorna el vector de distancia m’nima en un mundo toroidal
	 * @param from
	 * @param to
	 * @return
	 */
	public VectorXY getVector(Body from, Body to) {
		VectorXY o = from.getPosition(); 
		VectorXY[] t = new VectorXY[5];
		VectorXY a = to.getPosition();
		t[0] = a;
		t[1] = translate(a,getWidth(),0);
		t[2] = translate(a,-1*getWidth(),0);
		t[3] = translate(a,0,getHeight());
		t[4] = translate(a,0,-1*getHeight());
		
		for(int i=0;i<5;i++){
			t[i] = t[i].sub(o);
		}
	
		double min = Double.MAX_VALUE;
		VectorXY candidate = a;
		for(int i=0;i<5;i++){
			double d = t[i].length();
			if (d < min){
				min = d;
				candidate = t[i];
			}
		}
		return candidate;
	}
	
	
	
	
	private VectorXY translate(VectorXY a, int dx, int dy){
		VectorXY aux = new VectorXY(a);
		aux.x += dx;
		aux.y += dy;
		return aux;
	}

	/**
	 * Retorna un vector con todos los agentes que son del tipo indicado y que estan en la vecindad de p 
	 * @param bodyType
	 * @param p 
	 * @param distance radio de la vecindad
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getNear(int bodyType, Body p, int distance) {
		ArrayList aux = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			Body test = (Body)it.next();
			if (p == null && distance == -1){
				if (test.getType() == bodyType && !test.disabled()){
					aux.add(test);
				}
			}
			else {
				if (test.getType() == bodyType && !test.disabled() && !p.equals(test)){
					double td = distance(p,test);
					if ((int)td <= distance){
						aux.add(test);
					}
				}
			}
		}
		return aux;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getIteratorByType(int type) {
		return getNear(type,null,-1);
	}
	


	public VectorXY getRandomNearPosition(VectorXY b) {
		VectorXY c = new VectorXY(b);
		if (!existPlayerOnTile(c)){
			return toLocal(c);
		}
		for(int i=1;i<getWidth();i++){
			c.x = b.x + i;	
			if (!existPlayerOnTile(c)){
				return toLocal(c);
			}
			c.x = b.x - i;
			if (!existPlayerOnTile(c)){
				return toLocal(c);
			}
			c.y = b.y + i;
			if (!existPlayerOnTile(c)){
				return toLocal(c);
			}
			c.y = b.y - i;
			if (!existPlayerOnTile(c)){
				return toLocal(c);
			}
		}
		System.out.println("Error no pude entregar coordenada cerca de " + b);
		return b;
	}

	public Iterator<Body> getIterator() {
		return list.iterator();
	}
	
	public void addPlayerListener(PlayerListenerInterface pli) {
		Iterator<Body> it = getIterator();
		//notificamos al listener de todos los jugadores que ya existen
		while(it.hasNext()){
			Body b = it.next();
			pli.newPlayer(b);
		}
		this.playerListener = pli;		
	}

}
