package org.guce.siat.web.common.converter;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.guce.siat.common.model.Authority;
import org.guce.siat.common.service.AuthorityService;
import org.guce.siat.web.ct.controller.util.JsfUtil;

/**
 * The Class AuthorityConverter.
 */
@ManagedBean(name = "authorityConverter")
@RequestScoped
public class AuthorityConverter implements Converter {

    /**
     * The authority service.
     */
    @ManagedProperty(value = "#{authorityService}")
    private AuthorityService authorityService;


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
        return this.authorityService.find(getKey(value));
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
        if (object instanceof Authority) {
            final Authority o = (Authority) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
                    new Object[]{object, object.getClass().getName(), Authority.class.getName()});
            return null;
        }
    }

    /**
     * Gets the authority service.
     *
     * @return the authority service
     */
    public AuthorityService getAuthorityService() {
        return authorityService;
    }

    /**
     * Sets the authority service.
     *
     * @param authorityService the new authority service
     */
    public void setAuthorityService(final AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

}
