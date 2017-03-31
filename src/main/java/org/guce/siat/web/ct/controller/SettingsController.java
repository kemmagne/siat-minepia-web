package org.guce.siat.web.ct.controller;

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
import org.guce.siat.web.gr.controller.ParamsOrganismController;
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
@ManagedBean(name = "settingsController")
@SessionScoped
public class SettingsController extends AbstractController<ParamsOrganism> {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 6136435911440328457L;

	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ParamsOrganismController.class);

	/**
	 * The Constant PARAMS_ORGANISM_VALIDATION_ERROR.
	 */
	private static final String PARAMS_ORGANISM_VALIDATION_ERROR = "ParamsOrganismValidationMsg";

	/**
	 * The Constant COEFFICIENT_VALIDATION_ERROR.
	 */
	/**
	 * The Constant POSITIVE_NUMBER_ERROR.
	 */
	private static final String POSITIVE_NUMBER_ERROR = "PostiveNumberError";

	/**
	 * The Constant CATEGORY.
	 */
	private static final String CATEGORY = "category";

	/**
	 * The Constant PARAM.
	 */
	private static final String PARAM = "param";

	/**
	 * The root.
	 */
	private TreeNode root;

	/**
	 * The selected param.
	 */
	private ParamsNode selectedParam;

	/**
	 * The params organism service.
	 */
	@ManagedProperty(value = "#{paramsOrganismService}")
	private ParamsOrganismService paramsOrganismService;

	/**
	 * The params service.
	 */
	@ManagedProperty(value = "#{paramsService}")
	private ParamsService paramsService;

	/**
	 * Instantiates a new params organism controller.
	 */
	public SettingsController() {
		super(ParamsOrganism.class);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		if (LOG.isDebugEnabled()) {
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, ParamsOrganismController.class.getName());
		}
		super.setService(paramsOrganismService);
		super.setPageUrl(ControllerConstants.Pages.BO.SETTINGS_PAGE);
		root = populateParamsOrganismNode(getItems());
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#getItems()
	 */
	@Override
	public List<ParamsOrganism> getItems() {
		try {
			items = paramsOrganismService.findParamsOrganismByOrganism(getCurrentOrganism(), ParamsCategory.GN);
		} catch (final Exception ex) {
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
	public void edit() {
		if (ParamsNames.MAX_ATTEMPTS_USER_CONNEXION.getCode().equals(selectedParam.getName())) {
			final Params param = paramsService.findParamsByName(ParamsNames.MAX_ATTEMPTS_USER_CONNEXION.getCode());
			param.setValue(selectedParam.getValue());
			paramsService.update(param);
		} else if (ParamsNames.TOKEN_AUTHENTIFICATION.getCode().equals(selectedParam.getName())) {
			final Params param = paramsService.findParamsByName(ParamsNames.TOKEN_AUTHENTIFICATION.getCode());
			param.setValue(selectedParam.getValue());
			paramsService.update(param);
		} else {
			try {
				ParamsOrganism paramsOrganism = null;
				final Float paramValue = Float.parseFloat(selectedParam.getValue());
				if (paramValue >= 0) {
					paramsOrganism = new ParamsOrganism();
					paramsOrganism = convertParamsNode(selectedParam);
					if (paramsOrganism != null) {
						paramsOrganism.setValue(selectedParam.getValue());
						paramsOrganismService.update(paramsOrganism);
					} else if (!selectedParam.getValue().equalsIgnoreCase(getDefaultValueOfParamsOrganism(selectedParam))) {
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
				} else {
					final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
							.getString(POSITIVE_NUMBER_ERROR);
					JsfUtil.addErrorMessage(msg);
				}
			} catch (final NumberFormatException ex) {
				final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
						PARAMS_ORGANISM_VALIDATION_ERROR);
				JsfUtil.addErrorMessage(msg);
			} catch (final Exception ex) {
				if (LOG.isDebugEnabled()) {
					LOG.debug(ex.getCause().toString());
				}
				LOG.warn(null, ex);
				JsfUtil.addErrorMessage(ex,
						ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
			} finally {
				refreshNode();
			}
		}
	}

	/**
	 * Refresh node.
	 */
	public void refreshNode() {
		root = populateParamsOrganismNode(getItems());
	}

	/**
	 * Populate params organism node.
	 *
	 * @param list the list
	 * @return the tree node
	 */
	public TreeNode populateParamsOrganismNode(final List<ParamsOrganism> list) {

		final TreeNode parentRoot = new DefaultTreeNode();

		if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ROOT.getCode())) {
			final TreeNode authentificationAttemps = new DefaultTreeNode(new ParamsNode(
					CategoryNames.AUTHENTIFICATION_ATTEMPTS.getCode(), null, CATEGORY), parentRoot);
			authentificationAttemps.setExpanded(true);
			authentificationAttemps.setSelectable(false);
			final List<ParamsOrganism> generalsParamsOrganism = paramsOrganismService.findParamsOrganismByOrganism(
					getCurrentOrganism(), ParamsCategory.GN);
			final List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
			treeNodeList.add(authentificationAttemps);
			for (final ParamsOrganism paramsOrganism : generalsParamsOrganism) {
				initParamsOrganismBeanList(paramsOrganism, treeNodeList);
			}
		} else {

			final TreeNode cancelRequest = new DefaultTreeNode(
					new ParamsNode(CategoryNames.CANCEL_REQUEST.getCode(), null, CATEGORY), parentRoot);
			cancelRequest.setExpanded(true);
			cancelRequest.setSelectable(false);

			final List<TreeNode> treeNodeList = new ArrayList<TreeNode>();

			treeNodeList.add(cancelRequest);

			for (final ParamsOrganism paramsOrganism : list) {
				initParamsOrganismBeanList(paramsOrganism, treeNodeList);
			}
		}
		return parentRoot;
	}

	/**
	 * Inits the params organism bean list.
	 *
	 * @param paramsOrganism the params organism
	 * @param treeNodeList the tree node list
	 */
	public void initParamsOrganismBeanList(final ParamsOrganism paramsOrganism, final List<TreeNode> treeNodeList) {
		try {
			if (getLoggedUser().getAuthoritiesList().contains(AuthorityConstants.ROOT.getCode())) {
				if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.MAX_ATTEMPTS_USER_CONNEXION.getCode())) {
					final TreeNode maxAttemptsUserConnexion = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.ZERO));
					maxAttemptsUserConnexion.setSelectable(false);
				} else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.TOKEN_AUTHENTIFICATION.getCode())) {
					final TreeNode tokenAuthentification = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
							treeNodeList.get(Constants.ZERO));
					tokenAuthentification.setSelectable(false);
				}
			} else if (paramsOrganism.getParam().getName().equalsIgnoreCase(ParamsNames.MAX_CANCEL_REQUEST.getCode())) {
				final TreeNode maxCancelRequest = new DefaultTreeNode(convertParamsOrganism(paramsOrganism),
						treeNodeList.get(Constants.ZERO));
				maxCancelRequest.setSelectable(false);
			}
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Convert params organism.
	 *
	 * @param param the param
	 * @return the params node
	 */
	public ParamsNode convertParamsOrganism(final ParamsOrganism param) {
		return new ParamsNode(param.getParam().getName(), param.getValue(), PARAM);
	}

	/**
	 * Convert params node.
	 *
	 * @param paramsNode the params node
	 * @return the params organism
	 */
	public ParamsOrganism convertParamsNode(final ParamsNode paramsNode) {
		return paramsOrganismService.findParamsOrganismByOrganismAndName(currentOrganism, paramsNode.getName());
	}

	/**
	 * Gets the default value of params organism.
	 *
	 * @param paramsNode the params node
	 * @return the default value of params organism
	 */
	public String getDefaultValueOfParamsOrganism(final ParamsNode paramsNode) {
		final Params param = paramsService.findParamsByName(paramsNode.getName());
		return param.getValue();
	}

	/**
	 * Gets the params organism service.
	 *
	 * @return the params organism service
	 */
	public ParamsOrganismService getParamsOrganismService() {
		return paramsOrganismService;
	}

	/**
	 * Sets the params organism service.
	 *
	 * @param paramsOrganismService the new params organism service
	 */
	public void setParamsOrganismService(final ParamsOrganismService paramsOrganismService) {
		this.paramsOrganismService = paramsOrganismService;
	}

	/**
	 * Gets the root.
	 *
	 * @return the root
	 */
	public TreeNode getRoot() {
		return root;
	}

	/**
	 * Sets the root.
	 *
	 * @param root the root to set
	 */
	public void setRoot(final TreeNode root) {
		this.root = root;
	}

	/**
	 * Gets the selected param.
	 *
	 * @return the selectedParam
	 */
	public ParamsNode getSelectedParam() {
		return selectedParam;
	}

	/**
	 * Sets the selected param.
	 *
	 * @param selectedParam the selectedParam to set
	 */
	public void setSelectedParam(final ParamsNode selectedParam) {
		this.selectedParam = selectedParam;
	}

	/**
	 * Gets the params service.
	 *
	 * @return the paramsService
	 */
	public ParamsService getParamsService() {
		return paramsService;
	}

	/**
	 * Sets the params service.
	 *
	 * @param paramsService the paramsService to set
	 */
	public void setParamsService(final ParamsService paramsService) {
		this.paramsService = paramsService;
	}

}
