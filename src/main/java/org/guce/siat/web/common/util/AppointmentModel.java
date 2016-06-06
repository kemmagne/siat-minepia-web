package org.guce.siat.web.common.util;

import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;


/**
 * The Class AppointmentModel.
 */
public class AppointmentModel extends LazyScheduleModel
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.primefaces.model.DefaultScheduleModel#getEvent(java.lang.String)
	 */
	@Override
	public ScheduleEvent getEvent(final String id)
	{
		for (final ScheduleEvent event : getEvents())
		{
			if (event.getId() != null && event.getId().equals(id))
			{
				return event;
			}
		}

		return null;
	}


}
