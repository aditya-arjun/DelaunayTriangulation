import java.awt.image.BufferedImage;

public class ImageHelper {

	static int[][] getPixels(BufferedImage img) {
		int[][] ret = new int[img.getHeight()][img.getWidth()];

		for (int row = 0; row < ret.length; row++) {
			for (int col = 0; col < ret[0].length; col++) {
				ret[row][col] = img.getRGB(row, col);
			}
		}

		return ret;
	}

	static BufferedImage getImage(int[][] pixels) {
		BufferedImage ret = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_RGB);

		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				ret.setRGB(row, col, pixels[row][col]);
			}
		}
		return ret;
	}

	static int getRed(int pixel) {
		return (pixel >> 16) & 0xff;
	}

	static int getGreen(int pixel) {
		return (pixel >> 8) & 0xff;
	}

	static int getBlue(int pixel) {
		return (pixel) & 0xff;
	}
}
