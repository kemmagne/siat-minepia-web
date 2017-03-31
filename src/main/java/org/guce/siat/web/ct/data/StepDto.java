package org.guce.siat.web.ct.data;

import java.io.Serializable;

import org.guce.siat.common.model.Step;




/**
 * The Class FileDto.
 */
public class StepDto implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4147644501526408007L;

	/** The step. */
	private Step step;

	/** The redefined label fr. */
	private String redefinedLabelFr;

	/** The redefined label en. */
	private String redefinedLabelEn;

	/**
	 * Instantiates a new file type autority data.
	 */
	public StepDto()
	{
		super();
	}

	/**
	 * Gets the step.
	 *
	 * @return the step
	 */
	public Step getStep()
	{
		return step;
	}

	/**
	 * Sets the step.
	 *
	 * @param step
	 *           the new step
	 */
	public void setStep(final Step step)
	{
		this.step = step;
	}

	/**
	 * Gets the redefined label fr.
	 *
	 * @return the redefined label fr
	 */
	public String getRedefinedLabelFr()
	{
		return redefinedLabelFr;
	}

	/**
	 * Sets the redefined label fr.
	 *
	 * @param redefinedLabelFr
	 *           the new redefined label fr
	 */
	public void setRedefinedLabelFr(final String redefinedLabelFr)
	{
		this.redefinedLabelFr = redefinedLabelFr;
	}

	/**
	 * Gets the redefined label en.
	 *
	 * @return the redefined label en
	 */
	public String getRedefinedLabelEn()
	{
		return redefinedLabelEn;
	}

	/**
	 * Sets the redefined label en.
	 *
	 * @param redefinedLabelEn
	 *           the new redefined label en
	 */
	public void setRedefinedLabelEn(final String redefinedLabelEn)
	{
		this.redefinedLabelEn = redefinedLabelEn;
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
		builder.append("StepDto [step=");
		builder.append(step);
		builder.append(", redefinedLabelFr=");
		builder.append(redefinedLabelFr);
		builder.append(", redefinedLabelEn=");
		builder.append(redefinedLabelEn);
		builder.append("]");
		return builder.toString();
	}
}
