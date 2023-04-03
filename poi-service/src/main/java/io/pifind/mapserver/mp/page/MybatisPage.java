package io.pifind.mapserver.mp.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class MybatisPage<T> extends Page<T> {

    public MybatisPage() {
    }

    public MybatisPage(long current, long size) {
        super(current, size);
    }

    public MybatisPage(long current, long size, long total) {
        super(current, size, total);
    }

    public MybatisPage(long current, long size, boolean searchCount) {
        super(current, size, searchCount);
    }

    public MybatisPage(long current, long size, long total, boolean searchCount) {
        super(current, size, total, searchCount);
    }

}
