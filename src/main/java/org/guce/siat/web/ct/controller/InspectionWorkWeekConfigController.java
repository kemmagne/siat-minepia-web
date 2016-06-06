package org.guce.siat.web.ct.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.guce.siat.common.model.InspectionWorkWeekConfig;
import org.guce.siat.common.service.InspectionWorkWeekConfigService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class InspectionWorkWeekConfigController.
 */
@ManagedBean(name = "inspectionWorkWeekConfigController")
@SessionScoped
public class InspectionWorkWeekConfigController extends AbstractController<InspectionWorkWeekConfig>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5230559012089057716L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(InspectionWorkWeekConfigController.class);

	/** The inspection work week config service. */
	@ManagedProperty(value = "#{inspectionWorkWeekConfigService}")
	private InspectionWorkWeekConfigService inspectionWorkWeekConfigService;

	/** The i ww config list by organism. */
	private List<InspectionWorkWeekConfig> iWWConfigListByOrganism;

	/**
	 * Instantiates a new ministry controller.
	 */
	public InspectionWorkWeekConfigController()
	{
		super(InspectionWorkWeekConfig.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, InspectionWorkWeekConfigController.class.getName());
		}
		super.setService(inspectionWorkWeekConfigService);
		super.setPageUrl(ControllerConstants.Pages.BO.INSPECTION_WORK_WEEK_CONFIG_INDEX_PAGE);
		initIWWConfigListByOrganism();
	}

	/**
	 * Validate iwwc list.
	 */
	public void validateIWWCList()
	{
		for (final InspectionWorkWeekConfig iWWConfig : iWWConfigListByOrganism)
		{
			selected = iWWConfig;
			try
			{
				inspectionWorkWeekConfigService.update(selected);

				setSelected(null);
			}
			catch (final Exception ex)
			{
				JsfUtil.addErrorMessage(ex,
						ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
			}
			finally
			{
				initIWWConfigListByOrganism();
			}
		}
		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				InspectionWorkWeekConfig.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
		JsfUtil.addSuccessMessage(msg);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.controller.AbstractController#goToPage()
	 */
	@Override
	public void goToPage()
	{
		initIWWConfigListByOrganism();
		super.goToPage();
	}

	/**
	 * Inits the iww config list by organism.
	 */
	private void initIWWConfigListByOrganism()
	{
		iWWConfigListByOrganism = inspectionWorkWeekConfigService.findIWWConfigByOrganism(getCurrentOrganism());
	}

	/**
	 * Gets the inspection work week config service.
	 *
	 * @return the inspectionWorkWeekConfigService
	 */
	public InspectionWorkWeekConfigService getInspectionWorkWeekConfigService()
	{
		return inspectionWorkWeekConfigService;
	}

	/**
	 * Sets the inspection work week config service.
	 *
	 * @param inspectionWorkWeekConfigService
	 *           the inspectionWorkWeekConfigService to set
	 */
	public void setInspectionWorkWeekConfigService(final InspectionWorkWeekConfigService inspectionWorkWeekConfigService)
	{
		this.inspectionWorkWeekConfigService = inspectionWorkWeekConfigService;
	}

	/**
	 * Gets the i ww config list by organism.
	 *
	 * @return the iWWConfigListByOrganism
	 */
	public List<InspectionWorkWeekConfig> getiWWConfigListByOrganism()
	{
		return iWWConfigListByOrganism;
	}

	/**
	 * Sets the i ww config list by organism.
	 *
	 * @param iWWConfigListByOrganism
	 *           the iWWConfigListByOrganism to set
	 */
	public void setiWWConfigListByOrganism(final List<InspectionWorkWeekConfig> iWWConfigListByOrganism)
	{
		this.iWWConfigListByOrganism = iWWConfigListByOrganism;
	}

}
