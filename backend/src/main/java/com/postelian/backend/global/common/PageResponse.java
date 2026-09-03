package com.postelian.backend.global.common;

import lombok.Getter;
import java.util.List;

@Getter
public class PageResponse<T> {
    private final List<T> data;
    private final PageMetadata pagination;

    public PageResponse(List<T> data, PageMetadata pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public static <T> PageResponse<T> of(List<T> data, PageMetadata pagination) {
        return new PageResponse<>(data, pagination);
    }
}
