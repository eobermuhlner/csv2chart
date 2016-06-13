package ch.obermuhlner.csv2chart;

public class Parameters {

	public String directory = ".";
	public String filePattern = "*.csv";
	
	public String chart = "auto";
	
	public String title = null;
	
	public boolean headerRow = true;
	public boolean headerColumn = true;
	
	public String xAxisLabel = null;
	public String yAxisLabel = null;
	
	public int width = 800;
	public int height = 600;
	
	public Parameters copy() {
		Parameters result = new Parameters();
		
		result.directory = directory;
		result.filePattern = filePattern;
		result.title = title;
		result.headerRow = headerRow;
		result.headerColumn = headerColumn;
		result.xAxisLabel = xAxisLabel;
		result.yAxisLabel = yAxisLabel;
		result.width = width;
		result.height = height;
		
		return result;
	}

}
