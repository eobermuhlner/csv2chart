package ch.obermuhlner.csv2chart;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYLineChartFactory extends AbstractChartFactory {

	private boolean headerLine = true;

	@Override
	public JFreeChart createChart(Data data, Parameters parameters) {
		XYDataset categoryDatset = createCategoryDataset(data);

		if (parameters.xAxisLabel == null && headerLine) {
			parameters.xAxisLabel = data.getRows().get(0).get(0);
		}
		
		JFreeChart chart = org.jfree.chart.ChartFactory.createXYLineChart(parameters.title, parameters.xAxisLabel, parameters.yAxisLabel, categoryDatset);
		return chart;
	}

	private XYDataset createCategoryDataset(Data data) {
		List<List<String>> rows = data.getRows();
		int rowIndex = 0;
		
		List<XYSeries> xySeries = new ArrayList<>();
		int headerRowCount = data.getHeaderRowCount();
		for (int headerRowIndex = 0; headerRowIndex < headerRowCount; headerRowIndex++) {
			List<String> headerColumns = rows.get(rowIndex++);
			if (headerLine && headerRowIndex == 0) {
				for (int columnIndex = 1; columnIndex < headerColumns.size(); columnIndex++) {
					xySeries.add(new XYSeries(headerColumns.get(columnIndex)));
				}
			}
		}
		
		while (rowIndex < rows.size()) {
			List<String> columns = rows.get(rowIndex++);
			
			if (xySeries.size() == 0) {
				List<String> columnLabels = columns;
				for (int columnIndex = 1; columnIndex < columnLabels.size(); columnIndex++) {
					xySeries.add(new XYSeries(columnLabels.get(columnIndex)));
				}
			}

			double xValue = CsvDataLoader.toDouble(columns.get(0));
			for (int columnIndex = 1; columnIndex < columns.size(); columnIndex++) {
				double yValue = CsvDataLoader.toDouble(columns.get(columnIndex));
				xySeries.get(columnIndex - 1).add(xValue, yValue);
			}
		}
				
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (XYSeries series : xySeries) {
			dataset.addSeries(series);
		}
		return dataset;
	}
}
