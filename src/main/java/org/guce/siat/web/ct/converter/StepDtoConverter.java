package org.guce.siat.web.ct.converter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.guce.siat.common.model.FileType;
import org.guce.siat.common.model.Step;
import org.guce.siat.common.service.FileTypeStepService;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.guce.siat.web.ct.data.StepDto;


/**
 * The Class StepConverter.
 */
@ManagedBean(name = "stepDtoConverter")
@RequestScoped
public class StepDtoConverter implements Converter
{

	/** The step service. */
	@ManagedProperty(value = "#{fileTypeStepService}")
	private FileTypeStepService fileTypeStepService;


	/*
	 * (non-Javadoc)
	 *
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String value)
	{
		StepDto returnedStepDto = null;
		if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value))
		{
			return null;
		}
		final FileType fileType = (FileType) component.getAttributes().get("fileType");

		final Step step = this.fileTypeStepService.findStepByFileTypeAndLabel(fileType, value);

		if (step != null)
		{
			@SuppressWarnings("unchecked")
			final List<StepDto> stepDtoList = (List<StepDto>) component.getAttributes().get("stepDtoList");

			for (final StepDto stepDto : stepDtoList)
			{
				if (step.getId() == stepDto.getStep().getId())
				{
					returnedStepDto = stepDto;
				}
			}
		}
		return returnedStepDto;
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
	public String getAsString(final FacesContext facesContext, final UIComponent component, final Object object)
	{
		if (object == null || (object instanceof String && ((String) object).length() == 0))
		{
			return null;
		}
		if (object instanceof StepDto)
		{
			final StepDto o = (StepDto) object;
			return getStringKey(o.getStep().getId());
		}
		else
		{
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}",
					new Object[]
					{ object, object.getClass().getName(), Step.class.getName() });
			return null;
		}
	}

	/**
	 * Gets the file type step service.
	 *
	 * @return the file type step service
	 */
	public FileTypeStepService getFileTypeStepService()
	{
		return fileTypeStepService;
	}

	/**
	 * Sets the file type step service.
	 *
	 * @param fileTypeStepService
	 *           the new file type step service
	 */
	public void setFileTypeStepService(final FileTypeStepService fileTypeStepService)
	{
		this.fileTypeStepService = fileTypeStepService;
	}


}
