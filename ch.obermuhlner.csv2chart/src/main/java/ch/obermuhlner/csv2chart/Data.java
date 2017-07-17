package ch.obermuhlner.csv2chart;

import java.util.ArrayList;
import java.util.List;

public class Data {

	private List<List<String>> data = new ArrayList<>();
	private int headerRowCount;
	
	public void addRow(List<String> row) {
		data.add(row);
	}
	
	public List<List<String>> getRows() {
		return data;
	}

	public void setHeaderRowCount(int headerRowCount) {
		this.headerRowCount = headerRowCount;
	}
	
	public int getHeaderRowCount() {
		return headerRowCount;
	}
}
