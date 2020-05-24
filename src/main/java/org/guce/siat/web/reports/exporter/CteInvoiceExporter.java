package org.guce.siat.web.reports.exporter;

import com.google.zxing.WriterException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.common.utils.QRCodeUtils;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.core.ct.model.PaymentData;
import org.guce.siat.core.ct.util.enums.CctExportProductType;
import org.guce.siat.web.reports.vo.CteInvoiceFileItemVo;
import org.guce.siat.web.reports.vo.CteInvoiceVo;

/**
 *
 * @author tadzotsa
 */
public class CteInvoiceExporter extends AbstractReportInvoker {

    private static final String PARAM_QUANTITY_LABEL = "QUANTITY_LABEL";
    private static final String PARAM_PACKAGES_COUNT_LABEL = "PACKAGES_COUNT_LABEL";

    /**
     * The file.
     */
    private final File file;

    private final PaymentData paymentData;

    public CteInvoiceExporter(File file, PaymentData paymentData) {
        super("CCT_CT_E_INV", "CCT_CT_E_INV");
        this.file = file;
        this.paymentData = paymentData;
    }

    /*
    * (non-Javadoc)
    *
    * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        CteInvoiceVo invoiceVo = new CteInvoiceVo();

        invoiceVo.setFile(file);
        invoiceVo.setPaymentData(paymentData);
        invoiceVo.setOperationType("EXPORT");

        ItemFlow invValidItemFlow = getItemFlowService().findItemFlowByFileItemAndFlow2(file.getFileItemsList().get(0), FlowCode.FL_CT_121);
        if (invValidItemFlow == null) {
            invValidItemFlow = getItemFlowService().findItemFlowByFileItemAndFlow2(file.getFileItemsList().get(0), FlowCode.FL_CT_133);
        }
        invoiceVo.setSignatory(invValidItemFlow.getSender());
        invoiceVo.setSignatureDate(invValidItemFlow.getCreated());
        if (invoiceVo.getSignatureDate() == null) {
            invoiceVo.setSignatureDate(Calendar.getInstance().getTime());
        }

        FileFieldValue fileFieldValue = getFileFieldValueService().findValueByFileFieldAndFile("TYPE_PRODUIT_CODE", file);
        String productTypeCode = fileFieldValue.getValue();
        CctExportProductType productType = CctExportProductType.valueOf(productTypeCode);
        invoiceVo.setProductTypeCode(productTypeCode);

        List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

        if (CollectionUtils.isNotEmpty(fileFieldValueList)) {

            for (final FileFieldValue fileFieldValue1 : fileFieldValueList) {
                switch (fileFieldValue1.getFileField().getCode()) {
                    case "TYPE_PRODUIT_NOM":
                        invoiceVo.setProductTypeLabel(fileFieldValue1.getValue());
                        break;
                    case "CLIENT_PERSONNE_A_CONTACTER":
                        invoiceVo.setPersonToContact(fileFieldValue1.getValue());
                        break;
                    case "TRANSITAIRE_NUMERO_CONTRIBUABLE":
                        invoiceVo.setForwaderNiu(fileFieldValue1.getValue());
                        break;
                    case "TRANSITAIRE_RAISONSOCIALE":
                        invoiceVo.setForwaderName(fileFieldValue1.getValue());
                        break;
                    case "TRANSITAIRE_ADRESSE_EMAIL":
                        invoiceVo.setForwaderEmail(fileFieldValue1.getValue());
                        break;
                    case "TRANSITAIRE_TELEPHONE_MOBILE_NUMERO":
                        invoiceVo.setForwaderPhone(fileFieldValue1.getValue());
                        break;
                    case "TRANSITAIRE_ADRESSE_BP":
                        invoiceVo.setForwaderBp(fileFieldValue1.getValue());
                        break;
                }
            }
        }

        List<FileItem> fileItemList = file.getFileItemsList();
        List<CteInvoiceFileItemVo> fileItemVos = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(fileItemList)) {

            FileItemFieldValue fileItemFieldValue;
            for (FileItem fileItem : fileItemList) {

                CteInvoiceFileItemVo fileItemVo = new CteInvoiceFileItemVo();

                fileItemVo.setFileItem(fileItem);

                fileItemFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOM_COMMERCIAL", fileItem);
                if (fileItemFieldValue != null) {
                    fileItemVo.setTradeName(fileItemFieldValue.getValue());
                }

                if (Arrays.asList(CctExportProductType.CC, CctExportProductType.CF, CctExportProductType.AUTRES).contains(productType)) {
                    fileItemVo.setQuantity(fileItem.getQuantity());
                    fileItemFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOMBRE_SACS", fileItem);
                    if (fileItemFieldValue != null) {
                        fileItemVo.setPackagesCount(fileItemFieldValue.getValue());
                    }
                } else if (Arrays.asList(CctExportProductType.BT, CctExportProductType.GR, CctExportProductType.OA, CctExportProductType.PS, CctExportProductType.COTON).contains(productType)) {
                    fileItemFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOMBRE_GRUMES", fileItem);
                    if (fileItemFieldValue != null) {
                        fileItemVo.setPackagesCount(fileItemFieldValue.getValue());
                    }

                    if (!CctExportProductType.COTON.equals(productType)) {
                        fileItemFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("VOLUME", fileItem);
                        if (fileItemFieldValue != null) {
                            fileItemVo.setQuantity(fileItemFieldValue.getValue());
                        }
                    } else {
                        fileItemFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("POIDS", fileItem);
                        if (fileItemFieldValue != null) {
                            fileItemVo.setQuantity(fileItemFieldValue.getValue());
                        }
                    }

                    if (fileItemVo.getQuantity() == null) {
                        fileItemVo.setQuantity(fileItem.getQuantity());
                    }
                }

                if (Arrays.asList(CctExportProductType.CC, CctExportProductType.CF).contains(productType)) {
                    getJRParameters().put(PARAM_QUANTITY_LABEL, "Quantité (Kg)");
                    getJRParameters().put(PARAM_PACKAGES_COUNT_LABEL, "Nombre de sacs");
                    fileItemVo.setQuantityLabel("Quantité (Kg)");
                    fileItemVo.setPackagesCountLabel("Nombre de sacs");
                } else if (Arrays.asList(CctExportProductType.COTON).contains(productType)) {
                    getJRParameters().put(PARAM_QUANTITY_LABEL, "Poids/Volume");
                    getJRParameters().put(PARAM_PACKAGES_COUNT_LABEL, "Nombre de balles");
                    fileItemVo.setQuantityLabel("Poids/Volume");
                    fileItemVo.setPackagesCountLabel("Nombre de balles");
                } else if (Arrays.asList(CctExportProductType.GR).contains(productType)) {
                    getJRParameters().put(PARAM_QUANTITY_LABEL, "Poids/Volume");
                    getJRParameters().put(PARAM_PACKAGES_COUNT_LABEL, "Nombre de grumes");
                    fileItemVo.setQuantityLabel("Poids/Volume");
                    fileItemVo.setPackagesCountLabel("Nombre de grumes");
                } else if (Arrays.asList(CctExportProductType.BT, CctExportProductType.OA, CctExportProductType.PS, CctExportProductType.AUTRES).contains(productType)) {
                    getJRParameters().put(PARAM_QUANTITY_LABEL, "Poids/Volume");
                    getJRParameters().put(PARAM_PACKAGES_COUNT_LABEL, "Nombre de colis");
                    fileItemVo.setQuantityLabel("Poids/Volume");
                    fileItemVo.setPackagesCountLabel("Nombre de colis");
                }

                fileItemVos.add(fileItemVo);
            }
        }

        invoiceVo.setFileItemVosList(fileItemVos);

        String qrCodeContent = MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}",
                file.getNumeroDemande(), file.getReferenceSiat(), paymentData.getMontantHt() + paymentData.getMontantTva(), DateUtils.formatSimpleDate(DateUtils.FRENCH_DATE_TIME, invValidItemFlow.getCreated()), file.getClient().getCompanyName(), invValidItemFlow.getSender().getLastName());
        try {
            byte[] qrCodeBytes = QRCodeUtils.createQRImage(qrCodeContent, 100);
            invoiceVo.setQrCode(new ByteArrayInputStream(qrCodeBytes));
        } catch (WriterException | IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return new JRBeanCollectionDataSource(Collections.singleton(invoiceVo));
    }

    @Override
    protected Map<String, Object> getJRParameters() {
        final Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "phytosanitaire", "jpg"));
        jRParameters.put("DRAFT_IMAGE", getRealPath(IMAGES_PATH, "draft", "jpg"));
        return jRParameters;
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public File getFile() {
        return file;
    }

    public PaymentData getPaymentData() {
        return paymentData;
    }

}
