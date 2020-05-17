package org.guce.siat.web.ct.data;

import java.io.Serializable;
import java.util.Objects;
import org.guce.siat.core.ct.util.enums.CctExportProductType;

/**
 *
 * @author tadzotsa
 */
public class ProductTypeData implements Serializable {

	private static final long serialVersionUID = 8758595446750305292L;

	private CctExportProductType productType;
	private Boolean checked;

	public ProductTypeData() {
	}

	public ProductTypeData(CctExportProductType productType, Boolean selected) {
		this.productType = productType;
		this.checked = selected;
	}

	public CctExportProductType getProductType() {
		return productType;
	}

	public void setProductType(CctExportProductType productType) {
		this.productType = productType;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.productType);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ProductTypeData other = (ProductTypeData) obj;
		return this.productType == other.productType;
	}

}
