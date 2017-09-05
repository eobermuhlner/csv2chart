package ch.obermuhlner.csv2chart.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYZDataset;

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
		Map<String, XYSeries> seriesMap = new HashMap<>();
		
		DataVector categoryVector = data.getCategory();
		List<DataVector> valueVectors = data.getValues();

		DataVector xValueVector = valueVectors.get(0);
		
		if (parameters.xAxisLabel == null) {
			parameters.xAxisLabel = xValueVector.getFirstHeader();
		}
		
		if (parameters.yAxisLabel == null && valueVectors.size() == 2) {
			parameters.yAxisLabel = valueVectors.get(1).getFirstHeader();
		}
		
		for (int i = 1; i < valueVectors.size(); i++) {
			DataVector yValueVector = valueVectors.get(i);
			
			String seriesName = null;
			if (seriesName == null) {
				seriesName = yValueVector.getFirstHeader();
			}
			if (seriesName == null) {
				seriesName = "#" + i;
			}

			for (int valueIndex = 0; valueIndex < yValueVector.getValueCount(); valueIndex++) {
				if (categoryVector != null) {
					seriesName = categoryVector.getStringValue(valueIndex);
				}
				
				XYSeries series = seriesMap.computeIfAbsent(seriesName, key -> new XYSeries(key));

				Double x = xValueVector.getDoubleValue(valueIndex);
				Double y = yValueVector.getDoubleValue(valueIndex);
				if (x != null && y != null) {
					series.add(x, y);
				}
			}
		}

		if (parameters.legend == null) {
			parameters.legend = seriesMap.size() > 1;
			if (Boolean.TRUE.equals(parameters.valueLabels)) {
				parameters.legend = false;
			}
		}

		XYSeriesCollection dataset = new XYSeriesCollection();
		for (XYSeries series : seriesMap.values()) {
			dataset.addSeries(series);
		}
		return dataset;
	}

	protected XYZDataset createXYZDataset(DataModel data, Parameters parameters) {
		if (data.getValues().size() > 0 && data.getValues().get(0).getHeaderCount() > 0) {
			return createXYZDatasetFromColumns(data, parameters);
		}
		
		return createXYZDatasetFromMatrix(data, parameters);
	}
	
	private XYZDataset createXYZDatasetFromColumns(DataModel data, Parameters parameters) {
		DefaultXYZDataset dataset = new DefaultXYZDataset();

		List<DataVector> valueVectors = data.getValues();
				
		DataVector xVector = valueVectors.get(0);
		DataVector yVector = valueVectors.get(1);
		double[] xVectorArray = xVector.toDoubleArray();
		double[] yVectorArray = yVector.toDoubleArray();

		double zMinValue = Double.MAX_VALUE;
		double zMaxValue = -Double.MAX_VALUE;

		String zAxisName = null;
		
		for (int i = 2; i < valueVectors.size(); i++) {
			DataVector zVector = valueVectors.get(i);
			zMinValue = Math.min(zMinValue, zVector.getMinDoubleValue());
			zMaxValue = Math.max(zMaxValue, zVector.getMaxDoubleValue());

			String seriesName = zVector.getFirstHeader();
			if (seriesName == null) {
				seriesName = "#" + (i - 1);
			}
			if (zAxisName == null) {
				zAxisName = seriesName;
			}
			
			dataset.addSeries(seriesName, new double[][] {
				xVectorArray,
				yVectorArray,
				zVector.toDoubleArray() });
		}
		
		if (parameters.colorScaleMinValue == null) {
			parameters.colorScaleMinValue = zMinValue;
		}
		if (parameters.colorScaleMaxValue == null) {
			parameters.colorScaleMaxValue = zMaxValue;
		}

		if (parameters.xAxisLabel == null) {
			parameters.xAxisLabel = xVector.getFirstHeader();
		}
		if (parameters.yAxisLabel == null) {
			parameters.yAxisLabel = yVector.getFirstHeader();
		}
		if (parameters.zAxisLabel == null) {
			parameters.zAxisLabel = zAxisName;
		}
		if (parameters.valueLabels == null) {
			parameters.valueLabels = valueVectors.size() > parameters.autoValueLabelsThreshold;
		}
		if (parameters.legend == null) {
			if (Boolean.TRUE.equals(parameters.valueLabels)) {
				parameters.legend = false;
			}
		}
		
		return dataset;
	}

	private XYZDataset createXYZDatasetFromMatrix(DataModel data, Parameters parameters) {
		DefaultXYZDataset dataset = new DefaultXYZDataset();
		
		List<DataVector> valuesVector = data.getValues();
		
		boolean useXAxisValues = parameters.matrixYValues;
		boolean useYAxisValues = parameters.matrixXValues;
		
		double[] xAxisValues = new double[valuesVector.size() - 1]; 
		if (useXAxisValues) {
			for (int i = 0; i < xAxisValues.length; i++) {
				xAxisValues[i] = valuesVector.get(i + 1).getDoubleValue(0);
			}
		} else {
			for (int i = 0; i < xAxisValues.length; i++) {
				xAxisValues[i] = i;
			}
		}
		
		double[] yAxisValues = new double[valuesVector.get(0).getValueCount() - 1];
		if (useYAxisValues) {
			DataVector yAxisVector = valuesVector.get(0);
			for (int i = 0; i < yAxisValues.length; i++) {
				yAxisValues[i] = yAxisVector.getDoubleValue(i + 1);
			}
		} else {
			for (int i = 0; i < yAxisValues.length; i++) {
				yAxisValues[i] = i;
			}
		}
		
		int n = xAxisValues.length * yAxisValues.length;
		double[] xValues = new double[n];
		double[] yValues = new double[n];
		double[] zValues = new double[n];
		
		double minValue = Double.MAX_VALUE;
		double maxValue = -Double.MAX_VALUE;
		
		int index = 0;
		for (int xIndex = 0; xIndex < xAxisValues.length; xIndex++) {
			for (int yIndex = 0; yIndex < yAxisValues.length; yIndex++) {
				double value = valuesVector.get(xIndex + 1).getDoubleValue(yIndex + 1); 
				
				xValues[index] = xAxisValues[xIndex];
				yValues[index] = yAxisValues[yIndex];
				zValues[index] = value;
				
				minValue = Math.min(minValue, value);
				maxValue = Math.max(maxValue, value);
				
				index++;
			}
		}
		
		dataset.addSeries("series", new double[][] { yValues, xValues, zValues });
		
		if (parameters.colorScaleMinValue == null) {
			parameters.colorScaleMinValue = minValue;
		}
		if (parameters.colorScaleMaxValue == null) {
			parameters.colorScaleMaxValue = maxValue;
		}

		return dataset;
	}

}
