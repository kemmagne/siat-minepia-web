package org.guce.siat.web.common;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UISelectOne;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
import org.guce.siat.common.mail.MailConstants;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Entity;
import org.guce.siat.common.model.Ministry;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.AbstractService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractController.
 *
 * @param <T> the generic type
 */
public abstract class AbstractController<T extends Serializable> implements Serializable {

    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 6402483100621035581L;

    /**
     * The Constant PERSISTENCE_ERROR_OCCURED.
     */
    protected static final String PERSISTENCE_ERROR_OCCURED = "PersistenceErrorOccured";

    /**
     * The Constant SELECT_ONE_MESSAGE.
     */
    protected static final String SELECT_ONE_MESSAGE = "SelectOneMessage";

    /**
     * The Constant LOCAL_BUNDLE_NAME.
     */
    protected static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";

    /**
     * The Constant DELETED_ATTRIBUTE_NAME.
     */
    private static final String DELETED_ATTRIBUTE_NAME = "deleted";

    /**
     * The Constant CREATE_SIAT_ACCOUNT_MAIL_SUBJECT.
     */
    protected static final String CREATE_SIAT_ACCOUNT_MAIL_SUBJECT = "createAccountMailSubject";

    /**
     * The Constant WELCOME_MAIL_BODY.
     */
    protected static final String WELCOME_MAIL_BODY = "welcomeMailBody";
    /**
     * The service.
     */
    private AbstractService<T> service;

    /**
     * The item class.
     */
    private Class<T> itemClass;

    /**
     * The selected.
     */
    protected T selected;

    /**
     * The items.
     */
    protected List<T> items;

    /**
     * The page url.
     */
    protected String pageUrl;

    /**
     * The logged user.
     */
    protected User loggedUser;

    /**
     * The current administration.
     */
    protected Administration currentAdministration;

    /**
     * The current ministry.
     */
    protected Ministry currentMinistry;

    /**
     * The current organism.
     */
    protected Organism currentOrganism;

    /**
     * The current sub department.
     */
    protected SubDepartment currentSubDepartment;

    /**
     * The current service.
     */
    protected Service currentService;

    /**
     * The current entity.
     */
    protected Entity currentEntity;

    /**
     * Instantiates a new abstract controller.
     */
    public AbstractController() {
    }

    /**
     * Instantiates a new abstract controller.
     *
     * @param itemClass the item class
     */
    public AbstractController(final Class<T> itemClass) {
        this.itemClass = itemClass;
    }

    /**
     * Gets the items.
     *
     * @return the items
     */
    public List<T> getItems() {
        try {
            if (items == null) {
                if (hasDeletedAttribute()) {
                    items = this.service.findActiveItems();
                } else {
                    items = this.service.findAll();
                }
            }
        } catch (final Exception ex) {
            Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(), ex.getClass().getName() + ex.getMessage());
            JsfUtil.addErrorMessage(ex,
                    ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
        }
        return items;
    }

    /**
     * Go to page.
     */
    public void goToPage() {
        try {
            refreshItems();
            final FacesContext context = FacesContext.getCurrentInstance();
            final ExternalContext extContext = context.getExternalContext();
            final String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, pageUrl));
            extContext.redirect(url);
        } catch (final IOException ioe) {
            Logger.getLogger(this.getClass().getName())
                    .logp(Level.SEVERE, this.getClass().getName(),
                            Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(),
                            ioe.getClass().getName() + ioe.getMessage());
        }
    }

    /**
     * Creates the.
     */
    public void create() {
        final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                itemClass.getSimpleName() + PersistenceActions.CREATE.getCode());
        persist(PersistenceActions.CREATE, msg);
        if (!isValidationFailed()) {
            refreshItems();
            selected = null;
        }
    }

    /**
     * Edits the.
     */
    public void edit() {
        final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(itemClass.getSimpleName() + PersistenceActions.UPDATE.getCode());
        persist(PersistenceActions.UPDATE, msg);
        refreshItems();
        selected = null;
    }

    /**
     * Delete.
     */
    public void delete() {
        final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(itemClass.getSimpleName() + PersistenceActions.DELETE.getCode());
        persist(PersistenceActions.DELETE, msg);
        if (!isValidationFailed()) {
            selected = null;
            items = null;
        }
    }

    /**
     * Persist.
     *
     * @param persistAction the persist action
     * @param successMessage the success message
     */
    protected void persist(final PersistenceActions persistAction, final String successMessage) {
        if (selected != null) {
            try {
                if (null != persistAction) {
                    switch (persistAction) {
                        case UPDATE:
                            this.service.update(selected);
                            setSelected(null);
                            break;
                        case CREATE:
                            this.service.save(selected);
                            break;
                        default:
                            this.service.delete(selected);
                            break;
                    }
                }

                JsfUtil.addSuccessMessage(successMessage);
            } catch (final Exception ex) {
                Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(),
                        Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(),
                        ex.getClass().getName() + ex.getMessage());
                JsfUtil.addErrorMessage(ex,
                        ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
            } finally {
                reset();
            }
        }
    }

    /**
     * Prepare create.
     */
    public void prepareCreate() {
        T newItem;
        try {
            newItem = itemClass.newInstance();
            this.selected = newItem;
        } catch (final InstantiationException | IllegalAccessException ie) {
            Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(), ie.getClass().getName() + ie.getMessage());
        }
    }

    /**
     * Reset.
     */
    public void reset() {
        setSelected(null);
        refreshItems();
    }

    /**
     * Checks if is validation failed.
     *
     * @return true, if is validation failed
     */
    public boolean isValidationFailed() {
        return JsfUtil.isValidationFailed();
    }

    /**
     * Refresh items.
     */
    public void refreshItems() {
        items = null;
//        getItems();

    }

    /**
     * Checks for deleted attribute.
     *
     * @return true, if successful
     */
    protected boolean hasDeletedAttribute() {
        try {
            itemClass.getDeclaredField(DELETED_ATTRIBUTE_NAME);
        } catch (SecurityException se) {
            Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(), se.getClass().getName() + se.getMessage());
            return false;
        } catch (NoSuchFieldException nsfe) {
            Logger.getLogger(this.getClass().getName()).logp(Level.WARNING, this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(),
                    nsfe.getClass().getName() + nsfe.getMessage());
            return false;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(), ex.getClass().getName() + ex.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Locale file template.
     *
     * @param fileName the file name
     * @param language the language
     * @return the string
     */
    protected String localeFileTemplate(final String fileName, final String language) {
        final StringBuilder templateFileName = new StringBuilder("/vms/");
        templateFileName.append(fileName).append("_").append(language).append(".vm");
        return templateFileName.toString();
    }

    /**
     * Prepare map.
     *
     * @param subject the subject
     * @param from the from
     * @param person the person
     * @param newPassword the new password
     * @param template the template
     * @return the map
     */
    protected Map<String, String> prepareMap(final String subject, final String from, final User person, final String newPassword, final String template) {
        final Map<String, String> map = new HashMap<>();

        map.put(MailConstants.SUBJECT, subject);
        map.put(MailConstants.FROM, from);
        map.put("firstName", person.getFirstName());
        map.put("login", person.getLogin());
        map.put(MailConstants.EMAIL, person.getEmail());
        map.put("motDePasse", newPassword);
        map.put(MailConstants.VMF, template);
        return map;
    }

    /**
     * Gets the selected.
     *
     * @return the selected
     */
    public T getSelected() {
        return selected;
    }

    /**
     * Sets the selected.
     *
     * @param selected the new selected
     */
    public void setSelected(final T selected) {
        this.selected = selected;
    }

    /**
     * Sets the items.
     *
     * @param items the new items
     */
    public void setItems(final List<T> items) {
        this.items = items;
    }

    /**
     * Gets the service.
     *
     * @return the service
     */
    public AbstractService<T> getService() {
        return service;
    }

    /**
     * Sets the service.
     *
     * @param service the new service
     */
    public void setService(final AbstractService<T> service) {
        this.service = service;
    }

    /**
     * Gets the page url.
     *
     * @return the page url
     */
    public String getPageUrl() {
        return pageUrl;
    }

    /**
     * Sets the page url.
     *
     * @param pageUrl the new page url
     */
    public void setPageUrl(final String pageUrl) {
        this.pageUrl = pageUrl;
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
     * Gets the current locale code.
     *
     * @return the current locale code
     */
    public String getCurrentLocaleCode() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();
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
     * Gets the current administration.
     *
     * @return the current administration
     */
    public Administration getCurrentAdministration() {
        if (currentAdministration == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                currentAdministration = (Administration) session.getAttribute("currentAdministration");
            }
        }
        return currentAdministration;
    }

    /**
     * Gets the current ministry.
     *
     * @return the currentMinistry
     */
    public Ministry getCurrentMinistry() {

        if (currentMinistry == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                currentMinistry = (Ministry) session.getAttribute("currentMinistry");
            }
        }
        return currentMinistry;
    }

    /**
     * Gets the current organism.
     *
     * @return the current organism
     */
    public Organism getCurrentOrganism() {
        if (currentOrganism == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                currentOrganism = (Organism) session.getAttribute("currentOrganism");
            }
        }
        return currentOrganism;
    }

    /**
     * Gets the current sub department.
     *
     * @return the current sub department
     */
    public SubDepartment getCurrentSubDepartment() {
        if (currentSubDepartment == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                currentSubDepartment = (SubDepartment) session.getAttribute("currentSubDepartment");
            }
        }
        return currentSubDepartment;
    }

    /**
     * Gets the current service.
     *
     * @return the current service
     */
    public Service getCurrentService() {
        if (currentService == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                currentService = (Service) session.getAttribute("currentService");
            }
        }
        return currentService;
    }

    /**
     * Gets the current entity.
     *
     * @return the current entity
     */
    public Entity getCurrentEntity() {
        if (currentEntity == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                currentEntity = (Entity) session.getAttribute("currentEntity");
            }
        }
        return currentEntity;
    }

    /**
     * Checks if is selection is null.
     *
     * @param event the event
     * @return true, if is selection is null
     */
    public boolean isSelectionIsNull(final AjaxBehaviorEvent event) {
        if (event != null) {
            final UISelectOne select = (UISelectOne) event.getSource();
            final String selectOneMessage = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    SELECT_ONE_MESSAGE);
            if (select.getSubmittedValue() == null || select.getSubmittedValue().toString().equals(selectOneMessage)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the logged user.
     *
     * @param loggedUser the new logged user
     */
    public void setLoggedUser(final User loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * Sets the current administration.
     *
     * @param currentAdministration the currentAdministration to set
     */
    public void setCurrentAdministration(final Administration currentAdministration) {
        this.currentAdministration = currentAdministration;
    }

    /**
     * Sets the current ministry.
     *
     * @param currentMinistry the currentMinistry to set
     */
    public void setCurrentMinistry(final Ministry currentMinistry) {
        this.currentMinistry = currentMinistry;
    }

    /**
     * Sets the current organism.
     *
     * @param currentOrganism the new current organism
     */
    public void setCurrentOrganism(final Organism currentOrganism) {
        this.currentOrganism = currentOrganism;
    }

    /**
     * Sets the current sub department.
     *
     * @param currentSubDepartment the currentSubDepartment to set
     */
    public void setCurrentSubDepartment(final SubDepartment currentSubDepartment) {
        this.currentSubDepartment = currentSubDepartment;
    }

    /**
     * Sets the current service.
     *
     * @param currentService the new current service
     */
    public void setCurrentService(final Service currentService) {
        this.currentService = currentService;
    }

    /**
     * Sets the current entity.
     *
     * @param currentEntity the currentEntity to set
     */
    public void setCurrentEntity(final Entity currentEntity) {
        this.currentEntity = currentEntity;
    }
}
