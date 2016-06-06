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
import org.guce.siat.web.reports.vo.CtCctCsFileItemVo;
import org.guce.siat.web.reports.vo.CtCctCsFileVo;



/**
 * The Class CtCctCsExporter.
 */
public class CtCctCsExporter extends AbstractReportInvoker
{


	/** The file. */
	private final File file;



	/**
	 * Instantiates a new ct cct cs exporter.
	 *
	 * @param file
	 *           the file
	 */
	public CtCctCsExporter(final File file)
	{
		super("CT_CCT_CS", "CT_CCT_CS");
		this.file = file;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getReportDataSource()
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource()
	{

		final CtCctCsFileVo ctCctCsFileVo = new CtCctCsFileVo();

		if ((file != null))
		{
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{

						case "NUMERO_CT_CCT_CS":
							ctCctCsFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_DESTINATAIRE_RAISONSOCIALE":
							ctCctCsFileVo.setConsigneeName(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_ADRESSE1":
							ctCctCsFileVo.setConsigneeAddress1(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_ADRESSE2":
							ctCctCsFileVo.setConsigneeAddress2(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS":
							ctCctCsFileVo.setConsigneeCountry(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TRANSPORT_MOYEN_TRANSPORT_LIBELLE":
							ctCctCsFileVo.setMeansOfTransport(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_POINT_ENTREE_LIBELLE":
							ctCctCsFileVo.setPointOfEntry(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_SIGNATAIRE_NOM":
							ctCctCsFileVo.setSignatoryName(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}


			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<CtCctCsFileItemVo> fileItemVos = new ArrayList<CtCctCsFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final CtCctCsFileItemVo fileItemVo = new CtCctCsFileItemVo();

					fileItemVo.setDesc(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "NBR_LOTS_COLIS":
									fileItemVo.setNumberOfPackages(fileItemFieldValue.getValue());
									break;
								case "NATURE":
									fileItemVo.setNature(fileItemFieldValue.getValue());
									break;
								case "QUANTITE_TOTALE":
									fileItemVo.setDeclaredQuantity(fileItemFieldValue.getValue());
									break;
								default:
									break;
							}
						}
					}

					fileItemVos.add(fileItemVo);
				}
			}

			ctCctCsFileVo.setFileItemList(fileItemVos);
		}

		return new JRBeanCollectionDataSource(Collections.singleton(ctCctCsFileVo));
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile()
	{
		return file;
	}
}
