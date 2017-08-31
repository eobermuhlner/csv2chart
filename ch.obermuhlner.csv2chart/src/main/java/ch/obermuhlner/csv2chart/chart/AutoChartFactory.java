package ch.obermuhlner.csv2chart.chart;

import org.jfree.chart.JFreeChart;

import ch.obermuhlner.csv2chart.Data;
import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;

public class AutoChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(Data data, DataModel dataModel, Parameters parameters) {
		ChartFactory chartFactory = createChartFactory(dataModel);
		return chartFactory.createChart(data, dataModel, parameters);
	}

	private ChartFactory createChartFactory(DataModel data) {
		
		if (data.getCategory() == null) {
			return new XYLineChartFactory();
		}

		return new LineChartFactory();
	}

}
