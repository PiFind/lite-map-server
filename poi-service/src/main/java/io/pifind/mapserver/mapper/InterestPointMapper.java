package io.pifind.mapserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.pifind.mapserver.model.po.InterestPointPO;
import io.pifind.mapserver.mp.page.MybatisPage;
import org.apache.ibatis.annotations.Mapper;
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
            "SELECT * FROM interest_point_00 AS poi WHERE " +
            "poi_status = 0 AND " +
            "administrative_area_id LIKE CONCAT(#{administrativeAreaId},‘%’) AND " +
            "( " +
            "   SELECT COUNT(*) FROM interest_point_review WHERE " +
            "   interest_point_id = poi.id AND " +
            "   username = #{username} AND " +
            "   unavailable = 0 " +
            ") = 0 "
    )
    Page<InterestPointPO> selectReviewInterestPointPage(
            Page<InterestPointPO> page,
            String username,
            Long administrativeAreaId
    );

}
