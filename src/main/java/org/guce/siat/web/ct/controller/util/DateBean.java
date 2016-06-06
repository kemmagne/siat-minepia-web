package org.guce.siat.web.ct.controller.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.StringUtils;


/**
 * The Class DateBean.
 */
@ManagedBean(name = "dateBean")
@SessionScoped
public class DateBean implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5686925273488944918L;

	/** The Constant PATTERN_DD_MM_YYYY_HH_MM_SS. */
	private static final String PATTERN_DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";

	/** The Constant PATTERN_DD_MM_YYYY. */
	private static final String PATTERN_DD_MM_YYYY = "dd/MM/yyyy";

	/**
	 * Format date.
	 *
	 * @param date
	 *           the date
	 * @return the string
	 */
	public String formatDate(final Date date)
	{
		final SimpleDateFormat simpleFormat = new SimpleDateFormat(PATTERN_DD_MM_YYYY_HH_MM_SS);
		return simpleFormat.format(date);
	}

	/**
	 * Format simple date.
	 *
	 * @param date
	 *           the date
	 * @return the string
	 */
	public String formatSimpleDate(final Date date)
	{
		if (date != null)
		{
			final SimpleDateFormat simpleFormat = new SimpleDateFormat(PATTERN_DD_MM_YYYY);
			return simpleFormat.format(date);
		}
		else
		{
			return null;
		}

	}


	/**
	 * Checks if is valid string.
	 *
	 * @param value
	 *           the value
	 * @return true, if is valid string
	 */
	public boolean isValidString(final String value)
	{
		return value != null && value.trim().length() > 0;
	}

	/**
	 * Gets the current date.
	 *
	 * @return the current date
	 */
	public Date getCurrentDate()
	{
		return new Date();
	}

	/**
	 * Gets the string date.
	 *
	 * @param date
	 *           the date
	 * @return the string date
	 */
	public String getStringDate(final Date date)
	{
		if (date != null)
		{
			return formatSimpleDate(date);
		}
		else
		{
			return "";
		}
	}

	/**
	 * Gets the string time.
	 *
	 * @param date
	 *           the date
	 * @return the string time
	 */
	public String getStringTime(final Timestamp date)
	{
		if (date != null)
		{
			return formatSimpleDate(date);
		}
		else
		{
			return "";
		}
	}

	/**
	 * Gets the string date without time.
	 *
	 * @param date
	 *           the date
	 * @return the string date without time
	 */
	public String getStringDateWithoutTime(final Date date)
	{
		if (date != null)
		{
			return formatSimpleDate(date);
		}
		else
		{
			return "";
		}
	}

	/**
	 * Convert string to date.
	 *
	 * @param dateString
	 *           the date string
	 * @return the date
	 */
	public Date convertStringToDate(final String dateString)
	{
		final SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DD_MM_YYYY_HH_MM_SS);
		Date convertedDate = null;
		try
		{
			if (StringUtils.isNoneBlank(dateString))
			{
				convertedDate = formatter.parse(dateString);
			}
		}
		catch (final ParseException e)
		{
			return null;
		}

		return convertedDate;
	}

}
