package org.guce.siat.web.gr.util.enums;

/**
 * The Enum CategoryNames.
 */
public enum CategoryNames
{

	/** The produit connu. */
	PRODUIT_CONNU("ProduitConnu"),

	/** The authentification attempts. */
	AUTHENTIFICATION_ATTEMPTS("AuthentificationAttempts"),

	/** The analyse au laboratoire. */
	ANALYSE_AU_LABORATOIRE("AnalyseAuLaboratoire"),

	/** The decisions negatives. */
	DECISIONS_NEGATIVES("DecisionsNegatives"),

	/** The quantite. */
	QUANTITE("Quantite"),

	/** The importateur connu. */
	IMPORTATEUR_CONNU("ImportateurConnu"),

	/** The delai de convocation. */
	DELAI_DE_CONVOCATION("DelaiDeConvocation"),

	/** The rdd refoulement. */
	RDD_REFOULEMENT("RddRefoulement"),

	/** The generale. */
	GENERALE("Generale"),

	/** The generale. */
	CANCEL_REQUEST("cancelRequest");

	/** The code. */
	private final String code;


	/**
	 * Instantiates a new category name.
	 *
	 * @param code
	 *           the code
	 */
	private CategoryNames(final String code)
	{
		this.code = code.intern();
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode()
	{
		return this.code;
	}

}
