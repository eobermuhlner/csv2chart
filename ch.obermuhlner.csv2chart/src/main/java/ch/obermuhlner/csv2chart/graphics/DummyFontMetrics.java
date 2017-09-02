package ch.obermuhlner.csv2chart.graphics;

import java.awt.Font;
import java.awt.FontMetrics;

public class DummyFontMetrics extends FontMetrics {

	private static final int CHAR_WIDTH = 5;

	protected DummyFontMetrics(Font font) {
		super(font);
	}
	
	@Override
	public int stringWidth(String str) {
		return str.length() * CHAR_WIDTH;
	}

}
