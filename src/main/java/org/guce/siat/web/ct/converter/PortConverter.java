package org.guce.siat.web.ct.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.guce.siat.common.model.Port;
import org.guce.siat.common.service.PortService;
import org.guce.siat.web.ct.controller.util.JsfUtil;




/**
 * The Class PortConverter.
 */
@ManagedBean(name = "portConverter")
@RequestScoped
public class PortConverter implements Converter
{

	/** The port service. */
	@ManagedProperty(value = "#{portService}")
	private PortService portService;

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
		return this.portService.find(value);
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
		if (object instanceof Port)
		{
			final Port o = (Port) object;
			return o.getLocationCode();
		}
		else
		{
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
					new Object[]
					{ object, object.getClass().getName(), Port.class.getName() });
			return null;
		}
	}

	/**
	 * Gets the port service.
	 *
	 * @return the portService
	 */
	public PortService getPortService()
	{
		return portService;
	}

	/**
	 * Sets the port service.
	 *
	 * @param portService
	 *           the portService to set
	 */
	public void setPortService(final PortService portService)
	{
		this.portService = portService;
	}


}
