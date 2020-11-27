package ua.ugolek.payload.filters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilter extends SearchFilter
{
    private boolean archived;
}
