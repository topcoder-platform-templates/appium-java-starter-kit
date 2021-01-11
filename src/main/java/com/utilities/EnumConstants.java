package com.utilities;

public class EnumConstants {
	
	/**
	 * the locators for use in elementTracer.
	 * @author abc
	 *
	 */
	public enum Locator { 
		XPATH, ID, CLASSNAME, NAME, LINKTEXT, PARTIALLINKTEXT;
	}
	
	/**
	 * contains Direction for switching the switch buttons inside the pages
	 * @author abc
	 *
	 */
	public enum Direction {
		LEFT,RIGHT, UP, DOWN;
	}

	/**
	 * Represents possible screen orientations options.
	 * @author abc
	 *
	 */
	public enum Orientation {
		PORTRAIT, LANDSCAPE;

	}

	/**
	 * the dark mode options for Slack app display
	 * @author abc
	 *
	 */
	public enum Mode {
		OFF, ON, BATTERY_SAVER;
	}

}