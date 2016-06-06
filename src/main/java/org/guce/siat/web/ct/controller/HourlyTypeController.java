package org.guce.siat.web.ct.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.guce.siat.common.model.HourlyType;
import org.guce.siat.common.model.InspectionWorkWeekConfig;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.service.HourlyTypeService;
import org.guce.siat.common.service.InspectionWorkDayConfigService;
import org.guce.siat.common.service.InspectionWorkWeekConfigService;
import org.guce.siat.common.service.OrganismService;
import org.guce.siat.common.service.WorkYearConfigService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The class HourlyTypeController.
 */
@ManagedBean(name = "hourlyTypeController")
@SessionScoped
public class HourlyTypeController extends AbstractController<HourlyType>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2611557851726027501L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(HourlyTypeController.class);

	/** The Constant HOURLY_TYPE_USED_ERROR_MESSAGE. */
	private static final String HOURLY_TYPE_USED_ERROR_MESSAGE = "HourlyTypeMessageErrorForExist";

	/** The hourly type service. */
	@ManagedProperty(value = "#{hourlyTypeService}")
	private HourlyTypeService hourlyTypeService;

	/** The work year config service. */
	@ManagedProperty(value = "#{workYearConfigService}")
	private WorkYearConfigService workYearConfigService;


	/** The inspection work day config service. */
	@ManagedProperty(value = "#{inspectionWorkDayConfigService}")
	private InspectionWorkDayConfigService inspectionWorkDayConfigService;

	/** The inspection work week config service. */
	@ManagedProperty(value = "#{inspectionWorkWeekConfigService}")
	private InspectionWorkWeekConfigService inspectionWorkWeekConfigService;


	/** The organism service. */
	@ManagedProperty(value = "#{organismService}")
	private OrganismService organismService;


	/** The organisms. */
	private List<Organism> organisms;

	/**
	 * Instantiates a new hourly type controller.
	 */
	public HourlyTypeController()
	{
		super(HourlyType.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, HourlyTypeController.class.getName());
		}
		super.setService(hourlyTypeService);
		super.setPageUrl(ControllerConstants.Pages.BO.HOURLY_TYPE_INDEX_PAGE);
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(hourlyTypeService.find(this.getSelected().getId()));
	}


	/**
	 * Save new.
	 *
	 * @param event
	 *           the event
	 */
	public void saveNew(final ActionEvent event)
	{
		try
		{

			final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("HourlyTypeCreated");
			final HourlyType hourlyType = hourlyTypeService.save(selected);
			organisms = organismService.findAll();
			if (organisms != null)
			{
				for (final Organism organism : organisms)
				{
					final InspectionWorkWeekConfig iwwc = new InspectionWorkWeekConfig();
					iwwc.setMonday(true);
					iwwc.setThursday(true);
					iwwc.setWednesday(true);
					iwwc.setTuesday(true);
					iwwc.setFriday(true);
					iwwc.setSaturday(false);
					iwwc.setSunday(false);
					iwwc.setOrganism(organism);
					iwwc.setHourlyTypeId(hourlyType);
					inspectionWorkWeekConfigService.save(iwwc);

				}

			}
			JsfUtil.addSuccessMessage(msg);
		}
		catch (final Exception e)
		{
			LOG.error(e.getLocalizedMessage());
			JsfUtil.addErrorMessage(e,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}


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

		if ((!inspectionWorkDayConfigService.findByHourlyType(selected).isEmpty() || !workYearConfigService.findByHourlyType(
				selected).isEmpty())
				&& inspectionWorkWeekConfigService.findByHourlyType(selected).isEmpty())
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					HOURLY_TYPE_USED_ERROR_MESSAGE));
		}
		else if (!inspectionWorkWeekConfigService.findByHourlyType(selected).isEmpty())
		{

			for (final InspectionWorkWeekConfig inspectionWorkWeekConfig : inspectionWorkWeekConfigService
					.findByHourlyType(selected))
			{
				inspectionWorkWeekConfigService.delete(inspectionWorkWeekConfig);
			}
			super.delete();
		}
		else
		{
			super.delete();
		}
	}

	/**
	 * Gets the hourly type service.
	 *
	 * @return the hourlyTypeService
	 */
	public HourlyTypeService getHourlyTypeService()
	{
		return hourlyTypeService;
	}

	/**
	 * Sets the hourly type service.
	 *
	 * @param hourlyTypeService
	 *           the hourlyTypeService to set
	 */
	public void setHourlyTypeService(final HourlyTypeService hourlyTypeService)
	{
		this.hourlyTypeService = hourlyTypeService;
	}

	/**
	 * Gets the work year config service.
	 *
	 * @return the workYearConfigService
	 */
	public WorkYearConfigService getWorkYearConfigService()
	{
		return workYearConfigService;
	}

	/**
	 * Sets the work year config service.
	 *
	 * @param workYearConfigService
	 *           the workYearConfigService to set
	 */
	public void setWorkYearConfigService(final WorkYearConfigService workYearConfigService)
	{
		this.workYearConfigService = workYearConfigService;
	}

	/**
	 * Gets the inspection work day config service.
	 *
	 * @return the inspectionWorkDayConfigService
	 */
	public InspectionWorkDayConfigService getInspectionWorkDayConfigService()
	{
		return inspectionWorkDayConfigService;
	}

	/**
	 * Sets the inspection work day config service.
	 *
	 * @param inspectionWorkDayConfigService
	 *           the inspectionWorkDayConfigService to set
	 */
	public void setInspectionWorkDayConfigService(final InspectionWorkDayConfigService inspectionWorkDayConfigService)
	{
		this.inspectionWorkDayConfigService = inspectionWorkDayConfigService;
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
	 * Gets the organism service.
	 *
	 * @return the organismService
	 */
	public OrganismService getOrganismService()
	{
		return organismService;
	}

	/**
	 * Sets the organism service.
	 *
	 * @param organismService
	 *           the organismService to set
	 */
	public void setOrganismService(final OrganismService organismService)
	{
		this.organismService = organismService;
	}

	/**
	 * Gets the organisms.
	 *
	 * @return the organisms
	 */
	public List<Organism> getOrganisms()
	{
		return organisms;
	}

	/**
	 * Sets the organisms.
	 *
	 * @param organisms
	 *           the organisms to set
	 */
	public void setOrganisms(final List<Organism> organisms)
	{
		this.organisms = organisms;
	}



}
