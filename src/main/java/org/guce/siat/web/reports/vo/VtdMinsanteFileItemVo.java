package org.guce.siat.web.reports.vo;



/**
 * The Class VtdMinsanteFileItemVo.
 */
public class VtdMinsanteFileItemVo extends AbstractFileItemVo
{
	/** The code. */
	private String code;
	
	private String amm;
	private String unit;
	private String fobValue;

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code
	 *           the new code
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

	public String getAmm() {
		return amm;
	}

	public void setAmm(String amm) {
		this.amm = amm;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFobValue() {
		return fobValue;
	}

	public void setFobValue(String fobValue) {
		this.fobValue = fobValue;
	}
	
	

}
