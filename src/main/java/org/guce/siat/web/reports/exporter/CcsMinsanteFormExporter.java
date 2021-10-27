package org.guce.siat.web.reports.exporter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.guce.siat.common.model.Container;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.model.Item;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.core.ct.model.TreatmentInfosCCSMinsante;
import org.guce.siat.web.ct.controller.util.Utils;
import org.springframework.util.Assert;

/**
 *
 * @author etl
 */
public class CcsMinsanteFormExporter extends AbstractReportInvoker {

    private static final String FORM_FIELD_CHECKBOX_CHECKED_VALUE = "OK";
    private static final String FORM_FIELD_CHECKBOX_NOCHECKED_VALUE = "Off";
    
    private static final String CONFORME_O = "O";
    private static final String CONFRME_N = "N";

    /**
     * The decimal format
     */
    private DecimalFormat decimalFormat;

    /**
     * The file.
     */
    private final File file;
    private final TreatmentInfosCCSMinsante treatmentInfos;

    public CcsMinsanteFormExporter(File file, TreatmentInfosCCSMinsante treatmentInfos) {
        super("CCS_MINSANTE", "CCS_MINSANTE");
        this.file = file;
        this.treatmentInfos = treatmentInfos;
        initDecimalFormat();
    }

    public CcsMinsanteFormExporter(File file, String pdfFormFileName, TreatmentInfosCCSMinsante treatmentInfos) {
        super(pdfFormFileName, "CCS_MINSANTE");
        this.file = file;
        this.treatmentInfos = treatmentInfos;
        initDecimalFormat();
    }

    private void initDecimalFormat() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        decimalFormat = (DecimalFormat) nf;
        decimalFormat.applyPattern("#,##0.00");
    }

    @Override
    public byte[] getPdfFile() {

        byte[] pdfReport = null;

        String fileIn = getRealPath(PDF_FORMS_PATH, jasperFileName, "pdf");
        try {
            try (InputStream is = new FileInputStream(fileIn); PDDocument doc = PDDocument.load(is);) {

                buildDocument(doc);

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    doc.save(baos);
                    pdfReport = baos.toByteArray();
                }
            }
        } catch (IOException e) {
            logger.error(Objects.toString(e), e);
        }

        return pdfReport;
    }

    private void buildDocument(PDDocument doc) throws IOException {

        PDDocumentCatalog docCatalog = doc.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();

        Map<String, String> values = new HashMap<>();

        Assert.notNull(decimalFormat, "The decimal format field cannot be null. This may append because the initDecimalFormat method hasn't been called in the used constructor.");
        Assert.notEmpty(file.getFileItemsList(), "The file items list cannot be empty. it must contain at least on item.");
        Assert.notEmpty(file.getFileFieldValueList(), "The file fields value list cannot be empty.");

        FileItem fileItem = file.getFileItemsList().get(0);
        List<FileItem> fileItems = file.getFileItemsList();
        List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
        List<Container> containers = file.getContainers();

        int modifIndex = 0;
        String senderName = StringUtils.EMPTY, senderAddress = StringUtils.EMPTY, transportWay = StringUtils.EMPTY,
                goodsName = StringUtils.EMPTY, diNumber = StringUtils.EMPTY,
                blNumber = StringUtils.EMPTY, goodsWeight = StringUtils.EMPTY,
                container20Number = StringUtils.EMPTY, container40Number = StringUtils.EMPTY, originCountry = StringUtils.EMPTY,
                makerCountry = StringUtils.EMPTY, signatureDate = StringUtils.EMPTY;

        for (FileFieldValue fileFieldValue : fileFieldValueList) {
            switch (fileFieldValue.getFileField().getCode()) {
                case "DOCUMENTS_NUMERO_DI":
                    diNumber = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_NUMERO_BL":
                    blNumber = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_PAYS_ORIGINE_NOM_PAYS":
                    originCountry = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "EXPORTATEUR_ADRESSE_ADRESSE1":
                    senderAddress = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_NB_CONTENEUR20":
                    container20Number = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_NB_CONTENEUR40":
                    container40Number = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_MOYEN_TRANSPORT_LIBELLE":
                    transportWay = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "SIGNATAIRE_DATE":
                    signatureDate = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;

            }
        }
        values.put("NUMERO_CCS", file.getNumeroDossier());
        if (file.getSignatory() != null) {
            values.put("NOM_SIGNATAIRE_1", file.getSignatory().getLastName());
            values.put("NOM_SIGNATAIRE_2", file.getSignatory().getFirstName() + " " + file.getSignatory().getLastName());
        }
        if(file.getSignatureDate() != null){
            values.put("DATE_SIGNATURE_CCS", DateUtils.formatSimpleDate(DateUtils.FRENCH_DATE, file.getSignatureDate()));
        }else{
            values.put("DATE_SIGNATURE_CCS", DateUtils.formatSimpleDate(DateUtils.FRENCH_DATE, java.util.Calendar.getInstance().getTime()));
        }
        values.put("NUMERO_BL", blNumber);
        values.put("NUMERO_DI", diNumber);
        values.put("NOM_ADRESSE_EXPEDITEUR", senderAddress);
        values.put("NOMBRE_CONTENEUR_20", container20Number);
        values.put("NOMBRE_CONTENEUR_40", container40Number);
        values.put("PROVENANCE_MARCHANDISE", originCountry);
        values.put("NAVIRE_TRANSPORTEUR", transportWay);
        FileItemFieldValue fileItemFieldValue;
        modifIndex = 1;
        for (FileItem fi : fileItems) {
            FileItemFieldValue goodNameFileItemFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("DESCRIPTION", fi);
            if (goodNameFileItemFieldValue != null) {
                goodsName = goodNameFileItemFieldValue.getValue();
            }
            if (modifIndex <= 3) {
                FileItemFieldValue goodsWeightFileItemFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("POIDS", fi);
                if (goodsWeightFileItemFieldValue != null) {
                    goodsWeight = goodsWeightFileItemFieldValue.getValue();
                }
                values.put("TONNAGE_CONVENTIONNEL_" + modifIndex, goodsWeight);
            }
        }
        values.put("MARCHANDISE_DESIGNATION", goodsName);

        if (file.getClient() != null) {
            String companyName = file.getClient().getCompanyName();
            String companyAddress = StringUtils.EMPTY;
            if (StringUtils.isNotEmpty(file.getClient().getFullAddress())) {
                companyAddress = file.getClient().getFullAddress();
            } else if (StringUtils.isNotEmpty(file.getClient().getFirstAddress())) {
                companyAddress = file.getClient().getFirstAddress();
            }
            values.put("NOM_ADRESSE_DESTINATAIRE", companyName + " " + companyAddress);
        }
        int i = 1;
        for (Container container : containers) {
            if (i <= 5) {
                values.put("NUMERO_CONTENEUR_" + i, container.getContNumber());
            }
        }

        if (treatmentInfos != null) {
            if (treatmentInfos.getItemFlow() != null && treatmentInfos.getItemFlow().getSender() != null) {
                String controllerName = treatmentInfos.getItemFlow().getSender().getFirstName() + " " + treatmentInfos.getItemFlow().getSender().getLastName();
                values.put("NOM_CONTROLEUR", controllerName);
            }
            if (treatmentInfos.isCcsMinsanteDrugProducts()) {
                values.put("PDT_MEDICAMENTS", treatmentInfos.isDrugs()? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                values.put("PDT_DISPO_MEDICAUX", treatmentInfos.isMedicalDevices()? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                values.put("PDT_DERMOCO", treatmentInfos.isTropicalCorticosteroids()? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                values.put("PDT_PRODUIT_LABO", treatmentInfos.isLaboratoryProducts()? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                values.put("PDT_EMB_PRODUITS_FINIS", treatmentInfos.isPackagingSfProducts()? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                
                values.put("DOC_CONFORME_A", treatmentInfos.isConformeA()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_A", treatmentInfos.getConformityObservationA());
                
                values.put("DOC_CONFORME_AMM", treatmentInfos.isConformeAMM()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_AMM", treatmentInfos.getConformityObservationAMM());
                
                values.put("DOC_CONFORME_AI", treatmentInfos.isConformeAI()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_AI", treatmentInfos.getConformityObservationAI());
                
                values.put("DOC_CONFORME_VT", treatmentInfos.isConformeVT()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_VT", treatmentInfos.getConformityObservationVT());
                
                values.put("DOC_CONFORME_AOI", treatmentInfos.isConformeAOI()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_AOI", treatmentInfos.getConformityObservationAOI());
                
                values.put("DOC_CONFORME_CC", treatmentInfos.isConformeCC()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_CC", treatmentInfos.getConformityObservationCC());
                
                values.put("DOC_CONFORME_CBPSD", treatmentInfos.isConformeCBPSD()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_CBPSD", treatmentInfos.getConformityObservationCBPSD());
                
            } else {
                values.put("PDT_DENREES_AL", treatmentInfos.isProductFoodIHC() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                values.put("PDT_FRIPERIE", treatmentInfos.isThriftShop() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                values.put("PDT_BROCANTE", treatmentInfos.isFleaMarket() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                values.put("PDT_VEHICULE", treatmentInfos.isVehicle() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                values.put("PDT_HYG_ASS", treatmentInfos.isHygienSanitationProducts() ? FORM_FIELD_CHECKBOX_CHECKED_VALUE : FORM_FIELD_CHECKBOX_NOCHECKED_VALUE);
                
                values.put("DOC_CONFORME_A", treatmentInfos.isConformeA()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_A", treatmentInfos.getConformityObservationA());
                
                values.put("DOC_CONFORME_AMM_AMC", treatmentInfos.isConformeAmmAmc()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_AMM_AMC", treatmentInfos.getConformityObservationAmmAmc());
                
                values.put("DOC_CONFORME_AI", treatmentInfos.isConformeAI()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_AI", treatmentInfos.getConformityObservationAI());
                
                values.put("DOC_CONFORME_ATQ", treatmentInfos.isConformeATQ()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_ATQ", treatmentInfos.getConformityObservationATQ());
                
                values.put("DOC_CONFORME_CF_CD", treatmentInfos.isConformeCFCD()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_CFCD", treatmentInfos.getConformityObservationCFCD());
                
                values.put("DOC_CONFORME_CC", treatmentInfos.isConformeCC()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_CC", treatmentInfos.getConformityObservationCC());
                
                values.put("DOC_CONFORME_CAPC_M", treatmentInfos.isConformeCAPCM()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_CAPCM", treatmentInfos.getConformityObservationCAPCM());
                
                values.put("DOC_CONFORME_CE", treatmentInfos.isConformeCE()? CONFORME_O : CONFRME_N);
                values.put("DOC_OBSERVATIONS_CE", treatmentInfos.getConformityObservationCE());
            }
        }

        Iterator<PDField> fieldsIter = acroForm.getFieldIterator();
        while (fieldsIter.hasNext()) {
            PDField field = fieldsIter.next();
            String fieldName = field.getPartialName();
            String fieldValue = values.get(fieldName);
            if (fieldValue != null) {
                field.setValue(fieldValue);
            }
            field.setReadOnly(true);
        }
    }

    private void numbersForSameGlobalField(String fieldPrefix, Map<String, String> values, String currentValue, int nbFields) {

        if (StringUtils.isBlank(currentValue)) {
            return;
        }

        try {
            currentValue = new DecimalFormat(StringUtils.repeat("0", nbFields)).format(Integer.parseInt(currentValue));
        } catch (Exception ex) {
            logger.error(Objects.toString(ex), ex);
        }

        for (int i = 0; i < nbFields; i++) {
            values.put(String.format("%s_%s", fieldPrefix, i + 1), Objects.toString(currentValue.charAt(i)));
        }
    }

    /**
     * this method is used for testing purpose. It shouldn't be used. That's wy
     * it's private. So, to use it, we have to do introspection
     *
     * @param realPath
     */
    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    @Override
    protected JRBeanCollectionDataSource getReportDataSource() {
        return null;
    }

}
