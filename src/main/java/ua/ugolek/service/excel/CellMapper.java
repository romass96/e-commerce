package ua.ugolek.service.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

public abstract class CellMapper<T>
{
    public abstract void createRowCells(Row row, T object);
    public abstract String[] getHeaders();

    public void createHeaderCells(Row header, CellStyle headerStyle) {
        String[] headers = getHeaders();
        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(headerStyle);
        }
    }
}
