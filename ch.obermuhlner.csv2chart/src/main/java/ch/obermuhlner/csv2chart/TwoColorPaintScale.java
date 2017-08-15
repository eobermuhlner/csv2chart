package ch.obermuhlner.csv2chart;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.renderer.PaintScale;

public class TwoColorPaintScale implements PaintScale {

	private double lowerBound;
	private double upperBound;
	private Color lowerColor;
	private Color upperColor;
	private Color defaultColor;

	public TwoColorPaintScale(double lowerBound, double upperBound, Color lowerColor, Color upperColor, Color defaultColor) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.lowerColor = lowerColor;
		this.upperColor = upperColor;
		this.defaultColor = defaultColor;
	}
	
	@Override
	public double getLowerBound() {
		return lowerBound;
	}

	@Override
	public double getUpperBound() {
		return upperBound;
	}

	@Override
	public Paint getPaint(double value) {
		if (value < lowerBound || value > upperBound) {
			return defaultColor;
		}
		
		double level = (value - lowerBound) / (upperBound - lowerBound);
		
		int r = (int) ((upperColor.getRed() - lowerColor.getRed()) * level + lowerColor.getRed());
		int g = (int) ((upperColor.getGreen() - lowerColor.getGreen()) * level + lowerColor.getGreen());
		int b = (int) ((upperColor.getBlue() - lowerColor.getBlue()) * level + lowerColor.getBlue());
		
		r = clamp(r, 0, 255);
		g = clamp(g, 0, 255);
		b = clamp(b, 0, 255);

		return new Color(r, g, b);
	}

	private int clamp(int value, int minValue, int maxValue) {
		if (value < minValue) {
			return minValue;
		}
		if (value > maxValue) {
			return maxValue;
		}
		
		return value;
	}
}
