package ua.ugolek.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ua.ugolek.model.OrderStatus;
import java.time.LocalDate;

@Getter
@Setter
public class OrderFilter {
    private int pageNumber;
    private int perPage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate toDate;
    private Long categoryId;

    private OrderStatus status;

    private Double fromPrice;
    private Double toPrice;

    private String stringForSearch;

    private String sortBy;
    private boolean sortDesc;

}
