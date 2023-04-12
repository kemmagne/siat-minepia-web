package org.guce.siat.web.reports.exporter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.service.ApplicationPropretiesService;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.web.reports.vo.VtMinepiaFileItemVo;
import org.guce.siat.web.reports.vo.VtMinepiaFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.guce.siat.common.lookup.ServiceUtility;
import org.guce.siat.web.ct.controller.util.Utils;
import org.guce.siat.common.utils.QRCodeGenerator;

/**
 * The Class VtMinepiaExporter.
 */
public class VtMinepiaExporter extends AbstractReportInvoker {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(VtMinepiaExporter.class);

    /**
     * The file.
     */
    private final File file;

    /**
     * Instantiates a new vt minepia exporter.
     *
     * @param file the file
     */
    public VtMinepiaExporter(final File file) {
        super("VT_MINEPIA", "VT_MINEPIA");
        this.file = file;
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        final VtMinepiaFileVo vtMinepiaVo = new VtMinepiaFileVo();

        if ((file != null)) {
            final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

            if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
                for (final FileFieldValue fileFieldValue : fileFieldValueList) {
                    switch (fileFieldValue.getFileField().getCode()) {
                        case "NUMERO_VT_MINEPIA":
                            vtMinepiaVo.setDecisionNumber(fileFieldValue.getValue());
                            break;
                        case "FACTURE_NUMERO_FACTURE":
                            vtMinepiaVo.setInvoice(fileFieldValue.getValue());
                            break;
                        case "PAYS_ORIGINE_LIBELLE":
                            vtMinepiaVo.setCountryOfOrigin(fileFieldValue.getValue());
                            break;
                        case "PAYS_PROVENANCE_LIBELLE":
                            vtMinepiaVo.setCountryOfProvenance(fileFieldValue.getValue());
                            break;
                        case "FOURNISSEUR_RAISONSOCIALE":
                            vtMinepiaVo.setProvider(fileFieldValue.getValue());
                            break;
                        case "SIGNATAIRE_DATE":
                            if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                                try {
                                    vtMinepiaVo.setDecisionDate(new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue()));
                                    vtMinepiaVo.setSignatureDate(fileFieldValue.getValue());
                                } catch (Exception e) {
                                    LOG.error(Objects.toString(e), e);
                                }
                            }
                            break;
                        case "SIGNATAIRE_LIEU":
                            vtMinepiaVo.setDecisionPlace(fileFieldValue.getValue());
                            break;
                        default:
                            break;
                    }
                }
            }
            String numeroDossier = file.getParent() != null ? file.getParent().getNumeroDossier() : file.getNumeroDossier();
            vtMinepiaVo.setDecisionNumber(numeroDossier);
            if (StringUtils.isEmpty(vtMinepiaVo.getDecisionPlace())) {
                vtMinepiaVo.setDecisionPlace(file.getBureau().getCityFr());
            }
            if(file.getBureau().getRegionFr() != null){
                vtMinepiaVo.setRegionFr(file.getBureau().getRegionFr().toUpperCase());
            }
            
            if(file.getBureau().getRegionEn() != null){
                vtMinepiaVo.setRegionEn(file.getBureau().getRegionEn().toUpperCase());
            }

            if ((file.getClient() != null)) {
                vtMinepiaVo.setImporter(file.getClient().getCompanyName());
                vtMinepiaVo.setAddress(file.getClient().getFullAddress());
                vtMinepiaVo.setProfession(file.getClient().getProfession());
            }

            if (file.getSignatory() != null) {
                vtMinepiaVo.setSignatory(String.format("%s %s", file.getSignatory().getFirstName(), file.getSignatory().getLastName()));
            }
            if (file.getSignatureDate() != null) {
                String signatureDate = DateUtils.formatSimpleDate("dd/MM/yyyy", file.getSignatureDate());
                vtMinepiaVo.setSignatureDate(signatureDate);
            }
            vtMinepiaVo.setCode(String.format("%s/%s", file.getNumeroDemande(), numeroDossier));
            final List<FileItem> fileItemList = file.getFileItemsList();

            final List<VtMinepiaFileItemVo> fileItemVos = new ArrayList<VtMinepiaFileItemVo>();

            if (CollectionUtils.isNotEmpty(fileItemList)) {
                for (final FileItem fileItem : fileItemList) {
                    final VtMinepiaFileItemVo fileItemVo = new VtMinepiaFileItemVo();
                    fileItemVo.setCode(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemCode() : null);
                    fileItemVo.setDesc(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);
                    fileItemVo.setQuantity(Double.valueOf(fileItem.getQuantity()));

                    fileItemVos.add(fileItemVo);
                }
            }

            vtMinepiaVo.setFileItemList(fileItemVos);
            String qrContent = Utils.getFinalSecureDocumentUrl(getQrCodeBaseUrl(), file);
            try {
                InputStream qrCode = new ByteArrayInputStream(new QRCodeGenerator().generateQR(qrContent, 512));
                vtMinepiaVo.setQrCode(qrCode);
                if (file.getSignatory() != null) {
                    try {
                        InputStream signatorySignature = getUserStampSignatureService().getUserSignature(file.getSignatory());
                        if (signatorySignature != null) {
                            vtMinepiaVo.setSignatorySignature(signatorySignature);
                        }
                    } catch (Exception ex) {
                        LOG.error(Objects.toString(ex), ex);
                    }
                    try {
                        InputStream signatoryStamp = getUserStampSignatureService().getUserStamp(file.getSignatory());
                        if (signatoryStamp != null) {
                            vtMinepiaVo.setSignatoryStamp(signatoryStamp);
                        }
                    } catch (Exception ex) {
                        LOG.error(Objects.toString(ex), ex);
                    }
                }
                
            } catch (Exception ex) {
                logger.error(file.getNumeroDossier(), ex);
            }

        }

        return new JRBeanCollectionDataSource(Collections.singleton(vtMinepiaVo));
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getJRParameters()
     */
    @Override
    protected Map<String, Object> getJRParameters() {
        final Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINEPIA_LOGO", getRealPath(IMAGES_PATH, "minepia", "jpg"));
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

    
    private String getQrCodeBaseUrl() {
        ApplicationPropretiesService applicationPropretiesService = ServiceUtility.getBean(ApplicationPropretiesService.class);
        String environment = applicationPropretiesService.getAppEnv();
        String baseUrl;
        switch (environment) {
            case "standalone":
            case "production":
                baseUrl = "https://siat.guichetunique.cm";
                break;
            case "test":
                baseUrl = "https://testsiat.guichetunique.cm";
                break;
            default:
                baseUrl = "https://localhost:40081";
                break;
        }
        return baseUrl;
    }

}
