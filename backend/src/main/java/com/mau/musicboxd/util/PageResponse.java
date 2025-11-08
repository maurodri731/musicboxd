package com.mau.musicboxd.util;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private long total;
    private String next;
    private String previous;

    public PageResponse(Page<T> page, String baseUrl) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.total = page.getTotalElements();
        
        if (page.hasNext()) {
            this.next = baseUrl + "?page=" + (page.getNumber() + 1) + "&size=" + page.getSize();
        }
        if (page.hasPrevious()) {
            this.previous = baseUrl + "?page=" + (page.getNumber() - 1) + "&size=" + page.getSize();
        }
    }
}
