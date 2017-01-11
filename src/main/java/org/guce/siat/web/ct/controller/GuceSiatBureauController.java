package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.Entity;
import org.guce.siat.common.model.GuceSiatBureau;
import org.guce.siat.common.model.Ministry;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.service.GuceSiatBureauService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.BureauType;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class GuceSiatBureauController.
 */
@ManagedBean(name = "guceSiatBureauController")
@ViewScoped
public class GuceSiatBureauController extends AbstractController<GuceSiatBureau>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7059560675650803143L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(GuceSiatBureauController.class);


	/** The guce siat bureau service. */
	@ManagedProperty(value = "#{guceSiatBureauService}")
	private GuceSiatBureauService guceSiatBureauService;

	/** The selected ministry. */
	private Ministry selectedMinistry;

	/** The selected organism. */
	private Organism selectedOrganism;

	/** The selected sub department. */
	private SubDepartment selectedSubDepartment;

	/** The selected service. */
	private Service selectedService;

	/**
	 * Instantiates a new guce siat bureau controller.
	 */
	public GuceSiatBureauController()
	{
		super(GuceSiatBureau.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, GuceSiatBureauController.class.getName());
		}
		super.setService(guceSiatBureauService);
		super.setPageUrl(ControllerConstants.Pages.BO.GUCE_SIAT_OFFICES_PAGE);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<GuceSiatBureau> getItems()
	{
		try
		{
			if (items == null)
			{
				items = new ArrayList<GuceSiatBureau>();

				final List<Entity> entities = selectedService.getEntityList();

				for (final Entity e : entities)
				{
					if (e instanceof Bureau && BureauType.BUREAU_REGIONAL.equals(((Bureau) e).getBureauType()))
					{
						final Bureau bureauSiat = (Bureau) e;
						GuceSiatBureau guceSiatBureau = guceSiatBureauService.findGuceSiatBureauBySiatBureau(bureauSiat);
						if (guceSiatBureau == null)
						{
							guceSiatBureau = new GuceSiatBureau();
							guceSiatBureau.setSiatBureau(bureauSiat.getCode());
						}
						items.add(guceSiatBureau);
					}
				}

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
	 * Edits the guce bureau.
	 *
	 * @param event
	 *           the event
	 */
	public void editGuceBureau(final CellEditEvent event)
	{

		final DataTable guceSiatBureauTable = (DataTable) event.getSource();
		final Object oldValue = event.getOldValue();
		final Object newValue = event.getNewValue();
		selected = (GuceSiatBureau) guceSiatBureauTable.getRowData();

		if (selected != null && StringUtils.isNotBlank(newValue.toString()) && !newValue.equals(oldValue))
		{
			selected.setBureauGuceCode(newValue.toString());
			if (selected.getId() != null)
			{
				guceSiatBureauService.update(selected);
			}
			else
			{
				guceSiatBureauService.save(selected);
			}
			final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					"GuceSiatBureauUpdated");
			JsfUtil.addSuccessMessage(msg);
		}
		else
		{
			final String msg = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					"GuceSiatBureauUpdateFail");
			JsfUtil.addErrorMessage(msg);
		}
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(guceSiatBureauService.find(selected.getId()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#edit()
	 */

	@Override
	public void edit()
	{
		guceSiatBureauService.update(selected);

		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				GuceSiatBureau.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
		JsfUtil.addSuccessMessage(msg);
		reset();
		if (!isValidationFailed())
		{
			refreshItems();
			selected = null;
		}
	}

	/**
	 * Gets the selected ministry.
	 *
	 * @return the selected ministry
	 */
	public Ministry getSelectedMinistry()
	{
		return selectedMinistry;
	}

	/**
	 * Sets the selected ministry.
	 *
	 * @param selectedMinistry
	 *           the new selected ministry
	 */
	public void setSelectedMinistry(final Ministry selectedMinistry)
	{
		this.selectedMinistry = selectedMinistry;
	}

	/**
	 * Gets the selected organism.
	 *
	 * @return the selected organism
	 */
	public Organism getSelectedOrganism()
	{
		return selectedOrganism;
	}

	/**
	 * Sets the selected organism.
	 *
	 * @param selectedOrganism
	 *           the new selected organism
	 */
	public void setSelectedOrganism(final Organism selectedOrganism)
	{
		this.selectedOrganism = selectedOrganism;
	}

	/**
	 * Gets the selected sub department.
	 *
	 * @return the selected sub department
	 */
	public SubDepartment getSelectedSubDepartment()
	{
		return selectedSubDepartment;
	}

	/**
	 * Sets the selected sub department.
	 *
	 * @param selectedSubDepartment
	 *           the new selected sub department
	 */
	public void setSelectedSubDepartment(final SubDepartment selectedSubDepartment)
	{
		this.selectedSubDepartment = selectedSubDepartment;
	}

	/**
	 * Gets the selected service.
	 *
	 * @return the selected service
	 */
	public Service getSelectedService()
	{
		return selectedService;
	}

	/**
	 * Sets the selected service.
	 *
	 * @param selectedService
	 *           the new selected service
	 */
	public void setSelectedService(final Service selectedService)
	{
		this.selectedService = selectedService;
	}

	/**
	 * Gets the guce siat bureau service.
	 *
	 * @return the guce siat bureau service
	 */
	public GuceSiatBureauService getGuceSiatBureauService()
	{
		return guceSiatBureauService;
	}

	/**
	 * Sets the guce siat bureau service.
	 *
	 * @param guceSiatBureauService
	 *           the new guce siat bureau service
	 */
	public void setGuceSiatBureauService(final GuceSiatBureauService guceSiatBureauService)
	{
		this.guceSiatBureauService = guceSiatBureauService;
	}

}
