import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class EffectGenerator {

	private static final int MIN_WINDOW_SIZE = 3;
	private static final int MAX_WINDOW_SIZE = 24;
	private static final int DEFAULT_WINDOW_SIZE = 9;
	private int windowSize;

	private static final int MAX_ENERGY = 195076;

	private static BufferedImage result;

	public EffectGenerator(String imgPath) {
		windowSize = DEFAULT_WINDOW_SIZE;
		initialize(imgPath);
	}

	public EffectGenerator(int sz, String imgPath) {
		windowSize = Math.min(MAX_WINDOW_SIZE, Math.max(MIN_WINDOW_SIZE, sz));
		initialize(imgPath);
	}

	private void initialize(String imgPath) {
		try {
			BufferedImage orig = ImageIO.read(new File(imgPath));
			int[][] pixels = ImageHelper.getPixels(orig);
			int[][][] colors = new int[pixels.length][pixels[0].length][3];
			int[][] energy = new int[pixels.length][pixels[0].length];

			for (int i = 0; i < pixels.length; i++) {
				for (int j = 0; j < pixels[0].length; j++) {
					colors[i][j][0] = ImageHelper.getRed(pixels[i][j]);
					colors[i][j][1] = ImageHelper.getGreen(pixels[i][j]);
					colors[i][j][2] = ImageHelper.getBlue(pixels[i][j]);
				}
			}

			generateEnergy(colors, energy);

			ArrayList<Point> anchors = getMaximumEnergyPoints(energy);

		} catch (IOException e) {
			System.out.println("Invalid path to file");
		}

	}

	private void generateEnergy(int[][][] c, int[][] energy) {
		for (int i = 0; i < energy.length; i++) {
			energy[i][0] += MAX_ENERGY;
			energy[i][energy[0].length - 1] += MAX_ENERGY;
		}

		for (int i = 0; i < energy.length; i++) {
			energy[i][0] += MAX_ENERGY;
			energy[i][energy[0].length - 1] += MAX_ENERGY;
		}

		int[] dr = { 1, 0, 1, 1 };
		int[] dc = { 0, 1, 1, -1 };

		for (int i = 1; i < energy.length - 1; i++) {
			for (int j = 1; j < energy[0].length - 1; j++) {
				for (int k = 0; k < c[0][0].length; k++) {
					for (int d = 0; d < dr.length; d++) {
						energy[i][j] += (c[i + dr[d]][j + dc[d]][k] - c[i - dr[d]][j - dc[d]][k])
								* (c[i + dr[d]][j + dc[d]][k] - c[i - dr[d]][j - dc[d]][k]);
					}
				}
			}
		}
	}

	private ArrayList<Point> getMaximumEnergyPoints(int[][] energy) {
		ArrayList<Point> ret = new ArrayList<>();

		for (int i = 0; i < energy.length; i += windowSize) {
			for (int j = 0; j < energy[0].length; j += windowSize) {
				int endR = Math.min(energy.length, i + windowSize);
				int endC = Math.min(energy[0].length, j + windowSize);

				int maxEnergy = 0;
				Point maxPoint = new Point(0, 0);

				for (int r = i; r < endR; r++) {
					for (int c = j; c < endC; c++) {
						if (energy[r][c] > maxEnergy) {
							maxEnergy = energy[r][c];
							maxPoint = new Point(r, c);
						}
					}
				}

				ret.add(maxPoint);
			}
		}

		return ret;
	}
}
