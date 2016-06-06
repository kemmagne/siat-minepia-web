package org.guce.siat.web.ct.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.ReportOrganism;
import org.guce.siat.common.service.ReportOrganismService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.gr.controller.ParamsOrganismController;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class ParamsOrganismController.
 */
@ManagedBean(name = "reportSettingsController")
@SessionScoped
public class ReportSettingsController extends AbstractController<ReportOrganism>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6136435911440328457L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ParamsOrganismController.class);

	/** The report organism service. */
	@ManagedProperty(value = "#{reportOrganismService}")
	private ReportOrganismService reportOrganismService;

	/**
	 * Instantiates a new params organism controller.
	 */
	public ReportSettingsController()
	{
		super(ReportOrganism.class);
	}


	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, ParamsOrganismController.class.getName());
		}
		super.setService(reportOrganismService);
		super.setPageUrl(ControllerConstants.Pages.BO.REPORT_SETTINGS_PAGE);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<ReportOrganism> getItems()
	{
		try
		{
			items = reportOrganismService.findReportByOrganism(getCurrentOrganism());
		}
		catch (final Exception ex)
		{
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}
		return items;
	}
	
  /**
   * On cell edit.
   *
   * @param event the event
   */
  public void editReportPostfix(CellEditEvent event) {
     
     DataTable reportParamsTable=(DataTable) event.getSource();
     Object oldValue = event.getOldValue();
     Object newValue = event.getNewValue();
     selected=(ReportOrganism)reportParamsTable.getRowData();
      
     if(selected != null && StringUtils.isNotBlank(newValue.toString()) && !newValue.equals(oldValue)) {
         selected.setValue(newValue.toString());
         reportOrganismService.update(selected);
			final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
					.getString(ControllerConstants.Bundle.Messages.REPORT_POSTFIXE_EDIT_SUCCESS);
         JsfUtil.addSuccessMessage(msg);
      
     }else{
			final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
					.getString(ControllerConstants.Bundle.Messages.REPORT_POSTFIXE_EDIT_FAILED);
   	  JsfUtil.addErrorMessage(msg);
     }
 }


	/**
	 * Gets the report organism service.
	 *
	 * @return the report organism service
	 */
	public ReportOrganismService getReportOrganismService()
	{
		return reportOrganismService;
	}

	/**
	 * Sets the report organism service.
	 *
	 * @param reportOrganismService the new report organism service
	 */
	public void setReportOrganismService(ReportOrganismService reportOrganismService)
	{
		this.reportOrganismService = reportOrganismService;
	}

}
