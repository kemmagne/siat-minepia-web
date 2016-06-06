package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.service.AdministrationService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.core.ct.model.TreatmentCompany;
import org.guce.siat.core.ct.model.TreatmentType;
import org.guce.siat.core.ct.service.TreatmentCompanyService;
import org.guce.siat.core.ct.service.TreatmentTypeService;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class TreatmentCompanyController.
 */
@ManagedBean(name = "treatmentCompanyController")
@SessionScoped
public class TreatmentCompanyController extends AbstractController<TreatmentCompany>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6107251732371330472L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(TreatmentCompanyController.class);

	/** The treatment company service. */
	@ManagedProperty(value = "#{treatmentCompanyService}")
	private TreatmentCompanyService treatmentCompanyService;

	/** The treatment type service. */
	@ManagedProperty(value = "#{treatmentTypeService}")
	private TreatmentTypeService treatmentTypeService;

	/** The administration service. */
	@ManagedProperty(value = "#{administrationService}")
	private AdministrationService administrationService;

	/** The treatment types list. */
	private List<TreatmentType> treatmentTypesList;

	/** The treatment types pick list. */
	private DualListModel<TreatmentType> treatmentTypesPickList;

	/** The selected sub department. */
	private SubDepartment selectedSubDepartment;

	/** The services by sub department. */
	private List<Service> servicesBySubDepartment;

	/**
	 * Instantiates a new treatment company controller.
	 */
	public TreatmentCompanyController()
	{
		super(TreatmentCompany.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, TreatmentCompanyController.class.getName());
		}
		super.setService(treatmentCompanyService);
		super.setPageUrl(ControllerConstants.Pages.BO.TREATMENT_COMPANY_INDEX_PAGE);

		treatmentTypesList = treatmentTypeService.findByOrganism(getCurrentOrganism());
		treatmentTypesPickList = new DualListModel<TreatmentType>(new ArrayList<TreatmentType>(), new ArrayList<TreatmentType>());
		treatmentTypesPickList.getSource().addAll(treatmentTypesList);
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

		selected.setTreatmentTypeList(treatmentTypesPickList.getTarget());

		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				"TreatmentCompany" + PersistenceActions.CREATE.getCode());
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
	 * @see org.guce.siat.web.common.AbstractController#delete()
	 */
	@Override
	public void delete()
	{
		selected.setDeleted(true);
		getSelected().getTreatmentTypeList().addAll(treatmentTypesPickList.getTarget());

		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				"TreatmentCompany" + PersistenceActions.DELETE.getCode());
		persist(PersistenceActions.UPDATE, msg);
		refreshItems();
		selected = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#edit()
	 */
	@Override
	public void edit()
	{
		getSelected().getTreatmentTypeList().clear();
		getSelected().getTreatmentTypeList().addAll(treatmentTypesPickList.getTarget());
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
		selectedSubDepartment = null;
		servicesBySubDepartment = new ArrayList<Service>();
		treatmentTypesPickList = new DualListModel<TreatmentType>(new ArrayList<TreatmentType>(), new ArrayList<TreatmentType>());
		treatmentTypesPickList.getSource().addAll(treatmentTypesList);
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(treatmentCompanyService.find(this.getSelected().getId()));
		selectedSubDepartment = selected.getService().getSubDepartment();
		findServiceListBySelectedSubDepartment();

		treatmentTypesPickList = new DualListModel<TreatmentType>(new ArrayList<TreatmentType>(), new ArrayList<TreatmentType>());
		treatmentTypesPickList.getSource().addAll(treatmentTypesList);

		for (final TreatmentType treatmentType : this.getSelected().getTreatmentTypeList())
		{
			for (final TreatmentType treatmentType1 : treatmentTypesPickList.getSource())
			{
				if (treatmentType.getId().equals(treatmentType1.getId()))
				{
					treatmentTypesPickList.getTarget().add(treatmentType);
					treatmentTypesPickList.getSource().remove(treatmentType);
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<TreatmentCompany> getItems()
	{
		try
		{
			if (items == null)
			{
				items = treatmentCompanyService.findByOrganism(getCurrentOrganism());
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
	 * Find service list by selected sub department.
	 */
	public void findServiceListBySelectedSubDepartment()
	{
		servicesBySubDepartment = new ArrayList<Service>();
		for (final Administration administration : administrationService
				.getSubAdministrationByAdministration(selectedSubDepartment))
		{
			servicesBySubDepartment.add((Service) administration);
		}
	}

	/**
	 * Gets the treatment company service.
	 *
	 * @return the treatment company service
	 */
	public TreatmentCompanyService getTreatmentCompanyService()
	{
		return treatmentCompanyService;
	}

	/**
	 * Sets the treatment company service.
	 *
	 * @param treatmentCompanyService
	 *           the new treatment company service
	 */
	public void setTreatmentCompanyService(final TreatmentCompanyService treatmentCompanyService)
	{
		this.treatmentCompanyService = treatmentCompanyService;
	}

	/**
	 * Gets the treatment type service.
	 *
	 * @return the treatment type service
	 */
	public TreatmentTypeService getTreatmentTypeService()
	{
		return treatmentTypeService;
	}

	/**
	 * Sets the treatment type service.
	 *
	 * @param treatmentTypeService
	 *           the new treatment type service
	 */
	public void setTreatmentTypeService(final TreatmentTypeService treatmentTypeService)
	{
		this.treatmentTypeService = treatmentTypeService;
	}

	/**
	 * Gets the treatment types list.
	 *
	 * @return the treatment types list
	 */
	public List<TreatmentType> getTreatmentTypesList()
	{
		return treatmentTypesList;
	}

	/**
	 * Gets the administration service.
	 *
	 * @return the administrationService
	 */
	public AdministrationService getAdministrationService()
	{
		return administrationService;
	}

	/**
	 * Sets the administration service.
	 *
	 * @param administrationService
	 *           the administrationService to set
	 */
	public void setAdministrationService(final AdministrationService administrationService)
	{
		this.administrationService = administrationService;
	}

	/**
	 * Sets the treatment types list.
	 *
	 * @param treatmentTypesList
	 *           the new treatment types list
	 */
	public void setTreatmentTypesList(final List<TreatmentType> treatmentTypesList)
	{
		this.treatmentTypesList = treatmentTypesList;
	}

	/**
	 * Gets the treatment types pick list.
	 *
	 * @return the treatment types pick list
	 */
	public DualListModel<TreatmentType> getTreatmentTypesPickList()
	{
		return treatmentTypesPickList;
	}

	/**
	 * Sets the treatment types pick list.
	 *
	 * @param treatmentTypesPickList
	 *           the new treatment types pick list
	 */
	public void setTreatmentTypesPickList(final DualListModel<TreatmentType> treatmentTypesPickList)
	{
		this.treatmentTypesPickList = treatmentTypesPickList;
	}

	/**
	 * Gets the selected sub department.
	 *
	 * @return the selectedSubDepartment
	 */
	public SubDepartment getSelectedSubDepartment()
	{
		return selectedSubDepartment;
	}

	/**
	 * Sets the selected sub department.
	 *
	 * @param selectedSubDepartment
	 *           the selectedSubDepartment to set
	 */
	public void setSelectedSubDepartment(final SubDepartment selectedSubDepartment)
	{
		this.selectedSubDepartment = selectedSubDepartment;
	}

	/**
	 * Gets the services by sub department.
	 *
	 * @return the servicesBySubDepartment
	 */
	public List<Service> getServicesBySubDepartment()
	{
		return servicesBySubDepartment;
	}

	/**
	 * Sets the services by sub department.
	 *
	 * @param servicesBySubDepartment
	 *           the servicesBySubDepartment to set
	 */
	public void setServicesBySubDepartment(final List<Service> servicesBySubDepartment)
	{
		this.servicesBySubDepartment = servicesBySubDepartment;
	}

}
