package org.guce.siat.web.reports.vo;

import java.util.Date;



/**
 * The Class DiMinaderSFileVo.
 */
public class DiMinaderSFileVo extends AbstractFileVo<DiMinaderSFileItemVo>
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

	/** The session date. */
	private Date sessionDate;

	/** The transport mode. */
	private String transportMode;

	/** The reference. */
	private String reference;

	/** The validity date. */
	private Date validityDate;

	/** The approval number. */
	private String approvalNumber;

	/** The approval holder. */
	private String approvalHolder;

	/** The declarant address. */
	private String declarantAddress;

	/** The declarant name. */
	private String declarantName;

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
	 * Gets the session date.
	 *
	 * @return the session date
	 */
	public Date getSessionDate()
	{
		return sessionDate;
	}

	/**
	 * Sets the session date.
	 *
	 * @param sessionDate
	 *           the new session date
	 */
	public void setSessionDate(final Date sessionDate)
	{
		this.sessionDate = sessionDate;
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
	 * Gets the approval number.
	 *
	 * @return the approval number
	 */
	public String getApprovalNumber()
	{
		return approvalNumber;
	}


	/**
	 * Sets the approval number.
	 *
	 * @param approvalNumber
	 *           the new approval number
	 */
	public void setApprovalNumber(final String approvalNumber)
	{
		this.approvalNumber = approvalNumber;
	}


	/**
	 * Gets the approval holder.
	 *
	 * @return the approval holder
	 */
	public String getApprovalHolder()
	{
		return approvalHolder;
	}


	/**
	 * Sets the approval holder.
	 *
	 * @param approvalHolder
	 *           the new approval holder
	 */
	public void setApprovalHolder(final String approvalHolder)
	{
		this.approvalHolder = approvalHolder;
	}


	/**
	 * Gets the declarant address.
	 *
	 * @return the declarant address
	 */
	public String getDeclarantAddress()
	{
		return declarantAddress;
	}


	/**
	 * Sets the declarant address.
	 *
	 * @param declarantAddress
	 *           the new declarant address
	 */
	public void setDeclarantAddress(final String declarantAddress)
	{
		this.declarantAddress = declarantAddress;
	}


	/**
	 * Gets the declarant name.
	 *
	 * @return the declarant name
	 */
	public String getDeclarantName()
	{
		return declarantName;
	}


	/**
	 * Sets the declarant name.
	 *
	 * @param declarantName
	 *           the new declarant name
	 */
	public void setDeclarantName(final String declarantName)
	{
		this.declarantName = declarantName;
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
}
