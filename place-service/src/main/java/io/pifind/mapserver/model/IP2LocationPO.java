package io.pifind.mapserver.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ip2location_db11")
public class IP2LocationPO {

    /**
     * 开始IP段
     */
    private Integer ipFrom;

    /**
     * 结束IP段
     */
    private Integer ipTo;

    /**
     * 国家编码
     */
    private String countryCode;

    /**
     * 国家名
     */
    private String countryName;

    /**
     * 区域名
     */
    private String regionName;

    /**
     * 城市名
     */
    private String cityName;

    /**
     * 经度
     */
    private Double latitude;

    /**
     * 纬度
     */
    private Double longitude;

    /**
     * 邮政编码
     */
    private String zipCode;

    /**
     * 时区
     */
    private String timeZone;

    /**
     * 对应的行政区ID
     */
    private Long administrativeAreaId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
