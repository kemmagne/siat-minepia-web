package org.guce.siat.web.ct.controller.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.SelectItem;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.reports.exporter.AbstractReportInvoker;
import org.primefaces.context.RequestContext;

/**
 * The Class JsfUtil.
 */
public final class JsfUtil {

    /**
     * The Constant MAIN_GROWL_ID.
     */
    private static final String MAIN_GROWL_ID = "growl";

    /**
     * Instantiates a new jsf util.
     */
    private JsfUtil() {
    }

    /**
     * Adds the error message.
     *
     * @param ex the ex
     * @param defaultMsg the default msg
     */
    public static void addErrorMessage(final Exception ex, final String defaultMsg) {
        final String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    /**
     * Adds the error messages.
     *
     * @param messages the messages
     */
    public static void addErrorMessages(final List<String> messages) {
        for (final String message : messages) {
            addErrorMessage(message);
        }
    }

    /**
     * Adds the error message.
     *
     * @param msg the msg
     */
    public static void addErrorMessage(final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(MAIN_GROWL_ID, facesMsg);
        FacesContext.getCurrentInstance().validationFailed();
    }

    /**
     * Show message in dialog.
     *
     * @param severity the severity
     * @param summary the summary
     * @param detail the detail
     */
    public static void showMessageInDialog(final Severity severity, final String summary, final String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(severity, summary, detail));
    }

    /**
     * Adds the error message.
     *
     * @param key the key
     * @param msg the msg
     */
    public static void addErrorMessage(final String key, final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(key, facesMsg);
        FacesContext.getCurrentInstance().validationFailed();
    }

    /**
     * Adds the error message after redirect.
     *
     * @param msg the msg
     */
    public static void addErrorMessageAfterRedirect(final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        facesContext.addMessage(null, facesMsg);
        facesContext.validationFailed();
    }

    /**
     * Adds the error message after redirect.
     *
     * @param key the key
     * @param msg the msg
     */
    public static void addErrorMessageAfterRedirect(final String key, final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        facesContext.addMessage(key, facesMsg);
        facesContext.validationFailed();
    }

    /**
     * Adds the success message.
     *
     * @param msg the msg
     */
    public static void addSuccessMessage(final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(MAIN_GROWL_ID, facesMsg);
    }

    /**
     * Adds the success message.
     *
     * @param key the key
     * @param msg the msg
     */
    public static void addSuccessMessage(final String key, final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(key, facesMsg);
    }

    /**
     * Adds the success message after redirect.
     *
     * @param msg the msg
     */
    public static void addSuccessMessageAfterRedirect(final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        final FacesContext facesContext = FacesContext.getCurrentInstance();

        final Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        facesContext.addMessage(MAIN_GROWL_ID, facesMsg);
    }

    /**
     * Adds the success message after redirect.
     *
     * @param key the key
     * @param msg the msg
     */
    public static void addSuccessMessageAfterRedirect(final String key, final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        final FacesContext facesContext = FacesContext.getCurrentInstance();

        final Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        facesContext.addMessage(key, facesMsg);

    }

    /**
     * Adds the warning message.
     *
     * @param msg the msg
     */
    public static void addWarningMessage(final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(MAIN_GROWL_ID, facesMsg);
    }

    /**
     * Adds the warning message.
     *
     * @param key the key
     * @param msg the msg
     */
    public static void addWarningMessage(final String key, final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(key, facesMsg);
    }

    /**
     * Gets the root cause.
     *
     * @param cause the cause
     * @return the root cause
     */
    public static Throwable getRootCause(final Throwable cause) {
        if (cause != null) {
            final Throwable source = cause.getCause();
            if (source != null) {
                return getRootCause(source);
            } else {
                return cause;
            }
        }
        return null;
    }

    /**
     * Checks if is validation failed.
     *
     * @return true, if is validation failed
     */
    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }

    /**
     * Checks if is dummy select item.
     *
     * @param component the component
     * @param value the value
     * @return true, if is dummy select item
     */
    public static boolean isDummySelectItem(final UIComponent component, final String value) {
        for (final UIComponent children : component.getChildren()) {
            if (children instanceof UISelectItem) {
                final UISelectItem item = (UISelectItem) children;
                if (item.getItemValue() == null && item.getItemLabel().equals(value)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    /**
     * Adds the error with two message.
     *
     * @param msgHeader the msg header
     * @param msg the msg
     */
    public static void addErrorWithTwoMessage(final String msgHeader, final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msgHeader, msg);
        FacesContext.getCurrentInstance().addMessage(MAIN_GROWL_ID, facesMsg);
        //		FacesContext.getCurrentInstance().validationFailed();
    }

    /**
     * Adds the success with two message.
     *
     * @param msgHeader the msg header
     * @param msg the msg
     */
    public static void addSuccessWithTwoMessage(final String msgHeader, final String msg) {
        final FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msgHeader, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed();
    }

    /**
     * Gets the hours options.
     *
     * @return the hours options
     */
    public static SelectItem[] getHoursOptions() {
        final SelectItem[] hoursOptions = new SelectItem[Constants.TWENTYFOUR];

        hoursOptions[Constants.ZERO] = new SelectItem(Constants.STRING_ZERO, Constants.HOUR_ZERO);
        hoursOptions[Constants.ONE] = new SelectItem(Constants.STRING_ONE, Constants.HOUR_ONE);
        hoursOptions[Constants.TWO] = new SelectItem(Constants.STRING_TWO, Constants.HOUR_TWO);
        hoursOptions[Constants.THREE] = new SelectItem(Constants.STRING_THREE, Constants.HOUR_THREE);
        hoursOptions[Constants.FOUR] = new SelectItem(Constants.STRING_FOUR, Constants.HOUR_FOUR);
        hoursOptions[Constants.FIVE] = new SelectItem(Constants.STRING_FIVE, Constants.HOUR_FIVE);
        hoursOptions[Constants.SIX] = new SelectItem(Constants.STRING_SIX, Constants.HOUR_SIX);
        hoursOptions[Constants.SEVEN] = new SelectItem(Constants.STRING_SEVEN, Constants.HOUR_SEVEN);
        hoursOptions[Constants.EIGHT] = new SelectItem(Constants.STRING_EIGHT, Constants.HOUR_EIGHT);
        hoursOptions[Constants.NINE] = new SelectItem(Constants.STRING_NINE, Constants.HOUR_NINE);
        hoursOptions[Constants.TEN] = new SelectItem(Constants.STRING_TEN, Constants.HOUR_TEN);
        hoursOptions[Constants.ELEVEN] = new SelectItem(Constants.STRING_ELEVEN, Constants.HOUR_ELEVEN);
        hoursOptions[Constants.TWELVE] = new SelectItem(Constants.STRING_TWELVE, Constants.HOUR_TWELVE);
        hoursOptions[Constants.THIRTEEN] = new SelectItem(Constants.STRING_THIRTEEN, Constants.HOUR_THIRTEEN);
        hoursOptions[Constants.FOURTEEN] = new SelectItem(Constants.STRING_FOURTEEN, Constants.HOUR_FOURTEEN);
        hoursOptions[Constants.FIFTEEN] = new SelectItem(Constants.STRING_FIFTEEN, Constants.HOUR_FIFTEEN);
        hoursOptions[Constants.SIXTEEN] = new SelectItem(Constants.STRING_SIXTEEN, Constants.HOUR_SIXTEEN);
        hoursOptions[Constants.SEVENTEEN] = new SelectItem(Constants.STRING_SEVENTEEN, Constants.HOUR_SEVENTEEN);
        hoursOptions[Constants.EIGHTEEN] = new SelectItem(Constants.STRING_EIGHTEEN, Constants.HOUR_EIGHTEEN);
        hoursOptions[Constants.NINETEEN] = new SelectItem(Constants.STRING_NINETEEN, Constants.HOUR_NINETEEN);
        hoursOptions[Constants.TWENTY] = new SelectItem(Constants.STRING_TWENTY, Constants.HOUR_TWENTY);
        hoursOptions[Constants.TWENTYONE] = new SelectItem(Constants.STRING_TWENTYONE, Constants.HOUR_TWENTYONE);
        hoursOptions[Constants.TWENTYTWO] = new SelectItem(Constants.STRING_TWENTYTWO, Constants.HOUR_TWENTYTWO);
        hoursOptions[Constants.TWENTYTHREE] = new SelectItem(Constants.STRING_TWENTYTHREE, Constants.HOUR_TWENTYTHREE);

        return hoursOptions;
    }

    /**
     * Generate random password.
     *
     * @return the string
     */
    public static String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(Constants.SIX);
    }

    /**
     * Convert to french boolean.
     *
     * @param builder the builder
     * @return the string builder
     */
    public static StringBuilder convertToFrenchBoolean(final StringBuilder builder) {
        StringBuilder convertedBuilder = new StringBuilder();

        if (Boolean.TRUE.toString().equals(builder.toString())) {
            convertedBuilder.append("Oui");
        } else if (Boolean.FALSE.toString().equals(builder.toString())) {
            convertedBuilder.append("Non");
        } else {
            convertedBuilder = builder;
        }

        return convertedBuilder;
    }

    /**
     * Generate random string.
     *
     * @return the string
     */
    public static String generateRandomString() {
        return RandomStringUtils.randomAlphanumeric(Constants.TWENTYFOUR);
    }

    /**
     * Generate report.
     *
     * @param abstractReportInvoker the abstract report invoker
     */
    public static void generateReport(final AbstractReportInvoker abstractReportInvoker) {
        abstractReportInvoker.exportReportToPdf();
    }

    /**
     * Generate array values.
     *
     * @param fileFieldValue the file field value
     * @param rowSeparator the row separator
     * @return the string[]
     */
    public static String[] generateArrayValues(final FileFieldValue fileFieldValue, final String rowSeparator) {
        String[] arrayValues = null;

        if ((fileFieldValue != null) && StringUtils.isNotEmpty(fileFieldValue.getValue())) {
            arrayValues = fileFieldValue.getValue().split(rowSeparator);
        }

        return arrayValues;
    }

    /**
     * Gets the report.
     *
     * @param abstractReportInvoker the abstract report invoker
     * @return the report
     */
    public static byte[] getReport(final AbstractReportInvoker abstractReportInvoker) {
        return abstractReportInvoker.getPdfFile();
    }

    /**
     * Gets the report.
     *
     * @param abstractReportInvoker the abstract report invoker
     * @return the report
     */
    public static JasperPrint getReportJP(final AbstractReportInvoker abstractReportInvoker) {
        return abstractReportInvoker.getJasperReport();
    }

    /**
     * Generate table content.
     *
     * @param arrayValues the array values
     * @param columnSeparator the column separator
     * @return the list
     */
    public static List<Map<Integer, String>> generateTableContent(final String[] arrayValues, final String columnSeparator) {

        List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();

        if (arrayValues != null) {
            final String[] columns = arrayValues[0].split(columnSeparator);
            if (columns != null) {
                final int colNumber = columns.length;
                for (int i = 1; i < arrayValues.length; i++) {
                    final Map<Integer, String> map = new HashMap<Integer, String>();
                    final String[] rows = arrayValues[i].split(columnSeparator);
                    if (rows != null && rows.length == colNumber) {
                        for (int j = 0; j < rows.length; j++) {
                            map.put(j, rows[j]);
                        }
                        list.add(map);
                    } else {
                        list = null;
                        break;
                    }
                }
            }
        }

        return list;
    }

    public static byte[] mergePdf(List<JasperPrint> list) {
        try {
            JasperPrint globalJP = new JasperPrint();

            if (list != null && list.size() > 1) {
                JasperPrint jasperPrint = list.get(0);
                globalJP.setOrientation(jasperPrint.getOrientationValue());
                globalJP.setLocaleCode(jasperPrint.getLocaleCode());
                globalJP.setPageHeight(jasperPrint.getPageHeight());
                globalJP.setPageWidth(jasperPrint.getPageWidth());
                globalJP.setTimeZoneId(jasperPrint.getTimeZoneId());
                globalJP.setName(jasperPrint.getName());
                //Merging into one report
                for (JasperPrint jpPrint : list) {
                    for (JRPrintPage page : jpPrint.getPages()) {
                        globalJP.addPage(page);
                    }
                }
            }
            return JasperExportManager.exportReportToPdf(globalJP);
        } catch (JRException ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
