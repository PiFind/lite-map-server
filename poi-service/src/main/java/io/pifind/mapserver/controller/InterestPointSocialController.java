package io.pifind.mapserver.controller;

import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/poi/social")
public class InterestPointSocialController {

    @Autowired
    private InterestPointSocialService interestPointSocialService;

    @GetMapping("/browse/{id}")
    public R<Void> browse(@PathVariable Long id) {
        return interestPointSocialService.browse(id);
    }

    @GetMapping("/collect/{id}")
    public R<Void> collect(@PathVariable Long id) {
        return interestPointSocialService.collect(id);
    }

    @GetMapping("/evaluate/{id}")
    public R<Void> evaluate(
            @PathVariable Long id,
            @RequestParam("score") Double score
    ) {
        return interestPointSocialService.evaluate(id,score);
    }
}
