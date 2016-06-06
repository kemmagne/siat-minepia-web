package org.guce.siat.web.ct.controller.util.enums;

/**
 * The Enum ServiceItemType.
 */
public enum SearchCriteria
{

	/** The article. */
	ARTICLE("Article", "Product"),

	/** The importer. */
	OPERATEUR("Operateur", "Operator"),

	/** The transport type. */
	TRANSPORT_TYPE("Moyen de transport", "Conveyance"),

	/** The origin country. */
	ORIGIN_COUNTRY("Pays d'origin", "Origin Country"),

	/** The arrival port. */
	ARRIVAL_PORT("Port d'arrivée", "Arrival port"),

	/** The analyse type. */
	ANALYSE_TYPE("Type d'analyse", "Analyse type"),

	/** The controller. */
	CONTROLLER("Contrôleur", "Controller"),

	/** The document number. */
	DOCUMENT_NUMBER("Num dossier", "Document num"),

	/** The laboratory. */
	LABORATORY("Laboratoire", "Laboratory");



	/** The code. */
	private String codeFr;

	/** The code en. */
	private String codeEn;


	/**
	 * Instantiates a new service item type.
	 *
	 * @param codeFr
	 *           the code fr
	 * @param codeEn
	 *           the code en
	 */
	private SearchCriteria(final String codeFr, final String codeEn)
	{
		this.codeFr = codeFr;
		this.codeEn = codeEn;
	}


	/**
	 * Gets the code fr.
	 *
	 * @return the codeFr
	 */
	public String getCodeFr()
	{
		return codeFr;
	}


	/**
	 * Sets the code fr.
	 *
	 * @param codeFr
	 *           the codeFr to set
	 */
	public void setCodeFr(final String codeFr)
	{
		this.codeFr = codeFr;
	}


	/**
	 * Gets the code en.
	 *
	 * @return the codeEn
	 */
	public String getCodeEn()
	{
		return codeEn;
	}


	/**
	 * Sets the code en.
	 *
	 * @param codeEn
	 *           the codeEn to set
	 */
	public void setCodeEn(final String codeEn)
	{
		this.codeEn = codeEn;
	}



}
