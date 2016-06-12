package ch.obermuhlner.csv2chart;

import org.jfree.chart.JFreeChart;

public class AutoChartFactory extends AbstractChartFactory {

	public AutoChartFactory(Parameters parameters) {
		super(parameters);
	}

	@Override
	public JFreeChart createChart(Data data) {
		ChartFactory chartFactory = createChartFactory(data);
		return chartFactory.createChart(data);
	}

	private ChartFactory createChartFactory(Data data) {
		return new XYLineChartFactory(parameters);
//		return new LineChartFactory(parameters);
	}

}
