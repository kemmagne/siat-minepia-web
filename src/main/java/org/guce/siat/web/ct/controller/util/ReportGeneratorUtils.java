package org.guce.siat.web.ct.controller.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.FileFieldValueService;
import org.guce.siat.common.service.FileTypeFlowReportService;
import org.guce.siat.common.service.FlowService;
import org.guce.siat.common.service.ItemFlowService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.core.ct.model.CCTCPParamValue;
import org.guce.siat.core.ct.model.InspectionReport;
import org.guce.siat.core.ct.model.ParamCCTCP;
import org.guce.siat.core.ct.model.TreatmentInfos;
import org.guce.siat.core.ct.model.TreatmentResult;
import org.guce.siat.core.ct.service.CCTCPParamValueService;
import org.guce.siat.core.ct.service.InspectionReportService;
import org.guce.siat.core.ct.service.ParamCCTCPService;
import org.guce.siat.core.ct.service.PottingReportService;
import org.guce.siat.core.ct.service.TreatmentInfosService;
import org.guce.siat.core.ct.service.TreatmentResultService;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.reports.exporter.AbstractReportInvoker;
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

    public static byte[] getMinaderReportBytes(File file, FileTypeFlowReport fileTypeFlowReport) throws Exception {

        ItemFlowService itemFlowService = ServiceUtility.getBean(ItemFlowService.class);
        TreatmentInfosService treatmentInfosService = ServiceUtility.getBean(TreatmentInfosService.class);
        CCTCPParamValueService cCTCPParamValueService = ServiceUtility.getBean(CCTCPParamValueService.class);
        InspectionReportService inspectionReportService = ServiceUtility.getBean(InspectionReportService.class);
        TreatmentResultService treatmentResultService = ServiceUtility.getBean(TreatmentResultService.class);
        FileFieldValueService fileFieldValueService = ServiceUtility.getBean(FileFieldValueService.class);
        PottingReportService pottingReportService = ServiceUtility.getBean(PottingReportService.class);

        User signatory = file.getSignatory();

        String nomClasse = fileTypeFlowReport.getReportClassName();
        @SuppressWarnings("rawtypes")
        Class classe = Class.forName(nomClasse);
        Map<String, Object> forAnnexes = null;
        byte[] report = null;
        AbstractReportInvoker reportInvoker = null;
        FileItem ffi = file.getFileItemsList().get(0);

        Flow flow = fileTypeFlowReport.getFlow();
        if (BooleanUtils.toBoolean(flow.getToStep().getIsFinal())) {
            switch (file.getFileType().getCode()) {
                case CCT_CT_E: {
                    ItemFlow itemFlow = itemFlowService.findItemFlowByFileItemAndFlow2(ffi, FlowCode.FL_CT_151);
                    if (itemFlow == null) {
                        itemFlow = itemFlowService.findItemFlowByFileItemAndFlow2(ffi, FlowCode.FL_CT_07);
                    }
                    if (itemFlow == null) {
                        itemFlow = itemFlowService.findItemFlowByFileItemAndFlow2(ffi, FlowCode.FL_CT_112);
                    }
                    if (itemFlow == null) {
                        itemFlow = itemFlowService.findItemFlowByFileItemAndFlow2(ffi, FlowCode.FL_CT_117);
                    }
                    final TreatmentInfos ti = treatmentInfosService.findTreatmentInfosByItemFlow(itemFlow);
                    if (ti == null || ti.getId() == null) {
                        return null;
                    }
                    ItemFlow itemFlow2 = itemFlowService.findItemFlowByFileItemAndFlow(file.getFileItemsList().get(0), FlowCode.FL_CT_08);
                    if (itemFlow2 == null) {
                        itemFlow2 = itemFlowService.findItemFlowByFileItemAndFlow(file.getFileItemsList().get(0), FlowCode.FL_CT_114);
                    }
                    CCTCPParamValue paramValue = cCTCPParamValueService.findCCTCPParamValueByItemFlow(itemFlow2);
                    if (paramValue == null) {
                        fillCCTCPParamValue(paramValue, signatory, file);
                    }

                    if (ti.getDelivrableType() == null || "CCT_CT_E".equals(ti.getDelivrableType())) {
                        Map<String, Integer> count = countFileContainerAndPackage(file);
                        if (count.get(COUNT_GOODS) > paramValue.getMaxGoodsLineNumber()
                                || count.get(COUNT_CONTAINERS) > paramValue.getMaxContainerNumber()
                                || count.get(COUNT_PACKAGES) > paramValue.getMaxPackageNumber()) {
                            forAnnexes = new HashMap();
                            forAnnexes.put("ti", ti);
                            forAnnexes.put("paramValue", paramValue);
                            forAnnexes.put("fileNameAnnex", "CERTIFICAT_PHYTOSANITAIRE_ANNEXE");
                        }
                        reportInvoker = new CtCctCpEExporter(file, ti, paramValue, "CERTIFICAT_PHYTOSANITAIRE");

                    } else if ("CQ_CT".equals(ti.getDelivrableType())) {
                        reportInvoker = new CtCctCqeExporter(file, ti);
                    }
                    break;
                }
                case CCT_CT_E_PVI: {
                    ItemFlow itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_07);
                    InspectionReport ir = inspectionReportService.findByItemFlow(itemFlow);
                    reportInvoker = new CtPviExporter(file, ir);
                    break;
                }
                case CCT_CT_E_ATP:
                case CCT_CT_E_FSTP: {
                    ItemFlow itemFlow = itemFlowService.findItemFlowByFileItemAndFlow(ffi, FlowCode.FL_CT_07);
                    TreatmentResult tr = treatmentResultService.findTreatmentResultByItemFlow(itemFlow);
                    if (FileTypeCode.CCT_CT_E_ATP.equals(file.getFileType().getCode())) {
                        reportInvoker = new CtCctTreatmentExporter(file, "CCT_CT_E_ATP", tr);
                    } else {
                        reportInvoker = new CtCctTreatmentExporter(file, "CCT_CT_E_FSTP", tr);
                    }
                    break;
                }
                default:
                    Constructor constructor = classe.getConstructor(File.class);
                    reportInvoker = (AbstractReportInvoker) constructor.newInstance(file);
                    break;
            }
        }

        if (reportInvoker != null) {
            reportInvoker.setFileFieldValueService(fileFieldValueService);
            reportInvoker.setItemFlowService(itemFlowService);
            reportInvoker.setPottingReportService(pottingReportService);
            if (forAnnexes != null) {
                JasperPrint page1 = JsfUtil.getReportJP(reportInvoker);
                TreatmentInfos ti = (TreatmentInfos) forAnnexes.get("ti");
                CCTCPParamValue paramValue = (CCTCPParamValue) forAnnexes.get("paramValue");
                String fileAnnexName = (String) forAnnexes.get("fileNameAnnex");
                CtCctCpEExporter exporter = new CtCctCpEExporter(file, ti, paramValue, fileAnnexName);
                exporter.setFileFieldValueService(fileFieldValueService);
                JasperPrint report2 = JsfUtil.getReportJP(exporter);

                List<JasperPrint> inputStreams = new ArrayList<>();
                inputStreams.add(page1);
                inputStreams.add(report2);

                report = JsfUtil.mergePdf(inputStreams);
            } else {
                report = JsfUtil.getReport(reportInvoker);
            }
        }
        return report;
    }

    public static void fillCCTCPParamValue(CCTCPParamValue cCTCPParamValue, User user, File currentFile) {
        ServiceService serviceService = ServiceUtility.getBean(ServiceService.class);
        ParamCCTCPService paramCCTCPService = ServiceUtility.getBean(ParamCCTCPService.class);

        Service uService = serviceService.findServiceByUser(user);
        ParamCCTCP params = paramCCTCPService.findParamCCTCPByAdministration(uService);
        if (params != null) {
            cCTCPParamValue.setMaxContainerNumber(params.getMaxContainerNumber());
            cCTCPParamValue.setMaxGoodsLineNumber(params.getMaxGoodsLineNumber());
            cCTCPParamValue.setMaxPackageNumber(params.getMaxPackageNumber());
            cCTCPParamValue.setLabelMore(params.getLabelAttachment());
        } else {
            params = paramCCTCPService.findParamCCTCPDefault();
            if (params != null) {
                cCTCPParamValue.setMaxContainerNumber(params.getMaxContainerNumber());
                cCTCPParamValue.setMaxGoodsLineNumber(params.getMaxGoodsLineNumber());
                cCTCPParamValue.setMaxPackageNumber(params.getMaxPackageNumber());
                cCTCPParamValue.setLabelMore(params.getLabelAttachment());
            } else {
                //LOAD DEFAULT NOT REGISTERED VALUE HERE
                cCTCPParamValue.setMaxContainerNumber(12);
                cCTCPParamValue.setMaxGoodsLineNumber(6);
                cCTCPParamValue.setMaxPackageNumber(6);
                cCTCPParamValue.setLabelMore("Voir pièce jointe");
            }
        }

        Map<String, Integer> count = countFileContainerAndPackage(currentFile);
        cCTCPParamValue.setFileGoodsCountValue(count.get(COUNT_GOODS));
        cCTCPParamValue.setFileContainerCountValue(count.get(COUNT_CONTAINERS));
        cCTCPParamValue.setFilePackageCountValue(count.get(COUNT_PACKAGES));
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

        FlowService flowService = ServiceUtility.getBean(FlowService.class);
        FileTypeFlowReportService fileTypeFlowReportService = ServiceUtility.getBean(FileTypeFlowReportService.class);

        FileItem pfi = file.getFileItemsList().get(0);
        final Flow reportingFlow = flowService.findByToStep(pfi.getStep(), file.getFileType());

        List<FileTypeFlowReport> ftfr = fileTypeFlowReportService.findReportClassNameByFlowAndFileType(reportingFlow, file.getFileType());

        if (CollectionUtils.isEmpty(ftfr)) {
            return null;
        }

        for (final FileTypeFlowReport fileTypeFlowReport : ftfr) {
            try {
                final byte[] report = getMinaderReportBytes(file, fileTypeFlowReport);
                return report;
            } catch (Exception e) {
                LOGGER.error(file.toString(), e);
            }

        }

        return null;
    }

}
