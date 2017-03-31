package org.guce.siat.web.reports.vo;

import java.util.Date;




/**
 * The Class DiMinaderAtFileVo.
 */
public class DiMinaderAtFileVo extends AbstractFileVo<DiMinaderAtFileItemVo>
{

	/** The entry point. */
	private String entryPoint;

	/** The arrival date. */
	private Date arrivalDate;

	/** The origin country. */
	private String originCountry;

	/** The destination country. */
	private String destinationCountry;

	/** The importer. */
	private String importer;

	/** The transport mode. */
	private String transportMode;

	/** The reference. */
	private String reference;

	/** The validity date. */
	private Date validityDate;

	/** The local representative. */
	private String localRepresentative;

	/** The certificate holder. */
	private String certificateHolder;

	/** The reference date. */
	private String referenceDate;

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
	 * Gets the entry point.
	 *
	 * @return the entry point
	 */
	public String getEntryPoint()
	{
		return entryPoint;
	}


	/**
	 * Sets the entry point.
	 *
	 * @param entryPoint
	 *           the new entry point
	 */
	public void setEntryPoint(final String entryPoint)
	{
		this.entryPoint = entryPoint;
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
	 * Gets the arrival date.
	 *
	 * @return the arrival date
	 */
	public Date getArrivalDate()
	{
		return arrivalDate;
	}


	/**
	 * Sets the arrival date.
	 *
	 * @param arrivalDate
	 *           the new arrival date
	 */
	public void setArrivalDate(final Date arrivalDate)
	{
		this.arrivalDate = arrivalDate;
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

	/**
	 * Gets the transport mode.
	 *
	 * @return the transport mode
	 */
	public String getTransportMode()
	{
		return transportMode;
	}

	/**
	 * Sets the transport mode.
	 *
	 * @param transportMode
	 *           the new transport mode
	 */
	public void setTransportMode(final String transportMode)
	{
		this.transportMode = transportMode;
	}

	/**
	 * Gets the reference.
	 *
	 * @return the reference
	 */
	public String getReference()
	{
		return reference;
	}

	/**
	 * Sets the reference.
	 *
	 * @param reference
	 *           the new reference
	 */
	public void setReference(final String reference)
	{
		this.reference = reference;
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
	 * Gets the local representative.
	 *
	 * @return the localRepresentative
	 */
	public String getLocalRepresentative()
	{
		return localRepresentative;
	}

	/**
	 * Sets the local representative.
	 *
	 * @param localRepresentative
	 *           the localRepresentative to set
	 */
	public void setLocalRepresentative(final String localRepresentative)
	{
		this.localRepresentative = localRepresentative;
	}

	/**
	 * Gets the certificate holder.
	 *
	 * @return the certificate holder
	 */
	public String getCertificateHolder()
	{
		return certificateHolder;
	}

	/**
	 * Sets the certificate holder.
	 *
	 * @param certificateHolder
	 *           the new certificate holder
	 */
	public void setCertificateHolder(final String certificateHolder)
	{
		this.certificateHolder = certificateHolder;
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
	 * Gets the reference date.
	 *
	 * @return the reference date
	 */
	public String getReferenceDate()
	{
		return referenceDate;
	}


	/**
	 * Sets the reference date.
	 *
	 * @param referenceDate
	 *           the new reference date
	 */
	public void setReferenceDate(final String referenceDate)
	{
		this.referenceDate = referenceDate;
	}

}
