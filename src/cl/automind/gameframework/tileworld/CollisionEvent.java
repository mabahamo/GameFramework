package cl.automind.gameframework.tileworld;

public class CollisionEvent {
	
	private Body a,b;

	public CollisionEvent(Body a, Body b) {
		this.a = a;
		this.b = b;
	}

	public Body getBodyA() {
		return this.a;
	}

	public Body getBodyB() {
		return this.b;
	}

}
