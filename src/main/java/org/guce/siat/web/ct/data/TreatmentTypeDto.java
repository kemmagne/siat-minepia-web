package org.guce.siat.web.ct.data;

import java.io.Serializable;

import org.guce.siat.core.ct.model.TreatmentType;



/**
 * The Class TreatmentTypeDto.
 */
public class TreatmentTypeDto implements Serializable
{
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2624402169742478525L;

	/** The treatment type. */
	private TreatmentType treatmentType;

	/** The checked. */
	private Boolean checked;

	/**
	 * Instantiates a new treatment type dto.
	 */
	public TreatmentTypeDto()
	{
	}

	/**
	 * Instantiates a new treatment type dto.
	 *
	 * @param treatmentType
	 *           the treatment type
	 * @param checked
	 *           the checked
	 */
	public TreatmentTypeDto(final TreatmentType treatmentType, final boolean checked)
	{
		this.treatmentType = treatmentType;
		this.checked = checked;
	}

	/**
	 * Gets the treatment type.
	 *
	 * @return the treatment type
	 */
	public TreatmentType getTreatmentType()
	{
		return treatmentType;
	}

	/**
	 * Sets the treatment type.
	 *
	 * @param treatmentType
	 *           the new treatment type
	 */
	public void setTreatmentType(final TreatmentType treatmentType)
	{
		this.treatmentType = treatmentType;
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
			builder.append("AnalyseType [authority=");
			builder.append(treatmentType.getLabelFr());
			builder.append(", checked=");
			builder.append(checked);
			builder.append("]");
			return builder.toString();
		}
	}
}
