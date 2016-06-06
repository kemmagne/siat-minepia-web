package org.guce.siat.web.ct.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.guce.siat.common.model.AnalyseType;
import org.guce.siat.common.service.AnalyseTypeService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class AnalyseTypeController.
 */
@ManagedBean(name = "analyseTypeController")
@SessionScoped
public class AnalyseTypeController extends AbstractController<AnalyseType>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6230385255709388718L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(AnalyseTypeController.class);

	/** The AnalyseType service. */
	@ManagedProperty(value = "#{analyseTypeService}")
	private AnalyseTypeService analyseTypeService;

	/** The items. */
	private List<AnalyseType> items;


	/**
	 * Instantiates a new AnalyseType controller.
	 */
	public AnalyseTypeController()
	{
		super(AnalyseType.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, AnalyseTypeController.class.getName());
		}
		super.setService(analyseTypeService);
		super.setPageUrl(ControllerConstants.Pages.BO.ANALYSE_TYPE_INDEX_PAGE);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.controller.AbstractController#getItems()
	 */
	@Override
	public List<AnalyseType> getItems()
	{
		try
		{
			if (items == null)
			{
				items = analyseTypeService.findByAdministration(getCurrentOrganism());
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

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(analyseTypeService.find(this.getSelected().getId()));
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
	 * Sets the items.
	 *
	 * @param items
	 *           the items to set
	 */
	@Override
	public void setItems(final List<AnalyseType> items)
	{
		this.items = items;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.controller.AbstractController#refreshItems()
	 */
	@Override
	public void refreshItems()
	{
		items = null;
		getItems();
	}


}
