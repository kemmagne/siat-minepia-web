package org.guce.siat.web.ct.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.guce.siat.common.model.Car;
import org.guce.siat.common.service.CarService;
import org.guce.siat.common.service.OrganismService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class CarController.
 */
@ManagedBean(name = "carController")
@SessionScoped
public class CarController extends AbstractController<Car>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9048992027704089076L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(CarController.class);

	/** The Constant CAR_EXIST_ERROR_MESSAGE. */
	private static final String CAR_EXIST_ERROR_MESSAGE = "CarExistInfo";

	/** The Constant CAR_EXIST_ERROR. */
	private static final String CAR_EXIST_ERROR = "CarExistInfo";

	/** The Constant CONSTRAINT_VIOLATION_EXCEPTION. */
	private static final String CONSTRAINT_VIOLATION_EXCEPTION = "org.hibernate.exception.ConstraintViolationException";

	/** The car service. */
	@ManagedProperty(value = "#{carService}")
	private CarService carService;

	/** The organism service. */
	@ManagedProperty(value = "#{organismService}")
	private OrganismService organismService;


	/**
	 * Instantiates a new car controller.
	 */
	public CarController()
	{
		super(Car.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, CarController.class.getName());
		}
		super.setService(carService);
		super.setPageUrl(ControllerConstants.Pages.BO.CAR_INDEX_PAGE);
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
		final String carSerial = selected.getSerialNumber().toUpperCase();
		selected.setSerialNumber(carSerial);
		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				Car.class.getSimpleName() + PersistenceActions.CREATE.getCode());

		try
		{
			carService.save(selected);
			if (!isValidationFailed())
			{
				refreshItems();
				selected = null;
			}
			JsfUtil.addSuccessMessage(msg);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);

			if (CONSTRAINT_VIOLATION_EXCEPTION.equals(e.getCause().getClass().getName()))
			{
				JsfUtil.addErrorWithTwoMessage(
						ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(CAR_EXIST_ERROR), ResourceBundle
								.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(CAR_EXIST_ERROR_MESSAGE));
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
		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				Car.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
		try
		{
			carService.update(selected);
			if (!isValidationFailed())
			{
				refreshItems();
				selected = null;
			}
			JsfUtil.addSuccessMessage(msg);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			if (CONSTRAINT_VIOLATION_EXCEPTION.equals(e.getCause().getClass().getName()))
			{
				JsfUtil.addErrorWithTwoMessage(
						ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(CAR_EXIST_ERROR), ResourceBundle
								.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(CAR_EXIST_ERROR_MESSAGE));
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
	 * @see org.guce.siat.web.controller.AbstractController#getItems()
	 */
	@Override
	public List<Car> getItems()
	{
		try
		{
			if (items == null)
			{

				items = carService.findByOrganism(getCurrentOrganism());

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
		this.setSelected(carService.find(this.getSelected().getId()));
	}

	/**
	 * Gets the car service.
	 *
	 * @return the car service
	 */
	public CarService getCarService()
	{
		return carService;
	}

	/**
	 * Sets the car service.
	 *
	 * @param carService
	 *           the new car service
	 */
	public void setCarService(final CarService carService)
	{
		this.carService = carService;
	}

	/**
	 * Gets the organism service.
	 *
	 * @return the organism service
	 */
	public OrganismService getOrganismService()
	{
		return organismService;
	}

	/**
	 * Sets the organism service.
	 *
	 * @param organismService
	 *           the new organism service
	 */
	public void setOrganismService(final OrganismService organismService)
	{
		this.organismService = organismService;
	}

	/**
	 * Gets the car exist error message.
	 *
	 * @return the carExistErrorMessage
	 */
	public static String getCarExistErrorMessage()
	{
		return CAR_EXIST_ERROR_MESSAGE;
	}

	/**
	 * Gets the car exist error.
	 *
	 * @return the carExistError
	 */
	public static String getCarExistError()
	{
		return CAR_EXIST_ERROR;
	}

	/**
	 * Gets the constraint violation exception.
	 *
	 * @return the constraintViolationException
	 */
	public static String getConstraintViolationException()
	{
		return CONSTRAINT_VIOLATION_EXCEPTION;
	}



}
