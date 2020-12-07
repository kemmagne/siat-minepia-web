package org.guce.siat.web.ct.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.Ministry;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.BureauService;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.BureauType;
import org.guce.siat.common.utils.enums.StepCode;
import org.guce.siat.core.ct.filter.MinaderFileTrackingFilter;
import org.guce.siat.core.ct.model.MinaderFileTracking;
import org.guce.siat.core.ct.service.MinaderStatisticsService;
import org.guce.siat.core.ct.util.enums.CctExportProductType;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ht
 */
@ManagedBean(name = "fileTrackingController")
@SessionScoped
public class FileTrackingController extends AbstractController<File> {

    private static final long serialVersionUID = -4205559173927600324L;

    private static final Logger LOG = LoggerFactory.getLogger(FileTrackingController.class);

    /**
     * The Constant DATE_VALIDATION_ERROR_MESSAGE.
     */
    private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

    @ManagedProperty(value = "#{fileTypeService}")
    private FileTypeService fileTypeService;

    @ManagedProperty(value = "#{bureauService}")
    private BureauService bureauService;

    @ManagedProperty(value = "#{minaderStatisticsService}")
    private MinaderStatisticsService minaderStatisticsService;

    private MinaderFileTrackingFilter minaderFileTrackingFilter;
    private List<MinaderFileTracking> fileTrackingsList;
    private List<FileType> fileTypeList;
    private List<Bureau> bureauList;
    private MinaderFileTracking currentFileTracking;
    private List<User> potentialUsersList;

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        minaderFileTrackingFilter = new MinaderFileTrackingFilter();
    }

    public void doMinaderFilesTrackingFilter() {

        if (minaderFileTrackingFilter.getFromDate() != null && minaderFileTrackingFilter.getToDate() != null && minaderFileTrackingFilter.getFromDate().after(minaderFileTrackingFilter.getToDate())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(DATE_VALIDATION_ERROR_MESSAGE));
            return;
        }

        fileTrackingsList = minaderStatisticsService.retrieveFilesForTracking(minaderFileTrackingFilter);
    }

    public void initDefaultView() {
        minaderFileTrackingFilter = new MinaderFileTrackingFilter();
        initFilter();
        doMinaderFilesTrackingFilter();
    }

    public String displayDateDifference(Long diff) {

        if (diff == null) {
            return null;
        }

        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        int diffhours = (int) ((diff / (60 * 60 * 1000)) % 24);
        int diffmin = (int) ((diff / (60 * 1000)) % 60);
        int diffsec = (int) ((diff / 1000) % 60);

        StringBuilder res = new StringBuilder();

        res.append(diffDays).append(" j ").append(diffhours).append(" h ").append(diffmin).append(" m ").append(diffsec).append(" s");

        return res.toString();
    }

    public void goToFileTrackingPage() {
        try {
            initDefaultView();
            final FacesContext context = FacesContext.getCurrentInstance();
            final ExternalContext extContext = context.getExternalContext();
            final String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, ControllerConstants.Pages.FO.FILES_TRACKING));
            extContext.redirect(url);
        } catch (final IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
        }
    }

    public List<FileType> getFileTypeList() {

        if (fileTypeList == null) {
            fileTypeList = fileTypeService.findDistinctFileTypesByUser(getLoggedUser());
        }

        return fileTypeList;
    }

    public Map<String, String> getProductTypesMap() {
        return CctExportProductType.getProductTypesMap();
    }

    public List<Bureau> getBureauList() {
        if (bureauList == null) {
            if (getLoggedUser() != null) {
                Organism organism = getCurrentOrganism();
                if (organism != null) {
                    bureauList = bureauService.findBureauByTypeAndOrganism(BureauType.BUREAU_CENTRAL, organism);
                }

            }
        }
        return bureauList;
    }

    public MinaderStatisticsService getMinaderStatisticsService() {
        return minaderStatisticsService;
    }

    public void setMinaderStatisticsService(MinaderStatisticsService minaderStatisticsService) {
        this.minaderStatisticsService = minaderStatisticsService;
    }

    public FileTypeService getFileTypeService() {
        return fileTypeService;
    }

    public void setFileTypeService(FileTypeService fileTypeService) {
        this.fileTypeService = fileTypeService;
    }

    public BureauService getBureauService() {
        return bureauService;
    }

    public void setBureauService(BureauService bureauService) {
        this.bureauService = bureauService;
    }

    public MinaderFileTrackingFilter getMinaderFileTrackingFilter() {
        return minaderFileTrackingFilter;
    }

    public void setMinaderFileTrackingFilter(MinaderFileTrackingFilter minaderFileTrackingFilter) {
        this.minaderFileTrackingFilter = minaderFileTrackingFilter;
    }

    public List<MinaderFileTracking> getFileTrackingsList() {
        return fileTrackingsList;
    }

    public void setFileTrackingsList(List<MinaderFileTracking> fileTrackingsList) {
        this.fileTrackingsList = fileTrackingsList;
    }

    public boolean isLocalOffice() {

        boolean ok = false;

        Administration administration = getLoggedUser().getAdministration();

        if (administration instanceof Bureau) {

            Bureau bureau = (Bureau) administration;

            ok = BureauType.BUREAU_REGIONAL.equals(bureau.getBureauType());
            if (ok) {
                minaderFileTrackingFilter.setOfficesList(Arrays.asList(bureau.getId()));
            }
        }

//        if (!ok) {
//            minaderFileTrackingFilter.setOfficesList(new ArrayList<Long>());
//            for (Bureau bureau : getBureauList()) {
//                minaderFileTrackingFilter.getOfficesList().add(bureau.getId());
//            }
//        }
//
        return ok;
    }

    private void initFilter() {
        minaderFileTrackingFilter.setLoggedUser(getLoggedUser());

        minaderFileTrackingFilter.setFileState(MinaderFileTrackingFilter.FileStateFilter.IN_PROCESS);

        minaderFileTrackingFilter.setFileTypesList(new ArrayList<String>());
        for (FileType fileType : getFileTypeList()) {
            minaderFileTrackingFilter.getFileTypesList().add(fileType.getCode().name());
        }

        isLocalOffice();

        /**
         * current month by default
         */
        Calendar calendar = Calendar.getInstance();

        minaderFileTrackingFilter.setToDate(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        minaderFileTrackingFilter.setFromDate(calendar.getTime());
    }

    public boolean isMinader() {

        Ministry ministry = getCurrentMinistry();
        boolean ok = ministry != null && Constants.MINADER_MINISTRY.equals(ministry.getCode());

        return ok;
    }

    public void retrievePotentialAgents(MinaderFileTracking fileTracking) {

        potentialUsersList = minaderStatisticsService.retrievePotentialAgents(fileTracking);

        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('potentialUsersListDialog').show();");
    }

    public boolean isNotRejected(MinaderFileTracking fileTracking) {
        boolean ok = !StepCode.ST_CT_05.equals(fileTracking.getCurrenStep().getStepCode()) && !StepCode.ST_CT_54.equals(fileTracking.getCurrenStep().getStepCode());
        return ok;
    }

    public MinaderFileTracking getCurrentFileTracking() {
        return currentFileTracking;
    }

    public void setCurrentFileTracking(MinaderFileTracking currentFileTracking) {
        this.currentFileTracking = currentFileTracking;
    }

    public List<User> getPotentialUsersList() {
        return potentialUsersList;
    }

    public Map<String, String> getFileStates() {
        return MinaderFileTrackingFilter.FileStateFilter.getMap();
    }

}
