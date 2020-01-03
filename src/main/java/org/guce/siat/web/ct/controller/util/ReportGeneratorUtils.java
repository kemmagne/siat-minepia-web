package org.guce.siat.web.ct.controller.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.service.FileFieldValueService;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.reports.exporter.AbstractReportInvoker;

/**
 *
 * @author tadzotsa
 */
public class ReportGeneratorUtils {

    private ReportGeneratorUtils() {
    }

    public static byte[] generateReportBytes(FileFieldValueService fileFieldValueService, Class classe, File currentFile) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        final Constructor c1;
        final byte[] report;
        if (FileTypeCode.VT_MINEPDED.equals(currentFile.getFileType().getCode())) {
            c1 = classe.getConstructor(String.class, File.class);
            FileFieldValue fileFieldValue = fileFieldValueService.findValueByFileFieldAndFile(ControllerConstants.TYPE_OF_TECHNICAL_VISA, currentFile);
            if (fileFieldValue == null){
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

}
