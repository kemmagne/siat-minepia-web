package org.guce.siat.web.ct.data;

import java.io.Serializable;


/**
 * The Class ServiceItemDto.
 */
public class ServiceItemDto implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5442801345052705741L;

	/** The ssnh. */
	private String ssnh;

	/** The label. */
	private String label;

	/** The sum quantities. */
	private String sumQuantities;

	/** The sum valeur fob. */
	private String sumValeurFob;


	/**
	 * Instantiates a new service item dto.
	 */
	public ServiceItemDto()
	{
	}


	/**
	 * Instantiates a new service item dto.
	 *
	 * @param ssnh
	 *           the ssnh
	 * @param label
	 *           the label
	 * @param sumQuantities
	 *           the sum quantities
	 * @param sumValeurFob
	 *           the sum valeur fob
	 */
	public ServiceItemDto(final String ssnh, final String label, final String sumQuantities, final String sumValeurFob)
	{
		this.ssnh = ssnh;
		this.label = label;
		this.sumQuantities = sumQuantities;
		this.sumValeurFob = sumValeurFob;
	}


	/**
	 * Gets the ssnh.
	 *
	 * @return the ssnh
	 */
	public String getSsnh()
	{
		return ssnh;
	}


	/**
	 * Sets the ssnh.
	 *
	 * @param ssnh
	 *           the new ssnh
	 */
	public void setSsnh(final String ssnh)
	{
		this.ssnh = ssnh;
	}


	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}


	/**
	 * Sets the label.
	 *
	 * @param label
	 *           the new label
	 */
	public void setLabel(final String label)
	{
		this.label = label;
	}


	/**
	 * Gets the sum quantities.
	 *
	 * @return the sum quantities
	 */
	public String getSumQuantities()
	{
		return sumQuantities;
	}


	/**
	 * Sets the sum quantities.
	 *
	 * @param sumQuantities
	 *           the new sum quantities
	 */
	public void setSumQuantities(final String sumQuantities)
	{
		this.sumQuantities = sumQuantities;
	}


	/**
	 * Gets the sum valeur fob.
	 *
	 * @return the sum valeur fob
	 */
	public String getSumValeurFob()
	{
		return sumValeurFob;
	}


	/**
	 * Sets the sum valeur fob.
	 *
	 * @param sumValeurFob
	 *           the new sum valeur fob
	 */
	public void setSumValeurFob(final String sumValeurFob)
	{
		this.sumValeurFob = sumValeurFob;
	}
}
