package io.pifind.mapserver.mp.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * Mybatis-Plus分页对象
 * @param <T> 实体对象
 */
public class MybatisPage<T> extends Page<T> {

    /**
     * 构造
     */
    public MybatisPage() {
    }

    /**
     * 构造
     * @param current 当前页
     * @param size 每页显示条数
     */
    public MybatisPage(long current, long size) {
        super(current, size);
    }

    /**
     * 构造
     * @param current 当前页
     * @param size 每页显示条数
     * @param total 总数
     */
    public MybatisPage(long current, long size, long total) {
        super(current, size, total);
    }

    /**
     * 构造
     * @param current 当前页
     * @param size 每页显示条数
     * @param searchCount 是否查询总数
     */
    public MybatisPage(long current, long size, boolean searchCount) {
        super(current, size, searchCount);
    }

    /**
     * 构造
     * @param current 当前页
     * @param size 每页显示条数
     * @param total 总数
     * @param searchCount 是否查询总数
     */
    public MybatisPage(long current, long size, long total, boolean searchCount) {
        super(current, size, total, searchCount);
    }

}
