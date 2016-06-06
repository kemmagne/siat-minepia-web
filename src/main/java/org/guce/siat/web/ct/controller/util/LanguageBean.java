package org.guce.siat.web.ct.controller.util;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.ct.controller.util.enums.LocaleValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class LanguageBean.
 */
@ManagedBean(name = "language")
@SessionScoped
public class LanguageBean implements Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = 2987023441861264532L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(LanguageBean.class);

	/** The Constant LOCALE_SESSION_ATTRIBUTE. */
	private static final String LOCALE_SESSION_ATTRIBUTE = "locale";

	/** The locale code. */
	private String localeCode;

	/** The locale. */
	private Locale locale;

	/** The countries. */
	private static Map<String, Object> languages;

	static
	{
		languages = new LinkedHashMap<String, Object>();
		languages.put(Locale.getDefault().toString().contains(Locale.FRENCH.toString()) ? "Français" : "French", Locale.FRENCH);
		languages.put(Locale.getDefault().toString().contains(Locale.FRENCH.toString()) ? "Anglais" : "English", Locale.ENGLISH);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, LanguageBean.class.getName());
		}
		final FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session;
		if (context != null)
		{
			session = (HttpSession) context.getExternalContext().getSession(true);
			locale = (Locale) session.getAttribute(LOCALE_SESSION_ATTRIBUTE);
			if (locale == null)
			{
				locale = Locale.FRENCH;
			}
			localeCode = Locale.FRENCH.equals(locale) ? LocaleValues.FRENCH.getCode() : LocaleValues.ENGLISH.getCode();
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		}
	}

	/**
	 * Gets the countries in map.
	 *
	 * @return the countries in map
	 */
	public Map<String, Object> getlanguagesInMap()
	{
		return languages;
	}

	/**
	 * Country locale code changed.
	 *
	 * @param event
	 *           the event
	 */
	public void countryLocaleCodeChanged(final AjaxBehaviorEvent event)
	{
		final String newLocaleValue = (String) ((UIOutput) event.getSource()).getValue();
		locale = new Locale(newLocaleValue);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

	/**
	 * Locale fr changed.
	 */
	public void localeFRChanged()
	{
		locale = Locale.FRENCH;
		localeCode = LocaleValues.FRENCH.getCode();
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);

		final FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session;
		if (context != null)
		{
			session = (HttpSession) context.getExternalContext().getSession(true);
			session.setAttribute(LOCALE_SESSION_ATTRIBUTE, Locale.FRENCH);
		}
	}


	/**
	 * Locale uk changed.
	 */
	public void localeUKChanged()
	{
		locale = Locale.ENGLISH;
		localeCode = LocaleValues.ENGLISH.getCode();
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);

		final FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session;
		if (context != null)
		{
			session = (HttpSession) context.getExternalContext().getSession(true);
			session.setAttribute(LOCALE_SESSION_ATTRIBUTE, Locale.ENGLISH);
		}
	}

	/**
	 * Gets the language options.
	 *
	 * @return the language options
	 */
	public SelectItem[] getLanguageOptions()
	{
		final SelectItem[] languageOptions = new SelectItem[Constants.TWO];

		languageOptions[0] = new SelectItem("FR", "French");
		languageOptions[1] = new SelectItem("EN", "English");

		return languageOptions;
	}

	/**
	 * Gets the locale code.
	 *
	 * @return the locale code
	 */
	public String getLocaleCode()
	{
		return localeCode;
	}

	/**
	 * Sets the locale code.
	 *
	 * @param localeCode
	 *           the new locale code
	 */
	public void setLocaleCode(final String localeCode)
	{
		this.localeCode = localeCode;
	}

	/**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
	public Locale getLocale()
	{
		return locale;
	}

	/**
	 * Sets the locale.
	 *
	 * @param locale
	 *           the locale to set
	 */
	public void setLocale(final Locale locale)
	{
		this.locale = locale;
	}

}
