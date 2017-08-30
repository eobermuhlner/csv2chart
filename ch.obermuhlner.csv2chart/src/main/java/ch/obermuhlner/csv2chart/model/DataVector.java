package ch.obermuhlner.csv2chart.model;

import java.util.List;
import java.util.Objects;

public class DataVector {

	private final List<String> headers;
	private final List<String> values;

	public DataVector(List<String> headers, List<String> values) {
		this.headers = headers;
		this.values = values;
	}
	
	public int getHeaderCount() {
		return headers.size();
	}

	public String getFirstHeader() {
		for (int i = 0; i < headers.size(); i++) {
			if (headers.get(i) != null) {
				return headers.get(i);
			}
		}
		return null;
	}
	
	public String getHeader(int index) {
		return headers.get(index);
	}
	
	public int getValueCount() {
		return values.size();
	}
	
	public String getStringValue(int index) {
		return values.get(index);
	}
	
	public Double getDoubleValue(int index) {
		String string = getStringValue(index);
		if (string == null) {
			return null;
		}
		
		return Double.parseDouble(string);
	}

	@Override
	public int hashCode() {
		return Objects.hash(headers, values);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		DataVector other = (DataVector) obj;
		
		if (headers == null) {
			if (other.headers != null)
				return false;
		} else if (!headers.equals(other.headers))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return "DataVector{headers=" + headers + ", values=" + values + "}";
	}
}
