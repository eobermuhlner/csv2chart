package ch.obermuhlner.csv2chart;

import org.jfree.chart.JFreeChart;

public class AutoChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(Data data, Parameters parameters) {
		ChartFactory chartFactory = createChartFactory(data);
		return chartFactory.createChart(data, parameters);
	}

	private ChartFactory createChartFactory(Data data) {
		return new XYLineChartFactory();
//		return new LineChartFactory();
	}

}
