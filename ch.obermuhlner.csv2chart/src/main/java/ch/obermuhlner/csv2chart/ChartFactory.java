package ch.obermuhlner.csv2chart;

import org.jfree.chart.JFreeChart;

import ch.obermuhlner.csv2chart.model.DataModel;

public interface ChartFactory {

	JFreeChart createChart(Data data, DataModel dataModel, Parameters parameters);
}
