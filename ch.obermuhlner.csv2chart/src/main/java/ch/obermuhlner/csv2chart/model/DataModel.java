package ch.obermuhlner.csv2chart.model;

import java.util.List;

import ch.obermuhlner.csv2chart.Parameters;

public class DataModel {

	private final Parameters parameters;
	private final DataVector category;
	private final List<DataVector> values;

	public DataModel(Parameters parameters, DataVector category, List<DataVector> values) {
		this.parameters = parameters;
		this.category = category;
		this.values = values;
	}
	
	public Parameters getParameters() {
		return parameters;
	}
	
	public DataVector getCategory() {
		return category;
	}

	public List<DataVector> getValues() {
		return values;
	}
}
