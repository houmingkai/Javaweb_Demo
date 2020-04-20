package com.hmk.javaweb.utils;

import com.github.pagehelper.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PageFeign<T> implements Serializable {
    private static final long serialVersionUID = 1747263515504075479L;

    private List<T> content          = new ArrayList<>();

    private int               page;

    private int               size;

    private long              total;

    public static PageFeign setPageFeign (Page page){
        PageFeign pageFeign = new PageFeign();
        pageFeign.setContent(page.getResult());
        pageFeign.setPage(page.getPageNum());
        pageFeign.setSize(page.getPageSize());
        pageFeign.setTotal(page.getTotal());
        return pageFeign;
    }
}
