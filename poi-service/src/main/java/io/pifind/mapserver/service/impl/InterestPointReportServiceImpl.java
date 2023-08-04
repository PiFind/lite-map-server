package io.pifind.mapserver.service.impl;

import io.pifind.common.response.R;
import io.pifind.mapserver.mapper.InterestPointReportMapper;
import io.pifind.mapserver.model.constant.ReportType;
import io.pifind.mapserver.model.po.InterestPointReportPO;
import io.pifind.poi.api.IInterestPointReportService;
import io.pifind.poi.model.dto.InterestPointReportDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * InterestPointServiceImpl
 *
 * @author chenxiaoli
 * @date 2023-07-21
 * @description 举报服务
 */
@Service
public class InterestPointReportServiceImpl implements IInterestPointReportService {

    @Autowired
    private InterestPointReportMapper reportMapper;

    @Override
    public R report(InterestPointReportDTO dto) {
        if (Objects.isNull(ReportType.parse(dto.getType()))) {
            return R.failure("report reason is illegal");
        }
        InterestPointReportPO reportPO = new InterestPointReportPO();
        BeanUtils.copyProperties(dto, reportPO);
        reportMapper.insert(reportPO);
        return R.success();
    }
}
