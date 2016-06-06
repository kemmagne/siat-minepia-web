package org.guce.siat.web.ct.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.guce.siat.common.model.Attachment;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.ged.AlfrescoDirectoriesInitializer;
import org.guce.siat.common.utils.ged.CmisClient;
import org.guce.siat.common.utils.ged.CmisSession;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;


/**
 * The Class AttachmentController.
 */
@ManagedBean(name = "attachmentController")
@SessionScoped
public class AttachmentController implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8007921788685172375L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(AttachmentController.class);

	/** The Constant MIME_TYPE_PDF. */
	private static final String MIME_TYPE_PDF = "application/pdf";

	/** The selected attachment. */
	private Attachment selectedAttachment;

	/** The show panel view pdf. */
	private Boolean showPanelViewPdf = false;

	/** The show panel view jpeg. */
	private Boolean showPanelViewJpeg = false;

	/** The content stream. */
	private ContentStream contentStream;

	/** The content. */
	private StreamedContent streamPdf;

	/** The stream. */
	private StreamedContent streamJpeg;

	/**
	 * Instantiates a new attachment controller.
	 */
	public AttachmentController()
	{
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, AttachmentController.class.getName());
		}
		showPanelViewPdf = false;
		showPanelViewJpeg = false;
		generateContentStream();

	}

	/**
	 * Generate content stream.
	 */
	private void generateContentStream()
	{
		streamJpeg = null;
		contentStream = null;
		streamPdf = null;

		if (selectedAttachment != null)
		{
			final Session sessionCmisClient = CmisSession.getInstance();
			contentStream = CmisClient.getDocumentByPath(sessionCmisClient, getSelectedAttachment().getPath()
					+ AlfrescoDirectoriesInitializer.SLASH + getSelectedAttachment().getDocumentName());


			if (contentStream.getMimeType().equals(MIME_TYPE_PDF))
			{
				showPanelViewPdf = true;
				showPanelViewJpeg = false;
				getStreamPdf();
			}
			else
			{
				showPanelViewJpeg = true;
				showPanelViewPdf = false;
				getStreamJpeg();
			}
		}

	}

	/**
	 * Detach attachment.
	 *
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public void detachAttachment() throws IOException
	{
		streamJpeg = null;
		streamPdf = null;
		generateContentStream();
		getStreamJpeg();
		getStreamPdf();
		final RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('attachmentShowDialogWidVar').show();");
	}


	/**
	 * Gets the stream.
	 *
	 * @return the stream
	 */
	public StreamedContent getStreamJpeg()
	{

		if (streamJpeg == null && contentStream != null && showPanelViewJpeg)
		{
			final BufferedInputStream in = new BufferedInputStream(contentStream.getStream());
			final ByteArrayOutputStream out = new ByteArrayOutputStream();

			int val = -1;
			try
			{
				while ((val = in.read()) != -1)
				{
					out.write(val);
				}
			}
			catch (final IOException e)
			{
				LOG.error(e.getMessage(), e);
			}

			final byte[] bytes = out.toByteArray();

			showPanelViewJpeg = true;
			showPanelViewPdf = false;
			streamJpeg = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "image/png");
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Bytes ** PNG **  -> {}", bytes.length);
			}

		}

		return streamJpeg;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public StreamedContent getStreamPdf()
	{
		if (streamPdf == null && contentStream != null && showPanelViewPdf)
		{
			final BufferedInputStream in = new BufferedInputStream(contentStream.getStream());
			final ByteArrayOutputStream out = new ByteArrayOutputStream();

			int val = -1;
			try
			{
				while ((val = in.read()) != -1)
				{
					out.write(val);
				}
			}
			catch (final IOException ex)
			{
				LOG.error(ex.getMessage(), ex);
			}
			final byte[] bytes = out.toByteArray();

			try
			{
				final Document document = new Document();
				PdfWriter.getInstance(document, out);
				streamPdf = new DefaultStreamedContent(new ByteArrayInputStream(out.toByteArray()), "application/pdf");
			}
			catch (final Exception ex)
			{
				LOG.error(ex.getMessage(), ex);
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Bytes ** PDF **  -> {}", bytes.length);
			}
		}

		return streamPdf;
	}

	/**
	 * Gets the selected attachment.
	 *
	 * @return the selectedAttachment
	 */
	public Attachment getSelectedAttachment()
	{
		return selectedAttachment;
	}

	/**
	 * Sets the selected attachment.
	 *
	 * @param selectedAttachment
	 *           the selectedAttachment to set
	 */
	public void setSelectedAttachment(final Attachment selectedAttachment)
	{
		this.selectedAttachment = selectedAttachment;
	}

	/**
	 * Gets the show panel view pdf.
	 *
	 * @return the showPanelViewPdf
	 */
	public Boolean getShowPanelViewPdf()
	{
		return showPanelViewPdf;
	}

	/**
	 * Sets the show panel view pdf.
	 *
	 * @param showPanelViewPdf
	 *           the showPanelViewPdf to set
	 */
	public void setShowPanelViewPdf(final Boolean showPanelViewPdf)
	{
		this.showPanelViewPdf = showPanelViewPdf;
	}

	/**
	 * Gets the show panel view jpeg.
	 *
	 * @return the showPanelViewJpeg
	 */
	public Boolean getShowPanelViewJpeg()
	{
		return showPanelViewJpeg;
	}

	/**
	 * Sets the show panel view jpeg.
	 *
	 * @param showPanelViewJpeg
	 *           the showPanelViewJpeg to set
	 */
	public void setShowPanelViewJpeg(final Boolean showPanelViewJpeg)
	{
		this.showPanelViewJpeg = showPanelViewJpeg;
	}

	/**
	 * Gets the content stream.
	 *
	 * @return the contentStream
	 */
	public ContentStream getContentStream()
	{
		return contentStream;
	}

	/**
	 * Sets the content stream.
	 *
	 * @param contentStream
	 *           the contentStream to set
	 */
	public void setContentStream(final ContentStream contentStream)
	{
		this.contentStream = contentStream;
	}

	/**
	 * Gets the mime type pdf.
	 *
	 * @return the mimeTypePdf
	 */
	public static String getMimeTypePdf()
	{
		return MIME_TYPE_PDF;
	}

	/**
	 * Sets the stream.
	 *
	 * @param streamJpeg
	 *           the new stream jpeg
	 */
	public void setStreamJpeg(final StreamedContent streamJpeg)
	{
		this.streamJpeg = streamJpeg;
	}

	/**
	 * Sets the content.
	 *
	 * @param streamPdf
	 *           the new stream pdf
	 */
	public void setStreamPdf(final StreamedContent streamPdf)
	{
		this.streamPdf = streamPdf;
	}

}
