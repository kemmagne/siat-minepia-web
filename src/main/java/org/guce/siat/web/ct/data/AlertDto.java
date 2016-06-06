package org.guce.siat.web.ct.data;

import org.guce.siat.core.gr.model.Alert;

/**
 * The Class AlertDto.
 */
public class AlertDto
{
	private Alert alert;
	private Boolean cheked;
	
	/**
	 * Gets the alert.
	 *
	 * @return the alert
	 */
	public Alert getAlert()
	{
		return alert;
	}
	
	/**
	 * Sets the alert.
	 *
	 * @param alert
	 *           the new alert
	 */
	public void setAlert(Alert alert)
	{
		this.alert = alert;
	}

	/**
	 * Gets the cheked.
	 *
	 * @return the cheked
	 */
	public Boolean getCheked()
	{
		return cheked;
	}
	
	/**
	 * Sets the cheked.
	 *
	 * @param cheked
	 *           the new cheked
	 */
	public void setCheked(Boolean cheked)
	{
		this.cheked = cheked;
	}

	/**
	 * Instantiates a new alert dto.
	 *
	 * @param alert
	 *           the alert
	 * @param cheked
	 *           the cheked
	 */
	public AlertDto(Alert alert, Boolean cheked)
	{
		super();
		this.alert = alert;
		this.cheked = cheked;
	}
	
	/**
	 * Instantiates a new alert dto.
	 */
	public AlertDto()
	{

	}
	
}
