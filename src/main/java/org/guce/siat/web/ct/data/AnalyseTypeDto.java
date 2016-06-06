package org.guce.siat.web.ct.data;

import java.io.Serializable;

import org.guce.siat.common.model.AnalyseType;




/**
 * The Class FileTypeAutorityData.
 */
public class AnalyseTypeDto implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4147644501526408007L;

	/** The Analyse type. */
	private AnalyseType analyseType;

	/** The checked. */
	private Boolean checked;

	/**
	 * Instantiates a new file type autority data.
	 */
	public AnalyseTypeDto()
	{
	}

	/**
	 * Instantiates a new analyse type dto.
	 *
	 * @param analyseType
	 *           the analyse type
	 * @param checked
	 *           the checked
	 */
	public AnalyseTypeDto(final AnalyseType analyseType, final boolean checked)
	{
		this.analyseType = analyseType;
		this.checked = checked;
	}

	/**
	 * Gets the analyse type.
	 *
	 * @return the analyse type
	 */
	public AnalyseType getAnalyseType()
	{
		return analyseType;
	}

	/**
	 * Sets the analyse type.
	 *
	 * @param analyseType
	 *           the new analyse type
	 */
	public void setAnalyseType(final AnalyseType analyseType)
	{
		this.analyseType = analyseType;
	}

	/**
	 * Gets the checked.
	 *
	 * @return the checked
	 */
	public Boolean getChecked()
	{
		return checked;
	}

	/**
	 * Sets the checked.
	 *
	 * @param checked
	 *           the new checked
	 */
	public void setChecked(final Boolean checked)
	{
		this.checked = checked;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		{
			final StringBuilder builder = new StringBuilder();
			builder.append("FileTypeAutorityData [authority=");
			builder.append(analyseType.getLabelFr());
			builder.append(", checked=");
			builder.append(checked);
			builder.append("]");
			return builder.toString();
		}
	}
}
