package io.pifind.place.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.pifind.map.model.CoordinateDTO;
import lombok.Data;

/**
 * 位置实体类
 */
@Data
public class LocationDTO {

    /**
     * 位置名
     */
    private String name;

    /**
     * 国家代号
     */
    private String country;

    /**
     * 丢失坐标标志
     * <p>
     *     判断是否能通过 {@link #getCoordinate()} 获取到经纬度坐标
     *     <ul>
     *         <li>{@code true} - <b>不能</b>获取经纬度定位 ({@link #getCoordinate()} 为 {@code null})</li>
     *         <li>{@code false} - <b>能</b>获取经纬度定位 (可以通过 {@link #getCoordinate()} 获取到经纬度的值)</li>
     *     </ul>
     * </p>
     */
    private boolean missingCoordinate;

    /**
     * 定位的坐标
     */
    private CoordinateDTO coordinate;

    /**
     * 定位到的行政区ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private String administrativeAreaId;

}
