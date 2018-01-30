package org.guce.siat.web.common.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.guce.siat.common.model.User;
import org.guce.siat.common.service.MailService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class ResetPasswordBean.
 */
@ManagedBean(name = "resetPasswordBean")
@SessionScoped
public class ResetPasswordBean extends AbstractController<Serializable>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9175673210940650146L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ResetPasswordBean.class);

	/** The Constant LOCAL_BUNDLE_NAME. */
	private static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";

	/** The Constant RESET_SEND_MAIL_MESSAGE. */
	private static final String RESET_SEND_MAIL_MESSAGE = "reset_send_mail_message";

	/** The Constant RESET_USER_NOT_EXIST. */
	private static final String RESET_USER_NOT_EXIST = "reset_user_not_exist";

	/** The Constant RESET_SEND_MAIL_TITLE. */
	private static final String RESET_SEND_MAIL_TITLE = "reset_send_mail_title";

	/** The Constant RESET_MAIL_SUBJECT. */
	private static final String RESET_MAIL_SUBJECT = "reset_mail_subject";

	/** The Constant EMAIL_BODY_PASSWORD. */
	private static final String EMAIL_BODY_PASSWORD = "emailBodyPassword";

	/** The bundle. */
	private final ResourceBundle bundle;

	/** The context. */
	FacesContext context;

	/** The login. */
	private String login;

	/** The user service. */
	@ManagedProperty(value = "#{userService}")
	private UserService userService;

	/** The mail service. */
	@ManagedProperty(value = "#{mailService}")
	private MailService mailService;

	/** The login status. */
	private Boolean loginStatus = true;

	/**
	 * Instantiates a new reset password bean.
	 */

	public ResetPasswordBean()
	{
		context = FacesContext.getCurrentInstance();
		bundle = context.getApplication().getResourceBundle(context, "bundle");
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, ResetPasswordBean.class.getName());
		}
	}

	/**
	 * Change password and email.
	 */
	public void changePassword()
	{

		final String radomPassword = JsfUtil.generateRandomPassword();
		final User userToChange = userService.findByLogin(login);

		if (userToChange == null)
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(RESET_USER_NOT_EXIST));
		}
		else
		{
			userToChange.setPassword(radomPassword);
			userToChange.setFirstLogin(Boolean.valueOf(true));
			userService.updateUser(userToChange);

			final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(RESET_SEND_MAIL_MESSAGE);
			final String herader = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(RESET_SEND_MAIL_TITLE);

			JsfUtil.addSuccessWithTwoMessage(herader, msg);

			final String subject = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(RESET_MAIL_SUBJECT);

			final String templateFileName = localeFileTemplate(EMAIL_BODY_PASSWORD, getCurrentLocale().getLanguage());
			Map<String, String> map = new HashMap<String, String>();
			map = prepareMap(subject, mailService.getFromValue(), userToChange, radomPassword, templateFileName);
			mailService.sendMail(map);
		}



	}


	/**
	 * Change login status.
	 *
	 * @param value
	 *           the value
	 */
	public void changeLoginStatus(final Boolean value)
	{
		loginStatus = value;
	}

	/**
	 * Gets the current locale.
	 *
	 * @return the current locale
	 */
	@Override
	public Locale getCurrentLocale()
	{
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	/**
	 * Gets the login.
	 *
	 * @return the login
	 */
	public String getLogin()
	{
		return login;
	}

	/**
	 * Sets the login.
	 *
	 * @param login
	 *           the new login
	 */
	public void setLogin(final String login)
	{
		this.login = login;
	}

	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public FacesContext getContext()
	{
		return context;
	}

	/**
	 * Sets the context.
	 *
	 * @param context
	 *           the new context
	 */
	public void setContext(final FacesContext context)
	{
		this.context = context;
	}

	/**
	 * Gets the user service.
	 *
	 * @return the user service
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * Sets the user service.
	 *
	 * @param userService
	 *           the new user service
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * Gets the bundle.
	 *
	 * @return the bundle
	 */
	public ResourceBundle getBundle()
	{
		return bundle;
	}


	/**
	 * Gets the login status.
	 *
	 * @return the login status
	 */
	public Boolean getLoginStatus()
	{
		return loginStatus;
	}

	/**
	 * Sets the login status.
	 *
	 * @param loginStatus
	 *           the new login status
	 */
	public void setLoginStatus(final Boolean loginStatus)
	{
		this.loginStatus = loginStatus;
	}

	/**
	 * Gets the mail service.
	 *
	 * @return the mail service
	 */
	public MailService getMailService()
	{
		return mailService;
	}

	/**
	 * Sets the mail service.
	 *
	 * @param mailService
	 *           the new mail service
	 */
	public void setMailService(final MailService mailService)
	{
		this.mailService = mailService;
	}

	}
