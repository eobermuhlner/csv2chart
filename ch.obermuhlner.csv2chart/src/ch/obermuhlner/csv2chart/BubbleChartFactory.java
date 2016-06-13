package ch.obermuhlner.csv2chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;

public class BubbleChartFactory extends AbstractChartFactory {

	private boolean headerLine = true;

	@Override
	public JFreeChart createChart(Data data, Parameters parameters) {
		XYZDataset categoryDataset = createXYZDataset(data, parameters);

		JFreeChart chart = org.jfree.chart.ChartFactory.createBubbleChart(parameters.title, parameters.xAxisLabel, parameters.yAxisLabel, categoryDataset);
		return chart;
	}

	private XYZDataset createXYZDataset(Data data, Parameters parameters) {
		List<List<String>> rows = data.getRows();
		int rowIndex = 0;
		
		DefaultXYZDataset dataset = new DefaultXYZDataset();
		List<String> columnLabels = null;
		
		int headerRowCount = data.getHeaderRowCount();
		for (int headerRowIndex = 0; headerRowIndex < headerRowCount; headerRowIndex++) {
			List<String> headerColumns = rows.get(rowIndex++);
			if (headerLine && headerRowIndex == 0) {
				columnLabels = headerColumns;
			}
		}

		Map<String, Values> mapSeriesToValues= new HashMap<>();
		
		while (rowIndex < rows.size()) {
			List<String> columns = rows.get(rowIndex++);
			
			if (columnLabels == null) {
				columnLabels = new ArrayList<>();
				for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
					columnLabels.add(String.valueOf(columnIndex + 1));
				}
			}

			String series = columns.get(0);
			Values values = mapSeriesToValues.computeIfAbsent(series, (key) -> new Values());
			values.values1.add(CsvDataLoader.toDouble(columns.get(1)));
			values.values2.add(CsvDataLoader.toDouble(columns.get(2)));
			values.values3.add(CsvDataLoader.toDouble(columns.get(3)));
		}
		
		for(String series : mapSeriesToValues.keySet()) {
			Values values = mapSeriesToValues.get(series);
			double[][] dataValues = { toDoubleArray(values.values1), toDoubleArray(values.values2), toDoubleArray(values.values3) }; 
			dataset.addSeries(series, dataValues);
		}

		if (parameters.xAxisLabel == null) {
			parameters.xAxisLabel = columnLabels.get(1);
		}
		if (parameters.yAxisLabel == null) {
			parameters.yAxisLabel = columnLabels.get(2);
		}
		
		return dataset;
	}
	
	private static class Values {
		List<Double> values1 = new ArrayList<>();
		List<Double> values2 = new ArrayList<>();
		List<Double> values3 = new ArrayList<>();
	}

	private static double[] toDoubleArray(List<Double> values) {
		double[] result = new double[values.size()];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = values.get(i);
		}
		
		return result;
	}
}
