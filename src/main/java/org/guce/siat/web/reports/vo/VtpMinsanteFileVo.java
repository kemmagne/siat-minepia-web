package org.guce.siat.web.reports.vo;

import java.util.List;

/**
 * The Class VtpMinsanteFileVo.
 */
public class VtpMinsanteFileVo extends AbstractFileVo<VtpMinsanteFileItemVo> {

    private String supplierName;
	private String supplierAddress;
	private String supplierPobox;
	private String supplierCity;
	private String supplierCountry;
	private String supplierMail;
	private String supplierPhone;
	private String supplierFax;
	
	private String clientName;
	private String clientAddress;
	private String clientPobox;
	private String clientCity;
	private String clientCountry;
	private String clientTaxpayerNumber;
	private String clientMail;
	private String clientPhone;
	private String clientInscriptionCode;
	private String clientInscriptionDate;
	private String clientInscriptionIssueDate;

    private String invoiceNumber;

    private String invoiceDate;

    private String invoiceAmount;
	
	private String customPlace;

    private String transportWay;
	
	private String currency;
	
	private String fobCurrencyValue;
	
	private String fobCfaValue;
	
	private String totalAmount;
	
	private String attestation;
	
	private String originCountry;
	
	private String destinationCountry;

	public String getSupplierFax() {
		return supplierFax;
	}

	public void setSupplierFax(String supplierFax) {
		this.supplierFax = supplierFax;
	}
	
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}

	public String getSupplierMail() {
		return supplierMail;
	}

	public void setSupplierMail(String supplierMail) {
		this.supplierMail = supplierMail;
	}

	public String getSupplierPhone() {
		return supplierPhone;
	}

	public void setSupplierPhone(String supplierPhone) {
		this.supplierPhone = supplierPhone;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getSupplierPobox() {
		return supplierPobox;
	}

	public void setSupplierPobox(String supplierPobox) {
		this.supplierPobox = supplierPobox;
	}

	public String getSupplierCity() {
		return supplierCity;
	}

	public void setSupplierCity(String supplierCity) {
		this.supplierCity = supplierCity;
	}

	public String getSupplierCountry() {
		return supplierCountry;
	}

	public void setSupplierCountry(String supplierCountry) {
		this.supplierCountry = supplierCountry;
	}

	public String getClientPobox() {
		return clientPobox;
	}

	public void setClientPobox(String clientPobox) {
		this.clientPobox = clientPobox;
	}

	public String getClientCity() {
		return clientCity;
	}

	public void setClientCity(String clientCity) {
		this.clientCity = clientCity;
	}

	public String getClientCountry() {
		return clientCountry;
	}

	public void setClientCountry(String clientCountry) {
		this.clientCountry = clientCountry;
	}

	public String getClientTaxpayerNumber() {
		return clientTaxpayerNumber;
	}

	public void setClientTaxpayerNumber(String clientTaxpayerNumber) {
		this.clientTaxpayerNumber = clientTaxpayerNumber;
	}

	public String getClientMail() {
		return clientMail;
	}

	public void setClientMail(String clientMail) {
		this.clientMail = clientMail;
	}

	public String getClientPhone() {
		return clientPhone;
	}

	public void setClientPhone(String clientPhone) {
		this.clientPhone = clientPhone;
	}

	public String getClientInscriptionCode() {
		return clientInscriptionCode;
	}

	public void setClientInscriptionCode(String clientInscriptionCode) {
		this.clientInscriptionCode = clientInscriptionCode;
	}

	public String getClientInscriptionDate() {
		return clientInscriptionDate;
	}

	public void setClientInscriptionDate(String clientInscriptionDate) {
		this.clientInscriptionDate = clientInscriptionDate;
	}

	public String getClientInscriptionIssueDate() {
		return clientInscriptionIssueDate;
	}

	public void setClientInscriptionIssueDate(String clientInscriptionIssueDate) {
		this.clientInscriptionIssueDate = clientInscriptionIssueDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getCustomPlace() {
		return customPlace;
	}

	public void setCustomPlace(String customPlace) {
		this.customPlace = customPlace;
	}

	public String getTransportWay() {
		return transportWay;
	}

	public void setTransportWay(String transportWay) {
		this.transportWay = transportWay;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getFobCurrencyValue() {
		return fobCurrencyValue;
	}

	public void setFobCurrencyValue(String fobCurrencyValue) {
		this.fobCurrencyValue = fobCurrencyValue;
	}

	public String getFobCfaValue() {
		return fobCfaValue;
	}

	public void setFobCfaValue(String fobCfaValue) {
		this.fobCfaValue = fobCfaValue;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAttestation() {
		return attestation;
	}

	public void setAttestation(String attestation) {
		this.attestation = attestation;
	}
	
	public static class VtpMinsanteFileVoRecord{
		
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

}
