package org.guce.siat.web.ct.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.guce.siat.common.model.ControllerHoliday;
import org.guce.siat.common.model.Delegation;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.ControllerHolidayService;
import org.guce.siat.common.service.DelegationService;
import org.guce.siat.common.service.FileService;
import org.guce.siat.common.service.OrganismService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DelegationController.
 */
@ManagedBean(name = "delegationController")
@SessionScoped
public class DelegationController extends AbstractController<Delegation> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 825707560407854252L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(DelegationController.class);

    /**
     * The Constant OUT_PUT_DATE_PATTERN.
     */
    private static final String OUT_PUT_DATE_PATTERN = "dd/MM/yyyy";

    /**
     * The Constant MESSAGE_DELEGATING_USER.
     */
    private static final String MESSAGE_USER_ON_HOLIDAY = "DelegationUserOnHoliday";

    /**
     * The Constant MESSAGE_DELEGATION_USER_NOT_DISPO_AU.
     */
    private static final String MESSAGE_USER_HAS_DREFT = "DelegationUserHasDreft";

    /**
     * The Constant MESSAGE_FROM_USER_HAS_DELEGATION.
     */
    private static final String MESSAGE_FROM_USER_HAS_DELEGATION = "DelegationFromUserHasDelegation";

    /**
     * The Constant MESSAGE_TO_USER_IS_DELEGATE.
     */
    private static final String MESSAGE_TO_USER_IS_DELEGATE = "DelegationToUserIsDelegate";

    /**
     * The Constant DATE_VALIDATION_ERROR_MESSAGE.
     */
    private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

    /**
     * The Constant DELEGATION_CREATE_BUNDLE.
     */
    private static final String DELEGATION_CREATE_BUNDLE = "delegationCreate";

    /**
     * The Constant DELEGATION_EDIT_BUNDLE.
     */
    private static final String DELEGATION_EDIT_BUNDLE = "delegationEdit";

    /**
     * The delegation service.
     */
    @ManagedProperty(value = "#{delegationService}")
    private DelegationService delegationService;

    /**
     * The user service.
     */
    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    /**
     * The organism service.
     */
    @ManagedProperty(value = "#{organismService}")
    private OrganismService organismService;

    /**
     * The inspector holiday service.
     */
    @ManagedProperty(value = "#{controllerHolidayService}")
    private ControllerHolidayService controllerHolidayService;

    /**
     * The inspector holiday service.
     */
    @ManagedProperty(value = "#{fileService}")
    private FileService fileService;

    /**
     * The service service.
     */
    @ManagedProperty(value = "#{serviceService}")
    private ServiceService serviceService;

    /**
     * The list service.
     */
    private List<Service> listService;

    /**
     * The service getSelected().
     */
    private Service serviceSelected;

    /**
     * The select one menu delege disabled.
     */
    private Boolean selectOneMenuDelegeDisabled;

    /**
     * The select one menu delegateur disabled.
     */
    private Boolean selectOneMenuDelegateurDisabled;

    /**
     * The list user delegateur.
     */
    private List<User> listUserDelegateur;

    /**
     * The list user delege.
     */
    private List<User> listUserDelege;

    /**
     * Instantiates a new delegation controller.
     */
    public DelegationController() {
        super(Delegation.class);
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, DelegationController.class.getName());
        }
        super.setService(delegationService);
        super.setPageUrl(ControllerConstants.Pages.BO.DELEGATION_INDEX_PAGE);

        listService = new ArrayList<>();
        for (final SubDepartment subDepartment : getCurrentOrganism().getSubDepartmentsList()) {
            listService.addAll(subDepartment.getServicesList());
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
     */
    @Override
    public void prepareCreate() {
        super.prepareCreate();

        selectOneMenuDelegeDisabled = true;
        selectOneMenuDelegateurDisabled = true;
        serviceSelected = null;

        listUserDelegateur = null;
        listUserDelege = null;
    }

    /**
     * Service selection changed.
     */
    public void serviceSelectionChanged() {
        selectOneMenuDelegateurDisabled = false;
        selectOneMenuDelegeDisabled = true;

        listUserDelege = new ArrayList<>();

        if (serviceSelected != null) {
            listUserDelegateur = userService.findUsersByAdministrationAndAuthorities(serviceSelected);
        }
    }

    /**
     * Delegateur selection changed.
     */
    public void delegateurSelectionChanged() {
        selectOneMenuDelegeDisabled = false;
        listUserDelege = new ArrayList<>();
        for (final User user : listUserDelegateur) {
            if (!user.equals(getSelected().getFromUser())) {
                listUserDelege.add(user);
            }
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#create()
     */
    @Override
    public void create() {
        final String createBundle = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                DELEGATION_CREATE_BUNDLE);
        if (getSelected().getBeginDate() != null && getSelected().getEndDate() != null
                && getSelected().getBeginDate().after(getSelected().getEndDate())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    DATE_VALIDATION_ERROR_MESSAGE));
            return;
        }
        if (BooleanUtils.isTrue(getSelected().getEnabled())) {
            final List<File> drafts = fileService.findDraftFilesByUsr(getSelected().getFromUser());
            if (CollectionUtils.isNotEmpty(drafts)) {
                JsfUtil.addErrorMessage(MessageFormat.format(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(MESSAGE_USER_HAS_DREFT), createBundle, String.format("%s %s", getSelected().getFromUser().getFirstName(),
                        getSelected().getFromUser().getLastName()), drafts.size()));
                return;
            } else {
                final List<ControllerHoliday> controllerHolidayList = controllerHolidayService.findHolidayByControllerAndDates(
                        getSelected().getToUser(), getSelected().getBeginDate(), getSelected().getEndDate());
                if (CollectionUtils.isNotEmpty(controllerHolidayList)) {
                    JsfUtil.addErrorMessage(MessageFormat.format(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(MESSAGE_USER_ON_HOLIDAY), createBundle, String.format("%s %s", getSelected().getToUser()
                            .getFirstName(), getSelected().getToUser().getLastName()), new SimpleDateFormat(OUT_PUT_DATE_PATTERN)
                            .format(getSelected().getBeginDate()), new SimpleDateFormat(OUT_PUT_DATE_PATTERN).format(getSelected().getEndDate())));
                    return;
                } else {
                    final List<Delegation> delegationListByFromUser = delegationService.findDelegationByDateByFromUsers(
                            getSelected().getToUser(), getSelected().getBeginDate(), getSelected().getEndDate());
                    if (CollectionUtils.isNotEmpty(delegationListByFromUser)) {
                        final Delegation delegation = delegationListByFromUser.get(0);
                        JsfUtil.addErrorMessage(MessageFormat.format(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                                .getString(MESSAGE_FROM_USER_HAS_DELEGATION), createBundle, String.format("%s %s", getSelected().getFromUser()
                                .getFirstName(), getSelected().getFromUser().getLastName()), new SimpleDateFormat(OUT_PUT_DATE_PATTERN)
                                .format(delegation.getBeginDate()), new SimpleDateFormat(OUT_PUT_DATE_PATTERN).format(delegation
                                .getEndDate())));
                        return;
                    } else {
                        final List<Delegation> delegationListByToUser = delegationService.findDelegationByDateByToUsers(
                                getSelected().getFromUser(), getSelected().getBeginDate(), getSelected().getEndDate());
                        if (CollectionUtils.isNotEmpty(delegationListByToUser)) {
                            final Delegation delegation = delegationListByToUser.get(0);
                            JsfUtil.addErrorMessage(MessageFormat.format(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                                    .getString(MESSAGE_TO_USER_IS_DELEGATE), createBundle, String.format("%s %s", getSelected().getFromUser()
                                    .getFirstName(), getSelected().getFromUser().getLastName()), String.format("%s %s", delegation
                                    .getFromUser().getFirstName(), delegation.getFromUser().getLastName()), new SimpleDateFormat(
                                    OUT_PUT_DATE_PATTERN).format(delegation.getBeginDate()), new SimpleDateFormat(OUT_PUT_DATE_PATTERN)
                                    .format(delegation.getEndDate())));
                            return;
                        }
                    }
                }
            }
        }

        getSelected().setDeleted(false);
        final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                Delegation.class.getSimpleName() + PersistenceActions.CREATE.getCode());
        persist(PersistenceActions.CREATE, msg);
        if (!isValidationFailed()) {
            refreshItems();
            selected = null;
        }
    }

    /* (non-Javadoc)
	 * @see org.guce.siat.web.common.AbstractController#getItems()
     */
    @Override
    public List<Delegation> getItems() {

        return delegationService.findByOrganism(getCurrentOrganism());
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.guce.siat.web.common.AbstractController#persist(org.guce.siat.web.ct.controller.util.enums.PersistenceActions,
	 * java.lang.String)
     */
    @Override
    protected void persist(final PersistenceActions persistAction, final String successMessage) {
        if (selected != null) {
            try {
                if (null == persistAction) {
                    this.delegationService.delete(getSelected());
                } else {
                    switch (persistAction) {
                        case UPDATE:
                            delegationService.update(getSelected());
                            setSelected(null);
                            break;
                        case CREATE:
                            this.delegationService.save(getSelected());
                            break;
                        default:
                            this.delegationService.delete(getSelected());
                            break;
                    }
                }

                JsfUtil.addSuccessMessage(successMessage);
            } catch (final Exception ex) {
                LOG.error(ex.getMessage(), ex);

                if (ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            "OneActiveDelegationByUser"));
                } else {
                    JsfUtil.addErrorMessage(ex,
                            ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
                }
            } finally {
                reset();
            }
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#edit()
     */
    @Override
    public void edit() {

        final String editBundle = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(DELEGATION_EDIT_BUNDLE);
        if (getSelected().getBeginDate() != null && getSelected().getEndDate() != null
                && getSelected().getBeginDate().after(getSelected().getEndDate())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    DATE_VALIDATION_ERROR_MESSAGE));
            return;
        }
        if (BooleanUtils.isTrue(getSelected().getEnabled())) {
            final List<File> drafts = fileService.findDraftFilesByUsr(getSelected().getFromUser());
            if (CollectionUtils.isNotEmpty(drafts)) {
                JsfUtil.addErrorMessage(MessageFormat.format(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                        .getString(MESSAGE_USER_HAS_DREFT), editBundle, String.format("%s %s", getSelected().getFromUser().getFirstName(),
                        getSelected().getFromUser().getLastName()), drafts.size()));
                return;
            } else {
                final List<ControllerHoliday> controllerHolidayList = controllerHolidayService.findHolidayByControllerAndDates(
                        getSelected().getToUser(), getSelected().getBeginDate(), getSelected().getEndDate());
                if (CollectionUtils.isNotEmpty(controllerHolidayList)) {
                    JsfUtil.addErrorMessage(MessageFormat.format(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(MESSAGE_USER_ON_HOLIDAY), editBundle, String.format("%s %s", getSelected().getToUser().getFirstName(),
                            getSelected().getToUser().getLastName()), new SimpleDateFormat(OUT_PUT_DATE_PATTERN).format(getSelected()
                            .getBeginDate()), new SimpleDateFormat(OUT_PUT_DATE_PATTERN).format(getSelected().getEndDate())));
                    return;
                } else {
                    final List<Delegation> delegationListByFromUser = delegationService.findDelegationByDateByFromUsers(
                            getSelected().getToUser(), getSelected().getBeginDate(), getSelected().getEndDate());
                    if (CollectionUtils.isNotEmpty(delegationListByFromUser)) {
                        final Delegation delegation = delegationListByFromUser.get(0);
                        JsfUtil.addErrorMessage(MessageFormat.format(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                                .getString(MESSAGE_FROM_USER_HAS_DELEGATION), editBundle, String.format("%s %s", getSelected().getFromUser()
                                .getFirstName(), getSelected().getFromUser().getLastName()), new SimpleDateFormat(OUT_PUT_DATE_PATTERN)
                                .format(delegation.getBeginDate()), new SimpleDateFormat(OUT_PUT_DATE_PATTERN).format(delegation
                                .getEndDate())));
                        return;
                    } else {
                        final List<Delegation> delegationListByToUser = delegationService.findDelegationByDateByToUsers(
                                getSelected().getFromUser(), getSelected().getBeginDate(), getSelected().getEndDate());
                        if (CollectionUtils.isNotEmpty(delegationListByToUser)) {
                            final Delegation delegation = delegationListByToUser.get(0);
                            JsfUtil.addErrorMessage(MessageFormat.format(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                                    .getString(MESSAGE_TO_USER_IS_DELEGATE), editBundle, String.format("%s %s", getSelected().getFromUser()
                                    .getFirstName(), getSelected().getFromUser().getLastName()), String.format("%s %s", delegation
                                    .getFromUser().getFirstName(), delegation.getFromUser().getLastName()), new SimpleDateFormat(
                                    OUT_PUT_DATE_PATTERN).format(delegation.getBeginDate()), new SimpleDateFormat(OUT_PUT_DATE_PATTERN)
                                    .format(delegation.getEndDate())));
                            return;
                        }
                    }
                }
            }
        }
        getSelected().setDeleted(false);
        super.edit();
    }

    /**
     * Prepare edit.
     */
    public void prepareEdit() {
        this.setSelected(delegationService.find(this.getSelected().getId()));
        serviceSelected = serviceService.findServiceByUser(this.getSelected().getToUser());
    }

    /**
     * Gets the delegation service.
     *
     * @return the delegationService
     */
    public DelegationService getDelegationService() {
        return delegationService;
    }

    /**
     * Sets the delegation service.
     *
     * @param delegationService the delegationService to set
     */
    public void setDelegationService(final DelegationService delegationService) {
        this.delegationService = delegationService;
    }

    /**
     * Gets the user service.
     *
     * @return the userService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Sets the user service.
     *
     * @param userService the userService to set
     */
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets the organism service.
     *
     * @return the organismService
     */
    public OrganismService getOrganismService() {
        return organismService;
    }

    /**
     * Sets the organism service.
     *
     * @param organismService the organismService to set
     */
    public void setOrganismService(final OrganismService organismService) {
        this.organismService = organismService;
    }

    /**
     * Gets the controller holiday service.
     *
     * @return the controllerHolidayService
     */
    public ControllerHolidayService getControllerHolidayService() {
        return controllerHolidayService;
    }

    /**
     * Sets the controller holiday service.
     *
     * @param controllerHolidayService the controllerHolidayService to set
     */
    public void setControllerHolidayService(final ControllerHolidayService controllerHolidayService) {
        this.controllerHolidayService = controllerHolidayService;
    }

    /**
     * Gets the list service.
     *
     * @return the listService
     */
    public List<Service> getListService() {
        return listService;
    }

    /**
     * Sets the list service.
     *
     * @param listService the listService to set
     */
    public void setListService(final List<Service> listService) {
        this.listService = listService;
    }

    /**
     * Gets the service getSelected().
     *
     * @return the serviceSelected
     */
    public Service getServiceSelected() {
        return serviceSelected;
    }

    /**
     * Sets the service getSelected().
     *
     * @param serviceSelected the serviceSelected to set
     */
    public void setServiceSelected(final Service serviceSelected) {
        this.serviceSelected = serviceSelected;
    }

    /**
     * Gets the select one menu delege disabled.
     *
     * @return the selectOneMenuDelegeDisabled
     */
    public Boolean getSelectOneMenuDelegeDisabled() {
        return selectOneMenuDelegeDisabled;
    }

    /**
     * Sets the select one menu delege disabled.
     *
     * @param selectOneMenuDelegeDisabled the selectOneMenuDelegeDisabled to set
     */
    public void setSelectOneMenuDelegeDisabled(final Boolean selectOneMenuDelegeDisabled) {
        this.selectOneMenuDelegeDisabled = selectOneMenuDelegeDisabled;
    }

    /**
     * Gets the select one menu delegateur disabled.
     *
     * @return the selectOneMenuDelegateurDisabled
     */
    public Boolean getSelectOneMenuDelegateurDisabled() {
        return selectOneMenuDelegateurDisabled;
    }

    /**
     * Sets the select one menu delegateur disabled.
     *
     * @param selectOneMenuDelegateurDisabled the
     * selectOneMenuDelegateurDisabled to set
     */
    public void setSelectOneMenuDelegateurDisabled(final Boolean selectOneMenuDelegateurDisabled) {
        this.selectOneMenuDelegateurDisabled = selectOneMenuDelegateurDisabled;
    }

    /**
     * Gets the list user delegateur.
     *
     * @return the listUserDelegateur
     */
    public List<User> getListUserDelegateur() {
        return listUserDelegateur;
    }

    /**
     * Sets the list user delegateur.
     *
     * @param listUserDelegateur the listUserDelegateur to set
     */
    public void setListUserDelegateur(final List<User> listUserDelegateur) {
        this.listUserDelegateur = listUserDelegateur;
    }

    /**
     * Gets the list user delege.
     *
     * @return the listUserDelege
     */
    public List<User> getListUserDelege() {
        return listUserDelege;
    }

    /**
     * Sets the list user delege.
     *
     * @param listUserDelege the listUserDelege to set
     */
    public void setListUserDelege(final List<User> listUserDelege) {
        this.listUserDelege = listUserDelege;
    }

    /**
     * Gets the file service.
     *
     * @return the file service
     */
    public FileService getFileService() {
        return fileService;
    }

    /**
     * Sets the file service.
     *
     * @param fileService the new file service
     */
    public void setFileService(final FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Gets the service service.
     *
     * @return the service service
     */
    public ServiceService getServiceService() {
        return serviceService;
    }

    /**
     * Sets the service service.
     *
     * @param serviceService the new service service
     */
    public void setServiceService(final ServiceService serviceService) {
        this.serviceService = serviceService;
    }

}
