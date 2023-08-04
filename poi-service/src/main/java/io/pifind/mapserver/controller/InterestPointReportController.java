package io.pifind.mapserver.controller;

import io.pifind.authorization.annotation.UserEntity;
import io.pifind.authorization.model.User;
import io.pifind.common.response.R;
import io.pifind.poi.api.IInterestPointReportService;
import io.pifind.poi.model.dto.InterestPointReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * InterestPointReportController
 *
 * @author chenxiaoli
 * @date 2023-07-21
 * @description 举报Controller
 */
@RestController
@RequestMapping("/v1/poi/report")
public class InterestPointReportController {

    @Autowired
    private IInterestPointReportService iInterestPointService;

    /**
     * 举报
     * @param user 用户信息
     * @param dto 举报信息
     * @return 无
     */
    @PostMapping("")
    public R report(@UserEntity User user, @RequestBody InterestPointReportDTO dto) {
        dto.setUsername(user.getUsername());
        dto.setReportId(user.getId());
        return iInterestPointService.report(dto);
    }
}
