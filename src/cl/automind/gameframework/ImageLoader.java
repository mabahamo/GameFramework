package cl.automind.gameframework;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {

	private GraphicsConfiguration gc;
	private static ImageLoader instance = new ImageLoader();

	private ImageLoader() {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
	}

	public static ImageLoader getInstance() {
		return instance;
	}

	public BufferedImage getImage(String name) throws IOException {
		System.out.println("Cargando " + name);
		BufferedImage image = ImageIO.read(ImageLoader.class.getResource(name));
		int transparency = image.getColorModel( ).getTransparency( );
		return imageToManaged(image,transparency);
	}

	public BufferedImage getImage(String name, int bitmask) throws IOException {
		System.out.println("Cargando " + name);
		BufferedImage image = ImageIO.read(ImageLoader.class.getResource(name));
		return imageToManaged(image,bitmask);
	}
	
	private BufferedImage imageToManaged(BufferedImage src,int transparency) {
		BufferedImage dst = gc.createCompatibleImage(src.getWidth(),
				src.getHeight(), transparency);
		Graphics2D g2d = dst.createGraphics();
		g2d.drawImage(src, 0, 0, null);
		g2d.dispose();
		return dst;
	}

	public BufferedImage[] getImages(String string, int frames, int transparency)
			throws IOException {
		BufferedImage source = getImage(string);
		BufferedImage[] aux = new BufferedImage[frames];
		int imWidth = source.getWidth() / frames;
		int height = source.getHeight();
		Graphics2D g2d;
		for (int i = 0; i < frames; i++) {
			aux[i] = gc.createCompatibleImage(imWidth, height, transparency);
			g2d = aux[i].createGraphics();
			g2d.drawImage(source, 0, 0, imWidth, height, i * imWidth, 0,
					(i * imWidth) + imWidth, height, null);
			g2d.dispose();
		}
		return aux;
	}

	public BufferedImage[] getImages(String string, char c, int frames,
			int transparency) throws IOException {
		BufferedImage[] aux = new BufferedImage[frames];
		for (int i = 0; i < frames; i++) {
			String path = string.replaceAll("" + c, "" + i);
			aux[i] = getImage(path);
		}
		return aux;
	}
	
	public BufferedImage[] colorize(BufferedImage[] image, Color color){
		BufferedImage[] aux = new BufferedImage[image.length];
		for(int i=0;i<aux.length;i++){
			aux[i] = colorize(image[i],color);
		}
		return aux;
	}

	public BufferedImage colorize(BufferedImage image, Color color) {
		
		BufferedImage target = gc.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.BITMASK);
		int c = color.getRGB();
		int red = (c >> 16) & 0xFF;
		int green = (c >> 8) & 0xFF;
		int blue = c & 0xFF;
		float newHue = Color.RGBtoHSB(red, green, blue, null)[0];

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int c1 = image.getRGB(x, y);
				int r = (c1 >> 16) & 0xFF;
				int g = (c1 >> 8) & 0xFF;
				int b = c1 & 0xFF;
				int a = (c1 >> 24) & 0xff;
				float[] hsb = Color.RGBtoHSB(r, g, b, null);
				// float hue = hsb[0];
				float saturation = hsb[1];
				float brightness = hsb[2];
				Color newColor;
				if (a > 0) {
					newColor = Color
							.getHSBColor(newHue, saturation, brightness);
					r = newColor.getRed();
					g = newColor.getGreen();
					b = newColor.getBlue();
					newColor = new Color(r, g, b, a);
					target.setRGB(x, y, newColor.getRGB());
				} else {
					target.setRGB(x, y, c1);
				}

			}
		}
		image.flush();
		
		return target;
	}


}
