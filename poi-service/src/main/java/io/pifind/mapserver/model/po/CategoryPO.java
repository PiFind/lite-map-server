package io.pifind.mapserver.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.pifind.mapserver.mp.LocalizedNameTypeHandler;
import lombok.Data;

import java.util.Locale;
import java.util.Map;

@Data
public class CategoryPO {

    @TableId
    private Long id;

    @TableField(typeHandler = LocalizedNameTypeHandler.class)
    private Map<Locale,String> names;

    @TableField("name_en")
    private String nameEN;

    private Long superior;

    private int level;


}
