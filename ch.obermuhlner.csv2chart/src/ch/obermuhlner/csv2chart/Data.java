package ch.obermuhlner.csv2chart;

import java.util.ArrayList;
import java.util.List;

public class Data {

	private List<List<String>> data = new ArrayList<>();
	
	public void addRow(List<String> row) {
		data.add(row);
	}
	
	public List<List<String>> getData() {
		return data;
	}
}
