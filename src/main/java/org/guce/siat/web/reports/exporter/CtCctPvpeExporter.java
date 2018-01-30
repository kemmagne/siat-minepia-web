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
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.core.ct.model.AnalyseOrder;
import static org.guce.siat.web.reports.exporter.ReportCommand.IMAGES_PATH;
import org.guce.siat.web.reports.vo.CtCctPvpeVo;

/**
 *
 * @author yenke
 */
public class CtCctPvpeExporter extends AbstractReportInvoker {
	/** The file. */
	private AnalyseOrder analyseOrder;
	
	/**
	 * Instantiates a new ct cct cp e exporter.
	 *
	 * @param file
	 *           the file
	 */
	public CtCctPvpeExporter(final AnalyseOrder analyseOrder)
	{
		super("PVPE_MINADER", "PVPE_MINADER");
		this.analyseOrder = analyseOrder;
	}

	@Override
	protected JRBeanCollectionDataSource getReportDataSource() {
		final CtCctPvpeVo ctCctPvpeVo = new CtCctPvpeVo();
		if (analyseOrder != null){
			final File file = analyseOrder.getItemFlow().getFileItem().getFile();
			final FileItem fileItem = analyseOrder.getItemFlow().getFileItem();
			if (analyseOrder.getLaboratory() != null){
				ctCctPvpeVo.setLaboratory(analyseOrder.getLaboratory().getLabelFr());
			}
			if (analyseOrder.getSample() != null && analyseOrder.getSample().getSamplingDate() != null){
				ctCctPvpeVo.setSamplingDate(analyseOrder.getSample().getSamplingDate());
			}
			if (analyseOrder.getSample() != null && analyseOrder.getSample().getTakenQuantity() != null){
				ctCctPvpeVo.setQuantity(String.valueOf(analyseOrder.getSample().getTakenQuantity()));
			}
			if (analyseOrder.getQuantity() != null){
				ctCctPvpeVo.setSampleQuantity(String.valueOf(analyseOrder.getQuantity()));
			}
			if (analyseOrder.getSample() != null && analyseOrder.getSample().getTransportMeans() != null){
				ctCctPvpeVo.setTransportMeans(analyseOrder.getSample().getTransportMeans());
			}
			if (analyseOrder.getSample() != null && analyseOrder.getSample().getStoragePlace() != null){
				ctCctPvpeVo.setStoragePlace(analyseOrder.getSample().getStoragePlace());
			}
			if (analyseOrder.getSample() != null && analyseOrder.getSample().getReconditionningMode() != null){
				ctCctPvpeVo.setReconditionningMode(analyseOrder.getSample().getReconditionningMode());
			}
			if (analyseOrder.getSample() != null && analyseOrder.getSample().getReconditionningUnity() != null) {
				ctCctPvpeVo.setSampleReconditionnningUnity(analyseOrder.getSample().getReconditionningUnity());
			}
			if (file != null){
				if (file.getClient() != null)
				{
					ctCctPvpeVo.setOwner(file.getClient().getCompanyName());
				}
				final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

				if (CollectionUtils.isNotEmpty(fileFieldValueList))
				{
					for (final FileFieldValue fileFieldValue : fileFieldValueList)
					{
						switch (fileFieldValue.getFileField().getCode())
						{
							case "INFORMATIONS_GENERALES_DESTINATAIRE_RAISONSOCIALE":
								ctCctPvpeVo.setConsigneeSample(fileFieldValue.getValue());
								break;
							case "INFORMATIONS_GENERALES_TRANSPORT_NUM_CONNAISSEMENT_LTA":
								ctCctPvpeVo.setBillOfLoading(fileFieldValue.getValue());
								break;
							default:
								break;
						}
					}
				}
			}
			if (fileItem != null){
				if (fileItem.getNsh() != null){
					ctCctPvpeVo.setProductNature(fileItem.getNsh().getGoodsItemDesc());
				}
				final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
				if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "PAYS_ORIGINE_NOM_PAYS":
									ctCctPvpeVo.setOrigin(fileItemFieldValue.getValue());
									break;
//								case "NATURE":
//									ctCctPvpeVo.setProductNature(fileItemFieldValue.getValue());
//									break;
								default:
									break;
							}
						}
					}
			}
			
		}
		return new JRBeanCollectionDataSource(Collections.singleton(ctCctPvpeVo));
	}

	public AnalyseOrder getAnalyseOrder() {
		return analyseOrder;
	}

	public void setAnalyseOrder(AnalyseOrder analyseOrder) {
		this.analyseOrder = analyseOrder;
	}
	
	@Override
	protected Map<String, Object> getJRParameters()
	{
		final Map<String, Object> jRParameters = super.getJRParameters();
		jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "minader", "jpg"));
		return jRParameters;
	}
}
