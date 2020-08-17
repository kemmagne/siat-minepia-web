package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.Step;
import org.guce.siat.common.service.AuthorityService;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.StepService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class FileTypeController.
 */
@ManagedBean(name = "fileTypeController")
@SessionScoped
public class FileTypeController extends AbstractController<FileType> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -4293460191810169052L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(BureauRegionalController.class);

    /**
     * The entity service.
     */
    @ManagedProperty(value = "#{fileTypeService}")
    private FileTypeService fileTypeService;

    /**
     * The role service.
     */
    @ManagedProperty(value = "#{authorityService}")
    private AuthorityService authorityService;

    /**
     * The step service.
     */
    @ManagedProperty(value = "#{stepService}")
    private StepService stepService;

    /**
     * The service service.
     */
    @ManagedProperty(value = "#{serviceService}")
    private ServiceService serviceService;

    /**
     * The roles list.
     */
    private List<Authority> roleList;

    /**
     * The roles pick list.
     */
    private DualListModel<Authority> rolePickList;

    /**
     * The step list.
     */
    private List<Step> stepList;

    /**
     * The step pick list.
     */
    private DualListModel<Step> stepPickList;

    /**
     * The service list.
     */
    private List<Service> serviceList;

    /**
     * The service pick list.
     */
    private DualListModel<Service> servicePickList;

    /**
     * Instantiates a new file type controller.
     */
    public FileTypeController() {
        super(FileType.class);
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, BureauRegionalController.class.getName());
        }
        super.setService(fileTypeService);
        super.setPageUrl(ControllerConstants.Pages.BO.FILE_TYPE_INDEX_PAGE);

        roleList = authorityService.findAll();
        rolePickList = new DualListModel<>(new ArrayList<Authority>(), new ArrayList<Authority>());
        rolePickList.getSource().addAll(roleList);

        stepList = stepService.findAll();
        stepPickList = new DualListModel<>(new ArrayList<Step>(), new ArrayList<Step>());
        stepPickList.getSource().addAll(stepList);

        serviceList = serviceService.findAll();
        servicePickList = new DualListModel<>(new ArrayList<Service>(), new ArrayList<Service>());
        servicePickList.getSource().addAll(serviceList);
    }

    /**
     * Prepare edit.
     */
    public void prepareEdit() {
        this.setSelected(fileTypeService.find(getSelected().getId()));

        rolePickList = new DualListModel<>(new ArrayList<Authority>(), new ArrayList<Authority>());
        rolePickList.getSource().addAll(roleList);

        for (final Authority authority : getSelected().getRoleList()) {
            for (final Authority authority1 : rolePickList.getSource()) {
                if (authority.getId().equals(authority1.getId())) {
                    rolePickList.getTarget().add(authority);
                    rolePickList.getSource().remove(authority);
                    break;
                }
            }
        }

        stepPickList = new DualListModel<>(new ArrayList<Step>(), new ArrayList<Step>());
        stepPickList.getSource().addAll(stepList);

        for (final Step step : getSelected().getStepList()) {
            for (final Step step1 : stepPickList.getSource()) {
                if (step.getId().equals(step1.getId())) {
                    stepPickList.getTarget().add(step);
                    stepPickList.getSource().remove(step);
                    break;
                }
            }
        }

        servicePickList = new DualListModel<>(new ArrayList<Service>(), new ArrayList<Service>());
        servicePickList.getSource().addAll(serviceList);

        for (final Service service : getSelected().getServiceList()) {
            for (final Service service1 : servicePickList.getSource()) {
                if (service.getId().equals(service1.getId())) {
                    servicePickList.getTarget().add(service);
                    servicePickList.getSource().remove(service);
                    break;
                }
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
        //selected.getRoleList().clear();
        //selected.getRoleList().addAll(rolePickList.getTarget());

        /*final List<FileTypeStep> targetFileTypeStep = new ArrayList<FileTypeStep>();

		for (final Step step : stepPickList.getTarget()) {
			final FileTypeStep fileTypeStep = new FileTypeStep();
			fileTypeStep.setFileType(selected);
			fileTypeStep.setStep(step);
			targetFileTypeStep.add(fileTypeStep);
		}*/
        final List<org.guce.siat.common.model.FileTypeService> targetFileTypeService = new ArrayList<>();

        for (final Service service : servicePickList.getTarget()) {
            final org.guce.siat.common.model.FileTypeService fileTypeService_ = new org.guce.siat.common.model.FileTypeService();
            fileTypeService_.setFileType(getSelected());
            fileTypeService_.setService(service);
            targetFileTypeService.add(fileTypeService_);
        }
        fileTypeService.update(getSelected(), null, targetFileTypeService);
    }

    public String serviceHierarchieName(Service service) {
        StringBuilder sb = new StringBuilder();
        sb.append(service.getSubDepartment().getOrganism().getMinistry().getLabelFr())
                .append(" - ")
                .append(service.getSubDepartment().getOrganism().getAbreviation())
                .append(" - ")
                .append(service.getSubDepartment().getAbreviation())
                .append(" - ")
                .append(service.getLabelFr());
        return sb.toString();
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
     * Gets the step service.
     *
     * @return the step service
     */
    public StepService getStepService() {
        return stepService;
    }

    /**
     * Sets the step service.
     *
     * @param stepService the new step service
     */
    public void setStepService(final StepService stepService) {
        this.stepService = stepService;
    }

    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    public ServiceService getServiceService() {
        return serviceService;
    }

    /**
     * Gets the role list.
     *
     * @return the role list
     */
    public List<Authority> getRoleList() {
        return roleList;
    }

    /**
     * Sets the role list.
     *
     * @param roleList the new role list
     */
    public void setRoleList(final List<Authority> roleList) {
        this.roleList = roleList;
    }

    /**
     * Gets the role pick list.
     *
     * @return the role pick list
     */
    public DualListModel<Authority> getRolePickList() {
        return rolePickList;
    }

    /**
     * Sets the role pick list.
     *
     * @param rolePickList the new role pick list
     */
    public void setRolePickList(final DualListModel<Authority> rolePickList) {
        this.rolePickList = rolePickList;
    }

    /**
     * Gets the step list.
     *
     * @return the step list
     */
    public List<Step> getStepList() {
        return stepList;
    }

    /**
     * Sets the step list.
     *
     * @param stepList the new step list
     */
    public void setStepList(final List<Step> stepList) {
        this.stepList = stepList;
    }

    /**
     * Gets the step pick list.
     *
     * @return the step pick list
     */
    public DualListModel<Step> getStepPickList() {
        return stepPickList;
    }

    /**
     * Sets the step pick list.
     *
     * @param stepPickList the new step pick list
     */
    public void setStepPickList(final DualListModel<Step> stepPickList) {
        this.stepPickList = stepPickList;
    }

    public DualListModel<Service> getServicePickList() {
        return servicePickList;
    }

    public void setServicePickList(DualListModel<Service> servicePickList) {
        this.servicePickList = servicePickList;
    }

    @Override
    public FileType getSelected() {
        return super.getSelected();
    }

}
