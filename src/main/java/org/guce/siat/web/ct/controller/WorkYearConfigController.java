package org.guce.siat.web.ct.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.guce.siat.common.model.HourlyType;
import org.guce.siat.common.model.WorkYearConfig;
import org.guce.siat.common.service.HourlyTypeService;
import org.guce.siat.common.service.WorkYearConfigService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.WorkYearConfigBean;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class WorkYearConfigController.
 */
@ManagedBean(name = "workYearConfigController")
@SessionScoped
public class WorkYearConfigController extends AbstractController<WorkYearConfig>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2664243597608408070L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(WorkYearConfigController.class);

	/** The Constant DATE_ERROR_OCCURED. */
	private static final String DATE_ERROR_OCCURED = "DateErrorOccured";

	/** The Constant SAVE_ERROR_OCCURED. */
	private static final String SAVE_ERROR_OCCURED = "SaveErrorOccured";

	/** The work year config service. */
	@ManagedProperty(value = "#{workYearConfigService}")
	private WorkYearConfigService workYearConfigService;

	/** The hourly type service. */
	@ManagedProperty(value = "#{hourlyTypeService}")
	private HourlyTypeService hourlyTypeService;

	/** The list hourly types. */
	private List<HourlyType> listHourlyTypes;

	/** The years list. */
	private List<Integer> yearsList;

	/** The selected year. */
	private Integer selectedYear;

	/** The selected bean. */
	private WorkYearConfigBean selectedBean;

	/** The min date. */
	private Date minDate;

	/** The max date. */
	private Date maxDate;

	/** The work year config bean list. */
	private List<WorkYearConfigBean> workYearConfigBeanList;

	/** The work year config beans filtred. */
	private List<WorkYearConfigBean> workYearConfigBeansFiltred;

	/** The counter. */
	private long counter = 0;


	/**
	 * Instantiates a new work year config controller.
	 */
	public WorkYearConfigController()
	{
		super(WorkYearConfig.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, WorkYearConfigController.class.getName());
		}
		super.setService(workYearConfigService);
		super.setPageUrl(ControllerConstants.Pages.BO.WORK_YEAR_CONFIG_INDEX_PAGE);

		initYearsList();
		selectedYear = getYearsList().get(0);
		refreshTempWorkYearConfigList();
		initDates();
	}

	/**
	 * Inits the years list.
	 */
	private void initYearsList()
	{
		yearsList = new ArrayList<Integer>();
		final Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		int i = 0;
		while (i <= Constants.TWO)
		{
			yearsList.add(new Integer(currentYear));
			currentYear += 1;
			i++;
		}
	}

	/**
	 * Refresh temp work year config list.
	 */
	private void refreshTempWorkYearConfigList()
	{
		workYearConfigBeanList = new ArrayList<WorkYearConfigBean>();
		for (final WorkYearConfig workYearConfig : workYearConfigService.findWorkYearConfigByYear(selectedYear))
		{
			workYearConfigBeanList.add(convert(workYearConfig));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
	 */
	@Override
	public void prepareCreate()
	{
		listHourlyTypes = hourlyTypeService.findAll();
		selectedBean = new WorkYearConfigBean();

		initDates();
		selectedBean.setBeginDate(minDate);
		selectedBean.setEndDate(maxDate);
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		listHourlyTypes = hourlyTypeService.findAll();

		for (final WorkYearConfigBean wc : workYearConfigBeanList)
		{
			if (wc.getId().equals(selectedBean.getId()))
			{
				selectedBean = wc;
				break;
			}
		}
		initDates();
	}

	/**
	 * Inits the dates.
	 */
	private void initDates()
	{
		final Calendar calendar = Calendar.getInstance();

		calendar.set(selectedYear, Constants.ZERO, Constants.ONE);
		minDate = calendar.getTime();

		calendar.set(selectedYear, Constants.ELEVEN, Constants.THIRTYONE);
		maxDate = calendar.getTime();
	}

	/**
	 * Controle work year config.
	 *
	 * @param list
	 *           the list
	 * @param year
	 *           the year
	 * @return true, if successful
	 */
	public boolean controleWorkYearConfig(final List<WorkYearConfigBean> list, final Integer year)
	{
		final Calendar calendar = Calendar.getInstance();

		Date tomorrow = new Date();

		calendar.set(year, Constants.ZERO, Constants.ONE);
		final Date minDateControl = calendar.getTime();

		calendar.set(year, Constants.ELEVEN, Constants.THIRTYONE);
		final Date maxDateControl = calendar.getTime();

		if (!list.isEmpty())
		{
			final WorkYearConfigBean firstRow = list.get(0);
			final WorkYearConfigBean lastRow = list.get(list.size() - 1);

			if (DateUtils.formatSimpleDate(minDateControl).equals(DateUtils.formatSimpleDate(firstRow.getBeginDate()))
					&& DateUtils.formatSimpleDate(maxDateControl).equals(DateUtils.formatSimpleDate(lastRow.getEndDate())))
			{
				int i = 0;
				for (final WorkYearConfigBean wc : list)
				{
					if (i > 0 && !DateUtils.formatSimpleDate(tomorrow).equals(DateUtils.formatSimpleDate(wc.getBeginDate())))
					{
						return false;
					}
					tomorrow = DateUtils.getTomorrow(wc.getEndDate());
					i++;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}

		return true;
	}

	/**
	 * Save.
	 *
	 * @param event
	 *           the event
	 */
	public void save(final ActionEvent event)
	{
		if (controleWorkYearConfig(workYearConfigBeanList, selectedYear))
		{
			try
			{
				workYearConfigService.deleteWorkYearConfigByYear(selectedYear);

				final List<WorkYearConfig> listForSave = new ArrayList<WorkYearConfig>();
				for (final WorkYearConfigBean wc : workYearConfigBeanList)
				{
					listForSave.add(reverseConvert(wc));
				}

				workYearConfigService.saveOrUpdateList(listForSave);

				final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						WorkYearConfig.class.getSimpleName() + "s" + PersistenceActions.CREATE.getCode());
				JsfUtil.addSuccessMessage(msg);
			}
			catch (final Exception ex)
			{
				JsfUtil.addErrorMessage(ex,
						ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
			}
		}
		else
		{
			final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(SAVE_ERROR_OCCURED);
			JsfUtil.addErrorMessage(msg);
		}
	}

	/**
	 * Selection changed.
	 */
	public void selectionChanged()
	{
		refreshTempWorkYearConfigList();
	}

	/**
	 * Gets the work year config service.
	 *
	 * @return the work year config service
	 */
	public WorkYearConfigService getWorkYearConfigService()
	{
		return workYearConfigService;
	}

	/**
	 * Sets the work year config service.
	 *
	 * @param workYearConfigService
	 *           the new work year config service
	 */
	public void setWorkYearConfigService(final WorkYearConfigService workYearConfigService)
	{
		this.workYearConfigService = workYearConfigService;
	}

	/**
	 * Creates the temporary.
	 *
	 * @param event
	 *           the event
	 */
	public void createTemporary(final ActionEvent event)
	{
		try
		{
			if (selectedBean.getBeginDate().after(selectedBean.getEndDate()))
			{
				final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(DATE_ERROR_OCCURED);
				JsfUtil.addErrorMessage(msg);
			}
			else
			{
				selectedBean.setId(++counter);
				workYearConfigBeanList.add(selectedBean);
				final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						WorkYearConfig.class.getSimpleName() + PersistenceActions.CREATE.getCode());
				JsfUtil.addSuccessMessage(msg);
				selectedBean = null;
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getLocalizedMessage());
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}

	}

	/**
	 * Edits the temporary.
	 *
	 * @param event
	 *           the event
	 */
	public void editTemporary(final ActionEvent event)
	{
		final int index = workYearConfigBeanList.indexOf(selectedBean);
		workYearConfigBeanList.remove(index);
		workYearConfigBeanList.add(index, selectedBean);
		final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
				WorkYearConfig.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
		JsfUtil.addSuccessMessage(msg);

		selectedBean = null;
	}

	/**
	 * Delete temporary.
	 *
	 * @param event
	 *           the event
	 */
	public void deleteTemporary(final ActionEvent event)
	{
		if (workYearConfigBeanList != null)
		{
			workYearConfigBeanList.remove(selectedBean);
			final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
					WorkYearConfig.class.getSimpleName() + PersistenceActions.DELETE.getCode());
			JsfUtil.addSuccessMessage(msg);

			selectedBean = null;
		}
	}

	/**
	 * Gets the year of date.
	 *
	 * @param date
	 *           the date
	 * @return the year of date
	 */
	public Integer getYearOfDate(final Date date)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * Gets the years list.
	 *
	 * @return the years list
	 */
	public List<Integer> getYearsList()
	{
		return yearsList;
	}

	/**
	 * Gets the hourly type service.
	 *
	 * @return the hourly type service
	 */
	public HourlyTypeService getHourlyTypeService()
	{
		return hourlyTypeService;
	}

	/**
	 * Sets the hourly type service.
	 *
	 * @param hourlyTypeService
	 *           the new hourly type service
	 */
	public void setHourlyTypeService(final HourlyTypeService hourlyTypeService)
	{
		this.hourlyTypeService = hourlyTypeService;
	}

	/**
	 * Gets the list hourly types.
	 *
	 * @return the list hourly types
	 */
	public List<HourlyType> getListHourlyTypes()
	{
		return listHourlyTypes;
	}

	/**
	 * Sets the list hourly types.
	 *
	 * @param listHourlyTypes
	 *           the new list hourly types
	 */
	public void setListHourlyTypes(final List<HourlyType> listHourlyTypes)
	{
		this.listHourlyTypes = listHourlyTypes;
	}

	/**
	 * Gets the selected year.
	 *
	 * @return the selected year
	 */
	public Integer getSelectedYear()
	{
		return selectedYear;
	}

	/**
	 * Sets the selected year.
	 *
	 * @param selectedYear
	 *           the new selected year
	 */
	public void setSelectedYear(final Integer selectedYear)
	{
		this.selectedYear = selectedYear;
	}

	/**
	 * Sets the years list.
	 *
	 * @param yearsList
	 *           the new years list
	 */
	public void setYearsList(final List<Integer> yearsList)
	{
		this.yearsList = yearsList;
	}


	/**
	 * Gets the min date.
	 *
	 * @return the min date
	 */
	public Date getMinDate()
	{
		return minDate;
	}

	/**
	 * Sets the min date.
	 *
	 * @param minDate
	 *           the new min date
	 */
	public void setMinDate(final Date minDate)
	{
		this.minDate = minDate;
	}

	/**
	 * Gets the max date.
	 *
	 * @return the max date
	 */
	public Date getMaxDate()
	{
		return maxDate;
	}

	/**
	 * Sets the max date.
	 *
	 * @param maxDate
	 *           the new max date
	 */
	public void setMaxDate(final Date maxDate)
	{
		this.maxDate = maxDate;
	}

	/**
	 * Gets the selected bean.
	 *
	 * @return the selectedBean
	 */
	public WorkYearConfigBean getSelectedBean()
	{
		return selectedBean;
	}

	/**
	 * Sets the selected bean.
	 *
	 * @param selectedBean
	 *           the selectedBean to set
	 */
	public void setSelectedBean(final WorkYearConfigBean selectedBean)
	{
		this.selectedBean = selectedBean;
	}

	/**
	 * Gets the work year config bean list.
	 *
	 * @return the workYearConfigBeanList
	 */
	public List<WorkYearConfigBean> getWorkYearConfigBeanList()
	{
		return workYearConfigBeanList;
	}

	/**
	 * Sets the work year config bean list.
	 *
	 * @param workYearConfigBeanList
	 *           the new work year config bean list
	 */
	public void setWorkYearConfigBeanList(final List<WorkYearConfigBean> workYearConfigBeanList)
	{
		this.workYearConfigBeanList = workYearConfigBeanList;
	}

	/**
	 * Convert.
	 *
	 * @param workYearConfig
	 *           the work year config
	 * @return the work year config bean
	 */
	private WorkYearConfigBean convert(final WorkYearConfig workYearConfig)
	{
		final WorkYearConfigBean workYearConfigBean = new WorkYearConfigBean();

		workYearConfigBean.setId(++counter);
		workYearConfigBean.setBeginDate(workYearConfig.getBeginDate());
		workYearConfigBean.setEndDate(workYearConfig.getEndDate());
		workYearConfigBean.setHourlyType(workYearConfig.getHourlyType());

		return workYearConfigBean;
	}


	/**
	 * Reverse convert.
	 *
	 * @param workYearConfigBean
	 *           the work year config bean
	 * @return the work year config
	 */
	private WorkYearConfig reverseConvert(final WorkYearConfigBean workYearConfigBean)
	{
		final WorkYearConfig workYearConfig = new WorkYearConfig();

		workYearConfig.setBeginDate(workYearConfigBean.getBeginDate());
		workYearConfig.setEndDate(workYearConfigBean.getEndDate());
		workYearConfig.setHourlyType(workYearConfigBean.getHourlyType());

		return workYearConfig;
	}

	/**
	 * Gets the work year config beans filtred.
	 *
	 * @return the workYearConfigBeansFiltred
	 */
	public List<WorkYearConfigBean> getWorkYearConfigBeansFiltred()
	{
		return workYearConfigBeansFiltred;
	}

	/**
	 * Sets the work year config beans filtred.
	 *
	 * @param workYearConfigBeansFiltred
	 *           the workYearConfigBeansFiltred to set
	 */
	public void setWorkYearConfigBeansFiltred(final List<WorkYearConfigBean> workYearConfigBeansFiltred)
	{
		this.workYearConfigBeansFiltred = workYearConfigBeansFiltred;
	}

	/**
	 * Gets the counter.
	 *
	 * @return the counter
	 */
	public long getCounter()
	{
		return counter;
	}

	/**
	 * Sets the counter.
	 *
	 * @param counter
	 *           the counter to set
	 */
	public void setCounter(final long counter)
	{
		this.counter = counter;
	}

	/**
	 * Gets the date error occured.
	 *
	 * @return the dateErrorOccured
	 */
	public static String getDateErrorOccured()
	{
		return DATE_ERROR_OCCURED;
	}

	/**
	 * Gets the save error occured.
	 *
	 * @return the saveErrorOccured
	 */
	public static String getSaveErrorOccured()
	{
		return SAVE_ERROR_OCCURED;
	}


}
