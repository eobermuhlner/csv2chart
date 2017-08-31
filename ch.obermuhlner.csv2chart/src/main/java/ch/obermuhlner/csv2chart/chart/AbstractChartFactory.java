package ch.obermuhlner.csv2chart.chart;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;
import ch.obermuhlner.csv2chart.model.DataVector;

public abstract class AbstractChartFactory implements ChartFactory {

	protected CategoryDataset createCategoryDataset(DataModel dataModel, Parameters parameters) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		DataVector categoryVector = dataModel.getCategory();
		if (categoryVector != null) {
			List<DataVector> values = dataModel.getValues();
			for (DataVector valueVector : values) {
				String valueName = valueVector.getHeader(0);
				for (int i = 0; i < valueVector.getValueCount(); i++) {
					String categoryName = categoryVector.getStringValue(i);
					Double value = valueVector.getDoubleValue(i);
					if (value != null) {
						if (categoryVector.getValueCount() > 1) {
							dataset.addValue(value, valueName, categoryName);
						} else {
							dataset.addValue(value, categoryName, valueName);
						}
					}
				}
			}
		}
		
		return dataset;
	}
	
	protected XYDataset createXYDataset(DataModel data, Parameters parameters) {
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
