package org.guce.siat.web.common;

/**
 * The Interface ControllerConstants.
 */
public interface ControllerConstants {

    static final String TYPE_OF_TECHNICAL_VISA = "TYPE_OF_TECHNICAL_VISA";

    /**
     * The Interface Pages.
     */
    interface Pages {

        /**
         * The login page.
         */
        String LOGIN_PAGE = "/pages/unsecure/login.xhtml";

        /**
         * The index page.
         */
        String INDEX_PAGE = "/pages/index.xhtml";

        /**
         * The index page redirect.
         */
        String INDEX_PAGE_REDIRECT = "/pages/index.xhtml?faces-redirect=true";

        /**
         * The reset password page redirect.
         */
        String RESET_PASSWORD_PAGE_REDIRECT = "/pages/common/resetPassword.xhtml?faces-redirect=true";

        /**
         * The Interface BO.
         */
        interface BO {

            /**
             * The ministry index page.
             */
            String MINISTRY_INDEX_PAGE = "/pages/bo/ministry/index.xhtml";

            /**
             * The hourly type index page.
             */
            String HOURLY_TYPE_INDEX_PAGE = "/pages/bo/hourlyType/index.xhtml";

            /**
             * The organism index page.
             */
            String ORGANISM_INDEX_PAGE = "/pages/bo/organism/index.xhtml";

            /**
             * The work year config index page.
             */
            String WORK_YEAR_CONFIG_INDEX_PAGE = "/pages/bo/workYearConfig/index.xhtml";

            /**
             * The admin index page.
             */
            String ADMIN_INDEX_PAGE = "/pages/bo/admin/index.xhtml";

            /**
             * The holiday index page.
             */
            String HOLIDAY_INDEX_PAGE = "/pages/bo/holiday/index.xhtml";

            /**
             * The service index page.
             */
            String SERVICE_INDEX_PAGE = "/pages/bo/service/index.xhtml";

            /**
             * The car index page.
             */
            String CAR_INDEX_PAGE = "/pages/bo/car/index.xhtml";

            /**
             * The analyse type index page.
             */
            String ANALYSE_TYPE_INDEX_PAGE = "/pages/bo/analyseType/index.xhtml";

            /**
             * The treatment type index page.
             */
            String TREATMENT_TYPE_INDEX_PAGE = "/pages/bo/treatmentType/index.xhtml";

            /**
             * The item index page.
             */
            String ITEM_INDEX_PAGE = "/pages/bo/item/index.xhtml";

            /**
             * The services item index page.
             */
            String SERVICES_ITEM_INDEX_PAGE = "/pages/bo/servicesItem/index.xhtml";

            /**
             * The bureau regional index page.
             */
            String BUREAU_REGIONAL_INDEX_PAGE = "/pages/bo/bureauRegional/index.xhtml";

            /**
             * The laboratory index page.
             */
            String LABORATORY_INDEX_PAGE = "/pages/bo/laboratory/index.xhtml";

            /**
             * The treatment company index page.
             */
            String TREATMENT_COMPANY_INDEX_PAGE = "/pages/bo/treatmentCompany/index.xhtml";

            /**
             * The user index page.
             */
            String USER_INDEX_PAGE = "/pages/bo/user/index.xhtml";

            /**
             * The delegation index page.
             */
            String DELEGATION_INDEX_PAGE = "/pages/bo/delegation/index.xhtml";

            /**
             * The inspector holiday index page.
             */
            String INSPECTOR_HOLIDAY_INDEX_PAGE = "/pages/bo/controllerHoliday/index.xhtml";

            /**
             * The inspection work week config index page.
             */
            String INSPECTION_WORK_WEEK_CONFIG_INDEX_PAGE = "/pages/bo/inspWorkWeekConfig/index.xhtml";

            /**
             * The inspection work day config index page.
             */
            String INSPECTION_WORK_DAY_CONFIG_INDEX_PAGE = "/pages/bo/inspectionWorkDayConfig/index.xhtml";

            /**
             * The audit entity index page.
             */
            String AUDIT_ENTITY_INDEX_PAGE = "/pages/bo/auditEntity/index.xhtml";

            /**
             * The workflow designer index page.
             */
            String WORKFLOW_DESIGNER_INDEX_PAGE = "/pages/bo/workflowDesigner/index.xhtml";

            /**
             * The step index page.
             */
            String STEP_INDEX_PAGE = "/pages/bo/step/index.xhtml";

            /**
             * The flow index page.
             */
            String FLOW_INDEX_PAGE = "/pages/bo/flow/index.xhtml";

            /**
             * The file type index page.
             */
            String FILE_TYPE_INDEX_PAGE = "/pages/bo/fileType/index.xhtml";

            /**
             * The subdepartment index page.
             */
            String SUBDEPARTMENT_INDEX_PAGE = "/pages/bo/subDepartment/index.xhtml";

            /**
             * The position authority index page.
             */
            String POSITION_AUTHORITY_INDEX_PAGE = "/pages/bo/positionAuthority/index.xhtml";

            /**
             * The trade balance config index page.
             */
            String TRADE_BALANCE_CONFIG_INDEX_PAGE = "/pages/bo/tradeBalanceConfig/index.xhtml";

            /**
             * The settings page.
             */
            String SETTINGS_PAGE = "/pages/bo/params/index.xhtml";

            /**
             * The report settings page.
             */
            String REPORT_SETTINGS_PAGE = "/pages/bo/reportParams/index.xhtml";

            /**
             * The Guce-SIAT offices page.
             */
            String GUCE_SIAT_OFFICES_PAGE = "/pages/bo/bureauGuceSiat/bureauGuceSiatCellEditor.xhtml";

            /**
             * The Interface GR.
             */
            interface GR {

                /**
                 * The alert page.
                 */
                String ALERT_PAGE = "/pages/bo/gr/alert/index.xhtml";

                /**
                 * The ciblage page.
                 */
                String CIBLAGE_PAGE = "/pages/bo/gr/ciblage/index.xhtml";

                /**
                 * The ciblage history page.
                 */
                String CIBLAGE_HISTORY_PAGE = "/pages/bo/gr/historyCiblage/index.xhtml";

                /**
                 * The audit page.
                 */
                String AUDIT_PAGE = "/pages/bo/gr/historyAlert/index.xhtml";

                /**
                 * The params page.
                 */
                String PARAMS_PAGE = "/pages/bo/gr/params/index.xhtml";

                /**
                 * The settings page.
                 */
            }
        }

        /**
         * The Interface FO.
         */
        interface FO {

            /**
             * The dashboard cct index page redirect.
             */
            String DASHBOARD_CCT_INDEX_PAGE_REDIRECT = "/pages/fo/dashboardCCT/index.xhtml?faces-redirect=true";

            /**
             * The dashboard ap index page redirect.
             */
            String DASHBOARD_AP_INDEX_PAGE_REDIRECT = "/pages/fo/dashboardAP/index.xhtml?faces-redirect=true";

            /**
             * The simple search index page redirect.
             */
            String SIMPLE_SEARCH_INDEX_PAGE_REDIRECT = "/pages/fo/search/simple/index.xhtml?faces-redirect=true";

            /**
             * The p aymen t_ inde x_ pag e_ redirect.
             */
            String PAYMENT_INDEX_PAGE_REDIRECT = "/pages/fo/payment/index.xhtml?faces-redirect=true";

            /**
             * The dashboard cct index page.
             */
            String DASHBOARD_CCT_INDEX_PAGE = "/pages/fo/dashboardCCT/index.xhtml";

            /**
             * The details cct index page.
             */
            String DETAILS_CCT_INDEX_PAGE = "/pages/fo/dashboardCCT/fileDetails/index.xhtml";

            /**
             * The dashboard ap index page.
             */
            String DASHBOARD_AP_INDEX_PAGE = "/pages/fo/dashboardAP/index.xhtml";

            /**
             * The details ap index page.
             */
            String DETAILS_AP_INDEX_PAGE = "/pages/fo/dashboardAP/fileDetails/index.xhtml";

            /**
             * The retrieve ap index page.
             */
            String RETRIEVE_AP_INDEX_PAGE = "/pages/fo/retrieveAP/index.xhtml";

            /**
             * The simple search index page.
             */
            String SIMPLE_SEARCH_INDEX_PAGE = "/pages/fo/search/simple/index.xhtml";

            String ASSIGNED_FILE_ITEM_PAGE = "/pages/fo/search/assigned/index.xhtml";

            /**
             * The advanced search index page.
             */
            String ADVANCED_SEARCH_INDEX_PAGE = "/pages/fo/search/advanced/index.xhtml";

            /**
             * The inspection appointment cct page.
             */
            String INSPECTION_APPOINTMENT_CCT_PAGE = "/pages/fo/dashboardCCT/controllerAppointment/controllerAppointment.xhtml";

            /**
             * The search sample by laboratory.
             */
            String SEARCH_SAMPLE_BY_LABORATORY = "/pages/fo/statistic/sampleByLab/index.xhtml";

            /**
             * The SEARC h_ analys e_ b y_l aboratory.
             */
            String SEARCH_ANALYSE_BY_lABORATORY = "/pages/fo/statistic/analyseByLab/index.xhtml";

            /**
             * The search file item by desicion.
             */
            String SEARCH_FILE_ITEM_BY_DESICION = "/pages/fo/statistic/fileItemByDesicion/index.xhtml";

            /**
             * The search historic client by decision.
             */
            String SEARCH_HISTORIC_CLIENT_BY_DECISION = "/pages/fo/statistic/historicClientByDecision/index.xhtml";

            /**
             * The search historic client by product.
             */
            String SEARCH_HISTORIC_CLIENT_BY_PRODUCT = "/pages/fo/statistic/historicClientByProduct/index.xhtml";

            /**
             * The search inspection destribution by product.
             */
            String SEARCH_INSPECTION_DESTRIBUTION = "/pages/fo/statistic/inspectionDestribution/index.xhtml";

            /**
             * The search desicions delivrees.
             */
            String SEARCH_DESICIONS_DELIVREES = "/pages/fo/statistic/repartitionDecisionDelevree/index.xhtml";

            /**
             * The trade balance.
             */
            String TRADE_BALANCE = "/pages/fo/monitoring/tradeBalance/index.xhtml";
            /**
             * The search products quantity.
             */
            String SEARCH_PRODUCTS_QUANTITY = "/pages/fo/statistic/productsQuantity/index.xhtml";

            /**
             * The search products quantity by drd.
             */
            String SEARCH_PRODUCTS_QUANTITY_BY_DRD = "/pages/fo/statistic/productsQuantityByDrd/index.xhtml";

            /**
             * The dashboard fimex index page.
             */
            String DASHBOARD_FIMEX_INDEX_PAGE = "/pages/fo/monitoring/fimex/index.xhtml";

            /**
             * The details fimex index page.
             */
            String DETAILS_FIMEX_INDEX_PAGE = "/pages/fo/monitoring/fimex/fileDetails/index.xhtml";

            /**
             * The search statistics on business.
             */
            String SEARCH_STATISTICS_ON_BUSINESS = "/pages/fo/statistic/statisticsOnBusiness/index.xhtml";

            /**
             * The Payment_ index_ page.
             */
            String PAYMENT_INDEX_PAGE = "/pages/fo/payment/index.xhtml";

            /**
             * The costs index page.
             */
            String COSTS_INDEX_PAGE = "/pages/fo/payment/costs/index.xhtml";

            /**
             * The statistic pinding files.
             */
            String STATISTIC_PINDING_FILES = "/pages/fo/statistic/pendingFiles/index.xhtml";
            
            String STATISTIC_ACTIVITY_REPORT = "/pages/fo/statistic/activityReport/index.xhtml";
            String STATISTIC_GLOBAL_DELAY_LISTING = "/pages/fo/statistic/globalDelayListing/index.xhtml";
            String STATISTIC_DELAY_LISTING_STAKEHOLDER = "/pages/fo/statistic/delayListingStakeholder/index.xhtml";
            String STATISTIC_EXPORT_NSH_DESTINIATION = "/pages/fo/statistic/exportNshDestination/index.xhtml";
            String STATISTIC_EXPORT_NSH_DESTINIATION_SENDER = "/pages/fo/statistic/exportNshDestinationSender/index.xhtml";

            /**
             * The Interface GR.
             */
            interface GR {

                /**
                 * The trend performance page.
                 */
                String TREND_PERFORMANCE_PAGE = "/pages/fo/dashboardCCT/gr/trendPerformance/index.xhtml";

            }

            /**
             * The Interface Quota.
             */
            interface Quota {

                /**
                 * The quota home page.
                 */
                String QUOTA_HOME_PAGE = "/pages/fo/quota/index.xhtml";
            }
        }

        /**
         * The Interface AccountSetup.
         */
        interface AccountSetup {

            /**
             * The theme config page.
             */
            String THEME_CONFIG_PAGE = "/pages/fo/accountSetup/theme.xhtml";
        }
    }

    /**
     * The Interface Bundle.
     */
    interface Bundle {

        /**
         * The Constant LOCAL_BUNDLE_NAME.
         */
        String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";

        /**
         * The Interface Messages.
         */
        interface Messages {

            /**
             * The Constant CHOOSE_DECISION_ERROR.
             */
            String CHOOSE_DECISION_ERROR = "ChooseDecisionError";

            /**
             * The Constant CHOOSE_MULTI_DECISION_ERROR.
             */
            String CHOOSE_MULTI_DECISION_ERROR = "ChooseMultiDecisionError";

            /**
             * The Constant ROLLBACK_BEFORE_DECISION.
             */
            String ROLLBACK_BEFORE_DECISION = "RollBackBeforeDecision";

            /**
             * The ivalid file sise.
             */
            String IVALID_FILE_SISE = "invalidFileSize";

            /**
             * The ivalid file type.
             */
            String IVALID_FILE_TYPE = "invalideFileType";

            /**
             * The Constant DISPATCH_ERROR.
             */
            String DISPATCH_ERROR = "Dispatch_Error";

            /**
             * The Constant SEND_ERROR.
             */
            String SEND_ERROR = "SendError";

            /**
             * The Constant SEND_ERROR.
             */
            String RESEND_ERROR = "ReSendError";

            /**
             * The Constant SAME_STEPS_ERROR.
             */
            String SAME_STEPS_ERROR = "SameStepsError";

            /**
             * The all product must be selected.
             */
            String ALL_PRODUCT_MUST_BE_SELECTED = "SelectAllProduct";

            /**
             * The Constant CREATION_SUCCEE.
             */
            String CREATION_SUCCEE = "DecisionCreated";

            /**
             * The Constant CREATION_FAILED.
             */
            String CREATION_FAILED = "DecisionFailed";

            /**
             * The Constant SAVE_DECISION_FAILED : SaveDecisionFailed
             */
            String SAVE_DECISION_FAILED = "SaveDecisionFailed";

            /**
             * The Constant PERSISTENCE_ERROR_OCCURED.
             */
            String PERSISTENCE_ERROR_OCCURED = "PersistenceErrorOccured";

            /**
             * The Constant CREATE_RECOMMANDATION_REQUIRED_MESSAGE.
             */
            String CREATE_RECOMMANDATION_REQUIRED_MESSAGE = "CreateRecommandationRequiredMessage_value";

            /**
             * The Constant EDIT_RECOMMANDATION_REQUIRED_MESSAGE.
             */
            String EDIT_RECOMMANDATION_REQUIRED_MESSAGE = "EditRecommandationRequiredMessage_value";

            /**
             * The Constant SEND_SUCCESS.
             */
            String SEND_SUCCESS = "SendSuccess";

            /**
             * The Constant SEND_SUCCESS.
             */
            String RESEND_SUCCESS = "ReSendSuccess";

            /**
             * The Constant ROLL_BACK_SUCCESS.
             */
            String ROLL_BACK_SUCCESS = "RollBackSuccess";

            /**
             * The Constant ROLL_BACK_FAIL.
             */
            String ROLL_BACK_FAIL = "RollBackFail";

            /**
             * The Constant CALENDAR_ERROR_CHOOSE_APPOINTMENT.
             */
            String CALENDAR_ERROR_CHOOSE_APPOINTMENT = "Calendar_error_choose_appointment";

            /**
             * The all products concerned by this decision.
             */
            String ALL_PRODUCTS_CONCERNED_BY_THIS_DECISION = "DecisionAllProductConcerened";

            /**
             * The check products decision msg.
             */
            String CHECK_PRODUCTS_DECISION_MSG = "checkArticle";

            /**
             * The Constant EMPTY_TABLE.
             */
            String EMPTY_TABLE = "empty_table";

            /**
             * The Constant CANCEL_REQUEST_IN_PROGRESS.
             */
            String CANCEL_REQUEST_IN_PROGRESS = "cancel_request_in_progress";

            /**
             * The Constant ERROR_DIALOG_TITLE.
             */
            String ERROR_DIALOG_TITLE = "error_dialog_title";

            /**
             * The check analyse decision error.
             */
            String CHECK_ANALYSE_DECISION_ERROR = "check_analyse_decision_error";

            /**
             * The check treatment decision error.
             */
            String CHECK_TREATMENT_DECISION_ERROR = "TreatmentOrder_check_treatment_decision_error";

            /**
             * The history duration days.
             */
            String HISTORY_DURATION_DAYS = "history_duration_days";

            /**
             * The history duration hours.
             */
            String HISTORY_DURATION_HOURS = "history_duration_hours";

            /**
             * The history duration less than hour.
             */
            String HISTORY_DURATION_LESS_THAN_HOUR = "history_duration_less_than_hour";

            /**
             * The check analyse type choosen msg.
             */
            String CHECK_ANALYSE_TYPE_CHOOSEN_MSG = "check_analyse_type_error";

            /**
             * The check analyse type error.
             */
            String CHECK_ANALYSE_TYPE_ERROR = "checkAnalyseType";

            /**
             * The check treatment type error.
             */
            String CHECK_TREATMENT_TYPE_ERROR = "checktreatmentType";

            /**
             * The check treatment type choosen msg.
             */
            String CHECK_TREATMENT_TYPE_CHOOSEN_MSG = "check_treatment_type_error";

            /**
             * The amount error occured.
             */
            String AMOUNT_ERROR_OCCURED = "AmountErrorOccured";

            /**
             * The report postfixe edit success.
             */
            String REPORT_POSTFIXE_EDIT_SUCCESS = "reportParamsLabel_successEdit";

            /**
             * The report postfixe edit failed.
             */
            String REPORT_POSTFIXE_EDIT_FAILED = "reportParamsTitle_failedEdit";

            /**
             * The generate report failed.
             */
            String GENERATE_REPORT_FAILED = "generateReport_failed";

            /**
             * The object mail notification recept folder.
             */
            String OBJECT_MAIL_NOTIFICATION_RECEPT_FOLDER = "objetMailNotificationReceptFolder";

            /**
             * The no fimex message.
             */
            String NO_FIMEX_MESSAGE = "noFimexMessage";
        }
    }
}
