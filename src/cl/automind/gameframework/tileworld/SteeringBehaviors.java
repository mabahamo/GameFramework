package cl.automind.gameframework.tileworld;

import java.util.Vector;

import cl.automind.math.Coordinate;
import cl.automind.math.Vector2D;


public class SteeringBehaviors {


	
	public static Vector<Vector2D> vectoresPonderados(Body p, Vector<Body> target){
		Vector<Vector2D> aux = new Vector<Vector2D>();

		for(int i=0;i<target.size();i++){
			Body t = target.get(i);
			Coordinate tpos = t.getPosition();
			Coordinate ppos = p.getPosition();
			Vector2D vtpos = new Vector2D(tpos);
			Vector2D vppos = new Vector2D(ppos);
			vtpos = vtpos.sub(vppos);
			System.out.println("antes de normalizar " + vtpos);

			aux.add(vtpos);
		}
		
		double maxc = 0;
		for(int i=0;i<aux.size();i++){
			Vector2D v = aux.get(i);
			double l = v.length();
			if (l > 0){
				v.x = v.x/(l*l);
				v.y = v.y/(l*l);
				l = v.length();
				if (l > maxc){
					maxc = l;
				}
			}
			System.out.println("Ponderado " + v);
		}
		for(int i=0;i<aux.size();i++){
			Vector2D v = aux.get(i);
			double l = v.length();
			if (l > 0){
				v.x = v.x/(maxc);
				v.y = v.y/(maxc);
			}
			System.out.println("Normalizado " + v );
		}		
		return aux;
	}
	
	
}
