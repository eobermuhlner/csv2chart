package ch.obermuhlner.csv2chart.chart.color;

import java.awt.Color;

public class ColorUtil {

	public static Color interpolateColor(double value, double minValue, double maxValue, Color minColor, Color maxColor) {
		double level = (value - minValue) / (maxValue - minValue);
		
		int r = (int) ((maxColor.getRed() - minColor.getRed()) * level + minColor.getRed());
		int g = (int) ((maxColor.getGreen() - minColor.getGreen()) * level + minColor.getGreen());
		int b = (int) ((maxColor.getBlue() - minColor.getBlue()) * level + minColor.getBlue());
		
		r = clamp(r, 0, 255);
		g = clamp(g, 0, 255);
		b = clamp(b, 0, 255);
		
		return new Color(r, g, b);
	}

	public static int clamp(int value, int minValue, int maxValue) {
		if (value < minValue) {
			return minValue;
		}
		if (value > maxValue) {
			return maxValue;
		}
		
		return value;
	}

}
