package io.pifind.mapserver.controller;

import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointBaseService;
import io.pifind.poi.model.InterestPointDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/poi/base")
public class InterestPointBaseController {

    @Autowired
    private InterestPointBaseService interestPointBaseService;

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
    public R<Void> addInterestPoint(@RequestParam InterestPointDTO interestPoint) {
        return interestPointBaseService.addInterestPoint(interestPoint);
    }

    /**
     * 根据兴趣点ID获取兴趣点
     * @param id 兴趣点ID
     * @return 兴趣点实体类
     */
    @GetMapping("/{id}")
    public R<InterestPointDTO> getInterestPointById(@PathVariable Long id) {
        return interestPointBaseService.getInterestPointById(id);
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
    public R<Void> modifyInterestPoint(@RequestParam InterestPointDTO modifiedInterestPoint) {
        return interestPointBaseService.modifyInterestPoint(modifiedInterestPoint);
    }

    /**
     * 根据兴趣点ID删除兴趣点
     * @param id 兴趣点ID
     * @return 空
     */
    @DeleteMapping("/{id}")
    public R<Void> removeInterestPointById(@PathVariable Long id) {
        return interestPointBaseService.removeInterestPointById(id);
    }

}
