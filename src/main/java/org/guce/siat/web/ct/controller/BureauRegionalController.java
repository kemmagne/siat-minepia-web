package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.service.BureauService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.SubDepartmentService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.BureauType;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class BureauRegionalController.
 */
@ManagedBean(name = "bureauRegionalController")
@SessionScoped
public class BureauRegionalController extends AbstractController<Bureau> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 9188379234500179149L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(BureauRegionalController.class);

    /**
     * The bureau service.
     */
    @ManagedProperty(value = "#{bureauService}")
    private BureauService bureauService;

    /**
     * The service service.
     */
    @ManagedProperty(value = "#{serviceService}")
    private ServiceService serviceService;

    /**
     * The sub department service.
     */
    @ManagedProperty(value = "#{subDepartmentService}")
    private SubDepartmentService subDepartmentService;

    /**
     * The services.
     */
    private List<Service> services;

    /**
     * The subdepartments.
     */
    private List<SubDepartment> subDepartments;

    /**
     * The sub department.
     */
    private SubDepartment subDepartment;

    /**
     * Instantiates a new bureau regional controller.
     */
    public BureauRegionalController() {
        super(Bureau.class);
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, BureauRegionalController.class.getName());
        }
        super.setService(bureauService);
        super.setPageUrl(ControllerConstants.Pages.BO.BUREAU_REGIONAL_INDEX_PAGE);
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#create()
     */
    @Override
    public void create() {
        getSelected().setDeleted(false);
        getSelected().setBureauType(BureauType.BUREAU_REGIONAL);
        getSelected().setCityEn(getSelected().getCityFr());
        super.create();
    }

    @Override
    public void edit() {
        getSelected().setCityEn(getSelected().getCityFr());
        super.edit();
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#getItems()
     */
    @Override
    public List<Bureau> getItems() {
        try {
            if (items == null) {
                items = bureauService.findBureauByTypeAndOrganism(BureauType.BUREAU_REGIONAL, getCurrentOrganism());
            }
        } catch (final Exception ex) {
            LOG.error(null, ex);
            JsfUtil.addErrorMessage(ex,
                    ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
        }
        return items;
    }

    /**
     * Gets the subdepartments.
     *
     * @return the subdepartments
     */
    public List<SubDepartment> getSubDepartments() {
        try {
            if (subDepartments == null) {
                subDepartments = subDepartmentService.findSubDepartmentsByOrganism(getCurrentOrganism());
            }
        } catch (final Exception ex) {
            LOG.error(null, ex);
            JsfUtil.addErrorMessage(ex,
                    ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
        }
        return subDepartments;
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
     */
    @Override
    public void prepareCreate() {
        super.prepareCreate();
        getSelected().setBureauType(BureauType.BUREAU_REGIONAL);
        subDepartments = subDepartmentService.findSubDepartmentsByOrganism(getCurrentOrganism());
        services = new ArrayList<>();
        subDepartment = null;

    }

    /**
     * Prepare edit.
     */
    public void prepareEdit() {

        this.setSelected(bureauService.find(this.getSelected().getId()));
        subDepartment = getSelected().getService().getSubDepartment();

        subDepartments = subDepartmentService.findSubDepartmentsByOrganism(getCurrentOrganism());
        services = subDepartment.getServicesList();

    }

    /**
     * Gets the bureau service.
     *
     * @return the bureauService
     */
    public BureauService getBureauService() {
        return bureauService;
    }

    /**
     * Sets the bureau service.
     *
     * @param bureauService the bureauService to set
     */
    public void setBureauService(final BureauService bureauService) {
        this.bureauService = bureauService;
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
     * Sets the sub departments.
     *
     * @param subDepartments the new sub departments
     */
    public void setSubDepartments(final List<SubDepartment> subDepartments) {
        this.subDepartments = subDepartments;
    }

    /**
     * On sub department change.
     */
    public void onSubDepartmentChange() {
        if (subDepartments != null) {
            services = subDepartment.getServicesList();
        } else {
            services = new ArrayList<>();
        }
    }

    /**
     * Gets the sub department.
     *
     * @return the sub department
     */
    public SubDepartment getSubDepartment() {
        return subDepartment;
    }

    /**
     * Sets the sub department.
     *
     * @param subDepartment the new sub department
     */
    public void setSubDepartment(final SubDepartment subDepartment) {
        this.subDepartment = subDepartment;
    }

    /**
     * Gets the services.
     *
     * @return the services
     */
    public List<Service> getServices() {
        return services;
    }

    /**
     * Sets the services.
     *
     * @param services the new services
     */
    public void setServices(final List<Service> services) {
        this.services = services;
    }

    @Override
    public Bureau getSelected() {
        return super.getSelected();
    }

}
