package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.Ministry;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.User;
import org.guce.siat.common.model.UserAuthority;
import org.guce.siat.common.service.AuthorityService;
import org.guce.siat.common.service.MailService;
import org.guce.siat.common.service.MinistryService;
import org.guce.siat.common.service.OrganismService;
import org.guce.siat.common.service.UserAuthorityService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.AuthorityConstants;
import org.guce.siat.common.utils.enums.PositionType;
import org.guce.siat.common.utils.enums.Theme;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class AdminController.
 */
@SessionScoped
@ManagedBean(name = "adminController")
public class AdminController extends AbstractController<User>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2007635693968863910L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

	/** The Constant ALL_MINISTRIES_HAVE_ADMIN. */
	private static final String ALL_MINISTRIES_HAVE_ADMIN = "CreateAdminAllMinHaveAdmin";

	/** The Constant ALL_ORGANISM_HAVE_ADMIN. */
	private static final String ALL_ORGANISM_HAVE_ADMIN = "CreateAdminAllOrganismHaveAdmin";

	/** The Constant SAVE_ERROR_OCCURED. */
	private static final String SAVE_ADMIN_ERROR_OCCURED = "SaveAdminErrorOccured";

	/** The user service. */
	@ManagedProperty(value = "#{userService}")
	private UserService userService;

	/** The authority service. */
	@ManagedProperty(value = "#{authorityService}")
	private AuthorityService authorityService;

	/** The user authority service. */
	@ManagedProperty(value = "#{userAuthorityService}")
	private UserAuthorityService userAuthorityService;

	/** The ministry service. */
	@ManagedProperty(value = "#{ministryService}")
	private MinistryService ministryService;

	/** The organism service. */
	@ManagedProperty(value = "#{organismService}")
	private OrganismService organismService;

	/** The mail service. */
	@ManagedProperty(value = "#{mailService}")
	private MailService mailService;
	/** The authority. */
	private Authority authority;

	/** The ministries list. */
	private List<Ministry> ministriesList;

	/** The organisms list. */
	private List<Organism> organismsList;

	/** The al stringl min have admin msg. */
	private String allMinHaveAdminMsg;

	/** The all org have admin msg. */
	private String allOrgHaveAdminMsg;

	/**
	 * Instantiates a new admin controller.
	 */

	public AdminController()
	{
		super(User.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, AdminController.class.getName());
		}
		super.setService(userService);
		super.setPageUrl(ControllerConstants.Pages.BO.ADMIN_INDEX_PAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
	 */
	@Override
	public void prepareCreate()
	{
		final RequestContext context = RequestContext.getCurrentInstance();
		super.prepareCreate();

		if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ROOT.getCode()))
		{
			ministriesList = ministryService.findMinistryHasntAdmin();
			if (CollectionUtils.isEmpty(ministriesList))
			{
				JsfUtil.addWarningMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						ALL_MINISTRIES_HAVE_ADMIN));
				context.update(":growl");
			}
			else
			{

				//execute javascript oncomplete
				context.execute("PF('adminCreateDialog').show();");
			}
		}
		else if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_MINISTERE.getCode()))
		{
			organismsList = organismService.findOrganismNotHaveAdminByMinistry(getCurrentMinistry());
			if (CollectionUtils.isEmpty(organismsList))
			{
				JsfUtil.addWarningMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						ALL_ORGANISM_HAVE_ADMIN));
				context.update(":growl");
			}
			else
			{
				//execute javascript oncomplete
				context.execute("PF('adminCreateDialog').show();");
			}
		}

	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(userService.find(this.getSelected().getId()));

		final User selectedUser = userService.find(this.getSelected().getId());

		if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ROOT.getCode()))
		{
			ministriesList = ministryService.findAll();
		}
		else if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_MINISTERE.getCode()))
		{
			organismsList = getCurrentMinistry().getOrganismsList();
		}

		this.setSelected(selectedUser);

		for (final UserAuthority userAuthority : selectedUser.getUserAuthorityList())
		{
			if (AuthorityConstants.ADMIN_ORGANISME.getCode().equals(userAuthority.getAuthority()))
			{
				authority = userAuthority.getAuthorityGranted();
				break;
			}
		}
		this.setAuthority(authority);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#create()
	 */
	@Override
	public void create()
	{
		selected.setAccountNonExpired(true);
		selected.setDeleted(false);
		selected.setAccountNonLocked(true);
		selected.setCredentialsNonExpired(true);
		selected.setFirstLogin(Boolean.TRUE);
		selected.setAttempts(0);
		selected.setPosition(PositionType.ADMINISTRATEUR);
		selected.setTheme(Theme.BLUE);
		final String radomPassword = RandomStringUtils.randomAlphanumeric(Constants.SIX);
		selected.setPassword(radomPassword);

		//set the userAuthority
		final UserAuthority userAuthority = new UserAuthority();
		userAuthority.setUser(selected);
		//find Authority by the connected user authority (ROOT --> AM), (AM --> AO)
		if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ROOT.getCode()))
		{
			userAuthority.setAuthorityGranted(authorityService.findAuthorityByCode(AuthorityConstants.ADMIN_MINISTERE.getCode()));
		}
		else if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_MINISTERE.getCode()))
		{
			userAuthority.setAuthorityGranted(authorityService.findAuthorityByCode(AuthorityConstants.ADMIN_ORGANISME.getCode()));
		}
		try
		{
			userAuthorityService.createUserAndAuthorities(selected, userAuthority);
			final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					User.class.getSimpleName() + PersistenceActions.CREATE.getCode());
			JsfUtil.addSuccessMessage(msg);
			//send mail params
			final String subject = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					CREATE_SIAT_ACCOUNT_MAIL_SUBJECT);

			final String templateFileName = localeFileTemplate(WELCOME_MAIL_BODY, getCurrentLocale().getLanguage());
			Map<String, String> map = new HashMap<String, String>();
			map = prepareMap(subject, mailService.getFromValue(), selected, radomPassword, templateFileName);
			mailService.sendMail(map);
			reset();

			if (!isValidationFailed())
			{
				refreshItems();
				selected = null;
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					SAVE_ADMIN_ERROR_OCCURED));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#edit()
	 */
	@Override
	public void edit()
	{
		userService.updateUser(selected);

		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				User.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
		JsfUtil.addSuccessMessage(msg);
		reset();
		if (!isValidationFailed())
		{
			refreshItems();
			selected = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#delete()
	 */
	@Override
	public void delete()
	{
		selected.setDeleted(true);
		selected.setAdministration(null);
		userService.update(selected);

		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				User.class.getSimpleName() + PersistenceActions.DELETE.getCode());
		JsfUtil.addSuccessMessage(msg);
		reset();
		if (!isValidationFailed())
		{
			refreshItems();
			selected = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<User> getItems()
	{
		if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ROOT.getCode()))
		{
			items = userService.findUsersByAuthorities(AuthorityConstants.ADMIN_MINISTERE.getCode());
		}
		else if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ADMIN_MINISTERE.getCode()))
		{
			items = new ArrayList<User>();
			final List<Organism> organisms = getCurrentMinistry().getOrganismsList();
			for (final Organism orgnism : organisms)
			{
				items.addAll(userService.findUsersByOrganismAndAuthorities(orgnism, AuthorityConstants.ADMIN_ORGANISME.getCode()));
			}
		}
		return items;
	}

	/**
	 * Gets the list authorities by user.
	 *
	 * @param user
	 *           the user
	 * @return the list authorities by user
	 */
	public List<Authority> getListAuthoritiesByUser(final User user)
	{
		final List<Authority> listAuthoritiesByUser = new ArrayList<Authority>();

		for (final User item : getItems())
		{
			if (user != null && item.getId().equals(user.getId()))
			{
				listAuthoritiesByUser.addAll(item.getAuthorities());
				break;
			}
		}

		return listAuthoritiesByUser;
	}

	/**
	 * Gets the user service.
	 *
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * Sets the user service.
	 *
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * Gets the authority service.
	 *
	 * @return the authorityService
	 */
	public AuthorityService getAuthorityService()
	{
		return authorityService;
	}

	/**
	 * Sets the authority service.
	 *
	 * @param authorityService
	 *           the authorityService to set
	 */
	public void setAuthorityService(final AuthorityService authorityService)
	{
		this.authorityService = authorityService;
	}

	/**
	 * Gets the user authority service.
	 *
	 * @return the userAuthorityService
	 */
	public UserAuthorityService getUserAuthorityService()
	{
		return userAuthorityService;
	}

	/**
	 * Sets the user authority service.
	 *
	 * @param userAuthorityService
	 *           the userAuthorityService to set
	 */
	public void setUserAuthorityService(final UserAuthorityService userAuthorityService)
	{
		this.userAuthorityService = userAuthorityService;
	}

	/**
	 * Gets the authority.
	 *
	 * @return the authority
	 */
	public Authority getAuthority()
	{
		return authority;
	}

	/**
	 * Sets the authority.
	 *
	 * @param authority
	 *           the authority to set
	 */
	public void setAuthority(final Authority authority)
	{
		this.authority = authority;
	}

	/**
	 * Gets the ministry service.
	 *
	 * @return the ministryService
	 */
	public MinistryService getMinistryService()
	{
		return ministryService;
	}

	/**
	 * Sets the ministry service.
	 *
	 * @param ministryService
	 *           the ministryService to set
	 */
	public void setMinistryService(final MinistryService ministryService)
	{
		this.ministryService = ministryService;
	}

	/**
	 * Gets the ministries list.
	 *
	 * @return the ministriesList
	 */
	public List<Ministry> getMinistriesList()
	{
		return ministriesList;
	}

	/**
	 * Gets the organism service.
	 *
	 * @return the organismService
	 */
	public OrganismService getOrganismService()
	{
		return organismService;
	}

	/**
	 * Sets the organism service.
	 *
	 * @param organismService
	 *           the organismService to set
	 */
	public void setOrganismService(final OrganismService organismService)
	{
		this.organismService = organismService;
	}

	/**
	 * Sets the ministries list.
	 *
	 * @param ministriesList
	 *           the ministriesList to set
	 */
	public void setMinistriesList(final List<Ministry> ministriesList)
	{
		this.ministriesList = ministriesList;
	}

	/**
	 * Gets the organisms list.
	 *
	 * @return the organismsList
	 */
	public List<Organism> getOrganismsList()
	{
		return organismsList;
	}

	/**
	 * Sets the organisms list.
	 *
	 * @param organismsList
	 *           the organismsList to set
	 */
	public void setOrganismsList(final List<Organism> organismsList)
	{
		this.organismsList = organismsList;
	}

	/**
	 * Gets the all min have admin msg.
	 *
	 * @return the allMinHaveAdminMsg
	 */
	public String getAllMinHaveAdminMsg()
	{
		return allMinHaveAdminMsg;
	}

	/**
	 * Sets the all min have admin msg.
	 *
	 * @param allMinHaveAdminMsg
	 *           the allMinHaveAdminMsg to set
	 */
	public void setAllMinHaveAdminMsg(final String allMinHaveAdminMsg)
	{
		this.allMinHaveAdminMsg = allMinHaveAdminMsg;
	}

	/**
	 * Gets the all org have admin msg.
	 *
	 * @return the allOrgHaveAdminMsg
	 */
	public String getAllOrgHaveAdminMsg()
	{
		return allOrgHaveAdminMsg;
	}

	/**
	 * Sets the all org have admin msg.
	 *
	 * @param allOrgHaveAdminMsg
	 *           the allOrgHaveAdminMsg to set
	 */
	public void setAllOrgHaveAdminMsg(final String allOrgHaveAdminMsg)
	{
		this.allOrgHaveAdminMsg = allOrgHaveAdminMsg;
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
