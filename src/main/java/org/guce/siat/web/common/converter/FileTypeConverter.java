package org.guce.siat.web.common.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.guce.siat.common.model.FileType;
import org.guce.siat.common.service.FileTypeService;
import org.guce.siat.web.ct.controller.util.JsfUtil;


/**
 * The Class FileTypeConverter.
 */
@ManagedBean(name = "fileTypeConverter")
@RequestScoped
public class FileTypeConverter implements Converter
{

	/** The file type service. */
	@ManagedProperty(value = "#{fileTypeService}")
	private FileTypeService fileTypeService;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value)
	{
		if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value))
		{
			return null;
		}
		return this.fileTypeService.find(getKey(value));
	}

	/**
	 * Gets the key.
	 *
	 * @param value
	 *           the value
	 * @return the key
	 */
	Long getKey(final String value)
	{
		Long key;
		key = Long.valueOf(value);
		return key;
	}

	/**
	 * Gets the string key.
	 *
	 * @param value
	 *           the value
	 * @return the string key
	 */
	String getStringKey(final Long value)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(value);
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object object)
	{
		if (object == null || (object instanceof String && ((String) object).length() == 0))
		{
			return null;
		}
		if (object instanceof FileType)
		{
			final FileType fileType = (FileType) object;
			return getStringKey(fileType.getId());
		}
		else
		{
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
					new Object[]
					{ object, object.getClass().getName(), FileType.class.getName() });
			return null;
		}
	}

	/**
	 * Gets the file type service.
	 *
	 * @return the fileTypeService
	 */
	public FileTypeService getFileTypeService()
	{
		return fileTypeService;
	}

	/**
	 * Sets the file type service.
	 *
	 * @param fileTypeService
	 *           the fileTypeService to set
	 */
	public void setFileTypeService(final FileTypeService fileTypeService)
	{
		this.fileTypeService = fileTypeService;
	}

}
