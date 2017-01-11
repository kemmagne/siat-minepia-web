/*
 *
 */
package org.guce.siat.web.reports.exporter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.web.reports.vo.AiMinsanteFileVo;


/**
 * The Class AiMinsanteExporter.
 */
public class AiMinsanteExporter extends AbstractReportInvoker
{

	/**
	 * The file.
	 */
	private final File file;

	/**
	 * Instantiates a new ai minsante exporter.
	 *
	 * @param file
	 *           the file
	 */
	public AiMinsanteExporter(final File file)
	{
		super("AI_MINSANTE", "AI_MINSANTE");
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
		final AiMinsanteFileVo aiMinsanteFileVo = new AiMinsanteFileVo();
		final Map map = new HashMap();
		map.put("DERMOCORTICOÏDES", "DERMOCORTICOIDS");
		map.put("STUPEFIANTS", "NARCOTICS");
		String manufacturerAddress1 = null;
		String manufacturerAddress2 = null;
		String manufacturerPays = null;
		String manufacturerVille = null;
		String supplierAddress1 = null;
		String supplierAddress2 = null;
		String supplierPays = null;
		String supplierVille = null;

		aiMinsanteFileVo.setValidityDate(file.getSignatureDate());
		aiMinsanteFileVo.setDecisionDate(file.getSignatureDate());
		aiMinsanteFileVo.setDecisionPlace(file.getBureau().getAddress());

		if ((file != null) && (file.getClient() != null))
		{
			aiMinsanteFileVo.setImporter(file.getClient().getCompanyName());
			aiMinsanteFileVo.setImporterName(file.getClient().getCompanyName());
			aiMinsanteFileVo.setImporterAddress(new StringBuilder().append(file.getClient().getPostalCode()).append(" , ")
					.append(file.getClient().getFirstAddress()).append(" , ").append(file.getClient().getCity()).append(" , ")
					.append(file.getClient().getCountry().getCountryName()).toString());

			if (file.getCountryOfOrigin() != null)
			{
				aiMinsanteFileVo.setOriginCountry(file.getCountryOfOrigin().getCountryName());
			}

			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case "NUMERO_AI_MINSANTE":
							aiMinsanteFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "TYPE_PRODUIT":
							aiMinsanteFileVo.setProductTypeFr(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__RAISON_SOCIALE":
							aiMinsanteFileVo.setPharmacistName(fileFieldValue.getValue());
							break;
						case "PHARMACIEN__NOM_STRUCTURE_PHARMACIEN":
							aiMinsanteFileVo.setPharmacyName(fileFieldValue.getValue());
							break;
						case "LABORATOIRE_FABRICANT__RAISON_SOCIALE":
							aiMinsanteFileVo.setManufacturerName(fileFieldValue.getValue());
							break;
						case "LABORATOIRE_FABRICANT__ADRESSE__ADRESSE1":
							manufacturerAddress1 = fileFieldValue.getValue();
							break;
						case "LABORATOIRE_FABRICANT__ADRESSE__ADRESSE2":
							manufacturerAddress2 = fileFieldValue.getValue();
							break;
						case "LABORATOIRE_FABRICANT__ADRESSE__PAYS_ADRESSE__NOM_PAYS":
							manufacturerPays = fileFieldValue.getValue();
							break;
						case "LABORATOIRE_FABRICANT__ADRESSE__VILLE":
							manufacturerVille = fileFieldValue.getValue();
							break;
						case "FOURNISSEUR_RAISONSOCIALE":
							aiMinsanteFileVo.setSupplierName(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_ADRESSE_ADRESSE1":
							supplierAddress1 = fileFieldValue.getValue();
							break;
						case "FOURNISSEUR_ADRESSE_ADRESSE2":
							supplierAddress2 = fileFieldValue.getValue();
							break;
						case "FOURNISSEUR_ADRESSE_PAYSADDRESS_NOMPAYS":
							supplierPays = fileFieldValue.getValue();
							break;
						case "FOURNISSEUR_ADRESSE_VILLE":
							supplierVille = fileFieldValue.getValue();
							break;
						case "INFORMATIONS_GENERALES_TRANSPORT_MODE_TRANSPORT_LIBELLE":
							aiMinsanteFileVo.setTransportMode(fileFieldValue.getValue());
							break;
						case "INFORMATIONS_GENERALES_LIEU_DECHARGEMENT_LIBELLE":
							aiMinsanteFileVo.setEnterringCustomsOffice(fileFieldValue.getValue());
							break;

						default:
							break;
					}
				}
			}
			aiMinsanteFileVo.setPharmacistFunction("Docteur / Doctor");
			aiMinsanteFileVo.setManufacturerAddress(new StringBuilder()
					.append(
							manufacturerAddress1 == null ? (manufacturerAddress2 == null ? "" : manufacturerAddress2 + " , ")
									: (manufacturerAddress1 + " , ")).append(manufacturerVille == null ? "" : manufacturerVille + " , ")
					.append(manufacturerPays == null ? "" : manufacturerPays).toString());

			aiMinsanteFileVo.setSupplierAddress(new StringBuilder()
					.append(
							supplierAddress1 == null ? (supplierAddress2 == null ? "" : supplierAddress2 + " , ")
									: (supplierAddress1 + " , ")).append(supplierVille == null ? "" : supplierVille + " , ")
					.append(supplierPays == null ? "" : supplierPays).toString());

			aiMinsanteFileVo.setProductTypeEn((String) map.get(aiMinsanteFileVo.getProductTypeFr()));
			final List<FileItem> fileItemList = file.getFileItemsList();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{

					aiMinsanteFileVo.setProductQuantity(fileItem.getQuantity());
					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();
					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "DCI":
									aiMinsanteFileVo.setProductINN(fileItemFieldValue.getValue());
									break;
								case "FORME":
									aiMinsanteFileVo.setProductForm(fileItemFieldValue.getValue());
									break;
								case "PRESENTATION":
									aiMinsanteFileVo.setProductPresentation(fileItemFieldValue.getValue());
									break;
								case "DOSAGE":
									aiMinsanteFileVo.setProductDosage(fileItemFieldValue.getValue());
									break;
								case "NOM_COMMERCIAL":
									aiMinsanteFileVo.setProductCommercialName(fileItemFieldValue.getValue());
									break;
								default:
									break;
							}
						}
					}
					break;
				}
			}
		}
		final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(aiMinsanteFileVo));

		return dataSource;
	}

	@Override
	protected Map<String, Object> getJRParameters()
	{

		final Map<String, Object> jRParameters = super.getJRParameters();
		jRParameters.put("MINSANTE_LOGO", getRealPath(IMAGES_PATH, "minsante", "jpg"));
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
