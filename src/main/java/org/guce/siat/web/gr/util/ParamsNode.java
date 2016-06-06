package org.guce.siat.web.gr.util;



/**
 * The Class ParamsNode.
 */
public class ParamsNode
{

	/** The name. */
	private String name;

	/** The value. */
	private String value;

	/** The Type. */
	private String type;



	/**
	 * Instantiates a new params node.
	 */
	public ParamsNode()
	{
	}

	/**
	 * Instantiates a new params node.
	 *
	 * @param name
	 *           the name
	 * @param value
	 *           the value
	 * @param type
	 *           the type
	 */
	public ParamsNode(final String name, final String value, final String type)
	{
		super();
		this.name = name;
		this.value = value;
		this.type = type;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *           the value to set
	 */
	public void setValue(final String value)
	{
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type
	 *           the type to set
	 */
	public void setType(final String type)
	{
		this.type = type;
	}

}
