package ch.obermuhlner.csv2chart;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
public @interface Parameter {
	
	/**
	 * The name of the parameter property.
	 * 
	 * @return the name
	 */
    String name();

	/**
	 * The description of the property.
	 * 
	 * @return the description
	 */
    String description();

	/**
	 * The name of the command line option.
	 * 
	 * This will be used as the -- command line option.
	 * 
	 * @return the name or "" for none
	 */
    String optionName() default "";

	/**
	 * The description of the command line option argument.
	 * 
	 * This will be used as the description of the -- command line option arguments.
	 * 
	 * @return the name or "" for none
	 */
    String optionArgumentDescription() default "";

}
