package io.pifind.mapserver.open.service;

import io.pifind.common.response.Page;
import io.pifind.common.response.R;
import io.pifind.mapserver.open.support.PoiServiceAPI;
import io.pifind.poi.model.dto.InterestPointDTO;
import io.pifind.poi.model.vo.InterestPointVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 兴趣点基础服务
 * <p>
 *     该服务包含了除搜索服务外的增删改查（简单查询）等方法
 * </p>
 */
@FeignClient(name = "poi-publisher-service",url = PoiServiceAPI.URL)
public interface InterestPointPublisherFeignService {

    /**
     * 添加一个兴趣点
     * @param username 用户名
     * @param interestPoint {@link InterestPointDTO 兴趣点实体对象}
     * @return 无返回值
     * <ul>
     *     <li><b>添加兴趣点成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>添加兴趣点失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @PostMapping("/base/add")
    R<Void> addInterestPoint(
            @RequestHeader("username") String username,
            @RequestBody InterestPointDTO interestPoint
    );

    /**
     * 根据兴趣点ID获取兴趣点
     * 这个是给发布者自己查询使用的，所以兴趣点无论是否审核通过都会进行展示
     * @param username 用户名
     * @param id 兴趣点ID
     * @return 返回值类型为 {@link InterestPointDTO}
     * <ul>
     *     <li><b>查询到兴趣点</b> - 返回 {@link InterestPointDTO 兴趣点实体对象}</li>
     *     <li><b>没有查询到兴趣点</b> - 返回 {@code null}</li>
     * </ul>
     */
    @GetMapping("/base/get/{id}")
    R<InterestPointVO> getInterestPointById(
            @RequestHeader("username") String username,
            @PathVariable("id") Long id
    );

    /**
     * 根据发布者获取兴趣点分页
     * @param username 用户名
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return 返回值类型为 {@link Page<InterestPointDTO>}
     */
    @GetMapping("/base/get/page/{currentPage}/{pageSize}")
    R<Page<InterestPointVO>> getInterestPointPageByPublisher(
            @RequestHeader("username") String username,
            @PathVariable("currentPage") Integer currentPage,
            @PathVariable("pageSize") Integer pageSize
    );

    /**
     * 修改兴趣点信息
     * @param username 用户名
     * @param modifiedInterestPoint 修改过兴趣点信息后的{@link InterestPointDTO 兴趣点实体对象}
     * @return 无返回值
     * <ul>
     *     <li><b>修改兴趣点成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>修改兴趣点失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @PostMapping("/base/modify")
    R<Void> modifyInterestPoint(
            @RequestHeader("username") String username,
            @RequestBody InterestPointDTO modifiedInterestPoint
    );

    /**
     * 根据兴趣点ID删除兴趣点
     * @param username 用户名
     * @param id 兴趣点ID
     * @return 无返回值
     * <ul>
     *     <li><b>删除兴趣点成功</b> - 返回成功响应 {@code code == 0}</li>
     *     <li><b>删除兴趣点失败</b> - 返回失败响应 {@code code != 0}，且会在 {@link R#getMessage()} 中说明原因</li>
     * </ul>
     */
    @DeleteMapping("/base/remove/{id}")
    R<Void> removeInterestPointById(
            @RequestHeader("username") String username,
            @PathVariable("id") Long id
    );

}
