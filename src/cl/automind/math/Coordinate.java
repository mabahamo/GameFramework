package cl.automind.math;

public class Coordinate {

	public int x,y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate(Coordinate a) {
		this.x = a.x;
		this.y = a.y;
	}

	public double distance(Coordinate t) {
		double dx = x-t.x;
		double dy = y-t.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public boolean equal(Coordinate t){
		return t.x == this.x && t.y == this.y;
	}


}
