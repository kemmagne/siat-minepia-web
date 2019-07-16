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

        final CtCctCpEFileVo ctCctCpEFileVo = new CtCctCpEFileVo();

        ctCctCpEFileVo.setDecisionPlace(file.getBureau().getLabelFr());
        ctCctCpEFileVo.setDecisionDate(file.getSignatureDate());
        if (file.getSignatory() != null) {
            ctCctCpEFileVo.setSignatoryName(file.getSignatory().getLastName() + " " + file.getSignatory().getFirstName());
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
            ctCctCpEFileVo.setTreatmentDate(treatmentInfos.getTreatmentDate());
        }

        final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

        final String productType = getFileFieldValueService()
                .findValueByFileFieldAndFile("TYPE_PRODUIT_CODE", file).getValue();
        String emballage = Utils.getProductTypePackaging().get(productType);
		ctCctCpEFileVo.setPackaging(productType);

        if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
            String containersNumbers = null;
            for (final FileFieldValue fileFieldValue1 : fileFieldValueList) {
                switch (fileFieldValue1.getFileField().getCode()) {
                    case "NUMERO_CT_CCT_CP_E":
                        ctCctCpEFileVo.setDecisionNumber(fileFieldValue1.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_LIEU_CHARGEMENT_LIBELLE":
                        ctCctCpEFileVo.setDeliveryPlace(fileFieldValue1.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
                        ctCctCpEFileVo.setMeansOfTransport(fileFieldValue1.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_LIEU_DECHARGEMENT_LIBELLE":
                        ctCctCpEFileVo.setPointOfEntry(fileFieldValue1.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_SIGNATAIRE_NOM":
                        if (ctCctCpEFileVo.getSignatoryName() == null) {
                            ctCctCpEFileVo.setSignatoryName(fileFieldValue1.getValue());
                        }
                        break;
                    case "DESTINATAIRE_RAISONSOCIALE":
                        ctCctCpEFileVo.setConsigneeName(fileFieldValue1.getValue());
                        break;
                    case "DESTINATAIRE_ADRESSE_ADRESSE1":
                        ctCctCpEFileVo.setConsigneeAddress1(fileFieldValue1.getValue());
                        break;
                    case "INSPECTION_CONTENEURS_CONTENEUR":
                        containersNumbers = fileFieldValue1.getValue();
                        break;
                    case "NUMEROS_LOTS":
                        ctCctCpEFileVo.setLotsNumbers(fileFieldValue1.getValue());
                        break;
                    case "TYPE_DOSSIER_EGUCE":
                        ctCctCpEFileVo.setTransit("2".equals(fileFieldValue1.getValue()));
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
		ctCctCpEFileVo.setDecisionNumber(file.getNumeroDossier());

        final List<FileItem> fileItemList = file.getFileItemsList();

        final List<CtCctCpEFileItemVo> fileItemVos = new ArrayList<>();

        BigDecimal netWeight = BigDecimal.ZERO;
        BigDecimal grossWeight = BigDecimal.ZERO;

        if (CollectionUtils.isNotEmpty(fileItemList)) {

            final List<String> commoditiesList = new ArrayList<>();

            String unit = getFileFieldValueService()
                    .findFileItemFieldValueByCodeAndFileItem("UNITE", fileItemList.get(0)).getValue();

            FileItemFieldValue fileItemFieldValue;
            for (final FileItem fileItem : fileItemList) {

                String comName = "";
                fileItemFieldValue = getFileFieldValueService()
                        .findFileItemFieldValueByCodeAndFileItem("NOM_COMMERCIAL", fileItem);
                if (fileItemFieldValue != null) {
                    comName = fileItemFieldValue.getValue();
                }
				FileItemFieldValue botanicFileItemFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOM_BOTANIQUE", fileItem);
				if (botanicFileItemFieldValue != null){
					comName += " (" + botanicFileItemFieldValue.getValue() + ")";
				}
                String nb = "";
                if (Utils.getCacaProductsTypes().contains(productType)) {
					FileItemFieldValue nsf = getFileFieldValueService()
							.findFileItemFieldValueByCodeAndFileItem("NOMBRE_SACS", fileItem);
					if (nsf != null){
						nb = nsf.getValue();
					}
                    
                    netWeight = netWeight.add(new BigDecimal(fileItem.getQuantity()));
                } else if (Utils.getWoodProductsTypes().contains(productType)) {
					FileItemFieldValue ngf = getFileFieldValueService()
							.findFileItemFieldValueByCodeAndFileItem("NOMBRE_GRUMES", fileItem);
					if (ngf != null){
						nb = ngf.getValue();
					} else {
						nb = fileItem.getQuantity();
					}
                    
                    final String volumeStr = getFileFieldValueService()
                            .findFileItemFieldValueByCodeAndFileItem("VOLUME", fileItem).getValue();
                    netWeight = netWeight.add(new BigDecimal(volumeStr));
                } else {
					FileItemFieldValue nsf = getFileFieldValueService()
							.findFileItemFieldValueByCodeAndFileItem("NOMBRE_SACS", fileItem);
					if (nsf != null){
						nb = nsf.getValue();
					}
                    emballage = unit;
                    netWeight = netWeight.add(new BigDecimal(fileItem.getQuantity()));
                }
                commoditiesList.add(String.format("%s %s %s", nb, emballage, comName));

                fileItemFieldValue = getFileFieldValueService()
                        .findFileItemFieldValueByCodeAndFileItem("POIDS_BRUT", fileItem);
                if (fileItemFieldValue != null) {
                    grossWeight = grossWeight.add(new BigDecimal(fileItemFieldValue.getValue()));
                }
            }

            final StringBuilder builder = new StringBuilder();
            if (Utils.getCacaProductsTypes().contains(productType)) {
                builder.append("PN : ");
            } else {
                builder.append("VN : ");
            }
            if (Utils.getWoodProductsTypes().contains(productType)) {
                unit = "M3";
            }
            builder.append(netWeight).append(" ").append(unit);
            builder.append("<br/>").append("PB : ").append(grossWeight.toString()).append(" KG");
            ctCctCpEFileVo.setQuantities(builder.toString());

            ctCctCpEFileVo.setNames(StringUtils.join(commoditiesList, "<br/>"));
        }

        ctCctCpEFileVo.setFileItemList(fileItemVos);

        return new JRBeanCollectionDataSource(Collections.singleton(ctCctCpEFileVo));
    }

    @Override
    protected Map<String, Object> getJRParameters() {
        final Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "phytosanitaire", "jpg"));
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

