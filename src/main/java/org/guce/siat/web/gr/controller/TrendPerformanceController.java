package org.guce.siat.web.gr.controller;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.User;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.filter.AuditFilter;
import org.guce.siat.core.gr.model.TrendPerformance;
import org.guce.siat.core.gr.service.TrendPerformanceService;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.gr.util.ScenarioType;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class TrendPerformanceController.
 */
@ManagedBean(name = "trendPerformanceController")
@ViewScoped
public class TrendPerformanceController extends AbstractController<TrendPerformance> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -123851037055080940L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(TrendPerformanceController.class);

    /**
     * The Constant DATE_VALIDATION_ERROR_MESSAGE.
     */
    private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

    /**
     * The trend performance service.
     */
    @ManagedProperty(value = "#{trendPerformanceService}")
    private TrendPerformanceService trendPerformanceService;

    /**
     * The user service.
     */
    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    /**
     * The filter.
     */
    private AuditFilter filter;

    /**
     * The users.
     */
    private List<User> users;

    /**
     * The horizontal bar model.
     */
    private HorizontalBarChartModel horizontalBarModel;

    /**
     * The system accuracy.
     */
    float systemAccuracy;

    /**
     * Instantiates a new trend performance controller.
     */
    public TrendPerformanceController() {
        super(TrendPerformance.class);
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, TrendPerformanceController.class.getName());
        }
        filter = new AuditFilter();
        users = userService.findUsersByAdministrationAndAuthorities(getCurrentOrganism());
        getItems();
        createHorizontalBarModel();
        super.setService(trendPerformanceService);
        super.setPageUrl(ControllerConstants.Pages.FO.GR.TREND_PERFORMANCE_PAGE);

    }

    @Override
    public List<TrendPerformance> getItems() {
        try {
            if (items == null) {
                items = trendPerformanceService.findTrendPerformanceByUsers(users, null);
            }
        } catch (final Exception ex) {
            LOG.error(null, ex);
            JsfUtil.addErrorMessage(ex,
                    ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(PERSISTENCE_ERROR_OCCURED));
        }
        return items;
    }

    private void createHorizontalBarModel() {
        horizontalBarModel = new HorizontalBarChartModel();

        final ChartSeries system = new ChartSeries();

        //Système
        system.setLabel(getCurrentLocale().equals(Locale.FRANCE) ? ResourceBundle.getBundle(
                ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("GestionRisquesSystemLabel") : ResourceBundle
                .getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("GestionRisquesSystemLabel"));

        final ChartSeries user = new ChartSeries();

        //Utilisateur
        user.setLabel(getCurrentLocale().equals(Locale.FRANCE) ? ResourceBundle.getBundle(
                ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("GestionRisquesUserLabel") : ResourceBundle
                .getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("GestionRisquesUserLabel"));

        int systemNbrSia = 0;
        int systemNbrRdd = 0;
        int systemNbrCct = 0;
        int systemNbrConvocation = 0;
        int userNbrSia = 0;
        int userNbrRdd = 0;
        int userNbrCct = 0;
        float concordance = 0;
        systemAccuracy = 0;

        for (final TrendPerformance trendPerformance : getItems()) {
            //Décision système
            if (ScenarioType.SIA.name().equals(trendPerformance.getSystemDecision())) {
                systemNbrSia++;
            } else if (ScenarioType.RDD.name().equals(trendPerformance.getSystemDecision())) {
                systemNbrRdd++;
            } else if (ScenarioType.CONVOCATION.name().equals(trendPerformance.getSystemDecision())) {
                systemNbrConvocation++;
            } else if (ScenarioType.CCT.name().equals(trendPerformance.getSystemDecision())) {
                systemNbrCct++;
            }

            //Décision utilisateur
            if (ScenarioType.SIA.name().equals(trendPerformance.getUserDecision())) {
                userNbrSia++;
            } else if (ScenarioType.RDD.name().equals(trendPerformance.getUserDecision())) {
                userNbrRdd++;
            } else if (ScenarioType.CCT.name().equals(trendPerformance.getUserDecision())) {
                userNbrCct++;
            }

            if ((!ScenarioType.AD.name().equals(trendPerformance.getUserDecision()) || !ScenarioType.AD.name().equals(
                    trendPerformance.getSystemDecision()))
                    && trendPerformance.getConcordance()) {
                concordance++;
            }

        }

        if (CollectionUtils.isNotEmpty(items)) {
            systemAccuracy = (concordance / items.size()) * 100;
        }

        system.set(ScenarioType.SIA.name(), systemNbrSia);
        system.set(ScenarioType.RDD.name(), systemNbrRdd);
        system.set(ScenarioType.CONVOCATION.name(), systemNbrConvocation);
        system.set(ScenarioType.CCT.name(), systemNbrCct);

        user.set(ScenarioType.SIA.name(), userNbrSia);
        user.set(ScenarioType.RDD.name(), userNbrRdd);
        user.set(ScenarioType.CONVOCATION.name(), 0);
        user.set(ScenarioType.CCT.name(), userNbrCct);

        horizontalBarModel.addSeries(user);
        horizontalBarModel.addSeries(system);

        //tendance et performance
        horizontalBarModel.setTitle(getCurrentLocale().equals(Locale.FRANCE) ? ResourceBundle.getBundle(
                ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("GestionRisquesTitle") : ResourceBundle
                .getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("GestionRisquesTitle"));
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(true);

        final Axis xAxis = horizontalBarModel.getAxis(AxisType.X);

        //Nombre de décisions
        xAxis.setLabel(getCurrentLocale().equals(Locale.FRANCE) ? ResourceBundle.getBundle(
                ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("GestionRisquesDecisionNumberLabel")
                : ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString(
                        "GestionRisquesDecisionNumberLabel"));
        xAxis.setMin(0);
        xAxis.setMax(200);

        //Décisions
        final Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel(getCurrentLocale().equals(Locale.FRANCE) ? ResourceBundle.getBundle(
                ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString("GestionRisquesDecisionLabel")
                : ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, Locale.FRANCE).getString(
                        "GestionRisquesDecisionLabel"));
    }

    /**
     * Do simple search.
     */
    public void doSearchByFilter() {
        if (filter.getBeginDate() != null && filter.getEndDate() != null && filter.getBeginDate().after(filter.getEndDate())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(DATE_VALIDATION_ERROR_MESSAGE));
        } else {
            items = trendPerformanceService.findTrendPerformanceByUsers(users, filter);
            createHorizontalBarModel();
        }
    }

    /**
     * Gets the trend performance service.
     *
     * @return the trend performance service
     */
    public TrendPerformanceService getTrendPerformanceService() {
        return trendPerformanceService;
    }

    /**
     * Sets the trend performance service.
     *
     * @param trendPerformanceService the new trend performance service
     */
    public void setTrendPerformanceService(final TrendPerformanceService trendPerformanceService) {
        this.trendPerformanceService = trendPerformanceService;
    }

    /**
     * Gets the user service.
     *
     * @return the user service
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Sets the user service.
     *
     * @param userService the new user service
     */
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets the horizontal bar model.
     *
     * @return the horizontal bar model
     */
    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    /**
     * Sets the horizontal bar model.
     *
     * @param horizontalBarModel the new horizontal bar model
     */
    public void setHorizontalBarModel(final HorizontalBarChartModel horizontalBarModel) {
        this.horizontalBarModel = horizontalBarModel;
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public AuditFilter getFilter() {
        return filter;
    }

    /**
     * Sets the filter.
     *
     * @param filter the new filter
     */
    public void setFilter(final AuditFilter filter) {
        this.filter = filter;
    }

    /**
     * Gets the system accuracy.
     *
     * @return the system accuracy
     */
    public float getSystemAccuracy() {
        return systemAccuracy;
    }

    /**
     * Sets the system accuracy.
     *
     * @param systemAccuracy the new system accuracy
     */
    public void setSystemAccuracy(final float systemAccuracy) {
        this.systemAccuracy = systemAccuracy;
    }
}
