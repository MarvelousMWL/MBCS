package com.bank.common.result;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private long total;
    private int page;
    private int size;
    private int totalPages;
    private List<T> records;

    public static <T> PageResult<T> of(long total, int page, int size, int totalPages, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        result.setTotalPages(totalPages);
        result.setRecords(records);
        return result;
    }
}
