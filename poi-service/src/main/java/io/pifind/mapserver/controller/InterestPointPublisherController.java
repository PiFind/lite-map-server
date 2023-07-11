package io.pifind.mapserver.controller;

import io.pifind.authorization.annotation.UserName;
import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointPublisherService;
import io.pifind.poi.model.dto.InterestPointDTO;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 兴趣点基础控制器
 */
@RestController
@RequestMapping("/v1/poi/base")
public class InterestPointPublisherController {

    @Autowired
    private InterestPointPublisherService interestPointPublisherService;

    /**
     * 添加一个兴趣点
     * @param interestPoint {@link InterestPointDTO 兴趣点实体对象}
     * @return 无返回值
     * <ul>
     *     <li><b>添加兴趣点成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>添加兴趣点失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @PostMapping("/add")
    public R<Void> addInterestPoint(@UserName String username,
                                    @RequestHeader("lang") String lang,
                                    @RequestBody InterestPointDTO interestPoint) {
        interestPoint.setLocale(lang);
        return interestPointPublisherService.addInterestPoint(username,interestPoint);
    }

    /**
     * 根据兴趣点ID获取兴趣点
     * @param id 兴趣点ID
     * @return 兴趣点实体类
     */
    @GetMapping("/get/{id}")
    public R<InterestPointVO> getInterestPointById(@UserName String username, @PathVariable("id") Long id) {
        return interestPointPublisherService.getInterestPointById(username,id);
    }

    /**
     * 根据发布者获取兴趣点分页
     * @param username 用户名
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 返回值类型为 {@link Page<InterestPointDTO>}
     */
    @GetMapping("/get/page/{currentPage}/{pageSize}")
    public R<Page<InterestPointVO>> getInterestPointPageByPublisher(@UserName String username,
                                                                    @PathVariable("currentPage") Integer currentPage,
                                                                    @PathVariable("pageSize") Integer pageSize) {
        return interestPointPublisherService.getInterestPointPageByPublisher(username,currentPage,pageSize);
    }

    /**
     *
     * @param modifiedInterestPoint 修改过兴趣点信息后的{@link InterestPointDTO 兴趣点实体对象}
     * @return 无返回值
     * <ul>
     *     <li><b>修改兴趣点成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>修改兴趣点失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @PostMapping("/modify")
    public R<Void> modifyInterestPoint(@UserName String username,
                                       @RequestHeader("lang") String lang,
                                       @RequestBody InterestPointDTO modifiedInterestPoint) {
        modifiedInterestPoint.setLocale(lang);
        return interestPointPublisherService.modifyInterestPoint(username,modifiedInterestPoint);
    }

    /**
     * 根据兴趣点ID删除兴趣点
     * @param id 兴趣点ID
     * @return 空
     */
    @DeleteMapping("/remove/{id}")
    public R<Void> removeInterestPointById(@UserName String username, @PathVariable Long id) {
        return interestPointPublisherService.removeInterestPointById(username,id);
    }


    /**
     * 下架兴趣点
     * @param id 兴趣点ID
     * @return
     */
    @GetMapping("/off/{id}")
    public R offShelf(@PathVariable("id") Long id) {
        return interestPointPublisherService.offShelf(id);
    }
}
