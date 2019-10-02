package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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

import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.UserAuthorityFileType;
import org.guce.siat.common.service.AuthorityService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FileTypeStepService;
import org.guce.siat.common.service.ItemFlowService;
import org.guce.siat.common.service.UserAuthorityFileTypeService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.core.ct.service.CommonService;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class FileItemCctController.
 */
@ManagedBean(name = "fileItemCctController")
@ViewScoped
public class FileItemCctController extends AbstractController<FileItem> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 4138042330970406266L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FileItemCctController.class);

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
     * The item flow service.
     */
    @ManagedProperty(value = "#{itemFlowService}")
    private ItemFlowService itemFlowService;

    /**
     * The authority service.
     */
    @ManagedProperty(value = "#{authorityService}")
    private AuthorityService authorityService;

    /**
     * The user authority file type service.
     */
    @ManagedProperty(value = "#{userAuthorityFileTypeService}")
    private UserAuthorityFileTypeService userAuthorityFileTypeService;

    /**
     * The common service.
     */
    @ManagedProperty(value = "#{commonService}")
    private CommonService commonService;

    /**
     * The unread file items list.
     */
    private Set<FileItem> unreadFileItemsList;

    /**
     * The draft file items list.
     */
    private Set<FileItem> draftFileItemsList;

    /**
     * The received befor one day file items list.
     */
    private Set<FileItem> receivedBeforOneDayFileItemsList;

    /**
     * The received more tow day file items list.
     */
    private Set<FileItem> receivedMoreTowDayFileItemsList;

    /**
     * The received beatween tow day file items list.
     */
    private Set<FileItem> receivedBeatweenTowDayFileItemsList;

    /**
     * The detail page url.
     */
    private String detailPageUrl;

    /**
     * The list user authority file types.
     */
    private List<UserAuthorityFileType> listUserAuthorityFileTypes;

    /**
     * The granted authorities impl list.
     */
    private List<Authority> grantedAuthoritiesImplList;

    /**
     * The action filter.
     */
    private String actionFilter;

    /**
     * The first check.
     */
    private Boolean firstCheck;

    /**
     * Instantiates a new file item cct controller.
     */
    public FileItemCctController() {
        super(FileItem.class);
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, FileItemCctController.class.getName());
        }
        super.setService(fileItemService);
        super.setPageUrl(ControllerConstants.Pages.FO.DASHBOARD_CCT_INDEX_PAGE);
        dashboardCalculate();
    }

    /**
     * Go to detail page.
     */
    public void goToDetailPage() {

        for (final FileItem fileItem : selected.getFile().getFileItemsList()) {

            for (final ItemFlow itemFlow : fileItem.getItemFlowsList()) {
                if (itemFlow.getUnread() && fileItem.getStep().equals(itemFlow.getFlow().getToStep())) {
                    itemFlow.setUnread(Boolean.FALSE);
                    itemFlowService.update(itemFlow);
                }
            }
        }
        try {
            setDetailPageUrl(ControllerConstants.Pages.FO.DETAILS_CCT_INDEX_PAGE);
            refreshItems();
            final FacesContext context = FacesContext.getCurrentInstance();
            final ExternalContext extContext = context.getExternalContext();
            final FileItemCctDetailController fileItemCctDetailController = getInstanceOfPageFileItemCctDetailController();
            fileItemCctDetailController.setCurrentFileItem(fileItemService.find(selected.getId()));
            fileItemCctDetailController.init();

            Set<File> files = extractFilesFormItems(getItems());
            fileItemCctDetailController.setFilesList(files);

            final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
                    .getActionURL(context, detailPageUrl));

            extContext.redirect(url);
        } catch (final IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Extract files form items.
     *
     * @param items the items
     * @return the sets the
     */
    public static Set<File> extractFilesFormItems(final Collection<FileItem> items) {
        return Utils.extractFilesFormItems(items);
    }

    /**
     * Gets the instance of page file item cct detail controller.
     *
     * @return the instance of page file item cct detail controller
     */
    public FileItemCctDetailController getInstanceOfPageFileItemCctDetailController() {
        final FacesContext fctx = FacesContext.getCurrentInstance();
        final Application application = fctx.getApplication();
        final ELContext context = fctx.getELContext();
        final ExpressionFactory expressionFactory = application.getExpressionFactory();
        final ValueExpression createValueExpression = expressionFactory.createValueExpression(context,
                "#{fileItemCctDetailController}", FileItemCctDetailController.class);
        return (FileItemCctDetailController) createValueExpression.getValue(context);
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

    /**
     * Gets the items.
     *
     * @return the items
     */
    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#getItems()
     */
    @Override
    public List<FileItem> getItems() {
        try {
            if (items == null) {
                items = Utils.getItems(userAuthorityFileTypeService, fileItemService, getLoggedUser(), listUserAuthorityFileTypes);
            }
        } catch (final Exception ex) {
            LOG.error(ex.getMessage(), ex);
            JsfUtil.addErrorMessage(ex,
                    ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
        }

        if (items != null && firstCheck) {
            Collections.sort(items, new Comparator<FileItem>() {
                @Override
                public int compare(final FileItem fileItem1, final FileItem fileItem2) {

                    final File file1 = fileItem1.getFile();
                    final File file2 = fileItem2.getFile();

                    Date lastDecisionDate1 = file1.getLastDecisionDate();
                    if (lastDecisionDate1 == null) {
                        lastDecisionDate1 = file1.getCreatedDate();
                    }

                    Date lastDecisionDate2 = file2.getLastDecisionDate();
                    if (lastDecisionDate2 == null) {
                        lastDecisionDate2 = file2.getCreatedDate();
                    }

                    return lastDecisionDate1.compareTo(lastDecisionDate2);
                }
            });
            setFirstCheck(false);
        }
        return items;
    }

    /**
     * Dashboard calculate.
     */
    public void dashboardCalculate() {
        setFirstCheck(true);
        items = null;
        getItems();
        final Date systemDate = Calendar.getInstance().getTime();
        for (final FileItem fileItem : items != null ? items : new ArrayList<FileItem>()) {
            for (final ItemFlow flow : fileItem.getItemFlowsList()) {
                if (fileItem.getStep().equals(flow.getFlow().getToStep())) {
                    if (flow.getUnread()) {
                        getUnreadFileItemsList().add(fileItem);
                        break;
                    } else {
                        final long diff = systemDate.getTime() - flow.getCreated().getTime();
                        final long diffDays = diff / (DateUtils.CONST_DURATION_OF_DAY);
                        if (diffDays <= Constants.ONE) {
                            getReceivedBeforOneDayFileItemsList().add(fileItem);
                        } else if (diffDays > Constants.THREE) {
                            getReceivedMoreTowDayFileItemsList().add(fileItem);
                        } else if (diffDays > Constants.ONE && diffDays <= Constants.THREE) {
                            getReceivedBeatweenTowDayFileItemsList().add(fileItem);
                        }

                    }

                }
                if (!getDraftFileItemsList().contains(fileItem)
                        && !flow.getSent()
                        && (fileItem.getStep().equals(flow.getFlow().getToStep()) || fileItem.getStep().equals(
                        flow.getFlow().getFromStep()))) {
                    getReceivedBeforOneDayFileItemsList().remove(fileItem);
                    getReceivedMoreTowDayFileItemsList().remove(fileItem);
                    getReceivedBeatweenTowDayFileItemsList().remove(fileItem);
                    getDraftFileItemsList().add(fileItem);
                    break;
                }
            }
        }

    }

    /**
     * Checks if is unread file item.
     *
     * @param fileItem the file item
     * @return true, if is unread file item
     */
    public boolean isUnreadFileItem(final FileItem fileItem) {
        return getUnreadFileItemsList().contains(fileItem);
    }

    /**
     * Filter form dashboard.
     */
    public void filterFormDashboard() {
        switch (actionFilter) {
            case "unread":
                this.items = new ArrayList<>(unreadFileItemsList);
                break;
            case "oneday":
                this.items = new ArrayList<>(receivedBeforOneDayFileItemsList);
                break;
            case "towday":
                this.items = new ArrayList<>(receivedMoreTowDayFileItemsList);
                break;
            case "between":
                this.items = new ArrayList<>(receivedBeatweenTowDayFileItemsList);
                break;
            case "draft":
                this.items = new ArrayList<>(draftFileItemsList);
                break;
            case "all":
                this.items = null;
                break;
            default:
                break;
        }
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
     * Gets the unread file items list.
     *
     * @return the unreadFileItemsList
     */
    public Set<FileItem> getUnreadFileItemsList() {
        if (Objects.equals(unreadFileItemsList, null)) {
            unreadFileItemsList = new HashSet<>();
        }
        return unreadFileItemsList;
    }

    /**
     * Sets the unread file items list.
     *
     * @param unreadFileItemsList the unreadFileItemsList to set
     */
    public void setUnreadFileItemsList(final Set<FileItem> unreadFileItemsList) {
        this.unreadFileItemsList = unreadFileItemsList;
    }

    /**
     * Gets the draft file items list.
     *
     * @return the draftFileItemsList
     */
    public Set<FileItem> getDraftFileItemsList() {
        if (Objects.equals(draftFileItemsList, null)) {
            draftFileItemsList = new HashSet<>();
        }
        return draftFileItemsList;
    }

    /**
     * Sets the draft file items list.
     *
     * @param draftFileItemsList the draftFileItemsList to set
     */
    public void setDraftFileItemsList(final Set<FileItem> draftFileItemsList) {
        this.draftFileItemsList = draftFileItemsList;
    }

    /**
     * Gets the received befor one day file items list.
     *
     * @return the receivedBeforOneDayFileItemsList
     */
    public Set<FileItem> getReceivedBeforOneDayFileItemsList() {
        if (Objects.equals(receivedBeforOneDayFileItemsList, null)) {
            receivedBeforOneDayFileItemsList = new HashSet<>();
        }
        return receivedBeforOneDayFileItemsList;
    }

    /**
     * Sets the received befor one day file items list.
     *
     * @param receivedBeforOneDayFileItemsList the
     * receivedBeforOneDayFileItemsList to set
     */
    public void setReceivedBeforOneDayFileItemsList(final Set<FileItem> receivedBeforOneDayFileItemsList) {
        this.receivedBeforOneDayFileItemsList = receivedBeforOneDayFileItemsList;
    }

    /**
     * Gets the received more tow day file items list.
     *
     * @return the receivedMoreTowDayFileItemsList
     */
    public Set<FileItem> getReceivedMoreTowDayFileItemsList() {
        if (Objects.equals(receivedMoreTowDayFileItemsList, null)) {
            receivedMoreTowDayFileItemsList = new HashSet<>();
        }
        return receivedMoreTowDayFileItemsList;
    }

    /**
     * Sets the received more tow day file items list.
     *
     * @param receivedMoreTowDayFileItemsList the
     * receivedMoreTowDayFileItemsList to set
     */
    public void setReceivedMoreTowDayFileItemsList(final Set<FileItem> receivedMoreTowDayFileItemsList) {
        this.receivedMoreTowDayFileItemsList = receivedMoreTowDayFileItemsList;
    }

    /**
     * Gets the received beatween tow day file items list.
     *
     * @return the receivedBeatweenTowDayFileItemsList
     */
    public Set<FileItem> getReceivedBeatweenTowDayFileItemsList() {
        if (Objects.equals(receivedBeatweenTowDayFileItemsList, null)) {
            receivedBeatweenTowDayFileItemsList = new HashSet<>();
        }
        return receivedBeatweenTowDayFileItemsList;
    }

    /**
     * Sets the received beatween tow day file items list.
     *
     * @param receivedBeatweenTowDayFileItemsList the
     * receivedBeatweenTowDayFileItemsList to set
     */
    public void setReceivedBeatweenTowDayFileItemsList(final Set<FileItem> receivedBeatweenTowDayFileItemsList) {

        this.receivedBeatweenTowDayFileItemsList = receivedBeatweenTowDayFileItemsList;
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
     * Gets the common service.
     *
     * @return the common service
     */
    public CommonService getCommonService() {
        return commonService;
    }

    /**
     * Sets the common service.
     *
     * @param commonService the new common service
     */
    public void setCommonService(final CommonService commonService) {
        this.commonService = commonService;
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
