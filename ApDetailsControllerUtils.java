package org.guce.siat.web.ct.controller.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.Attachment;
import org.guce.siat.common.model.CopyRecipient;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileField;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileMarshall;
import org.guce.siat.common.model.FileTypeFlowReport;
import org.guce.siat.common.model.Flow;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.ItemFlowData;
import org.guce.siat.common.model.ReportOrganism;
import org.guce.siat.common.utils.ebms.ESBConstants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.core.utils.SendDocumentUtils;
import org.guce.siat.web.ct.controller.FileItemApDetailController;
import static org.guce.siat.web.ct.controller.FileItemApDetailController.safe;
import org.guce.siat.web.reports.exporter.AbstractReportInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ht
 */
public final class ApDetailsControllerUtils {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ApDetailsControllerUtils.class);

    public static boolean sendDecisions(FileItemApDetailController controller, Flow executedFlow, Flow flowToSend,
            List<FileItem> fileItemsList, List<ItemFlow> itemFlowList) throws Exception {

        String service = StringUtils.EMPTY;
        String documentType = StringUtils.EMPTY;
        //generate report
        Map<String, byte[]> attachedByteFiles = null;

        String reportNumber;
        if (FlowCode.FL_AP_107.name().equals(flowToSend.getCode()) || FlowCode.FL_AP_169.name().equals(flowToSend.getCode()) || FlowCode.FL_AP_202.name().equals(flowToSend.getCode())) {
            ItemFlow decisionFlow = null;
            if (controller.getCurrentFile().getFileType().getCode().equals(FileTypeCode.VTP_MINSANTE)) {
                decisionFlow = controller.getItemFlowService().findItemFlowByFileItemAndFlow(fileItemsList.get(0), FlowCode.FL_AP_103);
                if (decisionFlow == null) {
                    decisionFlow = controller.getItemFlowService().findItemFlowByFileItemAndFlow(fileItemsList.get(0), FlowCode.FL_AP_169);
                }
                if (decisionFlow == null) {
                    decisionFlow = controller.getItemFlowService().findItemFlowByFileItemAndFlow(fileItemsList.get(0), FlowCode.FL_AP_170);
                }
                if (decisionFlow == null) {
                    decisionFlow = controller.getItemFlowService().findItemFlowByFileItemAndFlow(fileItemsList.get(0), FlowCode.FL_AP_171);
                }
                if (decisionFlow == null) {
                    decisionFlow = controller.getItemFlowService().findItemFlowByFileItemAndFlow(fileItemsList.get(0), FlowCode.FL_AP_172);
                }
                if (decisionFlow == null) {
                    decisionFlow = controller.getItemFlowService().findItemFlowByFileItemAndFlow(fileItemsList.get(0), FlowCode.FL_AP_173);
                }
            } else if (controller.getCurrentFile().getFileType().getCode().equals(FileTypeCode.VTD_MINSANTE)) {
                decisionFlow = controller.getItemFlowService().findItemFlowByFileItemAndFlow(fileItemsList.get(0), FlowCode.FL_AP_105);
                if (decisionFlow == null) {
                    decisionFlow = controller.getItemFlowService().findItemFlowByFileItemAndFlow(fileItemsList.get(0), FlowCode.FL_AP_169);
                }
                if (decisionFlow == null) {
                    decisionFlow = controller.getItemFlowService().findItemFlowByFileItemAndFlow(fileItemsList.get(0), FlowCode.FL_AP_170);
                }
            } else if (controller.isBsbeMinfofFileType()) {
                decisionFlow = controller.getItemFlowService().findItemFlowByFileItemAndFlow(fileItemsList.get(0), FlowCode.FL_AP_106);
            }
            if (decisionFlow != null) {
                // Persist SIGNATAIRE_DATE
                FileField signataireDateFileField = controller.getFileFieldService().findFileFieldByCodeAndFileType(FileItemApDetailController.SIGNATAIRE_DATE_FIELD_NAME,
                        controller.getCurrentFile().getFileType().getCode());
                if (signataireDateFileField != null && decisionFlow.getCreated() != null) {
                    FileFieldValue signataireDateFieldValue = controller.getFileFieldValueService().findValueByFileFieldAndFile(signataireDateFileField.getCode(), controller.getCurrentFile());
                    if (signataireDateFieldValue != null) {
                        signataireDateFieldValue.setValue(new SimpleDateFormat("dd/MM/yyyy").format(decisionFlow.getCreated()));
                        controller.getFileFieldValueService().update(signataireDateFieldValue);
                    } else {
                        signataireDateFieldValue = new FileFieldValue();
                        signataireDateFieldValue.setFile(controller.getCurrentFile());
                        signataireDateFieldValue.setFileField(signataireDateFileField);
                        signataireDateFieldValue.setValue(new SimpleDateFormat("dd/MM/yyyy").format(decisionFlow.getCreated()));
                        controller.getCurrentFile().getFileFieldValueList().add(signataireDateFieldValue);
                        controller.getFileFieldValueService().save(signataireDateFieldValue);
                    }
                }
                final List<ItemFlowData> itemFlowDataList = decisionFlow.getItemFlowsDataList();
                for (final ItemFlowData ifd : itemFlowDataList) {
                    if (ifd.getDataType().getLabel().equalsIgnoreCase("Date validité")) {
                        final FileField dateValidityField = controller.getFileFieldService().findFileFieldByCodeAndFileType(
                                FileItemApDetailController.VALIDITY_DATE_FIELD_NAME, controller.getCurrentFile().getFileType().getCode());
                        if (dateValidityField != null) {
                            FileFieldValue dateValidityFieldValue = controller.getFileFieldValueService().findValueByFileFieldAndFile(dateValidityField.getCode(), controller.getCurrentFile());
                            if (dateValidityFieldValue != null) {
                                dateValidityFieldValue.setValue(ifd.getValue());
                                controller.getFileFieldValueService().update(dateValidityFieldValue);
                            } else {
                                dateValidityFieldValue = new FileFieldValue();
                                dateValidityFieldValue.setFile(controller.getCurrentFile());
                                dateValidityFieldValue.setFileField(dateValidityField);
                                dateValidityFieldValue.setValue(ifd.getValue());
                                controller.getCurrentFile().getFileFieldValueList().add(dateValidityFieldValue);
                                controller.getFileFieldValueService().save(dateValidityFieldValue);
                            }
                        }
                        break;
                    }
                    if (controller.isBsbeMinfofFileType() && ifd.getDataType().getLabel().equalsIgnoreCase("Numéro Enregistrement BSB")) {
                        final FileField registrationNumberField = controller.getFileFieldService().findFileFieldByCodeAndFileType(
                                "BULLETIN_SPECIFICATION_NUMERO_ENREGISTREMENT", controller.getCurrentFile().getFileType().getCode());
                        final FileField registrationDateField = controller.getFileFieldService().findFileFieldByCodeAndFileType(
                                "BULLETIN_SPECIFICATION_DATE", controller.getCurrentFile().getFileType().getCode());
                        if (registrationNumberField != null) {
                            FileFieldValue registrationNumberFieldValue = controller.getFileFieldValueService().findValueByFileFieldAndFile(registrationNumberField.getCode(), controller.getCurrentFile());
                            if (registrationNumberFieldValue != null) {
                                registrationNumberFieldValue.setValue(ifd.getValue());
                                controller.getFileFieldValueService().update(registrationNumberFieldValue);
                            } else {
                                registrationNumberFieldValue = new FileFieldValue();
                                registrationNumberFieldValue.setFile(controller.getCurrentFile());
                                registrationNumberFieldValue.setFileField(registrationNumberField);
                                registrationNumberFieldValue.setValue(ifd.getValue());
                                controller.getCurrentFile().getFileFieldValueList().add(registrationNumberFieldValue);
                                controller.getFileFieldValueService().save(registrationNumberFieldValue);
                            }
                        }
                        if (registrationDateField != null) {
                            FileFieldValue registrationDateFieldValue = controller.getFileFieldValueService().findValueByFileFieldAndFile(registrationDateField.getCode(), controller.getCurrentFile());
                            if (registrationDateFieldValue != null) {
                                registrationDateFieldValue.setValue(FileItemApDetailController.SIMPLE_DATE_FORMAT.format(java.util.Calendar.getInstance().getTime()));
                                controller.getFileFieldValueService().update(registrationDateFieldValue);
                            } else {
                                registrationDateFieldValue = new FileFieldValue();
                                registrationDateFieldValue.setFile(controller.getCurrentFile());
                                registrationDateFieldValue.setFileField(registrationDateField);
                                registrationDateFieldValue.setValue(FileItemApDetailController.SIMPLE_DATE_FORMAT.format(java.util.Calendar.getInstance().getTime()));
                                controller.getCurrentFile().getFileFieldValueList().add(registrationDateFieldValue);
                                controller.getFileFieldValueService().save(registrationDateFieldValue);
                            }
                        }
                        break;
                    }
                }
            }
            attachedByteFiles = new HashMap<>();

            final List<FileTypeFlowReport> fileTypeFlowReports = new ArrayList<>();

            final List<FileTypeFlowReport> fileTypeFlowReportsList = flowToSend.getFileTypeFlowReportsList();

            if (fileTypeFlowReportsList != null) {

                for (final FileTypeFlowReport fileTypeFlowReport : fileTypeFlowReportsList) {
                    if (controller.getCurrentFile().getFileType().equals(fileTypeFlowReport.getFileType())) {
                        fileTypeFlowReports.add(fileTypeFlowReport);
                    }
                }
            }
            for (final FileTypeFlowReport fileTypeFlowReport : fileTypeFlowReports) {

                //Begin Add new field value with report Number
                final ReportOrganism reportOrganism = controller.getReportOrganismService()
                        .findReportByFileTypeFlowReport(fileTypeFlowReport);
                final FileField reportField = controller.getFileFieldService().findFileFieldByCodeAndFileType(
                        fileTypeFlowReport.getFileFieldName(), fileTypeFlowReport.getFileType().getCode());
                reportNumber = (reportOrganism.getSequence() + 1)
                        + (reportOrganism.getValue() != null ? reportOrganism.getValue() : StringUtils.EMPTY);
                if (reportField != null) {
                    FileFieldValue reportFieldValue = controller.getFileFieldValueService().findValueByFileFieldAndFile(reportField.getCode(), controller.getCurrentFile());
                    if (reportFieldValue == null) {
                        reportFieldValue = new FileFieldValue();
                        reportFieldValue.setFile(controller.getCurrentFile());
                        reportFieldValue.setFileField(reportField);
                        reportFieldValue.setValue(reportNumber);
                        controller.getCurrentFile().getFileFieldValueList().add(reportFieldValue);
                        controller.getFileFieldValueService().save(reportFieldValue);
                    }
                }
                //End Add new field value with report Number
                byte[] report;

                if (controller.isAiMinmidtFileType()) {
                    Attachment finalAttachment = controller.findAuthorizationAttachment();
                    if (finalAttachment == null) {
                        if (controller.getFileToSave() != null) {
                            JsfUtil.addErrorMessage("Le rapport n'a pu être enregistré, mais sera envoyé");
                            report = FileUtils.readFileToByteArray(controller.getFileToSave());
                        } else {
                            JsfUtil.addErrorMessage("Le rapport n'a pu être enregistré");
                            return false;
                        }
                    } else {
                        report = controller.getBytesFromAttachment(finalAttachment);
                    }
                } else {
                    final String nomClasse = fileTypeFlowReport.getReportClassName();
                    @SuppressWarnings("rawtypes")
                    final Class classe = Class.forName(nomClasse);
                    if (controller.isBsbeMinfofFileType()) {
                        Constructor c1 = classe.getConstructor(File.class, List.class, String.class);
                        report = JsfUtil.getReport((AbstractReportInvoker) c1.newInstance(controller.getCurrentFile(), controller.getSpecsList(), controller.getWoodsType() == null ? "GRUMES" : controller.getWoodsType().getValue()));
                    } else {
                        report = ReportGeneratorUtils.generateReportBytes(controller.getFileFieldValueService(),
                                classe, controller.getCurrentFile());
                    }
                }
                attachedByteFiles.put(fileTypeFlowReport.getReportName(), report);
                List<Attachment> filesToSend = controller.findAttachmentsToSend(controller.getCurrentFile().getFileType().getCode());
                for (Attachment att : (List<Attachment>) safe(filesToSend)) {
                    attachedByteFiles.put(att.getDocumentName(), controller.getBytesFromAttachment(att));
                }
                String targetAttachmentName = new StringBuilder().append(controller.getCurrentFile().getNumeroDossier())
                        .append("-").append(controller.getCurrentFile().getReferenceSiat()).append("-")
                        .append(fileTypeFlowReport.getReportName()).toString();
                java.io.File folder = new java.io.File(controller.getApplicationPropretiesService().getAttachementFolder());
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                final java.io.File targetAttachment = new java.io.File(folder, targetAttachmentName);

                try ( FileOutputStream fileOuputStream = new FileOutputStream(targetAttachment)) {
                    fileOuputStream.write(report);
                }

                //Update report sequence
                reportOrganism.setSequence(reportOrganism.getSequence() + 1);
                controller.getReportOrganismService().update(reportOrganism);
                final Map<String, Date> dateParams = new HashMap<>();
                dateParams.put("SIGNATURE_DATE", java.util.Calendar.getInstance().getTime());
                controller.getFileService().updateSpecificColumn(dateParams, controller.getCurrentFile());
            }
        } else if (FlowCode.FL_AP_202.name().equals(flowToSend.getCode())) {
            FileMarshall fileMarshall = controller.getFileMarshallServce().findByFile(controller.getCurrentFile());
            if (fileMarshall != null) {
                Serializable object = (Serializable) SerializationUtils.deserialize(fileMarshall.getMarshall());
                File previousFile;
                switch (controller.getCurrentFile().getFileType().getCode()) {
                    case BSBE_MINFOF:
                        previousFile = controller.getXmlConverterService().convertDocumentToFile(object);
                        break;
                    default:
                        previousFile = null;
                }
                if (previousFile != null) {
                    controller.getXmlConverterService().rollbackFile(controller.getCurrentFile(), previousFile);
                }
            }
        }

        // convert file to document
        final Serializable documentSerializable = controller.getXmlConverterService().convertFileToDocument(controller.getProductInfoItems().get(0)
                .getFile(), fileItemsList, itemFlowList, executedFlow);

        // prepare document to send
        byte[] xmlBytes;
        try (final ByteArrayOutputStream output = SendDocumentUtils.prepareApDocument(documentSerializable, service, documentType)) {
            xmlBytes = output.toByteArray();
        }

        if (CollectionUtils.isNotEmpty(flowToSend.getCopyRecipientsList())) {
            final List<CopyRecipient> copyRecipients = flowToSend.getCopyRecipientsList();
            for (final CopyRecipient copyRecipient : copyRecipients) {
                LOG.info("SEND COPY RECIPIENT TO {}", copyRecipient.getToAuthority().getRole());
                final Map<String, Object> data = new HashMap<>();
                data.put(ESBConstants.FLOW, xmlBytes);
                data.put(ESBConstants.ATTACHMENT, attachedByteFiles);
                data.put(ESBConstants.TYPE_DOCUMENT, documentType);
                data.put(ESBConstants.SERVICE, service);
                data.put(ESBConstants.MESSAGE, null);
                data.put(ESBConstants.EBXML_TYPE, "STANDARD");
                data.put(ESBConstants.TO_PARTY_ID, copyRecipient.getToAuthority().getRole());
                data.put(ESBConstants.DEAD, "0");
                //
                data.put(ESBConstants.ITEM_FLOWS, itemFlowList);
                controller.getFileProducer().sendFile(data);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Message sent to OUT queue");
                }

            }
        } else {
            final Map<String, Object> data = new HashMap<>();
            data.put(ESBConstants.FLOW, xmlBytes);
            data.put(ESBConstants.ATTACHMENT, attachedByteFiles);
            data.put(ESBConstants.TYPE_DOCUMENT, documentType);
            data.put(ESBConstants.SERVICE, service);
            data.put(ESBConstants.MESSAGE, null);
            data.put(ESBConstants.EBXML_TYPE, "STANDARD");
            data.put(ESBConstants.TO_PARTY_ID, controller.getCurrentFile().getEmetteur());
            data.put(ESBConstants.DEAD, "0");
            //
            data.put(ESBConstants.ITEM_FLOWS, itemFlowList);
            controller.getFileProducer().sendFile(data);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Message sent to OUT queue");
            }
        }

        return true;
    }

    private ApDetailsControllerUtils() {
    }

}
