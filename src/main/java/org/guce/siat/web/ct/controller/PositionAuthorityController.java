package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.PositionAuthority;
import org.guce.siat.common.service.AuthorityService;
import org.guce.siat.common.service.PositionAuthorityService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.PositionType;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.guce.siat.web.ct.data.PositionAuthorityData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class PositionAuthorityController.
 */
@ManagedBean(name = "positionAuthorityController")
@SessionScoped
public class PositionAuthorityController extends AbstractController<PositionAuthority>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1275764248998058644L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(PositionAuthorityController.class);

	/** The authority service. */
	@ManagedProperty(value = "#{authorityService}")
	private AuthorityService authorityService;

	/** The positionAuthority service. */
	@ManagedProperty(value = "#{positionAuthorityService}")
	private PositionAuthorityService positionAuthorityService;

	/** The position list. */
	private List<PositionType> positionList;

	/** The position authorities. */
	private List<Authority> authorities;

	/** The authorities per positions. */
	private List<List<PositionAuthorityData>> authoritiesPerPositions;


	/**
	 * Instantiates a new organism controller.
	 */
	public PositionAuthorityController()
	{
		super(PositionAuthority.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, PositionAuthorityController.class.getName());
		}
		super.setService(positionAuthorityService);
		super.setPageUrl(ControllerConstants.Pages.BO.POSITION_AUTHORITY_INDEX_PAGE);
		getItems();
		getAuthorities();
		initPositionList();

	}


	/**
	 * Submit.
	 */
	@Override
	public void edit()
	{

		delete();

		final List<PositionAuthority> positionAuthourities = new ArrayList<PositionAuthority>();

		for (final List<PositionAuthorityData> authorityPerPosition : authoritiesPerPositions)
		{

			for (final PositionAuthorityData authorityPerPositionx : authorityPerPosition)
			{
				if (authorityPerPositionx.isChecked())
				{
					final PositionAuthority positionAuthority = new PositionAuthority();
					positionAuthority.setAuthority(authorityPerPositionx.getAuthority());
					positionAuthority.setPositionType(authorityPerPositionx.getPosition());
					positionAuthourities.add(positionAuthority);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(positionAuthourities))
		{
			positionAuthorityService.saveOrUpdateList(positionAuthourities);

		}
		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				PositionAuthority.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
		JsfUtil.addSuccessMessage(msg);
	}

	/**
	 * Gets the authorities.
	 *
	 * @return the authorities
	 */
	public List<Authority> getAuthorities()
	{
		try
		{
			if (authorities == null)
			{
				authorities = authorityService.findAll();
			}
		}
		catch (final Exception ex)
		{
			LOG.error(null, ex);
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}
		return authorities;
	}


	/**
	 * Gets the authorities per positions.
	 *
	 * @return the authorities per positions
	 */
	public List<List<PositionAuthorityData>> getAuthoritiesPerPositions()
	{
		if (authoritiesPerPositions == null)
		{
			authoritiesPerPositions = new ArrayList<List<PositionAuthorityData>>();

			for (final PositionType position : positionList)
			{
				final List<PositionAuthorityData> innerList = new ArrayList<PositionAuthorityData>();
				if (CollectionUtils.isNotEmpty(items))
				{
					for (final Authority authority : authorities)
					{
						boolean matched = false;
						for (final PositionAuthority positionAuthority : items)
						{
							if (positionAuthority.getPositionType().equals(position)
									&& positionAuthority.getAuthority().getId().equals(authority.getId()))

							{
								innerList.add(new PositionAuthorityData(authority, position, true));
								matched = true;
								break;
							}
						}
						if (!matched)
						{
							innerList.add(new PositionAuthorityData(authority, position, false));
						}
					}
				}
				authoritiesPerPositions.add(innerList);
			}
		}
		return authoritiesPerPositions;
	}

	/**
	 * Gets the authority service.
	 *
	 * @return the authority service
	 */
	public AuthorityService getAuthorityService()
	{
		return authorityService;
	}

	/**
	 * Sets the authority service.
	 *
	 * @param authorityService
	 *           the new authority service
	 */
	public void setAuthorityService(final AuthorityService authorityService)
	{
		this.authorityService = authorityService;
	}

	/**
	 * Gets the position authority service.
	 *
	 * @return the position authority service
	 */
	public PositionAuthorityService getPositionAuthorityService()
	{
		return positionAuthorityService;
	}

	/**
	 * Sets the position authority service.
	 *
	 * @param positionAuthorityService
	 *           the new position authority service
	 */
	public void setPositionAuthorityService(final PositionAuthorityService positionAuthorityService)
	{
		this.positionAuthorityService = positionAuthorityService;
	}

	/**
	 * Gets the position list.
	 *
	 * @return the position list
	 */
	public List<PositionType> getPositionList()
	{
		return positionList;
	}

	/**
	 * Sets the position list.
	 *
	 * @param positionList
	 *           the new position list
	 */
	public void setPositionList(final List<PositionType> positionList)
	{
		this.positionList = positionList;
	}


	/**
	 * Sets the authorities.
	 *
	 * @param authorities
	 *           the new authorities
	 */
	public void setAuthorities(final List<Authority> authorities)
	{
		this.authorities = authorities;
	}

	/**
	 * Delete.
	 */
	@Override
	public void delete()
	{
		final List<PositionAuthority> xxx = positionAuthorityService.findAll();
		for (final PositionAuthority positionAuthoritie : xxx)
		{
			positionAuthorityService.delete(positionAuthoritie);
		}
	}

	/**
	 * Sets the authorities per positions.
	 *
	 * @param authoritiesPerPositions
	 *           the new authorities per positions
	 */
	public void setAuthoritiesPerPositions(final List<List<PositionAuthorityData>> authoritiesPerPositions)
	{
		this.authoritiesPerPositions = authoritiesPerPositions;
	}

	/**
	 * Inits the position list.
	 */
	private void initPositionList()
	{
		positionList = new ArrayList<PositionType>();
		positionList.add(PositionType.ADMINISTRATEUR);
		positionList.add(PositionType.MINISTRE);
		positionList.add(PositionType.SECRETAIRE_GENERAL);
		positionList.add(PositionType.DIRECTEUR);
		positionList.add(PositionType.SOUS_DIRECTEUR);
		positionList.add(PositionType.CHEF_SERVICE);
		positionList.add(PositionType.CHEF_BUREAU);
		positionList.add(PositionType.AGENT);
	}
}
