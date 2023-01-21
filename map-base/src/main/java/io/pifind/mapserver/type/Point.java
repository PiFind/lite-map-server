package io.pifind.mapserver.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mysql 数据库 point 类型支持
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Point {

    /**
     * 经度
     */
    private Double latitude;

    /**
     * 纬度
     */
    private Double longitude;

}
