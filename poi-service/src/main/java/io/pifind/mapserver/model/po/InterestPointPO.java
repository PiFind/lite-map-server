package io.pifind.mapserver.model.po;

import com.baomidou.mybatisplus.annotation.*;
import io.pifind.mapserver.model.constant.BusinessStatusEnum;
import io.pifind.mapserver.model.constant.CoordinateSystemEnum;
import io.pifind.mapserver.model.constant.InterestPointStatusEnum;
import io.pifind.mapserver.model.constant.WeekEnum;
import io.pifind.mapserver.model.po.component.TimeIntervalSet;
import io.pifind.mapserver.mp.StringListTypeHandler;
import io.pifind.mapserver.mp.type.BusinessDayTypeHandler;
import io.pifind.mapserver.mp.type.BusinessHoursTypeHandler;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 兴趣点
 */
@Data
@TableName(value = "interest_point",autoResultMap = true)
public class InterestPointPO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id ;

    /**
     * [必填]本地语言的名字
     */
    private String name;

    /**
     * [可选]英文名
     * <p>
     *     默认英文名为空字符串，如果是空英文名，那么搜索英文时将会忽略该兴趣点
     * </p>
     */
    @TableField("name_en")
    private String nameEN;

    /**
     * [必填]本地语言的详细地址
     */
    private String address;

    /**
     * [可选]本地语言的详细地址
     * <p>
     *     默认英文名为空字符串，如果是空英文名，那么搜索英文时将会忽略该兴趣点
     * </p>
     */
    @TableField("address_en")
    private String addressEN;

    /**
     * [必填]分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * [必填]行政区ID
     */
    @TableField("administrative_area_id")
    private String administrativeAreaId;

    /**
     * [必填]营业日（单周）
     */
    @TableField(value = "business_day",typeHandler = BusinessDayTypeHandler.class)
    private Map<WeekEnum,Boolean> businessDay;

    /**
     * [必填]营业时间段（特殊编码）
     */
    @TableField(value = "business_hours",typeHandler = BusinessHoursTypeHandler.class)
    private TimeIntervalSet businessHours;

    /**
     * [可选]休店开始时间
     */
    @TableField("vacation_start_time")
    private Date vacationStartTime;

    /**
     * [可选]休店结束时间
     */
    @TableField("vacation_end_time")
    private Date vacationEndTime;

    /**
     * [可选]营业执照
     */
    @TableField("business_license")
    private String businessLicense;

    /**
     * [必填]营业状态
     */
    @TableField("business_status")
    private BusinessStatusEnum businessStatus;

    /**
     * [必填]电话
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> tels;

    /**
     * [可选] LOGO 图片
     */
    private String logo;

    /**
     * [可选] 背景图片
     */
    private String background;

    /**
     * [可选]图片
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> images;

    /**
     * [必填]人均消费
     */
    @TableField("consumption_per_person")
    private Double consumptionPerPerson;

    /**
     * [必填]人均消费使用的币种
     */
    @TableField("consumption_currency")
    private String consumptionCurrency;

    /**
     * [必填]支持消费的币种
     */
    @TableField(value = "supported_currencies",typeHandler = StringListTypeHandler.class)
    private List<String> supportedCurrencies;

    /**
     * [可选]标签
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> tags;

    /**
     * [必填]上传者
     */
    private String publisher;

    /**
     * [可选]可信度（0 - 100，100为完全可靠）
     * <p>
     *     默认为50
     * </p>
     */
    private Integer reliability;

    /**
     * [必填]哈希值
     */
    private String hash;

    /**
     * [可选]浏览量
     * <p>
     *     默认为0
     * </p>
     */
    private Integer pageviews;

    /**
     * [可选]收藏数
     * <p>
     *     默认为0
     * </p>
     */
    private Integer collections;

    /**
     * 评价总分
     */
    @TableField("total_score")
    private Integer totalScore;

    /**
     * 参与评价的人数
     */
    @TableField("total_participants")
    private Integer totalParticipants;

    /**
     * [可选] 隐藏分
     * <p>
     *     默认为0
     * </p>
     */
    @TableField("hidden_score")
    private Integer hiddenScore;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 定位坐标系
     */
    @TableField("coordinate_system")
    private CoordinateSystemEnum coordinateSystem;

    /**
     * 是否上首页
     */
    @TableField("home_page")
    private Boolean homePage;

    /**
     * [必填]兴趣点状态
     */
    @TableField("poi_status")
    private InterestPointStatusEnum poiStatus;

    /**
     * [扩展]扩展字段01
     */
    @TableField("extended_01")
    private String extended01;

    /**
     * [扩展]扩展字段02
     */
    @TableField("extended_02")
    private String extended02;

    /**
     * [扩展]扩展字段03
     */
    @TableField("extended_03")
    private String extended03;

    /**
     * [自动]创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * [自动]更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * [自动]逻辑删除字段
     */
    @TableLogic
    private Boolean unavailable;

    /**
     * 描述
     */
    private String desc;

    /**
     * 本地语言
     */
    private String locale;
}