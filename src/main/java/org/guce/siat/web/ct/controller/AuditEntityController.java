package org.guce.siat.web.ct.controller;

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
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class AuditEntityController.
 */
@ManagedBean(name = "auditEntityController")
@ViewScoped
public class AuditEntityController extends AbstractController<AuditEntity>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3571636845075817583L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(AuditEntityController.class);

	/** The Constant DATE_VALIDATION_ERROR_MESSAGE. */
	private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

	/** The audit entity service. */
	@ManagedProperty(value = "#{auditEntityService}")
	private AuditEntityService auditEntityService;

	/** The audit filter. */
	private AuditFilter auditFilter;

	/**
	 * Instantiates a new audit entity controller.
	 */
	public AuditEntityController()
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
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, AuditEntityController.class.getName());
		}
		super.setService(auditEntityService);
		super.setPageUrl(ControllerConstants.Pages.BO.AUDIT_ENTITY_INDEX_PAGE);
		initSearch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<AuditEntity> getItems()
	{
		return auditEntityService.findByAdmin(getLoggedUser());
	}

	/**
	 * Do search.
	 */
	public void doSearch()
	{
		if (auditFilter.getBeginDate() != null && auditFilter.getEndDate() != null
				&& auditFilter.getBeginDate().after(auditFilter.getEndDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			setItems(auditEntityService.findByFilter(auditFilter));
		}
	}

	/**
	 * Inits the search.
	 */
	public void initSearch()
	{
		auditFilter = new AuditFilter();
		items = null;
		getItems();
	}


	/**
	 * Gets the audit entity service.
	 *
	 * @return the audit entity service
	 */
	public AuditEntityService getAuditEntityService()
	{
		return auditEntityService;
	}

	/**
	 * Sets the audit entity service.
	 *
	 * @param auditEntityService
	 *           the new audit entity service
	 */
	public void setAuditEntityService(final AuditEntityService auditEntityService)
	{
		this.auditEntityService = auditEntityService;
	}

	/**
	 * Gets the audit filter.
	 *
	 * @return the audit filter
	 */
	public AuditFilter getAuditFilter()
	{
		return auditFilter;
	}

	/**
	 * Sets the audit filter.
	 *
	 * @param auditFilter
	 *           the new audit filter
	 */
	public void setAuditFilter(final AuditFilter auditFilter)
	{
		this.auditFilter = auditFilter;
	}

	/**
	 * Gets the date validation error message.
	 *
	 * @return the dateValidationErrorMessage
	 */
	public static String getDateValidationErrorMessage()
	{
		return DATE_VALIDATION_ERROR_MESSAGE;
	}



}
