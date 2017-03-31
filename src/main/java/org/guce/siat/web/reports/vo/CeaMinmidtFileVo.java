package org.guce.siat.web.reports.vo;

import java.util.Date;


/**
 * The Class CeaMinmidtFileVo.
 */
public class CeaMinmidtFileVo extends AbstractFileVo<CeaMinmidtFileItemVo>
{

	/** The company. */
	private String company;

	/** The decree number. */
	private String decreeNumber;

	/** The destination country. */
	private String destinationCountry;

	/** The decree date. */
	private Date decreeDate;

	/**
	 * Gets the company.
	 *
	 * @return the company
	 */
	public String getCompany()
	{
		return company;
	}

	/**
	 * Sets the company.
	 *
	 * @param company
	 *           the new company
	 */
	public void setCompany(final String company)
	{
		this.company = company;
	}

	/**
	 * Gets the decree number.
	 *
	 * @return the decree number
	 */
	public String getDecreeNumber()
	{
		return decreeNumber;
	}

	/**
	 * Sets the decree number.
	 *
	 * @param decreeNumber
	 *           the new decree number
	 */
	public void setDecreeNumber(final String decreeNumber)
	{
		this.decreeNumber = decreeNumber;
	}

	/**
	 * Gets the destination country.
	 *
	 * @return the destination country
	 */
	public String getDestinationCountry()
	{
		return destinationCountry;
	}

	/**
	 * Sets the destination country.
	 *
	 * @param destinationCountry
	 *           the new destination country
	 */
	public void setDestinationCountry(final String destinationCountry)
	{
		this.destinationCountry = destinationCountry;
	}

	/**
	 * Gets the decree date.
	 *
	 * @return the decree date
	 */
	public Date getDecreeDate()
	{
		return decreeDate;
	}

	/**
	 * Sets the decree date.
	 *
	 * @param decreeDate
	 *           the new decree date
	 */
	public void setDecreeDate(final Date decreeDate)
	{
		this.decreeDate = decreeDate;
	}

}
