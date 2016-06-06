package org.guce.siat.web.ct.controller;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.data.FieldGroupDto;
import org.guce.siat.common.model.Attachment;
import org.guce.siat.common.model.FieldGroup;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemField;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.model.Flow;
import org.guce.siat.common.model.Organism;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.User;
import org.guce.siat.common.model.UserAuthorityFileType;
import org.guce.siat.common.service.ApplicationPropretiesService;
import org.guce.siat.common.service.FieldGroupService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FileService;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.common.service.FlowService;
import org.guce.siat.common.service.ItemFlowService;
import org.guce.siat.common.service.ServiceService;
import org.guce.siat.common.service.UserAuthorityFileTypeService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.RepetableUtil;
import org.guce.siat.common.utils.Tab;
import org.guce.siat.web.common.ControllerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class FimexDetailController.
 */
@ManagedBean(name = "fimexDetailController")
@SessionScoped
public class FimexDetailController implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2087435973539682236L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(FimexDetailController.class);

	/** The Constant STYLE_CLASS. */
	protected static final String STYLE_CLASS = "required-star-right";

	/** The Constant LOCAL_BUNDLE_NAME. */
	protected static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";

	/** The flow service. */
	@ManagedProperty(value = "#{flowService}")
	private FlowService flowService;

	/** The file item service. */
	@ManagedProperty(value = "#{fileItemService}")
	private FileItemService fileItemService;

	/** The item flow service. */
	@ManagedProperty(value = "#{itemFlowService}")
	private ItemFlowService itemFlowService;

	/** The file service. */
	@ManagedProperty(value = "#{fileService}")
	private FileService fileService;

	/** The file type service. */
	@ManagedProperty(value = "#{fileTypeService}")
	private FileTypeService fileTypeService;

	/** The index page url. */
	private String indexPageUrl;

	/** The flows. */
	private List<Flow> flows;

	/** The current file. */
	private File currentFile;

	/** The attachment list. */
	private List<Attachment> attachmentList;

	/** The selected attachment. */
	private Attachment selectedAttachment;

	/** The allowed user. */
	private Boolean allowedUser;

	/** The product info items filtred. */
	private List<FileItem> productInfoItemsFiltred;

	/** The logged user. */
	private User loggedUser;

	/** The current organism. */
	private Organism currentOrganism;

	/** The current service. */
	private Service currentService;

	/** The id attachment. */
	private String idAttachment;

	/** The list user authority file types. */
	List<UserAuthorityFileType> listUserAuthorityFileTypes;

	/** The selected file item. */
	private FileItem selectedFileItem;

	/** The product info items. */
	private List<FileItem> productInfoItems;

	/** The tab list. */
	private List<Tab> tabList;

	/** The tab index list. */
	private String tabIndexList;

	/** The file field values. */
	private List<FileFieldValue> fileFieldValues;

	/** The file item field values. */
	private List<FileItemFieldValue> fileItemFieldValues;

	/** The field groups. */
	private List<FieldGroup> fieldGroups;
	/** The field groups items. */
	private List<FieldGroup> fieldGroupsItems = new ArrayList<FieldGroup>();
	/** The file field group dtos. */
	private List<FieldGroupDto<FileFieldValue>> fileFieldGroupDtos;

	/** The file item field group dtos. */
	private List<FieldGroupDto<FileItemFieldValue>> fileItemFieldGroupDtos;

	/** The show show attachment form. */
	private Boolean showShowAttachmentForm;

	/** The show product details form. */
	private Boolean showProductDetailsForm;

	/** The service service. */
	@ManagedProperty(value = "#{serviceService}")
	private ServiceService serviceService;

	/** The user authority file type service. */
	@ManagedProperty(value = "#{userAuthorityFileTypeService}")
	private UserAuthorityFileTypeService userAuthorityFileTypeService;

	/** The application propreties service. */
	@ManagedProperty(value = "#{applicationPropretiesService}")
	private ApplicationPropretiesService applicationPropretiesService;

	/** The field group service. */
	@ManagedProperty(value = "#{fieldGroupService}")
	private FieldGroupService fieldGroupService;


	/**
	 * Instantiates a new file item detail controller.
	 */
	public FimexDetailController()
	{
	}

	/**
	 * Inits the.
	 */
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, FimexDetailController.class.getName());
		}

		selectedFileItem = CollectionUtils.isNotEmpty(currentFile.getFileItemsList()) ? currentFile.getFileItemsList().get(0)
				: null;

		//List des user authority par FileType du current File
		listUserAuthorityFileTypes = userAuthorityFileTypeService.findUserAuthorityFileTypeByFileTypeAndUserList(
				currentFile.getFileType(), getLoggedUser().getMergedDelegatorList());

		initAttachmentView();
		setAttachmentList(null);

		//Initializer la liste des attachment du current File
		attachmentList = getCurrentFile().getAttachmentsList();

		//Initialiser la vue des détails produit
		setShowProductDetailsForm(true);

		//Initialiser la vue de la pièce jointe
		setShowShowAttachmentForm(false);

		fieldGroups = fieldGroupService.findAllByFileType(currentFile.getFileType(), "01");
		fileFieldGroupDtos = RepetableUtil.groupFileFieldValues(currentFile.getNonRepeatablefileFieldValueList(),
				currentFile.getRepeatablefileFieldValueList(), fieldGroups, applicationPropretiesService, selectedFileItem,
				getCurrentLocale());
		//Remplir la liste des filed Values du dossier
		//loadAndGroupFileFieldValues();

		//Remplir la liste des valeurs des filed Values pour le premier article
		loadAndGroupFileItemFieldValues();

		tabList = new ArrayList<Tab>();

		tabIndexList = concatenateActiveIndexString(tabList);
	}



	/**
	 * Show attachment Pour afficher la liste des attachement du File Courrant.
	 */
	public void showAttachment()
	{
		setShowProductDetailsForm(false);
		setShowShowAttachmentForm(true);
		//Passer l'Attachement sséléctioné au AttachmentController
		final AttachmentController attachmentController = getInstanceOfPageAttachmentController();
		attachmentController.setSelectedAttachment(selectedAttachment);
		attachmentController.init();
	}

	/**
	 * Gets the instance of page attachment controller.
	 *
	 * @return the instance of page attachment controller
	 */
	public AttachmentController getInstanceOfPageAttachmentController()
	{
		final FacesContext fctx = FacesContext.getCurrentInstance();
		final Application application = fctx.getApplication();
		final ELContext context = fctx.getELContext();
		final ExpressionFactory expressionFactory = application.getExpressionFactory();
		final ValueExpression createValueExpression = expressionFactory.createValueExpression(context, "#{attachmentController}",
				AttachmentController.class);
		return (AttachmentController) createValueExpression.getValue(context);
	}

	/**
	 * Inits the attachment view : Initialzer la Vue de l'attachment ViewPdf.xhtml selon le fichier selectioné
	 */
	public void initAttachmentView()
	{
		final AttachmentController attachmentController = getInstanceOfPageAttachmentController();
		attachmentController.setSelectedAttachment(null);
		attachmentController.setShowPanelViewJpeg(false);
		attachmentController.setShowPanelViewPdf(false);
		attachmentController.init();
	}



	/**
	 * Remplir la liste des valeurs des filed Values pour le un article.
	 */
	private void loadAndGroupFileItemFieldValues()
	{
		fileItemFieldValues = selectedFileItem.getFileItemFieldValueList();
		groupFileItemFieldValues();
	}

	/**
	 * Go to detail page.
	 */
	public void goToDetailPage()
	{
		try
		{
			setIndexPageUrl(ControllerConstants.Pages.FO.DASHBOARD_FIMEX_INDEX_PAGE);
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();

			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, indexPageUrl));

			extContext.redirect(url);
		}
		catch (final IOException ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Gets the current locale.
	 *
	 *
	 * @return the current locale
	 */
	public Locale getCurrentLocale()
	{
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();

	}

	/**
	 * Gets the logged user.
	 *
	 * @return the logged user
	 */
	public User getLoggedUser()
	{
		if (loggedUser == null)
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				loggedUser = (User) session.getAttribute("loggedUser");
			}
		}
		return loggedUser;
	}

	/**
	 * Gets the current organism.
	 *
	 * @return the current organism
	 */
	public Organism getCurrentOrganism()
	{
		if (currentOrganism == null)
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				currentOrganism = (Organism) session.getAttribute("currentOrganism");
			}
		}
		return currentOrganism;
	}

	/**
	 * Gets the current service.
	 *
	 * @return the current service
	 */
	public Service getCurrentService()
	{
		if (currentService == null)
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			if (context != null)
			{
				final HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
				currentService = (Service) session.getAttribute("currentService");
			}
		}
		return currentService;
	}


	/**
	 * Concatenate active index string.
	 *
	 * @param tabList
	 *           the tab list
	 * @return the string
	 */
	public String concatenateActiveIndexString(final List<Tab> tabList)
	{
		final StringBuilder out = new StringBuilder();
		for (int i = 0; i < tabList.size(); i++)
		{
			out.append(i);
			if (i + 1 != tabList.size())
			{
				out.append(applicationPropretiesService.getColumnSeparator());
			}
		}
		return out.toString();
	}






	/**
	 * Group file item field values.
	 */
	private void groupFileItemFieldValues()
	{
		if (fieldGroupsItems.isEmpty())
		{
			fieldGroupsItems = fieldGroupService.findAllByFileType(currentFile.getFileType(), "02");
		}
		fileItemFieldGroupDtos = new ArrayList<FieldGroupDto<FileItemFieldValue>>();
		for (final FieldGroup fieldGroup : fieldGroupsItems)
		{
			final FieldGroupDto<FileItemFieldValue> fileItemFieldGroupDto = new FieldGroupDto<FileItemFieldValue>();
			fileItemFieldGroupDto.setLabelFr(fieldGroup.getLabelFr());
			fileItemFieldGroupDto.setLabelEn(fieldGroup.getLabelEn());
			final List<FileItemFieldValue> listFileItemFieldValues = new ArrayList<FileItemFieldValue>();
			for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValues)
			{
				if (fieldGroup.getId().equals(fileItemFieldValue.getFileItemField().getGroup().getId()))
				{
					listFileItemFieldValues.add(fileItemFieldValue);
				}
			}
			fileItemFieldGroupDto.setFieldValues(listFileItemFieldValues);
			if (fieldGroup.getId().equals(1L))
			{
				populateFileItemGeneralGroup(fileItemFieldGroupDto);
			}
			if (!listFileItemFieldValues.isEmpty())
			{
				fileItemFieldGroupDtos.add(fileItemFieldGroupDto);
			}
		}
	}


	/**
	 * Populate file item general group.
	 *
	 * @param fileFieldGroupDto
	 *           the file field group dto
	 */
	private void populateFileItemGeneralGroup(final FieldGroupDto<FileItemFieldValue> fileFieldGroupDto)
	{

		final List<FileItemFieldValue> fieldValues = fileFieldGroupDto.getFieldValues();

		if (selectedFileItem.getNsh() != null)
		{
			// NSH : GoodsItemDesc
			final String goodsItemDescLabelFr = ResourceBundle
					.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("ProductDetailsLabel_item");
			final String goodsItemDescLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
					Locale.ENGLISH).getString("ProductDetailsLabel_item");
			final FileItemField fileItemFieldGoodsItemDesc = new FileItemField();
			fileItemFieldGoodsItemDesc.setLabelFr(goodsItemDescLabelFr);
			fileItemFieldGoodsItemDesc.setLabelEn(goodsItemDescLabelEn);
			final FileItemFieldValue goodsItemDesc = new FileItemFieldValue();
			goodsItemDesc.setValue(getCurrentLocale().equals(Locale.FRANCE) ? selectedFileItem.getNsh().getGoodsItemDesc()
					: selectedFileItem.getNsh().getGoodsItemDescEn());
			goodsItemDesc.setFileItemField(fileItemFieldGoodsItemDesc);
			fieldValues.add(0, goodsItemDesc);

			// NSH : GoodsItemCode
			final String goodsItemCodeLabelFr = ResourceBundle
					.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("ProductDetailsLabel_nsh");
			final String goodsItemCodeLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
					Locale.ENGLISH).getString("ProductDetailsLabel_nsh");
			final FileItemField fileItemFieldGoodsItemCode = new FileItemField();
			fileItemFieldGoodsItemCode.setLabelFr(goodsItemCodeLabelFr);
			fileItemFieldGoodsItemCode.setLabelEn(goodsItemCodeLabelEn);
			final FileItemFieldValue goodsItemCode = new FileItemFieldValue();
			goodsItemCode.setValue(selectedFileItem.getNsh().getGoodsItemCode());
			goodsItemCode.setFileItemField(fileItemFieldGoodsItemCode);
			fieldValues.add(0, goodsItemCode);
		}

		if (selectedFileItem.getSubfamily() != null)
		{
			// Subfamily : label
			final String subfamilyLabelLabelFr = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
					Locale.FRANCE).getString("ProductDetailsLabel_subfamily");
			final String subfamilyLabelLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
					Locale.ENGLISH).getString("ProductDetailsLabel_subfamily");
			final FileItemField fileItemFieldSubfamilyLabel = new FileItemField();
			fileItemFieldSubfamilyLabel.setLabelFr(subfamilyLabelLabelFr);
			fileItemFieldSubfamilyLabel.setLabelEn(subfamilyLabelLabelEn);
			final FileItemFieldValue subfamilyLabel = new FileItemFieldValue();
			subfamilyLabel.setValue(selectedFileItem.getSubfamily().getLabel());
			subfamilyLabel.setFileItemField(fileItemFieldSubfamilyLabel);
			fieldValues.add(0, subfamilyLabel);

			// Subfamily : code
			final String subfamilyCodeLabelFr = ResourceBundle
					.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("ProductDetailsLabel_code");
			final String subfamilyCodeLabelEn = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
					Locale.ENGLISH).getString("ProductDetailsLabel_code");
			final FileItemField fileItemFieldSubfamilyCode = new FileItemField();
			fileItemFieldSubfamilyCode.setLabelFr(subfamilyCodeLabelFr);
			fileItemFieldSubfamilyCode.setLabelEn(subfamilyCodeLabelEn);
			final FileItemFieldValue subfamilyCode = new FileItemFieldValue();
			subfamilyCode.setValue(selectedFileItem.getSubfamily().getCode());
			subfamilyCode.setFileItemField(fileItemFieldSubfamilyCode);
			fieldValues.add(0, subfamilyCode);
		}

		fileFieldGroupDto.setFieldValues(fieldValues);

	}

	/**
	 * Gets the flow service.
	 *
	 * @return the flow service
	 */
	public FlowService getFlowService()
	{
		return flowService;
	}

	/**
	 * Sets the flow service.
	 *
	 * @param flowService
	 *           the new flow service
	 */
	public void setFlowService(final FlowService flowService)
	{
		this.flowService = flowService;
	}

	/**
	 * Gets the flows.
	 *
	 * @return the flows
	 */
	public List<Flow> getFlows()
	{
		return flows;
	}

	/**
	 * Sets the flows.
	 *
	 * @param flows
	 *           the new flows
	 */
	public void setFlows(final List<Flow> flows)
	{
		this.flows = flows;
	}

	/**
	 * Gets the file item service.
	 *
	 * @return the file item service
	 */
	public FileItemService getFileItemService()
	{
		return fileItemService;
	}

	/**
	 * Sets the file item service.
	 *
	 * @param fileItemService
	 *           the new file item service
	 */
	public void setFileItemService(final FileItemService fileItemService)
	{
		this.fileItemService = fileItemService;
	}

	/**
	 * Gets the item flow service.
	 *
	 * @return the item flow service
	 */
	public ItemFlowService getItemFlowService()
	{
		return itemFlowService;
	}

	/**
	 * Sets the item flow service.
	 *
	 * @param itemFlowService
	 *           the new item flow service
	 */
	public void setItemFlowService(final ItemFlowService itemFlowService)
	{
		this.itemFlowService = itemFlowService;
	}

	/**
	 * Gets the file service.
	 *
	 * @return the file service
	 */
	public FileService getFileService()
	{
		return fileService;
	}

	/**
	 * Sets the file service.
	 *
	 * @param fileService
	 *           the new file service
	 */
	public void setFileService(final FileService fileService)
	{
		this.fileService = fileService;
	}

	/**
	 * Gets the current file.
	 *
	 * @return the current file
	 */
	public File getCurrentFile()
	{
		return currentFile;
	}

	/**
	 * Sets the current file.
	 *
	 * @param currentFile
	 *           the new current file
	 */
	public void setCurrentFile(final File currentFile)
	{
		this.currentFile = currentFile;
	}

	/**
	 * Gets the attachment list.
	 *
	 * @return the attachment list
	 */
	public List<Attachment> getAttachmentList()
	{
		return attachmentList;
	}

	/**
	 * Sets the attachment list.
	 *
	 * @param attachmentList
	 *           the new attachment list
	 */
	public void setAttachmentList(final List<Attachment> attachmentList)
	{
		this.attachmentList = attachmentList;
	}

	/**
	 * Gets the selected attachment.
	 *
	 * @return the selected attachment
	 */
	public Attachment getSelectedAttachment()
	{
		return selectedAttachment;
	}

	/**
	 * Sets the selected attachment.
	 *
	 * @param selectedAttachment
	 *           the new selected attachment
	 */
	public void setSelectedAttachment(final Attachment selectedAttachment)
	{
		this.selectedAttachment = selectedAttachment;
	}

	/**
	 * Sets the logged user.
	 *
	 * @param loggedUser
	 *           the loggedUser to set
	 */
	public void setLoggedUser(final User loggedUser)
	{
		this.loggedUser = loggedUser;
	}

	/**
	 * Gets the id attachment.
	 *
	 * @return the id attachment
	 */
	public String getIdAttachment()
	{
		return idAttachment;
	}

	/**
	 * Sets the current service.
	 *
	 * @param currentService
	 *           the new current service
	 */
	public void setCurrentService(final Service currentService)
	{
		this.currentService = currentService;
	}

	/**
	 * Sets the id attachment.
	 *
	 * @param idAttachment
	 *           the new id attachment
	 */
	public void setIdAttachment(final String idAttachment)
	{
		this.idAttachment = idAttachment;
	}

	/**
	 * Gets the index page url.
	 *
	 * @return the index page url
	 */
	public String getIndexPageUrl()
	{
		return indexPageUrl;
	}

	/**
	 * Sets the index page url.
	 *
	 * @param indexPageUrl
	 *           the new index page url
	 */
	public void setIndexPageUrl(final String indexPageUrl)
	{
		this.indexPageUrl = indexPageUrl;
	}

	/**
	 * Gets the allowed user.
	 *
	 * @return the allowed user
	 */
	public Boolean getAllowedUser()
	{
		return allowedUser;
	}

	/**
	 * Sets the allowed user.
	 *
	 * @param allowedUser
	 *           the new allowed user
	 */
	public void setAllowedUser(final Boolean allowedUser)
	{
		this.allowedUser = allowedUser;
	}

	/**
	 * Gets the list user authority file types.
	 *
	 * @return the listUserAuthorityFileTypes
	 */
	public List<UserAuthorityFileType> getListUserAuthorityFileTypes()
	{
		return listUserAuthorityFileTypes;
	}

	/**
	 * Sets the list user authority file types.
	 *
	 * @param listUserAuthorityFileTypes
	 *           the listUserAuthorityFileTypes to set
	 */
	public void setListUserAuthorityFileTypes(final List<UserAuthorityFileType> listUserAuthorityFileTypes)
	{
		this.listUserAuthorityFileTypes = listUserAuthorityFileTypes;
	}

	/**
	 * Gets the user authority file type service.
	 *
	 * @return the userAuthorityFileTypeService
	 */
	public UserAuthorityFileTypeService getUserAuthorityFileTypeService()
	{
		return userAuthorityFileTypeService;
	}

	/**
	 * Sets the user authority file type service.
	 *
	 * @param userAuthorityFileTypeService
	 *           the userAuthorityFileTypeService to set
	 */
	public void setUserAuthorityFileTypeService(final UserAuthorityFileTypeService userAuthorityFileTypeService)
	{
		this.userAuthorityFileTypeService = userAuthorityFileTypeService;
	}

	/**
	 * Gets the product info items filtred.
	 *
	 * @return the productInfoItemsFiltred
	 */
	public List<FileItem> getProductInfoItemsFiltred()
	{
		return productInfoItemsFiltred;
	}

	/**
	 * Sets the product info items filtred.
	 *
	 * @param productInfoItemsFiltred
	 *           the productInfoItemsFiltred to set
	 */
	public void setProductInfoItemsFiltred(final List<FileItem> productInfoItemsFiltred)
	{
		this.productInfoItemsFiltred = productInfoItemsFiltred;
	}

	/**
	 * Gets the selected file item.
	 *
	 * @return the selectedFileItem
	 */
	public FileItem getSelectedFileItem()
	{
		return selectedFileItem;
	}

	/**
	 * Sets the selected file item.
	 *
	 * @param selectedFileItem
	 *           the selectedFileItem to set
	 */
	public void setSelectedFileItem(final FileItem selectedFileItem)
	{
		this.selectedFileItem = selectedFileItem;
	}

	/**
	 * Gets the product info items.
	 *
	 * @return the productInfoItems
	 */
	public List<FileItem> getProductInfoItems()
	{
		return productInfoItems;
	}

	/**
	 * Sets the product info items.
	 *
	 * @param productInfoItems
	 *           the productInfoItems to set
	 */
	public void setProductInfoItems(final List<FileItem> productInfoItems)
	{
		this.productInfoItems = productInfoItems;
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
	 * Gets the file field values.
	 *
	 * @return the fileFieldValues
	 */
	public List<FileFieldValue> getFileFieldValues()
	{
		return fileFieldValues;
	}

	/**
	 * Sets the file field values.
	 *
	 * @param fileFieldValues
	 *           the fileFieldValues to set
	 */
	public void setFileFieldValues(final List<FileFieldValue> fileFieldValues)
	{
		this.fileFieldValues = fileFieldValues;
	}

	/**
	 * Gets the file item field values.
	 *
	 * @return the fileItemFieldValues
	 */
	public List<FileItemFieldValue> getFileItemFieldValues()
	{
		return fileItemFieldValues;
	}

	/**
	 * Sets the file item field values.
	 *
	 * @param fileItemFieldValues
	 *           the fileItemFieldValues to set
	 */
	public void setFileItemFieldValues(final List<FileItemFieldValue> fileItemFieldValues)
	{
		this.fileItemFieldValues = fileItemFieldValues;
	}

	/**
	 * Gets the show product details form.
	 *
	 * @return the show product details form
	 */
	public Boolean getShowProductDetailsForm()
	{
		return showProductDetailsForm;
	}

	/**
	 * Sets the show product details form.
	 *
	 * @param showProductDetailsForm
	 *           the new show product details form
	 */
	public void setShowProductDetailsForm(final Boolean showProductDetailsForm)
	{
		this.showProductDetailsForm = showProductDetailsForm;
	}

	/**
	 * Gets the show show attachment form.
	 *
	 * @return the show show attachment form
	 */
	public Boolean getShowShowAttachmentForm()
	{
		return showShowAttachmentForm;
	}

	/**
	 * Sets the show show attachment form.
	 *
	 * @param showShowAttachmentForm
	 *           the new show show attachment form
	 */
	public void setShowShowAttachmentForm(final Boolean showShowAttachmentForm)
	{
		this.showShowAttachmentForm = showShowAttachmentForm;
	}

	/**
	 * Gets the tab list.
	 *
	 * @return the tab list
	 */
	public List<Tab> getTabList()
	{
		return tabList;
	}

	/**
	 * Sets the tab list.
	 *
	 * @param tabList
	 *           the new tab list
	 */
	public void setTabList(final List<Tab> tabList)
	{
		this.tabList = tabList;
	}

	/**
	 * Gets the application propreties service.
	 *
	 * @return the application propreties service
	 */
	public ApplicationPropretiesService getApplicationPropretiesService()
	{
		return applicationPropretiesService;
	}

	/**
	 * Sets the application propreties service.
	 *
	 * @param applicationPropretiesService
	 *           the new application propreties service
	 */
	public void setApplicationPropretiesService(final ApplicationPropretiesService applicationPropretiesService)
	{
		this.applicationPropretiesService = applicationPropretiesService;
	}

	/**
	 * Gets the tab index list.
	 *
	 * @return the tab index list
	 */
	public String getTabIndexList()
	{
		return tabIndexList;
	}

	/**
	 * Sets the tab index list.
	 *
	 * @param tabIndexList
	 *           the new tab index list
	 */
	public void setTabIndexList(final String tabIndexList)
	{
		this.tabIndexList = tabIndexList;
	}

	/**
	 * Gets the field groups.
	 *
	 * @return the field groups
	 */
	public List<FieldGroup> getFieldGroups()
	{
		return fieldGroups;
	}

	/**
	 * Sets the field groups.
	 *
	 * @param fieldGroups
	 *           the new field groups
	 */
	public void setFieldGroups(final List<FieldGroup> fieldGroups)
	{
		this.fieldGroups = fieldGroups;
	}

	/**
	 * Gets the field group service.
	 *
	 * @return the field group service
	 */
	public FieldGroupService getFieldGroupService()
	{
		return fieldGroupService;
	}

	/**
	 * Sets the field group service.
	 *
	 * @param fieldGroupService
	 *           the new field group service
	 */
	public void setFieldGroupService(final FieldGroupService fieldGroupService)
	{
		this.fieldGroupService = fieldGroupService;
	}

	/**
	 * Gets the file field group dtos.
	 *
	 * @return the file field group dtos
	 */
	public List<FieldGroupDto<FileFieldValue>> getFileFieldGroupDtos()
	{
		return fileFieldGroupDtos;
	}

	/**
	 * Sets the file field group dtos.
	 *
	 * @param fileFieldGroupDtos
	 *           the new file field group dtos
	 */
	public void setFileFieldGroupDtos(final List<FieldGroupDto<FileFieldValue>> fileFieldGroupDtos)
	{
		this.fileFieldGroupDtos = fileFieldGroupDtos;
	}

	/**
	 * Gets the file item field group dtos.
	 *
	 * @return the file item field group dtos
	 */
	public List<FieldGroupDto<FileItemFieldValue>> getFileItemFieldGroupDtos()
	{
		return fileItemFieldGroupDtos;
	}

	/**
	 * Sets the file item field group dtos.
	 *
	 * @param fileItemFieldGroupDtos
	 *           the new file item field group dtos
	 */
	public void setFileItemFieldGroupDtos(final List<FieldGroupDto<FileItemFieldValue>> fileItemFieldGroupDtos)
	{
		this.fileItemFieldGroupDtos = fileItemFieldGroupDtos;
	}

	/**
	 * Gets the file type service.
	 *
	 * @return the file type service
	 */
	public FileTypeService getFileTypeService()
	{
		return fileTypeService;
	}

	/**
	 * Sets the file type service.
	 *
	 * @param fileTypeService
	 *           the new file type service
	 */
	public void setFileTypeService(final FileTypeService fileTypeService)
	{
		this.fileTypeService = fileTypeService;
	}

	/**
	 * Gets the field groups items.
	 *
	 * @return the field groups items
	 */
	public List<FieldGroup> getFieldGroupsItems()
	{
		return fieldGroupsItems;
	}

	/**
	 * Sets the field groups items.
	 *
	 * @param fieldGroupsItems
	 *           the new field groups items
	 */
	public void setFieldGroupsItems(final List<FieldGroup> fieldGroupsItems)
	{
		this.fieldGroupsItems = fieldGroupsItems;
	}

}
