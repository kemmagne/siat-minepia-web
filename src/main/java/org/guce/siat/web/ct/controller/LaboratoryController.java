package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.guce.siat.common.model.AnalyseType;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.service.AnalyseTypeService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.SubDepartmentService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.core.ct.model.Laboratory;
import org.guce.siat.core.ct.service.LaboratoryService;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class LaboratoryController.
 */
@ManagedBean(name = "laboratoryController")
@SessionScoped
public class LaboratoryController extends AbstractController<Laboratory>
{

	/**
	 *
	 */
	private static final long serialVersionUID = 4717215461988509904L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(LaboratoryController.class);

	/** The laboratory service. */
	@ManagedProperty(value = "#{laboratoryService}")
	private LaboratoryService laboratoryService;

	/** The analyse type service. */
	@ManagedProperty(value = "#{analyseTypeService}")
	private AnalyseTypeService analyseTypeService;

	/** The analyse types list. */
	private List<AnalyseType> analyseTypesList;

	/** The analyse types pick list. */
	private DualListModel<AnalyseType> analyseTypesPickList;

	/** The service service. */
	@ManagedProperty(value = "#{serviceService}")
	private ServiceService serviceService;


	/** The sub department service. */
	@ManagedProperty(value = "#{subDepartmentService}")
	private SubDepartmentService subDepartmentService;

	/** The services. */
	private List<Service> services;

	/** The subdepartments. */
	private List<SubDepartment> subDepartments;

	/** The sub department. */
	private SubDepartment subDepartment;

	/**
	 * Instantiates a new laboratory controller.
	 */
	public LaboratoryController()
	{
		super(Laboratory.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, LaboratoryController.class.getName());
		}
		super.setService(laboratoryService);
		super.setPageUrl(ControllerConstants.Pages.BO.LABORATORY_INDEX_PAGE);

		analyseTypesList = analyseTypeService.findByAdministration(getCurrentOrganism());
		analyseTypesPickList = new DualListModel<AnalyseType>(new ArrayList<AnalyseType>(), new ArrayList<AnalyseType>());
		analyseTypesPickList.getSource().addAll(analyseTypesList);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#create()
	 */
	@Override
	public void create()
	{
		selected.setDeleted(false);

		final List<AnalyseType> analyseTypeList = new ArrayList<AnalyseType>();
		analyseTypeList.addAll(analyseTypesPickList.getTarget());

		selected.setAnalyseTypeList(analyseTypeList);

		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
				.getString(Laboratory.class.getSimpleName() + PersistenceActions.CREATE.getCode());
		persist(PersistenceActions.CREATE, msg);
		if (!isValidationFailed())
		{
			refreshItems();
			selected = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#edit()
	 */
	@Override
	public void edit()
	{
		getSelected().getAnalyseTypeList().clear();
		getSelected().getAnalyseTypeList().addAll(analyseTypesPickList.getTarget());
		super.edit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
	 */
	@Override
	public void prepareCreate()
	{
		super.prepareCreate();
		selected.setService(getCurrentService());

		subDepartments = subDepartmentService.findSubDepartmentsByOrganism(getCurrentOrganism());
		subDepartment = null;

		analyseTypesPickList = new DualListModel<AnalyseType>(new ArrayList<AnalyseType>(), new ArrayList<AnalyseType>());
		analyseTypesPickList.getSource().addAll(analyseTypesList);
		services = new ArrayList<Service>();
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{

		subDepartment = selected.getService().getSubDepartment();

		subDepartments = subDepartmentService.findSubDepartmentsByOrganism(getCurrentOrganism());
		services = subDepartment.getServicesList();
		this.setSelected(laboratoryService.find(this.getSelected().getId()));

		analyseTypesPickList = new DualListModel<AnalyseType>(new ArrayList<AnalyseType>(), new ArrayList<AnalyseType>());
		analyseTypesPickList.getSource().addAll(analyseTypesList);

		for (final AnalyseType analyseType : this.getSelected().getAnalyseTypeList())
		{
			for (final AnalyseType analyseType1 : analyseTypesPickList.getSource())
			{
				if (analyseType.getId().equals(analyseType1.getId()))
				{
					analyseTypesPickList.getTarget().add(analyseType);
					analyseTypesPickList.getSource().remove(analyseType);
					break;
				}
			}
		}
	}

	/**
	 * On sub department change.
	 */
	public void onSubDepartmentChange()
	{
		if (subDepartments != null)
		{
			services = subDepartment.getServicesList();
		}
		else
		{
			services = new ArrayList<Service>();
		}
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<Laboratory> getItems()
	{
		try
		{
			if (items == null)
			{
				items = laboratoryService.findByOrganism(getCurrentOrganism());
			}
		}
		catch (final Exception ex)
		{
			LOG.error(null, ex);
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}
		return items;
	}

	/**
	 * Gets the laboratory service.
	 *
	 * @return the laboratory service
	 */
	public LaboratoryService getLaboratoryService()
	{
		return laboratoryService;
	}

	/**
	 * Sets the laboratory service.
	 *
	 * @param laboratoryService
	 *           the new laboratory service
	 */
	public void setLaboratoryService(final LaboratoryService laboratoryService)
	{
		this.laboratoryService = laboratoryService;
	}

	/**
	 * Gets the analyse type service.
	 *
	 * @return the analyse type service
	 */
	public AnalyseTypeService getAnalyseTypeService()
	{
		return analyseTypeService;
	}

	/**
	 * Sets the analyse type service.
	 *
	 * @param analyseTypeService
	 *           the new analyse type service
	 */
	public void setAnalyseTypeService(final AnalyseTypeService analyseTypeService)
	{
		this.analyseTypeService = analyseTypeService;
	}

	/**
	 * Gets the analyse types list.
	 *
	 * @return the analyse types list
	 */
	public List<AnalyseType> getAnalyseTypesList()
	{
		return analyseTypesList;
	}

	/**
	 * Sets the analyse types list.
	 *
	 * @param analyseTypesList
	 *           the new analyse types list
	 */
	public void setAnalyseTypesList(final List<AnalyseType> analyseTypesList)
	{
		this.analyseTypesList = analyseTypesList;
	}

	/**
	 * Gets the analyse types pick list.
	 *
	 * @return the analyse types pick list
	 */
	public DualListModel<AnalyseType> getAnalyseTypesPickList()
	{
		return analyseTypesPickList;
	}

	/**
	 * Sets the analyse types pick list.
	 *
	 * @param analyseTypesPickList
	 *           the new analyse types pick list
	 */
	public void setAnalyseTypesPickList(final DualListModel<AnalyseType> analyseTypesPickList)
	{
		this.analyseTypesPickList = analyseTypesPickList;
	}

	/**
	 * Gets the service service.
	 *
	 * @return the service service
	 */
	public ServiceService getServiceService()
	{
		return serviceService;
	}

	/**
	 * Sets the service service.
	 *
	 * @param serviceService
	 *           the new service service
	 */
	public void setServiceService(final ServiceService serviceService)
	{
		this.serviceService = serviceService;
	}

	/**
	 * Gets the sub department service.
	 *
	 * @return the sub department service
	 */
	public SubDepartmentService getSubDepartmentService()
	{
		return subDepartmentService;
	}

	/**
	 * Sets the sub department service.
	 *
	 * @param subDepartmentService
	 *           the new sub department service
	 */
	public void setSubDepartmentService(final SubDepartmentService subDepartmentService)
	{
		this.subDepartmentService = subDepartmentService;
	}

	/**
	 * Gets the services.
	 *
	 * @return the services
	 */
	public List<Service> getServices()
	{
		return services;
	}

	/**
	 * Sets the services.
	 *
	 * @param services
	 *           the new services
	 */
	public void setServices(final List<Service> services)
	{
		this.services = services;
	}

	/**
	 * Gets the sub departments.
	 *
	 * @return the sub departments
	 */
	public List<SubDepartment> getSubDepartments()
	{
		return subDepartments;
	}

	/**
	 * Sets the sub departments.
	 *
	 * @param subDepartments
	 *           the new sub departments
	 */
	public void setSubDepartments(final List<SubDepartment> subDepartments)
	{
		this.subDepartments = subDepartments;
	}

	/**
	 * Gets the sub department.
	 *
	 * @return the sub department
	 */
	public SubDepartment getSubDepartment()
	{
		return subDepartment;
	}

	/**
	 * Sets the sub department.
	 *
	 * @param subDepartment
	 *           the new sub department
	 */
	public void setSubDepartment(final SubDepartment subDepartment)
	{
		this.subDepartment = subDepartment;
	}
}
