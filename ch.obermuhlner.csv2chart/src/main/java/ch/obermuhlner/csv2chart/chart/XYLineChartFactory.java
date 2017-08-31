package ch.obermuhlner.csv2chart.chart;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ch.obermuhlner.csv2chart.Data;
import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;
import ch.obermuhlner.csv2chart.model.DataVector;

public class XYLineChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(Data data, DataModel dataModel, Parameters parameters) {
		XYDataset categoryDatset = createXYDataset(dataModel, parameters);

		JFreeChart chart = org.jfree.chart.ChartFactory.createXYLineChart(parameters.title, parameters.xAxisLabel, parameters.yAxisLabel, categoryDatset);
		return chart;
	}

	private XYDataset createXYDataset(DataModel data, Parameters parameters) {
		List<XYSeries> xySeries = new ArrayList<>();
		
		List<DataVector> valueVectors = data.getValues();

		DataVector xValueVector = valueVectors.get(0);
		
		if (parameters.xAxisLabel == null) {
			parameters.xAxisLabel = xValueVector.getFirstHeader();
		}
		
		for (int i = 1; i < valueVectors.size(); i++) {
			DataVector yValueVector = valueVectors.get(i);
			String seriesName = yValueVector.getFirstHeader();
			if (seriesName == null) {
				seriesName = "#" + i;
			}
			
			XYSeries series = new XYSeries(seriesName);
			xySeries.add(series);
			
			for (int valueIndex = 0; valueIndex < yValueVector.getValueCount(); valueIndex++) {
				Double x = xValueVector.getDoubleValue(valueIndex);
				Double y = yValueVector.getDoubleValue(valueIndex);
				if (x != null && y != null) {
					series.add(x, y);
				}
			}
		}
				
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (XYSeries series : xySeries) {
			dataset.addSeries(series);
		}
		return dataset;
	}
}
