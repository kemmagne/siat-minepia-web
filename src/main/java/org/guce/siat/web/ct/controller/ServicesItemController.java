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
import org.apache.commons.lang.StringUtils;
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
@ManagedBean(name = "servicesItemController")
@SessionScoped
public class ServicesItemController extends AbstractController<ServicesItem>
{

	/**
	 *
	 */
	private static final long serialVersionUID = -7125872429244340383L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);

	/** The Constant RELATED_SERVICE_ITEM_MESSAGE. */
	private static final String RELATED_SERVICE_ITEM_MESSAGE = "RelatedServiceItem";

	/** The Constant ATTACHED_SERVICE_ITEM_MESSAGE. */
	private static final String ATTACHED_SERVICE_ITEM_MESSAGE = "AttachedServiceItem";

	/** The Constant SERVICE_ITEM_DELETED_MESSAGE. */
	private static final String SERVICE_ITEM_DELETED_MESSAGE = "ServicesItemDeleted";

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

	/** The items nsh list. */
	private List<String> itemsNshList;

	/** The item nsh. */
	private String itemNsh;

	/** The item label. */
	private String itemLabel;

	/** The item code. */
	private String itemCode;

	/** The attachedToOtherService. */
	private Boolean attachedToOtherService;

	/** The exist nsh. */
	private Boolean existNsh;

	/** is inactive item. */
	private Boolean isInactive;


	/** The disable nsh. */
	private Boolean disableNsh;

	/** The related. */
	private Boolean attachedToService;

	/** The items list. */
	private List<ServicesItem> itemsList;

	/** The service list. */
	private List<Service> serviceList;

	/** The related service. */
	private String relatedService;

	/**
	 * Instantiates a new car controller.
	 */
	public ServicesItemController()
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
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, ServicesItemController.class.getName());
		}
		super.setService(servicesItemService);
		super.setPageUrl(ControllerConstants.Pages.BO.SERVICES_ITEM_INDEX_PAGE);
		fetchItems();
	}


	/**
	 * Related item warning.
	 */
	public void relatedItemWarning()
	{
		if (StringUtils.isNotBlank(itemNsh) && selectedService != null)
		{
			checkItemToAdd();
			if (attachedToOtherService)
			{

				JsfUtil.addWarningMessage("Attention ! "
						+ ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(RELATED_SERVICE_ITEM_MESSAGE)
						+ ": " + relatedService);
			}
			if (attachedToService)
			{
				JsfUtil.addErrorMessage("Attention ! "
						+ ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ATTACHED_SERVICE_ITEM_MESSAGE));
			}


		}
	}

	/**
	 * Initiate nsh.
	 */
	public void initiateNsh()
	{
		itemNsh = StringUtils.EMPTY;
		itemLabel = StringUtils.EMPTY;
		if (selectedService != null)
		{
			disableNsh = false;
		}
		else
		{
			disableNsh = true;
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
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					"CreateItemRequiredMessage_Label"));
			return;
		}
		if (attachedToService)
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					ATTACHED_SERVICE_ITEM_MESSAGE));
			return;
		}
		if (isInactive)
		{
			activateItem();
			fetchItems();
			return;
		}

		final Item item = itemService.findByGoodsItemCode(itemNsh);
		handleCalculateCode(itemNsh);
		selected.setNsh(item);
		selected.setCode(itemCode);
		selected.setType('S');
		selected.setService(selectedService);
		selected.setDeleted(false);
		super.create();
		fetchItems();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#edit()
	 */
	@Override
	public void edit()
	{

		checkItemToAdd();
		if (itemLabel.isEmpty() || itemLabel == null)
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					"CreateItemRequiredMessage_Label"));
			return;
		}
		if (attachedToService)
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					ATTACHED_SERVICE_ITEM_MESSAGE));
			return;
		}
		if (isInactive)
		{
			activateItem();
			fetchItems();
			return;
		}

		super.edit();
		fetchItems();

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
		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(SERVICE_ITEM_DELETED_MESSAGE);
		persist(PersistenceActions.UPDATE, msg);
		fetchItems();
	}

	/**
	 * Activate item.
	 */
	public void activateItem()
	{
		selected.setDeleted(false);
		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(SERVICE_ITEM_DELETED_MESSAGE);
		persist(PersistenceActions.UPDATE, msg);
		fetchItems();
	}


	/**
	 * Verify service item in service.
	 *
	 * @param serviceItemList
	 *           the service item list
	 */
	public void verifyServiceItemInService(final List<ServicesItem> serviceItemList)
	{

		for (final ServicesItem servicesItem : serviceItemList)
		{
			if (selectedService != null)
			{
				if ((ServiceItemType.SUBFAMILY.getCode().equals(servicesItem.getType().toString()))
						&& (servicesItem.getService().getId().equals(selectedService.getId()))
						&& (selected.getLabel().equals(servicesItem.getLabel())))
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
				if ((ServiceItemType.SUBFAMILY.getCode().equals(servicesItem.getType().toString())) && !servicesItem.getDeleted()
						&& (selected.getLabel().equals(servicesItem.getLabel())))
				{
					attachedToOtherService = true;
					relatedService += servicesItem.getService().getLabelFr() + ", ";
				}

			}
		}
	}

	/**
	 * Check related service item.
	 */
	public void checkItemToAdd()
	{
		final List<ServicesItem> serviceItemList = servicesItemService.findServicesItemByNsh(itemNsh);

		attachedToService = false;
		attachedToOtherService = false;
		isInactive = false;
		relatedService = StringUtils.EMPTY;

		if (CollectionUtils.isNotEmpty(serviceItemList))
		{
			verifyServiceItemInService(serviceItemList);
		}
		else
		{
			attachedToOtherService = false;
		}

	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(servicesItemService.find(this.getSelected().getId()));
		itemNsh = getSelected().getNsh().getGoodsItemCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
	 */
	@Override
	public void prepareCreate()
	{
		super.prepareCreate();
		itemNsh = StringUtils.EMPTY;
		selected.setLabel(StringUtils.EMPTY);
		itemLabel = StringUtils.EMPTY;
		disableNsh = true;
		selectedService = new Service();
	}



	/**
	 * Gets the NSH list.
	 *
	 * @param query
	 *           the query
	 * @return the NSH list
	 */
	public List<String> getNSHList(final String query)
	{

		List<ServicesItem> nativeItemsList = new ArrayList<ServicesItem>();
		itemsNshList = new ArrayList<String>();
		if (selectedService != null)
		{
			nativeItemsList = servicesItemService.findServicesItemByService(selectedService);
		}
		else
		{
			nativeItemsList = servicesItemService.findServicesItemByOrganism(getCurrentOrganism());
		}
		if (!nativeItemsList.isEmpty())
		{
			for (final ServicesItem item : nativeItemsList)
			{
				if ((ServiceItemType.NATIVE.getCode().equals(item.getType().toString())) && (!item.getDeleted())
						&& (item.getNsh().getGoodsItemCode().startsWith(query)))
				{
					itemsNshList.add(item.getNsh().getGoodsItemCode());
				}
			}
		}

		return itemsNshList;
	}

	/**
	 * Fetch items.
	 */
	public void fetchItems()
	{

		if (getCurrentOrganism() != null)
		{
			final List<ServicesItem> items = servicesItemService.findActiveServicesItemByOrganism(getCurrentOrganism());
			itemsList = new ArrayList<ServicesItem>();
			for (final ServicesItem servicesItem : items)
			{
				if (ServiceItemType.SUBFAMILY.getCode().equals(servicesItem.getType().toString()))
				{
					itemsList.add(servicesItem);
				}
			}
		}
	}


	/**
	 * Handle calculate code.
	 *
	 * @param nsh
	 *           the nsh
	 */
	public void handleCalculateCode(final String nsh)
	{

		final Integer currentMaxCode = servicesItemService.fetchMaxCodeByNsh(nsh);

		if (currentMaxCode != null)
		{
			final Integer nextCode = currentMaxCode + 1;
			if ((0 < nextCode) && (nextCode < Constants.NINE))
			{
				itemCode = "0" + nextCode.toString();
			}
			else
			{
				itemCode = nextCode.toString();
			}

		}
		else
		{
			itemCode = "01";
		}
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
	 * @return the itemsNshList
	 */
	public List<String> getItemsNshList()
	{
		return itemsNshList;
	}


	/**
	 * Sets the items nsh list.
	 *
	 * @param itemsNshList
	 *           the new items nsh list
	 */
	public void setItemsNshList(final List<String> itemsNshList)
	{
		this.itemsNshList = itemsNshList;
	}



	/**
	 * Gets the item nsh.
	 *
	 * @return the itemNsh
	 */
	public String getItemNsh()
	{
		return itemNsh;
	}

	/**
	 * @param itemNsh
	 *           the itemNsh to set
	 */
	public void setItemNsh(final String itemNsh)
	{
		this.itemNsh = itemNsh;
	}

	/**
	 * Gets the item label.
	 *
	 * @return the itemLabel
	 */
	public String getItemLabel()
	{
		if (itemNsh != null && !itemNsh.isEmpty())
		{
			final Boolean exist = checkNshInService(itemNsh);
			if (exist)
			{
				final List<ServicesItem> items = servicesItemService.findServicesItemByNsh(itemNsh);
				if (!items.isEmpty())
				{
					for (final ServicesItem servicesItem : items)
					{
						if (ServiceItemType.NATIVE.getCode().equals(servicesItem.getType().toString()))
						{
							if (Constants.LOCALE_ENGLISH.equals(getCurrentLocaleCode()))
							{
								itemLabel = servicesItem.getNsh().getGoodsItemDescEn();
							}
							else
							{
								itemLabel = servicesItem.getNsh().getGoodsItemDesc();
							}

							break;
						}
					}
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
	 * Check nsh in service: verifie l'existance du NSH dans le service selectionné.
	 *
	 * @param nsh
	 *           the nsh
	 * @return the boolean
	 */
	public Boolean checkNshInService(final String nsh)
	{

		existNsh = false;
		if (itemsNshList != null)
		{
			if (itemsNshList.isEmpty())
			{

				for (final String forcedNsh : itemsNshList)
				{
					if (!nsh.equals(forcedNsh))
					{
						existNsh = false;
					}
					else
					{
						existNsh = true;
						break;
					}
				}

			}
			else
			{
				//cas de l'edit
				existNsh = true;
			}

		}
		else
		{
			//cas de l'edit
			existNsh = true;
		}

		return existNsh;
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
	 * Gets the items list.
	 *
	 * @return the itemsList
	 */
	public List<ServicesItem> getItemsList()
	{
		fetchItems();
		return itemsList;
	}

	/**
	 * Sets the items list.
	 *
	 * @param itemsList
	 *           the itemsList to set
	 */
	public void setItemsList(final List<ServicesItem> itemsList)
	{
		this.itemsList = itemsList;
	}

	/**
	 * Gets the service list.
	 *
	 * @return the serviceList
	 */
	public List<Service> getServiceList()
	{
		if ((serviceList == null) && (getCurrentOrganism() != null))
		{
			serviceList = new ArrayList<Service>();
			for (final SubDepartment subDepartment : getCurrentOrganism().getSubDepartmentsList())
			{
				serviceList.addAll(subDepartment.getServicesList());
			}
		}
		return serviceList;
	}

	/**
	 * Sets the service list.
	 *
	 * @param serviceList
	 *           the serviceList to set
	 */
	public void setServiceList(final List<Service> serviceList)
	{
		this.serviceList = serviceList;
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
	 * Gets the item code.
	 *
	 * @return the itemCode
	 */
	public String getItemCode()
	{
		return itemCode;
	}

	/**
	 * Sets the item code.
	 *
	 * @param itemCode
	 *           the itemCode to set
	 */
	public void setItemCode(final String itemCode)
	{
		this.itemCode = itemCode;
	}

	/**
	 * Gets the related service.
	 *
	 * @return the relatedService
	 */
	public String getRelatedService()
	{
		return relatedService;
	}

	/**
	 * Sets the related service.
	 *
	 * @param relatedService
	 *           the relatedService to set
	 */
	public void setRelatedService(final String relatedService)
	{
		this.relatedService = relatedService;
	}


	/**
	 * Gets the disable nsh.
	 *
	 * @return the disable nsh
	 */
	public Boolean getDisableNsh()
	{
		return disableNsh;
	}

	/**
	 * Sets the disable nsh.
	 *
	 * @param disableNsh
	 *           the new disable nsh
	 */
	public void setDisableNsh(final Boolean disableNsh)
	{
		this.disableNsh = disableNsh;
	}

	/**
	 * Gets the exist nsh.
	 *
	 * @return the existNsh
	 */
	public Boolean getExistNsh()
	{
		return existNsh;
	}

	/**
	 * Sets the exist nsh.
	 *
	 * @param existNsh
	 *           the new exist nsh
	 */
	public void setExistNsh(final Boolean existNsh)
	{
		this.existNsh = existNsh;
	}



}
