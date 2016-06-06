package org.guce.siat.web.ct.controller;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.guce.siat.common.model.Holiday;
import org.guce.siat.common.service.HolidayService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class HolidayController.
 */
@ManagedBean(name = "holidayController")
@SessionScoped
public class HolidayController extends AbstractController<Holiday>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7195805216050646116L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(HolidayController.class);

	/** The Constant DUPLICATED_HOUR_ERROR. */
	private static final String DUPLICATED_DATE_ERROR = "DuplicatedDateError";

	/** The Constant CONSTRAINT_VIOLATION_EXCEPTION. */
	private static final String CONSTRAINT_VIOLATION_EXCEPTION = "org.hibernate.exception.ConstraintViolationException";

	/** The holiday service. */
	@ManagedProperty(value = "#{holidayService}")
	private HolidayService holidayService;


	/**
	 * Instantiates a new holiday controller.
	 */
	public HolidayController()
	{
		super(Holiday.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, HolidayController.class.getName());
		}
		super.setService(holidayService);
		super.setPageUrl(ControllerConstants.Pages.BO.HOLIDAY_INDEX_PAGE);
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(holidayService.find(this.getSelected().getId()));

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
			selected.setDeleted(false);
			holidayService.save(selected);
			if (!isValidationFailed())
			{
				refreshItems();
				selected = null;
			}
			final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					Holiday.class.getSimpleName() + PersistenceActions.CREATE.getCode());
			JsfUtil.addSuccessMessage(msg);
		}
		catch (final Exception pe)
		{
			LOG.error(pe.getMessage(), pe);
			if (CONSTRAINT_VIOLATION_EXCEPTION.equals(pe.getCause().getClass().getName()))
			{
				JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						DUPLICATED_DATE_ERROR));
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
			holidayService.update(selected);
			if (!isValidationFailed())
			{
				refreshItems();
				selected = null;
			}
			final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					Holiday.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
			JsfUtil.addSuccessMessage(msg);
		}
		catch (final Exception pe)
		{
			LOG.error(pe.getMessage(), pe);
			if (CONSTRAINT_VIOLATION_EXCEPTION.equals(pe.getCause().getClass().getName()))
			{
				JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						DUPLICATED_DATE_ERROR));
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
	 * @see org.guce.siat.web.common.AbstractController#delete()
	 */
	@Override
	public void delete()
	{
		selected.setDeleted(true);
		holidayService.update(selected);
		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				Holiday.class.getSimpleName() + PersistenceActions.DELETE.getCode());
		JsfUtil.addSuccessMessage(msg);
		refreshItems();
		selected = null;
	}


	/**
	 * Gets the holiday service.
	 *
	 * @return the holidayService
	 */
	public HolidayService getHolidayService()
	{
		return holidayService;
	}


	/**
	 * Sets the holiday service.
	 *
	 * @param holidayService
	 *           the holidayService to set
	 */
	public void setHolidayService(final HolidayService holidayService)
	{
		this.holidayService = holidayService;
	}
}
