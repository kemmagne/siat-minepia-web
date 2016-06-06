package org.guce.siat.web.ct.controller.util.enums;

/**
 * The Enum DecisionsSuiteVisite.
 */
public enum DecisionsSuiteVisite
{
	/** The remise. */
	REMISE("La remise immédiate à l'importateur", "Immediate delivery to importer"),

	/** The delivrance. */
	DELIVRANCE("La délivrance à l'importateur après traitement", "Issuance to importer after treatment"),

	/** The prelevement. */
	PRELEVEMENT("Le prélèvement d'échantillons pour examen", "Sampling for examination"),

	/** The destriction. */
	DESTRICTION("Destruction au frais de l'importateur", "Destruction at the importer's expense"),

	/** The quarantaine. */
	QUARANTAINE("Culture quarantaine", "Culture quarantine"),

	/** The refoulement. */
	REFOULEMENT("Le refoulement vers le pays d'origine au frais de l'importateur", ""),

	/** The autres. */
	AUTRES("Autres Décision", "Refoulement to the country of origin at the expense of the importer");

	/** The label fr. */
	private String labelFr;

	/** The label en. */
	private String labelEn;

	/**
	 * Instantiates a new decisions suite visite.
	 *
	 * @param labelFr
	 *           the label fr
	 * @param labelEn
	 *           the label en
	 */
	private DecisionsSuiteVisite(final String labelFr, final String labelEn)
	{
		this.labelFr = labelFr;
		this.labelEn = labelEn;
	}

	/**
	 * Gets the label fr.
	 *
	 * @return the labelFr
	 */
	public String getLabelFr()
	{
		return labelFr;
	}

	/**
	 * Sets the label fr.
	 *
	 * @param labelFr
	 *           the labelFr to set
	 */
	public void setLabelFr(final String labelFr)
	{
		this.labelFr = labelFr;
	}

	/**
	 * Gets the label en.
	 *
	 * @return the labelEn
	 */
	public String getLabelEn()
	{
		return labelEn;
	}

	/**
	 * Sets the label en.
	 *
	 * @param labelEn
	 *           the labelEn to set
	 */
	public void setLabelEn(final String labelEn)
	{
		this.labelEn = labelEn;
	}

}
