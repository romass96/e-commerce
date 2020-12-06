package ua.ugolek;

import java.time.ZoneOffset;

public class Constants {
    public final static ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(2);
    public final static String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm";

    public static final String ALL_FEEDBACKS = "all";
    public static final String POSITIVE_FEEDBACK_TYPE = "positive";
    public static final String NEGATIVE_FEEDBACK_TYPE = "negative";
    public static final String NORMAL_FEEDBACK_TYPE = "normal";
    public static final double NEGATIVE_HIGHER_BOUND = 3;
    public static final double POSITIVE_LOWER_BOUND = 4;
    public static final double POSITIVE_HIGHER_BOUND = 5;

    public static final int ITEMS_ON_PAGE = 5;
    public static final Integer[] ITEMS_ON_PAGE_OPTIONS = new Integer[]{ 5, 10, 15, 20, 25, 50};
    public static final String FEEDBACK_ITEMS_ON_PAGE_PROPERTY = "feedbacks.page.items";
    public static final String FEEDBACK_ITEMS_ON_PAGE_OPTIONS_PROPERTY = "feedbacks.page.items.options";
    public static final String CLIENTS_ITEMS_ON_PAGE_PROPERTY = "clients.page.items";
    public static final String CLIENTS_ITEMS_ON_PAGE_OPTIONS_PROPERTY = "clients.page.items.options";
    public static final String PRODUCTS_ITEMS_ON_PAGE_PROPERTY = "products.page.items";
    public static final String PRODUCTS_ITEMS_ON_PAGE_OPTIONS_PROPERTY = "products.page.items.options";


}
