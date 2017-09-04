package ch.obermuhlner.csv2chart;

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
			name = "header-row",
			description = "DEPRECATED")
	@Deprecated
	public boolean headerRow = true;

	@Parameter(
			name = "header-column",
			description = "DEPRECATED")
	@Deprecated
	public boolean headerColumn = true;
	
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

	@Deprecated
	public Boolean crowdedLegend;
	
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
			Field field = findField(name);
			Class<?> fieldType = field.getType();
			if (fieldType == Integer.class || fieldType == int.class) {
				value = Integer.parseInt(String.valueOf(value));
			} else if (fieldType == Boolean.class || fieldType == boolean.class) {
				value = Boolean.parseBoolean(String.valueOf(value));
			} else if (fieldType == Color.class) {
				value = new Color(Integer.parseInt(String.valueOf(value), 16));
			} else if (fieldType == Double.class || fieldType == double.class) {
				value = Double.parseDouble(String.valueOf(value));
			} else if (fieldType == ImageFormat.class) {
				value = ImageFormat.valueOf(String.valueOf(value).toUpperCase());
			} else if (fieldType == Locale.class) {
				value = Locale.forLanguageTag(String.valueOf(value));
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
	
	public Parameters copy() {
		try {
			return (Parameters) super.clone();
		} catch (CloneNotSupportedException e) {
			// never happens
		}
		return null;
	}
}
