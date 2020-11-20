package ua.ugolek.service.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public abstract class BaseExcelService<T> {
    private static String[] columns = {"Name", "Email", "Date Of Birth", "Salary"};

    private void createHeader(Sheet sheet, Workbook workbook) {
        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Age");
        headerCell.setCellStyle(headerStyle);
    }

    private void resizeColumnsToFitContentSize(Sheet sheet) {
        for (int i = 0; i < columns.length; i++ ) {
            sheet.autoSizeColumn(i);
        }
    }

    public void createExcelSheet(List<T> items, String sheetName) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        createHeader(sheet, workbook);
        writeItemsIntoSheet(items, sheet);

        resizeColumnsToFitContentSize(sheet);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }

    public void writeItemsIntoSheet(List<T> items, Sheet sheet) {
        int rowNumber = 1;
//        for (T item : items) {
//            Row row = sheet.createRow(rowNumber++);
//            row.createCell(0).setCellValue(product.getId());
//            row.createCell(1).setCellValue(product.getName());
//            row.createCell(2).setCellValue(product.getDescription());
//            row.createCell(3).setCellValue(product.getPrice().doubleValue());
//        }
    }

}
