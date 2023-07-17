package io.pifind.poi.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.pifind.map.model.CoordinateDTO;
import io.pifind.poi.constant.BusinessStatusEnum;
import io.pifind.poi.constant.PoiStatusEnum;
import io.pifind.poi.model.component.BusinessTimeDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 兴趣点
 */
@Data
public class InterestPointVO {

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 兴趣点名称
     */
    private String name;

    /**
     * 兴趣点名称（英文名）
     */
    private String nameEN;

    /**
     * 兴趣点详细地址
     */
    private String address;

    /**
     * 兴趣点详细地址（英文名）
     */
    private String addressEN;

    /**
     * 所属类别ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;

    /**
     * 行政区ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private String administrativeAreaId;

    /**
     * 营业时间
     */
    private BusinessTimeDTO businessTime;

    /**
     * 营业执照
     */
    private String businessLicense;

    /**
     * 营业执照图片路径
     */
    private String businessLicenseUrl;

    /**
     * 营业状态
     */
    private BusinessStatusEnum businessStatus;

    /**
     * 电话
     */
    private List<String> tels;

    /**
     * LOGO 图片
     */
    private String logo;

    /**
     * 北京图片
     */
    private String background;

    /**
     * 图片
     */
    private List<String> images;

    /**
     * 人均消费
     */
    private Double consumptionPerPerson;

    /**
     * 人均消费使用的币种
     */
    private String consumptionCurrency;

    /**
     * 支持消费的币种
     */
    private List<String> supportedCurrencies;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 上传者
     */
    private String publisher;

    /**
     * 哈希值
     */
    private String hash;

    /**
     * 浏览量
     */
    private Integer pageviews;

    /**
     * 收藏数
     */
    private Integer collections;

    /**
     * 顾客评分( 0 - 5 )
     */
    private Double score;

    /**
     * 兴趣点状态
     */
    private PoiStatusEnum poiStatus;

    /**
     * 定位的坐标
     */
    private CoordinateDTO coordinate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 描述
     */
    private String desc;

    /**
     * 本地语言
     */
    private String locale;
}
