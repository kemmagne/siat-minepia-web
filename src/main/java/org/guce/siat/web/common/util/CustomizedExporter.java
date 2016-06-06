package org.guce.siat.web.common.util;

import java.io.IOException;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.guce.siat.common.utils.Constants;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;


/**
 * The Class CustomizedExporter.
 */
@ManagedBean(name = "exporter")
@ApplicationScoped
public class CustomizedExporter
{
	/**
	 * Export pdf.
	 *
	 * @param table
	 *           the table
	 * @param filename
	 *           the filename
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	public void exportPDF(final DataTable table, final String filename) throws IOException
	{
		final FacesContext context = FacesContext.getCurrentInstance();
		final Exporter exporter = new CustomPDFExporter();
		exporter.export(context, table, filename, false, false, Constants.ISO_8859_1_ENCODING_TYPE, null, null);
		context.responseComplete();
	}

}
