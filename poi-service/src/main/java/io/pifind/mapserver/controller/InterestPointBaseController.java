package io.pifind.mapserver.controller;

import io.pifind.authorization.annotation.UserEntity;
import io.pifind.authorization.model.User;
import io.pifind.common.response.R;
import io.pifind.poi.api.InterestPointBaseService;
import io.pifind.poi.model.dto.InterestPointDTO;
import io.pifind.poi.model.vo.InterestPointVO;
import io.pifind.role.annotation.RequestPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 兴趣点基础控制器
 */
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
    @RequestPermission(name = "poi.base.add",description = "添加POI基础信息")
    public R<Void> addInterestPoint(
            @UserEntity User user,
            @RequestBody InterestPointDTO interestPoint
    ) {
        return interestPointBaseService.addInterestPoint(user.getUsername(),interestPoint);
    }

    /**
     * 根据兴趣点ID获取兴趣点
     * @param id 兴趣点ID
     * @return 兴趣点实体类
     */
    @GetMapping("/get/{id}")
    @RequestPermission(name = "poi.base.get",description = "获取POI基础信息")
    public R<InterestPointVO> getInterestPointById(
            @UserEntity User user,
            @PathVariable Long id
    ) {
        return interestPointBaseService.getInterestPointById(user.getUsername(),id);
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
    @RequestPermission(name = "poi.base.modify",description = "修改POI基础信息")
    public R<Void> modifyInterestPoint(
            @UserEntity User user,
            @RequestBody InterestPointDTO modifiedInterestPoint
    ) {
        return interestPointBaseService.modifyInterestPoint(user.getUsername(),modifiedInterestPoint);
    }

    /**
     * 根据兴趣点ID删除兴趣点
     * @param id 兴趣点ID
     * @return 空
     */
    @DeleteMapping("/remove/{id}")
    @RequestPermission(name = "poi.base.remove",description = "删除POI基础信息")
    public R<Void> removeInterestPointById(
            @UserEntity User user,
            @PathVariable Long id
    ) {
        return interestPointBaseService.removeInterestPointById(user.getUsername(),id);
    }

}
