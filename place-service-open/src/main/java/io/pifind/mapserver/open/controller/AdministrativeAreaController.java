package io.pifind.mapserver.open.controller;

import io.pifind.common.response.R;
import io.pifind.mapserver.open.service.IAdministrativeAreaFeignService;
import io.pifind.place.model.AdministrativeAreaDTO;
import io.pifind.role.annotation.RequestPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 行政区控制器
 */
@RestController
@RequestMapping("/v1/area")
public class AdministrativeAreaController {

    @Autowired
    private IAdministrativeAreaFeignService administrativeAreaFeignService;

    /**
     * 通过行政区ID获取行政区树
     * @param id 行政区ID
     * @param deep 树的高度（可选，默认为2）
     * @return 行政区树
     */
    @GetMapping("/get")
    @RequestPermission(name = "area.get",description = "获取行政区树")
    public R<AdministrativeAreaDTO> getAdministrativeAreaTreeById(
            @RequestParam("id") Long id,
            @RequestParam(value = "deep",defaultValue = "2",required = false) Integer deep
    ) {
        return administrativeAreaFeignService.getAdministrativeAreaById(id,deep);
    }

    /**
     * 检查是否存在行政区ID
     * @param id 行政区ID
     * @return 返回值的类型为 {@link Boolean}
     * <ul>
     *     <li><b>存在行政区</b> - 返回 {@code true}</li>
     *     <li><b>不存在行政区</b> - 返回 {@code false}</li>
     * </ul>
     */
    @GetMapping("/exist")
    @RequestPermission(name = "area.exist",description = "检查是否存在行政区ID")
    public R<Boolean> existAdministrativeAreaById(
            @RequestParam("id") Long id
    ) {
        return administrativeAreaFeignService.existAdministrativeAreaById(id);
    }

    /**
     * 获取详细地址
     * @param id 行政区ID
     * @param separator 行政区间的间隔夫（可选，默认为英文”,“）
     * @return 行政区ID对应的详细地址字符串
     */
    @GetMapping("/detail")
    @RequestPermission(name = "area.detail",description = "获取详细地址")
    public R<String> getDetailedAddress(
            @RequestParam("id") Long id,
            @RequestParam(value = "separator",defaultValue = ",",required = false) String separator
    ) {
        return administrativeAreaFeignService.getDetailedAddress(id,separator);
    }

}
