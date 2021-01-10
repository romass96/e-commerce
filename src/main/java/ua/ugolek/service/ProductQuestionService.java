package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.model.ProductQuestion;
import ua.ugolek.repository.ProductQuestionRepository;

@Service
public class ProductQuestionService extends CRUDService<ProductQuestion>
{
    @Autowired
    public ProductQuestionService(ProductQuestionRepository baseEntityRepository)
    {
        super(baseEntityRepository);
    }
}
