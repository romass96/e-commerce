package ua.ugolek.service.excel;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;
import ua.ugolek.model.Product;

@Component
public class ProductCellMapper extends CellMapper<Product>
{
    @Override
    public void createRowCells(Row row, Product product)
    {
        row.createCell(0).setCellValue(product.getId());
        row.createCell(1).setCellValue(product.getName());
        row.createCell(2).setCellValue(product.getPrice().doubleValue());
        row.createCell(3).setCellValue(product.getQuantity());
        row.createCell(4).setCellValue(product.getCategory().getName());
        row.createCell(5).setCellValue(product.getDescription());
    }

    @Override
    public String[] getHeaders()
    {
        return new String[] { "ID", "Name", "Price", "Quantity", "Category name", "Description" };
    }
}
