package org.guce.siat.web.reports.vo;



/**
 * The Class CpMinepdedFileVo.
 */
public class CpMinepdedFileVo extends AbstractFileVo<CpMinepdedFileItemVo>
{

	/** The importing country. */
	private String importingCountry;

	/** The reference number. */
	private String referenceNumber;

	/** The name. */
	private String name;

	/** The adresse. */
	private String adresse;

	/**
	 * Gets the importing country.
	 *
	 * @return the importing country
	 */
	public String getImportingCountry()
	{
		return importingCountry;
	}

	/**
	 * Sets the importing country.
	 *
	 * @param importingCountry
	 *           the new importing country
	 */
	public void setImportingCountry(final String importingCountry)
	{
		this.importingCountry = importingCountry;
	}

	/**
	 * Gets the reference number.
	 *
	 * @return the reference number
	 */
	public String getReferenceNumber()
	{
		return referenceNumber;
	}

	/**
	 * Sets the reference number.
	 *
	 * @param referenceNumber
	 *           the new reference number
	 */
	public void setReferenceNumber(final String referenceNumber)
	{
		this.referenceNumber = referenceNumber;
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
	 *           the new name
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Gets the adresse.
	 *
	 * @return the adresse
	 */
	public String getAdresse()
	{
		return adresse;
	}

	/**
	 * Sets the adresse.
	 *
	 * @param adresse
	 *           the new adresse
	 */
	public void setAdresse(final String adresse)
	{
		this.adresse = adresse;
	}

}
