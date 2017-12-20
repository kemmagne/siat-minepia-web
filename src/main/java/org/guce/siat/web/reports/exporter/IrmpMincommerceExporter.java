/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guce.siat.web.reports.exporter;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.web.reports.vo.IrmpMincommerceVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yenke
 */
public class IrmpMincommerceExporter extends AbstractReportInvoker{
    
    private static final Logger LOG = LoggerFactory.getLogger(VtpMinsanteExporter.class);
    private static final String PREFIX = "/SG/DPML/SDM du ";
    private final File file;

    public IrmpMincommerceExporter(final File file){
        super("IRMP_MINCOMMERCE", "IRMP_MINCOMMERCE");
        this.file = file;
    }

    @Override
    protected JRBeanCollectionDataSource getReportDataSource() {
        final IrmpMincommerceVo irmpMincommerceVo = new IrmpMincommerceVo();
        Date dateSignature = null;
        if (file != null) {
            irmpMincommerceVo.setDateSignature(file.getSignatureDate());
            List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
            if (CollectionUtils.isNotEmpty(fileFieldValueList)){
                for (FileFieldValue fileFieldValue : fileFieldValueList){
                    switch (fileFieldValue.getFileField().getCode()){
                        case "NUMERO_ARRETE_APPROBATION":
                            irmpMincommerceVo.setNumeroArreteApprobation(fileFieldValue.getValue());
                            break;
                        case "NUMERO_AMM_MINCOMMERCE":
                            irmpMincommerceVo.setDecisionNumber(fileFieldValue.getValue());
                            break;
                        default:
                            break;
                            
                    }
                }
            }
            irmpMincommerceVo.setNumeroAttestation(irmpMincommerceVo.getDecisionNumber()
					+ PREFIX
					+ new SimpleDateFormat("dd/MM/yyyy").format(dateSignature == null ? java.util.Calendar.getInstance().getTime()
							: dateSignature));
        }
        return new JRBeanCollectionDataSource(Collections.singleton(irmpMincommerceVo));
    }
}
