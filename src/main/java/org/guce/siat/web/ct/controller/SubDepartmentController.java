package org.guce.siat.web.ct.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.service.SubDepartmentService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class SubDepartmentController.
 */
@ManagedBean(name = "subDepartmentController")
@SessionScoped
public class SubDepartmentController extends AbstractController<SubDepartment>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7325028524318186616L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(SubDepartmentController.class);

	/** The sub department service. */
	@ManagedProperty(value = "#{subDepartmentService}")
	private SubDepartmentService subDepartmentService;

	/**
	 * Instantiates a new sub department controller.
	 */
	public SubDepartmentController()
	{
		super(SubDepartment.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, SubDepartmentController.class.getName());
		}
		super.setService(subDepartmentService);
		super.setPageUrl(ControllerConstants.Pages.BO.SUBDEPARTMENT_INDEX_PAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<SubDepartment> getItems()
	{
		try
		{
			if (items == null)
			{
				items = subDepartmentService.findSubDepartmentsByOrganism(getCurrentOrganism());
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
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(super.getService().find(this.getSelected().getId()));
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

}
