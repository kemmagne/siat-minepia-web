package org.guce.siat.web.common.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


/**
 * The Class UploadFileManager.
 *
 * @param <T>
 *           the generic type
 */
public class UploadFileManager<T>
{

	/** The part. */
	private T part;

	/** The file. */
	private UploadedFile file;

	/** The file name. */
	private String fileName = null;



	/**
	 * Handle file upload.
	 *
	 * @param event
	 *           the event
	 */
	public void handleFileUpload(final FileUploadEvent event)
	{
		file = event.getFile();
		fileName = file.getFileName();

		final String[] s = fileName.replace("\\", "/").split("[/]+");
		if (s != null && s.length > 0)
		{
			fileName = s[s.length - 1];
		}
		if (event.getFile().getContents().length > 104857600)
		{
			fileName = StringUtils.EMPTY;

			final String msg = MessageFormat.format(
					ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
							FacesContext.getCurrentInstance().getViewRoot().getLocale()).getString(
							ControllerConstants.Bundle.Messages.IVALID_FILE_SISE), event.getFile().getContents().length / 1048576, 100);
			JsfUtil.addErrorMessage(msg);
			return;

		}
		if (event.getFile().getFileName().split("\\.").length > 1
				&& (event.getFile().getFileName().split("\\.")[1].contains("exe") || event.getFile().getFileName().split("\\.")[1]
						.contains("bat")))
		{
			fileName = StringUtils.EMPTY;

			final String msg = MessageFormat.format(
					ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
							FacesContext.getCurrentInstance().getViewRoot().getLocale()).getString(
							ControllerConstants.Bundle.Messages.IVALID_FILE_TYPE), event.getFile().getFileName().split("\\.")[1]);
			JsfUtil.addErrorMessage(msg);
			return;
		}

	}

	/**
	 * Gets the analyse part.
	 *
	 * @return the analyse part
	 */
	public T getPart()
	{
		return part;
	}

	/**
	 * Sets the analyse part.
	 *
	 * @param part
	 *           the new part
	 */
	public void setPart(final T part)
	{
		this.part = part;
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public UploadedFile getFile()
	{
		return file;
	}

	/**
	 * Sets the file.
	 *
	 * @param file
	 *           the new file
	 */
	public void setFile(final UploadedFile file)
	{
		this.file = file;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName
	 *           the new file name
	 */
	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((part == null) ? 0 : part.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object)
	{
		if (!(object instanceof UploadFileManager))
		{
			return false;
		}
		final UploadFileManager<?> other = (UploadFileManager<?>) object;
		if ((this.getPart() == null && other.getPart() != null)
				|| (this.getPart() != null && !this.getPart().equals(other.getPart())))
		{
			return false;
		}
		return true;
	}
}
