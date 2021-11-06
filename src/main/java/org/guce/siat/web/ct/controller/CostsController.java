package org.guce.siat.web.ct.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.Attachment;
import org.guce.siat.common.model.CopyRecipient;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.Flow;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.service.AttachmentService;
import org.guce.siat.common.service.EbxmlPropertiesService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FileProducer;
import org.guce.siat.common.service.FileService;
import org.guce.siat.common.service.FlowService;
import org.guce.siat.common.service.ItemFlowService;
import org.guce.siat.common.service.XmlConverterService;
import static org.guce.siat.common.service.impl.AlfrescoDirectoryCreatorImpl.SLASH;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.ebms.ESBConstants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.common.utils.ged.CmisSession;
import org.guce.siat.common.utils.ged.CmisUtils;
import org.guce.siat.core.ct.model.PaymentData;
import org.guce.siat.core.ct.service.CotationService;
import org.guce.siat.core.ct.service.PaymentDataService;
import org.guce.siat.core.utils.SendDocumentUtils;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * The Class CostsController.
 */
@ManagedBean(name = "costsController")
@SessionScoped
public class CostsController extends AbstractController<PaymentData> implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -5106751417655195729L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CostsController.class);

    /**
     * The current file.
     */
    private File currentFile;

    private boolean vtMinepdedFileType;

    private java.io.File fileToSave;

    private String newAttachmentName;

    private String attachmentType;

    private Attachment selectedAttachment;

    private List<Attachment> attachmentList;

    /**
     * The current payment data.
     */
    private PaymentData currentPaymentData;

    /**
     * The item flow service.
     */
    @ManagedProperty(value = "#{itemFlowService}")
    private ItemFlowService itemFlowService;

    /**
     * The payment data service.
     */
    @ManagedProperty(value = "#{paymentDataService}")
    private PaymentDataService paymentDataService;

    /**
     * The file service.
     */
    @ManagedProperty(value = "#{fileService}")
    private FileService fileService;

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
     * The transaction manager.
     */
    @ManagedProperty(value = "#{transactionManager}")
    private PlatformTransactionManager transactionManager;

    @ManagedProperty(value = "#{cotationService}")
    private CotationService cotationService;

    /**
     * The xml converter service.
     */
    @ManagedProperty(value = "#{xmlConverterService}")
    private XmlConverterService xmlConverterService;
    /**
     * send file service.
     */
    @ManagedProperty(value = "#{fileProducer}")
    private FileProducer fileProducer;

    /**
     * The ebxml properties service.
     */
    @ManagedProperty(value = "#{ebxmlPropertiesService}")
    private EbxmlPropertiesService ebxmlPropertiesService;

    @ManagedProperty(value = "#{attachmentService}")
    private AttachmentService attachmentService;

    private static final CharsetEncoder ASCII_ENCODER = Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, CostsController.class.getName());
        }
        if (currentFile != null) {
            vtMinepdedFileType = FileTypeCode.VT_MINEPDED.equals(currentFile.getFileType().getCode());
            attachmentList = getCurrentFile().getAttachmentsList();
        }
    }

    /**
     * Validate payment.
     */
    public synchronized void validatePayment() throws IOException {

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
        //save attachments here
        if (attachmentList != null) {
            final Session sessionCmisClient = CmisSession.getInstance();
            for (Attachment at : attachmentList) {
                if (at.getId() == null) {
                    CmisUtils.sendDocument(Arrays.asList(at.getAttachmentFile()), sessionCmisClient, at.getPath());
                    attachmentService.save(at);
                }
            }
        }
        sendPayment();
        returnToPaymentView();
    }

    /**
     * Send payment.
     */
    public void sendPayment() {
        final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setReadOnly(false);
        final TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Flow flowToExecute;
            if (Arrays.asList(FileTypeCode.CCT_CSV).contains(currentFile.getFileType().getCode())) {
                flowToExecute = flowService.findFlowByCode(FlowCode.FL_CT_167.name());
            } else if (Arrays.asList(FileTypeCode.CCT_CSV, FileTypeCode.CCT_CT_E, FileTypeCode.CCT_CT_E_ATP).contains(currentFile.getFileType().getCode())) {
                flowToExecute = flowService.findFlowByCode(FlowCode.FL_CT_123.name());
            } else if (Arrays.asList(FileTypeCode.CCT_CT_E_PVI, FileTypeCode.CCT_CT_E_FSTP).contains(currentFile.getFileType().getCode())) {
                flowToExecute = flowService.findFlowByCode(FlowCode.FL_CT_126.name());
            } else if (Arrays.asList(FileTypeCode.CCT_CT_E_PVE).contains(currentFile.getFileType().getCode())) {
                ItemFlow invValidItemFlow = itemFlowService.findItemFlowByFileItemAndFlow2(currentFile.getFileItemsList().get(0), FlowCode.FL_CT_121);
                if (invValidItemFlow == null) {
                    invValidItemFlow = itemFlowService.findItemFlowByFileItemAndFlow2(currentFile.getFileItemsList().get(0), FlowCode.FL_CT_133);
                }
                if (FlowCode.FL_CT_121.name().equals(invValidItemFlow.getFlow().getCode())) {
                    if (currentFile.getParent() == null) {
                        flowToExecute = flowService.findFlowByCode(FlowCode.FL_CT_126.name());
                    } else {
                        flowToExecute = flowService.findFlowByCode(FlowCode.FL_CT_145.name());
                    }
                } else {
                    flowToExecute = flowService.findFlowByCode(FlowCode.FL_CT_135.name());
                }
            } else if (Arrays.asList(FileTypeCode.CCT_CT, FileTypeCode.CC_CT, FileTypeCode.CQ_CT).contains(currentFile.getFileType().getCode())) {
                flowToExecute = flowService.findFlowByCode(FlowCode.FL_CT_93.name());
            } else {
                if (currentFile.getFileType().getCode().equals(FileTypeCode.PIVPSRP_MINADER)) {
                    flowToExecute = flowService.findFlowByCode(FlowCode.FL_AP_168.name());
                } else {
                    flowToExecute = flowService.findFlowByCode(FlowCode.FL_AP_166.name());
                }
            }

            paymentDataService.takeDecisionForPayment(currentFile, getLoggedUser(), flowToExecute, currentPaymentData);
            List<ItemFlow> lastItemFlows = itemFlowService.findLastItemFlowsByFileItemList(currentFile.getFileItemsList());
            // convert file to document
            final String service = StringUtils.EMPTY;
            final String documentType = StringUtils.EMPTY;
            final Serializable documentSerializable = xmlConverterService.convertFileToDocument(currentFile, currentFile.getFileItemsList(), lastItemFlows, flowToExecute);
            byte[] xmlBytes;
            // prepare document to send
            if (isPhyto() || FileTypeCode.CCT_CSV.equals(currentFile.getFileType().getCode())) {
                try (final ByteArrayOutputStream output = SendDocumentUtils.preparePaymentDocument(documentSerializable, service, documentType)) {
                    xmlBytes = output.toByteArray();
                }
            } else if (Arrays.asList(FileTypeCode.CCT_CT, FileTypeCode.CC_CT, FileTypeCode.CQ_CT).contains(currentFile.getFileType().getCode())) {
                try (final ByteArrayOutputStream output = SendDocumentUtils.prepareCctDocument(documentSerializable, service, documentType)) {
                    xmlBytes = output.toByteArray();
                }
            } else {
                try (final ByteArrayOutputStream output = SendDocumentUtils.prepareApDocument(documentSerializable, service, documentType)) {
                    xmlBytes = output.toByteArray();
                }
            }

            if (CollectionUtils.isNotEmpty(currentPaymentData.getPaymentItemFlowList().get(0).getItemFlow().getFlow().getCopyRecipientsList())) {
                final List<CopyRecipient> copyRecipients = currentPaymentData.getPaymentItemFlowList().get(0).getItemFlow().getFlow().getCopyRecipientsList();
                for (final CopyRecipient copyRecipient : copyRecipients) {
                    LOG.info("SEND COPY RECIPIENT TO {}", copyRecipient.getToAuthority().getRole());
                    final Map<String, Object> data = new HashMap<>();
                    data.put(ESBConstants.FLOW, xmlBytes);
                    data.put(ESBConstants.ATTACHMENT, null);
                    data.put(ESBConstants.TYPE_DOCUMENT, documentType);
                    data.put(ESBConstants.SERVICE, service);
                    data.put(ESBConstants.MESSAGE, null);
                    data.put(ESBConstants.EBXML_TYPE, "STANDARD");
                    data.put(ESBConstants.TO_PARTY_ID, copyRecipient.getToAuthority().getRole());
                    data.put(ESBConstants.DEAD, "0");
                    data.put(ESBConstants.ITEM_FLOWS, lastItemFlows);
                    fileProducer.sendFile(data);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Message sent to OUT queue");
                    }
                }
            } else if (isPhyto() || FileTypeCode.CCT_CSV.equals(currentFile.getFileType().getCode())) {
                final Map<String, Object> data = new HashMap<>();
                data.put(ESBConstants.FLOW, xmlBytes);
                data.put(ESBConstants.ATTACHMENT, new HashMap<>());
                data.put(ESBConstants.SERVICE, service);
                data.put(ESBConstants.TYPE_DOCUMENT, documentType);
                data.put(ESBConstants.MESSAGE, null);
                data.put(ESBConstants.EBXML_TYPE, "STANDARD");
                data.put(ESBConstants.TO_PARTY_ID, ebxmlPropertiesService.getToPartyId());
                data.put(ESBConstants.DEAD, "0");
                //
                data.put(ESBConstants.ITEM_FLOWS, lastItemFlows);
                fileProducer.sendFile(data);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Message sent to OUT queue");
                }
            }

            cotationService.dispatch(currentFile, flowToExecute);

            transactionManager.commit(status);
        } catch (final Exception e) {
            transactionManager.rollback(status);

            LOG.error("####SEND DECISION Transaction rollbacked#### " + e.getMessage(), e);

        }
    }

    /**
     * Return to payment view.
     */
    public void returnToPaymentView() {
        try {
            setPageUrl(ControllerConstants.Pages.FO.PAYMENT_INDEX_PAGE);
            final FacesContext context = FacesContext.getCurrentInstance();
            final ExternalContext extContext = context.getExternalContext();

            final String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, pageUrl));

            extContext.redirect(url);
        } catch (final IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static boolean isPureAscii(String v) {
        return ASCII_ENCODER.canEncode(v);
    }

    public void handleFileUpload(FileUploadEvent event) {
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
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("File_too_large"));
                fileToSave = null;
//                name = "";
            } else {
                fileToSave = new java.io.File(name.replaceAll("/[^A-Za-z0-9]/", ""));
                newAttachmentName = fileToSave.getName();
                try {
                    FileUtils.writeByteArrayToFile(fileToSave, file.getContents());
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(FileItemApDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("Invalid_file_name"));
//            file = null;
//            name = "";
        }
    }

    public void saveAttachment() {
        Folder rootFolder;
        //try {
        final Session sessionCmisClient = CmisSession.getInstance();
        if (fileToSave == null) {
            return;
        }
        final List<java.io.File> attachedFiles = new ArrayList<>();
        attachedFiles.add(fileToSave);

        rootFolder = CmisUtils.getRootFolder(sessionCmisClient, "/siat");
        try {
            rootFolder = CmisUtils.getRootFolder(sessionCmisClient, "/siat/CT");
        } catch (final CmisObjectNotFoundException e) {
            rootFolder = CmisUtils.createFolder(rootFolder, "CT");
        }
        try {
            rootFolder = CmisUtils.getRootFolder(sessionCmisClient, "/siat/CT/MINEPDED/");
        } catch (final CmisObjectNotFoundException e) {
            rootFolder = CmisUtils.createFolder(rootFolder, "MINEPDED");
        }
        try {
            rootFolder = CmisUtils.getRootFolder(sessionCmisClient, "/siat/CT/MINEPDED/" + attachmentType);
        } catch (final CmisObjectNotFoundException e) {
            rootFolder = CmisUtils.createFolder(rootFolder, attachmentType);
        }
        try {
            rootFolder = CmisUtils.getRootFolder(sessionCmisClient, "/siat/CT/MINEPDED/" + attachmentType + SLASH + currentFile.getNumeroDossier());
        } catch (final CmisObjectNotFoundException e) {
            rootFolder = CmisUtils.createFolder(rootFolder, currentFile.getNumeroDossier());
        }
        /*CmisUtils.sendDocument(attachedFiles, sessionCmisClient, rootFolder.getPath());
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(FileItemApDetailController.class.getName()).log(Level.SEVERE, null, ex);
		}*/
        selectedAttachment = new Attachment();
        selectedAttachment.setFile(currentFile);
        selectedAttachment.setAttachmentType(attachmentType);
        selectedAttachment.setDocumentName(fileToSave.getName());
        selectedAttachment.setPath(rootFolder != null ? rootFolder.getPath() : "/siat");
        selectedAttachment.setAttachmentFile(fileToSave);
        //attachmentService.save(selectedAttachment);
        if (attachmentList == null) {
            attachmentList = new java.util.ArrayList<>();
        }
        attachmentList.add(selectedAttachment);
        fileToSave = null;
        newAttachmentName = "";
    }

    /**
     * Gets the payment data service.
     *
     * @return the paymentDataService
     */
    public PaymentDataService getPaymentDataService() {
        return paymentDataService;
    }

    /**
     * Sets the payment data service.
     *
     * @param paymentDataService the paymentDataService to set
     */
    public void setPaymentDataService(final PaymentDataService paymentDataService) {
        this.paymentDataService = paymentDataService;
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
     * Gets the current file.
     *
     * @return the currentFile
     */
    public File getCurrentFile() {
        return currentFile;
    }

    /**
     * Sets the current file.
     *
     * @param currentFile the currentFile to set
     */
    public void setCurrentFile(final File currentFile) {
        this.currentFile = currentFile;
    }

    /**
     * Gets the flow service.
     *
     * @return the flowService
     */
    public FlowService getFlowService() {
        return flowService;
    }

    /**
     * Sets the flow service.
     *
     * @param flowService the flowService to set
     */
    public void setFlowService(final FlowService flowService) {
        this.flowService = flowService;
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
     * Gets the current payment data.
     *
     * @return the currentPaymentData
     */
    public PaymentData getCurrentPaymentData() {
        return currentPaymentData;
    }

    /**
     * Sets the current payment data.
     *
     * @param currentPaymentData the currentPaymentData to set
     */
    public void setCurrentPaymentData(final PaymentData currentPaymentData) {
        this.currentPaymentData = currentPaymentData;
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

    public boolean isVtMinepdedFileType() {
        if (currentFile != null) {
            vtMinepdedFileType = FileTypeCode.VT_MINEPDED.equals(currentFile.getFileType().getCode());
        }
        return vtMinepdedFileType;
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

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Attachment getSelectedAttachment() {
        return selectedAttachment;
    }

    public void setSelectedAttachment(Attachment selectedAttachment) {
        this.selectedAttachment = selectedAttachment;
    }

    public AttachmentService getAttachmentService() {
        return attachmentService;
    }

    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    public List<Attachment> getAttachmentList() {
        if (attachmentList == null) {
            attachmentList = getCurrentFile().getAttachmentsList();
        }
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

    public void showAttachment() {
        final AttachmentController attachmentController = getInstanceOfPageAttachmentController();
        if (selectedAttachment.getId() != null) {
            attachmentController.setSelectedAttachment(selectedAttachment);
            attachmentController.init();
        } else {
            boolean showPaneViewPdf = selectedAttachment.getAttachmentFile().getName().toLowerCase().endsWith(".pdf");
            attachmentController.setShowPanelViewJpeg(!showPaneViewPdf);
            attachmentController.setShowPanelViewPdf(showPaneViewPdf);
            if (showPaneViewPdf) {
                StreamedContent streamPdf = null;
                try {
                    streamPdf = new DefaultStreamedContent(new FileInputStream(selectedAttachment.getAttachmentFile()), "application/pdf");
                } catch (FileNotFoundException ex) {
                    java.util.logging.Logger.getLogger(CostsController.class.getName()).log(Level.SEVERE, null, ex);
                }
                attachmentController.setStreamPdf(streamPdf);
            } else {
                StreamedContent streamJpeg = null;
                try {
                    streamJpeg = new DefaultStreamedContent(new FileInputStream(selectedAttachment.getAttachmentFile()), "image/png");
                } catch (FileNotFoundException ex) {
                    java.util.logging.Logger.getLogger(CostsController.class.getName()).log(Level.SEVERE, null, ex);
                }
                attachmentController.setStreamJpeg(streamJpeg);
            }
        }
    }

    public AttachmentController getInstanceOfPageAttachmentController() {
        final FacesContext fctx = FacesContext.getCurrentInstance();
        final Application application = fctx.getApplication();
        final ELContext context = fctx.getELContext();
        final ExpressionFactory expressionFactory = application.getExpressionFactory();
        final ValueExpression createValueExpression = expressionFactory.createValueExpression(context, "#{attachmentController}",
                AttachmentController.class);
        return (AttachmentController) createValueExpression.getValue(context);
    }

    public CotationService getCotationService() {
        return cotationService;
    }

    public void setCotationService(CotationService cotationService) {
        this.cotationService = cotationService;
    }

    public ItemFlowService getItemFlowService() {
        return itemFlowService;
    }

    public void setItemFlowService(ItemFlowService itemFlowService) {
        this.itemFlowService = itemFlowService;
    }

    public boolean isPhyto() {
        boolean checkMinaderMinistry = currentFile.getDestinataire().equalsIgnoreCase(Constants.MINADER_MINISTRY);
        return checkMinaderMinistry && Arrays.asList(FileTypeCode.CCT_CT_E, FileTypeCode.CCT_CT_E_ATP, FileTypeCode.CCT_CT_E_FSTP, FileTypeCode.CCT_CT_E_PVE, FileTypeCode.CCT_CT_E_PVI).contains(currentFile.getFileType().getCode());
    }

}
