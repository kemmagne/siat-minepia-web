package org.guce.siat.web.reports.exporter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.lookup.ServiceUtility;
import org.guce.siat.common.model.Container;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.service.ApplicationPropretiesService;
import org.guce.siat.common.utils.QRCodeGenerator;
import org.guce.siat.core.ct.model.CCTCPParamValue;
import org.guce.siat.core.ct.model.TreatmentInfos;
import org.guce.siat.core.ct.util.enums.CctExportProductType;
import org.guce.siat.web.ct.controller.util.ReportGeneratorUtils;
import org.guce.siat.web.ct.controller.util.Utils;
import org.guce.siat.web.reports.vo.CtCctCpEFileItemVo;
import org.guce.siat.web.reports.vo.CtCctCpEFileVo;

/**
 * The Class CtCctCpEExporter.
 */
public class CtCctCpEExporter extends AbstractReportInvoker {

    /**
     * The file.
     */
    private final File file;
    /**
     * the treatment order
     */
    private final TreatmentInfos treatmentInfos;
    /**
     * the paramValue
     */
    private final CCTCPParamValue paramValue;

    /**
     * Instantiates a new ct cct cp e exporter.
     *
     * @param file the file
     */
    public CtCctCpEExporter(final File file) {
        super("CT_CCT_CP_E", "CT_CCT_CP_E");
        this.file = file;
        this.treatmentInfos = null;
        this.paramValue = null;
    }

    public CtCctCpEExporter(final TreatmentInfos treatmentInfos, CCTCPParamValue paramValue, final String jasperFileName) {
        super(jasperFileName, jasperFileName + ".pdf");
        this.file = treatmentInfos.getItemFlow().getFileItem().getFile();
        this.treatmentInfos = treatmentInfos;
        this.paramValue = paramValue;

    }

    public CtCctCpEExporter(final File file, final TreatmentInfos treatmentInfos, CCTCPParamValue paramValue, final String jasperFileName) {
        super(jasperFileName, jasperFileName + ".pdf");
        this.file = file;
        this.treatmentInfos = treatmentInfos;
        this.paramValue = paramValue;
    }

    /*
    * (non-Javadoc)
    *
    * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        final CtCctCpEFileVo ctCctCpEFileVo = new CtCctCpEFileVo();

        ctCctCpEFileVo.setDecisionPlace(file.getBureau().getLabelFr());
        ctCctCpEFileVo.setDecisionDate(file.getSignatureDate());
        ctCctCpEFileVo.setSignatoryUser(file.getSignatory());
        if (file.getSignatory() != null) {
            ctCctCpEFileVo.setSignatoryName(file.getSignatory().getLastName() + " " + file.getSignatory().getFirstName());
        }
        if (file.getClient() != null) {
            ctCctCpEFileVo.setExporterName(file.getClient().getCompanyName());
            ctCctCpEFileVo.setExporterAddress(file.getClient().getFirstAddress());
            if (file.getClient().getCountry() != null) {
                ctCctCpEFileVo.setExporterCountry(file.getClient().getCountry().getCountryName());
            }
            ctCctCpEFileVo.setExporterTown(file.getClient().getCity());
            ctCctCpEFileVo.setExporterPoBox(file.getClient().getPostalCode());
        }
        ctCctCpEFileVo.setOrigin(file.getCountryOfOrigin() != null ? file.getCountryOfOrigin().getCountryName() : null);
        ctCctCpEFileVo.setDestination(file.getCountryOfDestination() != null ? file.getCountryOfDestination().getCountryName() : null);

        // traitement
        if (treatmentInfos != null) {
            ctCctCpEFileVo.setTypeOfTreatment(treatmentInfos.getTypeOfTreatment());
            ctCctCpEFileVo.setAdditionalInfos(treatmentInfos.getAdditionalInfos());
            ctCctCpEFileVo.setFumigation(treatmentInfos.isFumigation());
            ctCctCpEFileVo.setDisenfection(treatmentInfos.isDisinfection());
            ctCctCpEFileVo.setChemicalProduct(treatmentInfos.getChemicalProduct());
            ctCctCpEFileVo.setDuration(treatmentInfos.getDuration());
            ctCctCpEFileVo.setTemperature(treatmentInfos.getTemperature());
            ctCctCpEFileVo.setConcentration(treatmentInfos.getConcentration());
            ctCctCpEFileVo.setTreatmentsCarriedOut(treatmentInfos.getTreatmentsCarriedOut());
            ctCctCpEFileVo.setAdditionalDeclaration(treatmentInfos.getAdditionnalDeclaration());
            ctCctCpEFileVo.setTreatmentDate(treatmentInfos.getTreatmentDate());
//            ctCctCpEFileVo.setOriginCountryPhytoNumber(treatmentInfos.getCertificatCountryOrigin());
            if (treatmentInfos.getTreatmentDate() != null && treatmentInfos.getTreatmentEndDate() != null && treatmentInfos.getTreatmentEndDate().after(treatmentInfos.getTreatmentDate())) {
                ctCctCpEFileVo.setTreatmentEndDate(treatmentInfos.getTreatmentEndDate());
            }
        }

        final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

        final String productType = getFileFieldValueService().findValueByFileFieldAndFile(CctExportProductType.getFileFieldCode(), file).getValue();
        String emballage = Utils.getProductTypePackaging().get(productType);
        ctCctCpEFileVo.setPackaging(productType);
        String containersNumbers = null;
        boolean hasContainers = false;
        String packageNumber = null;
        if (CollectionUtils.isNotEmpty(fileFieldValueList)) {

            for (final FileFieldValue fileFieldValue1 : fileFieldValueList) {
                switch (fileFieldValue1.getFileField().getCode()) {
                    case "NUMERO_CT_CCT_CP_E":
                        ctCctCpEFileVo.setDecisionNumber(fileFieldValue1.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_LIEU_CHARGEMENT_LIBELLE":
                        ctCctCpEFileVo.setDeliveryPlace(fileFieldValue1.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
                        ctCctCpEFileVo.setMeansOfTransport(fileFieldValue1.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_LIEU_DECHARGEMENT_LIBELLE":
                        ctCctCpEFileVo.setPointOfEntry(fileFieldValue1.getValue());
                        break;
                    case "INFORMATIONS_GENERALES_SIGNATAIRE_NOM":
                        if (ctCctCpEFileVo.getSignatoryName() == null) {
                            ctCctCpEFileVo.setSignatoryName(fileFieldValue1.getValue());
                        }
                        break;
                    case "DESTINATAIRE_RAISONSOCIALE":
                        ctCctCpEFileVo.setConsigneeName(fileFieldValue1.getValue());
                        break;
                    case "DESTINATAIRE_ADRESSE_ADRESSE1":
                        ctCctCpEFileVo.setConsigneeAddress1(fileFieldValue1.getValue());
                        break;
                    case "DESTINATAIRE_AUTRE_CONTACT":
                        ctCctCpEFileVo.setConsignee(fileFieldValue1.getValue());
                        break;
                    case "DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS":
                        ctCctCpEFileVo.setConsigneeCountry(fileFieldValue1.getValue());
                        break;
                    case "DESTINATAIRE_ADRESSE_VILLE":
                        ctCctCpEFileVo.setConsigneeTown(fileFieldValue1.getValue());
                        break;
                    case "DESTINATAIRE_ADRESSE_BP":
                        ctCctCpEFileVo.setConsigneePoBox(fileFieldValue1.getValue());
                        break;
                    case "CLIENT_AUTRE_CONTACT":
                        ctCctCpEFileVo.setExporter(fileFieldValue1.getValue());
                        break;
                    case "INSPECTION_CONTENEURS_CONTENEUR":
                        containersNumbers = fileFieldValue1.getValue();
                        break;
                    case "NUMEROS_LOTS":
                        packageNumber = fileFieldValue1.getValue();
                        break;
                    case "AUTRE_INFORMATION_RENSEIGNEMENT_COMPLEMENTAIRE":
                        ctCctCpEFileVo.setUserAddInfos(fileFieldValue1.getValue());
                        break;
                    case "TYPE_DOSSIER_EGUCE":
                        ctCctCpEFileVo.setTransit("2".equals(fileFieldValue1.getValue()));
                        break;
                }
            }
            if (StringUtils.isNotBlank(containersNumbers)) {

                hasContainers = true;
                final String[] tab1 = containersNumbers.split(";");
                final int size = tab1.length;
                final int positionScelles = "Type".equalsIgnoreCase(tab1[0].split(",")[1]) ? 2 : 1;
                final StringBuilder builder = new StringBuilder();
                for (int i = 1; i < size; i++) {
                    if (StringUtils.isBlank(tab1[i])) {
                        continue;
                    }
                    final String[] tab2 = tab1[i].split(",");
                    builder.append(tab2[0]).append("/").append(tab2[positionScelles]).append(" ");
                }

                String contNumbers = builder.toString().trim();
                String containerNumbers[] = contNumbers.split(" ");
                int maxContainerNumber = 12;
                if (paramValue != null && paramValue.getMaxContainerNumber() > 0) {
                    maxContainerNumber = paramValue.getMaxContainerNumber();
                }
                if (paramValue != null && containerNumbers.length > maxContainerNumber) {
                    List<String> containers1 = new ArrayList<>();
                    List<String> containers2 = new ArrayList<>();
                    int containerCount = 0;
                    for (String containerNumber : containerNumbers) {
                        containerCount++;
                        if (containerCount <= maxContainerNumber) {
                            containers1.add(containerNumber);
                        } else {
                            containers2.add(containerNumber);
                        }
                    }
                    ctCctCpEFileVo.setContainersNumbers(StringUtils.join(containers1, " - ").concat("\n- " + paramValue.getLabelMore()));
                    ctCctCpEFileVo.setContainersNumbersAnnex(StringUtils.join(containers2, " - "));
                } else {
                    ctCctCpEFileVo.setContainersNumbers(contNumbers);
                }
            } else {
                List<Container> containers = file.getContainers();
                if (CollectionUtils.isNotEmpty(containers)) {
                    hasContainers = true;
                    final List<String> containers1 = new ArrayList<>();
                    final List<String> containers2 = new ArrayList<>();
                    final int maxContainersNumber;
                    if (paramValue != null && paramValue.getMaxContainerNumber() > 0) {
                        maxContainersNumber = paramValue.getMaxContainerNumber();
                    } else {
                        maxContainersNumber = 12;
                    }
                    final AtomicInteger counter = new AtomicInteger(0);
                    Collection<String> containerNumbers = CollectionUtils.collect(containers, new Transformer() {
                        @Override
                        public String transform(Object input) {
                            Container container = (Container) input;
                            if (counter.intValue() < maxContainersNumber) {
                                containers1.add(String.format("%s/%s", container.getContNumber(), container.getContSeal1()));
                            } else {
                                containers2.add(String.format("%s/%s", container.getContNumber(), container.getContSeal1()));
                            }
                            counter.addAndGet(1);
                            return String.format("%s/%s", container.getContNumber(), container.getContSeal1());
                        }
                    });

                    if (paramValue != null && containers.size() > maxContainersNumber) {
                        ctCctCpEFileVo.setContainersNumbers(StringUtils.join(containers1, " - ").concat("\n- " + paramValue.getLabelMore()));
                        ctCctCpEFileVo.setContainersNumbersAnnex(StringUtils.join(containers2, " - "));
                    } else {
                        ctCctCpEFileVo.setContainersNumbers(StringUtils.join(containerNumbers, " - "));
                    }
                }
            }

            if (StringUtils.isNotBlank(packageNumber)) {

                String packagesNumbers[] = packageNumber.split(" ");
                int maxPackagesNumber = 12;
                if (paramValue != null && paramValue.getMaxPackageNumber() > 0) {
                    maxPackagesNumber = paramValue.getMaxPackageNumber();
                }
                if (paramValue != null && packagesNumbers.length > maxPackagesNumber) {
                    List<String> packages1 = new ArrayList<>();
                    List<String> packages2 = new ArrayList<>();
                    int containerCount = 0;
                    for (String containerNumber : packagesNumbers) {
                        containerCount++;
                        if (containerCount <= maxPackagesNumber) {
                            packages1.add(containerNumber);
                        } else {
                            packages2.add(containerNumber);
                        }
                    }
                    ctCctCpEFileVo.setLotsNumbers(StringUtils.join(packages1, " - ").concat("\n- " + paramValue.getLabelMore()));
                    ctCctCpEFileVo.setLotsNumbersAnnex(StringUtils.join(packages2, " - "));
                } else {

                    ctCctCpEFileVo.setLotsNumbers(packageNumber);
                }

            }
        }
        ctCctCpEFileVo.setDecisionNumber(file.getNumeroDossier());

        List<FileItem> fileItemList = file.getFileItemsList();

        List<CtCctCpEFileItemVo> fileItemVos = new ArrayList<>();

        BigDecimal netWeight = BigDecimal.ZERO;
        BigDecimal grossWeight = BigDecimal.ZERO;

        if (CollectionUtils.isNotEmpty(fileItemList)) {

            List<String> commoditiesList = new ArrayList<>();
            List<String> commoditiesListAttachment = new ArrayList<>();
            int commoditiesCount = 0;

            String unit = "";
            FileItemFieldValue unitFifv = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("UNITE", fileItemList.get(0));
            if (unitFifv != null) {
                unit = unitFifv.getValue();
            }

            List<String> othersGrossWeightList = new ArrayList<>();
            List<String> othersGrossWeightAnnextList = new ArrayList<>();
            FileItemFieldValue fileItemFieldValue;
            for (FileItem fileItem : fileItemList) {

                String comName = "";
                fileItemFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOM_COMMERCIAL", fileItem);
                if (fileItemFieldValue != null) {
                    comName = fileItemFieldValue.getValue();
                }
                FileItemFieldValue botanicFileItemFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOM_BOTANIQUE", fileItem);
                if (botanicFileItemFieldValue != null) {
                    comName += " (" + botanicFileItemFieldValue.getValue() + ")";
                }
                String nb = "";
                if (Utils.getCacaProductsTypes().contains(productType)) {
                    FileItemFieldValue nsf = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOMBRE_SACS", fileItem);
                    if (nsf != null) {
                        nb = nsf.getValue();
                    }

                    netWeight = netWeight.add(new BigDecimal(fileItem.getQuantity()));
                } else if (Utils.getWoodProductsTypes().contains(productType)) {
                    FileItemFieldValue ngf = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOMBRE_GRUMES", fileItem);
                    if (ngf != null) {
                        nb = ngf.getValue();
                    } else {
                        nb = fileItem.getQuantity();
                    }

                    FileItemFieldValue volumeFifv = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("VOLUME", fileItem);
                    if (volumeFifv != null) {
                        netWeight = netWeight.add(new BigDecimal(volumeFifv.getValue()));
                    }
                } else if (Utils.COTONPRODUCTTYPE.equalsIgnoreCase(productType)) {
                    FileItemFieldValue ngf = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("NOMBRE_GRUMES", fileItem);
                    if (ngf != null) {
                        nb = ngf.getValue();
                    } else {
                        nb = fileItem.getQuantity();
                    }
                    FileItemFieldValue pnf = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("POIDS", fileItem);
                    if (pnf != null) {
                        netWeight = netWeight.add(new BigDecimal(pnf.getValue()));
                    } else if (StringUtils.isNotBlank(fileItem.getQuantity())) {
                        netWeight = netWeight.add(new BigDecimal(fileItem.getQuantity()));
                    }
                    unit = Utils.getProductTypePackaging().get(productType);
                } else {
                    nb = fileItem.getQuantity();
                    emballage = unit;
                    netWeight = netWeight.add(new BigDecimal(fileItem.getQuantity()));
                }
                commoditiesCount++;
                int maxGoodsNumber = 6;
                if (paramValue != null && paramValue.getMaxGoodsLineNumber() > 0) {
                    maxGoodsNumber = paramValue.getMaxGoodsLineNumber();
                }
                if (paramValue != null && commoditiesCount <= maxGoodsNumber) {
                    commoditiesList.add(String.format("%s %s %s", nb, emballage, comName));
                } else {
                    commoditiesListAttachment.add(String.format("%s %s %s", nb, emballage, comName));
                }

                fileItemFieldValue = getFileFieldValueService().findFileItemFieldValueByCodeAndFileItem("POIDS_BRUT", fileItem);
                if (fileItemFieldValue != null) {
                    String gw = fileItemFieldValue.getValue();
                    grossWeight = grossWeight.add(new BigDecimal(gw));
                    if (paramValue != null && commoditiesCount <= maxGoodsNumber) {
                        othersGrossWeightList.add(String.format("%s KG", gw));
                    } else {
                        othersGrossWeightAnnextList.add(String.format("%s KG", gw));
                    }
                } else {
                    if (paramValue != null && commoditiesCount <= maxGoodsNumber) {
                        othersGrossWeightList.add("-");
                    } else {
                        othersGrossWeightAnnextList.add("-");
                    }
                }
            }

            StringBuilder builder = new StringBuilder();
            if (Utils.getCacaProductsTypes().contains(productType)) {
                builder.append("PN : ");
            } else if (Utils.COTONPRODUCTTYPE.equalsIgnoreCase(productType)) {
                builder.append("PN : ");
                unit = "KG";
            } else {
                builder.append("VN : ");
            }
            if (Utils.getWoodProductsTypes().contains(productType)) {
                unit = "M3";
            }

            builder.append(netWeight).append(" ").append(unit);
            builder.append("<br/>").append("PB : ").append(grossWeight.toString()).append(" KG");
            if (!CctExportProductType.AUTRES.name().equals(productType)) {
                ctCctCpEFileVo.setQuantities(builder.toString());
            } else {
                ctCctCpEFileVo.setQuantities(StringUtils.join(othersGrossWeightList, "<br/>"));
                ctCctCpEFileVo.setQuantitiesAnnex(StringUtils.join(othersGrossWeightAnnextList, "<br/>"));
            }
            if (paramValue != null && !commoditiesListAttachment.isEmpty()) {
                ctCctCpEFileVo.setNamesAnnex(StringUtils.join(commoditiesListAttachment, "<br/>"));
            }

            ctCctCpEFileVo.setNames(StringUtils.join(commoditiesList, "<br/>"));
        }

        ctCctCpEFileVo.setFileItemList(fileItemVos);
        if (productType != null && !ReportGeneratorUtils.CP_DEF_ANNEX.equals(jasperFileName) && !ReportGeneratorUtils.CP_ANNEX_AUTRES.equals(jasperFileName)) {
            if (Utils.getCacaProductsTypes().contains(productType) || Utils.COTONPRODUCTTYPE.equalsIgnoreCase(productType)) {
                jasperFileName = ReportGeneratorUtils.CP_COTCOCAF;
            } else if (Utils.getWoodProductsTypes().contains(productType) && !hasContainers) {
                jasperFileName = ReportGeneratorUtils.CP_BOIS_CONV;
            }
        }

        try {
            String qrCodeContent = getDocumentPageUrl(file);
            int qrCodeImageSize = 512;
            InputStream qrCodeStream = new ByteArrayInputStream(new QRCodeGenerator().generateQR(qrCodeContent, qrCodeImageSize));
            ctCctCpEFileVo.setQrCode(qrCodeStream);
            ctCctCpEFileVo.setQrCodeAnnex(qrCodeStream);
        } catch (Exception ex) {
            logger.error(file.getNumeroDossier(), ex);
        }

        return new JRBeanCollectionDataSource(Collections.singleton(ctCctCpEFileVo));
    }

    private String getDocumentPageUrl(File file) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        String documentPageUrl = "/pages/unsecure/document.xhtml";
        String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, documentPageUrl));
        url = Utils.getFinalDetailPageUrl(file, url, false, false);
        ApplicationPropretiesService applicationPropretiesService = ServiceUtility.getBean(ApplicationPropretiesService.class);
        String environment = applicationPropretiesService.getAppEnv();
        String urlPrefix;
        switch (environment) {
            case "standalone":
            case "production":
                urlPrefix = "https://siat.guichetunique.cm";
                break;
            case "test":
                urlPrefix = "https://testsiat.guichetunique.cm";
                break;
            default:
                urlPrefix = "https://localhost:40081";
                break;
        }
        url = urlPrefix.concat(url);
        return url;
    }

    @Override
    protected Map<String, Object> getJRParameters() {
        Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "phytosanitaire", "jpg"));
        return jRParameters;
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
