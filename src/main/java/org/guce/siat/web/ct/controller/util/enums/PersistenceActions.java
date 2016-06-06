package org.guce.siat.web.ct.controller.util.enums;

/**
 * The Enum PersistenceAction.
 */
public enum PersistenceActions
{
	/** The create. */
	CREATE("Created"),

	/** The delete. */
	DELETE("Deleted"),

	/** The update. */
	UPDATE("Updated");

	/** The code. */
	private final String code;


	/**
	 * Instantiates a new persistence action.
	 *
	 * @param code
	 *           the code
	 */
	private PersistenceActions(final String code)
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
