package ch.obermuhlner.csv2chart.graphics;

import java.awt.Font;
import java.awt.FontMetrics;

public class DummyFontMetrics extends FontMetrics {

	private static final int ASCENT = 5;
	private static final int CHAR_WIDTH = 5;

	protected DummyFontMetrics(Font font) {
		super(font);
	}

	@Override public int getAscent() {
		return ASCENT;
	}

	@Override public int charWidth(char ch) {
		return CHAR_WIDTH;
	}

	@Override public int charWidth(int codePoint) {
		return CHAR_WIDTH;
	}

	@Override public int charsWidth(char[] data, int off, int len) {
		return CHAR_WIDTH * len;
	}

	@Override
	public int stringWidth(String str) {
		return str.length() * CHAR_WIDTH;
	}

}
