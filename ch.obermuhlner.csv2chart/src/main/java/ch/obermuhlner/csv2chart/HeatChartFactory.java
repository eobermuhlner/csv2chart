package ch.obermuhlner.csv2chart;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

public class HeatChartFactory extends AbstractChartFactory {

	@Override
	public JFreeChart createChart(Data data, Parameters parameters) {
		XYZDataset dataset = createXYZDataset(data, parameters);

		NumberAxis xAxis = new NumberAxis(parameters.xAxisLabel);
		xAxis.setAutoRangeIncludesZero(false);
		NumberAxis yAxis = new NumberAxis(parameters.yAxisLabel);
		yAxis.setAutoRangeIncludesZero(false);

		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, null);

		XYBlockRenderer renderer = new XYBlockRenderer();

		GrayPaintScale paintScale = new GrayPaintScale(parameters.colorScaleMinValue, parameters.colorScaleMaxValue);
		
		renderer.setPaintScale(paintScale);
		plot.setRenderer(renderer);
		plot.setOrientation(PlotOrientation.HORIZONTAL);

		JFreeChart chart = new JFreeChart(parameters.title, JFreeChart.DEFAULT_TITLE_FONT, plot, false);

		boolean legend = !Boolean.TRUE.equals(parameters.crowdedLegend);
		if (legend) {
			NumberAxis scaleAxis = new NumberAxis("Scale");
	        scaleAxis.setAxisLinePaint(Color.WHITE);
	        scaleAxis.setTickMarkPaint(Color.WHITE);
	        scaleAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 7));
	        scaleAxis.setRange(parameters.colorScaleMinValue, parameters.colorScaleMaxValue);
	        
			PaintScaleLegend scaleLegend = new PaintScaleLegend(paintScale, scaleAxis);
			scaleLegend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
			scaleLegend.setAxisOffset(5.0);
			scaleLegend.setMargin(new RectangleInsets(5, 5, 5, 5));
			scaleLegend.setPadding(new RectangleInsets(10, 10, 10, 10));
			scaleLegend.setStripWidth(10);
			scaleLegend.setPosition(RectangleEdge.RIGHT);
			chart.addSubtitle(scaleLegend);
		}
		
		return chart;
	}

	private XYZDataset createXYZDataset(Data data, Parameters parameters) {
		List<List<String>> rows = data.getRows();
		
		DefaultXYZDataset dataset = new DefaultXYZDataset();
		
		int headerColumnCount = parameters.headerColumn ? 1 : 0;
		
		int headerRowCount = data.getHeaderRowCount();
		if (headerRowCount == 0 && parameters.headerRow) {
			headerRowCount = 1;
		}
		
		int n = (rows.size() - headerRowCount) * (rows.get(0).size() - headerColumnCount);
		double[] xValues = new double[n];
		double[] yValues = new double[n];
		double[] zValues = new double[n];
		
		double minValue = Double.MAX_VALUE;
		double maxValue = -Double.MAX_VALUE;
		
		int index = 0;
		for (int rowIndex = headerRowCount; rowIndex < rows.size(); rowIndex++) {
			List<String> row = rows.get(rowIndex);
			for (int columnIndex = headerColumnCount; columnIndex < row.size(); columnIndex++) {
				double value = CsvDataLoader.toDouble(row.get(columnIndex));
				
				xValues[index] = rowIndex;
				yValues[index] = columnIndex;
				zValues[index] = value;
				
				minValue = Math.min(minValue, value);
				maxValue = Math.max(maxValue, value);
				
				index++;
			}
		}
		
		dataset.addSeries("series", new double[][] { xValues, yValues, zValues });
		
		if (parameters.colorScaleMinValue == null) {
			parameters.colorScaleMinValue = minValue;
		}
		if (parameters.colorScaleMaxValue == null) {
			parameters.colorScaleMaxValue = maxValue;
		}

		return dataset;
	}
}