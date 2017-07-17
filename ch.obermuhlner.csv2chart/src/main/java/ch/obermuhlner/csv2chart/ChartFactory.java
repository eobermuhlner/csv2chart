package ch.obermuhlner.csv2chart;

import org.jfree.chart.JFreeChart;

public interface ChartFactory {

	JFreeChart createChart(Data data, Parameters parameters);
}
