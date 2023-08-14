package io.pifind.mapserver.controller;

import io.pifind.authorization.annotation.UserEntity;
import io.pifind.authorization.model.User;
import io.pifind.common.response.R;
import io.pifind.poi.api.IInterestPointIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * IInterestPointIntegralController
 *
 * @author chenxiaoli
 * @date 2023-07-26
 * @description 积分Controller
 */
@RestController
@RequestMapping("/v1/poi/integral")
public class IInterestPointIntegralController {

    @Autowired
    private IInterestPointIntegralService interestPointIntegralService;

    /**
     * 积分
     * @param user 用户信息
     * @param type 类型
     * @return 无
     */
    @GetMapping
    public R integral(@UserEntity User user, @RequestParam String type) {
        return interestPointIntegralService.integral(user.getUsername(), type);
    }

    /**
     * 积分列表
     * @param user 用户信息
     * @return 无
     */
    @GetMapping("/list")
    public R integralList(@UserEntity User user) {
        return interestPointIntegralService.integralList(user.getUsername());
    }
}
