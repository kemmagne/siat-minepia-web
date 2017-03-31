package org.guce.siat.web.ct.data;

import java.io.Serializable;

import org.guce.siat.common.model.Authority;
import org.guce.siat.common.model.FileType;



/**
 * The Class FileTypeAutorityData.
 */
public class FileTypeAutorityData implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4147644501526408007L;

	/** The authority. */
	private Authority authority;

	/** The file type. */
	private FileType fileType;

	/** The checked. */
	private Boolean checked;

	/**
	 * Instantiates a new file type autority data.
	 */
	public FileTypeAutorityData()
	{
	}

	/**
	 * Instantiates a new file type autority data.
	 *
	 * @param authority
	 *           the authority
	 * @param fileType
	 *           the file type
	 * @param checked
	 *           the checked
	 */
	public FileTypeAutorityData(final Authority authority, final FileType fileType, final boolean checked)
	{
		this.authority = authority;
		this.fileType = fileType;
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
	 * Gets the file type.
	 *
	 * @return the file type
	 */
	public FileType getFileType()
	{
		return fileType;
	}

	/**
	 * Sets the file type.
	 *
	 * @param fileType
	 *           the new file type
	 */
	public void setFileType(final FileType fileType)
	{
		this.fileType = fileType;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("FileTypeAutorityData [authority=");
		builder.append(authority.getRole());
		builder.append(", fileType=");
		builder.append(fileType.getCode());
		builder.append(", checked=");
		builder.append(checked);
		builder.append("]");
		return builder.toString();
	}


}
