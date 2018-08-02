package org.guce.siat.web.reports.exporter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.guce.siat.web.reports.vo.CtCctCpEFileItemVo;
import org.guce.siat.web.reports.vo.CtCctCpEFileVo;

/**
 * The Class CtCctCpEExporter.
 */
public class CtCctCpEExporter extends AbstractReportInvoker {

    /**
     * The file.
     */
    private final File file;
    /**
     * the treatment order
     */
    private final TreatmentInfos treatmentInfos;

    /**
     * Instantiates a new ct cct cp e exporter.
     *
     * @param file the file
     */
    public CtCctCpEExporter(final File file) {
        super("CT_CCT_CP_E", "CT_CCT_CP_E");
        this.file = file;
        this.treatmentInfos = null;
    }

    public CtCctCpEExporter(final TreatmentInfos treatmentInfos, final String jasperFileName) {
        super(jasperFileName, jasperFileName + ".pdf");
        this.file = treatmentInfos.getItemFlow().getFileItem().getFile();
        this.treatmentInfos = treatmentInfos;
    }

    public CtCctCpEExporter(final File file, final TreatmentInfos treatmentInfos, final String jasperFileName) {
        super(jasperFileName, jasperFileName + ".pdf");
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

        if (file == null) {
            return null;
        }

        final CtCctCpEFileVo ctCctCpEFileVo = new CtCctCpEFileVo();

        ctCctCpEFileVo.setDecisionPlace(file.getBureau().getLabelFr());
        ctCctCpEFileVo.setDecisionDate(file.getSignatureDate());
        if (file.getAssignedUser() != null) {
            ctCctCpEFileVo.setSignatoryName(file.getAssignedUser().getLastName() + " " + file.getAssignedUser().getFirstName());
        }
        if (file.getClient() != null) {
            ctCctCpEFileVo.setExporterName(file.getClient().getCompanyName());
            ctCctCpEFileVo.setExporterAddress(file.getClient().getFirstAddress());
        }
        ctCctCpEFileVo.setOrigin(file.getCountryOfOrigin() != null ? (file.getCountryOfOrigin().getCountryNameFr() != null ? file.getCountryOfOrigin().getCountryNameFr() : file.getCountryOfOrigin().getCountryName()) : null);
        ctCctCpEFileVo.setDestination(file.getCountryOfDestination() != null ? (file.getCountryOfDestination().getCountryNameFr() != null ? file.getCountryOfDestination().getCountryNameFr() : file.getCountryOfDestination().getCountryName()) : null);

        // traitement
        if (treatmentInfos != null) {
            ctCctCpEFileVo.setTypeOfTreatment(treatmentInfos.getTypeOfTreatment());
            ctCctCpEFileVo.setAdditionalInfos(treatmentInfos.getAdditionalInfos());
            ctCctCpEFileVo.setFumigation(treatmentInfos.isFumigation());
            ctCctCpEFileVo.setDisenfection(treatmentInfos.isDisinfection());
            ctCctCpEFileVo.setChemicalProduct(treatmentInfos.getChemicalProduct());
            ctCctCpEFileVo.setDuration(treatmentInfos.getDuration());
            ctCctCpEFileVo.setTemperature(treatmentInfos.getTemperature());
            ctCctCpEFileVo.setConcentration(treatmentInfos.getConcentration());
            ctCctCpEFileVo.setTreatmentsCarriedOut(treatmentInfos.getTreatmentsCarriedOut());
            ctCctCpEFileVo.setAdditionalDeclaration(treatmentInfos.getAdditionnalDeclaration());
        }

        final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

        String typeProduit = null;
        String emballage = null;

        if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
            String containersNumbers = null;
            for (final FileFieldValue fileFieldValue : fileFieldValueList) {
                switch (fileFieldValue.getFileField().getCode()) {
                    case "NUMERO_CT_CCT_CP_E":
                        ctCctCpEFileVo.setDecisionNumber(fileFieldValue.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_LIEU_CHARGEMENT_LIBELLE":
                        ctCctCpEFileVo.setDeliveryPlace(fileFieldValue.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
                        ctCctCpEFileVo.setMeansOfTransport(fileFieldValue.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_LIEU_DECHARGEMENT_LIBELLE":
                        ctCctCpEFileVo.setPointOfEntry(fileFieldValue.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_SIGNATAIRE_NOM":
                        if (ctCctCpEFileVo.getSignatoryName() == null) {
                            ctCctCpEFileVo.setSignatoryName(fileFieldValue.getValue());
                        }
                        break;
                    case "DESTINATAIRE_RAISONSOCIALE":
                        ctCctCpEFileVo.setConsigneeName(fileFieldValue.getValue());
                        break;
                    case "DESTINATAIRE_ADRESSE_ADRESSE1":
                        ctCctCpEFileVo.setConsigneeAddress1(fileFieldValue.getValue());
                        break;
                    case "INSPECTION_CONTENEURS_CONTENEUR":
                        containersNumbers = fileFieldValue.getValue();
                        break;
                    case "NUMEROS_LOTS":
                        ctCctCpEFileVo.setLotsNumbers(fileFieldValue.getValue());
                        break;
                    case "TYPE_DOSSIER_EGUCE":
                        ctCctCpEFileVo.setTransit("2".equals(fileFieldValue.getValue()));
                        break;
                    case "TYPE_PRODUIT_CODE":
                        if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                            typeProduit = fileFieldValue.getValue();
                        } else {
                            typeProduit = "CC";
                        }
                        emballage = Utils.getProductTypePackaging().get(typeProduit);
                        break;
                }
            }
            if (StringUtils.isNotBlank(containersNumbers)) {

                final String[] tab1 = containersNumbers.split(";");
                final int size = tab1.length;
                final int positionScelles = "Type".equalsIgnoreCase(tab1[0].split(",")[1]) ? 2 : 1;
                final StringBuilder builder = new StringBuilder();
                for (int i = 1; i < size; i++) {
                    if (StringUtils.isBlank(tab1[i])) {
                        continue;
                    }
                    final String[] tab2 = tab1[i].split(",");
                    builder.append(tab2[0]).append("/").append(tab2[positionScelles]).append(" ");
                }
                ctCctCpEFileVo.setContainersNumbers(builder.substring(0, builder.lastIndexOf(" ")));
            }
        }

        final List<FileItem> fileItemList = file.getFileItemsList();

        final List<CtCctCpEFileItemVo> fileItemVos = new ArrayList<>();

        final StringBuilder commoditiesBuilder = new StringBuilder();
        BigDecimal netWeight = BigDecimal.ZERO;
        BigDecimal grossWeight = BigDecimal.ZERO;
        String mesure = "KG";

        if (CollectionUtils.isNotEmpty(fileItemList)) {
            for (final FileItem fileItem : fileItemList) {

                final String quantity = fileItem.getQuantity();
                if (StringUtils.isNotBlank(quantity)) {
                    netWeight = netWeight.add(new BigDecimal(quantity));
                }

                final CtCctCpEFileItemVo fileItemVo = new CtCctCpEFileItemVo();

                final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

                if (CollectionUtils.isNotEmpty(fileItemFieldValueList)) {
                    Collections.sort(fileItemFieldValueList, new Comparator<FileItemFieldValue>() {
                        @Override
                        public int compare(FileItemFieldValue o1, FileItemFieldValue o2) {
                            return ((Long) (o2.getFileItemField().getId() - o1.getFileItemField().getId())).intValue();
                        }
                    });
                    for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {
                        switch (fileItemFieldValue.getFileItemField().getCode()) {
                            case "NOMBRE_SACS":
                                commoditiesBuilder.append(fileItemFieldValue.getValue()).append(" ").append(emballage).append(" ");
                                break;
                            case "NOMBRE_GRUMES":
                                commoditiesBuilder.append(fileItemFieldValue.getValue()).append(" ").append(emballage).append(" ");
                                break;
                            case "NOM_COMMERCIAL":
                                commoditiesBuilder.append(fileItemFieldValue.getValue()).append("<br/>");
                                break;
                            case "UNITE":
                                mesure = fileItemFieldValue.getValue();
                                break;
                            case "VOLUME":
                                if (StringUtils.isNotBlank(fileItemFieldValue.getValue())) {
                                    netWeight = netWeight.add(new BigDecimal(fileItemFieldValue.getValue()));
                                }
                                break;
                            case "POIDS_BRUT":
                                if (StringUtils.isNotBlank(fileItemFieldValue.getValue())) {
                                    grossWeight = grossWeight.add(new BigDecimal(fileItemFieldValue.getValue()));
                                }
                                break;
                        }
                    }
                }

                fileItemVos.add(fileItemVo);
            }

            final StringBuilder builder = new StringBuilder();
            if (Utils.getCacaProductsTypes().contains(typeProduit)) {
                builder.append("PN : ");
            } else {
                builder.append("VN : ");
            }
            builder.append(netWeight);
            if (Utils.getCacaProductsTypes().contains(typeProduit)) {
                builder.append(" ").append(mesure);
            } else {
                builder.append(" M3");
            }
            builder.append("<br/>").append("PB : ").append(grossWeight.toString()).append(" KG");
            ctCctCpEFileVo.setQuantities(builder.toString());
        }

        ctCctCpEFileVo.setFileItemList(fileItemVos);
        ctCctCpEFileVo.setNames(commoditiesBuilder.substring(0, commoditiesBuilder.lastIndexOf("<br/>")));

        return new JRBeanCollectionDataSource(Collections.singleton(ctCctCpEFileVo));
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
