package io.pifind.mapserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.pifind.mapserver.model.po.CategoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<CategoryPO> {

    @Select(
        "SELECT * FROM category WHERE superior = #{id} "
    )
    List<CategoryPO> selectSubcategoryListById(Long id);

}
