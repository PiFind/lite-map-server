package io.pifind.poi.model.component;

import lombok.Data;

@Data
public class TimeIntervalDTO {

    /**
     * 营业开始时间
     * <p>
     *     格式为：HH:mm
     * </p>
     */
    private String startTime;

    /**
     * 营业结束时间
     * <p>
     *     格式为：HH:mm
     * </p>
     */
    private String endTime;

}
