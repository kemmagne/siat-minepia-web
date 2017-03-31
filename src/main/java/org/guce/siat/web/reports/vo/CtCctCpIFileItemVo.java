package org.guce.siat.web.reports.vo;




/**
 * The Class CtCctCpIFileItemVo.
 */
public class CtCctCpIFileItemVo extends AbstractFileItemVo
{

	/** The distinguishing mark. */
	private String distinguishingMark;

	/** The number of packages. */
	private String numberOfPackages;

	/** The nature. */
	private String nature;

	/** The scientific name. */
	private String botanicalName;

	/** The declared quantity. */
	private String declaredQuantity;

	/**
	 * Gets the distinguishing mark.
	 *
	 * @return the distinguishing mark
	 */
	public String getDistinguishingMark()
	{
		return distinguishingMark;
	}

	/**
	 * Sets the distinguishing mark.
	 *
	 * @param distinguishingMark
	 *           the new distinguishing mark
	 */
	public void setDistinguishingMark(final String distinguishingMark)
	{
		this.distinguishingMark = distinguishingMark;
	}

	/**
	 * Gets the number of packages.
	 *
	 * @return the number of packages
	 */
	public String getNumberOfPackages()
	{
		return numberOfPackages;
	}

	/**
	 * Sets the number of packages.
	 *
	 * @param numberOfPackages
	 *           the new number of packages
	 */
	public void setNumberOfPackages(final String numberOfPackages)
	{
		this.numberOfPackages = numberOfPackages;
	}

	/**
	 * Gets the nature.
	 *
	 * @return the nature
	 */
	public String getNature()
	{
		return nature;
	}

	/**
	 * Sets the nature.
	 *
	 * @param nature
	 *           the new nature
	 */
	public void setNature(final String nature)
	{
		this.nature = nature;
	}

	/**
	 * Gets the botanical name.
	 *
	 * @return the botanical name
	 */
	public String getBotanicalName()
	{
		return botanicalName;
	}

	/**
	 * Sets the botanical name.
	 *
	 * @param botanicalName
	 *           the new botanical name
	 */
	public void setBotanicalName(final String botanicalName)
	{
		this.botanicalName = botanicalName;
	}

	/**
	 * Gets the declared quantity.
	 *
	 * @return the declared quantity
	 */
	public String getDeclaredQuantity()
	{
		return declaredQuantity;
	}

	/**
	 * Sets the declared quantity.
	 *
	 * @param declaredQuantity
	 *           the new declared quantity
	 */
	public void setDeclaredQuantity(final String declaredQuantity)
	{
		this.declaredQuantity = declaredQuantity;
	}
}
