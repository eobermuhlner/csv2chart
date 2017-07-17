package ch.obermuhlner.csv2chart;

import java.util.List;

import org.jfree.chart.JFreeChart;

public class AutoChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(Data data, Parameters parameters) {
		ChartFactory chartFactory = createChartFactory(data);
		return chartFactory.createChart(data, parameters);
	}

	private ChartFactory createChartFactory(Data data) {
		List<String> dataRow = data.getRows().get(data.getHeaderRowCount());
		
		if (CsvDataLoader.isDouble(dataRow.get(0))) {
			return new XYLineChartFactory();
		}

		return new LineChartFactory();
	}

}
