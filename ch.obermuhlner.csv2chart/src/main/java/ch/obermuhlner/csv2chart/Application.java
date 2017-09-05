package ch.obermuhlner.csv2chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.BubbleXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.MultiplePiePlot;
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
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUnits;
import org.jfree.graphics2d.svg.SVGUtils;
import org.jfree.ui.RectangleInsets;

import ch.obermuhlner.csv2chart.chart.AutoChartFactory;
import ch.obermuhlner.csv2chart.chart.BarChartFactory;
import ch.obermuhlner.csv2chart.chart.BubbleChartFactory;
import ch.obermuhlner.csv2chart.chart.ChartFactory;
import ch.obermuhlner.csv2chart.chart.HeatChartFactory;
import ch.obermuhlner.csv2chart.chart.LineChartFactory;
import ch.obermuhlner.csv2chart.chart.PieChartFactory;
import ch.obermuhlner.csv2chart.chart.ScatterChartFactory;
import ch.obermuhlner.csv2chart.chart.XYLineChartFactory;
import ch.obermuhlner.csv2chart.graphics.LogGraphics2D;
import ch.obermuhlner.csv2chart.model.DataModel;
import ch.obermuhlner.csv2chart.model.csv.CsvDataModelLoader;

public class Application {

	private static Color gray = Color.decode("#666666");
	private static Color lightGray = Color.decode("#C0C0C0");

	private static final ArgumentHandler<Parameters> argumentHandler = new ArgumentHandler<>();
	
	static {
		argumentHandler.addOption("properties",
				"filename",
				"Loads the specified properties file.",
				1, (args, parameters) -> {
			loadProperties(new File(args.get(0)), parameters);
		});

		argumentHandler.addOption("property",
				"key=value",
				"Set the specified property.\n",
				1, (args, parameters) -> {
					parameters.setParameterKeyValue(args.get(0));
		});
		
		for (Parameter parameter : Parameters.getAllCommandLineOptions()) {
			String description = parameter.description() + "\n"
					+ "In properties files and csv comments this value can be set by using the key '" + parameter.name() + "'.";
			argumentHandler.addOption(
					parameter.optionName(),
					parameter.optionArgumentDescription(),
					description,
					1,
					(args, parameters) -> {
						parameters.setParameter(parameter.name(), args.get(0));
			});
		}
		
	}
	
	public static void main(String[] args) {
		Locale defaultLocale = Locale.getDefault();
		
		CsvDataModelLoader dataModelLoader = new CsvDataModelLoader();

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
			Path baseParent = path.getParent();
			if (baseParent == null) {
				baseParent = new File(".").toPath();
			}
			String baseFilename = baseFilename(path.getFileName().toString());
			parameters.title = baseFilename;
			loadProperties(baseParent.resolve("csv2chart.properties").toFile(), parameters);
			loadProperties(baseParent.resolve(baseFilename + ".properties").toFile(), parameters);
			
			if (parameters.locale == null) {
				Locale.setDefault(defaultLocale);
			} else {
				Locale.setDefault(parameters.locale);
			}
			
			DataModel dataModel = dataModelLoader.load(path.toFile(), parameters);
			
			ChartFactory chartFactory = createChartFactory(parameters);
			JFreeChart chart = chartFactory.createChart(dataModel, parameters);
			modifyTheme(chart, parameters);
			
			saveChartImage(chart, baseFilename, parameters);
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
		System.out.println("OPTIONS");

		for (String option : argumentHandler.getOptions()) {
			System.out.println("    --" + option + " " + argumentHandler.getOptionArgumentDescription(option));
			String description = argumentHandler.getOptionDescription(option);
			String[] lines = description.split("\n");
			for (String line : lines) {
				System.out.println("        " + line);
			}
			System.out.println();
		}

		System.out.println();
		System.out.println("PROPERTIES");
		System.out.println("   The following properties can be set in the properties file or as comments in the input files.");
		System.out.println();

		for (Parameter parameter : Parameters.getAllParameters()) {
			System.out.println("   " + parameter.name());
			String description = parameter.description();
			String[] lines = description.split("\n");
			for (String line : lines) {
				System.out.println("        " + line);
			}
			System.out.println();
		}
		
	}
	
	private static void loadProperties(File file, Parameters parameters) {
		Properties properties = new Properties();
		try (Reader reader = new BufferedReader(new FileReader(file))) {
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
		case "scatter":
			return new ScatterChartFactory();
		case "bubble":
			return new BubbleChartFactory();
		case "heat":
			return new HeatChartFactory();
		default:
			throw new IllegalArgumentException("Unknown chart: " + type);
		}
	}

	private static void modifyTheme(JFreeChart chart, Parameters parameters) {
		String fontName = "Helvetica";
		
		StandardChartTheme theme = (StandardChartTheme) org.jfree.chart.StandardChartTheme.createJFreeTheme();

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

					if (parameters.valueLabels) {
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
			setPiePlotStyle(piePlot);
		} else if (plot instanceof MultiplePiePlot) {
			MultiplePiePlot multiplePiePlot = (MultiplePiePlot) plot;
			setPiePlotStyle((PiePlot) multiplePiePlot.getPieChart().getPlot());
		}
	
		LegendTitle legend = chart.getLegend();
		if (legend != null) {
			legend.setFrame(BlockBorder.NONE);
		}
	}

	private static void setPiePlotStyle(PiePlot piePlot) {
		piePlot.setOutlineVisible(false);
		piePlot.setShadowPaint(null);
		
		piePlot.setLabelBackgroundPaint(null);
		piePlot.setLabelShadowPaint(null);
		piePlot.setLabelOutlinePaint(null);
		piePlot.setLabelLinkStyle(PieLabelLinkStyle.STANDARD);
		piePlot.setLabelLinkPaint(gray);
	}

	private static String baseFilename(String filename) {
		String result = filename;
		int lastIndexOfSeparator = filename.lastIndexOf(".");
		if (lastIndexOfSeparator > 0) {
			result = filename.substring(0, lastIndexOfSeparator);
		}
		return result;
	}

	private static void saveChartImage(JFreeChart chart, String baseFilename, Parameters parameters) {
		if (parameters.outDir == null) {
			parameters.outDir = ".";
		}
		
		String outFilename = parameters.outPrefix + baseFilename + parameters.outPostfix + "." + parameters.imageFormat.getExtension();
		File outFile = Paths.get(parameters.outDir).resolve(outFilename).toFile();
		
		saveChartImage(chart, outFile, parameters.width, parameters.height, parameters.imageFormat);
	}
	

	private static void saveChartImage(JFreeChart chart, File outputFile, int imageWidth, int imageHeight, ImageFormat imageFormat) {
		if (imageFormat.equals(ImageFormat.SVG)) {
			saveChartSvgImage(chart, outputFile, imageWidth, imageHeight);
			return;
		}

		if (imageFormat.equals(ImageFormat.LOG)) {
			try (Writer writer = new FileWriter(outputFile)) {
				LogGraphics2D graphics = new LogGraphics2D(imageWidth, imageHeight, writer);
				chart.draw(graphics, new Rectangle(imageWidth, imageHeight));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return;
		}

		try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
			switch(imageFormat) {
			case PNG:
				ChartUtilities.writeChartAsPNG(out, chart, imageWidth, imageHeight);
				break;
			case JPG:
				ChartUtilities.writeChartAsJPEG(out, chart, imageWidth, imageHeight);
				break;
			default:
				throw new IllegalArgumentException("Unsupported image format: " + imageFormat);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void saveChartSvgImage(JFreeChart chart, File outputFile, int imageWidth, int imageHeight) {
		SVGGraphics2D graphics = new SVGGraphics2D(imageWidth, imageHeight, SVGUnits.PX);
		Rectangle area = new Rectangle(imageWidth, imageHeight);
		chart.draw(graphics, area);
		
		try {
			SVGUtils.writeToSVG(outputFile, graphics.getSVGElement());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
