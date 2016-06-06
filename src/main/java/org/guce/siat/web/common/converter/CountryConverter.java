package org.guce.siat.web.common.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.guce.siat.common.model.Country;
import org.guce.siat.common.service.CountryService;
import org.guce.siat.web.ct.controller.util.JsfUtil;


/**
 * The Class CountryConverter.
 */
@ManagedBean(name = "countryConverter")
@RequestScoped
public class CountryConverter implements Converter
{

	/** The country service. */
	@ManagedProperty(value = "#{countryService}")
	private CountryService countryService;

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
		return this.countryService.find(value);
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
		if (object instanceof Country)
		{
			final Country o = (Country) object;
			return o.getCountryIdAlpha2();
		}
		else
		{
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
					new Object[]
					{ object, object.getClass().getName(), Country.class.getName() });
			return null;
		}
	}

	/**
	 * Gets the country service.
	 *
	 * @return the countryService
	 */
	public CountryService getCountryService()
	{
		return countryService;
	}

	/**
	 * Sets the country service.
	 *
	 * @param countryService
	 *           the countryService to set
	 */
	public void setCountryService(final CountryService countryService)
	{
		this.countryService = countryService;
	}


}
