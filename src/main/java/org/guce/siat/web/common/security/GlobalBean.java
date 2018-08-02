package org.guce.siat.web.common.security;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.Entity;
import org.guce.siat.common.model.Ministry;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.Params;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.ApplicationPropretiesService;
import org.guce.siat.common.service.ParamsService;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.gr.util.enums.ParamsNames;


/**
 * The Class GlobalBean.
 */
@ManagedBean(name = "globalBean")
@SessionScoped
public class GlobalBean implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2932583118833780928L;

	/** The logged user. */
	protected User loggedUser;

	/** The authorities. */
	private List<Authority> authorities;

	/** The current administration. */
	protected Administration currentAdministration;

	/** The current ministry. */
	protected Ministry currentMinistry;

	/** The current organism. */
	protected Organism currentOrganism;

	/** The current sub department. */
	protected SubDepartment currentSubDepartment;

	/** The current service. */
	protected Service currentService;

	/** The current entity. */
	protected Entity currentEntity;

	/** The app version. */
	private String appVersion;

	/** The app env. */
	private String appEnv;

	/** The application propreties service. */
	@ManagedProperty(value = "#{applicationPropretiesService}")
	private ApplicationPropretiesService applicationPropretiesService;

	/** The params service. */
	@ManagedProperty(value = "#{paramsService}")
	private ParamsService paramsService;

	/**
	 * Checks if is certificat authentifaction.
	 *
	 * @return true, if is certificat authentifaction
	 */
	public boolean getCertificatAuthentifaction()
	{
		final Params param = paramsService.findParamsByName(ParamsNames.TOKEN_AUTHENTIFICATION.getCode());
		return (param != null && !param.getValue().equals("0"));
	}

	/**
	 * Go to home page.
	 */
	public void goToHomePage()
	{
		try
		{
			final FacesContext facesContext = FacesContext.getCurrentInstance();
			final ExternalContext extContext = facesContext.getExternalContext();
			final String url = extContext.encodeActionURL(facesContext.getApplication().getViewHandler()
					.getActionURL(facesContext, ControllerConstants.Pages.INDEX_PAGE));
			extContext.redirect(url);
		}
		catch (final IOException ex)
		{
			JsfUtil.addErrorMessage(ex, ex.getMessage());
		}
	}

	/**
	 * Gets the logged user.
	 *
	 * @return the logged user
	 */
	public User getLoggedUser()
	{
		if (loggedUser == null)
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				loggedUser = (User) session.getAttribute("loggedUser");
			}
		}
		return loggedUser;
	}

	/**
	 * Gets the granted authorities.
	 *
	 * @return the grantedAuthorities
	 */
	@SuppressWarnings("unchecked")
	public List<Authority> getAuthorities()
	{
		if (authorities == null)
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			Set<Authority> setAuthorities = new HashSet<Authority>();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				setAuthorities = (Set<Authority>) (session.getAttribute("authorities"));
				authorities = new ArrayList<Authority>(setAuthorities);
			}
		}
		return authorities;
	}

	/**
	 * Gets the current administration.
	 *
	 * @return the current administration
	 */
	public Administration getCurrentAdministration()
	{
		if (currentAdministration == null)
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				currentAdministration = (Administration) session.getAttribute("currentAdministration");
			}
		}
		return currentAdministration;
	}

	/**
	 * Gets the current ministry.
	 *
	 * @return the currentMinistry
	 */
	public Ministry getCurrentMinistry()
	{

		if (currentMinistry == null)
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				currentMinistry = (Ministry) session.getAttribute("currentMinistry");
			}
		}
		return currentMinistry;
	}

	/**
	 * Gets the current organism.
	 *
	 * @return the current organism
	 */
	public Organism getCurrentOrganism()
	{
		if (currentOrganism == null)
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				currentOrganism = (Organism) session.getAttribute("currentOrganism");
			}
		}
		return currentOrganism;
	}

	/**
	 * Gets the current sub department.
	 *
	 * @return the current sub department
	 */
	public SubDepartment getCurrentSubDepartment()
	{
		if (currentSubDepartment == null)
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				currentSubDepartment = (SubDepartment) session.getAttribute("currentSubDepartment");
			}
		}
		return currentSubDepartment;
	}

	/**
	 * Gets the current service.
	 *
	 * @return the current service
	 */
	public Service getCurrentService()
	{
		if (currentService == null)
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				currentService = (Service) session.getAttribute("currentService");
			}
		}
		return currentService;
	}

	/**
	 * Gets the current entity.
	 *
	 * @return the current entity
	 */
	public Entity getCurrentEntity()
	{
		if (currentEntity == null)
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				currentEntity = (Entity) session.getAttribute("currentEntity");
			}
		}
		return currentEntity;
	}

	/**
	 * Sets the authorities.
	 *
	 * @param authorities
	 *           the authorities to set
	 */
	public void setAuthorities(final List<Authority> authorities)
	{
		this.authorities = authorities;
	}

	/**
	 * Sets the logged user.
	 *
	 * @param loggedUser
	 *           the new logged user
	 */
	public void setLoggedUser(final User loggedUser)
	{
		this.loggedUser = loggedUser;
	}

	/**
	 * Sets the current administration.
	 *
	 * @param currentAdministration
	 *           the currentAdministration to set
	 */
	public void setCurrentAdministration(final Administration currentAdministration)
	{
		this.currentAdministration = currentAdministration;
	}

	/**
	 * Sets the current ministry.
	 *
	 * @param currentMinistry
	 *           the currentMinistry to set
	 */
	public void setCurrentMinistry(final Ministry currentMinistry)
	{
		this.currentMinistry = currentMinistry;
	}

	/**
	 * Sets the current organism.
	 *
	 * @param currentOrganism
	 *           the new current organism
	 */
	public void setCurrentOrganism(final Organism currentOrganism)
	{
		this.currentOrganism = currentOrganism;
	}

	/**
	 * Sets the current sub department.
	 *
	 * @param currentSubDepartment
	 *           the currentSubDepartment to set
	 */
	public void setCurrentSubDepartment(final SubDepartment currentSubDepartment)
	{
		this.currentSubDepartment = currentSubDepartment;
	}

	/**
	 * Sets the current service.
	 *
	 * @param currentService
	 *           the new current service
	 */
	public void setCurrentService(final Service currentService)
	{
		this.currentService = currentService;
	}

	/**
	 * Sets the current entity.
	 *
	 * @param currentEntity
	 *           the currentEntity to set
	 */
	public void setCurrentEntity(final Entity currentEntity)
	{
		this.currentEntity = currentEntity;
	}

	/**
	 * Gets the application propreties service.
	 *
	 * @return the application propreties service
	 */
	public ApplicationPropretiesService getApplicationPropretiesService()
	{
		return applicationPropretiesService;
	}

	/**
	 * Sets the application propreties service.
	 *
	 * @param applicationPropretiesService
	 *           the new application propreties service
	 */
	public void setApplicationPropretiesService(final ApplicationPropretiesService applicationPropretiesService)
	{
		this.applicationPropretiesService = applicationPropretiesService;
	}

	/**
	 * Gets the app version.
	 *
	 * @return the app version
	 */
	public String getAppVersion()
	{
		if (appVersion == null)
		{
			appVersion = applicationPropretiesService.getAppVersion();
		}
		return appVersion;
	}

	/**
	 * Sets the app version.
	 *
	 * @param appVersion
	 *           the new app version
	 */
	public void setAppVersion(final String appVersion)
	{
		this.appVersion = appVersion;
	}

	/**
	 * Gets the app env.
	 *
	 * @return the app env
	 */
	public String getAppEnv()
	{
		if (appEnv == null)
		{
			appEnv = applicationPropretiesService.getAppEnv();
		}
		return appEnv;
	}

	/**
	 * Gets the params service.
	 *
	 * @return the params service
	 */
	public ParamsService getParamsService()
	{
		return paramsService;
	}

	/**
	 * Sets the params service.
	 *
	 * @param paramsService
	 *           the new params service
	 */
	public void setParamsService(final ParamsService paramsService)
	{
		this.paramsService = paramsService;
	}

	/**
	 * Sets the app env.
	 *
	 * @param appEnv
	 *           the new app env
	 */
	public void setAppEnv(final String appEnv)
	{
		this.appEnv = appEnv;
        }

        public boolean loggedUserHasAuthority(String authorityConstantsCode) {
                return getLoggedUser().getAuthoritiesList().contains(authorityConstantsCode);
        }
}
