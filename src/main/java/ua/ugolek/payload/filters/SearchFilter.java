package ua.ugolek.payload.filters;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
@Getter
public class SearchFilter {
    private int pageNumber;
    private int perPage;
    private String sortBy;
    private boolean sortDesc;
    private String stringForSearch;

    public Optional<String> getStringForSearchOptional() {
        return Optional.ofNullable(stringForSearch);
    }

    public Optional<String> getSortByOptional() {
        return Optional.ofNullable(sortBy);
    }
}
