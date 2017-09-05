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
		XYZDataset dataset = createXYZDatasetFromColumnsWithRadius(dataModel, parameters);

		NumberAxis xAxis = new NumberAxis(parameters.xAxisLabel);
		xAxis.setAutoRangeIncludesZero(false);
		NumberAxis yAxis = new NumberAxis(parameters.yAxisLabel);
		yAxis.setAutoRangeIncludesZero(false);

		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, null);

		XYItemRenderer renderer = new XYBubbleRenderer(XYBubbleRenderer.SCALE_ON_DOMAIN_AXIS);
		plot.setRenderer(renderer);
		plot.setOrientation(PlotOrientation.HORIZONTAL);

		boolean legend = !Boolean.TRUE.equals(parameters.valueLabels);
		JFreeChart chart = new JFreeChart(parameters.title, JFreeChart.DEFAULT_TITLE_FONT, plot, legend);
		return chart;
	}

	private XYZDataset createXYZDatasetFromColumnsWithRadius(DataModel dataModel, Parameters parameters) {
		DefaultXYZDataset dataset = new DefaultXYZDataset();

		DataVector categoryVector = dataModel.getCategory();
		
		DataVector xValues = dataModel.getValues().get(0);
		DataVector yValues = dataModel.getValues().get(1);
		DataVector radiusValues = dataModel.getValues().get(2);
		
		double xMinValue = xValues.getMinDoubleValue();
		double xMaxValue = xValues.getMaxDoubleValue();
		double xRangeValue = xMaxValue - xMinValue;

		double radiusMaxValue = radiusValues.getMaxDoubleValue();
		
		Map<String, Values> mapSeriesToValues = new HashMap<>();

		for (int i = 0; i < xValues.getValueCount(); i++) {
			String seriesName = categoryVector.getStringValue(i);
			Values values = mapSeriesToValues.computeIfAbsent(seriesName, (key) -> new Values());

			values.xValues.add(xValues.getDoubleValue(i));
			values.yValues.add(yValues.getDoubleValue(i));
			double z = radiusValues.getDoubleValue(i);
			z = z / radiusMaxValue * xRangeValue;
			z = z * 0.2;
			values.zValues.add(z);
		}

		for(String series : mapSeriesToValues.keySet()) {
			Values values = mapSeriesToValues.get(series);
			double[][] dataValues = { toDoubleArray(values.xValues), toDoubleArray(values.yValues), toDoubleArray(values.zValues) }; 
			dataset.addSeries(series, dataValues);
		}

		if (parameters.xAxisLabel == null) {
			parameters.xAxisLabel = xValues.getFirstHeader();
		}
		if (parameters.yAxisLabel == null) {
			parameters.yAxisLabel = yValues.getFirstHeader();
		}
		if (parameters.valueLabels == null) {
			parameters.valueLabels = mapSeriesToValues.size() > parameters.autoValueLabelsThreshold;
		}

		return dataset;
	}

	private static class Values {
		List<Double> xValues = new ArrayList<>();
		List<Double> yValues = new ArrayList<>();
		List<Double> zValues = new ArrayList<>();
	}

	private static double[] toDoubleArray(List<Double> values) {
		double[] result = new double[values.size()];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = values.get(i);
		}
		
		return result;
	}
}
