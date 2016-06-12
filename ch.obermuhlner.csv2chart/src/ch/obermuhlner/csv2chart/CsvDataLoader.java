package ch.obermuhlner.csv2chart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class CsvDataLoader {

	private final String separator = ",";
	
	public Data load(String file) {
		Data data = new Data();

		boolean headerMode = true;
		int headerRowCount = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = reader.readLine();
			while (line != null) {
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
				
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		data.setHeaderRowCount(headerRowCount);
		
		return data;
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
