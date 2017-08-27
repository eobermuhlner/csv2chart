package ch.obermuhlner.csv2chart;

import java.awt.Color;
import java.lang.reflect.Field;

public class Parameters implements Cloneable {

	public String outDir = null;
	
	public String outPrefix = "";
	public String outPostfix = "";
	
	public String chart = "auto";
	
	public String title = null;
	
	@Option(name = "header-row")
	public boolean headerRow = true;
	@Option(name = "header-column")
	public boolean headerColumn = true;
	
	@Option(name = "x-axis")
	public String xAxisLabel;
	@Option(name = "y-axis")
	public String yAxisLabel;
	@Option(name = "z-axis")
	public String zAxisLabel;

	@Option(name = "x-axis-column")
	public Integer xAxisColumn;
	@Option(name = "y-axis-column")
	public Integer yAxisColumn;

	public Boolean crowdedLegend;
	
	@Option(name = "scale-min-value")
	public Double colorScaleMinValue = null;
	@Option(name = "scale-mid-value")
	public Double colorScaleMidValue = null;
	@Option(name = "scale-max-value")
	public Double colorScaleMaxValue = null;

	@Option(name = "scale-min-color")
	public Color colorScaleMinColor = null;
	@Option(name = "scale-mid-color")
	public Color colorScaleMidColor = null;
	@Option(name = "scale-max-color")
	public Color colorScaleMaxColor = null;
	@Option(name = "scale-default-color")
	public Color colorScaleDefaultColor = null;

	public int width = 800;
	public int height = 600;

	public void setParameter(String name, Object value) {
		try {
			Field field = findField(name);
			Class<?> fieldType = field.getType();
			if (fieldType == Integer.class || fieldType == int.class) {
				value = Integer.parseInt(String.valueOf(value));
			} else if (fieldType == Boolean.class || fieldType == boolean.class) {
				value = Boolean.parseBoolean(String.valueOf(value));
			} else if (fieldType == Color.class) {
				value = new Color(Integer.parseInt(String.valueOf(value), 16));
			} else if (fieldType == Double.class || fieldType == double.class) {
				value = Double.parseDouble(String.valueOf(value));
			}
			field.set(this, value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private Field findField(String name) throws NoSuchFieldException, SecurityException {
		for (Field field : Parameters.class.getFields()) {
			Option option = field.getAnnotation(Option.class);
			if (option != null && name.equals(option.name())) {
				return field;
			}
		}
		
		return Parameters.class.getField(name);
	}
	
	public Parameters copy() {
		try {
			return (Parameters) super.clone();
		} catch (CloneNotSupportedException e) {
			// never happens
		}
		return null;
	}
}
