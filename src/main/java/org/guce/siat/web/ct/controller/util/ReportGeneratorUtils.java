package org.guce.siat.web.ct.controller.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.guce.siat.common.lookup.ServiceUtility;
import org.guce.siat.common.model.Container;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileTypeFlowReport;
import org.guce.siat.common.model.Flow;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.FileFieldValueService;
import org.guce.siat.common.service.FileTypeFlowReportService;
import org.guce.siat.common.service.FlowService;
import org.guce.siat.common.service.ItemFlowService;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.core.ct.model.ApprovedDecision;
import org.guce.siat.core.ct.model.CCTCPParamValue;
import org.guce.siat.core.ct.model.InspectionReport;
import org.guce.siat.core.ct.model.TreatmentInfos;
import org.guce.siat.core.ct.model.TreatmentResult;
import org.guce.siat.core.ct.service.CCTCPParamValueService;
import org.guce.siat.core.ct.service.InspectionReportService;
import org.guce.siat.core.ct.service.PottingReportService;
import org.guce.siat.core.ct.service.TreatmentInfosService;
import org.guce.siat.core.ct.service.TreatmentResultService;
import org.guce.siat.core.ct.service.UserStampSignatureService;
import org.guce.siat.core.ct.util.enums.CctExportProductType;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.reports.exporter.AbstractReportInvoker;
import org.guce.siat.web.reports.exporter.CctCsvExporter;
import org.guce.siat.web.reports.exporter.CtCctCpEExporter;
import org.guce.siat.web.reports.exporter.CtCctCqeExporter;
import org.guce.siat.web.reports.exporter.CtCctTreatmentExporter;
import org.guce.siat.web.reports.exporter.CtPviExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tadzotsa
 */
public final class ReportGeneratorUtils {

    /**
     * The Constant logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportGeneratorUtils.class);

    public static final String COUNT_GOODS = "goods";
    public static final String COUNT_CONTAINERS = "containers";
    public static final String COUNT_PACKAGES = "packages";

    public static final String CP_COTCOCAF = "CERTIFICAT_PHYTOSANITAIRE_CACAO_CAFE_COTON";
    public static final String CP_BOIS_CONV = "CERTIFICAT_PHYTOSANITAIRE_BOIS_CONVENTIONNEL";
    public static final String CP_DEF_ANNEX = "CERTIFICAT_PHYTOSANITAIRE_ANNEXE";
    public static final String CP_ANNEX_AUTRES = "CERTIFICAT_PHYTOSANITAIRE_ANNEXE_AUTRES";

    private ReportGeneratorUtils() {
    }

    public static byte[] generateReportBytes(FileFieldValueService fileFieldValueService, Class classe, File currentFile) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        final Constructor c1;
        final byte[] report;
        if (FileTypeCode.VT_MINEPDED.equals(currentFile.getFileType().getCode())) {
            c1 = classe.getConstructor(String.class, File.class);
            FileFieldValue fileFieldValue = fileFieldValueService.findValueByFileFieldAndFile(ControllerConstants.TYPE_OF_TECHNICAL_VISA, currentFile);
            if (fileFieldValue == null) {
                report = JsfUtil.getReport((AbstractReportInvoker) c1.newInstance(currentFile));
            } else {
                report = JsfUtil.getReport((AbstractReportInvoker) c1.newInstance(fileFieldValue.getValue(), currentFile));
            }

        } else {
            c1 = classe.getConstructor(File.class);
            report = JsfUtil.getReport((AbstractReportInvoker) c1.newInstance(currentFile));
        }
        return report;
    }

    public static byte[] getMinepiaReportBytes(File file, FileTypeFlowReport fileTypeFlowReport) throws Exception {

        UserStampSignatureService userStampSignatureService = ServiceUtility.getBean(UserStampSignatureService.class);
        User loggedUser = ServiceUtility.getBean(User.class);
        ApprovedDecision approvedDecision = ServiceUtility.getBean(ApprovedDecision.class);

        String nomClasse = fileTypeFlowReport.getReportClassName();
        @SuppressWarnings("rawtypes")
        Class classe = Class.forName(nomClasse);
        byte[] report = null;
        AbstractReportInvoker reportInvoker = null;

        Flow flow = fileTypeFlowReport.getFlow();
        if (BooleanUtils.toBoolean(flow.getToStep().getIsFinal())) {
            if (FileTypeCode.VT_MINEPIA.equals(file.getFileType().getCode())) {
                Constructor c1 = classe.getConstructor(File.class);
                reportInvoker = (AbstractReportInvoker) c1.newInstance(file);
                reportInvoker.setUserStampSignatureService(userStampSignatureService);
                report = JsfUtil.getReport(reportInvoker);
            }
            if (FileTypeCode.CCT_CSV.equals(file.getFileType().getCode())) {
                reportInvoker = new CctCsvExporter(file, loggedUser, approvedDecision);
                report = JsfUtil.getReport(reportInvoker);
            }
        }
        return report;
    }

    public static void fillCCTCPParamValue(CCTCPParamValue cCTCPParamValue, User user, File currentFile) {
        cCTCPParamValue.setMaxContainerNumber(12);
        cCTCPParamValue.setMaxGoodsLineNumber(6);
        cCTCPParamValue.setMaxPackageNumber(12);
        cCTCPParamValue.setLabelMore("Voir Annexe");
    }

    public static Map<String, Integer> countFileContainerAndPackage(File file) {
        Map<String, Integer> count = new HashMap<>();

        count.put(COUNT_GOODS, 0);
        count.put(COUNT_CONTAINERS, 0);
        count.put(COUNT_PACKAGES, 0);

        final List<FileItem> fileItemList = file.getFileItemsList();
        count.put(COUNT_GOODS, fileItemList.size());
        final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
        if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
            String containersNumbers = null;
            String packageNumber = null;
            for (final FileFieldValue fileFieldValue1 : fileFieldValueList) {
                switch (fileFieldValue1.getFileField().getCode()) {
                    case "INSPECTION_CONTENEURS_CONTENEUR":
                        containersNumbers = fileFieldValue1.getValue();
                        break;
                    case "NUMEROS_LOTS":
                        packageNumber = fileFieldValue1.getValue();
                        break;
                }
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(containersNumbers)) {
                final String[] tab1 = containersNumbers.split(";");
                final int size = tab1.length;
                final int positionScelles = "Type".equalsIgnoreCase(tab1[0].split(",")[1]) ? 2 : 1;
                final StringBuilder builder = new StringBuilder();
                for (int i = 1; i < size; i++) {
                    if (org.apache.commons.lang.StringUtils.isBlank(tab1[i])) {
                        continue;
                    }
                    final String[] tab2 = tab1[i].split(",");
                    builder.append(tab2[0]).append("/").append(tab2[positionScelles]).append(" ");
                }
                String containerNumbers[] = builder.substring(0, builder.lastIndexOf(" ")).split(" ");
                count.put(COUNT_CONTAINERS, containerNumbers.length);
            } else {
                List<Container> containers = file.getContainers();
                if (CollectionUtils.isNotEmpty(containers)) {
                    count.put(COUNT_CONTAINERS, containers.size());
                }
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(packageNumber)) {
                String packagesNumbers[] = packageNumber.split(" ");
                count.put(COUNT_PACKAGES, packagesNumbers.length);
            }
        }
        return count;
    }

    public static byte[] downloadReportByFile(File file) {

        if (file == null) {
            return null;

        }

        FlowService flowService = ServiceUtility.getBean(FlowService.class
        );
        FileTypeFlowReportService fileTypeFlowReportService = ServiceUtility.getBean(FileTypeFlowReportService.class
        );

        FileItem pfi = file.getFileItemsList().get(0);
        final Flow reportingFlow = flowService.findByToStep(pfi.getStep(), file.getFileType());

        List<FileTypeFlowReport> ftfr = fileTypeFlowReportService.findReportClassNameByFlowAndFileType(reportingFlow, file.getFileType());

        if (CollectionUtils.isEmpty(ftfr)) {
            return null;
        }

        for (final FileTypeFlowReport fileTypeFlowReport : ftfr) {
            try {
                final byte[] report = getMinepiaReportBytes(file, fileTypeFlowReport);
                return report;
            } catch (Exception e) {
                LOGGER.error(file.toString(), e);
            }

        }

        return null;
    }

}
