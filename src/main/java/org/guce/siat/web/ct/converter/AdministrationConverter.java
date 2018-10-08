package org.guce.siat.web.ct.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.service.AdministrationService;

import org.guce.siat.web.ct.controller.util.JsfUtil;

/**
 * The Class EntityConverter.
 */
@ManagedBean(name = "administrationConverter")
@RequestScoped
public class AdministrationConverter implements Converter {

	/**
	 * The entity service.
	 */
	@ManagedProperty(value = "#{administrationService}")
	private AdministrationService administrationService;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String value) {
		if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
			return null;
		}
		return this.administrationService.find(getKey(value));
	}

	/**
	 * Gets the key.
	 *
	 * @param value the value
	 * @return the key
	 */
	Long getKey(final String value) {
		Long key;
		key = Long.valueOf(value);
		return key;
	}

	/**
	 * Gets the string key.
	 *
	 * @param value the value
	 * @return the string key
	 */
	String getStringKey(final Long value) {
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
	public String getAsString(final FacesContext facesContext, final UIComponent component, final Object object) {
		if (object == null || (object instanceof String && ((String) object).length() == 0)) {
			return null;
		}
		if (object instanceof Administration) {
			final Administration o = (Administration) object;
			return getStringKey(o.getId());
		} else {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
					new Object[]{object, object.getClass().getName(), Administration.class.getName()});
			return null;
		}
	}

	/**
	 * Gets the administration service.
	 *
	 * @return the administrationService
	 */
	public AdministrationService getAdministrationService() {
		return administrationService;
	}

	/**
	 * Sets the administration service.
	 *
	 * @param administrationService the administrationService to set
	 */
	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}
}
