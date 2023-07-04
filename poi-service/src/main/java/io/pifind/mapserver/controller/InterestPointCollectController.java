package io.pifind.mapserver.controller;

import io.pifind.authorization.annotation.UserName;
import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointCollectService;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * InterestPointCollectController
 *
 * @author chenxiaoli
 * @date 2023-06-27
 * @description 收藏Controller
 */
@RestController
@RequestMapping("/v1/poi/collect")
public class InterestPointCollectController {

    @Autowired
    private InterestPointCollectService interestPointCollectService;

    /**
     * 收藏
     *
     * @param username 用户名
     * @return
     */
    @GetMapping("/{interestPointId}")
    public R<Void> collect(@UserName String username, @PathVariable("interestPointId") Long interestPointId) {
        return interestPointCollectService.collect(username, interestPointId);
    }

    /**
     * 是否收藏
     *
     * @param username 用户名
     * @return
     */
    @GetMapping("/has/{interestPointId}")
    public R<Boolean> hasCollect(@UserName String username, @PathVariable("interestPointId") Long interestPointId) {
        return interestPointCollectService.hasCollect(username, interestPointId);
    }

    /**
     * 取消收藏
     *
     * @param username 用户名
     * @return
     */
    @GetMapping("/cancel/{interestPointId}")
    public R<Void> cancel(@UserName String username, @PathVariable("interestPointId") Long interestPointId) {
        return interestPointCollectService.cancel(username, interestPointId);
    }

    /**
     * 获取收藏列表
     *
     * @param username
     * @return
     */
    @GetMapping("/page/{currentPage}/{pageSize}")
    R<Page<InterestPointVO>> getCollectList(@UserName String username, @PathVariable("currentPage") Integer currentPage,
                                            @PathVariable("pageSize") Integer pageSize) {
        return interestPointCollectService.getCollectList(username, currentPage, pageSize);
    }
}
