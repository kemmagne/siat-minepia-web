package org.guce.siat.web.reports.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.guce.siat.common.model.File;
import org.guce.siat.common.model.FileFieldValue;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.model.FileItemFieldValue;
import org.guce.siat.core.ct.model.PaymentData;
import org.guce.siat.web.reports.vo.InvFileItemVo;
import org.guce.siat.web.reports.vo.InvFileVo;




/**
 * The Class InvExporter.
 */
public class InvExporter extends AbstractReportInvoker
{
	/** The file. */
	private final File file;

	/** The payment data. */
	private final PaymentData paymentData;




	/**
	 * Instantiates a new inv exporter.
	 *
	 * @param file
	 *           the file
	 * @param paymentData
	 *           the payment data
	 */
	public InvExporter(final File file, final PaymentData paymentData)
	{
		super("INV", "INV");
		this.file = file;
		this.paymentData = paymentData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.guce.siat.web.reports.vo.JasperExporter#getReportDataSource(java.lang.Object[])
	 */
	@Override
	public JRBeanCollectionDataSource getReportDataSource()
	{

		final InvFileVo invFileVo = new InvFileVo();


		if ((paymentData != null))
		{
			//invFileVo.setPaymentMethod(paymentData.get);
			//invFileVo.setDiscount(fileItemFieldValue.getValue());
			//invFileVo.setVat(fileItemFieldValue.getValue());

		}


		if ((file != null))
		{
			final List<FileFieldValue> fileFieldValueList = file.getFileFieldValueList();
			invFileVo.setDecisionDate(file.getSignatureDate());
			if (CollectionUtils.isNotEmpty(fileFieldValueList))
			{
				for (final FileFieldValue fileFieldValue : fileFieldValueList)
				{
					switch (fileFieldValue.getFileField().getCode())
					{
						case "NUMERO_RCPT":
							invFileVo.setDecisionNumber(fileFieldValue.getValue());
							break;

						default:
							break;
					}
				}
			}

			final List<FileItem> fileItemList = file.getFileItemsList();

			final List<InvFileItemVo> fileItemVos = new ArrayList<InvFileItemVo>();

			if (CollectionUtils.isNotEmpty(fileItemList))
			{
				for (final FileItem fileItem : fileItemList)
				{
					final InvFileItemVo fileItemVo = new InvFileItemVo();
					fileItemVo.setCode(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemCode() : null);
					fileItemVo.setDesc(fileItem.getNsh() != null ? fileItem.getNsh().getGoodsItemDesc() : null);
					fileItemVo.setQuantity(Double.valueOf(fileItem.getQuantity()));

					final List<FileItemFieldValue> fileItemFieldValueList = fileItem.getFileItemFieldValueList();

					if (CollectionUtils.isNotEmpty(fileItemFieldValueList))
					{
						for (final FileItemFieldValue fileItemFieldValue : fileItemFieldValueList)
						{
							switch (fileItemFieldValue.getFileItemField().getCode())
							{
								case "PRIX_NUTITAIRE":
									fileItemVo.setUnitPrice(fileItemFieldValue.getValue() != null ? Double.valueOf(fileItemFieldValue
											.getValue()) : 0);
									break;
								default:
									break;
							}
						}
					}
					fileItemVos.add(fileItemVo);
				}
			}

			invFileVo.setFileItemList(fileItemVos);
		}

		return new JRBeanCollectionDataSource(Collections.singleton(invFileVo));
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
		jRParameters.put("CCIMA_LOGO", getRealPath(IMAGES_PATH, "ccima", "gif"));
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

	/**
	 * Gets the payment data.
	 *
	 * @return the payment data
	 */
	public PaymentData getPaymentData()
	{
		return paymentData;
	}

}
