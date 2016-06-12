package ch.obermuhlner.csv2chart;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChartFactory extends AbstractChartFactory {

	private boolean headerLine = true;
	private boolean typeLine = true;
	private boolean firstColumnAreLabels = true;

	public LineChartFactory(Parameters parameters) {
		super(parameters);
	}

	@Override
	public JFreeChart createChart(Data data) {
		CategoryDataset categoryDatset = createCategoryDataset(data);

		if (parameters.xAxisLabel == null && headerLine && firstColumnAreLabels) {
			parameters.xAxisLabel = data.getData().get(0).get(0);
		}
		
		JFreeChart chart = org.jfree.chart.ChartFactory.createLineChart(parameters.title, parameters.xAxisLabel, parameters.yAxisLabel, categoryDatset);
		return chart;
	}

	private CategoryDataset createCategoryDataset(Data data) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		List<List<String>> rows = data.getData();
		int rowIndex = 0;
		
		
		List<String> columnLabels = null;
		List<String> columnTypes = null;
		if (headerLine) {
			columnLabels = rows.get(rowIndex++);
		}
		if (typeLine) {
			columnTypes = rows.get(rowIndex++);
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
