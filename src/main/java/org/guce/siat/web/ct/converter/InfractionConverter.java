package org.guce.siat.web.ct.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.guce.siat.core.ct.model.Infraction;
import org.guce.siat.core.gr.service.InfractionService;
import org.guce.siat.web.ct.controller.util.JsfUtil;



/**
 * The Class InfractionConverter.
 */
@ManagedBean(name = "infractionConverter")
@RequestScoped
public class InfractionConverter implements Converter
{

	/** The authority service. */
	@ManagedProperty(value = "#{infractionService}")
	private InfractionService infractionService;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String value)
	{
		if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value))
		{
			return null;
		}
		return this.infractionService.find(getKey(value));
	}

	/**
	 * Gets the key.
	 *
	 * @param value
	 *           the value
	 * @return the key
	 */
	String getKey(final String value)
	{
		String key;
		key = String.valueOf(value);
		return key;
	}

	/**
	 * Gets the string key.
	 *
	 * @param value
	 *           the value
	 * @return the string key
	 */
	String getStringKey(final String value)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(value);
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(final FacesContext facesContext, final UIComponent component, final Object object)
	{
		if (object == null || (object instanceof String && ((String) object).length() == 0))
		{
			return null;
		}
		if (object instanceof Infraction)
		{
			final Infraction o = (Infraction) object;
			return getStringKey(o.getinfractionCode());
		}
		else
		{
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
					new Object[]
					{ object, object.getClass().getName(), Infraction.class.getName() });
			return null;
		}
	}

	/**
	 * Gets the infraction service.
	 *
	 * @return the infraction service
	 */
	public InfractionService getInfractionService()
	{
		return infractionService;
	}

	/**
	 * Sets the infraction service.
	 *
	 * @param infractionService
	 *           the new infraction service
	 */
	public void setInfractionService(final InfractionService infractionService)
	{
		this.infractionService = infractionService;
	}

}
