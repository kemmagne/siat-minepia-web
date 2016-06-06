package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.FileTypeStep;
import org.guce.siat.common.model.User;
import org.guce.siat.common.model.UserAuthorityFileType;
import org.guce.siat.common.service.AuthorityService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.common.service.FileTypeStepService;
import org.guce.siat.common.service.UserAuthorityFileTypeService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.SiatUtils;
import org.guce.siat.common.utils.enums.InformationSystemCode;
import org.guce.siat.common.utils.enums.StepCode;
import org.guce.siat.common.utils.filter.RetrieveSearchFilter;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class RetrieveApController.
 */
@ManagedBean(name = "retrieveApController")
@SessionScoped
public class RetrieveApController extends AbstractController<FileItem>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 212767340072397687L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(RetrieveApController.class);

	/** The Constant DATE_VALIDATION_ERROR_MESSAGE. */
	private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

	/** The detail page url. */
	private String detailPageUrl;

	/** The list user authority file types. */
	private List<UserAuthorityFileType> listUserAuthorityFileTypes;

	/** The files list. */
	private List<File> filesList;

	/** The selected file. */
	private File selectedFile;

	/** The filter. */
	private RetrieveSearchFilter filter;

	/** The file type items. */
	private List<SelectItem> fileTypeItems;

	/** The file item service. */
	@ManagedProperty(value = "#{fileItemService}")
	private FileItemService fileItemService;

	/** The authority service. */
	@ManagedProperty(value = "#{authorityService}")
	private AuthorityService authorityService;

	/** The user authority file type service. */
	@ManagedProperty(value = "#{userAuthorityFileTypeService}")
	private UserAuthorityFileTypeService userAuthorityFileTypeService;

	//	/** The service service. */
	//	@ManagedProperty(value = "#{serviceService}")
	//	private ServiceService serviceService;

	/** The file type service. */
	@ManagedProperty(value = "#{fileTypeService}")
	private FileTypeService fileTypeService;

	/** The file type step service. */
	@ManagedProperty(value = "#{fileTypeStepService}")
	private FileTypeStepService fileTypeStepService;

	/**
	 * Instantiates a new retrieve ap controller.
	 */
	public RetrieveApController()
	{
		super(FileItem.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		LOG.info(Constants.INIT_LOG_INFO_MESSAGE, RetrieveApController.class.getName());
		super.setService(fileItemService);
		super.setPageUrl(ControllerConstants.Pages.FO.RETRIEVE_AP_INDEX_PAGE);
		populateFileTypeItems();
	}

	/**
	 * Populate file type items.
	 */
	private void populateFileTypeItems()
	{
		fileTypeItems = new ArrayList<SelectItem>();
		final List<FileType> fileTypes = fileTypeService.findDistinctFileTypesByUser(getLoggedUser());
		for (final FileType fileType : fileTypes)
		{
			fileTypeItems.add(new SelectItem(fileType, "fr".equals(getCurrentLocaleCode()) ? fileType.getLabelFr() : fileType
					.getLabelEn()));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#goToPage()
	 */
	@Override
	public void goToPage()
	{
		initRetrieveSearch();
		try
		{
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, pageUrl));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{
			LOG.error(ioe.getMessage(), ioe);
		}
	}

	/**
	 * Go to detail page.
	 */
	public void goToDetailPage()
	{
		try
		{
			setDetailPageUrl(ControllerConstants.Pages.FO.DETAILS_AP_INDEX_PAGE);
			refreshItems();
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final FileItemApDetailController fileItemApDetailController = getInstanceOfPageFileItemApDetailController();
			fileItemApDetailController.setCurrentFile(selectedFile);
			fileItemApDetailController.setComeFromRetrieveAp(false);
			if (CollectionUtils.isNotEmpty(selectedFile.getFileItemsList())
					&& selectedFile.getFileItemsList().get(0).getStep().getStepCode().equals(StepCode.ST_AP_44))
			{
				fileItemApDetailController.setComeFromRetrieveAp(true);
			}
			fileItemApDetailController.init();

			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler()
					.getActionURL(context, detailPageUrl));

			extContext.redirect(url);
		}
		catch (final IOException ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Gets the instance of page file item ap detail controller.
	 *
	 * @return the instance of page file item ap detail controller
	 */
	public FileItemApDetailController getInstanceOfPageFileItemApDetailController()
	{
		final FacesContext fctx = FacesContext.getCurrentInstance();
		final Application application = fctx.getApplication();
		final ELContext context = fctx.getELContext();
		final ExpressionFactory expressionFactory = application.getExpressionFactory();
		final ValueExpression createValueExpression = expressionFactory.createValueExpression(context,
				"#{fileItemApDetailController}", FileItemApDetailController.class);
		return (FileItemApDetailController) createValueExpression.getValue(context);
	}

	/**
	 * Gets the files list.
	 *
	 * @return the filesList
	 */
	public List<File> getFilesList()
	{
		FileTypeStep fileTypeStep = null;
		filesList = new ArrayList<File>();
		if (CollectionUtils.isNotEmpty(items))
		{
			final List<FileItem> fileItems = items;
			for (final FileItem fileItem : fileItems)
			{
				if (!filesList.contains(fileItem.getFile()))
				{
					filesList.add(fileItem.getFile());
				}
			}
			if (CollectionUtils.isNotEmpty(filesList))
			{

				for (final File file : filesList)
				{
					if (CollectionUtils.isNotEmpty(file.getFileItemsList()))
					{
						for (final FileItem fileItem : file.getFileItemsList())
						{

							fileTypeStep = fileTypeStepService.findFileTypeStepByFileTypeAndStep(fileItem.getFile().getFileType(),
									fileItem.getStep());
							if (fileTypeStep != null && fileTypeStep.getLabelFr() != null)
							{
								fileItem.setRedefinedLabelEn(fileTypeStep.getLabelEn());
								fileItem.setRedefinedLabelFr(fileTypeStep.getLabelFr());
							}
						}
					}

				}
			}
		}
		return filesList;
	}

	/**
	 * Do search by filter.
	 */
	public void doSearchByFilter()
	{
		if (filter.getFromDate() != null && filter.getToDate() != null && filter.getFromDate().after(filter.getToDate()))
		{
			JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					DATE_VALIDATION_ERROR_MESSAGE));
			return;
		}
		else
		{
			try
			{
				listUserAuthorityFileTypes = userAuthorityFileTypeService.findUserAuthorityFileTypeByUserList(getLoggedUser()
						.getMergedDelegatorList());

				// Merge the logged user and their delegator users list in the list
				final List<Administration> administrationList = new ArrayList<Administration>();

				if (getLoggedUser().getMergedDelegatorList() != null)
				{
					for (final User user : getLoggedUser().getMergedDelegatorList())
					{
						administrationList.add(user.getAdministration());
					}
				}

				// get the services id for the administration of the logged user and their delegator users
				final List<Bureau> bureauList = SiatUtils.findCombinedBureausByAdministrationList(administrationList);

				items = fileItemService.findFileItemForRetreiveByFilter(bureauList, getLoggedUser(), InformationSystemCode.AP,
						listUserAuthorityFileTypes, filter, StepCode.ST_AP_44);
			}
			catch (final Exception ex)
			{
				JsfUtil.addErrorMessage(ex,
						ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
			}
		}
	}

	/**
	 * Inits the retrieve search.
	 */
	public void initRetrieveSearch()
	{
		filter = new RetrieveSearchFilter();
		doSearchByFilter();
	}

	/**
	 * Gets the detail page url.
	 *
	 * @return the detail page url
	 */
	public String getDetailPageUrl()
	{
		return detailPageUrl;
	}

	/**
	 * Sets the detail page url.
	 *
	 * @param detailPageUrl
	 *           the new detail page url
	 */
	public void setDetailPageUrl(final String detailPageUrl)
	{
		this.detailPageUrl = detailPageUrl;
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
	 * Gets the authority service.
	 *
	 * @return the authorityService
	 */
	public AuthorityService getAuthorityService()
	{
		return authorityService;
	}

	/**
	 * Sets the authority service.
	 *
	 * @param authorityService
	 *           the authorityService to set
	 */
	public void setAuthorityService(final AuthorityService authorityService)
	{
		this.authorityService = authorityService;
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
	 * Sets the files list.
	 *
	 * @param filesList
	 *           the filesList to set
	 */
	public void setFilesList(final List<File> filesList)
	{
		this.filesList = filesList;
	}

	/**
	 * Gets the selected file.
	 *
	 * @return the selectedFile
	 */
	public File getSelectedFile()
	{
		return selectedFile;
	}

	/**
	 * Sets the selected file.
	 *
	 * @param selectedFile
	 *           the selectedFile to set
	 */
	public void setSelectedFile(final File selectedFile)
	{
		this.selectedFile = selectedFile;
	}

	/**
	 * Gets the filter.
	 *
	 * @return the filter
	 */
	public RetrieveSearchFilter getFilter()
	{
		return filter;
	}

	/**
	 * Sets the filter.
	 *
	 * @param filter
	 *           the filter to set
	 */
	public void setFilter(final RetrieveSearchFilter filter)
	{
		this.filter = filter;
	}

	/**
	 * Gets the file type items.
	 *
	 * @return the fileTypeItems
	 */
	public List<SelectItem> getFileTypeItems()
	{
		return fileTypeItems;
	}

	/**
	 * Sets the file type items.
	 *
	 * @param fileTypeItems
	 *           the fileTypeItems to set
	 */
	public void setFileTypeItems(final List<SelectItem> fileTypeItems)
	{
		this.fileTypeItems = fileTypeItems;
	}

	/**
	 * Gets the date validation error message.
	 *
	 * @return the date validation error message
	 */
	public static String getDateValidationErrorMessage()
	{
		return DATE_VALIDATION_ERROR_MESSAGE;
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
	 * Gets the file type step service.
	 *
	 * @return the file type step service
	 */
	public FileTypeStepService getFileTypeStepService()
	{
		return fileTypeStepService;
	}

	/**
	 * Sets the file type step service.
	 *
	 * @param fileTypeStepService
	 *           the new file type step service
	 */
	public void setFileTypeStepService(final FileTypeStepService fileTypeStepService)
	{
		this.fileTypeStepService = fileTypeStepService;
	}


}
