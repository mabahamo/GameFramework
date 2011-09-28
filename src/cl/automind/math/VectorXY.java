package cl.automind.math;

public class VectorXY {

	public double x,y;
	
	public VectorXY(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString(){
		return "(" + x + "," + y + ") length: " + this.length();
	}

	public VectorXY(VectorXY a) {
		this.x = a.x;
		this.y = a.y;
	}

	public double distance(VectorXY t) {
		double dx = x-t.x;
		double dy = y-t.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public boolean equal(VectorXY t){
		return t.x == this.x && t.y == this.y;
	}
	
	public VectorXY add(VectorXY t){
		VectorXY v = new VectorXY(this.x,this.y);
		v.x+= t.x;
		v.y+= t.y;
		return v;
	}
	

	public VectorXY mul(double d){
		VectorXY v = new VectorXY(this.x,this.y);
		v.x *= d;
		v.y *= d;
		return v;
	}

	
	public double length(){
		return Math.sqrt(x*x + y*y);
	}
	
	public double lengthlength(){
		return x*x+y*y;
	}
	
	public VectorXY sub(VectorXY t){
		VectorXY v = new VectorXY(this.x,this.y);
		v.x -= t.x;
		v.y -= t.y;
		return v;
	}

}
