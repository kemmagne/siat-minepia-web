package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.persistence.PersistenceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.Entity;
import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.model.User;
import org.guce.siat.common.model.UserAuthority;
import org.guce.siat.common.model.UserAuthorityFileType;
import org.guce.siat.common.model.UserAuthorityFileTypeId;
import org.guce.siat.common.service.AuthorityService;
import org.guce.siat.common.service.EntityService;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.common.service.MailService;
import org.guce.siat.common.service.MinistryService;
import org.guce.siat.common.service.OrganismService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.SubDepartmentService;
import org.guce.siat.common.service.UserAuthorityFileTypeService;
import org.guce.siat.common.service.UserAuthorityService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.AuthorityConstants;
import org.guce.siat.common.utils.enums.PositionType;
import org.guce.siat.common.utils.enums.Theme;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.guce.siat.web.ct.data.FileTypeAutorityData;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class UserController.
 */
@ManagedBean(name = "userController")
@SessionScoped
public class UserController extends AbstractController<User> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -8329077461925308701L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(User.class);

    /**
     * The Constant USER_EXIST_ERROR_MESSAGE.
     */
    private static final String USER_EXIST_ERROR_MESSAGE = "UserAlreadyExists";

    /**
     * The Constant AUTHORITIES_REQUIRED_MESSAGE_CREATE.
     */
    private static final String AUTHORITIES_REQUIRED_MESSAGE_CREATE = "CreateUserRequiredMessage_role";

    /**
     * The Constant AUTHORITIES_REQUIRED_MESSAGE_EDIT.
     */
    private static final String AUTHORITIES_REQUIRED_MESSAGE_EDIT = "EditUserRequiredMessage_role";

    /**
     * The Constant CONSTRAINT_VIOLATION_EXCEPTION.
     */
    private static final String CONSTRAINT_VIOLATION_EXCEPTION = "org.hibernate.exception.ConstraintViolationException";

    /**
     * The Constant MINISTRY_ALREADY_AFFECTED_MINESTER.
     */
    private static final String MINISTRY_ALREADY_AFFECTED_MINESTER = "CreateUserError_MinestryAffected";

    /**
     * The Constant MINISTRY_ALREADY_AFFECTED_GENERAL_SECRETARY.
     */
    private static final String MINISTRY_ALREADY_AFFECTED_GENERAL_SECRETARY = "CreateUserError_MinestrySGAffected";

    /**
     * The Constant ORGANISM_ALREADY_AFFECTED_DIRECTOR.
     */
    private static final String ORGANISM_ALREADY_AFFECTED_DIRECTOR = "CreateUserError_MinestryDirAffected";

    /**
     * The Constant ORGANISM_ALREADY_AFFECTED_SUPERVISOR.
     */
    private static final String ORGANISM_ALREADY_AFFECTED_SUPERVISOR = "CreateUserError_OrganismSuperAffected";

    /**
     * The all posts affected notification message.
     */
    private static final String ALL_POSTS_AFFECTED_NOTIF_MSG = "CreateUser_NotifPostsAffected";

    private static final String DEFAULT_STRING_PASSWORD = "12345678";

    /**
     * The user service.
     */
    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    /**
     * The ministry service.
     */
    @ManagedProperty(value = "#{ministryService}")
    private MinistryService ministryService;

    /**
     * The organism service.
     */
    @ManagedProperty(value = "#{organismService}")
    private OrganismService organismService;

    /**
     * The sub department service.
     */
    @ManagedProperty(value = "#{subDepartmentService}")
    private SubDepartmentService subDepartmentService;

    /**
     * The service service.
     */
    @ManagedProperty(value = "#{serviceService}")
    private ServiceService serviceService;

    /**
     * The entity service.
     */
    @ManagedProperty(value = "#{entityService}")
    private EntityService entityService;

    /**
     * The file type service.
     */
    @ManagedProperty(value = "#{fileTypeService}")
    private FileTypeService fileTypeService;

    /**
     * The user authority file type service.
     */
    @ManagedProperty(value = "#{userAuthorityFileTypeService}")
    private UserAuthorityFileTypeService userAuthorityFileTypeService;

    /**
     * The authority service.
     */
    @ManagedProperty(value = "#{authorityService}")
    private AuthorityService authorityService;

    /**
     * The user authority service.
     */
    @ManagedProperty(value = "#{userAuthorityService}")
    private UserAuthorityService userAuthorityService;

    /**
     * The mail service.
     */
    @ManagedProperty(value = "#{mailService}")
    private MailService mailService;

    /**
     * The selecteds sub department.
     */
    private SubDepartment selectedsSubDepartment;

    /**
     * The selected service.
     */
    private Service selectedService;

    /**
     * The selected entity.
     */
    private Entity selectedEntity;

    /**
     * The file type autority datas.
     */
    private List<List<FileTypeAutorityData>> fileTypeAutorityDatas;

    /**
     * The authorities list.
     */
    private List<Authority> authoritiesList;

    /**
     * The user auth file types.
     */
    private List<UserAuthorityFileType> userAuthFileTypes;

    /**
     * The file type list.
     */
    private List<FileType> fileTypeList;

    /**
     * The file type list.
     */
    private List<Administration> userAdministrationHierarchy;

    /**
     * The sub departments items.
     */
    private List<SelectItem> subDepartmentsItems;

    /**
     * The services items.
     */
    private List<SelectItem> servicesItems;

    /**
     * The entities items.
     */
    private List<SelectItem> entitiesItems;

    /**
     * The new password after reset
     */
    private String newPassword;

    private boolean edition;

    private PositionType historyPositionType;
    private List<List<FileTypeAutorityData>> historyfileTypeAutorityDatas;

    /**
     * Instantiates a new user controller.
     */
    public UserController() {
        super(User.class);
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, UserController.class.getName());
        }
        super.setService(userService);
        super.setPageUrl(ControllerConstants.Pages.BO.USER_INDEX_PAGE);
    }

    /**
     * Go to page.
     */
    @Override
    public void goToPage() {
        selected = null;
        super.goToPage();
    }

    /**
     * Checks if the administration has a user affected.
     *
     * @return true, if successful
     */
    private boolean hasUserAffected() {
        if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_ORGANISME.getCode())) {
            // check if organism is affected to Director
            if (PositionType.DIRECTEUR.equals(getSelected().getPosition())) {
                if (organismService.hasDirectorAffected(getCurrentOrganism())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            ORGANISM_ALREADY_AFFECTED_DIRECTOR));
                    return true;
                }
                getSelected().setAdministration(getCurrentOrganism());
            } // check if organism is affected to Supervisor
            else if (PositionType.SUPERVISEUR.equals(getSelected().getPosition())) {
                if (organismService.hasSupervisorAffected(getCurrentOrganism())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            ORGANISM_ALREADY_AFFECTED_SUPERVISOR));
                    return true;
                }
                getSelected().setAdministration(getCurrentOrganism());
            } else if (PositionType.SOUS_DIRECTEUR.equals(getSelected().getPosition())) {
                getSelected().setAdministration(selectedsSubDepartment);
            } else if (PositionType.CHEF_SERVICE.equals(getSelected().getPosition())) {
                getSelected().setAdministration(selectedService);
            } else if ((PositionType.CHEF_BUREAU.getCode()
                    + "," + PositionType.CHEF_SECTEUR.getCode()
                    + "," + PositionType.AGENT.getCode()
                    + "," + PositionType.OBSERVATEUR
                    .getCode()).contains(getSelected().getPosition().getCode())) {
                getSelected().setAdministration(selectedEntity);
            }
        } else if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_MINISTERE.getCode())) {
            if (PositionType.MINISTRE.equals(getSelected().getPosition()) && ministryService.hasMinisterAffected(getCurrentMinistry())) {
                // check if ministry is affected to a minister
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        MINISTRY_ALREADY_AFFECTED_MINESTER));
                return true;
            } else if (PositionType.SECRETAIRE_GENERAL.equals(getSelected().getPosition())
                    && ministryService.hasSGAffected(getCurrentMinistry())) {
                // check if ministry is affected to a general secretary
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        MINISTRY_ALREADY_AFFECTED_GENERAL_SECRETARY));
                return true;

            }
            getSelected().setAdministration(getCurrentMinistry());
        }
        return false;
    }

    /**
     * Creates the.
     */
    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#create()
     */
    @Override
    public void create() {
        getSelected().setAccountNonExpired(Boolean.TRUE);
        getSelected().setDeleted(Boolean.FALSE);
        getSelected().setAccountNonLocked(Boolean.TRUE);
        getSelected().setCredentialsNonExpired(Boolean.TRUE);
        getSelected().setFirstLogin(Boolean.TRUE);
        getSelected().setAttempts(Constants.ZERO);
        getSelected().setTheme(Theme.BLUE);
        final String radomPassword = RandomStringUtils.randomAlphanumeric(Constants.EIGHT);
        getSelected().setPassword(radomPassword);
        fillUserAuthorityFileType();
        try {
            if (!hasUserAffected()) {
                if (CollectionUtils.isNotEmpty(userAuthFileTypes)) {
                    userAuthorityFileTypeService.saveListUserAuthorityFileType(userAuthFileTypes, getSelected());
                    final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            User.class.getSimpleName() + PersistenceActions.CREATE.getCode());
                    JsfUtil.addSuccessMessage(msg);
                    //send mail params
                    final String subject = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            CREATE_SIAT_ACCOUNT_MAIL_SUBJECT);

                    final String templateFileName = localeFileTemplate(WELCOME_MAIL_BODY, getCurrentLocale().getLanguage());
                    Map<String, String> map = prepareMap(subject, mailService.getFromValue(), getSelected(), radomPassword, templateFileName);
                    mailService.sendMail(map);
                    resetAttributes();
                    if (!isValidationFailed()) {
                        refreshItems();
                        selected = null;
                    }
                } else {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            AUTHORITIES_REQUIRED_MESSAGE_CREATE));
                }

            }
        } catch (final PersistenceException pe) {
            LOG.error(null, pe);
            if (CONSTRAINT_VIOLATION_EXCEPTION.equals(pe.getCause().getClass().getName())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        USER_EXIST_ERROR_MESSAGE));
            } else {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        PERSISTENCE_ERROR_OCCURED));
            }
        }
    }

    /**
     * Gets the file type list.
     *
     * @return the file type list
     */
    public List<FileType> getFileTypeList() {
        return fileTypeList;
    }

    /**
     * Gets the authorities list.
     *
     * @return the authorities list
     */
    public List<Authority> getAuthoritiesList() {
        return authoritiesList;
    }

    /**
     * Prepare edit.
     */
    public void prepareEdit() {
        edition = true;
        historyPositionType = getSelected().getPosition();
        userAdministrationHierarchy = new ArrayList<>();
        if (getSelected() != null) {
            setSelected(userService.find(getSelected().getId()));
        }
        selectedsSubDepartment = null;
        selectedService = null;
        selectedEntity = null;
        if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_ORGANISME.getCode())) {
            if (null != getSelected().getPosition()) {
                switch (getSelected().getPosition()) {
                    case SOUS_DIRECTEUR:
                        selectedsSubDepartment = (SubDepartment) getSelected().getAdministration();
                        break;
                    case CHEF_SERVICE:
                        selectedService = (Service) getSelected().getAdministration();
                        selectedsSubDepartment = selectedService.getSubDepartment();
                        break;
                    case AGENT:
                    case CHEF_BUREAU:
                    case CHEF_SECTEUR:
                    case OBSERVATEUR:
                        selectedEntity = (Entity) getSelected().getAdministration();
                        selectedService = selectedEntity.getService();
                        selectedsSubDepartment = selectedService.getSubDepartment();
                        if (PositionType.CHEF_SECTEUR.equals(getSelected().getPosition())
                                || PositionType.AGENT.equals(getSelected().getPosition())
                                || PositionType.OBSERVATEUR.equals(getSelected().getPosition())) {
                            userAdministrationHierarchy = getAdministrationHierarchy(getSelected().getAdministration());
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        historyfileTypeAutorityDatas = new ArrayList<>();
        fileTypeAutorityDatas = new ArrayList<>();
        userAuthFileTypes = userAuthorityFileTypeService.getFileTypeAndAuthorityByUser(getSelected());
        fileTypeList = fileTypeService.findFileTypeByMinistry(getCurrentMinistry());
        authoritiesList = authorityService.findDistinctAutoritiesByPosition(getSelected().getPosition());
        for (FileType fileType : fileTypeList) {
            List<FileTypeAutorityData> innerList = new ArrayList<>();
            for (Authority authority : authoritiesList) {
                boolean matched = false;
                for (UserAuthorityFileType userAuthorityFileType : userAuthFileTypes) {
                    if (userAuthorityFileType.getFileType().getId().equals(fileType.getId())
                            && userAuthorityFileType.getUserAuthority().getAuthorityGranted().getId().equals(authority.getId())) {
                        innerList.add(new FileTypeAutorityData(authority, fileType, true));
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    innerList.add(new FileTypeAutorityData(authority, fileType, false));
                }
            }
            historyfileTypeAutorityDatas.add(innerList);
            fileTypeAutorityDatas.add(innerList);
        }

        positionChangedHandler();
        subDepartmentChangedHandler();
        serviceChangedHandler();
        bureauChangedHandler();
    }

    /**
     * Fill user authority file type.
     */
    private void fillUserAuthorityFileType() {
        userAuthFileTypes = new ArrayList<UserAuthorityFileType>();
        final Set<UserAuthority> authorities = new HashSet<UserAuthority>();
        for (final List<FileTypeAutorityData> vos : fileTypeAutorityDatas) {
            for (final FileTypeAutorityData fileTypeAutorityVo : vos) {
                if (fileTypeAutorityVo.isChecked()) {
                    final UserAuthority userAuthority = new UserAuthority();
                    final UserAuthorityFileType uaf = new UserAuthorityFileType();
                    final UserAuthorityFileTypeId uafId = new UserAuthorityFileTypeId();
                    userAuthority.setAuthorityGranted(fileTypeAutorityVo.getAuthority());
                    userAuthority.setUser(getSelected());
                    authorities.add(userAuthority);
                    uafId.setFileType(fileTypeAutorityVo.getFileType());
                    uafId.setUserAuthority(userAuthority);
                    uaf.setPrimaryKey(uafId);
                    userAuthFileTypes.add(uaf);
                }
            }
        }
        final List<UserAuthority> userAuthorities = new ArrayList<UserAuthority>();
        userAuthorities.addAll(authorities);
        getSelected().setUserAuthorityList(userAuthorities);
    }

    public void resetPassword() {

        if (selected == null) {
            return;
        }

        try {
            newPassword = userService.resetUserPassword(getSelected());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('PasswordResetViewDialog').show();");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("ResetPasswordError"));
        }
    }

    /**
     * Edits the.
     */
    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#edit()
     */
    @Override
    public void edit() {
        getSelected().setAccountNonExpired(Boolean.TRUE);
        getSelected().setDeleted(Boolean.FALSE);
        getSelected().setCredentialsNonExpired(Boolean.TRUE);
        fillUserAuthorityFileType();
        try {
            if (CollectionUtils.isNotEmpty(userAuthFileTypes)) {
                //avoid password loss
                String actualPassword = getSelected().getPassword();
                userAuthorityFileTypeService.updateListUserAuthorityFileType(getSelected(), userAuthFileTypes);
                if (getSelected().getAccountNonLocked()) {
                    getSelected().setAttempts(0);
                    getSelected().setAccountNonLocked(Boolean.TRUE);
                    getSelected().setLastAttemptsTime(null);
                }
                getSelected().setPassword(actualPassword);
                userService.update(getSelected());
                final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        User.class.getSimpleName() + PersistenceActions.UPDATE.getCode());

                JsfUtil.addSuccessMessage(msg);
                resetAttributes();
                if (!isValidationFailed()) {
                    refreshItems();
                    selected = null;
                }
            } else {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        AUTHORITIES_REQUIRED_MESSAGE_EDIT));
            }
        } catch (final PersistenceException pe) {
            LOG.error(null, pe);
            if (CONSTRAINT_VIOLATION_EXCEPTION.equals(pe.getCause().getClass().getName())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        PERSISTENCE_ERROR_OCCURED));
            } else {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        PERSISTENCE_ERROR_OCCURED));
            }
        }
    }

    /**
     * Delete.
     */
    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#delete()
     */
    @Override
    public void delete() {
        getSelected().setDeleted(true);
        getSelected().setAdministration(null);
        userService.update(getSelected());
        String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(User.class.getSimpleName().concat(PersistenceActions.DELETE.getCode()));
        JsfUtil.addSuccessMessage(msg);
        resetAttributes();
        if (!isValidationFailed()) {
            refreshItems();
            selected = null;
        }
    }

    /**
     * Reset attributes.
     */
    public void resetAttributes() {
        setSelected(null);
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
    public List<User> getItems() {
        if (items == null
                && ((getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_MINISTERE.getCode()) && getCurrentMinistry() != null) || (getLoggedUser()
                .getAuthoritiesList().contains(AuthorityConstants.ADMIN_ORGANISME.getCode()) && getCurrentOrganism() != null))) {
            if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_MINISTERE.getCode())) {
                items = userService.findUsersByAdministrationAndPositions(getCurrentMinistry(), PositionType.SECRETAIRE_GENERAL,
                        PositionType.MINISTRE);
            } else if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_ORGANISME.getCode())) {
                items = userService.findUsersByAdministrationAndPositions(getCurrentOrganism(), PositionType.DIRECTEUR,
                        PositionType.SOUS_DIRECTEUR, PositionType.CHEF_SERVICE, PositionType.CHEF_BUREAU, PositionType.CHEF_SECTEUR,
                        PositionType.AGENT,
                        PositionType.SUPERVISEUR, PositionType.OBSERVATEUR);
            }
        }

        return items;
    }

    /**
     * Gets the list authorities by user.
     *
     * @param user the user
     * @return the list authorities by user
     */
    public List<Authority> getListAuthoritiesByUser(final User user) {
        final List<Authority> listAuthoritiesByUser = new ArrayList<>();
        for (final User item : getItems()) {
            if (user != null && item.getId().equals(user.getId())) {
                listAuthoritiesByUser.addAll(item.getAuthorities());
                break;
            }
        }

        return listAuthoritiesByUser;
    }

    /**
     * Gets the positions options.
     *
     * @return the positions options
     */
    public SelectItem[] getPositionsOptions() {
        final SelectItem[] positionsOptions = new SelectItem[getPositionsListForFilter().size() + 1];
        positionsOptions[0] = new SelectItem(StringUtils.EMPTY, ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                .getString(SELECT_ONE_MESSAGE));
        for (int i = 0; i < getPositionsListForFilter().size(); i++) {
            positionsOptions[i + 1] = new SelectItem(getPositionsListForFilter().get(i),
                    Constants.LOCALE_ENGLISH.equals(getCurrentLocaleCode()) ? getPositionsListForFilter().get(i).getLabelEn()
                    : getPositionsListForFilter().get(i).getLabelFr());
        }

        return positionsOptions;
    }

    /**
     * Prepare create.
     */
    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
     */
    @Override
    public void prepareCreate() {
        edition = false;
        historyPositionType = null;
        historyfileTypeAutorityDatas = null;
        if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_MINISTERE.getCode())
                && ministryService.hasMinisterAffected(getCurrentMinistry()) && ministryService.hasSGAffected(getCurrentMinistry())) {
            JsfUtil.addWarningMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    ALL_POSTS_AFFECTED_NOTIF_MSG));
        } else {
            selectedsSubDepartment = null;
            selectedService = null;
            selectedEntity = null;
            userAdministrationHierarchy = new ArrayList<>();
            super.prepareCreate();
            fileTypeList = fileTypeService.findFileTypeByMinistry(getCurrentMinistry());
            if (selected != null) {
                initFileTypeAuthorityDatas();
            }
        }

    }

    /**
     * Reset form.
     */
    public void resetForm() {
        initFileTypeAuthorityDatas();
        selected = null;
        userAdministrationHierarchy = new ArrayList<>();
    }

    /**
     * Init the file type authority data.
     */
    private void initFileTypeAuthorityDatas() {
        fileTypeAutorityDatas = new ArrayList<>();
        if (getSelected().getPosition() != null) {
            authoritiesList = authorityService.findDistinctAutoritiesByPosition(getSelected().getPosition());
        }
        for (final FileType fileType : fileTypeList) {
            final List<FileTypeAutorityData> innerList = new ArrayList<>();
            if (authoritiesList != null) {
                for (final Authority authority : authoritiesList) {
                    innerList.add(new FileTypeAutorityData(authority, fileType, false));
                }
            }
            fileTypeAutorityDatas.add(innerList);
        }
    }

    /**
     * Handle position changed.
     */
    public void positionChangedHandler() {
        subDepartmentsItems = new ArrayList<>();
        userAdministrationHierarchy = new ArrayList<>();
        if (selected != null && !PositionType.DIRECTEUR.equals(getSelected().getPosition())) {
            List<SubDepartment> subDepartments;
            if (PositionType.SOUS_DIRECTEUR.equals(getSelected().getPosition())) {
                if (!edition) {
                    subDepartments = subDepartmentService.findNonAffectedByOrganism(getCurrentOrganism());
                } else {
                    subDepartments = Arrays.asList(selectedsSubDepartment);
                }
            } else {
                subDepartments = subDepartmentService.findSubDepartmentsByOrganism(getCurrentOrganism());
            }
            for (final SubDepartment subDepartment : subDepartments) {
                subDepartmentsItems.add(new SelectItem(subDepartment, subDepartment.getAbreviation()));
            }
        }
        initFileTypeAuthorityDatas();
        if (historyPositionType != null && historyfileTypeAutorityDatas != null && historyPositionType.equals(getSelected().getPosition())) {
            fileTypeAutorityDatas = new ArrayList<>(historyfileTypeAutorityDatas);
        }
    }

    /**
     * Sub department changed handler.
     */
    public void subDepartmentChangedHandler() {
        servicesItems = new ArrayList<>();

        if (selectedsSubDepartment != null && !PositionType.DIRECTEUR.equals(getSelected().getPosition())
                && !PositionType.SOUS_DIRECTEUR.equals(getSelected().getPosition())) {
            List<Service> services;
            if (PositionType.CHEF_SERVICE.equals(getSelected().getPosition())) {
                if (!edition) {
                    services = serviceService.findNonAffectedServicesBySubDepartment(selectedsSubDepartment);
                } else {
                    services = Arrays.asList(selectedService);
                }
            } else {
                services = selectedsSubDepartment.getServicesList();
            }
            for (final Service service : services) {
                servicesItems.add(new SelectItem(service, service.getAbreviation()));
            }

        }
    }

    /**
     * Service changed handler.
     */
    public void serviceChangedHandler() {
        entitiesItems = new ArrayList<>();
        if (selectedService == null) {
            return;
        }

        if (PositionType.CHEF_BUREAU.equals(getSelected().getPosition())
                || PositionType.CHEF_SECTEUR.equals(getSelected().getPosition())
                || PositionType.AGENT.equals(getSelected().getPosition())
                || PositionType.OBSERVATEUR.equals(getSelected().getPosition())) {
            List<Entity> entities;
            if (!edition) {
                entities = entityService.findNonAffectedEntityByServiceAndPosition(selectedService, getSelected().getPosition());
            } else {
                entities = Arrays.asList(selectedEntity);
            }
            for (Entity ent : entities) {
                entitiesItems.add(new SelectItem(ent, ent.getCode()));
            }
        }
    }

    /**
     * Service changed handler.
     */
    public void bureauChangedHandler() {
        userAdministrationHierarchy = new ArrayList<>();
        if (selected != null && (PositionType.AGENT.equals(getSelected().getPosition())
                || PositionType.OBSERVATEUR.equals(getSelected().getPosition()))) {
            userAdministrationHierarchy = getAdministrationHierarchy(selectedEntity);
        }
    }

    /**
     * Gets the positions list for filter.
     *
     * @return the positions list for filter
     */
    private List<PositionType> getPositionsListForFilter() {
        final List<PositionType> positionsList = new ArrayList<>();
        if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_ORGANISME.getCode())) {
            if (!organismService.hasDirectorAffected(getCurrentOrganism())) {
                positionsList.add(PositionType.DIRECTEUR);
            }
            if (!organismService.hasSupervisorAffected(getCurrentOrganism())) {
                positionsList.add(PositionType.SUPERVISEUR);
            }
            positionsList.add(PositionType.SOUS_DIRECTEUR);
            positionsList.add(PositionType.CHEF_SERVICE);
            positionsList.add(PositionType.CHEF_BUREAU);
            positionsList.add(PositionType.CHEF_SECTEUR);
            positionsList.add(PositionType.AGENT);
            positionsList.add(PositionType.OBSERVATEUR);
        }
        if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_MINISTERE.getCode())) {
            if (!ministryService.hasMinisterAffected(getCurrentMinistry())) {
                positionsList.add(PositionType.MINISTRE);
            }
            if (!ministryService.hasSGAffected(getCurrentMinistry())) {
                positionsList.add(PositionType.SECRETAIRE_GENERAL);
            }
        }
        return positionsList;
    }

    /**
     * Gets the authorities options.
     *
     * @return the authorities options
     */
    public SelectItem[] getAuthoritiesOptions() {
        final SelectItem[] grantedAuthoritiesOptions = new SelectItem[getAuthoritiesListForFilter().size() + 1];
        grantedAuthoritiesOptions[0] = new SelectItem(StringUtils.EMPTY, ResourceBundle.getBundle(LOCAL_BUNDLE_NAME,
                getCurrentLocale()).getString(SELECT_ONE_MESSAGE));
        for (int i = 0; i < getAuthoritiesListForFilter().size(); i++) {
            grantedAuthoritiesOptions[i + 1] = new SelectItem(getAuthoritiesListForFilter().get(i).getCode(),
                    getAuthoritiesListForFilter().get(i).getLabel());
        }
        return grantedAuthoritiesOptions;
    }

    /**
     * Gets the authorities list for filter.
     *
     * @return the authorities list for filter
     */
    private List<AuthorityConstants> getAuthoritiesListForFilter() {

        final List<AuthorityConstants> authoritiesListForFilter = new ArrayList<>();

        authoritiesListForFilter.add(AuthorityConstants.AGENT_RECEVABILITE);
        authoritiesListForFilter.add(AuthorityConstants.INSPECTEUR);
        authoritiesListForFilter.add(AuthorityConstants.CONTROLEUR);
        authoritiesListForFilter.add(AuthorityConstants.LABORATOIRE);
        authoritiesListForFilter.add(AuthorityConstants.SOCIETE_TRAITEMENT);
        authoritiesListForFilter.add(AuthorityConstants.SIGNATAIRE);
        authoritiesListForFilter.add(AuthorityConstants.RESPONSABLE_TRAITEMENT);
        authoritiesListForFilter.add(AuthorityConstants.AGENT_COTATION_1);
        authoritiesListForFilter.add(AuthorityConstants.AGENT_COTATION_2);
        authoritiesListForFilter.add(AuthorityConstants.AGENT_COTATION_3);
        authoritiesListForFilter.add(AuthorityConstants.AGENT_COTATION_4);
        authoritiesListForFilter.add(AuthorityConstants.AGENT_COTATION_5);
        authoritiesListForFilter.add(AuthorityConstants.AGENT_COTATION_6);
        authoritiesListForFilter.add(AuthorityConstants.SUPERVISEUR);
        authoritiesListForFilter.add(AuthorityConstants.CONSULTER);
        authoritiesListForFilter.add(AuthorityConstants.STATISTIQUE);
        authoritiesListForFilter.add(AuthorityConstants.GESTION_AGENTS);

        return authoritiesListForFilter;
    }

    private List<Administration> getAdministrationHierarchy(Administration a) {
        List<Administration> adms = new ArrayList<>();
        adms.add(a);
        if (a instanceof Organism) {
            adms.addAll(getAdministrationHierarchy(((Organism) a).getMinistry()));
        } else if (a instanceof SubDepartment) {
            adms.addAll(getAdministrationHierarchy(((SubDepartment) a).getOrganism()));
        } else if (a instanceof Service) {
            adms.addAll(getAdministrationHierarchy(((Service) a).getSubDepartment()));
        } else if (a instanceof Bureau) {
            adms.addAll(getAdministrationHierarchy(((Bureau) a).getService()));
        }
        return adms;
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
     * Gets the selecteds sub department.
     *
     * @return the selecteds sub department
     */
    public SubDepartment getSelectedsSubDepartment() {
        return selectedsSubDepartment;
    }

    /**
     * Sets the selecteds sub department.
     *
     * @param selectedsSubDepartment the new selecteds sub department
     */
    public void setSelectedsSubDepartment(final SubDepartment selectedsSubDepartment) {
        this.selectedsSubDepartment = selectedsSubDepartment;
    }

    /**
     * Gets the selected service.
     *
     * @return the selected service
     */
    public Service getSelectedService() {
        return selectedService;
    }

    /**
     * Sets the selected service.
     *
     * @param selectedService the new selected service
     */
    public void setSelectedService(final Service selectedService) {
        this.selectedService = selectedService;
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

    /**
     * Gets the sub department service.
     *
     * @return the sub department service
     */
    public SubDepartmentService getSubDepartmentService() {
        return subDepartmentService;
    }

    /**
     * Sets the sub department service.
     *
     * @param subDepartmentService the new sub department service
     */
    public void setSubDepartmentService(final SubDepartmentService subDepartmentService) {
        this.subDepartmentService = subDepartmentService;
    }

    /**
     * Sets the entity service.
     *
     * @param entityService the new entity service
     */
    public void setEntityService(final EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * Gets the entity service.
     *
     * @return the entity service
     */
    public EntityService getEntityService() {
        return entityService;
    }

    /**
     * Gets the ministry service.
     *
     * @return the ministry service
     */
    public MinistryService getMinistryService() {
        return ministryService;
    }

    /**
     * Sets the ministry service.
     *
     * @param ministryService the new ministry service
     */
    public void setMinistryService(final MinistryService ministryService) {
        this.ministryService = ministryService;
    }

    /**
     * Gets the organism service.
     *
     * @return the organism service
     */
    public OrganismService getOrganismService() {
        return organismService;
    }

    /**
     * Sets the organism service.
     *
     * @param organismService the new organism service
     */
    public void setOrganismService(final OrganismService organismService) {
        this.organismService = organismService;
    }

    /**
     * Gets the selected entity.
     *
     * @return the selected entity
     */
    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    /**
     * Sets the selected entity.
     *
     * @param selectedEntity the new selected entity
     */
    public void setSelectedEntity(final Entity selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    /**
     * Gets the file type service.
     *
     * @return the file type service
     */
    public FileTypeService getFileTypeService() {
        return fileTypeService;
    }

    /**
     * Sets the file type service.
     *
     * @param fileTypeService the new file type service
     */
    public void setFileTypeService(final FileTypeService fileTypeService) {
        this.fileTypeService = fileTypeService;
    }

    /**
     * Gets the authority service.
     *
     * @return the authority service
     */
    public AuthorityService getAuthorityService() {
        return authorityService;
    }

    /**
     * Sets the authority service.
     *
     * @param authorityService the new authority service
     */
    public void setAuthorityService(final AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * Gets the user authority file type service.
     *
     * @return the user authority file type service
     */
    public UserAuthorityFileTypeService getUserAuthorityFileTypeService() {
        return userAuthorityFileTypeService;
    }

    /**
     * Sets the user authority file type service.
     *
     * @param userAuthorityFileTypeService the new user authority file type
     * service
     */
    public void setUserAuthorityFileTypeService(final UserAuthorityFileTypeService userAuthorityFileTypeService) {
        this.userAuthorityFileTypeService = userAuthorityFileTypeService;
    }

    /**
     * Gets the file type autority datas.
     *
     * @return the fileTypeAutorityDatas
     */
    public List<List<FileTypeAutorityData>> getFileTypeAutorityDatas() {
        return fileTypeAutorityDatas;
    }

    /**
     * Sets the file type authority data.
     *
     * @param fileTypeAutorityDatas the fileTypeAutorityDatas to set
     */
    public void setFileTypeAutorityDatas(final List<List<FileTypeAutorityData>> fileTypeAutorityDatas) {
        this.fileTypeAutorityDatas = fileTypeAutorityDatas;
    }

    /**
     * Gets the sub departments items.
     *
     * @return the subDepartmentsItems
     */
    public List<SelectItem> getSubDepartmentsItems() {
        if (subDepartmentsItems == null) {
            subDepartmentsItems = new ArrayList<>();
        }
        return subDepartmentsItems;
    }

    /**
     * Gets the services items.
     *
     * @return the servicesItems
     */
    public List<SelectItem> getServicesItems() {
        if (servicesItems == null) {
            servicesItems = new ArrayList<>();
        }
        return servicesItems;
    }

    /**
     * Gets the entities items.
     *
     * @return the entitiesItems
     */
    public List<SelectItem> getEntitiesItems() {
        if (entitiesItems == null) {
            entitiesItems = new ArrayList<>();
        }
        return entitiesItems;
    }

    /**
     * Gets the user authority service.
     *
     * @return the userAuthorityService
     */
    public UserAuthorityService getUserAuthorityService() {
        return userAuthorityService;
    }

    /**
     * Sets the user authority service.
     *
     * @param userAuthorityService the userAuthorityService to set
     */
    public void setUserAuthorityService(final UserAuthorityService userAuthorityService) {
        this.userAuthorityService = userAuthorityService;
    }

    /**
     * Gets the mail service.
     *
     * @return the mail service
     */
    public MailService getMailService() {
        return mailService;
    }

    /**
     * Sets the mail service.
     *
     * @param mailService the new mail service
     */
    public void setMailService(final MailService mailService) {
        this.mailService = mailService;
    }

    public List<Administration> getUserAdministrationHierarchy() {
        return userAdministrationHierarchy;
    }

    public void setUserAdministrationHierarchy(List<Administration> userAdministrationHierarchy) {
        this.userAdministrationHierarchy = userAdministrationHierarchy;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
