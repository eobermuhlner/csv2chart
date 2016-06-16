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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
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
		argumentHandler.addOption("parameter", 2, (args, parameters) -> {
			parameters.setParameter(args.get(0), args.get(1));
		});
		argumentHandler.addOption("properties", 2, (args, parameters) -> {
			loadProperties(args.get(0), parameters);
		});
		argumentHandler.addOption("chart", 1, (args, parameters) -> {
			parameters.chart = args.get(0);
		});
		argumentHandler.addOption("dir", 1, (args, parameters) -> {
			parameters.directory = args.get(0);
		});
		argumentHandler.addOption("pattern", 1, (args, parameters) -> {
			parameters.filePattern = args.get(0);
		});
		argumentHandler.addOption("out-prefix", 1, (args, parameters) -> {
			parameters.outPrefix = args.get(0);
		});
		argumentHandler.addOption("out-postfix", 1, (args, parameters) -> {
			parameters.outPostfix = args.get(0);
		});
		argumentHandler.addOption("title", 1, (args, parameters) -> {
			parameters.title = args.get(0);
		});
		argumentHandler.addOption("header-column", 0, (args, parameters) -> {
			parameters.headerColumn = true;
		});
		argumentHandler.addOption("row-column", 0, (args, parameters) -> {
			parameters.headerRow = true;
		});
		argumentHandler.addOption("x-axis", 1, (args, parameters) -> {
			parameters.xAxisLabel = args.get(0);
		});
		argumentHandler.addOption("y-axis", 1, (args, parameters) -> {
			parameters.yAxisLabel = args.get(0);
		});
		argumentHandler.addOption("width", 1, (args, parameters) -> {
			parameters.width = Integer.parseInt(args.get(0));
		});
		argumentHandler.addOption("height", 1, (args, parameters) -> {
			parameters.width = Integer.parseInt(args.get(0));
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
			try (DirectoryStream<Path> directories = Files.newDirectoryStream(Paths.get(globalParameters.directory), globalParameters.filePattern)) {
				for (Path path : directories) {
					inputFiles.add(path);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		default:
			throw new IllegalArgumentException("Unknown chart: " + type);
		}
	}

	private static void modifyTheme(JFreeChart chart, Parameters parameters) {
		String fontName = "Helevetica";
		
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

	private static void error(String message) {
		System.err.println(message);
		System.exit(1);
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
