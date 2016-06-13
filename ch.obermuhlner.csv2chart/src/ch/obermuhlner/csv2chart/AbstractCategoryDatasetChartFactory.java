package ch.obermuhlner.csv2chart;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public abstract class AbstractCategoryDatasetChartFactory extends AbstractChartFactory {

	protected CategoryDataset createCategoryDataset(Data data, Parameters parameters) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		List<List<String>> rows = data.getRows();
		int rowIndex = 0;
		
		List<String> columnLabels = null;
		int headerRowCount = data.getHeaderRowCount();
		for (int headerRowIndex = 0; headerRowIndex < headerRowCount; headerRowIndex++) {
			List<String> headerColumns = rows.get(rowIndex++);
			if (parameters.headerRow && headerRowIndex == 0) {
				columnLabels = headerColumns;
			}
		}
		
		int dataRowCount = 0;
			
		while (rowIndex < rows.size()) {
			dataRowCount++;
			
			List<String> columns = rows.get(rowIndex++);
			
			String rowLabel = String.valueOf(dataRowCount);
			if (parameters.headerColumn) {
				rowLabel = columns.get(0);
			}
			
			if (columnLabels == null) {
				columnLabels = new ArrayList<>();
				for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
					columnLabels.add(String.valueOf(columnIndex + 1));
				}
			}

			int startColumnIndex = parameters.headerColumn ? 1 : 0;
			for (int columnIndex = startColumnIndex; columnIndex < columns.size(); columnIndex++) {
				double value = CsvDataLoader.toDouble(columns.get(columnIndex));
				if (columnLabels.size() - startColumnIndex > 1) {
					dataset.addValue(value, columnLabels.get(columnIndex), rowLabel);
				} else {
					dataset.addValue(value, rowLabel, columnLabels.get(columnIndex));
				}
			}
		}
				
		return dataset;
	}
}
