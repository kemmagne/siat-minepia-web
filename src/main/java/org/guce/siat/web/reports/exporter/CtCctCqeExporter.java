package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.web.reports.vo.CtCctCqeFileItemVo;
import org.guce.siat.web.reports.vo.CtCctCqeFileVo;

/**
 * The Class CtCctCsExporter.
 */
public class CtCctCqeExporter extends AbstractReportInvoker {

    /**
     * The file.
     */
    private final File file;

    /**
     * Instantiates a new ct cct cs exporter.
     *
     * @param file the file
     */
    public CtCctCqeExporter(final File file) {
        super("CT_CCT_CQE", "CT_CCT_CQE");
        this.file = file;
    }


    /*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
     */
    @Override
    public JRBeanCollectionDataSource getReportDataSource() {

        final CtCctCqeFileVo fileVo = new CtCctCqeFileVo();

        if (null == file) {
            return null;
        }

//        fileVo.setCountryOfDestination(file.getCountryOfDestination().getCountryNameFr());
//        fileVo.setCountryOfOrigin(file.getCountryOfOrigin().getCountryNameFr());
        fileVo.setDecisionDate(null);
        fileVo.setDecisionPlace(null);
        fileVo.setEguceNumber(file.getNumeroDemande());
        fileVo.setEntryPhytoPolice(null);
        fileVo.setExporterAddress(file.getClient().getCompanyName());
        fileVo.setExporterAddress(file.getClient().getFirstAddress());
        fileVo.setForwarder(null);
        fileVo.setMeanOfTransport(null);
        fileVo.setOfficerName(null);
        fileVo.setOutPhytoPolice(null);
        fileVo.setPackager(null);
        fileVo.setPreservationTemperature(null);
        fileVo.setProductCategory(null);
        fileVo.setTotalQuantity(null);
        fileVo.setValidity(null);

        final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

        for (final FileFieldValue fileFieldValue : fileFieldValueList) {
            switch (fileFieldValue.getFileField().getCode()) {
                case "NUMERO_CT_CCT_CQE":
                    fileVo.setCertificateNumber(fileFieldValue.getValue());
                    break;
                case "INFORMATIONS_GENERALES_DESTINATAIRE_RAISONSOCIALE":
                    fileVo.setConsigneeName(fileFieldValue.getValue());
                    break;
                case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_ADRESSE1":
                    fileVo.setConsigneeAddress(fileFieldValue.getValue());
                    break;
            }
        }

        final List<FileItem> fileItemList = file.getFileItemsList();
        if (CollectionUtils.isEmpty(fileItemList)) {
            return null;
        }

        final List<CtCctCqeFileItemVo> fileItemVos = new ArrayList<>();
        CtCctCqeFileItemVo fileItemVo;
        List<FileItemFieldValue> fileItemFieldValueList;
        for (final FileItem fileItem : fileItemList) {
            fileItemVo = new CtCctCqeFileItemVo();

            fileItemFieldValueList = fileItem.getFileItemFieldValueList();
            if (CollectionUtils.isEmpty(fileItemFieldValueList)) {
                continue;
            }

            for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList) {
                switch (fileItemFieldValue.getFileItemField().getCode()) {
                    case "NATURE":
                        fileItemVo.setItemNature(fileItemFieldValue.getValue());
                        break;
                    case "QUANTITE_TOTALE":

                        break;
                }
            }

            fileItemVos.add(fileItemVo);
        }
        fileVo.setFileItemList(fileItemVos);

        return new JRBeanCollectionDataSource(Collections.singleton(fileVo));
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

