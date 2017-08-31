package ch.obermuhlner.csv2chart.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;

public class ScatterChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(DataModel dataModel, Parameters parameters) {

		XYDataset dataset = createXYDataset(dataModel, parameters);

		PlotOrientation orientation = PlotOrientation.VERTICAL;
		NumberAxis xAxis = new NumberAxis(parameters.xAxisLabel);
		xAxis.setAutoRangeIncludesZero(false);
		NumberAxis yAxis = new NumberAxis(parameters.yAxisLabel);
		yAxis.setAutoRangeIncludesZero(false);
		
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, null);
		
		XYItemRenderer renderer = new XYLineAndShapeRenderer(false, true);
		plot.setRenderer(renderer);
		plot.setOrientation(orientation);
		
		boolean legend = !Boolean.TRUE.equals(parameters.crowdedLegend);
		JFreeChart chart = new JFreeChart(parameters.title, JFreeChart.DEFAULT_TITLE_FONT, plot, legend);
		return chart;
	}
}
