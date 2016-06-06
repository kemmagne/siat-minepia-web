package org.guce.siat.web.common.util;

import java.util.Date;

import org.guce.siat.common.model.Appointment;
import org.guce.siat.common.model.User;
import org.primefaces.model.DefaultScheduleEvent;


/**
 * The Class AppointmentEvent.
 */
public class AppointmentEvent extends DefaultScheduleEvent
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The appointment. */
	private Appointment appointment;

	/** The type event. */
	private String typeEvent;

	/** The begin hour. */
	private Short beginHour;

	/** The begin minutes. */
	private Integer beginMinutes;

	/** The controller. */
	private User controller;

	/**
	 * Instantiates a new appointment event.
	 *
	 * @param string
	 *           the string
	 * @param object
	 *           the object
	 * @param object2
	 *           the object2
	 */
	public AppointmentEvent(final String string, final Date object, final Date object2)
	{
		super(string, object, object2);
	}

	/**
	 * Instantiates a new appointment event.
	 */
	public AppointmentEvent()
	{
	}

	/**
	 * Gets the appointment.
	 *
	 * @return the appointment
	 */
	public Appointment getAppointment()
	{
		return appointment;
	}

	/**
	 * Sets the appointment.
	 *
	 * @param appointment
	 *           the new appointment
	 */
	public void setAppointment(final Appointment appointment)
	{
		this.appointment = appointment;
	}

	/**
	 * Gets the type event.
	 *
	 * @return the type event
	 */
	public String getTypeEvent()
	{
		return typeEvent;
	}

	/**
	 * Sets the type event.
	 *
	 * @param typeEvent
	 *           the new type event
	 */
	public void setTypeEvent(final String typeEvent)
	{
		this.typeEvent = typeEvent;
	}

	/**
	 * Gets the begin hour.
	 *
	 * @return the begin hour
	 */
	public Short getBeginHour()
	{
		return beginHour;
	}

	/**
	 * Sets the begin hour.
	 *
	 * @param beginHour
	 *           the new begin hour
	 */
	public void setBeginHour(final Short beginHour)
	{
		this.beginHour = beginHour;
	}

	/**
	 * Gets the begin minutes.
	 *
	 * @return the begin minutes
	 */
	public Integer getBeginMinutes()
	{
		return beginMinutes;
	}

	/**
	 * Sets the begin minutes.
	 *
	 * @param beginMinutes
	 *           the new begin minutes
	 */
	public void setBeginMinutes(final Integer beginMinutes)
	{
		this.beginMinutes = beginMinutes;
	}

	/**
	 * Gets the controller.
	 *
	 * @return the controller
	 */
	public User getController()
	{
		return controller;
	}

	/**
	 * Sets the controller.
	 *
	 * @param controller
	 *           the new controller
	 */
	public void setController(User controller)
	{
		this.controller = controller;
	}




}
