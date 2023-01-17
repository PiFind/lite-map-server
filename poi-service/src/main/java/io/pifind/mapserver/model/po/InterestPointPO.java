package io.pifind.mapserver.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.pifind.mapserver.model.constant.InterestPointStatusEnum;
import io.pifind.mapserver.mp.PoiStringListTypeHandler;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 兴趣点
 */
@Data
@TableName("interest_point")
public class InterestPointPO {

    /**
     * 主键
     */
    private String id ;

    /**
     * 本地语言的名字
     */
    private String name;

    /**
     * 英语名字
     */
    @TableField("name_en")
    private String nameEN;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    @TableField("administrative_area_id")
    private Long administrativeAreaId;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 电话
     */
    @TableField(typeHandler = PoiStringListTypeHandler.class)
    private List<String> tels;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 图片
     */
    @TableField(typeHandler = PoiStringListTypeHandler.class)
    private List<String> images;

    /**
     * 人均消费
     */
    @TableField("consumption_per_person")
    private Double consumptionPerPerson;

    /**
     * 人均消费使用的币种
     */
    @TableField("consumption_currency")
    private String consumptionCurrency;

    /**
     * 支持消费的币种
     */
    @TableField("supported_currencies")
    private List<String> supportedCurrencies;

    /**
     * 标签
     */
    @TableField(typeHandler = PoiStringListTypeHandler.class)
    private List<String> tags;

    /**
     * 上传者
     */
    private String publisher;

    /**
     * 可信度（0 - 100，100为完全可靠）
     */
    private Integer reliability;

    /**
     * 评分（0 - 5）
     */
    private Double score;

    /**
     * 经度
     */
    private Double latitude;

    /**
     * 纬度
     */
    private Double longitude;

    /**
     * 哈希值
     */
    private String hash;

    /**
     * 状态
     */
    private InterestPointStatusEnum status;

    /**
     * Plus Code
     */
    @TableField("plus_code")
    private String plusCode;

    /**
     * 扩展字段01
     */
    @TableField("extended_01")
    private String extended01;

    /**
     * 扩展字段02
     */
    @TableField("extended_02")
    private String extended02;

    /**
     * 扩展字段03
     */
    @TableField("extended_03")
    private String extended03;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    private Boolean unavailable;

}