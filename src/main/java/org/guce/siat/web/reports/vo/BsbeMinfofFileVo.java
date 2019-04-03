package org.guce.siat.web.reports.vo;

import java.util.List;


/**
 * 
 * @author Ulrich ETEME
 */
public class BsbeMinfofFileVo extends AbstractFileVo<VtMinepdedFileItemVo>
{

	/** The registration number */
	private String registrationNumber;

	/** The country of provenance. */
	private String loadingPlace;

	/** The dischargePlace. */
	private String dischargePlace;
        
        /** The destinationCountry. */
	private String destinationCountry;

	/** The importer. */
	private String importer;
        
        private java.util.List<WoodSpecificationVo> woodSpecs;
        
        private String referenceEforce;

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
	 * Gets the e-FORCE reference.
	 *
	 * @return the e-FORCE reference
	 */
        public String getReferenceEforce() {
                return referenceEforce;
        }

        /**
	 * Sets the e-FORCE reference.
	 *
	 * @param referenceEforce 
	 *           the new e-FORCE reference
	 */
        public void setReferenceEforce(String referenceEforce) {
                this.referenceEforce = referenceEforce;
        }

        public String getRegistrationNumber() {
                return registrationNumber;
        }

        public void setRegistrationNumber(String registrationNumber) {
                this.registrationNumber = registrationNumber;
        }

        public String getLoadingPlace() {
                return loadingPlace;
        }

        public void setLoadingPlace(String loadingPlace) {
                this.loadingPlace = loadingPlace;
        }

        public String getDischargePlace() {
                return dischargePlace;
        }

        public void setDischargePlace(String dischargePlace) {
                this.dischargePlace = dischargePlace;
        }

        public String getDestinationCountry() {
                return destinationCountry;
        }

        public void setDestinationCountry(String destinationCountry) {
                this.destinationCountry = destinationCountry;
        }

        public List<WoodSpecificationVo> getWoodSpecs() {
                return woodSpecs;
        }

        public void setWoodSpecs(List<WoodSpecificationVo> woodSpecs) {
                this.woodSpecs = woodSpecs;
        }

}
