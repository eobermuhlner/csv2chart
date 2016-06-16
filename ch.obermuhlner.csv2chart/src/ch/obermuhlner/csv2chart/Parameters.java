package ch.obermuhlner.csv2chart;

import java.lang.reflect.Field;

public class Parameters implements Cloneable {

	public String directory = ".";
	public String filePattern = "*.csv";
	
	public String chart = "auto";
	
	public String title = null;
	
	public boolean headerRow = true;
	public boolean headerColumn = true;
	
	public String xAxisLabel;
	public String yAxisLabel;
	
	public Boolean crowdedLegend;
	
	public int width = 800;
	public int height = 600;

	public void setParameter(String name, Object value) {
		try {
			Field field = Parameters.class.getField(name);
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
	
	public Parameters copy() {
		try {
			return (Parameters) super.clone();
		} catch (CloneNotSupportedException e) {
			// never happens
		}
		return null;
	}
}
