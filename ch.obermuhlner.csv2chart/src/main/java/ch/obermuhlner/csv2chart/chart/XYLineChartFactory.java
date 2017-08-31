package ch.obermuhlner.csv2chart.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import ch.obermuhlner.csv2chart.Data;
import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;

public class XYLineChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(Data data, DataModel dataModel, Parameters parameters) {
		XYDataset categoryDatset = createXYDataset(dataModel, parameters);

		JFreeChart chart = org.jfree.chart.ChartFactory.createXYLineChart(parameters.title, parameters.xAxisLabel, parameters.yAxisLabel, categoryDatset);
		return chart;
	}
}
