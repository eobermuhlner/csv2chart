package ch.obermuhlner.csv2chart;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public abstract class AbstractCategoryDatasetChartFactory extends AbstractChartFactory {

	protected boolean headerLine = true;
	protected boolean firstColumnAreLabels = true;

	protected CategoryDataset createCategoryDataset(Data data) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		List<List<String>> rows = data.getRows();
		int rowIndex = 0;
		
		List<String> columnLabels = null;
		int headerRowCount = data.getHeaderRowCount();
		for (int headerRowIndex = 0; headerRowIndex < headerRowCount; headerRowIndex++) {
			List<String> headerColumns = rows.get(rowIndex++);
			if (headerLine && headerRowIndex == 0) {
				columnLabels = headerColumns;
			}
		}
		
		int dataRowCount = 0;
			
		while (rowIndex < rows.size()) {
			dataRowCount++;
			
			List<String> columns = rows.get(rowIndex++);
			
			String rowLabel = String.valueOf(dataRowCount);
			if (firstColumnAreLabels) {
				rowLabel = columns.get(0);
			}
			
			if (columnLabels == null) {
				columnLabels = new ArrayList<>();
				for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
					columnLabels.add(String.valueOf(columnIndex + 1));
				}
			}

			int startColumnIndex = firstColumnAreLabels ? 1 : 0;
			for (int columnIndex = startColumnIndex; columnIndex < columns.size(); columnIndex++) {
				double value;
				try {
					value = Double.parseDouble(columns.get(columnIndex));
				} catch (NumberFormatException e) {
					value = Double.NaN;
				}
				dataset.addValue(value, columnLabels.get(columnIndex), rowLabel);
			}
		}
				
		return dataset;
	}
}
