package io.pifind.poi.api;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.model.vo.InterestPointVO;

/**
 * InterestPointCollectService
 *
 * @author chenxiaoli
 * @date 2023-06-27
 * @description 兴趣点收藏服务
 */
public interface InterestPointCollectService {

    /**
     * 收藏
     *
     * @param username
     * @param interestPointId
     * @return
     */
    R<Void> collect(String username, Long interestPointId);

    /**
     * 取消收藏
     *
     * @param username
     * @param interestPointId
     * @return
     */
    R<Void> cancel(String username, Long interestPointId);

    /**
     * 获取收藏列表
     *
     * @param username
     * @return
     */
    R<Page<InterestPointVO>> getCollectList(String username, Integer currentPage, Integer pageSize);
}
