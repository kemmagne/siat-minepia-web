package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.guce.siat.common.model.File;
import org.guce.siat.common.service.FileService;
import org.guce.siat.common.service.ItemFlowService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class FimexController.
 */

@ManagedBean(name = "fimexController")
@ViewScoped
public class FimexController extends AbstractController<File>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4994522163212372773L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(FimexController.class);

	/** The item flow service. */
	@ManagedProperty(value = "#{itemFlowService}")
	private ItemFlowService itemFlowService;

	/** The file service. */
	@ManagedProperty(value = "#{fileService}")
	private FileService fileService;

	/** The detail page url. */
	private String detailPageUrl;

	/** The files list. */
	private List<File> filesList;

	/** The selected file. */
	private File selectedFile;

	/** The from date filter. */
	private Date fromDateFilter;

	/** The to date filter. */
	private Date toDateFilter;

	/**
	 * Instantiates a new file item ap controller.
	 */
	public FimexController()
	{
		super(File.class);
	}


	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, FimexController.class.getName());
		}
		super.setService(fileService);
		super.setPageUrl(ControllerConstants.Pages.FO.DASHBOARD_FIMEX_INDEX_PAGE);
		fromDateFilter = null;
		toDateFilter = null;
		initLoading();
	}

	private void initLoading()
	{
		filesList = fileService.findFilesByFileTypeCode(FileTypeCode.FIMEX);
	}

	/**
	 * Go to detail page.
	 */
	public void goToDetailPage()
	{
		try
		{
			setDetailPageUrl(ControllerConstants.Pages.FO.DETAILS_FIMEX_INDEX_PAGE);
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final FimexDetailController fimexDetailController = getInstanceOfPageFimexDetailController();
			fimexDetailController.setCurrentFile(selectedFile);
			fimexDetailController.init();

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
	 * Filter dashboard.
	 */
	public void filterDashboard()
	{
		initLoading();
		CollectionUtils.filter(filesList, new Predicate()
		{

			@Override
			public boolean evaluate(final Object object)
			{
				if (toDateFilter != null && fromDateFilter != null)
				{
					return ((File) object).getCreatedDate().before(toDateFilter)
							&& ((File) object).getCreatedDate().after(fromDateFilter);
				}
				else if (toDateFilter == null)
				{
					return ((File) object).getCreatedDate().after(fromDateFilter);
				}
				else if (fromDateFilter == null)
				{
					return ((File) object).getCreatedDate().before(toDateFilter);
				}
				return false;
			}
		});
	}

	/**
	 * Gets the instance of page file item ap detail controller.
	 *
	 * @return the instance of page file item ap detail controller
	 */
	public FimexDetailController getInstanceOfPageFimexDetailController()
	{
		final FacesContext fctx = FacesContext.getCurrentInstance();
		final Application application = fctx.getApplication();
		final ELContext context = fctx.getELContext();
		final ExpressionFactory expressionFactory = application.getExpressionFactory();
		final ValueExpression createValueExpression = expressionFactory.createValueExpression(context, "#{fimexDetailController}",
				FimexDetailController.class);
		return (FimexDetailController) createValueExpression.getValue(context);
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
	 * Gets the files list.
	 *
	 * @return the filesList
	 */
	public List<File> getFilesList()
	{
		return filesList;
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
	 * Gets the item flow service.
	 *
	 * @return the itemFlowService
	 */
	public ItemFlowService getItemFlowService()
	{
		return itemFlowService;
	}

	/**
	 * Sets the item flow service.
	 *
	 * @param itemFlowService
	 *           the itemFlowService to set
	 */
	public void setItemFlowService(final ItemFlowService itemFlowService)
	{
		this.itemFlowService = itemFlowService;
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
	 * Gets the from date filter.
	 *
	 * @return the from date filter
	 */
	public Date getFromDateFilter()
	{
		return fromDateFilter;
	}


	/**
	 * Sets the from date filter.
	 *
	 * @param fromDateFilter
	 *           the new from date filter
	 */
	public void setFromDateFilter(final Date fromDateFilter)
	{
		this.fromDateFilter = fromDateFilter;
	}


	/**
	 * Gets the to date filter.
	 *
	 * @return the to date filter
	 */
	public Date getToDateFilter()
	{
		return toDateFilter;
	}


	/**
	 * Sets the to date filter.
	 *
	 * @param toDateFilter
	 *           the new to date filter
	 */
	public void setToDateFilter(final Date toDateFilter)
	{
		this.toDateFilter = toDateFilter;
	}


}
