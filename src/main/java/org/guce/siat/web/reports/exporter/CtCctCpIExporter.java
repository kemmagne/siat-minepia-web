package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.web.reports.vo.CtCctCpIFileItemVo;
import org.guce.siat.web.reports.vo.CtCctCpIFileVo;

/**
 * The Class CtCctCpIExporter.
 */
public class CtCctCpIExporter extends AbstractReportInvoker {

    /**
     * The file.
     */
    private final File file;

    /**
     * Instantiates a new ct cct cp i exporter.
     *
     * @param file the file
     */
    public CtCctCpIExporter(final File file) {
        super("CT_CCT_CP_I", "CT_CCT_CP_I");
        this.file = file;
    }

    public CtCctCpIExporter(final File file, String jasperFileName, String pdfFileName) {
        super(jasperFileName, pdfFileName);
        this.file = file;
    }


    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        final CtCctCpIFileVo ctCctCpIFileVo = new CtCctCpIFileVo();

        if ((file != null)) {
            ctCctCpIFileVo.setDecisionPlace(file.getBureau().getLabelFr());
            ctCctCpIFileVo.setPhytosanitaryCertificateNumber(file.getReferenceGuce());
            ctCctCpIFileVo.setDecisionDate(Calendar.getInstance().getTime());
            ctCctCpIFileVo.setSignatoryName(file.getAssignedUser().getFirstName());
            if (file.getClient() != null) {
                ctCctCpIFileVo.setExporterName(file.getClient().getCompanyName());
                ctCctCpIFileVo.setExporterAddress1(file.getClient().getFirstAddress());
                ctCctCpIFileVo.setExporterCountry(file.getClient().getCountry() != null ? file.getClient().getCountry()
                        .getCountryIdAlpha2() : null);
            }
            ctCctCpIFileVo.setQuantity(file.getFileItemsList().get(0).getQuantity());
            final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

            if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
                for (final FileFieldValue fileFieldValue : fileFieldValueList) {
                    switch (fileFieldValue.getFileField().getCode()) {
                        case "NUMERO_CT_CCT_CP_I":
                            ctCctCpIFileVo.setDecisionNumber(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_DESTINATAIRE_RAISONSOCIALE":
                            ctCctCpIFileVo.setConsigneeName(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_ADRESSE1":
                            ctCctCpIFileVo.setConsigneeAddress1(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_ADRESSE2":
                            ctCctCpIFileVo.setConsigneeAddress2(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS":
                            ctCctCpIFileVo.setConsigneeCountry(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_TRANSPORT_MOYEN_TRANSPORT_LIBELLE":
                            ctCctCpIFileVo.setMeansOfTransport(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_POINT_ENTREE_LIBELLE":
                            ctCctCpIFileVo.setPointOfEntry(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_SIGNATAIRE_NOM":
                            ctCctCpIFileVo.setSignatoryName(fileFieldValue.getValue());
                            break;
                        case "MODE_EMBALLAGE":
                            ctCctCpIFileVo.setPackagingType(fileFieldValue.getValue());
                        case "NBR_LOTS_COLIS":
                            ctCctCpIFileVo.setPackagedOrRepackaged(fileFieldValue.getValue());
                            break;
                        default:
                            break;
                    }
                }
            }

            final List<FileItem> fileItemList = file.getFileItemsList();

            final List<CtCctCpIFileItemVo> fileItemVos = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(fileItemList)) {
                for (final FileItem fileItem : fileItemList) {
                    final CtCctCpIFileItemVo fileItemVo = new CtCctCpIFileItemVo();

                    fileItemVo.setDesc(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);

                    final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

                    if (CollectionUtils.isNotEmpty(fileItemFieldValueList)) {
                        for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {
                            switch (fileItemFieldValue.getFileItemField().getCode()) {
                                case "NBR_LOTS_COLIS":
                                    fileItemVo.setNumberOfPackages(fileItemFieldValue.getValue());
                                    break;
                                case "NATURE":
                                    fileItemVo.setNature(fileItemFieldValue.getValue());
                                    break;
                                case "NOM_BOTANIQUE":
                                    fileItemVo.setBotanicalName(fileItemFieldValue.getValue());
                                    break;
                                case "QUANTITE_TOTALE":
                                    fileItemVo.setDeclaredQuantity(fileItemFieldValue.getValue());
                                    ctCctCpIFileVo.setQuantity(fileItemFieldValue.getValue());
                                    break;
                                case "PAYS_ORIGINE_NOM_PAYS":
                                    ctCctCpIFileVo.setOrigin(fileItemFieldValue.getValue());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    fileItemVos.add(fileItemVo);
                }
            }

            ctCctCpIFileVo.setFileItemList(fileItemVos);
        }

        return new JRBeanCollectionDataSource(Collections.singleton(ctCctCpIFileVo));
    }

    @Override
    protected Map<String, Object> getJRParameters() {
        final Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "minader", "jpg"));
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
