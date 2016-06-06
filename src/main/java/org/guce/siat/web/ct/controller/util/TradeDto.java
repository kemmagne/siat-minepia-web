package org.guce.siat.web.ct.controller.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.guce.siat.core.ct.model.TradeBalanceConfig;


/**
 * The Class TradeDto.
 */
public class TradeDto implements Serializable

{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The balance config. */
	private TradeBalanceConfig balanceConfig;

	/** The data. */
	private List<String> data = new ArrayList<String>();

	/** The part. */
	private String part;

	/**
	 * Gets the balance config.
	 *
	 * @return the balance config
	 */
	public TradeBalanceConfig getBalanceConfig()
	{
		return balanceConfig;
	}

	/**
	 * Sets the balance config.
	 *
	 * @param balanceConfig
	 *           the new balance config
	 */
	public void setBalanceConfig(final TradeBalanceConfig balanceConfig)
	{
		this.balanceConfig = balanceConfig;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public List<String> getData()
	{
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data
	 *           the new data
	 */
	public void setData(final List<String> data)
	{
		this.data = data;
	}

	/**
	 * Gets the part.
	 *
	 * @return the part
	 */
	public String getPart()
	{
		return part;
	}

	/**
	 * Sets the part.
	 *
	 * @param part
	 *           the new part
	 */
	public void setPart(final String part)
	{
		this.part = part;
	}


}
