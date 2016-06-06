package org.guce.siat.web.reports.vo;



/**
 * The Class AieMinaderFileVo.
 */
public class AieMinaderFileVo extends AbstractFileVo<AieMinaderFileItemVo>
{

	/** The decision. */
	String decision;

	/** The importer. */
	String importer;

	/**
	 * Gets the decision.
	 *
	 * @return the decision
	 */
	public String getDecision()
	{
		return decision;
	}

	/**
	 * Sets the decision.
	 *
	 * @param decision
	 *           the new decision
	 */
	public void setDecision(final String decision)
	{
		this.decision = decision;
	}

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
	 * Sets the importer.
	 *
	 * @param importer
	 *           the new importer
	 */
	public void setImporter(final String importer)
	{
		this.importer = importer;
	}


}
