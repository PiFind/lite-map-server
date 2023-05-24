package io.pifind.poi.model.component;

import io.pifind.poi.constant.WeekEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 营业时间
 */
@Data
public class BusinessTimeDTO {

    /**
     * 营业日（单周）
     * <p>
     *     只放置有效的营业日
     * </p>
     */
    private List<WeekEnum> businessDay;

    /**
     * 营业时间段
     */
    private List<TimeIntervalDTO> businessHours;

    /**
     * 休店开始时间
     */
    private Date vacationStartTime;

    /**
     * 休店结束时间
     */
    private Date vacationEndTime;

}
