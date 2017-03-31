package org.guce.siat.web.ct.controller.util;

import java.io.Serializable;

import org.guce.siat.common.model.FileItem;


/**
 * The Class InspectionReportTemperatureVo.
 */
public class InspectionReportTemperatureVo implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3352911665861037640L;

	/** The value. */
	private Integer value;

	/** The aspect. */
	private String aspect;

	/** The file item. */
	private FileItem fileItem;

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Integer getValue()
	{
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *           the value to set
	 */
	public void setValue(final Integer value)
	{
		this.value = value;
	}

	/**
	 * Gets the aspect.
	 *
	 * @return the aspect
	 */
	public String getAspect()
	{
		return aspect;
	}

	/**
	 * Sets the aspect.
	 *
	 * @param aspect
	 *           the aspect to set
	 */
	public void setAspect(final String aspect)
	{
		this.aspect = aspect;
	}

	/**
	 * Gets the file item.
	 *
	 * @return the fileItem
	 */
	public FileItem getFileItem()
	{
		return fileItem;
	}

	/**
	 * Sets the file item.
	 *
	 * @param fileItem
	 *           the fileItem to set
	 */
	public void setFileItem(final FileItem fileItem)
	{
		this.fileItem = fileItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("TeperatureVo [value=");
		builder.append(value);
		builder.append(", aspect=");
		builder.append(aspect);
		builder.append("]");
		return builder.toString();
	}


}
