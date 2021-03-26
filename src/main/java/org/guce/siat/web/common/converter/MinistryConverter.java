package org.guce.siat.web.common.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.guce.siat.common.model.Ministry;
import org.guce.siat.common.service.MinistryService;
import org.guce.siat.web.ct.controller.util.JsfUtil;

/**
 * The Class MinistryConverter.
 */
@ManagedBean(name = "ministryConverter")
@RequestScoped
public class MinistryConverter implements Converter {

    /**
     * The ministry service.
     */
    @ManagedProperty(value = "#{ministryService}")
    private MinistryService ministryService;

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
        return this.ministryService.find(getKey(value));
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
        if (object instanceof Ministry) {
            final Ministry o = (Ministry) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
                    new Object[]{object, object.getClass().getName(), Ministry.class.getName()});
            return null;
        }
    }

    /**
     * Gets the ministry service.
     *
     * @return the ministryService
     */
    public MinistryService getMinistryService() {
        return ministryService;
    }

    /**
     * Sets the ministry service.
     *
     * @param ministryService the ministryService to set
     */
    public void setMinistryService(final MinistryService ministryService) {
        this.ministryService = ministryService;
    }
}
