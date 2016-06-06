package org.guce.siat.web.ct.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.Step;
import org.guce.siat.common.service.AuthorityService;
import org.guce.siat.common.service.StepService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.AuthorityConstants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.StepCode;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class StepController.
 */
@ManagedBean(name = "stepController")
@ViewScoped
public class StepController extends AbstractController<Step>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7361548902180105591L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(StepController.class);

	/** The Constant CONSTRAINT_FLOW_FK_VIOLATION. */
	private static final String CONSTRAINT_FLOW_FK_VIOLATION = "DeleteStep_constraintfkFlowViolation";
	/** The step service. */
	@ManagedProperty(value = "#{stepService}")
	private StepService stepService;

	/** The authority service. */
	@ManagedProperty(value = "#{authorityService}")
	private AuthorityService authorityService;

	/** The file type list. */
	private List<FileTypeCode> fileTypeCodeList;

	/** The selected granted authorities. */
	private Authority selectedGrantedAuthorities;

	/** The selected file type. */
	private FileTypeCode selectedFileTypeCode;

	/** The selected code. */
	private StepCode selectedCode;

	/** The step code list. */
	private List<StepCode> stepCodeList;

	/** The authority pick list. */
	private DualListModel<Authority> authorityPickList;

	/** The authority list. */
	private List<Authority> authorityList;



	/**
	 * Instantiates a new step controller.
	 */
	public StepController()
	{
		super(Step.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, StepController.class.getName());
		}
		super.setPageUrl(ControllerConstants.Pages.BO.STEP_INDEX_PAGE);
		super.setService(stepService);
		authorityList = authorityService.findByAuthoritiesType();
		authorityPickList = new DualListModel<Authority>(new ArrayList<Authority>(), new ArrayList<Authority>());
		authorityPickList.getSource().addAll(authorityList);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#prepareCreate()
	 */
	@Override
	public void prepareCreate()
	{

		this.selected = new Step();
		authorityList = populateAuthoritiesList();
		stepCodeList = Arrays.asList(StepCode.values());
		fileTypeCodeList = Arrays.asList(FileTypeCode.values());

		authorityPickList = new DualListModel<Authority>(new ArrayList<Authority>(), new ArrayList<Authority>());
		authorityPickList.getSource().addAll(authorityList);
	}

	/**
	 * Prepare edit.
	 */
	public void prepareEdit()
	{
		this.setSelected(stepService.find(this.getSelected().getId()));
		authorityList = populateAuthoritiesList();
		stepCodeList = Arrays.asList(StepCode.values());
		fileTypeCodeList = Arrays.asList(FileTypeCode.values());

		authorityPickList = new DualListModel<Authority>(new ArrayList<Authority>(), new ArrayList<Authority>());
		authorityPickList.getSource().addAll(authorityList);


		for (final Authority authority : this.getSelected().getRoleList())
		{
			for (final Authority authority1 : authorityPickList.getSource())
			{
				if (authority.getId().equals(authority1.getId()))
				{
					authorityPickList.getTarget().add(authority);
					authorityPickList.getSource().remove(authority);
					break;
				}
			}
		}
	}

	/**
	 * Populate authorities list.
	 *
	 * @return the list
	 */
	public List<Authority> populateAuthoritiesList()
	{
		if (authorityList == null)
		{
			final List<String> adminsAuthorities = new ArrayList<String>();
			adminsAuthorities.add(AuthorityConstants.ROOT.getCode());
			adminsAuthorities.add(AuthorityConstants.ADMIN_ORGANISME.getCode());
			adminsAuthorities.add(AuthorityConstants.SIGNATAIRE.getCode());
			adminsAuthorities.add(AuthorityConstants.AGENT_RECEVABILITE.getCode());
			adminsAuthorities.add(AuthorityConstants.AGENT_COTATION_1.getCode());
			adminsAuthorities.add(AuthorityConstants.INSPECTEUR.getCode());
			adminsAuthorities.add(AuthorityConstants.CONTROLEUR.getCode());
			adminsAuthorities.add(AuthorityConstants.LABORATOIRE.getCode());
			adminsAuthorities.add(AuthorityConstants.IMPORTATEUR.getCode());
			adminsAuthorities.add(AuthorityConstants.SOCIETE_TRAITEMENT.getCode());
			adminsAuthorities.add(AuthorityConstants.DOUANE.getCode());
			authorityList = authorityService.findAuthoritiesByAuthorityList(adminsAuthorities);
		}

		return authorityList;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#create()
	 */
	@Override
	public void create()
	{

		if (selected.getRoleList() == null)
		{
			selected.setRoleList(new ArrayList<Authority>());
		}

		selected.getRoleList().addAll(authorityPickList.getTarget());
		if (selected != null)
		{
			selected.setStepCode(selectedCode);
		}
		super.create();
		selectedGrantedAuthorities = null;
		selectedFileTypeCode = null;
		selectedCode = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.common.AbstractController#edit()
	 */
	@Override
	public void edit()
	{
		getSelected().getRoleList().clear();
		getSelected().getRoleList().addAll(authorityPickList.getTarget());
		super.edit();
		selected = new Step();
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
			this.getService().delete(selected);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			if (e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException)
			{
				JsfUtil.addErrorMessage("deleteGrowl",
						ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(CONSTRAINT_FLOW_FK_VIOLATION));
			}
		}

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
	 * Gets the authority list.
	 *
	 * @return the authorityList
	 */
	public List<Authority> getAuthorityList()
	{
		return authorityList;
	}

	/**
	 * Sets the authority list.
	 *
	 * @param authorityList
	 *           the authorityList to set
	 */
	public void setAuthorityList(final List<Authority> authorityList)
	{
		this.authorityList = authorityList;
	}

	/**
	 * Gets the selected granted authorities.
	 *
	 * @return the selectedGrantedAuthorities
	 */
	public Authority getSelectedGrantedAuthorities()
	{
		return selectedGrantedAuthorities;
	}

	/**
	 * Sets the selected granted authorities.
	 *
	 * @param selectedGrantedAuthorities
	 *           the selectedGrantedAuthorities to set
	 */
	public void setSelectedGrantedAuthorities(final Authority selectedGrantedAuthorities)
	{
		this.selectedGrantedAuthorities = selectedGrantedAuthorities;
	}

	/**
	 * Gets the file type list.
	 *
	 * @return the fileTypeList
	 */
	public List<FileTypeCode> getFileTypeList()
	{
		return fileTypeCodeList;
	}

	/**
	 * Sets the file type list.
	 *
	 * @param fileTypeList
	 *           the fileTypeList to set
	 */
	public void setFileTypeList(final List<FileTypeCode> fileTypeList)
	{
		this.fileTypeCodeList = fileTypeList;
	}

	/**
	 * Gets the selected file type.
	 *
	 * @return the selectedFileType
	 */
	public FileTypeCode getSelectedFileType()
	{
		return selectedFileTypeCode;
	}

	/**
	 * Sets the selected file type.
	 *
	 * @param selectedFileType
	 *           the selectedFileType to set
	 */
	public void setSelectedFileType(final FileTypeCode selectedFileType)
	{
		this.selectedFileTypeCode = selectedFileType;
	}

	/**
	 * Gets the selected code.
	 *
	 * @return the selectedCode
	 */
	public StepCode getSelectedCode()
	{
		return selectedCode;
	}

	/**
	 * Sets the selected code.
	 *
	 * @param selectedCode
	 *           the selectedCode to set
	 */
	public void setSelectedCode(final StepCode selectedCode)
	{
		this.selectedCode = selectedCode;
	}

	/**
	 * Gets the step code list.
	 *
	 * @return the stepCodeList
	 */
	public List<StepCode> getStepCodeList()
	{
		return stepCodeList;
	}

	/**
	 * Sets the step code list.
	 *
	 * @param stepCodeList
	 *           the stepCodeList to set
	 */
	public void setStepCodeList(final List<StepCode> stepCodeList)
	{
		this.stepCodeList = stepCodeList;
	}

	/**
	 * Gets the authority pick list.
	 *
	 * @return the authorityPickList
	 */
	public DualListModel<Authority> getAuthorityPickList()
	{
		return authorityPickList;
	}

	/**
	 * Sets the authority pick list.
	 *
	 * @param authorityPickList
	 *           the authorityPickList to set
	 */
	public void setAuthorityPickList(final DualListModel<Authority> authorityPickList)
	{
		this.authorityPickList = authorityPickList;
	}




	/**
	 * @return the stepCodeList
	 */





}
