package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.core.ct.model.TreatmentResult;
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
        CtCctTreatmentFileVo treatmentVo = new CtCctTreatmentFileVo();
        File file = treatmentResult.getItemFlow().getFileItem().getFile();
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
            }
        }
        //
        treatmentVo.setActiveIngredient(treatmentResult.getActiveIngredient());
        treatmentVo.setApplicationDose(treatmentResult.getTreatmentDose());
        treatmentVo.setConcentration(treatmentResult.getAtConcentration());
        treatmentVo.setConditioning(treatmentResult.getConditioning());
        treatmentVo.setDecisionDate(new Date());
        treatmentVo.setDecisionNumber("AAA");
        treatmentVo.setDecisionPlace("YAOUNDE");
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
        treatmentVo.setTreatmentCompanyAddress(treatmentResult.getTreatmentOrder().getTreatmentCompany().getAddress());
        treatmentVo.setTreatmentCompanyName(treatmentResult.getTreatmentOrder().getTreatmentCompany().getLabelFr());

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
                //fileItemVo.setCode(fileItem.);
                fileItemVo.setNumber(fileItem.getQuantity());
                //fileItemVo.setNature(fileItem.get)
                //fileItemVo.setVolume(fileItem.get);
                //
                fileItemVos.add(fileItemVo);
            }
        }
        treatmentVo.setFileItemList(fileItemVos);

        return new JRBeanCollectionDataSource(Collections.singleton(treatmentVo));
    }

}

