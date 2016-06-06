package org.guce.siat.web.ct.controller.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.guce.siat.common.model.Country;


/**
 * The Class TradeCountryDto.
 */
public class TradeCountryDto implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The country. */
	private Country country;
	/** The data. */
	private List<String> data = new ArrayList<String>();

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public List<String> getData()
	{
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data
	 *           the new data
	 */
	public void setData(final List<String> data)
	{
		this.data = data;
	}

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public Country getCountry()
	{
		return country;
	}

	/**
	 * Sets the country.
	 *
	 * @param country
	 *           the new country
	 */
	public void setCountry(final Country country)
	{
		this.country = country;
	}

}
