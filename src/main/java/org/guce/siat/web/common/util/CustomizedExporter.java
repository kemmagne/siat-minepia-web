package org.guce.siat.web.common.util;

import ar.com.fdvs.dj.domain.constants.Page;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.guce.siat.common.model.File;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.core.ct.filter.CteFilter;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.data.GlobalQuantityListingData;
import org.guce.util.exporter.CommonExporter;
import org.guce.util.exporter.ExcelExporter;
import org.guce.util.exporter.PDFExporter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.slf4j.LoggerFactory;

/**
 * The Class CustomizedExporter.
 */
@ManagedBean(name = "exporter")
@ApplicationScoped
public class CustomizedExporter {

    /**
     * The Constant LOG.
     */
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CustomizedExporter.class);

    /**
     * Export pdf.
     *
     * @param table the table
     * @param filename the filename
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void exportPDF(DataTable table, String filename) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new CustomPDFExporter();
        exporter.export(context, table, filename, false, false, Constants.ISO_8859_1_ENCODING_TYPE, null, null);
        context.responseComplete();
    }

    /**
     * Export pdf using guce-utils-exporter.
     *
     * @param table the table
     */
    public void exportFiles(DataTable table) {
        try {
            PDFExporter exporter = new PDFExporter();
            exporter.setPageSizeAndOrientation(Page.Page_A4_Landscape());
            exporter.setTitle("DOSSIERS DU TABLEAU DE BORD - ".concat(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime())));

            Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

            List<String> list = new ArrayList<>();
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("FileLabel_eforce_num"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("FileLabel_num_eguce"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("FileLabel_num_siat"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("FileLabel_date_dossier"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("processName"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("FileItemInformationLabel_step"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("GeneralInformationLabel_Client"));
            int nbColumns = list.size();
            String[] columnsNames = list.toArray(new String[nbColumns]);
            exporter.setColumnsNames(columnsNames);

            List<String[]> data = new ArrayList<>();
            Object value = table.getValue();
            List listOfValues = List.class.cast(value);
            for (Object object : listOfValues) {
                File file = File.class.cast(object);
                list = new ArrayList<>();
                list.add(file.getNumeroDemande());
                list.add(file.getNumeroDossier());
                list.add(file.getReferenceSiat());
                list.add(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(file.getCreatedDate()));
                list.add(file.getFileType().getLabelFr());
                list.add(file.getRedefinedLabelFr());
                list.add(file.getClient().getCompanyName());
                data.add(list.toArray(new String[nbColumns]));
            }

            exporter.setData(data);

            String fileName = "DASHBOARD-FILES-".concat(new SimpleDateFormat("yyyyMMdd_HHmm").format(Calendar.getInstance().getTime())).concat(".pdf");
            String mimeType = "application/pdf";

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType(mimeType);
            ec.setResponseHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            OutputStream output = ec.getResponseOutputStream();
            output.write(exporter.getByteArray());
            fc.responseComplete();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public void exportGlobalQuantityListingPDF(DataTable table, CteFilter filter) throws IOException {
        PDFExporter exporter = new PDFExporter();
        exporter.setPageSizeAndOrientation(Page.Page_A4_Landscape());
        exportGlobalQuantityListing(exporter, table, filter, false);
    }

    public void exportGlobalQuantityListingExcel(DataTable table, CteFilter filter) throws IOException {
        ExcelExporter exporter = new ExcelExporter();
        exportGlobalQuantityListing(exporter, table, filter, false);
    }

    private void exportGlobalQuantityListing(CommonExporter exporter, DataTable table, CteFilter filter, boolean detail) throws IOException {
        try {
            exporter.setTitle("LISTING DES QUANTITES : ".concat(new SimpleDateFormat("dd/MM/yyyy").format(filter.getCreationFromDate())).concat(" - ").concat(new SimpleDateFormat("dd/MM/yyyy").format(filter.getCreationToDate())));

            Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            List<String> list = new ArrayList<>();
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("FileLabel_eforce_num"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("FileLabel_num_eguce"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("FileLabel_num_siat"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("FileLabel_date_dossier"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("processName"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("FileItemInformationLabel_step"));
            list.add(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, locale).getString("GeneralInformationLabel_Client"));
            int nbColumns = list.size();
            String[] columnsNames = list.toArray(new String[nbColumns]);
            exporter.setColumnsNames(columnsNames);

            List<String[]> data = new ArrayList<>();
            List<GlobalQuantityListingData> globalQuantityListingDataList = (List<GlobalQuantityListingData>) table.getValue();
            for (GlobalQuantityListingData globalQuantityListingData : globalQuantityListingDataList) {

                list = new ArrayList<>();
//                list.add(file.getNumeroDemande());
//                list.add(file.getNumeroDossier());
//                list.add(file.getReferenceSiat());
//                list.add(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(file.getCreatedDate()));
//                list.add(file.getFileType().getLabelFr());
//                list.add(file.getRedefinedLabelFr());
//                list.add(file.getClient().getCompanyName());

                data.add(list.toArray(new String[nbColumns]));
            }

            exporter.setData(data);

            String fileName = "LISTING-QUANTITES";
            if (detail) {
                fileName = fileName.concat("-DETAIL");
            }
            fileName = fileName.concat("-").concat(new SimpleDateFormat("yyyyMMdd_HHmm").format(Calendar.getInstance().getTime())).concat(".pdf");
            String mimeType = "application/pdf";

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType(mimeType);
            ec.setResponseHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            OutputStream output = ec.getResponseOutputStream();
            output.write(exporter.getByteArray());
            fc.responseComplete();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
