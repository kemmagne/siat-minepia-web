package org.guce.siat.web.ct.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.guce.siat.common.model.TransportType;
import org.guce.siat.common.service.TransportTypeService;
import org.guce.siat.web.ct.controller.util.JsfUtil;



/**
 * The Class TransportTypeConverter.
 */
@ManagedBean(name = "transportTypeConverter")
@RequestScoped
public class TransportTypeConverter implements Converter
{

	/** The transport type service. */
	@ManagedProperty(value = "#{transportTypeService}")
	private TransportTypeService transportTypeService;

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
		return this.transportTypeService.find(value);
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
		if (object instanceof TransportType)
		{
			final TransportType o = (TransportType) object;
			return o.getTypeMeanTransportCode();
		}
		else
		{
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
					new Object[]
					{ object, object.getClass().getName(), TransportType.class.getName() });
			return null;
		}
	}

	/**
	 * Gets the transport type service.
	 *
	 * @return the transportTypeService
	 */
	public TransportTypeService getTransportTypeService()
	{
		return transportTypeService;
	}

	/**
	 * Sets the transport type service.
	 *
	 * @param transportTypeService
	 *           the transportTypeService to set
	 */
	public void setTransportTypeService(final TransportTypeService transportTypeService)
	{
		this.transportTypeService = transportTypeService;
	}




}
