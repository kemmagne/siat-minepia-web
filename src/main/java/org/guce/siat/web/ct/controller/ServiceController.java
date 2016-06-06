package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class ServiceController.
 */
@ManagedBean(name = "serviceController")
@SessionScoped
public class ServiceController extends AbstractController<Service>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5106751417655195729L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ServiceController.class);

	/** The service service. */
	@ManagedProperty(value = "#{serviceService}")
	private ServiceService serviceService;

	/**
	 * Instantiates a new service controller.
	 */
	public ServiceController()
	{
		super(Service.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, ServiceController.class.getName());
		}
		super.setService(serviceService);
		super.setPageUrl(ControllerConstants.Pages.BO.SERVICE_INDEX_PAGE);
	}


	@Override
	public List<Service> getItems()
	{
		try
		{
			if (items == null)
			{
				items = serviceService.findServicesByOrganism(getCurrentOrganism());
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

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(serviceService.find(this.getSelected().getId()));
	}

	/**
	 * Gets the service service.
	 *
	 * @return the serviceService
	 */
	public ServiceService getServiceService()
	{
		return serviceService;
	}

	/**
	 * Sets the service service.
	 *
	 * @param serviceService
	 *           the serviceService to set
	 */
	public void setServiceService(final ServiceService serviceService)
	{
		this.serviceService = serviceService;
	}

	/**
	 * Gets the services options.
	 *
	 * @return the services options
	 */
	public SelectItem[] getServicesOptions()
	{
		final List<Service> servicesList = new ArrayList<Service>();
		for (final SubDepartment subDepartment : getCurrentOrganism().getSubDepartmentsList())
		{
			servicesList.addAll(subDepartment.getServicesList());
		}

		SelectItem[] servicesOptions;
		servicesOptions = new SelectItem[servicesList.size()];

		for (int i = 0; i < servicesList.size(); i++)
		{
			servicesOptions[i] = new SelectItem(servicesList.get(i), servicesList.get(i).getLabelFr());
		}
		return servicesOptions;
	}
}
