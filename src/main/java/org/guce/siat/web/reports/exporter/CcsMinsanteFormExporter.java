package org.guce.siat.web.reports.exporter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.guce.siat.common.model.Item;
import org.guce.siat.common.utils.DateUtils;
import org.springframework.util.Assert;

/**
 *
 * @author etl
 */
public class CcsMinsanteFormExporter extends AbstractReportInvoker {

    private static final String FORM_FIELD_CHECKBOX_CHECKED_VALUE = "OK";
    private static final String FORM_FIELD_CHECKBOX_NOCHECKED_VALUE = "Off";

    /**
     * The decimal format
     */
    private DecimalFormat decimalFormat;

    /**
     * The file.
     */
    private final File file;

    public CcsMinsanteFormExporter(File file) {
        super("CCS_MINSANTE", "CCS_MINSANTE");
        this.file = file;

        initDecimalFormat();
    }

    public CcsMinsanteFormExporter(File file, String pdfFormFileName) {
        super(pdfFormFileName, "CCS_MINSANTE");
        this.file = file;

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
                blNumber = StringUtils.EMPTY, goodsVolume = StringUtils.EMPTY,
                container20Number = StringUtils.EMPTY, container40Number = StringUtils.EMPTY, originCountry = StringUtils.EMPTY,
                makerCountry = StringUtils.EMPTY;

        for (FileFieldValue fileFieldValue : fileFieldValueList) {
            switch (fileFieldValue.getFileField().getCode()) {
                case "DOCUMENTS_NUMERO_DI":
                    diNumber = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_NUMERO_BL":
                    blNumber = fileFieldValue.getValue() != null ? fileFieldValue.getValue() : "";
                    break;
                case "TRANSPORT_PAYS_PROVENANCE_NOM_PAYS":
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

            }
        }
        values.put("NUMERO_CCS", file.getNumeroDossier());
        if (file.getSignatory() != null) {
            values.put("NOM_SIGNATAIRE_1", file.getSignatory().getLastName());
            values.put("NOM_SIGNATAIRE_2", file.getSignatory().getFirstName() + " " + file.getSignatory().getLastName());
        }
        values.put("NUMERO_BL", blNumber);
        values.put("NUMERO_DI", diNumber);
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

//        int length = currentValue.length();
//        int remaining = nbFields - length;
//
//        for (int i = 0; i < remaining; i++) {
//            values.put(String.format("%s_%s", fieldPrefix, i + 1), "0");
//        }
//
//        for (int i = remaining; i < nbFields; i++) {
//            values.put(String.format("%s_%s", fieldPrefix, i + 1), Objects.toString(currentValue.charAt(i - remaining)));
//        }
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
