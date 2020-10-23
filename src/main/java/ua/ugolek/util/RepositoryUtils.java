package ua.ugolek.util;

import lombok.experimental.UtilityClass;

import javax.persistence.TypedQuery;

@UtilityClass
public class RepositoryUtils {

    public static void setPaginationParameters(TypedQuery<?> query, int pageNumber, int perPage) {
        query.setFirstResult((pageNumber - 1) * perPage);
        query.setMaxResults(perPage);
    }

    public static String like(String input) {
        return "%" + input + "%";
    }

}
