package org.guce.siat.web.reports.exporter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.lookup.ServiceUtility;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.service.FileService;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.core.ct.model.TreatmentResult;
import org.guce.siat.web.ct.controller.util.Utils;
import static org.guce.siat.web.reports.exporter.ReportCommand.IMAGES_PATH;
import org.guce.siat.web.reports.vo.CtCctTreatmentFileItemVo;
import org.guce.siat.web.reports.vo.CtCctTreatmentFileVo;

/**
 *
 * @author tadzotsa
 */
public class CtCctTreatmentExporter extends AbstractReportInvoker {

    private final File file;
    private final TreatmentResult treatmentResult;
    private final String referenceNumber;

    public CtCctTreatmentExporter(File file, String jasperFileName, TreatmentResult treatmentResult) {
        super(jasperFileName, jasperFileName);
        this.file = file;
        this.treatmentResult = treatmentResult;
        this.referenceNumber = null;
    }

    public CtCctTreatmentExporter(File file, String jasperFileName, TreatmentResult treatmentResult, String referenceNumber) {
        super(jasperFileName, jasperFileName);
        this.file = file;
        this.treatmentResult = treatmentResult;
        this.referenceNumber = referenceNumber;
    }

    @Override
    protected JRBeanCollectionDataSource getReportDataSource() {
        CtCctTreatmentFileVo treatmentVo = new CtCctTreatmentFileVo();
        treatmentVo.setFileNumber(file.getNumeroDossier());
        if (null != file.getClient()) {
            treatmentVo.setExporterName(file.getClient().getCompanyName());
            treatmentVo.setExporterBp(file.getClient().getPostalCode());
            treatmentVo.setExporterEmail(file.getClient().getEmail());
            treatmentVo.setExporterFax(file.getClient().getFax());
            treatmentVo.setExporterTel(file.getClient().getPhone());
            treatmentVo.setExporterAddress(file.getClient().getFullAddress());
        }

        // find DT file number
        FileService fileService = ServiceUtility.getBean(FileService.class);
        List<File> files = fileService.findByNumeroDemandeAndFileType(file.getNumeroDemande(), FileTypeCode.CCT_CT_E_FSTP);
        for (File file1 : files) {
            if (file1.getNumeroDossier().endsWith("DT")) {
                treatmentVo.setDtFileNumber(file1.getNumeroDossier());
                break;
            }
        }

        List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
        String lotNumber = "";
        for (FileFieldValue fileFieldValue : fileFieldValueList) {
            switch (fileFieldValue.getFileField().getCode()) {
//                case "NUMERO_CCT_CT_E_AT":
//                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
//                        treatmentVo.setDecisionNumber(fileFieldValue.getValue());
//                    }
//                    break;
                case "NUMERO_CCT_CT_E_FSTP":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        treatmentVo.setDecisionNumber(fileFieldValue.getValue());
                    }
                    break;
                case "DESTINATAIRE_RAISONSOCIALE":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        treatmentVo.setConsigneeName(fileFieldValue.getValue());
                    }
                    break;
                case "DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        treatmentVo.setConsigneeCountry(fileFieldValue.getValue());
                    }
                    break;
                case "INFORMATIONS_GENERALES_PAYS_DESTINATION_NOMPAYS":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        treatmentVo.setDestinationCountry(fileFieldValue.getValue());
                    }
                    break;
                case "TRAITEMENT_SOCIETE_TRAITEMENT_NOM":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        treatmentVo.setTreatmentCompanyName(fileFieldValue.getValue());
                    }
                    break;
                case "TRAITEMENT_SOCIETE_TRAITEMENT_ADRESSE":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        treatmentVo.setTreatmentCompanyAddress(fileFieldValue.getValue());
                    }
                    break;
                case "TRAITEMENT_SOCIETE_TRAITEMENT_TELEPHONE":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        treatmentVo.setTreatmentCompanyTel(fileFieldValue.getValue());
                    }
                    break;
                case "TRAITEMENT_SOCIETE_TRAITEMENT_FAX":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        treatmentVo.setTreatmentCompanyFax(fileFieldValue.getValue());
                    }
                    break;
                case "TRAITEMENT_SOCIETE_TRAITEMENT_EMAIL":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        treatmentVo.setTreatmentCompanyEmail(fileFieldValue.getValue());
                    }
                    break;
                case "TRAITEMENT_SOCIETE_TRAITEMENT_BP":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        treatmentVo.setTreatmentCompanyBp(fileFieldValue.getValue());
                    }
                    break;
                case "NUMEROS_LOTS":
                    lotNumber = fileFieldValue.getValue();
                    break;
            }
        }
        //
        if (file.getSignatory() != null) {
            treatmentVo.setSignatory(String.format("%s %s", file.getSignatory().getLastName(), file.getSignatory().getFirstName()));
        }
        if (file.getAssignedUser() != null) {
            treatmentVo.setInspector(String.format("%s %s", file.getAssignedUser().getLastName(), file.getAssignedUser().getFirstName()));
        }
        treatmentVo.setActiveIngredient(treatmentResult.getActiveIngredient());
        treatmentVo.setApplicationDose(treatmentResult.getTreatmentDose());
        treatmentVo.setConcentration(treatmentResult.getAtConcentration());
        treatmentVo.setConditioning(treatmentResult.getConditioning());
        treatmentVo.setTreatmentDate(treatmentResult.getTreatmentDate());
        treatmentVo.setDecisionDate(file.getSignatureDate());
        treatmentVo.setDecisionPlace(file.getBureau().getLabelFr());
        if (referenceNumber != null && treatmentVo.getDecisionNumber() == null) {
            treatmentVo.setDecisionNumber(referenceNumber);
        }
        if (FileTypeCode.CCT_CT_E_ATP.equals(file.getFileType().getCode()) || FileTypeCode.CCT_CT_E_FSTP.equals(file.getFileType().getCode())) {
            Calendar calendar = Calendar.getInstance();
            Date signatureDate = file.getSignatureDate();
            if (signatureDate != null) {
                calendar.setTime(signatureDate);
            }
            int year = calendar.get(Calendar.YEAR);
            //String fileType = FileTypeCode.CCT_CT_E_ATP.equals(file.getFileType().getCode()) ? "ATP" : "FSTP";
            String fileType = "ATP";
            String numeroDossier = file.getNumeroDossier().replaceAll("DT", "AT");
            String s = String.format("%s/%s/%s/%s/MINADER/SG/DRCQ", file.getNumeroDossier(), file.getNumeroDemande(), year, fileType);
            treatmentVo.setDecisionNumber(s);
        }
        treatmentVo.setGeneralObservations(treatmentResult.getGeneralObservations());
        treatmentVo.setHomologationNumber(treatmentResult.getHomologationNumber());
        treatmentVo.setOptimalTemperature(treatmentResult.getOptimalTemperature());
        treatmentVo.setOtherProductUsed(treatmentResult.getOtherProductUsed());
        treatmentVo.setOtherStoragePlace(treatmentResult.getOtherStoragePlace());
        treatmentVo.setOtherTreatmentEnvironment(treatmentResult.getOtherTreatmentEnvironment());
        treatmentVo.setOtherWeatherCondition(treatmentResult.getOtherWeatherCondition());
        treatmentVo.setPreventionPlaquePresent(treatmentResult.isPreventionPlaquePresent());
        treatmentVo.setResidentsInformations(treatmentResult.isResidentsInformations());
        treatmentVo.setSanitaryState(treatmentResult.isSanitaryState());
        treatmentVo.setTreatmentPrevention(treatmentResult.isTreatmentPrevention());
        treatmentVo.setProductComName(treatmentResult.getTreatmentProductName());
        treatmentVo.setProductUsed(treatmentResult.getProductUsed());
        treatmentVo.setProtectionEquipements(treatmentResult.getProtectionEquipements());
        treatmentVo.setStaffSecurityNature(treatmentResult.getStaffSecurityNature());
        treatmentVo.setStoragePlace(treatmentResult.getStoragePlace());
        treatmentVo.setTreatmentDose(treatmentResult.getDosage());
        treatmentVo.setTreatmentDuration(treatmentResult.getTreatmentDuration());
        treatmentVo.setTreatmentEnvironment(treatmentResult.getTreatmentEnvironment());
        treatmentVo.setTreatmentMode(treatmentResult.getTreatmentMode());
        treatmentVo.setTreatmentTime(treatmentResult.getTreatmentTime());
        treatmentVo.setTreatmentType(treatmentResult.getTreatmentType());
        treatmentVo.setTssConcentration(treatmentResult.getTsfConcentration());
        treatmentVo.setUncoveringDate(treatmentResult.getUncoveringDate());
        treatmentVo.setWeatherCondition(treatmentResult.getWeatherCondition());
        treatmentVo.setOtherTreatmentMode(treatmentResult.getOtherTreatmentMode());
        treatmentVo.setSupervisor(treatmentResult.getSupervisor());

        FileFieldValue productTypeFileFieldValue = getFileFieldValueService().findValueByFileFieldAndFile("TYPE_PRODUIT_CODE", file);
        String productType = productTypeFileFieldValue != null ? productTypeFileFieldValue.getValue() : null;
        if (productType == null) {
            throw new RuntimeException(String.format("The product type cannot be null : %s", file));
        }

        List<FileItem> fileItemList = file.getFileItemsList();
        List<CtCctTreatmentFileItemVo> fileItemVos = new ArrayList<>(fileItemList.size());
        BigDecimal totalVolume = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(fileItemList)) {

            BigDecimal itemQuantity = BigDecimal.ZERO;
            for (FileItem fileItem : fileItemList) {

                CtCctTreatmentFileItemVo fileItemVo = new CtCctTreatmentFileItemVo();
                fileItemVo.setProductLabel("PRODUIT");
                fileItemVo.setQuantityLabel("NOMBRE DE COLIS");
                fileItemVo.setVolumeWeightLabel("VOLUME");
                //
                if (fileItem.getNsh() != null) {
                    fileItemVo.setCode(fileItem.getNsh().getGoodsItemCode());
                }

                FileItemFieldValue tradeNameFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOM_COMMERCIAL", fileItem);
                if (tradeNameFieldValue != null) {
                    fileItemVo.setNature(tradeNameFieldValue.getValue());
                } else {
                    FileItemFieldValue nb = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOM_BOTANIQUE", fileItem);
                    if (nb != null) {
                        fileItemVo.setNature(fileItemVo.getNature() + " (" + nb.getValue() + ")");
                    } else {
                        fileItemVo.setNature("");
                    }
                }
                treatmentVo.setItemNature(fileItemVo.getNature());

                FileItemFieldValue volumeFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("VOLUME", fileItem);
                if (volumeFieldValue != null) {
                    fileItemVo.setVolume(volumeFieldValue.getValue());
                    totalVolume = totalVolume.add(new BigDecimal(volumeFieldValue.getValue()));
                }

                if (Utils.getCacaProductsTypes().contains(productType)) {
                    fileItemVo.setVolumeWeightLabel("POIDS");
                    fileItemVo.setQuantityLabel("NUMERO DES LOTS");
//                    fileItemVo.setWeight(fileItem.getQuantity());
                    fileItemVo.setVolume(fileItem.getQuantity());
                    fileItemVo.setNumber(lotNumber);
                    String nsfv = "";
                    FileItemFieldValue nsf = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOMBRE_SACS", fileItem);
                    if (nsf != null) {
                        nsfv = nsf.getValue();
                    }
                    String unit = Utils.getProductTypePackaging().get(productType);
                    fileItemVo.setNature(String.format("%s %s %s", nsfv, unit, fileItemVo.getNature()));
                } else if (Utils.getWoodProductsTypes().contains(productType)) {
                    if (productType.equalsIgnoreCase("GR")) {
                        fileItemVo.setQuantityLabel("NOMBRE DE GRUMES");
                        fileItemVo.setProductLabel("ESSENCE");
                    }
                    FileItemFieldValue ng = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOMBRE_GRUMES", fileItem);
                    if (ng != null) {
                        fileItemVo.setNumber(ng.getValue());
                    }

                    if (fileItemVo.getNumber() == null) {
                        fileItemVo.setNumber(fileItem.getQuantity());
                    }
                } else if (Utils.COTONPRODUCTTYPE.equalsIgnoreCase(productType)) {
                    fileItemVo.setQuantityLabel("NOMBRE DE BALLES");
                    fileItemVo.setVolumeWeightLabel("POIDS");
                    FileItemFieldValue ng = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOMBRE_GRUMES", fileItem);
                    if (ng != null) {
                        fileItemVo.setNumber(ng.getValue());
                    }
                    FileItemFieldValue grossWeightFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("POIDS_BRUT", fileItem);
                    if (grossWeightFieldValue != null) {
                        fileItemVo.setVolume(grossWeightFieldValue.getValue());
                    }
                    if (grossWeightFieldValue == null) {
                        FileItemFieldValue weightFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("POIDS", fileItem);
                        if (weightFieldValue != null) {
                            fileItemVo.setVolume(weightFieldValue.getValue());
                        }
                    }
                } else {
                    fileItemVo.setNumber(fileItem.getQuantity());
                }
                if (Utils.getCacaProductsTypes().contains(productType)) {
                    itemQuantity = itemQuantity.add(new BigDecimal(fileItem.getQuantity()));
                } else {
                    itemQuantity = itemQuantity.add(new BigDecimal(fileItemVo.getNumber()));
                }

                fileItemVos.add(fileItemVo);
            }

            treatmentVo.setItemQuantity(itemQuantity.toString());
            if (Utils.getCacaProductsTypes().contains(productType)) {
                treatmentVo.setItemQuantity(treatmentVo.getItemQuantity().concat(" KG"));
            } else if (Utils.COTONPRODUCTTYPE.equalsIgnoreCase(productType)) {
                treatmentVo.setItemQuantity(treatmentVo.getItemQuantity().concat(" BALLES"));
            } else if (Utils.getWoodProductsTypes().contains(productType)) {
                if (totalVolume.compareTo(BigDecimal.ZERO) > 0) {
                    treatmentVo.setItemQuantity(totalVolume.toString().concat(" M3"));
                } else {
                    treatmentVo.setItemQuantity(treatmentVo.getItemQuantity().concat(" M3"));
                }
            } else {
                FileItemFieldValue unitFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("UNITE", fileItemList.get(0));
                if (unitFieldValue != null) {
                    treatmentVo.setItemQuantity(treatmentVo.getItemQuantity().concat(" ").concat(unitFieldValue.getValue()));
                } else {
                    treatmentVo.setItemQuantity(treatmentVo.getItemQuantity().concat(" KG"));
                }
            }
        }
        treatmentVo.setFileItemList(fileItemVos);

        return new JRBeanCollectionDataSource(Collections.singleton(treatmentVo));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getJRParameters()
     */
    @Override
    protected Map<String, Object> getJRParameters() {
        Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "phytosanitaire", "jpg"));
        return jRParameters;
    }

}
