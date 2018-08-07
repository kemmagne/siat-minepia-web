package org.guce.siat.web.reports.vo;

import java.util.Date;


/**
 * The Class VtMinepiaFileVo.
 */
public class VtMinepdedFileVo extends AbstractFileVo<VtMinepdedFileItemVo>
{

	/** The country of origin. */
	private String countryOfOrigin;

	/** The country of provenance. */
	private String countryOfProvenance;

	/** The provider. */
	private String provider;

	/** The invoice. */
	private String invoice;

	/** The importer. */
	private String importer;

	/** The address. */
	private String address;

	/** The profession. */
	private String profession;

	/** The code. */
	private String code;

        /**
        * The record creation date.
        */
        private Date fileCreationDate;
	/**
	 * Gets the country of origin.
	 *
	 * @return the country of origin
	 */
	public String getCountryOfOrigin()
	{
		return countryOfOrigin;
	}

	/**
	 * Sets the country of origin.
	 *
	 * @param countryOfOrigin
	 *           the new country of origin
	 */
	public void setCountryOfOrigin(final String countryOfOrigin)
	{
		this.countryOfOrigin = countryOfOrigin;
	}

	/**
	 * Gets the provider.
	 *
	 * @return the provider
	 */
	public String getProvider()
	{
		return provider;
	}

	/**
	 * Sets the provider.
	 *
	 * @param provider
	 *           the new provider
	 */
	public void setProvider(final String provider)
	{
		this.provider = provider;
	}

	/**
	 * Gets the invoice.
	 *
	 * @return the invoice
	 */
	public String getInvoice()
	{
		return invoice;
	}

	/**
	 * Sets the invoice.
	 *
	 * @param invoice
	 *           the new invoice
	 */
	public void setInvoice(final String invoice)
	{
		this.invoice = invoice;
	}

	/**
	 * Gets the country of provenance.
	 *
	 * @return the country of provenance
	 */
	public String getCountryOfProvenance()
	{
		return countryOfProvenance;
	}

	/**
	 * Sets the country of provenance.
	 *
	 * @param countryOfProvenance
	 *           the new country of provenance
	 */
	public void setCountryOfProvenance(final String countryOfProvenance)
	{
		this.countryOfProvenance = countryOfProvenance;
	}

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
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address
	 *           the new address
	 */
	public void setAddress(final String address)
	{
		this.address = address;
	}

	/**
	 * Gets the profession.
	 *
	 * @return the profession
	 */
	public String getProfession()
	{
		return profession;
	}

	/**
	 * Sets the profession.
	 *
	 * @param profession
	 *           the new profession
	 */
	public void setProfession(final String profession)
	{
		this.profession = profession;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code
	 *           the new code
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

        public Date getFileCreationDate() {
            return fileCreationDate;
        }

        public void setFileCreationDate(Date fileCreationDate) {
            this.fileCreationDate = fileCreationDate;
        }
}
