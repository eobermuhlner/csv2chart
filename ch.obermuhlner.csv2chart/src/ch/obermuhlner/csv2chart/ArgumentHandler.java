package ch.obermuhlner.csv2chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ArgumentHandler<T> {

	private static final String OPTION_MARKER = "--";
	
	private static final String END_OF_OPTIONS_MARKER = OPTION_MARKER;

	private static final String ASSIGN_MARKER = "=";

	Map<String, Option<T>> options = new HashMap<>();
	
	public void addOption(String name, int argumentCount, BiConsumer<List<String>, T> optionHandler) {
		Option<T> option = new Option<T>();
		option.name = name;
		option.argumentCount = argumentCount;
		option.optionHandler = optionHandler;
		options.put(name, option);
	}
	
	public List<String> parseOptions(String[] args, T target) {
		List<String> arguments = new ArrayList<>();

		boolean optionsMode = true;
		
		for (int argumentIndex = 0; argumentIndex < args.length; argumentIndex++) {
			String arg = args[argumentIndex];
			
			if (optionsMode) {
				if (arg.equals(END_OF_OPTIONS_MARKER)) {
					optionsMode = false;
					argumentIndex++;
				} else if (arg.startsWith(OPTION_MARKER)) {
					String optionName = arg.substring(OPTION_MARKER.length());
					String firstOptionArgument = null;
					
					int indexOfAssign = optionName.indexOf(ASSIGN_MARKER);
					if (indexOfAssign >= 0) {
						firstOptionArgument = optionName.substring(indexOfAssign + 1);
						optionName = optionName.substring(0, indexOfAssign);
					}
					
					Option<T> option = options.get(optionName);
					if (option != null) {
						int optionArgumentCount = option.argumentCount;
						List<String> optionArguments = new ArrayList<>();
						if (firstOptionArgument != null) {
							if (optionArgumentCount == 0) {
								error("Option " + optionName + " does not allow arguments");
							}
							optionArgumentCount--;
							optionArguments.add(firstOptionArgument);
						}

						if (argumentIndex + optionArgumentCount > args.length) {
							error("Not enough arguments for option: " + option.name);
						}
						
						for (int optionArgumentIndex = 0; optionArgumentIndex < optionArgumentCount; optionArgumentIndex++) {
							optionArguments.add(args[++argumentIndex]);
						}
						option.optionHandler.accept(optionArguments, target);
					}
				} else {
					optionsMode = false;
				}
			}
			
			if (!optionsMode) {
				arguments.add(arg);
			}
		}
		
		return arguments;
	}
	
	private void error(String string) {
		System.err.println(string);
		System.exit(1);
	}

	private static class Option<T> {
		String name;
		int argumentCount;
		BiConsumer<List<String>, T> optionHandler;
	}
}
