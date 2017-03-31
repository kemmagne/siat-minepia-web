package org.guce.siat.web.ct.data;

import java.io.Serializable;

import org.guce.siat.common.model.File;




/**
 * The Class FileDto.
 */
public class FileDto implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4147644501526408007L;

	/** The file. */
	private File file;

	/** The amount. */
	private Long amount;

	/** The kind. */
	private String kind;

	/**
	 * Instantiates a new file type autority data.
	 */
	public FileDto()
	{
	}


	/**
	 * Instantiates a new file dto.
	 *
	 * @param file
	 *           the file
	 * @param amount
	 *           the amount
	 * @param kind
	 *           the kind
	 */
	public FileDto(final File file, final Long amount, final String kind)
	{
		this.file = file;
		this.amount = amount;
		this.kind = kind;

	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * Sets the file.
	 *
	 * @param file
	 *           the file to set
	 */
	public void setFile(final File file)
	{
		this.file = file;
	}

	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public Long getAmount()
	{
		return amount;
	}

	/**
	 * Sets the amount.
	 *
	 * @param amount
	 *           the amount to set
	 */
	public void setAmount(final Long amount)
	{
		this.amount = amount;
	}

	/**
	 * Gets the kind.
	 *
	 * @return the kind
	 */
	public String getKind()
	{
		return kind;
	}

	/**
	 * Sets the kind.
	 *
	 * @param kind
	 *           the kind to set
	 */
	public void setKind(final String kind)
	{
		this.kind = kind;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
			final StringBuilder builder = new StringBuilder();
			builder.append("File [file=");
			builder.append(file.getId());
			builder.append(", amount=");
			builder.append(amount);
			builder.append(", kind=");
			builder.append(kind);
			builder.append("]");
			return builder.toString();
	}
}
