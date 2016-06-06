/**
 *
 */
package org.guce.siat.web.gr.util;

/**
 * The Enum ScenarioType.
 */
public enum ScenarioType
{

	/** The sia. */
	SIA("Scénario d'inspection et analyse"),

	/** The convocation. */
	CONVOCATION("Convocation pour étude des dossiers en instance avec dépassement de délais"),

	/** The rdd. */
	RDD("Destruction/Refoulement"),

	/** The cct. */
	CCT("Octroi de certificat de contrôle technique"),

	/** The ad. */
	AD("Autre décision");

	/** The description. */
	private final String description;

	/**
	 * Instantiates a new scenario type.
	 *
	 * @param description
	 *           the description
	 */
	private ScenarioType(final String description)
	{
		this.description = description;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
}
