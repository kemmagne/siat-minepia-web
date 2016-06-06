package org.guce.siat.web.gr.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.Company;
import org.guce.siat.common.model.Country;
import org.guce.siat.common.model.ServicesItem;
import org.guce.siat.common.service.CompanyService;
import org.guce.siat.common.service.CountryService;
import org.guce.siat.common.service.ServicesItemService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.core.gr.model.Ciblage;
import org.guce.siat.core.gr.service.CiblageService;
import org.guce.siat.core.gr.utils.filter.CiblageFilter;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.primefaces.event.TabChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class CiblageController.
 */
@ManagedBean(name = "ciblageController")
@ViewScoped
public class CiblageController extends AbstractController<Ciblage>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3323949846696681213L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(CiblageController.class);

	/** The Constant DATE_VALIDATION_ERROR_MESSAGE. */
	private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

	/** The Constant SNSH_WARNING_MESSAGE. */
	private static final String SNSH_WARNING_MESSAGE = "CiblageWarningMessage";

	/** The ciblage service. */
	@ManagedProperty(value = "#{ciblageService}")
	private CiblageService ciblageService;

	/** The Service Item service. */
	@ManagedProperty(value = "#{servicesItemService}")
	private ServicesItemService servicesItemService;

	/** The country service. */
	@ManagedProperty(value = "#{countryService}")
	private CountryService countryService;

	/** The company service. */
	@ManagedProperty(value = "#{companyService}")
	private CompanyService companyService;

	/** The countries. */
	private List<Country> countries;

	/** The active index. */
	private Integer activeIndex;

	/** The filter. */
	private CiblageFilter filter;

	/**
	 * Instantiates a new ciblage controller.
	 */
	public CiblageController()
	{
		super(Ciblage.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, CiblageController.class.getName());
		}
		super.setService(ciblageService);
		super.setPageUrl(ControllerConstants.Pages.BO.GR.CIBLAGE_PAGE);
		countries = countryService.findAll();
		initSearch();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#create()
	 */
	@Override
	public void create()
	{
		selected.setOrganism(getCurrentOrganism());
		if (selected.getStartDate() != null && selected.getEndDate() != null
				&& selected.getStartDate().after(selected.getEndDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}

		if (selected.getCountry() != null)
		{
			selected.setCountryId(selected.getCountry().getCountryIdAlpha2());
		}

		super.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#delete()
	 */
	@Override
	public void delete()
	{
		super.delete();
		doSearch();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#edit()
	 */
	@Override
	public void edit()
	{
		if (selected.getStartDate() != null && selected.getEndDate() != null
				&& selected.getStartDate().after(selected.getEndDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}

		if (selected.getCountry() != null)
		{
			selected.setCountryId(selected.getCountry().getCountryIdAlpha2());
		}

		super.edit();
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(ciblageService.find(this.getSelected().getId()));

		if (selected.getServicesItem() != null)
		{
			activeIndex = Constants.ZERO;
		}
		else if (selected.getCountryId() != null)
		{
			activeIndex = Constants.ONE;
		}
		else if (selected.getCompany() != null)
		{
			activeIndex = Constants.TWO;
		}
		else if (selected.getManufacturer() != null)
		{
			activeIndex = Constants.THREE;
		}
	}

	/**
	 * Complete snsh.
	 *
	 * @param query
	 *           the query
	 * @return the list
	 */
	public List<ServicesItem> completeSnsh(final String query)
	{
		final List<ServicesItem> servicesItemList = servicesItemService.findAllActiveServicesItemByOrganism(getCurrentOrganism());
		final List<ServicesItem> filteredServicesItem = new ArrayList<ServicesItem>();

		if (CollectionUtils.isNotEmpty(servicesItemList))
		{
			for (final ServicesItem servicesItem : servicesItemList)
			{
				if (servicesItem.getSnsh().toLowerCase().startsWith(query.toLowerCase()))
				{
					filteredServicesItem.add(servicesItem);
				}
			}
		}
		else
		{
			JsfUtil.addWarningMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
					.getString(SNSH_WARNING_MESSAGE));
		}

		return filteredServicesItem;
	}

	/**
	 * Complete code douane.
	 *
	 * @param query
	 *           the query
	 * @return the list
	 */
	public List<Company> completeCodeDouane(final String query)
	{
		final List<Company> companiesList = companyService.findAll();
		final List<Company> filteredCompanies = new ArrayList<Company>();

		for (final Company company : companiesList)
		{
			if (company.getNumContribuable().toLowerCase().startsWith(query.toLowerCase()))
			{
				filteredCompanies.add(company);
			}
		}

		return filteredCompanies;
	}

	/* (non-Javadoc)
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<Ciblage> getItems()
	{
		return ciblageService.findByOrganism(getCurrentOrganism());
	}

	/**
	 * On create ciblage tab change.
	 *
	 * @param event
	 *           the event
	 */
	public void onCreateCiblageTabChange(final TabChangeEvent event)
	{
		selected = new Ciblage();
	}

	/**
	 * Inits the search.
	 */
	public void initSearch()
	{
		filter = new CiblageFilter();
		items = null;
		getItems();
	}

	/**
	 * Do search.
	 */
	public void doSearch()
	{
		if (filter.getBeginDate() != null && filter.getEndDate() != null && filter.getBeginDate().after(filter.getEndDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		setItems(ciblageService.findByFilter(filter));
	}

	/**
	 * Gets the ciblage service.
	 *
	 * @return the ciblageService
	 */
	public CiblageService getCiblageService()
	{
		return ciblageService;
	}

	/**
	 * Sets the ciblage service.
	 *
	 * @param ciblageService
	 *           the ciblageService to set
	 */
	public void setCiblageService(final CiblageService ciblageService)
	{
		this.ciblageService = ciblageService;
	}

	/**
	 * Gets the services item service.
	 *
	 * @return the servicesItemService
	 */
	public ServicesItemService getServicesItemService()
	{
		return servicesItemService;
	}

	/**
	 * Sets the services item service.
	 *
	 * @param servicesItemService
	 *           the servicesItemService to set
	 */
	public void setServicesItemService(final ServicesItemService servicesItemService)
	{
		this.servicesItemService = servicesItemService;
	}

	/**
	 * Gets the country service.
	 *
	 * @return the countryService
	 */
	public CountryService getCountryService()
	{
		return countryService;
	}

	/**
	 * Sets the country service.
	 *
	 * @param countryService
	 *           the countryService to set
	 */
	public void setCountryService(final CountryService countryService)
	{
		this.countryService = countryService;
	}

	/**
	 * Gets the countries.
	 *
	 * @return the countries
	 */
	public List<Country> getCountries()
	{
		return countries;
	}

	/**
	 * Sets the countries.
	 *
	 * @param countries
	 *           the countries to set
	 */
	public void setCountries(final List<Country> countries)
	{
		this.countries = countries;
	}

	/**
	 * Gets the company service.
	 *
	 * @return the companyService
	 */
	public CompanyService getCompanyService()
	{
		return companyService;
	}

	/**
	 * Sets the company service.
	 *
	 * @param companyService
	 *           the companyService to set
	 */
	public void setCompanyService(final CompanyService companyService)
	{
		this.companyService = companyService;
	}

	/**
	 * Gets the active index.
	 *
	 * @return the activeIndex
	 */
	public Integer getActiveIndex()
	{
		return activeIndex;
	}

	/**
	 * Sets the active index.
	 *
	 * @param activeIndex
	 *           the activeIndex to set
	 */
	public void setActiveIndex(final Integer activeIndex)
	{
		this.activeIndex = activeIndex;
	}

	/**
	 * Gets the filter.
	 *
	 * @return the filter
	 */
	public CiblageFilter getFilter()
	{
		return filter;
	}

	/**
	 * Sets the filter.
	 *
	 * @param filter
	 *           the filter to set
	 */
	public void setFilter(final CiblageFilter filter)
	{
		this.filter = filter;
	}

}
