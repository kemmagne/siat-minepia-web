package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.web.reports.vo.CtCctCsqFileItemVo;
import org.guce.siat.web.reports.vo.CtCctCsqFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class CtCctCsqExporter.
 */
public class CtCctCsqExporter extends AbstractReportInvoker
{

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(CtCctCsqExporter.class);

	/** The file. */
	private final File file;


	/**
	 * Instantiates a new ct cct csq exporter.
	 *
	 * @param file
	 *           the file
	 */
	public CtCctCsqExporter(final File file)
	{
		super("CT_CCT_CSQ", "CT_CCT_CSQ");
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

		final CtCctCsqFileVo ctCctCsqFileVo = new CtCctCsqFileVo();

		if ((file != null))
		{
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case "INFORMATIONS_GENERALES_DESTINATAIRE_RAISONSOCIALE":
							ctCctCsqFileVo.setRecipientName(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_PAYS_PROVENANCE_NOM_PAYS":
							ctCctCsqFileVo.setCountryOfProvenance(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_TRANSPORT_NUM_CONNAISSEMENT_LTA":
							ctCctCsqFileVo.setBillOfLadingNumber(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_ADRESSE1":
							ctCctCsqFileVo.setRecipientAddress1(fileFieldValue.getValue());
							break;

						case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_ADRESSE2":
							ctCctCsqFileVo.setRecipientAddress2(fileFieldValue.getValue());
							break;

						case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS":
							ctCctCsqFileVo.setRecipientCountry(fileFieldValue.getValue());
							break;

						case "SIGNATAIRE_DATE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								try
								{
									ctCctCsqFileVo.setDecisionDate(new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue()));
								}
								catch (final ParseException e)
								{
									LOG.error(Objects.toString(e), e);
								}
							}
							break;
						case "SIGNATAIRE_LIEU":
							ctCctCsqFileVo.setDecisionPlace(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_SIGNATAIRE_NOM":
							ctCctCsqFileVo.setSignatoryName(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}

			ctCctCsqFileVo.setDecisionNumber("/MINEP/SG/BNO");


			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<CtCctCsqFileItemVo> fileItemVos = new ArrayList<CtCctCsqFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final CtCctCsqFileItemVo fileItemVo = new CtCctCsqFileItemVo();

					fileItemVo.setQuantity(Double.valueOf(fileItem.getQuantity()));

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "NATURE":
									fileItemVo.setNature(fileItemFieldValue.getValue());
									break;
								case "NUMERO_CONTENEUR":
									fileItemVo.setContainerNumber(fileItemFieldValue.getValue());
									break;
								default:
									break;
							}
						}
					}

					fileItemVos.add(fileItemVo);
				}
			}

			ctCctCsqFileVo.setFileItemList(fileItemVos);
		}

		return new JRBeanCollectionDataSource(Collections.singleton(ctCctCsqFileVo));
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
