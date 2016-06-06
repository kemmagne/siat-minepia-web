package org.guce.siat.web.ct.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.guce.siat.common.model.Service;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.web.ct.controller.util.JsfUtil;


/**
 * The Class ServiceConverter.
 */
@ManagedBean(name = "serviceConverter")
@RequestScoped
public class ServiceConverter implements Converter
{

	/** The service service. */
	@ManagedProperty(value = "#{serviceService}")
	private ServiceService serviceService;



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
		return this.serviceService.find(getKey(value));
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
		if (object instanceof Service)
		{
			final Service o = (Service) object;
			return getStringKey(o.getId());
		}
		else
		{
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
					new Object[]
					{ object, object.getClass().getName(), Service.class.getName() });
			return null;
		}
	}

	/**
	 * Gets the service service.
	 *
	 * @return the service service
	 */
	public ServiceService getServiceService()
	{
		return serviceService;
	}

	/**
	 * Sets the service service.
	 *
	 * @param serviceService
	 *           the new service service
	 */
	public void setServiceService(final ServiceService serviceService)
	{
		this.serviceService = serviceService;
	}

}
