package org.guce.siat.web.reports.vo;

import java.io.ByteArrayInputStream;

/**
 * The Class VtdMinsanteFileVo.
 */
public class VtdMinsanteFileVo extends AbstractFileVo<VtdMinsanteFileItemVo> {

    /**
     * The country of origin.
     */
    private String countryOfOrigin;

    /**
     * The country of provenance.
     */
    private String countryOfProvenance;

    /**
     * The provider.
     */
    private String provider;

    /**
     * The invoice.
     */
    private String invoice;

    /**
     * The importer.
     */
    private String importer;

    /**
     * The address.
     */
    private String address;

    /**
     * The profession.
     */
    private String profession;

    /**
     * The code.
     */
    private String code;

    private String pharmacistName;

    private String pharmacy;

    private String pharmacyRoad;

    private String pharmacyPoBox;

    private String pharmacyTel;

    private String invoiceNumber;

    private String invoiceDate;

    private String invoiceAmount;

    private String loadingCustomsOffice;

    private String transportWay;
	
	private String transportMode;

    private String enterringCustomsOffice;

    private String expirationDate;

    private String supplierName;

    private String attestation;

    private String billOfLanding;
	
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
	
	private String supplierAddress;
	private String supplierPobox;
	private String supplierCity;
	private String supplierCountry;
	private String supplierMail;
	private String supplierPhone;
	private String supplierFax;
	
	private String currency;
	
	private ByteArrayInputStream qrCode;
	
	private String diNumber;
	private String vtpNumber;
	
	private String signatoryDate;

    /**
     * Gets the country of origin.
     *
     * @return the country of origin
     */
    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    /**
     * Sets the country of origin.
     *
     * @param countryOfOrigin the new country of origin
     */
    public void setCountryOfOrigin(final String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    /**
     * Gets the provider.
     *
     * @return the provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the provider.
     *
     * @param provider the new provider
     */
    public void setProvider(final String provider) {
        this.provider = provider;
    }

    /**
     * Gets the invoice.
     *
     * @return the invoice
     */
    public String getInvoice() {
        return invoice;
    }

    /**
     * Sets the invoice.
     *
     * @param invoice the new invoice
     */
    public void setInvoice(final String invoice) {
        this.invoice = invoice;
    }

    /**
     * Gets the country of provenance.
     *
     * @return the country of provenance
     */
    public String getCountryOfProvenance() {
        return countryOfProvenance;
    }

    /**
     * Sets the country of provenance.
     *
     * @param countryOfProvenance the new country of provenance
     */
    public void setCountryOfProvenance(final String countryOfProvenance) {
        this.countryOfProvenance = countryOfProvenance;
    }

    /**
     * Gets the importer.
     *
     * @return the importer
     */
    public String getImporter() {
        return importer;
    }

    /**
     * Sets the importer.
     *
     * @param importer the new importer
     */
    public void setImporter(final String importer) {
        this.importer = importer;
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address.
     *
     * @param address the new address
     */
    public void setAddress(final String address) {
        this.address = address;
    }

    /**
     * Gets the profession.
     *
     * @return the profession
     */
    public String getProfession() {
        return profession;
    }

    /**
     * Sets the profession.
     *
     * @param profession the new profession
     */
    public void setProfession(final String profession) {
        this.profession = profession;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code.
     *
     * @param code the new code
     */
    public void setCode(final String code) {
        this.code = code;
    }

    public String getPharmacistName() {
        return pharmacistName;
    }

    public void setPharmacistName(String pharmacistName) {
        this.pharmacistName = pharmacistName;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getPharmacyRoad() {
        return pharmacyRoad;
    }

    public void setPharmacyRoad(String pharmacyRoad) {
        this.pharmacyRoad = pharmacyRoad;
    }

    public String getPharmacyPoBox() {
        return pharmacyPoBox;
    }

    public void setPharmacyPoBox(String pharmacyPoBox) {
        this.pharmacyPoBox = pharmacyPoBox;
    }

    public String getPharmacyTel() {
        return pharmacyTel;
    }

    public void setPharmacyTel(String pharmacyTel) {
        this.pharmacyTel = pharmacyTel;
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

    public String getLoadingCustomsOffice() {
        return loadingCustomsOffice;
    }

    public void setLoadingCustomsOffice(String loadingCustomsOffice) {
        this.loadingCustomsOffice = loadingCustomsOffice;
    }

    public String getTransportWay() {
        return transportWay;
    }

    public void setTransportWay(String transportWay) {
        this.transportWay = transportWay;
    }

    public String getEnterringCustomsOffice() {
        return enterringCustomsOffice;
    }

    public void setEnterringCustomsOffice(String enterringCustomsOffice) {
        this.enterringCustomsOffice = enterringCustomsOffice;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAttestation() {
        return attestation;
    }

    public void setAttestation(String attestation) {
        this.attestation = attestation;
    }

    public String getBillOfLanding() {
        return billOfLanding;
    }

    public void setBillOfLanding(String billOfLanding) {
        this.billOfLanding = billOfLanding;
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

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
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

	public String getSupplierFax() {
		return supplierFax;
	}

	public void setSupplierFax(String supplierFax) {
		this.supplierFax = supplierFax;
	}

	public String getTransportMode() {
		return transportMode;
	}

	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public ByteArrayInputStream getQrCode() {
		return qrCode;
	}

	public void setQrCode(ByteArrayInputStream qrCode) {
		this.qrCode = qrCode;
	}

	public String getDiNumber() {
		return diNumber;
	}

	public void setDiNumber(String diNumber) {
		this.diNumber = diNumber;
	}

	public String getVtpNumber() {
		return vtpNumber;
	}

	public void setVtpNumber(String vtpNumber) {
		this.vtpNumber = vtpNumber;
	}

	public String getSignatoryDate() {
		return signatoryDate;
	}

	public void setSignatoryDate(String signatoryDate) {
		this.signatoryDate = signatoryDate;
	}

}
