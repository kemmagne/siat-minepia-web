package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.web.reports.vo.DiMinaderIeFileItemVo;
import org.guce.siat.web.reports.vo.DiMinaderIeFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class DiMinaderIeExporter.
 */
public class DiMinaderIeExporter extends AbstractReportInvoker
{

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(DiMinaderIeExporter.class);

	/** The file. */
	private final File file;

	/**
	 * Instantiates a new di minader ie exporter.
	 *
	 * @param file
	 *           the file
	 */
	public DiMinaderIeExporter(final File file)
	{
		super("DI_MINADER_IE", "DI_MINADER_IE");
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
		final DiMinaderIeFileVo diMinaderIeFileVo = new DiMinaderIeFileVo();
		final SimpleDateFormat formatter2 = new SimpleDateFormat(DateUtils.PATTERN_DDMMYYYY);
		final List<DiMinaderIeFileItemVo> fileItemVos = new ArrayList<DiMinaderIeFileItemVo>();

		if (file != null)
		{
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			diMinaderIeFileVo.setDecisionDate(new Date());
			diMinaderIeFileVo.setDecisionPlace("-");
			diMinaderIeFileVo.setSupplier("-");

			if (file.getCountryOfOrigin() != null)
			{

				diMinaderIeFileVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
			}

			if (file.getClient() != null)
			{
				diMinaderIeFileVo.setImporter(file.getClient().getCompanyName());
				diMinaderIeFileVo.setDeclarantName(file.getClient().getCompanyName());
				diMinaderIeFileVo.setDeclarantAddress(file.getClient().getFullAddress());
			}

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case"NUMERO_DI_MINADER_IE":
							diMinaderIeFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_AGREMENT_REFERENCE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								diMinaderIeFileVo.setReference(fileFieldValue.getValue());
							}
							break;

						case "NUMERO_AUTORISATION_SPECIALE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								diMinaderIeFileVo.setApprovalNumber(fileFieldValue.getValue());
							}
							break;

						case "INFORMATION_ARRIVEE_DESTINATION_PRODUIT":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								diMinaderIeFileVo.setDestinationCountry(fileFieldValue.getValue());
							}
							break;

						case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_CODE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								diMinaderIeFileVo.setTransportMode(fileFieldValue.getValue());
							}
							break;

						case "INFORMATION_ARRIVEE_DATE_ARRIVEE_PREVUE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								try
								{
									final Date convertedDate = formatter2.parse(fileFieldValue.getValue());
									diMinaderIeFileVo.setArrivalDate(convertedDate);
								}
								catch (final ParseException e)
								{
									LOG.error(Objects.toString(e));
								}
							}
							break;

						case "INFORMATION_ARRIVEE_POINT_ENTREE_UNLOCODE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								diMinaderIeFileVo.setEntryPoint(fileFieldValue.getValue());
							}
							break;

						case "DATE_VALIDITE_DECLARATION":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								try
								{
									final Date convertedDate = formatter2.parse(fileFieldValue.getValue());
									diMinaderIeFileVo.setValidityDate(convertedDate);
								}
								catch (final ParseException e)
								{
									LOG.error(Objects.toString(e));
								}
							}
							break;

						default:
							break;
					}
				}
			}
			final List<FileItem> fileItemList = file.getFileItemsList();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final DiMinaderIeFileItemVo fileItemVo = new DiMinaderIeFileItemVo();

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
					fileItemVo.setQuantity(Double.valueOf(fileItem.getQuantity()));
					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "NOM_COMMERCIAL":
									fileItemVo.setTradeName(fileItemFieldValue.getValue());
									break;

								default:
									break;
							}
						}
					}
					fileItemVos.add(fileItemVo);
				}
			}
			diMinaderIeFileVo.setFileItemList(fileItemVos);

		}

		return new JRBeanCollectionDataSource(Collections.singleton(diMinaderIeFileVo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.guce.siat.web.reports.exporter.AbstractReportInvoker#getJRParameters()
	 */
	@Override
	protected Map<String, Object> getJRParameters()
	{
		final Map<String, Object> jRParameters = super.getJRParameters();
		jRParameters.put("MINADER_LOGO", getRealPath(IMAGES_PATH, "minader", "jpg"));
		return jRParameters;
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
