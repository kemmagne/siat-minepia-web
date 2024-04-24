package org.guce.siat.web.reports.exporter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.swing.text.DateFormatter;
import org.guce.siat.common.model.ItemFlowData;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.ContentStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.lookup.ServiceUtility;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.model.ItemFlow;
import org.guce.siat.common.model.User;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.common.utils.QRCodeGenerator;
import org.guce.siat.common.utils.enums.FlowCode;
import org.guce.siat.common.utils.enums.StepCode;
import org.guce.siat.common.utils.ged.CmisClient;
import org.guce.siat.common.utils.ged.CmisSession;
import org.guce.siat.core.ct.model.UserStampSignature;
import org.guce.siat.core.ct.service.UserStampSignatureService;
import org.guce.siat.web.ct.controller.util.Utils;
import org.guce.siat.web.reports.vo.AtmMinepiaFileItemVo;
import org.guce.siat.web.reports.vo.AtmMinepiaFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AtMinepiaExporter.
 */
public class AtmMinepiaExporter extends AbstractReportInvoker {

	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AtmMinepiaExporter.class);
        
        private static final SimpleDateFormat DATEFORMATTER = new SimpleDateFormat("dd/MM/yyyy");
                

	/**
	 * The file.
	 */
	private final File file;

	/**
	 * Instantiates a new at minepia exporter.
	 *
	 * @param file the file
	 */
        

	public AtmMinepiaExporter(final File file) {
		super("ATM_MINEPIA", "ATM_MINEPIA");
		this.file = file;
	}
        
         /**
        * Dans le cadre de la procédure visa technique MINADER, il est possible de
        * générer deux types de rapport. D'où le paramètre jasperFileName
        *
        * @param jasperFileName
        * @param file
        */
       public AtmMinepiaExporter(String jasperFileName, final File file) {
           super(jasperFileName, "ATM_MINEPIA");
           this.file = file;
       }
       
         public static ItemFlow findLastItemFlow(File file ) {
       if(Objects.nonNull(file.getFileItemsList()) && !file.getFileItemsList().isEmpty()){
            List<ItemFlow> itemsFlows =  file.getFileItemsList().get(0).getItemFlowsList();
                    if(Objects.nonNull(itemsFlows) && Boolean.FALSE.equals(itemsFlows.isEmpty())){
                        Collections.sort(itemsFlows, new Comparator<ItemFlow>() {
                                    @Override
                                    public int compare(ItemFlow itf1, ItemFlow itf2) {
                                        return itf1.getCreated().compareTo(itf2.getCreated());
                                    }
                                });
                        return itemsFlows.get(itemsFlows.size() -1);
                    }
       }
        return null;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
	 */
          public  ItemFlow findOldestItemFlow(File file) {
        if(Objects.nonNull(file.getFileItemsList()) && !file.getFileItemsList().isEmpty()){
            List<ItemFlow> itemsFlows =  file.getFileItemsList().get(0).getItemFlowsList();
            if(Objects.nonNull(itemsFlows) && Boolean.FALSE.equals(itemsFlows.isEmpty())){
                for (ItemFlow itf : itemsFlows) {
                    if(itf.getFlow().getCode().equals(FlowCode.FL_AP_ATM_16.name()) || itf.getFlow().getCode().equals(FlowCode.FL_AP_ATM_29.name())){
                        return itf;      
                    }
                }
            }
        }
        return null;
    }
       
	@Override
	public JRBeanCollectionDataSource getReportDataSource() {
		final AtmMinepiaFileVo atmMinepiaFileVo = new AtmMinepiaFileVo();
		final List<AtmMinepiaFileItemVo> fileItemVos = new ArrayList<AtmMinepiaFileItemVo>();
		final SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.PATTERN_DDMMYYYY);
                final String cebs = "<<CEBS>>";
                
                atmMinepiaFileVo.setCebs(cebs);
                     ItemFlow lastItemFlow = this.findLastItemFlow(file);
                     for(final ItemFlowData itemFlowData : lastItemFlow.getItemFlowsDataList()){
                                                  switch(itemFlowData.getDataType().getLabel()){
                                                      case "Quantité de produit de l'importateur":
                                                           if(itemFlowData.getValue() != null && itemFlowData.getValue().length() > 0){
                                                               atmMinepiaFileVo.setQuandityRequired(Double.parseDouble(itemFlowData.getValue()));
                                                           }
                                                           break;
                                                      
                                                      case "Quantité de produit proposée":
                                                          if(itemFlowData.getValue() != null && itemFlowData.getValue().length() > 0){
                                                              Double quandityProposeed = Double.parseDouble(itemFlowData.getValue());
                                                              Double quandityGiven = 4000.00;
                                                              String quandity = quandityGiven.toString();
                                                               atmMinepiaFileVo.setQuanditeproposed(quandityProposeed);
                                                           }
                                                           break;
                                                          
                                                      case "Validité de l'avis technique proposée":
                                                          if(itemFlowData.getValue() != null && itemFlowData.getValue().length() > 0){
                                                              try{
                                                                atmMinepiaFileVo.setValidityDate(DATEFORMATTER.parse(itemFlowData.getValue()));
                                                              }catch(ParseException e){
                                                              
                                                                LOG.error("{}",  e);
                                                              }
                                                               
                                                           }
                                                           break;
                                                           
//                                                           case "CODE_TARIF_DESIGNATION":
//								atmMinepiaFileVo.setDesignation(itemFlowData.getValue());
//							    break;
                                                          
                                                      case "Ampliations proposées":
                                                         if(itemFlowData.getValue() != null && itemFlowData.getValue().length() > 0){
                                                               atmMinepiaFileVo.setAmpliation(itemFlowData.getValue());
                                                               String ampliantion = atmMinepiaFileVo.getAmpliation();
                                                           }
                                                           break;
                                                  }    
                                              }
                

		if (file != null) {
			List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			atmMinepiaFileVo.setDecisionDate(file.getSignatureDate());
			atmMinepiaFileVo.setDecisionPlace(file.getBureau().getAddress());
                        atmMinepiaFileVo.setBp(file.getClient().getPostalCode());
                        atmMinepiaFileVo.setDecisionNumber(file.getNumeroDossier());
                        
                          if(file.getNumeroDossier().contains("M0") && file.getParent() != null){
                                           File parentFile = file.getParent();
                                           fileFieldValueList = parentFile.getFileFieldValueList();
                                           String amendement = "Amendement N °";
                                          atmMinepiaFileVo.setAmmendement(amendement.concat(String.valueOf(file.getParent().getChildrenList().size())));
                                          atmMinepiaFileVo.setDecisionNumber(file.getNumeroDossier().replace("M01", ""));
                       } 
                   
                        String a = file.getNumeroDossier();
			if (file.getClient() != null) {
				atmMinepiaFileVo.setImporter(file.getClient().getCompanyName());
			}

			if (file.getCountryOfOrigin() != null) {
				atmMinepiaFileVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
                        }  
                       if (file.getClient() != null) {
				atmMinepiaFileVo.setTown(file.getClient().getCity()); 
                                atmMinepiaFileVo.setImporter(file.getClient().getCompanyName());
			}
                         if (CollectionUtils.isNotEmpty(fileFieldValueList)) {

			for (final FileFieldValue fileFieldValue : fileFieldValueList) {
				switch (fileFieldValue.getFileField().getCode()) {
//					case "NUMERO_ATM_MINEPIA":
//						atmMinepiaFileVo.setDecisionNumber(fileFieldValue.getValue());
//						break;
                               
//					case "DATE_VALIDITE_AVIS_TECHNIQUE":
//						if (StringUtils.isNotBlank(fileFieldValue.getValue())) {
//							try {
//								if (file.getClient() != null && file.getClient().getCommerceApprovalObtainedDate() != null) {
//									final Date convertedDate = formatter.parse(file.getClient().getCommerceApprovalObtainedDate());
//									atmMinepiaFileVo.setValidityDate(convertedDate);
//								}
//							} catch (final ParseException e) {
//								LOG.error(Objects.toString(e), e);
//							}
//						}
//
//						break;

					default:
						break;
				}

			}
                      }
                       // ItemFlow lastItemFlow = this.findLastItemFlow(file);       
                      if(Objects.nonNull(lastItemFlow) && file.getFileItemsList().get(0).getDraft() && !file.getFileItemsList().get(0).getStep().getStepCode().equals(StepCode.ST_AP_44) && Arrays.asList(FlowCode.FL_AP_ATM_07.name(), FlowCode.FL_AP_ATM_10.name(), FlowCode.FL_AP_ATM_13.name(), FlowCode.FL_AP_ATM_16.name(), FlowCode.FL_AP_ATM_24.name(), FlowCode.FL_AP_ATM_25.name(), FlowCode.FL_AP_ATM_27.name(), FlowCode.FL_AP_ATM_13R.name(), FlowCode.FL_AP_ATM_16R.name()).contains(lastItemFlow.getFlow().getCode())){
                        
                             atmMinepiaFileVo.setDraft(getRealPath(IMAGES_PATH, "draft", "jpg"));

                        }             
                                    
                                    
			final List<FileItem> fileItemList = file.getFileItemsList();

			if (CollectionUtils.isNotEmpty(fileItemList)) {
				for (final FileItem fileItem : fileItemList) {
					final AtmMinepiaFileItemVo fileItemVo = new AtmMinepiaFileItemVo();

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList)) {
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {
							switch (fileItemFieldValue.getFileItemField().getCode()) {
								case "CODE_TARIF_DESIGNATION":
									atmMinepiaFileVo.setDesignation(fileItemFieldValue.getValue());
                                                                        String designation = atmMinepiaFileVo.getDesignation();
									break;

								case "MARCHANDISE_QUANTITE":
									fileItemVo.setQuantity(Double.valueOf(fileItemFieldValue.getValue()));
									break;

								case "UNITE":
                                                                        fileItemVo.setMeasurementUnit(fileItemFieldValue.getValue());
                                                                        String unit = atmMinepiaFileVo.getMeasurementUnit();
									break;

								default:
									break;
							}
						}
                                              //ItemFlow lastItemFlow = this.findOldestItemFlow(file);
                                              for(final ItemFlowData itemFlowData : lastItemFlow.getItemFlowsDataList()){
                                                  switch(itemFlowData.getDataType().getLabel()){
                                                      case "Quantité de produit de l'importateur":
                                                           if(itemFlowData.getValue() != null && itemFlowData.getValue().length() > 0){
                                                               atmMinepiaFileVo.setQuandityRequired(Double.parseDouble(itemFlowData.getValue()));
                                                           }
                                                           break;
                                                      
                                                      case "Quantité de produit proposée":
                                                          if(itemFlowData.getValue() != null && itemFlowData.getValue().length() > 0){
                                                              Double quandityProposeed = Double.parseDouble(itemFlowData.getValue());
                                                              Double quandityGiven = 4000.00;
                                                              String quandity = quandityGiven.toString();
                                                               atmMinepiaFileVo.setQuanditeproposed(quandityProposeed);
                                                           }
                                                           break;
                                                          
                                                      case "Validité de l'avis technique proposée":
                                                          if(itemFlowData.getValue() != null && itemFlowData.getValue().length() > 0){
                                                              try{
                                                                atmMinepiaFileVo.setValidityDate(DATEFORMATTER.parse(itemFlowData.getValue()));
                                                              }catch(ParseException e){
                                                              
                                                                LOG.error("{}",  e);
                                                              }
                                                               
                                                           }
                                                           break;
                                                           
//                                                           case "CODE_TARIF_DESIGNATION":
//								atmMinepiaFileVo.setDesignation(itemFlowData.getValue());
//							    break;
                                                          
                                                      case "Ampliations proposées":
                                                         if(itemFlowData.getValue() != null && itemFlowData.getValue().length() > 0){
                                                               atmMinepiaFileVo.setAmpliation(itemFlowData.getValue());
                                                               String ampliantion = atmMinepiaFileVo.getAmpliation();
                                                           }
                                                           break;
                                                  }    
                                              }
					}
					fileItemVos.add(fileItemVo);
				}
			}
			atmMinepiaFileVo.setFileItemList(fileItemVos);
		}
                
                this.putSignatureAndStamp(atmMinepiaFileVo);
                this.putQrCode(atmMinepiaFileVo);

		return new JRBeanCollectionDataSource(Collections.singleton(atmMinepiaFileVo));

	}
	
   

         private void putSignatureAndStamp(AtmMinepiaFileVo atmMinepiaFileVo) {
        try {
            ItemFlow lastItemFlow = this.findOldestItemFlow(file);
                 //findLastItemFlow(file);
            User signatory = (
                lastItemFlow != null
            ) ? lastItemFlow.getSender() : null;
            String loginSignatory = (signatory != null) ? signatory.getLogin() : null;
            
            Session sessionCmisClient = CmisSession.getInstance();
            ContentStream contentStream;
            UserStampSignatureService stampSignatureService = ServiceUtility.getBean(UserStampSignatureService.class);
            UserStampSignature stampSignature = (signatory != null) ? stampSignatureService.findUserStampSignatureByUser(signatory) : null;
            if (stampSignature != null && stampSignature.getSignaturePath() != null) {
                String signatureFilePath = stampSignature.getSignaturePath();
                contentStream = CmisClient.getDocumentByPath(sessionCmisClient, signatureFilePath);
                if (contentStream != null) {
                    String ext = signatureFilePath.substring(signatureFilePath.lastIndexOf('.'));
                    String signatureNameSignatory = (loginSignatory != null) ? loginSignatory.concat("signature") : "signature";
                    java.io.File signatureTempFile = Files.createTempFile(signatureNameSignatory, ext).toFile();
                    byte[] signatureBytes = IOUtils.toByteArray(contentStream.getStream());
                    FileUtils.writeByteArrayToFile(signatureTempFile, signatureBytes);
                    atmMinepiaFileVo.setSignature(signatureTempFile.getPath());
                }
            }
            if (stampSignature != null && stampSignature.getStampPath() != null) {
                String stampFilePath = stampSignature.getStampPath();
                contentStream = CmisClient.getDocumentByPath(sessionCmisClient, stampFilePath);
                if (contentStream != null) {
                    String ext = stampFilePath.substring(stampFilePath.lastIndexOf('.'));
                    String stampNameSignatory = (loginSignatory != null) ? loginSignatory.concat("stamp") : "stamp";
                    java.io.File stampTempFile = Files.createTempFile(stampNameSignatory, ext).toFile();
                    byte[] stampBytes = IOUtils.toByteArray(contentStream.getStream());
                    FileUtils.writeByteArrayToFile(stampTempFile, stampBytes);
                    atmMinepiaFileVo.setStamp(stampTempFile.getPath());
                }
            }
        } catch (Exception ex) {
            logger.error(MessageFormat.format("Cannot put signature or/and stamp for {0}", file.getNumeroDossier()), ex);
        }
    }

    private void putQrCode(AtmMinepiaFileVo atmMinepiaFileVo) {
        try {
            String qrCodeContent = Utils.getFinalSecureDocumentUrl(file);
            int qrCodeImageSize = 512;
            InputStream qrCodeStream = new ByteArrayInputStream(new QRCodeGenerator().generateQR(qrCodeContent, qrCodeImageSize));
            byte[] qrCodeBytes = IOUtils.toByteArray(qrCodeStream);
            java.io.File qrCodeTempFile = Files.createTempFile(file.getNumeroDossier().concat("qr_code"), ".png").toFile();
            FileUtils.writeByteArrayToFile(qrCodeTempFile, qrCodeBytes);
            atmMinepiaFileVo.setQrCode(qrCodeTempFile.getPath());
        } catch (Exception ex) {
            logger.error(MessageFormat.format("Cannot put qr code for {0}", file.getNumeroDossier()), ex);
        }
    }
       /**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
       
}
