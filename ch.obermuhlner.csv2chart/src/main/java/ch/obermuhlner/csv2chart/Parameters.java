package ch.obermuhlner.csv2chart;

import java.lang.reflect.Field;

public class Parameters implements Cloneable {

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
	
	public Boolean crowdedLegend;
	
	public Double colorScaleMinValue = null;
	public Double colorScaleMidValue = null;
	public Double colorScaleMaxValue = null;
	
	public int width = 800;
	public int height = 600;

	public void setParameter(String name, Object value) {
		try {
			Field field = findField(name);
			Class<?> fieldType = field.getType();
			if (fieldType == int.class) {
				value = Integer.parseInt(String.valueOf(value));
			} else if (fieldType == boolean.class) {
				value = Boolean.parseBoolean(String.valueOf(value));
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
