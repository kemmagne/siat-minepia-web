package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.guce.siat.common.model.Item;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.ServicesItem;
import org.guce.siat.common.model.SubDepartment;
import org.guce.siat.common.service.ItemService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.ServicesItemService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.guce.siat.web.ct.controller.util.enums.ServiceItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class ItemController.
 */
@ManagedBean(name = "itemController")
@SessionScoped
public class ItemController extends AbstractController<ServicesItem>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4908723135996990144L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);

	/** The Constant NSH. */
	private static final String NSH = "## NSH : {} {}";

	/** The Constant REQUIRED_LABEL_MESSAGE. */
	private static final String REQUIRED_LABEL_MESSAGE = "CreateItemRequiredMessage_Label";

	/** The Constant ATTACHED_SERVICE_ITEM_MESSAGE. */
	private static final String ATTACHED_SERVICE_ITEM_MESSAGE = "AttachedServiceItem";

	/** The Service Item service. */
	@ManagedProperty(value = "#{servicesItemService}")
	private ServicesItemService servicesItemService;

	/** The item service. */
	@ManagedProperty(value = "#{itemService}")
	private ItemService itemService;

	/** The service service. */
	@ManagedProperty(value = "#{serviceService}")
	private ServiceService serviceService;

	/** The selected Service. */
	private Service selectedService;

	/** The selected sub department. */
	private SubDepartment selectedSubDepartment;

	/** The items nsh list. */
	private List<String> itemsNshList;

	/** The selected nsh. */
	private String selectedNSH;

	/** The item label. */
	private String itemLabel;

	/** The attachedToOtherService. */
	private Boolean attachedToOtherService;

	/** is inactive item. */
	private Boolean isInactive;

	/** The related. */
	private Boolean attachedToService;

	/** The sub department list. */
	private List<SubDepartment> subDepartmentList;

	/** The Sub family item. */
	private List<ServicesItem> subFamilyItem;

	/** The related service. */
	private String relatedService;

	/**
	 * Instantiates a new car controller.
	 */
	public ItemController()
	{
		super(ServicesItem.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, ItemController.class.getName());
		}
		super.setService(servicesItemService);
		super.setPageUrl(ControllerConstants.Pages.BO.ITEM_INDEX_PAGE);
		getItems();
	}

	/**
	 * Check related nsh warning.
	 */
	public void checkRelatedNSHWarning()
	{
		if (StringUtils.isNotEmpty(selectedNSH) && selectedService != null)
		{
			checkItemToAdd();
			if (attachedToOtherService)
			{
				JsfUtil.addWarningMessage("Attention !"
						+ ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("RelatedServiceItem") + " : "
						+ relatedService);
			}
			if (attachedToService)
			{
				JsfUtil.addWarningMessage("Attention !"
						+ ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("AttachedItem"));
			}

		}
	}

	/**
	 * Check related service item.
	 */
	public void checkItemToAdd()
	{
		final List<ServicesItem> serviceItemList = servicesItemService.findServicesItemByNsh(selectedNSH);

		attachedToService = false;
		attachedToOtherService = false;
		isInactive = false;
		relatedService = StringUtils.EMPTY;

		if (CollectionUtils.isNotEmpty(serviceItemList))
		{
			verifyItemInService(serviceItemList);
		}
	}

	/**
	 * Verify item in service.
	 *
	 * @param serviceItemList
	 *           the service item list
	 */
	public void verifyItemInService(final List<ServicesItem> serviceItemList)
	{
		for (final ServicesItem servicesItem : serviceItemList)
		{
			if (selectedService != null)
			{
				if ((ServiceItemType.NATIVE.getCode().equals(servicesItem.getType().toString()))
						&& (servicesItem.getService().getId().equals(selectedService.getId())))
				{
					if (!servicesItem.getDeleted())
					{
						attachedToService = true;
						attachedToOtherService = false;
						break;
					}
					else
					{
						isInactive = true;
						selected = servicesItem;
					}
				}
				if ((ServiceItemType.NATIVE.getCode().equals(servicesItem.getType().toString())) && !servicesItem.getDeleted())
				{
					attachedToOtherService = true;
					relatedService += servicesItem.getService().getLabelFr() + ", ";
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#create()
	 */
	@Override
	public void create()
	{
		checkItemToAdd();
		if (itemLabel.isEmpty() || itemLabel == null)
		{
			JsfUtil.addWarningMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					REQUIRED_LABEL_MESSAGE));
			return;
		}
		if (attachedToService)
		{
			JsfUtil.addWarningMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					ATTACHED_SERVICE_ITEM_MESSAGE));
			return;
		}
		if (isInactive)
		{
			activateItem();
			getItems();
			return;
		}

		final Item item = itemService.findByGoodsItemCode(selectedNSH);
                if (selected == null) {
                    selected = new ServicesItem();
                }
		selected.setNsh(item);
		selected.setType('N');
		selected.setLabel(itemLabel);
		selected.setService(selectedService);
		selected.setDeleted(false);
		super.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<ServicesItem> getItems()
	{
		try
		{
			if (items == null && getCurrentOrganism() != null)
			{
				final List<ServicesItem> tempItems = servicesItemService.findActiveServicesItemByOrganism(getCurrentOrganism());

				items = new ArrayList<ServicesItem>();
				for (final ServicesItem servicesItem : tempItems)
				{
					if (ServiceItemType.NATIVE.getCode().equals(servicesItem.getType().toString()))
					{
						items.add(servicesItem);
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
	 * Deactivate item.
	 *
	 * @param event
	 *           the event
	 */
	public void desactivateItem(final ActionEvent event)
	{
		selected.setDeleted(true);

		final List<ServicesItem> items = fetchSubFamilyItemByItem(selected);

		if (items != null)
		{
			for (final ServicesItem servicesItem : items)
			{
				servicesItem.setDeleted(true);
				servicesItemService.update(servicesItem);
			}
		}

		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("ServicesItemDeleted");
		persist(PersistenceActions.UPDATE, msg);
		getItems();
	}

	/**
	 * Activate item.
	 */
	public void activateItem()
	{
		selected.setDeleted(false);
		if (LOG.isDebugEnabled())
		{
			LOG.debug(NSH, selectedNSH, " est re-activé");
		}

		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("ServicesItemCreated");
		persist(PersistenceActions.UPDATE, msg);
		getItems();
	}

	/**
	 * Fetch sub family item by item.
	 *
	 * @param item
	 *           the item
	 * @return the list
	 */
	public List<ServicesItem> fetchSubFamilyItemByItem(final ServicesItem item)
	{
		final List<ServicesItem> items = servicesItemService.findServicesItemByService(item.getService());
		subFamilyItem = new ArrayList<ServicesItem>();
		if (item != null)
		{
			for (final ServicesItem servicesItem : items)
			{
				if ((servicesItem.getNsh().equals(item.getNsh()))
						&& (ServiceItemType.SUBFAMILY.getCode().equals(servicesItem.getType().toString())))
				{
					subFamilyItem.add(servicesItem);
				}
			}
		}

		return subFamilyItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
	 */
	@Override
	public void prepareCreate()
	{
		selectedNSH = null;
		itemLabel = null;
		selectedService = null;
		selectedSubDepartment = null;
		super.prepareCreate();
	}

	/**
	 * Complete native nsh.
	 *
	 * @param query
	 *           the query
	 * @return the list
	 */
	public List<String> completeNativeNSH(final String query)
	{
		final List<Item> nativeItemsList = itemService.findAll();
		itemsNshList = new ArrayList<String>();

		if (CollectionUtils.isNotEmpty(nativeItemsList))
		{
			for (final Item item : nativeItemsList)
			{
				if (item.getGoodsItemCode().startsWith(query))
				{
					itemsNshList.add(item.getGoodsItemCode());
				}
			}
		}

		return itemsNshList;
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
	 * Gets the item service.
	 *
	 * @return the itemService
	 */
	public ItemService getItemService()
	{
		return itemService;
	}

	/**
	 * Sets the item service.
	 *
	 * @param itemService
	 *           the itemService to set
	 */
	public void setItemService(final ItemService itemService)
	{
		this.itemService = itemService;
	}


	/**
	 * Gets the items nsh list.
	 *
	 * @return the items nsh list
	 */
	public List<String> getItemsNshList()
	{
		return itemsNshList;
	}

	/**
	 * Sets the items nsh list.
	 *
	 * @param itemsNshList
	 *           the itemsNshList to set
	 */
	public void setItemsNshList(final List<String> itemsNshList)
	{
		this.itemsNshList = itemsNshList;
	}

	/**
	 * Gets the selected nsh.
	 *
	 * @return the selectedNSH
	 */
	public String getSelectedNSH()
	{
		return selectedNSH;
	}

	/**
	 * Sets the selected nsh.
	 *
	 * @param selectedNSH
	 *           the selectedNSH to set
	 */
	public void setSelectedNSH(final String selectedNSH)
	{
		this.selectedNSH = selectedNSH;
	}

	/**
	 * Gets the item label.
	 *
	 * @return the itemLabel
	 */
	public String getItemLabel()
	{
		if (selectedNSH != null && !selectedNSH.isEmpty())
		{
			final Item item = itemService.findByGoodsItemCode(selectedNSH);
			if (item != null)
			{
				if (Constants.LOCALE_ENGLISH.equals(getCurrentLocaleCode()))
				{
					itemLabel = item.getGoodsItemDescEn();
				}
				else
				{
					itemLabel = item.getGoodsItemDesc();
				}
			}
			else
			{
				itemLabel = StringUtils.EMPTY;
			}
		}

		return itemLabel;
	}

	/**
	 * Sets the item label.
	 *
	 * @param itemLabel
	 *           the itemLabel to set
	 */
	public void setItemLabel(final String itemLabel)
	{
		this.itemLabel = itemLabel;
	}



	/**
	 * Gets the current service.
	 *
	 * @return the currentService
	 */
	@Override
	public Service getCurrentService()
	{
		return currentService;
	}

	/**
	 * Sets the current service.
	 *
	 * @param currentService
	 *           the currentService to set
	 */
	@Override
	public void setCurrentService(final Service currentService)
	{
		this.currentService = currentService;
	}

	/**
	 * Gets the selected service.
	 *
	 * @return the selectedService
	 */
	public Service getSelectedService()
	{
		return selectedService;
	}

	/**
	 * Sets the selected service.
	 *
	 * @param selectedService
	 *           the selectedService to set
	 */
	public void setSelectedService(final Service selectedService)
	{
		this.selectedService = selectedService;
	}

	/**
	 * Gets the attached to service.
	 *
	 * @return the attachedToService
	 */
	public Boolean getAttachedToService()
	{
		return attachedToService;
	}

	/**
	 * Sets the attached to service.
	 *
	 * @param attachedToService
	 *           the attachedToService to set
	 */
	public void setAttachedToService(final Boolean attachedToService)
	{
		this.attachedToService = attachedToService;
	}

	/**
	 * Gets the checks if is inactive.
	 *
	 * @return the isInactive
	 */
	public Boolean getIsInactive()
	{
		return isInactive;
	}

	/**
	 * Sets the checks if is inactive.
	 *
	 * @param isInactive
	 *           the isInactive to set
	 */
	public void setIsInactive(final Boolean isInactive)
	{
		this.isInactive = isInactive;
	}


	/**
	 * Gets the sub family item.
	 *
	 * @return the subFamilyItem
	 */
	public List<ServicesItem> getSubFamilyItem()
	{
		return subFamilyItem;
	}

	/**
	 * Sets the sub family item.
	 *
	 * @param subFamilyItem
	 *           the subFamilyItem to set
	 */
	public void setSubFamilyItem(final List<ServicesItem> subFamilyItem)
	{
		this.subFamilyItem = subFamilyItem;
	}

	/**
	 * Gets the attached to other service.
	 *
	 * @return the attachedToOtherService
	 */
	public Boolean getAttachedToOtherService()
	{
		return attachedToOtherService;
	}

	/**
	 * Sets the attached to other service.
	 *
	 * @param attachedToOtherService
	 *           the attachedToOtherService to set
	 */
	public void setAttachedToOtherService(final Boolean attachedToOtherService)
	{
		this.attachedToOtherService = attachedToOtherService;
	}

	/**
	 * Gets the selected sub department.
	 *
	 * @return the selectedSubDepartment
	 */
	public SubDepartment getSelectedSubDepartment()
	{
		return selectedSubDepartment;
	}

	/**
	 * Sets the selected sub department.
	 *
	 * @param selectedSubDepartment
	 *           the selectedSubDepartment to set
	 */
	public void setSelectedSubDepartment(final SubDepartment selectedSubDepartment)
	{
		this.selectedSubDepartment = selectedSubDepartment;
	}

	/**
	 * Gets the sub department list.
	 *
	 * @return the subDepartmentList
	 */
	public List<SubDepartment> getSubDepartmentList()
	{
		return subDepartmentList;
	}

	/**
	 * Sets the sub department list.
	 *
	 * @param subDepartmentList
	 *           the subDepartmentList to set
	 */
	public void setSubDepartmentList(final List<SubDepartment> subDepartmentList)
	{
		this.subDepartmentList = subDepartmentList;
	}


}
