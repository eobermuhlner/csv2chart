package ch.obermuhlner.csv2chart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.regex.Pattern;

public class CsvDataLoader {

	private final String comment = "#";

	private final String separator = ",";
	
	private static final String MARKER = "csv2chart.";
	
	public Data load(String file, Parameters parameters) {
		Data data = new Data();

		boolean headerMode = true;
		int headerRowCount = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = reader.readLine();
			while (line != null) {
				if (line.startsWith(comment)) {
					parseComment(line, parameters);
				} else {
					String[] split = line.split(separator);
					for (int i = 0; i < split.length; i++) {
						split[i] = split[i].trim();
					}
					if (headerMode) {
						if (containsNoDoubles(split)) {
							headerRowCount++;
						} else {
							headerMode = false;
						}
					}
					data.addRow(Arrays.asList(split));
				}
				
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		data.setHeaderRowCount(headerRowCount);
		
		return data;
	}

	private void parseComment(String line, Parameters parameters) {
		int indexOfMarker = line.indexOf(MARKER);
		if (indexOfMarker >= 0) {
			indexOfMarker += MARKER.length();
			
			int indexOfAssignment = line.indexOf("=", indexOfMarker);
			if (indexOfAssignment >= 0) {
				String name = line.substring(indexOfMarker, indexOfAssignment).trim();
				Object value = line.substring(indexOfAssignment + 1).trim();
				
				try {
					Field field = Parameters.class.getField(name);
					Class<?> fieldType = field.getType();
					if (fieldType == int.class) {
						value = Integer.parseInt(String.valueOf(value));
					}
					field.set(parameters, value);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean containsNoDoubles(String[] columns) {
		for (int i = 0; i < columns.length; i++) {
			if (isDouble(columns[i])) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isDouble(String string) {
		try {
			Double.parseDouble(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
