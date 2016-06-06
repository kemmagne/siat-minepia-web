package org.guce.siat.web.ct.controller.util;

import org.guce.siat.common.model.User;


/**
 * The Class InspectorCheck.
 */
public class InspectorCheck
{

	/** The controller. */
	private User controller;

	/** The checked. */
	private Boolean checked;

	/** The style. */
	private String style;


	/**
	 * Instantiates a new inspector check.
	 *
	 * @param controller
	 *           the controller
	 * @param checked
	 *           the checked
	 * @param style
	 *           the style
	 */
	public InspectorCheck(final User controller, final Boolean checked, final String style)
	{
		super();
		this.controller = controller;
		this.checked = checked;
		this.style = style;
	}



	/**
	 * Gets the checked.
	 *
	 * @return the checked
	 */
	public Boolean getChecked()
	{
		return checked;
	}

	/**
	 * Sets the checked.
	 *
	 * @param checked
	 *           the new checked
	 */
	public void setChecked(final Boolean checked)
	{
		this.checked = checked;
	}

	/**
	 * Gets the style.
	 *
	 * @return the style
	 */
	public String getStyle()
	{
		return style;
	}

	/**
	 * Sets the style.
	 *
	 * @param style
	 *           the new style
	 */
	public void setStyle(final String style)
	{
		this.style = style;
	}



	/**
	 * Gets the controller.
	 *
	 * @return the controller
	 */
	public User getController()
	{
		return controller;
	}



	/**
	 * Sets the controller.
	 *
	 * @param controller
	 *           the new controller
	 */
	public void setController(User controller)
	{
		this.controller = controller;
	}

}
