package ch.obermuhlner.csv2chart;

import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ch.obermuhlner.csv2chart.model.DataModel;

public class ScatterChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(Data data, DataModel dataModel, Parameters parameters) {

		XYDataset dataset = createXYDataset(data, parameters);

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

	private XYDataset createXYDataset(Data data, Parameters parameters) {
		XYSeriesCollection dataset = new XYSeriesCollection();

		int headerRowCount = data.getHeaderRowCount();
		int headerColumnCount = 1;
		
		List<List<String>> rows = data.getRows();
		List<String> headerRow = rows.get(0);
		
		for (int columnIndex = headerColumnCount; columnIndex < headerRow.size(); columnIndex++) {
			XYSeries series = new XYSeries(headerRow.get(columnIndex));

			for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
				List<String> dataRow = rows.get(rowIndex);
				double xValue;
				if (headerColumnCount > 0) {
					xValue = CsvDataLoader.toDouble(dataRow.get(0));
				} else {
					xValue = rowIndex - headerRowCount;
				}
				double yValue = CsvDataLoader.toDouble(dataRow.get(columnIndex));
				
				series.add(xValue, yValue);
			}
			
			dataset.addSeries(series);
		}
		
		return dataset;
	}
}
