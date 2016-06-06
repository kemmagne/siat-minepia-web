package org.guce.siat.web.ct.controller;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.CopyRecipient;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.Flow;
import org.guce.siat.common.service.EbxmlPropertiesService;
import org.guce.siat.common.service.FileItemService;
import org.guce.siat.common.service.FileProducer;
import org.guce.siat.common.service.FileService;
import org.guce.siat.common.service.FlowService;
import org.guce.siat.common.service.XmlConverterService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.ebms.ESBConstants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.core.ct.model.PaymentData;
import org.guce.siat.core.ct.service.PaymentDataService;
import org.guce.siat.core.utils.SendDocumentUtils;
import org.guce.siat.web.common.AbstractController;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


/**
 * The Class CostsController.
 */
@ManagedBean(name = "costsController")
@SessionScoped
public class CostsController extends AbstractController<PaymentData> implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5106751417655195729L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(CostsController.class);

	/** The current file. */
	private File currentFile;

	/** The current payment data. */
	private PaymentData currentPaymentData;

	/** The payment data service. */
	@ManagedProperty(value = "#{paymentDataService}")
	private PaymentDataService paymentDataService;

	/** The file service. */
	@ManagedProperty(value = "#{fileService}")
	private FileService fileService;

	/** The flow service. */
	@ManagedProperty(value = "#{flowService}")
	private FlowService flowService;

	/** The file item service. */
	@ManagedProperty(value = "#{fileItemService}")
	private FileItemService fileItemService;

	/** The transaction manager. */
	@ManagedProperty(value = "#{transactionManager}")
	private PlatformTransactionManager transactionManager;


	/** The xml converter service. */
	@ManagedProperty(value = "#{xmlConverterService}")
	private XmlConverterService xmlConverterService;
	/** send file service. */
	@ManagedProperty(value = "#{fileProducer}")
	private FileProducer fileProducer;

	/** The ebxml properties service. */
	@ManagedProperty(value = "#{ebxmlPropertiesService}")
	private EbxmlPropertiesService ebxmlPropertiesService;

	/**
	 * Instantiates a new costs controller.
	 */
	public CostsController()
	{

	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, CostsController.class.getName());
		}

	}


	/**
	 * Validate payment.
	 */
	public void validatePayment()
	{

		// Check if the step of fileItems was changed by another user when the decision popup is open
		if (fileItemService.verifyStepChangedWhileDecisionInProgress(currentFile.getFileItemsList()))
		{
			final String summary = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
					.getString(ControllerConstants.Bundle.Messages.ERROR_DIALOG_TITLE);

			final String detail = ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME, getCurrentLocale())
					.getString(ControllerConstants.Bundle.Messages.CANCEL_REQUEST_IN_PROGRESS);

			LOG.error(detail);
			// the user must refresh the details page (return to the dashboard then open the file details page) to get the correct decision list in the decision popup
			JsfUtil.showMessageInDialog(FacesMessage.SEVERITY_ERROR, summary, detail);
			return;
		}
		sendPayment();
		returnToPaymentView();

	}


	/**
	 * Send payment.
	 */
	public void sendPayment()
	{
		final DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		def.setReadOnly(false);
		final TransactionStatus status = transactionManager.getTransaction(def);
		try
		{
			Flow flowToExecute = null;
			if (Arrays.asList(FileTypeCode.CCT_CT, FileTypeCode.CCT_CT_E, FileTypeCode.CC_CT, FileTypeCode.CQ_CT).contains(
					currentFile.getFileType().getCode()))
			{
				flowToExecute = flowService.findFlowByCode(FlowCode.FL_CT_93.name());
			}
			else
			{
				if (currentFile.getFileType().getCode().equals(FileTypeCode.PIVPSRP_MINADER))
				{
					flowToExecute = flowService.findFlowByCode(FlowCode.FL_AP_168.name());
				}
				else
				{
				flowToExecute = flowService.findFlowByCode(FlowCode.FL_AP_166.name());
				}
			}

			paymentDataService.takeDecisionForPayment(currentFile, getLoggedUser(), flowToExecute, currentPaymentData);
			// convert file to document
			final String service = StringUtils.EMPTY;
			final String documentType = StringUtils.EMPTY;
			final Serializable documentSerializable = xmlConverterService.convertFileToDocument(currentFile,
					Collections.singletonList(currentPaymentData.getPaymentItemFlowList().get(0).getItemFlow().getFileItem()),
					Collections.singletonList(currentPaymentData.getPaymentItemFlowList().get(0).getItemFlow()), flowToExecute);
			java.io.File xmlFile = null;
			// prepare document to send
			if (Arrays.asList(FileTypeCode.CCT_CT, FileTypeCode.CCT_CT_E, FileTypeCode.CC_CT, FileTypeCode.CQ_CT).contains(
					currentFile.getFileType().getCode()))
			{
				xmlFile = SendDocumentUtils.prepareCctDocument(documentSerializable,
						ebxmlPropertiesService.getEbxmlFolder(), service, documentType);
			}
			else
			{
				xmlFile = SendDocumentUtils.prepareApDocument(documentSerializable,
						ebxmlPropertiesService.getEbxmlFolder(), service, documentType);
			}

			if (CollectionUtils.isNotEmpty(currentPaymentData.getPaymentItemFlowList().get(0).getItemFlow().getFlow()
					.getCopyRecipientsList()))
			{
				final List<CopyRecipient> copyRecipients = currentPaymentData.getPaymentItemFlowList().get(0).getItemFlow().getFlow()
						.getCopyRecipientsList();
				for (final CopyRecipient copyRecipient : copyRecipients)
				{
					LOG.info("SEND COPY RECIPIENT TO {}", copyRecipient.getToAuthority().getRole());
					final Map<String, Object> data = new HashMap<String, Object>();
					final Path path = Paths.get(xmlFile.getAbsolutePath());
					final byte[] ebxml = Files.readAllBytes(path);
					data.put(ESBConstants.FLOW, ebxml);
					data.put(ESBConstants.ATTACHMENT, null);
					data.put(ESBConstants.TYPE_DOCUMENT, documentType);
					data.put(ESBConstants.SERVICE, service);
					data.put(ESBConstants.MESSAGE, null);
					data.put(ESBConstants.EBXML_TYPE, "STANDARD");
					data.put(ESBConstants.TO_PARTY_ID, copyRecipient.getToAuthority().getRole());
					data.put(ESBConstants.DEAD, "0");
					fileProducer.sendFile(data);
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Message sent to OUT queue");
					}

				}
			}
			transactionManager.commit(status);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			transactionManager.rollback(status);

			LOG.error("####SEND DECISION Transaction rollbacked#### {}", e.getMessage());

		}
	}


	/**
	 * Return to payment view.
	 */
	public void returnToPaymentView()
	{
		try
		{
			setPageUrl(ControllerConstants.Pages.FO.PAYMENT_INDEX_PAGE);
			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();

			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, pageUrl));

			extContext.redirect(url);
		}
		catch (final IOException ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
	}


	/**
	 * Gets the payment data service.
	 *
	 * @return the paymentDataService
	 */
	public PaymentDataService getPaymentDataService()
	{
		return paymentDataService;
	}

	/**
	 * Sets the payment data service.
	 *
	 * @param paymentDataService
	 *           the paymentDataService to set
	 */
	public void setPaymentDataService(final PaymentDataService paymentDataService)
	{
		this.paymentDataService = paymentDataService;
	}

	/**
	 * Gets the file service.
	 *
	 * @return the fileService
	 */
	public FileService getFileService()
	{
		return fileService;
	}

	/**
	 * Sets the file service.
	 *
	 * @param fileService
	 *           the fileService to set
	 */
	public void setFileService(final FileService fileService)
	{
		this.fileService = fileService;
	}

	/**
	 * Gets the current file.
	 *
	 * @return the currentFile
	 */
	public File getCurrentFile()
	{
		return currentFile;
	}

	/**
	 * Sets the current file.
	 *
	 * @param currentFile
	 *           the currentFile to set
	 */
	public void setCurrentFile(final File currentFile)
	{
		this.currentFile = currentFile;
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
	 * Gets the file item service.
	 *
	 * @return the fileItemService
	 */
	public FileItemService getFileItemService()
	{
		return fileItemService;
	}

	/**
	 * Sets the file item service.
	 *
	 * @param fileItemService
	 *           the fileItemService to set
	 */
	public void setFileItemService(final FileItemService fileItemService)
	{
		this.fileItemService = fileItemService;
	}

	/**
	 * Gets the current payment data.
	 *
	 * @return the currentPaymentData
	 */
	public PaymentData getCurrentPaymentData()
	{
		return currentPaymentData;
	}

	/**
	 * Sets the current payment data.
	 *
	 * @param currentPaymentData
	 *           the currentPaymentData to set
	 */
	public void setCurrentPaymentData(final PaymentData currentPaymentData)
	{
		this.currentPaymentData = currentPaymentData;
	}

	/**
	 * Gets the transaction manager.
	 *
	 * @return the transaction manager
	 */
	public PlatformTransactionManager getTransactionManager()
	{
		return transactionManager;
	}

	/**
	 * Sets the transaction manager.
	 *
	 * @param transactionManager
	 *           the new transaction manager
	 */
	public void setTransactionManager(final PlatformTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}

	/**
	 * Gets the xml converter service.
	 *
	 * @return the xml converter service
	 */
	public XmlConverterService getXmlConverterService()
	{
		return xmlConverterService;
	}

	/**
	 * Sets the xml converter service.
	 *
	 * @param xmlConverterService
	 *           the new xml converter service
	 */
	public void setXmlConverterService(final XmlConverterService xmlConverterService)
	{
		this.xmlConverterService = xmlConverterService;
	}

	/**
	 * Gets the file producer.
	 *
	 * @return the file producer
	 */
	public FileProducer getFileProducer()
	{
		return fileProducer;
	}

	/**
	 * Sets the file producer.
	 *
	 * @param fileProducer
	 *           the new file producer
	 */
	public void setFileProducer(final FileProducer fileProducer)
	{
		this.fileProducer = fileProducer;
	}

	/**
	 * Gets the ebxml properties service.
	 *
	 * @return the ebxml properties service
	 */
	public EbxmlPropertiesService getEbxmlPropertiesService()
	{
		return ebxmlPropertiesService;
	}

	/**
	 * Sets the ebxml properties service.
	 *
	 * @param ebxmlPropertiesService
	 *           the new ebxml properties service
	 */
	public void setEbxmlPropertiesService(final EbxmlPropertiesService ebxmlPropertiesService)
	{
		this.ebxmlPropertiesService = ebxmlPropertiesService;
	}



}
