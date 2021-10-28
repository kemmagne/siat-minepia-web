package org.guce.siat.web.ct.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.guce.siat.common.data.FieldGroupDto;
import org.guce.siat.common.data.ItemFlowDto;
import org.guce.siat.common.mail.MailConstants;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Attachment;
import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.CopyRecipient;
import org.guce.siat.common.model.DataType;
import org.guce.siat.common.model.FieldGroup;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileAdministration;
import org.guce.siat.common.model.FileField;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemField;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.model.FileMarshall;
import org.guce.siat.common.model.FileTypeFlow;
import org.guce.siat.common.model.FileTypeFlowReport;
import org.guce.siat.common.model.FileTypeStep;
import org.guce.siat.common.model.Flow;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.ItemFlowData;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.Recommandation;
import org.guce.siat.common.model.RecommandationAuthority;
import org.guce.siat.common.model.RecommandationAuthorityId;
import org.guce.siat.common.model.ReportOrganism;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.Step;
import org.guce.siat.common.model.User;
import org.guce.siat.common.model.UserAuthorityFileType;
import org.guce.siat.common.service.AdministrationService;
import org.guce.siat.common.service.ApplicationPropretiesService;
import org.guce.siat.common.service.AttachmentService;
import org.guce.siat.common.service.EbxmlPropertiesService;
import org.guce.siat.common.service.FieldGroupService;
import org.guce.siat.common.service.FileFieldService;
import org.guce.siat.common.service.FileFieldValueService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FileMarshallService;
import org.guce.siat.common.service.FileProducer;
import org.guce.siat.common.service.FileService;
import org.guce.siat.common.service.FileTypeFlowService;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.common.service.FileTypeStepService;
import org.guce.siat.common.service.FlowService;
import org.guce.siat.common.service.ItemFlowService;
import org.guce.siat.common.service.MailService;
import org.guce.siat.common.service.MessageToSendService;
import org.guce.siat.common.service.RecommandationAuthorityService;
import org.guce.siat.common.service.RecommandationService;
import org.guce.siat.common.service.ReportOrganismService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.StepService;
import org.guce.siat.common.service.UserAuthorityFileTypeService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.service.XmlConverterService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.common.utils.RepetableUtil;
import org.guce.siat.common.utils.SiatUtils;
import org.guce.siat.common.utils.Tab;
import org.guce.siat.common.utils.ebms.ESBConstants;
import org.guce.siat.common.utils.enums.AperakType;
import org.guce.siat.common.utils.enums.AuthorityConstants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.common.utils.enums.StepCode;
import org.guce.siat.common.utils.ged.AlfrescoDirectoriesInitializer;
import org.guce.siat.common.utils.ged.CmisClient;
import org.guce.siat.common.utils.ged.CmisSession;
import org.guce.siat.common.utils.ged.CmisUtils;
import org.guce.siat.core.ct.model.AnalyseResultAp;
import org.guce.siat.core.ct.model.EssayTestAP;
import org.guce.siat.core.ct.model.Laboratory;
import org.guce.siat.core.ct.model.PaymentData;
import org.guce.siat.core.ct.model.PaymentItemFlow;
import org.guce.siat.core.ct.model.WoodSpecification;
import org.guce.siat.core.ct.service.AnalyseResultApService;
import org.guce.siat.core.ct.service.CommonService;
import org.guce.siat.core.ct.service.EssayTestApService;
import org.guce.siat.core.ct.service.LaboratoryService;
import org.guce.siat.core.ct.service.PaymentDataService;
import org.guce.siat.core.ct.service.WoodSpecificationService;
import org.guce.siat.core.utils.SendDocumentUtils;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.common.util.ApSpecificDecisionHistory;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.ReportGeneratorUtils;
import org.guce.siat.web.ct.controller.util.enums.DataTypeEnnumeration;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.guce.siat.web.reports.exporter.AbstractReportInvoker;
import org.guce.siat.web.reports.exporter.CpMinepdedExporter;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.message.Message;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * The Class FileItemApDetailController.
 */
@ManagedBean(name = "fileItemApDetailController")
@SessionScoped
public class FileItemApDetailController extends DefaultDetailController implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2087435973539682236L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FileItemApDetailController.class);

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
     * The Constant LOCAL_BUNDLE_NAME.
     */
    protected static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale".intern();

    /**
     * The Constant EMAIL_BODY_NOTIFICATION_FR.
     */
    private static final String EMAIL_BODY_NOTIFICATION_FR = "emailBodyNotification_fr.vm";

    /**
     * The Constant EMAIL_BODY_NOTIFICATION_EN.
     */
    private static final String EMAIL_BODY_NOTIFICATION_EN = "emailBodyNotification_en.vm";

    private static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);

    /**
     * The administration service.
     */
    @ManagedProperty(value = "#{administrationService}")
    private AdministrationService administrationService;

    /**
     * The mail service.
     */
    @ManagedProperty(value = "#{mailService}")
    private MailService mailService;

    /**
     * The user service.
     */
    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    /**
     * The flow service.
     */
    @ManagedProperty(value = "#{flowService}")
    private FlowService flowService;

    /**
     * The file item service.
     */
    @ManagedProperty(value = "#{fileItemService}")
    private FileItemService fileItemService;

    /**
     * The item flow service.
     */
    @ManagedProperty(value = "#{itemFlowService}")
    private ItemFlowService itemFlowService;

    /**
     * The file service.
     */
    @ManagedProperty(value = "#{fileService}")
    private FileService fileService;

    /**
     * The recommandation service.
     */
    @ManagedProperty(value = "#{recommandationService}")
    private RecommandationService recommandationService;

    /**
     * The step service.
     */
    @ManagedProperty(value = "#{stepService}")
    private StepService stepService;

    /**
     * The laboratory service.
     */
    @ManagedProperty(value = "#{laboratoryService}")
    private LaboratoryService laboratoryService;

    /**
     * The analyse result ap service.
     */
    @ManagedProperty(value = "#{analyseResultApService}")
    private AnalyseResultApService analyseResultApService;

    /**
     * The essay test ap service.
     */
    @ManagedProperty(value = "#{essayTestApService}")
    private EssayTestApService essayTestApService;
    /**
     * The file type step service.
     */
    @ManagedProperty(value = "#{fileTypeStepService}")
    private FileTypeStepService fileTypeStepService;

    /**
     * send file service.
     */
    @ManagedProperty(value = "#{fileProducer}")
    private FileProducer fileProducer;

    /**
     * The file type service.
     */
    @ManagedProperty(value = "#{fileTypeService}")
    private FileTypeService fileTypeService;

    /**
     * The user authority file type service.
     */
    @ManagedProperty(value = "#{recommandationAuthorityService}")
    private RecommandationAuthorityService recommandationAuthorityService;

    /**
     * The file field service.
     */
    @ManagedProperty(value = "#{fileFieldService}")
    private FileFieldService fileFieldService;

    /**
     * The file field value service.
     */
    @ManagedProperty(value = "#{fileFieldValueService}")
    private FileFieldValueService fileFieldValueService;

    /**
     * The report organism service.
     */
    @ManagedProperty(value = "#{reportOrganismService}")
    private ReportOrganismService reportOrganismService;

    /**
     * The file type flow service.
     */
    @ManagedProperty(value = "#{fileTypeFlowService}")
    private FileTypeFlowService fileTypeFlowService;

    @ManagedProperty(value = "#{attachmentService}")
    private AttachmentService attachmentService;

    /**
     * The message not already send service.
     */
    @ManagedProperty(value = "#{messageToSendService}")
    private MessageToSendService messageToSendService;

    /**
     * The index page url.
     */
    private String indexPageUrl;

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
     * The current file.
     */
    private File currentFile;

    /**
     * The attachment list.
     */
    private List<Attachment> attachmentList;
    /**
     * The attachment list.
     */
    private List<Attachment> attachmentListFiltred;

    /**
     * The selected attachment.
     */
    private Attachment selectedAttachment;

    /**
     * The recommandation list.
     */
    private List<Recommandation> recommandationList;

    /**
     * The recommandation list filtred.
     */
    private List<Recommandation> recommandationListFiltred;

    /**
     * The selected recommandation.
     */
    private Recommandation selectedRecommandation;

    /**
     * The selected recommandation article.
     */
    private Recommandation selectedRecommandationArticle;

    /**
     * The recommandation article list.
     */
    private List<Recommandation> recommandationArticleList;

    /**
     * The recommandation article list filtred.
     */
    private List<Recommandation> recommandationArticleListFiltred;

    /**
     * The allowed user.
     */
    private Boolean allowedUser;

    /**
     * The product info items filtred.
     */
    private List<FileItem> productInfoItemsFiltred;

    /**
     * The logged user.
     */
    private User loggedUser;

    /**
     * The current organism.
     */
    private Organism currentOrganism;

    /**
     * The current service.
     */
    private Service currentService;

    /**
     * The id attachment.
     */
    private String idAttachment;

    /**
     * The disabled tab synthese.
     */
    private Boolean disabledTabSynthese;

    /**
     * The decision allowed.
     */
    private Boolean decisionAllowed;

    /**
     * The cotation allowed.
     */
    private Boolean cotationAllowed;

    /**
     * The
     */
    private Boolean decisionAllowedAtCotationLevel;

    private Boolean decisionAtCotationLevel;

    /**
     * The dision system allowed.
     */
    private Boolean disionSystemAllowed;

    /**
     * The user list to affected file cotation.
     */
    private List<User> userListToAffectedFileCotation;

    /**
     * The list user authority file types.
     */
    List<UserAuthorityFileType> listUserAuthorityFileTypes;

    /**
     * The selected file item.
     */
    private FileItem selectedFileItem;

    /**
     * The destination flows from current step.
     */
    private List<Flow> destinationFlowsFromCurrentStep;

    /**
     * The destination flows from current coatation step
     */
    private List<Flow> destinationFlowsFromCurrentCotationStep;

    /**
     * The roll back decisions allowed.
     */
    private Boolean rollBackDecisionsAllowed;

    /**
     * The send decision allowed.
     */
    private Boolean sendDecisionAllowed;

    /**
     * The product info items.
     */
    private List<FileItem> productInfoItems;

    /**
     * The tab list.
     */
    private List<Tab> tabList;

    /**
     * The tab index list.
     */
    private String tabIndexList;

    /**
     * The assigned user for cotation.
     */
    private User assignedUserForCotation;

    /**
     * The ap decision step.
     */
    private Step apDecisionStep;

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
     * The authorities list.
     */
    private DualListModel<Authority> authoritiesList;

    /**
     * The authority list.
     */
    private List<Authority> authorityList;

    /**
     * The service service.
     */
    @ManagedProperty(value = "#{serviceService}")
    private ServiceService serviceService;

    /**
     * The xml converter service.
     */
    @ManagedProperty(value = "#{xmlConverterService}")
    private XmlConverterService xmlConverterService;

    /**
     * The user authority file type service.
     */
    @ManagedProperty(value = "#{userAuthorityFileTypeService}")
    private UserAuthorityFileTypeService userAuthorityFileTypeService;

    /**
     * The application propreties service.
     */
    @ManagedProperty(value = "#{applicationPropretiesService}")
    private ApplicationPropretiesService applicationPropretiesService;

    /**
     * The field group service.
     */
    @ManagedProperty(value = "#{fieldGroupService}")
    private FieldGroupService fieldGroupService;

    /**
     * The field group service.
     */
    @ManagedProperty(value = "#{commonService}")
    private CommonService commonService;

    /**
     * The file item has draft.
     */
    private Boolean fileItemHasDraft;

    /**
     * The generate report allowed.
     */
    private Boolean generateReportAllowed;

    /**
     * The ebxml properties service.
     */
    @ManagedProperty(value = "#{ebxmlPropertiesService}")
    private EbxmlPropertiesService ebxmlPropertiesService;

    /**
     * The payment data service.
     */
    @ManagedProperty(value = "#{paymentDataService}")
    private PaymentDataService paymentDataService;

    @ManagedProperty(value = "#{woodSpecificationService}")
    private WoodSpecificationService woodSpecificationServce;

    @ManagedProperty(value = "#{fileMarshallService}")
    private FileMarshallService fileMarshallServce;

    /**
     * The come from retrieve ap.
     */
    private Boolean comeFromRetrieveAp;

    /**
     * The analyse result aps.
     */
    private List<AnalyseResultAp> analyseResultApList;

    /**
     * The test result ap list.
     */
    private List<EssayTestAP> testResultApList;

    /**
     * The laboratories.
     */
    private List<Laboratory> laboratories;

    /**
     * The acceptation decision.
     */
    private String acceptationDecisionFileType;

    /**
     * The is payment.
     */
    private Boolean isPayment = Boolean.FALSE;

    /**
     * The payment data.
     */
    private PaymentData paymentData;

    /**
     * The invoice total amount.
     */
    private Long invoiceTotalAmount;

    /**
     * The invoice total amount.
     */
    private Long invoiceOtherAmount;

    /**
     * The invoice total ttc amount.
     */
    private Long invoiceTotalTtcAmount;

    /**
     * The show validate payment.
     */
    private Boolean showValidatePayment;

    // BEGIN Remind decision details attributes
    /**
     * The last decision ar.
     */
    private AnalyseResultAp lastDecisionAR;

    /**
     * The last decision test.
     */
    private EssayTestAP lastDecisionTR;

    /**
     * The last decision essay.
     */
    private EssayTestAP lastDecisionER;

    // END Remind decision details attributes
    // BEGIN History decision details attributes
    /**
     * The selected item flow dto.
     */
    private ItemFlowDto selectedItemFlowDto;

    /**
     * The decision details ar.
     */
    private AnalyseResultAp decisionDetailsAR;

    /**
     * The decision details test.
     */
    private EssayTestAP decisionDetailsTR;

    /**
     * The decision details essay.
     */
    private EssayTestAP decisionDetailsER;

    /**
     * The item flow history dto list.
     */
    private List<ItemFlowDto> itemFlowHistoryDtoList;

    /**
     * The item flow history dto list filtred.
     */
    private List<ItemFlowDto> itemFlowHistoryDtoListFiltred;

    /**
     * The specific decisions history.
     */
    private ApSpecificDecisionHistory specificDecisionsHistory;

    // END History decision details attributes
    /**
     * The come from search.
     */
    private boolean comeFromSearch;

    /**
     *
     */
    private boolean vtTypeSelectionViewable;
    private boolean aiMinmidtFileType;
    private boolean vtMinepdedFileType;
    private boolean bsbeMinfofFileType;
    /**
     *
     */
    private FileField vtTypeFileField;
    private String minepdedVtType;

    private String newAttachmentName;

    private java.io.File fileToSave;

    boolean update = true;

    private List<WoodSpecification> specsList;
    private FileFieldValue woodsType;

    /**
     * The Constant ACCEPTATION_FLOWS.
     */
    private static final List<String> ACCEPTATION_FLOWS = Arrays.asList(FlowCode.FL_AP_101.name(), FlowCode.FL_AP_102.name(),
            FlowCode.FL_AP_103.name(), FlowCode.FL_AP_104.name(), FlowCode.FL_AP_105.name(), FlowCode.FL_AP_106.name());

    private static final List<String> ACCPETATION_FLOWS_COTATION_STEP = Arrays.asList(FlowCode.FL_AP_169.name(), FlowCode.FL_AP_170.name(),
            FlowCode.FL_AP_171.name(), FlowCode.FL_AP_172.name(), FlowCode.FL_AP_173.name(), FlowCode.FL_AP_174.name());
    /**
     * Constant DECISION_FLOWS_AT_COTATION_STEP.
     */
    private static final List<String> DECISION_FLOWS_AT_COTATION_STEP = Arrays.asList(FlowCode.FL_AP_169.name(), FlowCode.FL_AP_170.name(),
            FlowCode.FL_AP_171.name(), FlowCode.FL_AP_172.name(), FlowCode.FL_AP_173.name(), FlowCode.FL_AP_174.name(),
            FlowCode.FL_AP_175.name(), FlowCode.FL_AP_176.name(), FlowCode.FL_AP_177.name(), FlowCode.FL_AP_178.name(), FlowCode.FL_AP_179.name(),
            FlowCode.FL_AP_180.name(), FlowCode.FL_AP_181.name(), FlowCode.FL_AP_182.name(), FlowCode.FL_AP_183.name(), FlowCode.FL_AP_184.name(),
            FlowCode.FL_AP_185.name(), FlowCode.FL_AP_186.name(), FlowCode.FL_AP_193.name(), FlowCode.FL_AP_194.name());

    /**
     *
     */
    private static final List<FileTypeCode> PROCESS_ALLOWING_DECISION_AT_COTATION_STEP = Arrays.asList(FileTypeCode.VTD_MINSANTE, FileTypeCode.VTP_MINSANTE,
            FileTypeCode.AI_MINSANTE, FileTypeCode.AT_MINSANTE, FileTypeCode.AE_MINADER, FileTypeCode.AE_MINMIDT, FileTypeCode.AIE_MINADER, FileTypeCode.AI_MINMIDT, FileTypeCode.AS_MINADER,
            FileTypeCode.AS_MINFOF, FileTypeCode.AS_MINCOMMERCE, FileTypeCode.AT_MINEPIA, FileTypeCode.VT_MINEPIA, FileTypeCode.CP_MINEPDED);
    /**
     * The transaction manager.
     */
    @ManagedProperty(value = "#{transactionManager}")
    private PlatformTransactionManager transactionManager;

    /**
     * The checked fimex file type.
     */
    private boolean checkedFimexFileType;

    /**
     * The rejct dispatch allowed.
     */
    private boolean rejctDispatchAllowed;

    /**
     * The name of the field vaidity Date
     */
    final static String VALIDITY_DATE_FIELD_NAME = "DATE_VALIDITE";

    final static String SIGNATAIRE_DATE_FIELD_NAME = "SIGNATAIRE_DATE";

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

    /**
     * Instantiates a new file item detail controller.
     */
    public FileItemApDetailController() {
    }

    /**
     * Inits the.
     */
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, FileItemApDetailController.class.getName());
        }

        selectedFileItem = CollectionUtils.isNotEmpty(currentFile.getFileItemsList()) ? currentFile.getFileItemsList().get(0)
                : null;

        //List des user authority par FileType du current File
        listUserAuthorityFileTypes = userAuthorityFileTypeService.findUserAuthorityFileTypeByFileTypeAndUserList(
                currentFile.getFileType(), getLoggedUser().getMergedDelegatorList());
        loadProductHistoryList();
        //Reloader la liste des fileItem dans la vue productionInfos avec les buttons
//        productInfoItems = initFileItemApDetails();

        // SET ATTCHMENT TO NULL AND DISABLED VIEW PANEL OF PDF VIEWER
        initAttachmentView();
        setAttachmentList(null);

        //Faire appel a la fonction de reset du datagrid des information Produits
        resetDataGridInofrmationProducts();

        // En accédant à partir de la recherche, vérifier la visibilité des boutons des décisions
        if (comeFromSearch) {
            checkIsAllowedUserForCurrentStep();
        }

        //Initializer la liste des attachment du current File
        attachmentList = getCurrentFile().getAttachmentsList();

        //Initialiser la vue du Rappel Décision
        setShowRemindDecisionForm(true);

        //Initialiser la vue de la liste de recommandations article
        setShowListRecommandationArticleForm(true);

        //Initialiser la vue des détails produit
        setShowProductDetailsForm(true);

        //Initialiser la vue de la pièce jointe
        setShowShowAttachmentForm(false);

        // show button to decide
        setShowDecisionButton(true);

        fieldGroups = fieldGroupService.findAllByFileType(currentFile.getFileType(), "01");
        fileFieldGroupDtos = RepetableUtil.groupFileFieldValues(currentFile.getNonRepeatablefileFieldValueList(),
                currentFile.getRepeatablefileFieldValueList(), fieldGroups, applicationPropretiesService, selectedFileItem,
                getCurrentLocale());

        //Remplir la liste des filed Values du dossier
        //Remplir la liste des valeurs des filed Values pour le premier article
        loadAndGroupFileItemFieldValues();

        //Récuperer la derniere décision du current File
        findLastDecisions();

        // Initialize and get file recommandations from database
        refreshRecommandationList();

        // Initialize and get product recommandations from database
        refreshRecommandationArticleList();

        checkIsAllowedRecommandation();

        checkGenerateReportAllowed();

        tabList = new ArrayList<>();
        tabIndexList = concatenateActiveIndexString(tabList);

        authoritiesList = new DualListModel<>(new ArrayList<Authority>(), new ArrayList<Authority>());
        selectedAttachment = null;

        checkedFimexFileType = false;
        if (currentFile != null && currentFile.getFileType().getCode().equals(FileTypeCode.IDE)
                || currentFile.getFileType().getCode().equals(FileTypeCode.IDI)) {
            checkedFimexFileType = true;
        }
        rejctDispatchAllowed = (cotationAllowed && !apDecisionStep.equals(currentFile.getFileItemsList().get(0).getStep()) && !StepCode.ST_AP_47
                .equals(currentFile.getFileItemsList().get(0).getStep().getStepCode()));

        processSpecificAddons();
        aiMinmidtFileType = FileTypeCode.AI_MINMIDT.equals(currentFile.getFileType().getCode());
        vtMinepdedFileType = FileTypeCode.VT_MINEPDED.equals(currentFile.getFileType().getCode());
        bsbeMinfofFileType = FileTypeCode.BSBE_MINFOF.equals(currentFile.getFileType().getCode());
    }

    private void processSpecificAddons() {
        if (currentFile != null && currentFile.getFileType() != null) {
            switch (currentFile.getFileType().getCode()) {
                case AI_MINMIDT:
                    aiMinmidtFileType = true;
                    break;
                case VT_MINEPDED:
                    vtMinepdedFileType = true;
                    break;
                case BSBE_MINFOF:
                    bsbeMinfofFileType = true;
                    specsList = woodSpecificationServce.findByFile(currentFile);
                    woodsType = fileFieldValueService.findValueByFileFieldAndFile("INFORMATIONS_GENERALES_BSB_CERTIFICAT_ENREGISTREMENT_TYPE_PRODUIT", currentFile);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Prepare decisions. On select button "Decider" we should initialize the
     * decision View before rendred it
     */
    public void prepareDecisions() {
        analyseResultApList = null;
        testResultApList = null;
        acceptationDecisionFileType = null;
        decisionAtCotationLevel = Boolean.FALSE;
        isPayment = Boolean.FALSE;
        if (CollectionUtils.isNotEmpty(currentFile.getFileItemsList()) && currentFile.getFileItemsList().get(0) != null
                && !currentFile.getFileItemsList().get(0).getDraft()) {
            // this methode is for RISK MANAGMENT
            for (final FileTypeStep fileTypeStep : currentFile.getFileType().getFileTypeStepList()) {
                if (fileTypeStep.getIsApDecision() != null && fileTypeStep.getIsApDecision()
                        && currentFile.getFileItemsList().get(0).getStep().getId().equals(fileTypeStep.getStep().getId())) {
                    setDisionSystemAllowed(true);
                    break;
                }
            }

            if (currentFile.getFileItemsList().isEmpty()) {
                final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.CHOOSE_DECISION_ERROR);
                JsfUtil.addErrorMessage(msg);
            }

            //Tester sur le Current file si tout les file Item appartiennent à la meme step (Controle important pour la decision)
            final FileItem referenceFileItem = currentFile.getFileItemsList().get(0);
            if (referenceFileItem.getStep() != null) {
                boolean equalsSteps = true;
                for (final FileItem fileItem : currentFile.getFileItemsList()) {
                    if (fileItem.getStep() == null || !referenceFileItem.getStep().getId().equals(fileItem.getStep().getId())) {
                        equalsSteps = false;
                        break;
                    }
                }

                if (equalsSteps) {
                    //Pour la Retreive Seulement une seule decision
                    if (Arrays.asList(FileTypeCode.BSBE_MINFOF, FileTypeCode.VT_MINEPIA).contains(currentFile.getFileType().getCode())) {
                        flows = flowService.findFlowsByFromStepAndFileType2(referenceFileItem.getStep(), referenceFileItem
                                .getFile().getFileType());
                        if (!CollectionUtils.isEmpty(flows)) {
                            for (Flow elt : flows) {
                                final FileTypeFlow fileTypeFlow = fileTypeFlowService.findByFlowAndFileType(currentFile.getFileType(), elt);
                                if (fileTypeFlow != null) {
                                    elt.setRedefinedLabelEn(fileTypeFlow.getLabelEn());
                                    elt.setRedefinedLabelFr(fileTypeFlow.getLabelFr());
                                }
                            }
                        }
                    } else {
                        flows = destinationFlowsFromCurrentStep;
                    }
                    if (comeFromRetrieveAp != null && comeFromRetrieveAp) {
                        selectedFlow = flowService.findFlowByCurrentStep(apDecisionStep);
                    } //Pour la decision et la cotation
                    else {
                        selectedFlow = null;
                    }
                    decisionDiv.getChildren().clear();
                    final RequestContext requestContext = RequestContext.getCurrentInstance();
                    requestContext.execute("PF('" + DECISION_DIALOG + "').show();");
                } else {
                    final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(ControllerConstants.Bundle.Messages.SAME_STEPS_ERROR);
                    JsfUtil.addErrorMessage(msg);
                }
            }

        } else {
            decisionAtCotationLevel = Boolean.FALSE;
            final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    ControllerConstants.Bundle.Messages.ROLLBACK_BEFORE_DECISION);
            JsfUtil.addErrorMessage(msg);
        }
    }

    /**
     * Prepare special decision at cotation level
     */
    public void prepareDecisionsAtCotation() {
        analyseResultApList = null;
        testResultApList = null;
        acceptationDecisionFileType = null;
        isPayment = Boolean.FALSE;
        decisionAtCotationLevel = Boolean.TRUE;
        if (CollectionUtils.isNotEmpty(currentFile.getFileItemsList()) && currentFile.getFileItemsList().get(0) != null
                && !currentFile.getFileItemsList().get(0).getDraft()) {
            // this methode is for RISK MANAGMENT
            for (final FileTypeStep fileTypeStep : currentFile.getFileType().getFileTypeStepList()) {
                if (fileTypeStep.getIsApDecision() != null && fileTypeStep.getIsApDecision()
                        && currentFile.getFileItemsList().get(0).getStep().getId().equals(fileTypeStep.getStep().getId())) {
                    setDisionSystemAllowed(true);
                    break;
                }
            }

            if (currentFile.getFileItemsList().isEmpty()) {
                final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.CHOOSE_DECISION_ERROR);
                JsfUtil.addErrorMessage(msg);
            }

            //Tester sur le Current file si tout les file Item appartiennent à la meme step (Controle important pour la decision)
            final FileItem referenceFileItem = currentFile.getFileItemsList().get(0);
            if (referenceFileItem.getStep() != null) {
                boolean equalsSteps = true;
                for (final FileItem fileItem : currentFile.getFileItemsList()) {
                    if (fileItem.getStep() == null || !referenceFileItem.getStep().getId().equals(fileItem.getStep().getId())) {
                        equalsSteps = false;
                        break;
                    }
                }

                if (equalsSteps) {
                    //Pour la Retreive Seulement une seule decision
                    flows = destinationFlowsFromCurrentCotationStep;
                    if (comeFromRetrieveAp != null && comeFromRetrieveAp) {
                        selectedFlow = flowService.findFlowByCurrentStep(apDecisionStep);
                    } //Pour la decision et la cotation
                    else {
                        selectedFlow = null;
                    }
                    decisionDiv.getChildren().clear();
                    final RequestContext requestContext = RequestContext.getCurrentInstance();
                    requestContext.execute("PF('" + DECISION_DIALOG + "').show();");
                } else {
                    final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(ControllerConstants.Bundle.Messages.SAME_STEPS_ERROR);
                    JsfUtil.addErrorMessage(msg);
                }
            }

        } else {
            final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    ControllerConstants.Bundle.Messages.ROLLBACK_BEFORE_DECISION);
            JsfUtil.addErrorMessage(msg);
        }
    }

    /**
     * Show attachment Pour afficher la liste des attachement du File Courrant.
     */
    public void showAttachment() {

        selectedFileItem = null;
        setShowRemindDecisionForm(false);
        setShowListRecommandationArticleForm(false);
        setShowProductDetailsForm(false);
        setShowShowAttachmentForm(true);
        // Hide button
        setShowDecisionButton(false);
        //Passer l'Attachement sséléctioné au AttachmentController
        final AttachmentController attachmentController = getInstanceOfPageAttachmentController();
        attachmentController.setSelectedAttachment(selectedAttachment);
        attachmentController.init();
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
                LOG.error(e.getMessage(), e);
            }

            final byte[] bytes = out.toByteArray();

            return new DefaultStreamedContent(new ByteArrayInputStream(bytes), !StringUtils.isEmpty(contentStream.getMimeType()) ? contentStream.getMimeType() : "application/msword", selectedAttachment.getDocumentName());
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Impossible de télécharger la pièce jointe");
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

    public byte[] getBytesFromAttachment(Attachment att) {
        if (att == null) {
            return null;
        }
        final Session sessionCmisClient = CmisSession.getInstance();
        ContentStream contentStream = CmisClient.getDocumentByPath(sessionCmisClient, att.getPath()
                + AlfrescoDirectoriesInitializer.SLASH + att.getDocumentName());
        if (contentStream == null) {
            contentStream = CmisClient.getDocumentByPath(sessionCmisClient, att.getPath());
        }
        if (contentStream == null) {
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
            LOG.error(e.getMessage(), e);
        }

        return out.toByteArray();
    }

    static CharsetEncoder asciiEncoder
            = Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

    public static boolean isPureAscii(String v) {
        return asciiEncoder.canEncode(v);
    }

    private Attachment findAuthorizationAttachment() {
        List<Attachment> attachments = attachmentService.findAttachmentsByFile(currentFile);
        if (attachments != null) {
            for (Attachment att : attachments) {
                if (att.getAttachmentType().equals("AI_MINMIDT")) {
                    return att;
                }
            }
        }
        return null;
    }

    private List<Attachment> findAttachmentsToSend(FileTypeCode fileTypeCode) {
        List<Attachment> attachmentsToSend = new ArrayList<>();
        List<Attachment> attachments = attachmentService.findAttachmentsByFile(currentFile);
        if (attachments == null) {
            return null;
        }
        switch (fileTypeCode) {
            case AI_MINMIDT: {
                for (Attachment att : attachments) {
                    if (att.getAttachmentType().equals(FileTypeCode.AI_MINMIDT.name())) {
                        attachmentsToSend.add(att);
                    }
                }
                break;
            }
            case VT_MINEPDED: {
                for (Attachment att : attachments) {
                    if (att.getAttachmentType().equals("RECU") || att.getAttachmentType().equals("QUITTANCE")) {
                        //attachmentsToSend.add(att);
                        //l'ajout des ses pièces jointes dans la réponse envoyé à webguce empêche la sauvegarde de la pièce jointe du VT MINEPDED.
                    }
                }
                break;
            }
            default:
                break;
        }
        return attachmentsToSend.isEmpty() ? null : attachmentsToSend;
    }

    public void handleFileUpload(FileUploadEvent event) {
        update = true;
        selectedAttachment = findAuthorizationAttachment();
        if (selectedAttachment == null) {
            selectedAttachment = new Attachment();
            selectedAttachment.setFile(currentFile);
            selectedAttachment.setAttachmentType("AI_MINMIDT");
            update = false;
        }
        UploadedFile file = event.getFile();
        if (file == null || StringUtils.isEmpty(file.getFileName())) {
            fileToSave = null;
            return;
        }
        String name = file.getFileName();
        if (isPureAscii(name)) {
            String[] s = name.replace("\\", "/").split("[/]+");
            if (s != null && s.length > 0) {
                name = s[s.length - 1];
            }
            if (event.getFile().getContents().length > 1024000) {
                System.out.println("handerFileUpload, file too large");
                JsfUtil.addErrorMessage("La taille du fichier n'est pas supportée");
                fileToSave = null;
            } else {
                System.out.println("handerFileUpload, good. Waiting to be saved");
                fileToSave = new java.io.File(name.replaceAll("/[^A-Za-z0-9]/", ""));
                newAttachmentName = fileToSave.getName();
                try {
                    FileUtils.writeByteArrayToFile(fileToSave, file.getContents());
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(FileItemApDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JsfUtil.addErrorMessage("Le nom de la pièce renseignée est invalide");
        }

    }

    public void saveAttachment() {
        try {
            if (fileToSave == null) {
                return;
            }
            final List<java.io.File> attachedFiles = new ArrayList<>();
            attachedFiles.add(fileToSave);
            CmisUtils.sendDocument(attachedFiles, CmisSession.getInstance(), "/siat");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FileItemApDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
        selectedAttachment.setFile(currentFile);
        selectedAttachment.setDocumentName(fileToSave.getName());
        selectedAttachment.setPath("/siat/" + fileToSave.getName());
        if (update) {
            attachmentService.update(selectedAttachment);
        } else {
            attachmentService.save(selectedAttachment);
        }
        fileToSave = null;
        newAttachmentName = "";
    }

    public String getNewAttachmentName() {
        return newAttachmentName;
    }

    public void setNewAttachmentName(String newAttachmentName) {
        this.newAttachmentName = newAttachmentName;
    }

    public java.io.File getFileToSave() {
        return fileToSave;
    }

    public void setFileToSave(java.io.File fileToSave) {
        this.fileToSave = fileToSave;
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
     * Inits the attachment view : Initialzer la Vue de l'attachment
     * ViewPdf.xhtml selon le fichier selectioné
     */
    public void initAttachmentView() {
        final AttachmentController attachmentController = getInstanceOfPageAttachmentController();
        attachmentController.setSelectedAttachment(null);
        attachmentController.setShowPanelViewJpeg(false);
        attachmentController.setShowPanelViewPdf(false);
        attachmentController.init();
    }

    /**
     * Inits the file item ap details: Initializer la liste des FileItem du File
     * Courrant.
     *
     * @return the list
     */
    public List<FileItem> initFileItemApDetails() {
        //Disabled Button Annuler, Decider, Coter et envoyer l'ors du premier chargement
        rollBackDecisionsAllowed = false;
        sendDecisionAllowed = false;
        decisionAllowed = false;
        cotationAllowed = false;
        fileItemHasDraft = false;
        rejctDispatchAllowed = false;
        decisionAllowedAtCotationLevel = false;
        boolean returnAllowed;
        boolean acceptationFlowFound;
        if (currentFile.getFileItemsList() == null) {
            currentFile.setFileItemsList(fileItemService.findFileItemsByFile(currentFile));

            //Récuperer la liste des FileItem selon le UserAuthorityFileTypes
            productInfoItemsEnabled = fileItemService.filterFileItemsByUserAuthorityFileTypes(currentFile.getFileItemsList(),
                    listUserAuthorityFileTypes, getLoggedUser());

            //Récuperer Step Courante du File Courant
            final Step currentStep = currentFile.getFileItemsList().get(0).getStep();

            final List<Step> stepListByFileType = currentFile.getFileType().getStepList();

            //apDecisionStep : pour voir si le step courant est un step Decisionnel du AP workflow
            for (final FileTypeStep fileTypeStep : currentFile.getFileType().getFileTypeStepList()) {
                if (fileTypeStep.getIsApDecision() != null && fileTypeStep.getIsApDecision()) {
                    apDecisionStep = fileTypeStep.getStep();
                    break;
                }
            }

            //List des step de Cotation et Etude
            final List<StepCode> apDecisionStepCode = Arrays.asList(StepCode.ST_AP_48, StepCode.ST_AP_49, StepCode.ST_AP_50,
                    StepCode.ST_AP_51, StepCode.ST_AP_52, StepCode.ST_AP_53);

            // Ici, on va remplir la liste des flow qui vont apparaitre dans la view Decision ainsi setter les variable boolean pour l'affichage des button
            if (stepListByFileType != null && stepListByFileType.contains(currentStep) && currentStep.getFromStepFlowsList() != null) {
                destinationFlowsFromCurrentStep = new ArrayList<>();
                destinationFlowsFromCurrentCotationStep = new ArrayList<>();
                returnAllowed = false;
                acceptationFlowFound = false;
                for (final Flow flow : currentStep.getFromStepFlowsList()) {
                    //to redefine flows labelFr, and labelEn
                    final FileTypeFlow fileTypeFlow = fileTypeFlowService.findByFlowAndFileType(currentFile.getFileType(), flow);
                    if (fileTypeFlow != null) {
                        flow.setRedefinedLabelEn(fileTypeFlow.getLabelEn());
                        flow.setRedefinedLabelFr(fileTypeFlow.getLabelFr());
                    }
                    //Dans cette condition nous somme sur la voie de COTATION
                    if (flow.getIsCota() && stepListByFileType.contains(flow.getToStep())) {
                        cotationAllowed = true;
                        decisionAllowed = false;
                    } //Dans cette condition nous somme sur la voie de DECISION (flow.getToStep() == null : cad rejet annulation)
                    else if (!cotationAllowed && !flow.getIsCota()
                            && (stepListByFileType.contains(flow.getToStep()) || flow.getToStep() == null)) {
                        if (currentStep.getStepCode().equals(StepCode.ST_AP_44)
                                && CollectionUtils.containsAny(getLoggedUser().getAuthorities(), currentStep.getRoleList())) {
                            this.setComeFromRetrieveAp(Boolean.TRUE);
                        } else {
                            decisionAllowed = true;
                        }
                        cotationAllowed = false;
                        if (!flow.getFromStep().equals(apDecisionStep) && !returnAllowed
                                && Arrays.asList(FlowCode.FL_AP_155.name(), FlowCode.FL_AP_156.name(), FlowCode.FL_AP_157.name(),
                                        FlowCode.FL_AP_158.name(), FlowCode.FL_AP_159.name()).contains(flow.getCode())) {
//                            destinationFlowsFromCurrentStep = Collections.singletonList(flow);
                            destinationFlowsFromCurrentStep = new ArrayList<>();
                            if (fileTypeFlow != null || !bsbeMinfofFileType) {
                                destinationFlowsFromCurrentStep.add(flow);
                            }
                            cotationAllowed = true;
                            decisionAllowed = false;
                            returnAllowed = true;
//                            break;
                        }
                        if (flow.getToStep() != null && apDecisionStepCode.contains(flow.getToStep().getStepCode()) && !returnAllowed
                                && flow.getToStep().getId().equals(apDecisionStep.getId())
                                && !DECISION_FLOWS_AT_COTATION_STEP.contains(flow.getCode())) {

                            if (fileTypeFlow != null || !bsbeMinfofFileType) {
                                destinationFlowsFromCurrentStep.add(flow);
                            }
                        } else if ((flow.getToStep() != null && !apDecisionStepCode.contains(flow.getToStep().getStepCode())) && !returnAllowed
                                && !DECISION_FLOWS_AT_COTATION_STEP.contains(flow.getCode()) || flow.getToStep() == null) {
                            destinationFlowsFromCurrentStep.add(flow);
                        }
                    }
                    if (comeFromRetrieveAp != null && comeFromRetrieveAp && !PROCESS_ALLOWING_DECISION_AT_COTATION_STEP.contains(currentFile.getFileType().getCode())
                            && !DECISION_FLOWS_AT_COTATION_STEP.contains(flow.getCode())) {
                        cotationAllowed = false;
                        decisionAllowed = false;
                    }
                    if (ACCEPTATION_FLOWS.contains(flow.getCode())) {
                        acceptationFlowFound = true;
                    }
                    if (!acceptationFlowFound && PROCESS_ALLOWING_DECISION_AT_COTATION_STEP.contains(currentFile.getFileType().getCode()) && !flow.getIsCota() && DECISION_FLOWS_AT_COTATION_STEP.contains(flow.getCode()) && stepListByFileType.contains(flow.getToStep())) {
                        if (!destinationFlowsFromCurrentCotationStep.contains(flow)) {
                            destinationFlowsFromCurrentCotationStep.add(flow);
                        }
                        decisionAllowedAtCotationLevel = Boolean.TRUE;
                    }
                }

                boolean hasPayment = false;
                boolean payed = false;
                for (final Step step : stepListByFileType) {
                    if (step.getStepCode().equals(StepCode.ST_AP_64)) {
                        hasPayment = true;
                        break;
                    }
                }
                if (hasPayment) {
                    for (final ItemFlowDto hist : itemFlowHistoryDtoList) {
                        if (hist.getItemFlow().getFlow().getCode().equals(FlowCode.FL_AP_166.name())) {
                            payed = true;
                            final Iterator<Flow> it = destinationFlowsFromCurrentStep.iterator();
                            while (it.hasNext()) {
                                final Flow flx = it.next();
                                if (Arrays.asList(FlowCode.FL_AP_160.name(), FlowCode.FL_AP_161.name(), FlowCode.FL_AP_162.name(),
                                        FlowCode.FL_AP_163.name(), FlowCode.FL_AP_164.name(), FlowCode.FL_AP_165.name(),
                                        FlowCode.FL_AP_193.name(), FlowCode.FL_AP_194.name()).contains(
                                        flx.getCode())) {
                                    it.remove();
                                }
                            }
                            final Iterator<Flow> it1 = destinationFlowsFromCurrentCotationStep.iterator();
                            while (it1.hasNext()) {
                                final Flow flx = it1.next();
                                if (Arrays.asList(FlowCode.FL_AP_160.name(), FlowCode.FL_AP_161.name(), FlowCode.FL_AP_162.name(),
                                        FlowCode.FL_AP_163.name(), FlowCode.FL_AP_164.name(), FlowCode.FL_AP_165.name(),
                                        FlowCode.FL_AP_193.name(), FlowCode.FL_AP_194.name()).contains(
                                        flx.getCode())) {
                                    it1.remove();
                                }
                            }
                        }

                    }
                    if (!payed) {
                        final Iterator<Flow> it = destinationFlowsFromCurrentStep.iterator();
                        while (it.hasNext()) {
                            final Flow flx = it.next();
                            if (!flx.getIsCota() && ACCEPTATION_FLOWS.contains(flx.getCode())) {
                                it.remove();
                            }
                        }
                        final Iterator<Flow> it1 = destinationFlowsFromCurrentCotationStep.iterator();
                        while (it1.hasNext()) {
                            final Flow flx = it1.next();
                            if (!flx.getIsCota() && ACCPETATION_FLOWS_COTATION_STEP.contains(flx.getCode())) {
                                it1.remove();
                            }
                        }
                    }
                }

            }
            if (currentFile.getFileType().getCode().equals(FileTypeCode.PIVPSRP_MINADER)) {

                boolean hasPayment = false;
                boolean payed = false;
                for (final Step step : stepListByFileType) {
                    if (step.getStepCode().equals(StepCode.ST_AP_65)) {
                        hasPayment = true;
                        break;
                    }
                }
                if (hasPayment) {
                    hist:
                    for (final ItemFlowDto hist : itemFlowHistoryDtoList) {
                        if (hist.getItemFlow().getFlow().getCode().equals(FlowCode.FL_AP_168.name())) {
                            payed = true;
                            final Iterator<Flow> it = destinationFlowsFromCurrentStep.iterator();
                            while (it.hasNext()) {
                                final Flow flx = it.next();
                                if (Arrays.asList(FlowCode.FL_AP_167.name()).contains(flx.getCode())) {
                                    it.remove();
                                }
                            }
                            break hist;
                        }

                    }
                    if (!payed) {
                        final Iterator<Flow> it = destinationFlowsFromCurrentStep.iterator();
                        while (it.hasNext()) {
                            final Flow flx = it.next();
                            if (FlowCode.FL_AP_88.name().contains(flx.getCode())) {
                                it.remove();
                            }
                        }
                    }
                }
            }
        }

        // Si on a des brouillon alors afficher les boutton send Decision et Annuller
        if (hasDraftFileItemInList(productInfoItems)) {
            rollBackDecisionsAllowed = true;
            sendDecisionAllowed = true;
            fileItemHasDraft = true;
        }

        return currentFile.getFileItemsList();
    }

    /**
     * Checks for draft file item in list : Renvoyer True si la Liste des
     * FileItem contient des FileItem en mode Brouillon.
     *
     * @param fileItems the file items
     * @return the boolean
     */
    public Boolean hasDraftFileItemInList(final List<FileItem> fileItems) {

        if (fileItems != null && !fileItems.isEmpty()) {
            for (final FileItem fileItem : fileItems) {
                if (fileItem.getDraft() != null && fileItem.getDraft()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the instance of page schedule controller. : Retourner l'Instance du
     * controlleur de Schedule (Agenda des rendez-vous inspecteur)
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
     * Change decision handler.
     */
    public void changeDecisionHandler() {
        decisionDiv.getChildren().clear();
        analyseResultApList = null;
        testResultApList = null;
        String stringId = null;
        acceptationDecisionFileType = null;
        final boolean isFimex = currentFile.getFileType().getCode().equals(FileTypeCode.FIMEX_WF)
                && FlowCode.FL_AP_106.name().equals(selectedFlow.getCode());
        isPayment = Boolean.FALSE;
        //paiement;FlowCode.FL_CO_155.name().equals(selectedFlow.getCode())
        if (Arrays.asList(FlowCode.FL_AP_160.name(), FlowCode.FL_AP_161.name(), FlowCode.FL_AP_162.name(),
                FlowCode.FL_AP_163.name(), FlowCode.FL_AP_164.name(), FlowCode.FL_AP_165.name(), FlowCode.FL_AP_167.name(),
                FlowCode.FL_AP_193.name(), FlowCode.FL_AP_194.name()).contains(
                selectedFlow.getCode())) {
            isPayment = Boolean.TRUE;
            paymentData = new PaymentData();

            paymentData.setPaymentItemFlowList(new ArrayList<PaymentItemFlow>());
            for (final FileItem fi : currentFile.getFileItemsList()) {
                paymentData.getPaymentItemFlowList().add(new PaymentItemFlow(false, fi.getId(), fi.getNsh()));
            }
        }
        for (final DataType dataType : selectedFlow.getDataTypeList()) {

            if (BooleanUtils.toBoolean(dataType.getDisabled())) {
                continue;
            }

            final FacesContext context = FacesContext.getCurrentInstance();

            if (dataType.getId() != null) {
                stringId = String.valueOf(dataType.getId());
            }
            HtmlPanelGroup htmlPanelGroup = null;
            if (!isFimex || dataType.getType().equals(DataTypeEnnumeration.CALENDAR.getCode())) {
                // Label for the component
                final HtmlOutputLabel htmlOutputLabel = (HtmlOutputLabel) context.getApplication().createComponent(
                        HtmlOutputLabel.COMPONENT_TYPE);
                htmlOutputLabel.setFor(ID_DECISION_LABEL + stringId);
                if (FacesContext.getCurrentInstance().getViewRoot().getLocale().equals(Locale.FRENCH)) {
                    htmlOutputLabel.setValue(dataType.getLabel());
                } else {
                    htmlOutputLabel.setValue(dataType.getLabelEn());
                }
                decisionDiv.getChildren().add(htmlOutputLabel);

                htmlPanelGroup = (HtmlPanelGroup) context.getApplication().createComponent(HtmlPanelGroup.COMPONENT_TYPE);
            }
            if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXT.getCode()) && !isFimex) {
                final HtmlInputText inputText = (HtmlInputText) context.getApplication()
                        .createComponent(HtmlInputText.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    inputText.setRequired(true);
                    inputText.setRequiredMessage(dataType.getLabel()
                            + Constants.SPACE
                            + ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                                    "RequiredMessage_standard"));
                }
                inputText.setId(ID_DECISION_LABEL + stringId);
                htmlPanelGroup.getChildren().add(inputText);
            } else if (dataType.getType().equals(DataTypeEnnumeration.CHEKBOX.getCode()) && !isFimex) {
                final HtmlSelectBooleanCheckbox booleanCheckbox = (HtmlSelectBooleanCheckbox) context.getApplication()
                        .createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    booleanCheckbox.setRequired(true);
                    booleanCheckbox.setRequiredMessage(dataType.getLabel()
                            + Constants.SPACE
                            + ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                                    "RequiredMessage_standard"));
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
                            + ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                                    "RequiredMessage_standard"));
                }
                calendar.setId(ID_DECISION_LABEL + stringId);
                calendar.setPattern("dd-MM-yyyy");
                calendar.setLocale(Locale.FRANCE);
                calendar.setNavigator(true);
                if (isFimex) {
                    final java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.set(java.util.Calendar.MONTH, 11);
                    cal.set(java.util.Calendar.DAY_OF_MONTH, 31);
                    calendar.setValue(cal.getTime());
                    calendar.setReadonly(true);
                }
                htmlPanelGroup.getChildren().add(calendar);
            } else if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXTAREA.getCode()) && !isFimex) {
                final HtmlInputTextarea inputTextarea = (HtmlInputTextarea) context.getApplication().createComponent(
                        HtmlInputTextarea.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    inputTextarea.setRequired(true);
                    inputTextarea.setRequiredMessage(dataType.getLabel()
                            + Constants.SPACE
                            + ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                                    "RequiredMessage_standard"));
                }
                inputTextarea.setRows(10);
                inputTextarea.setId(ID_DECISION_LABEL + stringId);
                htmlPanelGroup.getChildren().add(inputTextarea);
            }
            if (!isFimex || dataType.getType().equals(DataTypeEnnumeration.CALENDAR.getCode())) {
                final Message message = (Message) context.getApplication().createComponent(Message.COMPONENT_TYPE);
                message.setFor(ID_DECISION_LABEL + stringId);
                htmlPanelGroup.getChildren().add(message);

                decisionDiv.getChildren().add(htmlPanelGroup);
            }
        }

        vtTypeSelectionViewable = FileTypeCode.VT_MINEPDED.equals(currentFile.getFileType().getCode()) && (FlowCode.FL_AP_104.name().equals(selectedFlow.getCode()) || FlowCode.FL_AP_106.name().equals(selectedFlow.getCode()));
        vtTypeFileField = !vtTypeSelectionViewable ? null : fileFieldService.findFileFieldByCodeAndFileType(ControllerConstants.TYPE_OF_TECHNICAL_VISA, FileTypeCode.VT_MINEPDED);

        //Acceptation Suite Etude_3 AP EH Minader (affichage des formulaire : analyse result and test result)
        if (ACCEPTATION_FLOWS.contains(selectedFlow.getCode())
                && FileTypeCode.EH_MINADER.equals(selectedFileItem.getFile().getFileType().getCode())) {
            acceptationDecisionFileType = FileTypeCode.EH_MINADER.name();
            laboratories = laboratoryService.findByAdministration(getLoggedUser().getAdministration());
            analyseResultApList = new ArrayList<>();
            testResultApList = new ArrayList<>();
            final List<FileItem> fileItems = currentFile.getFileItemsList();
            for (final FileItem fileItem : fileItems) {
                final ItemFlow itemFlow = new ItemFlow();
                itemFlow.setFlow(selectedFlow);
                itemFlow.setUnread(Boolean.TRUE);
                itemFlow.setFileItem(fileItem);
                itemFlow.setSender(getLoggedUser());
                analyseResultApList.add(new AnalyseResultAp(itemFlow));
                testResultApList.add(new EssayTestAP(itemFlow));
            }
        } else if (ACCEPTATION_FLOWS.contains(selectedFlow.getCode())
                && FileTypeCode.CAT_MINADER.equals(selectedFileItem.getFile().getFileType().getCode())) {
            acceptationDecisionFileType = FileTypeCode.CAT_MINADER.name();
            final List<FileItem> fileItems = currentFile.getFileItemsList();
            testResultApList = new ArrayList<>();
            for (final FileItem fileItem : fileItems) {
                final ItemFlow itemFlow = new ItemFlow();
                itemFlow.setFlow(selectedFlow);
                itemFlow.setUnread(Boolean.TRUE);
                itemFlow.setFileItem(fileItem);
                itemFlow.setSender(getLoggedUser());
                testResultApList.add(new EssayTestAP(itemFlow));
            }
        }
    }

    /**
     * Payment amout value changed listener.
     */
    public void paymentAmoutValueChangedListener() {
        invoiceTotalAmount = 0L;
        Long totalTva = 0L;
        for (PaymentItemFlow pi : paymentData.getPaymentItemFlowList()) {
            if (pi.getMontantHt() == null) {
                pi.setMontantHt(0L);
            }
            if (pi.getMontantTva() == null) {
                pi.setMontantTva(0L);
            }

            invoiceTotalAmount += pi.getMontantHt();
            totalTva += pi.getMontantTva();
        }

        if (invoiceOtherAmount == null) {
            invoiceOtherAmount = 0L;
        }

        invoiceTotalTtcAmount = invoiceTotalAmount + totalTva + invoiceOtherAmount;

        paymentData.setMontantHt(invoiceTotalAmount);
        paymentData.setMontantTva(totalTva);
        paymentData.setMontantEncaissement(invoiceTotalTtcAmount.doubleValue());
        paymentData.setAutreMontant(invoiceOtherAmount);
    }

    /**
     * Remplir la liste des valeurs des filed Values pour le un article.
     */
    private void loadAndGroupFileItemFieldValues() {
        fileItemFieldValues = selectedFileItem.getFileItemFieldValueList();
        groupFileItemFieldValues();
    }

    /**
     * Find last decisions.
     */
    private void findLastDecisions() {
        lastDecisionAR = null;
        lastDecisionTR = null;
        lastDecisionER = null;

        lastDecisions = itemFlowService.findLastSentItemFlowByFileItem(selectedFileItem);
        final FileTypeFlow fileTypeFlow = fileTypeFlowService.findByFlowAndFileType(selectedFileItem.getFile().getFileType(),
                lastDecisions.getFlow());
        if (fileTypeFlow != null) {
            lastDecisions.getFlow().setRedefinedLabelEn(fileTypeFlow.getLabelEn());
            lastDecisions.getFlow().setRedefinedLabelFr(fileTypeFlow.getLabelFr());
        }
        specificDecisionsHistory = new ApSpecificDecisionHistory();

        if (FlowCode.FL_AP_102.name().equals(lastDecisions.getFlow().getCode())
                && FileTypeCode.EH_MINADER.equals(lastDecisions.getFileItem().getFile().getFileType().getCode())) {
            lastDecisionAR = analyseResultApService.findAnalyseResultApByItemFlow(lastDecisions);
        } else if (FlowCode.FL_AP_102.name().equals(lastDecisions.getFlow().getCode())
                && FileTypeCode.CAT_MINADER.equals(lastDecisions.getFileItem().getFile().getFileType().getCode())) {
            lastDecisionTR = essayTestApService.findByItemFlow(lastDecisions);
            lastDecisionER = essayTestApService.findByItemFlow(lastDecisions);
        } else if (Arrays.asList(FlowCode.FL_AP_VT1_03.name(), FlowCode.FL_AP_166.name()).contains(lastDecisions.getFlow().getCode())) {
            specificDecisionsHistory.setLastPaymentData(paymentDataService.findPaymentDataByItemFlow(lastDecisions));
            //    specificDecisionsHistory.setDecisionDetailsPayData(paymentDataService.findPaymentDataByItemFlow(lastDecisions));
        }
    }

    /**
     * Decision details by item flow.
     */
    public void decisionDetailsByItemFlow() {
        decisionDetailsAR = null;
        decisionDetailsTR = null;
        decisionDetailsER = null;
        specificDecisionsHistory = new ApSpecificDecisionHistory();

        showValidatePayment = Boolean.FALSE;

        if (FlowCode.FL_AP_102.name().equals(selectedItemFlowDto.getItemFlow().getFlow().getCode())
                && FileTypeCode.EH_MINADER.name().equals(
                        selectedItemFlowDto.getItemFlow().getFileItem().getFile().getFileType().getCode().name())) {
            decisionDetailsAR = analyseResultApService.findAnalyseResultApByItemFlow(selectedItemFlowDto.getItemFlow());
            decisionDetailsTR = essayTestApService.findByItemFlow(selectedItemFlowDto.getItemFlow());
        } else if (FlowCode.FL_AP_102.name().equals(selectedItemFlowDto.getItemFlow().getFlow().getCode())
                && FileTypeCode.CAT_MINADER.name().equals(
                        selectedItemFlowDto.getItemFlow().getFileItem().getFile().getFileType().getCode().name())) {
            decisionDetailsER = essayTestApService.findByItemFlow(selectedItemFlowDto.getItemFlow());
        } else if (FlowCode.FL_AP_166.name().equals(lastDecisions.getFlow().getCode())) {
            specificDecisionsHistory.setDecisionDetailsPayData(paymentDataService.findPaymentDataByItemFlow(lastDecisions));
        }
    }

    /**
     * Load product history list.
     */
    private void loadProductHistoryList() {
        itemFlowHistoryDtoList = new ArrayList<>();
        final List<ItemFlow> itemFlowHistoryList = itemFlowService.findItemFlowByFileItem(selectedFileItem);
        //final List<FileItem> itemstepHistoryList = fileItemService.findFileItemsByFile(selectedFileItem.getFile());

        if (CollectionUtils.isNotEmpty(itemFlowHistoryList)) {
            for (int i = 0; i < itemFlowHistoryList.size(); i++) {
                final FileTypeFlow fileTypeFlow = fileTypeFlowService.findByFlowAndFileType(selectedFileItem.getFile().getFileType(),
                        itemFlowHistoryList.get(i).getFlow());
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

                final FileTypeStep fileTypeStep = fileTypeStepService.findFileTypeStepByFileTypeAndStep(selectedFileItem.getFile()
                        .getFileType(), itemFlowDto.getItemFlow().getFlow().getToStep());

                if (fileTypeStep != null) {
                    itemFlowDto.getItemFlow().getFileItem().setRedefinedLabelEn(fileTypeStep.getLabelEn());
                    itemFlowDto.getItemFlow().getFileItem().setRedefinedLabelFr(fileTypeStep.getLabelFr());
                }

            }

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
     * Change product selection.
     */
    public void changeProductSelection() {
        if (selectedFileItem != null) {
            findLastDecisions();
            loadProductHistoryList();
            refreshRecommandationArticleList();
        }

        //Remplir la liste des valeurs des filed Values pour le premier article
        loadAndGroupFileItemFieldValues();

        selectedAttachment = null;
        setShowRemindDecisionForm(true);
        setShowListRecommandationArticleForm(true);
        setShowProductDetailsForm(true);
        setShowShowAttachmentForm(false);
        // enable decision
        setShowDecisionButton(true);
    }

    /**
     * Reload page listener.
     */
    public void reloadPage() {
        final FacesContext fc = FacesContext.getCurrentInstance();
        final boolean isGETMethod = ((HttpServletRequest) fc.getExternalContext().getRequest()).getMethod().equals("GET");
        // Skip ajax requests. and Http GET method
        if (isGETMethod) {
            init();
        }
    }

    /**
     * Save decision.
     *
     * @throws ParseException the parse exception
     */
    public synchronized void saveDecision() throws ParseException {
        final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        final TransactionStatus status = transactionManager.getTransaction(def);
        try {
            // Check if the step of fileItems was changed by another user when the decision popup is open
            if (fileItemService.verifyStepChangedWhileDecisionInProgress(currentFile.getFileItemsList())) {
                final String summary = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.ERROR_DIALOG_TITLE);

                final String detail = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.CANCEL_REQUEST_IN_PROGRESS);

                LOG.error(detail);
                // the user must refresh the details page (return to the dashboard then open the file details page) to get the correct decision list in the decision popup
                JsfUtil.showMessageInDialog(FacesMessage.SEVERITY_ERROR, summary, detail);
                return;
            }

            final List<ItemFlowData> flowDatas = new ArrayList<>();

            for (final DataType dataType : selectedFlow.getDataTypeList()) {

                if (BooleanUtils.toBoolean(dataType.getDisabled())) {
                    continue;
                }

                final ItemFlowData itemFlowData = new ItemFlowData();
                itemFlowData.setDataType(dataType);
                final boolean isFimex = currentFile.getFileType().getCode().equals(FileTypeCode.FIMEX_WF)
                        && FlowCode.FL_AP_106.name().equals(selectedFlow.getCode());

                if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXT.getCode()) && !isFimex) {

                    final HtmlInputText valueDataType = (HtmlInputText) decisionDiv.findComponent(ID_DECISION_LABEL + dataType.getId());

                    itemFlowData.setValue(valueDataType.getValue().toString());

                } else if (dataType.getType().equals(DataTypeEnnumeration.CHEKBOX.getCode()) && !isFimex) {
                    final HtmlSelectBooleanCheckbox valueDataType = (HtmlSelectBooleanCheckbox) decisionDiv
                            .findComponent(ID_DECISION_LABEL + dataType.getId());
                    itemFlowData.setValue(valueDataType.getValue().toString());

                } else if (dataType.getType().equals(DataTypeEnnumeration.CALENDAR.getCode())) {
                    final Calendar valueDataType = (Calendar) decisionDiv.findComponent(ID_DECISION_LABEL + dataType.getId());
                    itemFlowData.setValue(DateUtils.formatSimpleDateFromObject(DateUtils.FRENCH_DATE, valueDataType.getValue()));

                } else if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXTAREA.getCode()) && !isFimex) {
                    final HtmlInputTextarea valueDataType = (HtmlInputTextarea) decisionDiv.findComponent(ID_DECISION_LABEL
                            + dataType.getId());
                    itemFlowData.setValue(valueDataType.getValue().toString());
                }
                if (!Objects.equals(itemFlowData.getValue(), null)) {
                    flowDatas.add(itemFlowData);
                }
            }
            //        if there is an accepting step for EH_MINADER or CAT_MINADER
            if (ACCEPTATION_FLOWS.contains(selectedFlow.getCode())
                    && (FileTypeCode.EH_MINADER.equals(currentFile.getFileType().getCode()) || FileTypeCode.CAT_MINADER
                    .equals(currentFile.getFileType().getCode()))) {
                final List<ItemFlow> itemFlows = new ArrayList<>();
                for (final EssayTestAP essayTestAP : testResultApList) {
                    essayTestAP.getItemFlow().setCreated(null);
                    essayTestAP.getItemFlow().setFlow(selectedFlow);
                    essayTestAP.getItemFlow().setSent(Boolean.FALSE);
                    itemFlows.add(essayTestAP.getItemFlow());
                }
                analyseResultApService.takeAcceptationDecisionAndSaveAnalyseApResults(itemFlows, flowDatas, analyseResultApList,
                        testResultApList);
                analyseResultApList = null;
                testResultApList = null;
            } else {
                final List<ItemFlow> itemFlowsToAdd = new ArrayList<>();

                for (final FileItem fileItem : currentFile.getFileItemsList()) {
                    final ItemFlow itemFlow = new ItemFlow();

                    itemFlow.setCreated(null);
                    itemFlow.setFileItem(fileItem);
                    itemFlow.setFlow(selectedFlow);
                    itemFlow.setSender(getLoggedUser());
                    itemFlow.setSent(Boolean.FALSE);
                    itemFlow.setUnread(Boolean.TRUE);
                    itemFlow.setReceived(AperakType.APERAK_D.getCharCode());
                    itemFlowsToAdd.add(itemFlow);
                }

                if (Arrays.asList(FlowCode.FL_AP_160.name(), FlowCode.FL_AP_161.name(), FlowCode.FL_AP_162.name(),
                        FlowCode.FL_AP_163.name(), FlowCode.FL_AP_164.name(), FlowCode.FL_AP_165.name(), FlowCode.FL_AP_167.name(),
                        FlowCode.FL_AP_193.name(), FlowCode.FL_AP_194.name())
                        .contains(selectedFlow.getCode())) {
                    commonService.takeDacisionAndSavePayment(itemFlowsToAdd, paymentData);
                } else {
                    itemFlowService.takeDecision(itemFlowsToAdd, flowDatas);
                }

            }
            decisionDiv.getChildren().clear();
            resetDataGridInofrmationProducts();

            if (vtTypeSelectionViewable) {
                FileFieldValue fileFieldValue = new FileFieldValue();
                fileFieldValue.setFile(currentFile);
                fileFieldValue.setFileField(vtTypeFileField);
                fileFieldValue.setValue(minepdedVtType);
                fileFieldValueService.save(fileFieldValue);
                vtTypeSelectionViewable = false;
                minepdedVtType = null;
            }

            // update the last decision date on file
            currentFile.setLastDecisionDate(java.util.Calendar.getInstance().getTime());
            fileService.update(currentFile);

            transactionManager.commit(status);
            if (LOG.isDebugEnabled()) {
                LOG.info("####SEND DECISION Transaction commited####");
            }
        } catch (Exception ex) {
            transactionManager.rollback(status);
            LOG.error("####SEND DECISION Transaction rollbacked#### " + ex.getMessage(), ex);
        }
    }

    /**
     * Mark file.
     */
    public void dispatchFile() {
        List<ItemFlowData> flowDatas;

        flowDatas = new ArrayList<>();
        for (final DataType dataType : selectedFlow.getDataTypeList()) {

            if (BooleanUtils.toBoolean(dataType.getDisabled())) {
                continue;
            }

            final ItemFlowData itemFlowData = new ItemFlowData();
            itemFlowData.setDataType(dataType);

            if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXT.getCode())) {
                final HtmlInputText valueDataType = (HtmlInputText) dipatchDiv.findComponent(ID_DISPATCH_LABEL + dataType.getId());
                itemFlowData.setValue(valueDataType.getValue().toString());
            } else if (dataType.getType().equals(DataTypeEnnumeration.CHEKBOX.getCode())) {
                final HtmlSelectBooleanCheckbox valueDataType = (HtmlSelectBooleanCheckbox) dipatchDiv
                        .findComponent(ID_DISPATCH_LABEL + dataType.getId());
                itemFlowData.setValue(valueDataType.getValue().toString());

            } else if (dataType.getType().equals(DataTypeEnnumeration.CALENDAR.getCode())) {
                final Calendar valueDataType = (Calendar) dipatchDiv.findComponent(ID_DISPATCH_LABEL + dataType.getId());
                itemFlowData.setValue(DateUtils.formatSimpleDateFromObject(DateUtils.FRENCH_DATE, valueDataType.getValue()));

            } else if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXTAREA.getCode())) {
                final HtmlInputTextarea valueDataType = (HtmlInputTextarea) dipatchDiv.findComponent(ID_DISPATCH_LABEL
                        + dataType.getId());
                itemFlowData.setValue(valueDataType.getValue().toString());
            }
            flowDatas.add(itemFlowData);
        }

        final List<ItemFlow> itemFlowsToAdd = new ArrayList<>();
        for (final FileItem fileItem : currentFile.getFileItemsList()) {
            final ItemFlow itemFlow = new ItemFlow();

            fileItem.setDraft(true);
            itemFlow.setFileItem(fileItem);

            itemFlow.setCreated(null);
            itemFlow.setFlow(selectedFlow);
            itemFlow.setSender(getLoggedUser());
            itemFlow.setSent(Boolean.FALSE);
            itemFlow.setUnread(Boolean.TRUE);
            itemFlow.setReceived(AperakType.APERAK_D.getCharCode());
            itemFlowsToAdd.add(itemFlow);

        }
        //        if (FlowCode.FL_AP_155.name().equals(selectedFlow.getCode()))
        //        {
        //            commonService.takeDacisionAndSavePayment(itemFlowsToAdd, paymentData);
        //        }
        //        else
        //        {
        itemFlowService.takeDecision(itemFlowsToAdd, flowDatas);
        //        }

        if (assignedUserForCotation != null) {
            currentFile.setAssignedUser(assignedUserForCotation);
            //check if the actual office is part of the offices behind the assigned user
            List<FileAdministration> fadList = currentFile.getFileAdministrationsList();
            if (fadList != null && !fadList.isEmpty()) {
                List<Administration> candidates = new ArrayList<>();
                for (FileAdministration fa : fadList) {
                    candidates.add(fa.getAdministration());
                }
                List<Bureau> offices = SiatUtils.findCombinedBureausByAdministrationList(Arrays.asList(assignedUserForCotation.getAdministration()));
                List<Bureau> candidatesOffices = SiatUtils.findCombinedBureausByAdministrationList(candidates);
                //remove offices behind the assigned user which are not part on the candidates offiches behind the currentFile
                List<Bureau> filteredOffices = new ArrayList<>();
                for (Bureau o : offices) {
                    if (candidatesOffices.contains(o)) {
                        filteredOffices.add(o);
                    }
                }
                if (!filteredOffices.isEmpty() && !filteredOffices.contains(currentFile.getBureau())) {
                    //we suppose that the file has been coted to a user outside of the current office of the file, so it's an reassignment
                    currentFile.setBureau(filteredOffices.get(0));
                }
            }
            fileService.update(currentFile);
        }

        resetDataGridInofrmationProducts();
    }

    /**
     * Prepare dispatch file.
     */
    public void prepareDispatchFile() {
        String stringId = null;
        dipatchDiv.getChildren().clear();
        assignedUserForCotation = null;

        selectedFlow = flowService.findFlowByCurrentStep(currentFile.getFileItemsList().get(0).getStep());

        userListToAffectedFileCotation = userAuthorityFileTypeService.findUserByFileTypeAndStepAuthorities(
                currentFile.getFileType(), selectedFlow.getToStep(), currentFile, getLoggedUser());
        Set<User> users = new HashSet<>();
        for (User user : userListToAffectedFileCotation) {
            users.add(user);
        }
        userListToAffectedFileCotation = new ArrayList<>(users);

        for (final DataType dataType : selectedFlow.getDataTypeList()) {

            if (BooleanUtils.toBoolean(dataType.getDisabled())) {
                continue;
            }

            final FacesContext context = FacesContext.getCurrentInstance();
            if (dataType.getId() != null) {
                stringId = String.valueOf(dataType.getId());
            }

            final HtmlPanelGroup htmlPanelGroup = (HtmlPanelGroup) context.getApplication().createComponent(
                    HtmlPanelGroup.COMPONENT_TYPE);

            final HtmlOutputText labelOutput = (HtmlOutputText) context.getApplication().createComponent(
                    HtmlOutputText.COMPONENT_TYPE);
            if (FacesContext.getCurrentInstance().getViewRoot().getLocale().equals(Locale.FRENCH)) {
                labelOutput.setValue(dataType.getLabel());
            } else {
                labelOutput.setValue(dataType.getLabelEn());
            }
            htmlPanelGroup.getChildren().add(labelOutput);

            if (dataType.getRequired()) {
                final HtmlOutputText labelOutputRequired = (HtmlOutputText) context.getApplication().createComponent(
                        HtmlOutputText.COMPONENT_TYPE);
                labelOutputRequired.setValue("*");
                labelOutputRequired.setStyleClass(STYLE_CLASS);
                htmlPanelGroup.getChildren().add(labelOutputRequired);
            }

            dipatchDiv.getChildren().add(htmlPanelGroup);

            if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXT.getCode())) {
                final HtmlInputText inputText = (HtmlInputText) context.getApplication()
                        .createComponent(HtmlInputText.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    inputText.setRequired(true);
                }
                inputText.setId(ID_DISPATCH_LABEL + stringId);
                inputText.setLabel(dataType.getLabel());
                dipatchDiv.getChildren().add(inputText);
            } else if (dataType.getType().equals(DataTypeEnnumeration.CHEKBOX.getCode())) {
                final HtmlSelectBooleanCheckbox booleanCheckbox = (HtmlSelectBooleanCheckbox) context.getApplication()
                        .createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    booleanCheckbox.setRequired(true);
                }
                booleanCheckbox.setId(ID_DISPATCH_LABEL + stringId);
                booleanCheckbox.setLabel(dataType.getLabel());
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
                calendar.setLabel(dataType.getLabel());
                dipatchDiv.getChildren().add(calendar);

            } else if (dataType.getType().equals(DataTypeEnnumeration.INPUTTEXTAREA.getCode())) {
                final HtmlInputTextarea inputTextarea = (HtmlInputTextarea) context.getApplication().createComponent(
                        HtmlInputTextarea.COMPONENT_TYPE);
                if (dataType.getRequired()) {
                    inputTextarea.setRequired(true);
                }
                inputTextarea.setRows(10);
                inputTextarea.setId(ID_DISPATCH_LABEL + stringId);
                inputTextarea.setLabel(dataType.getLabel());
                dipatchDiv.getChildren().add(inputTextarea);
            }
        }
    }

    /**
     * Send decisions.
     *
     */
    @SuppressWarnings("unused")
    public synchronized void sendDecisions() {
        LOG.info("--------------------------------------------- Prise d'une décision AP - DEBUT ----------------------------------------------");
        LOG.info(currentFile.toString());
        final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            // Cotation case
            if (currentFile.getFileItemsList() != null && cotationAllowed != null && cotationAllowed && ((decisionAtCotationLevel != null && !decisionAtCotationLevel) || decisionAtCotationLevel == null)) {

                itemFlowService.sendDecisionsToDispatchFile(currentFile);

                JsfUtil.addSuccessMessageAfterRedirect(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                        getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.SEND_SUCCESS));
                goToDetailPage();
            } // Decision case
            else if (decisionAllowed || (comeFromRetrieveAp != null && comeFromRetrieveAp) || (decisionAtCotationLevel != null && decisionAtCotationLevel)) {

                if (currentFile.getFileItemsList() != null && !currentFile.getFileItemsList().isEmpty()) {
                    final Map<FileItem, Flow> map = itemFlowService.sendDecisions(currentFile.getFileItemsList().get(0).getFile(),
                            currentFile.getFileItemsList());
                    final List<FileItem> fileItemList = new ArrayList<>(map.keySet());
                    final List<ItemFlow> itemFlowList = itemFlowService.findLastItemFlowsByFileItemList(fileItemList);

                    ItemFlowData itemFlowData = null;
                    if (CollectionUtils.isNotEmpty(itemFlowList)
                            && CollectionUtils.isNotEmpty(itemFlowList.get(0).getItemFlowsDataList())) {
                        itemFlowData = itemFlowList.get(0).getItemFlowsDataList().get(0);
                    }
                    if (!map.isEmpty()) {
                        final Flow executedFlow = map.values().iterator().next();
                        String service = StringUtils.EMPTY;
                        String documentType = StringUtils.EMPTY;
                        final Flow flowToSend = map.get(fileItemList.get(0));
                        //generate report
                        Map<String, byte[]> attachedByteFiles = null;
//                        try {
                        String reportNumber;
                        if (FlowCode.FL_AP_107.name().equals(flowToSend.getCode()) || FlowCode.FL_AP_169.name().equals(flowToSend.getCode()) || FlowCode.FL_AP_202.name().equals(flowToSend.getCode())) {
                            ItemFlow decisionFlow = null;
                            if (currentFile.getFileType().getCode().equals(FileTypeCode.VTP_MINSANTE)) {
                                decisionFlow = itemFlowService.findItemFlowByFileItemAndFlow(fileItemList.get(0), FlowCode.FL_AP_103);
                                if (decisionFlow == null) {
                                    decisionFlow = itemFlowService.findItemFlowByFileItemAndFlow(fileItemList.get(0), FlowCode.FL_AP_169);
                                }
                                if (decisionFlow == null) {
                                    decisionFlow = itemFlowService.findItemFlowByFileItemAndFlow(fileItemList.get(0), FlowCode.FL_AP_170);
                                }
                                if (decisionFlow == null) {
                                    decisionFlow = itemFlowService.findItemFlowByFileItemAndFlow(fileItemList.get(0), FlowCode.FL_AP_171);
                                }
                                if (decisionFlow == null) {
                                    decisionFlow = itemFlowService.findItemFlowByFileItemAndFlow(fileItemList.get(0), FlowCode.FL_AP_172);
                                }
                                if (decisionFlow == null) {
                                    decisionFlow = itemFlowService.findItemFlowByFileItemAndFlow(fileItemList.get(0), FlowCode.FL_AP_173);
                                }
                            } else if (currentFile.getFileType().getCode().equals(FileTypeCode.VTD_MINSANTE)) {
                                decisionFlow = itemFlowService.findItemFlowByFileItemAndFlow(fileItemList.get(0), FlowCode.FL_AP_105);
                                if (decisionFlow == null) {
                                    decisionFlow = itemFlowService.findItemFlowByFileItemAndFlow(fileItemList.get(0), FlowCode.FL_AP_169);
                                }
                                if (decisionFlow == null) {
                                    decisionFlow = itemFlowService.findItemFlowByFileItemAndFlow(fileItemList.get(0), FlowCode.FL_AP_170);
                                }
                            } else if (isBsbeMinfofFileType()) {
                                decisionFlow = itemFlowService.findItemFlowByFileItemAndFlow(fileItemList.get(0), FlowCode.FL_AP_106);
                            }
                            if (decisionFlow != null) {
                                // Persist SIGNATAIRE_DATE
                                FileField signataireDateFileField = fileFieldService.findFileFieldByCodeAndFileType(SIGNATAIRE_DATE_FIELD_NAME, currentFile.getFileType().getCode());
                                if (signataireDateFileField != null && decisionFlow.getCreated() != null) {
                                    FileFieldValue signataireDateFieldValue = fileFieldValueService.findValueByFileFieldAndFile(signataireDateFileField.getCode(), currentFile);
                                    if (signataireDateFieldValue != null) {
                                        signataireDateFieldValue.setValue(new SimpleDateFormat("dd/MM/yyyy").format(decisionFlow.getCreated()));
                                        fileFieldValueService.update(signataireDateFieldValue);
                                    } else {
                                        signataireDateFieldValue = new FileFieldValue();
                                        signataireDateFieldValue.setFile(currentFile);
                                        signataireDateFieldValue.setFileField(signataireDateFileField);
                                        signataireDateFieldValue.setValue(new SimpleDateFormat("dd/MM/yyyy").format(decisionFlow.getCreated()));
                                        currentFile.getFileFieldValueList().add(signataireDateFieldValue);
                                        fileFieldValueService.save(signataireDateFieldValue);
                                    }
                                }
                                final List<ItemFlowData> itemFlowDataList = decisionFlow.getItemFlowsDataList();
                                for (final ItemFlowData ifd : itemFlowDataList) {
                                    if (ifd.getDataType().getLabel().equalsIgnoreCase("Date validité")) {
                                        final FileField dateValidityField = fileFieldService.findFileFieldByCodeAndFileType(
                                                VALIDITY_DATE_FIELD_NAME, currentFile.getFileType().getCode());
                                        if (dateValidityField != null) {
                                            FileFieldValue dateValidityFieldValue = fileFieldValueService.findValueByFileFieldAndFile(dateValidityField.getCode(), currentFile);
                                            if (dateValidityFieldValue != null) {
                                                dateValidityFieldValue.setValue(ifd.getValue());
                                                fileFieldValueService.update(dateValidityFieldValue);
                                            } else {
                                                dateValidityFieldValue = new FileFieldValue();
                                                dateValidityFieldValue.setFile(currentFile);
                                                dateValidityFieldValue.setFileField(dateValidityField);
                                                dateValidityFieldValue.setValue(ifd.getValue());
                                                currentFile.getFileFieldValueList().add(dateValidityFieldValue);
                                                fileFieldValueService.save(dateValidityFieldValue);
                                            }
                                        }
                                        break;
                                    }
                                    if (isBsbeMinfofFileType() && ifd.getDataType().getLabel().equalsIgnoreCase("Numéro Enregistrement BSB")) {
                                        final FileField registrationNumberField = fileFieldService.findFileFieldByCodeAndFileType(
                                                "BULLETIN_SPECIFICATION_NUMERO_ENREGISTREMENT", currentFile.getFileType().getCode());
                                        final FileField registrationDateField = fileFieldService.findFileFieldByCodeAndFileType(
                                                "BULLETIN_SPECIFICATION_DATE", currentFile.getFileType().getCode());
                                        if (registrationNumberField != null) {
                                            FileFieldValue registrationNumberFieldValue = fileFieldValueService.findValueByFileFieldAndFile(registrationNumberField.getCode(), currentFile);
                                            if (registrationNumberFieldValue != null) {
                                                registrationNumberFieldValue.setValue(ifd.getValue());
                                                fileFieldValueService.update(registrationNumberFieldValue);
                                            } else {
                                                registrationNumberFieldValue = new FileFieldValue();
                                                registrationNumberFieldValue.setFile(currentFile);
                                                registrationNumberFieldValue.setFileField(registrationNumberField);
                                                registrationNumberFieldValue.setValue(ifd.getValue());
                                                currentFile.getFileFieldValueList().add(registrationNumberFieldValue);
                                                fileFieldValueService.save(registrationNumberFieldValue);
                                            }
                                        }
                                        if (registrationDateField != null) {
                                            FileFieldValue registrationDateFieldValue = fileFieldValueService.findValueByFileFieldAndFile(registrationDateField.getCode(), currentFile);
                                            if (registrationDateFieldValue != null) {
                                                registrationDateFieldValue.setValue(SIMPLE_DATE_FORMAT.format(java.util.Calendar.getInstance().getTime()));
                                                fileFieldValueService.update(registrationDateFieldValue);
                                            } else {
                                                registrationDateFieldValue = new FileFieldValue();
                                                registrationDateFieldValue.setFile(currentFile);
                                                registrationDateFieldValue.setFileField(registrationDateField);
                                                registrationDateFieldValue.setValue(SIMPLE_DATE_FORMAT.format(java.util.Calendar.getInstance().getTime()));
                                                currentFile.getFileFieldValueList().add(registrationDateFieldValue);
                                                fileFieldValueService.save(registrationDateFieldValue);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            // edit signature elements
                            Date now = java.util.Calendar.getInstance().getTime();
                            currentFile.setSignatureDate(now);
                            currentFile.setSignatory(getLoggedUser());
                            attachedByteFiles = new HashMap<>();

                            final List<FileTypeFlowReport> fileTypeFlowReports = new ArrayList<>();

                            //final List<FileTypeFlowReport> fileTypeFlowReportsList = flowToSend.getFileTypeFlowReportsList();
                            List<FileTypeFlowReport> fileTypeFlowReportsList = fileTypeFlowReportService.findReportClassNameByFlowAndFileType(flowToSend, currentFile.getFileType());
                            if (fileTypeFlowReportsList != null) {

                                for (final FileTypeFlowReport fileTypeFlowReport : fileTypeFlowReportsList) {
                                    if (currentFile.getFileType().equals(fileTypeFlowReport.getFileType())) {
                                        fileTypeFlowReports.add(fileTypeFlowReport);
                                    }
                                }
                            }
                            for (final FileTypeFlowReport fileTypeFlowReport : fileTypeFlowReports) {

                                //Begin Add new field value with report Number
                                final ReportOrganism reportOrganism = reportOrganismService
                                        .findReportByFileTypeFlowReport(fileTypeFlowReport);
                                final FileField reportField = fileFieldService.findFileFieldByCodeAndFileType(
                                        fileTypeFlowReport.getFileFieldName(), fileTypeFlowReport.getFileType().getCode());
                                reportNumber = (reportOrganism.getSequence() + 1)
                                        + (reportOrganism.getValue() != null ? reportOrganism.getValue() : StringUtils.EMPTY);
                                if (reportField != null) {
                                    FileFieldValue reportFieldValue = fileFieldValueService.findValueByFileFieldAndFile(reportField.getCode(), currentFile);
                                    if (reportFieldValue == null) {
                                        reportFieldValue = new FileFieldValue();
                                        reportFieldValue.setFile(currentFile);
                                        reportFieldValue.setFileField(reportField);
                                        reportFieldValue.setValue(reportNumber);
                                        currentFile.getFileFieldValueList().add(reportFieldValue);
                                        fileFieldValueService.save(reportFieldValue);
                                    }
                                }
                                //End Add new field value with report Number
                                byte[] report;

                                if (aiMinmidtFileType) {
                                    Attachment finalAttachment = findAuthorizationAttachment();
                                    if (finalAttachment == null) {
                                        if (fileToSave != null) {
                                            JsfUtil.addErrorMessage("Le rapport n'a pu être enregistré, mais sera envoyé");
                                            report = FileUtils.readFileToByteArray(fileToSave);
                                        } else {
                                            JsfUtil.addErrorMessage("Le rapport n'a pu être enregistré");
                                            return;
                                        }
                                    } else {
                                        report = getBytesFromAttachment(finalAttachment);
                                    }
                                } else {
                                    final String nomClasse = fileTypeFlowReport.getReportClassName();
                                    @SuppressWarnings("rawtypes")
                                    final Class classe = Class.forName(nomClasse);
                                    if (bsbeMinfofFileType) {
                                        Constructor c1 = classe.getConstructor(File.class, List.class, String.class);
                                        report = JsfUtil.getReport((AbstractReportInvoker) c1.newInstance(currentFile, specsList, woodsType == null ? "GRUMES" : woodsType.getValue()));
                                    } else {
                                        report = ReportGeneratorUtils.generateReportBytes(fileFieldValueService, classe, currentFile);
                                    }
                                }
                                attachedByteFiles.put(fileTypeFlowReport.getReportName(), report);
                                List<Attachment> filesToSend = findAttachmentsToSend(currentFile.getFileType().getCode());
                                for (Attachment att : (List<Attachment>) safe(filesToSend)) {
                                    attachedByteFiles.put(att.getDocumentName(), getBytesFromAttachment(att));
                                }
                                String targetAttachmentName = new StringBuilder().append(currentFile.getNumeroDossier())
                                        .append("-").append(currentFile.getReferenceSiat()).append("-")
                                        .append(fileTypeFlowReport.getReportName()).toString();
                                java.io.File folder = new java.io.File(applicationPropretiesService.getAttachementFolder());
                                if (!folder.exists()) {
                                    folder.mkdirs();
                                }
                                final java.io.File targetAttachment = new java.io.File(folder, targetAttachmentName);

                                try (FileOutputStream fileOuputStream = new FileOutputStream(targetAttachment)) {
                                    fileOuputStream.write(report);
                                }

                                //Update report sequence
                                reportOrganism.setSequence(reportOrganism.getSequence() + 1);
                                reportOrganismService.update(reportOrganism);
//                                final Map<String, Date> dateParams = new HashMap<>();
//                                dateParams.put("SIGNATURE_DATE", java.util.Calendar.getInstance().getTime());
//                                fileService.updateSpecificColumn(dateParams, currentFile);
                                fileService.update(currentFile);
                            }
                        } else if (FlowCode.FL_AP_202.name().equals(flowToSend.getCode())) {
                            FileMarshall fileMarshall = fileMarshallServce.findByFile(currentFile);
                            if (fileMarshall != null) {
                                Serializable object = (Serializable) SerializationUtils.deserialize(fileMarshall.getMarshall());
                                File previousFile;
                                switch (currentFile.getFileType().getCode()) {
                                    case BSBE_MINFOF:
                                        previousFile = xmlConverterService.convertDocumentToFile(object);
                                        break;
                                    default:
                                        previousFile = null;
                                }
                                if (previousFile != null) {
                                    xmlConverterService.rollbackFile(currentFile, previousFile);
                                }
                            }
                        }
//                        } catch (final Exception e) {
//                            LOG.error("Error occured when loading report: " + e.getMessage(), e);
//                            attachedByteFiles = null;
//                        }

                        // convert file to document
                        final Serializable documentSerializable = xmlConverterService.convertFileToDocument(productInfoItems.get(0)
                                .getFile(), fileItemList, itemFlowList, executedFlow);

                        // prepare document to send
                        byte[] xmlBytes;
                        ByteArrayOutputStream output = SendDocumentUtils.prepareApDocument(documentSerializable, service, documentType);
                        xmlBytes = output.toByteArray();

                        if (CollectionUtils.isNotEmpty(flowToSend.getCopyRecipientsList())) {
                            final List<CopyRecipient> copyRecipients = flowToSend.getCopyRecipientsList();
                            for (final CopyRecipient copyRecipient : copyRecipients) {
                                LOG.info("SEND COPY RECIPIENT TO {}", copyRecipient.getToAuthority().getRole());
                                send(status, xmlBytes, attachedByteFiles, service, documentType, copyRecipient.getToAuthority().getRole(), itemFlowList);

                            }
                        } else {
                            send(status, xmlBytes, attachedByteFiles, service, documentType, currentFile.getEmetteur(), itemFlowList);
                        }

                    }
                    JsfUtil.addSuccessMessageAfterRedirect(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                            getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.SEND_SUCCESS));

                    decisionDiv.getChildren().clear();
                    goToDetailPage();

                }
            } else {
                final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.SEND_ERROR);
                JsfUtil.addErrorMessage(msg);
            }
            TransactionStatus tsCommit = status;
            status = null;
            transactionManager.commit(tsCommit);
            if (LOG.isDebugEnabled()) {
                LOG.info("####SEND DECISION Transaction commited####");
            }

            final File file = fileService.find(currentFile.getId());
            final Step currentStep = file.getFileItemsList().get(0).getStep();
            notificationEmail(currentFile, currentStep);
        } catch (Exception e) {
            LOG.error(currentFile.toString(), e);
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.SEND_ERROR, null);
        } finally {
            if (status != null) {
                transactionManager.rollback(status);
                if (LOG.isDebugEnabled()) {
                    LOG.info("####SEND DECISION Transaction rollbacked####");
                }
            }
        }
        LOG.info("--------------------------------------------- Prise d'une décision AP - FIN ----------------------------------------------");
    }

    private void send(TransactionStatus transactionStatus, byte[] xmlBytes, Map<String, byte[]> attachedByteFiles, String service, String documentType, String toPartyId, List<ItemFlow> itemFlowList) {
        Map<String, Object> data = new HashMap<>();
        data.put(ESBConstants.FLOW, xmlBytes);
        data.put(ESBConstants.ATTACHMENT, attachedByteFiles);
        data.put(ESBConstants.TYPE_DOCUMENT, documentType);
        data.put(ESBConstants.SERVICE, service);
        data.put(ESBConstants.MESSAGE, null);
        data.put(ESBConstants.EBXML_TYPE, "STANDARD");
        data.put(ESBConstants.TO_PARTY_ID, toPartyId);
        data.put(ESBConstants.DEAD, "0");
        //
        data.put(ESBConstants.ITEM_FLOWS, itemFlowList);
        if (!fileProducer.sendFile(data)) {
            if (transactionStatus != null) {
                //transactionManager.rollback(transactionStatus);
            }
            LOG.warn("cannot send the décision : " + currentFile.getNumeroDossier());
            messageToSendService.saveOrUpadateNotSendedMessageAsMessageToResend(data);
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.SEND_ERROR, null);
            return;
        } else {
            messageToSendService.deleteNotSendedMessageIfExistsAsMessageToResend(data);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Message sent to OUT queue");
        }
    }

    /**
     * Go to detail page.
     */
    public void goToDetailPage() {
        try {
            setIndexPageUrl(ControllerConstants.Pages.FO.DASHBOARD_AP_INDEX_PAGE);
            final FacesContext context = FacesContext.getCurrentInstance();
            final ExternalContext extContext = context.getExternalContext();

            final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
                    .getActionURL(context, indexPageUrl));

            extContext.redirect(url);
        } catch (final IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Prepare roll back decisions.
     */
    public void prepareRollBackDecisions() {
        if (currentFile.getFileItemsList() == null || currentFile.getFileItemsList().isEmpty() && cotationAllowed == null) {
            final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    ControllerConstants.Bundle.Messages.CHOOSE_DECISION_ERROR);
            JsfUtil.addErrorMessage(msg);
        } else if (currentFile.getFileItemsList() == null || currentFile.getFileItemsList().isEmpty() && cotationAllowed != null
                && cotationAllowed) {
            final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    ControllerConstants.Bundle.Messages.DISPATCH_ERROR);
            JsfUtil.addErrorMessage(msg);
        } else {
            final RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('" + CONFIRMATION_DIALOG + "').show()");
        }
    }

    /**
     * Annuler decisions.
     */
    public synchronized void annulerDecisions() {
        final List<Long> fileItemListIds = new ArrayList<>();
        for (final FileItem fileItem : currentFile.getFileItemsList()) {
            fileItemListIds.add(fileItem.getId());
        }
        final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        final TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        try {
            // Decision Case
            if (cotationAllowed != null && !cotationAllowed) {
                commonService.rollbackDecision(fileItemListIds);

                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.ROLL_BACK_SUCCESS));

                decisionDiv.getChildren().clear();
            } // Cotation Case
            else {
                itemFlowService.rollBackDecisionForDispatchFile(fileItemListIds);

                // If the las decision is a cotation flow then rollback the assigned user to the current user
                if (lastDecisions.getFlow().getIsCota()) {
                    currentFile.setAssignedUser(getLoggedUser());
                } else {
                    currentFile.setAssignedUser(null);
                }

                assignedUserForCotation = null;
                RequestContext.getCurrentInstance().update(":dispatchDialog:dispatch_affected_user");

                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.ROLL_BACK_SUCCESS));
            }

            // update the last decision date on file
            currentFile.setLastDecisionDate(currentFile.getFileItemsList().get(0).getItemFlowsList().get(0).getCreated());
            fileService.update(currentFile);

            transactionManager.commit(transactionStatus);
            if (LOG.isDebugEnabled()) {
                LOG.info("####SAVE DECISION Transaction commited####");
            }
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                    .getString(ControllerConstants.Bundle.Messages.ROLL_BACK_FAIL));
            try {
                transactionManager.rollback(transactionStatus);
            } catch (final Exception ex1) {
                LOG.error(Objects.toString(ex1), ex1);
            }
        }
        productInfoItems = disableDraftFromProductInfoItem(productInfoItems);
        resetDataGridInofrmationProducts();

    }

    /**
     * Disable draft from product info item.
     *
     * @param infoItems the info items
     * @return the list
     */
    private List<FileItem> disableDraftFromProductInfoItem(final List<FileItem> infoItems) {
        final List<FileItem> returnedInfoItems = new ArrayList<FileItem>();
        for (final FileItem fileItem : infoItems) {
            if (fileItem.getDraft() != null && fileItem.getDraft()) {
                fileItem.setDraft(false);
            }
            returnedInfoItems.add(fileItem);
        }
        return returnedInfoItems;
    }

    /**
     * Reset data grid inofrmation products.
     */
    public void resetDataGridInofrmationProducts() {
        selectedFlow = null;
        currentFile.setFileItemsList(null);
        setProductInfoItemsEnabled(null);
        FileTypeStep fileTypeStep = null;
        if (selectedFileItem != null) {
            fileTypeStep = fileTypeStepService.findFileTypeStepByFileTypeAndStep(selectedFileItem.getFile().getFileType(),
                    selectedFileItem.getStep());

        }
        productInfoItems = initFileItemApDetails();
        if (fileTypeStep != null && fileTypeStep.getLabelFr() != null) {
            for (final FileItem fileItem : productInfoItems) {
                fileItem.setRedefinedLabelEn((fileTypeStep.getLabelEn()));
                fileItem.setRedefinedLabelFr((fileTypeStep.getLabelFr()));
            }
        }

        if (hasDraftFileItemInList(productInfoItems)) {
            rollBackDecisionsAllowed = true;
            sendDecisionAllowed = true;
            fileItemHasDraft = true;
        }
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
     * Prepare create recommandation.
     */
    public void prepareCreateRecommandation() {
        selectedRecommandation = new Recommandation();

        authorityList = fileTypeService.findAuthoritiesByFileType(currentFile.getFileType());
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
        recommandationList = recommandationService.findRecommandationByFileAndAuthorties(currentFile, (new ArrayList<>(
                getLoggedUser().getAuthorities())));
    }

    /**
     * Save recommandation.
     */
    public void saveRecommandation() {
        try {
            if (selectedRecommandation != null && selectedRecommandation.getValue().trim().length() > 0) {
                selectedRecommandation.setFile(this.getCurrentFile());
                selectedRecommandation.setFileItem(null);
                selectedRecommandation.setSupervisor(this.getLoggedUser());
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
                final String errorMsg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.CREATE_RECOMMANDATION_REQUIRED_MESSAGE);
                JsfUtil.addErrorMessage(errorMsg);
            }
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                    .getString(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED));
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
                final String errorMsg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.EDIT_RECOMMANDATION_REQUIRED_MESSAGE);
                JsfUtil.addErrorMessage(errorMsg);
            }
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                    .getString(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED));
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
        } catch (final Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                    .getString(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED));
        }
    }

    /**
     * Generate report.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void generateReport() throws IOException {
        JsfUtil.generateReport(new CpMinepdedExporter(currentFile));
    }

    /**
     * Prepare create recommandation article.
     */
    public void prepareCreateRecommandationArticle() {
        selectedRecommandationArticle = new Recommandation();

        authorityList = fileTypeService.findAuthoritiesByFileType(currentFile.getFileType());
        authoritiesList = new DualListModel<>(new ArrayList<Authority>(), new ArrayList<Authority>());
        authoritiesList.getSource().addAll(authorityList);
    }

    /**
     * Prepare edit recommandation article.
     */
    public void prepareEditRecommandationArticle() {
        this.setSelectedRecommandationArticle(recommandationService.find(this.getSelectedRecommandationArticle().getId()));

        authorityList = fileTypeService.findAuthoritiesByFileType(currentFile.getFileType());

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
        recommandationArticleList = recommandationService.findRecommandationByFileItemAndAuthorties(selectedFileItem,
                (new ArrayList<>(getLoggedUser().getAuthorities())));
    }

    /**
     * Save recommandation article.
     */
    public void saveRecommandationArticle() {
        try {
            if (selectedRecommandationArticle != null && selectedRecommandationArticle.getValue().trim().length() > 0) {

                selectedRecommandationArticle.setFile(null);
                selectedRecommandationArticle.setFileItem(selectedFileItem);
                selectedRecommandationArticle.setSupervisor(getLoggedUser());
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
                    selectedRecommandationArticle = null;
                    refreshRecommandationArticleList();
                }
            } else {
                final String errorMsg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.CREATE_RECOMMANDATION_REQUIRED_MESSAGE);
                JsfUtil.addErrorMessage(errorMsg);
            }
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                    .getString(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED));
        }
    }

    /**
     * Edits the recommandation article.
     */
    public void editRecommandationArticle() {
        try {
            if (selectedRecommandationArticle != null && selectedRecommandationArticle.getValue().trim().length() > 0) {
                selectedRecommandationArticle.setCreated(java.util.Calendar.getInstance().getTime());

                recommandationService.update(selectedRecommandationArticle);

                final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(Recommandation.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
                JsfUtil.addSuccessMessage(msg);

                selectedRecommandationArticle = null;
                refreshRecommandationArticleList();
            } else {
                final String errorMsg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.EDIT_RECOMMANDATION_REQUIRED_MESSAGE);
                JsfUtil.addErrorMessage(errorMsg);
            }
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                    .getString(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED));
        }
    }

    /**
     * Delete recommandation article.
     */
    public void deleteRecommandationArticle() {
        try {
            recommandationService.delete(selectedRecommandationArticle);

            final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    Recommandation.class.getSimpleName() + PersistenceActions.DELETE.getCode());
            JsfUtil.addSuccessMessage(msg);

            if (!JsfUtil.isValidationFailed()) {
                selectedRecommandationArticle = null;
                refreshRecommandationArticleList();
            }
            this.setSelectedRecommandationArticle(null);
            refreshRecommandationArticleList();
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                    .getString(ControllerConstants.Bundle.Messages.PERSISTENCE_ERROR_OCCURED));
        }

    }

    /**
     * Check is allowed recommandation.
     */
    private void checkIsAllowedRecommandation() {
        final List<UserAuthorityFileType> userAFTList = userAuthorityFileTypeService
                .findUserAuthorityFileTypeByUserList(getLoggedUser().getMergedDelegatorList());

        for (final UserAuthorityFileType uaft : userAFTList) {
            if (AuthorityConstants.SUPERVISEUR.getCode().equals(uaft.getUserAuthority().getAuthorityGranted().getRole())
                    && currentFile.getFileType().equals(uaft.getFileType())) {
                allowedUser = Boolean.TRUE;
                break;
            }
        }
    }

    /**
     * Check is allowed user for current stept.
     */
    public void checkIsAllowedUserForCurrentStep() {
        final List<String> authorityTypes = getLoggedUser().getAuthoritiesList();
        Boolean authorizeUser = Boolean.FALSE;
        for (final Authority stepAut : selectedFileItem.getStep().getRoleList()) {
            User assignedUser;
            if (selectedFileItem != null) {
                assignedUser = selectedFileItem.getFile().getAssignedUser();
            } else {
                assignedUser = currentFile.getAssignedUser();
            }
            final boolean assignedUserAuthorized = assignedUser == null
                    || (assignedUser != null && assignedUser.getId().equals(getLoggedUser().getId()));
            if (authorityTypes.contains(stepAut.getRole()) && assignedUserAuthorized) {
                authorizeUser = Boolean.TRUE;

                break;
            }
        }
        if (!authorizeUser) {
            decisionAllowed = Boolean.FALSE;
            cotationAllowed = Boolean.FALSE;
        }
    }

    /**
     * Concatenate active index string.
     *
     * @param tabList the tab list
     * @return the string
     */
    public String concatenateActiveIndexString(final List<Tab> tabList) {
        final StringBuilder out = new StringBuilder();
        for (int i = 0; i < tabList.size(); i++) {
            out.append(i);
            if (i + 1 != tabList.size()) {
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
                if (fileItemFieldValue.getFileItemField().getGroup() != null
                        && fieldGroupItem.getId().equals(fileItemFieldValue.getFileItemField().getGroup().getId())) {
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
        if (selectedFileItem != null && StringUtils.isNotBlank(selectedFileItem.getFobValue())) {
            final FileItemField fileItemFieldFob = new FileItemField();
            final String fobLabelFr = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE)
                    .getString("ProductDetailsLabel_fobLabel");
            final String fobLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.ENGLISH)
                    .getString("ProductDetailsLabel_fobLabel");
            fileItemFieldFob.setLabelFr(fobLabelFr);
            fileItemFieldFob.setLabelEn(fobLabelEn);
            fileItemFieldValueFob = new FileItemFieldValue();
            fileItemFieldValueFob.setFileItemField(fileItemFieldFob);
            fileItemFieldValueFob.setValue(selectedFileItem.getFobValue());
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
        if (selectedFileItem != null && StringUtils.isNotBlank(selectedFileItem.getQuantity())) {
            final FileItemField fileItemFieldQtit = new FileItemField();
            final String qtitLabelFr = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE)
                    .getString("ProductDetailsLabel_quantity");
            final String qtitLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.ENGLISH)
                    .getString("ProductDetailsLabel_quantity");
            fileItemFieldQtit.setLabelFr(qtitLabelFr);
            fileItemFieldQtit.setLabelEn(qtitLabelEn);
            fileItemFieldValueQuanity = new FileItemFieldValue();
            fileItemFieldValueQuanity.setFileItemField(fileItemFieldQtit);
            fileItemFieldValueQuanity.setValue(selectedFileItem.getQuantity());
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

        if (selectedFileItem.getNsh() != null) {
            // NSH : GoodsItemDesc
            final String goodsItemDescLabelFr = ResourceBundle
                    .getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("ProductDetailsLabel_item");
            final String goodsItemDescLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                    Locale.ENGLISH).getString("ProductDetailsLabel_item");
            final FileItemField fileItemFieldGoodsItemDesc = new FileItemField();
            fileItemFieldGoodsItemDesc.setLabelFr(goodsItemDescLabelFr);
            fileItemFieldGoodsItemDesc.setLabelEn(goodsItemDescLabelEn);
            final FileItemFieldValue goodsItemDesc = new FileItemFieldValue();
            goodsItemDesc.setValue(getCurrentLocale().equals(Locale.FRANCE) ? selectedFileItem.getNsh().getGoodsItemDesc()
                    : selectedFileItem.getNsh().getGoodsItemDescEn());
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
            goodsItemCode.setValue(selectedFileItem.getNsh().getGoodsItemCode());
            goodsItemCode.setFileItemField(fileItemFieldGoodsItemCode);
            fieldValues.add(0, goodsItemCode);
        }

        if (selectedFileItem.getSubfamily() != null) {
            // Subfamily : label
            final String subfamilyLabelLabelFr = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                    Locale.FRANCE).getString("ProductDetailsLabel_subfamily");
            final String subfamilyLabelLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                    Locale.ENGLISH).getString("ProductDetailsLabel_subfamily");
            final FileItemField fileItemFieldSubfamilyLabel = new FileItemField();
            fileItemFieldSubfamilyLabel.setLabelFr(subfamilyLabelLabelFr);
            fileItemFieldSubfamilyLabel.setLabelEn(subfamilyLabelLabelEn);
            final FileItemFieldValue subfamilyLabel = new FileItemFieldValue();
            subfamilyLabel.setValue(selectedFileItem.getSubfamily().getLabel());
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
            subfamilyCode.setValue(selectedFileItem.getSubfamily().getCode());
            subfamilyCode.setFileItemField(fileItemFieldSubfamilyCode);
            fieldValues.add(0, subfamilyCode);
        }

        fileFieldGroupDto.setFieldValues(fieldValues);

    }

    /**
     * Check generate report allowed.
     */
    private void checkGenerateReportAllowed() {
        generateReportAllowed = false;
        final Flow reportingFlow = flowService.findByToStep(selectedFileItem.getStep(), currentFile.getFileType());
        if (reportingFlow == null) {
            return;
        }
        final List<FileTypeFlowReport> fileTypeFlowReportsList = fileTypeFlowReportService.findReportClassNameByFlowAndFileType(reportingFlow, currentFile.getFileType());
        generateReportAllowed = StepCode.ST_AP_44.equals(selectedFileItem.getStep().getStepCode()) && CollectionUtils.isNotEmpty(fileTypeFlowReportsList);
        if (aiMinmidtFileType) {
            generateReportAllowed = false;
        }
    }

    /**
     * Download report.
     *
     * @return the streamed content
     */
    public StreamedContent downloadReport() {
        List<FileTypeFlowReport> fileTypeFlowReports = new ArrayList<>();

        final Flow reportingFlow = flowService.findByToStep(selectedFileItem.getStep(), currentFile.getFileType());
//        final List<FileTypeFlowReport> fileTypeFlowReportsList = reportingFlow.getFileTypeFlowReportsList();
//
//        if (fileTypeFlowReportsList != null && !aiMinmidtFileType) {
//            for (final FileTypeFlowReport fileTypeFlowReport : fileTypeFlowReportsList) {
//                if (currentFile.getFileType().equals(fileTypeFlowReport.getFileType())) {
//                    fileTypeFlowReports.add(fileTypeFlowReport);
//                }
//            }
//        }
        if (!aiMinmidtFileType) {
            fileTypeFlowReports = fileTypeFlowReportService.findReportClassNameByFlowAndFileType(reportingFlow, currentFile.getFileType());
        }
        if (currentFile.getSignatory() == null) {
            currentFile.setSignatory(getLoggedUser());
        }
        if (currentFile.getSignatureDate() == null) {
            currentFile.setSignatureDate(java.util.Calendar.getInstance().getTime());
        }
        for (final FileTypeFlowReport fileTypeFlowReport : fileTypeFlowReports) {

            //Begin Add new field value with report Number
            try {
                final String nomClasse = fileTypeFlowReport.getReportClassName();
                @SuppressWarnings("rawtypes")
                Class classe = Class.forName(nomClasse);
                @SuppressWarnings({"rawtypes", "unchecked"})
                byte[] report;
                if (bsbeMinfofFileType) {
                    Constructor c1 = classe.getConstructor(File.class, List.class, String.class);
                    report = JsfUtil.getReport((AbstractReportInvoker) c1.newInstance(currentFile, specsList, woodsType == null ? "GRUMES" : woodsType.getValue()));
                } else {
                    report = ReportGeneratorUtils.generateReportBytes(fileFieldValueService, classe, currentFile);
                }
                final InputStream is = new ByteArrayInputStream(report);
                final StreamedContent fileToDownload = new DefaultStreamedContent(is, "application/pdf",
                        currentFile.getReferenceSiat() + '_' + fileTypeFlowReport.getReportName());
                return fileToDownload;
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.GENERATE_REPORT_FAILED);
                JsfUtil.addErrorMessage(msg);
                LOG.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Gets the instance of page fimex detail controller.
     *
     * @return the instance of page fimex detail controller
     */
    public FimexDetailController getInstanceOfPageFimexDetailController() {
        final FacesContext fctx = FacesContext.getCurrentInstance();
        final Application application = fctx.getApplication();
        final ELContext context = fctx.getELContext();
        final ExpressionFactory expressionFactory = application.getExpressionFactory();
        final ValueExpression createValueExpression = expressionFactory.createValueExpression(context, "#{fimexDetailController}",
                FimexDetailController.class);
        return (FimexDetailController) createValueExpression.getValue(context);
    }

    /**
     * Go to fimex details page.
     */
    public void goToFimexDetailsPage() {
        try {
            final List<FileTypeCode> fileTypeCodeList = Arrays.asList(FileTypeCode.FIMEX);
            final File fimex = fileService.findMatchingBetweenFimexAndPriorNotice(currentFile.getClient().getNumContribuable(),
                    fileTypeCodeList);

            if (fimex != null) {
                setIndexPageUrl(ControllerConstants.Pages.FO.DETAILS_FIMEX_INDEX_PAGE);
                final FacesContext context = FacesContext.getCurrentInstance();
                final ExternalContext extContext = context.getExternalContext();
                final FimexDetailController fimexDetailController = getInstanceOfPageFimexDetailController();
                fimexDetailController.setCurrentFile(fimex);
                fimexDetailController.init();

                final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
                        .getActionURL(context, indexPageUrl));

                extContext.redirect(url);
            } else {
                final String errorMsg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(ControllerConstants.Bundle.Messages.NO_FIMEX_MESSAGE);
                JsfUtil.addErrorMessage(errorMsg);
            }

        } catch (final IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
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
     * Gets the attachment list.
     *
     * @return the attachment list
     */
    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    /**
     * Sets the attachment list.
     *
     * @param attachmentList the new attachment list
     */
    public void setAttachmentList(final List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
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
     * Sets the logged user.
     *
     * @param loggedUser the loggedUser to set
     */
    public void setLoggedUser(final User loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * Gets the id attachment.
     *
     * @return the id attachment
     */
    public String getIdAttachment() {
        return idAttachment;
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
     * Sets the id attachment.
     *
     * @param idAttachment the new id attachment
     */
    public void setIdAttachment(final String idAttachment) {
        this.idAttachment = idAttachment;
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
     * Gets the decisionAllowedAtCotationLevel allowed.
     *
     * @return the decisionAllowedAtCotationLevel allowed
     */
    public Boolean getDecisionAllowedAtCotationLevel() {
        return decisionAllowedAtCotationLevel;
    }

    /**
     * Sets the decisionAllowedAtCotationLevel.
     *
     * @param decisionAllowedAtCotationLevel the new
     * decisionAllowedAtCotationLevel
     */
    public void setDecisionAllowedAtCotationLevel(final Boolean decisionAllowedAtCotationLevel) {
        this.decisionAllowedAtCotationLevel = decisionAllowedAtCotationLevel;
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
     * Gets the allowed user.
     *
     * @return the allowed user
     */
    public Boolean getAllowedUser() {
        return allowedUser;
    }

    /**
     * Sets the allowed user.
     *
     * @param allowedUser the new allowed user
     */
    public void setAllowedUser(final Boolean allowedUser) {
        this.allowedUser = allowedUser;
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
     * Gets the roll back decisions allowed.
     *
     * @return the rollBackDecisionsAllowed
     */
    public Boolean getRollBackDecisionsAllowed() {
        return rollBackDecisionsAllowed;
    }

    /**
     * Sets the roll back decisions allowed.
     *
     * @param rollBackDecisionsAllowed the rollBackDecisionsAllowed to set
     */
    public void setRollBackDecisionsAllowed(final Boolean rollBackDecisionsAllowed) {
        this.rollBackDecisionsAllowed = rollBackDecisionsAllowed;
    }

    /**
     * Gets the send decision allowed.
     *
     * @return the sendDecisionAllowed
     */
    public Boolean getSendDecisionAllowed() {
        return sendDecisionAllowed;
    }

    /**
     * Sets the send decision allowed.
     *
     * @param sendDecisionAllowed the sendDecisionAllowed to set
     */
    public void setSendDecisionAllowed(final Boolean sendDecisionAllowed) {
        this.sendDecisionAllowed = sendDecisionAllowed;
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
     * Gets the product info items filtred.
     *
     * @return the productInfoItemsFiltred
     */
    public List<FileItem> getProductInfoItemsFiltred() {
        return productInfoItemsFiltred;
    }

    /**
     * Sets the product info items filtred.
     *
     * @param productInfoItemsFiltred the productInfoItemsFiltred to set
     */
    public void setProductInfoItemsFiltred(final List<FileItem> productInfoItemsFiltred) {
        this.productInfoItemsFiltred = productInfoItemsFiltred;
    }

    /**
     * Gets the selected file item.
     *
     * @return the selectedFileItem
     */
    public FileItem getSelectedFileItem() {
        return selectedFileItem;
    }

    /**
     * Sets the selected file item.
     *
     * @param selectedFileItem the selectedFileItem to set
     */
    public void setSelectedFileItem(final FileItem selectedFileItem) {
        this.selectedFileItem = selectedFileItem;
    }

    /**
     * Gets the destination flows from current step.
     *
     * @return the destinationFlowsFromCurrentStep
     */
    public List<Flow> getDestinationFlowsFromCurrentStep() {
        return destinationFlowsFromCurrentStep;
    }

    public List<Flow> getDestinationFlowsFromCurrentCotationStep() {
        return destinationFlowsFromCurrentCotationStep;
    }

    /**
     * Sets the destination flows from current step.
     *
     * @param destinationFlowsFromCurrentStep the
     * destinationFlowsFromCurrentStep to set
     */
    public void setDestinationFlowsFromCurrentStep(final List<Flow> destinationFlowsFromCurrentStep) {
        this.destinationFlowsFromCurrentStep = destinationFlowsFromCurrentStep;
    }

    /**
     * Gets the product info items.
     *
     * @return the productInfoItems
     */
    public List<FileItem> getProductInfoItems() {
        return productInfoItems;
    }

    /**
     * Sets the product info items.
     *
     * @param productInfoItems the productInfoItems to set
     */
    public void setProductInfoItems(final List<FileItem> productInfoItems) {
        this.productInfoItems = productInfoItems;
    }

    /**
     * Gets the user list to affected file cotation.
     *
     * @return the userListToAffectedFileCotation
     */
    public List<User> getUserListToAffectedFileCotation() {
        return userListToAffectedFileCotation;
    }

    /**
     * Sets the user list to affected file cotation.
     *
     * @param userListToAffectedFileCotation the userListToAffectedFileCotation
     * to set
     */
    public void setUserListToAffectedFileCotation(final List<User> userListToAffectedFileCotation) {
        this.userListToAffectedFileCotation = userListToAffectedFileCotation;
    }

    /**
     * Gets the assigned user for cotation.
     *
     * @return the assignedUserForCotation
     */
    public User getAssignedUserForCotation() {
        return assignedUserForCotation;
    }

    /**
     * Sets the assigned user for cotation.
     *
     * @param assignedUserForCotation the assignedUserForCotation to set
     */
    public void setAssignedUserForCotation(final User assignedUserForCotation) {
        this.assignedUserForCotation = assignedUserForCotation;
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
     * Gets the ap decision step.
     *
     * @return the apDecisionStep
     */
    public Step getApDecisionStep() {
        return apDecisionStep;
    }

    /**
     * Sets the ap decision step.
     *
     * @param apDecisionStep the apDecisionStep to set
     */
    public void setApDecisionStep(final Step apDecisionStep) {
        this.apDecisionStep = apDecisionStep;
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
     * Gets the file item has draft.
     *
     * @return the fileItemHasDraft
     */
    public Boolean getFileItemHasDraft() {
        return fileItemHasDraft;
    }

    /**
     * Sets the file item has draft.
     *
     * @param fileItemHasDraft the fileItemHasDraft to set
     */
    public void setFileItemHasDraft(final Boolean fileItemHasDraft) {
        this.fileItemHasDraft = fileItemHasDraft;
    }

    /**
     * Gets the ebxml properties service.
     *
     * @return the ebxmlPropertiesService
     */
    public EbxmlPropertiesService getEbxmlPropertiesService() {
        return ebxmlPropertiesService;
    }

    /**
     * Sets the ebxml properties service.
     *
     * @param ebxmlPropertiesService the ebxmlPropertiesService to set
     */
    public void setEbxmlPropertiesService(final EbxmlPropertiesService ebxmlPropertiesService) {
        this.ebxmlPropertiesService = ebxmlPropertiesService;
    }

    /**
     * Gets the show remind decision form.
     *
     * @return the show remind decision form
     */
    public Boolean getShowRemindDecisionForm() {
        return showRemindDecisionForm;
    }

    /**
     * Sets the show remind decision form.
     *
     * @param showRemindDecisionForm the new show remind decision form
     */
    public void setShowRemindDecisionForm(final Boolean showRemindDecisionForm) {
        this.showRemindDecisionForm = showRemindDecisionForm;
    }

    /**
     * Gets the show list recommandation article form.
     *
     * @return the show list recommandation article form
     */
    public Boolean getShowListRecommandationArticleForm() {
        return showListRecommandationArticleForm;
    }

    /**
     * Sets the show list recommandation article form.
     *
     * @param showListRecommandationArticleForm the new show list recommandation
     * article form
     */
    public void setShowListRecommandationArticleForm(final Boolean showListRecommandationArticleForm) {
        this.showListRecommandationArticleForm = showListRecommandationArticleForm;
    }

    /**
     * Gets the show product details form.
     *
     * @return the show product details form
     */
    public Boolean getShowProductDetailsForm() {
        return showProductDetailsForm;
    }

    /**
     * Sets the show product details form.
     *
     * @param showProductDetailsForm the new show product details form
     */
    public void setShowProductDetailsForm(final Boolean showProductDetailsForm) {
        this.showProductDetailsForm = showProductDetailsForm;
    }

    /**
     * Gets the show show attachment form.
     *
     * @return the show show attachment form
     */
    public Boolean getShowShowAttachmentForm() {
        return showShowAttachmentForm;
    }

    /**
     * Sets the show show attachment form.
     *
     * @param showShowAttachmentForm the new show show attachment form
     */
    public void setShowShowAttachmentForm(final Boolean showShowAttachmentForm) {
        this.showShowAttachmentForm = showShowAttachmentForm;
    }

    /**
     * Gets the come from retrieve ap.
     *
     * @return the comeFromRetrieveAp
     */
    public Boolean getComeFromRetrieveAp() {
        return comeFromRetrieveAp;
    }

    /**
     * Sets the come from retrieve ap.
     *
     * @param comeFromRetrieveAp the comeFromRetrieveAp to set
     */
    public void setComeFromRetrieveAp(final Boolean comeFromRetrieveAp) {
        this.comeFromRetrieveAp = comeFromRetrieveAp;
    }

    /**
     * Gets the dision system allowed.
     *
     * @return the disionSystemAllowed
     */
    public Boolean getDisionSystemAllowed() {
        return disionSystemAllowed;
    }

    /**
     * Sets the dision system allowed.
     *
     * @param disionSystemAllowed the disionSystemAllowed to set
     */
    public void setDisionSystemAllowed(final Boolean disionSystemAllowed) {
        this.disionSystemAllowed = disionSystemAllowed;
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
     * Gets the analyse result ap list.
     *
     * @return the analyseResultApList
     */
    public List<AnalyseResultAp> getAnalyseResultApList() {
        return analyseResultApList;
    }

    /**
     * Sets the analyse result ap list.
     *
     * @param analyseResultApList the analyseResultApList to set
     */
    public void setAnalyseResultApList(final List<AnalyseResultAp> analyseResultApList) {
        this.analyseResultApList = analyseResultApList;
    }

    /**
     * Gets the test result ap list.
     *
     * @return the testResultApList
     */
    public List<EssayTestAP> getTestResultApList() {
        return testResultApList;
    }

    /**
     * Sets the test result ap list.
     *
     * @param testResultApList the testResultApList to set
     */
    public void setTestResultApList(final List<EssayTestAP> testResultApList) {
        this.testResultApList = testResultApList;
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
     * @param laboratories the laboratories to set
     */
    public void setLaboratories(final List<Laboratory> laboratories) {
        this.laboratories = laboratories;
    }

    /**
     * Gets the laboratory service.
     *
     * @return the laboratoryService
     */
    public LaboratoryService getLaboratoryService() {
        return laboratoryService;
    }

    /**
     * Sets the laboratory service.
     *
     * @param laboratoryService the laboratoryService to set
     */
    public void setLaboratoryService(final LaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
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
     * Gets the analyse result ap service.
     *
     * @return the analyseResultApService
     */
    public AnalyseResultApService getAnalyseResultApService() {
        return analyseResultApService;
    }

    /**
     * Sets the analyse result ap service.
     *
     * @param analyseResultApService the analyseResultApService to set
     */
    public void setAnalyseResultApService(final AnalyseResultApService analyseResultApService) {
        this.analyseResultApService = analyseResultApService;
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

    public AttachmentService getAttachmentService() {
        return attachmentService;
    }

    public void setAttachmentService(final AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    /**
     * Gets the acceptation decision file type.
     *
     * @return the acceptationDecisionFileType
     */
    public String getAcceptationDecisionFileType() {
        return acceptationDecisionFileType;
    }

    /**
     * Sets the acceptation decision file type.
     *
     * @param acceptationDecisionFileType the acceptationDecisionFileType to set
     */
    public void setAcceptationDecisionFileType(final String acceptationDecisionFileType) {
        this.acceptationDecisionFileType = acceptationDecisionFileType;
    }

    /**
     * Gets the last decision ar.
     *
     * @return the lastDecisionAR
     */
    public AnalyseResultAp getLastDecisionAR() {
        return lastDecisionAR;
    }

    /**
     * Sets the last decision ar.
     *
     * @param lastDecisionAR the lastDecisionAR to set
     */
    public void setLastDecisionAR(final AnalyseResultAp lastDecisionAR) {
        this.lastDecisionAR = lastDecisionAR;
    }

    /**
     * Gets the last decision tr.
     *
     * @return the lastDecisionTR
     */
    public EssayTestAP getLastDecisionTR() {
        return lastDecisionTR;
    }

    /**
     * Sets the last decision tr.
     *
     * @param lastDecisionTR the lastDecisionTR to set
     */
    public void setLastDecisionTR(final EssayTestAP lastDecisionTR) {
        this.lastDecisionTR = lastDecisionTR;
    }

    /**
     * Gets the last decision er.
     *
     * @return the lastDecisionER
     */
    public EssayTestAP getLastDecisionER() {
        return lastDecisionER;
    }

    /**
     * Sets the last decision er.
     *
     * @param lastDecisionER the lastDecisionER to set
     */
    public void setLastDecisionER(final EssayTestAP lastDecisionER) {
        this.lastDecisionER = lastDecisionER;
    }

    /**
     * Gets the essay test ap service.
     *
     * @return the essayTestApService
     */
    public EssayTestApService getEssayTestApService() {
        return essayTestApService;
    }

    /**
     * Sets the essay test ap service.
     *
     * @param essayTestApService the essayTestApService to set
     */
    public void setEssayTestApService(final EssayTestApService essayTestApService) {
        this.essayTestApService = essayTestApService;
    }

    /**
     * Gets the decision details ar.
     *
     * @return the decisionDetailsAR
     */
    public AnalyseResultAp getDecisionDetailsAR() {
        return decisionDetailsAR;
    }

    /**
     * Sets the decision details ar.
     *
     * @param decisionDetailsAR the decisionDetailsAR to set
     */
    public void setDecisionDetailsAR(final AnalyseResultAp decisionDetailsAR) {
        this.decisionDetailsAR = decisionDetailsAR;
    }

    /**
     * Gets the decision details tr.
     *
     * @return the decisionDetailsTR
     */
    public EssayTestAP getDecisionDetailsTR() {
        return decisionDetailsTR;
    }

    /**
     * Sets the decision details tr.
     *
     * @param decisionDetailsTR the decisionDetailsTR to set
     */
    public void setDecisionDetailsTR(final EssayTestAP decisionDetailsTR) {
        this.decisionDetailsTR = decisionDetailsTR;
    }

    /**
     * Gets the decision details er.
     *
     * @return the decisionDetailsER
     */
    public EssayTestAP getDecisionDetailsER() {
        return decisionDetailsER;
    }

    /**
     * Sets the decision details er.
     *
     * @param decisionDetailsER the decisionDetailsER to set
     */
    public void setDecisionDetailsER(final EssayTestAP decisionDetailsER) {
        this.decisionDetailsER = decisionDetailsER;
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
     * Gets the recommandation authority service.
     *
     * @return the recommandation authority service
     */
    public RecommandationAuthorityService getRecommandationAuthorityService() {
        return recommandationAuthorityService;
    }

    /**
     * Sets the recommandation authority service.
     *
     * @param recommandationAuthorityService the new recommandation authority
     * service
     */
    public void setRecommandationAuthorityService(final RecommandationAuthorityService recommandationAuthorityService) {
        this.recommandationAuthorityService = recommandationAuthorityService;
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

    /**
     * Gets the specific decisions history.
     *
     * @return the specific decisions history
     */
    public ApSpecificDecisionHistory getSpecificDecisionsHistory() {
        return specificDecisionsHistory;
    }

    /**
     * Sets the specific decisions history.
     *
     * @param specificDecisionsHistory the new specific decisions history
     */
    public void setSpecificDecisionsHistory(final ApSpecificDecisionHistory specificDecisionsHistory) {
        this.specificDecisionsHistory = specificDecisionsHistory;
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
     * Gets the item flow history dto list filtred.
     *
     * @return the item flow history dto list filtred
     */
    public List<ItemFlowDto> getItemFlowHistoryDtoListFiltred() {
        return itemFlowHistoryDtoListFiltred;
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
     * Gets the recommandation list filtred.
     *
     * @return the recommandation list filtred
     */
    public List<Recommandation> getRecommandationListFiltred() {
        return recommandationListFiltred;
    }

    /**
     * Sets the recommandation list filtred.
     *
     * @param recommandationListFiltred the new recommandation list filtred
     */
    public void setRecommandationListFiltred(final List<Recommandation> recommandationListFiltred) {
        this.recommandationListFiltred = recommandationListFiltred;
    }

    /**
     * Gets the attachment list filtred.
     *
     * @return the attachment list filtred
     */
    public List<Attachment> getAttachmentListFiltred() {
        return attachmentListFiltred;
    }

    /**
     * Sets the attachment list filtred.
     *
     * @param attachmentListFiltred the new attachment list filtred
     */
    public void setAttachmentListFiltred(final List<Attachment> attachmentListFiltred) {
        this.attachmentListFiltred = attachmentListFiltred;
    }

    /**
     * Gets the recommandation article list filtred.
     *
     * @return the recommandation article list filtred
     */
    public List<Recommandation> getRecommandationArticleListFiltred() {
        return recommandationArticleListFiltred;
    }

    /**
     * Sets the recommandation article list filtred.
     *
     * @param recommandationArticleListFiltred the new recommandation article
     * list filtred
     */
    public void setRecommandationArticleListFiltred(final List<Recommandation> recommandationArticleListFiltred) {
        this.recommandationArticleListFiltred = recommandationArticleListFiltred;
    }

    /**
     * Gets the come from search.
     *
     * @return the come from search
     */
    public boolean getComeFromSearch() {
        return comeFromSearch;
    }

    /**
     * Sets the come from search.
     *
     * @param comeFromSearch the new come from search
     */
    public void setComeFromSearch(final boolean comeFromSearch) {
        this.comeFromSearch = comeFromSearch;
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

    /**
     * Gets the show validate payment.
     *
     * @return the show validate payment
     */
    public Boolean getShowValidatePayment() {
        return showValidatePayment;
    }

    /**
     * Sets the show validate payment.
     *
     * @param showValidatePayment the new show validate payment
     */
    public void setShowValidatePayment(final Boolean showValidatePayment) {
        this.showValidatePayment = showValidatePayment;
    }

    /**
     * Gets the file field service.
     *
     * @return the fileFieldService
     */
    public FileFieldService getFileFieldService() {
        return fileFieldService;
    }

    /**
     * Sets the file field service.
     *
     * @param fileFieldService the fileFieldService to set
     */
    public void setFileFieldService(final FileFieldService fileFieldService) {
        this.fileFieldService = fileFieldService;
    }

    /**
     * Gets the file field value service.
     *
     * @return the fileFieldValueService
     */
    public FileFieldValueService getFileFieldValueService() {
        return fileFieldValueService;
    }

    /**
     * Sets the file field value service.
     *
     * @param fileFieldValueService the fileFieldValueService to set
     */
    public void setFileFieldValueService(final FileFieldValueService fileFieldValueService) {
        this.fileFieldValueService = fileFieldValueService;
    }

    /**
     * Gets the report organism service.
     *
     * @return the reportOrganismService
     */
    public ReportOrganismService getReportOrganismService() {
        return reportOrganismService;
    }

    /**
     * Sets the report organism service.
     *
     * @param reportOrganismService the reportOrganismService to set
     */
    public void setReportOrganismService(final ReportOrganismService reportOrganismService) {
        this.reportOrganismService = reportOrganismService;
    }

    /**
     * Gets the message to send service.
     *
     * @return the messageToSendService
     */
    public MessageToSendService getMessageToSendService() {
        return messageToSendService;
    }

    /**
     * Sets the message to send service.
     *
     * @param messageToSendService the messageToSendService to set
     */
    public void setMessageToSendService(MessageToSendService messageToSendService) {
        this.messageToSendService = messageToSendService;
    }

    /**
     * Notification email.
     *
     * @param file the file
     * @param step the step
     */
    public void notificationEmail(final File file, final Step step) {

        final Map<String, String> map = new HashMap<String, String>();

        String object = null;

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
     * Gets the mail service.
     *
     * @return the mail service
     */
    public MailService getMailService() {
        return mailService;
    }

    /**
     * Sets the mail service.
     *
     * @param mailService the new mail service
     */
    public void setMailService(final MailService mailService) {
        this.mailService = mailService;
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
     * Checks if is checked fimex file type.
     *
     * @return true, if is checked fimex file type
     */
    public boolean isCheckedFimexFileType() {
        return checkedFimexFileType;
    }

    /**
     * Sets the checked fimex file type.
     *
     * @param checkedFimexFileType the new checked fimex file type
     */
    public void setCheckedFimexFileType(final boolean checkedFimexFileType) {
        this.checkedFimexFileType = checkedFimexFileType;
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

    public WoodSpecificationService getWoodSpecificationServce() {
        return woodSpecificationServce;
    }

    public void setWoodSpecificationServce(WoodSpecificationService woodSpecificationServce) {
        this.woodSpecificationServce = woodSpecificationServce;
    }

    public FileMarshallService getFileMarshallServce() {
        return fileMarshallServce;
    }

    public void setFileMarshallServce(FileMarshallService fileMarshallServce) {
        this.fileMarshallServce = fileMarshallServce;
    }

    /**
     * Gets the generate report allowed.
     *
     * @return the generate report allowed
     */
    public Boolean getGenerateReportAllowed() {
        return generateReportAllowed;
    }

    /**
     * Sets the generate report allowed.
     *
     * @param generateReportAllowed the new generate report allowed
     */
    public void setGenerateReportAllowed(final Boolean generateReportAllowed) {
        this.generateReportAllowed = generateReportAllowed;
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
     * Checks if is rejct dispatch allowed.
     *
     * @return true, if is rejct dispatch allowed
     */
    public boolean isRejctDispatchAllowed() {
        return rejctDispatchAllowed;
    }

    /**
     * Sets the rejct dispatch allowed.
     *
     * @param rejctDispatchAllowed the new rejct dispatch allowed
     */
    public void setRejctDispatchAllowed(final boolean rejctDispatchAllowed) {
        this.rejctDispatchAllowed = rejctDispatchAllowed;
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

    public boolean isVtTypeSelectionViewable() {
        return vtTypeSelectionViewable;
    }

    public boolean isAiMinmidtFileType() {
        return aiMinmidtFileType;
    }

    public boolean isVtMinepdedFileType() {
        return vtMinepdedFileType;
    }

    public boolean isBsbeMinfofFileType() {
        return bsbeMinfofFileType;
    }

    public FileField getVtTypeFileField() {
        return vtTypeFileField;
    }

    public String getMinepdedVtType() {
        return minepdedVtType;
    }

    public void setMinepdedVtType(String minepdedVtType) {
        this.minepdedVtType = minepdedVtType;
    }

    public List<WoodSpecification> getSpecsList() {
        return specsList;
    }

    public void setSpecsList(List<WoodSpecification> specsList) {
        this.specsList = specsList;
    }

    public FileFieldValue getWoodsType() {
        return woodsType;
    }

    public void setWoodsType(FileFieldValue woodsType) {
        this.woodsType = woodsType;
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
                final DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
                transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                final TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
                try {
                    Flow currentSelectedFlow = selectedItemFlowDto.getItemFlow().getFlow();
                    final List<FileItem> fileItemList = currentFile.getFileItemsList();
                    final String service = StringUtils.EMPTY;
                    final String documentType = StringUtils.EMPTY;
//                    final List<ItemFlow> itemFlowList = selectedItemFlowDto.getItemFlow().getFlow().getItemsFlowsList();
                    final List<ItemFlow> itemFlowList = itemFlowService.findLastItemFlowsByFileItemListAndFlow(fileItemList, FlowCode.valueOf(currentSelectedFlow.getCode()));
                    //generate report
                    Map<String, byte[]> attachedByteFiles = null;
                    if (FlowCode.FL_AP_107.name().equals(currentSelectedFlow.getCode()) || FlowCode.FL_AP_169.name().equals(currentSelectedFlow.getCode())) {
                        attachedByteFiles = new HashMap<>();

                        final List<FileTypeFlowReport> fileTypeFlowReports = new ArrayList<>();

                        final List<FileTypeFlowReport> fileTypeFlowReportsList = currentSelectedFlow.getFileTypeFlowReportsList();

                        if (fileTypeFlowReportsList != null) {

                            for (final FileTypeFlowReport fileTypeFlowReport : fileTypeFlowReportsList) {
                                if (currentFile.getFileType().equals(fileTypeFlowReport.getFileType())) {
                                    fileTypeFlowReports.add(fileTypeFlowReport);
                                }
                            }
                        }
                        for (final FileTypeFlowReport fileTypeFlowReport : fileTypeFlowReports) {
                            final String nomClasse = fileTypeFlowReport.getReportClassName();
                            @SuppressWarnings("rawtypes")
                            final Class classe = Class.forName(nomClasse);
                            @SuppressWarnings({"rawtypes", "unchecked"})

                            final byte[] report = ReportGeneratorUtils.generateReportBytes(fileFieldValueService, classe, currentFile);
                            attachedByteFiles.put(fileTypeFlowReport.getReportName(), report);
                        }
                    }

                    // convert file to document
                    final Serializable documentSerializable = xmlConverterService.convertFileToDocument(fileItemList.get(0)
                            .getFile(), currentFile.getFileItemsList(), itemFlowList, currentSelectedFlow);

                    // prepare document to send
                    byte[] xmlBytes;
                    try (final ByteArrayOutputStream output = SendDocumentUtils.prepareApDocument(documentSerializable, service, documentType)) {
                        xmlBytes = output.toByteArray();
                    }
                    if (CollectionUtils.isNotEmpty(currentSelectedFlow.getCopyRecipientsList())) {
                        final List<CopyRecipient> copyRecipients = currentSelectedFlow.getCopyRecipientsList();
                        for (final CopyRecipient copyRecipient : copyRecipients) {
                            LOG.info("SEND COPY RECIPIENT TO {}", copyRecipient.getToAuthority().getRole());
                            send(transactionStatus, xmlBytes, attachedByteFiles, service, documentType, copyRecipient.getToAuthority().getRole(), itemFlowList);
                        }
                    } else {
                        send(transactionStatus, xmlBytes, attachedByteFiles, service, documentType, ebxmlPropertiesService.getToPartyId(), itemFlowList);
                    }

                    transactionManager.commit(transactionStatus);
                    if (LOG.isDebugEnabled()) {
                        LOG.info("####RESEND DECISION Transaction commited####");
                    }

                    LOG.info("####RESEND DECISION Transaction commited####");

                    JsfUtil.addSuccessMessageAfterRedirect(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                            getCurrentLocale()).getString(ControllerConstants.Bundle.Messages.RESEND_SUCCESS));
                } catch (final Exception e) {
                    transactionManager.rollback(transactionStatus);
                    throw e;
                }
            }
        } catch (Exception ex) {
            LOG.error("cannot resend the decision", ex);
            showErrorFacesMessage(ControllerConstants.Bundle.Messages.RESEND_ERROR, null);
        }
    }

    public static List safe(List list) {
        return list == null ? java.util.Collections.EMPTY_LIST : list;
    }

    /**
     * Show error faces message.
     *
     * @param bundle the bundle
     * @param clientId the client id
     */
    private static void showErrorFacesMessage(final String bundle, final String clientId) {
        final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                FacesContext.getCurrentInstance().getViewRoot().getLocale()).getString(bundle);
        if (org.apache.commons.lang3.StringUtils.isEmpty(clientId)) {
            JsfUtil.addErrorMessage(msg);
        } else {
            JsfUtil.addErrorMessage(clientId, msg);
        }
        LOG.warn(msg);
    }

    @Override
    public String getDetailIndexPageUrl() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canDecide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canConfirm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canRollback() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
