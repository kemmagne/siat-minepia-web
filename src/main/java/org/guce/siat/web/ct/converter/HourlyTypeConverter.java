package org.guce.siat.web.ct.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.guce.siat.common.model.HourlyType;
import org.guce.siat.common.service.HourlyTypeService;
import org.guce.siat.web.ct.controller.util.JsfUtil;


/**
 * The Class HourlyTypeConverter.
 */
@ManagedBean
@RequestScoped
public class HourlyTypeConverter implements Converter
{

	/** The hourly type service. */
	@ManagedProperty(value = "#{hourlyTypeService}")
	private HourlyTypeService hourlyTypeService;

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
		return this.hourlyTypeService.find(getKey(value));
	}

	/**
	 * Gets the key.
	 *
	 * @param value
	 *           the value
	 * @return the key
	 */
	Long getKey(final String value)
	{
		Long key;
		key = Long.valueOf(value);
		return key;
	}

	/**
	 * Gets the string key.
	 *
	 * @param value
	 *           the value
	 * @return the string key
	 */
	String getStringKey(final Long value)
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
		if (object instanceof HourlyType)
		{
			final HourlyType hourlyType = (HourlyType) object;
			return getStringKey(hourlyType.getId());
		}
		else
		{
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
					new Object[]
					{ object, object.getClass().getName(), HourlyType.class.getName() });
			return null;
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




}
