package com.macheret.robotheroes.test;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class ColorTest {
	public static void main(String... args) throws Exception {
		Robot robot = new Robot();
		
		Color[] colorsToFind = new Color[] {
				new Color(0xffff5208),
				new Color(0xffff8205),
				new Color(0xffffde08),
				new Color(0xfff5e0b5),
		};
		
		Point topLeft = new Point(9, 238);
		Point[] candyLocations = new Point[] {
				new Point(514, 457),
				new Point(745, 345),
				new Point(852, 469),
				new Point(995, 428),
		};
		
		boolean z = true;
		while (z) {
			if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK) == true) {
				Point point = MouseInfo.getPointerInfo().getLocation();
				Color color = robot.getPixelColor(point.x, point.y);
				System.out.println((point.x - topLeft.x) + " " + (point.y - topLeft.y) + " " + toHex(color));// + " " + colorDifference(color, colorToFind));
			}
			Thread.sleep(250);
		}
		
		for (Point candyLocation : candyLocations) {
			System.out.println(candyLocation);
			BufferedImage capture = robot.createScreenCapture(new Rectangle(topLeft.x + candyLocation.x - 20, topLeft.y + candyLocation.y - 20, 40, 40));
			for (Color colorToFind : colorsToFind) {
				for (int x = 0; x < capture.getWidth(); x++) {
					for (int y = 0; y < capture.getHeight(); y++) {
						Color color = new Color(capture.getRGB(x, y) | 0xff000000);
						int diff = colorDifference(color, colorToFind);
						if (diff < 20) {
							System.out.println(x + "\t" + y + "\t" + toHex(colorToFind) + " " + toHex(color) + " " + diff);
						}
					}
				}
			}
		}
		
//		File folder = new File("data");
//		for (final File file : folder.listFiles()) {
//			BufferedImage img = ImageIO.read(file);
//
//			Map<Integer, Integer> colorCount = new HashMap<>();
//			for (int x = 0; x < img.getWidth(); x++) {
//				for (int y = 0; y < img.getHeight(); y++) {
//					int color = img.getRGB(x, y) | 0xff000000;
//					Integer count = colorCount.get(color);
//					colorCount.put(color, count == null ? 1 : count + 1);
//				}
//			}
//			
//			Set<Map.Entry<Integer, Integer>> sortedColorCounts = new TreeSet<>(new Comparator<Map.Entry<Integer, Integer>>() {
//				@Override
//				public int compare(Map.Entry<Integer, Integer> e1, Map.Entry<Integer, Integer> e2) {
//					// reversed natural ordering on values, nulls will break
//					int result = e2.getValue().compareTo(e1.getValue());
//					if (result != 0) return result;
//					
//					// fall back on natural ordering on keys for ties
//					return e1.getKey().compareTo(e2.getKey());
//				}
//			});
//			sortedColorCounts.addAll(colorCount.entrySet());
//			
//			for (Map.Entry<Integer, Integer> entry : sortedColorCounts) {
//				boolean found = false;
//				captureLoop:
//				for (int x = 0; x < capture.getWidth(); x++) {
//					for (int y = 0; y < capture.getHeight(); y++) {
//						int color = capture.getRGB(x, y) | 0xff000000;
//						if (color == entry.getValue()) {
//							found = true;
//							break captureLoop;
//						}
//					}
//				}
//				
//				if (found) {
//					System.out.println(entry.getValue() + "\t" + toHex(entry.getKey()));
//				}
//			}
//			System.out.println("Total: " + (img.getWidth() * img.getHeight()));
//		}
		
	}
	
	public static void main3(String ... args) throws Exception {
		System.out.println("Clicking in 3000 ms");
		Thread.sleep(3000);
		System.out.println("Clicking now");
		
		Robot robot = new Robot();

		while (true) {
			System.out.println("shift start");
			robot.keyPress(KeyEvent.VK_1);
			Thread.sleep(100);
			robot.keyRelease(KeyEvent.VK_1);
			System.out.println("shift end");
			Thread.sleep(1000);
		}
		
		//Thread.sleep(1000);
	}
	
	public static void main2(String ... args) throws Exception {
		Robot robot = new Robot();
		
		Set<Color> colors = new HashSet<>();
		
		System.out.println(colorDifference(new Color(0xffffdb38), new Color(0xfff4e998)));
		System.out.println(colorDifference(new Color(0xfff4e998), new Color(0xffffdb38)));
		
		Color gildColor = new Color(0xffffdb39);
		Color noGildColor = new Color(0xfff3e997);
		System.out.println(colorDifference(gildColor, noGildColor));
		
		while(true) {
			if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK) == true) {
				Point position = MouseInfo.getPointerInfo().getLocation();
				
				long sum1 = 0;
				for (int x=-1; x<=1; x++) {
					for (int y=-1; y<=1; y++) {
						sum1 += robot.getPixelColor(position.x, position.y).getRGB();
					}
				}
				Color color = new Color((int)(sum1 / 9));
				
				
				colors.add(color);
				if (colors.size() > 1) {
					int minRed = 0xff;
					int minGreen = 0xff;
					int minBlue = 0xff;
					int maxRed = 0;
					int maxGreen = 0;
					int maxBlue = 0;
					
					for (Color oneColor : colors) {
						minRed = Math.min(minRed, oneColor.getRed());
						minGreen = Math.min(minGreen, oneColor.getGreen());
						minBlue = Math.min(minBlue, oneColor.getBlue());
						maxRed = Math.max(maxRed, oneColor.getRed());
						maxGreen = Math.max(maxGreen, oneColor.getGreen());
						maxBlue = Math.max(maxBlue, oneColor.getBlue());
					}
					
					Color averageColor = noGildColor;//new Color((minRed + maxRed) / 2, (minGreen + maxGreen) / 2, (minBlue + maxBlue) / 2);
					int diff = colorDifference(color, averageColor);
					
					int maxDiff = 0;
					for (Color oneColor : colors) {
						maxDiff = Math.max(maxDiff, colorDifference(oneColor, averageColor));
					}
					
					System.out.println(String.format("%8s", Integer.toHexString(color.getRGB())) + " " + String.format("%8s", Integer.toHexString(averageColor.getRGB())) + " diff=" + diff + " maxDiff=" + maxDiff + " " + String.format("%2s%2s%2s", Integer.toHexString(minRed), Integer.toHexString(minGreen), Integer.toHexString(minBlue)) + " " + String.format("%2s%2s%2s", Integer.toHexString(maxRed), Integer.toHexString(maxGreen), Integer.toHexString(maxBlue)));
				} else {
					System.out.println(String.format("%8s", Integer.toHexString(color.getRGB())));
				}
			} else {
				colors.clear();
			}
			Thread.sleep(50);
		}
	}
	
	private static String toHex(Color color) {
		return toHex(color.getRGB());
	}
	
	private static String toHex(int color) {
		return String.format("%8s", Integer.toHexString(color));
	}
	
	private static int colorDifference(Color c1, Color c2) {
		return (int)Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2)
				+ Math.pow(c1.getGreen() - c2.getGreen(), 2)
				+ Math.pow(c1.getBlue() - c2.getBlue(), 2));
	}
}
