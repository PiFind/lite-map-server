package io.pifind.mapserver.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.pifind.mapserver.mp.AreaCodeTypeHandler;
import lombok.Data;

import java.util.List;

/**
 * 国家
 */
@Data
@TableName(value = "country",autoResultMap = true)
public class CountryPO {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 中文名
     */
    @TableField("name_cn")
    private String nameCN;

    /**
     * 英文名
     */
    @TableField("name_en")
    private String nameEN;

    /**
     * ISO标准2位代码
     */
    @TableField("iso_2")
    private String ISO2;

    /**
     * ISO标准3位代码
     */
    @TableField("iso_3")
    private String ISO3;

    /**
     * 3位数字代码
     */
    private Integer number;

    /**
     * 电话区号
     */
    @TableField(value="area_code",typeHandler = AreaCodeTypeHandler.class)
    private List<Integer> areaCodes;

    /**
     * 域名后缀
     */
    private String domain;

    /**
     * 备注
     */
    private String remark;

}
