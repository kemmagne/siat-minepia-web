package org.guce.siat.web.gr.util.enums;


/**
 * The Enum ParamsNames.
 */
public enum ParamsNames
{

	/** The product known period. */
	PRODUCT_KNOWN_PERIOD("ProductKnownPeriod"),

	/** The product known threshold. */
	PRODUCT_KNOWN_THRESHOLD("ProductKnownThreshold"),

	/** The negative decisions period. */
	NEGATIVE_DECISIONS_PERIOD("NegativeDecisionsPeriod"),

	/** The product tested period. */
	PRODUCT_TESTED_PERIOD("ProductTestedPeriod"),

	/** The importer known period. */
	IMPORTER_KNOWN_PERIOD("ImporterKnownPeriod"),

	/** The importer known threshold. */
	IMPORTER_KNOWN_THRESHOLD("ImporterKnownThreshold"),

	/** The quantity coefficient. */
	QUANTITY_COEFFICIENT("QuantityCoefficient"),

	/** The rdd delay. */
	RDD_DELAY("RddDelay"),

	/** The mecdelay. */
	MEC_DELAY("MecDelay"),

	/** The nb rdd. */
	NB_RDD("NbRDD"),

	/** The clearance delay. */
	CLEARANCE_DELAY("ClearanceDelay"),

	/** The max attempts user connexion. */
	MAX_ATTEMPTS_USER_CONNEXION("MaxAttemptsUserConnexion"),

	/** The token authentification. */
	TOKEN_AUTHENTIFICATION("TokenAuthentification"),

	/** The max cancel request. */
	MAX_CANCEL_REQUEST("MaxCancelRequest");

	/** The code. */
	private final String code;


	/**
	 * Instantiates a new parametre.
	 *
	 * @param code
	 *           the code
	 */
	private ParamsNames(final String code)
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
