package io.pifind.mapserver.open.controller;

import io.pifind.authorization.annotation.UserEntity;
import io.pifind.authorization.model.User;
import io.pifind.common.response.R;
import io.pifind.mapserver.open.service.InterestPointSocialFeignService;
import io.pifind.role.annotation.RequestPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 兴趣点社交控制器
 */
@RestController
@RequestMapping("/v1/poi/social")
public class InterestPointSocialController {

    @Autowired
    private InterestPointSocialFeignService interestPointSocialFeignService;

    /**
     * 浏览兴趣点
     * @param id 兴趣点ID
     * @return 无
     */
    @GetMapping("/browse/{id}")
    @RequestPermission(name = "poi.social.browse",description = "浏览兴趣点")
    public R<Void> browse(
            @UserEntity User user,
            @PathVariable Long id
    ) {
        return interestPointSocialFeignService.browse(user.getUsername(), id);
    }

    /**
     * 收藏兴趣点
     * @param id 兴趣点ID
     * @return 无
     */
    @GetMapping("/collect/{id}")
    @RequestPermission(name = "poi.social.collect",description = "收藏兴趣点")
    public R<Void> collect(
            @UserEntity User user,
            @PathVariable Long id
    ) {
        return interestPointSocialFeignService.collect(user.getUsername(), id);
    }

    /**
     * 评价兴趣点
     * @param id 兴趣点ID
     * @param score 评分
     * @return 无
     */
    @GetMapping("/evaluate/{id}/{score}")
    @RequestPermission(name = "poi.social.evaluate",description = "评价兴趣点")
    public R<Void> evaluate(
            @UserEntity User user,
            @PathVariable Long id,
            @PathVariable Double score
    ) {
        return interestPointSocialFeignService.evaluate(
                user.getUsername(),
                id,
                score
        );
    }
}
