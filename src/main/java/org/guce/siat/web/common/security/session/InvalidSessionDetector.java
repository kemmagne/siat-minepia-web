package org.guce.siat.web.common.security.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.util.StringUtils;


/**
 * The Class RedirectAfterSessionInvalidated.
 */
public class InvalidSessionDetector implements InvalidSessionStrategy
{

	/** The Constant FACES_REQUEST_HEADER. */
	private static final String FACES_REQUEST_HEADER = "faces-request";

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(InvalidSessionDetector.class);


	/** The invalid session url. */
	private String invalidSessionUrl;

	/**
	 * Instantiates a new redirect after session invalidated.
	 *
	 * @param invalidSessionUrl
	 *           the invalid session url
	 */
	public InvalidSessionDetector(final String invalidSessionUrl)
	{
		super();
		this.invalidSessionUrl = invalidSessionUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.session.InvalidSessionStrategy#onInvalidSessionDetected(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void onInvalidSessionDetected(final HttpServletRequest request, final HttpServletResponse response) throws IOException,
			ServletException
	{
		final boolean ajaxRedirect = "partial/ajax".equals(request.getHeader(FACES_REQUEST_HEADER));
		if (ajaxRedirect)
		{
			final String contextPath = request.getContextPath();
			final String redirectUrl = contextPath + invalidSessionUrl;
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Session expired due to ajax request, redirecting to '{}'", redirectUrl);
			}

			final String ajaxRedirectXml = createAjaxRedirectXml(redirectUrl);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Ajax partial response to redirect: {}", ajaxRedirectXml);
			}

			response.setContentType("text/xml");
			response.getWriter().write(ajaxRedirectXml);
		}
		else
		{
			final String requestURI = getRequestUrl(request);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("starting a new session and redirect to requested url '{}'", requestURI);
			}
			request.getSession(true);
			response.sendRedirect(requestURI);
		}
	}

	/**
	 * Gets the request url.
	 *
	 * @param request
	 *           the request
	 * @return the request url
	 */
	private String getRequestUrl(final HttpServletRequest request)
	{
		final StringBuilder requestURL = new StringBuilder(request.getRequestURL());

		final String queryString = request.getQueryString();
		if (StringUtils.hasText(queryString))
		{
			requestURL.append("?").append(queryString);
		}

		return requestURL.toString();
	}

	/**
	 * Creates the ajax redirect xml.
	 *
	 * @param redirectUrl
	 *           the redirect url
	 * @return the string
	 */
	private String createAjaxRedirectXml(final String redirectUrl)
	{
		return new StringBuilder().append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<partial-response><redirect url=\"").append(redirectUrl).append("\"></redirect></partial-response>")
				.toString();
	}

	/**
	 * Sets the invalid session url.
	 *
	 * @param invalidSessionUrl
	 *           the new invalid session url
	 */
	public void setInvalidSessionUrl(final String invalidSessionUrl)
	{
		this.invalidSessionUrl = invalidSessionUrl;
	}

}
