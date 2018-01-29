package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
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
     * Instantiates a new ct cct cp e exporter.
     *
     * @param file the file
     */
    public CtCctCpEExporter(final File file) {
        super("CT_CCT_CP_E", "CT_CCT_CP_E");
        this.file = file;
    }

    public CtCctCpEExporter(final File file, String jasperFileName) {
        super(jasperFileName, jasperFileName + ".pdf");
        this.file = file;
    }


    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        final CtCctCpEFileVo ctCctCpEFileVo = new CtCctCpEFileVo();

        if ((file != null)) {
            ctCctCpEFileVo.setDecisionPlace(file.getBureau().getLabelFr());
            ctCctCpEFileVo.setDecisionDate(file.getSignatureDate());
            ctCctCpEFileVo.setSignatoryName(file.getAssignedUser().getFirstName());
            if (file.getClient() != null) {
                ctCctCpEFileVo.setConsignorName(file.getClient().getCompanyName());
                ctCctCpEFileVo.setConsignorAddress1(file.getClient().getFirstAddress());
                ctCctCpEFileVo.setConsignorCountry(file.getClient().getCountry() != null ? file.getClient().getCountry()
                        .getCountryIdAlpha2() : null);
            }
            final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

            if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
                for (final FileFieldValue fileFieldValue : fileFieldValueList) {
                    switch (fileFieldValue.getFileField().getCode()) {
                        case "NUMERO_CT_CCT_CP_E":
                            ctCctCpEFileVo.setDecisionNumber(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_DESTINATAIRE_RAISONSOCIALE":
                            ctCctCpEFileVo.setConsigneeName(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_ADRESSE1":
                            ctCctCpEFileVo.setConsigneeAddress1(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_ADRESSE2":
                            ctCctCpEFileVo.setConsigneeAddress2(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS":
                            ctCctCpEFileVo.setConsigneeCountry(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_TRANSPORT_MOYEN_TRANSPORT_LIBELLE":
                            ctCctCpEFileVo.setMeansOfTransport(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_POINT_ENTREE_LIBELLE":
                            ctCctCpEFileVo.setPointOfEntry(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_SIGNATAIRE_NOM":
                            ctCctCpEFileVo.setSignatoryName(fileFieldValue.getValue());
                            break;
                        case "INFORMATIONS_GENERALES_CERTIFICAT_EXPERTISE_NUMERO":
                            ctCctCpEFileVo.setPhytosanitaryCertificateNumber(fileFieldValue.getValue());
                            break;
                        default:
                            break;
                    }
                }
            }

            final List<FileItem> fileItemList = file.getFileItemsList();

            final List<CtCctCpEFileItemVo> fileItemVos = new ArrayList<CtCctCpEFileItemVo>();

            if (CollectionUtils.isNotEmpty(fileItemList)) {
                for (final FileItem fileItem : fileItemList) {
                    final CtCctCpEFileItemVo fileItemVo = new CtCctCpEFileItemVo();

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
                                    break;
                                case "PAYS_ORIGINE_NOM_PAYS":
                                    ctCctCpEFileVo.setOrigin(fileItemFieldValue.getValue());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    fileItemVos.add(fileItemVo);
                }
            }

            ctCctCpEFileVo.setFileItemList(fileItemVos);
        }

        return new JRBeanCollectionDataSource(Collections.singleton(ctCctCpEFileVo));
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
