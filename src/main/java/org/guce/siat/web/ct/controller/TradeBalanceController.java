package org.guce.siat.web.ct.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.guce.siat.common.model.Country;
import org.guce.siat.common.model.FileItem;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.enums.FileTypeCode;
import org.guce.siat.core.ct.model.TradeBalanceConfig;
import org.guce.siat.core.ct.service.TradeBalanceConfigService;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.ct.controller.util.TradeCountryDto;
import org.guce.siat.web.ct.controller.util.TradeDto;
import org.guce.siat.web.ct.controller.util.TradeEvolutionDto;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * The Class TradeBalanceConfigController.
 */
@ManagedBean(name = "tradeBalanceController")
@SessionScoped
public class TradeBalanceController implements Serializable
{


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(TradeBalanceController.class);
	/** The page url. */
	private String pageUrl;

	/** The Constant PERSISTENCE_ERROR_OCCURED. */
	protected static final String PERSISTENCE_ERROR_OCCURED = "PersistenceErrorOccured";
	/** The Constant LOCAL_BUNDLE_NAME. */
	protected static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";

	/** The start year. */
	private Integer startYear;

	/** The end year. */
	private Integer endYear;

	/** The type trade. */
	private String typeTrade;

	/** The years. */
	private List<Integer> years = new ArrayList<Integer>();

	/** The trade balance configs. */
	private List<TradeBalanceConfig> tradeBalanceConfigs = new ArrayList<TradeBalanceConfig>();

	/** The trade balance configs import. */
	private List<TradeBalanceConfig> tradeBalanceConfigsImport = new ArrayList<TradeBalanceConfig>();

	/** The country dtos. */
	private List<TradeCountryDto> countryDtos = new ArrayList<TradeCountryDto>();

	/** The table. */
	private Boolean table;

	/** The file. */
	private StreamedContent file;

	/** The country. */
	private List<Country> countrys = new ArrayList<Country>();

	/** The total orientation. */
	private List<String> totalOrientation;

	/** The active index. */
	private Integer activeIndex;

	/** The trade balance config service. */
	@ManagedProperty(value = "#{tradeBalanceConfigService}")
	private TradeBalanceConfigService tradeBalanceConfigService;




	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public StreamedContent getFile()
	{

		file = new DefaultStreamedContent(export(), "application/xls", findMsg("balance_sheet_balance") + type
				+ dates.get(dates.size() - 1) + ".xls");
		return file;
	}

	/**
	 * Export.
	 *
	 * @return the byte array input stream
	 */
	private ByteArrayInputStream export()
	{
		ByteArrayOutputStream outputStream = null;
		outputStream = new ByteArrayOutputStream();
		final HSSFWorkbook wb = new HSSFWorkbook();
		final HSSFSheet sheet = wb.createSheet(findMsg("balance_sheet_balance"));//feuille 1
		final HSSFSheet sheet1 = wb.createSheet("Export");//feuille 2
		final HSSFSheet sheet3 = wb.createSheet("Import");//feuille 3
		final HSSFSheet sheet4 = wb.createSheet(findMsg("balance_sheet_orientation"));//feuille 4


		HSSFCellStyle cellStyle = null;
		cellStyle = wb.createCellStyle();
		HSSFCellStyle cellStyle1 = null;
		cellStyle1 = wb.createCellStyle();
		HSSFCellStyle cellStyle2 = null;
		cellStyle2 = wb.createCellStyle();
		HSSFCellStyle cellStyle3 = null;
		cellStyle3 = wb.createCellStyle();
		cellStyle3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFCellStyle cellStyle4 = null;
		cellStyle4 = wb.createCellStyle();
		HSSFCellStyle cellStyle5 = null;
		cellStyle5 = wb.createCellStyle();

		HSSFCellStyle cellStyle6 = null;
		cellStyle6 = wb.createCellStyle();
		//
		cellStyle6.setFillPattern(HSSFCellStyle.BORDER_HAIR);
		/**** style ***/
		cellStyle2.setBorderBottom(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle2.setBorderLeft(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle2.setBorderRight(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle2.setBorderTop(HSSFBorderFormatting.BORDER_DOTTED);

		cellStyle3.setBorderBottom(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle3.setBorderLeft(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle3.setBorderRight(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle3.setBorderTop(HSSFBorderFormatting.BORDER_DOTTED);

		cellStyle4.setBorderBottom(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle4.setBorderLeft(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle4.setBorderRight(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle4.setBorderTop(HSSFBorderFormatting.BORDER_DOTTED);

		cellStyle6.setBorderBottom(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle6.setBorderLeft(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle6.setBorderRight(HSSFBorderFormatting.BORDER_DOTTED);
		cellStyle6.setBorderTop(HSSFBorderFormatting.BORDER_DOTTED);
		/****************************************/
		final HSSFFont fonte = wb.createFont();
		fonte.setFontHeightInPoints((short) 16);
		fonte.setFontName("Algerian");
		cellStyle.setFont(fonte);

		final HSSFFont font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 8);
		font1.setFontName("Comic Sans MS");
		font1.setItalic(true);
		cellStyle1.setFont(font1);

		final HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 9);
		font2.setFontName("Comic Sans MS");
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle3.setFont(font2);
		cellStyle2.setFont(font2);

		final HSSFFont font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 9);
		font3.setFontName("Comic Sans MS");
		cellStyle4.setFont(font3);


		final HSSFFont font4 = wb.createFont();
		font4.setFontHeightInPoints((short) 10);
		font4.setFontName("Arial");
		font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font4.setColor(HSSFColor.RED.index);
		cellStyle5.setFont(font4);
		/**
		 * Font 2
		 */


		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = null;
		/*** Feuille 1 entete ****/
		cell = row.createCell(0);
		cell.setCellValue(findMsg("table_balance_evolution") + "( " + type + ")");
		cell.setCellStyle(cellStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_sheet_qv"));
		cell.setCellStyle(cellStyle1);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

		/*** Feuille 2 entete ****/
		row = sheet1.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("table_balance_export") + " ( " + type + ")");
		cell.setCellStyle(cellStyle);
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));

		row = sheet1.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_sheet_qv"));
		cell.setCellStyle(cellStyle1);
		sheet1.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

		/*** Feuille 3 entete ****/
		row = sheet3.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("table_balance_import") + "( " + type + ")");
		cell.setCellStyle(cellStyle);
		sheet3.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));

		row = sheet3.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_sheet_qv"));
		cell.setCellStyle(cellStyle1);
		sheet3.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));
		/*** Feuille 4 entete ****/
		row = sheet4.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("table_balance_geo") + "( " + type + ")");
		cell.setCellStyle(cellStyle);
		sheet4.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));
		row = sheet4.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_sheet_v"));
		cell.setCellStyle(cellStyle1);
		sheet4.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));
		/************* Contenue feuille 1 ******/
		/** période ***/
		row = sheet.createRow(3);
		cell = row.createCell(0);
		sheet.setColumnWidth(0, 7000);
		cell.setCellValue(findMsg("balance_periode"));
		cell.setCellStyle(cellStyle2);
		int j = 0;
		for (int i = 1; i < details.size(); i = i + 2)
		{
			cell = row.createCell(i);
			cell.setCellValue(year.get(j));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, i, i + 1));
			cell.setCellStyle(cellStyle2);
			j++;
		}

		/** Libéllés ***/
		row = sheet.createRow(4);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_label"));
		cell.setCellStyle(cellStyle2);

		for (int i = 1; i < details.size(); i = i + 2)
		{
			cell = row.createCell(i);
			cell.setCellValue("Q");
			cell.setCellStyle(cellStyle2);
			cell = row.createCell(i + 1);
			cell.setCellValue("V");
			cell.setCellStyle(cellStyle2);
		}

		//
		/** Exportations ***/
		row = sheet.createRow(5);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(0).getLabelFr() : colHeaders.get(0)
				.getLabelEn());
		cell.setCellStyle(cellStyle3);
		for (int i = 1; i <= colHeaders.get(0).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(0).getData().get(i - 1));
			cell.setCellStyle(cellStyle3);
		}
		/** Pétole brut ***/
		row = sheet.createRow(6);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(1).getLabelFr() : colHeaders.get(1)
				.getLabelEn());
		cell.setCellStyle(cellStyle4);
		for (int i = 1; i <= colHeaders.get(1).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(1).getData().get(i - 1));
			cell.setCellStyle(cellStyle4);
		}
		/** Exportations hors pétrole ***/
		row = sheet.createRow(7);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(2).getLabelFr() : colHeaders.get(2)
				.getLabelEn());
		cell.setCellStyle(cellStyle4);
		for (int i = 1; i <= colHeaders.get(2).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(2).getData().get(i - 1));
			cell.setCellStyle(cellStyle4);
		}
		/** Exportations hors Hydrocarbures ***/
		row = sheet.createRow(8);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(3).getLabelFr() : colHeaders.get(3)
				.getLabelEn());
		cell.setCellStyle(cellStyle4);
		for (int i = 1; i <= colHeaders.get(3).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(3).getData().get(i - 1));
			cell.setCellStyle(cellStyle4);
		}

		/** Importations ***/
		row = sheet.createRow(9);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(4).getLabelFr() : colHeaders.get(4)
				.getLabelEn());
		cell.setCellStyle(cellStyle3);
		for (int i = 1; i <= colHeaders.get(4).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(4).getData().get(i - 1));
			cell.setCellStyle(cellStyle3);
		}
		/** Pétole brut ***/
		row = sheet.createRow(10);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(5).getLabelFr() : colHeaders.get(5)
				.getLabelEn());
		cell.setCellStyle(cellStyle4);
		for (int i = 1; i <= colHeaders.get(5).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(5).getData().get(i - 1));
			cell.setCellStyle(cellStyle4);
		}

		/** Importations hors pétrole ***/
		row = sheet.createRow(11);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(6).getLabelFr() : colHeaders.get(6)
				.getLabelEn());
		cell.setCellStyle(cellStyle4);
		for (int i = 1; i <= colHeaders.get(6).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(6).getData().get(i - 1));
			cell.setCellStyle(cellStyle4);
		}

		/** Importations hors Hydrocarbures ***/
		row = sheet.createRow(12);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(7).getLabelFr() : colHeaders.get(7)
				.getLabelEn());
		cell.setCellStyle(cellStyle4);
		for (int i = 1; i <= colHeaders.get(7).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(7).getData().get(i - 1));
			cell.setCellStyle(cellStyle4);
		}

		/** Balance commerciale ***/
		row = sheet.createRow(13);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(8).getLabelFr() : colHeaders.get(8)
				.getLabelEn());
		cell.setCellStyle(cellStyle3);
		for (int i = 1; i <= colHeaders.get(8).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(8).getData().get(i - 1));
			cell.setCellStyle(cellStyle3);
		}
		/** Bal.com.hors pétrole ***/
		row = sheet.createRow(14);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(9).getLabelFr() : colHeaders.get(9)
				.getLabelEn());
		cell.setCellStyle(cellStyle4);
		for (int i = 1; i <= colHeaders.get(9).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(9).getData().get(i - 1));
			cell.setCellStyle(cellStyle4);
		}

		/** Bal.com.horsHydrocarbures ***/
		row = sheet.createRow(15);
		cell = row.createCell(0);

		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(10).getLabelFr() : colHeaders.get(10)
				.getLabelEn());

		cell.setCellStyle(cellStyle4);
		for (int i = 1; i <= colHeaders.get(10).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(10).getData().get(i - 1));
			cell.setCellStyle(cellStyle4);
		}

		/** Taux de couverture en % ***/
		row = sheet.createRow(16);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(11).getLabelFr() : colHeaders.get(11)
				.getLabelEn());
		cell.setCellStyle(cellStyle3);
		for (int i = 1; i <= colHeaders.get(11).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(11).getData().get(i - 1));
			cell.setCellStyle(cellStyle3);
		}
		/** Tx de couv.hors pétrole en % ***/
		row = sheet.createRow(17);
		cell = row.createCell(0);
		cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? colHeaders.get(12).getLabelFr() : colHeaders.get(12)
				.getLabelEn());
		cell.setCellStyle(cellStyle4);
		for (int i = 1; i <= colHeaders.get(12).getData().size(); i++)
		{
			cell = row.createCell(i);
			cell.setCellValue(colHeaders.get(12).getData().get(i - 1));
			cell.setCellStyle(cellStyle4);
		}

		/** * Données provisoires % ***/
		row = sheet.createRow(19);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_data"));
		cell.setCellStyle(cellStyle5);
		/********* contenue feuille 2 ************************/
		/** période ***/
		row = sheet1.createRow(3);
		cell = row.createCell(1);
		sheet1.setColumnWidth(1, 7000);
		cell.setCellValue(findMsg("balance_periode"));
		cell.setCellStyle(cellStyle2);
		j = 2;
		for (int i = 0; i < year.size(); i++)
		{
			cell = row.createCell(j);
			cell.setCellValue(year.get(i));
			//sheet1.addMergedRegion(new CellRangeAddress(3, 3, j, j + 2));
			cell.setCellStyle(cellStyle2);
			j = j + 2;
		}
		cell = row.createCell(details.size() + 2);

		cell.setCellValue("Part");
		cell.setCellStyle(cellStyle2);
		/** Code libile ***/
		row = sheet1.createRow(4);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_products"));
		cell.setCellStyle(cellStyle2);
		sheet1.setColumnWidth(0, 4000);


		/** Libéllés ***/

		cell = row.createCell(1);
		cell.setCellValue(findMsg("balance_label"));
		cell.setCellStyle(cellStyle2);

		for (int i = 2; i <= details.size(); i = i + 2)
		{
			cell = row.createCell(i);
			cell.setCellValue("Q");
			cell.setCellStyle(cellStyle2);
			cell = row.createCell(i + 1);
			cell.setCellValue("V");
			cell.setCellStyle(cellStyle2);
		}

		cell = row.createCell(details.size() + 2);
		cell.setCellValue("V");
		cell.setCellStyle(cellStyle2);
		/******* code *****/

		for (int i = 0; i < donnes.size(); i++)
		{
			row = sheet1.createRow(5 + i);
			cell = row.createCell(0);
			cell.setCellValue(donnes.get(i).getBalanceConfig().getCode());
			cell.setCellStyle(cellStyle4);
			cell = row.createCell(1);
			cell.setCellValue(donnes.get(i).getBalanceConfig().getLabel());
			cell.setCellStyle(cellStyle4);
			for (j = 0; j < donnes.get(i).getData().size(); j++)
			{
				cell = row.createCell(2 + j);
				cell.setCellValue(donnes.get(i).getData().get(j));
				cell.setCellStyle(cellStyle4);
			}

			cell = row.createCell(j + 2);
			cell.setCellValue(donnes.get(i).getPart());
			cell.setCellStyle(cellStyle4);
		}
		/****** Total des principaux produit *****/
		row = sheet1.createRow(donnes.size() + 5);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_total_product"));
		sheet1.addMergedRegion(new CellRangeAddress(donnes.size() + 5, donnes.size() + 5, 0, 1));
		cell.setCellStyle(cellStyle6);

		for (int i = 0; i < total.size(); i++)
		{
			cell = row.createCell(i + 2);
			cell.setCellValue(total.get(i));
			cell.setCellStyle(cellStyle6);
		}
		/****** Total des principaux produit *****/
		row = sheet1.createRow(donnes.size() + 6);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_total_export"));
		sheet1.addMergedRegion(new CellRangeAddress(donnes.size() + 6, donnes.size() + 6, 0, 1));
		cell.setCellStyle(cellStyle3);

		for (int i = 0; i < totalExportation.size(); i++)
		{
			cell = row.createCell(i + 2);
			cell.setCellValue(totalExportation.get(i));
			cell.setCellStyle(cellStyle3);
		}
		cell = row.createCell(details.size() + 2);
		cell.setCellValue("100%");
		cell.setCellStyle(cellStyle3);
		/***** légende ***/
		row = sheet1.createRow(donnes.size() + 7);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_sheet_legend"));
		sheet1.addMergedRegion(new CellRangeAddress(donnes.size() + 7, donnes.size() + 7, 0, 10));
		cell.setCellStyle(cellStyle4);

		/** * Données provisoires % ***/
		row = sheet1.createRow(donnes.size() + 9);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_data"));
		cell.setCellStyle(cellStyle5);
		/********* contenue feuille 3 ************************/
		/** période ***/
		row = sheet3.createRow(3);
		cell = row.createCell(1);
		sheet3.setColumnWidth(1, 7000);
		cell.setCellValue(findMsg("balance_periode"));
		cell.setCellStyle(cellStyle2);
		j = 2;
		for (int i = 0; i < year.size(); i++)
		{
			cell = row.createCell(j);
			cell.setCellValue(year.get(i));
			//sheet3.addMergedRegion(new CellRangeAddress(3, 3, j, j + 2));
			cell.setCellStyle(cellStyle2);
			j = j + 2;
		}
		cell = row.createCell(details.size() + 2);

		cell.setCellValue("Part");
		cell.setCellStyle(cellStyle2);
		/** Code libile ***/
		row = sheet3.createRow(4);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_products"));
		cell.setCellStyle(cellStyle2);
		sheet3.setColumnWidth(0, 4000);


		/** Libéllés ***/

		cell = row.createCell(1);
		cell.setCellValue(findMsg("balance_label"));
		cell.setCellStyle(cellStyle2);

		for (int i = 2; i <= details.size(); i = i + 2)
		{
			cell = row.createCell(i);
			cell.setCellValue("Q");
			cell.setCellStyle(cellStyle2);
			cell = row.createCell(i + 1);
			cell.setCellValue("V");
			cell.setCellStyle(cellStyle2);
		}

		cell = row.createCell(details.size() + 2);
		cell.setCellValue("V");
		cell.setCellStyle(cellStyle2);
		/******* code *****/

		for (int i = 0; i < donnesImport.size(); i++)
		{
			row = sheet3.createRow(5 + i);
			cell = row.createCell(0);
			cell.setCellValue(donnesImport.get(i).getBalanceConfig().getCode());
			cell.setCellStyle(cellStyle4);
			cell = row.createCell(1);
			cell.setCellValue(donnesImport.get(i).getBalanceConfig().getLabel());
			cell.setCellStyle(cellStyle4);
			for (j = 0; j < donnesImport.get(i).getData().size(); j++)
			{
				cell = row.createCell(2 + j);
				cell.setCellValue(donnesImport.get(i).getData().get(j));
				cell.setCellStyle(cellStyle4);
			}

			cell = row.createCell(j + 2);
			cell.setCellValue(donnesImport.get(i).getPart());
			cell.setCellStyle(cellStyle4);
		}

		/****** Total des principaux produit *****/
		row = sheet3.createRow(donnes.size() + 6);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_total_import"));
		sheet3.addMergedRegion(new CellRangeAddress(donnesImport.size() + 6, donnesImport.size() + 6, 0, 1));
		cell.setCellStyle(cellStyle3);

		for (int i = 0; i < totalImportation.size(); i++)
		{
			cell = row.createCell(i + 2);
			cell.setCellValue(totalImportation.get(i));
			cell.setCellStyle(cellStyle3);
		}
		cell = row.createCell(details.size() + 2);
		cell.setCellValue("100%");
		cell.setCellStyle(cellStyle3);
		/***** légende ***/
		row = sheet3.createRow(donnesImport.size() + 7);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_sheet_legend"));
		sheet3.addMergedRegion(new CellRangeAddress(donnes.size() + 7, donnesImport.size() + 7, 0, 10));
		cell.setCellStyle(cellStyle4);

		/** * Données provisoires % ***/
		row = sheet3.createRow(donnesImport.size() + 9);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_data"));
		cell.setCellStyle(cellStyle5);
		/********* contenue feuille 4 ************************/
		/** Année ***/
		row = sheet4.createRow(3);
		cell = row.createCell(0);
		sheet4.setColumnWidth(0, 7000);
		cell.setCellValue(findMsg("balance_year"));
		cell.setCellStyle(cellStyle2);
		j = 1;
		for (int i = 0; i < dates.size(); i++)
		{
			cell = row.createCell(j);
			cell.setCellValue(dates.get(i));
			sheet4.addMergedRegion(new CellRangeAddress(3, 3, j, j + 1));
			cell.setCellStyle(cellStyle2);
			j = j + 3;
		}
		row = sheet4.createRow(4);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_country"));
		cell.setCellStyle(cellStyle2);
		j = 1;
		for (int i = 0; i < dates.size(); i++)
		{

			cell = row.createCell(j);
			cell.setCellValue("Export");
			cell.setCellStyle(cellStyle2);

			cell = row.createCell(j + 1);
			cell.setCellValue("Import");
			cell.setCellStyle(cellStyle2);
			cell = row.createCell(j + 2);
			cell.setCellValue("Import-Export");
			cell.setCellStyle(cellStyle2);
			j = j + 3;
		}

		for (int i = 0; i < countryDtos.size(); i++)
		{
			row = sheet4.createRow(5 + i);
			cell = row.createCell(0);
			cell.setCellValue(getCurrentLocale().getLanguage().equals("fr") ? countryDtos.get(i).getCountry().getCountryNameCplFr()
					: countryDtos.get(i).getCountry().getCountryNameCpl());
			cell.setCellStyle(cellStyle4);
			for (j = 0; j < orientation.size(); j++)
			{
				cell = row.createCell(j + 1);
				cell.setCellValue(countryDtos.get(i).getData().get(j));
				cell.setCellStyle(cellStyle4);
			}
		}
		row = sheet4.createRow(countryDtos.size() + 5);
		cell = row.createCell(0);
		cell.setCellValue(findMsg("balance_total"));
		cell.setCellStyle(cellStyle2);
		for (j = 0; j < orientation.size(); j++)
		{
			cell = row.createCell(j + 1);
			cell.setCellValue(totalOrientation.get(j));
			cell.setCellStyle(cellStyle4);
		}

		// write the Excel content to the output stream
		byte[] bytes;
		try
		{
			wb.write(outputStream);
		}
		catch (final IOException e)
		{
			LOG.error(Objects.toString(e));
		}
		bytes = outputStream.toByteArray();
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		return inputStream;
	}


	/**
	 * On tab change.
	 *
	 * @param event
	 *           the event
	 */
	public void onTabChange(final TabChangeEvent event)
	{
		if (event.getTab().getTitle().equals("Evolution de la balance commerciale"))
		{
			activeIndex = Constants.ZERO;
		}
		else if (event.getTab().getTitle().equals("Exportation"))
		{
			activeIndex = Constants.ONE;
		}
		else if (event.getTab().getTitle().equals("Importation"))
		{
			activeIndex = Constants.TWO;
		}
		else if (event.getTab().getTitle().equals("Orientation Géographique"))
		{
			activeIndex = Constants.THREE;
		}
	}

	/**
	 * Sets the file.
	 *
	 * @param file
	 *           the new file
	 */
	public void setFile(final StreamedContent file)
	{
		this.file = file;
	}

	/**
	 * Gets the table.
	 *
	 * @return the table
	 */
	public Boolean getTable()
	{
		return table;
	}

	/**
	 * Sets the table.
	 *
	 * @param table
	 *           the new table
	 */
	public void setTable(final Boolean table)
	{
		this.table = table;
	}





	/**
	 * Gets the page url.
	 *
	 * @return the page url
	 */
	public String getPageUrl()
	{
		return pageUrl;
	}



	/**
	 * Sets the page url.
	 *
	 * @param pageUrl
	 *           the new page url
	 */
	public void setPageUrl(final String pageUrl)
	{
		this.pageUrl = pageUrl;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Constants.INIT_LOG_INFO_MESSAGE, DelegationController.class.getName());
		}

		listOfYears();
		listOfCountry();
		setPageUrl(ControllerConstants.Pages.FO.TRADE_BALANCE);




	}

	/**
	 * List of country.
	 */
	private void listOfCountry()
	{
		setCountrys(tradeBalanceConfigService.findAllCountry());

	}

	/**
	 * List of years.
	 */
	private void listOfYears()
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		final Date date = new Date();
		final String anneeFin = dateFormat.format(date);

		String anneeDeb = dateFormat.format(tradeBalanceConfigService.findMinDateFromFile());
		if (anneeDeb == null)
		{
			anneeDeb = anneeFin;
		}
		final Integer max = Integer.parseInt(anneeFin);
		final Integer min = Integer.parseInt(anneeDeb);


		for (int i = min; i <= max; i++)
		{
			years.add(i);
		}


	}

	/** The year. */
	List<String> year;

	/**
	 * Gets the year.
	 *
	 * @return the year
	 */
	public List<String> getYear()
	{
		return year;
	}

	/**
	 * Sets the year.
	 *
	 * @param year
	 *           the new year
	 */
	public void setYear(final List<String> year)
	{
		this.year = year;
	}

	/** The details. */
	List<String> details = new ArrayList<String>();

	/**
	 * Gets the details.
	 *
	 * @return the details
	 */
	public List<String> getDetails()
	{
		return details;
	}

	/**
	 * Sets the details.
	 *
	 * @param details
	 *           the new details
	 */
	public void setDetails(final List<String> details)
	{
		this.details = details;
	}

	/** The donnes. */
	private List<TradeDto> donnes;

	/** The donnes import. */
	private List<TradeDto> donnesImport;

	/** The data. */
	private List<String> data;

	/** The dates. */
	private List<String> dates;

	/**
	 * Gets the dates.
	 *
	 * @return the dates
	 */
	public List<String> getDates()
	{
		return dates;
	}

	/**
	 * Sets the dates.
	 *
	 * @param dates
	 *           the new dates
	 */
	public void setDates(final List<String> dates)
	{
		this.dates = dates;
	}

	/** The file type codes. */
	private List<FileTypeCode> fileTypeCodes;

	/** The file type codes import. */
	private List<FileTypeCode> fileTypeCodesImport;

	/** The df. */
	DecimalFormat df = new DecimalFormat("#.0");

	/** The details o. */
	private List<String> detailsO = new ArrayList<String>();

	/** The orientation. */
	private List<String> orientation = new ArrayList<String>();

	/**
	 * Trade export.
	 */
	private void tradeExport()
	{
		dates = new ArrayList<String>();
		tradeBalanceConfigs = tradeBalanceConfigService.findAllTradeBalanceConfigBytype("02");
		table = true;
		year = new ArrayList<String>();//co n1
		details = new ArrayList<String>();//co n2
		donnes = new ArrayList<TradeDto>();
		totalExportation = new ArrayList<String>();
		detailsO = new ArrayList<String>();
		total = new ArrayList<String>();


		for (int i = startYear; i <= endYear; i++)
		{
			dates.add(String.valueOf(i));
			detailsO.add("E");
			detailsO.add("I");
			if (startYear - endYear != 0)
			{
				if (i == endYear)
				{
					year.add("jan-dec" + String.valueOf(i) + "(2)");
				}
				else if (i == endYear - 1)
				{
					year.add("jan-dec" + String.valueOf(i) + "(1)");
				}
				else
				{
					year.add("jan-dec" + String.valueOf(i));
				}
			}
			else
			{
				year.add("jan-dec" + String.valueOf(i));
			}
			details.add("Q");
			details.add("V");
		}

		for (int k = 0; k < year.size(); k++)
		{
			final FileItem fileItem = tradeBalanceConfigService.findQuantityAndValueByNshAndFileTytpes(null, dates.get(k),
					fileTypeCodes);
			totalExportation.add(StringUtils.isNotBlank(fileItem.getQuantity()) ? fileItem.getQuantity() : "0");//Q
			totalExportation.add(StringUtils.isNotBlank(fileItem.getFobValue()) ? fileItem.getFobValue() : "0");//V
		}

		for (int i = 0; i < tradeBalanceConfigs.size(); i++)
		{


			final TradeDto dto = new TradeDto();
			final List<String> d = new ArrayList<String>();


			for (int k = 0; k < year.size(); k++)
			{//findQuatite and valu by code in year
				final FileItem fileItem = tradeBalanceConfigService.findQuantityAndValueByNshAndFileTytpes(tradeBalanceConfigs.get(i)
						.getCode(), dates.get(k), fileTypeCodes);
				d.add(StringUtils.isNotBlank(fileItem.getQuantity()) ? fileItem.getQuantity() : "0");//Q
				d.add(StringUtils.isNotBlank(fileItem.getFobValue()) ? fileItem.getFobValue() : "0");//V

			}

			//part by
			Double r;
			if (StringUtils.isNotBlank(d.get(details.size() - 1))
					&& StringUtils.isNotBlank(totalExportation.get(details.size() - 1))
					&& stringToDouble(d.get(details.size() - 1)) != 0)
			{
				final Double a = stringToDouble(d.get(details.size() - 1));
				final Double t = stringToDouble(totalExportation.get(details.size() - 1));
				r = (a / t) * 100;

			}
			else
			{
				r = null;
			}

			if (r == null || r <= 0.0)
			{
				dto.setPart("0.0%");
			}
			else
			{
				dto.setPart(df.format(r) + "%");
			}



			if (startYear - endYear != 0)
			{
				final String q = findQuantity(d);
				final String v = findValue(d);
				d.add(q);
				d.add(v);
			}
			dto.setBalanceConfig(tradeBalanceConfigs.get(i));

			dto.setData(d);
			donnes.add(dto);


		}

		//total exportation variation
		if (startYear - endYear != 0)
		{
			final String q = findQuantity(totalExportation);
			final String v = findValue(totalExportation);
			totalExportation.add(q);
			totalExportation.add(v);
		}


	}

	/** The total. */
	// data
	List<String> total = new ArrayList<String>();

	/** The total exportation. */
	List<String> totalExportation = new ArrayList<String>();

	/** The total importation. */
	List<String> totalImportation = new ArrayList<String>();

	/**
	 * Gets the total importation.
	 *
	 * @return the total importation
	 */
	public List<String> getTotalImportation()
	{
		return totalImportation;
	}

	/**
	 * Sets the total importation.
	 *
	 * @param totalImportation
	 *           the new total importation
	 */
	public void setTotalImportation(final List<String> totalImportation)
	{
		this.totalImportation = totalImportation;
	}

	/**
	 * Gets the total exportation.
	 *
	 * @return the total exportation
	 */
	public List<String> getTotalExportation()
	{
		return totalExportation;
	}

	/**
	 * Sets the total exportation.
	 *
	 * @param totalExportation
	 *           the new total exportation
	 */
	public void setTotalExportation(final List<String> totalExportation)
	{
		this.totalExportation = totalExportation;
	}

	/**
	 * Gets the total.
	 *
	 * @return the total
	 */
	public List<String> getTotal()
	{
		return total;
	}

	/**
	 * Sets the total.
	 *
	 * @param total
	 *           the new total
	 */
	public void setTotal(final List<String> total)
	{
		this.total = total;
	}

	/**
	 * Validate.
	 *
	 */
	private String type;

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *           the new type
	 */
	public void setType(final String type)
	{
		this.type = type;
	}

	/**
	 * Validate.
	 *
	 * @throws ParseException
	 *            the parse exception
	 */
	public void validate() throws ParseException
	{

		type = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString("Trade_type_" + typeTrade);

		fileTypeCodes = new ArrayList<FileTypeCode>();
		fileTypeCodesImport = new ArrayList<FileTypeCode>();
		if (typeTrade.equals("01"))
		{
			fileTypeCodes.add(FileTypeCode.DE_MINCOMMERCE);
			fileTypeCodesImport.add(FileTypeCode.DI_MINCOMMERCE);
		}
		else
		{
			fileTypeCodes.add(FileTypeCode.IDE);
			fileTypeCodesImport.add(FileTypeCode.IDI);
		}
		tradeExport();
		tradeImport();
		tradeResult();
		tradeOrientation();

		//total des principeaux produits
		int j = 0;
		for (int i = 0; i < year.size(); i++)
		{


			Double q = (double) 0;
			Double v = (double) 0;
			for (final TradeDto dto : donnes)

			{
				j = i * 2;

				if (dto.getBalanceConfig().getCount())
				{
					q = q + stringToDouble(dto.getData().get(j));


					j++;
					v = v + stringToDouble(dto.getData().get(j));

				}

			}
			if (q == 0)
			{
				total.add("0");
			}
			else
			{
				total.add(df.format(q));
			}
			if (v == 0)
			{
				total.add("0");
			}
			else
			{
				total.add(df.format(v));
			}
		}
		if (startYear - endYear != 0)
		{
			final String q = findQuantity(total);
			final String v = findValue(total);
			total.add(q);
			total.add(v);



		}
		//part
		Double r;
		if (StringUtils.isNotBlank(total.get(details.size() - 1))
				&& StringUtils.isNotBlank(totalExportation.get(details.size() - 1))
				&& stringToDouble(total.get(details.size() - 1)) != 0)
		{
			final Double a = stringToDouble(total.get(details.size() - 1));
			final Double t = stringToDouble(totalExportation.get(details.size() - 1));
			r = (a / t) * 100;

		}
		else
		{
			r = null;
		}

		if (r == null)
		{
			total.add(StringUtils.EMPTY);
		}
		else if (r <= 0.0)
		{
			total.add("0.0%");
		}
		else
		{
			total.add(df.format(r) + "%");
		}
		if (startYear - endYear != 0)
		{
			year.add("Variation en %(2/1)");
			details.add("Q");
			details.add("V");
		}

	}

	/**
	 * Trade orientation.
	 */
	private void tradeOrientation()
	{
		orientation = new ArrayList<String>();
		countryDtos = new ArrayList<TradeCountryDto>();
		for (int j = 0; j < dates.size(); j++)
		{
			orientation.add("I");
			orientation.add("E");
			orientation.add("D");
		}
		for (int i = 0; i < countrys.size(); i++)
		{
			int r = 0;
			final TradeCountryDto dto = new TradeCountryDto();
			final List<String> data = new ArrayList<String>();
			for (int j = 0; j < dates.size(); j++)
			{


				FileItem fileItem = tradeBalanceConfigService.findQuantityAndValueByCountryAndFileTytpes(dates.get(j), fileTypeCodes,
						countrys.get(i).getCountryIdAlpha2(), "E");
				data.add(fileItem.getFobValue() != null && StringUtils.isNotBlank(fileItem.getFobValue()) ? fileItem.getFobValue()
						: "0");

				fileItem = tradeBalanceConfigService.findQuantityAndValueByCountryAndFileTytpes(dates.get(j), fileTypeCodesImport,
						countrys.get(i).getCountryIdAlpha2(), "I");
				data.add(fileItem.getFobValue() != null && StringUtils.isNotBlank(fileItem.getFobValue()) ? fileItem.getFobValue()
						: "0");
				data.add(String.valueOf(Integer.parseInt(data.get(r + 1)) - Integer.parseInt(data.get(r))));
				r += 3;
			}
			dto.setCountry(countrys.get(i));
			dto.setData(data);
			countryDtos.add(dto);
		}
		totalOrientation = new ArrayList<String>();
		int j = 0;
		for (int i = 0; i < dates.size(); i++)
		{

			Double q = (double) 0;
			Double v = (double) 0;
			Double t = (double) 0;

			for (final TradeCountryDto dto : countryDtos)

			{
				q = q + stringToDouble(dto.getData().get(j));

				v = v + stringToDouble(dto.getData().get(j + 1));

				t = t + stringToDouble(dto.getData().get(j + 2));

			}
			if (q == 0)
			{
				totalOrientation.add("0");
			}
			else
			{
				totalOrientation.add(df.format(q));
			}
			if (v == 0)
			{
				totalOrientation.add("0");
			}
			else
			{
				totalOrientation.add(df.format(v));
			}
			if (t == 0)
			{
				totalOrientation.add("0");
			}
			else
			{
				totalOrientation.add(df.format(t));
			}
			//			totalOrientation.add(String.valueOf(Double.parseDouble(totalOrientation.get(r + 1).replace(",", "."))
			//					- Double.parseDouble(totalOrientation.get(r).replace(",", "."))));
			//	r = r + 3;
			j = j + 3;
		}

	}


	/** The col headers. */
	List<TradeEvolutionDto> colHeaders;

	/**
	 * Gets the col headers.
	 *
	 * @return the col headers
	 */
	public List<TradeEvolutionDto> getColHeaders()
	{
		return colHeaders;
	}

	/**
	 * Sets the col headers.
	 *
	 * @param colHeaders
	 *           the new col headers
	 */
	public void setColHeaders(final List<TradeEvolutionDto> colHeaders)
	{
		this.colHeaders = colHeaders;
	}

	/**
	 * Trade result.
	 */
	int l;

	/**
	 * Trade result.
	 */
	private void tradeResult()
	{
		colHeaders = new ArrayList<TradeEvolutionDto>();
		final TradeEvolutionDto exp = new TradeEvolutionDto("Exportations", "Exports", true);
		final TradeEvolutionDto epb = new TradeEvolutionDto("Pétrole brut", "crude Oil", false);
		final TradeEvolutionDto ehp = new TradeEvolutionDto("Exportation hors pétrole", "Exports excluding oil", false);
		final TradeEvolutionDto ehh = new TradeEvolutionDto("Exportation hors hydrocarbures", "export excluding hydrocarbons",
				false);
		final TradeEvolutionDto imp = new TradeEvolutionDto("Importations", "Imports", true);
		final TradeEvolutionDto ipb = new TradeEvolutionDto("Pétrole brut", "crude Oil", false);
		final TradeEvolutionDto ihp = new TradeEvolutionDto("Importatios hors pétrole", "Imports excluding oil", false);
		final TradeEvolutionDto ihh = new TradeEvolutionDto("Importations hors hydrocarbures", "export excluding hydrocarbons",
				false);
		final TradeEvolutionDto bc = new TradeEvolutionDto("Balance commerciale", "Trade balance", true);
		final TradeEvolutionDto bchp = new TradeEvolutionDto("Bal.com.hors pétrole", "Trade balance excluding oil", false);
		final TradeEvolutionDto bchh = new TradeEvolutionDto("Bal.com.hors hydrocarbures", "Trade balance excluding hydrocarbons",
				false);
		final TradeEvolutionDto tc = new TradeEvolutionDto("Taux de couverture en %", "Coverage ratio in %", true);
		final TradeEvolutionDto tchp = new TradeEvolutionDto("Tx de couv.hors pétrole en %", "Coverage ratio excluding oil in %",
				false);


		//Exp
		List<String> export = new ArrayList<String>();
		for (int j = 0; j < details.size(); j++)
		{
			export.add(totalExportation.get(j));

		}
		if (startYear - endYear != 0)
		{

			final String q = findQuantity(export);
			final String v = findValue(export);
			export.add(q);
			export.add(v);


		}
		exp.setData(export);
		colHeaders.add(exp);
		//pétrole brut
		export = new ArrayList<String>();
		for (int j = 0; j < dates.size(); j++)
		{
			final FileItem fileItem = tradeBalanceConfigService.findQuantityAndValueByNshAndFileTytpes("27.9.00.10", dates.get(j),
					fileTypeCodes);
			export.add(StringUtils.isNotBlank(fileItem.getQuantity()) ? fileItem.getQuantity() : "0");//Q
			export.add(StringUtils.isNotBlank(fileItem.getFobValue()) ? fileItem.getFobValue() : "0");//V

		}
		if (startYear - endYear != 0)
		{
			final String q = findQuantity(export);
			final String v = findValue(export);
			export.add(q);
			export.add(v);
		}
		epb.setData(export);
		colHeaders.add(epb);
		//Exportation hors pétrole
		export = new ArrayList<String>();
		for (int j = 0; j < details.size(); j++)
		{
			final Double a = stringToDouble(exp.getData().get(j));
			final Double b = stringToDouble(epb.getData().get(j));
			export.add(String.valueOf(a - b));

		}
		if (startYear - endYear != 0)
		{
			final String q = findQuantity(export);
			final String v = findValue(export);
			export.add(q);
			export.add(v);

		}
		ehp.setData(export);
		colHeaders.add(ehp);

		//Exportation hors hydro
		export = new ArrayList<String>();
		l = 0;
		for (int j = 0; j < dates.size(); j++)
		{
			l = l + j;
			final FileItem fileItem = tradeBalanceConfigService.findQuantityAndValueByNshAndFileTytpes("27.10", dates.get(j),
					fileTypeCodes);
			final String q = StringUtils.isNotBlank(fileItem.getQuantity()) ? fileItem.getQuantity() : "0";//Q
			final String v = StringUtils.isNotBlank(fileItem.getFobValue()) ? fileItem.getFobValue() : "0";//V

			final Double eq = stringToDouble(exp.getData().get(l)); //export
			final Double pq = stringToDouble(epb.getData().get(l));// pertoke

			final Double ev = stringToDouble(exp.getData().get(l + 1)); //export
			final Double pv = stringToDouble(epb.getData().get(l + 1));// pertoke
			final Double hq = stringToDouble(q);
			final Double hv = stringToDouble(v);

			export.add(String.valueOf(eq - (pq + hq)));//quantity
			export.add(String.valueOf(ev - (pv + hv)));//valeur


		}
		if (startYear - endYear != 0)
		{
			final String q = findQuantity(export);
			final String v = findValue(export);
			export.add(q);
			export.add(v);

		}
		ehh.setData(export);
		colHeaders.add(ehh);

		//Importation
		export = new ArrayList<String>();
		for (int j = 0; j < details.size(); j++)
		{
			export.add(totalImportation.get(j));

		}
		if (startYear - endYear != 0)
		{
			final String q = findQuantity(export);
			final String v = findValue(export);
			export.add(q);
			export.add(v);
		}
		imp.setData(export);
		colHeaders.add(imp);
		//Pétrole brut
		export = new ArrayList<String>();
		for (int j = 0; j < dates.size(); j++)
		{
			final FileItem fileItem = tradeBalanceConfigService.findQuantityAndValueByNshAndFileTytpes("27.9.00.10", dates.get(j),
					fileTypeCodesImport);
			export.add(StringUtils.isNotBlank(fileItem.getQuantity()) ? fileItem.getQuantity() : "0");//Q
			export.add(StringUtils.isNotBlank(fileItem.getFobValue()) ? fileItem.getFobValue() : "0");//V

		}
		if (startYear - endYear != 0)
		{
			final String q = findQuantity(export);
			final String v = findValue(export);
			export.add(q);
			export.add(v);
		}
		ipb.setData(export);
		colHeaders.add(ipb);
		//Importation hors pétrole
		export = new ArrayList<String>();
		for (int j = 0; j < details.size(); j++)
		{
			final Double a = stringToDouble(imp.getData().get(j));
			final Double b = stringToDouble(ipb.getData().get(j));
			export.add(String.valueOf(a - b));

		}
		if (startYear - endYear != 0)
		{
			final String q = findQuantity(export);
			final String v = findValue(export);
			export.add(q);
			export.add(v);

		}
		ihp.setData(export);
		colHeaders.add(ihp);
		//Importation hors hydrocarbures

		export = new ArrayList<String>();
		l = 0;
		for (int j = 0; j < dates.size(); j++)
		{
			l = l + j;
			final FileItem fileItem = tradeBalanceConfigService.findQuantityAndValueByNshAndFileTytpes("27.10", dates.get(j),
					fileTypeCodesImport);
			final String q = StringUtils.isNotBlank(fileItem.getQuantity()) ? fileItem.getQuantity() : "0";//Q
			final String v = StringUtils.isNotBlank(fileItem.getFobValue()) ? fileItem.getFobValue() : "0";//V

			final Double eq = stringToDouble(imp.getData().get(l)); //export
			final Double pq = stringToDouble(ipb.getData().get(l));// pertoke
			//l++;
			final Double ev = stringToDouble(imp.getData().get(l + 1)); //export
			final Double pv = stringToDouble(ipb.getData().get(l + 1));// pertoke
			final Double hq = stringToDouble(q);
			final Double hv = stringToDouble(v);

			export.add(String.valueOf(eq - (pq + hq)));//quantity
			export.add(String.valueOf(ev - (pv + hv)));//valeur


		}
		if (startYear - endYear != 0)
		{
			final String q = findQuantity(export);
			final String v = findValue(export);
			export.add(q);
			export.add(v);

		}
		ihh.setData(export);
		colHeaders.add(ihh);
		//Balance Commerciale
		export = new ArrayList<String>();

		for (int j = 0; j < details.size(); j = j + 2)
		{

			export.add(StringUtils.EMPTY);
			export.add(String.valueOf(stringToDouble(exp.getData().get(j + 1)) - stringToDouble(imp.getData().get(j + 1))));


		}

		bc.setData(export);
		colHeaders.add(bc);
		//Balance Commerciale hors pétrole


		export = new ArrayList<String>();
		for (int j = 0; j < details.size(); j = j + 2)
		{

			export.add(StringUtils.EMPTY);
			export.add(String.valueOf(stringToDouble(epb.getData().get(j + 1)) - stringToDouble(ipb.getData().get(j + 1))));


		}

		bchp.setData(export);
		colHeaders.add(bchp);
		//Balance Commerciale hors hydro
		export = new ArrayList<String>();

		export = new ArrayList<String>();
		for (int j = 0; j < details.size(); j = j + 2)
		{

			export.add(StringUtils.EMPTY);
			export.add(String.valueOf(stringToDouble(ehh.getData().get(j + 1)) - stringToDouble(ihh.getData().get(j + 1))));


		}

		bchh.setData(export);
		colHeaders.add(bchh);
		//Taux de couv
		export = new ArrayList<String>();
		for (int j = 0; j < details.size(); j = j + 2)
		{

			export.add(StringUtils.EMPTY);
			if (stringToDouble(imp.getData().get(j + 1)) != 0)
			{
				export.add(df.format(stringToDouble(exp.getData().get(j + 1)) * 100 / stringToDouble(imp.getData().get(j + 1))));
			}
			else
			{
				export.add("100");
			}


		}
		tc.setData(export);
		colHeaders.add(tc);
		//Taux de couv hp

		export = new ArrayList<String>();

		for (int j = 0; j < details.size(); j = j + 2)
		{

			export.add(StringUtils.EMPTY);
			if (stringToDouble(ipb.getData().get(j + 1)) != 0)
			{
				final Double r = stringToDouble(epb.getData().get(j + 1)) * 100 / stringToDouble(ipb.getData().get(j + 1));
				if (r <= 0.0)
				{
					export.add("0,0");
				}
				else
				{
					export.add(df.format(r));
				}
			}
			else
			{
				export.add("100");
			}


		}

		tchp.setData(export);
		colHeaders.add(tchp);

	}

	/**
	 * Trade import.
	 */
	private void tradeImport()
	{
		tradeBalanceConfigsImport = tradeBalanceConfigService.findAllTradeBalanceConfigBytype("01");
		donnesImport = new ArrayList<TradeDto>();
		totalImportation = new ArrayList<String>();
		for (int k = 0; k < year.size(); k++)
		{
			final FileItem fileItem = tradeBalanceConfigService.findQuantityAndValueByNshAndFileTytpes(null, dates.get(k),
					fileTypeCodesImport);
			totalImportation.add(StringUtils.isNotBlank(fileItem.getQuantity()) ? fileItem.getQuantity() : "0");//Q
			totalImportation.add(StringUtils.isNotBlank(fileItem.getFobValue()) ? fileItem.getFobValue() : "0");//V
		}

		for (int i = 0; i < tradeBalanceConfigsImport.size(); i++)
		{


			final TradeDto dto = new TradeDto();
			final List<String> d = new ArrayList<String>();


			for (int k = 0; k < year.size(); k++)
			{//findQuatite and valu by code in year
				final FileItem fileItem = tradeBalanceConfigService.findQuantityAndValueByNshAndFileTytpes(tradeBalanceConfigsImport
						.get(i).getCode(), dates.get(k), fileTypeCodesImport);
				d.add(StringUtils.isNotBlank(fileItem.getQuantity()) ? fileItem.getQuantity() : "0");//Q
				d.add(StringUtils.isNotBlank(fileItem.getFobValue()) ? fileItem.getFobValue() : "0");//V

			}

			//part by
			Double r;
			if (StringUtils.isNotBlank(d.get(details.size() - 1))
					&& StringUtils.isNotBlank(totalImportation.get(details.size() - 1))
					&& stringToDouble(d.get(details.size() - 1)) != 0)
			{
				final Double a = stringToDouble(d.get(details.size() - 1));
				final Double t = stringToDouble(totalImportation.get(details.size() - 1));
				r = (a / t) * 100;

			}
			else
			{
				r = null;
			}

			if (r == null || r <= 0.0)
			{
				dto.setPart("0.0%");
			}
			else
			{
				dto.setPart(df.format(r) + "%");
			}



			if (startYear - endYear != 0)
			{
				final String q = findQuantity(d);
				final String v = findValue(d);
				d.add(q);
				d.add(v);

			}
			dto.setBalanceConfig(tradeBalanceConfigsImport.get(i));

			dto.setData(d);
			donnesImport.add(dto);


		}


		//total importation variation
		//total exportation variation
		if (startYear - endYear != 0)
		{
			final String q = findQuantity(totalImportation);
			final String v = findValue(totalImportation);
			totalImportation.add(q);
			totalImportation.add(v);
		}



	}

	/**
	 * Change year.
	 */
	public void changeYear()
	{
		endYear = startYear;
	}

	/**
	 * Gets the trade balance config service.
	 *
	 * @return the trade balance config service
	 */
	public TradeBalanceConfigService getTradeBalanceConfigService()
	{
		return tradeBalanceConfigService;
	}

	/**
	 * Sets the trade balance config service.
	 *
	 * @param tradeBalanceConfigService
	 *           the new trade balance config service
	 */
	public void setTradeBalanceConfigService(final TradeBalanceConfigService tradeBalanceConfigService)
	{
		this.tradeBalanceConfigService = tradeBalanceConfigService;
	}

	/**
	 * Go to page.
	 */
	public void goToPage()
	{
		try
		{
			activeIndex = Constants.ZERO;

			final FacesContext context = FacesContext.getCurrentInstance();
			final ExternalContext extContext = context.getExternalContext();
			final String url = extContext.encodeActionURL(context.getApplication().getViewHandler().getActionURL(context, pageUrl));
			extContext.redirect(url);
		}
		catch (final IOException ioe)
		{

			java.util.logging.Logger.getLogger(this.getClass().getName())
					.logp(Level.SEVERE, this.getClass().getName(),
							Thread.currentThread().getStackTrace()[Constants.ONE].getMethodName(),
							ioe.getClass().getName() + ioe.getMessage());
		}
	}





	/**
	 * Gets the current locale.
	 *
	 * @return the current locale
	 */
	public Locale getCurrentLocale()
	{
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}



	/**
	 * Gets the start year.
	 *
	 * @return the start year
	 */
	public Integer getStartYear()
	{
		return startYear;
	}



	/**
	 * Sets the start year.
	 *
	 * @param startYear
	 *           the new start year
	 */
	public void setStartYear(final Integer startYear)
	{
		this.startYear = startYear;
	}



	/**
	 * Gets the end year.
	 *
	 * @return the end year
	 */
	public Integer getEndYear()
	{
		return endYear;
	}



	/**
	 * Sets the end year.
	 *
	 * @param endYear
	 *           the new end year
	 */
	public void setEndYear(final Integer endYear)
	{
		this.endYear = endYear;
	}



	/**
	 * Gets the years.
	 *
	 * @return the years
	 */
	public List<Integer> getYears()
	{
		return years;
	}



	/**
	 * Sets the years.
	 *
	 * @param years
	 *           the new years
	 */
	public void setYears(final List<Integer> years)
	{
		this.years = years;
	}



	/**
	 * Gets the type trade.
	 *
	 * @return the type trade
	 */
	public String getTypeTrade()
	{
		return typeTrade;
	}



	/**
	 * Sets the type trade.
	 *
	 * @param typeTrade
	 *           the new type trade
	 */
	public void setTypeTrade(final String typeTrade)
	{
		this.typeTrade = typeTrade;
	}

	/**
	 * Gets the trade balance configs.
	 *
	 * @return the trade balance configs
	 */
	public List<TradeBalanceConfig> getTradeBalanceConfigs()
	{
		return tradeBalanceConfigs;
	}

	/**
	 * Sets the trade balance configs.
	 *
	 * @param tradeBalanceConfigs
	 *           the new trade balance configs
	 */
	public void setTradeBalanceConfigs(final List<TradeBalanceConfig> tradeBalanceConfigs)
	{
		this.tradeBalanceConfigs = tradeBalanceConfigs;
	}

	/**
	 * Gets the donnes.
	 *
	 * @return the donnes
	 */
	public List<TradeDto> getDonnes()
	{
		return donnes;
	}

	/**
	 * Sets the donnes.
	 *
	 * @param donnes
	 *           the new donnes
	 */
	public void setDonnes(final List<TradeDto> donnes)
	{
		this.donnes = donnes;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public List<String> getData()
	{
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data
	 *           the new data
	 */
	public void setData(final List<String> data)
	{
		this.data = data;
	}

	/**
	 * Gets the trade balance configs import.
	 *
	 * @return the trade balance configs import
	 */
	public List<TradeBalanceConfig> getTradeBalanceConfigsImport()
	{
		return tradeBalanceConfigsImport;
	}

	/**
	 * Sets the trade balance configs import.
	 *
	 * @param tradeBalanceConfigsImport
	 *           the new trade balance configs import
	 */
	public void setTradeBalanceConfigsImport(final List<TradeBalanceConfig> tradeBalanceConfigsImport)
	{
		this.tradeBalanceConfigsImport = tradeBalanceConfigsImport;
	}

	/**
	 * Gets the donnes import.
	 *
	 * @return the donnes import
	 */
	public List<TradeDto> getDonnesImport()
	{
		return donnesImport;
	}

	/**
	 * Sets the donnes import.
	 *
	 * @param donnesImport
	 *           the new donnes import
	 */
	public void setDonnesImport(final List<TradeDto> donnesImport)
	{
		this.donnesImport = donnesImport;
	}

	/**
	 * String to double.
	 *
	 * @param value
	 *           the value
	 * @return the double
	 */
	public Double stringToDouble(final String value)
	{
		final NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
		try
		{
			if (StringUtils.isNotBlank(value))
			{
				return format.parse(value).doubleValue();
			}
			return (double) 0;
		}
		catch (final ParseException e)
		{
			LOG.error(Objects.toString(e));
			return (double) 0;
		}
	}

	/**
	 * Gets the file type codes import.
	 *
	 * @return the file type codes import
	 */
	public List<FileTypeCode> getFileTypeCodesImport()
	{
		return fileTypeCodesImport;
	}

	/**
	 * Sets the file type codes import.
	 *
	 * @param fileTypeCodesImport
	 *           the new file type codes import
	 */
	public void setFileTypeCodesImport(final List<FileTypeCode> fileTypeCodesImport)
	{
		this.fileTypeCodesImport = fileTypeCodesImport;
	}

	/**
	 * Find quantity.
	 *
	 * @param value
	 *           the value
	 * @return the string
	 */
	public String findQuantity(final List<String> value)
	{
		String q = "0";

		if (StringUtils.isNotBlank(value.get(details.size() - 2)) && StringUtils.isNotBlank(value.get(details.size() - 4))
				&& (stringToDouble(value.get(details.size() - 4)) != 0))
		{
			final Double a = stringToDouble(value.get(details.size() - 2));
			final Double b = stringToDouble(value.get(details.size() - 4));
			final Double p = ((a - b) / b) * 100;

			q = df.format(p);



		}
		return q;
	}

	/**
	 * Find value.
	 *
	 * @param value
	 *           the value
	 * @return the string
	 */
	public String findValue(final List<String> value)
	{
		String v = "0";
		if (StringUtils.isNotBlank(value.get(details.size() - 1)) && StringUtils.isNotBlank(value.get(details.size() - 3))
				&& (stringToDouble(value.get(details.size() - 3)) != 0))
		{
			final Double a = stringToDouble(value.get(details.size() - 1));
			final Double b = stringToDouble(value.get(details.size() - 3));
			final Double p = ((a - b) / b) * 100;
			v = df.format(p);

		}

		return v;
	}

	/**
	 * Find msg.
	 *
	 * @param key
	 *           the key
	 * @return the string
	 */
	public String findMsg(final String key)
	{
		return ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(key);
	}

	/**
	 * Gets the countrys.
	 *
	 * @return the countrys
	 */
	public List<Country> getCountrys()
	{
		return countrys;
	}

	/**
	 * Sets the countrys.
	 *
	 * @param countrys
	 *           the new countrys
	 */
	public void setCountrys(final List<Country> countrys)
	{
		this.countrys = countrys;
	}

	/**
	 * Gets the country dtos.
	 *
	 * @return the country dtos
	 */
	public List<TradeCountryDto> getCountryDtos()
	{
		return countryDtos;
	}

	/**
	 * Sets the country dtos.
	 *
	 * @param countryDtos
	 *           the new country dtos
	 */
	public void setCountryDtos(final List<TradeCountryDto> countryDtos)
	{
		this.countryDtos = countryDtos;
	}

	/**
	 * Gets the details o.
	 *
	 * @return the details o
	 */
	public List<String> getDetailsO()
	{
		return detailsO;
	}

	/**
	 * Sets the details o.
	 *
	 * @param detailsO
	 *           the new details o
	 */
	public void setDetailsO(final List<String> detailsO)
	{
		this.detailsO = detailsO;
	}

	/**
	 * Gets the total orientation.
	 *
	 * @return the total orientation
	 */
	public List<String> getTotalOrientation()
	{
		return totalOrientation;
	}

	/**
	 * Sets the total orientation.
	 *
	 * @param totalOrientation
	 *           the new total orientation
	 */
	public void setTotalOrientation(final List<String> totalOrientation)
	{
		this.totalOrientation = totalOrientation;
	}

	/**
	 * Gets the active index.
	 *
	 * @return the active index
	 */
	public Integer getActiveIndex()
	{
		return activeIndex;
	}

	/**
	 * Sets the active index.
	 *
	 * @param activeIndex
	 *           the new active index
	 */
	public void setActiveIndex(final Integer activeIndex)
	{
		this.activeIndex = activeIndex;
	}

	/**
	 * Gets the orientation.
	 *
	 * @return the orientation
	 */
	public List<String> getOrientation()
	{
		return orientation;
	}

	/**
	 * Sets the orientation.
	 *
	 * @param orientation
	 *           the new orientation
	 */
	public void setOrientation(final List<String> orientation)
	{
		this.orientation = orientation;
	}

}
