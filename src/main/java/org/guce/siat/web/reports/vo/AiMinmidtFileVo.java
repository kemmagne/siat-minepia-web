package org.guce.siat.web.reports.vo;

import java.util.Date;



/**
 * The Class AiMinmidtFileVo.
 */
public class AiMinmidtFileVo extends AbstractFileVo<AiMinmidtFileItemVo>
{

	/** The importer. */
	private String importer;

	/** The validity date. */
	private Date validityDate;

	/** The origin country. */
	private String originCountry;


	/**
	 * Gets the importer.
	 *
	 * @return the importer
	 */
	public String getImporter()
	{
		return importer;
	}

	/**
	 * Sets the importer.
	 *
	 * @param importer
	 *           the new importer
	 */
	public void setImporter(final String importer)
	{
		this.importer = importer;
	}

	/**
	 * Gets the validity date.
	 *
	 * @return the validity date
	 */
	public Date getValidityDate()
	{
		return validityDate;
	}

	/**
	 * Sets the validity date.
	 *
	 * @param validityDate
	 *           the new validity date
	 */
	public void setValidityDate(final Date validityDate)
	{
		this.validityDate = validityDate;
	}

	/**
	 * Gets the origin country.
	 *
	 * @return the origin country
	 */
	public String getOriginCountry()
	{
		return originCountry;
	}

	/**
	 * Sets the origin country.
	 *
	 * @param originCountry
	 *           the new origin country
	 */
	public void setOriginCountry(final String originCountry)
	{
		this.originCountry = originCountry;
	}

}
