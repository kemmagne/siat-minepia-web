package org.guce.siat.web.reports.exporter;



/**
 * The Interface JasperExporter.
 */
public interface ReportCommand
{

	/** The siat logo. */
	String SIAT_LOGO = "siat_logo";

	/** The reports path. */
	String REPORTS_PATH = "jasperReport";

	/** The images path. */
	String IMAGES_PATH = "images/reports";
        
        /** The pdf forms path. */
        String PDF_FORMS_PATH = "pdfForms";

	/**
	 * Generate report.
	 */
	void exportReportToPdf();
}
