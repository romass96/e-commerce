package ua.ugolek.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResponse<T> {
    private long totalItems;
    private List<T> items;
    private int totalPages;
    private int currentPage;
}
