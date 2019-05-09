package org.guce.siat.web.reports.vo;

/**
 *
 * @author tadzotsa
 */
public class CtCctTreatmentFileItemVo {

    private String code;
    private String number;
    private String nature;
    private String volume;
    private String weight;
	
	private String productLabel;
	private String quantityLabel;
	private String volumeWeightLabel;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
	
	public String getProductLabel() {
		return productLabel;
	}

	public void setProductLabel(String productLabel) {
		this.productLabel = productLabel;
	}

	public String getQuantityLabel() {
		return quantityLabel;
	}

	public void setQuantityLabel(String quantityLabel) {
		this.quantityLabel = quantityLabel;
	}

	public String getVolumeWeightLabel() {
		return volumeWeightLabel;
	}

	public void setVolumeWeightLabel(String volumeWeightLabel) {
		this.volumeWeightLabel = volumeWeightLabel;
	}

}
