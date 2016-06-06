package org.guce.siat.web.ct.controller.util;

import org.guce.siat.common.model.FileItem;


/**
 * The Class FileItemCheck.
 */
public class FileItemCheck
{

	/** The file item. */
	private FileItem fileItem;

	/** The decision check. */
	private Boolean decisionCheck;

	/** The rollback check. */
	private Boolean rollbackCheck;

	/** The enabled check. */
	private Boolean enabledCheck;

	/**
	 * Instantiates a new file item check.
	 *
	 * @param fileItem
	 *           the file item
	 * @param decisionCheck
	 *           the decision check
	 * @param rollbackCheck
	 *           the rollback check
	 * @param enabledCheck
	 *           the enabled check
	 */
	public FileItemCheck(final FileItem fileItem, final Boolean decisionCheck, final Boolean rollbackCheck,
			final Boolean enabledCheck)
	{
		super();
		this.fileItem = fileItem;
		this.decisionCheck = decisionCheck;
		this.rollbackCheck = rollbackCheck;
		this.enabledCheck = enabledCheck;
	}

	/**
	 * Gets the file item.
	 *
	 * @return the file item
	 */
	public FileItem getFileItem()
	{
		return fileItem;
	}

	/**
	 * Sets the file item.
	 *
	 * @param fileItem
	 *           the new file item
	 */
	public void setFileItem(final FileItem fileItem)
	{
		this.fileItem = fileItem;
	}

	/**
	 * Gets the decision check.
	 *
	 * @return the decision check
	 */
	public Boolean getDecisionCheck()
	{
		return decisionCheck;
	}

	/**
	 * Sets the decision check.
	 *
	 * @param decisionCheck
	 *           the new decision check
	 */
	public void setDecisionCheck(final Boolean decisionCheck)
	{
		this.decisionCheck = decisionCheck;
	}

	/**
	 * Gets the rollback check.
	 *
	 * @return the rollback check
	 */
	public Boolean getRollbackCheck()
	{
		return rollbackCheck;
	}

	/**
	 * Sets the rollback check.
	 *
	 * @param rollbackCheck
	 *           the new rollback check
	 */
	public void setRollbackCheck(final Boolean rollbackCheck)
	{
		this.rollbackCheck = rollbackCheck;
	}

	/**
	 * Gets the enabled check.
	 *
	 * @return the enabled check
	 */
	public Boolean getEnabledCheck()
	{
		return enabledCheck;
	}

	/**
	 * Sets the enabled check.
	 *
	 * @param enabledCheck
	 *           the new enabled check
	 */
	public void setEnabledCheck(final Boolean enabledCheck)
	{
		this.enabledCheck = enabledCheck;
	}
}
