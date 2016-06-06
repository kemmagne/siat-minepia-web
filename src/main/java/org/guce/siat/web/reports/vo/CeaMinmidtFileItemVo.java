package org.guce.siat.web.reports.vo;

/**
 * The Class CeaMinmidtFileItemVo.
 */
public class CeaMinmidtFileItemVo extends AbstractFileItemVo
{

	/** The weight. */
	private double weight;

	/** The gold content. */
	private String goldContent;

	/** The name. */
	private String name;


	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public double getWeight()
	{
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight
	 *           the new weight
	 */
	public void setWeight(final double weight)
	{
		this.weight = weight;
	}

	/**
	 * Gets the gold content.
	 *
	 * @return the gold content
	 */
	public String getGoldContent()
	{
		return goldContent;
	}

	/**
	 * Sets the gold content.
	 *
	 * @param goldContent
	 *           the new gold content
	 */
	public void setGoldContent(final String goldContent)
	{
		this.goldContent = goldContent;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *           the new name
	 */
	public void setName(final String name)
	{
		this.name = name;
	}


}
