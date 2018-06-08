package org.guce.siat.web.reports.exporter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.core.ct.model.TreatmentInfos;
import org.guce.siat.web.ct.controller.util.Utils;
import static org.guce.siat.web.reports.exporter.ReportCommand.IMAGES_PATH;
import org.guce.siat.web.reports.vo.CtCctCqeFileItemVo;
import org.guce.siat.web.reports.vo.CtCctCqeFileVo;

/**
 * The Class CtCctCsExporter.
 */
public class CtCctCqeExporter extends AbstractReportInvoker {

    /**
     * The file.
     */
    private final File file;
    private final TreatmentInfos treatmentInfos;

    /**
     * Instantiates a new ct cct cs exporter.
     *
     * @param file the file
     */
    public CtCctCqeExporter(final File file) {
        super("CT_CCT_CQE", "CT_CCT_CQE");
        this.file = file;
        this.treatmentInfos = null;
    }

    public CtCctCqeExporter(final TreatmentInfos treatmentInfos) {
        super("CT_CCT_CQE", "CT_CCT_CQE");
        this.file = treatmentInfos.getItemFlow().getFileItem().getFile();
        this.treatmentInfos = treatmentInfos;
    }

    public CtCctCqeExporter(final File file, final TreatmentInfos treatmentInfos) {
        super("CT_CCT_CQE", "CT_CCT_CQE");
        this.file = file;
        this.treatmentInfos = treatmentInfos;
    }


    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        final CtCctCqeFileVo fileVo = new CtCctCqeFileVo();

        if (null == file) {
            return null;
        }

        if (file.getClient() != null) {
            fileVo.setExporterName(file.getClient().getCompanyName());
            fileVo.setExporterAddress(file.getClient().getFirstAddress());
        }
        if (file.getAssignedUser() != null) {
            fileVo.setOfficerName(file.getAssignedUser().getLastName() + " " + file.getAssignedUser().getFirstName());
        }

        fileVo.setCountryOfOrigin(file.getCountryOfOrigin() != null ? (file.getCountryOfOrigin().getCountryNameFr() != null ? file.getCountryOfOrigin().getCountryNameFr() : file.getCountryOfOrigin().getCountryName()) : null);
        fileVo.setCountryOfDestination(file.getCountryOfDestination() != null ? (file.getCountryOfDestination().getCountryNameFr() != null ? file.getCountryOfDestination().getCountryNameFr() : file.getCountryOfDestination().getCountryName()) : null);
        fileVo.setDecisionDate(file.getSignatureDate());
        fileVo.setDecisionPlace(file.getBureau().getLabelFr());
        fileVo.setEguceNumber(file.getNumeroDossier());
        fileVo.setPackager(treatmentInfos.getPackagerOnPackaging());
        fileVo.setPreservationTemperature(treatmentInfos.getConservationTemperature());
        fileVo.setValidity(treatmentInfos.getValidity());
        fileVo.setProductCategory(treatmentInfos.getCommodityCaterory());

        final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

        String typeProduit = null;
        String emballage = null;

        for (final FileFieldValue fileFieldValue : fileFieldValueList) {
            switch (fileFieldValue.getFileField().getCode()) {
                case "NUMERO_CT_CCT_CP_E":
                    fileVo.setCertificateNumber(fileFieldValue.getValue());
                    break;
                case "NUMERO_CT_CCT_CQE":
                    fileVo.setCertificateNumber(fileFieldValue.getValue());
                    break;
                case "DESTINATAIRE_RAISONSOCIALE":
                    fileVo.setConsigneeName(fileFieldValue.getValue());
                    break;
                case "DESTINATAIRE_ADRESSE_ADRESSE1":
                    fileVo.setConsigneeAddress(fileFieldValue.getValue());
                    break;
                case "TRANSITAIRE_RAISONSOCIALE":
                    fileVo.setForwarder(fileFieldValue.getValue());
                    break;
                case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
                    fileVo.setMeanOfTransport(fileFieldValue.getValue());
                    break;
                case "INFORMATIONS_GENERALES_LIEU_CHARGEMENT_LIBELLE":
                    fileVo.setOutPhytoPolice(fileFieldValue.getValue());
                    break;
                case "INFORMATIONS_GENERALES_LIEU_DECHARGEMENT_LIBELLE":
                    fileVo.setEntryPhytoPolice(fileFieldValue.getValue());
                    break;
                case "TYPE_PRODUIT_CODE":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        typeProduit = fileFieldValue.getValue();
                    } else {
                        typeProduit = "CC";
                    }
                    emballage = Utils.getProductTypePackaging().get(typeProduit);
            }
        }

        final List<FileItem> fileItemList = file.getFileItemsList();

        final List<CtCctCqeFileItemVo> fileItemVos = new ArrayList<>();
        CtCctCqeFileItemVo fileItemVo;
        List<FileItemFieldValue> fileItemFieldValueList;
        BigDecimal totalQuantity = BigDecimal.ZERO;
        String measure = " KG";
        for (final FileItem fileItem : fileItemList) {
            fileItemVo = new CtCctCqeFileItemVo();

            fileItemVo.setCategory(fileVo.getProductCategory());
            final String quantity = fileItem.getQuantity();
            if (StringUtils.isNotBlank(quantity) && Utils.getCacaProductsTypes().contains(typeProduit)) {
                fileItemVo.setNetWeight(fileItem.getQuantity());
                totalQuantity = totalQuantity.add(new BigDecimal(quantity));
            }
            fileItemVo.setPacking(emballage);

            fileItemFieldValueList = fileItem.getFileItemFieldValueList();
            if (CollectionUtils.isEmpty(fileItemFieldValueList)) {
                continue;
            }

            for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {
                switch (fileItemFieldValue.getFileItemField().getCode()) {
                    case "NOM_COMMERCIAL":
                        fileItemVo.setItemNature(fileItemFieldValue.getValue());
                        break;
                    case "VOLUME":
                        final String volume = fileItemFieldValue.getValue();
                        if (StringUtils.isNotBlank(volume) && Utils.getWoodProductsTypes().contains(typeProduit)) {
                            fileItemVo.setNetWeight(volume + " (M3)");
                            totalQuantity = totalQuantity.add(new BigDecimal(volume));
                            measure = " M3";
                        }
                        break;
                    case "POIDS_BRUT":
                        fileItemVo.setGrossWeight(fileItemFieldValue.getValue());
                        break;
                }
            }

            fileItemVos.add(fileItemVo);
        }
        fileVo.setTotalQuantity(totalQuantity.toString() + measure);
        fileVo.setFileItemList(fileItemVos);

        return new JRBeanCollectionDataSource(Collections.singleton(fileVo));
    }

    @Override
    protected Map<String, Object> getJRParameters() {
        final Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "minader", "jpg"));
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

}
