package io.pifind.mapserver.controller;

import io.pifind.authorization.annotation.UserName;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 兴趣点社交控制器
 */
@RestController
@RequestMapping("/v1/poi/social")
public class InterestPointSocialController {

    @Autowired
    private InterestPointSocialService interestPointSocialService;

    /**
     * 浏览兴趣点
     * @param id 兴趣点ID
     * @return 无
     */
    @GetMapping("/browse/{id}")
    public R<Void> browse(
            @UserName String username,
            @PathVariable Long id
    ) {
        return interestPointSocialService.browse(username, id);
    }

    /**
     * 收藏兴趣点
     * @param id 兴趣点ID
     * @return 无
     */
    @GetMapping("/collect/{id}")
    public R<Void> collect(
            @UserName String username,
            @PathVariable Long id
    ) {
        return interestPointSocialService.collect(username, id);
    }

    /**
     * 评价兴趣点
     * @param id 兴趣点ID
     * @param score 评分
     * @return 无
     */
    @GetMapping("/evaluate/{id}/{score}")
    public R<Void> evaluate(
            @UserName String username,
            @PathVariable Long id,
            @PathVariable Double score
    ) {
        return interestPointSocialService.evaluate(
                username,
                id,
                score
        );
    }
}
