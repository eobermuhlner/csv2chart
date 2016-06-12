package ch.obermuhlner.csv2chart;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYLineChartFactory extends AbstractChartFactory {

	private boolean headerLine = true;
	private boolean typeLine = true;

	public XYLineChartFactory(Parameters parameters) {
		super(parameters);
	}

	@Override
	public JFreeChart createChart(Data data) {
		XYDataset categoryDatset = createCategoryDataset(data);

		if (parameters.xAxisLabel == null && headerLine) {
			parameters.xAxisLabel = data.getData().get(0).get(0);
		}
		
		JFreeChart chart = org.jfree.chart.ChartFactory.createXYLineChart(parameters.title, parameters.xAxisLabel, parameters.yAxisLabel, categoryDatset);
		return chart;
	}

	private XYDataset createCategoryDataset(Data data) {
		List<List<String>> rows = data.getData();
		int rowIndex = 0;
		
		List<XYSeries> xySeries = new ArrayList<>();
		List<String> columnTypes = null;
		if (headerLine) {
			List<String> columnLabels = rows.get(rowIndex++);
			for (int columnIndex = 1; columnIndex < columnLabels.size(); columnIndex++) {
				xySeries.add(new XYSeries(columnLabels.get(columnIndex)));
			}
		}
		if (typeLine) {
			columnTypes = rows.get(rowIndex++);
		}
		
		while (rowIndex < rows.size()) {
			List<String> columns = rows.get(rowIndex++);
			
			if (xySeries.size() == 0) {
				List<String> columnLabels = columns;
				for (int columnIndex = 1; columnIndex < columnLabels.size(); columnIndex++) {
					xySeries.add(new XYSeries(columnLabels.get(columnIndex)));
				}
			}

			double xValue = parseDouble(columns.get(0));
			for (int columnIndex = 1; columnIndex < columns.size(); columnIndex++) {
				double yValue = parseDouble(columns.get(columnIndex));
				xySeries.get(columnIndex - 1).add(xValue, yValue);
			}
		}
				
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (XYSeries series : xySeries) {
			dataset.addSeries(series);
		}
		return dataset;
	}

	private static double parseDouble(String string) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}
}
