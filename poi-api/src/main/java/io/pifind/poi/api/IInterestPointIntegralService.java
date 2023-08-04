package io.pifind.poi.api;

import io.pifind.common.response.R;

/**
 * IInterestPointIntegralService
 *
 * @author chenxiaoli
 * @date 2023-07-26
 * @description 积分服务接口
 */
public interface IInterestPointIntegralService {

    /**
     * 扣减积分
     *
     * @param username
     * @param type
     * @return
     */
    R integral(String username, String type);
}
