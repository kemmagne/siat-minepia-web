package org.guce.siat.web.reports.exporter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.guce.siat.common.service.FileFieldValueService;
import org.guce.siat.common.service.FileItemFieldValueService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class JasperReportBuilder.
 */
public abstract class AbstractReportInvoker implements ReportCommand {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractReportInvoker.class);

    /**
     * The pdf file name.
     */
    protected String pdfFileName;

    /**
     * The jasper file name.
     */
    protected String jasperFileName;

    private boolean draft;
    private FileFieldValueService fileFieldValueService;

    /**
     * Instantiates a new jasper report builder.
     *
     * @param jasperFileName the jasper file name
     * @param pdfFileName the pdf file name
     */
    public AbstractReportInvoker(final String jasperFileName, final String pdfFileName) {
        this.jasperFileName = jasperFileName;
        this.pdfFileName = pdfFileName;
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.vo.JasperExporter#generateReport()
     */
    @Override
    public void exportReportToPdf() {
        try {
            // the method for generating the document in pdf format
            final JasperPrint jasperPrint = JasperFillManager.fillReport(getRealPath(REPORTS_PATH, jasperFileName, "jasper"),
                    getJRParameters(), getReportDataSource());
            final byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            final HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance()
                    .getExternalContext().getResponse();

            //set pdf type
            try ( // Provides an output stream for sending binary data
                    ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
                //set pdf type
                httpServletResponse.setContentType("application/pdf");
                httpServletResponse.setContentLength(bytes.length);
                httpServletResponse.setHeader("Content-disposition", getPdfFileName());
                servletOutputStream.write(bytes);
                servletOutputStream.flush();
            }
            FacesContext.getCurrentInstance().responseComplete();
        } catch (final JRException | IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Gets the JR parameters.
     *
     * @return the JR parameters
     */
    protected Map<String, Object> getJRParameters() {
        //Initialize the global parameters we're going to need
        final Map<String, Object> jRParameters = new HashMap<>();
        jRParameters.put("SIAT_LOGO", getRealPath(IMAGES_PATH, SIAT_LOGO, "png"));
        jRParameters.put("DRAFT", draft);
        return jRParameters;
    }

    /**
     * Gets the real path.
     *
     * @param relativePath the relative path
     * @param fileName the file name
     * @param fileExtension the file extension
     * @return the real path
     */
    protected String getRealPath(final String relativePath, final String fileName, final String fileExtension) {
        final StringBuilder builder = new StringBuilder();
        builder.append(File.separator);
        builder.append(relativePath);
        builder.append(File.separator);
        builder.append(fileName);
        builder.append(".");
        builder.append(fileExtension);
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath(builder.toString());
    }

    /**
     * Gets the pdf file name.
     *
     * @return the pdfFileName
     */
    public String getPdfFileName() {
        final StringBuilder builder = new StringBuilder();
        builder.append("attachment; filename=");
        builder.append(pdfFileName);
        builder.append(".pdf");
        return builder.toString();
    }

    /**
     * Gets the pdf file.
     *
     * @return the pdf file
     */
    public byte[] getPdfFile() {
        byte[] pdfReport = null;
        try {
            final JasperPrint jasperPrint = JasperFillManager.fillReport(getRealPath(REPORTS_PATH, jasperFileName, "jasper"),
                    getJRParameters(), getReportDataSource());
            pdfReport = JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (final JRException e) {
            LOG.error(Objects.toString(e), e);
        }
        return pdfReport;
    }

    /**
     * Gets the report data source.
     *
     * @return the report data source
     */
    protected abstract JRBeanCollectionDataSource getReportDataSource();

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public FileFieldValueService getFileFieldValueService() {
        return fileFieldValueService;
    }

    public void setFileFieldValueService(FileFieldValueService fileFieldValueService) {
        this.fileFieldValueService = fileFieldValueService;
    }

}

