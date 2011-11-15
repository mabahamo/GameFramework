package cl.automind.gameframework.tileworld;

import cl.automind.gameframework.BodyFactoryInterface;

public class BodyFactory implements BodyFactoryInterface{

	private World world;
	private static int id = 1;
	
	public BodyFactory(World world){
		this.world = world;
	}
	
//	public AbstractBody createBody(int x, int y, int type, int startEnergy) throws InstantiationException, IllegalAccessException {
//		AbstractBody b = new AbstractBody(x,y,type,startEnergy);
//		b.setId(BodyFactory.id++);
//		b.setSize(world.getBodySize());
//		b.setPosition(world.localCoordinates(x,y));
//		b.setWorldSize(world.getWidth(),world.getHeight());
//		world.add(b);
//		return b;
//	}
}
