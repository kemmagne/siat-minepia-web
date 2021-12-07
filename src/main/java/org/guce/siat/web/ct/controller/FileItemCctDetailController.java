package org.guce.siat.web.ct.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.guce.siat.common.data.FieldGroupDto;
import org.guce.siat.common.data.ItemFlowDto;
import org.guce.siat.common.mail.MailConstants;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.AnalyseType;
import org.guce.siat.common.model.Appointment;
import org.guce.siat.common.model.Attachment;
import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.Container;
import org.guce.siat.common.model.CopyRecipient;
import org.guce.siat.common.model.Country;
import org.guce.siat.common.model.DataType;
import org.guce.siat.common.model.FieldGroup;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileField;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemField;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.FileTypeFlow;
import org.guce.siat.common.model.FileTypeFlowReport;
import org.guce.siat.common.model.FileTypeStep;
import org.guce.siat.common.model.Flow;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.ItemFlowData;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.ParamsOrganism;
import org.guce.siat.common.model.Recommandation;
import org.guce.siat.common.model.RecommandationAuthority;
import org.guce.siat.common.model.RecommandationAuthorityId;
import org.guce.siat.common.model.ReportOrganism;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.Step;
import org.guce.siat.common.model.Transfer;
import org.guce.siat.common.model.User;
import org.guce.siat.common.model.UserAuthority;
import org.guce.siat.common.model.UserAuthorityFileType;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.common.utils.FileUtils;
import org.guce.siat.common.utils.RepetableUtil;
import org.guce.siat.common.utils.SiatUtils;
import org.guce.siat.common.utils.Tab;
import org.guce.siat.common.utils.ebms.ESBConstants;
import org.guce.siat.common.utils.enums.AperakType;
import org.guce.siat.common.utils.enums.AuthorityConstants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.common.utils.enums.ParamsCategory;
import org.guce.siat.common.utils.enums.StepCode;
import org.guce.siat.common.utils.ged.AlfrescoDirectoriesInitializer;
import org.guce.siat.common.utils.ged.CmisClient;
import org.guce.siat.common.utils.ged.CmisSession;
import org.guce.siat.core.ct.model.AnalyseOrder;
import org.guce.siat.core.ct.model.AnalysePart;
import org.guce.siat.core.ct.model.AnalyseResult;
import org.guce.siat.core.ct.model.ApprovedDecision;
import org.guce.siat.core.ct.model.CCTCPParamValue;
import org.guce.siat.core.ct.model.DecisionHistory;
import org.guce.siat.core.ct.model.Infraction;
import org.guce.siat.core.ct.model.InspectionController;
import org.guce.siat.core.ct.model.InspectionReport;
import org.guce.siat.core.ct.model.InterceptionNotification;
import org.guce.siat.core.ct.model.InvoiceLine;
import org.guce.siat.core.ct.model.Laboratory;
import org.guce.siat.core.ct.model.PaymentData;
import org.guce.siat.core.ct.model.PaymentItemFlow;
import org.guce.siat.core.ct.model.PottingReport;
import org.guce.siat.core.ct.model.Sample;
import org.guce.siat.core.ct.model.TreatmentCompany;
import org.guce.siat.core.ct.model.TreatmentInfos;
import org.guce.siat.core.ct.model.TreatmentInfosCCSMinsante;
import org.guce.siat.core.ct.model.TreatmentOrder;
import org.guce.siat.core.ct.model.TreatmentPart;
import org.guce.siat.core.ct.model.TreatmentResult;
import org.guce.siat.core.ct.model.TreatmentType;
import org.guce.siat.core.ct.service.CCTCPParamValueService;
import org.guce.siat.core.ct.service.ParamCCTCPService;
import org.guce.siat.core.ct.service.UserStampSignatureService;
import org.guce.siat.core.ct.util.enums.CctExportProductType;
import org.guce.siat.core.gr.model.TrendPerformance;
import org.guce.siat.core.gr.utils.SynthesisConfig;
import org.guce.siat.core.gr.utils.enums.CertficatGoodness;
import org.guce.siat.core.utils.SendDocumentUtils;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.common.util.CctSpecificDecision;
import org.guce.siat.web.common.util.CctSpecificDecisionHistory;
import org.guce.siat.web.common.util.UploadFileManager;
import org.guce.siat.web.ct.controller.helpers.CctDetailControllerHelper;
import org.guce.siat.web.ct.controller.util.CustumMap;
import org.guce.siat.web.ct.controller.util.FileItemCheck;
import org.guce.siat.web.ct.controller.util.FileTypeDto;
import org.guce.siat.web.ct.controller.util.InspectionReportData;
import org.guce.siat.web.ct.controller.util.InspectionReportEtiquetageVo;
import org.guce.siat.web.ct.controller.util.InspectionReportTemperatureVo;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.RelatedFilesUtils;
import org.guce.siat.web.ct.controller.util.ReportGeneratorUtils;
import org.guce.siat.web.ct.controller.util.Utils;
import org.guce.siat.web.ct.controller.util.enums.DataTypeEnnumeration;
import org.guce.siat.web.ct.controller.util.enums.DataTypePropEnnum;
import org.guce.siat.web.ct.controller.util.enums.DecisionsSuiteVisite;
import org.guce.siat.web.ct.controller.util.enums.NITakenMeasure;
import org.guce.siat.web.ct.controller.util.enums.PVILastTreatmentDateState;
import org.guce.siat.web.ct.controller.util.enums.PVIProtectionMeasures;
import org.guce.siat.web.ct.controller.util.enums.PVIStorageEnv;
import org.guce.siat.web.ct.controller.util.enums.PVITransportEnv;
import org.guce.siat.web.ct.controller.util.enums.PVITreatmentType;
import org.guce.siat.web.ct.controller.util.enums.PVIWeatherConditions;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.guce.siat.web.ct.controller.util.enums.TRConditioning;
import org.guce.siat.web.ct.controller.util.enums.TRProductUsed;
import org.guce.siat.web.ct.controller.util.enums.TRProtectionEquipement;
import org.guce.siat.web.ct.controller.util.enums.TRStoragePlace;
import org.guce.siat.web.ct.controller.util.enums.TRTreatmentEnvironment;
import org.guce.siat.web.ct.controller.util.enums.TRWeatherCondition;
import org.guce.siat.web.ct.data.AnalyseTypeDto;
import org.guce.siat.web.ct.data.FileItemDto;
import org.guce.siat.web.ct.data.TreatmentTypeDto;
import org.guce.siat.web.gr.controller.RiskController;
import org.guce.siat.web.gr.util.GrUtilsWeb;
import org.guce.siat.web.gr.util.ScenarioType;
import org.guce.siat.web.reports.exporter.AbstractReportInvoker;
import org.guce.siat.web.reports.exporter.CctCsvExporter;
import org.guce.siat.web.reports.exporter.CcsMinsanteExporter;
import org.guce.siat.web.reports.exporter.CtCctCpEExporter;
import org.guce.siat.web.reports.exporter.CtCctCqeExporter;
import org.guce.siat.web.reports.exporter.CtCctCsvExporter;
import org.guce.siat.web.reports.exporter.CtCctTreatmentExporter;
import org.guce.siat.web.reports.exporter.CtPviExporter;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.message.Message;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.ComponentUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * The Class FileItemCctDetailController.
 */
@ViewScoped
@ManagedBean(name = "fileItemCctDetailController")
public class FileItemCctDetailController extends DefaultDetailController {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 5854830660037778807L;

    /**
     * The detail.
     */
    private Boolean detail;

    /**
     * The Constant DECISION_DIALOG.
     */
    private static final String DECISION_DIALOG = "decisionDialog".intern();

    /**
     * The Constant CONFIRMATION_DIALOG.
     */
    private static final String CONFIRMATION_DIALOG = "confirmation".intern();

    /**
     * The Constant STYLE_CLASS.
     */
    protected static final String STYLE_CLASS = "required-star-right".intern();

    /**
     * The Constant ID_DECISION_LABEL.
     */
    private static final String ID_DECISION_LABEL = "idDecision".intern();

    /**
     * The Constant ID_DISPATCH_LABEL.
     */
    private static final String ID_DISPATCH_LABEL = "idDispatch".intern();

    /**
     * The Constant CMIS_CONNEXION_ERROR.
     */
    private static final String CMIS_CONNEXION_ERROR = "cmisConnexionError.vm";

    /**
     * The Constant APPOINTMENT_DECISIONS_LIST.
     */
    private static final List<String> APPOINTMENT_DECISIONS_LIST = Arrays.asList(FlowCode.FL_CT_26.name(), FlowCode.FL_CT_42.name(), FlowCode.FL_CT_41.name());

    // Steps in which the decision by FileItem allowed
    /**
     * The Constant DECISION_STEPS_LIST.
     */
    private static final List<StepCode> DECISION_STEPS_LIST = Arrays.asList(StepCode.ST_CT_04, StepCode.ST_CT_13);

    /**
     * The Constant CONSTAT_FLOW_LIST.
     */
    private static final List<String> CONSTAT_FLOW_LIST = Arrays.asList(FlowCode.FL_CT_44.name(), FlowCode.FL_CT_28.name());

    /**
     * The Constant AJOURNEMENT_FLOW_LIST.
     */
    private static final List<String> AJOURNEMENT_FLOW_LIST = Arrays.asList(FlowCode.FL_CT_27.name(), FlowCode.FL_CT_43.name());

    /**
     * The Constant RDD_FLOW_CODES.
     */
    private static final List<String> RDD_FLOW_CODES = Arrays.asList(FlowCode.FL_CT_12.name(), FlowCode.FL_CT_14.name(), FlowCode.FL_CT_73.name(), FlowCode.FL_CT_74.name());

    /**
     * The Constant DCC_FLOW_CODES.
     */
    private static final List<String> DCC_FLOW_CODES = Arrays.asList(FlowCode.FL_CT_CVS_03.name(), FlowCode.FL_CT_CVS_07.name(), FlowCode.FL_CT_CVS_05.name());

    /**
     * The Constant EMAIL_BODY_NOTIFICATION_FR.
     */
    private static final String EMAIL_BODY_NOTIFICATION_FR = "emailBodyNotification_fr.vm";

    /**
     * The Constant EMAIL_BODY_NOTIFICATION_EN.
     */
    private static final String EMAIL_BODY_NOTIFICATION_EN = "emailBodyNotification_en.vm";

    private static final List<String> DECISION_FLOW_LIST_AT_COTATION_LEVEL = Arrays.asList(FlowCode.FL_CT_116.name(), FlowCode.FL_CT_117.name());

    private static final List<StepCode> COTATION_STEP_LIST_ALLOW_DECISION = Arrays.asList(StepCode.ST_CT_53);

    /**
     * The CCT CP param value service.
     */
    @ManagedProperty(value = "#{cCTCPParamValueService}")
    private CCTCPParamValueService cCTCPParamValueService;

    /**
     * The CCT CP param value service.
     */
    @ManagedProperty(value = "#{paramCCTCPService}")
    private ParamCCTCPService paramCCTCPService;

    /**
     * The User Signature and Stamp service.
     */
    @ManagedProperty(value = "#{userStampSignatureService}")
    private UserStampSignatureService userStampSignatureService;

    /**
     * The index page URL.
     */
    @ManagedProperty(value = "#{jmsPropretiesService}")
    private String indexPageUrl;

    /**
     * The send decision allowed.
     */
    private Boolean sendDecisionAllowed;

    /**
     * The roll back decisions allowed.
     */
    private Boolean rollBackDecisionsAllowed;

    /**
     * The cotation button allowed.
     */
    private Boolean cotationButtonAllowed;

    /**
     * The list user authority file types.
     */
    private List<UserAuthorityFileType> listUserAuthorityFileTypes;

    /**
     * The file field values.
     */
    private List<FileFieldValue> fileFieldValues;

    /**
     * The file item field values.
     */
    private List<FileItemFieldValue> fileItemFieldValues;

    /**
     * The field groups.
     */
    private List<FieldGroup> fieldGroups;

    /**
     * The field groups items.
     */
    private List<FieldGroup> fieldGroupsItems = new ArrayList<>();
    /**
     * The file field group dtos.
     */
    private List<FieldGroupDto<FileFieldValue>> fileFieldGroupDtos;

    /**
     * The file item field group dtos.
     */
    private List<FieldGroupDto<FileItemFieldValue>> fileItemFieldGroupDtos;

    /**
     * The selected flow.
     */
    private Flow selectedFlow;

    /**
     * The decision div.
     */
    private HtmlPanelGrid decisionDiv;

    /**
     * The dipatch div.
     */
    private HtmlPanelGrid dipatchDiv;

    /**
     * The product info items.
     */
    private List<FileItem> productInfoItems;

    /**
     * The product info items enabled.
     */
    private List<FileItem> productInfoItemsEnabled;

    /**
     * The flows.
     */
    private List<Flow> flows;

    /**
     * The last decisions.
     */
    private ItemFlow lastDecisions;

    /**
     * The selected attachment.
     */
    private Attachment selectedAttachment;

    /**
     * The selected recommendation.
     */
    private Recommandation selectedRecommandation;

    /**
     * The selected recommendation article.
     */
    private Recommandation selectedRecommandationArticle;

    /**
     * The recommandation list.
     */
    private List<Recommandation> recommandationList;

    /**
     * The recommandation article list.
     */
    private List<Recommandation> recommandationArticleList;

    /**
     * The product info checks.
     */
    private List<FileItemCheck> productInfoChecks;

    /**
     * The product info checksfiltred.
     */
    private List<FileItemCheck> productInfoChecksfiltred;

    /**
     * The file item check for decision by file allowed.
     */
    private List<FileItemCheck> fileItemCheckListForDecisionByFileAllowed;

    /**
     * The selected file item check.
     */
    private FileItemCheck selectedFileItemCheck;

    /**
     * The select all decisions.
     */
    private Boolean selectAllDecisions;

    /**
     * The select all roll back.
     */
    private Boolean selectAllRollBack;

    /**
     * The generate report allowed.
     */
    private boolean generateDraftAllowed;

    /**
     * The generate report allowed.
     */
    private Boolean generateReportAllowed;

    private List<FileTypeFlowReport> fileTypeFlowReportsDraft;

    private List<FileTypeFlowReport> fileTypeFlowReports;

    /**
     * The current organism.
     */
    private Organism currentOrganism;

    /**
     * The current service.
     */
    private Service currentService;

    /**
     * The disabled tab synthese.
     */
    private Boolean disabledTabSynthese;

    /**
     * The decision allowed.
     */
    private Boolean decisionAllowed;

    /**
     * The decision by file allowed.
     */
    private Boolean decisionByFileAllowed;

    /**
     * The decision button allowed.
     */
    private Boolean decisionButtonAllowed;

    private boolean userDecisionAllowed;
    private boolean userConfirmationAllowed;

    private Boolean decisionButtonAllowedAtCotationLevel;

    private boolean displayGenerateDraft = true;

    /**
     * The cotation allowed.
     */
    private Boolean cotationAllowed;

    /**
     * The inspector list.
     */
    private List<User> inspectorList;

    /**
     * The enabled decision by file.
     */
    private Boolean enabledDecisionByFile;

    /**
     * The dision system allowed.
     */
    private Boolean disionSystemAllowed;

    /**
     * The show remind decision form.
     */
    private Boolean showRemindDecisionForm;

    /**
     * The show decision.
     */
    private Boolean showDecisionButton;

    /**
     * The show list recommandation article form.
     */
    private Boolean showListRecommandationArticleForm;

    /**
     * The show show attachment form.
     */
    private Boolean showShowAttachmentForm;

    /**
     * The show product details form.
     */
    private Boolean showProductDetailsForm;

    /**
     * The assigned user for cotation.
     */
    private User assignedUserForCotation;

    /**
     * The tab list.
     */
    private List<Tab> tabList;

    /**
     * The tab index list.
     */
    private String tabIndexList;

    /**
     * The inspection report data.
     */
    private InspectionReportData inspectionReportData;

    /**
     * The controller for inspection report.
     */
    private InspectionController controllerForInspectionReport;

    /**
     * The specific decisions history.
     */
    private CctSpecificDecisionHistory specificDecisionsHistory;

    /**
     * The selected item flow dto.
     */
    private ItemFlowDto selectedItemFlowDto;

    /**
     * The inspection controllers.
     */
    private List<InspectionController> inspectionControllers;

    /**
     * The products have same rdd status.
     */
    private Boolean productsHaveSameRDDStatus;

    /**
     * The chcked list size.
     */
    private int chckedListSize;

    /**
     * The analyse order.
     */
    private AnalyseOrder analyseOrder;

    /**
     * The laboratories.
     */
    private List<Laboratory> analyseLaboratories;

    /**
     * The selected laboratory.
     */
    private Laboratory selectedLaboratory;

    /**
     * The analyse type dtos list.
     */
    private List<AnalyseTypeDto> analyseTypeDtosList;

    /**
     * The analyse result.
     */
    private AnalyseResult analyseResult;

    /**
     * The treatment result.
     */
    private TreatmentResult treatmentResult;

    /**
     * The analyse parts list.
     */
    private List<AnalysePart> analysePartsList;

    /**
     * The treatment type dtos list.
     */
    private List<TreatmentTypeDto> treatmentTypeDtosList;

    /**
     * The selected company.
     */
    private TreatmentCompany selectedTreatmentCompany;

    /**
     * The treatment order.
     */
    private TreatmentOrder treatmentOrder;

    private TreatmentInfos treatmentInfos;

    private TreatmentInfosCCSMinsante treatmentInfosCCSMinsante;

    private ApprovedDecision approvedDecision;

    private CCTCPParamValue cCTCPParamValue;

    /**
     * The treatment companys.
     */
    private List<TreatmentCompany> treatmentCompanys;

    /**
     * The treatment parts list.
     */
    private List<TreatmentPart> treatmentPartsList;

    /**
     * The file managers.
     */
    private List<UploadFileManager<AnalysePart>> analysesFileManagers;

    /**
     * The treatment file managers.
     */
    private List<UploadFileManager<TreatmentPart>> treatmentFileManagers;

    /**
     * The analyse report map.
     */
    private Map<Long, StreamedContent> reportMap;

    /**
     * The specific decision.
     */
    private CctSpecificDecision specificDecision;

    private CctSpecificDecision lastSpecificDecision;

    /**
     * The allowed recommandation.
     */
    private Boolean allowedRecommandation;

    /**
     * The item flow history dto list.
     */
    private List<ItemFlowDto> itemFlowHistoryDtoList;

    /**
     * The item flow history dto list filtred.
     */
    private List<ItemFlowDto> itemFlowHistoryDtoListFiltred;

    /**
     * The synthesis config.
     */
    private SynthesisConfig synthesisConfig;

    /**
     * The list params organisms.
     */
    private List<ParamsOrganism> listParamsOrganisms;

    /**
     * The synthesis result.
     */
    private org.guce.siat.core.gr.utils.SynthesisResult synthesisResult;

    /**
     * The authorities list.
     */
    private DualListModel<Authority> authoritiesList;

    /**
     * The authority list.
     */
    private List<Authority> authorityList;

    /**
     * the transaction helper
     */
//    @ManagedProperty(value = "#{transactionHelper}")
//    private TransactionHelper transactionHelper;
    /**
     * The infraction type list.
     */
    private List<Infraction> infractionList;

    /**
     * The infraction.
     */
    private Infraction infraction;

    /**
     * The is minepded ministry.
     */
    private boolean checkMinepdedMinistry;

    /**
     * this is minader ministry
     */
    private boolean checkMinaderMinistry;
    /**
     * this is minepia ministry
     */
    private boolean checkMinepiaMinistry;
    /**
     * Determine if it is last decision step for phyto
     */
    private boolean phytoEnd;

    /**
     * The is payment.
     */
    private Boolean isPayment = Boolean.FALSE;

    /**
     * The payment data.
     */
    private PaymentData paymentData;

    private InvoiceLine selectedInvoiceLine;

    /**
     * The invoice total amount.
     */
    private Long invoiceTotalAmount;

    /**
     * The invoice total ttc amount.
     */
    private Long invoiceTotalTtcAmount;

    /**
     * The invoice other amount.
     */
    private Long invoiceOtherAmount;

    private InterceptionNotification interceptionNotification;

    List<Appointment> relatedAppointments;
    private Appointment appointment;

    private final String MINEPIA_MINISTRY = "MINEPIA";

    private final String TRANSITE_SUPER_FILE_TYPE = "2";
    private final String TRANSITE_SUPER_FILE_TYPE_KEY = "TYPE_DOSSIER_EGUCE";

    private List<DecisionHistory> decisionHistories;
    private boolean maskOfficialPosition;

    private static final int GLOBAL_PROPAGATION_TRANSACTION_BEHAVIOUR = TransactionDefinition.PROPAGATION_REQUIRES_NEW;

    private Long counter;

    private PottingReport pottingReport;

    private CctExportProductType productType;

    private List<FileTypeDto> relatedFileTypesInfos;

    private final CctDetailControllerHelper helper = new CctDetailControllerHelper();

    private static final String COMMON_OBSERVATION_LABEL = "observation";
    private String commonObservation;
    private boolean commonObservationPrintable;
    private boolean commonObservationRequired;

    private Step currentStep;

    private List<FileItemDto> fileItemDtos;

    private List<Country> countriesList;

    private boolean ccsMinsantefFileType;
    private boolean ccsMinsanteFoodProducts;
    private boolean ccsMinsanteDrugProducts;

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        super.initFile();
        if (currentFile == null) {
            goToPreviousPage();
            return;
        }
        currentStep = currentFileItem.getStep();
        checkMinaderMinistry = currentFile.getDestinataire().equalsIgnoreCase(Constants.MINADER_MINISTRY);
        checkMinepiaMinistry = currentFile.getDestinataire().equalsIgnoreCase(MINEPIA_MINISTRY);
        allowedRecommandation = checkIsAllowadRecommandation();
        listUserAuthorityFileTypes = userAuthorityFileTypeService.findUserAuthorityFileTypeByFileTypeAndUserList(currentFile.getFileType(), loggedUser.getMergedDelegatorList());

        countriesList = countryService.findAll();

        getRelatedFileTypesInfos();

        selectedFileItemCheck = new FileItemCheck(getCurrentFileItem(), false, false, false);

        FileFieldValue ptFieldValue = fileFieldValueService.findValueByFileFieldAndFile(CctExportProductType.getFileFieldCode(), currentFile);
        if (ptFieldValue != null) {
            try {
                productType = CctExportProductType.valueOf(ptFieldValue.getValue());
            } catch (Exception ex) {
                logger.error(currentFile.toString(), ex);
            }
        }

        final FileTypeStep fileTypeStep = fileTypeStepService.findFileTypeStepByFileTypeAndStep(selectedFileItemCheck.getFileItem().getFile().getFileType(), selectedFileItemCheck.getFileItem().getStep());
        if (fileTypeStep != null && fileTypeStep.getLabelFr() != null) {
            selectedFileItemCheck.getFileItem().setRedefinedLabelEn((fileTypeStep.getLabelEn()));
            selectedFileItemCheck.getFileItem().setRedefinedLabelFr((fileTypeStep.getLabelFr()));
        }

        loadProductHistoryList();
        // Initialiser la vue du Rappel Décision
        setShowRemindDecisionForm(true);
        // Initialiser la vue de la liste de recommandations article
        setShowListRecommandationArticleForm(true);
        // Initialiser la vue des détails produit
        setShowProductDetailsForm(true);
        // Initialiser la vue de la pièce jointe
        setShowShowAttachmentForm(false);
        // show button to decide
        setShowDecisionButton(true);

        enabledDecisionByFile = true;
        disionSystemAllowed = false;

        fieldGroups = fieldGroupService.findAllByFileType(currentFile.getFileType(), "01".intern());
        fileFieldGroupDtos = RepetableUtil.groupFileFieldValues(currentFile.getNonRepeatablefileFieldValueList(), currentFile.getRepeatablefileFieldValueList(), fieldGroups, applicationPropretiesService, currentFileItem, getCurrentLocale());

        // Remplir la liste des valeurs des filed Values pour le premier article
        loadAndGroupFileItemFieldValues();

        // Récuperer la derniere décision du current File
        findLastDecisions();

        // SET ATTCHMENT TO NULL AND DISABLED VIEW PANEL OF PDF VIEWER
        initAttachmentView();

        resetDataGridInofrmationProducts();
        // load decisions history

        findLastDecisions();

        // Initialize and get file recommandations from database
        refreshRecommandationList();
        // Initialize and get product recommandations from database
        refreshRecommandationArticleList();

        checkGenerateDraftAllowed();
        checkGenerateReportAllowed();

        tabList = new ArrayList<>();
        tabIndexList = concatenateActiveIndexString(tabList);

        authoritiesList = new DualListModel<>(new ArrayList<Authority>(), new ArrayList<Authority>());
        selectedAttachment = null;
        ccsMinsantefFileType = FileTypeCode.CCS_MINSANTE.equals(currentFile.getFileType().getCode());
    }

    /**
     * Checks for authority by current file type.
     *
     * @param authority the authority
     * @return the boolean
     */
    public Boolean hasAuthorityByCurrentFileType(final AuthorityConstants authority) {
        for (final UserAuthorityFileType userAuthorityFileType : listUserAuthorityFileTypes) {
            if (authority.name().equals(userAuthorityFileType.getUserAuthority().getAuthority())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prepare roll back decisions.
     */
    public synchronized void prepareRollBackDecisions() {
        final List<Long> chckedProductInfoChecksList = getCheckedRollBacksFileItemCheckList();
        if (chckedProductInfoChecksList == null || chckedProductInfoChecksList.isEmpty() && cotationAllowed == null
                && !decisionByFileAllowed) {
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.CHOOSE_DECISION_ERROR, null);
        } else if (CollectionUtils.isEmpty(chckedProductInfoChecksList) && BooleanUtils.isTrue(cotationAllowed)) {
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.DISPATCH_ERROR, null);
        } else {
            final RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('" + CONFIRMATION_DIALOG + "').show()".intern());
        }
    }

    /**
     * Checks for cotation rule.
     *
     * @param loggedUser the current user
     * @return true, if successful
     */
    private boolean hasCotationRule(final User loggedUser) {
        boolean hasCotationRule = false;
        for (final UserAuthority userAuthority : loggedUser.getUserAuthorityList()) {
            final Authority authority = userAuthority.getAuthorityGranted();
            if (AuthorityConstants.AGENT_COTATION_1.getCode().equals(authority.getRole())) {
                hasCotationRule = true;
                break;
            }
        }
        return hasCotationRule;
    }

    /**
     * Prepare decisions.
     */
    public synchronized void prepareDecisions() {
        specificDecision = null;
        // Prepare analyse order attribute
        selectedLaboratory = null;
        // Prepare treatment order attribute
        selectedTreatmentCompany = null;

        selectedFlow = null;
        itemFlowService.findLastItemFlowByFileItem(currentFileItem);

        List<FileItemCheck> chckedProductInfoChecksList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(productInfoChecks)) {
            // IF Decision enable for FILE, you set ALL fileItem to Take
            // Decision
            if (BooleanUtils.isTrue(decisionByFileAllowed)) {
                chckedProductInfoChecksList = getFileItemCheckListForDecisionByFileAllowed();
                // This condition is for disabling multi decision
                if (BooleanUtils.isFalse(enabledDecisionByFile)) {
                    showErrorFacesMessage(ControllerConstants.Bundle.Messages.CHOOSE_MULTI_DECISION_ERROR, null);
                    return;
                }
            } else // ELSE : SET CHEEKED FILE
            {
                chckedProductInfoChecksList = selectCheckedFileItemCheck();
            }
            // Show Decsion System Only if we have decision = Étude Approfondie
            if (CollectionUtils.isNotEmpty(chckedProductInfoChecksList)
                    && (StepCode.ST_CT_04.equals(chckedProductInfoChecksList.get(0).getFileItem().getStep().getStepCode())) && !isCcsMinsantefFileType()) {
                setDisionSystemAllowed(true);
            } else {
                disabledTabSynthese = chckedProductInfoChecksList.size() > Constants.ONE;
            }
            if (CollectionUtils.isNotEmpty(chckedProductInfoChecksList)) {
                final FileItem referenceFileItemCheck = chckedProductInfoChecksList.get(0).getFileItem();
                if (referenceFileItemCheck.getStep() != null) {
                    boolean equalsSteps = true;
                    for (final FileItemCheck fileItemCheck : chckedProductInfoChecksList) {
                        if (fileItemCheck.getFileItem().getStep() == null
                                || !referenceFileItemCheck.getStep().getId().equals(fileItemCheck.getFileItem().getStep().getId())) {
                            equalsSteps = false;
                            break;
                        }
                    }

                    if (equalsSteps) {
                        if (isPhyto() || FileTypeCode.CCT_CSV.equals(currentFile.getFileType().getCode())) {
                            flows = flowService.findFlowsByFromStepAndFileType2(referenceFileItemCheck.getStep(), referenceFileItemCheck.getFile().getFileType());
                            List<String> flowsToRemove = new ArrayList<>();
                            for (Flow flx : flows) {
                                if (flx.getIsCota()) {
                                    flowsToRemove.add(flx.getCode());
                                }
                                if (FileTypeCode.CCT_CT_E_PVE.equals(currentFile.getFileType().getCode()) && StepCode.ST_CT_57.equals(currentFileItem.getStep().getStepCode())) {
                                    if (currentFile.getParent() == null && FlowCode.FL_CT_144.name().equals(flx.getCode())) {
                                        flowsToRemove.add(flx.getCode());
                                    } else if (currentFile.getParent() != null && FlowCode.FL_CT_125.name().equals(flx.getCode())) {
                                        flowsToRemove.add(flx.getCode());
                                    }
                                }
                            }
                            flows = deleteFlowFromFlowList(flows, flowsToRemove.toArray(new String[0]));
                        } else if (isCcsMinsantefFileType()) {
                            flows = flowService.findFlowsByFromStepAndFileType2(referenceFileItemCheck.getStep(), referenceFileItemCheck.getFile().getFileType());
                            List<String> flowsToRemove = new ArrayList<>();
                            for (Flow flx : flows) {
                                if (flx.getIsCota()) {
                                    flowsToRemove.add(flx.getCode());
                                }
                            }
                            flows = deleteFlowFromFlowList(flows, flowsToRemove.toArray(new String[0]));
                        } else {
                            flows = flowService.findFlowsByFromStepAndFileType(referenceFileItemCheck.getStep(), referenceFileItemCheck.getFile().getFileType());
                        }
                        // Ajout du flow de renouvellement de RDV ou inspection physique
                        // (FileTypeCode.CCT_CT_E_FSTP.equals(currentFile.getFileType().getCode()) || FileTypeCode.CCT_CT_E_PVI.equals(currentFile.getFileType().getCode()) || FileTypeCode.CCT_CT_E.equals(currentFile.getFileType().getCode())
                        if (isCheckMinaderMinistry() && StepCode.ST_CT_04.equals(referenceFileItemCheck.getStep().getStepCode()) && Arrays.asList(FileTypeCode.CCT_CT_E_FSTP, FileTypeCode.CCT_CT_E_PVI, FileTypeCode.CCT_CT_E, FileTypeCode.CCT_CT_E_PVE).contains(currentFile.getFileType().getCode())) {
                            String flc = FlowCode.FL_CT_118.name();
                            if (FileTypeCode.CCT_CT_E.equals(currentFile.getFileType().getCode())) {
                                flc = FlowCode.FL_CT_119.name();
                            }
                            Flow tmpFlow = flowService.findFlowByCode(flc);
                            if (tmpFlow != null) {
                                flows.add(tmpFlow);
                            }
                        }

//                        if (checkMinepiaMinistry && referenceFileItemCheck.getStep().getStepCode().equals(StepCode.ST_CT_04)) {
//                            //"FL_CT_CVS_05,FL_CT_CVS_08,FL_CT_CVS_09";
//                            List<String> MINEPIA_FLOW_CODE_LIST = Arrays.asList(FlowCode.FL_CT_CVS_05.name(), FlowCode.FL_CT_CVS_08.name(), FlowCode.FL_CT_CVS_09.name());
//                            List<String> flowsToRemove = new ArrayList<>();
//                            for (Flow f : flows) {
//                                if (!MINEPIA_FLOW_CODE_LIST.contains(f.getCode())) {
//                                    flowsToRemove.add(f.getCode());
//                                }
//                            }
//
//                            flows = deleteFlowFromFlowList(flows, flowsToRemove.toArray(new String[0]));
//
//                        }
                        productsHaveSameRDDStatus = productsHaveSameRDDStatus(chckedProductInfoChecksList);

                        /*
						 * Si plusieurs produits sont sélectionnés et ils n’ont pas le même statut par rapport aux décisions
						 * RDD précédentes, On supprime les flux de destructions dans la liste des décisions et on affiche un
						 * message informatif dans le popup de décision informant le client que les produits sélectionnés
						 * n’ont pas le même statut concernant les décisions de destruction.
                         */
                        if (DECISION_STEPS_LIST.contains(referenceFileItemCheck.getStep().getStepCode()) && !productsHaveSameRDDStatus) {
                            flows = deleteFlowFromFlowList(flows, FlowCode.FL_CT_11.name(), FlowCode.FL_CT_13.name(), FlowCode.FL_CT_33.name(), FlowCode.FL_CT_35.name());
                        } /*
						 * Si le/les produit(s) sélectionnés ont atteints la valeur seuil des RDD (nbRDD) --> il faut
						 * supprimer le flux "RDD" dans la liste des décisions sinon il faut supprimer le flux "RDD Final"
						 * dans la liste des décisions.
                         */ else if (DECISION_STEPS_LIST.contains(referenceFileItemCheck.getStep().getStepCode()) && productsHaveSameRDDStatus) {
                            final Long nbrRDDReference = itemFlowService.findNbrDecisionByFileItemHistory(RDD_FLOW_CODES, referenceFileItemCheck);
                            final Long nbRDDParam = paramsOrganismService.findLongParamsOrganismByOrganismAndName(referenceFileItemCheck.getFile().getBureau().getService().getSubDepartment().getOrganism(), "nbRDD");

                            if (nbRDDParam != null && nbrRDDReference >= nbRDDParam) {
                                flows = deleteFlowFromFlowList(flows, FlowCode.FL_CT_11.name(), FlowCode.FL_CT_35.name());
                            } else {
                                flows = deleteFlowFromFlowList(flows, FlowCode.FL_CT_13.name(), FlowCode.FL_CT_33.name());
                            }
                        }

                        boolean payed = false;
                        for (ItemFlowDto hist : itemFlowHistoryDtoList) {
                            if (hist.getItemFlow() != null && hist.getItemFlow().getFlow() != null && Arrays.asList(FlowCode.FL_CT_93.name(), FlowCode.FL_CT_160.name()).contains(hist.getItemFlow().getFlow().getCode())) {
                                payed = true;
                                Iterator<Flow> it = flows.iterator();
                                while (it.hasNext()) {
                                    Flow flx = it.next();
                                    if (Arrays.asList(FlowCode.FL_CT_92.name(), FlowCode.FL_CT_158.name()).contains(flx.getCode())) {
                                        it.remove();
                                    }
                                }
                            }
                        }
                        if (!payed && !(FileTypeCode.CCT_CT_E_PVI.equals(currentFile.getFileType().getCode())
                                || FileTypeCode.CCT_CT_E_ATP.equals(currentFile.getFileType().getCode())
                                || FileTypeCode.CCT_CT_E_FSTP.equals(currentFile.getFileType().getCode())
                                || FileTypeCode.CCT_CT_E_PVE.equals(currentFile.getFileType().getCode()))) {
                            final Iterator<Flow> it = flows.iterator();
                            while (it.hasNext()) {
                                Flow flx = it.next();
                                if (!flx.getIsCota() && flx.getCode().equals(FlowCode.FL_CT_32.name())) {
                                    it.remove();
                                }
                            }
                        } // En Cas de Réexamen: pour différencier les DSC et EA
                        else if (StepCode.ST_CT_22.equals(referenceFileItemCheck.getStep().getStepCode())) {
                            ItemFlow lastOutoingItemFlow = itemFlowService.findLastOutgoingItemFlowByFileItem(referenceFileItemCheck);
                            if (FlowCode.FL_CT_12.name().equals(lastOutoingItemFlow.getFlow().getCode())
                                    || FlowCode.FL_CT_50.name().equals(lastOutoingItemFlow.getFlow().getCode())) {
                                flows = deleteFlowFromFlowList(flows, FlowCode.FL_CT_53.name());
                            } else if (FlowCode.FL_CT_73.name().equals(lastOutoingItemFlow.getFlow().getCode())
                                    || FlowCode.FL_CT_50.name().equals(lastOutoingItemFlow.getFlow().getCode())) {
                                flows = deleteFlowFromFlowList(flows, FlowCode.FL_CT_52.name());
                            }
                        }
                        for (Flow flow : flows) {
                            //to redefine flows labelFr, and labelEn
                            FileTypeFlow fileTypeFlow = fileTypeFlowService.findByFlowAndFileType(currentFile.getFileType(), flow);
                            if (fileTypeFlow != null) {
                                flow.setRedefinedLabelEn(fileTypeFlow.getLabelEn());
                                flow.setRedefinedLabelFr(fileTypeFlow.getLabelFr());
                            }
                        }
                        selectedFlow = null;
                        decisionDiv.getChildren().clear();

                        RiskController riskController = getInstanceOfRiskController();
                        riskController.generateSystemDecisionAndSynthesisResults(referenceFileItemCheck);
                        RequestContext requestContext = RequestContext.getCurrentInstance();
                        requestContext.execute("PF('" + DECISION_DIALOG + "').show();");
                    } else {
                        showErrorFacesMessage(ControllerConstants.Bundle.Messages.SAME_STEPS_ERROR, null);
                    }
                }
            } else {
                showErrorFacesMessage(ControllerConstants.Bundle.Messages.CHOOSE_DECISION_ERROR, null);
            }
        }
        chckedListSize = chckedProductInfoChecksList.size();
    }

    /**
     * Gets the instance of risk controller.
     *
     * @return the instance of risk controller
     */
    public RiskController getInstanceOfRiskController() {
        final FacesContext fctx = FacesContext.getCurrentInstance();
        final Application application = fctx.getApplication();
        final ELContext context = fctx.getELContext();
        final ExpressionFactory expressionFactory = application.getExpressionFactory();
        final ValueExpression createValueExpression = expressionFactory.createValueExpression(context, "#{riskController}",
                RiskController.class);
        return (RiskController) createValueExpression.getValue(context);
    }

    /**
     * Products have same rdd status.
     *
     * @param fileItemCheckList the file item check list
     * @return the boolean
     */
    private Boolean productsHaveSameRDDStatus(final List<FileItemCheck> fileItemCheckList) {
        final FileItem referenceFileItemCheck = fileItemCheckList.get(0).getFileItem();

        // find number of RDD decision for the referennce fileItem
        final Long nbrRDDReference = itemFlowService.findNbrDecisionByFileItemHistory(RDD_FLOW_CODES, referenceFileItemCheck);

        boolean productsHavedifferentRDDStatus = true;
        for (final FileItemCheck fileItemCheck : fileItemCheckList) {
            // find number of RDD decision for the fileItem
            final Long nbrRDD = itemFlowService.findNbrDecisionByFileItemHistory(RDD_FLOW_CODES, fileItemCheck.getFileItem());
            if (!Objects.equals(nbrRDD, nbrRDDReference)) {
                productsHavedifferentRDDStatus = false;
                break;
            }
        }

        return productsHavedifferentRDDStatus;
    }

    /**
     * Delete flow from flow list.
     *
     * @param decisionList the decision list
     * @param deletedFlowCode the deleted flow code
     * @return the list
     */
    private List<Flow> deleteFlowFromFlowList(final List<Flow> decisionList, final String... deletedFlowCode) {
        final Iterator<Flow> iterator = decisionList.iterator();
        while (iterator.hasNext()) {
            final Flow flow = iterator.next();
            if (Arrays.asList(deletedFlowCode).contains(flow.getCode())) {
                iterator.remove();
            }
        }

        return decisionList;
    }

    /**
     * Gets the checked roll backs file item check list.
     *
     * @return the checked roll backs file item check list
     */
    public List<Long> getCheckedRollBacksFileItemCheckList() {
        List<FileItemCheck> checks = new ArrayList<>();
        if (BooleanUtils.isTrue(cotationAllowed) && currentFile.getFileItemsList() != null) {
            for (final FileItem fileItem : currentFile.getFileItemsList()) {
                final FileItemCheck check = new FileItemCheck(fileItem, false, true, false);
                checks.add(check);
            }
        } else if (BooleanUtils.isTrue(decisionByFileAllowed)) {
            for (final FileItemCheck fileItemCheck : getFileItemCheckListForDecisionByFileAllowed()) {
                fileItemCheck.setRollbackCheck(true);
                checks.add(fileItemCheck);
            }
        } else {
            checks = productInfoChecks;
        }

        final List<Long> returnedList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(checks)) {

            for (final FileItemCheck fileItemCheck : checks) {
                if (fileItemCheck.getRollbackCheck()) {
                    returnedList.add(fileItemCheck.getFileItem().getId());
                }
            }

        }
        return returnedList;
    }

    /**
     * Select checked file item check.
     *
     * @return the list
     */
    public List<FileItemCheck> selectCheckedFileItemCheck() {
        final List<FileItemCheck> selectedProducts = (List<FileItemCheck>) CollectionUtils.select(productInfoChecks,
                new Predicate() {
            @Override
            public boolean evaluate(final Object object) {
                return ((FileItemCheck) object).getDecisionCheck();
            }
        });
        return selectedProducts;
    }

    /**
     * Select all decisions handler.
     */
    public void selectAllDecisionsHandler() {
        if (CollectionUtils.isNotEmpty(productInfoChecks)) {
            for (final FileItemCheck fileItemCheck : productInfoChecks) {
                if (!fileItemCheck.getFileItem().getDraft() && fileItemCheck.getEnabledCheck()) {
                    fileItemCheck.setDecisionCheck(selectAllDecisions);
                }
            }
        }
        refreshViewForProductInformations();
    }

    /**
     * Select all rolls back handler.
     */
    public void selectAllRollsBackHandler() {
        if (CollectionUtils.isNotEmpty(productInfoChecks)) {
            for (final FileItemCheck fileItemCheck : productInfoChecks) {
                if (fileItemCheck.getFileItem().getDraft() && fileItemCheck.getEnabledCheck()) {
                    fileItemCheck.setRollbackCheck(selectAllRollBack);
                }
            }
        }
    }

    /**
     * Show attachment.
     */
    public void showAttachment() {

        selectedFileItemCheck = null;
        setShowRemindDecisionForm(false);
        setShowListRecommandationArticleForm(false);
        setShowProductDetailsForm(false);
        setShowShowAttachmentForm(true);
        // Hide button
        setShowDecisionButton(false);
        final AttachmentController attachmentController = getInstanceOfPageAttachmentController();
        attachmentController.setSelectedAttachment(selectedAttachment);
        attachmentController.init();
    }

    /**
     * Gets the instance of page attachment controller.
     *
     * @return the instance of page attachment controller
     */
    public AttachmentController getInstanceOfPageAttachmentController() {
        final FacesContext fctx = FacesContext.getCurrentInstance();
        final Application application = fctx.getApplication();
        final ELContext context = fctx.getELContext();
        final ExpressionFactory expressionFactory = application.getExpressionFactory();
        final ValueExpression createValueExpression = expressionFactory.createValueExpression(context, "#{attachmentController}",
                AttachmentController.class);
        return (AttachmentController) createValueExpression.getValue(context);
    }

    /**
     * Inits the attachment view.
     */
    public void initAttachmentView() {
        final AttachmentController attachmentController = getInstanceOfPageAttachmentController();
        attachmentController.setSelectedAttachment(null);
        attachmentController.setShowPanelViewJpeg(false);
        attachmentController.setShowPanelViewPdf(false);
        attachmentController.init();
    }

    /**
     * Gets the product info items.
     *
     * @return the product info items
     */
    public List<FileItem> getProductInfoItems() {
        // productInfoItems = initFileItemsForCCt();

        refreshViewForProductInformations();

        if (BooleanUtils.isTrue(comeFromSearch)) {
            checkIsAllowedUserForCurrentStep();
        }
        return productInfoItems;
    }

    /**
     * Inits the file items for c ct.
     *
     * @return the list
     */
    private List<FileItem> initFileItemsForCCt() {
        Boolean decisionByFile = Boolean.TRUE;
        Boolean errorDecisionByFile = Boolean.TRUE;
        setDecisionAllowed(Boolean.TRUE);
        // Boolean for Send Button and Rollback Button in ProductInformation
        if (productInfoItems == null) {
            // all products
            productInfoItems = fileItemService.findFileItemsByFile(getCurrentFile());

            for (final FileItem fileItem : productInfoItems) {
                final FileTypeStep fileTypeStep = fileTypeStepService.findFileTypeStepByFileTypeAndStep(fileItem.getFile()
                        .getFileType(), fileItem.getStep());
                if (fileTypeStep != null && fileTypeStep.getLabelFr() != null) {
                    fileItem.setRedefinedLabelEn((fileTypeStep.getLabelEn()));
                    fileItem.setRedefinedLabelFr((fileTypeStep.getLabelFr()));
                }
            }

            // allowed products
//            productInfoItemsEnabled = fileItemService.filterFileItemsByUserAuthorityFileTypes(productInfoItems, listUserAuthorityFileTypes, loggedUser);
            productInfoItemsEnabled = new ArrayList<>(productInfoItems);

            final Iterator<FileItem> iter = productInfoItemsEnabled.iterator();
            while (iter.hasNext()) {
                final FileItem fileItem = iter.next();
                for (final ItemFlow flow : fileItem.getItemFlowsList()) {
                    if (fileItem.getStep().equals(flow.getFlow().getToStep())
                            && !commonService.showEnabledFileItem(fileItem, flow, getLoggedUser())) {
                        iter.remove();
                    }
                }
            }

            if (CollectionUtils.isEmpty(productInfoItemsEnabled)) {
                decisionAllowed = Boolean.FALSE;
                decisionButtonAllowed = Boolean.FALSE;
                decisionButtonAllowedAtCotationLevel = Boolean.FALSE;
            }
            // GET THE STEP CODE FROM THE FIRST FILEITEM IN THE FOLDER TO
            // COMPARE IT WITH OTHER FILEITEM
            final Long oneCurrentStep = productInfoItems.get(0).getStep().getId();
            // GET ALL FILEITEMS
            for (final FileItem fileItem : productInfoItems) {
                if (oneCurrentStep.equals(fileItem.getStep().getId()) && StepCode.ST_CT_02.equals(fileItem.getStep().getStepCode())) {
                    decisionByFile = Boolean.TRUE;
                    setCotationAllowed(Boolean.FALSE);
                } else if (oneCurrentStep.equals(fileItem.getStep().getId())
                        && Arrays.asList(StepCode.ST_CT_03, StepCode.ST_CT_47, StepCode.ST_CT_53, StepCode.ST_CT_62).contains(fileItem.getStep().getStepCode())
                        && hasCotationRule(loggedUser)) {
                    decisionByFile = Boolean.TRUE;
                    setCotationAllowed(Boolean.TRUE);
                    setDecisionAllowed(Boolean.TRUE);
                    if (COTATION_STEP_LIST_ALLOW_DECISION.contains(fileItem.getStep().getStepCode())) {
                        setDecisionButtonAllowedAtCotationLevel(Boolean.TRUE);
                    }
                    StepCode sc = fileItem.getStep().getStepCode();
                    if (StepCode.ST_CT_03.equals(sc) && isCheckMinepiaMinistry() && !BooleanUtils.toBoolean(sendDecisionAllowed)) {
                        setDecisionButtonAllowed(Boolean.TRUE);
                    }
                } else {
                    errorDecisionByFile = Boolean.TRUE;
                    setCotationAllowed(Boolean.FALSE);
                }

            }
            // IF THE FILEITEMS HAS THE SAME STEPCODE
            if (decisionByFile && errorDecisionByFile) {
                decisionByFileAllowed = Boolean.TRUE;
                final List<FileItemCheck> fileItemChecks = new ArrayList<>();
                for (final FileItem fileItem : productInfoItems) {
                    final FileItemCheck fileItemCheck = new FileItemCheck(fileItem, false, false, false);
                    fileItemChecks.add(fileItemCheck);
                    if (!fileItem.getDraft()) {
                        setEnabledDecisionByFile(true);
                    }
                }
                setFileItemCheckListForDecisionByFileAllowed(fileItemChecks);
            } else {
                setDecisionByFileAllowed(false);
            }
        }

        if (productInfoItems != null && BooleanUtils.isFalse(productInfoItems.get(0).getDraft())) {
            rollBackDecisionsAllowed = Boolean.FALSE;
            sendDecisionAllowed = Boolean.FALSE;
        } else {
            rollBackDecisionsAllowed = Boolean.TRUE;
            sendDecisionAllowed = Boolean.TRUE;
        }
        return productInfoItems;
    }

    /**
     * Gets the instance of page schedule controller.
     *
     * @return the instance of page schedule controller
     */
    public ScheduleController getInstanceOfPageScheduleController() {
        final FacesContext fctx = FacesContext.getCurrentInstance();
        final Application application = fctx.getApplication();
        final ELContext context = fctx.getELContext();
        final ExpressionFactory expressionFactory = application.getExpressionFactory();
        final ValueExpression createValueExpression = expressionFactory.createValueExpression(context, "#{scheduleController}",
                ScheduleController.class);
        return (ScheduleController) createValueExpression.getValue(context);
    }

    /**
     * Change laboratory handler.
     */
    public void changeLaboratoryHandler() {
        analyseTypeDtosList = new ArrayList<>();
        for (final AnalyseType analyseType : selectedLaboratory.getAnalyseTypeList()) {
            final AnalyseTypeDto analyseTypeDto = new AnalyseTypeDto();
            analyseTypeDto.setAnalyseType(analyseType);
            analyseTypeDto.setChecked(Boolean.FALSE);
            analyseTypeDtosList.add(analyseTypeDto);
        }
    }

    /**
     * Change laboratory handler.
     */
    public void changeTreatmentCompanyHandler() {
        treatmentTypeDtosList = new ArrayList<>();

        for (final TreatmentType treatmentType : selectedTreatmentCompany.getTreatmentTypeList()) {
            final TreatmentTypeDto analyseTypeDto = new TreatmentTypeDto();
            analyseTypeDto.setTreatmentType(treatmentType);
            analyseTypeDto.setChecked(Boolean.FALSE);
            treatmentTypeDtosList.add(analyseTypeDto);
        }
    }

    /**
     * Change decision handler.
     *
     * @throws java.text.ParseException
     */
    public void changeDecisionHandler() throws ParseException {
        specificDecision = null;
        relatedAppointments = null;
        decisionDiv.getChildren().clear();
        final List<FileItemCheck> checksProduct = selectCheckedFileItemCheck();
        isPayment = Boolean.FALSE;
        if (FlowCode.FL_CT_92.name().equals(selectedFlow.getCode()) || FlowCode.FL_CT_158.name().equals(selectedFlow.getCode())) {
            isPayment = Boolean.TRUE;
            paymentData = new PaymentData();
            paymentData.setPaymentItemFlowList(new ArrayList<PaymentItemFlow>());
            if (FlowCode.FL_CT_158.name().equals(selectedFlow.getCode())) {
                FileItem fi = currentFile.getFileItemsList().get(0);
                paymentData.setRefFacture(currentFile.getNumeroDossier());
                paymentData.getPaymentItemFlowList().add(new PaymentItemFlow(false, fi.getId(), fi.getNsh()));
            } else {
                for (final FileItemCheck fileItemCheck : checksProduct) {
                    if (fileItemCheck.getDecisionCheck()) {
                        paymentData.getPaymentItemFlowList().add(new PaymentItemFlow(false, fileItemCheck.getFileItem().getId()));
                    }
                }
            }
        }

        if (isCsvBeforeTreatment(selectedFlow)) {
            inspectorList = userService.findCotationsAgentByBureauAndRole(currentFile.getBureau(), AuthorityConstants.SOCIETE_TRAITEMENT.getCode());
            if (currentFile.getAssignedUser() != null) {
                assignedUserForCotation = currentFile.getAssignedUser();
            }
        }

        if (isPhytoBilling(selectedFlow) || Arrays.asList(FlowCode.FL_CT_159.name(), FlowCode.FL_CT_174.name()).contains(selectedFlow.getCode())) {
            counter = -1L;
            invoiceTotalAmount = 0L;
            if (!Arrays.asList(FileTypeCode.CCT_CSV).contains(currentFile.getFileType().getCode())) {
                specificDecision = CctSpecificDecision.CCT_CT_E_BILL;
            }
            paymentData = paymentDataService.findPaymentDataByFileItem(currentFileItem);
            if (paymentData == null) {
                paymentData = new PaymentData();
                paymentData.setMontantTva(0L);
                paymentData.setLieuSignature(currentFile.getBureau().getLabelFr());
                paymentData.setNomSignature(String.format("%s %s", loggedUser.getLastName(), loggedUser.getFirstName()));
                paymentData.setDateSignature(java.util.Calendar.getInstance().getTime());
                paymentData.setNatureFrais("Frais ".concat(currentFile.getFileType().getLabelFr()));
                paymentData.setInvoiceLines(new ArrayList<InvoiceLine>());
            } else {
                invoiceTotalAmount = paymentData.getMontantHt();
            }
        }

        List<FileTypeCode> codes = new ArrayList<>(Arrays.asList(FileTypeCode.CCT_CT_E_FSTP, FileTypeCode.CCT_CT_E_PVE, FileTypeCode.CCT_CT_E_PVI));
        if (isPhytoAppointment(selectedFlow)) {
            specificDecision = CctSpecificDecision.CCT_CT_E_APP;

            codes.remove(currentFile.getFileType().getCode());

            relatedAppointments = appointmentService.findRelatedAppointments(currentFile, codes.toArray(new FileTypeCode[0]));

            Appointment app;
            if (Arrays.asList(FlowCode.FL_CT_118.name()).contains(selectedFlow.getCode())) {
                appointment = appointmentService.findAppointmentByFileItem(currentFileItem);
                if (appointment == null) {
                    appointment = new Appointment();
                    app = appointmentService.findDeletedAppointmentByFileItem(currentFileItem);
                    if (app != null) {
                        appointment.setBeginTime(app.getBeginTime());
                    }
                }
            } else {
                appointment = new Appointment();
            }

            appointment.setController(getLoggedUser());
            appointment.setBundle("Appointment_".concat(currentFile.getFileType().getCode().name()));
        }

        // Rendez-vous
        if (APPOINTMENT_DECISIONS_LIST.contains(selectedFlow.getCode())) {
            specificDecision = CctSpecificDecision.APP;
            final ScheduleController scheduleController = getInstanceOfPageScheduleController();
            scheduleController.setCurrentService(getCurrentFile().getBureau().getService());
            scheduleController.init();
            if (FlowCode.FL_CT_42.name().equals(selectedFlow.getCode()) || lastDecisions.getFlow().getCode().equals(FlowCode.FL_CT_27.toString())) {
                final List<ItemFlow> lastItemFlowList = itemFlowService.findLastItemFlowsByFileItemList(productInfoItems);
                final Appointment appointment1 = appointmentService.findAppointmentByItemFlowList(lastItemFlowList);
                if (appointment1 != null) {
                    scheduleController.getAppointmentList().remove(appointment1);
                    scheduleController.createNewEventFromApoitment(appointment1);
                }
            }
        } else if (isPviReadyForSignature(selectedFlow)) {
            inspectionReportData = new InspectionReportData();
            InspectionReport ir = inspectionReportService.findLastInspectionReportsByFileItem(currentFileItem);
            if (ir == null && currentFile.getParent() != null) {
                ir = inspectionReportService.findLastInspectionReportsByFileItem(currentFile.getParent().getFileItemsList().get(0));
            }

            if (ir != null) {
                inspectionReportData.from(ir);
            }
            prepareGoodsForModif();
        } // Signature de DCC (Certificat de Contrôle Documentaire)
        else if (DCC_FLOW_CODES.contains(selectedFlow.getCode())) {
            lastDecisions = itemFlowService.findLastSentItemFlowByFileItem(selectedFileItemCheck.getFileItem());

            approvedDecision = approvedDecisionService.findApprovedDecisionByItemFlow(lastDecisions);

            maskOfficialPosition = selectedFlow.getCode().equals(FlowCode.FL_CT_CVS_05.name());

            if (approvedDecision == null) {
                approvedDecision = new ApprovedDecision();
                fillApprovedDecision(approvedDecision);
                if (selectedFlow.getCode().equals(FlowCode.FL_CT_CVS_03.name())) {
                    approvedDecision.setOfficialPosition("Chef du Poste d'inspection Sanitaire Vétérinaire");
                }
            } else {
                approvedDecision.setOfficialPosition("Chef du Poste d'inspection Sanitaire Vétérinaire");
            }

            specificDecision = CctSpecificDecision.DCC;
        } // Saisie Constat
        else if (CONSTAT_FLOW_LIST.contains(selectedFlow.getCode())) {
            checkMinepdedMinistry = false;
            if (currentFileItem.getSubfamily() != null && currentFileItem.getSubfamily().getService() != null
                    && currentFileItem.getSubfamily().getService().getSubDepartment() != null
                    && currentFileItem.getSubfamily().getService().getSubDepartment().getOrganism() != null
                    && currentFileItem.getSubfamily().getService().getSubDepartment().getOrganism().getMinistry() != null
                    && currentFileItem.getSubfamily().getService().getSubDepartment().getOrganism().getMinistry().getLabelFr().equals(Constants.MINEPDED_MINISTRY)) {
                checkMinepdedMinistry = true;
            }
            infraction = new Infraction();
            infractionList = infractionService.findAll();
            specificDecision = CctSpecificDecision.IR;
            controllerForInspectionReport = null;
            inspectionControllers = new ArrayList<>();
            final String dataTableId = ComponentUtils.findComponentClientId("controllersDT");
            RequestContext.getCurrentInstance().update(dataTableId);
            inspectionReportData = new InspectionReportData();
            inspectionReportData.setSamples(new ArrayList<Sample>());
            inspectionReportData.setEtiquetageList(new ArrayList<InspectionReportEtiquetageVo>());
            inspectionReportData.setControllers(new ArrayList<InspectionController>());

            inspectionReportData.setTemperatureList(new ArrayList<InspectionReportTemperatureVo>());
            for (final FileItem fileItem : productInfoItemsEnabled) {
                final Sample echantillon = new Sample();
                final InspectionReportTemperatureVo temperature = new InspectionReportTemperatureVo();
                final InspectionReportEtiquetageVo etiquetage = new InspectionReportEtiquetageVo();
                echantillon.setFileItem(fileItem);
                temperature.setFileItem(fileItem);
                etiquetage.setFileItem(fileItem);
                inspectionReportData.getSamples().add(echantillon);
                inspectionReportData.getTemperatureList().add(temperature);
                inspectionReportData.getEtiquetageList().add(etiquetage);
                RequestContext.getCurrentInstance().execute("PF('controllersDTWdg').filter()");
            }
        } // Demande d Analyse
        else if (FlowCode.FL_CT_29.name().equals(selectedFlow.getCode())) {
            selectedLaboratory = null;
            analyseOrder = new AnalyseOrder();
            analyseOrder.setDate(java.util.Calendar.getInstance().getTime());
            analyseTypeDtosList = null;

            if (chckedListSize == Constants.ONE) {
                specificDecision = CctSpecificDecision.AN;
                analyseOrder = new AnalyseOrder();
                analyseOrder.setSample(commonService.findSampleByFileItem(checksProduct.get(0).getFileItem()));

                analyseLaboratories = laboratoryService.findByAdministration(getLoggedUser().getAdministration());
            } else {
                showErrorFacesMessage(ControllerConstants.Bundle.Messages.CHECK_ANALYSE_DECISION_ERROR, ControllerConstants.Bundle.Messages.CHECK_PRODUCTS_DECISION_MSG);

            }
        } // fourniture des informations relatives au traitement
        else if (isPhytoReadyForSignature(selectedFlow)) {
            treatmentInfos = createTreatmentInfos();
            prepareGoodsForModif();
        } // Signature effective du certificat phytosanitaire
        else if (isPhytoReadyForSignatureEnd(selectedFlow)) {
            buildGenericFormFromDataType(selectedFlow.getDataTypeList());
            cCTCPParamValue = cCTCPParamValueService.findCCTCPParamValueByItemFlow(lastDecisions);

            if (cCTCPParamValue == null) {
                cCTCPParamValue = new CCTCPParamValue();
                ReportGeneratorUtils.fillCCTCPParamValue(cCTCPParamValue, getLoggedUser(), currentFile);
            }

            specificDecision = CctSpecificDecision.CCT_CP;
        }// Demande de Traitement
        else if (FlowCode.FL_CT_64.name().equals(selectedFlow.getCode())) {
            treatmentOrder = new TreatmentOrder();
            treatmentOrder.setDate(java.util.Calendar.getInstance().getTime());
            treatmentTypeDtosList = null;
            selectedTreatmentCompany = null;
        } // Envoie Résultat d Analyse
        else if (FlowCode.FL_CT_31.name().equals(selectedFlow.getCode())) {
            if (chckedListSize == Constants.ONE) {
                specificDecision = CctSpecificDecision.ANR;
                lastDecisions = itemFlowService.findLastSentItemFlowByFileItem(selectedFileItemCheck.getFileItem());

                final AnalyseOrder lastAnalyseOrder = analyseOrderService.findByItemFlow(lastDecisions);
                analyseResult = new AnalyseResult();
                analyseResult.setAnalyseOrder(lastAnalyseOrder);
                analysesFileManagers = new ArrayList<>();
                for (final AnalysePart analysePart : lastAnalyseOrder.getAnalysePartsList()) {
                    final UploadFileManager<AnalysePart> fileManager = new UploadFileManager<>();
                    fileManager.setPart(analysePart);
                    analysesFileManagers.add(fileManager);
                }
            } else {
                showErrorFacesMessage(ControllerConstants.Bundle.Messages.CHECK_ANALYSE_DECISION_ERROR, ControllerConstants.Bundle.Messages.CHECK_PRODUCTS_DECISION_MSG);
            }
        } // Envoie Résultat de Traitement
        else if (isFstpReadyForSignature(selectedFlow) || isAtReadyForSignature(selectedFlow)) {
            TreatmentResult tr = treatmentResultService.findLastTreatmentResultByFileItem(currentFileItem);
            if (tr == null && currentFile.getParent() != null) {
                tr = treatmentResultService.findLastTreatmentResultByFileItem(currentFile.getParent().getFileItemsList().get(0));
            }
            treatmentResult = new TreatmentResult();
            if (tr != null) {
                org.springframework.beans.BeanUtils.copyProperties(tr, treatmentResult, "id", "itemFlow");
            }
        } else if (FlowCode.FL_CT_66.name().equals(selectedFlow.getCode())) {
            if (chckedListSize == Constants.ONE) {
                specificDecision = CctSpecificDecision.TRR;
                lastDecisions = itemFlowService.findLastSentItemFlowByFileItem(selectedFileItemCheck.getFileItem());
                final TreatmentOrder lastTreatmentOrder = treatmentOrderService.findTreatmentOrderByItemFlow(lastDecisions);
                treatmentResult = new TreatmentResult();
                treatmentResult.setTreatmentOrder(lastTreatmentOrder);
                treatmentFileManagers = new ArrayList<>();
                if (lastTreatmentOrder != null) {
                    for (final TreatmentPart tratmentPart : lastTreatmentOrder.getTreatmentPartsList()) {
                        final UploadFileManager<TreatmentPart> fileManager = new UploadFileManager<>();
                        fileManager.setPart(tratmentPart);
                        treatmentFileManagers.add(fileManager);
                    }
                }
            } else {
                showErrorFacesMessage(ControllerConstants.Bundle.Messages.CHECK_ANALYSE_DECISION_ERROR, ControllerConstants.Bundle.Messages.CHECK_PRODUCTS_DECISION_MSG);
            }
        } else if (isCCSMinsanteReadyForSignature(selectedFlow)) {
            ItemFlow itemFlow;
            TreatmentInfosCCSMinsante tr = null;
            treatmentInfosCCSMinsante = new TreatmentInfosCCSMinsante();
            if (selectedFlow.getCode().equals(FlowCode.FL_CT_07.name())) {
                itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(currentFileItem, FlowCode.FL_CT_07);
                if (itemFlow != null) {
                    tr = treatmentInfosCCSMinsanteService.findTreatmentInfosByItemFlow(itemFlow);
                }
            } else if (selectedFlow.getCode().equals(FlowCode.FL_CT_CCS_03.name())) {
                File updatedFile = fileService.findByNumDossierGuce(currentFile.getNumeroDossierBase());
                if (updatedFile != null) {
                    FileItem updatedFileFileItem = updatedFile.getFileItemsList().get(0);
                    itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(updatedFileFileItem, FlowCode.FL_CT_07);
                    if (itemFlow != null) {
                        tr = treatmentInfosCCSMinsanteService.findTreatmentInfosByItemFlow(itemFlow);
                    }
                }
            }
            if (tr != null) {
                try {
                    BeanUtils.copyProperties(treatmentInfosCCSMinsante, tr);
                    if (treatmentInfosCCSMinsante != null && treatmentInfosCCSMinsante.getId() != null) {
                        treatmentInfosCCSMinsante.setId(null);
                    }
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    Logger.getLogger(FileItemCctDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } // Generic : construction du formulaire à partir des DataType
        else {
            buildGenericFormFromDataType(selectedFlow.getDataTypeList());
        }

        if (isPveReadyForSignature(selectedFlow)) {

            pottingReport = pottingReportService.findPottingReportByFile(currentFile, false);

            if (pottingReport == null && currentFile.getParent() != null) {
                pottingReport = pottingReportService.findPottingReportByFile(currentFile.getParent());
                if (pottingReport != null) {
                    PottingReport pottingReportNew = new PottingReport();
                    org.springframework.beans.BeanUtils.copyProperties(pottingReport, pottingReportNew, "id", "file", "validated");
                    pottingReport = new PottingReport();
                    org.springframework.beans.BeanUtils.copyProperties(pottingReportNew, pottingReport);
                } else {
                    pottingReport = new PottingReport();
                }
                pottingReport.setFile(currentFile);
            }

            pottingReport.setPottingController(getLoggedUser());
        } else if (isAppointmentOkForPve(selectedFlow)) {
            if (FlowCode.FL_CT_104.name().equals(selectedFlow.getCode())) {
                pottingReport = new PottingReport();
                pottingReport.setFile(currentFile);
                pottingReport.setPottingController(getLoggedUser());
            } else {
                pottingReport = pottingReportService.findPottingReportByFile(currentFile, false);
                pottingReport.setAppointmentDateBack(pottingReport.getAppointmentDate());
            }
        }
    }

    private void buildGenericFormFromDataType(List<DataType> dataTypes) {
        for (final DataType dataType : dataTypes) {

            if (BooleanUtils.toBoolean(dataType.getDisabled())) {
                continue;
            }

            final String dataTypeProps = dataType.getProps();
            Properties properties = new Properties();
            if (dataTypeProps != null) {
                try {
                    properties.load(new StringReader(dataTypeProps));
                } catch (IOException ex) {
                    logger.debug("Problem occured when trying to load properties of data type : " + dataType, ex);
                }
            }

            String fileTypesAllowed = properties.getProperty(DataTypePropEnnum.FILE_TYPES.getCode(), "");
            if (StringUtils.isNotBlank(fileTypesAllowed) && !Arrays.asList(StringUtils.split(fileTypesAllowed, ',')).contains(currentFile.getFileType().getCode().name())) {
                continue;
            }

            final FacesContext context = FacesContext.getCurrentInstance();

            String stringId = dataType.getId().toString();

            // Label for the component
            String label = dataType.getLabel().toLowerCase();
            commonObservationPrintable = dataType.getType().equals(DataTypeEnnumeration.INPUTTEXTAREA.getCode()) && (label.equalsIgnoreCase(COMMON_OBSERVATION_LABEL) || label.startsWith(COMMON_OBSERVATION_LABEL));
            if (!commonObservationPrintable) {
                final HtmlOutputLabel htmlOutputLabel = (HtmlOutputLabel) context.getApplication().createComponent(HtmlOutputLabel.COMPONENT_TYPE);
                htmlOutputLabel.setFor(ID_DECISION_LABEL + stringId);
                if (FacesContext.getCurrentInstance().getViewRoot().getLocale().equals(Locale.FRENCH)) {
                    htmlOutputLabel.setValue(label);
                } else {
                    htmlOutputLabel.setValue(dataType.getLabelEn());
                }
                decisionDiv.getChildren().add(htmlOutputLabel);
            }

            final HtmlPanelGroup htmlPanelGroup = (HtmlPanelGroup) context.getApplication().createComponent(HtmlPanelGroup.COMPONENT_TYPE);

            if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXT.getCode())) {
                final HtmlInputText inputText = (HtmlInputText) context.getApplication().createComponent(HtmlInputText.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    inputText.setRequired(true);
                    inputText.setRequiredMessage(dataType.getLabel()
                            + Constants.SPACE
                            + ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("RequiredMessage_standard"));
                }
                inputText.setId(ID_DECISION_LABEL + stringId);
                htmlPanelGroup.getChildren().add(inputText);
            } else if (dataType.getType().equals(DataTypeEnnumeration.CHEKBOX.getCode())) {
                final HtmlSelectBooleanCheckbox booleanCheckbox = (HtmlSelectBooleanCheckbox) context.getApplication().createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    booleanCheckbox.setRequired(true);
                    booleanCheckbox.setRequiredMessage(dataType.getLabel()
                            + Constants.SPACE
                            + ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("RequiredMessage_standard"));
                }
                booleanCheckbox.setId(ID_DECISION_LABEL + stringId);
                htmlPanelGroup.getChildren().add(booleanCheckbox);
            } else if (dataType.getType().equals(DataTypeEnnumeration.CALENDAR.getCode())) {
                final Calendar calendar = (Calendar) context.getApplication().createComponent(Calendar.COMPONENT_TYPE);
                calendar.setShowOn("button");
                if (dataType.getRequired()) {
                    calendar.setRequired(true);
                    calendar.setRequiredMessage(dataType.getLabel()
                            + Constants.SPACE
                            + ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("RequiredMessage_standard"));
                }
                calendar.setId(ID_DECISION_LABEL + stringId);
                String pattern = DateUtils.FRENCH_DATE;
                pattern = properties.getProperty(DataTypePropEnnum.PATTERN.getCode(), pattern);
                calendar.setPattern(pattern);
                calendar.setLocale(Locale.FRANCE);
                calendar.setNavigator(true);
                htmlPanelGroup.getChildren().add(calendar);

            } else if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXTAREA.getCode()) && !commonObservationPrintable) {
                final HtmlInputTextarea inputTextarea = (HtmlInputTextarea) context.getApplication().createComponent(HtmlInputTextarea.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    inputTextarea.setRequired(true);
                    inputTextarea.setRequiredMessage(label
                            + Constants.SPACE
                            + ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("RequiredMessage_standard"));
                }
                inputTextarea.setRows(10);
                inputTextarea.setId(ID_DECISION_LABEL + stringId);
                htmlPanelGroup.getChildren().add(inputTextarea);
            } else if (commonObservationPrintable) {
                commonObservationRequired = BooleanUtils.toBoolean(dataType.getRequired());
            }

            if (!commonObservationPrintable) {
                final Message message = (Message) context.getApplication().createComponent(Message.COMPONENT_TYPE);
                message.setFor(ID_DECISION_LABEL + stringId);
                htmlPanelGroup.getChildren().add(message);
            }

            decisionDiv.getChildren().add(htmlPanelGroup);
        }
    }

    /**
     * Payment amout value changed listener.
     */
    public void paymentAmoutValueChangedListener() {
        invoiceTotalAmount = 0L;
        invoiceTotalTtcAmount = 0L;
        Long totalTva = 0L;
        for (final PaymentItemFlow pi : paymentData.getPaymentItemFlowList()) {
            invoiceTotalAmount += pi.getMontantHt() != null ? pi.getMontantHt() : 0;
            invoiceTotalTtcAmount += invoiceTotalAmount;
            invoiceTotalTtcAmount += pi.getMontantTva() != null ? pi.getMontantTva() : 0;
            totalTva += pi.getMontantTva() != null ? pi.getMontantTva() : 0;
        }
        invoiceTotalTtcAmount += invoiceOtherAmount != null ? invoiceOtherAmount : 0;
        paymentData.setMontantHt(invoiceTotalAmount);
        paymentData.setMontantTva(totalTva);
        paymentData.setMontantEncaissement(Double.parseDouble(invoiceTotalTtcAmount.toString()));
        paymentData.setAutreMontant(invoiceOtherAmount);
    }

    /**
     * CCS Minsante products Type value changed listener.
     */
    public void ccsProductsTypeChangedListener() {
        ccsMinsanteFoodProducts = treatmentInfosCCSMinsante != null && (treatmentInfosCCSMinsante.isProductFoodIHC() || treatmentInfosCCSMinsante.isHygienSanitationProducts() || treatmentInfosCCSMinsante.isFleaMarket() || treatmentInfosCCSMinsante.isThriftShop() || treatmentInfosCCSMinsante.isVehicle());
        ccsMinsanteDrugProducts = treatmentInfosCCSMinsante != null && (treatmentInfosCCSMinsante.isDrugs() || treatmentInfosCCSMinsante.isMedicalDevices() || treatmentInfosCCSMinsante.isTropicalCorticosteroids() || treatmentInfosCCSMinsante.isLaboratoryProducts() || treatmentInfosCCSMinsante.isPackagingSfProducts());
    }

    public synchronized void addInvoiceLine() {
        InvoiceLine invoiceLine = new InvoiceLine();
        invoiceLine.setId(counter--);
        invoiceLine.setMontantTva(0L);
        paymentData.getInvoiceLines().add(invoiceLine);
    }

    public synchronized void removeInvoiceLine() {

        if (selectedInvoiceLine == null) {
            return;
        }

        paymentData.getInvoiceLines().remove(selectedInvoiceLine);
        paymentAmoutValueChangedListenerCte();
    }

    public void paymentAmoutValueChangedListenerCte() {
        invoiceTotalAmount = 0L;
        for (InvoiceLine invoiceLine : paymentData.getInvoiceLines()) {
            invoiceTotalAmount += invoiceLine.getMontantHt();
        }
        paymentData.setMontantHt(invoiceTotalAmount);
    }

    /**
     * Remplir la liste des valeurs des filed Values pour le un article.
     */
    private void loadAndGroupFileItemFieldValues() {
        fileItemFieldValues = selectedFileItemCheck.getFileItem().getFileItemFieldValueList();
        groupFileItemFieldValues();
        decisionHistories = decisionHistoryService.findByFile(currentFile);
    }

    /**
     * Find last decisions.
     */
    private void findLastDecisions() {

        specificDecisionsHistory = new CctSpecificDecisionHistory();

        lastDecisions = itemFlowService.findLastSentItemFlowByFileItem(selectedFileItemCheck.getFileItem());
        final FileTypeFlow fileTypeFlow = fileTypeFlowService.findByFlowAndFileType(selectedFileItemCheck.getFileItem().getFile().getFileType(), lastDecisions.getFlow());
        if (fileTypeFlow != null) {
            lastDecisions.getFlow().setRedefinedLabelEn(fileTypeFlow.getLabelEn());
            lastDecisions.getFlow().setRedefinedLabelFr(fileTypeFlow.getLabelFr());
        }
        if (CONSTAT_FLOW_LIST.contains(lastDecisions.getFlow().getCode())) {
            specificDecisionsHistory.setLastDecisionIR(inspectionReportService.findLastInspectionReportsByFileItem(lastDecisions.getFileItem()));
        } else if (APPOINTMENT_DECISIONS_LIST.contains(lastDecisions.getFlow().getCode())) {
            specificDecisionsHistory.setLastDecisionApp(appointmentService.findAppointmentsByItemFlow(lastDecisions));
        } else if (DCC_FLOW_CODES.contains(lastDecisions.getFlow().getCode())) {
            specificDecisionsHistory.setApprovedDecision(approvedDecisionService.findApprovedDecisionByItemFlow(lastDecisions));
        } else if (isPhytoReadyForSignatureEnd(lastDecisions.getFlow())) {
            specificDecisionsHistory.setcCTCPParamValue(cCTCPParamValueService.findCCTCPParamValueByItemFlow(lastDecisions));
        } else if (FlowCode.FL_CT_29.name().equals(lastDecisions.getFlow().getCode())) {
            specificDecisionsHistory.setLastAnalyseOrder(analyseOrderService.findByItemFlow(lastDecisions));
        } else if (isPhytoReadyForSignature(lastDecisions.getFlow())) {
            specificDecisionsHistory.setLastTreatmentInfos(treatmentInfosService.findTreatmentInfosByItemFlow(lastDecisions));
        } else if (isCCSMinsanteReadyForSignature(lastDecisions.getFlow())) {
            specificDecisionsHistory.setLastTreatmentInfosCCSMinsante(treatmentInfosCCSMinsanteService.findTreatmentInfosByItemFlow(lastDecisions));
        } else if (isPviReadyForSignature(lastDecisions.getFlow())) {
//            specificDecisionsHistory.setLastDecisionIR(inspectionReportService.findLastInspectionReportsByFileItem(lastDecisions
//                    .getFileItem()));
            specificDecisionsHistory.setLastDecisionIR(inspectionReportService.findByItemFlow(lastDecisions));
        } else if (isAtReadyForSignature(lastDecisions.getFlow())) {
            specificDecisionsHistory.setLastTreatmentResult(treatmentResultService.findTreatmentResultByItemFlow(lastDecisions));
//            final List<Long> servicesIds = serviceService.findServicesIdsByAdministration(loggedUser.getAdministration());
//            treatmentCompanys = treatmentCompanyService.findByAdministration(servicesIds);
        } else if (isFstpReadyForSignature(lastDecisions.getFlow())) {
            specificDecisionsHistory.setLastTreatmentResult(treatmentResultService.findTreatmentResultByItemFlow(lastDecisions));
        } else if (FlowCode.FL_CT_64.name().equals(lastDecisions.getFlow().getCode())) {
            specificDecisionsHistory.setLastTreatmentOrder(treatmentOrderService.findTreatmentOrderByItemFlow(lastDecisions));
        } else if (FlowCode.FL_CT_31.name().equals(lastDecisions.getFlow().getCode())) {
            specificDecisionsHistory.setLastAnalyseResult(analyseResultService.findAnalyseResultByItemFlow(lastDecisions));
            final List<AnalysePart> analyseParts = specificDecisionsHistory.getLastAnalyseResult().getAnalyseOrder().getAnalysePartsList();
            downloadAttachment(analyseParts);
        } else if (FlowCode.FL_CT_66.name().equals(lastDecisions.getFlow().getCode())) {
            specificDecisionsHistory.setLastTreatmentResult(treatmentResultService.findTreatmentResultByItemFlow(lastDecisions));
            final List<TreatmentPart> treatmentParts = specificDecisionsHistory.getLastTreatmentResult().getTreatmentOrder().getTreatmentPartsList();
            downloadAttachment(treatmentParts);
        } else if (FlowCode.FL_CT_93.name().equals(lastDecisions.getFlow().getCode()) || FlowCode.FL_CT_160.name().equals(lastDecisions.getFlow().getCode())) {
            specificDecisionsHistory.setLastPaymentData(paymentDataService.findPaymentDataByItemFlow(lastDecisions));
        } else if (isPhytoBilling(lastDecisions.getFlow())) {
            lastSpecificDecision = CctSpecificDecision.CCT_CT_E_BILL;
            specificDecisionsHistory.setLastPaymentData(paymentDataService.findPaymentDataByFileItem(lastDecisions.getFileItem()));
        } else if (isPveReadyForSignature(lastDecisions.getFlow()) || isAppointmentOkForPve(lastDecisions.getFlow())) {
            PottingReport pr = pottingReportService.findPottingReportByFile(currentFile, false);
            specificDecisionsHistory.setLastPottingReport(pr);
        } else if (isPhytoAppointment(lastDecisions.getFlow())) {
            lastSpecificDecision = CctSpecificDecision.CCT_CT_E_APP;
            specificDecisionsHistory.setLastDecisionApp(appointmentService.findAppointmentByFileItem(currentFileItem));
        }
    }

    /**
     * Decision details by item flow.
     *
     */
    public void decisionDetailsByItemFlow() {

        specificDecisionsHistory = new CctSpecificDecisionHistory();

        if (CONSTAT_FLOW_LIST.contains(selectedItemFlowDto.getItemFlow().getFlow().getCode())) {
            specificDecisionsHistory.setDecisionDetailsIR(inspectionReportService.findLastInspectionReportsByFileItem(selectedItemFlowDto.getItemFlow().getFileItem()));
        } else if (APPOINTMENT_DECISIONS_LIST.contains(selectedItemFlowDto.getItemFlow().getFlow().getCode())) {
            specificDecisionsHistory.setDecisionDetailsApp(appointmentService.findAppointmentsByItemFlow(selectedItemFlowDto.getItemFlow()));
        } else if (DCC_FLOW_CODES.contains(selectedItemFlowDto.getItemFlow().getFlow().getCode())) {
            specificDecisionsHistory.setApprovedDecision(approvedDecisionService.findApprovedDecisionByItemFlow(selectedItemFlowDto.getItemFlow()));
        } else if (isPhytoReadyForSignatureEnd(selectedItemFlowDto.getItemFlow().getFlow())) {
            specificDecisionsHistory.setcCTCPParamValue(cCTCPParamValueService.findCCTCPParamValueByItemFlow(selectedItemFlowDto.getItemFlow()));
        } else if (FlowCode.FL_CT_29.name().equals(selectedItemFlowDto.getItemFlow().getFlow().getCode())) {
            specificDecisionsHistory.setDecisionDetailsAO(analyseOrderService.findByItemFlow(selectedItemFlowDto.getItemFlow()));
        } else if (isPhytoReadyForSignature(selectedItemFlowDto.getItemFlow().getFlow())) {
            specificDecisionsHistory.setDecisionDetailsTI(treatmentInfosService.findTreatmentInfosByItemFlow(selectedItemFlowDto.getItemFlow()));
        } else if (isCCSMinsanteReadyForSignature(selectedItemFlowDto.getItemFlow().getFlow())) {
            specificDecisionsHistory.setDecisionDetailsTICCSMinsante(treatmentInfosCCSMinsanteService.findTreatmentInfosByItemFlow(selectedItemFlowDto.getItemFlow()));
        } else if (isPviReadyForSignature(selectedItemFlowDto.getItemFlow().getFlow())) {
            specificDecisionsHistory.setDecisionDetailsIR(inspectionReportService.findByItemFlow(selectedItemFlowDto.getItemFlow()));
        } else if (isAtReadyForSignature(selectedItemFlowDto.getItemFlow().getFlow())) {
            specificDecisionsHistory.setDecisionDetailsTR(treatmentResultService.findTreatmentResultByItemFlow(selectedItemFlowDto.getItemFlow()));
        } else if (isFstpReadyForSignature(selectedItemFlowDto.getItemFlow().getFlow())) {
            specificDecisionsHistory.setDecisionDetailsTR(treatmentResultService.findTreatmentResultByItemFlow(selectedItemFlowDto.getItemFlow()));
        } else if (FlowCode.FL_CT_64.name().equals(selectedItemFlowDto.getItemFlow().getFlow().getCode())) {
            specificDecisionsHistory.setDecisionDetailsTO(treatmentOrderService.findTreatmentOrderByItemFlow(selectedItemFlowDto.getItemFlow()));
        } else if (FlowCode.FL_CT_31.name().equals(selectedItemFlowDto.getItemFlow().getFlow().getCode())) {
            specificDecisionsHistory.setDecisionDetailsAR(analyseResultService.findAnalyseResultByItemFlow(selectedItemFlowDto.getItemFlow()));
            final List<AnalysePart> analyseParts = specificDecisionsHistory.getDecisionDetailsAR().getAnalyseOrder().getAnalysePartsList();
            downloadAttachment(analyseParts);
        } else if (FlowCode.FL_CT_66.name().equals(selectedItemFlowDto.getItemFlow().getFlow().getCode())) {
            specificDecisionsHistory.setDecisionDetailsTR(treatmentResultService.findTreatmentResultByItemFlow(selectedItemFlowDto.getItemFlow()));
            final List<TreatmentPart> treatmentParts = specificDecisionsHistory.getDecisionDetailsTR().getTreatmentOrder().getTreatmentPartsList();
            downloadAttachment(treatmentParts);
        } else if (FlowCode.FL_CT_93.name().equals(lastDecisions.getFlow().getCode())) {
            specificDecisionsHistory.setDecisionDetailsPayData(paymentDataService.findPaymentDataByItemFlow(lastDecisions));
        } else if (isPhytoBilling(selectedItemFlowDto.getItemFlow().getFlow())) {
            specificDecisionsHistory.setDecisionDetailsPayData(paymentDataService.findPaymentDataByFileItem(selectedItemFlowDto.getItemFlow().getFileItem()));
            lastSpecificDecision = CctSpecificDecision.CCT_CT_E_BILL;
        } else if (isPveReadyForSignature(selectedItemFlowDto.getItemFlow().getFlow()) || isAppointmentOkForPve(selectedItemFlowDto.getItemFlow().getFlow())) {
            PottingReport pr = pottingReportService.findPottingReportByFile(currentFile);
            if (pr == null) {
                pr = pottingReportService.findPottingReportByFile(currentFile, false);
            }
            specificDecisionsHistory.setDecisionDetailsPR(pr);
        } else if (isPhytoAppointment(selectedItemFlowDto.getItemFlow().getFlow())) {
            specificDecisionsHistory.setDecisionDetailsApp(appointmentService.findAppointmentByFileItem(currentFileItem));
        }
    }

    /**
     * Download attachment.
     *
     * @param partsList the parts list
     */
    private void downloadAttachment(final List<? extends Object> partsList) {
        reportMap = new HashMap<>();
        Session sessionCmisClient;
        try {
            sessionCmisClient = CmisSession.getInstance();
        } catch (CmisConnectionException ce) {
            logger.error(Objects.toString(ce), ce);
            try {
                String senderMail = mailService.getReplyToValue();
                String templateFileName = CMIS_CONNEXION_ERROR;
                Map<String, String> map = new HashMap<>();
                map.put(MailConstants.SUBJECT, "[SIAT-CT] : Connexion Cmis échoué");
                map.put(MailConstants.FROM, mailService.getFromValue());
                map.put(MailConstants.EMAIL, senderMail);
                map.put("gedUrl", alfrescoPropretiesService.getIpRepoValue());
                map.put(MailConstants.VMF, templateFileName);
                mailService.sendMail(map);
            } catch (Exception ex) {
                logger.error(Objects.toString(ex), ex);
            }
            return;
        }

        for (Object part : partsList) {
            String fileName = null;
            Long partId = null;
            if (part instanceof AnalysePart) {
                fileName = ((AnalysePart) part).getAttachment();
                partId = ((AnalysePart) part).getId();
            } else if (part instanceof TreatmentPart) {
                fileName = ((TreatmentPart) part).getAttachment();
                partId = ((TreatmentPart) part).getId();
            }

            if (!Objects.equals(fileName, null)) {
                ContentStream contentStream = CmisClient.getDocumentByName(sessionCmisClient, fileName).getContentStream();
                try (InputStream inputStream = contentStream.getStream();) {
                    byte[] content = IOUtils.toByteArray(inputStream);
                    reportMap.put(partId, new DefaultStreamedContent(new ByteArrayInputStream(content),
                            contentStream.getMimeType(), fileName));
                } catch (IOException e) {
                    logger.error("Cannot download {} from file {}", fileName, currentFile);
                    logger.error(fileName, e);
                }
            }
        }
    }

    /**
     * Load product history list.
     */
    private void loadProductHistoryList() {
        itemFlowHistoryDtoList = new ArrayList<>();
        List<ItemFlow> itemFlowHistoryList = itemFlowService.findItemFlowByFileItem(selectedFileItemCheck.getFileItem());
        if (CollectionUtils.isNotEmpty(itemFlowHistoryList)) {
            for (int i = 0; i < itemFlowHistoryList.size(); i++) {
                final FileTypeFlow fileTypeFlow = fileTypeFlowService.findByFlowAndFileType(selectedFileItemCheck.getFileItem()
                        .getFile().getFileType(), itemFlowHistoryList.get(i).getFlow());
                if (fileTypeFlow != null) {
                    itemFlowHistoryList.get(i).getFlow().setRedefinedLabelEn(fileTypeFlow.getLabelEn());
                    itemFlowHistoryList.get(i).getFlow().setRedefinedLabelFr(fileTypeFlow.getLabelFr());
                }
                if (Constants.ONE <= itemFlowHistoryList.size()) {
                    final ItemFlowDto itemFlowDto = new ItemFlowDto();
                    if (i + Constants.ONE == itemFlowHistoryList.size()) {
                        itemFlowDto.setDuration("-");
                        itemFlowDto.setItemFlow(itemFlowHistoryList.get(i));
                    } else {
                        final String duration = calculatDuration(itemFlowHistoryList.get(i).getCreated().getTime(), itemFlowHistoryList
                                .get(i + Constants.ONE).getCreated().getTime());
                        itemFlowDto.setDuration(duration);
                        itemFlowDto.setItemFlow(itemFlowHistoryList.get(i));
                    }
                    itemFlowHistoryDtoList.add(itemFlowDto);
                }
            }

            for (final ItemFlowDto itemFlowDto : itemFlowHistoryDtoList) {

                final FileTypeStep fileTypeStep = fileTypeStepService.findFileTypeStepByFileTypeAndStep(selectedFileItemCheck
                        .getFileItem().getFile().getFileType(), itemFlowDto.getItemFlow().getFlow().getToStep());

                if (fileTypeStep != null) {
                    itemFlowDto.getItemFlow().getFileItem().setRedefinedLabelEn(fileTypeStep.getLabelEn());
                    itemFlowDto.getItemFlow().getFileItem().setRedefinedLabelFr(fileTypeStep.getLabelFr());
                }

            }

            List<Transfer> transfers = transferService.findByFile(currentFile);
            for (Transfer transfer : transfers) {
                ItemFlowDto itemFlowDto = helper.transform(transfer);
                if (itemFlowDto != null) {
                    itemFlowHistoryDtoList.add(itemFlowDto);
                }
            }
            Collections.sort(itemFlowHistoryDtoList, new Comparator<ItemFlowDto>() {
                @Override
                public int compare(ItemFlowDto o1, ItemFlowDto o2) {
                    return o2.getItemFlow().getCreated().compareTo(o1.getItemFlow().getCreated());
                }
            });
        }
    }

    /**
     * Calculat duration.
     *
     * @param beginDate the begin date
     * @param endDate the end date
     * @return the string
     */
    public String calculatDuration(final long beginDate, final long endDate) {
        long secs = (endDate - beginDate) / 1000;
        final int days = (int) (secs / 86400);
        secs = secs % 86400;
        final int hours = (int) (secs / 3600);
        secs = secs % 3600;
        final StringBuilder duration = new StringBuilder();
        if (0 == days && 0 == hours && 0 < secs) {
            duration.append(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    ControllerConstants.Bundle.Messages.HISTORY_DURATION_LESS_THAN_HOUR));
        } else {
            if (days > 0) {
                duration.append(days).append(" ").append(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        ControllerConstants.Bundle.Messages.HISTORY_DURATION_DAYS)).append(", ");
            }

            if (hours > 0) {
                duration.append(hours).append(" ").append(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        ControllerConstants.Bundle.Messages.HISTORY_DURATION_HOURS));
            }
        }
        return duration.toString();
    }

    /**
     * Find last decisions and load product information.
     */
    public void changeProductSelection() {
        if (selectedFileItemCheck != null) {
            findLastDecisions();
            loadProductHistoryList();
            refreshRecommandationArticleList();
        }
        // Remplir la liste des valeurs des filed Values pour le premier article
        loadAndGroupFileItemFieldValues();
        selectedAttachment = null;
        setShowRemindDecisionForm(true);
        setShowListRecommandationArticleForm(true);
        setShowProductDetailsForm(true);
        // enable decision
        setShowDecisionButton(true);
        setShowShowAttachmentForm(false);
    }

    /**
     * Save decision.
     *
     * @throws ParseException the parse exception
     */
    public synchronized void saveDecision() throws ParseException {

        if (!userDecisionAllowed) {
            return;
        }

        ItemFlow ifl = itemFlowService.findLastItemFlowByFileItem(currentFileItem);
        if (selectedFlow.getCode().equals(ifl.getFlow().getCode())) {
            return;
        }

        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(GLOBAL_PROPAGATION_TRANSACTION_BEHAVIOUR);
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        displayGenerateDraft = true;
        try {
            if (FlowCode.FL_CT_29.name().equals(selectedFlow.getCode()) && chckedListSize != Constants.ONE) {
                showErrorFacesMessage(ControllerConstants.Bundle.Messages.CHECK_ANALYSE_DECISION_ERROR, ControllerConstants.Bundle.Messages.CHECK_PRODUCTS_DECISION_MSG);
                return;
            } // Demande de d'analyse
            else if (FlowCode.FL_CT_29.name().equals(selectedFlow.getCode()) && chckedListSize == Constants.ONE) {
                analysePartsList = new ArrayList<>();
                lastDecisions = itemFlowService.findLastSentItemFlowByFileItem(selectCheckedFileItemCheck().get(0).getFileItem());
                final InspectionReport lastDecisionIR = inspectionReportService.findLastInspectionReportsByFileItem(lastDecisions.getFileItem());

                analyseOrder.setSample(lastDecisionIR.getSample());
                analyseOrder.setLaboratory(selectedLaboratory);
                analyseOrder.setDate(java.util.Calendar.getInstance().getTime());

                for (final AnalyseTypeDto analyseTypeDto : analyseTypeDtosList) {
                    if (analyseTypeDto.getChecked()) {
                        final AnalysePart analysePart = new AnalysePart();
                        analysePart.setAnalyseType(analyseTypeDto.getAnalyseType());
                        analysePart.setAnalyseOrder(analyseOrder);
                        analysePartsList.add(analysePart);
                    }
                }
                if (CollectionUtils.isEmpty(analysePartsList)) {
                    showErrorFacesMessage(ControllerConstants.Bundle.Messages.CHECK_ANALYSE_TYPE_CHOOSEN_MSG, ControllerConstants.Bundle.Messages.CHECK_ANALYSE_TYPE_ERROR);
                    return;
                }
            } else if ((FlowCode.FL_CT_64.name().equals(selectedFlow.getCode()) && chckedListSize != Constants.ONE)) {
                showErrorFacesMessage(ControllerConstants.Bundle.Messages.CHECK_TREATMENT_DECISION_ERROR, ControllerConstants.Bundle.Messages.CHECK_PRODUCTS_DECISION_MSG);
            } // Demande de Traitement
            else if ((FlowCode.FL_CT_64.name().equals(selectedFlow.getCode()) && chckedListSize == Constants.ONE)) {
                treatmentPartsList = new ArrayList<>();
                treatmentOrder.setTreatmentCompany(selectedTreatmentCompany);
                treatmentOrder.setDate(java.util.Calendar.getInstance().getTime());

                for (final TreatmentTypeDto treatmentTypeDto : treatmentTypeDtosList) {
                    if (treatmentTypeDto.getChecked()) {
                        final TreatmentPart treatmentPart = new TreatmentPart();
                        treatmentPart.setTreatmentType(treatmentTypeDto.getTreatmentType());
                        treatmentPart.setTreatmentOrder(treatmentOrder);
                        treatmentPartsList.add(treatmentPart);
                    }
                }
                if (CollectionUtils.isEmpty(treatmentPartsList)) {
                    showErrorFacesMessage(ControllerConstants.Bundle.Messages.CHECK_TREATMENT_TYPE_CHOOSEN_MSG, ControllerConstants.Bundle.Messages.CHECK_TREATMENT_TYPE_ERROR);
                    return;
                }
            } else if (isCsvBeforeTreatment(selectedFlow) && currentFile.getAssignedUser() == null) {
                currentFile.setAssignedUser(assignedUserForCotation);
            }
            // Check if the step of fileItems was changed by another user when
            // the decision popup is open
            if (decisionByFileAllowed && fileItemService.verifyStepChangedWhileDecisionInProgress(productInfoItems)) {
                final String summary = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.ERROR_DIALOG_TITLE);
                final String detail1 = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.CANCEL_REQUEST_IN_PROGRESS);
                logger.error(detail1);
                // the user must refresh the details page (return to the
                // dashboard then open the file details page) to get the correct
                // decision list in the decision popup
                JsfUtil.showMessageInDialog(FacesMessage.SEVERITY_ERROR, summary, detail1);
                return;

            } else if (!decisionByFileAllowed) {
                final List<FileItemCheck> chckedProductInfoChecksList = selectCheckedFileItemCheck();

                if (fileItemService.verifyStepChangedWhileDecisionInProgress(getFileItemListFromFileItemChekList(chckedProductInfoChecksList))) {
                    final String summary = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.ERROR_DIALOG_TITLE);

                    final String detail1 = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.CANCEL_REQUEST_IN_PROGRESS);
                    logger.error(detail1);
                    // the user must refresh the details page (return to the
                    // dashboard then open the file details page) to get the
                    // correct decision list in the decision popup
                    JsfUtil.showMessageInDialog(FacesMessage.SEVERITY_ERROR, summary, detail1);
                    return;
                }
            }

            final List<ItemFlow> itemFlowsToAdd = new ArrayList<>();

            // IF Decision enable by FILE, ALL fileItem must has the same
            // Decision (ItemFlow)
            if (decisionByFileAllowed) {
                for (final FileItem fileItem : productInfoItemsEnabled) {
                    final ItemFlow itemFlow = new ItemFlow();

                    itemFlow.setFileItem(fileItem);
                    itemFlow.setFlow(selectedFlow);
                    itemFlow.setSender(loggedUser);
                    itemFlow.setSent(Boolean.FALSE);
                    itemFlow.setUnread(Boolean.TRUE);
                    itemFlow.setReceived(AperakType.APERAK_D.getCharCode());
                    itemFlowsToAdd.add(itemFlow);
                }
            } // ELSE : SET CHEEKED FILE
            else {
                final List<FileItemCheck> chckedProductInfoChecksList = selectCheckedFileItemCheck();

                for (final FileItemCheck fileItemCheck : chckedProductInfoChecksList) {
                    final ItemFlow itemFlow = new ItemFlow();

                    itemFlow.setFileItem(fileItemCheck.getFileItem());
                    itemFlow.setFlow(selectedFlow);
                    itemFlow.setSender(loggedUser);
                    itemFlow.setSent(Boolean.FALSE);
                    itemFlow.setUnread(Boolean.TRUE);
                    itemFlow.setReceived(AperakType.APERAK_D.getCharCode());

                    itemFlowsToAdd.add(itemFlow);
                }
            }
            // Demande de d'analyse
            if (FlowCode.FL_CT_29.name().equals(selectedFlow.getCode()) && chckedListSize == Constants.ONE) {
                if (CollectionUtils.isNotEmpty(analysePartsList)) {
                    commonService.takeDecisionAndSaveAnalyseRequest(analysePartsList, analyseOrder, itemFlowsToAdd);
                }
            } else if (isCCSMinsanteReadyForSignature(selectedFlow)) {
                commonService.takeDecisionAndSaveTreatmentInfosCCSMinsante(treatmentInfosCCSMinsante, itemFlowsToAdd);
            } else if (isPhytoReadyForSignature(selectedFlow)) {
                commonService.takeDecisionAndSaveTreatmentInfos(treatmentInfos, itemFlowsToAdd);
                saveModifiedGoods();
            } // Demande de Traitement
            else if ((FlowCode.FL_CT_64.name().equals(selectedFlow.getCode()) && chckedListSize == Constants.ONE)) {
                if (CollectionUtils.isNotEmpty(treatmentPartsList)) {
                    commonService.takeDecisionAndSaveTreatmentRequest(treatmentPartsList, treatmentOrder, itemFlowsToAdd);
                }
            } // Rendez-vous
            else if (APPOINTMENT_DECISIONS_LIST.contains(selectedFlow.getCode())) {
                if (getInstanceOfPageScheduleController() != null && getInstanceOfPageScheduleController().getAddedEvent() != null) {
                    final Appointment appointment1 = getInstanceOfPageScheduleController().getAddedEvent().getAppointment();
                    if (FlowCode.FL_CT_26.name().equals(selectedFlow.getCode())) {
                        appointment1.setInspectionOnDock(Boolean.FALSE);
                    } else {
                        appointment1.setInspectionOnDock(Boolean.TRUE);
                    }
                    commonService.takeDecisionAndAppointment(itemFlowsToAdd, appointment1);
                } else {
                    showErrorFacesMessage(ControllerConstants.Bundle.Messages.CALENDAR_ERROR_CHOOSE_APPOINTMENT, null);
                    return;
                }
            } else if (isPviReadyForSignature(selectedFlow)) {
                List<InspectionReport> inspectionReports = inspectionReportData.transformToReportList(null);
                commonService.takeDecisionAndSaveInspectionReport(inspectionReports.get(0), itemFlowsToAdd);
                saveModifiedGoods();
            } // Saisie Constat
            else if (CONSTAT_FLOW_LIST.contains(selectedFlow.getCode())) {
                Appointment appointment1 = appointmentService.findAppoitmentByFileItemAndController(productInfoItemsEnabled, loggedUser);
                inspectionReportData.setControllers(inspectionControllers);
                if (infraction != null && StringUtils.isNotBlank(infraction.getinfractionCode())) {
                    inspectionReportData.setInfraction(infraction.getinfractionCode());
                }
                List<InspectionReport> inspectionReports = inspectionReportData.transformToReportList(appointment1);
                commonService.takeDecisionAndSaveInspectionReports(inspectionReports, itemFlowsToAdd);
            } // Envoie Résultat d Analyse
            else if (FlowCode.FL_CT_31.name().equals(selectedFlow.getCode())) {
                analyseResult.getAnalyseOrder().setAnalysePartsList(new ArrayList<AnalysePart>());
                for (UploadFileManager<AnalysePart> entry : analysesFileManagers) {
                    analyseResult.getAnalyseOrder().getAnalysePartsList().add(entry.getPart());
                    if (!Objects.equals(entry.getFile(), null)) {
                        AnalysePart analysePart = entry.getPart();
                        analysePart.setAttachment(entry.getFile().getFileName());
                        java.io.File targetAttachment = new java.io.File(String.format(applicationPropretiesService.getAttachementFolder() + "%s%s", java.io.File.separator, entry.getFileName()));
                        try (InputStream initialStream = new ByteArrayInputStream(entry.getFile().getContents()); OutputStream outStream = new FileOutputStream(targetAttachment);) {
                            byte[] buffer = new byte[initialStream.available()];
                            initialStream.read(buffer);
                            outStream.write(buffer);
                            commonService.sendAttachedReports(Collections.singletonList(targetAttachment));
                            if (logger.isDebugEnabled()) {
                                logger.debug("//Attachment --> Alfresco {}", analysePart.getId());
                            }
                        }
                    }
                }
                commonService.takeDecisionAndSaveAnalyzeResult(analyseResult, itemFlowsToAdd);
                // Attachment --> Alfresco
            } else if (isFstpReadyForSignature(selectedFlow) || isAtReadyForSignature(selectedFlow)) {
                commonService.takeDecisionAndSaveTreatmentResult2(treatmentResult, itemFlowsToAdd);
            } // Envoie Résultat de Traitement
            else if (FlowCode.FL_CT_66.name().equals(selectedFlow.getCode())) {
                if (treatmentResult.getTreatmentOrder() != null) {
                    treatmentResult.getTreatmentOrder().setTreatmentPartsList(new ArrayList<TreatmentPart>());
                    for (final UploadFileManager<TreatmentPart> entry : treatmentFileManagers) {
                        treatmentResult.getTreatmentOrder().getTreatmentPartsList().add(entry.getPart());
                        if (!Objects.equals(entry.getFile(), null)) {
                            final TreatmentPart treatmentPart = entry.getPart();
                            treatmentPart.setAttachment(entry.getFile().getFileName());
                            final java.io.File targetAttachment = new java.io.File(String.format(applicationPropretiesService.getAttachementFolder() + "%s%s", java.io.File.separator, entry.getFileName()));
                            try (InputStream initialStream = new ByteArrayInputStream(entry.getFile().getContents()); OutputStream outStream = new FileOutputStream(targetAttachment);) {
                                final byte[] buffer = new byte[initialStream.available()];
                                initialStream.read(buffer);
                                outStream.write(buffer);
                                commonService.sendAttachedReports(Collections.singletonList(targetAttachment));
                                if (logger.isDebugEnabled()) {
                                    logger.debug("//Attachment --> Alfresco treatmentPart :{} ", treatmentPart.getId());
                                }
                            }
                        }
                    }
                }
                commonService.takeDecisionAndSaveTreatmentResult(treatmentResult, itemFlowsToAdd);
                // Attachment --> Alfresco
            } // DCC (Signature Certificat Controle Documentaire)
            else if (DCC_FLOW_CODES.contains(selectedFlow.getCode())) {
                commonService.takeDecisionAndSaveApprovedDecision(approvedDecision, itemFlowsToAdd);
            } // CCT_CP (Signature du phytosanitaire)
            else if (isPhytoReadyForSignatureEnd(selectedFlow)) {

                List<ItemFlowData> flowDatas = getValuesOfDataTypeForDecision(itemFlowsToAdd, selectedFlow.getDataTypeList());

                commonService.takeDecisionAndSaveCCTCPParamValueAndDataType(cCTCPParamValue, flowDatas, itemFlowsToAdd);

            } // Geniric (affichage des itemFlowData)
            else {

                // Recuperate the values of DataType (Observation text area ...)
                List<ItemFlowData> flowDatas = getValuesOfDataTypeForDecision(itemFlowsToAdd, selectedFlow.getDataTypeList());

                if (Arrays.asList(FlowCode.FL_CT_92.name(), FlowCode.FL_CT_158.name(), FlowCode.FL_CT_159.name(), FlowCode.FL_CT_174.name()).contains(selectedFlow.getCode()) || isPhytoBilling(selectedFlow)) {

                    if (Arrays.asList(FlowCode.FL_CT_159.name(), FlowCode.FL_CT_174.name()).contains(selectedFlow.getCode())) {
                        try {
                            paymentData.setMontantHt((long) Integer.parseInt(flowDatas.get(0).getValue()));
                        } catch (NumberFormatException nfe) {
                            showErrorFacesMessage(ControllerConstants.Bundle.Messages.CANNOT_PARSE_AMOUNT_ERROR, null);
                            return;
                        }
                    }
                    if (isPhytoBilling(selectedFlow)) {
                        if (CollectionUtils.isEmpty(paymentData.getInvoiceLines())) {
                            showErrorFacesMessage(ControllerConstants.Bundle.Messages.BILLING_WITHOUT_INVOICE_LINES_ERROR, null);
                            return;
                        }

                        if (CollectionUtils.isNotEmpty(paymentData.getPaymentItemFlowList())) {
                            coreService.delete(paymentData.getPaymentItemFlowList());
                        }
                        paymentAmoutValueChangedListenerCte();
                    }

                    commonService.takeDacisionAndSavePayment(itemFlowsToAdd, paymentData);
                    itemFlowService.takeDecision(itemFlowsToAdd, flowDatas);
                } else {

                    List<ItemFlow> addedItemFlows = itemFlowService.takeDecision(itemFlowsToAdd, flowDatas);

                    if (isAppointmentOkForPve(selectedFlow)) {
                        appointment.setBeginTime(pottingReport.getAppointmentDate());
                        appointment.setController(getLoggedUser());
                        appointment.setObservations(pottingReport.getAppointmentDetails());
                        pottingReport.setAppointmentItemFlow(addedItemFlows.get(0));
                    }

                    if (isPveReadyForSignature(selectedFlow)) {
                        pottingReport.setValidationItemFlow(addedItemFlows.get(0));
                    }

                    if (isPveReadyForSignature(selectedFlow) || isAppointmentOkForPve(selectedFlow)) {
                        commonService.takeDecisionAndSavePottingReport(pottingReport);
                    }

                    if (CctSpecificDecision.CCT_CT_E_APP.equals(specificDecision)) {
                        appointmentService.saveAppointment(appointment, addedItemFlows);
                    }
                }
                decisionDiv.getChildren().clear();
            }

            // update the last decision date on file
            currentFile.setLastDecisionDate(java.util.Calendar.getInstance().getTime());
            fileService.update(currentFile);

            TransactionStatus tsCommit = transactionStatus;
            transactionStatus = null;
            transactionManager.commit(tsCommit);
            if (logger.isDebugEnabled()) {
                logger.debug("####SAVE DECISION Transaction commited####");
            }
        } catch (final Exception ex) {
            logger.error(currentFile.toString(), ex);
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.SAVE_DECISION_FAILED, null);
        } finally {
            if (transactionStatus != null) {
                transactionManager.rollback(transactionStatus);
                if (logger.isDebugEnabled()) {
                    logger.debug("####SAVE DECISION Transaction rollbacked####");
                }
            }
        }

        resetDataGridInofrmationProducts();
        reloadPage();
    }

    private List<ItemFlowData> getValuesOfDataTypeForDecision(List<ItemFlow> itemsFlows, List<DataType> dataTypes) {
        List<ItemFlowData> flowDatas = new ArrayList<>();
        List<FileFieldValue> fileFieldValues1 = new ArrayList<>();
        for (final DataType dataType : dataTypes) {

            if (BooleanUtils.toBoolean(dataType.getDisabled())) {
                continue;
            }

            final String dataTypeProps = dataType.getProps();
            Properties properties = new Properties();
            if (dataTypeProps != null) {
                try {
                    properties.load(new StringReader(dataTypeProps));
                } catch (IOException ex) {
                    logger.debug("Problem occured when trying to load properties of data type : " + dataType, ex);
                }
            }

            String fileTypesAllowed = properties.getProperty(DataTypePropEnnum.FILE_TYPES.getCode(), "");
            if (StringUtils.isNotBlank(fileTypesAllowed) && !Arrays.asList(StringUtils.split(fileTypesAllowed, ',')).contains(currentFile.getFileType().getCode().name())) {
                continue;
            }

            final ItemFlowData itemFlowData = new ItemFlowData();
            itemFlowData.setDataType(dataType);

            if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXT.getCode())) {
                final HtmlInputText valueDataType = (HtmlInputText) decisionDiv.findComponent(ID_DECISION_LABEL + dataType.getId());
                itemFlowData.setValue(valueDataType.getValue().toString());
            } else if (dataType.getType().equals(DataTypeEnnumeration.CHEKBOX.getCode())) {
                final HtmlSelectBooleanCheckbox valueDataType = (HtmlSelectBooleanCheckbox) decisionDiv.findComponent(ID_DECISION_LABEL + dataType.getId());
                itemFlowData.setValue(valueDataType.getValue().toString());

            } else if (dataType.getType().equals(DataTypeEnnumeration.CALENDAR.getCode())) {
                final Calendar valueDataType = (Calendar) decisionDiv.findComponent(ID_DECISION_LABEL + dataType.getId());
                String pattern = DateUtils.FRENCH_DATE;
                pattern = properties.getProperty(DataTypePropEnnum.PATTERN.getCode(), pattern);
                final String itemFlowDataTypeValue = DateUtils.formatSimpleDateFromObject(pattern, valueDataType.getValue());
                itemFlowData.setValue(itemFlowDataTypeValue);

            } else if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXTAREA.getCode())) {
                if (!commonObservationPrintable) {
                    final HtmlInputTextarea valueDataType = (HtmlInputTextarea) decisionDiv.findComponent(ID_DECISION_LABEL + dataType.getId());
                    itemFlowData.setValue(valueDataType.getValue().toString());
                } else {
                    itemFlowData.setValue(commonObservation);
                }
            }

            flowDatas.add(itemFlowData);

            addFileFieldValue(fileFieldValues1, itemFlowData.getDataType().getCode(), itemFlowData.getValue());
        }

        if (CollectionUtils.isNotEmpty(fileFieldValues1)) {
            for (FileFieldValue ffv : fileFieldValues1) {
                FileFieldValue ffv1 = fileFieldValueService.findValueByFileFieldAndFile(ffv.getFileField().getCode(), currentFile);
                if (ffv1 != null) {
                    fileFieldValueService.delete(ffv1);
                }
                fileFieldValueService.save(ffv);
            }
        }

        return flowDatas;

    }

    /**
     * Annuler decisions.
     */
    public synchronized void annulerDecisions() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(GLOBAL_PROPAGATION_TRANSACTION_BEHAVIOUR);
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            //
            final List<Long> chckedProductInfoChecksList = getCheckedRollBacksFileItemCheckList();
            if (!BooleanUtils.toBoolean(cotationAllowed)) {
                commonService.rollbackDecision(chckedProductInfoChecksList);

                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.ROLL_BACK_SUCCESS));

                decisionDiv.getChildren().clear();
            } else {
                commonService.rollbackDecision(chckedProductInfoChecksList);
                // If the las decision is a cotation flow then rollback the
                // assigned user to the current user
                if (lastDecisions.getFlow().getIsCota()) {
                    currentFile.setAssignedUser(loggedUser);
                } else {
                    currentFile.setAssignedUser(null);
                }

                assignedUserForCotation = null;
                RequestContext.getCurrentInstance().update(":dispatchForm:dispatch_inspector");

                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.ROLL_BACK_SUCCESS));
            }

            // update the last decision date on file
            currentFile.setLastDecisionDate(currentFileItem.getItemFlowsList().get(0).getCreated());
            fileService.update(currentFile);

            TransactionStatus tsCommit = transactionStatus;
            transactionStatus = null;
            transactionManager.commit(tsCommit);
            if (logger.isDebugEnabled()) {
                logger.debug("####ROLLBACK DECISION Transaction commited####");
            }
        } catch (final Exception ex) {
            logger.error(currentFile.toString(), ex);
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.ROLL_BACK_FAIL, null);
        } finally {
            if (transactionStatus != null) {
                transactionManager.rollback(transactionStatus);
                if (logger.isDebugEnabled()) {
                    logger.debug("####ROLLBACK DECISION Transaction rollbacked####");
                }
            }
        }

        productInfoItems = disableDraftFromProductInfoItem(productInfoItems);

        resetDataGridInofrmationProducts();
        reloadPage();
    }

    /**
     * Gets the file item list from file item chek list.
     *
     * @param fileItemCheckList the file item check list
     * @return the file item list from file item chek list
     */
    private List<FileItem> getFileItemListFromFileItemChekList(final List<FileItemCheck> fileItemCheckList) {
        return (List<FileItem>) CollectionUtils.collect(fileItemCheckList, new Transformer() {
            @Override
            public Object transform(final Object fileItemCheck) {
                return ((FileItemCheck) fileItemCheck).getFileItem();
            }
        });
    }

    /**
     * Mark file.
     */
    public synchronized void dispatchFile() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(GLOBAL_PROPAGATION_TRANSACTION_BEHAVIOUR);
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        displayGenerateDraft = false;
        try {
            final List<ItemFlowData> flowDatas = new ArrayList<>();
            for (final DataType dataType : selectedFlow.getDataTypeList()) {
                final ItemFlowData itemFlowData = new ItemFlowData();
                itemFlowData.setDataType(dataType);

                if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXT.getCode())) {
                    final HtmlInputText valueDataType = (HtmlInputText) dipatchDiv.findComponent(ID_DISPATCH_LABEL + dataType.getId());
                    itemFlowData.setValue(valueDataType.getValue().toString());
                } else if (dataType.getType().equals(DataTypeEnnumeration.CHEKBOX.getCode())) {
                    final HtmlSelectBooleanCheckbox valueDataType = (HtmlSelectBooleanCheckbox) dipatchDiv.findComponent(ID_DISPATCH_LABEL + dataType.getId());
                    itemFlowData.setValue(valueDataType.getValue().toString());

                } else if (dataType.getType().equals(DataTypeEnnumeration.CALENDAR.getCode())) {
                    final Calendar valueDataType = (Calendar) dipatchDiv.findComponent(ID_DISPATCH_LABEL + dataType.getId());
                    itemFlowData.setValue(DateUtils.formatSimpleDateFromObject(DateUtils.FRENCH_DATE, valueDataType.getValue()));

                } else if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXTAREA.getCode())) {
                    if (!commonObservationPrintable) {
                        final HtmlInputTextarea valueDataType = (HtmlInputTextarea) dipatchDiv.findComponent(ID_DISPATCH_LABEL + dataType.getId());
                        itemFlowData.setValue(valueDataType.getValue().toString());
                    } else {
                        itemFlowData.setValue(commonObservation);
                    }
                }
                flowDatas.add(itemFlowData);
            }

            final List<ItemFlow> itemFlowsToAdd = new ArrayList<>();
            if (getFileItemCheckListForDecisionByFileAllowed() != null) {
                for (final FileItemCheck fileItemCheck : getFileItemCheckListForDecisionByFileAllowed()) {
                    final ItemFlow itemFlow = new ItemFlow();

                    itemFlow.setCreated(null);
                    itemFlow.setFileItem(fileItemCheck.getFileItem());
                    itemFlow.setFlow(selectedFlow);
                    itemFlow.setSender(loggedUser);
                    itemFlow.setSent(Boolean.FALSE);
                    itemFlow.setUnread(Boolean.TRUE);
                    itemFlow.setReceived(AperakType.APERAK_D.getCharCode());
                    if (assignedUserForCotation != null) {
                        itemFlow.setAssignedUser(assignedUserForCotation);
                    }
                    itemFlowsToAdd.add(itemFlow);
                }
                itemFlowService.takeDecision(itemFlowsToAdd, flowDatas);
            }
            if (assignedUserForCotation != null) {
                currentFile.setAssignedUser(assignedUserForCotation);
                fileService.update(currentFile);
            }

            TransactionStatus tsCommit = transactionStatus;
            transactionStatus = null;
            transactionManager.commit(tsCommit);
            if (logger.isDebugEnabled()) {
                logger.info("####DISPATCH DECISION Transaction commited####");
            }
        } catch (Exception ex) {
            logger.error(currentFile.toString(), ex);
        } finally {
            if (transactionStatus != null) {
                transactionManager.rollback(transactionStatus);
                if (logger.isDebugEnabled()) {
                    logger.info("####DISPATCH DECISION Transaction rollbacked####");
                }
            }
        }
        resetDataGridInofrmationProducts();
    }

    /**
     * Prepare dispatch file.
     */
    public synchronized void prepareDispatchFile() {
        String stringId = null;
        assignedUserForCotation = null;
        dipatchDiv.getChildren().clear();
        // let find the cotation flow
        final FlowCode flowCode;
        final FileTypeCode currentFileTypeCode = currentFile.getFileType().getCode();
        switch (currentFileTypeCode) {
            case CCT_CT_E_FSTP:
            case CCT_CT_E_PVI:
            case CCT_CT_E_PVE:
                flowCode = FlowCode.FL_CT_103;
                break;
            default:
                flowCode = FlowCode.FL_CT_06;
                break;
        }
        FileItem fi = currentFile.getFileItemsList().get(0);
        Flow cotationFlow = null;
        if (isPhyto() || FileTypeCode.CCT_CSV.equals(currentFileTypeCode)) {
            final List<Flow> flowList = flowService.findFlowsByFromStepAndFileType2(fi.getStep(), currentFile.getFileType());
            if (CollectionUtils.isNotEmpty(flowList)) {
                for (Flow flowItem : flowList) {
                    if (BooleanUtils.toBoolean(flowItem.getIsCota())) {
                        cotationFlow = flowItem;
                    }
                }
            }
        }
        if (cotationFlow != null) {
            selectedFlow = cotationFlow;
        } else {
            selectedFlow = flowService.findFlowByCode(flowCode.name());
        }
        List<User> cotationActors;
        if (checkMinepiaMinistry) {
            cotationActors = userService.findCotationsAgentByBureauAndRole(currentFile.getBureau(), AuthorityConstants.SOCIETE_TRAITEMENT.getCode());
        } else if (isPhyto()) {
            cotationActors = cotationService.findCotationAgentsByBureauAndRoleAndProductType(currentFile);
        } else {
            cotationActors = userService.findInspectorsByBureau(currentFile.getBureau());
        }
        setInspectorList(cotationActors);

        for (final DataType dataType : selectedFlow.getDataTypeList()) {

            final FacesContext context = FacesContext.getCurrentInstance();
            if (dataType.getId() != null) {
                stringId = String.valueOf(dataType.getId());
            }

            String label = dataType.getLabel().toLowerCase();
            commonObservationPrintable = dataType.getType().equals(DataTypeEnnumeration.INPUTTEXTAREA.getCode()) && (label.equalsIgnoreCase(COMMON_OBSERVATION_LABEL) || label.startsWith(COMMON_OBSERVATION_LABEL));

            if (!commonObservationPrintable) {
                final HtmlPanelGroup htmlPanelGroup = (HtmlPanelGroup) context.getApplication().createComponent(HtmlPanelGroup.COMPONENT_TYPE);

                final HtmlOutputLabel labelOutput = (HtmlOutputLabel) context.getApplication().createComponent(HtmlOutputLabel.COMPONENT_TYPE);
                if (FacesContext.getCurrentInstance().getViewRoot().getLocale().equals(Locale.FRENCH)) {
                    labelOutput.setValue(dataType.getLabel());
                } else {
                    labelOutput.setValue(dataType.getLabelEn());
                }
                htmlPanelGroup.getChildren().add(labelOutput);

                if (dataType.getRequired()) {
                    final HtmlOutputLabel labelOutputRequired = (HtmlOutputLabel) context.getApplication().createComponent(HtmlOutputLabel.COMPONENT_TYPE);
                    labelOutputRequired.setValue("*");
                    labelOutputRequired.setStyleClass(STYLE_CLASS);
                    htmlPanelGroup.getChildren().add(labelOutputRequired);
                }

                dipatchDiv.getChildren().add(htmlPanelGroup);
            }

            if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXT.getCode())) {
                final HtmlInputText inputText = (HtmlInputText) context.getApplication().createComponent(HtmlInputText.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    inputText.setRequired(true);
                }
                inputText.setId(ID_DISPATCH_LABEL + stringId);
                if (FacesContext.getCurrentInstance().getViewRoot().getLocale().equals(Locale.FRENCH)) {
                    inputText.setLabel(dataType.getLabel());
                } else {
                    inputText.setLabel(dataType.getLabelEn());
                }
                dipatchDiv.getChildren().add(inputText);
            } else if (dataType.getType().equals(DataTypeEnnumeration.CHEKBOX.getCode())) {
                final HtmlSelectBooleanCheckbox booleanCheckbox = (HtmlSelectBooleanCheckbox) context.getApplication().createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    booleanCheckbox.setRequired(true);
                }
                booleanCheckbox.setId(ID_DISPATCH_LABEL + stringId);
                if (FacesContext.getCurrentInstance().getViewRoot().getLocale().equals(Locale.FRENCH)) {
                    booleanCheckbox.setLabel(dataType.getLabel());
                } else {
                    booleanCheckbox.setLabel(dataType.getLabelEn());
                }
                dipatchDiv.getChildren().add(booleanCheckbox);

            } else if (dataType.getType().equals(DataTypeEnnumeration.CALENDAR.getCode())) {
                final Calendar calendar = (Calendar) context.getApplication().createComponent(Calendar.COMPONENT_TYPE);
                calendar.setShowOn("button");
                if (dataType.getRequired()) {
                    calendar.setRequired(true);
                }
                calendar.setId(ID_DISPATCH_LABEL + stringId);
                calendar.setPattern("dd-MM-yyyy");
                calendar.setLocale(Locale.FRANCE);
                calendar.setNavigator(true);
                if (FacesContext.getCurrentInstance().getViewRoot().getLocale().equals(Locale.FRENCH)) {
                    calendar.setLabel(dataType.getLabel());
                } else {
                    calendar.setLabel(dataType.getLabelEn());
                }
                dipatchDiv.getChildren().add(calendar);

            } else if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXTAREA.getCode()) && !commonObservationPrintable) {
                final HtmlInputTextarea inputTextarea = (HtmlInputTextarea) context.getApplication().createComponent(HtmlInputTextarea.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    inputTextarea.setRequired(true);
                }
                inputTextarea.setRows(10);
                inputTextarea.setId(ID_DISPATCH_LABEL + stringId);
                if (FacesContext.getCurrentInstance().getViewRoot().getLocale().equals(Locale.FRENCH)) {
                    inputTextarea.setLabel(dataType.getLabel());
                } else {
                    inputTextarea.setLabel(dataType.getLabelEn());
                }
                dipatchDiv.getChildren().add(inputTextarea);
            } else if (commonObservationPrintable) {
                commonObservationRequired = BooleanUtils.toBoolean(dataType.getRequired());
            }
        }
    }

    /**
     * Group file items to send by flow.
     *
     * @param mapToGroup the map to group
     * @return the map
     */
    public Map<Flow, List<FileItem>> groupFileItemsToSendByFlow(final Map<FileItem, Flow> mapToGroup) {
        // grouper les flow par file items (pour generer après plusieurs
        // fichiers ebxml au cas ou il ya plusieurs flux sortant par articles)
        final Map<Flow, List<FileItem>> returnedMap = new HashMap<>();

        for (final FileItem fileItem : mapToGroup.keySet()) {
            if (returnedMap.containsKey(mapToGroup.get(fileItem))) {
                returnedMap.get(mapToGroup.get(fileItem)).add(fileItem);

            } else {
                final List<FileItem> groupedFileItemsToAdd = new ArrayList<>();
                groupedFileItemsToAdd.add(fileItem);
                returnedMap.put(mapToGroup.get(fileItem), groupedFileItemsToAdd);
            }
        }

        return returnedMap;
    }

    /**
     * Adds the inspection report controller.
     */
    public void addControllerInspectionReport() {
        if (!Objects.equals(controllerForInspectionReport, null)) {
            inspectionControllers.add(controllerForInspectionReport);
        }
        controllerForInspectionReport = null;
    }

    /**
     * Prepare add.
     */
    public void prepareAdd() {
        if (controllerForInspectionReport == null) {
            controllerForInspectionReport = new InspectionController();
        }
    }

    /**
     * Removes the controller inspection report.
     *
     * @param controller the controller
     */
    public void removeControllerInspectionReport(final InspectionController controller) {
        for (final Iterator<InspectionController> iter = inspectionControllers.iterator(); iter.hasNext();) {
            final InspectionController s = iter.next();
            if (s.getName().equals(controller.getName())) {
                iter.remove();
            }
        }
        final String dataTableId = ComponentUtils.findComponentClientId("controllersDT");
        RequestContext.getCurrentInstance().update(dataTableId);

    }

    /**
     * Send decisions.
     */
    public synchronized void sendDecisions() {
        logger.info("--------------------------------------------- Prise d'une décision CCT - DEBUT ----------------------------------------------");
        logger.info(currentFile.toString());
        if (!userConfirmationAllowed) {
            return;
        }

        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(GLOBAL_PROPAGATION_TRANSACTION_BEHAVIOUR);
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {

            List<ItemFlow> lastItemFlowList = null;
            ItemFlow lastItemFlow = null;
            if (currentFile.getFileItemsList() != null) {
                lastItemFlowList = itemFlowService.findLastItemFlowsByFileItemList(currentFile.getFileItemsList());
                if (lastItemFlowList != null && !lastItemFlowList.isEmpty()) {
                    lastItemFlow = lastItemFlowList.get(0);
                }
            }

            if (currentFile.getFileItemsList() != null && cotationAllowed != null && cotationAllowed && (lastItemFlowList != null && lastItemFlow != null && !DECISION_FLOW_LIST_AT_COTATION_LEVEL.contains(lastItemFlow.getFlow().getCode()))) {
                itemFlowService.sendDecisionsToDispatchCctFile(currentFile, productInfoItemsEnabled);

                JsfUtil.addSuccessMessageAfterRedirect(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.SEND_SUCCESS));
                goToDetailPage();
            } else if (hasDraftAndEnabledForDecision()) {
                boolean allHasDecision = true;
                boolean sameStep = true;
                for (FileItem selected : currentFile.getFileItemsList()) {
                    if (!currentStep.getStepCode().equals(selected.getStep().getStepCode())) {
                        sameStep = false;
                        break;
                    }
                }
                if (sameStep) {
                    for (FileItem selected : currentFile.getFileItemsList()) {
                        if (BooleanUtils.isNotTrue(selected.getDraft())) {
                            break;
                        } else {
                            ItemFlow draftItemFlow = itemFlowService.findDraftByFileItem(selected);
                            if (draftItemFlow != null && draftItemFlow.getSender() != null && draftItemFlow.getSender().getId() != null && !draftItemFlow.getSender().getId().equals(getLoggedUser().getId())) {
                                break;
                            }
                        }
                    }
                    for (FileItem selected : currentFile.getFileItemsList()) {
                        ItemFlow draftItemFlow = itemFlowService.findDraftByFileItem(selected);
                        if (draftItemFlow != null
                                && (Arrays.asList(FlowCode.FL_CT_29.name(), FlowCode.FL_CT_31.name(), FlowCode.FL_CT_64.name(),
                                        FlowCode.FL_CT_66.name()).contains(draftItemFlow.getFlow().getCode()) || isFstpReadyForSignature(selectedFlow) || isAtReadyForSignature(selectedFlow))) {
                            allHasDecision = true;
                            break;
                        }
                    }
                }
                if (!allHasDecision) {
                    showErrorFacesMessage(ControllerConstants.Bundle.Messages.ALL_PRODUCT_MUST_BE_SELECTED, null);
                    if (transactionStatus != null) {
                        transactionManager.rollback(transactionStatus);
                    }
                    return;
                }

                if (CollectionUtils.isNotEmpty(productInfoItemsEnabled)) {

                    Flow flow = flowService.findFlowBySentFileItem(productInfoItemsEnabled.get(0));
                    if (!Objects.equals(flow, null) && AJOURNEMENT_FLOW_LIST.contains(flow.getCode())) {
                        Appointment rdv = appointmentService.findAppoitmentByFileItemAndController(productInfoItemsEnabled,
                                loggedUser);
                        if (rdv != null) {
                            rdv.setDeleted(Boolean.TRUE);
                            appointmentService.update(rdv);
                        }
                    }

                    for (FileItem fileItem : productInfoItemsEnabled) {
                        if (fileItem.getDraft()) {
                            ItemFlow draftItemFlow = itemFlowService.findDraftByFileItem(fileItem);
                            FileItem fItem = fileItemService.find(fileItem.getId());
                            addTrendPerformance(fItem, draftItemFlow);
                        }

                    }
                    Map<FileItem, Flow> mapWithinAllFileItemAndFlowsToSend = itemFlowService.sendDecisions(currentFile, productInfoItemsEnabled);

                    if (!mapWithinAllFileItemAndFlowsToSend.isEmpty()) {

                        pottingReportService.updateAfterSignature(currentFile);

                        Map<Flow, List<FileItem>> groupedFlow = groupFileItemsToSendByFlow(mapWithinAllFileItemAndFlowsToSend);
                        for (Flow flowToSend : groupedFlow.keySet()) {
                            List<FileItem> fileItemList = groupedFlow.get(flowToSend);
                            List<ItemFlow> itemFlowList = itemFlowService.findLastItemFlowsByFileItemList(fileItemList);
                            String service = StringUtils.EMPTY;
                            String documentType = StringUtils.EMPTY;

                            //generate report
                            Map<String, byte[]> attachedByteFiles = null;
                            String reportNumber;
                            if (Arrays.asList(FlowCode.FL_CT_89.name(), FlowCode.FL_CT_08.name(),
                                    FlowCode.FL_CT_114.name(), FlowCode.FL_CT_117.name(), FlowCode.FL_CT_140.name(),
                                    FlowCode.FL_CT_CVS_03.name(), FlowCode.FL_CT_CVS_07.name(), FlowCode.FL_CT_162.name(), FlowCode.FL_CT_169.name(), FlowCode.FL_CT_CCS_05.name())
                                    .contains(flowToSend.getCode())) {
                                // edit signature elements
                                Date now = java.util.Calendar.getInstance().getTime();
                                currentFile.setSignatureDate(now);
                                currentFile.setSignatory(getLoggedUser());

                                attachedByteFiles = new HashMap<>();

                                List<FileTypeFlowReport> fileTypeFlowReportList = fileTypeFlowReportService.findReportClassNameByFlowAndFileType(flowToSend, currentFile.getFileType());
                                for (final FileTypeFlowReport fileTypeFlowReport : fileTypeFlowReportList) {

                                    if (!Arrays.asList(FileTypeCode.CCT_CSV, FileTypeCode.CCT_CT_E_PVE).contains(currentFile.getFileType().getCode())) {
                                        String reportFieldCode = fileTypeFlowReport.getFileFieldName();
                                        FileFieldValue reportFieldValue = fileFieldValueService.findValueByFileFieldAndFile(reportFieldCode, currentFile);
                                        if (reportFieldValue == null) {
                                            ReportOrganism reportOrganism = reportOrganismService.findReportByFileTypeFlowReport(fileTypeFlowReport);
                                            FileField reportField = fileFieldService.findFileFieldByCodeAndFileType(reportFieldCode, fileTypeFlowReport.getFileType().getCode());
                                            String eforceRef = currentFile.getNumeroDemande();
                                            reportNumber = String.format("%s/%s%s", eforceRef,
                                                    java.util.Calendar.getInstance().get(java.util.Calendar.YEAR),
                                                    ((reportOrganism != null && reportOrganism.getValue() != null) ? reportOrganism.getValue() : StringUtils.EMPTY));
                                            reportFieldValue = new FileFieldValue();
                                            reportFieldValue.setFile(currentFile);
                                            reportFieldValue.setFileField(reportField);
                                            reportFieldValue.setValue(reportNumber);
                                            currentFile.getFileFieldValueList().add(reportFieldValue);
                                            fileFieldValueService.save(reportFieldValue);
                                        }
                                    }

                                    byte[] report = getReportBytes(currentFile, fileTypeFlowReport, false);
                                    if (report == null) {
                                        showErrorFacesMessage(ControllerConstants.Bundle.Messages.CANNOT_GENERATE_REPORT, null);
                                        if (transactionStatus != null) {
                                            transactionManager.rollback(transactionStatus);
                                        }
                                        return;
                                    }

                                    attachedByteFiles.put(fileTypeFlowReport.getReportName(), report);
                                }

                                fileService.update(currentFile);

                                // si validation d'une demande de modification
                                File root = currentFile.getParent();
                                if (root != null && FileTypeCode.CCT_CSV.equals(currentFile.getFileType().getCode())) {
                                    FileUtils.applyModifications(currentFile, root);
                                    fileFieldValueService.saveOrUpdateList(root.getFileFieldValueList());
                                }
                            } else if (Arrays.asList(FlowCode.FL_CT_121.name(), FlowCode.FL_CT_133.name()).contains(flowToSend.getCode())) {

                                attachedByteFiles = new HashMap<>();

                                List<FileTypeFlowReport> fileTypeFlowReportList = fileTypeFlowReportService.findReportClassNameByFlowAndFileType(flowToSend, currentFile.getFileType());
                                FileTypeFlowReport fileTypeFlowReport = fileTypeFlowReportList.get(0);
                                byte[] report = getReportBytes(currentFile, fileTypeFlowReport, false);

                                if (report == null) {
                                    showErrorFacesMessage(ControllerConstants.Bundle.Messages.CANNOT_GENERATE_REPORT, null);
                                    if (transactionStatus != null) {
                                        transactionManager.rollback(transactionStatus);
                                    }
                                    return;
                                }

                                attachedByteFiles.put(fileTypeFlowReport.getReportName(), report);
                            }

                            // convert file to document
                            Serializable documentSerializable = xmlConverterService.convertFileToDocument(currentFile, fileItemList, itemFlowList, flowToSend);

                            // prepare document to send
                            byte[] xmlBytes;
                            ByteArrayOutputStream output = SendDocumentUtils.prepareCctDocument(documentSerializable, service, documentType);
                            xmlBytes = output.toByteArray();

                            if (CollectionUtils.isNotEmpty(flowToSend.getCopyRecipientsList())) {
                                List<CopyRecipient> copyRecipients = flowToSend.getCopyRecipientsList();
                                for (CopyRecipient copyRecipient : copyRecipients) {
                                    logger.info("SEND COPY RECIPIENT TO {}", copyRecipient.getToAuthority().getRole());
                                    send(transactionStatus, xmlBytes, attachedByteFiles, service, documentType, copyRecipient.getToAuthority().getRole(), itemFlowList);
                                }
                            } else {
                                send(transactionStatus, xmlBytes, attachedByteFiles, service, documentType, ebxmlPropertiesService.getToPartyId(), itemFlowList);
                            }
                        }
                    }

                    cotationService.dispatch(currentFile, selectedFlow);

                    JsfUtil.addSuccessMessageAfterRedirect(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.SEND_SUCCESS));

                    decisionDiv.getChildren().clear();
                    goToDetailPage();
                }
            } else {
                logger.warn("cannot send the décision : " + currentFile.getNumeroDossier());
                showErrorFacesMessage(ControllerConstants.Bundle.Messages.SEND_ERROR, null);
            }

            TransactionStatus tsCommit = transactionStatus;
            transactionStatus = null;
            transactionManager.commit(tsCommit);
            if (logger.isDebugEnabled()) {
                logger.info("####SEND DECISION Transaction commited####");
            }

            notificationEmail(currentFile, currentStep);
        } catch (Exception e) {
            logger.error(currentFile.toString(), e);
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.SEND_ERROR, null);
        } finally {
            if (transactionStatus != null) {
                transactionManager.rollback(transactionStatus);
                if (logger.isDebugEnabled()) {
                    logger.info("####SEND DECISION Transaction rollbacked####");
                }
            }
        }
        logger.info("--------------------------------------------- Prise d'une décision CCT - FIN ----------------------------------------------");
    }

    private void send(TransactionStatus transactionStatus, byte[] xmlBytes, Map<String, byte[]> attachedByteFiles, String service, String documentType, String toPartyId, List<ItemFlow> itemFlowList) {
        Map<String, Object> data = new HashMap<>();
        data.put(ESBConstants.FLOW, xmlBytes);
        data.put(ESBConstants.ATTACHMENT, attachedByteFiles);
        data.put(ESBConstants.SERVICE, service);
        data.put(ESBConstants.TYPE_DOCUMENT, documentType);
        data.put(ESBConstants.MESSAGE, null);
        data.put(ESBConstants.EBXML_TYPE, "STANDARD");
        data.put(ESBConstants.TO_PARTY_ID, toPartyId);
        data.put(ESBConstants.DEAD, "0");
        //
        data.put(ESBConstants.ITEM_FLOWS, itemFlowList);
        if (!fileProducer.sendFile(data)) {
            if (transactionStatus != null) {
//                transactionManager.rollback(transactionStatus);
            }
            logger.warn("cannot send the décision : " + currentFile.getNumeroDossier());
            messageToSendService.saveOrUpadateNotSendedMessageAsMessageToResend(data);
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.SEND_ERROR, null);
            return;
        } else {
            messageToSendService.deleteNotSendedMessageIfExistsAsMessageToResend(data);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Message sent to OUT queue");
        }
    }

    /**
     * Go to detail page.
     */
    @Override
    protected void goToDetailPage() {
        try {
            setIndexPageUrl(ControllerConstants.Pages.FO.DASHBOARD_CCT_INDEX_PAGE);
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext extContext = context.getExternalContext();
            String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, indexPageUrl));
            extContext.redirect(url);
        } catch (IOException ex) {
            if (currentFile != null) {
                logger.error(currentFile.toString(), ex);
            } else {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * Checks for draft and enabled for decision.
     *
     * @return the boolean
     */
    public Boolean hasDraftAndEnabledForDecision() {
        if (productInfoChecks != null && !productInfoChecks.isEmpty()) {
            for (FileItemCheck fileItemCheck : productInfoChecks) {
                if (fileItemCheck.getFileItem().getDraft() && fileItemCheck.getEnabledCheck()) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * Refresh view for product informations. (Dans l'affichage du button
     * Decider : pas de controle sur la selection des cases à cocher car il n'y
     * a pas de handler l'ors de l'unchek des checkbox pour detecter l'action)
     */
    public void refreshViewForProductInformations() {
        decisionButtonAllowed = Boolean.FALSE;
        cotationButtonAllowed = Boolean.FALSE;
        decisionButtonAllowedAtCotationLevel = Boolean.FALSE;

        if (CollectionUtils.isEmpty(productInfoItems)) {
            productInfoItems = initFileItemsForCCt();
        }

        // Gestion des Button pour la décision Par dossier
        if (BooleanUtils.isTrue(decisionByFileAllowed)) {
            // Cas de Cotation
            if (BooleanUtils.isTrue(cotationAllowed)) {
                // Tous les FileItem sont en Draft Mode
                if (allFileItemInListAreDraft(productInfoItemsEnabled)) {
                    cotationButtonAllowed = false;
                    rollBackDecisionsAllowed = true;
                    sendDecisionAllowed = true;
                    decisionButtonAllowedAtCotationLevel = false;
                } else {
                    cotationButtonAllowed = true;
                    rollBackDecisionsAllowed = false;
                    sendDecisionAllowed = false;
                    if (COTATION_STEP_LIST_ALLOW_DECISION.contains(productInfoItems.get(0).getStep().getStepCode())) {
                        setDecisionButtonAllowedAtCotationLevel(Boolean.TRUE);
                    }
                    if (StepCode.ST_CT_03.equals(productInfoItems.get(0).getStep().getStepCode()) && isCheckMinepiaMinistry() && !sendDecisionAllowed) {
                        setDecisionButtonAllowed(Boolean.TRUE);
                    }
                }
            } // Cas de Decision
            else if (BooleanUtils.isTrue(decisionAllowed)) {
                // Tous les FileItem sont en Draft Mode
                if (allFileItemInListAreDraft(productInfoItemsEnabled)) {
                    decisionButtonAllowed = false;
                    rollBackDecisionsAllowed = true;
                    sendDecisionAllowed = true;
                } else {
                    decisionButtonAllowed = true;
                    rollBackDecisionsAllowed = false;
                    sendDecisionAllowed = false;
                }
            }
        } // Cas de La decision MIXTE
        else // Tous les FileItem sont en Draft Mode
        if (allFileItemInListAreDraft(productInfoItemsEnabled)) {
            rollBackDecisionsAllowed = true;
            sendDecisionAllowed = true;
            decisionButtonAllowed = false;
        } // Les FileItem appartenant à ce dossier contienne un ou plus en
        // mode DRAFT
        else if (oneFileItemInListIsDraft(productInfoItemsEnabled)) {
            rollBackDecisionsAllowed = true;
            sendDecisionAllowed = true;
            decisionButtonAllowed = true;
        } // Les FileItem n'ont pas de DRAFT
        else if (CollectionUtils.isNotEmpty(productInfoItemsEnabled)) {
            // Dans l'affichage du button Decider : pas de controle sur la
            // selection des cases à cocher car il n'y a pas de handler
            // l'ors de l'unchek des checkbox pour detecter l'action
            rollBackDecisionsAllowed = false;
            sendDecisionAllowed = false;
            decisionButtonAllowed = true;
        }
    }

    /**
     * Reset data grid inofrmation products.
     */
    public void resetDataGridInofrmationProducts() {
        refreshViewForProductInformations();

        enabledDecisionByFile = Boolean.TRUE;
        selectAllDecisions = Boolean.FALSE;
        selectAllRollBack = Boolean.FALSE;
        selectedFlow = null;

        setProductInfoItems(null);
        setProductInfoItemsEnabled(null);
        setProductInfoChecksfiltred(null);
        setProductInfoChecks(null);
        setDisionSystemAllowed(false);

        checkGenerateDraftAllowed();

    }

    /**
     * One file item in list is draft.
     *
     * @param fileItems the file items
     * @return true, if successful
     */
    private boolean oneFileItemInListIsDraft(final List<FileItem> fileItems) {
        Boolean oneIsNotDraft = false;
        if (fileItems != null && !fileItems.isEmpty()) {
            for (final FileItem fileItem : fileItems) {
                if (fileItem.getDraft() && !getDisabledRow(fileItem)) {
                    oneIsNotDraft = true;
                    break;
                }
            }
        }
        return oneIsNotDraft;
    }

    /**
     * All file item in list are draft.
     *
     * @param fileItems the file items
     * @return the boolean
     */
    public Boolean allFileItemInListAreDraft(final List<FileItem> fileItems) {
        Boolean allDraft = false;
        Boolean oneIsNotDraft = false;
        if (fileItems != null && !fileItems.isEmpty()) {
            for (final FileItem fileItem : fileItems) {
                if (fileItem.getDraft()) {
                    allDraft = true;
                } else {
                    oneIsNotDraft = true;
                }
            }
        }
        return allDraft && !oneIsNotDraft;
    }

    /**
     * Disable draft from product info item.
     *
     * @param infoItems the info items
     * @return the list
     */
    private List<FileItem> disableDraftFromProductInfoItem(final List<FileItem> infoItems) {
        final List<FileItem> returnedInfoItems = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(productInfoChecks) && !decisionByFileAllowed) {
            for (final FileItem fileItem : infoItems) {
                for (final FileItemCheck check : productInfoChecks) {
                    if (check.getRollbackCheck() && fileItem.getId().equals(check.getFileItem().getId())
                            && fileItem.getDraft() != null && fileItem.getDraft()) {
                        fileItem.setDraft(false);
                    }
                    returnedInfoItems.add(fileItem);
                }
            }
            return returnedInfoItems;
        } else {
            for (final FileItem fileItem : infoItems) {
                if (fileItem.getDraft() != null && fileItem.getDraft()) {
                    fileItem.setDraft(false);
                }
                returnedInfoItems.add(fileItem);
            }
            return returnedInfoItems;
        }

    }

    /**
     * Adds the trend performance.
     *
     * @param fileItem the file item
     * @param itemFlow the item flow
     */
    private void addTrendPerformance(final FileItem fileItem, final ItemFlow itemFlow) {
        final TrendPerformance trendPerformance = new TrendPerformance();

        trendPerformance.setItemFlow(itemFlow);
        trendPerformance.setUser(getLoggedUser());
        final String userDecision = generateUserDecision(itemFlow.getFlow());
        trendPerformance.setUserDecision(userDecision);

        synthesisConfig = GrUtilsWeb.loadSynthesisConfigSettings(paramsOrganismService.findParamsOrganismByOrganism(
                getCurrentOrganism(), ParamsCategory.GR));
        synthesisResult = riskService.getSynthesis(synthesisConfig, fileItem);

        final String systemDecision = GrUtilsWeb.getScenario(synthesisConfig, synthesisResult).name();
        trendPerformance.setSystemDecision(systemDecision);
        trendPerformance.setConcordance(systemDecision.equals(userDecision));
        trendPerformanceService.update(trendPerformance);
    }

    /**
     * Generate user decision.
     *
     * @param flow the flow
     * @return the string
     */
    private String generateUserDecision(final Flow flow) {
        // VQ ou APE
        if (FlowCode.FL_CT_17.name().equals(flow.getCode()) || FlowCode.FL_CT_19.name().equals(flow.getCode())) {
            return ScenarioType.SIA.name();
        } // RDD ou RDDF
        else if (FlowCode.FL_CT_11.name().equals(flow.getCode()) || FlowCode.FL_CT_13.name().equals(flow.getCode())
                || FlowCode.FL_CT_33.name().equals(flow.getCode()) || FlowCode.FL_CT_35.name().equals(flow.getCode())) {
            return ScenarioType.RDD.name();
        } // CCT
        else if (FlowCode.FL_CT_07.name().equals(flow.getCode())) {
            return ScenarioType.CCT.name();
        }
        return ScenarioType.AD.name();
    }

    /**
     * Gets the current organism.
     *
     * @return the current organism
     */
    public Organism getCurrentOrganism() {
        if (currentOrganism == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                currentOrganism = (Organism) session.getAttribute("currentOrganism");
            }
        }
        return currentOrganism;
    }

    /**
     * Gets the current service.
     *
     * @return the current service
     */
    public Service getCurrentService() {
        if (currentService == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                currentService = (Service) session.getAttribute("currentService");
            }
        }
        return currentService;
    }

    /**
     * Convert file items to file items cheks and check enabled file items.
     *
     * @param fileItems the file items
     * @param enabledFileItems the enabled file items
     * @return the list
     */
    public List<FileItemCheck> convertFileItemsToFileItemsCheksAndCheckEnabledFileItems(final List<FileItem> fileItems,
            final List<FileItem> enabledFileItems) {
        final List<FileItemCheck> returnList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(fileItems)) {
            for (final FileItem fileItem : fileItems) {
                final FileItemCheck fileItemCheck = new FileItemCheck(fileItem, false, false, false);
                if (CollectionUtils.isNotEmpty(enabledFileItems)) {
                    final FileItem selected = (FileItem) CollectionUtils.find(enabledFileItems, new Predicate() {
                        @Override
                        public boolean evaluate(final Object object) {
                            return ((FileItem) object).getId().equals(fileItem.getId());
                        }
                    });
                    if (selected != null) {
                        fileItemCheck.setEnabledCheck(true);
                    }
                }
                returnList.add(fileItemCheck);
            }
        }

        return returnList;
    }

    /**
     * Save recommandation.
     */
    public void saveRecommandation() {
        try {
            if (selectedRecommandation != null && selectedRecommandation.getValue().trim().length() > 0) {

                selectedRecommandation.setFile(this.getCurrentFile());
                selectedRecommandation.setFileItem(null);
                selectedRecommandation.setSupervisor(loggedUser);
                selectedRecommandation.setCreated(java.util.Calendar.getInstance().getTime());

                selectedRecommandation.setAuthorizedAuthorityList(new ArrayList<RecommandationAuthority>());

                for (final Authority authority : authoritiesList.getTarget()) {
                    final RecommandationAuthorityId recommandationAuthorityId = new RecommandationAuthorityId();
                    recommandationAuthorityId.setAuthority(authority);
                    recommandationAuthorityId.setRecommandation(selectedRecommandation);

                    final RecommandationAuthority recommandationAuthority = new RecommandationAuthority(recommandationAuthorityId);

                    selectedRecommandation.getAuthorizedAuthorityList().add(recommandationAuthority);
                }

                recommandationService.save(selectedRecommandation);

                final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString("RecommandationCreated");
                JsfUtil.addSuccessMessage(msg);

                if (!JsfUtil.isValidationFailed()) {
                    selectedRecommandation = null;
                    refreshRecommandationList();
                }
            } else {
                showErrorFacesMessage(ControllerConstants.Bundle.Messages.CREATE_RECOMMANDATION_REQUIRED_MESSAGE, null);
            }
        } catch (final Exception e) {
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED, null);
        }

    }

    /**
     * Edits the recommandation.
     */
    public void editRecommandation() {
        try {
            if (selectedRecommandation != null && selectedRecommandation.getValue().trim().length() > 0) {
                selectedRecommandation.setCreated(java.util.Calendar.getInstance().getTime());

                selectedRecommandation.setAuthorizedAuthorityList(new ArrayList<RecommandationAuthority>());
                for (final Authority authority : authoritiesList.getTarget()) {
                    final RecommandationAuthorityId recommandationAuthorityId = new RecommandationAuthorityId();
                    recommandationAuthorityId.setAuthority(authority);
                    recommandationAuthorityId.setRecommandation(selectedRecommandation);

                    final RecommandationAuthority recommandationAuthority = new RecommandationAuthority(recommandationAuthorityId);
                    selectedRecommandation.getAuthorizedAuthorityList().add(recommandationAuthority);
                    recommandationAuthorityService.update(recommandationAuthority);
                }

                selectedRecommandation.setCreated(java.util.Calendar.getInstance().getTime());

                recommandationService.update(selectedRecommandation);

                final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(Recommandation.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
                JsfUtil.addSuccessMessage(msg);

                selectedRecommandation = null;
                refreshRecommandationList();
            } else {
                final String errorMsg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.EDIT_RECOMMANDATION_REQUIRED_MESSAGE);
                JsfUtil.addErrorMessage(errorMsg);
            }
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED));
        }
    }

    /**
     * Delete recommandation.
     */
    public void deleteRecommandation() {
        try {
            final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    Recommandation.class.getSimpleName() + PersistenceActions.DELETE.getCode());
            JsfUtil.addSuccessMessage(msg);

            recommandationService.delete(selectedRecommandation);
            if (!JsfUtil.isValidationFailed()) {
                selectedRecommandation = null;
                refreshRecommandationList();
            }
            selectedRecommandation = null;

        } catch (final Exception e) {
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED, null);
        }

    }

    /**
     * Check is allowad recommandation.
     *
     * @return the boolean
     */
    private Boolean checkIsAllowadRecommandation() {
        final List<UserAuthorityFileType> userAFTList = userAuthorityFileTypeService.findUserAuthorityFileTypeByUserList(loggedUser
                .getMergedDelegatorList());

        for (final UserAuthorityFileType uaft : userAFTList) {
            if (AuthorityConstants.SUPERVISEUR.getCode().equals(uaft.getUserAuthority().getAuthorityGranted().getRole())
                    && currentFile.getFileType().equals(uaft.getFileType())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * is allowed recommandation.
     *
     * @return the boolean
     */
    public boolean isAllowedRecommandation() {
        return allowedRecommandation;
    }

    /**
     * Disabled row.
     *
     * @param fileItem the file item
     * @return true, if successful
     */
    public boolean getDisabledRow(final FileItem fileItem) {
        return !(productInfoItemsEnabled != null && productInfoItemsEnabled.contains(fileItem));
    }

    /**
     * Check is allowed user for current step.
     */
    public void checkIsAllowedUserForCurrentStep() {
        final List<String> authorityTypes = loggedUser.getAuthoritiesList();
        Boolean authorizedUser = Boolean.FALSE;
        for (final FileItem fileItem : productInfoItems) {
            for (final Authority stepAut : fileItem.getStep().getRoleList()) {
                if (authorityTypes.contains(stepAut.getRole())) {
                    authorizedUser = Boolean.TRUE;
                    break;
                }
            }
        }
        if (!authorizedUser) {
            decisionButtonAllowed = Boolean.FALSE;
            cotationButtonAllowed = Boolean.FALSE;
            decisionButtonAllowedAtCotationLevel = Boolean.FALSE;
            decisionAllowed = Boolean.FALSE;
        }
    }

    /**
     * Prepare create recommandation.
     */
    public void prepareCreateRecommandation() {
        selectedRecommandation = new Recommandation();

        authorityList = fileTypeService.findAuthoritiesByFileType(selectedFileItemCheck.getFileItem().getFile().getFileType());
        authoritiesList = new DualListModel<>(new ArrayList<Authority>(), new ArrayList<Authority>());
        authoritiesList.getSource().addAll(authorityList);
    }

    /**
     * Prepare edit recommandation.
     */
    public void prepareEditRecommandation() {
        this.setSelectedRecommandation(recommandationService.find(this.getSelectedRecommandation().getId()));

        authorityList = fileTypeService.findAuthoritiesByFileType(currentFile.getFileType());

        authoritiesList = new DualListModel<>(new ArrayList<Authority>(), new ArrayList<Authority>());
        authoritiesList.getSource().addAll(authorityList);

        for (final RecommandationAuthority recAuthority : this.getSelectedRecommandation().getAuthorizedAuthorityList()) {
            for (final Authority authority1 : authoritiesList.getSource()) {
                if (recAuthority.getAuthority().getId().equals(authority1.getId())) {
                    authoritiesList.getTarget().add(recAuthority.getAuthority());
                    authoritiesList.getSource().remove(recAuthority.getAuthority());
                    break;
                }
            }
        }

    }

    /**
     * Refresh recommandation list.
     */
    public void refreshRecommandationList() {
        recommandationList = recommandationService.findRecommandationByFileAndAuthorties(currentFile, (new ArrayList<>(getLoggedUser().getAuthorities())));
    }

    /**
     * Save recommandation article.
     */
    public void saveRecommandationArticle() {
        try {
            if (selectedRecommandationArticle != null && selectedRecommandationArticle.getValue().trim().length() > 0) {

                selectedRecommandationArticle.setFile(null);
                selectedRecommandationArticle.setFileItem(selectedFileItemCheck.getFileItem());
                selectedRecommandationArticle.setSupervisor(loggedUser);
                selectedRecommandationArticle.setCreated(java.util.Calendar.getInstance().getTime());

                selectedRecommandationArticle.setAuthorizedAuthorityList(new ArrayList<RecommandationAuthority>());

                for (final Authority authority : authoritiesList.getTarget()) {
                    final RecommandationAuthorityId recommandationAuthorityId = new RecommandationAuthorityId();
                    recommandationAuthorityId.setAuthority(authority);
                    recommandationAuthorityId.setRecommandation(selectedRecommandationArticle);

                    final RecommandationAuthority recommandationAuthority = new RecommandationAuthority(recommandationAuthorityId);

                    selectedRecommandationArticle.getAuthorizedAuthorityList().add(recommandationAuthority);
                }

                recommandationService.save(selectedRecommandationArticle);

                final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString("RecommandationCreated");
                JsfUtil.addSuccessMessage(msg);
                if (!JsfUtil.isValidationFailed()) {
                    refreshRecommandationArticleList();
                    selectedRecommandationArticle = null;
                }
            } else {
                showErrorFacesMessage(ControllerConstants.Bundle.Messages.CREATE_RECOMMANDATION_REQUIRED_MESSAGE, null);
            }
        } catch (final Exception e) {
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED, null);
        }

    }

    /**
     * Edits the recommandation article.
     */
    public void editRecommandationArticle() {
        try {
            if (selectedRecommandationArticle != null && selectedRecommandationArticle.getValue().trim().length() > 0) {
                selectedRecommandationArticle.setCreated(java.util.Calendar.getInstance().getTime());

                selectedRecommandationArticle.setAuthorizedAuthorityList(new ArrayList<RecommandationAuthority>());
                for (final Authority authority : authoritiesList.getTarget()) {
                    final RecommandationAuthorityId recommandationAuthorityId = new RecommandationAuthorityId();
                    recommandationAuthorityId.setAuthority(authority);
                    recommandationAuthorityId.setRecommandation(selectedRecommandationArticle);

                    final RecommandationAuthority recommandationAuthority = new RecommandationAuthority(recommandationAuthorityId);
                    selectedRecommandationArticle.getAuthorizedAuthorityList().add(recommandationAuthority);
                    recommandationAuthorityService.update(recommandationAuthority);
                }

                recommandationService.update(selectedRecommandationArticle);

                final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(Recommandation.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
                JsfUtil.addSuccessMessage(msg);
                selectedRecommandationArticle = null;
                refreshRecommandationArticleList();
            } else {
                showErrorFacesMessage(ControllerConstants.Bundle.Messages.EDIT_RECOMMANDATION_REQUIRED_MESSAGE, null);
            }
        } catch (final Exception e) {
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED, null);
        }
    }

    /**
     * Delete recommandation article.
     */
    public void deleteRecommandationArticle() {

        try {
            final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    Recommandation.class.getSimpleName() + PersistenceActions.DELETE.getCode());
            JsfUtil.addSuccessMessage(msg);

            recommandationService.delete(selectedRecommandationArticle);
            if (!JsfUtil.isValidationFailed()) {
                selectedRecommandationArticle = null;
            }

            selectedRecommandationArticle = null;

            refreshRecommandationArticleList();
        } catch (final Exception e) {
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED, null);
        }
    }

    /**
     * Concatenate active index string.
     *
     * @param collection the collection
     * @return the string
     */
    public String concatenateActiveIndexString(final Collection<Tab> collection) {
        final StringBuilder out = new StringBuilder();
        for (int i = 0; i < collection.size(); i++) {
            out.append(i);
            if (i + 1 != collection.size()) {
                out.append(applicationPropretiesService.getColumnSeparator());
            }
        }
        return out.toString();
    }

    /**
     * Group file item field values.
     */
    private void groupFileItemFieldValues() {
        if (fieldGroupsItems.isEmpty()) {
            fieldGroupsItems = fieldGroupService.findAllByFileType(currentFile.getFileType(), "02");
        }
        fileItemFieldGroupDtos = new ArrayList<>();
        for (final FieldGroup fieldGroupItem : fieldGroupsItems) {
            final FieldGroupDto<FileItemFieldValue> fileItemFieldGroupDto = new FieldGroupDto<>();
            fileItemFieldGroupDto.setLabelFr(fieldGroupItem.getLabelFr());
            fileItemFieldGroupDto.setLabelEn(fieldGroupItem.getLabelEn());
            final List<FileItemFieldValue> listFileItemFieldValues = new ArrayList<>();
            for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValues) {
                if (fieldGroupItem.getId().equals(fileItemFieldValue.getFileItemField().getGroup().getId())) {
                    listFileItemFieldValues.add(fileItemFieldValue);
                }
            }

            fileItemFieldGroupDto.setFieldValues(listFileItemFieldValues);
            if (fieldGroupItem.getId().equals(1L)) {
                final FileItemFieldValue fileItemFieldValueFob = generateFobValue();
                final FileItemFieldValue fileItemFieldValueQtit = generateQuantityValue();
                if (fileItemFieldValueFob != null) {
                    listFileItemFieldValues.add(fileItemFieldValueFob);
                }

                if (fileItemFieldValueQtit != null) {
                    listFileItemFieldValues.add(fileItemFieldValueQtit);
                }

                populateFileItemGeneralGroup(fileItemFieldGroupDto);
            }
            if (!listFileItemFieldValues.isEmpty()) {
                fileItemFieldGroupDtos.add(fileItemFieldGroupDto);
            }
        }
    }

    /**
     * Generate fob value.
     *
     * @return the file item field value
     */
    private FileItemFieldValue generateFobValue() {
        FileItemFieldValue fileItemFieldValueFob = null;
        if (currentFileItem != null && StringUtils.isNotBlank(currentFileItem.getFobValue())) {
            final FileItemField fileItemFieldFob = new FileItemField();
            final String fobLabelFr = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE)
                    .getString("ProductDetailsLabel_fobLabel");
            final String fobLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.ENGLISH)
                    .getString("ProductDetailsLabel_fobLabel");
            fileItemFieldFob.setLabelFr(fobLabelFr);
            fileItemFieldFob.setLabelEn(fobLabelEn);
            fileItemFieldValueFob = new FileItemFieldValue();
            fileItemFieldValueFob.setFileItemField(fileItemFieldFob);
            fileItemFieldValueFob.setValue(currentFileItem.getFobValue());
        }
        return fileItemFieldValueFob;
    }

    /**
     * Generate quantity value.
     *
     * @return the file item field value
     */
    private FileItemFieldValue generateQuantityValue() {
        FileItemFieldValue fileItemFieldValueQuanity = null;
        if (currentFileItem != null && StringUtils.isNotBlank(currentFileItem.getQuantity())) {
            final FileItemField fileItemFieldQtit = new FileItemField();
            final String qtitLabelFr = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE)
                    .getString("ProductDetailsLabel_quantity");
            final String qtitLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.ENGLISH)
                    .getString("ProductDetailsLabel_quantity");
            fileItemFieldQtit.setLabelFr(qtitLabelFr);
            fileItemFieldQtit.setLabelEn(qtitLabelEn);
            fileItemFieldValueQuanity = new FileItemFieldValue();
            fileItemFieldValueQuanity.setFileItemField(fileItemFieldQtit);
            fileItemFieldValueQuanity.setValue(currentFileItem.getQuantity());
        }
        return fileItemFieldValueQuanity;
    }

    /**
     * Populate file item general group.
     *
     * @param fileFieldGroupDto the file field group dto
     */
    private void populateFileItemGeneralGroup(final FieldGroupDto<FileItemFieldValue> fileFieldGroupDto) {
        final List<FileItemFieldValue> fieldValues = fileFieldGroupDto.getFieldValues();

        if (selectedFileItemCheck.getFileItem().getNsh() != null) {
            // NSH : GoodsItemDesc
            final String goodsItemDescLabelFr = ResourceBundle
                    .getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("ProductDetailsLabel_item");
            final String goodsItemDescLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                    Locale.ENGLISH).getString("ProductDetailsLabel_item");
            final FileItemField fileItemFieldGoodsItemDesc = new FileItemField();
            fileItemFieldGoodsItemDesc.setLabelFr(goodsItemDescLabelFr);
            fileItemFieldGoodsItemDesc.setLabelEn(goodsItemDescLabelEn);
            final FileItemFieldValue goodsItemDesc = new FileItemFieldValue();
            goodsItemDesc.setValue(getCurrentLocale().equals(Locale.FRANCE) ? selectedFileItemCheck.getFileItem().getNsh()
                    .getGoodsItemDesc() : selectedFileItemCheck.getFileItem().getNsh().getGoodsItemDescEn());
            goodsItemDesc.setFileItemField(fileItemFieldGoodsItemDesc);
            fieldValues.add(0, goodsItemDesc);

            // NSH : GoodsItemCode
            final String goodsItemCodeLabelFr = ResourceBundle
                    .getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("ProductDetailsLabel_nsh");
            final String goodsItemCodeLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                    Locale.ENGLISH).getString("ProductDetailsLabel_nsh");
            final FileItemField fileItemFieldGoodsItemCode = new FileItemField();
            fileItemFieldGoodsItemCode.setLabelFr(goodsItemCodeLabelFr);
            fileItemFieldGoodsItemCode.setLabelEn(goodsItemCodeLabelEn);
            final FileItemFieldValue goodsItemCode = new FileItemFieldValue();
            goodsItemCode.setValue(selectedFileItemCheck.getFileItem().getNsh().getGoodsItemCode());
            goodsItemCode.setFileItemField(fileItemFieldGoodsItemCode);
            fieldValues.add(0, goodsItemCode);
        }

        if (selectedFileItemCheck.getFileItem().getSubfamily() != null) {
            // Subfamily : label
            final String subfamilyLabelLabelFr = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                    Locale.FRANCE).getString("ProductDetailsLabel_subfamily");
            final String subfamilyLabelLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                    Locale.ENGLISH).getString("ProductDetailsLabel_subfamily");
            final FileItemField fileItemFieldSubfamilyLabel = new FileItemField();
            fileItemFieldSubfamilyLabel.setLabelFr(subfamilyLabelLabelFr);
            fileItemFieldSubfamilyLabel.setLabelEn(subfamilyLabelLabelEn);
            final FileItemFieldValue subfamilyLabel = new FileItemFieldValue();
            subfamilyLabel.setValue(selectedFileItemCheck.getFileItem().getSubfamily().getLabel());
            subfamilyLabel.setFileItemField(fileItemFieldSubfamilyLabel);
            fieldValues.add(0, subfamilyLabel);

            // Subfamily : code
            final String subfamilyCodeLabelFr = ResourceBundle
                    .getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("ProductDetailsLabel_code");
            final String subfamilyCodeLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                    Locale.ENGLISH).getString("ProductDetailsLabel_code");
            final FileItemField fileItemFieldSubfamilyCode = new FileItemField();
            fileItemFieldSubfamilyCode.setLabelFr(subfamilyCodeLabelFr);
            fileItemFieldSubfamilyCode.setLabelEn(subfamilyCodeLabelEn);
            final FileItemFieldValue subfamilyCode = new FileItemFieldValue();
            subfamilyCode.setValue(selectedFileItemCheck.getFileItem().getSubfamily().getCode());
            subfamilyCode.setFileItemField(fileItemFieldSubfamilyCode);
            fieldValues.add(0, subfamilyCode);
        }
        fileFieldGroupDto.setFieldValues(fieldValues);
    }

    /**
     * Show error faces message.
     *
     * @param bundle the bundle
     * @param clientId the client id
     */
    private void showErrorFacesMessage(final String bundle, final String clientId) {
        final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, FacesContext.getCurrentInstance().getViewRoot().getLocale()).getString(bundle);
        if (StringUtils.isEmpty(clientId)) {
            JsfUtil.addErrorMessage(msg);
        } else {
            JsfUtil.addErrorMessage(clientId, msg);
        }
        logger.error(msg);
    }

    /**
     * Gets the other certficat goodness.
     *
     * @return the other certficat goodness
     */
    public List<CertficatGoodness> getOtherCertficatGoodness() {
        return Arrays.asList(CertficatGoodness.values());
    }

    /**
     * Prepare create recommandation article.
     */
    public void prepareCreateRecommandationArticle() {
        selectedRecommandationArticle = new Recommandation();

        authorityList = fileTypeService.findAuthoritiesByFileType(selectedFileItemCheck.getFileItem().getFile().getFileType());
        authoritiesList = new DualListModel<>(new ArrayList<Authority>(), new ArrayList<Authority>());
        authoritiesList.getSource().addAll(authorityList);
    }

    /**
     * Prepare edit recommandation article.
     */
    public void prepareEditRecommandationArticle() {
        this.setSelectedRecommandationArticle(recommandationService.find(this.getSelectedRecommandationArticle().getId()));
        authorityList = fileTypeService.findAuthoritiesByFileType(selectedFileItemCheck.getFileItem().getFile().getFileType());

        authoritiesList = new DualListModel<>(new ArrayList<Authority>(), new ArrayList<Authority>());
        authoritiesList.getSource().addAll(authorityList);

        for (final RecommandationAuthority recAuthority : this.getSelectedRecommandationArticle().getAuthorizedAuthorityList()) {
            for (final Authority authority1 : authoritiesList.getSource()) {
                if (recAuthority.getAuthority().getId().equals(authority1.getId())) {
                    authoritiesList.getTarget().add(recAuthority.getAuthority());
                    authoritiesList.getSource().remove(recAuthority.getAuthority());
                    break;
                }
            }
        }
    }

    /**
     * Refresh recommandation article list.
     */
    public void refreshRecommandationArticleList() {
        recommandationArticleList = recommandationService.findRecommandationByFileItemAndAuthorties(selectedFileItemCheck.getFileItem(), (new ArrayList<>(getLoggedUser().getAuthorities())));
    }

    /**
     * Check generate draft allowed.
     */
    private void checkGenerateDraftAllowed() {
        Flow reportingFlow = flowService.findFlowByCode(FlowCode.FL_CT_08.name());

        if (StepCode.ST_CT_63.equals(currentFileItem.getStep().getStepCode()) || StepCode.ST_CT_64.equals(currentFileItem.getStep().getStepCode())) {
            reportingFlow = flowService.findFlowByCode(FlowCode.FL_CT_140.name());
        }

        if (FileTypeCode.CCT_CT_E_PVE.equals(currentFile.getFileType().getCode()) && currentFile.getParent() != null) {
            reportingFlow = flowService.findFlowByCode(FlowCode.FL_CT_114.name());
        }

        if (FileTypeCode.CCS_MINSANTE.equals(currentFile.getFileType().getCode()) && currentFile.getParent() != null) {
            reportingFlow = flowService.findFlowByCode(FlowCode.FL_CT_CCS_05.name());
        }

        boolean okInvoice = StepCode.ST_CT_57.equals(currentFileItem.getStep().getStepCode()) || StepCode.ST_CT_60.equals(currentFileItem.getStep().getStepCode());
        ItemFlow draftItemFlow = itemFlowService.findDraftByFileItem(currentFileItem);
        okInvoice = okInvoice && draftItemFlow != null && Arrays.asList(FlowCode.FL_CT_121.name(), FlowCode.FL_CT_133.name()).contains(draftItemFlow.getFlow().getCode());
        if (okInvoice && draftItemFlow != null) {
            reportingFlow = draftItemFlow.getFlow();
        }

        Flow currentDecision = selectedFlow;
        if (currentDecision == null && draftItemFlow != null) {
            currentDecision = draftItemFlow.getFlow();
        }

        fileTypeFlowReportsDraft = fileTypeFlowReportService.findReportClassNameByFlowAndFileType(reportingFlow, currentFile.getFileType());
        generateDraftAllowed = !Arrays.asList(FileTypeCode.CCT_CSV).contains(currentFile.getFileType().getCode()) && sendDecisionAllowed && showDecisionButton && displayGenerateDraft
                && ((Arrays.asList(StepCode.ST_CT_38, StepCode.ST_CT_31, StepCode.ST_CT_53, StepCode.ST_CT_55, StepCode.ST_CT_56, StepCode.ST_CT_64, StepCode.ST_CT_CCS_02).contains(currentFileItem.getStep().getStepCode())
                && reportingFlow.equals(currentDecision))
                || (currentDecision != null && StepCode.ST_CT_65.equals(currentFileItem.getStep().getStepCode()) && FlowCode.FL_CT_153.name().equals(currentDecision.getCode()))
                || okInvoice || isPveReadyForSignature(currentDecision))
                && CollectionUtils.isNotEmpty(fileTypeFlowReportsDraft);
    }

    /**
     * Check generate report allowed.
     */
    private void checkGenerateReportAllowed() {
        final Flow reportingFlow = flowService.findByToStep(currentFileItem.getStep());
        fileTypeFlowReports = fileTypeFlowReportService.findReportClassNameByFlowAndFileType(reportingFlow, currentFile.getFileType());
        generateReportAllowed = StepCode.ST_CT_06.equals(currentFileItem.getStep().getStepCode()) && CollectionUtils.isNotEmpty(fileTypeFlowReports);
    }

    /**
     * Download report.
     *
     * @param draft
     * @return the streamed content
     */
    public synchronized StreamedContent downloadReport(boolean draft) {
        List<FileTypeFlowReport> fileTypeFlowReportsList = draft ? new ArrayList<>(fileTypeFlowReportsDraft) : new ArrayList<>(fileTypeFlowReports);
        if (CollectionUtils.isEmpty(fileTypeFlowReportsList)) {
            return null;
        }

        if (currentFile.getSignatory() == null) {
            currentFile.setSignatory(getLoggedUser());
        }
        if (currentFile.getSignatureDate() == null) {
            currentFile.setSignatureDate(java.util.Calendar.getInstance().getTime());
        }

        for (FileTypeFlowReport fileTypeFlowReport : fileTypeFlowReportsList) {
            //Begin Add new field value with report Number
            try {
                byte[] report = getReportBytes(currentFile, fileTypeFlowReport, draft);
                if (report != null) {
                    InputStream is = new ByteArrayInputStream(report);
                    String name = currentFile.getReferenceSiat() + '_' + fileTypeFlowReport.getReportName();
                    if (draft) {
                        name = "PREVIEW_".concat(name);
                    }

                    StreamedContent fileToDownload = new DefaultStreamedContent(is, "application/pdf", name);

                    return fileToDownload;
                }
            } catch (Exception e) {
                logger.error(currentFile.toString(), e);
                final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.GENERATE_REPORT_FAILED);
                JsfUtil.addErrorMessage(msg);
            }
        }

        return null;
    }

    public synchronized StreamedContent downloadReferenceBaseReport(final boolean draft) {
        return downloadReportByFile(currentFile.getParent());
    }

    public synchronized StreamedContent downloadReportByFile(File file) {

        if (file == null) {
            return null;
        }

        FileItem pfi = file.getFileItemsList().get(0);
        final Flow reportingFlow = flowService.findByToStep(pfi.getStep(), file.getFileType());

        List<FileTypeFlowReport> ftfr = fileTypeFlowReportService.findReportClassNameByFlowAndFileType(reportingFlow, file.getFileType());

        if (CollectionUtils.isEmpty(ftfr)) {
            return null;
        }

        for (final FileTypeFlowReport fileTypeFlowReport : ftfr) {
            try {
                final byte[] report = getReportBytes(file, fileTypeFlowReport, false);
                if (report != null) {
                    final InputStream is = new ByteArrayInputStream(report);
                    final StreamedContent fileToDownload = new DefaultStreamedContent(is, "application/pdf", file.getReferenceSiat() + '_' + fileTypeFlowReport.getReportName());

                    return fileToDownload;
                }
            } catch (Exception e) {
                logger.error(file.toString(), e);
                final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.GENERATE_REPORT_FAILED);
                JsfUtil.addErrorMessage(msg);
            }

        }

        return null;
    }

    /**
     * Gets the flows.
     *
     * @return the flows
     */
    public List<Flow> getFlows() {
        return flows;
    }

    /**
     * Sets the flows.
     *
     * @param flows the new flows
     */
    public void setFlows(final List<Flow> flows) {
        this.flows = flows;
    }

    /**
     * Gets the selected flow.
     *
     * @return the selected flow
     */
    public Flow getSelectedFlow() {
        return selectedFlow;
    }

    /**
     * Sets the selected flow.
     *
     * @param selectedFlow the new selected flow
     */
    public void setSelectedFlow(final Flow selectedFlow) {
        this.selectedFlow = selectedFlow;
    }

    /**
     * Gets the decision div.
     *
     * @return the decision div
     */
    public HtmlPanelGrid getDecisionDiv() {
        return decisionDiv;
    }

    /**
     * Sets the decision div.
     *
     * @param decisionDiv the new decision div
     */
    public void setDecisionDiv(final HtmlPanelGrid decisionDiv) {
        this.decisionDiv = decisionDiv;
    }

    /**
     * Sets the product info items.
     *
     * @param productInfoItems the new product info items
     */
    public void setProductInfoItems(final List<FileItem> productInfoItems) {
        this.productInfoItems = productInfoItems;
    }

    /**
     * Gets the last decisions.
     *
     * @return the lastDecisions
     */
    public ItemFlow getLastDecisions() {
        return lastDecisions;
    }

    /**
     * Sets the last decisions.
     *
     * @param lastDecisions the lastDecisions to set
     */
    public void setLastDecisions(final ItemFlow lastDecisions) {
        this.lastDecisions = lastDecisions;
    }

    /**
     * Gets the selected attachment.
     *
     * @return the selected attachment
     */
    public Attachment getSelectedAttachment() {
        return selectedAttachment;
    }

    /**
     * Sets the selected attachment.
     *
     * @param selectedAttachment the new selected attachment
     */
    public void setSelectedAttachment(final Attachment selectedAttachment) {
        this.selectedAttachment = selectedAttachment;
    }

    /**
     * Gets the selected recommandation.
     *
     * @return the selected recommandation
     */
    public Recommandation getSelectedRecommandation() {
        return selectedRecommandation;
    }

    /**
     * Sets the selected recommandation.
     *
     * @param selectedRecommandation the new selected recommandation
     */
    public void setSelectedRecommandation(final Recommandation selectedRecommandation) {
        this.selectedRecommandation = selectedRecommandation;
    }

    /**
     * Gets the product info checks.
     *
     * @return the product info checks
     */
    public List<FileItemCheck> getProductInfoChecks() {
        if (productInfoItems == null || productInfoChecks == null) {
            selectedFileItemCheck = new FileItemCheck(getCurrentFileItem(), false, false, false);

            getProductInfoItems();
            productInfoChecks = convertFileItemsToFileItemsCheksAndCheckEnabledFileItems(productInfoItems, productInfoItemsEnabled);
        }

        return productInfoChecks;
    }

    /**
     * Gets the product info checksfiltred.
     *
     * @return the product info checksfiltred
     */
    public List<FileItemCheck> getProductInfoChecksfiltred() {
        return productInfoChecksfiltred;
    }

    /**
     * Sets the product info checksfiltred.
     *
     * @param productInfoChecksfiltred the new product info checksfiltred
     */
    public void setProductInfoChecksfiltred(final List<FileItemCheck> productInfoChecksfiltred) {
        this.productInfoChecksfiltred = productInfoChecksfiltred;
    }

    /**
     * Gets the selected file item check.
     *
     * @return the selected file item check
     */
    public FileItemCheck getSelectedFileItemCheck() {
        return selectedFileItemCheck;
    }

    /**
     * Sets the selected file item check.
     *
     * @param selectedFileItemCheck the new selected file item check
     */
    public void setSelectedFileItemCheck(final FileItemCheck selectedFileItemCheck) {
        this.selectedFileItemCheck = selectedFileItemCheck;
    }

    /**
     * Gets the select all decisions.
     *
     * @return the select all decisions
     */
    public Boolean getSelectAllDecisions() {
        return selectAllDecisions;
    }

    /**
     * Sets the select all decisions.
     *
     * @param selectAllDecisions the new select all decisions
     */
    public void setSelectAllDecisions(final Boolean selectAllDecisions) {
        this.selectAllDecisions = selectAllDecisions;
    }

    /**
     * Gets the select all roll back.
     *
     * @return the select all roll back
     */
    public Boolean getSelectAllRollBack() {
        return selectAllRollBack;
    }

    /**
     * Sets the select all roll back.
     *
     * @param selectAllRollBack the new select all roll back
     */
    public void setSelectAllRollBack(final Boolean selectAllRollBack) {
        this.selectAllRollBack = selectAllRollBack;
    }

    /**
     * Sets the product info checks.
     *
     * @param productInfoChecks the new product info checks
     */
    public void setProductInfoChecks(final List<FileItemCheck> productInfoChecks) {
        this.productInfoChecks = productInfoChecks;
    }

    /**
     * Sets the logged user.
     *
     * @param loggedUser the loggedUser to set
     */
    public void setLoggedUser(final User loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * Sets the current service.
     *
     * @param currentService the new current service
     */
    public void setCurrentService(final Service currentService) {
        this.currentService = currentService;
    }

    /**
     * Gets the index page url.
     *
     * @return the index page url
     */
    public String getIndexPageUrl() {
        return indexPageUrl;
    }

    /**
     * Gets the detail index page url.
     *
     * @return the detail index page url
     */
    @Override
    public String getDetailIndexPageUrl() {
        return ControllerConstants.Pages.FO.DETAILS_CCT_INDEX_PAGE;
    }

    /**
     * Sets the index page url.
     *
     * @param indexPageUrl the new index page url
     */
    public void setIndexPageUrl(final String indexPageUrl) {
        this.indexPageUrl = indexPageUrl;
    }

    /**
     * Gets the product info items enabled.
     *
     * @return the product info items enabled
     */
    public List<FileItem> getProductInfoItemsEnabled() {
        return productInfoItemsEnabled;
    }

    /**
     * Sets the product info items enabled.
     *
     * @param productInfoItemsEnabled the new product info items enabled
     */
    public void setProductInfoItemsEnabled(final List<FileItem> productInfoItemsEnabled) {
        this.productInfoItemsEnabled = productInfoItemsEnabled;
    }

    /**
     * Gets the decision allowed.
     *
     * @return the decision allowed
     */
    public Boolean getDecisionAllowed() {
        return decisionAllowed;
    }

    /**
     * Sets the decision allowed.
     *
     * @param decisionAllowed the new decision allowed
     */
    public void setDecisionAllowed(final Boolean decisionAllowed) {
        this.decisionAllowed = decisionAllowed;
    }

    /**
     * Gets the selected recommandation article.
     *
     * @return the selected recommandation article
     */
    public Recommandation getSelectedRecommandationArticle() {
        return selectedRecommandationArticle;
    }

    /**
     * Sets the selected recommandation article.
     *
     * @param selectedRecommandationArticle the new selected recommandation
     * article
     */
    public void setSelectedRecommandationArticle(final Recommandation selectedRecommandationArticle) {
        this.selectedRecommandationArticle = selectedRecommandationArticle;
    }

    /**
     * Gets the disabled tab synthese.
     *
     * @return the disabledTabSynthese
     */
    public Boolean getDisabledTabSynthese() {
        return disabledTabSynthese;
    }

    /**
     * Sets the disabled tab synthese.
     *
     * @param disabledTabSynthese the disabledTabSynthese to set
     */
    public void setDisabledTabSynthese(final Boolean disabledTabSynthese) {
        this.disabledTabSynthese = disabledTabSynthese;
    }

    /**
     * Gets the decision by file allowed.
     *
     * @return the decisionByFileAllowed
     */
    public Boolean getDecisionByFileAllowed() {
        return decisionByFileAllowed;
    }

    /**
     * Sets the decision by file allowed.
     *
     * @param decisionByFileAllowed the decisionByFileAllowed to set
     */
    public void setDecisionByFileAllowed(final Boolean decisionByFileAllowed) {
        this.decisionByFileAllowed = decisionByFileAllowed;
    }

    /**
     * Gets the file item check list for decision by file allowed.
     *
     * @return the fileItemCheckListForDecisionByFileAllowed
     */
    public List<FileItemCheck> getFileItemCheckListForDecisionByFileAllowed() {
        return fileItemCheckListForDecisionByFileAllowed;
    }

    /**
     * Sets the file item check list for decision by file allowed.
     *
     * @param fileItemCheckListForDecisionByFileAllowed the
     * fileItemCheckListForDecisionByFileAllowed to set
     */
    public void setFileItemCheckListForDecisionByFileAllowed(final List<FileItemCheck> fileItemCheckListForDecisionByFileAllowed) {
        this.fileItemCheckListForDecisionByFileAllowed = fileItemCheckListForDecisionByFileAllowed;
    }

    /**
     * Gets the inspector list.
     *
     * @return the inspectorList
     */
    public List<User> getInspectorList() {
        return inspectorList;
    }

    /**
     * Sets the inspector list.
     *
     * @param inspectorList the inspectorList to set
     */
    public void setInspectorList(final List<User> inspectorList) {
        this.inspectorList = inspectorList;
    }

    /**
     * Gets the cotation allowed.
     *
     * @return the cotationAllowed
     */
    public Boolean getCotationAllowed() {
        return cotationAllowed;
    }

    /**
     * Sets the cotation allowed.
     *
     * @param cotationAllowed the cotationAllowed to set
     */
    public void setCotationAllowed(final Boolean cotationAllowed) {
        this.cotationAllowed = cotationAllowed;
    }

    /**
     * Gets the dipatch div.
     *
     * @return the dipatchDiv
     */
    public HtmlPanelGrid getDipatchDiv() {
        return dipatchDiv;
    }

    /**
     * Sets the dipatch div.
     *
     * @param dipatchDiv the dipatchDiv to set
     */
    public void setDipatchDiv(final HtmlPanelGrid dipatchDiv) {
        this.dipatchDiv = dipatchDiv;
    }

    /**
     * Gets the enabled decision by file.
     *
     * @return the enabled decision by file
     */
    public Boolean getEnabledDecisionByFile() {
        return enabledDecisionByFile;
    }

    /**
     * Sets the enabled decision by file.
     *
     * @param enabledDecisionByFile the new enabled decision by file
     */
    public void setEnabledDecisionByFile(final Boolean enabledDecisionByFile) {
        this.enabledDecisionByFile = enabledDecisionByFile;
    }

    /**
     * Gets the dision system allowed.
     *
     * @return the dision system allowed
     */
    public Boolean getDisionSystemAllowed() {
        return disionSystemAllowed;
    }

    /**
     * Sets the dision system allowed.
     *
     * @param disionSystemAllowed the new dision system allowed
     */
    public void setDisionSystemAllowed(final Boolean disionSystemAllowed) {
        this.disionSystemAllowed = disionSystemAllowed;
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
     * Gets the send decision allowed.
     *
     * @return the send decision allowed
     */
    public Boolean getSendDecisionAllowed() {
        return sendDecisionAllowed;
    }

    /**
     * Sets the send decision allowed.
     *
     * @param sendDecisionAllowed the new send decision allowed
     */
    public void setSendDecisionAllowed(final Boolean sendDecisionAllowed) {
        this.sendDecisionAllowed = sendDecisionAllowed;
    }

    /**
     * Gets the roll back decisions allowed.
     *
     * @return the roll back decisions allowed
     */
    public Boolean getRollBackDecisionsAllowed() {
        return rollBackDecisionsAllowed;
    }

    /**
     * Sets the roll back decisions allowed.
     *
     * @param rollBackDecisionsAllowed the new roll back decisions allowed
     */
    public void setRollBackDecisionsAllowed(final Boolean rollBackDecisionsAllowed) {
        this.rollBackDecisionsAllowed = rollBackDecisionsAllowed;
    }

    /**
     * Gets the assigned user for cotation.
     *
     * @return the assigned user for cotation
     */
    public User getAssignedUserForCotation() {
        return assignedUserForCotation;
    }

    /**
     * Sets the assigned user for cotation.
     *
     * @param assignedUserForCotation the new assigned user for cotation
     */
    public void setAssignedUserForCotation(final User assignedUserForCotation) {
        this.assignedUserForCotation = assignedUserForCotation;
    }

    /**
     * Gets the file field values.
     *
     * @return the fileFieldValues
     */
    public List<FileFieldValue> getFileFieldValues() {
        return fileFieldValues;
    }

    /**
     * Sets the file field values.
     *
     * @param fileFieldValues the fileFieldValues to set
     */
    public void setFileFieldValues(final List<FileFieldValue> fileFieldValues) {
        this.fileFieldValues = fileFieldValues;
    }

    /**
     * Gets the file item field values.
     *
     * @return the fileItemFieldValues
     */
    public List<FileItemFieldValue> getFileItemFieldValues() {
        return fileItemFieldValues;
    }

    /**
     * Sets the file item field values.
     *
     * @param fileItemFieldValues the fileItemFieldValues to set
     */
    public void setFileItemFieldValues(final List<FileItemFieldValue> fileItemFieldValues) {
        this.fileItemFieldValues = fileItemFieldValues;
    }

    /**
     * Gets the decision button allowed.
     *
     * @return the decisionButtonAllowed
     */
    public Boolean getDecisionButtonAllowed() {
        return decisionButtonAllowed;
    }

    /**
     * Sets the decision button allowed.
     *
     * @param decisionButtonAllowed the decisionButtonAllowed to set
     */
    public void setDecisionButtonAllowed(final Boolean decisionButtonAllowed) {
        this.decisionButtonAllowed = decisionButtonAllowed;
    }

    public Boolean getDecisionButtonAllowedAtCotationLevel() {
        return decisionButtonAllowedAtCotationLevel;
    }

    public void setDecisionButtonAllowedAtCotationLevel(Boolean decisionButtonAllowedAtCotationLevel) {
        this.decisionButtonAllowedAtCotationLevel = decisionButtonAllowedAtCotationLevel;
    }

    /**
     * Gets the cotation button allowed.
     *
     * @return the cotationButtonAllowed
     */
    public Boolean getCotationButtonAllowed() {
        return cotationButtonAllowed;
    }

    /**
     * Sets the cotation button allowed.
     *
     * @param cotationButtonAllowed the cotationButtonAllowed to set
     */
    public void setCotationButtonAllowed(final Boolean cotationButtonAllowed) {
        this.cotationButtonAllowed = cotationButtonAllowed;
    }

    /**
     * Gets the show remind decision form.
     *
     * @return the showRemindDecisionForm
     */
    public Boolean getShowRemindDecisionForm() {
        return showRemindDecisionForm;
    }

    /**
     * Sets the show remind decision form.
     *
     * @param showRemindDecisionForm the showRemindDecisionForm to set
     */
    public void setShowRemindDecisionForm(final Boolean showRemindDecisionForm) {
        this.showRemindDecisionForm = showRemindDecisionForm;
    }

    /**
     * Gets the show list recommandation article form.
     *
     * @return the showListRecommandationArticleForm
     */
    public Boolean getShowListRecommandationArticleForm() {
        return showListRecommandationArticleForm;
    }

    /**
     * Sets the show list recommandation article form.
     *
     * @param showListRecommandationArticleForm the
     * showListRecommandationArticleForm to set
     */
    public void setShowListRecommandationArticleForm(final Boolean showListRecommandationArticleForm) {
        this.showListRecommandationArticleForm = showListRecommandationArticleForm;
    }

    /**
     * Gets the show show attachment form.
     *
     * @return the showShowAttachmentForm
     */
    public Boolean getShowShowAttachmentForm() {
        return showShowAttachmentForm;
    }

    /**
     * Sets the show show attachment form.
     *
     * @param showShowAttachmentForm the showShowAttachmentForm to set
     */
    public void setShowShowAttachmentForm(final Boolean showShowAttachmentForm) {
        this.showShowAttachmentForm = showShowAttachmentForm;
    }

    /**
     * Gets the show product details form.
     *
     * @return the showProductDetailsForm
     */
    public Boolean getShowProductDetailsForm() {
        return showProductDetailsForm;
    }

    /**
     * Sets the show product details form.
     *
     * @param showProductDetailsForm the showProductDetailsForm to set
     */
    public void setShowProductDetailsForm(final Boolean showProductDetailsForm) {
        this.showProductDetailsForm = showProductDetailsForm;
    }

    /**
     * Gets the tab list.
     *
     * @return the tab list
     */
    public List<Tab> getTabList() {
        return tabList;
    }

    /**
     * Sets the tab list.
     *
     * @param tabList the new tab list
     */
    public void setTabList(final List<Tab> tabList) {
        this.tabList = tabList;
    }

    /**
     * Gets the tab index list.
     *
     * @return the tab index list
     */
    public String getTabIndexList() {
        return tabIndexList;
    }

    /**
     * Sets the tab index list.
     *
     * @param tabIndexList the new tab index list
     */
    public void setTabIndexList(final String tabIndexList) {
        this.tabIndexList = tabIndexList;
    }

    /**
     * Gets the inspection report data.
     *
     * @return the inspectionReportData
     */
    public InspectionReportData getInspectionReportData() {
        return inspectionReportData;
    }

    /**
     * Sets the inspection report data.
     *
     * @param inspectionReportData the inspectionReportData to set
     */
    public void setInspectionReportData(final InspectionReportData inspectionReportData) {
        this.inspectionReportData = inspectionReportData;
    }

    /**
     * Gets the controller for inspection report.
     *
     * @return the controllerForInspectionReport
     */
    public InspectionController getControllerForInspectionReport() {
        return controllerForInspectionReport;
    }

    /**
     * Sets the controller for inspection report.
     *
     * @param controllerForInspectionReport the controllerForInspectionReport to
     * set
     */
    public void setControllerForInspectionReport(final InspectionController controllerForInspectionReport) {
        this.controllerForInspectionReport = controllerForInspectionReport;
    }

    /**
     * Gets the decisions.
     *
     * @return the decisions
     */
    public List<DecisionsSuiteVisite> getDecisionsSuiteVisite() {
        return Arrays.asList(DecisionsSuiteVisite.values());

    }

    /**
     * Gets the field groups.
     *
     * @return the field groups
     */
    public List<FieldGroup> getFieldGroups() {
        return fieldGroups;
    }

    /**
     * Sets the field groups.
     *
     * @param fieldGroups the new field groups
     */
    public void setFieldGroups(final List<FieldGroup> fieldGroups) {
        this.fieldGroups = fieldGroups;
    }

    /**
     * Gets the file field group dtos.
     *
     * @return the file field group dtos
     */
    public List<FieldGroupDto<FileFieldValue>> getFileFieldGroupDtos() {
        return fileFieldGroupDtos;
    }

    /**
     * Sets the file field group dtos.
     *
     * @param fileFieldGroupDtos the new file field group dtos
     */
    public void setFileFieldGroupDtos(final List<FieldGroupDto<FileFieldValue>> fileFieldGroupDtos) {
        this.fileFieldGroupDtos = fileFieldGroupDtos;
    }

    /**
     * Gets the file item field group dtos.
     *
     * @return the file item field group dtos
     */
    public List<FieldGroupDto<FileItemFieldValue>> getFileItemFieldGroupDtos() {
        return fileItemFieldGroupDtos;
    }

    /**
     * Sets the file item field group dtos.
     *
     * @param fileItemFieldGroupDtos the new file item field group dtos
     */
    public void setFileItemFieldGroupDtos(final List<FieldGroupDto<FileItemFieldValue>> fileItemFieldGroupDtos) {
        this.fileItemFieldGroupDtos = fileItemFieldGroupDtos;
    }

    /**
     * Gets the selected item flow dto.
     *
     * @return the selected item flow dto
     */
    public ItemFlowDto getSelectedItemFlowDto() {
        return selectedItemFlowDto;
    }

    /**
     * Sets the selected item flow dto.
     *
     * @param selectedItemFlowDto the new selected item flow dto
     */
    public void setSelectedItemFlowDto(final ItemFlowDto selectedItemFlowDto) {
        this.selectedItemFlowDto = selectedItemFlowDto;
    }

    /**
     * Gets the inspection controllers.
     *
     * @return the inspectionControllers
     */
    public List<InspectionController> getInspectionControllers() {
        return inspectionControllers;
    }

    /**
     * Sets the inspection controllers.
     *
     * @param inspectionControllers the inspectionControllers to set
     */
    public void setInspectionControllers(final List<InspectionController> inspectionControllers) {
        this.inspectionControllers = inspectionControllers;
    }

    /**
     * Gets the products have same rdd status.
     *
     * @return the productsHaveSameRDDStatus
     */
    public Boolean getProductsHaveSameRDDStatus() {
        return productsHaveSameRDDStatus;
    }

    /**
     * Sets the products have same rdd status.
     *
     * @param productsHaveSameRDDStatus the productsHaveSameRDDStatus to set
     */
    public void setProductsHaveSameRDDStatus(final Boolean productsHaveSameRDDStatus) {
        this.productsHaveSameRDDStatus = productsHaveSameRDDStatus;
    }

    /**
     * Gets the chcked list size.
     *
     * @return the chcked list size
     */
    public int getChckedListSize() {
        return chckedListSize;
    }

    /**
     * Sets the chcked list size.
     *
     * @param chckedListSize the new chcked list size
     */
    public void setChckedListSize(final int chckedListSize) {
        this.chckedListSize = chckedListSize;
    }

    /**
     * Gets the analyse order.
     *
     * @return the analyse order
     */
    public AnalyseOrder getAnalyseOrder() {
        return analyseOrder;
    }

    /**
     * Sets the analyse order.
     *
     * @param analyseOrder the new analyse order
     */
    public void setAnalyseOrder(final AnalyseOrder analyseOrder) {
        this.analyseOrder = analyseOrder;
    }

    /**
     * Gets the analyse laboratories.
     *
     * @return the analyse laboratories
     */
    public List<Laboratory> getAnalyseLaboratories() {
        return analyseLaboratories;
    }

    /**
     * Sets the analyse laboratories.
     *
     * @param analyseLaboratories the new analyse laboratories
     */
    public void setAnalyseLaboratories(final List<Laboratory> analyseLaboratories) {
        this.analyseLaboratories = analyseLaboratories;
    }

    /**
     * Gets the selected laboratory.
     *
     * @return the selected laboratory
     */
    public Laboratory getSelectedLaboratory() {
        return selectedLaboratory;
    }

    /**
     * Sets the selected laboratory.
     *
     * @param selectedLaboratory the new selected laboratory
     */
    public void setSelectedLaboratory(final Laboratory selectedLaboratory) {
        this.selectedLaboratory = selectedLaboratory;
    }

    /**
     * Gets the analyse type dtos list.
     *
     * @return the analyse type dtos list
     */
    public List<AnalyseTypeDto> getAnalyseTypeDtosList() {
        return analyseTypeDtosList;
    }

    /**
     * Sets the analyse type dtos list.
     *
     * @param analyseTypeDtosList the new analyse type dtos list
     */
    public void setAnalyseTypeDtosList(final List<AnalyseTypeDto> analyseTypeDtosList) {
        this.analyseTypeDtosList = analyseTypeDtosList;
    }

    /**
     * Gets the analyse parts list.
     *
     * @return the analyse parts list
     */
    public List<AnalysePart> getAnalysePartsList() {
        return analysePartsList;
    }

    /**
     * Sets the analyse parts list.
     *
     * @param analysePartsList the new analyse parts list
     */
    public void setAnalysePartsList(final List<AnalysePart> analysePartsList) {
        this.analysePartsList = analysePartsList;
    }

    /**
     * Gets the analyse result.
     *
     * @return the analyse result
     */
    public AnalyseResult getAnalyseResult() {
        return analyseResult;
    }

    /**
     * Sets the analyse result.
     *
     * @param analyseResult the new analyse result
     */
    public void setAnalyseResult(final AnalyseResult analyseResult) {
        this.analyseResult = analyseResult;
    }

    /**
     * Gets the treatment type dtos list.
     *
     * @return the treatment type dtos list
     */
    public List<TreatmentTypeDto> getTreatmentTypeDtosList() {
        return treatmentTypeDtosList;
    }

    /**
     * Sets the treatment type dtos list.
     *
     * @param treatmentTypeDtosList the new treatment type dtos list
     */
    public void setTreatmentTypeDtosList(final List<TreatmentTypeDto> treatmentTypeDtosList) {
        this.treatmentTypeDtosList = treatmentTypeDtosList;
    }

    /**
     * Gets the selected treatment company.
     *
     * @return the selected treatment company
     */
    public TreatmentCompany getSelectedTreatmentCompany() {
        return selectedTreatmentCompany;
    }

    /**
     * Sets the selected treatment company.
     *
     * @param selectedTreatmentCompany the new selected treatment company
     */
    public void setSelectedTreatmentCompany(final TreatmentCompany selectedTreatmentCompany) {
        this.selectedTreatmentCompany = selectedTreatmentCompany;
    }

    /**
     * Gets the treatment order.
     *
     * @return the treatment order
     */
    public TreatmentOrder getTreatmentOrder() {
        return treatmentOrder;
    }

    /**
     * Sets the treatment order.
     *
     * @param treatmentOrder the new treatment order
     */
    public void setTreatmentOrder(final TreatmentOrder treatmentOrder) {
        this.treatmentOrder = treatmentOrder;
    }

    public TreatmentInfos getTreatmentInfos() {
        return treatmentInfos;
    }

    public void setTreatmentInfos(TreatmentInfos treatmentInfos) {
        this.treatmentInfos = treatmentInfos;
    }

    public TreatmentInfosCCSMinsante getTreatmentInfosCCSMinsante() {
        return treatmentInfosCCSMinsante;
    }

    public void setTreatmentInfosCCSMinsante(TreatmentInfosCCSMinsante treatmentInfosCCSMinsante) {
        this.treatmentInfosCCSMinsante = treatmentInfosCCSMinsante;
    }

    /**
     * Gets the treatment companys.
     *
     * @return the treatment companys
     */
    public List<TreatmentCompany> getTreatmentCompanys() {
        return treatmentCompanys;
    }

    /**
     * Sets the treatment companys.
     *
     * @param treatmentCompanys the new treatment companys
     */
    public void setTreatmentCompanys(final List<TreatmentCompany> treatmentCompanys) {
        this.treatmentCompanys = treatmentCompanys;
    }

    /**
     * Gets the treatment parts list.
     *
     * @return the treatment parts list
     */
    public List<TreatmentPart> getTreatmentPartsList() {
        return treatmentPartsList;
    }

    /**
     * Sets the treatment parts list.
     *
     * @param treatmentPartsList the new treatment parts list
     */
    public void setTreatmentPartsList(final List<TreatmentPart> treatmentPartsList) {
        this.treatmentPartsList = treatmentPartsList;
    }

    /**
     * Sets the current organism.
     *
     * @param currentOrganism the new current organism
     */
    public void setCurrentOrganism(final Organism currentOrganism) {
        this.currentOrganism = currentOrganism;
    }

    /**
     * Gets the analyses file managers.
     *
     * @return the analyses file managers
     */
    public List<UploadFileManager<AnalysePart>> getAnalysesFileManagers() {
        return analysesFileManagers;
    }

    /**
     * Sets the analyses file managers.
     *
     * @param analysesFileManagers the new analyses file managers
     */
    public void setAnalysesFileManagers(final List<UploadFileManager<AnalysePart>> analysesFileManagers) {
        this.analysesFileManagers = analysesFileManagers;
    }

    /**
     * Gets the treatment file managers.
     *
     * @return the treatment file managers
     */
    public List<UploadFileManager<TreatmentPart>> getTreatmentFileManagers() {
        return treatmentFileManagers;
    }

    /**
     * Sets the treatment file managers.
     *
     * @param treatmentFileManagers the new treatment file managers
     */
    public void setTreatmentFileManagers(final List<UploadFileManager<TreatmentPart>> treatmentFileManagers) {
        this.treatmentFileManagers = treatmentFileManagers;
    }

    /**
     * Gets the treatment result.
     *
     * @return the treatment result
     */
    public TreatmentResult getTreatmentResult() {
        return treatmentResult;
    }

    /**
     * Sets the treatment result.
     *
     * @param treatmentResult the new treatment result
     */
    public void setTreatmentResult(final TreatmentResult treatmentResult) {
        this.treatmentResult = treatmentResult;
    }

    /**
     * Gets the report map.
     *
     * @return the reportMap
     */
    public Map<Long, StreamedContent> getReportMap() {
        return reportMap;
    }

    /**
     * Sets the report map.
     *
     * @param reportMap the reportMap to set
     */
    public void setReportMap(final Map<Long, StreamedContent> reportMap) {
        this.reportMap = reportMap;
    }

    /**
     * Gets the specific decision.
     *
     * @return the specific decision
     */
    public CctSpecificDecision getSpecificDecision() {
        return specificDecision;
    }

    /**
     * Sets the specific decision.
     *
     * @param specificDecision the new specific decision
     */
    public void setSpecificDecision(final CctSpecificDecision specificDecision) {
        this.specificDecision = specificDecision;
    }

    public CctSpecificDecision getLastSpecificDecision() {
        return lastSpecificDecision;
    }

    public void setLastSpecificDecision(CctSpecificDecision lastSpecificDecision) {
        this.lastSpecificDecision = lastSpecificDecision;
    }

    /**
     * Gets the specific decisions history.
     *
     * @return the specific decisions history
     */
    public CctSpecificDecisionHistory getSpecificDecisionsHistory() {
        return specificDecisionsHistory;
    }

    /**
     * Sets the specific decisions history.
     *
     * @param specificDecisionsHistory the new specific decisions history
     */
    public void setSpecificDecisionsHistory(final CctSpecificDecisionHistory specificDecisionsHistory) {
        this.specificDecisionsHistory = specificDecisionsHistory;
    }

    /**
     * Sets the item flow history dto list filtred.
     *
     * @param itemFlowHistoryDtoListFiltred the new item flow history dto list
     * filtred
     */
    public void setItemFlowHistoryDtoListFiltred(final List<ItemFlowDto> itemFlowHistoryDtoListFiltred) {
        this.itemFlowHistoryDtoListFiltred = itemFlowHistoryDtoListFiltred;
    }

    /**
     * Gets the field groups items.
     *
     * @return the field groups items
     */
    public List<FieldGroup> getFieldGroupsItems() {
        return fieldGroupsItems;
    }

    /**
     * Sets the field groups items.
     *
     * @param fieldGroupsItems the new field groups items
     */
    public void setFieldGroupsItems(final List<FieldGroup> fieldGroupsItems) {
        this.fieldGroupsItems = fieldGroupsItems;
    }

    /**
     * Gets the allowed recommandation.
     *
     * @return the allowed recommandation
     */
    public Boolean getAllowedRecommandation() {
        return allowedRecommandation;
    }

    /**
     * Sets the allowed recommandation.
     *
     * @param allowedRecommandation the new allowed recommandation
     */
    public void setAllowedRecommandation(final Boolean allowedRecommandation) {
        this.allowedRecommandation = allowedRecommandation;
    }

    /**
     * Gets the item flow history dto list.
     *
     * @return the item flow history dto list
     */
    public List<ItemFlowDto> getItemFlowHistoryDtoList() {
        return itemFlowHistoryDtoList;
    }

    /**
     * Sets the item flow history dto list.
     *
     * @param itemFlowHistoryDtoList the new item flow history dto list
     */
    public void setItemFlowHistoryDtoList(final List<ItemFlowDto> itemFlowHistoryDtoList) {
        this.itemFlowHistoryDtoList = itemFlowHistoryDtoList;
    }

    /**
     * Sets the list user authority file types.
     *
     * @param listUserAuthorityFileTypes the new list user authority file types
     */
    public void setListUserAuthorityFileTypes(final List<UserAuthorityFileType> listUserAuthorityFileTypes) {
        this.listUserAuthorityFileTypes = listUserAuthorityFileTypes;
    }

    /**
     * Gets the synthesis config.
     *
     * @return the synthesis config
     */
    public SynthesisConfig getSynthesisConfig() {
        return synthesisConfig;
    }

    /**
     * Sets the synthesis config.
     *
     * @param synthesisConfig the new synthesis config
     */
    public void setSynthesisConfig(final SynthesisConfig synthesisConfig) {
        this.synthesisConfig = synthesisConfig;
    }

    /**
     * Gets the list params organisms.
     *
     * @return the list params organisms
     */
    public List<ParamsOrganism> getListParamsOrganisms() {
        return listParamsOrganisms;
    }

    /**
     * Sets the list params organisms.
     *
     * @param listParamsOrganisms the new list params organisms
     */
    public void setListParamsOrganisms(final List<ParamsOrganism> listParamsOrganisms) {
        this.listParamsOrganisms = listParamsOrganisms;
    }

    /**
     * Gets the synthesis result.
     *
     * @return the synthesis result
     */
    public org.guce.siat.core.gr.utils.SynthesisResult getSynthesisResult() {
        return synthesisResult;
    }

    /**
     * Sets the synthesis result.
     *
     * @param synthesisResult the new synthesis result
     */
    public void setSynthesisResult(final org.guce.siat.core.gr.utils.SynthesisResult synthesisResult) {
        this.synthesisResult = synthesisResult;
    }

    /**
     * Gets the authorities list.
     *
     * @return the authorities list
     */
    public DualListModel<Authority> getAuthoritiesList() {
        return authoritiesList;
    }

    /**
     * Sets the authorities list.
     *
     * @param authoritiesList the new authorities list
     */
    public void setAuthoritiesList(final DualListModel<Authority> authoritiesList) {
        this.authoritiesList = authoritiesList;
    }

    /**
     * Gets the authority list.
     *
     * @return the authority list
     */
    public List<Authority> getAuthorityList() {
        return authorityList;
    }

    /**
     * Sets the authority list.
     *
     * @param authorityList the new authority list
     */
    public void setAuthorityList(final List<Authority> authorityList) {
        this.authorityList = authorityList;
    }

    /**
     * Gets the detail.
     *
     * @return the detail
     */
    public Boolean getDetail() {
        return detail;
    }

    /**
     * Sets the detail.
     *
     * @param detail the new detail
     */
    public void setDetail(final Boolean detail) {
        this.detail = detail;
    }

    /**
     * Gets the recommandation list.
     *
     * @return the recommandation list
     */
    public List<Recommandation> getRecommandationList() {
        return recommandationList;
    }

    /**
     * Sets the recommandation list.
     *
     * @param recommandationList the new recommandation list
     */
    public void setRecommandationList(final List<Recommandation> recommandationList) {
        this.recommandationList = recommandationList;
    }

    /**
     * Gets the recommandation article list.
     *
     * @return the recommandation article list
     */
    public List<Recommandation> getRecommandationArticleList() {
        return recommandationArticleList;
    }

    /**
     * Sets the recommandation article list.
     *
     * @param recommandationArticleList the new recommandation article list
     */
    public void setRecommandationArticleList(final List<Recommandation> recommandationArticleList) {
        this.recommandationArticleList = recommandationArticleList;
    }

    /**
     * Gets the item flow history dto list filtred.
     *
     * @return the item flow history dto list filtred
     */
    public List<ItemFlowDto> getItemFlowHistoryDtoListFiltred() {
        return itemFlowHistoryDtoListFiltred;
    }

    public CCTCPParamValueService getcCTCPParamValueService() {
        return cCTCPParamValueService;
    }

    public void setcCTCPParamValueService(CCTCPParamValueService cCTCPParamValueService) {
        this.cCTCPParamValueService = cCTCPParamValueService;
    }

    public ParamCCTCPService getParamCCTCPService() {
        return paramCCTCPService;
    }

    public void setParamCCTCPService(ParamCCTCPService paramCCTCPService) {
        this.paramCCTCPService = paramCCTCPService;
    }

    public boolean isPhytoEnd() {
        return phytoEnd;
    }

    public void setPhytoEnd(boolean phytoEnd) {
        this.phytoEnd = phytoEnd;
    }

    /**
     * Gets the generate report allowed.
     *
     * @return the generateReportAllowed
     */
    public Boolean getGenerateReportAllowed() {
        return generateReportAllowed;
    }

    /**
     * Sets the generate report allowed.
     *
     * @param generateReportAllowed the generateReportAllowed to set
     */
    public void setGenerateReportAllowed(final Boolean generateReportAllowed) {
        this.generateReportAllowed = generateReportAllowed;
    }

    /**
     * Notification email.
     *
     * @param file the file
     * @param step the step
     */
    public void notificationEmail(final File file, final Step step) {

        final Map<String, String> map = new HashMap<>();

        String object;

        if (file.getAssignedUser() != null) {
            final User usr = file.getAssignedUser();

            if ("FR".equals(usr.getPreferedLanguage())) {
                object = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString(
                        ControllerConstants.Bundle.Messages.OBJECT_MAIL_NOTIFICATION_RECEPT_FOLDER);
                map.put(MailConstants.SUBJECT, object);
                map.put(MailConstants.VMF, EMAIL_BODY_NOTIFICATION_FR);
            } else if ("EN".equals(usr.getPreferedLanguage())) {
                object = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.ENGLISH).getString(
                        ControllerConstants.Bundle.Messages.OBJECT_MAIL_NOTIFICATION_RECEPT_FOLDER);
                map.put(MailConstants.SUBJECT, object);
                map.put(MailConstants.VMF, EMAIL_BODY_NOTIFICATION_EN);
            }

            map.put("firstName", usr.getFirstName());
            map.put(MailConstants.FROM, mailService.getFromValue());
            map.put(MailConstants.EMAIL, usr.getEmail());
            map.put("referenceSiat", file.getReferenceSiat());

            mailService.sendMail(map);

        } else {

            final Administration administration = administrationService.find(file.getBureau().getId());
            final List<Administration> adminList = new ArrayList<>();
            adminList.add(administration);

            // get the bureaus  for the administration of the logged user and their delegator users
            final List<Bureau> bureauList = SiatUtils.findCombinedBureausByAdministrationList(adminList);

            final List<User> users = userService.findByStepAndFileTypeAndAdministration(step.getId(), file.getFileType().getId(),
                    bureauList);

            for (final User usr : users) {

                if ("FR".equals(usr.getPreferedLanguage())) {
                    object = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString(
                            ControllerConstants.Bundle.Messages.OBJECT_MAIL_NOTIFICATION_RECEPT_FOLDER);
                    map.put(MailConstants.SUBJECT, object);
                    map.put(MailConstants.VMF, EMAIL_BODY_NOTIFICATION_FR);
                } else if ("EN".equals(usr.getPreferedLanguage())) {
                    object = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.ENGLISH).getString(
                            ControllerConstants.Bundle.Messages.OBJECT_MAIL_NOTIFICATION_RECEPT_FOLDER);
                    map.put(MailConstants.SUBJECT, object);
                    map.put(MailConstants.VMF, EMAIL_BODY_NOTIFICATION_EN);
                }

                map.put("firstName", usr.getFirstName());
                map.put(MailConstants.FROM, mailService.getFromValue());
                map.put(MailConstants.EMAIL, usr.getEmail());
                map.put("referenceSiat", file.getReferenceSiat());

                mailService.sendMail(map);

            }

        }

    }

    /**
     * Gets the show decision button.
     *
     * @return the showDecisionButton
     */
    public Boolean getShowDecisionButton() {
        return showDecisionButton;
    }

    /**
     * Sets the show decision button.
     *
     * @param showDecisionButton the showDecisionButton to set
     */
    public void setShowDecisionButton(final Boolean showDecisionButton) {
        this.showDecisionButton = showDecisionButton;
    }

    /**
     * Gets the infraction list.
     *
     * @return the infraction list
     */
    public List<Infraction> getInfractionList() {
        return infractionList;
    }

    /**
     * Sets the infraction list.
     *
     * @param infractionList the new infraction list
     */
    public void setInfractionList(final List<Infraction> infractionList) {
        this.infractionList = infractionList;
    }

    /**
     * Gets the infraction.
     *
     * @return the infraction
     */
    public Infraction getInfraction() {
        return infraction;
    }

    /**
     * Sets the infraction.
     *
     * @param infraction the new infraction
     */
    public void setInfraction(final Infraction infraction) {
        this.infraction = infraction;
    }

    /**
     * Checks if is check minepded ministry.
     *
     * @return true, if is check minepded ministry
     */
    public boolean isCheckMinepdedMinistry() {
        return checkMinepdedMinistry;
    }

    /**
     * Sets the check minepded ministry.
     *
     * @param checkMinepdedMinistry the new check minepded ministry
     */
    public void setCheckMinepdedMinistry(final boolean checkMinepdedMinistry) {
        this.checkMinepdedMinistry = checkMinepdedMinistry;
    }

    public boolean isCheckMinaderMinistry() {
        return checkMinaderMinistry;
    }

    public void setCheckMinaderMinistry(boolean checkMinaderMinistry) {
        this.checkMinaderMinistry = checkMinaderMinistry;
    }

    /**
     * Sets the check minepia ministry.
     *
     * @param checkMinepiaMinistry the new check minepia ministry
     */
    public void setCheckMinepiaMinistry(final boolean checkMinepiaMinistry) {
        this.checkMinepiaMinistry = checkMinepiaMinistry;
    }

    public boolean isCheckMinepiaMinistry() {
        return checkMinepiaMinistry;
    }

    /**
     * Gets the checks if is payment.
     *
     * @return the checks if is payment
     */
    public Boolean getIsPayment() {
        return isPayment;
    }

    /**
     * Sets the checks if is payment.
     *
     * @param isPayment the new checks if is payment
     */
    public void setIsPayment(final Boolean isPayment) {
        this.isPayment = isPayment;
    }

    /**
     * Gets the payment data.
     *
     * @return the payment data
     */
    public PaymentData getPaymentData() {
        return paymentData;
    }

    /**
     * Sets the payment data.
     *
     * @param paymentData the new payment data
     */
    public void setPaymentData(final PaymentData paymentData) {
        this.paymentData = paymentData;
    }

    public InvoiceLine getSelectedInvoiceLine() {
        return selectedInvoiceLine;
    }

    public void setSelectedInvoiceLine(InvoiceLine selectedInvoiceLine) {
        this.selectedInvoiceLine = selectedInvoiceLine;
    }

    /**
     * Gets the invoice total amount.
     *
     * @return the invoice total amount
     */
    public Long getInvoiceTotalAmount() {
        return invoiceTotalAmount;
    }

    /**
     * Sets the invoice total amount.
     *
     * @param invoiceTotalAmount the new invoice total amount
     */
    public void setInvoiceTotalAmount(final Long invoiceTotalAmount) {
        this.invoiceTotalAmount = invoiceTotalAmount;
    }

    /**
     * Gets the invoice total ttc amount.
     *
     * @return the invoice total ttc amount
     */
    public Long getInvoiceTotalTtcAmount() {
        return invoiceTotalTtcAmount;
    }

    /**
     * Sets the invoice total ttc amount.
     *
     * @param invoiceTotalTtcAmount the new invoice total ttc amount
     */
    public void setInvoiceTotalTtcAmount(final Long invoiceTotalTtcAmount) {
        this.invoiceTotalTtcAmount = invoiceTotalTtcAmount;
    }

    /**
     * Gets the invoice other amount.
     *
     * @return the invoice other amount
     */
    public Long getInvoiceOtherAmount() {
        return invoiceOtherAmount;
    }

    /**
     * Sets the invoice other amount.
     *
     * @param invoiceOtherAmount the new invoice other amount
     */
    public void setInvoiceOtherAmount(final Long invoiceOtherAmount) {
        this.invoiceOtherAmount = invoiceOtherAmount;
    }

    public List<CustumMap> getPivTreatmentTypes() {
        List<CustumMap> treatmentTypes = new ArrayList<>();
        for (PVITreatmentType treatmentType : PVITreatmentType.values()) {
            treatmentTypes.add(new CustumMap(treatmentType.name(), treatmentType.getLabel()));
        }
        return treatmentTypes;
    }

    public List<CustumMap> getPivLastTreatmentDateStates() {
        List<CustumMap> lastTreatmentDateStates = new ArrayList<>();
        for (PVILastTreatmentDateState lastTreatmentDateState : PVILastTreatmentDateState.values()) {
            lastTreatmentDateStates.add(new CustumMap(lastTreatmentDateState.name(), lastTreatmentDateState.getLabel()));
        }
        return lastTreatmentDateStates;
    }

    public List<CustumMap> getPivStorageEnvs() {
        List<CustumMap> storageEnvs = new ArrayList<>();
        for (PVIStorageEnv storageEnv : PVIStorageEnv.values()) {
            storageEnvs.add(new CustumMap(storageEnv.name(), storageEnv.getLabel()));
        }
        return storageEnvs;
    }

    public List<CustumMap> getPivTransportEnvs() {
        List<CustumMap> transportEnvs = new ArrayList<>();
        for (PVITransportEnv transportEnv : PVITransportEnv.values()) {
            transportEnvs.add(new CustumMap(transportEnv.name(), transportEnv.getLabel()));
        }
        return transportEnvs;
    }

    public List<CustumMap> getPivWeatherConditions() {
        List<CustumMap> weatherConditions = new ArrayList<>();
        for (PVIWeatherConditions weatherCondition : PVIWeatherConditions.values()) {
            weatherConditions.add(new CustumMap(weatherCondition.name(), weatherCondition.getLabel()));
        }
        return weatherConditions;
    }

    public List<CustumMap> getPivProtectionMeasures() {
        List<CustumMap> protectionMeasures = new ArrayList<>();
        for (PVIProtectionMeasures protectionMeasure : PVIProtectionMeasures.values()) {
            protectionMeasures.add(new CustumMap(protectionMeasure.name(), protectionMeasure.getLabel()));
        }
        return protectionMeasures;
    }

    public List<CustumMap> getTrProductsUsed() {
        List<CustumMap> productsUsed = new ArrayList<>();
        for (TRProductUsed productUsed : TRProductUsed.values()) {
            productsUsed.add(new CustumMap(productUsed.name(), productUsed.getLabel()));
        }
        return productsUsed;
    }

    public List<CustumMap> getTrTreatmentEnvironments() {
        List<CustumMap> productsUsed = new ArrayList<>();
        for (TRTreatmentEnvironment te : TRTreatmentEnvironment.values()) {
            productsUsed.add(new CustumMap(te.name(), te.getLabel()));
        }
        return productsUsed;
    }

    public List<CustumMap> getTrStoragePlaces() {
        List<CustumMap> productsUsed = new ArrayList<>();
        for (TRStoragePlace storagePlace : TRStoragePlace.values()) {
            productsUsed.add(new CustumMap(storagePlace.name(), storagePlace.getLabel()));
        }
        return productsUsed;
    }

    public List<CustumMap> getTrConditionings() {
        List<CustumMap> conditionings = new ArrayList<>();
        for (TRConditioning conditioning : TRConditioning.values()) {
            conditionings.add(new CustumMap(conditioning.name(), conditioning.getLabel()));
        }
        return conditionings;
    }

    public List<CustumMap> getTrProtectionEquipments() {
        List<CustumMap> protectionEquipments = new ArrayList<>();
        for (TRProtectionEquipement pe : TRProtectionEquipement.values()) {
            protectionEquipments.add(new CustumMap(pe.name(), pe.getLabel()));
        }
        return protectionEquipments;
    }

    public List<CustumMap> getTrWeatherConditions() {
        List<CustumMap> weatherConditions = new ArrayList<>();
        for (TRWeatherCondition wc : TRWeatherCondition.values()) {
            weatherConditions.add(new CustumMap(wc.name(), wc.getLabel()));
        }
        return weatherConditions;
    }

    public List<CustumMap> getNiTakenMeasures() {
        List<CustumMap> takenMeasures = new ArrayList<>();
        for (NITakenMeasure takenMeasure : NITakenMeasure.values()) {
            takenMeasures.add(new CustumMap(takenMeasure.name(), takenMeasure.getLabel()));
        }
        return takenMeasures;
    }

    public InterceptionNotification getInterceptionNotification() {
        return interceptionNotification;
    }

    public void setInterceptionNotification(InterceptionNotification interceptionNotification) {
        this.interceptionNotification = interceptionNotification;
    }

    public boolean seizurePrintable() {
        if (selectedItemFlowDto == null || selectedItemFlowDto.getItemFlow() == null) {
            return false;
        }
        return (selectedItemFlowDto.getItemFlow().getFlow().getCode().equals(FlowCode.FL_CT_19.name())
                || selectedItemFlowDto.getItemFlow().getFlow().getCode().equals(FlowCode.FL_CT_20.name())
                || selectedItemFlowDto.getItemFlow().getFlow().getCode().equals(FlowCode.FL_CT_21.name()));
    }

    public boolean resendMessageAllowed() {
        return selectedItemFlowDto != null
                && selectedItemFlowDto.getItemFlow() != null
                && selectedItemFlowDto.getItemFlow().getFlow() != null
                && selectedItemFlowDto.getItemFlow().getFlow().getOutgoing() != null
                && selectedItemFlowDto.getItemFlow().getFlow().getOutgoing() > 0;
    }

    public void reSendDecision() {
        try {
            if (fileProducer.resendDecision(selectedItemFlowDto.getItemFlow())) {
                JsfUtil.addSuccessMessageAfterRedirect(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                        getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.RESEND_SUCCESS));
            } else {
                DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
                transactionDefinition.setPropagationBehavior(GLOBAL_PROPAGATION_TRANSACTION_BEHAVIOUR);
                TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
                try {
                    Flow currentSelectedFlow = selectedItemFlowDto.getItemFlow().getFlow();
                    final Flow flowToSend = selectedItemFlowDto.getItemFlow().getFlow();
                    final File selectedFile = selectedItemFlowDto.getItemFlow().getFileItem().getFile();
                    //generate report
                    Map<String, byte[]> attachedByteFiles = null;
                    final List<FileItem> fileItemList = selectedFile.getFileItemsList();
                    final List<ItemFlow> itemFlowList = itemFlowService.findLastItemFlowsByFileItemListAndFlow(fileItemList, FlowCode.valueOf(currentSelectedFlow.getCode()));
                    String service = StringUtils.EMPTY;
                    String documentType = StringUtils.EMPTY;

                    if (Arrays.asList(FlowCode.FL_CT_89.name(), FlowCode.FL_CT_08.name(), FlowCode.FL_CT_114.name(), FlowCode.FL_CT_117.name(), FlowCode.FL_CT_140.name(), FlowCode.FL_CT_CVS_03.name(), FlowCode.FL_CT_CVS_07.name(), FlowCode.FL_CT_121.name(), FlowCode.FL_CT_133.name())
                            .contains(flowToSend.getCode())) {

                        attachedByteFiles = new HashMap<>();

                        final List<FileTypeFlowReport> fileTypeFlowReportList = fileTypeFlowReportService.findReportClassNameByFlowAndFileType(flowToSend, selectedFile.getFileType());
                        for (final FileTypeFlowReport fileTypeFlowReport : fileTypeFlowReportList) {

                            final byte[] report = getReportBytes(selectedFile, fileTypeFlowReport, false);
                            if (report == null) {
                                showErrorFacesMessage(ControllerConstants.Bundle.Messages.CANNOT_GENERATE_REPORT, null);
                                return;
                            }

                            attachedByteFiles.put(fileTypeFlowReport.getReportName(), report);
                        }
                    }

                    // convert file to document
                    final Serializable documentSerializable = xmlConverterService.convertFileToDocument(selectedFile, fileItemList, itemFlowList, flowToSend);

                    // prepare document to send
                    byte[] xmlBytes;
                    try (final ByteArrayOutputStream output = SendDocumentUtils.prepareCctDocument(documentSerializable, service, documentType)) {
                        xmlBytes = output.toByteArray();
                    }

                    if (CollectionUtils.isNotEmpty(flowToSend.getCopyRecipientsList())) {
                        final List<CopyRecipient> copyRecipients = flowToSend.getCopyRecipientsList();
                        for (final CopyRecipient copyRecipient : copyRecipients) {
                            logger.info("SEND COPY RECIPIENT TO {}", copyRecipient.getToAuthority().getRole());
                            send(transactionStatus, xmlBytes, attachedByteFiles, service, documentType, copyRecipient.getToAuthority().getRole(), itemFlowList);
                        }
                    } else {
                        send(transactionStatus, xmlBytes, attachedByteFiles, service, documentType, ebxmlPropertiesService.getToPartyId(), itemFlowList);
                    }

                    TransactionStatus tsCommit = transactionStatus;
                    transactionStatus = null;
                    transactionManager.commit(tsCommit);
                    if (logger.isDebugEnabled()) {
                        logger.info("####RESEND DECISION Transaction commited####");
                    }

                    JsfUtil.addSuccessMessageAfterRedirect(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                            getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.RESEND_SUCCESS));
                } catch (Exception ex) {
                    throw ex;
                } finally {
                    if (transactionStatus != null) {
                        transactionManager.rollback(transactionStatus);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("cannot resend the decision : " + currentFile, ex);
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.RESEND_ERROR, null);
        }
    }

    public boolean isPhytoReadyForSignature(Flow flow) {
        boolean ok = flow != null && checkMinaderMinistry && FileTypeCode.CCT_CT_E.equals(currentFile.getFileType().getCode()) && Arrays.asList(FlowCode.FL_CT_07.name(), FlowCode.FL_CT_112.name(), FlowCode.FL_CT_117.name(), FlowCode.FL_CT_151.name()).contains(flow.getCode());
        return ok;
    }

    public boolean isCCSMinsanteReadyForSignature(Flow flow) {
        boolean ok = flow != null && FileTypeCode.CCS_MINSANTE.equals(currentFile.getFileType().getCode()) && Arrays.asList(FlowCode.FL_CT_07.name(), FlowCode.FL_CT_CCS_03.name()).contains(flow.getCode());
        return ok;
    }

    public boolean isPhytoReadyForSignatureEnd(Flow flow) {
        phytoEnd = flow != null && checkMinaderMinistry && FileTypeCode.CCT_CT_E.equals(currentFile.getFileType().getCode()) && FlowCode.FL_CT_08.name().equals(flow.getCode());
        return phytoEnd;
    }

    public boolean isFstpReadyForSignature(Flow flow) {
        boolean ok = flow != null && checkMinaderMinistry && FileTypeCode.CCT_CT_E_FSTP.equals(currentFile.getFileType().getCode()) && (FlowCode.FL_CT_07.name().equals(flow.getCode()) || FlowCode.FL_CT_112.name().equals(flow.getCode()) || FlowCode.FL_CT_117.name().equals(flow.getCode()));
        return ok;
    }

    public boolean isPviReadyForSignature(Flow flow) {
        boolean ok = flow != null && checkMinaderMinistry && FileTypeCode.CCT_CT_E_PVI.equals(currentFile.getFileType().getCode()) && (FlowCode.FL_CT_07.name().equals(flow.getCode()) || FlowCode.FL_CT_112.name().equals(flow.getCode()) || FlowCode.FL_CT_117.name().equals(flow.getCode()));
        return ok;
    }

    public boolean isAppointmentOkForPve(Flow flow) {
        boolean ok = flow != null && checkMinaderMinistry && FileTypeCode.CCT_CT_E_PVE.equals(currentFile.getFileType().getCode()) && (FlowCode.FL_CT_104.name().equals(flow.getCode()) || FlowCode.FL_CT_118.name().equals(flow.getCode()));
        return ok;
    }

    public boolean isPveReadyForSignature(Flow flow) {
        boolean ok = flow != null && checkMinaderMinistry && FileTypeCode.CCT_CT_E_PVE.equals(currentFile.getFileType().getCode()) && Arrays.asList(FlowCode.FL_CT_07.name(), FlowCode.FL_CT_138.name(), FlowCode.FL_CT_112.name()).contains(flow.getCode());
        return ok;
    }

    public boolean isAtReadyForSignature(Flow flow) {
        boolean ok = flow != null && checkMinaderMinistry && FileTypeCode.CCT_CT_E_ATP.equals(currentFile.getFileType().getCode()) && (FlowCode.FL_CT_07.name().equals(flow.getCode()) || FlowCode.FL_CT_112.name().equals(flow.getCode()) || FlowCode.FL_CT_117.name().equals(flow.getCode()));
        return ok;
    }

    public boolean isRDVConfirm(Flow flow) {
        boolean ok = flow != null && checkMinaderMinistry && FileTypeCode.CCT_CT_E_FSTP.equals(currentFile.getFileType().getCode()) && (FlowCode.FL_CT_07.name().equals(flow.getCode()) || FlowCode.FL_CT_112.name().equals(flow.getCode()));
        return ok;
    }

    public boolean isPhytoTransitFile() {
        final FileFieldValue fileFieldValue = fileFieldValueService.findValueByFileFieldAndFile("TYPE_DOSSIER_EGUCE", currentFile);
        if (fileFieldValue == null) {
            return false;
        }
        final String superFileType = fileFieldValue.getValue();
        boolean ok = checkMinaderMinistry && FileTypeCode.CCT_CT_E.equals(currentFile.getFileType().getCode()) && "2".equals(superFileType);
        return ok;
    }

    public boolean isPrintRelatedDecisions() {
        return getShowProductDetailsForm() && CollectionUtils.isNotEmpty(decisionHistories);
    }

    public Map<FileType, List<DecisionHistory>> getRelatedDecisionHistories() {

        Map<FileType, List<DecisionHistory>> map = new HashMap<>();

        if (CollectionUtils.isNotEmpty(decisionHistories)) {
            for (DecisionHistory decisionHistory : decisionHistories) {

                if (map.get(decisionHistory.getFileType()) == null) {
                    map.put(decisionHistory.getFileType(), new ArrayList<DecisionHistory>());
                }

                map.get(decisionHistory.getFileType()).add(decisionHistory);
            }
        }

        return map;
    }

    private void prepareGoodsForModif() {

        fileItemDtos = new ArrayList<>();

        List<FileItem> fileItems = currentFile.getFileItemsList();
        for (FileItem fileItem : fileItems) {

            FileItemDto fileItemDto = new FileItemDto();

            fileItemDto.setFileItem(fileItem);

            FileItemFieldValue tradeName = fileItemFieldValueService.findValueByFileItemFieldAndFileItem(FileItemDto.NOM_COMMERCIAL, fileItem);
            if (tradeName == null) {
                tradeName = new FileItemFieldValue();
            }
            fileItemDto.setTradeName(tradeName);

            FileItemFieldValue botanicalName = fileItemFieldValueService.findValueByFileItemFieldAndFileItem(FileItemDto.NOM_BOTANIQUE, fileItem);
            if (botanicalName == null) {
                botanicalName = new FileItemFieldValue();
            }
            fileItemDto.setBotanicalName(botanicalName);

            fileItemDtos.add(fileItemDto);
        }
    }

    private void saveModifiedGoods() {

        List<FileItemFieldValue> fifvsToUpdate = new ArrayList<>();
        List<FileItemFieldValue> fifvsToSave = new ArrayList<>();

        FileItemField tradeNamefif = fileItemFieldValueService.findByFileTypeAndCode(currentFile.getFileType().getCode(), FileItemDto.NOM_COMMERCIAL);
        FileItemField botanicalNamefif = fileItemFieldValueService.findByFileTypeAndCode(currentFile.getFileType().getCode(), FileItemDto.NOM_BOTANIQUE);

        for (FileItemDto fileItemDto : fileItemDtos) {

            FileItem fileItem = fileItemService.find(fileItemDto.getFileItem().getId());

            FileItemFieldValue tradeName = fileItemDto.getTradeName();
            if (StringUtils.isNotBlank(tradeName.getValue()) && tradeName.getFileItemField() != null) {
                fifvsToUpdate.add(tradeName);
            } else if (StringUtils.isNotBlank(tradeName.getValue()) && tradeName.getFileItemField() == null) {
                tradeName.setFileItemField(tradeNamefif);
                tradeName.setFileItem(fileItem);
                fifvsToSave.add(tradeName);
            }

            FileItemFieldValue botanicalName = fileItemDto.getBotanicalName();
            if (StringUtils.isNotBlank(botanicalName.getValue()) && botanicalName.getFileItemField() != null) {
                fifvsToUpdate.add(botanicalName);
            } else if (StringUtils.isNotBlank(botanicalName.getValue()) && botanicalName.getFileItemField() == null) {
                botanicalName.setFileItemField(botanicalNamefif);
                botanicalName.setFileItem(fileItem);
                fifvsToSave.add(botanicalName);
            }
        }

        if (!fifvsToUpdate.isEmpty()) {
            fileItemFieldValueService.saveOrUpdateList(fifvsToUpdate);
        }
        if (!fifvsToSave.isEmpty()) {
            fileItemFieldValueService.saveList(fifvsToSave);
        }
    }

    public List<FileItemDto> getFileItemDtos() {
        return fileItemDtos;
    }

    private byte[] getReportBytes(File file, FileTypeFlowReport fileTypeFlowReport, boolean draft) throws Exception {
        String nomClasse = fileTypeFlowReport.getReportClassName();
        Class classe = Class.forName(nomClasse);
        Map<String, Object> forAnnexes = null;
        byte[] report = null;
        AbstractReportInvoker reportInvoker = null;
        List<AbstractReportInvoker> reportInvokersForFstpAndAtp = null;
        FileItem ffi = file.getFileItemsList().get(0);
        if (checkMinaderMinistry) {
            String reportNumber = null;
            if (draft) {
                final ReportOrganism reportOrganism = reportOrganismService.findReportByFileTypeFlowReport(fileTypeFlowReport);
                reportNumber = file.getNumeroDemande()
                        + "/" + java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
                        + ((reportOrganism != null && reportOrganism.getValue() != null) ? reportOrganism.getValue() : StringUtils.EMPTY);

            }
            Flow flow = fileTypeFlowReport.getFlow();
            if (BooleanUtils.toBoolean(flow.getToStep().getIsFinal())) {
                switch (file.getFileType().getCode()) {
                    case CCT_CT_E: {
                        ItemFlow itemFlow = itemFlowService.findItemFlowByFileItemAndFlow2(ffi, FlowCode.FL_CT_151);
                        if (itemFlow == null) {
                            itemFlow = itemFlowService.findItemFlowByFileItemAndFlow2(ffi, FlowCode.FL_CT_07);
                        }
                        if (itemFlow == null) {
                            itemFlow = itemFlowService.findItemFlowByFileItemAndFlow2(ffi, FlowCode.FL_CT_112);
                        }
                        if (itemFlow == null) {
                            itemFlow = itemFlowService.findItemFlowByFileItemAndFlow2(ffi, FlowCode.FL_CT_117);
                        }

                        final TreatmentInfos ti = treatmentInfosService.findTreatmentInfosByItemFlow(itemFlow);
                        if (ti == null || ti.getId() == null) {
                            final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("draftNotAllowedForReject");
                            JsfUtil.addErrorMessage(msg);
                            return null;
                        }

                        ItemFlow itemFlow2 = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_08);
                        if (itemFlow2 == null) {
                            itemFlow2 = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_114);
                        }
                        CCTCPParamValue paramValue = cCTCPParamValueService.findCCTCPParamValueByItemFlow(itemFlow2);

                        if (paramValue == null) {
                            paramValue = new CCTCPParamValue();
                            ReportGeneratorUtils.fillCCTCPParamValue(paramValue, getLoggedUser(), file);
                        }

                        if (ti.getDelivrableType() == null || "CCT_CT_E".equals(ti.getDelivrableType())) {
                            Map<String, Integer> count = ReportGeneratorUtils.countFileContainerAndPackage(file);
                            if (count.get(ReportGeneratorUtils.COUNT_GOODS) > paramValue.getMaxGoodsLineNumber()
                                    || count.get(ReportGeneratorUtils.COUNT_CONTAINERS) > paramValue.getMaxContainerNumber()
                                    || count.get(ReportGeneratorUtils.COUNT_PACKAGES) > paramValue.getMaxPackageNumber()) {
                                forAnnexes = new HashMap();
                                forAnnexes.put("ti", ti);
                                forAnnexes.put("paramValue", paramValue);
                                if (!CctExportProductType.AUTRES.equals(productType)) {
                                    forAnnexes.put("fileNameAnnex", ReportGeneratorUtils.CP_DEF_ANNEX);
                                } else {
                                    forAnnexes.put("fileNameAnnex", ReportGeneratorUtils.CP_ANNEX_AUTRES);
                                }
                            }

                            reportInvoker = new CtCctCpEExporter(file, ti, paramValue, "CERTIFICAT_PHYTOSANITAIRE");
                        } else if ("CQ_CT".equals(ti.getDelivrableType())) {
                            reportInvoker = new CtCctCqeExporter(file, ti);
                        }
                        break;
                    }
                    case CCT_CT_E_PVI: {
                        ItemFlow itemFlow;
                        if (currentFile.getParent() == null) {
                            itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_07);
                        } else {
                            itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_112);
                        }
                        final InspectionReport ir = inspectionReportService.findByItemFlow(itemFlow);
                        if (draft) {
                            reportInvoker = new CtPviExporter(file, ir, reportNumber);
                        } else {
                            reportInvoker = new CtPviExporter(file, ir);
                        }
                        break;
                    }
                    case CCT_CT_E_ATP: {

                        ItemFlow itemFlow;
                        if (currentFile.getParent() == null) {
                            itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_07);
                        } else {
                            itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_112);
                        }
                        TreatmentResult tr = treatmentResultService.findTreatmentResultByItemFlow(itemFlow);
                        if (draft) {
                            reportInvoker = new CtCctTreatmentExporter(file, "CCT_CT_E_ATP", tr, reportNumber, FileTypeCode.CCT_CT_E_ATP);
                        } else {
                            reportInvoker = new CtCctTreatmentExporter(file, "CCT_CT_E_ATP", tr, FileTypeCode.CCT_CT_E_ATP);
                        }
                        break;
                    }
                    case CCT_CT_E_FSTP: {
                        ItemFlow itemFlow;
                        if (currentFile.getParent() == null) {
                            itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_07);
                        } else {
                            itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_112);
                        }
                        TreatmentResult tr = treatmentResultService.findTreatmentResultByItemFlow(itemFlow);
                        reportInvokersForFstpAndAtp = new ArrayList<>();
                        AbstractReportInvoker ri;
                        if (draft) {
                            ri = new CtCctTreatmentExporter(file, "CCT_CT_E_FSTP", tr, reportNumber, FileTypeCode.CCT_CT_E_FSTP);
                        } else {
                            ri = new CtCctTreatmentExporter(file, "CCT_CT_E_FSTP", tr, FileTypeCode.CCT_CT_E_FSTP);
                        }
                        reportInvokersForFstpAndAtp.add(ri);
                        if (draft) {
                            ri = new CtCctTreatmentExporter(file, "CCT_CT_E_ATP", tr, reportNumber, FileTypeCode.CCT_CT_E_ATP);
                        } else {
                            ri = new CtCctTreatmentExporter(file, "CCT_CT_E_ATP", tr, FileTypeCode.CCT_CT_E_ATP);
                        }
                        reportInvokersForFstpAndAtp.add(ri);
                        break;
                    }
                    default:
                        Constructor constructor = classe.getConstructor(File.class);
                        reportInvoker = (AbstractReportInvoker) constructor.newInstance(file);
                        break;
                }
            } else if (FlowCode.FL_CT_121.name().equals(flow.getCode()) || FlowCode.FL_CT_133.name().equals(flow.getCode())) {
                PaymentData payData = paymentDataService.findPaymentDataByFileItem(ffi);
                Constructor constructor = classe.getConstructor(File.class, PaymentData.class);
                reportInvoker = (AbstractReportInvoker) constructor.newInstance(file, payData);
            }

        } else if (checkMinepiaMinistry) {
            switch (file.getFileType().getCode()) {
                case CCT_CT: {
                    if (approvedDecision == null) {
                        lastDecisions = itemFlowService.findLastSentItemFlowByFileItem(ffi);
                        approvedDecision = approvedDecisionService.findApprovedDecisionByItemFlow(lastDecisions);
                    }
                    reportInvoker = new CtCctCsvExporter(file, loggedUser, approvedDecision);
                    break;
                }
                case CCT_CSV:
                    if (approvedDecision == null) {
                        lastDecisions = itemFlowService.findLastSentItemFlowByFileItem(selectedFileItemCheck.getFileItem());

                        approvedDecision = approvedDecisionService.findApprovedDecisionByItemFlow(lastDecisions);
                    }
                    reportInvoker = new CctCsvExporter(currentFile, loggedUser, approvedDecision);
                    break;
            }
        } else if (FileTypeCode.CCS_MINSANTE.equals(file.getFileType().getCode())) {
            ItemFlow itemFlow;
            if (file.getParent() != null) {
                itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_CCS_03);
            } else {
                itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_07);
            }
            final TreatmentInfosCCSMinsante tr = treatmentInfosCCSMinsanteService.findTreatmentInfosByItemFlow(itemFlow);
            List<AbstractReportInvoker> reportInvokersPrincipalAndAnnex = new ArrayList<>();
            String reportFileName = "CCS_MINSANTE";
            String reportAnnexFileName = "CCS_MINSANTE_ANNEXE";
            if (tr != null && tr.isCcsMinsanteDrugProducts()) {
                reportFileName = "CCS_MINSANTE_MEDICAMENTS";
            }
            //reportInvoker = (AbstractReportInvoker) constructor.newInstance(file, reportFileName, tr);
            //For principal report
            reportInvoker = new CcsMinsanteExporter(file, reportFileName, tr);
            reportInvokersPrincipalAndAnnex.add(reportInvoker);
            //For Annex report
            reportInvoker = new CcsMinsanteExporter(file, reportAnnexFileName, reportAnnexFileName, tr);
            reportInvokersPrincipalAndAnnex.add(reportInvoker);
            List<JasperPrint> inputStreamsForPrincipalAndAnnex = new ArrayList<>();
            for (AbstractReportInvoker reportInvokerPrincipalOrAnnex : reportInvokersPrincipalAndAnnex) {
                reportInvokerPrincipalOrAnnex.setDraft(draft);
                reportInvokerPrincipalOrAnnex.setFileFieldValueService(fileFieldValueService);
                reportInvokerPrincipalOrAnnex.setItemFlowService(itemFlowService);
                reportInvokerPrincipalOrAnnex.setCctDetailController(this);
                reportInvokerPrincipalOrAnnex.setUserStampSignatureService(userStampSignatureService);
                JasperPrint reportJPForPrincipalOrAnnex = JsfUtil.getReportJP(reportInvokerPrincipalOrAnnex);
                inputStreamsForPrincipalAndAnnex.add(reportJPForPrincipalOrAnnex);
            }
            if (!CollectionUtils.isEmpty(inputStreamsForPrincipalAndAnnex)) {
                report = JsfUtil.mergePdf(inputStreamsForPrincipalAndAnnex);
            }
            reportInvoker = null;
        } else {
            Constructor constructor = classe.getConstructor(File.class);
            reportInvoker = (AbstractReportInvoker) constructor.newInstance(file);
        }

        if (FileTypeCode.CCT_CT_E_FSTP.equals(file.getFileType().getCode()) && reportInvokersForFstpAndAtp != null) {
            List<JasperPrint> inputStreamsForFstpAndAtp = new ArrayList<>();
            for (AbstractReportInvoker reportInvokerFstpOrAtp : reportInvokersForFstpAndAtp) {
                reportInvokerFstpOrAtp.setDraft(draft);
                reportInvokerFstpOrAtp.setFileFieldValueService(fileFieldValueService);
                reportInvokerFstpOrAtp.setItemFlowService(itemFlowService);
                reportInvokerFstpOrAtp.setPottingReportService(pottingReportService);
                reportInvokerFstpOrAtp.setCctDetailController(this);
                JasperPrint reportJPForFstpOrAtp = JsfUtil.getReportJP(reportInvokerFstpOrAtp);
                inputStreamsForFstpAndAtp.add(reportJPForFstpOrAtp);
            }
            if (!CollectionUtils.isEmpty(inputStreamsForFstpAndAtp)) {
                report = JsfUtil.mergePdf(inputStreamsForFstpAndAtp);
            }
        } else if (reportInvoker != null) {
            reportInvoker.setDraft(draft);
            reportInvoker.setFileFieldValueService(fileFieldValueService);
            reportInvoker.setItemFlowService(itemFlowService);
            reportInvoker.setPottingReportService(pottingReportService);
            reportInvoker.setCctDetailController(this);
            if (forAnnexes != null) {
                JasperPrint page1 = JsfUtil.getReportJP(reportInvoker);
                TreatmentInfos ti = (TreatmentInfos) forAnnexes.get("ti");
                CCTCPParamValue paramValue = (CCTCPParamValue) forAnnexes.get("paramValue");
                String fileAnnexName = (String) forAnnexes.get("fileNameAnnex");
                CtCctCpEExporter exporter = new CtCctCpEExporter(file, ti, paramValue, fileAnnexName);
                exporter.setDraft(draft);
                exporter.setFileFieldValueService(fileFieldValueService);
                JasperPrint report2 = JsfUtil.getReportJP(exporter);

                List<JasperPrint> inputStreams = new ArrayList<>();
                inputStreams.add(page1);
                inputStreams.add(report2);

                report = JsfUtil.mergePdf(inputStreams);
            } else {
                report = JsfUtil.getReport(reportInvoker);
            }
        }

        return report;
    }

    public boolean isGenerateDraftAllowed() {
        return generateDraftAllowed;
    }

    public StreamedContent downloadAllAttachments() {
        Map<String, byte[]> attachments = CmisClient.extractAttachments(currentFile);
        if (MapUtils.isEmpty(attachments)) {
            JsfUtil.addWarningMessage("Il n'y aucune pièce jointe à télécharger");
            return null;
        }
        byte[] bytes = org.guce.siat.common.utils.io.IOUtils.attachmentsToZip(attachments);
        return new DefaultStreamedContent(new java.io.ByteArrayInputStream(bytes), "application/zip", "ATTACHMENTS-".concat(currentFile.getNumeroDossier()).concat(".zip"), StandardCharsets.UTF_8.displayName());
    }

    public StreamedContent downloadAttachment() {
        if (selectedAttachment == null) {
            JsfUtil.addWarningMessage("Selectionnez la pièce à télécharger");
            return null;
        }
        try {
            final Session sessionCmisClient = CmisSession.getInstance();
            ContentStream contentStream = CmisClient.getDocumentByPath(sessionCmisClient, getSelectedAttachment().getPath()
                    + AlfrescoDirectoriesInitializer.SLASH + getSelectedAttachment().getDocumentName());
            if (contentStream == null) {
                contentStream = CmisClient.getDocumentByPath(sessionCmisClient, getSelectedAttachment().getPath());
            }
            if (contentStream == null) {
                JsfUtil.addErrorMessage("Impossible de trouver la pièce jointe");
                return null;
            }
            final BufferedInputStream in = new BufferedInputStream(contentStream.getStream());
            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            int val;
            try {
                while ((val = in.read()) != -1) {
                    out.write(val);
                }
            } catch (final IOException e) {
                logger.error(e.getMessage(), e);
            }

            final byte[] bytes = out.toByteArray();

            return new DefaultStreamedContent(new java.io.ByteArrayInputStream(bytes), !org.apache.commons.lang.StringUtils.isEmpty(contentStream.getMimeType()) ? contentStream.getMimeType() : "application/msword", selectedAttachment.getDocumentName());
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Impossible de télécharger la pièce jointe");
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public ApprovedDecision getApprovedDecision() {
        return approvedDecision;
    }

    public void setApprovedDecision(ApprovedDecision approvedDecision) {
        this.approvedDecision = approvedDecision;
    }

    public CCTCPParamValue getcCTCPParamValue() {
        return cCTCPParamValue;
    }

    public void setcCTCPParamValue(CCTCPParamValue cCTCPParamValue) {
        this.cCTCPParamValue = cCTCPParamValue;
    }

    private void fillApprovedDecision(ApprovedDecision approvedDecision) {
        if (CollectionUtils.isNotEmpty(currentFile.getFileFieldValueList())) {
            for (final FileFieldValue fileFieldValue : currentFile.getFileFieldValueList()) {
                switch (fileFieldValue.getFileField().getCode()) {
                    case "DATE_DEPART": {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            approvedDecision.setDepartureDate(sdf.parse(fileFieldValue.getValue()));
                        } catch (ParseException e) {
                            java.util.logging.Logger.getLogger(FileItemCctDetailController.class.getName()).log(Level.SEVERE, null, e);
                        }
                        break;
                    }
                    case "TEMPERATURE_PRODUIT": {
                        approvedDecision.setProductTemperature(fileFieldValue.getValue());
                        break;
                    }
                    case "NOMBRE_UNITES_EMBALLES": {
                        approvedDecision.setNumberOfUnitPackaged(fileFieldValue.getValue());
                        break;
                    }
                    case "NATURE_EMBALLAGE": {
                        approvedDecision.setTypeOfPagkaging(fileFieldValue.getValue());
                        break;
                    }
                    case "MARCHANDISE_POUR": {
                        approvedDecision.setGoodsCertifiedFor(fileFieldValue.getValue());
                        break;
                    }
                    case "MARCHANDISE_ESPECE": {
                        approvedDecision.setGoodsSpecies(fileFieldValue.getValue());
                        break;
                    }
                    case "MARCHANDISE_NATURE": {
                        approvedDecision.setGoodsNature(fileFieldValue.getValue());
                        break;
                    }
                    case "MARCHANDISE_TRAITEMENT": {
                        approvedDecision.setGoodsTreatment(fileFieldValue.getValue());
                        break;
                    }
                    case "MARCHANDISE_NB_COLIS": {
                        approvedDecision.setGoodsPackageNumber(fileFieldValue.getValue());
                        break;
                    }
                    case "MARCHANDISE_NB_APPROUVES": {
                        approvedDecision.setGoodsAgreementReference(fileFieldValue.getValue());
                        break;
                    }
                    case "MARCHANDISE_POIDS_NET": {
                        approvedDecision.setGoodsNetWeight(fileFieldValue.getValue());
                        break;
                    }
                    case "ID_CONTENEURS_SCELLES": {
                        approvedDecision.setContainerSeals(fileFieldValue.getValue());
                        break;
                    }
                    case "CCT_CONTENEURS_CONTENEUR": {
                        String containers = fileFieldValue.getValue();
                        if (StringUtils.isNotBlank(containers)) {
                            final String[] tab1 = containers.split(";");
                            final int size = tab1.length;
                            final StringBuilder builder = new StringBuilder();
                            for (int i = 1; i < size; i++) {
                                if (StringUtils.isBlank(tab1[i])) {
                                    continue;
                                }
                                final String[] tab2 = tab1[i].split(",");
                                builder.append(tab2[0]).append("/").append(tab2[3]).append("; ");
                            }
                            approvedDecision.setContainerSeals(builder.substring(0, builder.lastIndexOf(" ")));
                        } else {
                            approvedDecision.setContainerSeals("-");
                        }
                        break;
                    }
                }
            }
        }
    }

    public boolean isMaskOfficialPosition() {
        return maskOfficialPosition;
    }

    public void setMaskOfficialPosition(boolean maskOfficialPosition) {
        this.maskOfficialPosition = maskOfficialPosition;
    }

    public TreatmentInfos populateTreatmentInfos(File tmpFile) {
        TreatmentInfos treatInfo = new TreatmentInfos();
        treatInfo.setDelivrableType(FileTypeCode.CCT_CT_E.name());
        try {
            ItemFlow itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(tmpFile.getFileItemsList().get(0), FlowCode.FL_CT_07);
            if (itemFlow == null) {
                itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(tmpFile.getFileItemsList().get(0), FlowCode.FL_CT_112);
            }
            if (itemFlow == null) {
                itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(tmpFile.getFileItemsList().get(0), FlowCode.FL_CT_117);
            }
            TreatmentInfos tmp = treatmentInfosService.findTreatmentInfosByItemFlow(itemFlow);
            if (tmp != null) {
                treatInfo = (TreatmentInfos) BeanUtils.cloneBean(tmp);
            }

            // Load TreatmentInfos from FileField
            final List<FileFieldValue> fileFieldValueList = tmpFile.getFileFieldValueList();
            if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
                for (final FileFieldValue fileFieldValue1 : fileFieldValueList) {
                    switch (fileFieldValue1.getFileField().getCode()) {
                        case "AUTRE_INFORMATION_DECLARATION_ADDITIONNELLE":
                            treatInfo.setAdditionnalDeclaration(fileFieldValue1.getValue());
                            break;
                        case "AUTRE_INFORMATION_DATE_TRAITEMENT":
                            treatInfo.setTreatmentDate(new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue1.getValue()));
                            break;
                        case "AUTRE_INFORMATION_TYPE_TRAITEMENT":
                            treatInfo.setTypeOfTreatment(fileFieldValue1.getValue());
                            break;
                        case "AUTRE_INFORMATION_DUREE_TRAITEMENT":
                            treatInfo.setDuration(fileFieldValue1.getValue());
                            break;
                        case "AUTRE_INFORMATION_TEMPERATURE":
                            treatInfo.setTemperature(fileFieldValue1.getValue());
                            break;
                        case "AUTRE_INFORMATION_TRAITEMENT_EFFECTUE":
                            treatInfo.setTreatmentsCarriedOut(fileFieldValue1.getValue());
                            break;
                        case "AUTRE_INFORMATION_PRODUIT_CHIMIQUE":
                            treatInfo.setChemicalProduct(fileFieldValue1.getValue());
                            break;
                        case "AUTRE_INFORMATION_CONCENTRATION":
                            treatInfo.setConcentration(fileFieldValue1.getValue());
                            break;
//                        case "AUTRE_INFORMATION_RENSEIGNEMENT_COMPLEMENTAIRE":
//                            treatInfo.setAdditionalInfos(fileFieldValue1.getValue());
//                            break;
//                        case "AUTRE_NUMERO_CERTIFICAT_ORIGIN":
//                            treatInfo.setCertificatCountryOrigin(fileFieldValue1.getValue());
//                            break;
                        case "AUTRE_INFORMATION_FUMIGATION":
                            treatInfo.setFumigation(Boolean.parseBoolean(fileFieldValue1.getValue()));
                            break;
                        case "AUTRE_INFORMATION_DESINFECTION":
                            treatInfo.setDisinfection(Boolean.parseBoolean(fileFieldValue1.getValue()));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return treatInfo;
    }

    public TreatmentInfos createTreatmentInfos() {
        TreatmentInfos res;
        res = populateTreatmentInfos(currentFile);
        if (res.getId() != null) {
            res.setId(null);
            res.setItemFlow(null);
        }
        return res;
    }

    @Override
    public boolean canDecide() {

        if (BooleanUtils.toBoolean(cotationButtonAllowed)) {
            return false;
        }

        UserAuthorityFileType uaft = userAuthorityFileTypeService.findByCurrentStepFileAndLoggedUser(currentStep, currentFile, getLoggedUser());
        userDecisionAllowed = uaft != null;
        if (userDecisionAllowed && BooleanUtils.toBoolean(currentFileItem.getStep().getTreatmentStep()) && checkMinaderMinistry) {
            userDecisionAllowed = Objects.equals(getLoggedUser(), currentFile.getAssignedUser());
        }
        return userDecisionAllowed;
    }

    @Override
    public boolean canConfirm() {
        ItemFlow itemFlow = currentFileItem.getItemFlowsList().get(0);
        userConfirmationAllowed = getLoggedUser().equals(itemFlow.getSender());
        return userConfirmationAllowed;
    }

    @Override
    public boolean canRollback() {
        return canConfirm();
    }

    public List<Container> getContainers() {
        return currentFile.getContainers();
    }

    public boolean isPhyto() {
        return Utils.isPhyto(currentFile);
    }

    private void addFileFieldValue(List<FileFieldValue> fileFieldValues, String code, String value) {

        /**
         * pour ajouter votre procédure, vous devez vous assurer que le couple
         * (code, fileType) est unique
         *
         * cette contrainte a été ajoutée pour éviter qu'il n'y ait violation de
         * la contrainte d'unicité du couple présenté plus haut
         */
        FileTypeCode fileTypeCode = currentFile.getFileType().getCode();
        if (!FileTypeCode.CCT_CT_E_PVE.equals(fileTypeCode)) {
            return;
        }

        if (code == null) {
            return;
        }

        FileFieldValue ffv = new FileFieldValue();

        ffv.setFile(currentFile);
        ffv.setFileField(fileFieldService.findFileFieldByCodeAndFileType(code, fileTypeCode));
        ffv.setValue(value);

        fileFieldValues.add(ffv);
    }

    public PottingReport getPottingReport() {
        return pottingReport;
    }

    public void setPottingReport(PottingReport pottingReport) {
        this.pottingReport = pottingReport;
    }

    public CctExportProductType getProductType() {
        return productType;
    }

    private boolean isPhytoBilling(Flow flow) {
        return isPhyto() && flow != null && Arrays.asList(FlowCode.FL_CT_120.name(), FlowCode.FL_CT_124.name(), FlowCode.FL_CT_132.name(), FlowCode.FL_CT_143.name()).contains(flow.getCode());
    }

    public FileType getFileType(FileTypeCode fileTypeCode) {
        List<FileType> fileTypes = fileTypeService.findFileTypesByCodes(fileTypeCode);
        return CollectionUtils.isNotEmpty(fileTypes) ? fileTypes.get(0) : null;
    }

    public List<FileTypeDto> getRelatedFileTypesInfos() {

        List<FileTypeCode> codes = new ArrayList<>(Arrays.asList(FileTypeCode.CCT_CT_E, FileTypeCode.CCT_CT_E_ATP, FileTypeCode.CCT_CT_E_FSTP, FileTypeCode.CCT_CT_E_PVE, FileTypeCode.CCT_CT_E_PVI));
        if (codes.contains(currentFile.getFileType().getCode())) {
            codes.remove(currentFile.getFileType().getCode());

            List<FileType> fileTypes = fileTypeService.findFileTypesByCodes(codes.toArray(new FileTypeCode[0]));
            relatedFileTypesInfos = RelatedFilesUtils.getRelatedFileTypesInfos(this, fileTypes);
        }
        return relatedFileTypesInfos;
    }

    public boolean isPhytoAppointment(Flow flow) {
        List<FileTypeCode> codes = new ArrayList<>(Arrays.asList(FileTypeCode.CCT_CT_E_FSTP, FileTypeCode.CCT_CT_E_PVE, FileTypeCode.CCT_CT_E_PVI));
        boolean ok = checkMinaderMinistry && flow != null && codes.contains(currentFile.getFileType().getCode()) && Arrays.asList(FlowCode.FL_CT_104.name(), FlowCode.FL_CT_118.name()).contains(flow.getCode());
        return ok;
    }

    /**
     * Lorsque le signature décide transmettre le dossier à un agent de
     * traitement
     *
     * @param flow
     * @return
     */
    public boolean isCsvBeforeTreatment(Flow flow) {
        return checkMinepiaMinistry && flow != null && Arrays.asList(FlowCode.FL_CT_165.name(), FlowCode.FL_CT_171.name()).contains(flow.getCode());
    }

    public List<Appointment> getRelatedAppointments() {
        return relatedAppointments;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public String getCommonObservation() {
        return commonObservation;
    }

    public void setCommonObservation(String commonObservation) {
        this.commonObservation = commonObservation;
    }

    public boolean isCommonObservationPrintable() {
        return commonObservationPrintable;
    }

    public void setCommonObservationPrintable(boolean commonObservationPrintable) {
        this.commonObservationPrintable = commonObservationPrintable;
    }

    public boolean isCommonObservationRequired() {
        return commonObservationRequired;
    }

    public List<Country> getCountriesList() {
        return countriesList;
    }

    public boolean isCcsMinsantefFileType() {
        return ccsMinsantefFileType;
    }

    public void setCcsMinsantefFileType(boolean ccsMinsantefFileType) {
        this.ccsMinsantefFileType = ccsMinsantefFileType;
    }

    public boolean isCcsMinsanteFoodProducts() {
        ccsMinsanteFoodProducts = treatmentInfosCCSMinsante != null && (treatmentInfosCCSMinsante.isProductFoodIHC() || treatmentInfosCCSMinsante.isHygienSanitationProducts() || treatmentInfosCCSMinsante.isFleaMarket() || treatmentInfosCCSMinsante.isThriftShop() || treatmentInfosCCSMinsante.isVehicle());
        return ccsMinsanteFoodProducts;
    }

    public void setCcsMinsanteFoodProducts(boolean ccsMinsanteFoodProducts) {
        this.ccsMinsanteFoodProducts = ccsMinsanteFoodProducts;
    }

    public boolean isCcsMinsanteDrugProducts() {
        ccsMinsanteDrugProducts = treatmentInfosCCSMinsante != null && (treatmentInfosCCSMinsante.isDrugs() || treatmentInfosCCSMinsante.isMedicalDevices() || treatmentInfosCCSMinsante.isTropicalCorticosteroids() || treatmentInfosCCSMinsante.isLaboratoryProducts() || treatmentInfosCCSMinsante.isPackagingSfProducts());
        return ccsMinsanteDrugProducts;
    }

    public void setCcsMinsanteDrugProducts(boolean ccsMinsanteDrugProducts) {
        this.ccsMinsanteDrugProducts = ccsMinsanteDrugProducts;
    }

    public UserStampSignatureService getUserStampSignatureService() {
        return userStampSignatureService;
    }

    public void setUserStampSignatureService(UserStampSignatureService userStampSignatureService) {
        this.userStampSignatureService = userStampSignatureService;
    }

}
