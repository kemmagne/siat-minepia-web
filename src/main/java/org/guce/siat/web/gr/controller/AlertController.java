package org.guce.siat.web.gr.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.dao.FileTypeDao;
import org.guce.siat.common.model.Country;
import org.guce.siat.common.model.FlowGuceSiat;
import org.guce.siat.common.model.ServicesItem;
import org.guce.siat.common.service.CountryService;
import org.guce.siat.common.service.EbxmlPropertiesService;
import org.guce.siat.common.service.FileProducer;
import org.guce.siat.common.service.FlowGuceSiatService;
import org.guce.siat.common.service.ServicesItemService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.common.utils.ebms.ESBConstants;
import org.guce.siat.common.utils.ebms.EbmsUtility;
import org.guce.siat.common.utils.ebms.UtilitiesException;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.common.utils.io.IOUtils;
import org.guce.siat.core.gr.model.Alert;
import org.guce.siat.core.gr.service.AlertService;
import org.guce.siat.core.gr.utils.filter.AlertFilter;
import org.guce.siat.jaxb.alert.ALERT.DOCUMENT;
import org.guce.siat.utility.jaxb.common.MESSAGE;
import org.guce.siat.utility.jaxb.common.REFERENCEDOSSIER;
import org.guce.siat.utility.jaxb.common.ROUTAGE;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.controller.util.enums.PersistenceActions;
import org.guce.siat.web.ct.data.AlertDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.xml.sax.SAXException;

/**
 * The Class AlertController.
 */
@ManagedBean(name = "alertController")
@ViewScoped
public class AlertController extends AbstractController<Alert> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -123851037055080940L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AlertController.class);

    /**
     * The Constant DATE_VALIDATION_ERROR_MESSAGE.
     */
    private static final String DATE_VALIDATION_ERROR_MESSAGE = "DateValidationError";

    /**
     * The Constant CAR_EXIST_ERROR_MESSAGE.
     */
    private static final String ALERT_EXIST_ERROR_MESSAGE = "AlertExistInfo";

    /**
     * The Constant CAR_EXIST_ERROR.
     */
    private static final String ALERT_VALIDATION_ERROR = "AlertValidationInfo";

    /**
     * The Constant CONSTRAINT_VIOLATION_EXCEPTION.
     */
    private static final String CONSTRAINT_VIOLATION_EXCEPTION = "org.hibernate.exception.ConstraintViolationException";

    /**
     * The Constant SNSH_WARNING_MESSAGE.
     */
    private static final String SNSH_WARNING_MESSAGE = "AlertWarningMessage";

    /**
     * The Constant SUCCES_REPORT.
     */
    private static final String SUCCES_REPORT = "succesRepport";

    /**
     * The Constant ERROR_REPORT.
     */
    private static final String ERROR_REPORT = "erreurRepport";

    /**
     * The Constant EMPTY_ALERT.
     */
    private static final String EMPTY_ALERT = "emptyAlert";

    /**
     * The alert service.
     */
    @ManagedProperty(value = "#{alertService}")
    private AlertService alertService;

    /**
     * The Service Item service.
     */
    @ManagedProperty(value = "#{servicesItemService}")
    private ServicesItemService servicesItemService;

    /**
     * The country service.
     */
    @ManagedProperty(value = "#{countryService}")
    private CountryService countryService;
    /**
     * The EBXMl properties.
     */
    @ManagedProperty(value = "#{ebxmlPropertiesService}")
    private EbxmlPropertiesService ebxmlPropertiesService;
    /**
     * The transaction manager.
     */
    @ManagedProperty(value = "#{transactionManager}")
    private PlatformTransactionManager transactionManager;

    /**
     * The flow guce siat service.
     */
    @ManagedProperty(value = "#{flowGuceSiatService}")
    private FlowGuceSiatService flowGuceSiatService;

    /**
     * The file type dao.
     */
    @ManagedProperty(value = "#{fileTypeDao}")
    private FileTypeDao fileTypeDao;

    /**
     * The countries.
     */
    private List<Country> countries;

    /**
     * The filter.
     */
    private AlertFilter filter;

    /**
     * The alerts.
     */
    private List<AlertDto> alerts;

    /**
     * The selected filter.
     */
    private AlertDto selectedAlert;

    /**
     * The selecteds.
     */
    private List<AlertDto> selecteds;

    /**
     * The alert selected.
     */
    private Boolean alertSelected;

    /**
     * send file service.
     */
    @ManagedProperty(value = "#{fileProducer}")
    private FileProducer fileProducer;

    /**
     * Instantiates a new alert controller.
     */
    public AlertController() {
        super(Alert.class);
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, AlertController.class.getName());
        }
        super.setService(alertService);
        super.setPageUrl(ControllerConstants.Pages.BO.GR.ALERT_PAGE);
        countries = countryService.findAll();
        selected = new Alert();
        initSearch();
        initAlert();
    }

    /**
     * Inits the create.
     */
    private void initCreate() {
        refreshItems();
        initAlert();
    }

    /**
     * Inits the alert.
     */
    private void initAlert() {
        alerts = new ArrayList<AlertDto>();
        for (final Alert alert : getItems()) {
            alerts.add(new AlertDto(alert, false));
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#create()
     */
    @Override
    public void create() {
        if (getSelected().getServicesItem() != null || getSelected().getCountry() != null) {
            try {
                if (getSelected().getCountry() != null) {
                    getSelected().setCountryId(getSelected().getCountry().getCountryIdAlpha2());
                }
                getSelected().setSend(false);
                alertService.save(getSelected());
                if (!isValidationFailed()) {
                    initCreate();

                    selectedAlert = null;
                }
                final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        Alert.class.getSimpleName() + PersistenceActions.CREATE.getCode());
                JsfUtil.addSuccessMessage(msg);
            } catch (final Exception pe) {
                LOG.error(pe.getMessage(), pe);

                if (CONSTRAINT_VIOLATION_EXCEPTION.equals(pe.getCause().getClass().getName())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            ALERT_EXIST_ERROR_MESSAGE));
                } else {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            PERSISTENCE_ERROR_OCCURED));
                }

            }
        } else {
            FacesContext.getCurrentInstance().isValidationFailed();
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                    .getString(ALERT_VALIDATION_ERROR));
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#edit()
     */
    @Override
    public void edit() {
        if (getSelected().getServicesItem() != null || getSelected().getCountry() != null) {
            try {
                if (getSelected().getCountry() != null) {
                    getSelected().setCountryId(getSelected().getCountry().getCountryIdAlpha2());
                } else {
                    getSelected().setCountryId(null);
                }
                getSelected().setSend(false);
                alertService.update(getSelected());
                if (!isValidationFailed()) {
                    initCreate();
                    selectedAlert = null;
                }
                final String msg = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                        Alert.class.getSimpleName() + PersistenceActions.UPDATE.getCode());
                JsfUtil.addSuccessMessage(msg);
            } catch (final Exception pe) {
                LOG.error(pe.getMessage(), pe);

                if (CONSTRAINT_VIOLATION_EXCEPTION.equals(pe.getCause().getClass().getName())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            ALERT_EXIST_ERROR_MESSAGE));
                } else {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                            PERSISTENCE_ERROR_OCCURED));
                }
            }
        } else {
            FacesContext.getCurrentInstance().isValidationFailed();
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                    .getString(ALERT_VALIDATION_ERROR));
        }

    }

    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#delete()
     */
    @Override
    public void delete() {
        super.delete();
        selectedAlert = null;
        doSearch();
    }

    /**
     * Do search.
     */
    public void doSearch() {
        if (filter.getBeginDate() != null && filter.getEndDate() != null && filter.getBeginDate().after(filter.getEndDate())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                    DATE_VALIDATION_ERROR_MESSAGE));
            return;
        } else {
            setItems(alertService.findByFilter(filter));
            initAlert();
        }
    }


    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.common.AbstractController#getItems()
     */
    @Override
    public List<Alert> getItems() {
        return alertService.findByOrganism(getCurrentOrganism());
    }

    /**
     * Inits the search.
     */
    public void initSearch() {
        filter = new AlertFilter();
        items = null;
        getItems();
    }

    /**
     * Prepare edit.
     */
    public void prepareEdit() {
        this.setSelected(alertService.find(this.getSelectedAlert().getAlert().getId()));
    }

    /**
     * Complete snsh.
     *
     * @param query the query
     * @return the list
     */
    public List<ServicesItem> completeSnsh(final String query) {
        final List<ServicesItem> allServicesItem = servicesItemService.findAllActiveServicesItemByOrganism(getCurrentOrganism());
        final List<ServicesItem> filteredServicesItem = new ArrayList<ServicesItem>();
        if (CollectionUtils.isNotEmpty(allServicesItem)) {
            for (final ServicesItem servicesItem : allServicesItem) {
                if (servicesItem.getSnsh().toLowerCase().startsWith(query.toLowerCase())) {
                    filteredServicesItem.add(servicesItem);
                }
            }
        } else {
            JsfUtil.addWarningMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale())
                    .getString(SNSH_WARNING_MESSAGE));
        }

        return filteredServicesItem;
    }

    /**
     * Gets the alert service.
     *
     * @return the alertService
     */
    public synchronized AlertService getAlertService() {
        return alertService;
    }

    /**
     * Sets the alert service.
     *
     * @param alertService the alertService to set
     */
    public synchronized void setAlertService(final AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * Gets the services item service.
     *
     * @return the servicesItemService
     */
    public synchronized ServicesItemService getServicesItemService() {
        return servicesItemService;
    }

    /**
     * Sets the services item service.
     *
     * @param servicesItemService the servicesItemService to set
     */
    public synchronized void setServicesItemService(final ServicesItemService servicesItemService) {
        this.servicesItemService = servicesItemService;
    }

    /**
     * Gets the country service.
     *
     * @return the countryService
     */
    public CountryService getCountryService() {
        return countryService;
    }

    /**
     * Sets the country service.
     *
     * @param countryService the countryService to set
     */
    public void setCountryService(final CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Gets the countries.
     *
     * @return the countries
     */
    public List<Country> getCountries() {

        return countries;
    }

    /**
     * Sets the countries.
     *
     * @param countries the countries to set
     */
    public void setCountries(final List<Country> countries) {
        this.countries = countries;
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public AlertFilter getFilter() {
        return filter;
    }

    /**
     * Sets the filter.
     *
     * @param filter the filter to set
     */
    public void setFilter(final AlertFilter filter) {
        this.filter = filter;
    }

    /**
     * Gets the alerts.
     *
     * @return the alerts
     */
    public List<AlertDto> getAlerts() {
        return alerts;
    }

    /**
     * Sets the alerts.
     *
     * @param alerts the new alerts
     */
    public void setAlerts(final List<AlertDto> alerts) {
        this.alerts = alerts;
    }

    /**
     * Gets the selected alert.
     *
     * @return the selected alert
     */
    public AlertDto getSelectedAlert() {
        return selectedAlert;
    }

    /**
     * Sets the selected alert.
     *
     * @param selectedAlert the new selected alert
     */
    public void setSelectedAlert(final AlertDto selectedAlert) {
        this.selectedAlert = selectedAlert;
    }

    /**
     * Gets the selecteds.
     *
     * @return the selecteds
     */
    public List<AlertDto> getSelecteds() {
        return selecteds;
    }

    /**
     * Sets the selecteds.
     *
     * @param selecteds the new selecteds
     */
    public void setSelecteds(final List<AlertDto> selecteds) {
        this.selecteds = selecteds;
    }

    /**
     * The list to send.
     */
    List<AlertDto> listToSend;

    /**
     * Send alert.
     *
     * @return the ebxml properties service
     */
    public EbxmlPropertiesService getEbxmlPropertiesService() {
        return ebxmlPropertiesService;
    }

    /**
     * Sets the ebxml properties service.
     *
     * @param ebxmlPropertiesService the new ebxml properties service
     */
    public void setEbxmlPropertiesService(final EbxmlPropertiesService ebxmlPropertiesService) {
        this.ebxmlPropertiesService = ebxmlPropertiesService;
    }

    /**
     * Gets the transaction manager.
     *
     * @return the transaction manager
     */
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * Sets the transaction manager.
     *
     * @param transactionManager the new transaction manager
     */
    public void setTransactionManager(final PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * Alert selected.
     *
     * @param event the event
     */
    public void listnerAlert(final AjaxBehaviorEvent event) {
        setAlertSelected(verifiy(alerts));
    }

    /**
     * Verifiy.
     *
     * @param dtos the dtos
     * @return the boolean
     */
    public Boolean verifiy(final List<AlertDto> dtos) {
        for (final AlertDto dto : dtos) {
            if (dto.getCheked()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Send alert.
     */
    public void sendAlert() {
        final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setReadOnly(false);
        final TransactionStatus status = transactionManager.getTransaction(def);
        listToSend = new ArrayList<AlertDto>();
        int i = 0;
        for (final AlertDto dto : alerts) {
            if (dto.getCheked()) {
                i = 1;
                listToSend.add(dto);
            }
        }
        if (i > 0) {
            try {
                //TODO send the alert message
                final String service = StringUtils.EMPTY;
                final String documentType = StringUtils.EMPTY;
                // convert file to document
                final Serializable documentSerializable = convertFileToDocumentAlert(listToSend);
                // prepare document to send
                final java.io.File xmlFile = prepareAlertDocument(documentSerializable,
                        ebxmlPropertiesService.getEbxmlFolder(), service, documentType);
                final Map<String, Object> data = new HashMap<String, Object>();
                final Path path = Paths.get(xmlFile.getAbsolutePath());
                final byte[] ebxml = Files.readAllBytes(path);
                data.put(ESBConstants.FLOW, ebxml);
                data.put(ESBConstants.ATTACHMENT, null);
                data.put(ESBConstants.TYPE_DOCUMENT, documentType);
                data.put(ESBConstants.SERVICE, service);
                data.put(ESBConstants.MESSAGE, null);
                data.put(ESBConstants.EBXML_TYPE, "STANDARD");
                data.put(ESBConstants.TO_PARTY_ID, ebxmlPropertiesService.getToPartyId());
                data.put(ESBConstants.DEAD, "0");
                fileProducer.sendFile(data);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Message sent to OUT queue");
                }
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(SUCCES_REPORT));
                setAlertSelected(false);
                for (final AlertDto dto : listToSend) {
                    dto.setCheked(false);
                    dto.getAlert().setSend(true);
                    alertService.update(dto.getAlert());
                }
                transactionManager.commit(status);
            } catch (final Exception e) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(ERROR_REPORT));
                transactionManager.rollback(status);
            }
        } else {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(EMPTY_ALERT));
        }
    }

    /**
     * Convert file to document alert.
     *
     * @param alerts the alerts
     * @return the serializable
     * @throws UtilitiesException the utilities exception
     */
    private Serializable convertFileToDocumentAlert(final List<AlertDto> alerts) throws UtilitiesException {
        final FlowGuceSiat flowGuceSiat = flowGuceSiatService.findFlowGuceSiatByFlowSiatAndFileType(FlowCode.FL_ALERT_01.name(),
                fileTypeDao.findByCode(FileTypeCode.ALERT).getId());

        org.guce.siat.jaxb.alert.ALERT.DOCUMENT alertDocument = new org.guce.siat.jaxb.alert.ALERT.DOCUMENT();
        alertDocument = initHeader(alertDocument);

        alertDocument.setTYPEDOCUMENT(flowGuceSiat.getFlowGuce());
        alertDocument.setCONTENT(new org.guce.siat.jaxb.alert.ALERT.DOCUMENT.CONTENT());
        final List<org.guce.siat.jaxb.alert.ALERT.DOCUMENT.CONTENT.ALERTS.ALERT> alrts = new ArrayList<org.guce.siat.jaxb.alert.ALERT.DOCUMENT.CONTENT.ALERTS.ALERT>();
        for (final AlertDto alert : alerts) {
            final org.guce.siat.jaxb.alert.ALERT.DOCUMENT.CONTENT.ALERTS.ALERT alrt = new org.guce.siat.jaxb.alert.ALERT.DOCUMENT.CONTENT.ALERTS.ALERT();
            if (alert.getAlert().getBrand() != null) {
                alrt.setBRAND(alert.getAlert().getBrand());
            }
            if (alert.getAlert().getCountryId() != null) {
                alrt.setCOUNTRY(alert.getAlert().getCountryId());
            }
            if (alert.getAlert().getServicesItem() != null) {
                alrt.setNSH(alert.getAlert().getServicesItem().getNsh().getGoodsItemCode());
            }
            alrt.setSTARTDATE(org.guce.siat.common.utils.DateUtils.formatSimpleDate(DateUtils.GUCE_DATE, alert.getAlert()
                    .getStartDate()));
            alrt.setENDDATE(org.guce.siat.common.utils.DateUtils
                    .formatSimpleDate(DateUtils.GUCE_DATE, alert.getAlert().getEndDate()));
            if (alert.getAlert().getManufacturer() != null) {
                alrt.setMANUFACTURER(alert.getAlert().getManufacturer());
            }
            if (alert.getAlert().getModel() != null) {
                alrt.setMODEL(alert.getAlert().getModel());
            }
            alrts.add(alrt);

        }
        alertDocument.getCONTENT().setALERTS(new org.guce.siat.jaxb.alert.ALERT.DOCUMENT.CONTENT.ALERTS());
        alertDocument.getCONTENT().getALERTS().getALERT().addAll(alrts);
        return alertDocument;
    }

    /**
     * Prepare alert document.
     *
     * @param documentSerializable the document serializable
     * @param ebxmlFolder the ebxml folder
     * @param service the service
     * @param documentType the document type
     * @return the file
     * @throws SAXException the SAX exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws JAXBException the JAXB exception
     */
    private File prepareAlertDocument(final Serializable documentSerializable, final String ebxmlFolder, String service,
            String documentType) throws SAXException, IOException, JAXBException {
        File xmlFile = null;

        final JAXBContext jaxbContext = org.guce.siat.jaxb.alert.ALERT.JAXBContextCreator.getInstance();
        final org.guce.siat.jaxb.alert.ALERT.DOCUMENT document = (org.guce.siat.jaxb.alert.ALERT.DOCUMENT) documentSerializable;
        final Marshaller jaxbMarshallerz = jaxbContext.createMarshaller();

        final JAXBSource source = new JAXBSource(jaxbContext, document);

        //Validation
        final SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = sf.newSchema(new StreamSource(new ClassPathResource("xsd/ALERT/ALERT.xsd").getInputStream()));

        final Validator validator = schema.newValidator();
        validator.setErrorHandler(null);
        validator.validate(source);

        xmlFile = new java.io.File(ebxmlFolder + File.separator + document.getMESSAGE().getNUMEROMESSAGE()
                + ESBConstants.XML_FILE_EXTENSION);
        jaxbMarshallerz.marshal(document, xmlFile);
        documentType = document.getTYPEDOCUMENT();
        service = document.getREFERENCEDOSSIER().getSERVICE();

        return xmlFile;
    }

    /**
     * Inits the header.
     *
     * @param alertDocument the alert document
     * @return the document
     */
    private DOCUMENT initHeader(final DOCUMENT alertDocument) {//init MESSAGE
        final MESSAGE message = new MESSAGE();

        message.setNUMEROMESSAGE(IOUtils.generateMessageID());
        message.setDATEEMISSION(EbmsUtility.getCurrentUTCDateTime());
        message.setNUMEROMESSAGEORIGINE(StringUtils.EMPTY);
        message.setDATEEMISSIONMSGORIGINE(StringUtils.EMPTY);
        message.setETAT(StringUtils.EMPTY);
        message.setTYPEMESSAGE(StringUtils.EMPTY);
        alertDocument.setMESSAGE(message);
        //init ROUTAGE
        final ROUTAGE routage = new ROUTAGE();

        routage.setDESTINATAIRE(StringUtils.EMPTY);
        routage.setEMETTEUR(StringUtils.EMPTY);
        alertDocument.setROUTAGE(routage);
        //init REFERENCE
        final REFERENCEDOSSIER referencedossier = new REFERENCEDOSSIER();

        //	reference dossier
        referencedossier.setREFERENCEGUCE(StringUtils.EMPTY);
        referencedossier.setREFERENCESIAT(StringUtils.EMPTY);
        referencedossier.setDATECREATION(StringUtils.EMPTY);
        referencedossier.setNUMERODEMANDE(StringUtils.EMPTY);
        referencedossier.setNUMERODOSSIER(StringUtils.EMPTY);
        referencedossier.setSERVICE(StringUtils.EMPTY);
        alertDocument.setREFERENCEDOSSIER(referencedossier);

        return alertDocument;
    }

    /**
     * Prepare alert document.
     *
     * @param documentSerializable the document serializable
     * @return the file
     * @throws JAXBException the JAXB exception
     */
    public File prepareAlertDocument(final Serializable documentSerializable) throws JAXBException {

        JAXBContext jaxbContext = null;
        Marshaller jaxbMarshallerz = null;
        java.io.File xmlFile = null;

        jaxbContext = org.guce.siat.jaxb.alert.ALERT.JAXBContextCreator.getInstance();
        final org.guce.siat.jaxb.alert.ALERT.DOCUMENT document = (org.guce.siat.jaxb.alert.ALERT.DOCUMENT) documentSerializable;
        jaxbMarshallerz = jaxbContext.createMarshaller();
        final Random randomGenerator = new Random();

        final int randomInt = randomGenerator.nextInt(99999);
        xmlFile = new java.io.File(ebxmlPropertiesService.getEbxmlFolder() + File.separator + "Alert_" + randomInt
                + ESBConstants.XML_FILE_EXTENSION);
        jaxbMarshallerz.marshal(document, xmlFile);

        return xmlFile;
    }

    /**
     * Gets the alert selected.
     *
     * @return the alert selected
     */
    public Boolean getAlertSelected() {
        return alertSelected;
    }

    /**
     * Sets the alert selected.
     *
     * @param alertSelected the new alert selected
     */
    public void setAlertSelected(final Boolean alertSelected) {
        this.alertSelected = alertSelected;
    }

    /**
     * Gets the flow guce siat service.
     *
     * @return the flow guce siat service
     */
    public FlowGuceSiatService getFlowGuceSiatService() {
        return flowGuceSiatService;
    }

    /**
     * Sets the flow guce siat service.
     *
     * @param flowGuceSiatService the new flow guce siat service
     */
    public void setFlowGuceSiatService(final FlowGuceSiatService flowGuceSiatService) {
        this.flowGuceSiatService = flowGuceSiatService;
    }

    /**
     * Gets the file type dao.
     *
     * @return the file type dao
     */
    public FileTypeDao getFileTypeDao() {
        return fileTypeDao;
    }

    /**
     * Sets the file type dao.
     *
     * @param fileTypeDao the new file type dao
     */
    public void setFileTypeDao(final FileTypeDao fileTypeDao) {
        this.fileTypeDao = fileTypeDao;
    }

    /**
     * Gets the file producer.
     *
     * @return the file producer
     */
    public FileProducer getFileProducer() {
        return fileProducer;
    }

    /**
     * Sets the file producer.
     *
     * @param fileProducer the new file producer
     */
    public void setFileProducer(final FileProducer fileProducer) {
        this.fileProducer = fileProducer;
    }

    @Override
    public Alert getSelected() {
        return super.getSelected();
    }

}
