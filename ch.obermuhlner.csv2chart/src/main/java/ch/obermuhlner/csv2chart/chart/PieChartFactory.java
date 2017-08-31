package ch.obermuhlner.csv2chart.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.TableOrder;

import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;
import ch.obermuhlner.csv2chart.model.DataVector;

public class PieChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(DataModel dataModel, Parameters parameters) {
		if (dataModel.getValues().size() == 1) {
			PieDataset pieDatset = createPieDataset(dataModel, parameters);
			
			JFreeChart chart = org.jfree.chart.ChartFactory.createPieChart(parameters.title, pieDatset);
			return chart;
		} else {
			CategoryDataset categoryDataset = createCategoryDataset(dataModel, parameters);

			JFreeChart chart = org.jfree.chart.ChartFactory.createMultiplePieChart(parameters.title, categoryDataset, TableOrder.BY_COLUMN, false, false, false);
			return chart;
		}
	}

	private PieDataset createPieDataset(DataModel data, Parameters parameters) {
		DefaultPieDataset pieDataset = new DefaultPieDataset();

		DataVector categoryVector = data.getCategory();
		DataVector valueVector = data.getValues().get(0);
		
		for (int i = 0; i < valueVector.getValueCount(); i++) {
			String categoryName = categoryVector.getStringValue(i);
			Double value = valueVector.getDoubleValue(i);
			
			if (value != null) {
				pieDataset.setValue(categoryName, value);
			}
		}		
		
		return pieDataset;
	}
}
