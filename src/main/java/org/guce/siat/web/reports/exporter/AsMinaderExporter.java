package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.web.reports.vo.AsMinaderFileItemVo;
import org.guce.siat.web.reports.vo.AsMinaderFileVo;


/**
 * The Class AsMinaderExporter.
 */
public class AsMinaderExporter extends AbstractReportInvoker
{

	/** The file. */
	private final File file;

	/**
	 * Instantiates a new as minader exporter.
	 *
	 * @param file
	 *           the file
	 */
	public AsMinaderExporter(final File file)
	{
		super("AS_MINADER", "AS_MINADER");
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
		final AsMinaderFileVo asMinaderFileVo = new AsMinaderFileVo();



		asMinaderFileVo.setDecisionDate(Calendar.getInstance().getTime());
		asMinaderFileVo.setValidityDate(Calendar.getInstance().getTime());
		asMinaderFileVo.setArrivalDate(Calendar.getInstance().getTime());


		if (file != null)
		{
			asMinaderFileVo.setImporter(file.getClient().getCompanyName());
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();

			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{

					switch (fileFieldValue.getFileField().getCode())
					{
						case"NUMERO_AS_MINADER":
							asMinaderFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;

						case "PROMOTEUR_RAISONSOCIALE":
							asMinaderFileVo.setSupplierName(fileFieldValue.getValue());
							break;

						case "PROMOTEUR_ADRESSE_ADRESSE1":
							asMinaderFileVo.setSupplierAddress("Adresse1 / Address1:" + fileFieldValue.getValue());
							break;

						case "PROMOTEUR_ADRESSE_ADRESSE2":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								final String supplierAddress1 = asMinaderFileVo.getSupplierAddress();
								asMinaderFileVo.setSupplierAddress(supplierAddress1 + "/" + "Adresse 2 / Address2:"
										+ fileFieldValue.getValue());
							}
							break;

						case "FABRICANT_MATIERE_ACTIVE_RAISONSOCIALE":
							asMinaderFileVo.setManufacturerName(fileFieldValue.getValue());
							break;

						case "FABRICANT_MATIERE_ACTIVE_ADRESSE_ADRESSE1":
							asMinaderFileVo.setManufacturerAddress("Adresse1 / Address1:" + fileFieldValue.getValue());
							break;

						case "FABRICANT_MATIERE_ACTIVE_ADRESSE_ADRESSE2":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								final String manufacturerAddress1 = asMinaderFileVo.getManufacturerAddress();
								asMinaderFileVo.setManufacturerAddress(manufacturerAddress1 + "/" + "Adresse 2 / Address2:"
										+ fileFieldValue.getValue());
							}
							break;

						case "INFORMATIONS_GENERALES_POINT_ENTREE_LIBELLE":
							asMinaderFileVo.setEntryPoint(fileFieldValue.getValue());
							break;

						case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
							asMinaderFileVo.setTransportMode(fileFieldValue.getValue());
							break;

						case "INFORMATIONS_GENERALES_DESTINATAIRE_ADRESSE_PAYSADDRESS_NOMPAYS":
							asMinaderFileVo.setDestinationCountry(fileFieldValue.getValue());
							break;

						default:
							break;
					}
				}
			}


			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<AsMinaderFileItemVo> fileItemVos = new ArrayList<AsMinaderFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final AsMinaderFileItemVo fileItemVo = new AsMinaderFileItemVo();

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{

							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "NOM_COMMERCIAL":
									fileItemVo.setTradeName(fileItemFieldValue.getValue());
									break;
								case "MATIERE_ACTIVE":
									fileItemVo.setActiveSubstanceContent(fileItemFieldValue.getValue());
									break;
								case "TYPE_FORMULATION":
									fileItemVo.setFormulationType(fileItemFieldValue.getValue());
									break;
								case "USAGE":
									fileItemVo.setUse(fileItemFieldValue.getValue());
									break;
								case "QUANTITE_IMPORTEE":
									fileItemVo.setQuantity(Double.parseDouble(fileItemFieldValue.getValue()));
									break;
								case "PAYS_ORIGINE_NOM_PAYS":
									fileItemVo.setOriginCountry(fileItemFieldValue.getValue());
									break;
								default:
									break;
							}
						}
					}

					fileItemVos.add(fileItemVo);
				}
			}

			asMinaderFileVo.setFileItemList(fileItemVos);

		}
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(asMinaderFileVo));

		return dataSource;
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
