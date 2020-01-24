package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRParameter;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.web.reports.vo.VtMinepdedFileItemVo;
import org.guce.siat.web.reports.vo.VtMinepdedFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class VtMinepdedExporter.
 */
public class VtMinepdedExporter extends AbstractReportInvoker {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(VtMinepdedExporter.class);

    /**
     * The file.
     */
    private final File file;

    /**
     * Instantiates a new vt minepded exporter.
     *
     * @param file the file
     */
    public VtMinepdedExporter(final File file) {
        super("VT_MINEPDED", "VT_MINEPDED");
        this.file = file;
    }

    /**
     * Dans le cadre de la procédure visa technique MINEPDED, il est possible de
     * générer deux types de rapport. D'où le paramètre jasperFileName
     *
     * @param jasperFileName
     * @param file
     */
    public VtMinepdedExporter(String jasperFileName, final File file) {
        super(jasperFileName, "VT_MINEPDED");
        this.file = file;
    }


    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        final VtMinepdedFileVo vtMinepdedVo = new VtMinepdedFileVo();

        if ((file != null)) {
            vtMinepdedVo.setFileCreationDate(file.getCreatedDate());
            vtMinepdedVo.setDecisionDate(file.getSignatureDate());
            String numeroFacture = null;
            vtMinepdedVo.setInvoice(numeroFacture);
            vtMinepdedVo.setCountryOfOrigin(null != file.getCountryOfOrigin() ? file.getCountryOfOrigin().getCountryName() : null);
            vtMinepdedVo.setCountryOfProvenance(null != file.getCountryOfProvenance() ? file.getCountryOfProvenance().getCountryName() : null);
            final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
            if (CollectionUtils.isNotEmpty(fileFieldValueList)) {
                for (final FileFieldValue fileFieldValue : fileFieldValueList) {
                    switch (fileFieldValue.getFileField().getCode()) {
                        case "NUMERO_VT_MINEPDED":
                            vtMinepdedVo.setDecisionNumber(fileFieldValue.getValue());
                            break;
                        case "FACTURE_NUMERO_FACTURE":
                            vtMinepdedVo.setInvoice(fileFieldValue.getValue());
                            break;
                        case "FOURNISSEUR_RAISONSOCIALE":
                            vtMinepdedVo.setProvider(fileFieldValue.getValue());
                            break;
                        case "SIGNATAIRE_LIEU":
                            vtMinepdedVo.setDecisionPlace(fileFieldValue.getValue());
                            break;
                        default:
                            break;
                    }
                }
            }

            if ((file.getClient() != null)) {
                vtMinepdedVo.setImporter(file.getClient().getCompanyName());
                vtMinepdedVo.setAddress(file.getClient().getFullAddress());
                vtMinepdedVo.setProfession(file.getClient().getProfession());
            }

            final List<FileItem> fileItemList = file.getFileItemsList();

            final List<VtMinepdedFileItemVo> fileItemVos = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(fileItemList)) {
                for (final FileItem fileItem : fileItemList) {
                    final VtMinepdedFileItemVo fileItemVo = new VtMinepdedFileItemVo();
                    fileItemVo.setCode(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemCode() : null);
                    fileItemVo.setDesc(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);
                    fileItemVo.setQuantity(fileItem.getQuantity() != null ? Double.valueOf(fileItem.getQuantity()) : 0);

                    fileItemVos.add(fileItemVo);
                }
            }

            vtMinepdedVo.setFileItemList(fileItemVos);
        }

        return new JRBeanCollectionDataSource(Collections.singleton(vtMinepdedVo));
    }


    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getJRParameters()
     */
    @Override
    protected Map<String, Object> getJRParameters() {

        final Map<String, Object> jRParameters = super.getJRParameters();
        jRParameters.put("MINEPDED_LOGO", getRealPath(IMAGES_PATH, "minepded", "jpg"));
        jRParameters.put(JRParameter.REPORT_LOCALE, new java.util.Locale("FR"));
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
