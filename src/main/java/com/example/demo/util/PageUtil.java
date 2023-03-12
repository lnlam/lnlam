package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PageUtil {

    public int getCurrentPage(int page) {
        return page <= 0 ? 1 : page;
    }

    public int getCurrentPageSize(int pageSize) {
        return pageSize <= 0 ? 5 : pageSize;
    }

    public List<Integer> getPageNumbers(int totalPages) {
        return IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
    }
}
