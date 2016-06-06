package org.guce.siat.web.gr.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.guce.siat.common.model.AuditEntity;
import org.guce.siat.common.service.AuditEntityService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.filter.AuditFilter;
import org.guce.siat.core.gr.model.Alert;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.gr.util.HistoryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class HistoryAlertController.
 */
@ManagedBean(name = "historyAlertController")
@ViewScoped
public class HistoryAlertController extends AbstractController<AuditEntity>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3428598605499635288L;

	/** The Constant DATE_VALIDATION_ERROR_MESSAGE. */
	private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(HistoryAlertController.class);

	/** The audit entity service. */
	@ManagedProperty(value = "#{auditEntityService}")
	private AuditEntityService auditEntityService;

	/** The filter. */
	private AuditFilter filter = new AuditFilter();

	/** The history models. */
	private List<HistoryModel> historyModelsList;


	/**
	 * Instantiates a new history alert controller.
	 */
	public HistoryAlertController()
	{
		super(AuditEntity.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, HistoryAlertController.class.getName());
		}
		super.setService(auditEntityService);
		super.setPageUrl(ControllerConstants.Pages.BO.GR.AUDIT_PAGE);
		initSearch();
	}

	/**
	 * Inits the search.
	 */
	public void initSearch()
	{
		filter = new AuditFilter();
		historyModelsList = generateHistoryModelList(auditEntityService.findByModel(Alert.class.getSimpleName()));
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
		else if (filter.getBeginDate() != null || filter.getEndDate() != null)
		{

			historyModelsList = generateHistoryModelList(auditEntityService
					.findByFilterAndModel(filter, Alert.class.getSimpleName()));
		}
		else
		{
			historyModelsList = generateHistoryModelList(auditEntityService.findByModel(Alert.class.getSimpleName()));
		}

	}

	/**
	 * Generate history model list.
	 *
	 * @param auditEntities
	 *           the audit entities
	 * @return the list
	 */
	public List<HistoryModel> generateHistoryModelList(final List<AuditEntity> auditEntities)
	{
		final List<HistoryModel> histModelsList = new ArrayList<HistoryModel>();

		try
		{
			for (final AuditEntity auditEntity : auditEntities)
			{
				boolean found = false;
				for (final HistoryModel historyModel : histModelsList)
				{

					if (auditEntity.getIdModel().equals(historyModel.getIdModel()))
					{
						found = true;
						historyModel.getListAuditEntity().add(auditEntity);
						break;
					}
				}

				if (!found)
				{
					final HistoryModel historyModel = new HistoryModel();
					historyModel.setIdModel(auditEntity.getIdModel());
					historyModel.setListAuditEntity(new ArrayList<AuditEntity>());
					historyModel.getListAuditEntity().add(auditEntity);
					histModelsList.add(historyModel);
				}
			}
		}
		catch (final Exception ex)
		{
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}
		return histModelsList;
	}

	/**
	 * Gets the history models list.
	 *
	 * @return the historyModelsList
	 */
	public List<HistoryModel> getHistoryModelsList()
	{
		return historyModelsList;
	}

	/**
	 * Sets the history models list.
	 *
	 * @param historyModelsList
	 *           the historyModelsList to set
	 */
	public void setHistoryModelsList(final List<HistoryModel> historyModelsList)
	{
		this.historyModelsList = historyModelsList;
	}

	/**
	 * Gets the audit entity service.
	 *
	 * @return the auditEntityService
	 */
	public AuditEntityService getAuditEntityService()
	{
		return auditEntityService;
	}

	/**
	 * Sets the audit entity service.
	 *
	 * @param auditEntityService
	 *           the auditEntityService to set
	 */
	public void setAuditEntityService(final AuditEntityService auditEntityService)
	{
		this.auditEntityService = auditEntityService;
	}

	/**
	 * Gets the filter.
	 *
	 * @return the filter
	 */
	public AuditFilter getFilter()
	{
		return filter;
	}

	/**
	 * Sets the filter.
	 *
	 * @param filter
	 *           the filter to set
	 */
	public void setFilter(final AuditFilter filter)
	{
		this.filter = filter;
	}

}
