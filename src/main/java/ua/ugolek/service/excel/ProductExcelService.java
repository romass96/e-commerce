package ua.ugolek.service.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.model.Product;
import ua.ugolek.service.ProductService;

@Service
public class ProductExcelService extends BaseExcelService<Product>
{
    @Autowired
    public ProductExcelService(ProductService productService, ProductCellMapper productCellMapper)
    {
        super(productCellMapper, productService);
    }
}
