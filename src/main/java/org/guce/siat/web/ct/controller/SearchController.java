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
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.guce.siat.common.model.AnalyseType;
import org.guce.siat.common.model.Company;
import org.guce.siat.common.model.Country;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.FileTypeStep;
import org.guce.siat.common.model.Port;
import org.guce.siat.common.model.ServicesItem;
import org.guce.siat.common.model.Step;
import org.guce.siat.common.model.TransportType;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.AnalyseTypeService;
import org.guce.siat.common.service.CompanyService;
import org.guce.siat.common.service.CountryService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FileService;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.common.service.FileTypeStepService;
import org.guce.siat.common.service.PortService;
import org.guce.siat.common.service.ServicesItemService;
import org.guce.siat.common.service.StepService;
import org.guce.siat.common.service.TransportTypeService;
import org.guce.siat.common.service.UserAuthorityFileTypeService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.AuthorityConstants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.core.ct.filter.FileItemFilter;
import org.guce.siat.core.ct.model.Laboratory;
import org.guce.siat.core.ct.service.CommonService;
import org.guce.siat.core.ct.service.LaboratoryService;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.SearchCriteria;
import org.guce.siat.web.ct.controller.util.enums.ServiceItemType;
import org.guce.siat.web.ct.data.StepDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SearchController.
 */
@ManagedBean(name = "searchController")
@SessionScoped
public class SearchController extends AbstractController<FileItem> {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -3905783767082615980L;

	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

	/**
	 * The Constant LOCAL_BUNDLE_NAME.
	 */
	private static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";

	/**
	 * The Constant DATE_VALIDATION_ERROR_MESSAGE.
	 */
	private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

	/**
	 * The Constant QUICK_SEARCH_VALIDATION_ERROR_MESSAGE.
	 */
	private static final String QUICK_SEARCH_VALIDATION_ERROR_MESSAGE = "QuickSearchValidationError";

	/**
	 * The Constant INVALID_DOC_NUMBER_ERROR_MESSAGE.
	 */
	private static final String INVALID_DOC_NUMBER_ERROR_MESSAGE = "InvalidDocumentNumberError";

	/**
	 * The Constant DOCUMENT_ACCESS_DENIED_ERROR.
	 */
	private static final String DOCUMENT_ACCESS_DENIED_ERROR = "DocumentAccessDeniedError";

	/**
	 * The step service.
	 */
	@ManagedProperty(value = "#{stepService}")
	private StepService stepService;

	/**
	 * The file item service.
	 */
	@ManagedProperty(value = "#{fileItemService}")
	private FileItemService fileItemService;

	/**
	 * The file service.
	 */
	@ManagedProperty(value = "#{fileService}")
	private FileService fileService;

	/**
	 * The transport type service.
	 */
	@ManagedProperty(value = "#{transportTypeService}")
	private TransportTypeService transportTypeService;

	/**
	 * The country service.
	 */
	@ManagedProperty(value = "#{countryService}")
	private CountryService countryService;

	/**
	 * The port service.
	 */
	@ManagedProperty(value = "#{portService}")
	private PortService portService;

	/**
	 * The company service.
	 */
	@ManagedProperty(value = "#{companyService}")
	private CompanyService companyService;

	/**
	 * The services item service.
	 */
	@ManagedProperty(value = "#{servicesItemService}")
	private ServicesItemService servicesItemService;

	/**
	 * The user authority file type service.
	 */
	@ManagedProperty(value = "#{userAuthorityFileTypeService}")
	private UserAuthorityFileTypeService userAuthorityFileTypeService;

	/**
	 * The file type service.
	 */
	@ManagedProperty(value = "#{fileTypeService}")
	private FileTypeService fileTypeService;

	/**
	 * The user service.
	 */
	@ManagedProperty(value = "#{userService}")
	private UserService userService;

	/**
	 * The analyse type service.
	 */
	@ManagedProperty(value = "#{analyseTypeService}")
	private AnalyseTypeService analyseTypeService;

	/**
	 * The common service.
	 */
	@ManagedProperty(value = "#{commonService}")
	private CommonService commonService;

	/**
	 * The file type step service.
	 */
	@ManagedProperty(value = "#{fileTypeStepService}")
	private FileTypeStepService fileTypeStepService;

	/**
	 * The filter.
	 */
	private FileItemFilter filter;

	/**
	 * The detail page url.
	 */
	private String detailPageUrl;

	/**
	 * The document number filter.
	 */
	private String documentNumberFilter;

	/**
	 * The search criteria.
	 */
	private String searchCriteria;

	/**
	 * The is importer.
	 */
	private Boolean isOperator;

	/**
	 * The is article.
	 */
	private Boolean isArticle;

	/**
	 * The is country.
	 */
	private Boolean isCountry;

	/**
	 * The is port.
	 */
	private Boolean isPort;

	/**
	 * The is controller.
	 */
	private Boolean isController;

	/**
	 * The is analyse type.
	 */
	private Boolean isAnalyseType;

	/**
	 * The is file.
	 */
	private Boolean isFile;

	/**
	 * The is trans type.
	 */
	private Boolean isTransType;

	/**
	 * The is file type.
	 */
	private Boolean isFileType;

	/**
	 * The simple search item list.
	 */
	private List<FileItem> simpleSearchItemList;

	/**
	 * The search criteria list.
	 */
	private List<String> searchCriteriaList;

	/**
	 * The transport type list.
	 */
	private List<TransportType> transportTypeList;

	/**
	 * The step dto list.
	 */
	private List<StepDto> stepDtoList;

	/**
	 * The countries.
	 */
	private List<Country> countries;

	/**
	 * The arrival ports.
	 */
	private List<Port> arrivalPorts;

	/**
	 * The controllers.
	 */
	private List<User> controllers;

	/**
	 * The analyse types.
	 */
	private List<AnalyseType> analyseTypes;

	/**
	 * The operator list.
	 */
	private List<Company> operatorList;

	/**
	 * The nsh list.
	 */
	private List<String> nshList;

	/**
	 * The file type items.
	 */
	private List<SelectItem> fileTypeItems;

	/**
	 * The is laboratory.
	 */
	private Boolean isLaboratory;

	/**
	 * The laboratories.
	 */
	private List<Laboratory> laboratories;

	/**
	 * The laboratory service.
	 */
	@ManagedProperty(value = "#{laboratoryService}")
	private LaboratoryService laboratoryService;

	/**
	 * The selected step filter.
	 */
	private StepDto selectedStepFilter;

	/**
	 * Instantiates a new search controller.
	 */
	public SearchController() {
		super(FileItem.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		if (LOG.isDebugEnabled()) {
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, SearchController.class.getName());
		}
		documentNumberFilter = null;
		filter = new FileItemFilter();
		selectedStepFilter = null;
		initSimpleSearch();
		initAdvancedSearch();
	}

	/**
	 * Inits the simple search.
	 */
	public void initSimpleSearch() {

		simpleSearchItemList = new ArrayList<FileItem>();
		fileTypeItems = new ArrayList<SelectItem>();
		final List<FileType> fileTypes = fileTypeService.findDistinctFileTypesByUser(getLoggedUser());

		for (final FileType fileType : fileTypes) {
			fileTypeItems.add(new SelectItem(fileType, "fr".equals(getCurrentLocaleCode()) ? fileType.getLabelFr() : fileType
					.getLabelEn()));
		}

	}

	/**
	 * Inits the advanced search.
	 */
	public void initAdvancedSearch() {
		isOperator = false;
		isArticle = false;
		isCountry = false;
		isPort = false;
		isFile = false;
		isTransType = false;
		isController = false;
		isAnalyseType = false;
		isLaboratory = false;
		searchCriteria = null;
		transportTypeList = transportTypeService.findAll();
		countries = countryService.findAll();
		arrivalPorts = portService.findAll();
		operatorList = companyService.findOperator();
		controllers = userService.findUsersByAdministrationAndAuthorities(getCurrentOrganism() != null ? getCurrentOrganism()
				: getCurrentMinistry(), AuthorityConstants.CONTROLEUR.getCode());
		analyseTypes = analyseTypeService.findByAdministration(getCurrentOrganism() != null ? getCurrentOrganism()
				: getCurrentAdministration());
		laboratories = laboratoryService.findByAdministration(getCurrentOrganism() != null ? getCurrentOrganism()
				: getCurrentAdministration());
	}

	/**
	 * Go to simple search page.
	 */
	public void goToSimpleSearchPage() {
		try {
			init();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.SIMPLE_SEARCH_INDEX_PAGE));
			extContext.redirect(url);
		} catch (final IOException ioe) {
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Go to advanced search page.
	 */
	public void goToAdvancedSearchPage() {
		try {
			init();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, ControllerConstants.Pages.FO.ADVANCED_SEARCH_INDEX_PAGE));
			extContext.redirect(url);
		} catch (final IOException ioe) {
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Go to detail page.
	 */
	public void goToDetailPage() {
		try {

			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();

			final FileItem fileItem = fileItemService.find(selected.getId());
			final List<FileTypeCode> cctCodes = Arrays.asList(FileTypeCode.CCT_CT, FileTypeCode.CCT_CT_E, FileTypeCode.CC_CT, FileTypeCode.CQ_CT);

			if (cctCodes.contains(fileItem.getFile().getFileType().getCode())) {
				setDetailPageUrl(ControllerConstants.Pages.FO.DETAILS_CCT_INDEX_PAGE);

				final FileItemCctDetailController fileItemCctDetailController = getInstanceOfPageFileItemCctDetailController();
				fileItemCctDetailController.setCurrentFileItem(fileItem);
				fileItemCctDetailController.setComeFromSearch(Boolean.TRUE);
				fileItemCctDetailController.init();
			} else if (FileTypeCode.FIMEX.equals(fileItem.getFile().getFileType().getCode())) {
				setDetailPageUrl(ControllerConstants.Pages.FO.DETAILS_FIMEX_INDEX_PAGE);

				final FimexDetailController fimexDetailController = getInstanceOfPageFimexDetailController();
				fimexDetailController.setCurrentFile(fileItem.getFile());
				fimexDetailController.init();
			} else {
				setDetailPageUrl(ControllerConstants.Pages.FO.DETAILS_AP_INDEX_PAGE);

				final FileItemApDetailController fileItemApDetailController = getInstanceOfPageFileItemApDetailController();
				fileItemApDetailController.setCurrentFile(fileItem.getFile());
				fileItemApDetailController.setComeFromSearch(Boolean.TRUE);
				fileItemApDetailController.setComeFromRetrieveAp(Boolean.FALSE);
				fileItemApDetailController.init();
			}

			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, detailPageUrl));
			extContext.redirect(url);
		} catch (final IOException ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Gets the instance of page file item detail controller.
	 *
	 * @return the instance of page file item detail controller
	 */
	public FileItemCctDetailController getInstanceOfPageFileItemCctDetailController() {
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
	public FileItemApDetailController getInstanceOfPageFileItemApDetailController() {
		final FacesContext fctx = FacesContext.getCurrentInstance();
		final Application application = fctx.getApplication();
		final ELContext context = fctx.getELContext();
		final ExpressionFactory expressionFactory = application.getExpressionFactory();
		final ValueExpression createValueExpression = expressionFactory.createValueExpression(context,
				"#{fileItemApDetailController}", FileItemApDetailController.class);
		return (FileItemApDetailController) createValueExpression.getValue(context);
	}

	/**
	 * Gets the instance of page fimex detail controller.
	 *
	 * @return the instance of page fimex detail controller
	 */
	private FimexDetailController getInstanceOfPageFimexDetailController() {
		final FacesContext fctx = FacesContext.getCurrentInstance();
		final Application application = fctx.getApplication();
		final ELContext context = fctx.getELContext();
		final ExpressionFactory expressionFactory = application.getExpressionFactory();
		final ValueExpression createValueExpression = expressionFactory.createValueExpression(context, "#{fimexDetailController}",
				FimexDetailController.class);
		return (FimexDetailController) createValueExpression.getValue(context);
	}

	/**
	 * Do simple search.
	 */
	public void doSearchByFilter() {
		if (filter.getFromDate() != null && filter.getToDate() != null && filter.getFromDate().after(filter.getToDate())) {
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
		} else if (selectedStepFilter != null) {
			filter.setStep(selectedStepFilter.getStep());
		}
		setSimpleSearchItemList(commonService.findByFilter(filter, getLoggedUser(), getCurrentAdministration()));

		if (CollectionUtils.isNotEmpty(simpleSearchItemList)) {
			for (final FileItem fileItem : simpleSearchItemList) {

				final FileTypeStep fileTypeStep = fileTypeStepService.findFileTypeStepByFileTypeAndStep(fileItem.getFile()
						.getFileType(), fileItem.getStep());
				if (fileTypeStep != null && fileTypeStep.getLabelFr() != null) {
					fileItem.setRedefinedLabelEn((fileTypeStep.getLabelEn()));
					fileItem.setRedefinedLabelFr((fileTypeStep.getLabelFr()));
				}
			}
		}
	}

	/**
	 * Do quick search.
	 */
	public void doQuickSearch() {
		try {
			if (StringUtils.isNoneBlank(documentNumberFilter)) {
				List<FileItem> fileItems = Collections.emptyList();
				final File file = fileService.quickSearch(documentNumberFilter, getCurrentAdministration(), getLoggedUser());

				if (file != null) {
					fileItems = fileService.quickSearch(documentNumberFilter, getCurrentAdministration(), getLoggedUser())
							.getFileItemsList();
					if (CollectionUtils.isNotEmpty(fileItems)) {
						selected = fileItems.get(0);
						goToDetailPage();
					}
				} else {
					JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
							DOCUMENT_ACCESS_DENIED_ERROR));
				}

				documentNumberFilter = null;
			} else {
				JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						QUICK_SEARCH_VALIDATION_ERROR_MESSAGE));
			}
		} catch (final NumberFormatException nfe) {
			documentNumberFilter = null;
			LOG.error("number format exeception", nfe);
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					INVALID_DOC_NUMBER_ERROR_MESSAGE));
		}

	}

	/**
	 * Change search criteria handler.
	 */
	public void changeSearchCriteriaHandler() {
		filter = new FileItemFilter();
		selectedStepFilter = null;
		isOperator = false;
		isArticle = false;
		isCountry = false;
		isPort = false;
		isFile = false;
		isController = false;
		isAnalyseType = false;
		isLaboratory = false;
		searchCriteriaArticle();
		searchCriteriaImporter();
		searchCriteriaCoutry();
		searchCriteriaPort();
		searchCriteriaTransportType();
		searchCriteriaDocNumber();
		searchCriteriaController();
		searchCriteriaAnalyseType();
		searchCriteriaLaboratory();
	}

	/**
	 * Search criteria article.
	 */
	public void searchCriteriaArticle() {
		if (SearchCriteria.ARTICLE.getCodeFr().equals(searchCriteria) || SearchCriteria.ARTICLE.getCodeEn().equals(searchCriteria)) {
			isOperator = false;
			isArticle = true;
			isCountry = false;
			isPort = false;
			isFile = false;
			isTransType = false;
			isController = false;
			isAnalyseType = false;
			isLaboratory = false;

		}
	}

	/**
	 * Search criteria importer.
	 */
	public void searchCriteriaImporter() {
		if (SearchCriteria.OPERATEUR.getCodeFr().equals(searchCriteria)
				|| SearchCriteria.OPERATEUR.getCodeEn().equals(searchCriteria)) {
			isOperator = true;
			isArticle = false;
			isCountry = false;
			isPort = false;
			isFile = false;
			isTransType = false;
			isController = false;
			isAnalyseType = false;
			isLaboratory = false;
		}
	}

	/**
	 * Search criteria coutry.
	 */
	public void searchCriteriaCoutry() {

		if (SearchCriteria.ORIGIN_COUNTRY.getCodeFr().equals(searchCriteria)
				|| SearchCriteria.ORIGIN_COUNTRY.getCodeEn().equals(searchCriteria)) {

			isOperator = false;
			isArticle = false;
			isCountry = true;
			isPort = false;
			isFile = false;
			isTransType = false;
			isController = false;
			isAnalyseType = false;
			isLaboratory = false;
		}
	}

	/**
	 * Search criteria port.
	 */
	public void searchCriteriaPort() {
		if (SearchCriteria.ARRIVAL_PORT.getCodeFr().equals(searchCriteria)
				|| SearchCriteria.ARRIVAL_PORT.getCodeEn().equals(searchCriteria)) {
			isOperator = false;
			isArticle = false;
			isCountry = false;
			isPort = true;
			isFile = false;
			isTransType = false;
			isController = false;
			isAnalyseType = false;
			isLaboratory = false;
		}
	}

	/**
	 * Search criteria transport type.
	 */
	public void searchCriteriaTransportType() {
		if (SearchCriteria.TRANSPORT_TYPE.getCodeFr().equals(searchCriteria)
				|| SearchCriteria.TRANSPORT_TYPE.getCodeEn().equals(searchCriteria)) {
			isOperator = false;
			isArticle = false;
			isCountry = false;
			isPort = false;
			isFile = false;
			isTransType = true;
			isController = false;
			isAnalyseType = false;
			isLaboratory = false;
		}
	}

	/**
	 * Search criteria doc number.
	 */
	public void searchCriteriaDocNumber() {
		if (SearchCriteria.DOCUMENT_NUMBER.getCodeFr().equals(searchCriteria)
				|| SearchCriteria.DOCUMENT_NUMBER.getCodeEn().equals(searchCriteria)) {
			isOperator = false;
			isArticle = false;
			isCountry = false;
			isPort = false;
			isFile = true;
			isTransType = false;
			isController = false;
			isAnalyseType = false;
			isLaboratory = false;
		}
	}

	/**
	 * Search criteria controller.
	 */
	public void searchCriteriaController() {
		if (SearchCriteria.CONTROLLER.getCodeFr().equals(searchCriteria)
				|| SearchCriteria.CONTROLLER.getCodeEn().equals(searchCriteria)) {
			isOperator = false;
			isArticle = false;
			isCountry = false;
			isPort = false;
			isFile = false;
			isTransType = false;
			isController = false;
			isController = true;
			isAnalyseType = false;
			isLaboratory = false;
		}
	}

	/**
	 * Search criteria analyse type.
	 */
	public void searchCriteriaAnalyseType() {
		if (SearchCriteria.ANALYSE_TYPE.getCodeFr().equals(searchCriteria)
				|| SearchCriteria.ANALYSE_TYPE.getCodeEn().equals(searchCriteria)) {
			isOperator = false;
			isArticle = false;
			isCountry = false;
			isPort = false;
			isFile = false;
			isTransType = false;
			isController = false;
			isController = false;
			isAnalyseType = true;
			isLaboratory = false;
		}
	}

	/**
	 * Search criteria laboratory.
	 */
	public void searchCriteriaLaboratory() {
		if (SearchCriteria.LABORATORY.getCodeFr().equals(searchCriteria)
				|| SearchCriteria.LABORATORY.getCodeEn().equals(searchCriteria)) {
			isOperator = false;
			isArticle = false;
			isCountry = false;
			isPort = false;
			isFile = false;
			isTransType = false;
			isController = false;
			isController = false;
			isAnalyseType = false;
			isLaboratory = true;
		}
	}

	/**
	 * Fetch nsh list.
	 *
	 * @param query the query
	 * @return the list
	 */
	public List<String> fetchNSHList(final String query) {

		List<ServicesItem> nativeItemsList = new ArrayList<ServicesItem>();
		nshList = new ArrayList<String>();

		nativeItemsList = servicesItemService.findServicesItemByService(getCurrentService());

		if (CollectionUtils.isNotEmpty(nativeItemsList)) {
			for (final ServicesItem item : nativeItemsList) {
				if ((ServiceItemType.NATIVE.getCode().equals(item.getType().toString())) && (!item.getDeleted())
						&& (item.getNsh().getGoodsItemCode().startsWith(query))) {
					nshList.add(item.getNsh().getGoodsItemCode());
				}
			}
		}
		return nshList;
	}

	/**
	 * On file type changed.
	 */
	public void onFileTypeChanged() {
		if (filter.getFileType() != null) {
			selectedStepFilter = null;
			final List<Step> stepList = filter.getFileType().getStepList();
			stepDtoList = new ArrayList<StepDto>();
			if (CollectionUtils.isNotEmpty(stepList)) {
				for (final Step step : stepList) {
					final StepDto stepDto = new StepDto();
					stepDto.setStep(step);

					FileTypeStep fileTypeStep = null;
					fileTypeStep = fileTypeStepService.findFileTypeStepByFileTypeAndStep(filter.getFileType(), step);
					if (fileTypeStep != null && fileTypeStep.getLabelFr() != null) {
						stepDto.setRedefinedLabelEn((fileTypeStep.getLabelEn()));
						stepDto.setRedefinedLabelFr((fileTypeStep.getLabelFr()));
					}
					stepDtoList.add(stepDto);
				}
			}
		}
	}

	/**
	 * Gets the step service.
	 *
	 * @return the stepService
	 */
	public StepService getStepService() {
		return stepService;
	}

	/**
	 * Sets the step service.
	 *
	 * @param stepService the stepService to set
	 */
	public void setStepService(final StepService stepService) {
		this.stepService = stepService;
	}

	/**
	 * Gets the filter.
	 *
	 * @return the filter
	 */
	public FileItemFilter getFilter() {
		return filter;
	}

	/**
	 * Sets the filter.
	 *
	 * @param filter the filter to set
	 */
	public void setFilter(final FileItemFilter filter) {
		this.filter = filter;
	}

	/**
	 * Gets the simple search item list.
	 *
	 * @return the simpleSearchItemList
	 */
	public List<FileItem> getSimpleSearchItemList() {

		return simpleSearchItemList;
	}

	/**
	 * Sets the simple search item list.
	 *
	 * @param simpleSearchItemList the simpleSearchItemList to set
	 */
	public void setSimpleSearchItemList(final List<FileItem> simpleSearchItemList) {
		this.simpleSearchItemList = simpleSearchItemList;
	}

	/**
	 * Gets the file item service.
	 *
	 * @return the fileItemService
	 */
	public FileItemService getFileItemService() {
		return fileItemService;
	}

	/**
	 * Sets the file item service.
	 *
	 * @param fileItemService the fileItemService to set
	 */
	public void setFileItemService(final FileItemService fileItemService) {
		this.fileItemService = fileItemService;
	}

	/**
	 * Gets the detail page url.
	 *
	 * @return the detailPageUrl
	 */
	public String getDetailPageUrl() {
		return detailPageUrl;
	}

	/**
	 * Sets the detail page url.
	 *
	 * @param detailPageUrl the detailPageUrl to set
	 */
	public void setDetailPageUrl(final String detailPageUrl) {
		this.detailPageUrl = detailPageUrl;
	}

	/**
	 * Gets the document number filter.
	 *
	 * @return the documentNumberFilter
	 */
	public String getDocumentNumberFilter() {
		return documentNumberFilter;
	}

	/**
	 * Sets the document number filter.
	 *
	 * @param documentNumberFilter the documentNumberFilter to set
	 */
	public void setDocumentNumberFilter(final String documentNumberFilter) {
		this.documentNumberFilter = documentNumberFilter;
	}

	/**
	 * Gets the search criteria list.
	 *
	 * @return the searchCriteriaList
	 */
	public List<String> getSearchCriteriaList() {
		searchCriteriaList = new ArrayList<String>();
		for (final SearchCriteria enumSearchCriteria : SearchCriteria.values()) {
			if (Constants.LOCALE_ENGLISH.equals(getCurrentLocaleCode())) {
				searchCriteriaList.add(enumSearchCriteria.getCodeEn());
			} else {
				searchCriteriaList.add(enumSearchCriteria.getCodeFr());
			}

		}

		return searchCriteriaList;
	}

	/**
	 * Sets the search criteria list.
	 *
	 * @param searchCriteriaList the searchCriteriaList to set
	 */
	public void setSearchCriteriaList(final List<String> searchCriteriaList) {
		this.searchCriteriaList = searchCriteriaList;
	}

	/**
	 * Gets the search criteria.
	 *
	 * @return the searchCriteria
	 */
	public String getSearchCriteria() {
		return searchCriteria;
	}

	/**
	 * Sets the search criteria.
	 *
	 * @param searchCriteria the searchCriteria to set
	 */
	public void setSearchCriteria(final String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	/**
	 * Gets the checks if is operator.
	 *
	 * @return the isOperator
	 */
	public Boolean getIsOperator() {
		return isOperator;
	}

	/**
	 * Sets the checks if is operator.
	 *
	 * @param isOperator the isOperator to set
	 */
	public void setIsOperator(final Boolean isOperator) {
		this.isOperator = isOperator;
	}

	/**
	 * Gets the transport type list.
	 *
	 * @return the transportTypeList
	 */
	public List<TransportType> getTransportTypeList() {
		return transportTypeList;
	}

	/**
	 * Sets the transport type list.
	 *
	 * @param transportTypeList the transportTypeList to set
	 */
	public void setTransportTypeList(final List<TransportType> transportTypeList) {
		this.transportTypeList = transportTypeList;
	}

	/**
	 * Gets the transport type service.
	 *
	 * @return the transportTypeService
	 */
	public TransportTypeService getTransportTypeService() {
		return transportTypeService;
	}

	/**
	 * Sets the transport type service.
	 *
	 * @param transportTypeService the transportTypeService to set
	 */
	public void setTransportTypeService(final TransportTypeService transportTypeService) {
		this.transportTypeService = transportTypeService;
	}

	/**
	 * Gets the countries.
	 *
	 * @return the countries
	 */
	public List<Country> getCountries() {
		return countries;
	}

	/**
	 * Sets the countries.
	 *
	 * @param countries the countries to set
	 */
	public void setCountries(final List<Country> countries) {
		this.countries = countries;
	}

	/**
	 * Gets the country service.
	 *
	 * @return the countryService
	 */
	public CountryService getCountryService() {
		return countryService;
	}

	/**
	 * Sets the country service.
	 *
	 * @param countryService the countryService to set
	 */
	public void setCountryService(final CountryService countryService) {
		this.countryService = countryService;
	}

	/**
	 * Gets the checks if is article.
	 *
	 * @return the isArticle
	 */
	public Boolean getIsArticle() {
		return isArticle;
	}

	/**
	 * Sets the checks if is article.
	 *
	 * @param isArticle the isArticle to set
	 */
	public void setIsArticle(final Boolean isArticle) {
		this.isArticle = isArticle;
	}

	/**
	 * Gets the checks if is country.
	 *
	 * @return the isCountry
	 */
	public Boolean getIsCountry() {
		return isCountry;
	}

	/**
	 * Sets the checks if is country.
	 *
	 * @param isCountry the isCountry to set
	 */
	public void setIsCountry(final Boolean isCountry) {
		this.isCountry = isCountry;
	}

	/**
	 * Gets the checks if is port.
	 *
	 * @return the isPort
	 */
	public Boolean getIsPort() {
		return isPort;
	}

	/**
	 * Sets the checks if is port.
	 *
	 * @param isPort the isPort to set
	 */
	public void setIsPort(final Boolean isPort) {
		this.isPort = isPort;
	}

	/**
	 * Gets the checks if is file.
	 *
	 * @return the isFile
	 */
	public Boolean getIsFile() {
		return isFile;
	}

	/**
	 * Sets the checks if is file.
	 *
	 * @param isFile the isFile to set
	 */
	public void setIsFile(final Boolean isFile) {
		this.isFile = isFile;
	}

	/**
	 * Gets the checks if is trans type.
	 *
	 * @return the isTransType
	 */
	public Boolean getIsTransType() {
		return isTransType;
	}

	/**
	 * Sets the checks if is trans type.
	 *
	 * @param isTransType the isTransType to set
	 */
	public void setIsTransType(final Boolean isTransType) {
		this.isTransType = isTransType;
	}

	/**
	 * Gets the arrival ports.
	 *
	 * @return the arrivalPorts
	 */
	public List<Port> getArrivalPorts() {
		return arrivalPorts;
	}

	/**
	 * Sets the arrival ports.
	 *
	 * @param arrivalPorts the arrivalPorts to set
	 */
	public void setArrivalPorts(final List<Port> arrivalPorts) {
		this.arrivalPorts = arrivalPorts;
	}

	/**
	 * Gets the port service.
	 *
	 * @return the portService
	 */
	public PortService getPortService() {
		return portService;
	}

	/**
	 * Sets the port service.
	 *
	 * @param portService the portService to set
	 */
	public void setPortService(final PortService portService) {
		this.portService = portService;
	}

	/**
	 * Gets the company service.
	 *
	 * @return the companyService
	 */
	public CompanyService getCompanyService() {
		return companyService;
	}

	/**
	 * Sets the company service.
	 *
	 * @param companyService the companyService to set
	 */
	public void setCompanyService(final CompanyService companyService) {
		this.companyService = companyService;
	}

	/**
	 * Gets the operator list.
	 *
	 * @return the operatorList
	 */
	public List<Company> getOperatorList() {
		return operatorList;
	}

	/**
	 * Sets the operator list.
	 *
	 * @param operatorList the operatorList to set
	 */
	public void setOperatorList(final List<Company> operatorList) {
		this.operatorList = operatorList;
	}

	/**
	 * Gets the services item service.
	 *
	 * @return the servicesItemService
	 */
	public ServicesItemService getServicesItemService() {
		return servicesItemService;
	}

	/**
	 * Sets the services item service.
	 *
	 * @param servicesItemService the servicesItemService to set
	 */
	public void setServicesItemService(final ServicesItemService servicesItemService) {
		this.servicesItemService = servicesItemService;
	}

	/**
	 * Gets the nsh list.
	 *
	 * @return the nsh list
	 */
	public List<String> getNshList() {
		return nshList;
	}

	/**
	 * Sets the nsh list.
	 *
	 * @param nshList the new nsh list
	 */
	public void setNshList(final List<String> nshList) {
		this.nshList = nshList;
	}

	/**
	 * Gets the user authority file type service.
	 *
	 * @return the userAuthorityFileTypeService
	 */
	public UserAuthorityFileTypeService getUserAuthorityFileTypeService() {
		return userAuthorityFileTypeService;
	}

	/**
	 * Sets the user authority file type service.
	 *
	 * @param userAuthorityFileTypeService the userAuthorityFileTypeService to
	 * set
	 */
	public void setUserAuthorityFileTypeService(final UserAuthorityFileTypeService userAuthorityFileTypeService) {
		this.userAuthorityFileTypeService = userAuthorityFileTypeService;
	}

	/**
	 * Gets the file type service.
	 *
	 * @return the fileTypeService
	 */
	public FileTypeService getFileTypeService() {
		return fileTypeService;
	}

	/**
	 * Sets the file type service.
	 *
	 * @param fileTypeService the fileTypeService to set
	 */
	public void setFileTypeService(final FileTypeService fileTypeService) {
		this.fileTypeService = fileTypeService;
	}

	/**
	 * Gets the checks if is file type.
	 *
	 * @return the isFileType
	 */
	public Boolean getIsFileType() {
		return isFileType;
	}

	/**
	 * Sets the checks if is file type.
	 *
	 * @param isFileType the isFileType to set
	 */
	public void setIsFileType(final Boolean isFileType) {
		this.isFileType = isFileType;
	}

	/**
	 * Gets the file type items.
	 *
	 * @return the fileTypeItems
	 */
	public List<SelectItem> getFileTypeItems() {
		return fileTypeItems;
	}

	/**
	 * Sets the file type items.
	 *
	 * @param fileTypeItems the fileTypeItems to set
	 */
	public void setFileTypeItems(final List<SelectItem> fileTypeItems) {
		this.fileTypeItems = fileTypeItems;
	}

	/**
	 * Gets the file service.
	 *
	 * @return the fileService
	 */
	public FileService getFileService() {
		return fileService;
	}

	/**
	 * Sets the file service.
	 *
	 * @param fileService the fileService to set
	 */
	public void setFileService(final FileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * Gets the checks if is controller.
	 *
	 * @return the checks if is controller
	 */
	public Boolean getIsController() {
		return isController;
	}

	/**
	 * Sets the checks if is controller.
	 *
	 * @param isController the new checks if is controller
	 */
	public void setIsController(final Boolean isController) {
		this.isController = isController;
	}

	/**
	 * Gets the checks if is analyse type.
	 *
	 * @return the checks if is analyse type
	 */
	public Boolean getIsAnalyseType() {
		return isAnalyseType;
	}

	/**
	 * Sets the checks if is analyse type.
	 *
	 * @param isAnalyseType the new checks if is analyse type
	 */
	public void setIsAnalyseType(final Boolean isAnalyseType) {
		this.isAnalyseType = isAnalyseType;
	}

	/**
	 * Gets the controllers.
	 *
	 * @return the controllers
	 */
	public List<User> getControllers() {
		return controllers;
	}

	/**
	 * Sets the controllers.
	 *
	 * @param controllers the new controllers
	 */
	public void setControllers(final List<User> controllers) {
		this.controllers = controllers;
	}

	/**
	 * Gets the analyse types.
	 *
	 * @return the analyse types
	 */
	public List<AnalyseType> getAnalyseTypes() {
		return analyseTypes;
	}

	/**
	 * Sets the analyse types.
	 *
	 * @param analyseTypes the new analyse types
	 */
	public void setAnalyseTypes(final List<AnalyseType> analyseTypes) {
		this.analyseTypes = analyseTypes;
	}

	/**
	 * Gets the user service.
	 *
	 * @return the user service
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user service.
	 *
	 * @param userService the new user service
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	/**
	 * Gets the analyse type service.
	 *
	 * @return the analyse type service
	 */
	public AnalyseTypeService getAnalyseTypeService() {
		return analyseTypeService;
	}

	/**
	 * Sets the analyse type service.
	 *
	 * @param analyseTypeService the new analyse type service
	 */
	public void setAnalyseTypeService(final AnalyseTypeService analyseTypeService) {
		this.analyseTypeService = analyseTypeService;
	}

	/**
	 * Gets the checks if is laboratory.
	 *
	 * @return the checks if is laboratory
	 */
	public Boolean getIsLaboratory() {
		return isLaboratory;
	}

	/**
	 * Sets the checks if is laboratory.
	 *
	 * @param isLaboratory the new checks if is laboratory
	 */
	public void setIsLaboratory(final Boolean isLaboratory) {
		this.isLaboratory = isLaboratory;
	}

	/**
	 * Gets the laboratories.
	 *
	 * @return the laboratories
	 */
	public List<Laboratory> getLaboratories() {
		return laboratories;
	}

	/**
	 * Sets the laboratories.
	 *
	 * @param laboratories the new laboratories
	 */
	public void setLaboratories(final List<Laboratory> laboratories) {
		this.laboratories = laboratories;
	}

	/**
	 * Gets the common service.
	 *
	 * @return the common service
	 */
	public CommonService getCommonService() {
		return commonService;
	}

	/**
	 * Sets the common service.
	 *
	 * @param commonService the new common service
	 */
	public void setCommonService(final CommonService commonService) {
		this.commonService = commonService;
	}

	/**
	 * Gets the laboratory service.
	 *
	 * @return the laboratory service
	 */
	public LaboratoryService getLaboratoryService() {
		return laboratoryService;
	}

	/**
	 * Sets the laboratory service.
	 *
	 * @param laboratoryService the new laboratory service
	 */
	public void setLaboratoryService(final LaboratoryService laboratoryService) {
		this.laboratoryService = laboratoryService;
	}

	/**
	 * Gets the file type step service.
	 *
	 * @return the file type step service
	 */
	public FileTypeStepService getFileTypeStepService() {
		return fileTypeStepService;
	}

	/**
	 * Sets the file type step service.
	 *
	 * @param fileTypeStepService the new file type step service
	 */
	public void setFileTypeStepService(final FileTypeStepService fileTypeStepService) {
		this.fileTypeStepService = fileTypeStepService;
	}

	/**
	 * Gets the step dto list.
	 *
	 * @return the step dto list
	 */
	public List<StepDto> getStepDtoList() {
		return stepDtoList;
	}

	/**
	 * Sets the step dto list.
	 *
	 * @param stepDtoList the new step dto list
	 */
	public void setStepDtoList(final List<StepDto> stepDtoList) {
		this.stepDtoList = stepDtoList;
	}

	/**
	 * Gets the selected step filter.
	 *
	 * @return the selected step filter
	 */
	public StepDto getSelectedStepFilter() {
		return selectedStepFilter;
	}

	/**
	 * Sets the selected step filter.
	 *
	 * @param selectedStepFilter the new selected step filter
	 */
	public void setSelectedStepFilter(final StepDto selectedStepFilter) {
		this.selectedStepFilter = selectedStepFilter;
	}

}
