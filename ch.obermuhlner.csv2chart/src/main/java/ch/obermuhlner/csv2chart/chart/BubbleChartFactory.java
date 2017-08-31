package ch.obermuhlner.csv2chart.chart;

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

import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;
import ch.obermuhlner.csv2chart.model.DataVector;

public class BubbleChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(DataModel dataModel, Parameters parameters) {
		XYZDataset dataset = createXYZDataset(dataModel, parameters);

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

	private XYZDataset createXYZDataset(DataModel dataModel, Parameters parameters) {
		DefaultXYZDataset dataset = new DefaultXYZDataset();

		DataVector categoryVector = dataModel.getCategory();
		
		DataVector xValues = dataModel.getValues().get(0);
		DataVector yValues = dataModel.getValues().get(1);
		DataVector radiusValues = dataModel.getValues().get(2);
		
		double xMinValue = xValues.getMinDoubleValue();
		double xMaxValue = xValues.getMaxDoubleValue();
		double radiusMinValue = radiusValues.getMinDoubleValue();
		double radiusMaxValue = radiusValues.getMaxDoubleValue();
		double xRangeValue = xMaxValue - xMinValue;
		double radiusRangeValue = radiusMaxValue - radiusMinValue;
		
		Map<String, Values> mapSeriesToValues = new HashMap<>();

		for (int i = 0; i < xValues.getValueCount(); i++) {
			String seriesName = categoryVector.getStringValue(i);
			Values values = mapSeriesToValues.computeIfAbsent(seriesName, (key) -> new Values());

			values.values1.add(xValues.getDoubleValue(i));
			values.values2.add(yValues.getDoubleValue(i));
			double value3 = radiusValues.getDoubleValue(i);
			value3 = value3 / radiusRangeValue * xRangeValue;
			value3 *= 4;
			values.values3.add(value3);
		}

		for(String series : mapSeriesToValues.keySet()) {
			Values values = mapSeriesToValues.get(series);
			double[][] dataValues = { toDoubleArray(values.values1), toDoubleArray(values.values2), toDoubleArray(values.values3) }; 
			dataset.addSeries(series, dataValues);
		}

		if (parameters.xAxisLabel == null) {
			parameters.xAxisLabel = xValues.getFirstHeader();
		}
		if (parameters.yAxisLabel == null) {
			parameters.yAxisLabel = yValues.getFirstHeader();
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
