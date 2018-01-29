/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guce.siat.web.reports.exporter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.web.reports.vo.CtCctSeizureVo;

/**
 *
 * @author yenke
 */
public class CtCctSeizureExporter extends AbstractReportInvoker{
	private File file;

	public CtCctSeizureExporter(String jasperFileName, String pdfFileName, File file) {
		super(jasperFileName, pdfFileName);
		this.file = file;
	}


	@Override
	protected JRBeanCollectionDataSource getReportDataSource() {
		CtCctSeizureVo seizureVo = new CtCctSeizureVo();
		if (file != null){
			if (file.getClient() != null){
				seizureVo.setCustomerName(file.getClient().getCompanyName());
			}
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case "INFORMATIONS_GENERALES_TRANSPORT_NUM_CONNAISSEMENT_LTA":
							seizureVo.setBillOfLoading(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TRANSPORT_MOYEN_TRANSPORT_LIBELLE":
							seizureVo.setMeansOfTransport(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}
			if (file.getFileItemsList() != null && !file.getFileItemsList().isEmpty()){
				final List<FileItemFieldValue> fileItemFieldValueList = file.getFileItemsList().get(0).getFileItemFieldValueList();
				if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "NATURE":
									seizureVo.setLotName(fileItemFieldValue.getValue());
									break;
								case "ADRESSE_STOCKAGE_ADRESSE1":
									seizureVo.setStoragePlace(fileItemFieldValue.getValue());
									break;
								case "PAYS_ORIGINE_NOM_PAYS":
									seizureVo.setOrigin(fileItemFieldValue.getValue());
									break;
								default:
									break;
							}
						}
					}
			}
		}
		return new JRBeanCollectionDataSource(Collections.singleton(seizureVo)); 
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	@Override
	protected Map<String, Object> getJRParameters()
	{
		final Map<String, Object> jRParameters = super.getJRParameters();
		jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "minader", "jpg"));
		return jRParameters;
	}
}
