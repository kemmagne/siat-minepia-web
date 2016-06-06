package org.guce.siat.web.ct.controller;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.Appointment;
import org.guce.siat.common.service.AppointmentService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class AppointmentDecisionController.
 */
/**
 *
 */
@ManagedBean(name = "appointmentDecisionController")
@ViewScoped
public class AppointmentDecisionController extends AbstractController<Appointment>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 701036028409676656L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(AppointmentDecisionController.class);

	/** The appointment service. */
	@ManagedProperty(value = "#{appointmentService}")
	private AppointmentService appointmentService;

	/** The selected appointment. */
	private Appointment selectedAppointment;

	/** The event model. */
	private ScheduleModel eventModel;

	/** The lazy event model. */
	private ScheduleModel lazyEventModel;

	/** The event. */
	private ScheduleEvent event = new DefaultScheduleEvent();

	/**
	 * Instantiates a new appointment decision controller.
	 */
	public AppointmentDecisionController()
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
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, AppointmentDecisionController.class.getName());
		}
		super.setService(appointmentService);
		eventModel = new DefaultScheduleModel();
		eventModel.addEvent(new DefaultScheduleEvent("Champions League Match", previousDay8Pm(), previousDay11Pm()));
		eventModel.addEvent(new DefaultScheduleEvent("Birthday Party", today1Pm(), today6Pm()));
		eventModel.addEvent(new DefaultScheduleEvent("Breakfast at Tiffanys", nextDay9Am(), nextDay11Am()));
		eventModel.addEvent(new DefaultScheduleEvent("Plant the new garden stuff", theDayAfter3Pm(), fourDaysLater3pm()));

		lazyEventModel = new LazyScheduleModel()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void loadEvents(final Date start, final Date end)
			{
				Date random = getRandomDate(start);
				addEvent(new DefaultScheduleEvent("Lazy Event 1", random, random));

				random = getRandomDate(start);
				addEvent(new DefaultScheduleEvent("Lazy Event 2", random, random));
			}
		};
	}

	/**
	 * Validate appointment.
	 */
	public void validateAppointment()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(" *********** Appointment StartDate And And Date = {}  ****  {}", selected.getBeginTime(),
					selected.getEndTime());
		}
		setSelectedAppointment(selectedAppointment);
	}

	/**
	 * Click button schedule.
	 */
	public void clickButtonSchedule()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(" ***** AppointmentDecisionController.clickButtonSchedule()  ***** ");
		}
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
	 * Gets the random date.
	 *
	 * @param base
	 *           the base
	 * @return the random date
	 */
	public Date getRandomDate(final Date base)
	{
		final Calendar date = Calendar.getInstance();
		date.setTime(base);
		//set random day of month
		date.add(Calendar.DATE, ((int) (Math.random() * Constants.THIRTY_SIX)) + Constants.ONE);

		return date.getTime();
	}

	/**
	 * Gets the initial date.
	 *
	 * @return the initial date
	 */
	public Date getInitialDate()
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), Constants.ZERO, Constants.ZERO,
				Constants.ZERO);

		return calendar.getTime();
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
	 * Gets the lazy event model.
	 *
	 * @return the lazy event model
	 */
	public ScheduleModel getLazyEventModel()
	{
		return lazyEventModel;
	}

	/**
	 * Today.
	 *
	 * @return the calendar
	 */
	private Calendar today()
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), Constants.ZERO,
				Constants.ZERO, Constants.ZERO);

		return calendar;
	}

	/**
	 * Previous day8 pm.
	 *
	 * @return the date
	 */
	private Date previousDay8Pm()
	{
		final Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) - Constants.ONE);
		t.set(Calendar.HOUR, Constants.EIGHT);

		return t.getTime();
	}

	/**
	 * Previous day11 pm.
	 *
	 * @return the date
	 */
	private Date previousDay11Pm()
	{
		final Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) - Constants.ONE);
		t.set(Calendar.HOUR, Constants.ELEVEN);

		return t.getTime();
	}

	/**
	 * Today1 pm.
	 *
	 * @return the date
	 */
	private Date today1Pm()
	{
		final Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.HOUR, Constants.ONE);

		return t.getTime();
	}

	/**
	 * The day after3 pm.
	 *
	 * @return the date
	 */
	private Date theDayAfter3Pm()
	{
		final Calendar t = (Calendar) today().clone();
		t.set(Calendar.DATE, t.get(Calendar.DATE) + Constants.TWO);
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.HOUR, Constants.THREE);

		return t.getTime();
	}

	/**
	 * Today6 pm.
	 *
	 * @return the date
	 */
	private Date today6Pm()
	{
		final Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.HOUR, Constants.SIX);

		return t.getTime();
	}

	/**
	 * Next day9 am.
	 *
	 * @return the date
	 */
	private Date nextDay9Am()
	{
		final Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.AM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) + Constants.ONE);
		t.set(Calendar.HOUR, Constants.NINE);

		return t.getTime();
	}

	/**
	 * Next day11 am.
	 *
	 * @return the date
	 */
	private Date nextDay11Am()
	{
		final Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.AM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) + Constants.ONE);
		t.set(Calendar.HOUR, Constants.ELEVEN);

		return t.getTime();
	}

	/**
	 * Four days later3pm.
	 *
	 * @return the date
	 */
	private Date fourDaysLater3pm()
	{
		final Calendar t = (Calendar) today().clone();
		t.set(Calendar.AM_PM, Calendar.PM);
		t.set(Calendar.DATE, t.get(Calendar.DATE) + Constants.FOUR);
		t.set(Calendar.HOUR, Constants.THREE);

		return t.getTime();
	}

	/**
	 * Gets the event.
	 *
	 * @return the event
	 */
	public ScheduleEvent getEvent()
	{
		return event;
	}

	/**
	 * Sets the event.
	 *
	 * @param event
	 *           the new event
	 */
	public void setEvent(final ScheduleEvent event)
	{
		this.event = event;
	}

	/**
	 * Adds the event.
	 *
	 * @param actionEvent
	 *           the action event
	 */
	public void addEvent(final ActionEvent actionEvent)
	{
		if (event.getId() == null)
		{
			eventModel.addEvent(event);
		}
		else
		{
			eventModel.updateEvent(event);
		}

		event = new DefaultScheduleEvent();
	}

	/**
	 * On event select.
	 *
	 * @param selectEvent
	 *           the select event
	 */
	public void onEventSelect(final SelectEvent selectEvent)
	{
		event = (ScheduleEvent) selectEvent.getObject();
		if (LOG.isDebugEnabled())
		{
			LOG.debug(" ****** AppointmentDecisionController.onEventSelect()  **** DATE : {}", event.getStartDate());
		}
		selected = new Appointment();
		selected.setBeginTime(event.getStartDate());
		selected.setEndTime(event.getEndDate());
		setSelected(selected);

	}

	/**
	 * On date select.
	 *
	 * @param selectEvent
	 *           the select event
	 */
	public void onDateSelect(final SelectEvent selectEvent)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(" ****** AppointmentDecisionController.onDateSelect() **** DATE : {}", selectEvent.getObject());
		}
		event = new DefaultScheduleEvent(StringUtils.EMPTY, (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
		selected = new Appointment();
		selected.setBeginTime((Date) selectEvent.getObject());
		setSelected(selected);

	}

	/**
	 * On event move.
	 *
	 * @param event
	 *           the event
	 */
	public void onEventMove(final ScheduleEntryMoveEvent event)
	{
		final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta()
				+ ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	/**
	 * On event resize.
	 *
	 * @param event
	 *           the event
	 */
	public void onEventResize(final ScheduleEntryResizeEvent event)
	{
		final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:"
				+ event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	/**
	 * Adds the message.
	 *
	 * @param message
	 *           the message
	 */
	private void addMessage(final FacesMessage message)
	{
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/**
	 * Gets the selected appointment.
	 *
	 * @return the selectedAppointment
	 */
	public Appointment getSelectedAppointment()
	{
		return selectedAppointment;
	}

	/**
	 * Sets the selected appointment.
	 *
	 * @param selectedAppointment
	 *           the selectedAppointment to set
	 */
	public void setSelectedAppointment(final Appointment selectedAppointment)
	{
		this.selectedAppointment = selectedAppointment;
	}


}
