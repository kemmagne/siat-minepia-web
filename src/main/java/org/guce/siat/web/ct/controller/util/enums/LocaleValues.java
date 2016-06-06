package org.guce.siat.web.ct.controller.util.enums;

/**
 * The Enum LocaleValues.
 */
public enum LocaleValues
{
	/** The french. */
	FRENCH("fr"),

	/** The english. */
	ENGLISH("en");

	/** The code. */
	private final String code;

	/**
	 * Instantiates a new locale values.
	 *
	 * @param code
	 *           the code
	 */
	private LocaleValues(final String code)
	{
		this.code = code.intern();
	}


	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode()
	{
		return this.code;
	}
}
