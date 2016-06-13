package ch.obermuhlner.csv2chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.BubbleXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.SymbolicXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
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

	public static void main(String[] args) {
		CsvDataLoader dataLoader = new CsvDataLoader();

		Parameters globalParameters = new Parameters();

		boolean parsingOptions = true;
		List<Path> inputFiles = new ArrayList<>(); 
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			if (parsingOptions) {
				if (arg.startsWith("--")) {
					String option = arg.substring(2);
					switch (option) {
					case "":
						parsingOptions = false;
						i++;
						break;
					case "chart":
						globalParameters.chart = args[++i];
						break;
					case "dir":
						globalParameters.directory = args[++i];
						break;
					case "pattern":
						globalParameters.filePattern = args[++i];
						break;
					case "title":
						globalParameters.title = args[++i];
						break;
					case "header-column":
						globalParameters.headerColumn = Boolean.parseBoolean(args[++i]);
						break;
					case "header-row":
						globalParameters.headerRow = Boolean.parseBoolean(args[++i]);
						break;
					case "x-axis":
						globalParameters.xAxisLabel = args[++i];
						break;
					case "y-axis":
						globalParameters.yAxisLabel = args[++i];
						break;
					case "width":
						globalParameters.width = Integer.parseInt(args[++i]);
						break;
					case "height":
						globalParameters.height = Integer.parseInt(args[++i]);
						break;
					default:
						error("Unknown option: " + option);
					}
				} else {
					parsingOptions = false;
				}
			} 
			
			if (!parsingOptions) {
				inputFiles.add(Paths.get(arg));
			}
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
			Data data = dataLoader.load(path.toString(), parameters);
			if (parameters.title == null) {
				parameters.title = baseFilename;
			}
			ChartFactory chartFactory = createChartFactory(parameters);
			JFreeChart chart = chartFactory.createChart(data, parameters);
			modifyTheme(chart, parameters);
			saveChartImage(chart, baseFilename, parameters.width, parameters.height);
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

			for (int i = 0; i < xyPlot.getSeriesCount(); i++) {
				xyPlot.getRenderer().setSeriesStroke(i, new BasicStroke(3.0f));
				
				XYItemRenderer renderer = xyPlot.getRenderer();
				if (renderer instanceof XYBubbleRenderer) {
					XYBubbleRenderer xyBubbleRenderer = (XYBubbleRenderer) renderer;

					if (parameters.crowdedLegend) {
						xyBubbleRenderer.setItemLabelGenerator(new BubbleXYItemLabelGenerator() {
							@Override
							public String generateLabel(XYDataset dataset, int series, int item) {
								DefaultXYZDataset xyzDataset = (DefaultXYZDataset) dataset;
								return String.valueOf(xyzDataset.getSeriesKey(series));
							}
						});
						xyBubbleRenderer.setItemLabelsVisible(true);
						xyBubbleRenderer.setItemLabelPaint(gray);
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
