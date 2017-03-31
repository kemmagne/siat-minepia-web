package org.guce.siat.web.reports.vo;

import java.util.Date;




/**
 * The Class InvFileVo.
 */
public class InvFileVo extends AbstractFileVo<InvFileItemVo>
{


	/** The invoice date. */
	private Date invoiceDate;

	/** The file number. */
	private String fileNumber;

	/** The invoice number. */
	private String invoiceNumber;

	/** The seller. */
	private String seller;

	/** The task. */
	private String task;

	/** The shipping method. */
	private String shippingMethod;

	/** The shipping conditions. */
	private String shippingConditions;

	/** The delivery date. */
	private Date deliveryDate;

	/** The payment method. */
	private String paymentMethod;

	/** The payment date. */
	private Date paymentDate;

	/** The discount. */
	private Double discount;

	/** The vat. */
	private Double vat;

	/**
	 * Gets the invoice date.
	 *
	 * @return the invoice date
	 */
	public Date getInvoiceDate()
	{
		return invoiceDate;
	}

	/**
	 * Sets the invoice date.
	 *
	 * @param invoiceDate
	 *           the new invoice date
	 */
	public void setInvoiceDate(final Date invoiceDate)
	{
		this.invoiceDate = invoiceDate;
	}

	/**
	 * Gets the file number.
	 *
	 * @return the file number
	 */
	public String getFileNumber()
	{
		return fileNumber;
	}

	/**
	 * Sets the file number.
	 *
	 * @param fileNumber
	 *           the new file number
	 */
	public void setFileNumber(final String fileNumber)
	{
		this.fileNumber = fileNumber;
	}

	/**
	 * Gets the invoice number.
	 *
	 * @return the invoice number
	 */
	public String getInvoiceNumber()
	{
		return invoiceNumber;
	}

	/**
	 * Sets the invoice number.
	 *
	 * @param invoiceNumber
	 *           the new invoice number
	 */
	public void setInvoiceNumber(final String invoiceNumber)
	{
		this.invoiceNumber = invoiceNumber;
	}

	/**
	 * Gets the seller.
	 *
	 * @return the seller
	 */
	public String getSeller()
	{
		return seller;
	}

	/**
	 * Sets the seller.
	 *
	 * @param seller
	 *           the new seller
	 */
	public void setSeller(final String seller)
	{
		this.seller = seller;
	}

	/**
	 * Gets the task.
	 *
	 * @return the task
	 */
	public String getTask()
	{
		return task;
	}

	/**
	 * Sets the task.
	 *
	 * @param task
	 *           the new task
	 */
	public void setTask(final String task)
	{
		this.task = task;
	}

	/**
	 * Gets the shipping method.
	 *
	 * @return the shipping method
	 */
	public String getShippingMethod()
	{
		return shippingMethod;
	}

	/**
	 * Sets the shipping method.
	 *
	 * @param shippingMethod
	 *           the new shipping method
	 */
	public void setShippingMethod(final String shippingMethod)
	{
		this.shippingMethod = shippingMethod;
	}

	/**
	 * Gets the shipping conditions.
	 *
	 * @return the shipping conditions
	 */
	public String getShippingConditions()
	{
		return shippingConditions;
	}

	/**
	 * Sets the shipping conditions.
	 *
	 * @param shippingConditions
	 *           the new shipping conditions
	 */
	public void setShippingConditions(final String shippingConditions)
	{
		this.shippingConditions = shippingConditions;
	}

	/**
	 * Gets the delivery date.
	 *
	 * @return the delivery date
	 */
	public Date getDeliveryDate()
	{
		return deliveryDate;
	}

	/**
	 * Sets the delivery date.
	 *
	 * @param deliveryDate
	 *           the new delivery date
	 */
	public void setDeliveryDate(final Date deliveryDate)
	{
		this.deliveryDate = deliveryDate;
	}

	/**
	 * Gets the payment method.
	 *
	 * @return the payment method
	 */
	public String getPaymentMethod()
	{
		return paymentMethod;
	}

	/**
	 * Sets the payment method.
	 *
	 * @param paymentMethod
	 *           the new payment method
	 */
	public void setPaymentMethod(final String paymentMethod)
	{
		this.paymentMethod = paymentMethod;
	}

	/**
	 * Gets the payment date.
	 *
	 * @return the payment date
	 */
	public Date getPaymentDate()
	{
		return paymentDate;
	}

	/**
	 * Sets the payment date.
	 *
	 * @param paymentDate
	 *           the new payment date
	 */
	public void setPaymentDate(final Date paymentDate)
	{
		this.paymentDate = paymentDate;
	}

	/**
	 * Gets the discount.
	 *
	 * @return the discount
	 */
	public Double getDiscount()
	{
		return discount;
	}

	/**
	 * Sets the discount.
	 *
	 * @param discount
	 *           the new discount
	 */
	public void setDiscount(final Double discount)
	{
		this.discount = discount;
	}

	/**
	 * Gets the vat.
	 *
	 * @return the vat
	 */
	public Double getVat()
	{
		return vat;
	}

	/**
	 * Sets the vat.
	 *
	 * @param vat
	 *           the new vat
	 */
	public void setVat(final Double vat)
	{
		this.vat = vat;
	}
}
