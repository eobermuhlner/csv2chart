package ch.obermuhlner.csv2chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;

public class BubbleChartFactory extends AbstractChartFactory {

	private boolean headerLine = true;

	@Override
	public JFreeChart createChart(Data data, Parameters parameters) {
		XYZDataset dataset = createXYZDataset(data, parameters);

		NumberAxis xAxis = new NumberAxis(parameters.xAxisLabel);
		xAxis.setAutoRangeIncludesZero(false);
		NumberAxis yAxis = new NumberAxis(parameters.yAxisLabel);
		yAxis.setAutoRangeIncludesZero(false);

		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, null);

		XYItemRenderer renderer = new XYBubbleRenderer(XYBubbleRenderer.SCALE_ON_RANGE_AXIS);
		plot.setRenderer(renderer);
		plot.setOrientation(PlotOrientation.HORIZONTAL);

		boolean legend = parameters.crowdedLegend != true;
		JFreeChart chart = new JFreeChart(parameters.title, JFreeChart.DEFAULT_TITLE_FONT, plot, legend);
		return chart;
	}

	private XYZDataset createXYZDataset(Data data, Parameters parameters) {
		List<List<String>> rows = data.getRows();
		
		DefaultXYZDataset dataset = new DefaultXYZDataset();
		List<String> columnLabels = null;
		
		int headerRowCount = data.getHeaderRowCount();

		int columnIndex1 = 1;
		int columnIndex2 = 2;
		int columnIndex3 = 3;

		double minValue1 = Double.MAX_VALUE;
		double maxValue1 = Double.MIN_VALUE;
		double minValue3 = Double.MAX_VALUE;
		double maxValue3 = Double.MIN_VALUE;
		for (int rowIndex = headerRowCount; rowIndex < rows.size(); rowIndex++) {
			List<String> row = rows.get(rowIndex);
			double value1 = CsvDataLoader.toDouble(row.get(columnIndex1));
			double value3 = CsvDataLoader.toDouble(row.get(columnIndex3));
			minValue1 = Math.min(minValue1, value1);
			maxValue1 = Math.max(maxValue1, value1);
			minValue3 = Math.min(minValue3, value3);
			maxValue3 = Math.max(maxValue3, value3);
		}
		double rangeValue1 = maxValue1 - minValue1;
		double rangeValue3 = maxValue3 - minValue3;
		
		int rowIndex = 0;

		for (int headerRowIndex = 0; headerRowIndex < headerRowCount; headerRowIndex++) {
			List<String> headerColumns = rows.get(rowIndex++);
			if (headerLine && headerRowIndex == 0) {
				columnLabels = headerColumns;
			}
		}

		Map<String, Values> mapSeriesToValues = new HashMap<>();
		
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
			values.values1.add(CsvDataLoader.toDouble(columns.get(columnIndex1)));
			values.values2.add(CsvDataLoader.toDouble(columns.get(columnIndex2)));
			double value3 = CsvDataLoader.toDouble(columns.get(columnIndex3));
			value3 = value3 / rangeValue3 * rangeValue1;
			value3 *= 4;
			values.values3.add(value3);
		}
		
		for(String series : mapSeriesToValues.keySet()) {
			Values values = mapSeriesToValues.get(series);
			double[][] dataValues = { toDoubleArray(values.values1), toDoubleArray(values.values2), toDoubleArray(values.values3) }; 
			dataset.addSeries(series, dataValues);
		}

		if (parameters.xAxisLabel == null) {
			parameters.xAxisLabel = columnLabels.get(columnIndex1);
		}
		if (parameters.yAxisLabel == null) {
			parameters.yAxisLabel = columnLabels.get(columnIndex2);
		}
		if (parameters.crowdedLegend == null) {
			parameters.crowdedLegend = mapSeriesToValues.size() > 10;
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
