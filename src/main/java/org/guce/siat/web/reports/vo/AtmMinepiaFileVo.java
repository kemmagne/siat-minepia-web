package org.guce.siat.web.reports.vo;

import java.util.Date;



/**
 * The Class AtmMinsanteFileVo.
 */
public class AtmMinepiaFileVo extends AbstractFileVo<AtmMinepiaFileItemVo>
{
	/** The importer. */
	private String importer;

	/** The validity date. */
	private Date validityDate;
	/** The origin country. */
	private String originCountry;
        /**Add the Orignal Signature***/
        private String signature;
        /**Add the stamps***/
        private String stamp;
         /**Add the qrCode***/
        private String qrCode;
        /***Validaation date**/
        private String cebs;
        private String bp;
        private String town;
        private double quandityRequired;
        private double quanditeproposed;
        private String ampliation;
        
       
        
        //Good properties
  	/** The designation. */
	private String designation;
	/** The measurement unit. */
	private String measurementUnit;
        
        private String ammendement;
        
        private String draft;

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }
        
        

    public double getQuandityRequired() {
        return quandityRequired;
    }

    public void setQuandityRequired(double quandityRequired) {
        this.quandityRequired = quandityRequired;
    }

    public double getQuanditeproposed() {
        return quanditeproposed;
    }

    public void setQuanditeproposed(double quanditeproposed) {
        this.quanditeproposed = quanditeproposed;
    }

    public String getAmpliation() {
        return ampliation;
    }

    public void setAmpliation(String ampliation) {
        this.ampliation = ampliation;
    }

    public String getCebs() {
        return cebs;
    }

    public void setCebs(String cebs) {
        this.cebs = cebs;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
     
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }


	/**
	 * Gets the importer.
	 *
	 * @return the importer
	 */
	public String getImporter()
	{
		return importer;
	}

	/**
	 * Sets the importer.
	 *
	 * @param importer
	 *           the new importer
	 */
	public void setImporter(final String importer)
	{
		this.importer = importer;
	}

	/**
	 * Gets the validity date.
	 *
	 * @return the validity date
	 */
	public Date getValidityDate()
	{
		return validityDate;
	}

	/**
	 * Sets the validity date.
	 *
	 * @param validityDate
	 *           the new validity date
	 */
	public void setValidityDate(final Date validityDate)
	{
		this.validityDate = validityDate;
	}

	/**
	 * Gets the origin country.
	 *
	 * @return the origin country
	 */
	public String getOriginCountry()
	{
		return originCountry;
	}

	/**
	 * Sets the origin country.
	 *
	 * @param originCountry
	 *           the new origin country
	 */
	public void setOriginCountry(final String originCountry)
	{
		this.originCountry = originCountry;
	}

    public String getAmmendement() {
        return ammendement;
    }

    public void setAmmendement(String ammendement) {
        this.ammendement = ammendement;
    }

        
        

}
