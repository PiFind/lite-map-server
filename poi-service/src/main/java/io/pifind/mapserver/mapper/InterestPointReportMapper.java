package io.pifind.mapserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.pifind.mapserver.model.po.InterestPointReportPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * InterestPointReportMapper
 *
 * @author chenxiaoli
 * @date 2023-07-21
 * @description 举报mapper
 */
@Mapper
public interface InterestPointReportMapper extends BaseMapper<InterestPointReportPO> {
}
