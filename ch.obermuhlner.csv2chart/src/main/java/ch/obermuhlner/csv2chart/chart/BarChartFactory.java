package ch.obermuhlner.csv2chart.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;

import ch.obermuhlner.csv2chart.AbstractCategoryDatasetChartFactory;
import ch.obermuhlner.csv2chart.Data;
import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;

public class BarChartFactory extends AbstractCategoryDatasetChartFactory {

	@Override
	public JFreeChart createChart(Data data, DataModel dataModel, Parameters parameters) {
		CategoryDataset categoryDataset = createCategoryDataset(dataModel, parameters);

		if (parameters.xAxisLabel == null) {
			parameters.xAxisLabel = dataModel.getCategory().getFirstHeader();
		}
		
		JFreeChart chart = org.jfree.chart.ChartFactory.createBarChart(parameters.title, parameters.xAxisLabel, parameters.yAxisLabel, categoryDataset);
		return chart;
	}
}
