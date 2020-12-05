package ua.ugolek.service.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import ua.ugolek.model.BaseEntity;
import ua.ugolek.service.CRUDService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

public abstract class BaseExcelService<T extends BaseEntity>
{
    private final CellMapper<T> cellMapper;

    private CRUDService<T> crudService;

    public BaseExcelService(CellMapper<T> cellMapper, CRUDService<T> crudService) {
        this.cellMapper = cellMapper;
        this.crudService = crudService;
    }

    public void exportEntities(OutputStream stream) throws IOException
    {
        WorkbookCreator workbookCreator = new WorkbookCreator("Items");
        crudService.processAllEntities(workbookCreator.itemWriter);
        workbookCreator.writeIntoStream(stream);
    }

    private class WorkbookCreator
    {
        private final static int WORKBOOK_WINDOW_SIZE = 500;
        private final SXSSFWorkbook workbook = new SXSSFWorkbook (WORKBOOK_WINDOW_SIZE);
        private SXSSFSheet sheet;
        private int currentRowIndex = 0;

        private final Consumer<T> itemWriter = item -> {
            Row row = sheet.createRow(currentRowIndex++);
            cellMapper.createRowCells(row,  item);
        };

        public WorkbookCreator(String sheetName) throws IOException
        {
            this.sheet = workbook.createSheet(sheetName);
            createHeader();
            sheet.trackAllColumnsForAutoSizing();
        }

        private void createHeader()
        {
            Row header = sheet.createRow(currentRowIndex);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short)16);
            font.setBold(true);
            headerStyle.setFont(font);

            cellMapper.createHeaderCells(header, headerStyle);
            currentRowIndex++;
        }

        private void resizeColumnsToFitContentSize()
        {
            for ( int i = 0; i < cellMapper.getHeaders().length; i++ )
            {
                sheet.autoSizeColumn(i);
            }
        }

        public void writeIntoStream(OutputStream stream) throws IOException
        {
            resizeColumnsToFitContentSize();
            workbook.write(stream);
            workbook.dispose();
        }
    }


}
