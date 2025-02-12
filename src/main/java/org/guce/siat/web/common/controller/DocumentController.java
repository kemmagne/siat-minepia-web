package org.guce.siat.web.common.controller;

import java.io.Serializable;
import java.util.Base64;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.service.FileService;
import org.guce.siat.web.ct.controller.util.ReportGeneratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tadzotsa
 */
@ViewScoped
@ManagedBean
public class DocumentController implements Serializable {

    private static final long serialVersionUID = 9055497961438838435L;

    /**
     * The Constant logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The file service.
     */
    @ManagedProperty(value = "#{fileService}")
    protected FileService fileService;

    private String pageTitle;

    private String fileNumer;

    private String fileType;

    private String niu;

    // PREVIEW_CT124648_CCT_CT_E_AT-3.pdf
    private String documentPath;

    private String documentNotFound;
    
    private byte[] reportBytes;

    @PostConstruct
    public void init() {

        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            String encodedParams = requestParameterMap.get("params");
            if (StringUtils.isNotEmpty(encodedParams)) {
                byte[] decodedBytes = Base64.getDecoder().decode(encodedParams);
                String decodedParams = new String(decodedBytes);
                if (StringUtils.isNotEmpty(decodedParams)) {
                    String[] paramsList = decodedParams.split(",");
                    if (paramsList != null && paramsList.length > 0) {
                        fileNumer = paramsList[0];
                        fileType = paramsList[1];
                        niu = paramsList[2];
                    }
                }
            }
            if (StringUtils.isEmpty(fileNumer)) {
                documentNotFound = "FileNotFound";
                return;
            }
            File file = fileService.findByNumDossierGuce(fileNumer);
            if (file == null) {
                documentNotFound = "FileNotFound";
                return;
            }
            reportBytes = ReportGeneratorUtils.downloadReportByFile(file);
            String pdfFileName = String.format("%s.pdf", file.getNumeroDossier());
            String pdfFilePath = getRealPath("pages/unsecure", "document", "xhtml");
            java.io.File pdfFileParent = new java.io.File(pdfFilePath);
            java.io.File pdfFile = new java.io.File(pdfFileParent.getParentFile(), pdfFileName);
            FileUtils.writeByteArrayToFile(pdfFile, reportBytes);
            documentPath = pdfFileName;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            documentNotFound = "CannotGetDocument";
        }
    }

    /**
     * Gets the real path.
     *
     * @param relativePath the relative path
     * @param fileName the file name
     * @param fileExtension the file extension
     * @return the real path
     */
    private String getRealPath(final String relativePath, final String fileName, final String fileExtension) {
        StringBuilder builder = new StringBuilder();
        builder.append(java.io.File.separator);
        builder.append(relativePath);
        builder.append(java.io.File.separator);
        builder.append(fileName);
        builder.append(".");
        builder.append(fileExtension);
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath(builder.toString());
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public String getDocumentNotFound() {
        return documentNotFound;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
    
}
