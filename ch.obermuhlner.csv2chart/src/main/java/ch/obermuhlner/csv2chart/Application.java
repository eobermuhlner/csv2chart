package ch.obermuhlner.csv2chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.BubbleXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

public class Application {

	private static final ArgumentHandler<Parameters> argumentHandler = new ArgumentHandler<>();
	
	static {
		argumentHandler.addOption("properties",
				"filename",
				"Loads the specified properties file.",
				1, (args, parameters) -> {
			loadProperties(args.get(0), parameters);
		});
		argumentHandler.addOption("out-prefix",
				"fileprefix",
				"Prefix for output chart files",
				1, (args, parameters) -> {
			parameters.outPrefix = args.get(0);
		});
		argumentHandler.addOption("out-postfix",
				"filepostfix",
				"Postfix for output chart files",
				1, (args, parameters) -> {
			parameters.outPostfix = args.get(0);
		});
		argumentHandler.addOption("chart", 
				"charttype",
				"The chart type to generate.\n"
				+ "Supported types: auto, line, bar, xyline, pie, bubble\n"
				+ "Default: 'auto'",
				1, (args, parameters) -> {
			parameters.chart = args.get(0);
		});
		argumentHandler.addOption("title",
				"text",
				"Text to appear as title in the chart.", 1, (args, parameters) -> {
			parameters.title = args.get(0);
		});
		argumentHandler.addOption("no-header-column",
				"",
				"When specified the first column is not interpreted as headers", 0, (args, parameters) -> {
			parameters.headerColumn = false;
		});
		argumentHandler.addOption("no-header-row",
				"",
				"When specified the first row is not interpreted as headers", 0, (args, parameters) -> {
			parameters.headerRow = false;
		});
		argumentHandler.addOption("header-column",
				"",
				"When specified the first column is interpreted as headers", 0, (args, parameters) -> {
			parameters.headerColumn = true;
		});
		argumentHandler.addOption("header-row",
				"",
				"When specified the first row is interpreted as headers", 0, (args, parameters) -> {
			parameters.headerRow = true;
		});
		argumentHandler.addOption("x-axis",
				"text",
				"Text to appear as x-axis label.",
				1, (args, parameters) -> {
			parameters.xAxisLabel = args.get(0);
		});
		argumentHandler.addOption("y-axis",
				"text",
				"Text to appear as y-axis label.",
				1, (args, parameters) -> {
			parameters.yAxisLabel = args.get(0);
		});
		argumentHandler.addOption("z-axis",
				"text",
				"Text to appear as z-axis label.",
				1, (args, parameters) -> {
			parameters.zAxisLabel = args.get(0);
		});
		argumentHandler.addOption("width",
				"pixels",
				"The width of the generated charts in pixels.\n"
				+ "Default: 800",
				1, (args, parameters) -> {
			parameters.width = Integer.parseInt(args.get(0));
		});
		argumentHandler.addOption("height",
				"pixels",
				"The height of the generated charts in pixels.\n"
				+ "Default: 600",
				1, (args, parameters) -> {
			parameters.height = Integer.parseInt(args.get(0));
		});
	}
	
	public static void main(String[] args) {
		CsvDataLoader dataLoader = new CsvDataLoader();

		Parameters globalParameters = new Parameters();

		List<Path> inputFiles = new ArrayList<>(); 
		
		List<String> arguments = argumentHandler.parseOptions(args, globalParameters);
		for (String argument : arguments) {
			inputFiles.add(Paths.get(argument));
		}
		
		if (inputFiles.isEmpty()) {
			printHelp();
		}

		for (Path path : inputFiles) {
			System.out.println("Processing: " + path);
			Parameters parameters = globalParameters.copy();
			String baseFilename = baseFilename(path.getFileName().toString());
			loadProperties(baseFilename + ".properties", parameters);
			Data data = dataLoader.load(path.toString(), parameters);
			if (parameters.title == null) {
				parameters.title = baseFilename;
			}
			ChartFactory chartFactory = createChartFactory(parameters);
			JFreeChart chart = chartFactory.createChart(data, parameters);
			modifyTheme(chart, parameters);
			
			String outBaseFilename = parameters.outPrefix + baseFilename + parameters.outPostfix;
			saveChartImage(chart, outBaseFilename, parameters.width, parameters.height);
		}		
	}
	
	private static void printHelp() {
		System.out.println("NAME");
		System.out.println("    csv2chart - create chart from csv file");
		System.out.println();
		System.out.println("SYNOPSIS");
		System.out.println("    csv2chart [options] [csv-files]");
		System.out.println();
		System.out.println("DESCRIPTION");
		System.out.println("   Options may be set in the command line, as comments in the csv file or in a properties file with the same basename as the csv file.");
		System.out.println("   Command line options are prefixed with --.");
		System.out.println("   Options in the csv file can be specified in comments at the top of the file, prefixed with 'csv2chart.'.");
		System.out.println("   Example: ");
		System.out.println("       # csv2chart.title=Sales - Sections");
		System.out.println("       # csv2chart.chart=pie");
		System.out.println("   Options in properties files are specified without prefix.");
		System.out.println("   Example: ");
		System.out.println("       title=Sales - Sections");
		System.out.println("       chart=pie");
		System.out.println();

		for (String option : argumentHandler.getOptions()) {
			System.out.println("    --" + option + " " + argumentHandler.getOptionArgumentDescription(option));
			String description = argumentHandler.getOptionDescription(option);
			String[] lines = description.split("\n");
			for (String line : lines) {
				System.out.println("        " + line);
			}
			System.out.println();
		}
	}
	
	private static void loadProperties(String string, Parameters parameters) {
		Properties properties = new Properties();
		try (Reader reader = new BufferedReader(new FileReader(string))) {
			properties.load(reader);
			for (Entry<Object, Object> entry : properties.entrySet()) {
				parameters.setParameter(String.valueOf(entry.getKey()), entry.getValue());
			}
		} catch (IOException e) {
			// ignore
		}
	}

	private static ChartFactory createChartFactory(Parameters parameters) {
		String type = parameters.chart;
		switch (type) {
		case "auto":
			return new AutoChartFactory();
		case "line":
			return new LineChartFactory();
		case "bar":
			return new BarChartFactory();
		case "xyline":
			return new XYLineChartFactory();
		case "pie":
			return new PieChartFactory();
		case "bubble":
			return new BubbleChartFactory();
		case "heatmap":
			return new HeatChartFactory();
		default:
			throw new IllegalArgumentException("Unknown chart: " + type);
		}
	}

	private static void modifyTheme(JFreeChart chart, Parameters parameters) {
		String fontName = "Helvetica";
		
		StandardChartTheme theme = (StandardChartTheme) org.jfree.chart.StandardChartTheme.createJFreeTheme();

		Color gray = Color.decode("#666666");
		Color lightGray = Color.decode("#C0C0C0");
		
		chart.setTextAntiAlias(true);
		chart.setAntiAlias(true);

		//theme.setTitlePaint(Color.decode("#4572a7"));
		theme.setExtraLargeFont(new Font(fontName, Font.BOLD, 18)); // title
		theme.setLargeFont(new Font(fontName, Font.BOLD, 14)); // axis-title
		theme.setRegularFont(new Font(fontName, Font.PLAIN, 12));
		theme.setRangeGridlinePaint(lightGray);
		theme.setPlotBackgroundPaint(Color.white);
		theme.setChartBackgroundPaint(Color.white);
		theme.setGridBandPaint(Color.red);
		theme.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
		theme.setBarPainter(new StandardBarPainter());
		theme.setAxisLabelPaint(gray);
		theme.apply(chart);

		Plot plot = chart.getPlot();
		if (plot instanceof CategoryPlot) {
			CategoryPlot categoryPlot = (CategoryPlot) plot;
			categoryPlot.setOutlineVisible(false);
			categoryPlot.getRangeAxis().setAxisLineVisible(false);
			categoryPlot.getRangeAxis().setTickMarksVisible(false);
			categoryPlot.setRangeGridlineStroke(new BasicStroke());
			categoryPlot.getRangeAxis().setTickLabelPaint(gray);
			categoryPlot.getDomainAxis().setTickLabelPaint(gray);
			
			for (int i = 0; i < categoryPlot.getCategories().size(); i++) {
				categoryPlot.getRenderer().setSeriesStroke(i, new BasicStroke(3.0f));
			}

			CategoryItemRenderer renderer = categoryPlot.getRenderer();
			if (renderer instanceof BarRenderer) {
				BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();
				barRenderer.setShadowVisible(true);
				barRenderer.setShadowXOffset(2);
				barRenderer.setShadowYOffset(0);
				barRenderer.setShadowPaint(lightGray);
				barRenderer.setMaximumBarWidth(0.1);
			}
		} else if (plot instanceof XYPlot) {
			XYPlot xyPlot = (XYPlot) plot;
			xyPlot.setOutlineVisible(false);

			for (int seriesIndex = 0; seriesIndex < xyPlot.getSeriesCount(); seriesIndex++) {
				xyPlot.getRenderer().setSeriesStroke(seriesIndex, new BasicStroke(3.0f));
				
				XYItemRenderer renderer = xyPlot.getRenderer();
				if (renderer instanceof XYBubbleRenderer) {
					XYBubbleRenderer xyBubbleRenderer = (XYBubbleRenderer) renderer;

					if (parameters.crowdedLegend) {
						xyBubbleRenderer.setSeriesItemLabelGenerator(seriesIndex, new BubbleXYItemLabelGenerator() {
							private static final long serialVersionUID = 1L;

							@Override
							public String generateLabel(XYDataset dataset, int series, int item) {
								DefaultXYZDataset xyzDataset = (DefaultXYZDataset) dataset;
								return String.valueOf(xyzDataset.getSeriesKey(series));
							}
						});
						xyBubbleRenderer.setSeriesItemLabelsVisible(seriesIndex, true);
						xyBubbleRenderer.setSeriesItemLabelPaint(seriesIndex, gray);
					}
				}
			}
		} else if (plot instanceof PiePlot) {
			PiePlot piePlot = (PiePlot) plot;
			piePlot.setOutlineVisible(false);
			piePlot.setShadowPaint(null);
			
			piePlot.setLabelBackgroundPaint(null);
			piePlot.setLabelShadowPaint(null);
			piePlot.setLabelOutlinePaint(null);
			piePlot.setLabelLinkStyle(PieLabelLinkStyle.STANDARD);
			piePlot.setLabelLinkPaint(gray);
		}
	
		LegendTitle legend = chart.getLegend();
		if (legend != null) {
			legend.setFrame(BlockBorder.NONE);
		}
	}

	private static String baseFilename(String filename) {
		String result = filename;
		int lastIndexOfSeparator = filename.lastIndexOf(".");
		if (lastIndexOfSeparator > 0) {
			result = filename.substring(0, lastIndexOfSeparator);
		}
		return result;
	}

	private static void saveChartImage(JFreeChart chart, String baseFilename, int imageWidth, int imageHeight) {
		try {
			String filename = baseFilename + ".png";
			OutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
			ChartUtilities.writeChartAsPNG(out, chart, imageWidth, imageHeight);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
