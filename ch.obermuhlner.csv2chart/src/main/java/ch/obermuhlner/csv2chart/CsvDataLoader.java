package ch.obermuhlner.csv2chart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
				if (line.trim().isEmpty()) {
					// ignore
				}
				else if (line.startsWith(comment)) {
					parseComment(line, parameters);
				} else {
					List<String> columns = parseRow(line);
					if (headerMode) {
						if (containsNoDoubles(columns)) {
							headerRowCount++;
						} else {
							headerMode = false;
						}
					}
					data.addRow(columns);
				}
				
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		data.setHeaderRowCount(headerRowCount);
		
		return data;
	}

	private List<String> parseRow(String line) {
		String[] split = line.split(separator);
		for (int i = 0; i < split.length; i++) {
			split[i] = split[i].trim();
		}
		return Arrays.asList(split);
	}

	private void parseComment(String line, Parameters parameters) {
		int indexOfMarker = line.indexOf(MARKER);
		if (indexOfMarker >= 0) {
			indexOfMarker += MARKER.length();
			
			int indexOfAssignment = line.indexOf("=", indexOfMarker);
			if (indexOfAssignment >= 0) {
				String name = line.substring(indexOfMarker, indexOfAssignment).trim();
				String value = line.substring(indexOfAssignment + 1).trim();

				parameters.setParameter(name, value);
			}
		}
	}
	
	private boolean containsNoDoubles(Collection<String> columns) {
		for (String column : columns) {
			if (isDouble(column)) {
				return false;
			}
		}
		return true;
	}
	
	public static double toDouble(String string) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
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
