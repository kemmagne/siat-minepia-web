package org.guce.siat.web.ct.controller.util;

import java.util.List;

import org.guce.siat.common.utils.TableColHeader;


/**
 * The Class TradeEvolutionDto.
 */
public class TradeEvolutionDto extends TableColHeader
{

	/** The data. */
	private List<String> data;

	/** The important. */
	private Boolean important;


	/**
	 * Gets the important.
	 *
	 * @return the important
	 */
	public Boolean getImportant()
	{
		return important;
	}

	/**
	 * Sets the important.
	 *
	 * @param important
	 *           the new important
	 */
	public void setImportant(final Boolean important)
	{
		this.important = important;
	}


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
	 * Instantiates a new trade evolution dto.
	 *
	 * @param labelFr
	 *           the label fr
	 * @param labelEn
	 *           the label en
	 * @param important
	 *           the important
	 */
	public TradeEvolutionDto(final String labelFr, final String labelEn, final Boolean important)
	{
		super(labelFr, labelEn);
		this.important = important;

	}


}
