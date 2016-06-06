package org.guce.siat.web.ct.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.service.SubDepartmentService;
import org.guce.siat.web.ct.controller.util.JsfUtil;


/**
 * The Class SubDepartmentConverter.
 */
@ManagedBean(name = "subDepartmentConverter")
@RequestScoped
public class SubDepartmentConverter implements Converter
{

	/** The sub department service. */
	@ManagedProperty(value = "#{subDepartmentService}")
	private SubDepartmentService subDepartmentService;


	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String value)
	{
		if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value) || !(value instanceof String))
		{
			return null;
		}
		return this.subDepartmentService.find(getKey(value));
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
		if (object instanceof SubDepartment)
		{
			final SubDepartment o = (SubDepartment) object;
			return getStringKey(o.getId());
		}
		else
		{
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
					new Object[]
					{ object, object.getClass().getName(), SubDepartment.class.getName() });
			return null;
		}
	}

	/**
	 * Gets the sub department service.
	 *
	 * @return the subDepartmentService
	 */
	public SubDepartmentService getSubDepartmentService()
	{
		return subDepartmentService;
	}

	/**
	 * Sets the sub department service.
	 *
	 * @param subDepartmentService
	 *           the subDepartmentService to set
	 */
	public void setSubDepartmentService(final SubDepartmentService subDepartmentService)
	{
		this.subDepartmentService = subDepartmentService;
	}

}
