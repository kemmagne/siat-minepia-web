package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileTypeStep;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.Step;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.AdministrationService;
import org.guce.siat.common.service.AlfrescoPropretiesService;
import org.guce.siat.common.service.ApplicationPropretiesService;
import org.guce.siat.common.service.AppointmentService;
import org.guce.siat.common.service.AuthorityService;
import org.guce.siat.common.service.CoreService;
import org.guce.siat.common.service.CountryService;
import org.guce.siat.common.service.EbxmlPropertiesService;
import org.guce.siat.common.service.FieldGroupService;
import org.guce.siat.common.service.FileFieldService;
import org.guce.siat.common.service.FileFieldValueService;
import org.guce.siat.common.service.FileItemFieldValueService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FileProducer;
import org.guce.siat.common.service.FileService;
import org.guce.siat.common.service.FileTypeFlowReportService;
import org.guce.siat.common.service.FileTypeFlowService;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.common.service.FileTypeStepService;
import org.guce.siat.common.service.FlowService;
import org.guce.siat.common.service.ItemFlowService;
import org.guce.siat.common.service.MailService;
import org.guce.siat.common.service.MessageToSendService;
import org.guce.siat.common.service.ParamsOrganismService;
import org.guce.siat.common.service.ParamsService;
import org.guce.siat.common.service.RecommandationAuthorityService;
import org.guce.siat.common.service.RecommandationService;
import org.guce.siat.common.service.ReportOrganismService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.StepService;
import org.guce.siat.common.service.TransferService;
import org.guce.siat.common.service.UserAuthorityFileTypeService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.service.XmlConverterService;
import org.guce.siat.core.ct.service.AnalyseOrderService;
import org.guce.siat.core.ct.service.AnalysePartService;
import org.guce.siat.core.ct.service.AnalyseResultService;
import org.guce.siat.core.ct.service.ApprovedDecisionService;
import org.guce.siat.core.ct.service.CommonService;
import org.guce.siat.core.ct.service.CotationService;
import org.guce.siat.core.ct.service.DecisionHistoryService;
import org.guce.siat.core.ct.service.InspectionReportService;
import org.guce.siat.core.ct.service.InterceptionNotificationService;
import org.guce.siat.core.ct.service.LaboratoryService;
import org.guce.siat.core.ct.service.PaymentDataService;
import org.guce.siat.core.ct.service.PottingReportService;
import org.guce.siat.core.ct.service.TreatmentCompanyService;
import org.guce.siat.core.ct.service.TreatmentInfosService;
import org.guce.siat.core.ct.service.TreatmentOrderService;
import org.guce.siat.core.ct.service.TreatmentResultService;
import org.guce.siat.core.gr.service.InfractionService;
import org.guce.siat.core.gr.service.RiskService;
import org.guce.siat.core.gr.service.TrendPerformanceService;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.common.util.WebConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author ht
 */
public abstract class DefaultDetailController implements Serializable {

    private static final long serialVersionUID = -7247026594059398411L;

    /**
     * The Constant logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The file type step service.
     */
    @ManagedProperty(value = "#{fileTypeStepService}")
    protected FileTypeStepService fileTypeStepService;

    /**
     * The administration service.
     */
    @ManagedProperty(value = "#{administrationService}")
    protected AdministrationService administrationService;

    /**
     * The user authority file type service.
     */
    @ManagedProperty(value = "#{userAuthorityFileTypeService}")
    protected UserAuthorityFileTypeService userAuthorityFileTypeService;

    /**
     * The flow service.
     */
    @ManagedProperty(value = "#{flowService}")
    protected FlowService flowService;

    /**
     * The params service.
     */
    @ManagedProperty(value = "#{paramsService}")
    protected ParamsService paramsService;

    /**
     * The file item service.
     */
    @ManagedProperty(value = "#{fileItemService}")
    protected FileItemService fileItemService;

    /**
     * The item flow service.
     */
    @ManagedProperty(value = "#{itemFlowService}")
    protected ItemFlowService itemFlowService;

    /**
     * The file service.
     */
    @ManagedProperty(value = "#{fileService}")
    protected FileService fileService;

    /**
     * The user service.
     */
    @ManagedProperty(value = "#{userService}")
    protected UserService userService;

    /**
     * The recommendation service.
     */
    @ManagedProperty(value = "#{recommandationService}")
    protected RecommandationService recommandationService;

    /**
     * The trend performance service.
     */
    @ManagedProperty(value = "#{trendPerformanceService}")
    protected TrendPerformanceService trendPerformanceService;

    /**
     * The appointment service.
     */
    @ManagedProperty(value = "#{appointmentService}")
    protected AppointmentService appointmentService;

    /**
     * The approved decision service.
     */
    @ManagedProperty(value = "#{approvedDecisionService}")
    protected ApprovedDecisionService approvedDecisionService;

    /**
     * The field group service.
     */
    @ManagedProperty(value = "#{fieldGroupService}")
    protected FieldGroupService fieldGroupService;

    /**
     * The step service.
     */
    @ManagedProperty(value = "#{stepService}")
    protected StepService stepService;

    /**
     * The XML converter service.
     */
    @ManagedProperty(value = "#{xmlConverterService}")
    protected XmlConverterService xmlConverterService;

    /**
     * The application properties service.
     */
    @ManagedProperty(value = "#{applicationPropretiesService}")
    protected ApplicationPropretiesService applicationPropretiesService;

    /**
     * The inspection report service.
     */
    @ManagedProperty(value = "#{inspectionReportService}")
    protected InspectionReportService inspectionReportService;

    /**
     * The common service.
     */
    @ManagedProperty(value = "#{commonService}")
    protected CommonService commonService;

    /**
     * The service service.
     */
    @ManagedProperty(value = "#{serviceService}")
    protected ServiceService serviceService;

    /**
     * The analyze part service.
     */
    @ManagedProperty(value = "#{analysePartService}")
    protected AnalysePartService analysePartService;

    /**
     * The laboratory service.
     */
    @ManagedProperty(value = "#{laboratoryService}")
    protected LaboratoryService laboratoryService;

    /**
     * The analyze order service.
     */
    @ManagedProperty(value = "#{analyseOrderService}")
    protected AnalyseOrderService analyseOrderService;

    /**
     * The treatment order service.
     */
    @ManagedProperty(value = "#{treatmentOrderService}")
    protected TreatmentOrderService treatmentOrderService;

    /**
     * The treatment infos service.
     */
    @ManagedProperty(value = "#{treatmentInfosService}")
    protected TreatmentInfosService treatmentInfosService;

    /**
     * The EBXMl properties.
     */
    @ManagedProperty(value = "#{ebxmlPropertiesService}")
    protected EbxmlPropertiesService ebxmlPropertiesService;

    /**
     * The PARAMS organism service.
     */
    @ManagedProperty(value = "#{paramsOrganismService}")
    protected ParamsOrganismService paramsOrganismService;

    /**
     * The alfresco properties service.
     */
    @ManagedProperty(value = "#{alfrescoPropretiesService}")
    protected AlfrescoPropretiesService alfrescoPropretiesService;

    /**
     * The mail service.
     */
    @ManagedProperty(value = "#{mailService}")
    protected MailService mailService;

    /**
     * The treatment company service.
     */
    @ManagedProperty(value = "#{treatmentCompanyService}")
    protected TreatmentCompanyService treatmentCompanyService;

    /**
     * The analyse result service.
     */
    @ManagedProperty(value = "#{analyseResultService}")
    protected AnalyseResultService analyseResultService;

    /**
     * The treatment result service.
     */
    @ManagedProperty(value = "#{treatmentResultService}")
    protected TreatmentResultService treatmentResultService;

    /**
     * The risk service.
     */
    @ManagedProperty(value = "#{riskService}")
    protected RiskService riskService;

    /**
     * The file field service.
     */
    @ManagedProperty(value = "#{fileFieldService}")
    protected FileFieldService fileFieldService;

    /**
     * The file field value service.
     */
    @ManagedProperty(value = "#{fileFieldValueService}")
    protected FileFieldValueService fileFieldValueService;

    /**
     * The report organism service.
     */
    @ManagedProperty(value = "#{reportOrganismService}")
    protected ReportOrganismService reportOrganismService;

    /**
     * send file service.
     */
    @ManagedProperty(value = "#{fileProducer}")
    protected FileProducer fileProducer;
    
    /**
     * The message not already send service.
     */
    @ManagedProperty(value = "#{messageToSendService}")
    protected MessageToSendService messageToSendService;

    /**
     * The treatment result service.
     */
    @ManagedProperty(value = "#{authorityService}")
    protected AuthorityService authorityService;

    /**
     * The file type service.
     */
    @ManagedProperty(value = "#{fileTypeService}")
    protected FileTypeService fileTypeService;

    /**
     * The file type flow report service.
     */
    @ManagedProperty(value = "#{fileTypeFlowReportService}")
    protected FileTypeFlowReportService fileTypeFlowReportService;

    /**
     * The user authority file type service.
     */
    @ManagedProperty(value = "#{recommandationAuthorityService}")
    protected RecommandationAuthorityService recommandationAuthorityService;

    /**
     * The file type flow service.
     */
    @ManagedProperty(value = "#{fileTypeFlowService}")
    protected FileTypeFlowService fileTypeFlowService;

    /**
     * The infraction service.
     */
    @ManagedProperty(value = "#{infractionService}")
    protected InfractionService infractionService;

    /**
     * The payment data service.
     */
    @ManagedProperty(value = "#{paymentDataService}")
    protected PaymentDataService paymentDataService;

    @ManagedProperty(value = "#{interceptionNotificationService}")
    protected InterceptionNotificationService interceptionNotificationService;

    @ManagedProperty(value = "#{coreService}")
    protected CoreService coreService;

    @ManagedProperty(value = "#{pottingReportService}")
    protected PottingReportService pottingReportService;

    @ManagedProperty(value = "#{transferService}")
    protected TransferService transferService;

    /**
     * The decision history service.
     */
    @ManagedProperty(value = "#{decisionHistoryService}")
    protected DecisionHistoryService decisionHistoryService;

    @ManagedProperty(value = "#{cotationService}")
    protected CotationService cotationService;

    @ManagedProperty(value = "#{fileItemFieldValueService}")
    protected FileItemFieldValueService fileItemFieldValueService;

    /**
     * The transaction manager.
     */
    @ManagedProperty(value = "#{transactionManager}")
    protected PlatformTransactionManager transactionManager;

    /**
     * The country service.
     */
    @ManagedProperty(value = "#{countryService}")
    protected CountryService countryService;

    /**
     * The logged user.
     */
    protected User loggedUser;

    /**
     * The current file.
     */
    protected File currentFile;

    /**
     * The current file item.
     */
    protected FileItem currentFileItem;

    protected Set<File> filesSet;

    protected String fileNumer;

    protected boolean viewAnyFile;

    /**
     * The come from search.
     */
    protected boolean comeFromSearch;

    protected void initFile() {
        loggedUser = getLoggedUser();
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        fileNumer = requestParameterMap.get(WebConstants.FILE_NUMBER_REQUEST_PARAM_KEY);
        if (StringUtils.isBlank(fileNumer)) {
            addErrorMessage("DetailCannotGetFileNumber");
            goToPreviousPage();
            return;
        }
        comeFromSearch = BooleanUtils.toBoolean(requestParameterMap.get(WebConstants.SEARCH_BOOLEAN_REQUEST_PARAM_KEY));
        // file exists ?
        File file = fileService.findByNumDossierGuce(fileNumer);
        if (file == null) {
            file = fileService.findByRefSiat(fileNumer);
            if (file == null) {
                addErrorMessage("DetailCannotGetFile");
                goToPreviousPage();
                return;
            }
        }
        // security
//        filesSet = Utils.getFilesSet(fileTypeStepService, userAuthorityFileTypeService, fileItemService, commonService, fileFieldValueService, loggedUser);
//        viewAnyFile = comeFromSearch || Utils.canViewAnyFile(paramsService, loggedUser);
//        if (!(viewAnyFile || (CollectionUtils.isNotEmpty(filesSet) && filesSet.contains(file)))) {
//            addErrorMessage("DetailDontHaveAccessToFile");
//            goToPreviousPage();
//            return;
//        }
        // all is ok : set current file and current file item
        setCurrentFile(file);
        setCurrentFileItem(getCurrentFile().getFileItemsList().get(0));
        // set file step labels
        setFileStepLabels();
        // update last item flows
        if (!comeFromSearch) {
            updateLastItemFLows();
        }
    }

    /**
     * Reload page listener.
     */
    protected void reloadPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext extContext = context.getExternalContext();
            String detailPageUrl = Utils.getFinalDetailPageUrl(currentFile, getDetailIndexPageUrl(), false, false);
            String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, detailPageUrl));
            extContext.redirect(url);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public String getFinalDetailPageUrl(File file) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        String detailPageUrl = getDetailIndexPageUrl();
        String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, detailPageUrl));
        return Utils.getFinalDetailPageUrl(file, url, false, true);
    }

    protected void goToPreviousPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            String referer = externalContext.getRequestHeaderMap().get("referer");
            if (StringUtils.isNotBlank(referer)) {
                externalContext.redirect(referer);
            } else {
                goToDetailPage();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    protected void setFileStepLabels() {
        Step step = currentFileItem.getStep();
        if (step != null) {
            FileTypeStep fts = fileTypeStepService.findFileTypeStepByFileTypeAndStep(currentFile.getFileType(), step);
            if (fts != null) {
                currentFile.setRedefinedLabelEn(fts.getLabelEn());
                currentFile.setRedefinedLabelFr(fts.getLabelFr());
            } else {
                currentFile.setRedefinedLabelEn(step.getLabelEn());
                currentFile.setRedefinedLabelFr(step.getLabelFr());
            }
        } else {
            logger.warn("The step for the file {} isn't supposed to be null", currentFile);
        }
    }

    private void updateLastItemFLows() {
        List<FileItem> fileItems = getCurrentFile().getFileItemsList();
        for (final FileItem fileItem : fileItems) {
            for (final ItemFlow itemFlow : fileItem.getItemFlowsList()) {
                if (BooleanUtils.toBoolean(itemFlow.getUnread()) && fileItem.getStep().equals(itemFlow.getFlow().getToStep())) {
                    itemFlow.setUnread(Boolean.FALSE);
                    itemFlowService.update(itemFlow);
                }
            }
        }
    }

    private void addErrorMessage(String bundle) {
        String errorMsg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(bundle);
        JsfUtil.addErrorMessage("detailIndexMsg", errorMsg);
    }

    public abstract String getDetailIndexPageUrl();

    protected abstract void goToDetailPage();

    public abstract boolean canDecide();

    public abstract boolean canConfirm();

    public abstract boolean canRollback();

    /**
     * Gets the logged user.
     *
     * @return the logged user
     */
    public User getLoggedUser() {
        if (loggedUser == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                loggedUser = (User) session.getAttribute("loggedUser");
            }
        }
        return loggedUser;
    }

    /**
     * Gets the current locale.
     *
     *
     * @return the current locale
     */
    public Locale getCurrentLocale() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
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

    public TransferService getTransferService() {
        return transferService;
    }

    public void setTransferService(TransferService transferService) {
        this.transferService = transferService;
    }

    public ParamsService getParamsService() {
        return paramsService;
    }

    public void setParamsService(ParamsService paramsService) {
        this.paramsService = paramsService;
    }

    public PottingReportService getPottingReportService() {
        return pottingReportService;
    }

    public void setPottingReportService(PottingReportService pottingReportService) {
        this.pottingReportService = pottingReportService;
    }

    public CoreService getCoreService() {
        return coreService;
    }

    public void setCoreService(CoreService coreService) {
        this.coreService = coreService;
    }

    public CotationService getCotationService() {
        return cotationService;
    }

    public void setCotationService(CotationService cotationService) {
        this.cotationService = cotationService;
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
     * Get the file type flow report service
     *
     * @return the file type flow report service
     */
    public FileTypeFlowReportService getFileTypeFlowReportService() {
        return fileTypeFlowReportService;
    }

    /**
     * Set the file type flow report service
     *
     * @param fileTypeFlowReportService
     */
    public void setFileTypeFlowReportService(FileTypeFlowReportService fileTypeFlowReportService) {
        this.fileTypeFlowReportService = fileTypeFlowReportService;
    }

    /**
     * Gets the item flow service.
     *
     * @return the item flow service
     */
    public ItemFlowService getItemFlowService() {
        return itemFlowService;
    }

    /**
     * Sets the item flow service.
     *
     * @param itemFlowService the new item flow service
     */
    public void setItemFlowService(final ItemFlowService itemFlowService) {
        this.itemFlowService = itemFlowService;
    }

    /**
     * Gets the file service.
     *
     * @return the file service
     */
    public FileService getFileService() {
        return fileService;
    }

    /**
     * Sets the file service.
     *
     * @param fileService the new file service
     */
    public void setFileService(final FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Gets the user service.
     *
     * @return the userService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Sets the user service.
     *
     * @param userService the userService to set
     */
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets the flow service.
     *
     * @return the flow service
     */
    public FlowService getFlowService() {
        return flowService;
    }

    /**
     * Sets the flow service.
     *
     * @param flowService the new flow service
     */
    public void setFlowService(final FlowService flowService) {
        this.flowService = flowService;
    }

    /**
     * Gets the administration service.
     *
     * @return the administration service
     */
    public AdministrationService getAdministrationService() {
        return administrationService;
    }

    /**
     * Sets the administration service.
     *
     * @param administrationService the new administration service
     */
    public void setAdministrationService(final AdministrationService administrationService) {
        this.administrationService = administrationService;
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
     * Gets the xml converter service.
     *
     * @return the xml converter service
     */
    public XmlConverterService getXmlConverterService() {
        return xmlConverterService;
    }

    /**
     * Sets the xml converter service.
     *
     * @param xmlConverterService the new xml converter service
     */
    public void setXmlConverterService(final XmlConverterService xmlConverterService) {
        this.xmlConverterService = xmlConverterService;
    }

    /**
     * Gets the ebxml properties service.
     *
     * @return the ebxml properties service
     */
    public EbxmlPropertiesService getEbxmlPropertiesService() {
        return ebxmlPropertiesService;
    }

    /**
     * Sets the ebxml properties service.
     *
     * @param ebxmlPropertiesService the new ebxml properties service
     */
    public void setEbxmlPropertiesService(final EbxmlPropertiesService ebxmlPropertiesService) {
        this.ebxmlPropertiesService = ebxmlPropertiesService;
    }

    /**
     * Gets the application propreties service.
     *
     * @return the application propreties service
     */
    public ApplicationPropretiesService getApplicationPropretiesService() {
        return applicationPropretiesService;
    }

    /**
     * Sets the application propreties service.
     *
     * @param applicationPropretiesService the new application propreties
     * service
     */
    public void setApplicationPropretiesService(final ApplicationPropretiesService applicationPropretiesService) {
        this.applicationPropretiesService = applicationPropretiesService;
    }

    /**
     * Gets the appointment service.
     *
     * @return the appointmentService
     */
    public AppointmentService getAppointmentService() {
        return appointmentService;
    }

    /**
     * Sets the appointment service.
     *
     * @param appointmentService the appointmentService to set
     */
    public void setAppointmentService(final AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Gets the inspection report service.
     *
     * @return the inspectionReportService
     */
    public InspectionReportService getInspectionReportService() {
        return inspectionReportService;
    }

    /**
     * Sets the inspection report service.
     *
     * @param inspectionReportService the inspectionReportService to set
     */
    public void setInspectionReportService(final InspectionReportService inspectionReportService) {
        this.inspectionReportService = inspectionReportService;
    }

    /**
     * Gets the field group service.
     *
     * @return the field group service
     */
    public FieldGroupService getFieldGroupService() {
        return fieldGroupService;
    }

    /**
     * Sets the field group service.
     *
     * @param fieldGroupService the new field group service
     */
    public void setFieldGroupService(final FieldGroupService fieldGroupService) {
        this.fieldGroupService = fieldGroupService;
    }

    /**
     * Gets the service service.
     *
     * @return the service service
     */
    public ServiceService getServiceService() {
        return serviceService;
    }

    /**
     * Sets the service service.
     *
     * @param serviceService the new service service
     */
    public void setServiceService(final ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Gets the analyse part service.
     *
     * @return the analyse part service
     */
    public AnalysePartService getAnalysePartService() {
        return analysePartService;
    }

    /**
     * Sets the analyse part service.
     *
     * @param analysePartService the new analyse part service
     */
    public void setAnalysePartService(final AnalysePartService analysePartService) {
        this.analysePartService = analysePartService;
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
     * Gets the analyse order service.
     *
     * @return the analyse order service
     */
    public AnalyseOrderService getAnalyseOrderService() {
        return analyseOrderService;
    }

    /**
     * Sets the analyse order service.
     *
     * @param analyseOrderService the new analyse order service
     */
    public void setAnalyseOrderService(final AnalyseOrderService analyseOrderService) {
        this.analyseOrderService = analyseOrderService;
    }

    /**
     * Gets the params organism service.
     *
     * @return the paramsOrganismService
     */
    public ParamsOrganismService getParamsOrganismService() {
        return paramsOrganismService;
    }

    /**
     * Sets the params organism service.
     *
     * @param paramsOrganismService the paramsOrganismService to set
     */
    public void setParamsOrganismService(final ParamsOrganismService paramsOrganismService) {
        this.paramsOrganismService = paramsOrganismService;
    }

    /**
     * Gets the alfresco propreties service.
     *
     * @return the alfresco propreties service
     */
    public AlfrescoPropretiesService getAlfrescoPropretiesService() {
        return alfrescoPropretiesService;
    }

    /**
     * Sets the alfresco propreties service.
     *
     * @param alfrescoPropretiesService the new alfresco propreties service
     */
    public void setAlfrescoPropretiesService(final AlfrescoPropretiesService alfrescoPropretiesService) {
        this.alfrescoPropretiesService = alfrescoPropretiesService;
    }

    /**
     * Gets the treatment company service.
     *
     * @return the treatment company service
     */
    public TreatmentCompanyService getTreatmentCompanyService() {
        return treatmentCompanyService;
    }

    /**
     * Sets the treatment company service.
     *
     * @param treatmentCompanyService the new treatment company service
     */
    public void setTreatmentCompanyService(final TreatmentCompanyService treatmentCompanyService) {
        this.treatmentCompanyService = treatmentCompanyService;
    }

    /**
     * Gets the analyse result service.
     *
     * @return the analyse result service
     */
    public AnalyseResultService getAnalyseResultService() {
        return analyseResultService;
    }

    /**
     * Sets the analyse result service.
     *
     * @param analyseResultService the new analyse result service
     */
    public void setAnalyseResultService(final AnalyseResultService analyseResultService) {
        this.analyseResultService = analyseResultService;
    }

    /**
     * Gets the treatment order service.
     *
     * @return the treatmentOrderService
     */
    public TreatmentOrderService getTreatmentOrderService() {
        return treatmentOrderService;
    }

    /**
     * Sets the treatment order service.
     *
     * @param treatmentOrderService the treatmentOrderService to set
     */
    public void setTreatmentOrderService(final TreatmentOrderService treatmentOrderService) {
        this.treatmentOrderService = treatmentOrderService;
    }

    public TreatmentInfosService getTreatmentInfosService() {
        return treatmentInfosService;
    }

    public void setTreatmentInfosService(final TreatmentInfosService treatmentInfosService) {
        this.treatmentInfosService = treatmentInfosService;
    }

    public ApprovedDecisionService getApprovedDecisionService() {
        return approvedDecisionService;
    }

    public void setApprovedDecisionService(ApprovedDecisionService approvedDecisionService) {
        this.approvedDecisionService = approvedDecisionService;
    }

    /**
     * Gets the treatment result service.
     *
     * @return the treatmentResultService
     */
    public TreatmentResultService getTreatmentResultService() {
        return treatmentResultService;
    }

    /**
     * Sets the treatment result service.
     *
     * @param treatmentResultService the treatmentResultService to set
     */
    public void setTreatmentResultService(final TreatmentResultService treatmentResultService) {
        this.treatmentResultService = treatmentResultService;
    }

    /**
     * Gets the file producer.
     *
     * @return the file producer
     */
    public FileProducer getFileProducer() {
        return fileProducer;
    }

    /**
     * Sets the file producer.
     *
     * @param fileProducer the new file producer
     */
    public void setFileProducer(final FileProducer fileProducer) {
        this.fileProducer = fileProducer;
    }

    /**
     * Gets the trend performance service.
     *
     * @return the trend performance service
     */
    public TrendPerformanceService getTrendPerformanceService() {
        return trendPerformanceService;
    }

    /**
     * Sets the trend performance service.
     *
     * @param trendPerformanceService the new trend performance service
     */
    public void setTrendPerformanceService(final TrendPerformanceService trendPerformanceService) {
        this.trendPerformanceService = trendPerformanceService;
    }

    /**
     * Gets the risk service.
     *
     * @return the risk service
     */
    public RiskService getRiskService() {
        return riskService;
    }

    /**
     * Sets the risk service.
     *
     * @param riskService the new risk service
     */
    public void setRiskService(final RiskService riskService) {
        this.riskService = riskService;
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
     * Gets the Recommandation Authority service.
     *
     * @return recommandationAuthorityService
     */
    public RecommandationAuthorityService getRecommandationAuthorityService() {
        return recommandationAuthorityService;
    }

    /**
     * Sets the recommandation authority service.
     *
     * @param recommandationAuthorityService the recommandationAuthorityService
     * to set
     */
    public void setRecommandationAuthorityService(final RecommandationAuthorityService recommandationAuthorityService) {
        this.recommandationAuthorityService = recommandationAuthorityService;
    }

    /**
     * Gets the recommandation service.
     *
     * @return the recommandation service
     */
    public RecommandationService getRecommandationService() {
        return recommandationService;
    }

    /**
     * Sets the recommandation service.
     *
     * @param recommandationService the new recommandation service
     */
    public void setRecommandationService(final RecommandationService recommandationService) {
        this.recommandationService = recommandationService;
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
     * Gets the mail service.
     *
     * @return the mailService
     */
    public MailService getMailService() {
        return mailService;
    }

    /**
     * Sets the mail service.
     *
     * @param mailService the mailService to set
     */
    public void setMailService(final MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * Gets the file field value service.
     *
     * @return the file field value service
     */
    public FileFieldValueService getFileFieldValueService() {
        return fileFieldValueService;
    }

    /**
     * Sets the file field value service.
     *
     * @param fileFieldValueService the new file field value service
     */
    public void setFileFieldValueService(final FileFieldValueService fileFieldValueService) {
        this.fileFieldValueService = fileFieldValueService;
    }

    /**
     * Gets the report organism service.
     *
     * @return the report organism service
     */
    public ReportOrganismService getReportOrganismService() {
        return reportOrganismService;
    }

    /**
     * Sets the report organism service.
     *
     * @param reportOrganismService the new report organism service
     */
    public void setReportOrganismService(final ReportOrganismService reportOrganismService) {
        this.reportOrganismService = reportOrganismService;
    }

    /**
     * Gets the file field service.
     *
     * @return the file field service
     */
    public FileFieldService getFileFieldService() {
        return fileFieldService;
    }

    /**
     * Sets the file field service.
     *
     * @param fileFieldService the new file field service
     */
    public void setFileFieldService(final FileFieldService fileFieldService) {
        this.fileFieldService = fileFieldService;
    }

    /**
     * Gets the file type flow service.
     *
     * @return the file type flow service
     */
    public FileTypeFlowService getFileTypeFlowService() {
        return fileTypeFlowService;
    }

    /**
     * Sets the file type flow service.
     *
     * @param fileTypeFlowService the new file type flow service
     */
    public void setFileTypeFlowService(final FileTypeFlowService fileTypeFlowService) {
        this.fileTypeFlowService = fileTypeFlowService;
    }

    /**
     * Gets the infraction service.
     *
     * @return the infraction service
     */
    public InfractionService getInfractionService() {
        return infractionService;
    }

    /**
     * Sets the infraction service.
     *
     * @param infractionService the new infraction service
     */
    public void setInfractionService(final InfractionService infractionService) {
        this.infractionService = infractionService;
    }

    /**
     * Gets the payment data service.
     *
     * @return the payment data service
     */
    public PaymentDataService getPaymentDataService() {
        return paymentDataService;
    }

    /**
     * Sets the payment data service.
     *
     * @param paymentDataService the new payment data service
     */
    public void setPaymentDataService(final PaymentDataService paymentDataService) {
        this.paymentDataService = paymentDataService;
    }

    public InterceptionNotificationService getInterceptionNotificationService() {
        return interceptionNotificationService;
    }

    public void setInterceptionNotificationService(final InterceptionNotificationService interceptionNotificationService) {
        this.interceptionNotificationService = interceptionNotificationService;
    }

    public DecisionHistoryService getDecisionHistoryService() {
        return decisionHistoryService;
    }

    public void setDecisionHistoryService(DecisionHistoryService decisionHistoryService) {
        this.decisionHistoryService = decisionHistoryService;
    }

    /**
     * Gets the transaction manager.
     *
     * @return the transaction manager
     */
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * Sets the transaction manager.
     *
     * @param transactionManager the new transaction manager
     */
    public void setTransactionManager(final PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public FileItemFieldValueService getFileItemFieldValueService() {
        return fileItemFieldValueService;
    }

    public void setFileItemFieldValueService(FileItemFieldValueService fileItemFieldValueService) {
        this.fileItemFieldValueService = fileItemFieldValueService;
    }

    public CountryService getCountryService() {
        return countryService;
    }

    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Gets the current file.
     *
     * @return the current file
     */
    public File getCurrentFile() {
        return currentFile;
    }

    /**
     * Sets the current file.
     *
     * @param currentFile the new current file
     */
    public void setCurrentFile(final File currentFile) {
        this.currentFile = currentFile;
    }

    /**
     * Gets the current file item.
     *
     * @return the current file item
     */
    public FileItem getCurrentFileItem() {
        return currentFileItem;
    }

    /**
     * Sets the current file item.
     *
     * @param currentFileItem the new current file item
     */
    public void setCurrentFileItem(final FileItem currentFileItem) {
        this.currentFileItem = currentFileItem;
    }

    public String getFileNumer() {
        return fileNumer;
    }

    public void setFileNumer(String fileNumer) {
        this.fileNumer = fileNumer;
    }

    public MessageToSendService getMessageToSendService() {
        return messageToSendService;
    }

    public void setMessageToSendService(MessageToSendService messageToSendService) {
        this.messageToSendService = messageToSendService;
    }
}
