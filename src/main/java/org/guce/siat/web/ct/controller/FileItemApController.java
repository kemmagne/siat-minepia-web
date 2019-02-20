package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.BooleanUtils;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.User;
import org.guce.siat.common.model.UserAuthorityFileType;
import org.guce.siat.common.service.AuthorityService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FileTypeStepService;
import org.guce.siat.common.service.ItemFlowService;
import org.guce.siat.common.service.UserAuthorityFileTypeService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.common.utils.SiatUtils;
import org.guce.siat.common.utils.enums.InformationSystemCode;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class FileItemApController.
 */
@ManagedBean(name = "fileItemApController")
@ViewScoped
public class FileItemApController extends AbstractController<FileItem> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -4994522163212372773L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FileItemApController.class);
    /**
     * The file type step service.
     */
    @ManagedProperty(value = "#{fileTypeStepService}")
    private FileTypeStepService fileTypeStepService;

    /**
     * The file item service.
     */
    @ManagedProperty(value = "#{fileItemService}")
    private FileItemService fileItemService;

    /**
     * The authority service.
     */
    @ManagedProperty(value = "#{authorityService}")
    private AuthorityService authorityService;

    /**
     * The item flow service.
     */
    @ManagedProperty(value = "#{itemFlowService}")
    private ItemFlowService itemFlowService;

    /**
     * The user authority file type service.
     */
    @ManagedProperty(value = "#{userAuthorityFileTypeService}")
    private UserAuthorityFileTypeService userAuthorityFileTypeService;

    /**
     * The unread files list.
     */
    private Set<File> unreadFilesList;

    /**
     * The draft files count.
     */
    private Set<File> draftFilesList;

    /**
     * The received and befor one day files list.
     */
    private Set<File> receivedBeforOneDayFilesList;

    /**
     * The received more two days files list.
     */
    private Set<File> receivedMoreTwoDaysFilesList;

    /**
     * The received between two day files list.
     */
    private Set<File> receivedBetweenTwoDayFilesList;

    /**
     * The detail page url.
     */
    private String detailPageUrl;

    /**
     * The list user authority file types.
     */
    private List<UserAuthorityFileType> listUserAuthorityFileTypes;

    /**
     * The files list.
     */
    private List<File> filesList;

    /**
     * The files list filtred.
     */
    private List<File> filesListFiltred;

    /**
     * The selected file.
     */
    private File selectedFile;

    /**
     * The action filter.
     */
    private String actionFilter;

    /**
     * The granted authorities impl list.
     */
    private List<Authority> grantedAuthoritiesImplList;

    /**
     * The first check.
     */
    private Boolean firstCheck;

    /**
     * Instantiates a new file item ap controller.
     */
    public FileItemApController() {
        super(FileItem.class);
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, FileItemApController.class.getName());
        }
        super.setService(fileItemService);
        super.setPageUrl(ControllerConstants.Pages.FO.DASHBOARD_AP_INDEX_PAGE);
        dashboardCalculate();

    }

    /**
     * Go to detail page.
     */
    public void goToDetailPage() {
        try {
            for (final FileItem fileItem : selectedFile.getFileItemsList()) {
                for (final ItemFlow itemFlow : fileItem.getItemFlowsList()) {
                    if (itemFlow.getUnread() && fileItem.getStep().equals(itemFlow.getFlow().getToStep())) {
                        itemFlow.setUnread(Boolean.FALSE);
                        itemFlowService.update(itemFlow);
                    }
                }
            }
            setDetailPageUrl(ControllerConstants.Pages.FO.DETAILS_AP_INDEX_PAGE);
            refreshItems();
            final FacesContext context = FacesContext.getCurrentInstance();
            final ExternalContext extContext = context.getExternalContext();
            final FileItemApDetailController fileItemApDetailController = getInstanceOfPageFileItemApDetailController();
            fileItemApDetailController.setCurrentFile(selectedFile);
            fileItemApDetailController.setComeFromRetrieveAp(false);
            fileItemApDetailController.init();

            final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
                    .getActionURL(context, detailPageUrl));

            extContext.redirect(url);
        } catch (final IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Gets the instance of page file item ap detail controller.
     *
     * @return the instance of page file item ap detail controller
     */
    public FileItemApDetailController getInstanceOfPageFileItemApDetailController() {
        final FacesContext fctx = FacesContext.getCurrentInstance();
        final Application application = fctx.getApplication();
        final ELContext context = fctx.getELContext();
        final ExpressionFactory expressionFactory = application.getExpressionFactory();
        final ValueExpression createValueExpression = expressionFactory.createValueExpression(context,
                "#{fileItemApDetailController}", FileItemApDetailController.class);
        return (FileItemApDetailController) createValueExpression.getValue(context);
    }

    /**
     * Gets the detail page url.
     *
     * @return the detail page url
     */
    public String getDetailPageUrl() {
        return detailPageUrl;
    }

    /**
     * Sets the detail page url.
     *
     * @param detailPageUrl the new detail page url
     */
    public void setDetailPageUrl(final String detailPageUrl) {
        this.detailPageUrl = detailPageUrl;
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#getItems()
     */
    @Override
    public List<FileItem> getItems() {
        try {
            if (items == null) {

                if (listUserAuthorityFileTypes == null) {
                    listUserAuthorityFileTypes = userAuthorityFileTypeService.findUserAuthorityFileTypeByUserList(getLoggedUser()
                            .getMergedDelegatorList());
                }

                // Merge the logged user and their delegator users list in the list
                final List<Administration> adminList = new ArrayList<>();

                if (getLoggedUser().getMergedDelegatorList() != null) {
                    for (final User user : getLoggedUser().getMergedDelegatorList()) {
                        adminList.add(user.getAdministration());
                    }
                }

                // get the services id for the administration of the logged user and their delegator users
                final List<Bureau> bureauList = SiatUtils.findCombinedBureausByAdministrationList(adminList);

                //Ajout dans la liste des bureaux, tous les bureaux de toutes les unités qui appartiennent
                //à l'administration dont l'utilisateur actuel possède des privilèges globaux
                User logUser = this.getLoggedUser();
                if (logUser.getAdministrationExtendRoles() != null) {
                    //l'utilisateur possède des privilèges globaux
                    List<? extends Administration> administrations = Arrays.asList(logUser.getAdministrationExtendRoles());
                    bureauList.addAll(SiatUtils.findCombinedBureausByAdministrationList(administrations));
                }

                items = fileItemService.findFileItemByServiceAndAuthoritiesAndFileType(bureauList, getLoggedUser(),
                        InformationSystemCode.AP, listUserAuthorityFileTypes);

            }
        } catch (final Exception ex) {
            JsfUtil.addErrorMessage(ex,
                    ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
        }
        return items;
    }

    /**
     * Dashboard calculate.
     */
    public void dashboardCalculate() {
        setFirstCheck(true);
        resetDashboardCount();
        getItems();
        final Date systemDate = Calendar.getInstance().getTime();
        for (final FileItem fileItem : items != null ? items : new ArrayList<FileItem>()) {
            for (final ItemFlow flow : fileItem.getItemFlowsList()) {
                if (fileItem.getStep().equals(flow.getFlow().getToStep())) {
                    if (flow.getUnread()) {
                        getUnreadFilesList().add(fileItem.getFile());
                        break;
                    } else {
                        final long diff = systemDate.getTime() - flow.getCreated().getTime();
                        final long diffDays = diff / (DateUtils.CONST_DURATION_OF_DAY);
                        if (diffDays <= Constants.ONE) {
                            getReceivedBeforOneDayFilesList().add(fileItem.getFile());
                        } else if (diffDays > Constants.THREE) {
                            getReceivedMoreTwoDaysFilesList().add(fileItem.getFile());
                        } else if (diffDays > Constants.ONE && diffDays <= Constants.THREE) {
                            getReceivedBetweenTwoDayFilesList().add(fileItem.getFile());
                        }
                    }
                }
                if (!getDraftFilesList().contains(fileItem.getFile())
                        && BooleanUtils.isNotTrue(flow.getSent())
                        && (fileItem.getStep().equals(flow.getFlow().getToStep()) || fileItem.getStep().equals(
                        flow.getFlow().getFromStep()))) {
                    getReceivedBeforOneDayFilesList().remove(fileItem.getFile());
                    getReceivedBetweenTwoDayFilesList().remove(fileItem.getFile());
                    getReceivedMoreTwoDaysFilesList().remove(fileItem.getFile());
                    getDraftFilesList().add(fileItem.getFile());
                    break;
                }
            }
        }

    }

    /**
     * Checks for unread item.
     *
     * @param file the file
     * @return true, if successful
     */
    public boolean hasUnreadItem(final File file) {
        return getUnreadFilesList().contains(file);
    }

    /**
     * Reset dashboard count.
     */
    private void resetDashboardCount() {
        items = null;
        filesList = null;
        unreadFilesList = null;
        draftFilesList = null;
        receivedBeforOneDayFilesList = null;
        receivedBetweenTwoDayFilesList = null;
        receivedMoreTwoDaysFilesList = null;
    }

    /**
     * Filter form dashboard.
     */
    public void filterFormDashboard() {
        switch (actionFilter) {
            case "unread":
                this.filesList = new ArrayList<>(unreadFilesList);
                break;
            case "oneday":
                this.filesList = new ArrayList<>(receivedBeforOneDayFilesList);
                break;
            case "towday":
                this.filesList = new ArrayList<>(receivedMoreTwoDaysFilesList);
                break;
            case "between":
                this.filesList = new ArrayList<>(receivedBetweenTwoDayFilesList);
                break;
            case "draft":
                this.filesList = new ArrayList<>(draftFilesList);
                break;
            case "all":
                this.filesList = null;
                break;
            default:
                break;
        }
    }

    /**
     * Extract files.
     *
     * @return the sets the
     */
    public Set<File> extractFiles() {
        @SuppressWarnings("unchecked")
        final List<File> files = (List<File>) CollectionUtils.collect(items != null ? items : new HashSet<>(),
                new Transformer() {
            @Override
            public Object transform(final Object input) {
                return ((FileItem) input).getFile();
            }
        });
        if (!Objects.equals(files, null)) {
            return new HashSet<>(files);
        }
        return Collections.emptySet();
    }

    /**
     * Gets the files list.
     *
     * @return the filesList
     */
    public List<File> getFilesList() {
        if (Objects.equals(filesList, null)) {
            filesList = new ArrayList<>(extractFiles());
        }

        if (firstCheck) {
            Collections.sort(filesList, new Comparator<File>() {
                @Override
                public int compare(final File file, final File otherFile) {
                    final FileItem fi1 = file.getFileItemsList().get(0);
                    final FileItem fi2 = otherFile.getFileItemsList().get(0);

                    final ItemFlow if1 = itemFlowService.findLastItemFlowByFileItem(fi1);
                    final ItemFlow if2 = itemFlowService.findLastItemFlowByFileItem(fi2);

                    if (if1.getCreated().getTime() > if2.getCreated().getTime()) {
                        return 1;
                    } else if (if1.getCreated().getTime() < if2.getCreated().getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            
            Collections.reverse(filesList);
            setFirstCheck(false);
        }
        return filesList;
    }

    /**
     * Gets the file item service.
     *
     * @return the file item service
     */
    public FileItemService getFileItemService() {
        return fileItemService;
    }

    /**
     * Sets the file item service.
     *
     * @param fileItemService the new file item service
     */
    public void setFileItemService(final FileItemService fileItemService) {
        this.fileItemService = fileItemService;
    }

    /**
     * Gets the authority service.
     *
     * @return the authorityService
     */
    public AuthorityService getAuthorityService() {
        return authorityService;
    }

    /**
     * Sets the authority service.
     *
     * @param authorityService the authorityService to set
     */
    public void setAuthorityService(final AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * Gets the granted authorities impl list.
     *
     * @return the grantedAuthoritiesImplList
     */
    public List<Authority> getGrantedAuthoritiesImplList() {
        return grantedAuthoritiesImplList;
    }

    /**
     * Sets the granted authorities impl list.
     *
     * @param grantedAuthoritiesImplList the grantedAuthoritiesImplList to set
     */
    public void setGrantedAuthoritiesImplList(final List<Authority> grantedAuthoritiesImplList) {
        this.grantedAuthoritiesImplList = grantedAuthoritiesImplList;
    }

    /**
     * Gets the user authority file type service.
     *
     * @return the userAuthorityFileTypeService
     */
    public UserAuthorityFileTypeService getUserAuthorityFileTypeService() {
        return userAuthorityFileTypeService;
    }

    /**
     * Sets the user authority file type service.
     *
     * @param userAuthorityFileTypeService the userAuthorityFileTypeService to
     * set
     */
    public void setUserAuthorityFileTypeService(final UserAuthorityFileTypeService userAuthorityFileTypeService) {
        this.userAuthorityFileTypeService = userAuthorityFileTypeService;
    }

    /**
     * Gets the list user authority file types.
     *
     * @return the listUserAuthorityFileTypes
     */
    public List<UserAuthorityFileType> getListUserAuthorityFileTypes() {
        return listUserAuthorityFileTypes;
    }

    /**
     * Sets the list user authority file types.
     *
     * @param listUserAuthorityFileTypes the listUserAuthorityFileTypes to set
     */
    public void setListUserAuthorityFileTypes(final List<UserAuthorityFileType> listUserAuthorityFileTypes) {
        this.listUserAuthorityFileTypes = listUserAuthorityFileTypes;
    }

    /**
     * Sets the files list.
     *
     * @param filesList the filesList to set
     */
    public void setFilesList(final List<File> filesList) {
        this.filesList = filesList;
    }

    /**
     * Gets the selected file.
     *
     * @return the selectedFile
     */
    public File getSelectedFile() {
        return selectedFile;
    }

    /**
     * Sets the selected file.
     *
     * @param selectedFile the selectedFile to set
     */
    public void setSelectedFile(final File selectedFile) {
        this.selectedFile = selectedFile;
    }

    /**
     * Gets the unread files list.
     *
     * @return the unreadFilesList
     */
    public Set<File> getUnreadFilesList() {
        if (Objects.equals(unreadFilesList, null)) {
            unreadFilesList = new HashSet<>();
        }
        return unreadFilesList;
    }

    /**
     * Sets the unread files list.
     *
     * @param unreadFilesList the unreadFilesList to set
     */
    public void setUnreadFilesList(final Set<File> unreadFilesList) {
        this.unreadFilesList = unreadFilesList;
    }

    /**
     * Gets the draft files list.
     *
     * @return the draftFilesList
     */
    public Set<File> getDraftFilesList() {
        if (Objects.equals(draftFilesList, null)) {
            draftFilesList = new HashSet<>();
        }
        return draftFilesList;
    }

    /**
     * Sets the draft files list.
     *
     * @param draftFilesList the draftFilesList to set
     */
    public void setDraftFilesList(final Set<File> draftFilesList) {
        this.draftFilesList = draftFilesList;
    }

    /**
     * Gets the received befor one day files list.
     *
     * @return the receivedBeforOneDayFilesList
     */
    public Set<File> getReceivedBeforOneDayFilesList() {
        if (Objects.equals(receivedBeforOneDayFilesList, null)) {
            receivedBeforOneDayFilesList = new HashSet<>();
        }
        return receivedBeforOneDayFilesList;
    }

    /**
     * Sets the received befor one day files list.
     *
     * @param receivedBeforOneDayFilesList the receivedBeforOneDayFilesList to
     * set
     */
    public void setReceivedBeforOneDayFilesList(final Set<File> receivedBeforOneDayFilesList) {
        this.receivedBeforOneDayFilesList = receivedBeforOneDayFilesList;
    }

    /**
     * Gets the received more two days files list.
     *
     * @return the receivedMoreTwoDaysFilesList
     */
    public Set<File> getReceivedMoreTwoDaysFilesList() {
        if (Objects.equals(receivedMoreTwoDaysFilesList, null)) {
            receivedMoreTwoDaysFilesList = new HashSet<>();
        }
        return receivedMoreTwoDaysFilesList;
    }

    /**
     * Sets the received more two days files list.
     *
     * @param receivedMoreTwoDaysFilesList the receivedMoreTwoDaysFilesList to
     * set
     */
    public void setReceivedMoreTwoDaysFilesList(final Set<File> receivedMoreTwoDaysFilesList) {
        this.receivedMoreTwoDaysFilesList = receivedMoreTwoDaysFilesList;
    }

    /**
     * Gets the received between two day files list.
     *
     * @return the receivedBetweenTwoDayFilesList
     */
    public Set<File> getReceivedBetweenTwoDayFilesList() {
        if (Objects.equals(receivedBetweenTwoDayFilesList, null)) {
            receivedBetweenTwoDayFilesList = new HashSet<>();
        }
        return receivedBetweenTwoDayFilesList;
    }

    /**
     * Sets the received between two day files list.
     *
     * @param receivedBetweenTwoDayFilesList the receivedBetweenTwoDayFilesList
     * to set
     */
    public void setReceivedBetweenTwoDayFilesList(final Set<File> receivedBetweenTwoDayFilesList) {
        this.receivedBetweenTwoDayFilesList = receivedBetweenTwoDayFilesList;
    }

    /**
     * Gets the item flow service.
     *
     * @return the itemFlowService
     */
    public ItemFlowService getItemFlowService() {
        return itemFlowService;
    }

    /**
     * Sets the item flow service.
     *
     * @param itemFlowService the itemFlowService to set
     */
    public void setItemFlowService(final ItemFlowService itemFlowService) {
        this.itemFlowService = itemFlowService;
    }

    /**
     * Gets the action filter.
     *
     * @return the actionFilter
     */
    public String getActionFilter() {
        return actionFilter;
    }

    /**
     * Sets the action filter.
     *
     * @param actionFilter the actionFilter to set
     */
    public void setActionFilter(final String actionFilter) {
        this.actionFilter = actionFilter;
    }

    /**
     * Gets the files list filtred.
     *
     * @return the files list filtred
     */
    public List<File> getFilesListFiltred() {
        return filesListFiltred;
    }

    /**
     * Sets the files list filtred.
     *
     * @param filesListFiltred the new files list filtred
     */
    public void setFilesListFiltred(final List<File> filesListFiltred) {
        this.filesListFiltred = filesListFiltred;
    }

    /**
     * Gets the file type step service.
     *
     * @return the file type step service
     */
    public FileTypeStepService getFileTypeStepService() {
        return fileTypeStepService;
    }

    /**
     * Sets the file type step service.
     *
     * @param fileTypeStepService the new file type step service
     */
    public void setFileTypeStepService(final FileTypeStepService fileTypeStepService) {
        this.fileTypeStepService = fileTypeStepService;
    }

    /**
     * @return the firstCheck
     */
    public Boolean getFirstCheck() {
        return firstCheck;
    }

    /**
     * @param firstCheck the firstCheck to set
     */
    public void setFirstCheck(Boolean firstCheck) {
        this.firstCheck = firstCheck;
    }

}

