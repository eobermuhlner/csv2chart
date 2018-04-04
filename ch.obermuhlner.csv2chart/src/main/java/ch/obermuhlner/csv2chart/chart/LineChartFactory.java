package ch.obermuhlner.csv2chart.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;

public class LineChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(DataModel dataModel, Parameters parameters) {
		CategoryDataset categoryDataset = createCategoryDataset(dataModel, parameters);

		if (parameters.xAxisLabel == null) {
			parameters.xAxisLabel = dataModel.getCategory().getFirstHeader();
		}

		boolean legend = Parameters.withDefault(parameters.legend, true);

		JFreeChart chart = org.jfree.chart.ChartFactory.createLineChart(parameters.title, parameters.xAxisLabel, parameters.yAxisLabel, categoryDataset, PlotOrientation.VERTICAL, legend, true, false);
		return chart;
	}
}
