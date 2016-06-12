package ch.obermuhlner.csv2chart;

public class Parameters {

	public String directory = ".";
	public String filePattern = "*.csv";
	
	public String chart = "auto";
	
	public String title = null;
	
	public String xAxisLabel = null;
	public String yAxisLabel = null;
	
	public int imageWidth = 800;
	public int imageHeight = 600;
	
	public Parameters copy() {
		Parameters result = new Parameters();
		
		result.directory = directory;
		result.filePattern = filePattern;
		result.title = title;
		result.xAxisLabel = xAxisLabel;
		result.yAxisLabel = yAxisLabel;
		result.imageWidth = imageWidth;
		result.imageHeight = imageHeight;
		
		return result;
	}

}
