package ch.obermuhlner.csv2chart.model.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;
import ch.obermuhlner.csv2chart.model.DataVector;

public class CsvDataModelLoader {

	private static final String MARKER = "csv2chart.";

	private final String separator;
	private final String comment;

	public CsvDataModelLoader() {
		this(",", "#");
	}
	
	public CsvDataModelLoader(String separator, String comment) {
		this.separator = separator;
		this.comment = comment;
	}
	
	public DataModel load(File file, Parameters parameters) {
		Matrix<String> matrix = loadMatrix(file, parameters);
		return load(matrix, parameters);
	}
		
	public DataModel load(Matrix<String> matrix, Parameters parameters) {
		int headerRowCount = countHeaderRows(matrix);
		int valueRowCount = matrix.getHeight() - headerRowCount;

		DataVector category = null;
		List<DataVector> values = new ArrayList<>();

		for (int x = 0; x < matrix.getWidth(); x++) {
			if (containsDoubles(matrix, x, headerRowCount, 1, valueRowCount)) {
				values.add(toDataVector(matrix, x, headerRowCount));
			} else {
				if (category == null) {
					category = toDataVector(matrix, x, headerRowCount);
				}
			}
		}
		
		return new DataModel(parameters, category, values);
	}
	
	private DataVector toDataVector(Matrix<String> matrix, int x, int headerRowCount) {
		List<String> headers = new ArrayList<>();
		List<String> values = new ArrayList<>();

		for (int y = 0; y < headerRowCount; y++) {
			headers.add(matrix.get(x, y));
		}

		for (int y = headerRowCount; y < matrix.getHeight(); y++) {
			values.add(matrix.get(x, y));
		}
		
		return new DataVector(headers, values);
	}

	private int countHeaderRows(Matrix<String> matrix) {
		for (int y = 0; y < matrix.getHeight(); y++) {
			if (containsDoubles(matrix, 0, y, matrix.getWidth(), 1)) {
				return y;
			}
		}
		return matrix.getHeight();
	}

	private Matrix<String> loadMatrix(File file, Parameters parameters) {
		Matrix<String> matrix = new Matrix<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			int y = 0;
			String line = reader.readLine();
			while (line != null) {
				if (line.trim().isEmpty()) {
					// ignore
				}
				else if (line.startsWith(comment)) {
					parseComment(line, parameters);
				} else {
					List<String> rowValues = parseRow(line);
					for (int x = 0; x < rowValues.size(); x++) {
						matrix.set(x, y, rowValues.get(x));
					}
					y++;
				}
				
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return matrix;
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

	private static boolean containsDoubles(Matrix<String> matrix, int x, int y, int width, int height) {
		for (int iy = y; iy < y+height; iy++) {
			for (int ix = x; ix < x+width; ix++) {
				String value = matrix.get(ix, iy);
				if (value != null && isDouble(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean isDouble(String string) {
		try {
			Double.parseDouble(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
