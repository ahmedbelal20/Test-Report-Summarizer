import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelGenerator {

	public static void generateExcel(ArrayList<TestReport> testReports, String path) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		createTestCasesSheet(workbook, testReports);
		prepareChartsData(workbook);
		createCharts(workbook);
		writeExcel(workbook, path);
	}

	private static void createTestCasesSheet(Workbook workbook, ArrayList<TestReport> testReports) {
		// Create workbook and first sheet
		Sheet sheet = workbook.createSheet("Test Cases");
		String[] columns = { "Test Report Title", "Test Case No.", "Test Case Name", "Result", "Automatic/Manual" };
		// Header Cell Style
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.BLACK.getIndex());
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setBorderTop(BorderStyle.THIN);
		headerCellStyle.setBorderBottom(BorderStyle.THIN);
		headerCellStyle.setBorderLeft(BorderStyle.THIN);
		headerCellStyle.setBorderRight(BorderStyle.THIN);
		// Create header row
		Row headerRow = sheet.createRow(0);
		for (int column = 0; column < columns.length; column++) {
			Cell cell = headerRow.createCell(column);
			cell.setCellValue(columns[column]);
			cell.setCellStyle(headerCellStyle);
		}
		// Create body font
		Font bodyFont = workbook.createFont();
		bodyFont.setBold(false);
		bodyFont.setFontHeightInPoints((short) 11);
		bodyFont.setColor(IndexedColors.BLACK.getIndex());
		CellStyle bodyCellStyle = workbook.createCellStyle();
		bodyCellStyle.setFont(bodyFont);
		// Create body rows
		int row = 1;
		for (int i = 0; i < testReports.size(); i++) {
			TestReport testReport = testReports.get(i);
			for (int j = 0; j < testReport.getTestCasesNumber(); j++) {
				TestCase testCase = testReport.getTestCaseIndex(j);
				Row bodyRow = sheet.createRow(row);
				Cell cell = bodyRow.createCell(0);
				cell.setCellValue(testReport.getTitle());
				cell.setCellStyle(bodyCellStyle);
				cell = bodyRow.createCell(1);
				cell.setCellValue(testCase.getNumber());
				cell.setCellStyle(bodyCellStyle);
				cell = bodyRow.createCell(2);
				cell.setCellValue(testCase.getTitle());
				cell.setCellStyle(bodyCellStyle);
				cell = bodyRow.createCell(3);
				cell.setCellValue(testCase.getResult());
				cell.setCellStyle(bodyCellStyle);
				cell = bodyRow.createCell(4);
				cell.setCellValue("Automatic");
				cell.setCellStyle(bodyCellStyle);
				row++;
			}
		}
		// Auto size all columns to fit the data
		for (int column = 0; column < columns.length; column++)
			sheet.autoSizeColumn(column);
	}

	private static void prepareChartsData(Workbook workbook) {
		Sheet hiddenSheet = workbook.createSheet("Charts Data");
		// Create pass/fail categories
		String[] testResultsColumns = { "Passed", "Failed", "Other" };
		Row row = hiddenSheet.createRow(0);
		for (int i = 0; i < testResultsColumns.length; i++)
			row.createCell(i).setCellValue(testResultsColumns[i]);
		// Create automatic/manual categories
		String[] automationPercentageColumns = { "Automatic", "Manual" };
		row = hiddenSheet.createRow(2);
		for (int i = 0; i < automationPercentageColumns.length; i++)
			row.createCell(i).setCellValue(automationPercentageColumns[i]);
		// Calculate pass/fail values
		row = hiddenSheet.createRow(1);
		row.createCell(0).setCellFormula("COUNTIF('Test Cases'!D2:D1048576, \"pass\")");
		row.createCell(1).setCellFormula("COUNTIF('Test Cases'!D2:D1048576, \"fail\")");
		row.createCell(2).setCellFormula(
				"COUNTIFS('Test Cases'!D2:D1048576, \"<>pass\", 'Test Cases'!D2:D1048576, \"<>fail\", 'Test Cases'!D2:D1048576, \"<>\")");
		// Calculate automatic/manual values
		row = hiddenSheet.createRow(3);
		row.createCell(0).setCellFormula("COUNTIF('Test Cases'!E2:E1048576, \"Automatic\")");
		row.createCell(1).setCellFormula(
				"COUNTIFS('Test Cases'!E2:E1048576, \"<>Automatic\", 'Test Cases'!E2:E1048576, \"<>\")");
		workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheet), true);
	}

	private static void createCharts(Workbook workbook) {
		// Create the Summary sheet
		Sheet sheet = workbook.createSheet("Summary");
		Sheet dataSheet = workbook.getSheet("Charts Data");
		// Test Results Charts
		XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 2, 10, 25);
		XSSFChart chart = drawing.createChart(anchor);
		chart.setTitleText("Test Results");
		chart.setTitleOverlay(false);
		XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.RIGHT);
		XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromStringCellRange((XSSFSheet) dataSheet,
				new CellRangeAddress(0, 0, 0, 2));
		XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange((XSSFSheet) dataSheet,
				new CellRangeAddress(1, 1, 0, 2));
		XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
		data.setVaryColors(true);
		data.addSeries(categories, values);
		chart.plot(data);
		// Automation Percentage Chart
		drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
		anchor = drawing.createAnchor(0, 0, 0, 0, 11, 2, 21, 25);
		chart = drawing.createChart(anchor);
		chart.setTitleText("Automation Percentage");
		chart.setTitleOverlay(false);
		legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.RIGHT);
		categories = XDDFDataSourcesFactory.fromStringCellRange((XSSFSheet) dataSheet,
				new CellRangeAddress(2, 2, 0, 1));
		values = XDDFDataSourcesFactory.fromNumericCellRange((XSSFSheet) dataSheet, new CellRangeAddress(3, 3, 0, 1));
		data = chart.createData(ChartTypes.PIE, null, null);
		data.setVaryColors(true);
		data.addSeries(categories, values);
		chart.plot(data);
	}

	private static void writeExcel(Workbook workbook, String path) throws IOException {
		// Write the output file
		FileOutputStream fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}
}
