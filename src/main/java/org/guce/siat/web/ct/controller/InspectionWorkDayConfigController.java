package org.guce.siat.web.ct.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.guce.siat.common.model.HourlyType;
import org.guce.siat.common.model.InspectionWorkDayConfig;
import org.guce.siat.common.service.HourlyTypeService;
import org.guce.siat.common.service.InspectionWorkDayConfigService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class InspectionWorkDayConfigController.
 */
@ManagedBean(name = "inspectionWorkDayConfigController")
@SessionScoped
public class InspectionWorkDayConfigController extends AbstractController<InspectionWorkDayConfig>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4562770456506825196L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(InspectionWorkDayConfigController.class);

	/** The Constant HOUR_ERROR_OCCURED. */
	private static final String HOUR_ERROR_OCCURED = "HourErrorOccured";

	/** The Constant MISSING_HOURLYTYPE_ERROR. */
	private static final String MISSING_HOURLYTYPE_ERROR = "MissingHourlyTypeError";

	/** The Constant DUPLICATED_HOUR_ERROR. */
	private static final String DUPLICATED_HOUR_ERROR = "DuplicatedHourError";

	/** The Constant HOURS_OF_DAY. */
	private static final int HOURS_OF_DAY = 23;

	/** The Constant CONSTRAINT_VIOLATION_EXCEPTION. */
	private static final String CONSTRAINT_VIOLATION_EXCEPTION = "org.hibernate.exception.ConstraintViolationException";

	/** The inspection work day config service. */
	@ManagedProperty(value = "#{inspectionWorkDayConfigService}")
	private InspectionWorkDayConfigService inspectionWorkDayConfigService;

	/** The hourly type service. */
	@ManagedProperty(value = "#{hourlyTypeService}")
	private HourlyTypeService hourlyTypeService;

	/** The hourly type list. */
	private List<HourlyType> hourlyTypeList;

	/** The selected hourly type. */
	private HourlyType selectedHourlyType;

	/** The selected hour. */
	private SelectItem[] selectedHour;


	/**
	 * Instantiates a new inspection work day config controller.
	 */
	public InspectionWorkDayConfigController()
	{
		super(InspectionWorkDayConfig.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, InspectionWorkDayConfigController.class.getName());
		}
		super.setPageUrl(ControllerConstants.Pages.BO.INSPECTION_WORK_DAY_CONFIG_INDEX_PAGE);
		super.setService(inspectionWorkDayConfigService);
		this.setHourlyTypeList(hourlyTypeService.findAll());
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
		selected.setOrganism(getCurrentOrganism());
		selected.setHourlyType(selectedHourlyType);
		selectedHour = JsfUtil.getHoursOptions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#create()
	 */
	@Override
	public void create()
	{

		try
		{
			if (selected.getHourlyType() == null)
			{
				final String createErrorMsg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						MISSING_HOURLYTYPE_ERROR);
				JsfUtil.addErrorMessage(createErrorMsg);
			}
			else if (selected.getHour() < 0 || selected.getHour() > HOURS_OF_DAY)
			{
				final String hourErrorMsg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						HOUR_ERROR_OCCURED);
				JsfUtil.addErrorMessage(hourErrorMsg);
			}
			else
			{
				inspectionWorkDayConfigService.save(selected);
				if (!isValidationFailed())
				{
					refreshItems();
					selected = null;
				}
				final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						InspectionWorkDayConfig.class.getSimpleName() + PersistenceActions.CREATE.getCode());
				JsfUtil.addSuccessMessage(msg);
			}
		}
		catch (final Exception pe)
		{
			LOG.error(pe.getMessage(), pe);
			if (CONSTRAINT_VIOLATION_EXCEPTION.equals(pe.getCause().getClass().getName()))
			{
				JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						DUPLICATED_HOUR_ERROR));
				selected.setId(null);
			}
			else
			{
				JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						PERSISTENCE_ERROR_OCCURED));
			}
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
		try
		{
			if (selected.getHour() < 0 || selected.getHour() > HOURS_OF_DAY)
			{
				final String hourErrorMsg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						HOUR_ERROR_OCCURED);
				JsfUtil.addErrorMessage(hourErrorMsg);
			}
			else if (duplicatedHour(getItems(), selected.getHour()))
			{
				final String duplicatedHourErrorMsg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						DUPLICATED_HOUR_ERROR);
				JsfUtil.addErrorMessage(duplicatedHourErrorMsg);
			}
			else
			{
				inspectionWorkDayConfigService.update(selected);
				if (!isValidationFailed())
				{
					refreshItems();
					selected = null;
				}
				final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						InspectionWorkDayConfig.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
				JsfUtil.addSuccessMessage(msg);
			}
		}
		catch (final Exception pe)
		{
			LOG.error(pe.getMessage(), pe);
			if (CONSTRAINT_VIOLATION_EXCEPTION.equals(pe.getCause().getClass().getName()))
			{
				JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						DUPLICATED_HOUR_ERROR));
			}
			else
			{
				JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						PERSISTENCE_ERROR_OCCURED));
			}
		}
	}




	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(inspectionWorkDayConfigService.find(this.getSelected().getId()));
		selected.setOrganism(getCurrentOrganism());
		selectedHour = JsfUtil.getHoursOptions();
	}




	/**
	 * Selection changed.
	 *
	 * @param e
	 *           the e
	 */
	public void selectionChanged(final AjaxBehaviorEvent e)
	{

		if (isSelectionIsNull(e))
		{
			selectedHourlyType = null;
		}


		getItems();
	}

	/**
	 * Duplicated hour.
	 *
	 * @param list
	 *           the list
	 * @param hour
	 *           the hour
	 * @return true, if successful
	 */
	public boolean duplicatedHour(final List<InspectionWorkDayConfig> list, final Short hour)
	{
		for (final InspectionWorkDayConfig inspectionWorkDayConfig : list)
		{
			if (inspectionWorkDayConfig.getHour().equals(hour))
			{
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.controller.AbstractController#getItems()
	 */
	@Override
	public List<InspectionWorkDayConfig> getItems()
	{
		try
		{
			if (selectedHourlyType == null)
			{
				items = null;
			}
			else
			{
				items = inspectionWorkDayConfigService.findInspectionWorkDayConfigByHourlyTypeAndOrganism(selectedHourlyType,
						getCurrentOrganism());
			}

		}
		catch (final Exception ex)
		{
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}
		return items;
	}

	/**
	 * Gets the inspection work day config service.
	 *
	 * @return the inspection work day config service
	 */
	public InspectionWorkDayConfigService getInspectionWorkDayConfigService()
	{
		return inspectionWorkDayConfigService;
	}


	/**
	 * Sets the inspection work day config service.
	 *
	 * @param inspectionWorkDayConfigService
	 *           the new inspection work day config service
	 */
	public void setInspectionWorkDayConfigService(final InspectionWorkDayConfigService inspectionWorkDayConfigService)
	{
		this.inspectionWorkDayConfigService = inspectionWorkDayConfigService;
	}

	/**
	 * Gets the hourly type service.
	 *
	 * @return the hourly type service
	 */
	public HourlyTypeService getHourlyTypeService()
	{
		return hourlyTypeService;
	}

	/**
	 * Sets the hourly type service.
	 *
	 * @param hourlyTypeService
	 *           the new hourly type service
	 */
	public void setHourlyTypeService(final HourlyTypeService hourlyTypeService)
	{
		this.hourlyTypeService = hourlyTypeService;
	}

	/**
	 * Gets the hourly type list.
	 *
	 * @return the hourly type list
	 */
	public List<HourlyType> getHourlyTypeList()
	{
		return hourlyTypeList;
	}


	/**
	 * Sets the hourly type list.
	 *
	 * @param hourlyTypeList
	 *           the new hourly type list
	 */
	public void setHourlyTypeList(final List<HourlyType> hourlyTypeList)
	{
		this.hourlyTypeList = hourlyTypeList;
	}

	/**
	 * Gets the selected hourly type.
	 *
	 * @return the selected hourly type
	 */
	public HourlyType getSelectedHourlyType()
	{
		return selectedHourlyType;
	}

	/**
	 * Sets the selected hourly type.
	 *
	 * @param selectedHourlyType
	 *           the new selected hourly type
	 */
	public void setSelectedHourlyType(final HourlyType selectedHourlyType)
	{
		this.selectedHourlyType = selectedHourlyType;
	}

	/**
	 * Gets the selected hour.
	 *
	 * @return the selectedHour
	 */
	public SelectItem[] getSelectedHour()
	{
		return selectedHour;
	}

	/**
	 * Sets the selected hour.
	 *
	 * @param selectedHour
	 *           the selectedHour to set
	 */
	public void setSelectedHour(final SelectItem[] selectedHour)
	{
		this.selectedHour = selectedHour;
	}



}
