package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.Theme;
import org.guce.siat.core.ct.model.ParamCCTCP;
import org.guce.siat.core.ct.service.ParamCCTCPService;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.common.util.ServletUtils;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AccountSetupController.
 */
@SessionScoped
@ManagedBean(name = "accountSetupController")
public class AccountSetupController implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -3827154485703748011L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AccountSetupController.class);

    /**
     * The Constant SELECT_ONE_MESSAGE.
     */
    private static final String SELECT_ONE_MESSAGE = "SelectOneMessage";

    /**
     * The Constant LOCAL_BUNDLE_NAME.
     */
    private static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";

    /**
     * The theme page url.
     */
    private String themePageUrl;

    /**
     * The theme page url.
     */
    private String cctCpConfigPageUrl;
    /**
     * The logged user.
     */
    private User loggedUser;

    /**
     * The selected theme.
     */
    private Theme selectedTheme;

    /**
     * The cookie theme.
     */
    private String cookieTheme;

    /**
     * The office selected.
     */
    private Administration selectedOffice;

    /**
     * The office selected.
     */
    private List<Administration> bureaux = new ArrayList<>();

    /**
     * The user service.
     */
    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    /**
     * The CCT CP param value service.
     */
    @ManagedProperty(value = "#{paramCCTCPService}")
    private ParamCCTCPService paramCCTCPService;

    @ManagedProperty(value = "#{fileTypeService}")
    private FileTypeService fileTypeService;

    @ManagedProperty(value = "#{serviceService}")
    private ServiceService serviceService;

    private ParamCCTCP paramCCTCP;

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, AccountSetupController.class.getName());
        }
        setThemePageUrl(ControllerConstants.Pages.AccountSetup.THEME_CONFIG_PAGE);
        setCctCpConfigPageUrl(ControllerConstants.Pages.AccountSetup.PARAM_CCT_CP_CONFIG_PAGE);

        if (getLoggedUser() != null) {
            selectedTheme = getLoggedUser().getTheme();

            List<FileType> filetypes = fileTypeService.findFileTypesByCodes(FileTypeCode.CCT_CT_E);
            if (filetypes != null) {
                if (filetypes.size() == 1) {
                    bureaux.addAll(serviceService.findServicesByAdministration(filetypes.get(0)));
                } else {
                    for (FileType ft : filetypes) {
                        if (ft.getCode().equals(FileTypeCode.CCT_CT_E)) {
                            bureaux.addAll(serviceService.findServicesByAdministration(ft));
                        }
                    }
                }
            }
        }

        cookieTheme = ServletUtils.getCookieValue("theme");
        if (getLoggedUser() != null && paramCCTCP == null) {
            if (paramCCTCP == null) {
                paramCCTCP = new ParamCCTCP();
            }
        }
    }

    /**
     * Go to theme page.
     */
    public void goToThemePage() {
        try {
            final FacesContext context = FacesContext.getCurrentInstance();
            final ExternalContext extContext = context.getExternalContext();
            final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
                    .getActionURL(context, themePageUrl));
            extContext.redirect(url);
        } catch (final IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
        }
    }

    /**
     * Go to Phytosanitary report config page.
     */
    public void goToCctCpCongigPage() {
        try {
            final FacesContext context = FacesContext.getCurrentInstance();
            final ExternalContext extContext = context.getExternalContext();
            final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
                    .getActionURL(context, cctCpConfigPageUrl));
            extContext.redirect(url);
        } catch (final IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
        }
    }

    /**
     * Go to my account page.
     */
    public void goToMyAccountPage() {
        try {
            final FacesContext context = FacesContext.getCurrentInstance();
            final ExternalContext extContext = context.getExternalContext();
            final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
                    .getActionURL(context, "/pages/fo/accountSetup/edit.xhtml"));
            extContext.redirect(url);
        } catch (final IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
        }
    }

    /**
     * Change theme.
     */
    public void changeTheme() {
        loggedUser.setTheme(selectedTheme);
        userService.update(loggedUser);

        ServletUtils.setCookies("theme", selectedTheme.getCode());

        final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                Theme.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
        JsfUtil.addSuccessMessage(msg);
    }

    public void updateParam() {
        if (selectedOffice != null) {
            if (paramCCTCP != null) {
                if (paramCCTCP.getId() == null) {
                    paramCCTCP.setAdministration(selectedOffice);
                    paramCCTCPService.save(paramCCTCP);
                } else {
                    paramCCTCPService.update(paramCCTCP);
                }

                final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        "ParamCctCpUpdated");
                JsfUtil.addSuccessMessage(msg);
            }
        }
    }

    public void changeOffice() {
        if (selectedOffice != null) {
            paramCCTCP = paramCCTCPService.findParamCCTCPByAdministration(selectedOffice);
            if (paramCCTCP == null) {
                paramCCTCP = new ParamCCTCP();
            }
        } else {
            paramCCTCP = null;
        }
    }

    /**
     * Gets the theme options.
     *
     * @return the theme options
     */
    public SelectItem[] getThemeOptions() {
        final SelectItem[] themeOptions = new SelectItem[getThemeList().size() + 1];
        themeOptions[0] = new SelectItem(StringUtils.EMPTY, ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                .getString(SELECT_ONE_MESSAGE));
        for (int i = 0; i < getThemeList().size(); i++) {
            themeOptions[i + 1] = new SelectItem(getThemeList().get(i), getThemeList().get(i).getLabel());
        }
        return themeOptions;
    }

    /**
     * Gets the theme list.
     *
     * @return the theme list
     */
    private List<Theme> getThemeList() {
        final List<Theme> themesList = new ArrayList<>();
        themesList.add(Theme.BLUE);
        themesList.add(Theme.BLUE_SKY);
        themesList.add(Theme.BROWN);
        themesList.add(Theme.GREEN);
        themesList.add(Theme.PURPLE);
        themesList.add(Theme.YELLOW);

        return themesList;
    }

    /**
     * Gets the logged user.
     *
     * @return the logged user
     */
    public User getLoggedUser() {
        if (loggedUser == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                loggedUser = (User) session.getAttribute("loggedUser");
            }
        }
        return loggedUser;
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
     * Gets the theme page url.
     *
     * @return the themePageUrl
     */
    public String getThemePageUrl() {
        return themePageUrl;
    }

    /**
     * Sets the theme page url.
     *
     * @param themePageUrl the themePageUrl to set
     */
    public void setThemePageUrl(final String themePageUrl) {
        this.themePageUrl = themePageUrl;
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
     * Gets the selected theme.
     *
     * @return the selectedTheme
     */
    public Theme getSelectedTheme() {
        return selectedTheme;
    }

    /**
     * Sets the selected theme.
     *
     * @param selectedTheme the selectedTheme to set
     */
    public void setSelectedTheme(final Theme selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    /**
     * Gets the cookie theme.
     *
     * @return the cookieTheme
     */
    public String getCookieTheme() {
        return cookieTheme;
    }

    /**
     * Sets the cookie theme.
     *
     * @param cookieTheme the cookieTheme to set
     */
    public void setCookieTheme(final String cookieTheme) {
        this.cookieTheme = cookieTheme;
    }

    public Administration getSelectedOffice() {
        return selectedOffice;
    }

    public void setSelectedOffice(Administration selectedOffice) {
        this.selectedOffice = selectedOffice;
    }

    public String getCctCpConfigPageUrl() {
        return cctCpConfigPageUrl;
    }

    public void setCctCpConfigPageUrl(String cctCpConfigPageUrl) {
        this.cctCpConfigPageUrl = cctCpConfigPageUrl;
    }

    public ParamCCTCPService getParamCCTCPService() {
        return paramCCTCPService;
    }

    public void setParamCCTCPService(ParamCCTCPService paramCCTCPService) {
        this.paramCCTCPService = paramCCTCPService;
    }

    public FileTypeService getFileTypeService() {
        return fileTypeService;
    }

    public void setFileTypeService(FileTypeService fileTypeService) {
        this.fileTypeService = fileTypeService;
    }

    public ServiceService getServiceService() {
        return serviceService;
    }

    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    public List<Administration> getBureaux() {
        return bureaux;
    }

    public void setBureaux(List<Administration> bureaux) {
        this.bureaux = bureaux;
    }

    public ParamCCTCP getParamCCTCP() {
        return paramCCTCP;
    }

    public void setParamCCTCP(ParamCCTCP paramCCTCP) {
        this.paramCCTCP = paramCCTCP;
    }

    
}
