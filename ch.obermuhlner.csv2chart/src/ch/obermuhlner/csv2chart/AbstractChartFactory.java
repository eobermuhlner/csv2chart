package ch.obermuhlner.csv2chart;

public abstract class AbstractChartFactory implements ChartFactory {

	protected Parameters parameters;
	
	public AbstractChartFactory(Parameters parameters) {
		this.parameters = parameters;
	}
}
