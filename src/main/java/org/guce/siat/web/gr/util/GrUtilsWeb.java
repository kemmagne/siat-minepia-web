package org.guce.siat.web.gr.util;

import java.util.List;

import org.guce.siat.common.model.ParamsOrganism;
import org.guce.siat.core.gr.utils.SynthesisConfig;
import org.guce.siat.core.gr.utils.SynthesisResult;
import org.guce.siat.web.gr.util.enums.ParamsNames;




/**
 * The Class GrUtils.
 */
public final class GrUtilsWeb
{
	private GrUtilsWeb()
	{

	}

	/**
	 * Load synthesis config settings.
	 *
	 * @param listParamsOrganisms
	 *           the list params organisms
	 * @return the synthesis config
	 */
	public static SynthesisConfig loadSynthesisConfigSettings(final List<ParamsOrganism> listParamsOrganisms)
	{
		final SynthesisConfig config = new SynthesisConfig();

		for (final ParamsOrganism paramsOrganism : listParamsOrganisms)
		{
			if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.PRODUCT_KNOWN_PERIOD.getCode()))
			{
				config.setProductKnownPeriod(new Integer(paramsOrganism.getValue()));
			}
			else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.PRODUCT_KNOWN_THRESHOLD.getCode()))
			{
				config.setProductKnownThreshold(new Integer(paramsOrganism.getValue()));
			}
			else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.PRODUCT_TESTED_PERIOD.getCode()))
			{
				config.setProductTestedPeriod(new Integer(paramsOrganism.getValue()));
			}
			else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.NEGATIVE_DECISIONS_PERIOD.getCode()))
			{
				config.setNegativeDecisionsPeriod(new Integer(paramsOrganism.getValue()));
			}
			else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.QUANTITY_COEFFICIENT.getCode()))
			{
				config.setQuantityCoefficient(new Float(paramsOrganism.getValue()));
			}
			else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.IMPORTER_KNOWN_PERIOD.getCode()))
			{
				config.setImporterKnownPeriod(new Integer(paramsOrganism.getValue()));
			}
			else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.IMPORTER_KNOWN_THRESHOLD.getCode()))
			{
				config.setImporterKnownThreshold(new Integer(paramsOrganism.getValue()));
			}
			else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.RDD_DELAY.getCode()))
			{
				config.setRddDelay(new Integer(paramsOrganism.getValue()));
			}
			else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.CLEARANCE_DELAY.getCode()))
			{
				config.setClearanceDelay(new Integer(paramsOrganism.getValue()));
			}
			else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.MEC_DELAY.getCode()))
			{
				config.setMecDelay(new Integer(paramsOrganism.getValue()));
			}
		}
		return config;

	}



	/**
	 * Gets the scenario.
	 *
	 * @param param
	 *           the param
	 * @param result
	 *           the result
	 * @return the scenario
	 */
	public static ScenarioType getScenario(final SynthesisConfig param, final SynthesisResult result)
	{
		if (param == null)
		{
			throw new NullPointerException("param is null.");
		}

		if (result == null)
		{
			throw new NullPointerException("result is null.");
		}

		//vérifier dossier en instance
		if (result.getOutDatedRequests() > 0)
		{
			return ScenarioType.CONVOCATION;
		}

		//Importateur ciblé
		if (result.getImporterHasTarget())
		{
			return ScenarioType.SIA;
		}

		// Produit ciblé ?
		if (result.getProductHasTarget())
		{
			return ScenarioType.RDD;
		}

		//connu?
		else if (!(result.getProductIsKnown()))
		{
			return ScenarioType.SIA;
		}

		//Origine douteuse
		if (result.getProductHasSuspiciousOrigins())
		{
			return ScenarioType.SIA;
		}

		// Bon fournisseur ?
		if (!result.getExporterReputation())
		{
			return ScenarioType.SIA;
		}

		// Quantité ?
		if (result.getQuantityIsSmall())
		{
			return ScenarioType.CCT;
		}

		return ScenarioType.AD;
	}
}
