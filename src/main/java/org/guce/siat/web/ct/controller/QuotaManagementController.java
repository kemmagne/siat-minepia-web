package org.guce.siat.web.ct.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.core.ct.service.CommonService;
import org.guce.siat.core.ct.util.quota.QuotaDto;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class QuotaManagementController.
 */
@ManagedBean(name = "quotaController")
@ViewScoped
public class QuotaManagementController extends AbstractController<FileItem>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2706442341568834194L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(QuotaManagementController.class);

	/** The common service. */
	@ManagedProperty(value = "#{commonService}")
	private CommonService commonService;

	/** The search filter. */
	private QuotaDto searchFilter;

	/** The search result. */
	private List<QuotaDto> searchResult;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, QuotaManagementController.class.getName());
		}
		super.setPageUrl(ControllerConstants.Pages.FO.Quota.QUOTA_HOME_PAGE);
		searchFilter = new QuotaDto();
	}

	/**
	 * Search.
	 */
	public void search()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("search with {} as filter", searchFilter.toString());
		}
		setSearchResult(commonService.findQuotsByCreteria(searchFilter));
		searchFilter = new QuotaDto();
	}

	/**
	 * Sets the common service.
	 *
	 * @param commonService
	 *           the new common service
	 */
	public void setCommonService(final CommonService commonService)
	{
		this.commonService = commonService;
	}


	/**
	 * Gets the search filter.
	 *
	 * @return the search filter
	 */
	public QuotaDto getSearchFilter()
	{
		return searchFilter;
	}


	/**
	 * Sets the search filter.
	 *
	 * @param searchFilter
	 *           the new search filter
	 */
	public void setSearchFilter(final QuotaDto searchFilter)
	{
		this.searchFilter = searchFilter;
	}


	/**
	 * Gets the search result.
	 *
	 * @return the search result
	 */
	public List<QuotaDto> getSearchResult()
	{
		return searchResult;
	}


	/**
	 * Sets the search result.
	 *
	 * @param searchResult
	 *           the new search result
	 */
	public void setSearchResult(final List<QuotaDto> searchResult)
	{
		this.searchResult = searchResult;
	}

}
