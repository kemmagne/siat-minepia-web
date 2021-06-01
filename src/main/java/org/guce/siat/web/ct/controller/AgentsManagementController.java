package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.guce.siat.common.model.Administration;
import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.Bureau;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.AuthorityConstants;
import org.guce.siat.core.ct.service.CommonService;
import org.guce.siat.core.ct.util.enums.CctExportProductType;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.data.ProductTypeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AgentsManagementController
 *
 * @author tadzotsa
 */
@ManagedBean(name = "agentsManagementController")
@SessionScoped
public class AgentsManagementController extends AbstractController<User> {

    private static final long serialVersionUID = -2422142068329086826L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AgentsManagementController.class);

    @ManagedProperty(value = "#{commonService}")
    private CommonService commonService;

    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    private Map<User, List<CctExportProductType>> usersAndProductTypes;

    private List<List<ProductTypeData>> productTypeDatas;

    public AgentsManagementController() {
        super(User.class);
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, getClass().getName());
        }
        setLoggedUser(getLoggedUser());
        getItems();
    }

    public void goToAgentsAssigmentPage() {

        try {
            initAgentsAssigment();
            goTo(ControllerConstants.Pages.BO.AGENTS_ASSIGNMENT_PAGE);
        } catch (final IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
        }
    }

    public void initAgentsAssigment() {
        usersAndProductTypes = commonService.findUsersAndProductTypes();
        initProductTypeDatas();
    }

    private void initProductTypeDatas() {
        productTypeDatas = new ArrayList<>();
        productTypeDatas.add(new ArrayList<ProductTypeData>());
        for (CctExportProductType pt : CctExportProductType.getProductTypes()) {
            ProductTypeData ptd = new ProductTypeData(pt, Boolean.FALSE);
            productTypeDatas.get(0).add(ptd);
        }
    }

    private void goTo(String page) throws IOException {
        final FacesContext context = FacesContext.getCurrentInstance();
        final ExternalContext extContext = context.getExternalContext();
        final String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, page));
        extContext.redirect(url);
    }

    public void prepareEdit() {

        initProductTypeDatas();

        List<CctExportProductType> productTypes = usersAndProductTypes.get(selected);
        if (CollectionUtils.isEmpty(productTypes)) {
            return;
        }

        for (ProductTypeData ptd : productTypeDatas.get(0)) {
            ptd.setChecked(productTypes.contains(ptd.getProductType()));
        }

    }

    public List<String> getSingletonList() {
        return Collections.singletonList("xxx");
    }

    @Override
    public void edit() {

        List<CctExportProductType> list = new ArrayList<>();
        for (ProductTypeData ptd : productTypeDatas.get(0)) {
            if (BooleanUtils.toBoolean(ptd.getChecked())) {
                list.add(ptd.getProductType());
            }
        }

        commonService.associateAgentToProductTypes(getLoggedUser(), getSelected(), list);

        refreshItems();
        resetForm();
    }

    public void resetForm() {
        initAgentsAssigment();
        selected = null;
    }

    @Override
    public List<User> getItems() {

        if (items != null) {
            return items;
        }

        Administration admin = getLoggedUser().getAdministration();
        if (!(admin instanceof Bureau)) {
            LOG.warn("The administration of the logged user must be an instance of Bureau : {}", getLoggedUser());
            return null;
        }

        Bureau bureau = (Bureau) admin;
        items = userService.findUsersByAdministrationAndAuthorities(bureau,
                AuthorityConstants.AGENT_RECEVABILITE.getCode(),
                AuthorityConstants.INSPECTEUR.getCode(),
                AuthorityConstants.VALIDATION_SIGNATURE.getCode(),
                AuthorityConstants.SIGNATAIRE.getCode());

        return items;
    }

    /**
     * Gets the list authorities by user.
     *
     * @param user the user
     * @return the list authorities by user
     */
    public List<Authority> getAuthoritiesByUser(User user) {

        if (user == null) {
            return null;
        }

        final List<Authority> authoritiesByUser = new ArrayList<>(user.getAuthorities());

        return authoritiesByUser;
    }

    public List<CctExportProductType> getProductTypesByUser(User user) {

        if (user == null) {
            return null;
        }

        return usersAndProductTypes.get(user);
    }

    public String getProductTypes(User user) {

        if (user == null) {
            return null;
        }

        List<CctExportProductType> productTypes = usersAndProductTypes.get(user);
        if (CollectionUtils.isEmpty(productTypes)) {
            return StringUtils.EMPTY;
        }

        List<String> labels = new ArrayList<>();
        for (CctExportProductType pt : productTypes) {
            labels.add(pt.getLabel());
        }

        return StringUtils.join(labels, ',');
    }

    public List<CctExportProductType> getProductTypes() {
        return CctExportProductType.getProductTypes();
    }

    public List<List<ProductTypeData>> getProductTypeDatas() {
        return productTypeDatas;
    }

    public CommonService getCommonService() {
        return commonService;
    }

    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets the authorities options.
     *
     * @return the authorities options
     */
    public SelectItem[] getAuthoritiesOptions() {

        final SelectItem[] grantedAuthoritiesOptions = new SelectItem[getAuthoritiesListForFilter().size() + 1];

        grantedAuthoritiesOptions[0] = new SelectItem(StringUtils.EMPTY, ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(SELECT_ONE_MESSAGE));
        for (int i = 0; i < getAuthoritiesListForFilter().size(); i++) {
            grantedAuthoritiesOptions[i + 1] = new SelectItem(getAuthoritiesListForFilter().get(i).getCode(), getAuthoritiesListForFilter().get(i).getLabel());
        }

        return grantedAuthoritiesOptions;
    }

    /**
     * Gets the authorities list for filter.
     *
     * @return the authorities list for filter
     */
    private List<AuthorityConstants> getAuthoritiesListForFilter() {

        final List<AuthorityConstants> authoritiesListForFilter = new ArrayList<>();

        authoritiesListForFilter.add(AuthorityConstants.AGENT_RECEVABILITE);
        authoritiesListForFilter.add(AuthorityConstants.INSPECTEUR);
        authoritiesListForFilter.add(AuthorityConstants.VALIDATION_SIGNATURE);
        authoritiesListForFilter.add(AuthorityConstants.SIGNATAIRE);

        return authoritiesListForFilter;
    }

}
