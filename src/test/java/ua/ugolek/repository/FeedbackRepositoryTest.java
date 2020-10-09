package ua.ugolek.repository;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.ugolek.helpers.CategoryHelper;
import ua.ugolek.helpers.ClientHelper;
import ua.ugolek.helpers.ProductHelper;
import ua.ugolek.model.Category;
import ua.ugolek.model.Client;
import ua.ugolek.model.Feedback;
import ua.ugolek.model.Product;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ua.ugolek.utils.Utility.generateString;
import static ua.ugolek.utils.Utility.getDate;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class FeedbackRepositoryTest {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ClientHelper clientHelper;

    @Autowired
    private CategoryHelper categoryHelper;

    @Autowired
    private ProductHelper productHelper;

    @Test
    public void testFilter() {
        Client client = clientHelper.createClients(1).get(0);
        List<Category> categories = categoryHelper.createCategories(3);

        Product product1 = productHelper.createProduct(categories.get(0));
        Product product2 = productHelper.createProduct(categories.get(0));
        Product product3 = productHelper.createProduct(categories.get(1));

        Feedback feedback1 = new Feedback();
        feedback1.setClient(client);
        feedback1.setDisadvantages(generateString(10));
        feedback1.setAdvantages(generateString(10));
        feedback1.setText(generateString(20));
        feedback1.setProduct(product1);
        feedback1.setRating(2.5);
        feedback1.setCreatedDate(getDate(2020, 2, 23));
        feedbackRepository.save(feedback1);

        Feedback feedback2 = new Feedback();
        feedback2.setClient(client);
        feedback2.setDisadvantages(generateString(10));
        feedback2.setAdvantages(generateString(10));
        feedback2.setText(generateString(20));
        feedback2.setProduct(product2);
        feedback2.setRating(3.5);
        feedback2.setCreatedDate(getDate(2020, 7, 23));
        feedbackRepository.save(feedback2);

        Feedback feedback3 = new Feedback();
        feedback3.setClient(client);
        feedback3.setDisadvantages(generateString(10));
        feedback3.setAdvantages(generateString(10));
        feedback3.setText(generateString(20));
        feedback3.setProduct(product3);
        feedback3.setRating(3.5);
        feedback3.setCreatedDate(getDate(2020, 4, 13));
        feedbackRepository.save(feedback3);

        Pageable pageable = PageRequest.of(0, 10);
        List<Feedback> feedbacks = feedbackRepository.filter(pageable,
                getDate(2020, 1, 23),
                getDate(2020, 8, 23),
                2.2,
                4.8, null).getContent();
        assertEquals(feedbacks.size(), 3);

    }
}
