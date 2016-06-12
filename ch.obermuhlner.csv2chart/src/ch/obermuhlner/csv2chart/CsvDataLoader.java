package ch.obermuhlner.csv2chart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class CsvDataLoader {

	private final String separator = ",";
	
	public Data load(String file) {
		Data data = new Data();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = reader.readLine();
			while (line != null) {
				data.addRow(Arrays.asList(line.split(separator)));
				
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return data;
	}
}
