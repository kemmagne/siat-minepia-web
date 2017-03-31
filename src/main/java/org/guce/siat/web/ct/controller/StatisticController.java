package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.Company;
import org.guce.siat.common.model.Country;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.Flow;
import org.guce.siat.common.model.Item;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.ServicesItem;
import org.guce.siat.common.service.CompanyService;
import org.guce.siat.common.service.CountryService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FlowService;
import org.guce.siat.common.service.ItemService;
import org.guce.siat.common.utils.ChartUtils;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.FinalDecisionType;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.core.ct.filter.AnalyseFilter;
import org.guce.siat.core.ct.filter.Filter;
import org.guce.siat.core.ct.filter.HistoricClientFilter;
import org.guce.siat.core.ct.filter.InspectionDestribFilter;
import org.guce.siat.core.ct.filter.SampleFilter;
import org.guce.siat.core.ct.filter.StatisticBusinessFilter;
import org.guce.siat.core.ct.model.AnalyseOrder;
import org.guce.siat.core.ct.model.Laboratory;
import org.guce.siat.core.ct.service.CommonService;
import org.guce.siat.core.ct.service.LaboratoryService;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.data.ServiceItemDto;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;


/**
 * The Class StatisticController.
 */
@ManagedBean(name = "statisticController")
@SessionScoped
public class StatisticController extends AbstractController<FileItem>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1914289769771096824L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(StatisticController.class);

	/** The Constant LOCAL_BUNDLE_NAME. */
	private static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";

	/** The Constant DATE_VALIDATION_ERROR_MESSAGE. */
	private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

	/** The common service. */
	@ManagedProperty(value = "#{commonService}")
	private CommonService commonService;

	/** The laboratory service. */
	@ManagedProperty(value = "#{laboratoryService}")
	private LaboratoryService laboratoryService;

	/** The company service. */
	@ManagedProperty(value = "#{companyService}")
	private CompanyService companyService;

	/** The item service. */
	@ManagedProperty(value = "#{itemService}")
	private ItemService itemService;

	/** The item service. */
	@ManagedProperty(value = "#{fileItemService}")
	private FileItemService fileItemService;

	/** The country service. */
	@ManagedProperty(value = "#{countryService}")
	private CountryService countryService;

	/** The country service. */
	@ManagedProperty(value = "#{flowService}")
	private FlowService flowService;

	/** The sample filter. */
	private SampleFilter sampleFilter;

	/** The analyse filter. */
	private AnalyseFilter analyseFilter;

	/** The filter. */
	private Filter filter;

	/** The filter decision delivree. */
	private Filter filterDecisionDelivree;

	/** The detail page url. */
	private String detailPageUrl;

	/** The sample lab list. */
	private List<AnalyseOrder> sampleLabList;

	/** The analyse lab list. */
	private List<AnalyseOrder> analyseLabList;

	/** The laboratories list. */
	private List<Laboratory> laboratoriesList;

	/** The file item by desision list. */
	private List<FileItem> fileItemByDesicionList;

	/** The file item by client desicion list. */
	private List<FileItem> fileItemByClientDesicionList;

	/** The file item by client product list. */
	private List<FileItem> fileItemByClientProductList;

	/** The client list. */
	private List<Company> clientList;

	/** The nsh list. */
	private List<Item> nshList;

	/** The historic client filter. */
	private HistoricClientFilter historicClientFilter;

	/** The file item by insp destribution list. */
	private List<FileItem> fileItemByInspDestributionList;

	/** The inspection destrib filter. */
	private InspectionDestribFilter inspectionDestribFilter;

	/** The service item by products quantities list. */
	private List<ServicesItem> serviceItemByProductsQuantitiesList;

	/** The service item by products quantities by drd list. */
	private List<ServicesItem> serviceItemByProductsQuantitiesByDrdList;

	/** The service item dto by products quantities list. */
	private List<ServiceItemDto> serviceItemDtoByProductsQuantitiesList;

	/** The service item dto by products quantities by drd list. */
	private List<ServiceItemDto> serviceItemDtoByProductsQuantitiesByDrdList;

	/** The flow code list. */
	private final List<String> flowCodeList = Arrays.asList(FlowCode.FL_CT_14.name(), FlowCode.FL_CT_74.name());
	/** The file item by decisions delivrees list. */
	private List<FileItem> fileItemByDecisionsDelivreesList;

	/** The decision data json. */
	private String decisionDataJSON;

	/** The type. */
	private String type;

	/** The file item by statistic business list. */
	private List<FileItem> fileItemByStatisticBusinessList;

	/** The statistic business filter. */
	private StatisticBusinessFilter statisticBusinessFilter;

	/** The countries. */
	private List<Country> countries;

	/** The check operation. */
	private Boolean checkOperation;

	/** The search result. */
	private List<FileItem> searchResult;

	/** The final decisions. */
	private List<Flow> finalDecisions;

	/** The file items sauv. */
	private List<FileItem> fileItemsSauv = new ArrayList<FileItem>();

	/**
	 * Instantiates a new statistic controller.
	 */
	public StatisticController()
	{
		super();
	}



	/** The decision type. */
	private List<String> decisionType = new ArrayList<String>();



	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, StatisticController.class.getName());
		}
		decisionType.add(FinalDecisionType.CCT.getType());
		decisionType.add(FinalDecisionType.AP.getType());
		decisionType.add(FinalDecisionType.RD.getType());
		decisionType.add(FinalDecisionType.REJET.getType());
		decisionType.add(FinalDecisionType.ANNULATION.getType());
	}

	/**
	 * Inits the sample by lab search.
	 */
	public void initSampleByLabSearch()
	{
		sampleFilter = new SampleFilter();
		sampleLabList = new ArrayList<AnalyseOrder>();
		laboratoriesList = laboratoryService.findByAdministration(getCurrentOrganism() != null ? getCurrentOrganism()
				: getCurrentAdministration());
	}


	/**
	 * Inits the analyse by lab search.
	 */
	public void initAnalyseByLabSearch()
	{
		analyseFilter = new AnalyseFilter();
		analyseLabList = new ArrayList<AnalyseOrder>();
		laboratoriesList = laboratoryService.findByAdministration(getCurrentOrganism() != null ? getCurrentOrganism()
				: getCurrentAdministration());
	}

	/**
	 * Inits the analyse by lab search.
	 */
	public void initFileItemByDesicionSearch()
	{
		filter = new Filter();
		fileItemByDesicionList = new ArrayList<FileItem>();
		doFileItemDesicionByFilter();
	}

	/**
	 * Inits the file item by client desicion search.
	 */
	public void initFileItemByClientDesicionSearch()
	{
		historicClientFilter = new HistoricClientFilter();
		fileItemByClientDesicionList = new ArrayList<FileItem>();
		finalDecisions = flowService.findFinalDecisions();
		clientList = companyService.findAll();
		doFileItemClientDesicionByFilter();
	}

	/**
	 * Inits the file item by client product search.
	 */
	public void initFileItemByClientProductSearch()
	{
		historicClientFilter = new HistoricClientFilter();
		fileItemByClientProductList = new ArrayList<FileItem>();
		clientList = companyService.findAll();
		nshList = itemService.findAll();
		doFileItemClientProductByFilter();
	}

	/**
	 * Inits the file item by insp destribution search.
	 */
	public void initFileItemByInsptionDestribSearch()
	{
		inspectionDestribFilter = new InspectionDestribFilter();
		fileItemByInspDestributionList = new ArrayList<FileItem>();
		doFileItemInspectionDestribByFilter();
	}

	/**
	 * Inits the file item by insp destribution search.
	 */
	public void initServiceItemByProductsQuantitiesSearch()
	{
		filter = new Filter();
		serviceItemByProductsQuantitiesList = new ArrayList<ServicesItem>();
		doServiceItemProductsQuantitiesFilter();
	}

	/**
	 * Inits the file item by products quantities by search.
	 */
	public void initServiceItemByProductsQuantitiesByDrdSearch()
	{
		filter = new Filter();
		serviceItemByProductsQuantitiesByDrdList = new ArrayList<ServicesItem>();
		doServiceItemProductsQuantitiesByDrdFilter();
	}

	/**
	 * Inits the service item by statistics business search.
	 */
	public void initServiceItemByStatisticsBusinessSearch()
	{
		statisticBusinessFilter = new StatisticBusinessFilter();
		fileItemByStatisticBusinessList = new ArrayList<FileItem>();
		countries = countryService.findAll();
		clientList = companyService.findAll();
		nshList = itemService.findAll();
		doServiceItemByStatisticsBusinessFilter();
	}

	/**
	 * Inits the pinding files search.
	 */
	public void initPindingFilesSearch()
	{
		filter = new Filter();
		findPindingFiles();
	}

	/**
	 * Go to sample by lab statistic page.
	 */
	public void goToSampleByLabStatisticPage()
	{
		try
		{
			initSampleByLabSearch();
			sampleFilter = new SampleFilter();
			sampleLabList = new ArrayList<AnalyseOrder>();

			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.SEARCH_SAMPLE_BY_LABORATORY));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Go to sample by lab statistic page.
	 */
	public void goToDecisionRefoulementDestructionPage()
	{
		try
		{
			initFileItemByDesicionSearch();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.SEARCH_FILE_ITEM_BY_DESICION));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}
	}


	/**
	 * Go to analyse by lab statistic page.
	 */
	public void goToAnalyseByLabStatisticPage()
	{
		try
		{
			analyseFilter = new AnalyseFilter();
			analyseLabList = new ArrayList<AnalyseOrder>();
			initAnalyseByLabSearch();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.SEARCH_ANALYSE_BY_lABORATORY));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Go to historic Client decision.
	 */
	public void goToHistoricClientDecisionPage()
	{
		try
		{
			initFileItemByClientDesicionSearch();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.SEARCH_HISTORIC_CLIENT_BY_DECISION));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Go to historic Client product.
	 */
	public void goToHistoricClientProductPage()
	{
		try
		{
			initFileItemByClientProductSearch();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.SEARCH_HISTORIC_CLIENT_BY_PRODUCT));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Go to inspection destribution page.
	 */
	public void goToInspectionDestributionPage()
	{
		try
		{
			initFileItemByInsptionDestribSearch();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.SEARCH_INSPECTION_DESTRIBUTION));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Go to repartition decision delivree.
	 */
	public void goToRepartitionDecisionDelivree()
	{
		try
		{
			initFileItemByDecisionDelivreeSearch();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.SEARCH_DESICIONS_DELIVREES));

			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}

	}


	/**
	 * Go to products quantities page.
	 */
	public void goToProductsQuantitiesPage()
	{
		try
		{
			initServiceItemByProductsQuantitiesSearch();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.SEARCH_PRODUCTS_QUANTITY));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Go to pinding search page.
	 */
	public void goToPindingSearchPage()
	{

		try
		{
			initPindingFilesSearch();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.STATISTIC_PINDING_FILES));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}

	}

	/**
	 * Go to products quantities by drd pag.
	 */
	public void goToProductsQuantitiesByDrdPage()
	{
		try
		{
			initServiceItemByProductsQuantitiesByDrdSearch();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.SEARCH_PRODUCTS_QUANTITY_BY_DRD));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Go to statistics business page.
	 */
	public void goToStatisticsBusinessPage()
	{
		try
		{
			initServiceItemByStatisticsBusinessSearch();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.SEARCH_STATISTICS_ON_BUSINESS));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}
	}




	/**
	 * Inits the file item by decision delivree search.
	 */
	public void initFileItemByDecisionDelivreeSearch()
	{
		fileItemByDecisionsDelivreesList = new ArrayList<FileItem>();
		filterDecisionDelivree = new Filter();
		decisionDataJSON = StringUtils.EMPTY;

	}


	/**
	 * Do sample lab search by filter.
	 */
	public void doSampleLabSearchByFilter()
	{
		if (sampleFilter.getFromDate() != null && sampleFilter.getToDate() != null
				&& sampleFilter.getFromDate().after(sampleFilter.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			sampleLabList = commonService.findStatisticByFilter(sampleFilter);
		}
	}

	/**
	 * Do search by filter.
	 */
	public void doAnalyseLabSearchByFilter()
	{
		if (analyseFilter.getFromDate() != null && analyseFilter.getToDate() != null
				&& analyseFilter.getFromDate().after(analyseFilter.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			analyseLabList = commonService.findStatisticByFilter(analyseFilter);
		}
	}

	/**
	 * Do file item by desicion.
	 */
	public void doFileItemDesicionByFilter()
	{

		if (filter.getFromDate() != null && filter.getToDate() != null && filter.getFromDate().after(filter.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			fileItemByDesicionList = commonService.fileItemByDesicionByFilter(filter, getLoggedUser(),
					Arrays.asList(FlowCode.FL_CT_14.name(), FlowCode.FL_CT_74.name()),
					Collections.singletonList(FlowCode.FL_CT_72.name()), getCurrentAdministration());
		}
	}

	/**
	 * Do file item desicion delivree by filter.
	 */
	public void doFileItemDesicionDelivreeByFilter()
	{
		//Certificat de contrôle technique
		final List<String> cct = Arrays.asList(FlowCode.FL_CT_08.name(), FlowCode.FL_CT_89.name());
		//Autorisation préalable d’enlèvement
		final List<String> ap = Arrays.asList(FlowCode.FL_AP_107.name());
		//Refoulement/destruction
		final List<String> rf = Arrays.asList(FlowCode.FL_CT_14.name(), FlowCode.FL_CT_74.name());
		//Rejet
		final List<String> rejet = Arrays.asList(FlowCode.FL_CT_04.name(), FlowCode.FL_CT_91.name(), FlowCode.FL_AP_87.name(),
				FlowCode.FL_AP_95.name(), FlowCode.FL_AP_96.name(), FlowCode.FL_AP_97.name(), FlowCode.FL_AP_98.name(),
				FlowCode.FL_AP_99.name(), FlowCode.FL_AP_100.name());
		//Annulation
		final List<String> annulation = Arrays.asList(FlowCode.FL_CT_62.name(), FlowCode.FL_AP_152.name());
		final List<String> flowCodeList = new ArrayList<String>();
		flowCodeList.addAll(cct);
		flowCodeList.addAll(ap);
		flowCodeList.addAll(rf);
		flowCodeList.addAll(rejet);
		flowCodeList.addAll(annulation);

		final Gson gson = new Gson();



		final List<ChartUtils> fileItems = new ArrayList<ChartUtils>();


		Integer numberCct = 0;
		Integer numberAP = 0;
		Integer numberAN = 0;
		Integer numberRE = 0;
		Integer numberRF = 0;

		if (filterDecisionDelivree.getFromDate() != null && filterDecisionDelivree.getToDate() != null
				&& filterDecisionDelivree.getFromDate().after(filterDecisionDelivree.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			fileItemByDecisionsDelivreesList = commonService.fileItemByDesicionByFilter(filterDecisionDelivree, getLoggedUser(),
					flowCodeList, null, getCurrentAdministration());

			for (final FileItem fileItem : fileItemByDecisionsDelivreesList)
			{
				for (final ItemFlow itemFlow : fileItem.getItemFlowsList())
				{

					if (cct.contains(itemFlow.getFlow().getCode()))
					{
						fileItem.setDecisionType(FinalDecisionType.CCT);
						numberCct++;
					}
					else if (ap.contains(itemFlow.getFlow().getCode()))
					{
						fileItem.setDecisionType(FinalDecisionType.AP);
						numberAP++;
					}
					else if (rf.contains(itemFlow.getFlow().getCode()))
					{
						fileItem.setDecisionType(FinalDecisionType.RD);
						numberRF++;
					}
					else if (rejet.contains(itemFlow.getFlow().getCode()))
					{
						fileItem.setDecisionType(FinalDecisionType.REJET);
						numberRE++;
					}
					else if (annulation.contains(itemFlow.getFlow().getCode()))
					{
						fileItem.setDecisionType(FinalDecisionType.ANNULATION);
						numberAN++;
					}
				}
			}
			fileItemsSauv = fileItemByDecisionsDelivreesList;
			if (numberCct > 0)
			{
				fileItems.add(new ChartUtils(FinalDecisionType.CCT.getType(), numberCct));
			}
			if (numberAP > 0)
			{
				fileItems.add(new ChartUtils(FinalDecisionType.AP.getType(), numberAP));
			}
			if (numberAN > 0)
			{
				fileItems.add(new ChartUtils(FinalDecisionType.ANNULATION.getType(), numberAN));
			}
			if (numberRE > 0)
			{
				fileItems.add(new ChartUtils(FinalDecisionType.REJET.getType(), numberRE));
			}
			if (numberRF > 0)
			{
				fileItems.add(new ChartUtils(FinalDecisionType.RD.getType(), numberRF));
			}
			if (CollectionUtils.isNotEmpty(fileItemByDecisionsDelivreesList))
			{
				decisionDataJSON = gson.toJson(fileItems);
				final RequestContext context = RequestContext.getCurrentInstance();
				context.execute("	chartInc();");
			}

		}
	}

	/**
	 * Do file item client desicion by filter.
	 */
	public void doFileItemClientDesicionByFilter()
	{
		if (historicClientFilter.getFromDate() != null && historicClientFilter.getToDate() != null
				&& historicClientFilter.getFromDate().after(historicClientFilter.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			fileItemByClientDesicionList = commonService.fileItemByCompanyAndDecicionByFilter(historicClientFilter, getLoggedUser(),
					getCurrentAdministration());
		}
	}

	/**
	 * Do file item client product by filter.
	 */
	public void doFileItemClientProductByFilter()
	{
		if (historicClientFilter.getFromDate() != null && historicClientFilter.getToDate() != null
				&& historicClientFilter.getFromDate().after(historicClientFilter.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			fileItemByClientProductList = commonService.fileItemByCompanyAndProductByFilter(historicClientFilter, getLoggedUser(),
					getCurrentAdministration());
		}
	}

	/**
	 * Do file item insp destribution by filter.
	 */
	public void doFileItemInspectionDestribByFilter()
	{
		if (inspectionDestribFilter.getFromDate() != null && inspectionDestribFilter.getToDate() != null
				&& inspectionDestribFilter.getFromDate().after(inspectionDestribFilter.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			fileItemByInspDestributionList = commonService.fileItemByInspectionDestribByFilter(inspectionDestribFilter,
					getLoggedUser(), getCurrentAdministration());
		}
	}


	/**
	 * Do service item products quantities filter.
	 */
	public void doServiceItemProductsQuantitiesFilter()
	{
		if (filter.getFromDate() != null && filter.getToDate() != null && filter.getFromDate().after(filter.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			final List<Object[]> objectsList = commonService.serviceItemProductsQuantitiesByFilter(filter, getLoggedUser(),
					getCurrentAdministration());
			serviceItemDtoByProductsQuantitiesList = new ArrayList<ServiceItemDto>();
			for (final Object[] objects : objectsList)
			{
				final ServiceItemDto serviceItemDto = new ServiceItemDto();
				serviceItemDto.setSsnh(objects[0].toString());
				serviceItemDto.setLabel(objects[1].toString());
				if (objects[2] != null)
				{
					serviceItemDto.setSumQuantities(objects[2].toString());
				}
				if (objects[3] != null)
				{
					serviceItemDto.setSumValeurFob(objects[3].toString());
				}
				serviceItemDtoByProductsQuantitiesList.add(serviceItemDto);
			}
		}
	}


	/**
	 * Do service item products quantities by drd filter.
	 */
	public void doServiceItemProductsQuantitiesByDrdFilter()
	{
		if (filter.getFromDate() != null && filter.getToDate() != null && filter.getFromDate().after(filter.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			final List<Object[]> objectsList = commonService.serviceItemProductsQuantitiesByDrdByFilter(filter,
					Arrays.asList(FlowCode.FL_CT_14.name(), FlowCode.FL_CT_74.name()), getLoggedUser(), getCurrentAdministration());
			serviceItemDtoByProductsQuantitiesByDrdList = new ArrayList<ServiceItemDto>();
			for (final Object[] objects : objectsList)
			{
				final ServiceItemDto serviceItemDto = new ServiceItemDto();
				serviceItemDto.setSsnh(objects[0].toString());
				serviceItemDto.setLabel(objects[1].toString());
				if (objects[2] != null)
				{
					serviceItemDto.setSumQuantities(objects[2].toString());
				}
				if (objects[3] != null)
				{
					serviceItemDto.setSumValeurFob(objects[3].toString());
				}
				serviceItemDtoByProductsQuantitiesByDrdList.add(serviceItemDto);
			}
		}
	}

	/**
	 * Do service item by statistics business filter.
	 */
	public void doServiceItemByStatisticsBusinessFilter()
	{
		if (statisticBusinessFilter.getFromDate() != null && statisticBusinessFilter.getToDate() != null
				&& statisticBusinessFilter.getFromDate().after(statisticBusinessFilter.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			fileItemByStatisticBusinessList = commonService.fileItemByStatisticBusinessByFilter(statisticBusinessFilter,
					getLoggedUser(), getCurrentAdministration());
		}
	}

	/**
	 * Find pinding files.
	 */
	public void findPindingFiles()
	{
		if (filter.getFromDate() != null && filter.getToDate() != null && filter.getFromDate().after(filter.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			searchResult = commonService.findPindingFileItem(filter, getLoggedUser());
			System.err.println();
		}

	}

	/**
	 * Reset statistics on business search.
	 */
	public void resetStatisticsOnBusinessSearch()
	{
		statisticBusinessFilter = new StatisticBusinessFilter();
	}

	/**
	 * Gets the detail page url.
	 *
	 * @return the detailPageUrl
	 */
	public String getDetailPageUrl()
	{
		return detailPageUrl;
	}

	/**
	 * Sets the detail page url.
	 *
	 * @param detailPageUrl
	 *           the detailPageUrl to set
	 */
	public void setDetailPageUrl(final String detailPageUrl)
	{
		this.detailPageUrl = detailPageUrl;
	}

	/**
	 * Gets the analyse lab list.
	 *
	 * @return the analyse lab list
	 */
	public List<AnalyseOrder> getAnalyseLabList()
	{
		return analyseLabList;
	}

	/**
	 * Sets the analyse lab list.
	 *
	 * @param analyseLabList
	 *           the new analyse lab list
	 */
	public void setAnalyseLabList(final List<AnalyseOrder> analyseLabList)
	{
		this.analyseLabList = analyseLabList;
	}

	/**
	 * Gets the laboratories list.
	 *
	 * @return the laboratories list
	 */
	public List<Laboratory> getLaboratoriesList()
	{
		return laboratoriesList;
	}


	/**
	 * Sets the laboratories list.
	 *
	 * @param laboratoriesList
	 *           the new laboratories list
	 */
	public void setLaboratoriesList(final List<Laboratory> laboratoriesList)
	{
		this.laboratoriesList = laboratoriesList;
	}


	/**
	 * Gets the laboratory service.
	 *
	 * @return the laboratory service
	 */
	public LaboratoryService getLaboratoryService()
	{
		return laboratoryService;
	}


	/**
	 * Sets the laboratory service.
	 *
	 * @param laboratoryService
	 *           the new laboratory service
	 */
	public void setLaboratoryService(final LaboratoryService laboratoryService)
	{
		this.laboratoryService = laboratoryService;
	}


	/**
	 * Gets the sample filter.
	 *
	 * @return the sample filter
	 */
	public SampleFilter getSampleFilter()
	{
		return sampleFilter;
	}


	/**
	 * Sets the sample filter.
	 *
	 * @param sampleFilter
	 *           the new sample filter
	 */
	public void setSampleFilter(final SampleFilter sampleFilter)
	{
		this.sampleFilter = sampleFilter;
	}


	/**
	 * Gets the analyse filter.
	 *
	 * @return the analyse filter
	 */
	public AnalyseFilter getAnalyseFilter()
	{
		return analyseFilter;
	}


	/**
	 * Sets the analyse filter.
	 *
	 * @param analyseFilter
	 *           the new analyse filter
	 */
	public void setAnalyseFilter(final AnalyseFilter analyseFilter)
	{
		this.analyseFilter = analyseFilter;
	}


	/**
	 * Gets the common service.
	 *
	 * @return the common service
	 */
	public CommonService getCommonService()
	{
		return commonService;
	}


	/**
	 * Sets the common service.
	 *
	 * @param commonService
	 *           the new common service
	 */
	public void setCommonService(final CommonService commonService)
	{
		this.commonService = commonService;
	}

	/**
	 * Gets the sample lab list.
	 *
	 * @return the sample lab list
	 */
	public List<AnalyseOrder> getSampleLabList()
	{
		return sampleLabList;
	}


	/**
	 * Sets the sample lab list.
	 *
	 * @param sampleLabList
	 *           the new sample lab list
	 */
	public void setSampleLabList(final List<AnalyseOrder> sampleLabList)
	{
		this.sampleLabList = sampleLabList;
	}


	/**
	 * Gets the filter.
	 *
	 * @return the filter
	 */
	public Filter getFilter()
	{
		return filter;
	}


	/**
	 * Sets the filter.
	 *
	 * @param filter
	 *           the new filter
	 */
	public void setFilter(final Filter filter)
	{
		this.filter = filter;
	}


	/**
	 * Gets the file item by desicion list.
	 *
	 * @return the file item by desicion list
	 */
	public List<FileItem> getFileItemByDesicionList()
	{
		return fileItemByDesicionList;
	}


	/**
	 * Sets the file item by desicion list.
	 *
	 * @param fileItemByDesicionList
	 *           the new file item by desicion list
	 */
	public void setFileItemByDesicionList(final List<FileItem> fileItemByDesicionList)
	{
		this.fileItemByDesicionList = fileItemByDesicionList;
	}


	/**
	 * Gets the company service.
	 *
	 * @return the company service
	 */
	public CompanyService getCompanyService()
	{
		return companyService;
	}


	/**
	 * Sets the company service.
	 *
	 * @param companyService
	 *           the new company service
	 */
	public void setCompanyService(final CompanyService companyService)
	{
		this.companyService = companyService;
	}


	/**
	 * Gets the item service.
	 *
	 * @return the item service
	 */
	public ItemService getItemService()
	{
		return itemService;
	}


	/**
	 * Sets the item service.
	 *
	 * @param itemService
	 *           the new item service
	 */
	public void setItemService(final ItemService itemService)
	{
		this.itemService = itemService;
	}


	/**
	 * Gets the file item by client desicion list.
	 *
	 * @return the file item by client desicion list
	 */
	public List<FileItem> getFileItemByClientDesicionList()
	{
		return fileItemByClientDesicionList;
	}


	/**
	 * Sets the file item by client desicion list.
	 *
	 * @param fileItemByClientDesicionList
	 *           the new file item by client desicion list
	 */
	public void setFileItemByClientDesicionList(final List<FileItem> fileItemByClientDesicionList)
	{
		this.fileItemByClientDesicionList = fileItemByClientDesicionList;
	}


	/**
	 * Gets the file item by client product list.
	 *
	 * @return the file item by client product list
	 */
	public List<FileItem> getFileItemByClientProductList()
	{
		return fileItemByClientProductList;
	}


	/**
	 * Sets the file item by client product list.
	 *
	 * @param fileItemByClientProductList
	 *           the new file item by client product list
	 */
	public void setFileItemByClientProductList(final List<FileItem> fileItemByClientProductList)
	{
		this.fileItemByClientProductList = fileItemByClientProductList;
	}

	/**
	 * Gets the client list.
	 *
	 * @return the client list
	 */
	public List<Company> getClientList()
	{
		return clientList;
	}

	/**
	 * Gets the nsh list.
	 *
	 * @return the nsh list
	 */
	public List<Item> getNshList()
	{
		return nshList;
	}


	/**
	 * Sets the nsh list.
	 *
	 * @param nshList
	 *           the new nsh list
	 */
	public void setNshList(final List<Item> nshList)
	{
		this.nshList = nshList;
	}

	/**
	 * Sets the client list.
	 *
	 * @param clientList
	 *           the new client list
	 */
	public void setClientList(final List<Company> clientList)
	{
		this.clientList = clientList;
	}


	/**
	 * Gets the historic client filter.
	 *
	 * @return the historic client filter
	 */
	public HistoricClientFilter getHistoricClientFilter()
	{
		return historicClientFilter;
	}


	/**
	 * Sets the historic client filter.
	 *
	 * @param historicClientFilter
	 *           the new historic client filter
	 */
	public void setHistoricClientFilter(final HistoricClientFilter historicClientFilter)
	{
		this.historicClientFilter = historicClientFilter;
	}


	/**
	 * Gets the file item service.
	 *
	 * @return the file item service
	 */
	public FileItemService getFileItemService()
	{
		return fileItemService;
	}


	/**
	 * Sets the file item service.
	 *
	 * @param fileItemService
	 *           the new file item service
	 */
	public void setFileItemService(final FileItemService fileItemService)
	{
		this.fileItemService = fileItemService;
	}


	/**
	 * Gets the file item by insp destribution list.
	 *
	 * @return the file item by insp destribution list
	 */
	public List<FileItem> getFileItemByInspDestributionList()
	{
		return fileItemByInspDestributionList;
	}


	/**
	 * Sets the file item by insp destribution list.
	 *
	 * @param fileItemByInspDestributionList
	 *           the new file item by insp destribution list
	 */
	public void setFileItemByInspDestributionList(final List<FileItem> fileItemByInspDestributionList)
	{
		this.fileItemByInspDestributionList = fileItemByInspDestributionList;
	}


	/**
	 * Gets the inspection destrib filter.
	 *
	 * @return the inspection destrib filter
	 */
	public InspectionDestribFilter getInspectionDestribFilter()
	{
		return inspectionDestribFilter;
	}


	/**
	 * Sets the inspection destrib filter.
	 *
	 * @param inspectionDestribFilter
	 *           the new inspection destrib filter
	 */
	public void setInspectionDestribFilter(final InspectionDestribFilter inspectionDestribFilter)
	{
		this.inspectionDestribFilter = inspectionDestribFilter;
	}


	/**
	 * Gets the service item by products quantities list.
	 *
	 * @return the service item by products quantities list
	 */
	public List<ServicesItem> getServiceItemByProductsQuantitiesList()
	{
		return serviceItemByProductsQuantitiesList;
	}


	/**
	 * Sets the service item by products quantities list.
	 *
	 * @param serviceItemByProductsQuantitiesList
	 *           the new service item by products quantities list
	 */
	public void setServiceItemByProductsQuantitiesList(final List<ServicesItem> serviceItemByProductsQuantitiesList)
	{
		this.serviceItemByProductsQuantitiesList = serviceItemByProductsQuantitiesList;
	}


	/**
	 * Gets the service item by products quantities by drd list.
	 *
	 * @return the service item by products quantities by drd list
	 */
	public List<ServicesItem> getServiceItemByProductsQuantitiesByDrdList()
	{
		return serviceItemByProductsQuantitiesByDrdList;
	}


	/**
	 * Sets the service item by products quantities by drd list.
	 *
	 * @param serviceItemByProductsQuantitiesByDrdList
	 *           the new service item by products quantities by drd list
	 */
	public void setServiceItemByProductsQuantitiesByDrdList(final List<ServicesItem> serviceItemByProductsQuantitiesByDrdList)
	{
		this.serviceItemByProductsQuantitiesByDrdList = serviceItemByProductsQuantitiesByDrdList;
	}


	/**
	 * Gets the service item dto by products quantities list.
	 *
	 * @return the service item dto by products quantities list
	 */
	public List<ServiceItemDto> getServiceItemDtoByProductsQuantitiesList()
	{
		return serviceItemDtoByProductsQuantitiesList;
	}


	/**
	 * Sets the service item dto by products quantities list.
	 *
	 * @param serviceItemDtoByProductsQuantitiesList
	 *           the new service item dto by products quantities list
	 */
	public void setServiceItemDtoByProductsQuantitiesList(final List<ServiceItemDto> serviceItemDtoByProductsQuantitiesList)
	{
		this.serviceItemDtoByProductsQuantitiesList = serviceItemDtoByProductsQuantitiesList;
	}


	/**
	 * Gets the service item dto by products quantities by drd list.
	 *
	 * @return the service item dto by products quantities by drd list
	 */
	public List<ServiceItemDto> getServiceItemDtoByProductsQuantitiesByDrdList()
	{
		return serviceItemDtoByProductsQuantitiesByDrdList;
	}


	/**
	 * Sets the service item dto by products quantities by drd list.
	 *
	 * @param serviceItemDtoByProductsQuantitiesByDrdList
	 *           the new service item dto by products quantities by drd list
	 */
	public void setServiceItemDtoByProductsQuantitiesByDrdList(
			final List<ServiceItemDto> serviceItemDtoByProductsQuantitiesByDrdList)
	{
		this.serviceItemDtoByProductsQuantitiesByDrdList = serviceItemDtoByProductsQuantitiesByDrdList;
	}

	/**
	 * Gets the file item by decisions delivrees list.
	 *
	 * @return the file item by decisions delivrees list
	 */
	public List<FileItem> getFileItemByDecisionsDelivreesList()
	{
		return fileItemByDecisionsDelivreesList;
	}


	/**
	 * Sets the file item by decisions delivrees list.
	 *
	 * @param fileItemByDecisionsDelivreesList
	 *           the new file item by decisions delivrees list
	 */
	public void setFileItemByDecisionsDelivreesList(final List<FileItem> fileItemByDecisionsDelivreesList)
	{
		this.fileItemByDecisionsDelivreesList = fileItemByDecisionsDelivreesList;
	}


	/**
	 * Gets the filter decision delivree.
	 *
	 * @return the filter decision delivree
	 */
	public Filter getFilterDecisionDelivree()
	{
		return filterDecisionDelivree;
	}


	/**
	 * Sets the filter decision delivree.
	 *
	 * @param filterDecisionDelivree
	 *           the new filter decision delivree
	 */
	public void setFilterDecisionDelivree(final Filter filterDecisionDelivree)
	{
		this.filterDecisionDelivree = filterDecisionDelivree;
	}


	/**
	 * Gets the decision data json.
	 *
	 * @return the decision data json
	 */
	public String getDecisionDataJSON()
	{
		return decisionDataJSON;
	}


	/**
	 * Sets the decision data json.
	 *
	 * @param decisionDataJSON
	 *           the new decision data json
	 */
	public void setDecisionDataJSON(final String decisionDataJSON)
	{
		this.decisionDataJSON = decisionDataJSON;
	}

	/**
	 * Gets the decision type.
	 *
	 * @return the decision type
	 */
	public List<String> getDecisionType()
	{
		return decisionType;
	}

	/**
	 * Sets the decision type.
	 *
	 * @param decisionType
	 *           the new decision type
	 */
	public void setDecisionType(final List<String> decisionType)
	{
		this.decisionType = decisionType;
	}

	/**
	 * Execute.
	 *
	 *
	 */
	public void execute()
	{
		final List<FileItem> fileItems = new ArrayList<FileItem>();
		for (final FileItem fileItem : fileItemsSauv)
		{
			if (type.equals(fileItem.getDecisionType().getType()))
			{
				fileItems.add(fileItem);
			}
		}

		fileItemByDecisionsDelivreesList = fileItems;
	}

	/**
	 * Go to detail page.
	 */
	public void goToDetailPage()
	{
		try
		{

			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();

			//	final FileItem fileItem = fileItemService.find(selected.getId());
			final List<FileTypeCode> cctCodes = Arrays.asList(FileTypeCode.CCT_CT, FileTypeCode.CC_CT, FileTypeCode.CQ_CT);

			if (cctCodes.contains(selected.getFile().getFileType().getCode()))
			{
				setDetailPageUrl(ControllerConstants.Pages.FO.DETAILS_CCT_INDEX_PAGE);

				final FileItemCctDetailController fileItemCctDetailController = getInstanceOfPageFileItemCctDetailController();
				fileItemCctDetailController.setCurrentFileItem(selected);
				fileItemCctDetailController.setComeFromSearch(Boolean.TRUE);
				fileItemCctDetailController.init();
			}
			else
			{
				setDetailPageUrl(ControllerConstants.Pages.FO.DETAILS_AP_INDEX_PAGE);

				final FileItemApDetailController fileItemApDetailController = getInstanceOfPageFileItemApDetailController();
				fileItemApDetailController.setCurrentFile(selected.getFile());
				fileItemApDetailController.setComeFromSearch(Boolean.TRUE);
				fileItemApDetailController.init();
			}

			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, detailPageUrl));
			extContext.redirect(url);
		}
		catch (final IOException ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Gets the instance of page file item detail controller.
	 *
	 * @return the instance of page file item detail controller
	 */
	public FileItemCctDetailController getInstanceOfPageFileItemCctDetailController()
	{
		final FacesContext fctx = FacesContext.getCurrentInstance();
		final Application application = fctx.getApplication();
		final ELContext context = fctx.getELContext();
		final ExpressionFactory expressionFactory = application.getExpressionFactory();
		final ValueExpression createValueExpression = expressionFactory.createValueExpression(context,
				"#{fileItemCctDetailController}", FileItemCctDetailController.class);
		return (FileItemCctDetailController) createValueExpression.getValue(context);
	}

	/**
	 * Gets the instance of page file item ap detail controller.
	 *
	 * @return the instance of page file item ap detail controller
	 */
	public FileItemApDetailController getInstanceOfPageFileItemApDetailController()
	{
		final FacesContext fctx = FacesContext.getCurrentInstance();
		final Application application = fctx.getApplication();
		final ELContext context = fctx.getELContext();
		final ExpressionFactory expressionFactory = application.getExpressionFactory();
		final ValueExpression createValueExpression = expressionFactory.createValueExpression(context,
				"#{fileItemApDetailController}", FileItemApDetailController.class);
		return (FileItemApDetailController) createValueExpression.getValue(context);
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *           the new type
	 */
	public void setType(final String type)
	{
		this.type = type;
	}

	/**
	 * Gets the file items sauv.
	 *
	 * @return the file items sauv
	 */
	public List<FileItem> getFileItemsSauv()
	{
		return fileItemsSauv;
	}

	/**
	 * Sets the file items sauv.
	 *
	 * @param fileItemsSauv
	 *           the new file items sauv
	 */
	public void setFileItemsSauv(final List<FileItem> fileItemsSauv)
	{
		this.fileItemsSauv = fileItemsSauv;
	}

	/**
	 * Gets the file item by statistic business list.
	 *
	 * @return the file item by statistic business list
	 */
	public List<FileItem> getFileItemByStatisticBusinessList()
	{
		return fileItemByStatisticBusinessList;
	}

	/**
	 * Sets the file item by statistic business list.
	 *
	 * @param fileItemByStatisticBusinessList
	 *           the new file item by statistic business list
	 */
	public void setFileItemByStatisticBusinessList(final List<FileItem> fileItemByStatisticBusinessList)
	{
		this.fileItemByStatisticBusinessList = fileItemByStatisticBusinessList;
	}

	/**
	 * Gets the statistic business filter.
	 *
	 * @return the statistic business filter
	 */
	public StatisticBusinessFilter getStatisticBusinessFilter()
	{
		return statisticBusinessFilter;
	}

	/**
	 * Sets the statistic business filter.
	 *
	 * @param statisticBusinessFilter
	 *           the new statistic business filter
	 */
	public void setStatisticBusinessFilter(final StatisticBusinessFilter statisticBusinessFilter)
	{
		this.statisticBusinessFilter = statisticBusinessFilter;
	}

	/**
	 * Gets the flow code list.
	 *
	 * @return the flow code list
	 */
	public List<String> getFlowCodeList()
	{
		return flowCodeList;
	}

	/**
	 * Gets the country service.
	 *
	 * @return the country service
	 */
	public CountryService getCountryService()
	{
		return countryService;
	}

	/**
	 * Sets the country service.
	 *
	 * @param countryService
	 *           the new country service
	 */
	public void setCountryService(final CountryService countryService)
	{
		this.countryService = countryService;
	}

	/**
	 * Gets the countries.
	 *
	 * @return the countries
	 */
	public List<Country> getCountries()
	{
		return countries;
	}

	/**
	 * Sets the countries.
	 *
	 * @param countries
	 *           the new countries
	 */
	public void setCountries(final List<Country> countries)
	{
		this.countries = countries;
	}

	/**
	 * Gets the check operation.
	 *
	 * @return the check operation
	 */
	public Boolean getCheckOperation()
	{
		return checkOperation;
	}

	/**
	 * Sets the check operation.
	 *
	 * @param checkOperation
	 *           the new check operation
	 */
	public void setCheckOperation(final Boolean checkOperation)
	{
		this.checkOperation = checkOperation;
	}

	/**
	 * Gets the search result.
	 *
	 * @return the search result
	 */
	public List<FileItem> getSearchResult()
	{
		return searchResult;
	}

	/**
	 * Sets the search result.
	 *
	 * @param searchResult
	 *           the new search result
	 */
	public void setSearchResult(final List<FileItem> searchResult)
	{
		this.searchResult = searchResult;
	}

	/**
	 * Gets the flow service.
	 *
	 * @return the flow service
	 */
	public FlowService getFlowService()
	{
		return flowService;
	}

	/**
	 * Sets the flow service.
	 *
	 * @param flowService
	 *           the new flow service
	 */
	public void setFlowService(final FlowService flowService)
	{
		this.flowService = flowService;
	}

	/**
	 * Gets the final decisions.
	 *
	 * @return the final decisions
	 */
	public List<Flow> getFinalDecisions()
	{
		return finalDecisions;
	}

	/**
	 * Sets the final decisions.
	 *
	 * @param finalDecisions
	 *           the new final decisions
	 */
	public void setFinalDecisions(final List<Flow> finalDecisions)
	{
		this.finalDecisions = finalDecisions;
	}

}
