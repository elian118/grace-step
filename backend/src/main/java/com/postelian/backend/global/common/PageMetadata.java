package com.postelian.backend.global.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageMetadata {
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean first;
    private final boolean last;

    @Builder
    public PageMetadata(int page, int size, long totalElements, int totalPages, boolean first, boolean last) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    public static PageMetadata from(Page<?> page) {
        return PageMetadata.builder()
                .page(page.getNumber() + 1) // 1-based page
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
