package ch.obermuhlner.csv2chart;

import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class PieChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(Data data, Parameters parameters) {
		PieDataset pieDatset = createPieDataset(data, parameters);
		
		JFreeChart chart = org.jfree.chart.ChartFactory.createPieChart(parameters.title, pieDatset);
		return chart;
	}

	private PieDataset createPieDataset(Data data, Parameters parameters) {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		
		List<List<String>> rows = data.getRows();
		
		int rowIndex = data.getHeaderRowCount();
		while (rowIndex < rows.size()) {
			List<String> columns = rows.get(rowIndex++);

			pieDataset.setValue(columns.get(0), CsvDataLoader.toDouble(columns.get(1)));
		}		
		
		return pieDataset;
	}

}
