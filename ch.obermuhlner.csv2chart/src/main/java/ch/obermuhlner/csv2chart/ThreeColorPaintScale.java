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
			return ColorUtil.interpolateColor(value, lowerBound, midPoint, lowerColor, midColor);
		}
		
		return ColorUtil.interpolateColor(value, midPoint, upperBound, midColor, upperColor);
	}
}
