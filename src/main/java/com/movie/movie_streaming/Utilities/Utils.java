package com.movie.movie_streaming.Utilities;


import com.movie.movie_streaming.common.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Utils {

    public static <T, R> PageResponse<R> generatePageResponse(
            Page<T> page,
            Function<T, R> mapper
            ){
        List<R> mapped = page.stream()
                .map(mapper)
                .toList() ;

        return PageResponse.<R>builder()
                .content(mapped)
                .number(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public static <T> void updateFieldIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
