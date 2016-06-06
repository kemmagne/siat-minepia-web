package org.guce.siat.web.ct.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.OrganismService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.AuthorityConstants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class OrganismController.
 */
@ManagedBean(name = "organismController")
@SessionScoped
public class OrganismController extends AbstractController<Organism>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7049280393815079694L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(OrganismController.class);

	/** The organism service. */
	@ManagedProperty(value = "#{organismService}")
	private OrganismService organismService;

	/** The user service. */
	@ManagedProperty(value = "#{userService}")
	private UserService userService;

	/** The list admins. */
	private List<User> listAdmins;

	/**
	 * Instantiates a new organism controller.
	 */
	public OrganismController()
	{
		super(Organism.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, OrganismController.class.getName());
		}
		super.setService(organismService);
		super.setPageUrl(ControllerConstants.Pages.BO.ORGANISM_INDEX_PAGE);
	}

	@Override
	public List<Organism> getItems()
	{
		try
		{
			if (items == null)
			{
				items = organismService.findOrganismsByMinistry(getCurrentMinistry());
			}
		}
		catch (final Exception ex)
		{
			LOG.error(null, ex);
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}
		return items;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#create()
	 */
	@Override
	public void create()
	{
		selected.setDeleted(false);
		super.create();
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		listAdmins = userService.findUsersByAuthorities(AuthorityConstants.ADMIN_ORGANISME.getCode());
		this.setSelected(organismService.find(this.getSelected().getId()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
	 */
	@Override
	public void prepareCreate()
	{
		listAdmins = null;
		super.prepareCreate();
	}

	/**
	 * Gets the organism service.
	 *
	 * @return the organism service
	 */
	public OrganismService getOrganismService()
	{
		return organismService;
	}

	/**
	 * Sets the organism service.
	 *
	 * @param organismService
	 *           the new organism service
	 */
	public void setOrganismService(final OrganismService organismService)
	{
		this.organismService = organismService;
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
	 * Gets the list admins.
	 *
	 * @return the list admins
	 */
	public List<User> getListAdmins()
	{
		if (listAdmins == null || listAdmins.isEmpty())
		{
			listAdmins = userService.findAllActiveAdminsNotaffected();
		}
		return listAdmins;
	}

	/**
	 * Sets the list admins.
	 *
	 * @param listAdmins
	 *           the new list admins
	 */
	public void setListAdmins(final List<User> listAdmins)
	{
		this.listAdmins = listAdmins;
	}


}
