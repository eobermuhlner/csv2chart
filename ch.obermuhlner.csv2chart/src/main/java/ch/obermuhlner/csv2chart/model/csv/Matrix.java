package ch.obermuhlner.csv2chart.model.csv;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Matrix<T> {

	private int width = 0;
	private int height = 0;
	
	private Map<Coord, T> map = new HashMap<>();
	
	public void set(int x, int y, T value) {
		width = Math.max(x + 1,  width);
		height = Math.max(y + 1,  height);
		
		map.put(new Coord(x, y), value);
	}
	
	public void setRow(int y, @SuppressWarnings("unchecked") T... values) {
		for (int x = 0; x < values.length; x++) {
			set(x, y, values[x]);
		}
	}
	
	public T get(int x, int y) {
		return map.get(new Coord(x, y));
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append("Matrix{\n");
		for (int y = 0; y < height; y++) {
			result.append("  ");
			for (int x = 0; x < width; x++) {
				if (x != 0) {
					result.append(", ");
				}
				result.append(get(x, y));
			}
			result.append("\n");
		}
		result.append("}");
		
		return result.toString();
	}
	
	private static class Coord {
		public final int x;
		public final int y;
		
		public Coord(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			
			Coord other = (Coord) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			
			return true;
		}
		
	}
}
