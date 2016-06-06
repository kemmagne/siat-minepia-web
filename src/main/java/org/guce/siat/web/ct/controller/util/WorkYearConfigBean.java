package org.guce.siat.web.ct.controller.util;

import java.util.Date;

import org.guce.siat.common.model.HourlyType;


/**
 * The Class WorkYearConfigBean.
 */
public class WorkYearConfigBean
{

	/** The id. */
	private Long id;

	/** The begin date. */
	private Date beginDate;

	/** The end date. */
	private Date endDate;

	/** The hourly type. */
	private HourlyType hourlyType;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *           the id to set
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * Gets the begin date.
	 *
	 * @return the beginDate
	 */
	public Date getBeginDate()
	{
		return beginDate;
	}

	/**
	 * Sets the begin date.
	 *
	 * @param beginDate
	 *           the beginDate to set
	 */
	public void setBeginDate(final Date beginDate)
	{
		this.beginDate = beginDate;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the endDate
	 */
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate
	 *           the endDate to set
	 */
	public void setEndDate(final Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * Gets the hourly type.
	 *
	 * @return the hourlyType
	 */
	public HourlyType getHourlyType()
	{
		return hourlyType;
	}

	/**
	 * Sets the hourly type.
	 *
	 * @param hourlyType
	 *           the hourlyType to set
	 */
	public void setHourlyType(final HourlyType hourlyType)
	{
		this.hourlyType = hourlyType;
	}
}
