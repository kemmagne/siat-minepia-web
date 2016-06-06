package org.guce.siat.web.ct.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.guce.siat.common.utils.Constants;
import org.guce.siat.core.ct.model.TreatmentType;
import org.guce.siat.core.ct.service.TreatmentTypeService;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class TreatmentTypeController.
 */
@ManagedBean(name = "treatmentTypeController")
@SessionScoped
public class TreatmentTypeController extends AbstractController<TreatmentType>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5184564428307556391L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(TreatmentTypeController.class);

	/** The treatment type service. */
	@ManagedProperty(value = "#{treatmentTypeService}")
	private TreatmentTypeService treatmentTypeService;

	/** The items. */
	private List<TreatmentType> items;


	/**
	 * Instantiates a new treatment type controller.
	 */
	public TreatmentTypeController()
	{
		super(TreatmentType.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, TreatmentTypeController.class.getName());
		}
		super.setService(treatmentTypeService);
		super.setPageUrl(ControllerConstants.Pages.BO.TREATMENT_TYPE_INDEX_PAGE);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<TreatmentType> getItems()
	{
		try
		{
			if (items == null)
			{
				items = treatmentTypeService.findByOrganism(getCurrentOrganism());
			}
		}
		catch (final Exception ex)
		{
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}
		return items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#create()
	 */
	@Override
	public void create()
	{
		selected.setOrganism(getCurrentOrganism());
		selected.setDeleted(false);
		super.create();
		refreshItems();
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
		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				"TreatmentType" + PersistenceActions.DELETE.getCode());
		persist(PersistenceActions.UPDATE, msg);
		refreshItems();
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(treatmentTypeService.find(this.getSelected().getId()));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#setItems(java.util.List)
	 */
	@Override
	public void setItems(final List<TreatmentType> items)
	{
		this.items = items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#refreshItems()
	 */
	@Override
	public void refreshItems()
	{
		items = null;
		getItems();
	}
}
