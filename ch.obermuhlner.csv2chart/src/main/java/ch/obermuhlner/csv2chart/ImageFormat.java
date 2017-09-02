package ch.obermuhlner.csv2chart;

public enum ImageFormat {
	PNG("png"),
	JPG("jpg"),
	SVG("svg"),
	LOG("log");
	
	private String extension;

	private ImageFormat(String extension) {
		this.extension = extension;
	}
	
	public String getExtension() {
		return extension;
	}
	
}
