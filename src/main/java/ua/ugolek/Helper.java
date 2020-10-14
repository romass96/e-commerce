package ua.ugolek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.ugolek.model.*;
import ua.ugolek.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Helper  implements CommandLineRunner {
    private static final int CLIENTS_COUNT = 30;
    private static final int FEEDBACKS_COUNT = 10;
    private static final int CATEGORIES_COUNT = 8;
    private static final int PRODUCTS_COUNT = 10;
    private static final int ORDERS_COUNT = 10;
    
    private Map<Long, Client> clientMap = new HashMap<>();
    private Map<Long, Product> productMap = new HashMap<>();
    private Map<Long, Category> categoryMap = new HashMap<>();
    private Map<Long, Order> orderMap = new HashMap<>();

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        createAdmins();
        createClients();

        PropertyDefinition definition1 = new PropertyDefinition();
        definition1.setName("Длина шахты");

        PropertyDefinition definition2 = new PropertyDefinition();
        definition2.setName("Цвет");

        Category category1 = new Category();
        category1.setName("Кальяны");
        category1.addDefinition(definition1);
        category1.addDefinition(definition2);
        categoryService.add(category1);

        Category category2 = new Category();
        category2.setName("Аксессуары");
        category2 = categoryService.add(category2);

        Category category3 = new Category();
        category3.setName("Колбы");
        category3.setParentCategory(category2);
        category3 = categoryService.add(category3);

        Category category4 = new Category();
        category4.setName("Табаки");
        category4 = categoryService.add(category4);

        Category category5 = new Category();
        category5.setName("Синие колбы");
        category5.setParentCategory(category3);
        categoryService.add(category5);

        Category category6 = new Category();
        category6.setName("Красные колбы");
        category6.setParentCategory(category3);
        categoryService.add(category6);

        Category category7 = new Category();
        category7.setName("Тяжелые табаки");
        category7.setParentCategory(category4);
        categoryService.add(category7);

        Category category8 = new Category();
        category8.setName("Легкие табаки");
        category8.setParentCategory(category4);
        categoryService.add(category8);

        categoryMap = categoryService.getAll().stream()
                .collect(Collectors.toMap(Category::getId, Function.identity()));

        createProducts();

        createFeedbacks();

        createOrders();

    }

    public void createProducts() {
        String[] names = {"Кальян", "Стиральная машина", "Телевизор", "Чайник", "Табак"};
        for (int i = 1; i <= PRODUCTS_COUNT; i++) {
            Product product = new Product();

            int nameIndex = ThreadLocalRandom.current().nextInt(0, names.length);
            product.setName(names[nameIndex] + " " + generateString(3));

            product.setDescription(generateString(250));
            product.setCategory(categoryMap.get(ThreadLocalRandom.current().nextLong(1, CATEGORIES_COUNT + 1)));
            product.setPrice(BigDecimal.valueOf(getRandomDouble(1.0, 35598.50)));
            product.setQuantity(ThreadLocalRandom.current().nextInt(0, 1500));
            productService.add(product);
        }
        productMap = productService.getAll().stream().collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    public void createAdmins() {
        User user1 = new User();
        user1.setEmail("romababiy96@gmail.com");
        user1.setPassword("ugolek");
        user1.setFirstName("Роман");
        user1.setLastName("Бабий");
        userService.createAdmin(user1);

        User user2 = new User();
        user2.setEmail("dima.horobets95@gmail.com");
        user2.setPassword("ugolek95");
        user2.setFirstName("Дмитрий");
        user2.setLastName("Горобец");
        userService.createAdmin(user2);

        User user3 = new User();
        user3.setEmail("romababiy34@gmail.com");
        user3.setPassword("ugolek9590");
        user3.setFirstName("Иван");
        user3.setLastName("Качан");
        userService.createManager(user3);

        User user4 = new User();
        user4.setEmail("oleg.ermak@gmail.com");
        user4.setPassword("ermak9590");
        user4.setFirstName("Олег");
        user4.setLastName("Ермак");
        userService.createManager(user4);

        User user5 = new User();
        user5.setEmail("angela@gmail.com");
        user5.setPassword("romarulit");
        user5.setFirstName("Анжелочка");
        user5.setLastName("Гриневич");
        userService.createAdmin(user5);
    }

    public void createClients() {
        String[] firstNames = {"Иван", "Олег", "Александр", "Алексей", "Дмитрий", "Роман", "Евгений"};
        String[] lastNames = {"Иванов", "Петров", "Михайлов", "Добров", "Качер", "Бруяк", "Качан"};
        String[] domains = {"@gmail.com", "@mail.ru", "@ukr.net", "@yahoo.com"};
        for (int i = 1; i <= CLIENTS_COUNT; i++) {
            Client client = new Client();

            int domainIndex = ThreadLocalRandom.current().nextInt(0, domains.length);
            client.setEmail(generateString(10) + domains[domainIndex]);

            int firstNameIndex = ThreadLocalRandom.current().nextInt(0, firstNames.length);
            client.setFirstName(firstNames[firstNameIndex]);

            int lastNameIndex = ThreadLocalRandom.current().nextInt(0, lastNames.length);
            client.setLastName(lastNames[lastNameIndex]);

            client.setPhoneNumber("+3809" + generateNumericString(8));
            client.setPassword(generateString(6));

            LocalDateTime startDate = LocalDateTime.of(2020, 1, 1, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2020, 9, 30, 0, 0);

            client.setRegistrationDate(generateDate(startDate, endDate));

            clientService.create(client);
        }

        clientMap = clientService.getAll().stream().collect(Collectors.toMap(Client::getId, Function.identity()));
    }

    public void createFeedbacks() {
        for (int i = 1; i <= FEEDBACKS_COUNT; i++) {
            Feedback feedback = new Feedback();
            feedback.setClient(clientMap.get(getRandomLong(1, CLIENTS_COUNT + 1)));
            feedback.setProduct(productMap.get(getRandomLong(1, PRODUCTS_COUNT + 1)));
            feedback.setText(generateString(200));
            feedback.setRating(getRandomDouble(1, 5));
            feedback.setAdvantages(generateString(30));
            feedback.setDisadvantages(generateString(25));

            LocalDateTime startDate = LocalDateTime.of(2020, 1, 1, 0, 0);
            feedback.setCreatedDate(generateDate(startDate));
            feedbackService.create(feedback);
        }
    }

    public void createOrders() {
        for (int i = 1; i <= ORDERS_COUNT; i++) {
            Order order = new Order();
            order.setClient(clientMap.get(ThreadLocalRandom.current().nextLong(1, CLIENTS_COUNT + 1)));

            int orderItems = getRandomInt(1, PRODUCTS_COUNT + 1);
            for (int itemNumber = 1; itemNumber <= orderItems; itemNumber++) {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setQuantity(getRandomInt(1, 10));
                item.setProduct(productMap.get((long) itemNumber));
                order.addItem(item);
            }
            order.setComment(generateString(200));


            LocalDateTime startDate = LocalDateTime.of(2020, 1, 1, 0, 0);
            order.setCreatedDate(generateDate(startDate));
            orderService.create(order);
        }
    }

    public String generateString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String generateNumericString(int length) {
        int leftLimit = 48; // number '0'
        int rightLimit = 57; // number '9'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public int getRandomInt(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }

    public long getRandomLong(int start, int end) {
        return ThreadLocalRandom.current().nextLong(start, end);
    }

    public double getRandomDouble(double start, double end) {
        return ThreadLocalRandom.current().nextDouble(start, end);
    }

    private LocalDateTime generateDate(LocalDateTime start) {
        return generateDate(start, LocalDateTime.now());
    }

    private LocalDateTime generateDate(LocalDateTime start, LocalDateTime end) {
        long startSeconds = start.toEpochSecond(ZoneOffset.UTC);
        long endSeconds = end.toEpochSecond(ZoneOffset.UTC);
        long randomSeconds = ThreadLocalRandom
                .current()
                .nextLong(startSeconds, endSeconds);

        return LocalDateTime.ofEpochSecond(randomSeconds, 0, ZoneOffset.UTC);

    }


}
