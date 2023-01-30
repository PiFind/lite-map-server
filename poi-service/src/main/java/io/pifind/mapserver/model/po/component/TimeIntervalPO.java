package io.pifind.mapserver.model.po.component;

import lombok.Data;

@Data
public class TimeIntervalPO {

    /**
     * 开始时间
     * <p>
     *     24小时制，保留一位小数，区间为 0.0-24.0
     * </p>
     */
    private double start;

    /**
     * 结束时间
     * <p>
     *     24小时制，保留一位小数，区间为 0.0-24.0
     * </p>
     */
    private double end;

}
