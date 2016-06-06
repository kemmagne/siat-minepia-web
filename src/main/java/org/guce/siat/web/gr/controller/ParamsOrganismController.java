package org.guce.siat.web.gr.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.guce.siat.common.model.Params;
import org.guce.siat.common.model.ParamsOrganism;
import org.guce.siat.common.service.ParamsOrganismService;
import org.guce.siat.common.service.ParamsService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.AuthorityConstants;
import org.guce.siat.common.utils.enums.ParamsCategory;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.guce.siat.web.gr.util.ParamsNode;
import org.guce.siat.web.gr.util.enums.CategoryNames;
import org.guce.siat.web.gr.util.enums.ParamsNames;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class ParamsOrganismController.
 */
@ManagedBean(name = "paramsOrganismController")
@SessionScoped
public class ParamsOrganismController extends AbstractController<ParamsOrganism>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6136435911440328457L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(ParamsOrganismController.class);

	/** The Constant PARAMS_ORGANISM_VALIDATION_ERROR. */
	private static final String PARAMS_ORGANISM_VALIDATION_ERROR = "ParamsOrganismValidationMsg";

	/** The Constant COEFFICIENT_VALIDATION_ERROR. */
	private static final String COEFFICIENT_VALIDATION_ERROR = "CoefficientValidationMsg";

	/** The Constant POSITIVE_NUMBER_ERROR. */
	private static final String POSITIVE_NUMBER_ERROR = "PostiveNumberError";

	/** The Constant CATEGORY. */
	private static final String CATEGORY = "category";

	/** The Constant PARAM. */
	private static final String PARAM = "param";

	/** The root. */
	private TreeNode root;

	/** The selected param. */
	private ParamsNode selectedParam;

	/** The params organism service. */
	@ManagedProperty(value = "#{paramsOrganismService}")
	private ParamsOrganismService paramsOrganismService;

	/** The params service. */
	@ManagedProperty(value = "#{paramsService}")
	private ParamsService paramsService;

	/**
	 * Instantiates a new params organism controller.
	 */
	public ParamsOrganismController()
	{
		super(ParamsOrganism.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, ParamsOrganismController.class.getName());
		}
		super.setService(paramsOrganismService);
		super.setPageUrl(ControllerConstants.Pages.BO.GR.PARAMS_PAGE);
		root = populateParamsOrganismNode(getItems());
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<ParamsOrganism> getItems()
	{
		try
		{
			items = paramsOrganismService.findParamsOrganismByOrganism(getCurrentOrganism(), ParamsCategory.GR);
		}
		catch (final Exception ex)
		{
			JsfUtil.addErrorMessage(ex,
					ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
		}
		return items;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#edit()
	 */
	@Override
	public void edit()
	{
		if (ParamsNames.MAX_ATTEMPTS_USER_CONNEXION.getCode().equals(selectedParam.getName()))
		{
			final Params param = paramsService.findParamsByName(ParamsNames.MAX_ATTEMPTS_USER_CONNEXION.getCode());
			param.setValue(selectedParam.getValue());
			paramsService.update(param);
		}
		else
		{
			try
			{
				ParamsOrganism paramsOrganism = null;
				final Float paramValue = Float.parseFloat(selectedParam.getValue());
				if (selectedParam.getName().equalsIgnoreCase(ParamsNames.QUANTITY_COEFFICIENT.getCode())
						&& (paramValue < 0 || paramValue > 1))
				{
					final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
							COEFFICIENT_VALIDATION_ERROR);
					JsfUtil.addErrorMessage(msg);

				}
				else if (paramValue >= 0)
				{
					paramsOrganism = new ParamsOrganism();
					paramsOrganism = convertParamsNode(selectedParam);
					if (paramsOrganism != null)
					{
						paramsOrganism.setValue(selectedParam.getValue());
						paramsOrganismService.update(paramsOrganism);
					}
					else if (!selectedParam.getValue().equalsIgnoreCase(getDefaultValueOfParamsOrganism(selectedParam)))
					{
						paramsOrganism = new ParamsOrganism();
						final Params param = paramsService.findParamsByName(selectedParam.getName());
						paramsOrganism.setOrganism(currentOrganism);
						paramsOrganism.setParam(param);
						paramsOrganism.setValue(selectedParam.getValue());
						paramsOrganismService.save(paramsOrganism);

					}
					final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
							ParamsOrganism.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
					JsfUtil.addSuccessMessage(msg);
				}
				else
				{
					final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
							.getString(POSITIVE_NUMBER_ERROR);
					JsfUtil.addErrorMessage(msg);
				}
			}
			catch (final NumberFormatException ex)
			{
				final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						PARAMS_ORGANISM_VALIDATION_ERROR);
				JsfUtil.addErrorMessage(msg);
			}
			catch (final Exception ex)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(ex.getCause().toString());
				}
				JsfUtil.addErrorMessage(ex,
						ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
			}
			finally
			{
				refreshNode();
			}
		}
	}

	/**
	 * Refresh node.
	 */
	public void refreshNode()
	{
		root = populateParamsOrganismNode(getItems());
	}

	/**
	 * Populate params organism node.
	 *
	 * @param list
	 *           the list
	 * @return the tree node
	 */
	public TreeNode populateParamsOrganismNode(final List<ParamsOrganism> list)
	{

		final TreeNode parentRoot = new DefaultTreeNode();


		if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ROOT.getCode()))
		{
			final TreeNode authentificationAttemps = new DefaultTreeNode(new ParamsNode(
					CategoryNames.AUTHENTIFICATION_ATTEMPTS.getCode(), null, CATEGORY), parentRoot);
			authentificationAttemps.setExpanded(true);
			authentificationAttemps.setSelectable(false);
			final List<ParamsOrganism> generalsParamsOrganism = paramsOrganismService.findParamsOrganismByOrganism(
					getCurrentOrganism(), ParamsCategory.GN);
			final List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
			treeNodeList.add(authentificationAttemps);
			for (final ParamsOrganism paramsOrganism : generalsParamsOrganism)
			{
				initParamsOrganismBeanList(paramsOrganism, treeNodeList);
			}
		}
		else
		{
			final TreeNode produitConnu = new DefaultTreeNode(new ParamsNode(CategoryNames.PRODUIT_CONNU.getCode(), null, CATEGORY),
					parentRoot);
			produitConnu.setExpanded(true);
			produitConnu.setSelectable(false);



			final TreeNode analyseAuLaboratoire = new DefaultTreeNode(new ParamsNode(CategoryNames.ANALYSE_AU_LABORATOIRE.getCode(),
					null, CATEGORY), parentRoot);
			analyseAuLaboratoire.setExpanded(true);
			analyseAuLaboratoire.setSelectable(false);



			final TreeNode decisionNegtive = new DefaultTreeNode(new ParamsNode(CategoryNames.DECISIONS_NEGATIVES.getCode(), null,
					CATEGORY), parentRoot);
			decisionNegtive.setExpanded(true);
			decisionNegtive.setSelectable(false);



			final TreeNode quantite = new DefaultTreeNode(new ParamsNode(CategoryNames.QUANTITE.getCode(), null, CATEGORY),
					parentRoot);
			quantite.setExpanded(true);
			quantite.setSelectable(false);



			final TreeNode importateurConnu = new DefaultTreeNode(new ParamsNode(CategoryNames.IMPORTATEUR_CONNU.getCode(), null,
					CATEGORY), parentRoot);
			importateurConnu.setExpanded(true);
			importateurConnu.setSelectable(false);



			final TreeNode delaiDeConvocation = new DefaultTreeNode(new ParamsNode(CategoryNames.DELAI_DE_CONVOCATION.getCode(),
					null, CATEGORY), parentRoot);
			final TreeNode rddNode = new DefaultTreeNode(new ParamsNode(CategoryNames.RDD_REFOULEMENT.getCode(), null, CATEGORY),
					parentRoot);
			rddNode.setExpanded(true);
			rddNode.setSelectable(false);
			rddNode.setExpanded(true);
			rddNode.setSelectable(false);


			importateurConnu.setExpanded(true);
			importateurConnu.setSelectable(false);

			final List<TreeNode> treeNodeList = new ArrayList<TreeNode>();

			treeNodeList.add(produitConnu);
			treeNodeList.add(analyseAuLaboratoire);
			treeNodeList.add(decisionNegtive);
			treeNodeList.add(quantite);
			treeNodeList.add(importateurConnu);
			treeNodeList.add(delaiDeConvocation);
			treeNodeList.add(rddNode);

			for (final ParamsOrganism paramsOrganism : list)
			{
				initParamsOrganismBeanList(paramsOrganism, treeNodeList);
			}
		}
		return parentRoot;
	}

	/**
	 * Inits the params organism bean list.
	 *
	 * @param paramsOrganism
	 *           the params organism
	 * @param treeNodeList
	 *           the tree node list
	 */
	public void initParamsOrganismBeanList(final ParamsOrganism paramsOrganism, final List<TreeNode> treeNodeList)
	{
		try
		{

				if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.PRODUCT_KNOWN_PERIOD.getCode()))
				{
					final TreeNode productknowPeriod = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.ZERO));
					productknowPeriod.setSelectable(false);
				}

				else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.PRODUCT_KNOWN_THRESHOLD.getCode()))
				{
					final TreeNode productknownthreshold = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.ZERO));
					productknownthreshold.setSelectable(false);
				}
				else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.PRODUCT_TESTED_PERIOD.getCode()))
				{
					final TreeNode productTestedPeriod = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.ONE));
					productTestedPeriod.setSelectable(false);
				}
				else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.NEGATIVE_DECISIONS_PERIOD.getCode()))
				{
					final TreeNode negativeDecisionPeriod = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.TWO));
					negativeDecisionPeriod.setSelectable(false);
				}
				else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.QUANTITY_COEFFICIENT.getCode()))
				{
					final TreeNode quantityCoefficient = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.THREE));
					quantityCoefficient.setSelectable(false);
				}
				else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.IMPORTER_KNOWN_PERIOD.getCode()))
				{
					final TreeNode importerKnowPeriod = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.FOUR));
					importerKnowPeriod.setSelectable(false);
				}
				else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.IMPORTER_KNOWN_THRESHOLD.getCode()))
				{
					final TreeNode importerKnowthreshold = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.FOUR));
					importerKnowthreshold.setSelectable(false);
				}

				else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.RDD_DELAY.getCode()))
				{
					final TreeNode rddDelay = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.FIVE));
					rddDelay.setSelectable(false);
				}
				else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.CLEARANCE_DELAY.getCode()))
				{
					final TreeNode clearanceDelay = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.FIVE));
					clearanceDelay.setSelectable(false);
				}
				else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.MEC_DELAY.getCode()))
				{
					final TreeNode mecDelay = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.FIVE));
					mecDelay.setSelectable(false);
				}
				else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.NB_RDD.getCode()))
				{
					final TreeNode nbRdd = new DefaultTreeNode(convertParamsOrganism(paramsOrganism), treeNodeList.get(Constants.SIX));
					nbRdd.setSelectable(false);
				}


		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Convert params organism.
	 *
	 * @param param
	 *           the param
	 * @return the params node
	 */
	public ParamsNode convertParamsOrganism(final ParamsOrganism param)
	{
		return new ParamsNode(param.getParam().getName(), param.getValue(), PARAM);
	}

	/**
	 * Convert params node.
	 *
	 * @param paramsNode
	 *           the params node
	 * @return the params organism
	 */
	public ParamsOrganism convertParamsNode(final ParamsNode paramsNode)
	{
		return paramsOrganismService.findParamsOrganismByOrganismAndName(currentOrganism, paramsNode.getName());
	}

	/**
	 * Gets the default value of params organism.
	 *
	 * @param paramsNode
	 *           the params node
	 * @return the default value of params organism
	 */
	public String getDefaultValueOfParamsOrganism(final ParamsNode paramsNode)
	{
		final Params param = paramsService.findParamsByName(paramsNode.getName());
		return param.getValue();
	}

	/**
	 * Gets the params organism service.
	 *
	 * @return the params organism service
	 */
	public ParamsOrganismService getParamsOrganismService()
	{
		return paramsOrganismService;
	}


	/**
	 * Sets the params organism service.
	 *
	 * @param paramsOrganismService
	 *           the new params organism service
	 */
	public void setParamsOrganismService(final ParamsOrganismService paramsOrganismService)
	{
		this.paramsOrganismService = paramsOrganismService;
	}

	/**
	 * Gets the root.
	 *
	 * @return the root
	 */
	public TreeNode getRoot()
	{
		return root;
	}

	/**
	 * Sets the root.
	 *
	 * @param root
	 *           the root to set
	 */
	public void setRoot(final TreeNode root)
	{
		this.root = root;
	}

	/**
	 * Gets the selected param.
	 *
	 * @return the selectedParam
	 */
	public ParamsNode getSelectedParam()
	{
		return selectedParam;
	}

	/**
	 * Sets the selected param.
	 *
	 * @param selectedParam
	 *           the selectedParam to set
	 */
	public void setSelectedParam(final ParamsNode selectedParam)
	{
		this.selectedParam = selectedParam;
	}

	/**
	 * Gets the params service.
	 *
	 * @return the paramsService
	 */
	public ParamsService getParamsService()
	{
		return paramsService;
	}

	/**
	 * Sets the params service.
	 *
	 * @param paramsService
	 *           the paramsService to set
	 */
	public void setParamsService(final ParamsService paramsService)
	{
		this.paramsService = paramsService;
	}







}
