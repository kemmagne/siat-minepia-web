package org.guce.siat.web.ct.controller.util.enums;

/**
 * The Enum LocaleValues.
 */
public enum DataTypeEnumeration
{

	/** The inputtext. */
	INPUTTEXT("inputText"),

	/** The chekbox. */
	CHEKBOX("selectBooleanCheckbox"),

	/** The calendar. */
	CALENDAR("calendar"),

	/** The inputtextarea. */
	INPUTTEXTAREA("inputTextarea");

	/** The inputnumber. */
	//	INPUTNUMBER("inputNumber");

	/** The code. */
	private final String code;

	/**
	 * Instantiates a new locale values.
	 *
	 * @param code
	 *           the code
	 */
	private DataTypeEnumeration(final String code)
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
