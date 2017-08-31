package ch.obermuhlner.csv2chart.chart;

import org.jfree.chart.JFreeChart;

import ch.obermuhlner.csv2chart.Data;
import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;

public interface ChartFactory {

	JFreeChart createChart(Data data, DataModel dataModel, Parameters parameters);
}
