package ch.obermuhlner.csv2chart;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;

public class LineChartFactory extends AbstractCategoryDatasetChartFactory {

	@Override
	public JFreeChart createChart(Data data, Parameters parameters) {
		CategoryDataset categoryDatset = createCategoryDataset(data, parameters);

		if (parameters.xAxisLabel == null && parameters.headerRow && parameters.headerColumn) {
			parameters.xAxisLabel = data.getRows().get(0).get(0);
		}
		
		JFreeChart chart = org.jfree.chart.ChartFactory.createLineChart(parameters.title, parameters.xAxisLabel, parameters.yAxisLabel, categoryDatset);
		return chart;
	}
}
