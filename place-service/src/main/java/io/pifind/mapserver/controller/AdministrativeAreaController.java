package io.pifind.mapserver.controller;

import io.pifind.common.response.R;
import io.pifind.place.api.IAdministrativeAreaService;
import io.pifind.place.model.AdministrativeAreaDTO;
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
    private IAdministrativeAreaService administrativeAreaService;

    /**
     * 通过行政区ID获取行政区树
     * @param id 行政区ID
     * @param deep 树的高度（可选，默认为2）
     * @return 行政区树
     */
    @GetMapping("/get")
    public R<AdministrativeAreaDTO> getAdministrativeAreaTreeById(
            @RequestParam("id") Long id,
            @RequestParam(value = "deep",defaultValue = "2",required = false) Integer deep
    ) {
        return administrativeAreaService.getAdministrativeAreaById(id,deep);
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
    public R<Boolean> existAdministrativeAreaById(
            @RequestParam("id") Long id
    ) {
        return administrativeAreaService.existAdministrativeAreaById(id);
    }

    /**
     * 获取详细地址
     * @param id 行政区ID
     * @param separator 行政区间的间隔符（可选，默认为英文”,“）
     * @return 行政区ID对应的详细地址字符串
     */
    @GetMapping("/detail")
    public R<String> getDetailedAddress(
            @RequestParam("id") Long id,
            @RequestParam(value = "separator",defaultValue = ",",required = false) String separator
    ) {
        return administrativeAreaService.getDetailedAddress(id,separator);
    }

}
