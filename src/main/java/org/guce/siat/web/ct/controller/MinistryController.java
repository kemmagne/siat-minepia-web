package org.guce.siat.web.ct.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.guce.siat.common.model.Ministry;
import org.guce.siat.common.service.MinistryService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class MinistryController.
 */
@ManagedBean(name = "ministryController")
@SessionScoped
public class MinistryController extends AbstractController<Ministry>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2873239198179366692L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(MinistryController.class);

	/** The ministry service. */
	@ManagedProperty(value = "#{ministryService}")
	private MinistryService ministryService;

	/**
	 * Instantiates a new ministry controller.
	 */
	public MinistryController()
	{
		super(Ministry.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, MinistryController.class.getName());
		}
		super.setService(ministryService);
		super.setPageUrl(ControllerConstants.Pages.BO.MINISTRY_INDEX_PAGE);
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(ministryService.find(this.getSelected().getId()));
	}

	/**
	 * Gets the ministry service.
	 *
	 * @return the ministryService
	 */
	public MinistryService getMinistryService()
	{
		return ministryService;
	}

	/**
	 * Sets the ministry service.
	 *
	 * @param ministryService
	 *           the ministryService to set
	 */
	public void setMinistryService(final MinistryService ministryService)
	{
		this.ministryService = ministryService;
	}

}
