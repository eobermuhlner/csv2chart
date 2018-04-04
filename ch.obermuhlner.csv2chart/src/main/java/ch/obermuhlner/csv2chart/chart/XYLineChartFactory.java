package ch.obermuhlner.csv2chart.chart;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;

import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;

public class XYLineChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(DataModel dataModel, Parameters parameters) {
		XYDataset categoryDataset = createXYDataset(dataModel, parameters);

		if (dataModel.getValues().size() == 2) {
			if (parameters.xAxisLabel == null) {
				parameters.xAxisLabel = dataModel.getValues().get(0).getFirstHeader();
			}
			if (parameters.yAxisLabel == null) {
				parameters.yAxisLabel = dataModel.getValues().get(1).getFirstHeader();
			}
		}

		boolean legend = Parameters.withDefault(parameters.legend, dataModel.getValues().size() > 2);

		JFreeChart chart = org.jfree.chart.ChartFactory.createXYLineChart(parameters.title, parameters.xAxisLabel, parameters.yAxisLabel, categoryDataset,
		   PlotOrientation.VERTICAL, legend, true, false);
		return chart;
	}
}
