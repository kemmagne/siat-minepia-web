package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.core.ct.model.TradeBalanceConfig;
import org.guce.siat.core.ct.service.TradeBalanceConfigService;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class TradeBalanceConfigController.
 */
@ManagedBean(name = "tradeBalanceConfigController")
@ViewScoped
public class TradeBalanceConfigController implements Serializable, Validator
{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(TradeBalanceConfigController.class);
	/** The page url. */
	private String pageUrl;

	/** The trade balance config. */
	private TradeBalanceConfig tradeBalanceConfig = new TradeBalanceConfig();

	/** The trade balance configs. */
	private List<TradeBalanceConfig> tradeBalanceConfigs = new ArrayList<TradeBalanceConfig>();

	/** The Constant PERSISTENCE_ERROR_OCCURED. */
	protected static final String PERSISTENCE_ERROR_OCCURED = "PersistenceErrorOccured";
	/** The Constant LOCAL_BUNDLE_NAME. */
	protected static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";

	/**
	 * Gets the page url.
	 *
	 * @return the page url
	 */
	public String getPageUrl()
	{
		return pageUrl;
	}



	/**
	 * @param pageUrl
	 */
	public void setPageUrl(final String pageUrl)
	{
		this.pageUrl = pageUrl;
	}

	/**
	 * Gets the trade balance config.
	 *
	 * @return the trade balance config
	 */
	public TradeBalanceConfig getTradeBalanceConfig()
	{
		return tradeBalanceConfig;
	}

	/**
	 * Sets the trade balance config.
	 *
	 * @param tradeBalanceConfig
	 *           the new trade balance config
	 */
	public void setTradeBalanceConfig(final TradeBalanceConfig tradeBalanceConfig)
	{
		this.tradeBalanceConfig = tradeBalanceConfig;
	}

	/**
	 * Gets the trade balance configs.
	 *
	 * @return the trade balance configs
	 */
	public List<TradeBalanceConfig> getTradeBalanceConfigs()
	{
		return tradeBalanceConfigs;
	}

	/**
	 * Sets the trade balance configs.
	 *
	 * @param tradeBalanceConfigs
	 *           the new trade balance configs
	 */
	public void setTradeBalanceConfigs(final List<TradeBalanceConfig> tradeBalanceConfigs)
	{
		this.tradeBalanceConfigs = tradeBalanceConfigs;
	}

	/** The trade balance config service. */
	@ManagedProperty(value = "#{tradeBalanceConfigService}")
	private TradeBalanceConfigService tradeBalanceConfigService;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, DelegationController.class.getName());
		}

		setPageUrl(ControllerConstants.Pages.BO.TRADE_BALANCE_CONFIG_INDEX_PAGE);
		setTradeBalanceConfigService(tradeBalanceConfigService);
		refreshTradeBalanceConfig();
	}

	/**
	 * Gets the trade balance config service.
	 *
	 * @return the trade balance config service
	 */
	public TradeBalanceConfigService getTradeBalanceConfigService()
	{
		return tradeBalanceConfigService;
	}

	/**
	 * Sets the trade balance config service.
	 *
	 * @param tradeBalanceConfigService
	 *           the new trade balance config service
	 */
	public void setTradeBalanceConfigService(final TradeBalanceConfigService tradeBalanceConfigService)
	{
		this.tradeBalanceConfigService = tradeBalanceConfigService;
	}

	/**
	 * Go to page.
	 */
	public void goToPage()
	{
		try
		{

			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, pageUrl));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{

			java.util.logging.Logger.getLogger(this.getClass().getName())
					.logp(Level.SEVERE, this.getClass().getName(),
							Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(),
							ioe.getClass().getName() + ioe.getMessage());
		}
	}



	/**
	 * Refresh trade balance config.
	 */
	public void refreshTradeBalanceConfig()
	{
		tradeBalanceConfig = new TradeBalanceConfig();
		try
		{
			tradeBalanceConfigs = tradeBalanceConfigService.findAllTradeBalanceConfig();
		}
		catch (final Exception ex)
		{
			java.util.logging.Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(),
					Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(), ex.getClass().getName() + ex.getMessage());
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));

		}
	}

	/**
	 * Gets the current locale.
	 *
	 * @return the current locale
	 */
	public Locale getCurrentLocale()
	{
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	/**
	 * Prepare create.
	 */
	public void prepareCreate()
	{
		tradeBalanceConfig = new TradeBalanceConfig();
		tradeBalanceConfig.setCount(true);
	}


	/**
	 * Validate action.
	 */

	public void doEdit()
	{
		try
		{
			tradeBalanceConfigService.update(tradeBalanceConfig);
			refreshTradeBalanceConfig();


		}
		catch (final Exception ex)
		{
			java.util.logging.Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(),
					Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(), ex.getClass().getName() + ex.getMessage());
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}
	}

	/**
	 * Do create.
	 */


	public void doCreate()
	{



	try{
			tradeBalanceConfigService.save(tradeBalanceConfig);
		refreshTradeBalanceConfig();

			}
				catch (final Exception ex)
				{

					java.util.logging.Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(),
							Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(), ex.getClass().getName() + ex.getMessage());
					JsfUtil.addErrorMessage(ex,
							ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
				}
	}

	/**
	 * Do delete.
	 */
	public void doDelete()
	{
		try
		{
			tradeBalanceConfigService.delete(tradeBalanceConfig);
			refreshTradeBalanceConfig();

		}
		catch (final Exception ex)
		{
			java.util.logging.Logger.getLogger(this.getClass().getName()).logp(Level.SEVERE, this.getClass().getName(),
					Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(), ex.getClass().getName() + ex.getMessage());
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}
	}



	@Override
	public void validate(final FacesContext context, final UIComponent component, final Object value) throws ValidatorException
	{

		final FacesContext fc = FacesContext.getCurrentInstance();
		final Map<String, String> params = fc.getExternalContext().getRequestParameterMap();


		if (!tradeBalanceConfigService.existNSHCodeInItem((String) value))
		{
			final String message = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					"TradeBalanceConfigValidator_nsh");

			final FacesMessage msg = new FacesMessage(StringUtils.EMPTY, message);
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
		else
		{


			Boolean result = null;
			String type;
			if (component.getId().equals("trade_code_c"))
			{
				type = params.get("tradeCreateForm:trade_Type_input");
				if (StringUtils.isNotBlank(type))
				{
					result = tradeBalanceConfigService.existTradeBalanceConfigByCode((String) value, null, type);
				}
			}
			else
			{
				type = params.get("tradeEditForm:trade_Type_input");
				if (StringUtils.isNotBlank(type))
				{
					result = tradeBalanceConfigService.existTradeBalanceConfigByCode((String) value, tradeBalanceConfig.getId(), type);
				}
			}

			if (result != null && result)
			{
				final String message = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						"TradeBalanceConfigValidator");

				final FacesMessage msg = new FacesMessage(StringUtils.EMPTY, (String) value + " " + message);
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}


		}

	}

}
