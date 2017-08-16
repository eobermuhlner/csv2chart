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
		
		return ColorUtil.interpolateColor(value, lowerBound, upperBound, lowerColor, upperColor);
	}
}
