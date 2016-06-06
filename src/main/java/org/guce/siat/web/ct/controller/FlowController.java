package org.guce.siat.web.ct.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.guce.siat.common.model.Flow;
import org.guce.siat.common.model.Step;
import org.guce.siat.common.service.FlowService;
import org.guce.siat.common.service.StepService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class FlowController.
 */
@ManagedBean(name = "flowController")
@ViewScoped
public class FlowController extends AbstractController<Flow>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3865007631726218221L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(FlowController.class);

	/** The Constant DURATION_ERROR. */
	private static final String DURATION_ERROR = "DurationError";

	/** The Constant OUTGOING_ERROR. */
	private static final String OUTGOING_ERROR = "OutgoingError";

	/** The Constant CONSTRAINT_STEP_FK_VIOLATION. */
	private static final String CONSTRAINT_STEP_FK_VIOLATION = "DeleteFlow_constraintfkStepViolation";

	/** The step list. */
	private List<Step> stepList;

	/** The selected from step. */
	private Step selectedFromStep;

	/** The selected to step. */
	private Step selectedToStep;

	/** The flow service. */
	@ManagedProperty(value = "#{flowService}")
	private FlowService flowService;

	/** The step service. */
	@ManagedProperty(value = "#{stepService}")
	private StepService stepService;

	/**
	 * Instantiates a new flow controller.
	 */
	public FlowController()
	{
		super(Flow.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, FlowController.class.getName());
		}
		super.setPageUrl(ControllerConstants.Pages.BO.FLOW_INDEX_PAGE);
		super.setService(flowService);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
	 */
	@Override
	public void prepareCreate()
	{
		selected = new Flow();
		stepList = stepService.findAll();
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(flowService.find(this.getSelected().getId()));
		stepList = stepService.findAll();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#create()
	 */
	@Override
	public void create()
	{
		if (selected != null)
		{

			selected.setFromStep(selectedFromStep);
			selected.setToStep(selectedToStep);
			boolean validation = true;
			if (selected.getDuration() < Constants.ZERO)
			{
				JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(DURATION_ERROR));
				validation = false;
			}
			if (selected.getOutgoing() < 0 || selected.getOutgoing() > 1)
			{
				JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(OUTGOING_ERROR));
				validation = false;
			}
			if (validation)
			{
				super.create();
				selectedFromStep = null;
				selectedToStep = null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#edit()
	 */
	@Override
	public void edit()
	{
		if (selected != null)
		{
			boolean validation = true;
			if (selected.getDuration() < Constants.ZERO)
			{
				JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(DURATION_ERROR));
				validation = false;
			}
			if (selected.getOutgoing() < 0 || selected.getOutgoing() > 1)
			{
				JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(OUTGOING_ERROR));
				validation = false;
			}
			if (validation)
			{
				super.edit();
			}

		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#delete()
	 */
	@Override
	public void delete()
	{
		try
		{
			super.delete();
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			if (e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException)
			{
				JsfUtil.addErrorMessage("deleteGrowl",
						ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(CONSTRAINT_STEP_FK_VIOLATION));
			}
		}
	}

	/**
	 * Gets the step list.
	 *
	 * @return the stepList
	 */
	public List<Step> getStepList()
	{
		return stepList;
	}

	/**
	 * Sets the step list.
	 *
	 * @param stepList
	 *           the stepList to set
	 */
	public void setStepList(final List<Step> stepList)
	{
		this.stepList = stepList;
	}

	/**
	 * Gets the step service.
	 *
	 * @return the stepService
	 */
	public StepService getStepService()
	{
		return stepService;
	}

	/**
	 * Sets the step service.
	 *
	 * @param stepService
	 *           the stepService to set
	 */
	public void setStepService(final StepService stepService)
	{
		this.stepService = stepService;
	}

	/**
	 * Gets the flow service.
	 *
	 * @return the flowService
	 */
	public FlowService getFlowService()
	{
		return flowService;
	}

	/**
	 * Sets the flow service.
	 *
	 * @param flowService
	 *           the flowService to set
	 */

	public void setFlowService(final FlowService flowService)
	{
		this.flowService = flowService;
	}

	/**
	 * Gets the selected from step.
	 *
	 * @return the selectedFromStep
	 */
	public Step getSelectedFromStep()
	{
		return selectedFromStep;
	}

	/**
	 * Sets the selected from step.
	 *
	 * @param selectedFromStep
	 *           the selectedFromStep to set
	 */
	public void setSelectedFromStep(final Step selectedFromStep)
	{
		this.selectedFromStep = selectedFromStep;
	}

	/**
	 * Gets the selected to step.
	 *
	 * @return the selectedToStep
	 */
	public Step getSelectedToStep()
	{
		return selectedToStep;
	}

	/**
	 * Sets the selected to step.
	 *
	 * @param selectedToStep
	 *           the selectedToStep to set
	 */
	public void setSelectedToStep(final Step selectedToStep)
	{
		this.selectedToStep = selectedToStep;
	}

}
