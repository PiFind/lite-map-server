package io.pifind.mapserver.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("administrative_area")
public class AdministrativeAreaPO {

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
     * 行政区划等级
     * <ol start="0">
     *     <li>国家(country)</li>
     *     <li>省/州/直辖市(state)</li>
     *     <li>城市(city)</li>
     *     <li>...</li>
     * </ol>
     */
    private Integer level;

    /**
     * 当前行政区的上级行政区
     */
    private Long superior;

    /**
     * 首字母(大写)
     */
    private String initial;

}
