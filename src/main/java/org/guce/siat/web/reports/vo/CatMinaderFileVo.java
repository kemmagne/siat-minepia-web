package org.guce.siat.web.reports.vo;

import java.util.Date;



/**
 * The Class CatMinaderFileVo.
 */
public class CatMinaderFileVo extends AbstractFileVo<CatMinaderFileItemVo>
{

	/** The treatment equipment. */
	private String treatmentEquipment;

	/** The importer. */
	private String importer;

	/** The session date. */
	Date sessionDate;

	/** The class device. */
	private String classDevice;

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
	 * Gets the treatment equipment.
	 *
	 * @return the treatment equipment
	 */
	public String getTreatmentEquipment()
	{
		return treatmentEquipment;
	}



	/**
	 * Sets the treatment equipment.
	 *
	 * @param treatmentEquipment
	 *           the new treatment equipment
	 */
	public void setTreatmentEquipment(final String treatmentEquipment)
	{
		this.treatmentEquipment = treatmentEquipment;
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
	 * Gets the class device.
	 *
	 * @return the class device
	 */
	public String getClassDevice()
	{
		return classDevice;
	}

	/**
	 * Sets the class device.
	 *
	 * @param classDevice
	 *           the new class device
	 */
	public void setClassDevice(final String classDevice)
	{
		this.classDevice = classDevice;
	}
}
