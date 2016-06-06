package org.guce.siat.web.common.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class ServletUtils.
 */
public final class ServletUtils
{
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ServletUtils.class);

	/**
	 * Instantiates a new servlet utils.
	 */
	private ServletUtils()
	{

	}

	/**
	 * Sets the cookies.
	 *
	 * @param name
	 *           the name
	 * @param value
	 *           the value
	 */
	public static void setCookies(final String name, final String value)
	{
		final FacesContext facesContext = FacesContext.getCurrentInstance();

		final HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

		final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

		final Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(3600);
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);
	}

	/**
	 * Gets the cookie.
	 *
	 * @param name
	 *           the name
	 * @return the cookie
	 */
	public static Cookie getCookie(final String name)
	{
		final FacesContext facesContext = FacesContext.getCurrentInstance();

		final HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

		final Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0)
		{
			for (int i = 0; i < cookies.length; i++)
			{
				if (cookies[i].getName().equals(name))
				{
					return cookies[i];
				}
			}
		}
		else
		{
			LOG.info("Cannot find any cookie");
		}

		return null;
	}

	/**
	 * Gets the cookie value.
	 *
	 * @param name
	 *           the name
	 * @return the cookie value
	 */
	public static String getCookieValue(final String name)
	{
		final FacesContext facesContext = FacesContext.getCurrentInstance();

		final HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

		final Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0)
		{
			for (int i = 0; i < cookies.length; i++)
			{
				if (cookies[i].getName().equals(name))
				{
					return cookies[i].getValue();
				}
			}
		}
		else
		{
			LOG.info("Cannot find any cookie");
		}

		return null;
	}
}
