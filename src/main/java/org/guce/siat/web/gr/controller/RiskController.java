package org.guce.siat.web.gr.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Entity;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.ParamsOrganism;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.service.ItemFlowService;
import org.guce.siat.common.service.ParamsOrganismService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.ParamsCategory;
import org.guce.siat.common.utils.enums.StepCode;
import org.guce.siat.core.gr.model.Alert;
import org.guce.siat.core.gr.model.Ciblage;
import org.guce.siat.core.gr.service.AlertService;
import org.guce.siat.core.gr.service.CiblageService;
import org.guce.siat.core.gr.service.RiskService;
import org.guce.siat.core.gr.utils.SynthesisConfig;
import org.guce.siat.core.gr.utils.SynthesisResult;
import org.guce.siat.web.gr.util.GrUtilsWeb;
import org.guce.siat.web.gr.util.enums.ParamsNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class RiskController.
 */
@ManagedBean(name = "riskController")
@SessionScoped
public class RiskController implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2105185674256662911L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(RiskController.class);

	/** The params organism service. */
	@ManagedProperty(value = "#{paramsOrganismService}")
	private ParamsOrganismService paramsOrganismService;

	/** The ciblage service. */
	@ManagedProperty(value = "#{ciblageService}")
	private CiblageService ciblageService;

	/** The alert service. */
	@ManagedProperty(value = "#{alertService}")
	private AlertService alertService;

	/** The item flow service. */
	@ManagedProperty(value = "#{itemFlowService}")
	private ItemFlowService itemFlowService;

	/** The risk service. */
	@ManagedProperty(value = "#{riskService}")
	private RiskService riskService;

	/** The synthese active tab. */
	private Integer syntheseActiveTab;

	/** The synthese params active tab. */
	private Integer syntheseParamsActiveTab;

	/** The Constant SERVICE_ALIMENTAIRE. */
	public static final String SERVICE_ALIMENTAIRE = "Alimentaire";
	/** The list params organisms. */
	private List<ParamsOrganism> listParamsOrganisms;

	/** The ciblage tab list. */
	private List<Ciblage> ciblageTabList;

	/** The alert tab list. */
	private List<Alert> alertTabList;

	/** The imp decisions negatives tab list. */
	private List<FileItem> impDecisionsNegativesTabList;

	/** The product decisions negatives tab list. */
	private List<FileItem> productDecisionsNegativesTabList;

	/** The document info list. */
	private List<org.guce.siat.core.gr.utils.DocumentInfo> documentInfoList;

	/** The current organism. */
	private Organism currentOrganism;

	/** The current organism. */
	private Service currentService;

	/** The file items list. */
	private List<FileItem> fileItemsList;

	/** The synthesis result. */
	private SynthesisResult synthesisResult;

	/** The decision system. */
	private String decisionSystem;

	/** The synthesis config. */
	private SynthesisConfig synthesisConfig;

	/** The checked file item. */
	private FileItem checkedFileItem;


	/**
	 * Instantiates a new system decision controller.
	 */
	public RiskController()
	{
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, RiskController.class.getName());
		}
		refreshParams();
	}


	/**
	 * Gets the current organism.
	 *
	 * @return the current organism
	 */
	public Organism getCurrentOrganism()
	{

		final Administration administration = getCurrentAdministration();
		if (administration instanceof Entity)
		{
			currentOrganism = ((Entity) administration).getService().getSubDepartment().getOrganism();
		}
		else if (administration instanceof Service)
		{
			currentOrganism = ((Service) administration).getSubDepartment().getOrganism();
		}
		else if (administration instanceof SubDepartment)
		{
			currentOrganism = ((SubDepartment) administration).getOrganism();
		}
		else if (administration instanceof Organism)
		{
			currentOrganism = (Organism) administration;
		}
		return currentOrganism;

	}


	/**
	 * Prepare show.
	 *
	 * @param state
	 *           the state
	 * @param fileItem
	 *           the file item
	 */
	public void prepareShow(final String state, final FileItem fileItem)
	{
		generateSystemDecisionAndSynthesisResults(fileItem);

		if ("synthesisTab".equals(state))
		{
			setSyntheseActiveTab(Constants.FIVE);
		}
		else
		{
			setSyntheseActiveTab(Constants.ZERO);
		}

		alertTabList = alertService.findAlertForRiskManagment(fileItem);
		ciblageTabList = ciblageService.findCiblageforRiskManagment(fileItem);

		final List<StepCode> negativeDecisionsList = populateNegativeDecisionsList();
		impDecisionsNegativesTabList = itemFlowService.findFileItemsHistoryByNegativeDecisionsAndCompany(fileItem,
				negativeDecisionsList);
		productDecisionsNegativesTabList = itemFlowService.findFileItemsHistoryByNegativeDecisionsAndProduct(fileItem,
				negativeDecisionsList);

		documentInfoList = riskService.getPendingRequests(fileItem.getFile().getClient().getId(), synthesisConfig);

	}

	/**
	 * Generate system decision and synthesis results.
	 *
	 * @param fileItem
	 *           the file item
	 */
	public void generateSystemDecisionAndSynthesisResults(final FileItem fileItem)
	{
		checkedFileItem = fileItem;
		synthesisConfig = loadSynthesisConfigSettings(getListParamsOrganisms());
		synthesisResult = riskService.getSynthesis(synthesisConfig, fileItem);

		decisionSystem = GrUtilsWeb.getScenario(synthesisConfig, synthesisResult).getDescription();
	}


	/**
	 * Populate negative decisions list.
	 *
	 * @return the list
	 */
	private List<StepCode> populateNegativeDecisionsList()
	{
		final List<StepCode> stepCodes = new ArrayList<StepCode>();

		//rejet
		stepCodes.add(StepCode.ST_CT_05);

		//régularisation
		stepCodes.add(StepCode.ST_CT_09);

		//RDD
		stepCodes.add(StepCode.ST_CT_25);

		//Fin RDD
		stepCodes.add(StepCode.ST_CT_26);

		return stepCodes;
	}

	/**
	 * Synthesis config settings.
	 *
	 * @param listParamsOrganisms
	 *           the list params organisms
	 * @return the synthesis config
	 */
	public SynthesisConfig loadSynthesisConfigSettings(final List<ParamsOrganism> listParamsOrganisms)
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
	 * Refresh params.
	 */
	public void refreshParams()
	{
		if (listParamsOrganisms == null)
		{
			listParamsOrganisms = paramsOrganismService.findParamsOrganismByOrganism(getCurrentOrganism(), ParamsCategory.GR);
		}
		synthesisConfig = loadSynthesisConfigSettings(listParamsOrganisms);

	}

	/**
	 * On synthesis params tab change.
	 */
	public void onSynthesisParamsTabChange()
	{
		refreshParams();
		synthesisConfig = loadSynthesisConfigSettings(listParamsOrganisms);
		synthesisResult = riskService.getSynthesis(synthesisConfig, checkedFileItem);
	}

	/**
	 * Gets the current service.
	 *
	 * @return the current service
	 */
	public Service getCurrentService()
	{
		final Administration administration = getCurrentAdministration();
		if (administration instanceof Service)
		{
			currentService = (Service) administration;
		}
		else if (administration instanceof Entity)
		{
			final Entity entity = (Entity) administration;
			currentService = entity.getService();
		}
		return currentService;
	}

	/**
	 * Gets the current administration.
	 *
	 * @return the current administration
	 */
	public Administration getCurrentAdministration()
	{
		Administration currentAdministration = null;
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				currentAdministration = (Administration) session.getAttribute("currentAdministration");
			}
		}
		return currentAdministration;
	}

	/**
	 * Gets the synthese active tab.
	 *
	 * @return the syntheseActiveTab
	 */
	public Integer getSyntheseActiveTab()
	{
		return syntheseActiveTab;
	}

	/**
	 * Sets the synthese active tab.
	 *
	 * @param syntheseActiveTab
	 *           the syntheseActiveTab to set
	 */
	public void setSyntheseActiveTab(final Integer syntheseActiveTab)
	{
		this.syntheseActiveTab = syntheseActiveTab;
	}

	/**
	 * Gets the synthese params active tab.
	 *
	 * @return the syntheseParamsActiveTab
	 */
	public Integer getSyntheseParamsActiveTab()
	{
		return syntheseParamsActiveTab;
	}

	/**
	 * Sets the synthese params active tab.
	 *
	 * @param syntheseParamsActiveTab
	 *           the syntheseParamsActiveTab to set
	 */
	public void setSyntheseParamsActiveTab(final Integer syntheseParamsActiveTab)
	{
		this.syntheseParamsActiveTab = syntheseParamsActiveTab;
	}


	/**
	 * Gets the params organism service.
	 *
	 * @return the paramsOrganismService
	 */
	public ParamsOrganismService getParamsOrganismService()
	{
		return paramsOrganismService;
	}

	/**
	 * Sets the params organism service.
	 *
	 * @param paramsOrganismService
	 *           the paramsOrganismService to set
	 */
	public void setParamsOrganismService(final ParamsOrganismService paramsOrganismService)
	{
		this.paramsOrganismService = paramsOrganismService;
	}

	/**
	 * Gets the list params organisms.
	 *
	 * @return the listParamsOrganisms
	 */
	public List<ParamsOrganism> getListParamsOrganisms()
	{
		return listParamsOrganisms;
	}

	/**
	 * Sets the list params organisms.
	 *
	 * @param listParamsOrganisms
	 *           the listParamsOrganisms to set
	 */
	public void setListParamsOrganisms(final List<ParamsOrganism> listParamsOrganisms)
	{
		this.listParamsOrganisms = listParamsOrganisms;
	}


	/**
	 * Gets the ciblage tab list.
	 *
	 * @return the ciblageTabList
	 */
	public List<Ciblage> getCiblageTabList()
	{
		return ciblageTabList;
	}

	/**
	 * Sets the ciblage tab list.
	 *
	 * @param ciblageTabList
	 *           the ciblageTabList to set
	 */
	public void setCiblageTabList(final List<Ciblage> ciblageTabList)
	{
		this.ciblageTabList = ciblageTabList;
	}

	/**
	 * Gets the ciblage service.
	 *
	 * @return the ciblageService
	 */
	public CiblageService getCiblageService()
	{
		return ciblageService;
	}

	/**
	 * Sets the ciblage service.
	 *
	 * @param ciblageService
	 *           the ciblageService to set
	 */
	public void setCiblageService(final CiblageService ciblageService)
	{
		this.ciblageService = ciblageService;
	}

	/**
	 * Gets the alert service.
	 *
	 * @return the alertService
	 */
	public AlertService getAlertService()
	{
		return alertService;
	}

	/**
	 * Sets the alert service.
	 *
	 * @param alertService
	 *           the alertService to set
	 */
	public void setAlertService(final AlertService alertService)
	{
		this.alertService = alertService;
	}

	/**
	 * Gets the alert tab list.
	 *
	 * @return the alertTabList
	 */
	public List<Alert> getAlertTabList()
	{
		return alertTabList;
	}

	/**
	 * Sets the alert tab list.
	 *
	 * @param alertTabList
	 *           the alertTabList to set
	 */
	public void setAlertTabList(final List<Alert> alertTabList)
	{
		this.alertTabList = alertTabList;
	}

	/**
	 * Gets the file items list.
	 *
	 * @return the fileItemsList
	 */
	public List<FileItem> getFileItemsList()
	{
		return fileItemsList;
	}

	/**
	 * Sets the file items list.
	 *
	 * @param fileItemsList
	 *           the fileItemsList to set
	 */
	public void setFileItemsList(final List<FileItem> fileItemsList)
	{
		this.fileItemsList = fileItemsList;
	}

	/**
	 * Gets the document info list.
	 *
	 * @return the documentInfoList
	 */
	public List<org.guce.siat.core.gr.utils.DocumentInfo> getDocumentInfoList()
	{
		return documentInfoList;
	}

	/**
	 * Sets the document info list.
	 *
	 * @param documentInfoList
	 *           the documentInfoList to set
	 */
	public void setDocumentInfoList(final List<org.guce.siat.core.gr.utils.DocumentInfo> documentInfoList)
	{
		this.documentInfoList = documentInfoList;
	}

	/**
	 * Gets the risk service.
	 *
	 * @return the riskService
	 */
	public RiskService getRiskService()
	{
		return riskService;
	}

	/**
	 * Sets the risk service.
	 *
	 * @param riskService
	 *           the riskService to set
	 */
	public void setRiskService(final RiskService riskService)
	{
		this.riskService = riskService;
	}

	/**
	 * Gets the synthesis result.
	 *
	 * @return the synthesisResult
	 */
	public SynthesisResult getSynthesisResult()
	{
		return synthesisResult;
	}

	/**
	 * Sets the synthesis result.
	 *
	 * @param synthesisResult
	 *           the synthesisResult to set
	 */
	public void setSynthesisResult(final SynthesisResult synthesisResult)
	{
		this.synthesisResult = synthesisResult;
	}

	/**
	 * Gets the decision system.
	 *
	 * @return the decisionSystem
	 */
	public String getDecisionSystem()
	{
		return decisionSystem;
	}

	/**
	 * Sets the decision system.
	 *
	 * @param decisionSystem
	 *           the decisionSystem to set
	 */
	public void setDecisionSystem(final String decisionSystem)
	{
		this.decisionSystem = decisionSystem;
	}

	/**
	 * Gets the synthesis config.
	 *
	 * @return the synthesisConfig
	 */
	public SynthesisConfig getSynthesisConfig()
	{
		return synthesisConfig;
	}

	/**
	 * Sets the synthesis config.
	 *
	 * @param synthesisConfig
	 *           the synthesisConfig to set
	 */
	public void setSynthesisConfig(final SynthesisConfig synthesisConfig)
	{
		this.synthesisConfig = synthesisConfig;
	}

	/**
	 * Gets the checked file item.
	 *
	 * @return the checkedFileItem
	 */
	public FileItem getCheckedFileItem()
	{
		return checkedFileItem;
	}

	/**
	 * Sets the checked file item.
	 *
	 * @param checkedFileItem
	 *           the checkedFileItem to set
	 */
	public void setCheckedFileItem(final FileItem checkedFileItem)
	{
		this.checkedFileItem = checkedFileItem;
	}

	/**
	 * Gets the service alimentaire.
	 *
	 * @return the serviceAlimentaire
	 */
	public static String getServiceAlimentaire()
	{
		return SERVICE_ALIMENTAIRE;
	}

	/**
	 * Sets the current organism.
	 *
	 * @param currentOrganism
	 *           the new current organism
	 */
	public void setCurrentOrganism(final Organism currentOrganism)
	{
		this.currentOrganism = currentOrganism;
	}

	/**
	 * Sets the current service.
	 *
	 * @param currentService
	 *           the currentService to set
	 */
	public void setCurrentService(final Service currentService)
	{
		this.currentService = currentService;
	}

	/**
	 * Gets the item flow service.
	 *
	 * @return the item flow service
	 */
	public ItemFlowService getItemFlowService()
	{
		return itemFlowService;
	}

	/**
	 * Sets the item flow service.
	 *
	 * @param itemFlowService
	 *           the new item flow service
	 */
	public void setItemFlowService(final ItemFlowService itemFlowService)
	{
		this.itemFlowService = itemFlowService;
	}

	/**
	 * Gets the imp decisions negatives tab list.
	 *
	 * @return the imp decisions negatives tab list
	 */
	public List<FileItem> getImpDecisionsNegativesTabList()
	{
		return impDecisionsNegativesTabList;
	}

	/**
	 * Sets the imp decisions negatives tab list.
	 *
	 * @param impDecisionsNegativesTabList
	 *           the new imp decisions negatives tab list
	 */
	public void setImpDecisionsNegativesTabList(final List<FileItem> impDecisionsNegativesTabList)
	{
		this.impDecisionsNegativesTabList = impDecisionsNegativesTabList;
	}

	/**
	 * Gets the product decisions negatives tab list.
	 *
	 * @return the product decisions negatives tab list
	 */
	public List<FileItem> getProductDecisionsNegativesTabList()
	{
		return productDecisionsNegativesTabList;
	}

	/**
	 * Sets the product decisions negatives tab list.
	 *
	 * @param productDecisionsNegativesTabList
	 *           the new product decisions negatives tab list
	 */
	public void setProductDecisionsNegativesTabList(final List<FileItem> productDecisionsNegativesTabList)
	{
		this.productDecisionsNegativesTabList = productDecisionsNegativesTabList;
	}
}
