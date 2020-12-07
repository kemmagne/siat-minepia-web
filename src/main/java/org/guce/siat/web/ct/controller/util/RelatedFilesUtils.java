package org.guce.siat.web.ct.controller.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.FileTypeStep;
import org.guce.siat.common.model.Flow;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.Step;
import org.guce.siat.common.utils.enums.CompanyType;
import org.guce.siat.common.utils.enums.StepCode;
import org.guce.siat.core.ct.service.MinaderStatisticsService;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.FileItemCctDetailController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ht
 */
public final class RelatedFilesUtils {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RelatedFilesUtils.class);

    private static final String LABEL = "label";
    private static final String MAIN_REPORT = "mainReport";
    private static final String FILE = "file";
    private static final String MODIFS = "modifs";

    public static List<FileTypeDto> getRelatedFileTypesInfos(FileItemCctDetailController controller, List<FileType> fileTypes) {

        List<FileTypeDto> fileTypeDtos = new ArrayList<>();

        for (FileType fileType : fileTypes) {

            FileTypeDto fileTypeDto = new FileTypeDto();

            fileTypeDto.setFileType(fileType);
            try {
                fileTypeDto.setInfos(getLineMap(controller, fileType));
            } catch (Exception ex) {
                LOG.error(controller.getCurrentFile().toString(), ex);
            }

            fileTypeDtos.add(fileTypeDto);
        }

        return fileTypeDtos;
    }

    public static Map<String, Object> getLineMap(FileItemCctDetailController controller, FileType fileType) {

        Map<String, Object> line = new HashMap<>();

        File currentFile = controller.getCurrentFile();
        List<File> files = controller.getFileService().findByNumeroDemandeAndFileType(currentFile.getNumeroDemande(), fileType);
        List<File> modifFiles = new ArrayList<>();

        if (files.isEmpty()) {

            try {
                String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, controller.getCurrentLocale()).getString("AucuneResultat");
                line.put(LABEL, msg);
            } catch (Exception ex) {
                line.put(LABEL, "Aucun dossier");
            }

            return line;
        }

        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File next = iterator.next();
            if (next.getParent() != null) {
                modifFiles.add(next);
                iterator.remove();
            } else if (StepCode.ST_CT_05.equals(next.getFileItemsList().get(0).getStep().getStepCode())) {
                iterator.remove();
            }
        }

        iterator = modifFiles.iterator();
        while (iterator.hasNext()) {
            File next = iterator.next();
            if (next.getSignatureDate() == null) {
                iterator.remove();
            }
        }

        if (files.isEmpty()) {

            try {
                String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, controller.getCurrentLocale()).getString("AucuneResultat");
                line.put(LABEL, msg);
            } catch (Exception ex) {
                line.put(LABEL, "Aucun dossier");
            }

            return line;
        }

        File file = files.get(0);
        FileItem fileItem = file.getFileItemsList().get(0);
        Step currentStep = fileItem.getStep();
        ItemFlow lastDecision = controller.getItemFlowService().findLastItemFlowByFileItem(fileItem);
        Flow currentFlow = lastDecision.getFlow();

        line.put(FILE, file);
        line.put(MODIFS, modifFiles);
        line.put(MAIN_REPORT, StepCode.ST_CT_06.equals(currentStep.getStepCode()));

        List<String> labels = new ArrayList<>();
        labels.add(file.getNumeroDossier());
        String stepLabel = currentStep.getLabelFr();
        FileTypeStep fts = controller.getFileTypeStepService().findFileTypeStepByFileTypeAndStep(file.getFileType(), currentStep);
        if (fts != null) {
            stepLabel = fts.getLabelFr();
        }
        labels.add(stepLabel);

        Flow ciFlow = controller.getFlowService().findCiResponseFlow(currentFlow.getCode());
        boolean ci = ciFlow != null;
        if (ci) {
            labels.add(CompanyType.DECLARANT.name());
        }

        boolean treatment = MinaderStatisticsService.TREATMENT_STEPS_CODES.contains(currentStep.getStepCode());
        if (treatment && file.getAssignedUser() != null) {
            labels.add(String.format("%s", file.getAssignedUser().getLastName()));
        }

        line.put(LABEL, StringUtils.join(labels, ", "));

        return line;
    }

    private RelatedFilesUtils() {
    }

}
