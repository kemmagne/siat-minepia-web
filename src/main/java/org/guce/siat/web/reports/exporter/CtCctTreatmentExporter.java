package org.guce.siat.web.reports.exporter;

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
import org.guce.siat.core.ct.model.TreatmentResult;
import static org.guce.siat.web.reports.exporter.ReportCommand.IMAGES_PATH;
import org.guce.siat.web.reports.vo.CtCctTreatmentFileItemVo;
import org.guce.siat.web.reports.vo.CtCctTreatmentFileVo;

/**
 *
 * @author tadzotsa
 */
public class CtCctTreatmentExporter extends AbstractReportInvoker {

    private final TreatmentResult treatmentResult;

    public CtCctTreatmentExporter(String jasperFileName, TreatmentResult treatmentResult) {
        super(jasperFileName, jasperFileName);
        this.treatmentResult = treatmentResult;
    }

    @Override
    protected JRBeanCollectionDataSource getReportDataSource() {
        final CtCctTreatmentFileVo treatmentVo = new CtCctTreatmentFileVo();
        final File file = treatmentResult.getItemFlow().getFileItem().getFile();
        treatmentVo.setFileNumber(file.getNumeroDossier());
        if (null != file.getClient()) {
            treatmentVo.setExporterName(file.getClient().getCompanyName());
            treatmentVo.setExporterBp(file.getClient().getPostalCode());
            treatmentVo.setExporterEmail(file.getClient().getEmail());
            treatmentVo.setExporterFax(file.getClient().getFax());
            treatmentVo.setExporterTel(file.getClient().getPhone());
            treatmentVo.setExporterAddress(file.getClient().getFullAddress());
        }

        final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
        for (final FileFieldValue fileFieldValue : fileFieldValueList) {
            switch (fileFieldValue.getFileField().getCode()) {
                case "NUMERO_CCT_CT_E_AT":
                    if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
                        treatmentVo.setDecisionNumber(fileFieldValue.getValue());
                    }
                    break;
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
            }
        }
        //
        treatmentVo.setActiveIngredient(treatmentResult.getActiveIngredient());
        treatmentVo.setApplicationDose(treatmentResult.getTreatmentDose());
        treatmentVo.setConcentration(treatmentResult.getAtConcentration());
        treatmentVo.setConditioning(treatmentResult.getConditioning());
        treatmentVo.setDecisionDate(file.getSignatureDate());
        treatmentVo.setDecisionPlace(file.getBureau().getLabelFr());
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

        final List<FileItem> fileItemList = file.getFileItemsList();
        List<CtCctTreatmentFileItemVo> fileItemVos = new ArrayList<>(fileItemList.size());
        if (CollectionUtils.isNotEmpty(fileItemList)) {
            CtCctTreatmentFileItemVo fileItemVo;
            for (final FileItem fileItem : fileItemList) {
                final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
                fileItemVo = new CtCctTreatmentFileItemVo();
                if (CollectionUtils.isNotEmpty(fileItemFieldValueList)) {
                    for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {
                        switch (fileItemFieldValue.getFileItemField().getCode()) {
                            case "QUANTITE_TOTALE":
                                fileItemVo.setNumber(fileItemFieldValue.getValue());
                                treatmentVo.setItemQuantity(fileItemFieldValue.getValue());
                                break;
                            case "POIDS":
                                fileItemVo.setWeight(fileItemFieldValue.getValue());
                                break;
                            case "VOLUME":
                                fileItemVo.setVolume(fileItemFieldValue.getValue());
                                break;
                            case "NATURE":
                                fileItemVo.setNature(fileItemFieldValue.getValue());
                                treatmentVo.setItemNature(fileItemFieldValue.getValue());
                                break;
                        }
                    }
                }
                //
                if (fileItem.getNsh() != null) {
                    fileItemVo.setCode(fileItem.getNsh().getGoodsItemCode());
                }
                fileItemVo.setNumber(fileItem.getQuantity());
                fileItemVos.add(fileItemVo);
                //fileItem.get
            }
        }
        treatmentVo.setFileItemList(fileItemVos);
        if (treatmentVo.getItemQuantity() == null) {
            treatmentVo.setItemQuantity(fileItemVos.get(0).getWeight() + " KG");
        }

        return new JRBeanCollectionDataSource(Collections.singleton(treatmentVo));
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getJRParameters()
     */
    @Override
    protected Map<String, Object> getJRParameters() {
        final Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "minader", "jpg"));
        return jRParameters;
    }

}

