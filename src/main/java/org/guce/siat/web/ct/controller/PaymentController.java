package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Attachment;
import org.guce.siat.common.model.Company;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.User;
import org.guce.siat.common.model.UserAuthorityFileType;
import org.guce.siat.common.service.ApplicationPropretiesService;
import org.guce.siat.common.service.AuthorityService;
import org.guce.siat.common.service.CompanyService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FileService;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.common.service.ItemFlowService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.UserAuthorityFileTypeService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.common.utils.PropertiesConstants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.core.ct.filter.PaymentFilter;
import org.guce.siat.core.ct.model.PaymentData;
import org.guce.siat.core.ct.service.CommonService;
import org.guce.siat.core.ct.service.PaymentDataService;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.data.FileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PaymentController.
 */
@ManagedBean(name = "paymentController")
@ViewScoped
public class PaymentController extends AbstractController<File> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 212767340072397687L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);

    /**
     * The Constant DATE_VALIDATION_ERROR_MESSAGE.
     */
    private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

    /**
     * The costs page url.
     */
    private String costsPageUrl;

    /**
     * The list user authority file types.
     */
    private List<UserAuthorityFileType> listUserAuthorityFileTypes;

    /**
     * The files list.
     */
    private List<File> filesList;

    /**
     * The file Dto list.
     */
    private List<FileDto> fileDtoList;

    /**
     * The selected file.
     */
    private FileDto selectedFile;

    /**
     * The filter.
     */
    private PaymentFilter filter;

    /**
     * The operator list.
     */
    private List<Company> operatorList;

    /**
     * The file type items.
     */
    private List<SelectItem> fileTypeItems;

    /**
     * The file item service.
     */
    @ManagedProperty(value = "#{fileItemService}")
    private FileItemService fileItemService;
    
    /**
     * The file file service.
     */
    @ManagedProperty(value = "#{fileService}")
    private FileService fileService;

    /**
     * The common service.
     */
    @ManagedProperty(value = "#{commonService}")
    private CommonService commonService;

    /**
     * The authority service.
     */
    @ManagedProperty(value = "#{authorityService}")
    private AuthorityService authorityService;

    /**
     * The user authority file type service.
     */
    @ManagedProperty(value = "#{userAuthorityFileTypeService}")
    private UserAuthorityFileTypeService userAuthorityFileTypeService;

    /**
     * The service service.
     */
    @ManagedProperty(value = "#{serviceService}")
    private ServiceService serviceService;

    /**
     * The file type service.
     */
    @ManagedProperty(value = "#{fileTypeService}")
    private FileTypeService fileTypeService;

    /**
     * The company service.
     */
    @ManagedProperty(value = "#{companyService}")
    private CompanyService companyService;

    /**
     * The payment data service.
     */
    @ManagedProperty(value = "#{paymentDataService}")
    private PaymentDataService paymentDataService;

    @ManagedProperty(value = "#{itemFlowService}")
    private ItemFlowService itemFlowService;

    @ManagedProperty(value = "#{applicationPropretiesService}")
    private ApplicationPropretiesService applicationPropretiesService;

    /**
     * The Constant PREFIXE_FACTURE.
     */
    public static final String PREFIXE_FACTURE = "FAC-";

    /**
     * The Constant PREFIXE_RECU.
     */
    public static final String PREFIXE_RECU = "REC-";

    /**
     * The Constant PREFIXE_QUITTANCE.
     */
    public static final String PREFIXE_QUITTANCE = "QUIT-";

    public static final String NATURE_FRAIS_VT = "Frais visa technique";

    public static final String NATURE_FRAIS_CP = "Frais consentement préalable";

    private boolean initializingPage = true;

    /**
     * Instantiates a new payment controller.
     */
    public PaymentController() {
        super(File.class);
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        LOG.info(Constants.INIT_LOG_INFO_MESSAGE, PaymentController.class.getName());
        super.setService(fileService);
        super.setPageUrl(ControllerConstants.Pages.FO.PAYMENT_INDEX_PAGE);
        operatorList = companyService.findOperator();
        populateFileTypeItems();
        initPaymentSearch();
    }

    /**
     * Populate file type items.
     */
    private void populateFileTypeItems() {
        fileTypeItems = new ArrayList<>();
        final List<FileType> fileTypes = fileTypeService.findDistinctFileTypesByUser(getLoggedUser());
        for (final FileType fileType : fileTypes) {
            fileTypeItems.add(new SelectItem(fileType, "fr".equals(getCurrentLocaleCode()) ? fileType.getLabelFr() : fileType.getLabelEn()));
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#goToPage()
     */
    @Override
    public void goToPage() {
        initPaymentSearch();
        try {
            final FacesContext context = FacesContext.getCurrentInstance();
            final ExternalContext extContext = context.getExternalContext();
            final String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, pageUrl));
            extContext.redirect(url);
        } catch (final IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
        }
    }

    /**
     * Go to detail page.
     */
    public void goToCostsPage() {
        try {
            final FileItem selectedFileItem = selectedFile.getFile().getFileItemsList().get(0);
            ItemFlow lastItemFlow = itemFlowService.findLastSentItemFlowByFileItem(selectedFileItem);
            PaymentData paymentData = null;
            if (isPhyto(selectedFile.getFile()) || FileTypeCode.CCT_CSV.equals(selectedFile.getFile().getFileType().getCode())) {
                paymentData = paymentDataService.findPaymentDataByFileItem(selectedFileItem);
            } else if (isCcsMinsante(selectedFile.getFile())) {
                paymentData = getPaymentDataByFileAndPaymentFlow(selectedFile.getFile(), FlowCode.FL_CT_158);
            } else if (isVtMinepia(selectedFile.getFile())) {
                paymentData = getPaymentDataByFileAndPaymentFlow(selectedFile.getFile(), FlowCode.FL_AP_VT1_01);
            } else {
                paymentData = paymentDataService.findPaymentDataByItemFlow(lastItemFlow);
            }

            if (paymentData != null) {
                if (StringUtils.isBlank(paymentData.getRefFacture())) {
                    paymentData.setRefFacture(new DecimalFormat(PREFIXE_FACTURE + "SIAT-000000").format(paymentData.getId()));
                }

                if (StringUtils.isBlank(paymentData.getNumRecu())) {
                    paymentData.setNumRecu(new DecimalFormat(PREFIXE_RECU + "SIAT-000000").format(paymentData.getId()));
                }

                if (StringUtils.isBlank(paymentData.getNumQuittance())) {
                    paymentData.setNumQuittance(new DecimalFormat(PREFIXE_QUITTANCE + "SIAT-000000").format(paymentData.getId()));
                }

                User user = getLoggedUser();
                paymentData.setNomSignature(user.getFirstName() + " " + user.getLastName());
                paymentData.setDateSignature(java.util.Calendar.getInstance().getTime());
                paymentData.setDateEncaissement(java.util.Calendar.getInstance().getTime());
                paymentData.setQualiteSignature(user.getPosition().getLabelFr());
                paymentData.setLieuSignature(user.getAdministration().getLabelFr());
                Long amount = paymentData.getMontantHt() + paymentData.getMontantTva();
                if (paymentData.getMontantEncaissement() == null || paymentData.getMontantEncaissement() == 0) {
                    paymentData.setMontantEncaissement(Double.parseDouble(amount.toString()));
                }

                if (selectedFile.getFile().getClient() != null) {
                    paymentData.setPartVersCont(selectedFile.getFile().getClient().getNumContribuable());
                    paymentData.setPartVersRs(selectedFile.getFile().getClient().getCompanyName());
                }

                if (isPhyto(selectedFile.getFile()) || FileTypeCode.CCT_CSV.equals(selectedFile.getFile().getFileType().getCode())) {
                    paymentData.setNatureEncaissement("Frais ".concat(selectedFile.getFile().getFileType().getLabelFr()));
                } else {
                    paymentData.setNatureEncaissement(FileTypeCode.VT_MINEPDED.equals(selectedFile.getFile().getFileType().getCode()) ? NATURE_FRAIS_VT : NATURE_FRAIS_CP);
                }
            }
            setCostsPageUrl(ControllerConstants.Pages.FO.COSTS_INDEX_PAGE);
            final FacesContext context = FacesContext.getCurrentInstance();
            final ExternalContext extContext = context.getExternalContext();
            final CostsController costsController = getInstanceOfPageCostsController();
            costsController.setCurrentPaymentData(paymentData);
            costsController.setCurrentFile(selectedFile.getFile());
            List<Attachment> attachmentList = selectedFile.getFile().getAttachmentsList();
            costsController.setAttachmentList(attachmentList);

            final String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, costsPageUrl));

            extContext.redirect(url);
        } catch (final IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Gets the instance of page file item ap detail controller.
     *
     * @return the instance of page file item ap detail controller
     */
    public CostsController getInstanceOfPageCostsController() {
        final FacesContext fctx = FacesContext.getCurrentInstance();
        final Application application = fctx.getApplication();
        final ELContext context = fctx.getELContext();
        final ExpressionFactory expressionFactory = application.getExpressionFactory();
        final ValueExpression createValueExpression = expressionFactory.createValueExpression(context, "#{costsController}", CostsController.class);
        return (CostsController) createValueExpression.getValue(context);
    }

    /**
     * Gets the files list.
     *
     * @return the filesList
     */
    public List<File> getFilesList() {
//        final List<FileItem> fileItems = items;
//        filesList = new ArrayList<>();
//        for (final FileItem fileItem : fileItems) {
//            if (!filesList.contains(fileItem.getFile())) {
//                filesList.add(fileItem.getFile());
//            }
//        }
        
        return items;
    }

    /**
     * Gets the file dto list.
     *
     * @return the fileDtoList
     */
    public List<FileDto> getFileDtoList() {

        final List<File> paymentFileList = getFilesList();
        fileDtoList = new ArrayList<>();
        Long amount;
        for (final File paymentFile : paymentFileList) {
            final FileDto fileDto = new FileDto();
            PaymentData paymentData;
            fileDto.setFile(paymentFile);

            if (isPhyto(paymentFile) || FileTypeCode.CCT_CSV.equals(paymentFile.getFileType().getCode())) {
                paymentData = paymentDataService.findPaymentDataByFileItem(paymentFile.getFileItemsList().get(0));
            } else if (isCcsMinsante(paymentFile)) {
                paymentData = getPaymentDataByFileAndPaymentFlow(paymentFile, FlowCode.FL_CT_158);
            } else if (isVtMinepia(paymentFile)) {
                paymentData = getPaymentDataByFileAndPaymentFlow(paymentFile, FlowCode.FL_AP_VT1_01);
            } else {
                final ItemFlow lastItemFlow = itemFlowService.findLastSentItemFlowByFileItem(paymentFile.getFileItemsList().get(0));
                paymentData = paymentDataService.findPaymentDataByItemFlow(lastItemFlow);
            }

            if (paymentData != null) {
                amount = (paymentData.getMontantEncaissement() != null && paymentData.getMontantEncaissement() > 0) ? paymentData.getMontantEncaissement().longValue() : paymentData.getMontantHt();
                fileDto.setAmount(amount);
                fileDto.setKind(paymentData.getNatureFrais());
                fileDtoList.add(fileDto);
            }

        }

        return fileDtoList;
    }

    /**
     * Do search by filter.
     */
    public void doSearchByFilter() {
        try {
            if (filter.getFromDate() == null && filter.getToDate() == null && StringUtils.isEmpty(filter.getFileNumber()) && filter.getOperator() == null) {
                Date toDate = new Date();
                Date fromDate = new Date();
                String filterIntervalTime = applicationPropretiesService.getPropertiesLoader().getProperty(PropertiesConstants.LISTING_PAYMENT_FILES_INTERVAL_TIME);
                String filterIntervalTimeNumberStr = applicationPropretiesService.getPropertiesLoader().getProperty(PropertiesConstants.LISTING_PAYMENT_FILES_INTERVAL_TIME_NUMBER);
                filterIntervalTime = StringUtils.isNotBlank(filterIntervalTime) ? filterIntervalTime : "MONTH";
                int filterIntervalTimeNumber = StringUtils.isNotBlank(filterIntervalTimeNumberStr) ? Integer.parseInt(filterIntervalTimeNumberStr) : 1;
                if (null != filterIntervalTime) {
                    switch (filterIntervalTime) {
                        case "YEAR":
                            fromDate = DateUtils.addDays(toDate, -365 * filterIntervalTimeNumber);
                        case "MONTH":
                            fromDate = DateUtils.addDays(toDate, -30 * filterIntervalTimeNumber);
                            break;
                        case "WEEK":
                            fromDate = DateUtils.addDays(toDate, -7 * filterIntervalTimeNumber);
                            break;
                        case "DAY":
                            fromDate = DateUtils.addDays(toDate, -1 * filterIntervalTimeNumber);
                            break;
                        default:
                            break;
                    }
                }
                filter.setFromDate(fromDate);
                filter.setToDate(toDate);
            } else {
                initializingPage = false;
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        if (filter.getFromDate() != null && filter.getToDate() != null && filter.getFromDate().after(filter.getToDate())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(DATE_VALIDATION_ERROR_MESSAGE));
        } else {
            try {
                listUserAuthorityFileTypes = userAuthorityFileTypeService.findUserAuthorityFileTypeByUserList(getLoggedUser().getMergedDelegatorList());

                // Merge the logged user and their delegator users list in the list
                final List<Administration> adminList = new ArrayList<>();

                if (getLoggedUser().getMergedDelegatorList() != null) {
                    for (final User user : getLoggedUser().getMergedDelegatorList()) {
                        adminList.add(user.getAdministration());
                    }
                }

                items = commonService.findByFilter2(filter, getLoggedUser(), getCurrentAdministration());
            } catch (final Exception ex) {
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
            }
        }
        if (initializingPage) {
            filter.setFromDate(null);
            filter.setToDate(null);
        }

    }

    /**
     * Inits the payment search.
     */
    public void initPaymentSearch() {
        filter = new PaymentFilter();
        doSearchByFilter();
    }

    /**
     * Gets the costs page url.
     *
     * @return the costsPageUrl
     */
    public String getCostsPageUrl() {
        return costsPageUrl;
    }

    /**
     * Sets the costs page url.
     *
     * @param costsPageUrl the costsPageUrl to set
     */
    public void setCostsPageUrl(final String costsPageUrl) {
        this.costsPageUrl = costsPageUrl;
    }

    /**
     * Gets the file item service.
     *
     * @return the file item service
     */
    public FileItemService getFileItemService() {
        return fileItemService;
    }

    /**
     * Sets the file item service.
     *
     * @param fileItemService the new file item service
     */
    public void setFileItemService(final FileItemService fileItemService) {
        this.fileItemService = fileItemService;
    }

    /**
     * Gets the authority service.
     *
     * @return the authorityService
     */
    public AuthorityService getAuthorityService() {
        return authorityService;
    }

    /**
     * Sets the authority service.
     *
     * @param authorityService the authorityService to set
     */
    public void setAuthorityService(final AuthorityService authorityService) {
        this.authorityService = authorityService;
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
     * Gets the list user authority file types.
     *
     * @return the listUserAuthorityFileTypes
     */
    public List<UserAuthorityFileType> getListUserAuthorityFileTypes() {
        return listUserAuthorityFileTypes;
    }

    /**
     * Sets the list user authority file types.
     *
     * @param listUserAuthorityFileTypes the listUserAuthorityFileTypes to set
     */
    public void setListUserAuthorityFileTypes(final List<UserAuthorityFileType> listUserAuthorityFileTypes) {
        this.listUserAuthorityFileTypes = listUserAuthorityFileTypes;
    }

    /**
     * Gets the service service.
     *
     * @return the serviceService
     */
    public ServiceService getServiceService() {
        return serviceService;
    }

    /**
     * Sets the service service.
     *
     * @param serviceService the serviceService to set
     */
    public void setServiceService(final ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Sets the files list.
     *
     * @param filesList the filesList to set
     */
    public void setFilesList(final List<File> filesList) {
        this.filesList = filesList;
    }

    /**
     * Gets the selected file.
     *
     * @return the selectedFile
     */
    public FileDto getSelectedFile() {
        return selectedFile;
    }

    /**
     * Sets the selected file.
     *
     * @param selectedFile the selectedFile to set
     */
    public void setSelectedFile(final FileDto selectedFile) {
        this.selectedFile = selectedFile;
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public PaymentFilter getFilter() {
        return filter;
    }

    /**
     * Sets the filter.
     *
     * @param filter the filter to set
     */
    public void setFilter(final PaymentFilter filter) {
        this.filter = filter;
    }

    /**
     * Gets the file type items.
     *
     * @return the file type items
     */
    public List<SelectItem> getFileTypeItems() {
        return fileTypeItems;
    }

    /**
     * Sets the file type items.
     *
     * @param fileTypeItems the new file type items
     */
    public void setFileTypeItems(final List<SelectItem> fileTypeItems) {
        this.fileTypeItems = fileTypeItems;
    }

    /**
     * Gets the date validation error message.
     *
     * @return the date validation error message
     */
    public static String getDateValidationErrorMessage() {
        return DATE_VALIDATION_ERROR_MESSAGE;
    }

    /**
     * Gets the file type service.
     *
     * @return the file type service
     */
    public FileTypeService getFileTypeService() {
        return fileTypeService;
    }

    /**
     * Sets the file type service.
     *
     * @param fileTypeService the new file type service
     */
    public void setFileTypeService(final FileTypeService fileTypeService) {
        this.fileTypeService = fileTypeService;
    }

    /**
     * Sets the file dto list.
     *
     * @param fileDtoList the fileDtoList to set
     */
    public void setFileDtoList(final List<FileDto> fileDtoList) {
        this.fileDtoList = fileDtoList;
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
     * Gets the common service.
     *
     * @return the commonService
     */
    public CommonService getCommonService() {
        return commonService;
    }

    /**
     * Sets the common service.
     *
     * @param commonService the commonService to set
     */
    public void setCommonService(final CommonService commonService) {
        this.commonService = commonService;
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
     * @return the paymentDataService
     */
    public PaymentDataService getPaymentDataService() {
        return paymentDataService;
    }

    /**
     * @param paymentDataService the paymentDataService to set
     */
    public void setPaymentDataService(final PaymentDataService paymentDataService) {
        this.paymentDataService = paymentDataService;
    }

    /**
     * @return the itemFlowService
     */
    public ItemFlowService getItemFlowService() {
        return itemFlowService;
    }

    /**
     * @param itemFlowService the itemFlowService to set
     */
    public void setItemFlowService(final ItemFlowService itemFlowService) {
        this.itemFlowService = itemFlowService;
    }

    public ApplicationPropretiesService getApplicationPropretiesService() {
        return applicationPropretiesService;
    }

    public void setApplicationPropretiesService(ApplicationPropretiesService applicationPropretiesService) {
        this.applicationPropretiesService = applicationPropretiesService;
    }

    public boolean isInitializingPage() {
        return initializingPage;
    }

    public void setInitializingPage(boolean initializingPage) {
        this.initializingPage = initializingPage;
    }

    public boolean isPhyto(File currentFile) {
        boolean checkMinaderMinistry = currentFile.getDestinataire().equalsIgnoreCase(Constants.MINADER_MINISTRY);
        return checkMinaderMinistry && Arrays.asList(FileTypeCode.CCT_CT_E, FileTypeCode.CCT_CT_E_ATP, FileTypeCode.CCT_CT_E_FSTP, FileTypeCode.CCT_CT_E_PVE, FileTypeCode.CCT_CT_E_PVI).contains(currentFile.getFileType().getCode());
    }

    public boolean isCcsMinsante(File currentFile) {
        return FileTypeCode.CCS_MINSANTE.equals(currentFile.getFileType().getCode());
    }

    public boolean isVtMinepia(File currentFile) {
        return FileTypeCode.VT_MINEPIA.equals(currentFile.getFileType().getCode());
    }

    private PaymentData getPaymentDataByFileAndPaymentFlow(File file, FlowCode paymentFlowCode) {
        ItemFlow lastItemFlow;
        PaymentData paymentData = null;
        final List<ItemFlow> lastPaymentItemFlowList = itemFlowService.findLastItemFlowsByFileAndFlow(file, paymentFlowCode);
        if (lastPaymentItemFlowList != null && !lastPaymentItemFlowList.isEmpty()) {
            for (final ItemFlow itemFlow : lastPaymentItemFlowList) {
                lastItemFlow = itemFlow;
                paymentData = paymentDataService.findPaymentDataByItemFlow(lastItemFlow);
                if (paymentData != null) {
                    break;
                }
            }
        }
        return paymentData;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
