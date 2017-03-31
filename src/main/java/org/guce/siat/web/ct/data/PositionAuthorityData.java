package org.guce.siat.web.ct.data;

import java.io.Serializable;

import org.guce.siat.common.model.Authority;
import org.guce.siat.common.utils.enums.PositionType;


/**
 * The Class AuthorityPerPosition.
 */
public class PositionAuthorityData implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3053209435782400505L;

	/** The authority. */
	private Authority authority;

	/** The position. */
	private PositionType position;

	/** The checked. */
	private boolean checked;


	/**
	 * Instantiates a new authority per position.
	 */
	public PositionAuthorityData()
	{
	}

	/**
	 * Instantiates a new authority per position.
	 *
	 * @param authority
	 *           the authority
	 * @param position
	 *           the position
	 * @param checked
	 *           the checked
	 */
	public PositionAuthorityData(final Authority authority, final PositionType position, final boolean checked)
	{
		this.authority = authority;
		this.position = position;
		this.checked = checked;
	}

	/**
	 * Gets the authority.
	 *
	 * @return the authority
	 */
	public Authority getAuthority()
	{
		return authority;
	}

	/**
	 * Sets the authority.
	 *
	 * @param authority
	 *           the new authority
	 */
	public void setAuthority(final Authority authority)
	{
		this.authority = authority;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public PositionType getPosition()
	{
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position
	 *           the new position
	 */
	public void setPosition(final PositionType position)
	{
		this.position = position;
	}

	/**
	 * Checks if is checked.
	 *
	 * @return true, if is checked
	 */
	public boolean isChecked()
	{
		return checked;
	}

	/**
	 * Sets the checked.
	 *
	 * @param checked
	 *           the new checked
	 */
	public void setChecked(final boolean checked)
	{
		this.checked = checked;
	}



	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("AuthorityPerPosition [authority=");
		builder.append(authority);
		builder.append(", position=");
		builder.append(position);
		builder.append(", checked=");
		builder.append(checked);
		builder.append("]");
		return builder.toString();
	}
}
