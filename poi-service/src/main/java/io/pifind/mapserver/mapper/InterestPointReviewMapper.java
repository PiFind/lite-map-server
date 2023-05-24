package io.pifind.mapserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.pifind.mapserver.model.po.InterestPointReviewPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InterestPointReviewMapper extends BaseMapper<InterestPointReviewPO> {

    @Select(
            "SELECT * FROM interest_point_review WHERE interest_point_id = #{interestPointId} "
    )
    List<InterestPointReviewPO> getReviewList(Long interestPointId);
}
