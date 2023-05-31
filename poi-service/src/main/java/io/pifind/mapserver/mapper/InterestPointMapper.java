package io.pifind.mapserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.mapserver.mp.StringListTypeHandler;
import io.pifind.mapserver.mp.page.MybatisPage;
import io.pifind.mapserver.mp.type.BusinessDayTypeHandler;
import io.pifind.mapserver.mp.type.BusinessHoursTypeHandler;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * 兴趣点 Mapper 接口
 */
@Mapper
public interface InterestPointMapper extends BaseMapper<InterestPointPO> {

    /**
     * 查询需要审核的兴趣点
     * @param page 分页
     * @param username 用户名
     * @param administrativeAreaId 行政区划ID
     * @return 兴趣点列表
     */
    @Select(
        "SELECT * " +
        "FROM interest_point_00 " +
        "WHERE poi_status = 0 " +
        "AND administrative_area_id LIKE CONCAT(#{administrativeAreaId}, '%') " +
        "AND ( " +
        "    SELECT COUNT(*) " +
        "    FROM interest_point_review " +
        "    WHERE interest_point_id = interest_point_00.id " +
        "    AND username = #{username} " +
        "    AND unavailable = 0 " +
        ") = 0"
    )
    @Results({
            @Result(column = "business_day", property = "businessDay",typeHandler = BusinessDayTypeHandler.class),
            @Result(column = "business_hours", property = "businessHours",typeHandler = BusinessHoursTypeHandler.class),
            @Result(column = "tels", property = "tels",typeHandler = StringListTypeHandler.class),
            @Result(column = "images", property = "images",typeHandler = StringListTypeHandler.class),
            @Result(column = "supported_currencies", property = "supportedCurrencies",typeHandler = StringListTypeHandler.class),
            @Result(column = "tags", property = "tags",typeHandler = StringListTypeHandler.class),
    })
    Page<InterestPointPO> selectReviewInterestPointPage(
            Page<InterestPointPO> page,
            String username,
            String administrativeAreaId
    );

}
