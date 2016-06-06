package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.Appointment;
import org.guce.siat.common.model.AppointmentItemFlow;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.Holiday;
import org.guce.siat.common.service.AppointmentService;
import org.guce.siat.common.service.FileService;
import org.guce.siat.common.service.HolidayService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.common.util.AppointmentEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class AppointmentDecisionController.
 */
@ManagedBean(name = "inspectionAppointmentController")
@SessionScoped
public class InspectionAppointmentController extends AbstractController<Appointment>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 701036028409676656L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(InspectionAppointmentController.class);

	/** The Constant EVENT_HOLIDAY. */
	private static final String EVENT_HOLIDAY = "holiday";

	/** The Constant CALENDAR_EVENT_TYPE. */
	private static final String CALENDAR_EVENT_TYPE = "Calendar_eventType_";

	/** The appointment service. */
	@ManagedProperty(value = "#{appointmentService}")
	private AppointmentService appointmentService;

	/** The file service. */
	@ManagedProperty(value = "#{fileService}")
	private FileService fileService;

	/** The holiday service. */
	@ManagedProperty(value = "#{holidayService}")
	private HolidayService holidayService;

	/** The event model. */
	private ScheduleModel eventModel;

	/** The file field value. */
	private FileFieldValue fileFieldValue;

	/**
	 * Instantiates a new appointment decision controller.
	 */
	public InspectionAppointmentController()
	{
		super(Appointment.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, InspectionAppointmentController.class.getName());
		}
		super.setService(appointmentService);
		super.setPageUrl(ControllerConstants.Pages.FO.INSPECTION_APPOINTMENT_CCT_PAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#goToPage()
	 */
	@Override
	public void goToPage()
	{
		iniControllerApppointment();

		try
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, pageUrl));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Ini controller apppointment.
	 */
	public void iniControllerApppointment()
	{
		refreshItems();

		eventModel = new DefaultScheduleModel();

		try
		{
			fillHolidays();
		}
		catch (final ParseException e)
		{
			LOG.error(e.getMessage(), e);
		}

		for (final Appointment appointment : items)
		{
			final List<AppointmentItemFlow> appItemFlowList = new ArrayList<AppointmentItemFlow>(
					appointment.getAppointmentItemFlowList());

			if (BooleanUtils.isFalse(appointment.getInspectionOnDock()))
			{
				eventModel.addEvent(new DefaultScheduleEvent(appItemFlowList.get(0).getItemFlow().getFileItem().getFile().getClient()
						.getFirstAddress(), appointment.getBeginTime(), appointment.getEndTime()));
			}
			else
			{
				fileFieldValue = fileService.findFileFieldValueByFieldCode(appItemFlowList.get(0).getItemFlow().getFileItem()
						.getFile(), "INFORMATIONS_GENERALES_POINT_ENTREE_UNLOCODE");

				eventModel.addEvent(new DefaultScheduleEvent(fileFieldValue != null ? fileFieldValue.getValue() : StringUtils.EMPTY,
						appointment.getBeginTime(), appointment.getEndTime()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<Appointment> getItems()
	{
		if (items == null)
		{
			items = appointmentService.findAppointmentsByControllerList(Collections.singletonList(getLoggedUser()));
		}
		return items;
	}

	/**
	 * Fill holidays.
	 *
	 * @throws ParseException
	 *            the parse exception
	 */
	private void fillHolidays() throws ParseException
	{

		final List<Holiday> holidayList = holidayService.findBetweenTwoDates(DateUtils.getCurrentYearStartDate(),
				DateUtils.getCurrentYearEndDate());

		if (CollectionUtils.isNotEmpty(holidayList))
		{
			for (final Holiday holiday : holidayList)
			{
				final String title = generateTitleForEvent(EVENT_HOLIDAY, holiday);
				final AppointmentEvent scheduleEvent = new AppointmentEvent();
				scheduleEvent.setTitle(title);
				scheduleEvent.setStartDate(new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS).parse(holiday.getHolidayDate()
						+ " 00:00:00"));
				scheduleEvent.setEndDate(new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS).parse(holiday.getHolidayDate()
						+ " 23:59:59"));
				scheduleEvent.setStyleClass(EVENT_HOLIDAY);
				scheduleEvent.setBeginHour((short) 0);
				scheduleEvent.setBeginMinutes(0);
				scheduleEvent.setTypeEvent(EVENT_HOLIDAY);
				scheduleEvent.setEditable(false);
				scheduleEvent.setDescription(title);
				scheduleEvent.setAppointment(null);
				eventModel.addEvent(scheduleEvent);
			}
		}
	}

	/**
	 * Generate title for event.
	 *
	 * @param eventType
	 *           the event type
	 * @param holiday
	 *           the holiday
	 * @return the string
	 */
	private String generateTitleForEvent(final String eventType, final Holiday holiday)
	{
		final StringBuilder stringBuilder = new StringBuilder();

		final String typeEvent = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				CALENDAR_EVENT_TYPE + eventType);

		if (EVENT_HOLIDAY.equals(eventType))
		{

			stringBuilder.append(typeEvent).append(holiday.getLabel());
		}

		return stringBuilder.toString();
	}

	/**
	 * Gets the appointment service.
	 *
	 * @return the appointmentService
	 */
	public AppointmentService getAppointmentService()
	{
		return appointmentService;
	}

	/**
	 * Sets the appointment service.
	 *
	 * @param appointmentService
	 *           the appointmentService to set
	 */
	public void setAppointmentService(final AppointmentService appointmentService)
	{
		this.appointmentService = appointmentService;
	}

	/**
	 * Gets the event model.
	 *
	 * @return the event model
	 */
	public ScheduleModel getEventModel()
	{
		return eventModel;
	}

	/**
	 * Gets the file service.
	 *
	 * @return the file service
	 */
	public FileService getFileService()
	{
		return fileService;
	}

	/**
	 * Sets the file service.
	 *
	 * @param fileService
	 *           the new file service
	 */
	public void setFileService(final FileService fileService)
	{
		this.fileService = fileService;
	}

	/**
	 * Gets the file field value.
	 *
	 * @return the file field value
	 */
	public FileFieldValue getFileFieldValue()
	{
		return fileFieldValue;
	}

	/**
	 * Sets the file field value.
	 *
	 * @param fileFieldValue
	 *           the new file field value
	 */
	public void setFileFieldValue(final FileFieldValue fileFieldValue)
	{
		this.fileFieldValue = fileFieldValue;
	}

	/**
	 * Sets the event model.
	 *
	 * @param eventModel
	 *           the new event model
	 */
	public void setEventModel(final ScheduleModel eventModel)
	{
		this.eventModel = eventModel;
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
