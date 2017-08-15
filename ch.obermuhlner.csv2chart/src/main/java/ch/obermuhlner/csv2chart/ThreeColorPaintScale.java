package ch.obermuhlner.csv2chart;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.renderer.PaintScale;

public class ThreeColorPaintScale implements PaintScale {

	private double lowerBound;
	private double midPoint;
	private double upperBound;
	private Color lowerColor;
	private Color midColor;
	private Color upperColor;
	private Color defaultColor;

	public ThreeColorPaintScale(double lowerBound, double midPoint, double upperBound, Color lowerColor, Color midColor, Color upperColor, Color defaultColor) {
		this.lowerBound = lowerBound;
		this.midPoint = midPoint;
		this.upperBound = upperBound;
		this.lowerColor = lowerColor;
		this.midColor = midColor;
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
		
		if (value < midPoint) {
			return interpolatePaint(value, lowerBound, midPoint, lowerColor, midColor);
		}
		return interpolatePaint(value, midPoint, upperBound, midColor, upperColor);
	}
	
	private Paint interpolatePaint(double value, double minValue, double maxValue, Color minColor, Color maxColor) {
		double level = (value - minValue) / (maxValue - minValue);
		
		int r = (int) ((maxColor.getRed() - minColor.getRed()) * level + minColor.getRed());
		int g = (int) ((maxColor.getGreen() - minColor.getGreen()) * level + minColor.getGreen());
		int b = (int) ((maxColor.getBlue() - minColor.getBlue()) * level + minColor.getBlue());
		
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
