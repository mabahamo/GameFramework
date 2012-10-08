package cl.automind.gameframework;

import java.awt.Color;

public class ColorConverter {
	
	public static String[] validColors = {"amarillo", "rojo", "verde", "azul", "blanco", "negro", "cyan", "celeste"};
	
	public static Color getColor(String strColor) {
		if (strColor == null){
			return Color.BLACK;
		}
		String aux = strColor.toLowerCase().trim();
		if (aux.equals("amarillo"))
			return Color.YELLOW;
		if (aux.equals("rojo"))
			return Color.RED;
		if (aux.equals("verde"))
			return Color.GREEN;
		if (aux.equals("azul"))
			return Color.BLUE;
		if (aux.equals("blanco"))
			return Color.WHITE;
		if (aux.equals("negro"))
			return Color.BLACK;
		if (aux.equals("cyan"))
		     return Color.CYAN;
		if (aux.equals("celeste"))
			return Color.CYAN;

		return Color.YELLOW;

	}

}
