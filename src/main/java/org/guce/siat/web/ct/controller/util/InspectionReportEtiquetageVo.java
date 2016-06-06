package org.guce.siat.web.ct.controller.util;

import java.io.Serializable;

import org.guce.siat.common.model.FileItem;


/**
 * The Class InspectionReportEtiquetageVo.
 */
public class InspectionReportEtiquetageVo implements Serializable
{



	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7368319592706607457L;

	/** The label. */
	private String label;

	/** The standard compliance. */
	private Boolean standardCompliance;

	/** The standard number. */
	private Integer standardNumber;

	private FileItem fileItem;

	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label
	 *           the label to set
	 */
	public void setLabel(final String label)
	{
		this.label = label;
	}

	/**
	 * @return the standardCompliance
	 */
	public Boolean getStandardCompliance()
	{
		return standardCompliance;
	}

	/**
	 * @param standardCompliance
	 *           the standardCompliance to set
	 */
	public void setStandardCompliance(final Boolean standardCompliance)
	{
		this.standardCompliance = standardCompliance;
	}

	/**
	 * @return the standardNumber
	 */
	public Integer getStandardNumber()
	{
		return standardNumber;
	}

	/**
	 * @param standardNumber
	 *           the standardNumber to set
	 */
	public void setStandardNumber(final Integer standardNumber)
	{
		this.standardNumber = standardNumber;
	}

	/**
	 * @return the fileItem
	 */
	public FileItem getFileItem()
	{
		return fileItem;
	}

	/**
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
		builder.append("InspectionReportEtiquetageVo [label=");
		builder.append(label);
		builder.append(", standardCompliance=");
		builder.append(standardCompliance);
		builder.append(", standardNumber=");
		builder.append(standardNumber);
		builder.append("]");
		return builder.toString();
	}


}
