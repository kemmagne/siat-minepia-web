package org.guce.siat.web.reports.exporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.web.reports.vo.VtMinepiaFileItemVo;
import org.guce.siat.web.reports.vo.VtMinepiaFileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class VtMinepiaExporter.
 */
public class VtMinepiaExporter extends AbstractReportInvoker
{

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(VtMinepiaExporter.class);

	/** The file. */
	private final File file;

	/**
	 * Instantiates a new vt minepia exporter.
	 *
	 * @param file
	 *           the file
	 */
	public VtMinepiaExporter(final File file)
	{
		super("VT_MINEPIA", "VT_MINEPIA");
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource()
	{

		final VtMinepiaFileVo vtMinepiaVo = new VtMinepiaFileVo();

		if ((file != null))
		{
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			vtMinepiaVo.setDecisionDate(file.getSignatureDate());
			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case "NUMERO_VT_MINEPIA":
							vtMinepiaVo.setDecisionNumber(fileFieldValue.getValue());
							break;
						case "FACTURE_NUMERO_FACTURE":
							vtMinepiaVo.setInvoice(fileFieldValue.getValue());
							break;
						case "PAYS_ORIGINE_LIBELLE":
							vtMinepiaVo.setCountryOfOrigin(fileFieldValue.getValue());
							break;
						case "PAYS_PROVENANCE_LIBELLE":
							vtMinepiaVo.setCountryOfProvenance(fileFieldValue.getValue());
							break;
						case "FOURNISSEUR_RAISONSOCIALE":
							vtMinepiaVo.setProvider(fileFieldValue.getValue());
							break;
						case "SIGNATAIRE_DATE":
							if (StringUtils.isNotBlank(fileFieldValue.getValue()))
							{
								try
								{
									vtMinepiaVo.setDecisionDate(new SimpleDateFormat("dd/MM/yyyy").parse(fileFieldValue.getValue()));
								}
								catch (final ParseException e)
								{
									LOG.error(Objects.toString(e));
								}
							}
							break;
						case "SIGNATAIRE_LIEU":
							vtMinepiaVo.setDecisionPlace(fileFieldValue.getValue());
							break;
						default:
							break;
					}
				}
			}

			if ((file.getClient() != null))
			{
				vtMinepiaVo.setImporter(file.getClient().getCompanyName());
				vtMinepiaVo.setAddress(file.getClient().getFullAddress());
				vtMinepiaVo.setProfession(file.getClient().getProfession());
			}

			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<VtMinepiaFileItemVo> fileItemVos = new ArrayList<VtMinepiaFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final VtMinepiaFileItemVo fileItemVo = new VtMinepiaFileItemVo();
					fileItemVo.setCode(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemCode() : null);
					fileItemVo.setDesc(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);
					fileItemVo.setQuantity(Double.valueOf(fileItem.getQuantity()));

					fileItemVos.add(fileItemVo);
				}
			}

			vtMinepiaVo.setFileItemList(fileItemVos);
		}

		return new JRBeanCollectionDataSource(Collections.singleton(vtMinepiaVo));
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
		jRParameters.put("MINEPIA_LOGO", getRealPath(IMAGES_PATH, "minepia", "jpg"));
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
