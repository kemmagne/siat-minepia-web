package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.ControllerHoliday;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.ControllerHolidayService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ControllerHolidayController.
 */
@ManagedBean(name = "controllerHolidayController")
@SessionScoped
public class ControllerHolidayController extends AbstractController<ControllerHoliday> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -759811278178834882L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ControllerHolidayController.class);

    /**
     * The Constant DATE_VALIDATION_ERROR_MESSAGE.
     */
    private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

    /**
     * The Constant CONTROLLER_ALREADY_ON_HOLIDAY.
     */
    private static final String CONTROLLER_ALREADY_ON_HOLIDAY = "ContollerAleadyOnHoliday";

    /**
     * The user service.
     */
    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    /**
     * The controller holiday service.
     */
    @ManagedProperty(value = "#{controllerHolidayService}")
    private ControllerHolidayService controllerHolidayService;

    /**
     * The list service.
     */
    private List<Service> listService;

    /**
     * The service getSelected().
     */
    private Service serviceSelected;

    /**
     * The list user par service.
     */
    private List<User> listUserParService;

    /**
     * The sub departments.
     */
    private List<SubDepartment> subDepartments;

    /**
     * The selected sub department.
     */
    private SubDepartment selectedSubDepartment;

    /**
     * The bol select inspector.
     */
    private Boolean bolSelectInspector = true;

    /**
     * Instantiates a new inspector holiday controller.
     */
    public ControllerHolidayController() {
        super(ControllerHoliday.class);
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, ControllerHolidayController.class.getName());
        }
        super.setService(controllerHolidayService);
        super.setPageUrl(ControllerConstants.Pages.BO.INSPECTOR_HOLIDAY_INDEX_PAGE);

    }

    /**
     * Service selection changed.
     */
    public void serviceSelectionChangedHandler() {
        if (serviceSelected != null) {
            listUserParService = userService.findControleursByService(serviceSelected);
        } else {
            listUserParService = null;
        }
    }

    /**
     * Sub department changed handler.
     */
    public void subDepartmentChangedHandler() {
        if (selectedSubDepartment != null) {
            listService = selectedSubDepartment.getServicesList();

        } else {
            listService = null;
        }
        listUserParService = null;
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#create()
     */
    @Override
    public void create() {
        if (getSelected().getFromDate() != null && getSelected().getToDate() != null && getSelected().getFromDate().after(getSelected().getToDate())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    DATE_VALIDATION_ERROR_MESSAGE));
            return;
        }

        if (getSelected().getEnabled()) {
            final List<ControllerHoliday> existsHolidays = controllerHolidayService.findHolidayByControllerAndDates(
                    getSelected().getUser(), getSelected().getFromDate(), getSelected().getToDate());

            if (CollectionUtils.isNotEmpty(existsHolidays)) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        CONTROLLER_ALREADY_ON_HOLIDAY));
                return;
            }
        }
        getSelected().setDeleted(false);
        super.create();
        resetFields();
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#edit()
     */
    @Override
    public void edit() {
        if (getSelected().getFromDate() != null && getSelected().getToDate() != null && getSelected().getFromDate().after(getSelected().getToDate())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    DATE_VALIDATION_ERROR_MESSAGE));
            return;
        }

        if (getSelected().getEnabled()) {
            final List<ControllerHoliday> existsHolidays = controllerHolidayService.findHolidayByControllerAndDates(
                    getSelected().getUser(), getSelected().getFromDate(), getSelected().getToDate());

            if (existsHolidays.remove(getSelected()) && CollectionUtils.isNotEmpty(existsHolidays)) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        CONTROLLER_ALREADY_ON_HOLIDAY));
                return;
            }
        }
        super.edit();
        resetFields();
    }

    /**
     * Reset fields.
     */
    public void resetFields() {
        selectedSubDepartment = null;
        serviceSelected = null;
        listService = new ArrayList<>();
        listUserParService = new ArrayList<>();
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
     */
    @Override
    public void prepareCreate() {
        super.prepareCreate();
        subDepartments = getCurrentOrganism().getSubDepartmentsList();
        selectedSubDepartment = null;
        serviceSelected = null;
        listService = new ArrayList<>();
        listUserParService = new ArrayList<>();
    }

    /**
     * Prepare edit.
     */
    public void prepareEdit() {
        this.setSelected(controllerHolidayService.find(this.getSelected().getId()));
        if (getSelected().getUser().getAdministration() instanceof Bureau) {
            serviceSelected = ((Bureau) getSelected().getUser().getAdministration()).getService();
            selectedSubDepartment = serviceSelected.getSubDepartment();
        }
    }

    /* (non-Javadoc)
	 * @see org.guce.siat.web.common.AbstractController#getItems()
     */
    @Override
    public List<ControllerHoliday> getItems() {
        return controllerHolidayService.findByOrganism(getCurrentOrganism());
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
     * Gets the list user par service.
     *
     * @return the listUserParService
     */
    public List<User> getListUserParService() {
        return listUserParService;
    }

    /**
     * Sets the list user par service.
     *
     * @param listUserParService the listUserParService to set
     */
    public void setListUserParService(final List<User> listUserParService) {
        this.listUserParService = listUserParService;
    }

    /**
     * Gets the bol select inspector.
     *
     * @return the bolSelectInspector
     */
    public Boolean getBolSelectInspector() {
        return bolSelectInspector;
    }

    /**
     * Sets the bol select inspector.
     *
     * @param bolSelectInspector the bolSelectInspector to set
     */
    public void setBolSelectInspector(final Boolean bolSelectInspector) {
        this.bolSelectInspector = bolSelectInspector;
    }

    /**
     * Gets the sub departments.
     *
     * @return the subDepartments
     */
    public List<SubDepartment> getSubDepartments() {
        return subDepartments;
    }

    /**
     * Sets the sub departments.
     *
     * @param subDepartments the subDepartments to set
     */
    public void setSubDepartments(final List<SubDepartment> subDepartments) {
        this.subDepartments = subDepartments;
    }

    /**
     * Gets the selected sub department.
     *
     * @return the selectedSubDepartment
     */
    public SubDepartment getSelectedSubDepartment() {
        return selectedSubDepartment;
    }

    /**
     * Sets the selected sub department.
     *
     * @param selectedSubDepartment the selectedSubDepartment to set
     */
    public void setSelectedSubDepartment(final SubDepartment selectedSubDepartment) {
        this.selectedSubDepartment = selectedSubDepartment;
    }

}
