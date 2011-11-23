package cl.automind.gameframework;

import java.awt.Font;


public final class FontLoader {

	public static Font loadFont(String url, int size) {
		
		
		Font font = null;
		try
		{
			Font base = Font.createFont(Font.TRUETYPE_FONT, FontLoader.class.getResourceAsStream(url));
//				font = base.deriveFont(Font.BOLD, size);
			font = base.deriveFont(Font.PLAIN, size);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return font;
	}

}