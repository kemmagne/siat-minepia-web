package org.guce.siat.web.ct.controller.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.Flow;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.Step;
import org.guce.siat.common.utils.enums.CompanyType;
import org.guce.siat.common.utils.enums.StepCode;
import org.guce.siat.web.ct.controller.FileItemCctDetailController;

/**
 *
 * @author ht
 */
public final class RelatedFilesUtils {

    private static final List<StepCode> ADMISSIBILITY_STEPS_CODES = Arrays.asList(StepCode.ST_AM_42);
    private static final List<StepCode> SIGNATURE_STEPS_CODES = Arrays.asList(StepCode.ST_AM_42);
    private static final List<StepCode> TREATMENT_STEPS_CODES = Arrays.asList(StepCode.ST_AM_42);

    private static final String LABEL = "label";
    private static final String MAIN_REPORT = "mainReport";

    public static Map<FileType, Map<String, Object>> getRelatedFileTypesInfos(FileItemCctDetailController controller, List<FileType> fileTypes) {

        Map<FileType, Map<String, Object>> map = new HashMap<>(fileTypes.size());

        for (FileType fileType : fileTypes) {

            Map<String, Object> line = new HashMap<>();

            map.put(fileType, line);
        }

        return map;
    }

    public static Map<String, Object> getLineMap(FileItemCctDetailController controller, FileType fileType) {

        Map<String, Object> line = new HashMap<>();

        File currentFile = controller.getCurrentFile();
        List<File> files = controller.getFileService().findByNumeroDemandeAndFileType(currentFile.getNumeroDemande(), fileType);
        List<File> modifFiles = new ArrayList<>();

        if (files.isEmpty()) {
            return new HashMap<>();
        }

        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File next = iterator.next();
            if (next.getParent() != null) {
                modifFiles.add(next);
                iterator.remove();
            }
            if (!StepCode.ST_CT_06.equals(next.getFileItemsList().get(0).getStep().getStepCode())) {
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

        File file = files.get(0);
        FileItem fileItem = file.getFileItemsList().get(0);
        Step currentStep = fileItem.getStep();
        ItemFlow lastDecision = controller.getItemFlowService().findLastItemFlowByFileItem(fileItem);
        Flow currentFlow = lastDecision != null ? lastDecision.getFlow() : null;

        line.put(MAIN_REPORT, file);

        if (currentFlow == null) {
            return new HashMap<>();
        }

        List<String> labels = new ArrayList<>();
        labels.add(file.getNumeroDossier());
        labels.add(currentStep.getLabelFr());

        Flow ciFlow = controller.getFlowService().findCiResponseFlow(currentFlow.getCode());
        boolean ci = ciFlow != null;
        if (ci) {
            labels.add(CompanyType.DECLARANT.name());
        }

        boolean admissibility = ADMISSIBILITY_STEPS_CODES.contains(currentStep.getStepCode());
        boolean treatment = TREATMENT_STEPS_CODES.contains(currentStep.getStepCode());
        boolean signature = SIGNATURE_STEPS_CODES.contains(currentStep.getStepCode());

        line.put(LABEL, StringUtils.join(labels, ", "));

        return line;
    }

    private RelatedFilesUtils() {
    }

}
