package ua.ugolek.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.ugolek.dto.ProductQuestionDTO;
import ua.ugolek.payload.ListResponse;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.service.dto.ProductQuestionDTOService;

@RestController
@RequestMapping("/api/productQuestions")
public class ProductQuestionController
{
    @Autowired
    private ProductQuestionDTOService productQuestionDTOService;

    @PostMapping("/filter")
    public ListResponse<ProductQuestionDTO> getProductQuestionsByFilter(@RequestBody SearchFilter filter) {
        return productQuestionDTOService.queryByFilter(filter);
    }
}
