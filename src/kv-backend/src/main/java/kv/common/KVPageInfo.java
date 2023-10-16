package kv.common;

import com.github.pagehelper.PageInfo;

import java.util.List;

public class KVPageInfo {
    private int page;
    private int pageSize;
    private int pages;
    private long total;
    private List<?> list;

    public static KVPageInfo build(List<?> list) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        KVPageInfo kvPageInfo = new KVPageInfo();

        kvPageInfo.setPage(pageInfo.getPageNum());
        kvPageInfo.setPageSize(pageInfo.getPageSize());
        kvPageInfo.setPages(pageInfo.getPages());
        kvPageInfo.setTotal(pageInfo.getTotal());
        kvPageInfo.setList(list);

        return kvPageInfo;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
