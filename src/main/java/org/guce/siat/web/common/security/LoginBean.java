package org.guce.siat.web.common.security;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.BooleanUtils;
import org.guce.siat.common.mail.MailConstants;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Entity;
import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.Ministry;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.ParamsOrganism;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.AdministrationService;
import org.guce.siat.common.service.DelegationService;
import org.guce.siat.common.service.EntityService;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.common.service.MailService;
import org.guce.siat.common.service.MinistryService;
import org.guce.siat.common.service.OrganismService;
import org.guce.siat.common.service.ParamsOrganismService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.SubDepartmentService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.ParamsCategory;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.common.util.ServletUtils;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * The Class LoginBean.
 */
@ManagedBean(name = "loginMgmtBean")
@RequestScoped
@SessionScoped
public class LoginBean implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -8770486440615437519L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(LoginBean.class);

    /**
     * The Constant LOCAL_BUNDLE_NAME.
     */
    private static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";

    /**
     * The Constant AUTHENTIFICATION_PROBLEM.
     */
    private static final String AUTHENTIFICATION_PROBLEM = "authentification_problem";

    /**
     * The Constant RESET_CHANGE_SUCCESS.
     */
    private static final String RESET_CHANGE_SUCCESS = "reset_change_success";

    /**
     * The Constant SEND_MAIL_MESSAGE.
     */
    private static final String SEND_MAIL_MESSAGE = "send_mail_message";

    /**
     * The Constant EMAIL_BODY.
     */
    private static final String EMAIL_BODY = "emailBodyUserLocked";

    /**
     * The Constant EMAIL_SUBJECT_USER_LOCKED.
     */
    private static final String EMAIL_SUBJECT_USER_LOCKED = "send_mail_title";

    /**
     * The user name.
     */
    private String userName;

    /**
     * The password.
     */
    private String password;

    /**
     * The logged user.
     */
    private User loggedUser;

    /**
     * The bundle.
     */
    private ResourceBundle bundle;

    /**
     * The context.
     */
    private FacesContext context;

    /**
     * The new password.
     */
    private String newPassword;

    /**
     * The gr params organism map.
     */
    private Map<String, ParamsOrganism> grParamsOrganismMap;

    /**
     * The authentication manager.
     */
    @ManagedProperty(value = "#{authenticationManager}")
    private AuthenticationManager authenticationManager;

    /**
     * The administration service.
     */
    @ManagedProperty(value = "#{administrationService}")
    private AdministrationService administrationService;

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
     * The user service.
     */
    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    /**
     * The delegation service.
     */
    @ManagedProperty(value = "#{delegationService}")
    private DelegationService delegationService;

    /**
     * The params organism service.
     */
    @ManagedProperty(value = "#{paramsOrganismService}")
    private ParamsOrganismService paramsOrganismService;

    /**
     * The mail service.
     */
    @ManagedProperty(value = "#{mailService}")
    private MailService mailService;

    /**
     * The file type service.
     */
    @ManagedProperty(value = "#{fileTypeService}")
    private FileTypeService fileTypeService;

    /**
     * The session authentication strategy.
     */
    @ManagedProperty(value = "#{sessionAuthenticationStrategy}")
    private SessionAuthenticationStrategy sessionAuthenticationStrategy;

    /**
     * The session registry.
     */
    @ManagedProperty(value = "#{sessionRegistry}")
    private SessionRegistry sessionRegistry;

    /**
     * The ap rendred.
     */
    private Boolean apRendred;

    /**
     * The cct rendred.
     */
    private Boolean cctRendred;

    /**
     * The search render.
     */
    private Boolean searchRender;

    /**
     * The payment render.
     */
    private Boolean paymentRender;

    /**
     * Instantiates a new login bean.
     */
    public LoginBean() {
        context = FacesContext.getCurrentInstance();
        bundle = context.getApplication().getResourceBundle(context, "bundle");
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, LoginBean.class.getName());
        }
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            final String username = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
            final User user = userService.findByLogin(username);
            if (!user.getFirstLogin()) {
                final FacesContext facesContext = FacesContext.getCurrentInstance();
                final ExternalContext extContext = facesContext.getExternalContext();
                final String url = extContext.encodeActionURL(facesContext.getApplication().getViewHandler()
                        .getActionURL(facesContext, ControllerConstants.Pages.INDEX_PAGE));
                try {
                    extContext.redirect(url);
                } catch (final IOException e) {
                    LOG.error(e.getMessage(), e);
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            AUTHENTIFICATION_PROBLEM));
                }
            }
        }
    }

    /**
     * Login.
     *
     * @return the string
     * @throws ServletException the servlet exception
     */
    public String login() throws ServletException {
        try {
            ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
            final Authentication request = new UsernamePasswordAuthenticationToken(this.getUserName(), this.getPassword());
            final Authentication result = authenticationManager.authenticate(request);
            final HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();
            final HttpServletResponse httpResp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
                    .getResponse();

            sessionAuthenticationStrategy.onAuthentication(result, httpReq, httpResp);

            SecurityContextHolder.getContext().setAuthentication(result);

            final User user = userService.findByLogin(userName);

            ServletUtils.setCookies("theme", user.getTheme().getCode());

            this.setLoggedUser(user);
            final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);

            //reset fail attempts connection of the user and unlock the user
            user.setAttempts(0);
            user.setAccountNonLocked(Boolean.TRUE);
            user.setLastAttemptsTime(null);
            userService.update(user);

            final Administration currentAdministration = user.getAdministration();
            final Ministry currentMinistry = ministryService.findMinistryByUser(user);
            final Organism currentOrganism = organismService.findOrganismByUser(user);
            final SubDepartment currentSubDepartment = subDepartmentService.findSubDepartmentByUser(user);
            final Service currentService = serviceService.findServiceByUser(user);
            final Entity currentEntity = entityService.findEntityByUser(user);

            session.setAttribute("locale", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            session.setAttribute("loggedUser", loggedUser);
            session.setAttribute("currentAdministration", currentAdministration);
            session.setAttribute("currentMinistry", currentMinistry);
            session.setAttribute("currentOrganism", currentOrganism);
            session.setAttribute("currentSubDepartment", currentSubDepartment);
            session.setAttribute("currentService", currentService);
            session.setAttribute("currentEntity", currentEntity);

            // Chargement de la liste des params organism dans la session
            if (currentOrganism != null) {
                addParamsOrganismInSession(currentOrganism);
            }

            session.setAttribute("authorities", user.getAuthorities());

            if (user.getFirstLogin()) {
                return ControllerConstants.Pages.RESET_PASSWORD_PAGE_REDIRECT;
            }

            // Conditional Redirection (AP, CCT, Search)
            userDashboardsRender();

            if (BooleanUtils.isTrue(cctRendred)) {
                return ControllerConstants.Pages.FO.DASHBOARD_CCT_INDEX_PAGE_REDIRECT;
            } else if (BooleanUtils.isTrue(apRendred)) {
                return ControllerConstants.Pages.FO.DASHBOARD_AP_INDEX_PAGE_REDIRECT;
            } else if (BooleanUtils.isTrue(searchRender)) {
                return ControllerConstants.Pages.FO.SIMPLE_SEARCH_INDEX_PAGE_REDIRECT;
            } else if (BooleanUtils.isTrue(paymentRender)) {
                return ControllerConstants.Pages.FO.PAYMENT_INDEX_PAGE_REDIRECT;
            } else {
                return ControllerConstants.Pages.INDEX_PAGE;
            }

        } catch (final BadCredentialsException bce) {
            LOG.error("######  An attempt to connect with bad credentials " + userName, bce);

            final User user = userService.findByLogin(userName);

            // increments the attempts column if the attempts number < maxAttemptsNumber -1 else lock the user
            userService.updateFailAttempts(user);

            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    "authentification_loginFailed_badCredentials"));
        } catch (final LockedException le) {
            LOG.error("######  An attempt to connect with Locked account " + userName, le);

            final User user = userService.findByLogin(userName);

            sendEmail(user);

            final String message = MessageFormat.format(
                    ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            "authentification_loginFailed_lockedUser"), ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                            .getString(SEND_MAIL_MESSAGE), user.getLogin(), DateUtils.formatSimpleDateForOracle(user
                    .getLastAttemptsTime()));
            JsfUtil.addErrorMessage(message);
        } catch (final SessionAuthenticationException sae) {
            //maximum of sessions exceeded
            LOG.error("######  An attempt to connect ( maximum of sessions exceeded ) " + userName, sae);

            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    "logPage_maximumSessionsexceeded"));

            RequestContext.getCurrentInstance().execute("PF('logoutAthorSession').show();");
        } catch (final AuthenticationException ae) {
            LOG.error("######  An attempt to connect ( AuthenticationException ) " + userName, ae);

            if (userName.isEmpty() && password.isEmpty()) {

                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        "authentification_loginFailed_accessDenied_user_password"));
            } else if (password.isEmpty()) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        "authentification_loginFailed_accessDenied_password"));
            } else if (userName.isEmpty()) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        "authentification_loginFailed_accessDenied_user"));
            } else {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        "authentification_loginFailed_accessDenied_prob"));
            }
        }

        return null;
    }

    /**
     * User dashboards render.
     */
    private void userDashboardsRender() {

        final List<FileTypeCode> apCodes = Arrays.asList(FileTypeCode.AIE_MINADER, FileTypeCode.EH_MINADER,
                FileTypeCode.AS_MINADER, FileTypeCode.CAT_MINADER, FileTypeCode.PIVPSRP_MINADER, FileTypeCode.DI_MINADER,
                FileTypeCode.AT_MINSANTE, FileTypeCode.VTP_MINSANTE, FileTypeCode.VTD_MINSANTE, FileTypeCode.AI_MINSANTE,
                FileTypeCode.AI_MINMIDT, FileTypeCode.AE_MINMIDT, FileTypeCode.CEA_MINMIDT, FileTypeCode.AT_MINEPIA,
                FileTypeCode.VT_MINEPIA, FileTypeCode.VT_MINEPDED, FileTypeCode.CP_MINEPDED, FileTypeCode.AS_MINFOF,
                FileTypeCode.AS_MINCOMMERCE, FileTypeCode.CO_MINFOF_FORET, FileTypeCode.CO_MINFOF_FAUNE, FileTypeCode.BSBE_MINFOF, FileTypeCode.FIMEX_WF);

        apRendred = Boolean.FALSE;

        cctRendred = Boolean.FALSE;

        final List<FileTypeCode> cctCodes = Arrays.asList(FileTypeCode.CCT_CT, FileTypeCode.CC_CT, FileTypeCode.CQ_CT,
                FileTypeCode.CCT_CT_E, FileTypeCode.CCT_CT_E_ATP, FileTypeCode.CCT_CT_E_FSTP, FileTypeCode.CCT_CT_E_PVE, FileTypeCode.CCT_CT_E_PVI);

        final List<FileType> fileTypes = fileTypeService.findDistinctFileTypesByUser(getLoggedUser());
        final Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

        for (final FileType fileType : fileTypes) {
            if (apCodes.contains(fileType.getCode())) {
                sessionMap.put("apRendred", true);
                apRendred = Boolean.TRUE;
            } else if (cctCodes.contains(fileType.getCode())) {
                sessionMap.put("cctRendred", true);
                cctRendred = Boolean.TRUE;
            }
        }
        if (getLoggedUser().getAuthoritiesList().contains("SUPER") || getLoggedUser().getAuthoritiesList().contains("CONS")) {
            apRendred = Boolean.FALSE;
            cctRendred = Boolean.FALSE;
            searchRender = Boolean.TRUE;
        }
        if (getLoggedUser().getAuthoritiesList().contains("CA")) {
            apRendred = Boolean.FALSE;
            cctRendred = Boolean.FALSE;
            paymentRender = Boolean.TRUE;
        }

    }

    /**
     * Confirm logout other session.
     *
     * @return the
     */
    public String confirmLogoutOtherSession() {

        final List<Object> sList = sessionRegistry.getAllPrincipals();
        for (final Object principal : sList) {
            final UserDetails userDetails = (UserDetails) principal;
            if (userDetails.getUsername().equals(userName)) {
                for (final SessionInformation information : sessionRegistry.getAllSessions(userDetails, true)) {
                    information.expireNow();
                }
            }
        }
        try {
            return this.login();
        } catch (final ServletException ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * Adds the params organism in session.
     *
     * @param organism the organism
     */
    private void addParamsOrganismInSession(final Organism organism) {
        grParamsOrganismMap = new HashMap<>();

        // Chargement de la liste des params organism dans la session
        final List<ParamsOrganism> grParamsOrganism = paramsOrganismService.findParamsOrganismByOrganism(organism,
                ParamsCategory.GR);

        for (final ParamsOrganism params : grParamsOrganism) {
            grParamsOrganismMap.put(params.getParam().getName(), params);
        }

        final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("grParamsMap", grParamsOrganismMap);
    }

    /**
     * Validation new password.
     *
     * @throws ServletException the servlet exception
     */
    public void validationNewPassword() throws ServletException {
        try {
            final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            final String nomUtilisateur = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
            final User user = userService.findByLogin(nomUtilisateur);

            user.setFirstLogin(false);
            user.setPassword(newPassword);
            userService.updateUser(user);

            final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(RESET_CHANGE_SUCCESS);

            JsfUtil.addSuccessMessage(msg);
            SecurityContextHolder.getContext().setAuthentication(null);

        } catch (final Exception ex) {
            JsfUtil.addErrorMessage(ex, ex.getMessage());
        }
        try {
            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final ExternalContext extContext = facesContext.getExternalContext();
            final String url = extContext.encodeActionURL(facesContext.getApplication().getViewHandler()
                    .getActionURL(facesContext, ControllerConstants.Pages.INDEX_PAGE));
            extContext.redirect(url);
        } catch (final IOException ex) {
            JsfUtil.addErrorMessage(ex, ex.getMessage());
        }
    }

    /**
     * Send email.
     *
     * @param user the user
     */
    public void sendEmail(final User user) {
        final String templateFileName = localeFileTemplate(EMAIL_BODY, getCurrentLocale().getLanguage());
        Map<String, String> map = new HashMap<>();
        map = prepareMap(user, templateFileName);
        mailService.sendMail(map);
    }

    /**
     * Prepare map.
     *
     * @param user the user
     * @param template the template
     * @return the map
     */
    private Map<String, String> prepareMap(final User user, final String template) {
        final Map<String, String> map = new HashMap<>();

        map.put(MailConstants.SUBJECT,
                ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(EMAIL_SUBJECT_USER_LOCKED));
        map.put(MailConstants.FROM, mailService.getFromValue());
        map.put(MailConstants.EMAIL, user.getEmail());
        map.put("firstName", user.getFirstName());
        map.put("login", user.getLogin());
        map.put("dateTentative", user.getLastAttemptsTime() == null ? null : DateUtils.formatSimpleDateForOracle(user.getLastAttemptsTime()));
        map.put(MailConstants.VMF, template);
        return map;
    }

    /**
     * Locale file template.
     *
     * @param fileName the file name
     * @param language the language
     * @return the string
     */
    private String localeFileTemplate(final String fileName, final String language) {
        final StringBuilder templateFileName = new StringBuilder(fileName);
        templateFileName.append("_").append(language).append(".vm");
        return templateFileName.toString();
    }

    /**
     * Gets the current locale.
     *
     * @return the current locale
     */
    public Locale getCurrentLocale() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    /**
     * Cancel.
     *
     * @return the string
     */
    public String cancel() {
        return null;
    }

    /**
     * Gets the user name.
     *
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName the userName to set
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Gets the logged user.
     *
     * @return the loggedUser
     */
    public User getLoggedUser() {
        return loggedUser;
    }

    /**
     * Sets the logged user.
     *
     * @param loggedUser the loggedUser to set
     */
    public void setLoggedUser(final User loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * Gets the bundle.
     *
     * @return the bundle
     */
    public ResourceBundle getBundle() {
        return bundle;
    }

    /**
     * Sets the bundle.
     *
     * @param bundle the bundle to set
     */
    public void setBundle(final ResourceBundle bundle) {
        this.bundle = bundle;
    }

    /**
     * Gets the context.
     *
     * @return the context
     */
    public FacesContext getContext() {
        return context;
    }

    /**
     * Sets the context.
     *
     * @param context the context to set
     */
    public void setContext(final FacesContext context) {
        this.context = context;
    }

    /**
     * Gets the new password.
     *
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the new password.
     *
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Gets the gr params organism map.
     *
     * @return the grParamsOrganismMap
     */
    public Map<String, ParamsOrganism> getGrParamsOrganismMap() {
        return grParamsOrganismMap;
    }

    /**
     * Sets the gr params organism map.
     *
     * @param grParamsOrganismMap the grParamsOrganismMap to set
     */
    public void setGrParamsOrganismMap(final Map<String, ParamsOrganism> grParamsOrganismMap) {
        this.grParamsOrganismMap = grParamsOrganismMap;
    }

    /**
     * Gets the authentication manager.
     *
     * @return the authenticationManager
     */
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    /**
     * Sets the authentication manager.
     *
     * @param authenticationManager the authenticationManager to set
     */
    public void setAuthenticationManager(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Gets the administration service.
     *
     * @return the administrationService
     */
    public AdministrationService getAdministrationService() {
        return administrationService;
    }

    /**
     * Sets the administration service.
     *
     * @param administrationService the administrationService to set
     */
    public void setAdministrationService(final AdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    /**
     * Gets the ministry service.
     *
     * @return the ministryService
     */
    public MinistryService getMinistryService() {
        return ministryService;
    }

    /**
     * Sets the ministry service.
     *
     * @param ministryService the ministryService to set
     */
    public void setMinistryService(final MinistryService ministryService) {
        this.ministryService = ministryService;
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
     * Gets the sub department service.
     *
     * @return the subDepartmentService
     */
    public SubDepartmentService getSubDepartmentService() {
        return subDepartmentService;
    }

    /**
     * Sets the sub department service.
     *
     * @param subDepartmentService the subDepartmentService to set
     */
    public void setSubDepartmentService(final SubDepartmentService subDepartmentService) {
        this.subDepartmentService = subDepartmentService;
    }

    /**
     * Gets the service service.
     *
     * @return the serviceService
     */
    public ServiceService getServiceService() {
        return serviceService;
    }

    /**
     * Sets the service service.
     *
     * @param serviceService the serviceService to set
     */
    public void setServiceService(final ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Gets the entity service.
     *
     * @return the entityService
     */
    public EntityService getEntityService() {
        return entityService;
    }

    /**
     * Sets the entity service.
     *
     * @param entityService the entityService to set
     */
    public void setEntityService(final EntityService entityService) {
        this.entityService = entityService;
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
     * Gets the params organism service.
     *
     * @return the paramsOrganismService
     */
    public ParamsOrganismService getParamsOrganismService() {
        return paramsOrganismService;
    }

    /**
     * Sets the params organism service.
     *
     * @param paramsOrganismService the paramsOrganismService to set
     */
    public void setParamsOrganismService(final ParamsOrganismService paramsOrganismService) {
        this.paramsOrganismService = paramsOrganismService;
    }

    /**
     * Gets the mail service.
     *
     * @return the mailService
     */
    public MailService getMailService() {
        return mailService;
    }

    /**
     * Sets the mail service.
     *
     * @param mailService the mailService to set
     */
    public void setMailService(final MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * Gets the file type service.
     *
     * @return the fileTypeService
     */
    public FileTypeService getFileTypeService() {
        return fileTypeService;
    }

    /**
     * Sets the file type service.
     *
     * @param fileTypeService the fileTypeService to set
     */
    public void setFileTypeService(final FileTypeService fileTypeService) {
        this.fileTypeService = fileTypeService;
    }

    /**
     * Gets the session authentication strategy.
     *
     * @return the sessionAuthenticationStrategy
     */
    public SessionAuthenticationStrategy getSessionAuthenticationStrategy() {
        return sessionAuthenticationStrategy;
    }

    /**
     * Sets the session authentication strategy.
     *
     * @param sessionAuthenticationStrategy the sessionAuthenticationStrategy to
     * set
     */
    public void setSessionAuthenticationStrategy(final SessionAuthenticationStrategy sessionAuthenticationStrategy) {
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
    }

    /**
     * Gets the session registry.
     *
     * @return the sessionRegistry
     */
    public SessionRegistry getSessionRegistry() {
        return sessionRegistry;
    }

    /**
     * Sets the session registry.
     *
     * @param sessionRegistry the sessionRegistry to set
     */
    public void setSessionRegistry(final SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * Gets the ap rendred.
     *
     * @return the ap rendred
     */
    public Boolean getApRendred() {
        return apRendred;
    }

    /**
     * Sets the ap rendred.
     *
     * @param apRendred the new ap rendred
     */
    public void setApRendred(final Boolean apRendred) {
        this.apRendred = apRendred;
    }

    /**
     * Gets the cct rendred.
     *
     * @return the cct rendred
     */
    public Boolean getCctRendred() {
        return cctRendred;
    }

    /**
     * Sets the cct rendred.
     *
     * @param cctRendred the new cct rendred
     */
    public void setCctRendred(final Boolean cctRendred) {
        this.cctRendred = cctRendred;
    }

}
