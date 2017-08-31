package ch.obermuhlner.csv2chart.chart;

import java.util.List;

import org.jfree.chart.JFreeChart;

import ch.obermuhlner.csv2chart.CsvDataLoader;
import ch.obermuhlner.csv2chart.Data;
import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;

public class AutoChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(Data data, DataModel dataModel, Parameters parameters) {
		ChartFactory chartFactory = createChartFactory(data);
		return chartFactory.createChart(data, dataModel, parameters);
	}

	private ChartFactory createChartFactory(Data data) {
		List<String> dataRow = data.getRows().get(data.getHeaderRowCount());
		
		if (CsvDataLoader.isDouble(dataRow.get(0))) {
			return new XYLineChartFactory();
		}

		return new LineChartFactory();
	}

}
