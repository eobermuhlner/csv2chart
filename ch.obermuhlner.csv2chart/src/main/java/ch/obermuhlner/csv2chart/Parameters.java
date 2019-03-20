package ch.obermuhlner.csv2chart;

import ch.obermuhlner.csv2chart.graphics.Colors;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Parameters implements Cloneable {

	@Parameter(
			name = "locale",
			description = "The locale used for formatting values.\n"
					+ "The format is: language_country\n"
					+ "For example: en_US for US english.\n"
					+ "Default: system locale",
			optionName = "locale",
			optionArgumentDescription = "locale")
	public Locale locale = null;
	
	@Parameter(
			name = "out.dir",
			description = "The output directory.\n"
					+ "Default: same directory as the input file",
			optionName = "out-dir",
			optionArgumentDescription = "path")
	public String outDir = null;
	
	@Parameter(
			name = "out.prefix",
			description = ""
					+ "Prefix for output chart files.\n"
					+ "Default: ''",
			optionName = "out-prefix",
			optionArgumentDescription = "text")
	public String outPrefix = "";
	@Parameter(
			name = "out.postfix",
			description = ""
					+ "Postfix for output chart files.\n"
					+ "Default: ''",
			optionName = "out-postfix",
			optionArgumentDescription = "text")
	public String outPostfix = "";

	@Parameter(
			name = "out.format",
			description = ""
					+ "Format of output chart files.\n"
					+ "Supported formats: svg, png, jpg\n"
					+ "Default: svg",
			optionName = "format",
			optionArgumentDescription = "format")
	public ImageFormat imageFormat = ImageFormat.SVG;

	@Parameter(
			name = "chart",
			description = ""
					+ "The chart type to generate.\n"
					+ "Supported types: auto, bar, line, xyline, pie, bubble, scatter, heat\n"
					+ "Default: auto")
	public String chart = "auto";
	
	@Parameter(
			name = "title",
			description = ""
					+ "Text to appear as title in the chart.\n"
					+ "Default: basename of the input file")
	public String title = null;

    @Parameter(
            name = "subtitle",
            description = ""
                    + "Text to appear as sub title in the chart.\n"
                    + "Default: none")
    public String subtitle = null;

    @Parameter(
			name = "matrix-x-values",
			description = ""
					+ "Specifies whether the first row of matrix values is used as values on the x-axis.\n"
					+ "Default: true")
	public boolean matrixXValues = true;

	@Parameter(
			name = "matrix-y-values",
			description = ""
					+ "Specifies whether the first row of matrix values is used as values on the x-axis.\n"
					+ "Default: true")
	public boolean matrixYValues = true;
	
	@Parameter(
			name = "x-axis",
			description = "Text to appear as label on the x-axis.")
	public String xAxisLabel;
	@Parameter(
			name = "y-axis",
			description = "Text to appear as label on the y-axis.")
	public String yAxisLabel;
	@Parameter(
			name = "z-axis",
			description = "Text to appear as label on the z-axis.")
	public String zAxisLabel;

	public Double xAxisMinDelta;
	public Double yAxisMinDelta;
	
	@Parameter(
			name = "value-labels-threshold",
			description = ""
					+ "Threshold of value labels count to switch from a separate legend to value labels.\n"
					+ "Default: 5")
	public int autoValueLabelsThreshold = 5;
	
	@Parameter(
			name = "value-labels",
			description = ""
					+ "Labels appear next to the values where possible.\n"
					+ "Default: Dynamically determined by the parameter 'value-labels-threshold'")
	public Boolean valueLabels;

	@Parameter(
			name = "legend",
			description = ""
					+ "Shows a legend for the value categories where necessary.\n"
					+ "Default: Dynamically determined (generally true)")
	public Boolean legend;

	@Parameter(
			name = "data-colors",
			description = ""
					+ "Color scheme to render data.\n"
					+ "Supported schemes: wheel, random\n"
					+ "Default: wheel")
	public DataColors dataColors = DataColors.WHEEL;

	@Parameter(
			name = "data-color-saturation",
			description = ""
					+ "Saturation of data colors.\n"
					+ "Supported values: 0.0 - 1.0\n"
					+ "Default: 0.7")
	public double dataColorSaturation = 0.7;

	@Parameter(
			name = "data-color-brightness",
			description = ""
					+ "Brightness of data colors.\n"
					+ "Supported values: 0.0 - 1.0\n"
					+ "Default: 0.9")
	public double dataColorBrightness = 0.9;

	@Parameter(
			name = "data-color-alpha",
			description = ""
					+ "Alpha value of data colors.\n"
					+ "Supported values: 0.0 - 1.0\n"
					+ "Default: 0.8")
	public double dataColorAlpha = 0.8;

	@Parameter(
			name = "scale-min-value",
			description = ""
					+ "Minimum value of the color scale.")
	public Double colorScaleMinValue = null;
	@Parameter(
			name = "scale-mid-value",
			description = ""
					+ "Mid value of the color scale.")
	public Double colorScaleMidValue = null;
	@Parameter(
			name = "scale-max-value",
			description = ""
					+ "Maximum value of the color scale.")
	public Double colorScaleMaxValue = null;

	@Parameter(
			name = "scale-min-color",
			description = ""
					+ "Minimum color of the color scale as a hex value (RRGGBB).")
	public Color colorScaleMinColor = null;
	@Parameter(
			name = "scale-mid-color",
			description = ""
					+ "Mid color of the color scale as a hex value (RRGGBB).")
	public Color colorScaleMidColor = null;
	@Parameter(
			name = "scale-max-color",
			description = ""
					+ "Maximum color of the color scale as a hex value (RRGGBB).")
	public Color colorScaleMaxColor = null;
	@Parameter(
			name = "scale-default-color",
			description = ""
					+ "Default color of the color scale used for non-existing values.")
	public Color colorScaleDefaultColor = null;

	@Parameter(
			name = "out.width",
			description = ""
					+ "The width of the generated charts in pixels.\n"
					+ "Default: 800",
			optionName = "width",
			optionArgumentDescription = "pixels")
	public int width = 800;
	@Parameter(
			name = "out.height",
			description = ""
					+ "The height of the generated charts in pixels.\n"
					+ "Default: 600",
			optionName = "height",
			optionArgumentDescription = "pixels")
	public int height = 600;

	@Parameter(
			name = "theme",
			description = ""
					+ "Color theme.\n"
					+ "Supported themes: light, dark, lightSolarized, darkSolarized\n"
					+ "Default: light",
			optionName = "theme",
			optionArgumentDescription = "name")
	public String theme = "light";

	@Parameter(
			name = "color.background",
			description = ""
					+ "Color of the background.")
	public Color themeBackgroundColor = Color.white;

	@Parameter(
			name = "color.title",
			description = ""
					+ "Color of the title.")
	public Color themeTitleColor = Color.black;

	@Parameter(
			name = "color.subtitle",
			description = ""
					+ "Color of the subtitle.")
	public Color themeSubtitleColor = Color.black;

	@Parameter(
			name = "color.legend",
			description = ""
					+ "Color of the legend.")
	public Color themeLegendColor = Color.black;

	@Parameter(
			name = "color.label",
			description = ""
					+ "Color of the text labels.")
	public Color themeLabelColor = Colors.gray50;

	@Parameter(
			name = "color.axis.line",
			description = ""
					+ "Color of the axis line.")
	public Color themeAxisLineColor = Colors.gray20;

	@Parameter(
			name = "color.axis.label",
			description = ""
					+ "Color of the axis labels.")
	public Color themeAxisLabelColor = Colors.gray20;

	@Parameter(
			name = "color.grid.line",
			description = ""
					+ "Color of the grid lines.")
	public Color themeGridLineColor = Colors.gray90;

	@Parameter(
			name = "color.grid.band",
			description = ""
					+ "Color of the grid band.")
	public Color themeGridBandColor = Color.red;

	public void setParameterKeyValue(String keyValue) {
		int assignmentIndex = keyValue.indexOf("=");
		if (assignmentIndex < 0) {
			throw new RuntimeException("Missing '=': " + keyValue);
		}
		if (assignmentIndex == 0) {
			throw new RuntimeException("Missing key: " + keyValue);
		}
		
		String key = keyValue.substring(0, assignmentIndex);
		String value = keyValue.substring(assignmentIndex + 1);
		setParameter(key, value);
	}

	public void setParameter(String name, Object value) {
		try {
			String string = String.valueOf(value);
			Field field = findField(name);
			Class<?> fieldType = field.getType();
			if (fieldType == Integer.class || fieldType == int.class) {
				value = Integer.parseInt(string);
			} else if (fieldType == Boolean.class || fieldType == boolean.class) {
				value = Boolean.parseBoolean(string);
			} else if (fieldType == Color.class) {
				value = Colors.parseColor(string);
			} else if (fieldType == Double.class || fieldType == double.class) {
				value = Double.parseDouble(string);
			} else if (fieldType == ImageFormat.class) {
				value = ImageFormat.valueOf(string.toUpperCase());
			} else if (fieldType == ColorTheme.class) {
				value = ColorTheme.valueOf(string.toUpperCase());
			} else if (fieldType == DataColors.class) {
				value = DataColors.valueOf(string.toUpperCase());
			} else if (fieldType == Locale.class) {
				value = Locale.forLanguageTag(string);
			}
			field.set(this, value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private Field findField(String name) throws NoSuchFieldException, SecurityException {
		for (Field field : Parameters.class.getFields()) {
			Parameter option = field.getAnnotation(Parameter.class);
			if (option != null && name.equals(option.name())) {
				return field;
			}
		}
		
		return Parameters.class.getField(name);
	}
	
	public static List<Parameter> getAllParameters() {
		return Arrays.stream(Parameters.class.getFields())
			.map(field -> field.getAnnotation(Parameter.class))
			.filter(parameter -> parameter != null)
			.sorted(Comparator.comparing(Parameter::name))
			.collect(Collectors.toList());
	}
	
	public static List<Parameter> getAllCommandLineOptions() {
		return Arrays.stream(Parameters.class.getFields())
			.map(field -> field.getAnnotation(Parameter.class))
			.filter(parameter -> parameter != null)
			.filter(parameter -> !parameter.optionName().equals(""))
			.sorted(Comparator.comparing(Parameter::optionName))
			.collect(Collectors.toList());
	}

	public static <T> T withDefault(T value, T defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	public Parameters copy() {
		try {
			return (Parameters) super.clone();
		} catch (CloneNotSupportedException e) {
			// never happens
		}
		return null;
	}
}
