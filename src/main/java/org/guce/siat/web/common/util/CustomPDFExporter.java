package org.guce.siat.web.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.guce.siat.common.model.Authority;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.primefaces.component.celleditor.CellEditor;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.PDFExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.faces.facelets.component.UIRepeat;

/**
 * The Class CustomPDFExporter.
 */
public class CustomPDFExporter extends PDFExporter {

	/**
	 * The Constant LOG.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(CustomPDFExporter.class);

	/**
	 * The Constant AUTHORITIES_IMAGES_LIST_ID.
	 */
	private static final String AUTHORITIES_IMAGES_LIST_ID = "authoritiesImagesList";

	/*
	 * (non-Javadoc)
	 *
	 * @see org.primefaces.component.export.Exporter#exportValue(javax.faces.context.FacesContext,
	 * javax.faces.component.UIComponent)
	 */
	@Override
	protected String exportValue(final FacesContext context, final UIComponent component) {
		if (component instanceof CellEditor) {
			return exportValue(context, ((CellEditor) component).getFacet("output"));
		} else if (component instanceof HtmlGraphicImage) {
			return (String) component.getAttributes().get("value");
		} else if (component instanceof UIRepeat
				&& AUTHORITIES_IMAGES_LIST_ID.equals(component.getParent().getAttributes().get("id"))) {
			final StringBuilder imageUrlList = new StringBuilder();

			@SuppressWarnings("unchecked")
			final List<Authority> authoritiesList = (List<Authority>) component.getAttributes().get("value");

			for (final Authority authority : authoritiesList) {
				imageUrlList.append("/images/icons/icon-" + authority.getRole() + ".png");
				if (authoritiesList.size() > 1) {
					imageUrlList.append("_");
				}
			}

			return imageUrlList.toString();
		} else {
			return super.exportValue(context, component);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.primefaces.component.export.PDFExporter#addColumnValue(com.lowagie.text.pdf.PdfPTable, java.util.List,
	 * com.lowagie.text.Font)
	 */
	@Override
	protected void addColumnValue(final PdfPTable pdfTable, final List<UIComponent> components, final Font font) {
		final StringBuilder builder = new StringBuilder();
		List<String> imagesUrlList = new ArrayList<String>();

		final FacesContext context = FacesContext.getCurrentInstance();

		for (final UIComponent component : components) {
			if (component.isRendered()) {
				final String value = exportValue(context, component);

				if (value != null) {
					if (component instanceof UIRepeat) {
						imagesUrlList = Arrays.asList(value.split("_"));
					} else {
						builder.append(value);
					}
				}
			}
		}

		addColumnsPdfPTable(pdfTable, font, builder, imagesUrlList);
	}

	/**
	 * Adds the columns pdf p table.
	 *
	 * @param pdfTable the pdf table
	 * @param font the font
	 * @param builder the builder
	 * @param imagesUrlList the images url list
	 */
	private void addColumnsPdfPTable(final PdfPTable pdfTable, final Font font, final StringBuilder builder,
			final List<String> imagesUrlList) {
		// Add cells in the pdf table by type : text or graphic images
		if (builder.toString().isEmpty() && imagesUrlList.isEmpty()) {
			pdfTable.addCell(new Paragraph(Constants.EMPTY_STRING, font));
		} else if (!builder.toString().isEmpty()) {
			pdfTable.addCell(new Paragraph(JsfUtil.convertToFrenchBoolean(builder).toString(), font));
		} else if (!imagesUrlList.isEmpty() && builder.toString().isEmpty()) {
			final PdfPCell pdfCell = new PdfPCell();
			for (final String imageUrl : imagesUrlList) {
				if (StringUtils.isNotBlank(imageUrl)) {
					try {
						final ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
								.getContext();
						final String graphicImageUrl = servletContext.getRealPath(StringUtils.EMPTY) + imageUrl;
						final Image graphicImage = Image.getInstance(graphicImageUrl);

						graphicImage.scaleAbsoluteHeight(Constants.FIFTEEN);
						graphicImage.scaleAbsoluteWidth(Constants.FIFTEEN);

						pdfCell.addElement(graphicImage);
					} catch (final BadElementException bee) {
						LOG.error(bee.getMessage(), bee);
					} catch (final IOException ioe) {
						if ("FileNotFoundException".equals(ioe.getClass().getSimpleName())) {
							pdfCell.addElement(new Paragraph(Constants.EMPTY_STRING, font));
							LOG.error(ioe.getMessage(), ioe);
						}

					}
				}
			}

			pdfCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pdfTable.addCell(pdfCell);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.primefaces.component.export.PDFExporter#export(javax.faces.context.FacesContext,
	 * org.primefaces.component.datatable.DataTable, java.lang.String, boolean, boolean, java.lang.String,
	 * javax.el.MethodExpression, javax.el.MethodExpression)
	 */
	@Override
	public void export(final FacesContext context, final DataTable table, final String filename, final boolean pageOnly,
			final boolean selectionOnly, final String encodingType, final MethodExpression preProcessor,
			final MethodExpression postProcessor) throws IOException {
		try {
			final Document document = new Document();
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, baos);

			if (preProcessor != null) {
				preProcessor.invoke(context.getELContext(), new Object[]{document});
			}

			if (!document.isOpen()) {
				document.setPageSize(PageSize.A4.rotate());
				document.setMargins(Constants.NEGATIVE_THIRTY_SIX, Constants.NEGATIVE_THIRTY_SIX, Constants.THIRTY_SIX,
						Constants.THIRTY_SIX);
				document.open();
			}

			document.add(exportPDFTable(context, table, pageOnly, selectionOnly, encodingType));

			if (postProcessor != null) {
				postProcessor.invoke(context.getELContext(), new Object[]{document});
			}

			document.close();

			writePDFToResponse(context.getExternalContext(), baos, filename);

		} catch (final DocumentException e) {
			LOG.error(e.getMessage(), e);
			throw new IOException(e.getMessage());
		}
	}
}
