package io.pifind.poi.api;

import io.pifind.common.response.R;
import io.pifind.poi.model.dto.InterestPointReportDTO;

/**
 * IInterestPointReportService
 *
 * @author chenxiaoli
 * @date 2023-07-21
 * @description 举报服务接口
 */
public interface IInterestPointReportService {

    /**
     * 举报
     *
     * @param dto
     * @return
     */
    R report(InterestPointReportDTO dto);
}
